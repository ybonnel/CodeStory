package fr.ybonnel.codestory.query;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateQueryHandler extends AbstractQueryHandler {

    private Pattern patternPlus = Pattern.compile("(\\d+)\\+(\\d+)");
    private Pattern patternParenthesis = Pattern.compile("\\((.*)\\)");
    private Pattern patternMultiple = Pattern.compile("(\\d+)\\*(\\d+)");
    private Pattern patternDivide = Pattern.compile("(\\d+)/(\\d+)");
    private Pattern patternJustANumber = Pattern.compile("(\\d+)");

    @Override
    public String getResponse(String query) {
        String calculateQuery = query.replace(' ', '+');

        Matcher matcherParenthsis = patternParenthesis.matcher(calculateQuery);

        while (matcherParenthsis.find()) {
            String queryBetweenParenthesis = matcherParenthsis.group(1);
            int result = Integer.parseInt(calculateWithoutParenthesis(queryBetweenParenthesis));
            calculateQuery = calculateQuery.substring(0, matcherParenthsis.start()) + result + calculateQuery.substring(matcherParenthsis.end());
            matcherParenthsis = patternParenthesis.matcher(calculateQuery);
        }

        calculateQuery = calculateWithoutParenthesis(calculateQuery);

        Matcher matcherJustANumber = patternJustANumber.matcher(calculateQuery);

        if (matcherJustANumber.matches()) {
            return calculateQuery;
        }

        return null;
    }

    private String calculateWithoutParenthesis(String calculateQuery) {
        Matcher matcherMultiple = patternMultiple.matcher(calculateQuery);

        while (matcherMultiple.find()) {
            int a = Integer.parseInt(matcherMultiple.group(1));
            int b = Integer.parseInt(matcherMultiple.group(2));
            int result = a*b;
            calculateQuery = calculateQuery.substring(0, matcherMultiple.start()) + result + calculateQuery.substring(matcherMultiple.end());
            matcherMultiple = patternMultiple.matcher(calculateQuery);
        }

        Matcher matcherDivide = patternDivide.matcher(calculateQuery);

        while (matcherDivide.find()) {
            int a = Integer.parseInt(matcherDivide.group(1));
            int b = Integer.parseInt(matcherDivide.group(2));
            int result = a / b;
            calculateQuery = calculateQuery.substring(0, matcherDivide.start()) + result + calculateQuery.substring(matcherDivide.end());
            matcherDivide = patternPlus.matcher(calculateQuery);
        }

        Matcher matcherPlus = patternPlus.matcher(calculateQuery);

        while (matcherPlus.find()) {
            int a = Integer.parseInt(matcherPlus.group(1));
            int b = Integer.parseInt(matcherPlus.group(2));
            int result = a + b;
            calculateQuery = calculateQuery.substring(0, matcherPlus.start()) + result + calculateQuery.substring(matcherPlus.end());
            matcherPlus = patternPlus.matcher(calculateQuery);
        }
        return calculateQuery;
    }


}
