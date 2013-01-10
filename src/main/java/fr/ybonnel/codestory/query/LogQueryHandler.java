package fr.ybonnel.codestory.query;


import fr.ybonnel.codestory.database.DatabaseManager;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogQueryHandler extends AbstractQueryHandler {

    @Override
    public String getResponse(String query, String path, HttpServletRequest request) throws IOException {
        if ("log".equals(query)) {
            return logsToHtml(DatabaseManager.INSTANCE.getLogs());
        }

        Matcher matcher = Pattern.compile("log\\(([a-zA-Z])\\)").matcher(query);
        if (matcher.matches()) {
            return logsToHtml(DatabaseManager.INSTANCE.getLogsByType(matcher.group(1)));
        }
        return null;
    }

    private String logsToHtml(List<DatabaseManager.LogMessage> logMessages) {

        StringBuilder builder = new StringBuilder("<table id=\"log\" border=\"1\">");
        builder.append("<tr><th>Time</th><th>Type</th><th>Message</th></tr>");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss,SSS");
        for (DatabaseManager.LogMessage logMessage : logMessages) {
            builder.append("<tr>");
            builder.append("<td>");
            builder.append(sdf.format(logMessage.getDate()));
            builder.append("</td>");
            builder.append("<td>");
            builder.append(logMessage.getType());
            builder.append("</td>");
            builder.append("<td><pre>");
            builder.append(logMessage.getMessage().replaceAll("\t", ""));
            builder.append("</pre></td>");
            builder.append("</tr>");
        }
        builder.append("</table>");

        return builder.toString();
    }
}
