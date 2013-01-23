package fr.ybonnel.codestory;

import fr.ybonnel.codestory.util.LogUtil;
import org.mortbay.jetty.Server;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Signal handler in order to stop server when we receive a SIG TERM.
 */
public class StopHandler implements SignalHandler {

    private Server server;

    public StopHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(Signal signal) {
        LogUtil.logMessage("CodeStory stopping");
        try {
            server.stop();
        } catch (Exception e) {
            assert false;
        }
    }
}
