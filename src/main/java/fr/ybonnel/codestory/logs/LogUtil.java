package fr.ybonnel.codestory.logs;

import fr.ybonnel.codestory.WebServer;
import fr.ybonnel.codestory.database.DatabaseManager;
import fr.ybonnel.codestory.database.modele.LogMessage;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class LogUtil {

    private static boolean mustLog = true;

    public static void disableLogs() {
        mustLog = false;
    }

    public static void enableLogs() {
        mustLog = true;
    }


    public static void logHttpRequest(Date date, HttpServletRequest request, String payLoad, int status, String response, long elapsedTime, String specifiqueLog) {
        if (!mustLog) {
            return;
        }
        String query = request.getParameter(WebServer.QUERY_PARAMETER);
        if (query != null && query.startsWith("log")
                || "/favicon.ico".equals(request.getPathInfo())
                || request.getPathInfo().endsWith(".png")) {
            return;
        }


        StringBuilder logMessage = new StringBuilder();
        logMessage.append('\t');
        logMessage.append(request.getMethod());
        logMessage.append("\n\tPath info : ")
                .append(request.getPathInfo());
        if (specifiqueLog == null) {
            logMessage.append("\n\tRequest parameters : ")
                    .append(convertParametersMap(request));
        }
        logMessage.append("\n\tRemote adress : ")
                .append(request.getRemoteAddr());
        logMessage.append("\n\tRequest headers : ")
                .append(getRequestHeaders(request));
        if (specifiqueLog == null) {
        logMessage.append("\n\tRequest payload : ")
                .append(payLoad);
        }
        logMessage.append("\n\tResponse status : ").append(status);
        logMessage.append("\n\tResponse time : ").append(NumberFormat.getInstance(Locale.FRANCE).format(elapsedTime)).append("ns");
        if (specifiqueLog == null) {
            logMessage.append("\n\tResponse : ").append(response);
        } else {
            logMessage.append("\n\tSpecific : ").append(specifiqueLog);
        }

        DatabaseManager.INSTANCE.getLogDao().insert(new LogMessage(date, DatabaseManager.TYPE_Q, logMessage.toString()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss,SSS");
        System.out.println(sdf.format(date) + '\n' + logMessage);
    }

    private static Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = newHashMap();
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
        Enumeration<?> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            builder.append("\n\t\tparameter(").append(parameter).append("={");
            builder.append(request.getParameter(parameter)).append('}');
        }
        return builder.toString();
    }

    public static void logUnkownQuery(String queryParameter) {
        System.err.println("#### QueryType inconnue : " + queryParameter + " ####");
        DatabaseManager.INSTANCE.getLogDao().insert(new LogMessage(DatabaseManager.TYPE_NEW, queryParameter));
    }

    public static void logRequestUrl(HttpServletRequest request) {
        if (!mustLog) {
            return;
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        StringBuilder logRequest = new StringBuilder(sdf.format(date)).append(':');
        logRequest.append(request.getMethod()).append(':');
        logRequest.append(request.getRequestURL());
        if (request.getQueryString() != null) {
            logRequest.append('?').append(request.getQueryString());
        }
        System.err.println(logRequest.toString());
    }
}
