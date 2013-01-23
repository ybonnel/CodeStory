package fr.ybonnel.codestory.util;

import java.util.concurrent.TimeUnit;

/**
 * Utility class to have a chrono.
 */
public class Chrono {

    private Long startTime;
    private Long elapsedTime;

    /**
     * Start the chrono.
     * @return current instance of Chrono to chain call.
     */
    public Chrono start() {
        if (startTime != null) {
            throw new IllegalArgumentException("Method start already called");
        }
        startTime = System.nanoTime();
        return this;
    }

    /**
     * Stop the chrono.
     * @return current instance of Chrono to chain call.
     */
    public Chrono stop() {
        if (startTime == null) {
            throw new IllegalArgumentException("Method start not called");
        }
        if (elapsedTime != null) {
            throw new IllegalArgumentException("Method stop already called");
        }
        elapsedTime = System.nanoTime() - startTime;
        return this;
    }

    /**
     * @return time in milli-seconds.
     */
    public long getTimeInMs() {
        if (elapsedTime == null) {
            throw new IllegalArgumentException("Method start or stop not called");
        }
        return TimeUnit.NANOSECONDS.toMillis(elapsedTime);
    }

    /**
     * @return time in nano-seconds.
     */
    public long getTimeInNs() {
        if (elapsedTime == null) {
            throw new IllegalArgumentException("Method start or stop not called");
        }
        return elapsedTime;
    }

}
