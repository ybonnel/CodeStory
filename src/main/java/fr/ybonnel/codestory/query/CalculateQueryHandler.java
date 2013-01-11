package fr.ybonnel.codestory.query;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateQueryHandler extends AbstractQueryHandler {

    private Pattern patternPlus = Pattern.compile("(\\d+)\\+(\\d+)");
    private Pattern patternMultiple = Pattern.compile("(\\d+)\\*(\\d+)");

    @Override
    public String getResponse(String query) {
        String calculateQuery = query.replace(' ', '+');

        Matcher matcherPlus = patternPlus.matcher(calculateQuery);
        if (matcherPlus.matches()) {
            int a = Integer.parseInt(matcherPlus.group(1));
            int b = Integer.parseInt(matcherPlus.group(2));
            return Integer.toString(a + b);
        }
        Matcher matcherMultiple = patternMultiple.matcher(calculateQuery);
        if (matcherMultiple.matches()) {
            int a = Integer.parseInt(matcherMultiple.group(1));
            int b = Integer.parseInt(matcherMultiple.group(2));
            return Integer.toString(a * b);
        }
        return null;
    }
}
