package fr.ybonnel.codestory;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {

    static void log(HttpServletRequest request, int status, String reponse) {
        String query = request.getParameter(WebServer.QUERY_PARAMETER);
        if ((query != null && "log".equals(query))
                || "/favicon.ico".equals(request.getPathInfo())) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss,SSS");
        final StringBuilder logMessage = new StringBuilder(sdf.format(new Date()));
        logMessage.append("\n\t");
        logMessage.append(request.getMethod());
        logMessage.append("\n\tPath info:")
                .append(request.getPathInfo());
        logMessage.append("\n\tRequest parameters:")
                .append(convertParametersMap(request));
        logMessage.append("\n\tRemote adress:")
                .append(request.getRemoteAddr());
        logMessage.append("\n\tResponse status:").append(status);
        logMessage.append("\n\tResponse:").append(reponse);

        System.out.println(logMessage.toString());
    }

    private static Map<String, String> convertParametersMap(HttpServletRequest request) {
        Enumeration parameters = request.getParameterNames();
        Map<String, String> retour = new HashMap<String, String>();
        while (parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            retour.put(parameter, request.getParameter(parameter));
        }
        return retour;
    }
}
