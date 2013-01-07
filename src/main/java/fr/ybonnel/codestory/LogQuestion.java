package fr.ybonnel.codestory;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogQuestion extends AbstractQuestion {

    private Gson gson = new GsonBuilder().create();

    @Override
    public String getReponse(String question) throws IOException {
        if ("log".equals(question)) {
            return logsToHtml(DatabaseManager.INSTANCE.getLogs());
        }

        Matcher matcher = Pattern.compile("log\\(([a-zA-Z])\\)").matcher(question);
        if (matcher.matches()) {
            return logsToHtml(DatabaseManager.INSTANCE.getLogsByType(matcher.group(1)));
        }
        return null;
    }

    private String logsToHtml(List<DatabaseManager.LogMessage> logMessages) {

        StringBuilder builder = new StringBuilder("<table border=\"1\">");
        builder.append("<tr><th>Heure</th><th>Type</th><th>Message</th></tr>");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss,SSS");
        for (DatabaseManager.LogMessage logMessage : logMessages) {
            builder.append("<tr>");
            builder.append("<td>");
            builder.append(sdf.format(logMessage.getDate()));
            builder.append("</td>");
            builder.append("<td>");
            builder.append(logMessage.getType());
            builder.append("</td>");
            builder.append("<td>");
            builder.append(logMessage.getMessage().replaceAll("\n", "<br/>"));
            builder.append("</td>");
            builder.append("</tr>");
        }
        builder.append("</table>");

        return builder.toString();
    }
}
