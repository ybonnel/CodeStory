package fr.ybonnel.codestory.path;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.ybonnel.codestory.database.DatabaseManager;
import org.pegdown.PegDownProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertEnonceHandler extends AbstractPathHandler {

    private Gson gson = new GsonBuilder().create();

    @Override
    public PathType.PathResponse getResponse(HttpServletRequest request, String...params) throws Exception {
        int id = Integer.parseInt(params[0]);
        DatabaseManager.Enonce enonce = contructEnonce(id, request);
        DatabaseManager.INSTANCE.insertEnonce(enonce);
        return new PathType.PathResponse(HttpServletResponse.SC_CREATED, gson.toJson(enonce));
    }

    public DatabaseManager.Enonce contructEnonce(int id, HttpServletRequest request) {
        Enumeration parameters = request.getParameterNames();
        String titre = (String) parameters.nextElement();
        return new DatabaseManager.Enonce(id, titre, request.getParameter(titre));

    }
}
