package fr.ybonnel.codestory.logs;

import fr.ybonnel.codestory.WebServer;
import fr.ybonnel.codestory.database.DatabaseManager;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogUtil {


    public static void logHttpRequest(HttpServletRequest request, int status, String requestBody, String response, long elapsedTime) {
        String query = request.getParameter(WebServer.QUERY_PARAMETER);
        if ((query != null && query.startsWith("log"))
                || "/favicon.ico".equals(request.getPathInfo())) {
            return;
        }


        final StringBuilder logMessage = new StringBuilder();
        logMessage.append("\t");
        logMessage.append(request.getMethod());
        logMessage.append("\n\tPath info : ")
                .append(request.getPathInfo());
        logMessage.append("\n\tRequest parameters : ")
                .append(convertParametersMap(request));
        logMessage.append("\n\tRemote adress : ")
                .append(request.getRemoteAddr());
        logMessage.append("\n\tRequest body : ")
                .append(requestBody);
        logMessage.append("\n\tRequest headers : ")
                .append(getRequestHeaders(request));
        logMessage.append("\n\tResponse status : ").append(status);
        logMessage.append("\n\tResponse time : ").append(NumberFormat.getInstance(Locale.FRANCE).format(elapsedTime)).append("ns");
        logMessage.append("\n\tResponse : ").append(response);

        DatabaseManager.INSTANCE.insertLog(DatabaseManager.TYPE_Q, logMessage.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss,SSS");
        System.out.println(sdf.format(new Date()) + "\n" + logMessage.toString());
    }

    private static Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<String, String>();
        if (request.getHeader("Accept") != null) {
            headersMap.put("Accept", request.getHeader("Accept"));
        }
        if (request.getHeader("Content-Type") !=null) {
            headersMap.put("Content-Type", request.getHeader("Content-Type"));
        }
        return headersMap;
    }

    private static String convertParametersMap(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        Enumeration parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            builder.append("\n\t\tparameter(").append(parameter).append("={");
            builder.append(request.getParameter(parameter)).append('}');
        }
        return builder.toString();
    }

    public static void logUnkownQuery(String queryParameter) {
        System.err.println("#### QueryType inconnue : " + queryParameter + " ####");
        DatabaseManager.INSTANCE.insertLog(DatabaseManager.TYPE_NEW, queryParameter);
    }

}
