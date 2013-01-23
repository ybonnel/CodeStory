package fr.ybonnel.codestory.query;


import com.google.common.base.Throwables;
import fr.ybonnel.codestory.query.calculate.Operator;
import fr.ybonnel.codestory.query.calculate.SearchParanthesis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateQueryHandler extends AbstractQueryHandler {

    private static final String NOMBRE = "\\-?\\d+\\.?\\d*";
    private static final String PATTERN_DIVIDE = "(" + NOMBRE + ")/(" + NOMBRE + ")";
    private static final String PATTERN_MULTIPLY = "(" + NOMBRE + ")\\*(" + NOMBRE + ")";
    private static final String PATTERN_ADD = "(" + NOMBRE + ")\\+(" + NOMBRE + ")";

    // operators list by priority.
    private List<Operator> operators = new ArrayList<Operator>() {{
        // operator divide.
        add(new Operator(PATTERN_DIVIDE) {
            @Override
            public BigDecimal operate(BigDecimal a, BigDecimal b) {
                try {
                    return a.divide(b);
                } catch (ArithmeticException exception) {
                    return a.divide(b, 1000, RoundingMode.HALF_UP);
                }
            }
        });
        // Operator Multiply.
        add(new Operator(PATTERN_MULTIPLY) {
            @Override
            public BigDecimal operate(BigDecimal a, BigDecimal b) {
                return a.multiply(b);
            }
        });
        // Operator Add.
        add(new Operator(PATTERN_ADD) {
            @Override
            public BigDecimal operate(BigDecimal a, BigDecimal b) {
                return a.add(b);
            }
        });
    }};

    private Pattern patternParenthesis = Pattern.compile("\\((.*)\\)");
    private NumberFormat format = new DecimalFormat("#0.#", new DecimalFormatSymbols(Locale.FRANCE));

    public CalculateQueryHandler() {
        format.setMaximumFractionDigits(500);
    }

    @Override
    public String getResponse(String query) {
        String result = null;
        try {
            result = calculateWithParenthesis(query.replace(' ', '+').replace(',', '.'));
        } catch (ParseException e) {
            Throwables.propagate(e);
        }

        try {
            BigDecimal retour = new BigDecimal(result);
            return format.format(retour);
        } catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            return null;
        }
    }

    private String calculateWithParenthesis(String calculateQuery) throws ParseException {
        Matcher matcherParenthsis = patternParenthesis.matcher(calculateQuery);

        while (matcherParenthsis.find()) {
            SearchParanthesis searchParanthesis = new SearchParanthesis(calculateQuery).invoke();
            int start = searchParanthesis.getStart();
            int end = searchParanthesis.getEnd();

            // Calculate the content of parenthesis.
            String queryBetweenParenthesis = calculateQuery.substring(start + 1, end - 1);
            String result = calculateWithoutParenthesis(queryBetweenParenthesis);

            // Replace the parenthesis group with result.
            calculateQuery = calculateQuery.substring(0, start) + result + calculateQuery.substring(end);
            matcherParenthsis = patternParenthesis.matcher(calculateQuery);
        }

        calculateQuery = calculateWithoutParenthesis(calculateQuery);
        return calculateQuery;
    }

    private String calculateWithoutParenthesis(String calculateQuery) throws ParseException {

        for (Operator operator : operators) {
            Matcher matcher = operator.matcher(calculateQuery);

            while (matcher.find()) {
                BigDecimal a = new BigDecimal(matcher.group(1));
                BigDecimal b = new BigDecimal(matcher.group(2));
                BigDecimal result = operator.operate(a, b);

                // Replace sur operation in string by result.
                calculateQuery = calculateQuery.substring(0, matcher.start()) + result.toString() + calculateQuery.substring(matcher.end());

                matcher = operator.matcher(calculateQuery);
            }
        }

        return calculateQuery;
    }


}
