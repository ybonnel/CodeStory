package fr.ybonnel.codestory.path;


import javax.servlet.http.HttpServletRequest;

public abstract class AbstractPathHandler {
    public abstract PathType.PathResponse getResponse(HttpServletRequest request, String...params) throws Exception;
}