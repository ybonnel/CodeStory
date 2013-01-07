package fr.ybonnel.codestory.logs;

import fr.ybonnel.codestory.WebServer;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {


    public static void logHttpRequest(HttpServletRequest request, int status, String response) {
        String query = request.getParameter(WebServer.QUERY_PARAMETER);
        if ((query != null && query.startsWith("log"))
                || "/favicon.ico".equals(request.getPathInfo())) {
            return;
        }


        final StringBuilder logMessage = new StringBuilder();
        logMessage.append("\t");
        logMessage.append(request.getMethod());
        logMessage.append("\n\tPath info:")
                .append(request.getPathInfo());
        logMessage.append("\n\tRequest parameters:")
                .append(convertParametersMap(request));
        logMessage.append("\n\tRemote adress:")
                .append(request.getRemoteAddr());
        logMessage.append("\n\tResponse status:").append(status);
        logMessage.append("\n\tResponse:").append(response);

        DatabaseManager.INSTANCE.insertLog(DatabaseManager.TYPE_Q, logMessage.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss,SSS");
        System.out.println(sdf.format(new Date()) + "\n" + logMessage.toString());
    }

    private static Map<String, String> convertParametersMap(HttpServletRequest request) {
        Enumeration parameters = request.getParameterNames();
        Map<String, String> parametersMap = new HashMap<String, String>();
        while (parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            parametersMap.put(parameter, request.getParameter(parameter));
        }
        return parametersMap;
    }

    public static void logUnkownQuery(String queryParameter) {
        System.err.println("#### QueryType inconnue : " + queryParameter + " ####");
        DatabaseManager.INSTANCE.insertLog(DatabaseManager.TYPE_NEW, queryParameter);

    }

}
