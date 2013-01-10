package fr.ybonnel.codestory;


import fr.ybonnel.codestory.database.DatabaseManager;
import fr.ybonnel.codestory.database.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.mortbay.jetty.Server;

import static net.sourceforge.jwebunit.junit.JWebUnit.setBaseUrl;

public abstract class WebServerTestUtil {

    private final int portNumber;

    public WebServerTestUtil() {
        this.portNumber = Integer.getInteger("port", 18080);
    }

    public String getURL() {
        return "http://localhost:" + portNumber + "/";
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
