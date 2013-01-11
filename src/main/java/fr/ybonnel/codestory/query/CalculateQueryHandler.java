package fr.ybonnel.codestory.query;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateQueryHandler extends AbstractQueryHandler {
    @Override
    public String getResponse(String query) {
        String calculateQuery = query.replace(' ', '+');

        Pattern pattern = Pattern.compile("(\\d+)\\+(\\d+)");

        Matcher matcher = pattern.matcher(calculateQuery);

        if (matcher.matches()) {
            int a = Integer.parseInt(matcher.group(1));
            int b = Integer.parseInt(matcher.group(2));
            return Integer.toString(a + b);
        }
        return null;
    }
}
