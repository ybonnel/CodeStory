package fr.ybonnel.codestory.query;


import fr.ybonnel.codestory.logs.DatabaseManager;
import org.pegdown.PegDownProcessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnonceQueryHandler extends AbstractQueryHandler {
    @Override
    public String getResponse(String query, String path, String requestBody) throws Exception {
        if (path.equals("/enonce")) {
            return getAllEnonce();
        }
        Matcher matcher = Pattern.compile("/enonce/(\\d+)").matcher(path);
        if (matcher.matches()) {
            int id = Integer.parseInt(matcher.group(1));
            String enonce = requestBody;
            if (enonce.startsWith("{") && enonce.endsWith("}")) {
                enonce = enonce.substring(1, enonce.length() - 1);
            }
            DatabaseManager.INSTANCE.insertEnonce(id, enonce);
            return "OK, j'ai compris";
        }
        return null;
    }

    public String getAllEnonce() {
        return enoncesToHtml(DatabaseManager.INSTANCE.getAllEnonces());
    }

    private String enoncesToHtml(List<DatabaseManager.Enonce> allEnonces) {
        StringBuilder builder = new StringBuilder("<table id=\"enonces\" border=\"1\">");
        builder.append("<tr><th>ID</th><th>Enonc&eacute;</th></tr>");

        for (DatabaseManager.Enonce enonce : allEnonces) {
            builder.append("<tr>");
            builder.append("<td>");
            builder.append(enonce.getId());
            builder.append("</td>");
            builder.append("<td>");
            builder.append(formatMarkdown(enonce.getContent()));
            builder.append("</td>");
            builder.append("</tr>");
        }
        builder.append("</table>");

        return builder.toString();
    }

    private String formatMarkdown(String content) {
        return new PegDownProcessor().markdownToHtml(content);
    }
}
