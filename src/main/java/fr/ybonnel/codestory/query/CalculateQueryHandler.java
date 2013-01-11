package fr.ybonnel.codestory.query;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateQueryHandler extends AbstractQueryHandler {

    private Pattern patternPlus = Pattern.compile("(\\d+)\\+(\\d+)");
    private Pattern patternPlusWithParenthesis = Pattern.compile("\\((\\d+)\\+(\\d+)\\)");
    private Pattern patternMultiple = Pattern.compile("(\\d+)\\*(\\d+)");
    private Pattern patternJustANumber = Pattern.compile("(\\d+)");

    @Override
    public String getResponse(String query) {
        String calculateQuery = query.replace(' ', '+');

        Matcher matcherPlusWithParenthesis = patternPlusWithParenthesis.matcher(calculateQuery);

        while (matcherPlusWithParenthesis.find()) {
            int a = Integer.parseInt(matcherPlusWithParenthesis.group(1));
            int b = Integer.parseInt(matcherPlusWithParenthesis.group(2));
            int result = a + b;
            calculateQuery = calculateQuery.substring(0, matcherPlusWithParenthesis.start()) + result + calculateQuery.substring(matcherPlusWithParenthesis.end());
            matcherPlusWithParenthesis = patternPlus.matcher(calculateQuery);
        }

        Matcher matcherMultiple = patternMultiple.matcher(calculateQuery);

        while (matcherMultiple.find()) {
            int a = Integer.parseInt(matcherMultiple.group(1));
            int b = Integer.parseInt(matcherMultiple.group(2));
            int result = a*b;
            calculateQuery = calculateQuery.substring(0, matcherMultiple.start()) + result + calculateQuery.substring(matcherMultiple.end());
            matcherMultiple = patternMultiple.matcher(calculateQuery);
        }

        Matcher matcherPlus = patternPlus.matcher(calculateQuery);

        while (matcherPlus.find()) {
            int a = Integer.parseInt(matcherPlus.group(1));
            int b = Integer.parseInt(matcherPlus.group(2));
            int result = a + b;
            calculateQuery = calculateQuery.substring(0, matcherPlus.start()) + result + calculateQuery.substring(matcherPlus.end());
            matcherPlus = patternPlus.matcher(calculateQuery);
        }

        Matcher matcherJustANumber = patternJustANumber.matcher(calculateQuery);

        if (matcherJustANumber.matches()) {
            return calculateQuery;
        }

        return null;
    }


}
