package fr.ybonnel.codestory.path;


import fr.ybonnel.codestory.database.DatabaseManager;
import fr.ybonnel.codestory.query.AbstractQueryHandler;
import org.pegdown.PegDownProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetEnoncesHandler extends AbstractPathHandler {
    @Override
    public PathType.PathResponse getResponse(HttpServletRequest request, String...param) throws Exception {
        return new PathType.PathResponse(HttpServletResponse.SC_OK, getAllEnonce());
    }

    public String getAllEnonce() {
        return enoncesToHtml(DatabaseManager.INSTANCE.getAllEnonces());
    }

    private String enoncesToHtml(List<DatabaseManager.Enonce> allEnonces) {
        StringBuilder builder = new StringBuilder("<table id=\"enonces\" border=\"1\">");
        builder.append("<tr><th>ID</th><th>Titre</th><th>Enonc&eacute;</th></tr>");

        for (DatabaseManager.Enonce enonce : allEnonces) {
            builder.append("<tr>");
            builder.append("<td>");
            builder.append(enonce.getId());
            builder.append("</td>");
            builder.append("<td>");
            builder.append(enonce.getTitle());
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
