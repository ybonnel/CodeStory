package fr.ybonnel.codestory.path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ybonnel.codestory.WebServerResponse;
import fr.ybonnel.codestory.path.jajascript.Flight;
import fr.ybonnel.codestory.path.jajascript.JajaScriptResponse;
import fr.ybonnel.codestory.path.jajascript.JajascriptService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class OptimizeJajascriptHandler extends AbstractPathHandler {

    public static final String CONTENT_TYPE_JSON = "application/json";
    private final ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final TypeReference<List<Flight>> requestType = new TypeReference<List<Flight>>() {
    };


    @Override
    public WebServerResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws Exception {
        List<Flight> commandes = mapper.readValue(payLoad, requestType);

        JajaScriptResponse jajaScriptResponse = new JajascriptService(commandes).calculate();

        WebServerResponse pathResponse = new WebServerResponse(HttpServletResponse.SC_OK, mapper.writeValueAsString(jajaScriptResponse));
        pathResponse.setContentType(CONTENT_TYPE_JSON);
        // Specific log to avoid log big request with thousands flights.
        pathResponse.setSpecificLog("NbCommands=" + commandes.size() + ", gain=" + jajaScriptResponse.getGain());
        return pathResponse;
    }
}
