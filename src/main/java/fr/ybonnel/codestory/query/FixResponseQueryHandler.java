package fr.ybonnel.codestory.query;

public class FixResponseQueryHandler extends AbstractQueryHandler {

    private String response;

    public FixResponseQueryHandler(String response) {
        this.response = response;
    }

    @Override
    public String getResponse(String query, String path, String requestBody) throws Exception {
        return response;
    }
}
