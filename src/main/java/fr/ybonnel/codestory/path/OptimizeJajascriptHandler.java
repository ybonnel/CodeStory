package fr.ybonnel.codestory.path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ybonnel.codestory.path.jajascript.Commande;
import fr.ybonnel.codestory.path.jajascript.JajaScriptResponse;
import fr.ybonnel.codestory.path.jajascript.JajascriptService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class OptimizeJajascriptHandler extends AbstractPathHandler {

    private final ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final TypeReference<Commande[]> requestType = new TypeReference<Commande[]>() {
    };


    @Override
    public PathResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws Exception {
        long startTime = System.nanoTime();
        Commande[] commandes = mapper.readValue(payLoad, requestType);

        JajaScriptResponse jajaScriptResponse = new JajascriptService(commandes).calculate();

        PathResponse pathResponse = new PathResponse(HttpServletResponse.SC_OK, mapper.writeValueAsString(jajaScriptResponse));
        pathResponse.setContentType("application/json");
        long elapsedTime = System.nanoTime() - startTime;
        pathResponse.setSpecificLog("NbCommands=" + commandes.length + ", gain=" + jajaScriptResponse.getGain() + ", time=" + TimeUnit.NANOSECONDS.toMillis(elapsedTime) + "ms");
        return pathResponse;
    }
}
