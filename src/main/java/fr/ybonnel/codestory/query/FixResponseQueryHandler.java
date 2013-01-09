package fr.ybonnel.codestory.query;

import javax.servlet.http.HttpServletRequest;

public class FixResponseQueryHandler extends AbstractQueryHandler {

    private String response;

    public FixResponseQueryHandler(String response) {
        this.response = response;
    }

    @Override
    public String getResponse(String query, String path, HttpServletRequest request) throws Exception {
        return response;
    }
}
