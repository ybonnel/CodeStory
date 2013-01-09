package fr.ybonnel.codestory.query;


public abstract class AbstractQueryHandler {
    public abstract String getResponse(String query, String path, String requestBody) throws Exception;
}
