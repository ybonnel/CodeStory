package fr.ybonnel.codestory.path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ybonnel.codestory.path.jajascript.JajaScriptResponse;
import fr.ybonnel.codestory.path.jajascript.JajascriptRequest;
import fr.ybonnel.codestory.path.jajascript.JajascriptService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class OptimizeJajascriptHandler extends AbstractPathHandler {

    private final ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private Map<String, String> mapReponses = new HashMap<String, String>();


    @Override
    public PathResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws Exception {

        String reponse;

        if (mapReponses.containsKey(payLoad)) {
            reponse = mapReponses.get(payLoad);
        } else {

            JajascriptRequest jajascriptRequest = JajascriptRequest.fromPayLoad(payLoad);

            JajaScriptResponse jajaScriptResponse = new JajascriptService(jajascriptRequest).calculate();

            reponse = mapper.writeValueAsString(jajaScriptResponse);

            mapReponses.put(payLoad, reponse);
        }

        PathResponse pathResponse = new PathResponse(HttpServletResponse.SC_OK, reponse);
        pathResponse.setContentType("application/json");
        return pathResponse;
    }
}
