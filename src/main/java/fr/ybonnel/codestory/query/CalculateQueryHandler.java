package fr.ybonnel.codestory.query;


import com.google.common.base.Throwables;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateQueryHandler extends AbstractQueryHandler {

    private Pattern patternPlus = Pattern.compile("(\\d+)\\+(\\d+)");
    private Pattern patternParenthesis = Pattern.compile("\\((.*)\\)");
    private Pattern patternMultiple = Pattern.compile("(\\d+,?\\d*)\\*(\\d+,?\\d*)");
    private Pattern patternDivide = Pattern.compile("(\\d+)/(\\d+)");
    private Pattern patternJustANumber = Pattern.compile("(\\d+,?\\d*)");

    private NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);

    @Override
    public String getResponse(String query) {
        String calculateQuery = query.replace(' ', '+');

        try {
            calculateQuery = calculateWithParenthesis(calculateQuery);
        } catch (ParseException e) {
            Throwables.propagate(e);
        }

        Matcher matcherJustANumber = patternJustANumber.matcher(calculateQuery);

        if (matcherJustANumber.matches()) {
            return calculateQuery;
        }

        return null;
    }

    private String calculateWithParenthesis(String calculateQuery) throws ParseException {
        Matcher matcherParenthsis = patternParenthesis.matcher(calculateQuery);

        while (matcherParenthsis.find()) {
            SearchParanthesis searchParanthesis = new SearchParanthesis(calculateQuery).invoke();
            int start = searchParanthesis.getStart();
            int end = searchParanthesis.getEnd();

            String queryBetweenParenthesis = calculateQuery.substring(start+1, end-1);
            System.out.println(queryBetweenParenthesis);
            String result = calculateWithoutParenthesis(queryBetweenParenthesis);
            calculateQuery = calculateQuery.substring(0, start) + result + calculateQuery.substring(end);
            matcherParenthsis = patternParenthesis.matcher(calculateQuery);
        }

        calculateQuery = calculateWithoutParenthesis(calculateQuery);
        return calculateQuery;
    }

    private String calculateWithoutParenthesis(String calculateQuery) throws ParseException {
        System.out.println(calculateQuery);
        Matcher matcherMultiple = patternMultiple.matcher(calculateQuery);

        while (matcherMultiple.find()) {
            double a = format.parse(matcherMultiple.group(1)).doubleValue();
            double b = format.parse(matcherMultiple.group(2)).doubleValue();
            double result = a*b;
            calculateQuery = calculateQuery.substring(0, matcherMultiple.start()) + format.format(result) + calculateQuery.substring(matcherMultiple.end());
            matcherMultiple = patternMultiple.matcher(calculateQuery);
        }

        Matcher matcherDivide = patternDivide.matcher(calculateQuery);

        while (matcherDivide.find()) {
            double a = format.parse(matcherDivide.group(1)).doubleValue();
            double b = format.parse(matcherDivide.group(2)).doubleValue();
            double result = a / b;
            calculateQuery = calculateQuery.substring(0, matcherDivide.start()) + format.format(result) + calculateQuery.substring(matcherDivide.end());
            matcherDivide = patternPlus.matcher(calculateQuery);
        }

        Matcher matcherPlus = patternPlus.matcher(calculateQuery);

        while (matcherPlus.find()) {
            double a = format.parse(matcherPlus.group(1)).doubleValue();
            double b = format.parse(matcherPlus.group(2)).doubleValue();
            double result = a + b;
            calculateQuery = calculateQuery.substring(0, matcherPlus.start()) + format.format(result) + calculateQuery.substring(matcherPlus.end());
            matcherPlus = patternPlus.matcher(calculateQuery);
        }
        return calculateQuery;
    }


    private class SearchParanthesis {
        private String calculateQuery;
        private int start;
        private int end;

        public SearchParanthesis(String calculateQuery) {
            this.calculateQuery = calculateQuery;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public SearchParanthesis invoke() {
            start = -1;
            end = -1;
            int currentProf=0;
            int currentStart=-1;
            int maxProf=-1;
            for (int index=0;index < calculateQuery.length(); index++) {
                char car = calculateQuery.charAt(index);
                if (car == '(') {
                    currentStart = index;
                    currentProf++;
                }

                if (car == ')') {
                    if (currentProf > maxProf) {
                        start = currentStart;
                        end = index+1;
                        maxProf = currentProf;
                    }
                    currentProf--;
                }
            }
            return this;
        }
    }
}
