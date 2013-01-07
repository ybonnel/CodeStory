package fr.ybonnel.codestory;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static junit.framework.Assert.assertEquals;

public class WebServerTest {

    public static final int PORT = 18080;
    private Server server;

    @Before
    public void setup() throws Exception {
        WebServer.setTest(true);
        server = new Server(PORT);
        server.setHandler(new WebServer());
        server.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    server.join();
                } catch (InterruptedException ignore) {
                }
            }
        }.start();

    }

    @After
    public void teardown() throws Exception {
        server.stop();
    }



    @Test
    public void should_answear_to_whatsyourmail() throws Exception {
        String url = "http://localhost:" + PORT + "/?q=Quelle+est+ton+adresse+email";
        HttpResponse response = sendGetRequest(url);
        assertEquals("Status code must be 200", 200, response.getStatusCode());
        assertEquals("Response must be my mail", "ybonnel@gmail.com", responseToString(response));
    }

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private HttpResponse sendGetRequest(String url) throws IOException {
        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory();
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(url));
        return request.execute();
    }

    private String responseToString(HttpResponse response) throws IOException {
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getContent(), response.getContentCharset()));
        StringBuilder builder = new StringBuilder();
        for (String line = bufReader.readLine();line!=null;line = bufReader.readLine()) {
            builder.append(line);
        }
        return builder.toString();
    }

}
