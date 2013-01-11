package fr.ybonnel.codestory.path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PathType {

    INSERT_ENONCE(new InsertEnonceHandler(), "/enonce/(\\d+)", "POST"),
    GET_ENONCES(new GetEnoncesHandler(), "/enonce(/)*", "GET"),
    SCALASKEL_CHANGES(new ChangeScalaskelHandler(), "/scalaskel/change/(\\d+)", "GET");

    private AbstractPathHandler handler;

    private Pattern pathPattern;
    private String method;

    private PathType(AbstractPathHandler handler, String pathPattern, String method) {
        this.handler = handler;
        this.pathPattern = Pattern.compile(pathPattern);
        this.method = method;
    }


    public Matcher isThisPath(String method, String path) {
        if (this.method.equals(method)) {
            return pathPattern.matcher(path);
        } else {
            return null;
        }
    }

    public static class PathResponse {
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

    public static PathResponse getResponse(HttpServletRequest request) throws Exception {
        for (PathType onePath : values()) {
            Matcher isThisPath = onePath.isThisPath(request.getMethod(), request.getPathInfo());
            if (isThisPath != null && isThisPath.matches()) {
                String[] params = new String[isThisPath.groupCount()];
                for (int groupIndex = 1; groupIndex <= isThisPath.groupCount(); groupIndex++) {
                    params[groupIndex - 1] = isThisPath.group(groupIndex);
                }

                return onePath.handler.getResponse(request, params);
            }
        }
        return new PathResponse(HttpServletResponse.SC_NOT_FOUND, "This path is unknown");
    }
}
