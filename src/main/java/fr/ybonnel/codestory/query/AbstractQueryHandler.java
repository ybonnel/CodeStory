package fr.ybonnel.codestory.query;


import javax.servlet.http.HttpServletRequest;

public abstract class AbstractQueryHandler {
    public abstract String getResponse(String query, String path, HttpServletRequest request) throws Exception;
}
