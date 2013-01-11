package fr.ybonnel.codestory;

import org.mortbay.jetty.Server;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.text.SimpleDateFormat;
import java.util.Date;


public class StopHandler implements SignalHandler {

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
