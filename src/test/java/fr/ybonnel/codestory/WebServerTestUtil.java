package fr.ybonnel.codestory;


import fr.ybonnel.codestory.database.DatabaseManager;
import fr.ybonnel.codestory.database.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.mortbay.jetty.Server;

import java.util.Random;

public abstract class WebServerTestUtil {

    private final int portNumber;

    private final Random random = new Random();

    protected int getRandomPort() {
        return random.nextInt(1000) + 20000;
    }

    protected WebServerTestUtil() {
        portNumber = getRandomPort();
    }

    public String getURL() {
        return getURL(portNumber);
    }

    public static String getURL(int portNumber) {
        return "http://localhost:" + portNumber + '/';
    }

    private Server server;

    @Before
    public void startServer() throws Exception {
        DatabaseUtil.goInTestMode();
        DatabaseManager.INSTANCE.createDatabase();
        server = new Server(portNumber);
        server.setHandler(new WebServer());
        server.start();
    }

    @After
    public void stopServer() throws Exception {
        server.stop();
    }
}
