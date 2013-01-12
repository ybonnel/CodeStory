package fr.ybonnel.codestory.path;


import fr.ybonnel.codestory.database.DatabaseManager;
import fr.ybonnel.codestory.database.modele.Enonce;
import org.pegdown.PegDownProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GetEnoncesHandler extends AbstractPathHandler {
    @Override
    public PathResponse getResponse(HttpServletRequest request, String payLoad, String... param) {
        return new PathResponse(HttpServletResponse.SC_OK, getAllEnonce());
    }

    public String getAllEnonce() {
        return enoncesToHtml(DatabaseManager.INSTANCE.getEnonceDao().findAll());
    }

    private String enoncesToHtml(List<Enonce> allEnonces) {
        StringBuilder builder = new StringBuilder("<table id=\"enonces\" border=\"1\">");
        builder.append("<tr><th>ID</th><th>Titre</th><th>Enonc&eacute;</th></tr>");

        for (Enonce enonce : allEnonces) {
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
