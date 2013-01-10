package fr.ybonnel.codestory;

import fr.ybonnel.codestory.logs.LogUtil;
import fr.ybonnel.codestory.path.PathType;
import fr.ybonnel.codestory.query.QueryType;
import org.apache.commons.io.IOUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebServer extends AbstractHandler {

    public static final String QUERY_PARAMETER = "q";

    @Override
    public void handle(String target,
                       HttpServletRequest request,
                       HttpServletResponse httpResponse,
                       int dispatch)
            throws IOException, ServletException {

        long startTime = System.nanoTime();

        String query = request.getParameter(QUERY_PARAMETER);
        httpResponse.setContentType("text/html;charset=utf-8");

        String response;
        int status = HttpServletResponse.SC_OK;

        try {
            if (query != null) {
                response = QueryType.getResponse(query, request.getPathInfo(), request);
                if (response == null) {
                    response = "Query " + query + " is unknown";
                    status = HttpServletResponse.SC_NOT_FOUND;
                }
            } else {
                PathType.PathResponse pathResponse = PathType.getResponse(request);
                status = pathResponse.getStatusCode();
                response = pathResponse.getResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            response = e.getMessage();
        }

        httpResponse.setHeader("Server", "YboServer");
        httpResponse.setStatus(status);
        PrintWriter writer = httpResponse.getWriter();
        writer.print(response);
        writer.close();

        long elapsedTime = System.nanoTime() - startTime;


        LogUtil.logHttpRequest(request, status, response, elapsedTime);
    }


    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        System.err.println(sdf.format(new Date()) + ":CodeStory starting");
        int port = 10080;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server(port);

        Signal.handle(new Signal("TERM"), new StopHandler(server));

        server.setHandler(new WebServer());
        server.start();
        server.join();

        System.err.println(sdf.format(new Date()) + ":CodeStory stopped");
    }

    public static class StopHandler implements SignalHandler {

        private Server server;

        public StopHandler(Server server) {
            this.server = server;
        }

        @Override
        public void handle(Signal signal) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
            System.err.println(sdf.format(new Date()) + ":CodeStory stoping");
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(60);
            }
        }
    }

}
