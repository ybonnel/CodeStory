package fr.ybonnel.codestory.path;


import fr.ybonnel.codestory.WebServerResponse;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractPathHandler {
    public abstract WebServerResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws Exception;
}
