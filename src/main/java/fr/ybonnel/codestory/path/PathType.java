package fr.ybonnel.codestory.path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PathType {

    INSERT_ENONCE(new InsertEnonceHandler(), "/enonce/(\\d+)", "POST"),
    GET_ENONCES(new GetEnoncesHandler(), "/enonce(/)*", "GET"),
    SCALASKEL_CHANGES(new ChangeScalaskelHandler(), "/scalaskel/change/(\\d+)", "GET"),
    JAJASCRIPT(new OptimizeJajascriptHandler(), "/jajascript/optimize.*", "POST");

    private AbstractPathHandler handler;

    private Pattern pathPattern;
    private String method;

    PathType(AbstractPathHandler handler, String pathPattern, String method) {
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

    public static PathResponse getResponse(HttpServletRequest request, String payLoad) throws Exception {
        for (PathType onePath : values()) {
            Matcher isThisPath = onePath.isThisPath(request.getMethod(), request.getPathInfo());
            if (isThisPath != null && isThisPath.matches()) {
                return onePath.handler.getResponse(request, payLoad, extractParameters(isThisPath));
            }
        }
        return new PathResponse(HttpServletResponse.SC_NOT_FOUND, "This path is unknown");
    }

    private static String[] extractParameters(Matcher thisPath) {
        String[] params = new String[thisPath.groupCount()];
        for (int groupIndex = 1; groupIndex <= thisPath.groupCount(); groupIndex++) {
            params[groupIndex - 1] = thisPath.group(groupIndex);
        }
        return params;
    }
}
