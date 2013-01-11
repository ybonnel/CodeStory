package fr.ybonnel.codestory.path;


public class PathResponse {
    private int statusCode;
    private String response;

    public PathResponse(int statusCode, String response) {
        this.statusCode = statusCode;
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponse() {
        return response;
    }
}
