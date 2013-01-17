package fr.ybonnel.codestory.path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ybonnel.codestory.path.jajascript.Commande;
import fr.ybonnel.codestory.path.jajascript.JajaScriptResponse;
import fr.ybonnel.codestory.path.jajascript.JajascriptRequest;
import fr.ybonnel.codestory.path.jajascript.JajascriptService;
import fr.ybonnel.codestory.path.jajascript.LegacyJajascriptService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimizeJajascriptHandler extends AbstractPathHandler {

    private final ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final TypeReference<List<Commande>> requestType = new TypeReference<List<Commande>>() {
    };


    @Override
    public PathResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws Exception {
        List<Commande> commandes = mapper.readValue(payLoad, requestType);

        JajaScriptResponse jajaScriptResponse;
        if (request.getPathInfo().endsWith("legacy")) {
            jajaScriptResponse = new LegacyJajascriptService(commandes).calculate();
        } else {
            jajaScriptResponse = new JajascriptService(commandes).calculate();
        }

        PathResponse pathResponse = new PathResponse(HttpServletResponse.SC_OK, mapper.writeValueAsString(jajaScriptResponse));
        pathResponse.setContentType("application/json");
        pathResponse.setSpecificLog("NbCommands=" + commandes.size() + ", gain=" + jajaScriptResponse.getGain());
        return pathResponse;
    }
}
