package fr.ybonnel.codestory;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebServer extends AbstractHandler {

    public static final String QUERY_PARAMETER = "q";

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
            throws IOException, ServletException {

        String query = request.getParameter(QUERY_PARAMETER);
        response.setContentType("text/html;charset=utf-8");

        String reponse = null;
        int status = HttpServletResponse.SC_OK;
        try {
            reponse = Question.getReponse(query);
            if (reponse == null) {
                reponse = "";
                status = HttpServletResponse.SC_NOT_FOUND;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            reponse = "";
        }

        response.setHeader("Server", "YboServer");
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        writer.println(reponse);
        writer.close();

        LogUtil.log(request, status, reponse);
    }


    public static void main(String[] args) throws Exception {
        int port = 10080;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server(port);
        server.setHandler(new WebServer());
        server.start();
        server.join();
    }

}
