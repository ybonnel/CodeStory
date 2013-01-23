package fr.ybonnel.codestory.path;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ybonnel.codestory.WebServerResponse;
import fr.ybonnel.codestory.database.DatabaseManager;
import fr.ybonnel.codestory.database.modele.Enonce;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class InsertEnonceHandler extends AbstractPathHandler {

    private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Override
    public WebServerResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws JsonProcessingException {

        int id = Integer.parseInt(params[0]);
        Enonce enonce = payLoad == null ? contructEnonce(id, request) : new Enonce(id, "Enonce " + id, payLoad);
        DatabaseManager.INSTANCE.getEnonceDao().insert(enonce);
        return new WebServerResponse(HttpServletResponse.SC_CREATED, mapper.writeValueAsString(enonce));
    }

    public Enonce contructEnonce(int id, HttpServletRequest request) {
        Enumeration<?> parameters = request.getParameterNames();
        String titre = (String) parameters.nextElement();
        return new Enonce(id, "Enonce " + id, titre + "=" + request.getParameter(titre));

    }
}
