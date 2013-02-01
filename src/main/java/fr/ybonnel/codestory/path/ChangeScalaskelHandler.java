package fr.ybonnel.codestory.path;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import fr.ybonnel.codestory.WebServerResponse;
import fr.ybonnel.codestory.path.scalaskel.Coin;
import fr.ybonnel.codestory.path.scalaskel.Change;
import fr.ybonnel.codestory.path.scalaskel.ScalaskelChangeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ChangeScalaskelHandler extends AbstractPathHandler {

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private boolean wrongParams(int centsToPay) {
        return centsToPay <= 0 || centsToPay > 100;
    }

    @Override
    public WebServerResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws JsonProcessingException, NumberFormatException {
        int centsToPay = Integer.parseInt(params[0]);
        if (wrongParams(centsToPay)) {
            return new WebServerResponse(HttpServletResponse.SC_FORBIDDEN, "Wrong parameters");
        }

        List<Change> changes = ScalaskelChangeService.getInstance().calculateChanges(centsToPay);
        return new WebServerResponse(HttpServletResponse.SC_OK, objectMapper.writeValueAsString(changes));
    }


}
