package fr.ybonnel.codestory.path;


public class PathResponse {
    private int statusCode;
    private String response;
    private String contentType;
    private String specificLog;

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSpecificLog() {
        return specificLog;
    }

    public void setSpecificLog(String specificLog) {
        this.specificLog = specificLog;
    }
}
