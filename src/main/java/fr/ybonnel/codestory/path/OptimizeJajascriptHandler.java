package fr.ybonnel.codestory.path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OptimizeJajascriptHandler extends AbstractPathHandler {


    @Override
    public PathResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws Exception {
        return new PathResponse(HttpServletResponse.SC_OK, "{\"gain\":18,\"path\":[\"MONAD42\",\"YAGNI17\"]}");
    }
}
