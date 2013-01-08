package fr.ybonnel.codestory;


import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mortbay.jetty.Server;

import static net.sourceforge.jwebunit.junit.JWebUnit.setBaseUrl;

public class WebServerRule implements TestRule {

    private final int portNumber;

    public WebServerRule() {
        this.portNumber = Integer.getInteger("port", 18080);
    }

    public String getURL() {
        return "http://localhost:" + portNumber + "/";
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                WebServer.setTest(true);
                final Server server = new Server(portNumber);
                server.setHandler(new WebServer());
                server.start();

                try {
                    setBaseUrl(getURL());
                    base.evaluate();
                } finally {
                    server.stop();
                }
            }
        };
    }
}
