package fr.ybonnel.codestory.path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ybonnel.codestory.path.jajascript.JajaScriptResponse;
import fr.ybonnel.codestory.path.jajascript.JajascriptRequest;
import fr.ybonnel.codestory.path.jajascript.JajascriptService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OptimizeJajascriptHandler extends AbstractPathHandler {

    private final ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);


    @Override
    public PathResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws Exception {

        JajascriptRequest jajascriptRequest = JajascriptRequest.fromPayLoad(payLoad);

        JajaScriptResponse jajaScriptResponse = new JajascriptService(jajascriptRequest).calculate();



        PathResponse pathResponse = new PathResponse(HttpServletResponse.SC_OK, mapper.writeValueAsString(jajaScriptResponse));
        pathResponse.setContentType("application/json");
        return pathResponse;
    }
}
