package fr.ybonnel.codestory;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class WebServer extends AbstractHandler {

    public static final String QUERY_PARAMETER = "q";

    private static final Map<String, String> mapOfResponse = new HashMap<String, String>() {{
        put("Quelle est ton adresse email", "ybonnel@gmail.com");
    }};

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
            throws IOException, ServletException {

        String resp = request.getParameter(QUERY_PARAMETER);
        response.setContentType("text/html;charset=utf-8");
        int status = HttpServletResponse.SC_NOT_FOUND;
        String reponse = "";
        if (resp != null && mapOfResponse.containsKey(resp)) {
            status = HttpServletResponse.SC_OK;
            reponse = mapOfResponse.get(resp);
        }

        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        writer.println(reponse);
        writer.close();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss,SSS");

        System.out.println();
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

    private Map<String, String> convertParametersMap(HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        Map<String, String> retour = new HashMap<String, String>();
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            retour.put(parameter, request.getParameter(parameter));
        }
        return retour;
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new WebServer());
        server.start();
        server.join();
    }

}
