package fr.ybonnel.codestory.path;


import javax.servlet.http.HttpServletRequest;

public abstract class AbstractPathHandler {
    public abstract PathResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws Exception;
}
