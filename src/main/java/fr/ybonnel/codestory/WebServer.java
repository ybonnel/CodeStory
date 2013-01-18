package fr.ybonnel.codestory;

import fr.ybonnel.codestory.logs.LogUtil;
import fr.ybonnel.codestory.path.PathResponse;
import fr.ybonnel.codestory.path.PathType;
import fr.ybonnel.codestory.query.QueryType;
import org.apache.commons.io.IOUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import sun.misc.Signal;

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

        Date date = new Date();
        long startTime = System.nanoTime();


        String payLoad;
        try {
            payLoad = IOUtils.toString(request.getInputStream());
        } catch (Exception exception) {
            payLoad = IOUtils.toString(request.getReader());
        }

        LogUtil.logRequestUrl(request);


        String query = request.getParameter(QUERY_PARAMETER);
        httpResponse.setContentType("text/html;charset=utf-8");

        String response;
        int status = HttpServletResponse.SC_OK;

        String specificLog = null;

        try {
            if (query != null) {
                response = QueryType.getResponse(query);
                if (response == null) {
                    LogUtil.logUnkownQuery(query);
                    response = "Query " + query + " is unknown";
                    status = HttpServletResponse.SC_NOT_FOUND;
                }
            } else {
                PathResponse pathResponse = PathType.getResponse(request, payLoad);
                status = pathResponse.getStatusCode();
                response = pathResponse.getResponse();
                if (pathResponse.getContentType() != null) {
                    httpResponse.setContentType(pathResponse.getContentType());
                }
                specificLog = pathResponse.getSpecificLog();
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

        LogUtil.logHttpRequest(date, request, payLoad, status, response, elapsedTime, specificLog);
    }


    public static void main(String... args) throws Exception {
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

}
