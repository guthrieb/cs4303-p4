package helpers;

import java.util.Date;

public class Timer {
    private final long timeLimit;
    private long startTime = System.currentTimeMillis();
    private long elapsedTime = 0L;

    public Timer(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean completed() {
        elapsedTime = (new Date().getTime()) - startTime;

        return elapsedTime >= timeLimit;
    }

    public void reset() {
        this.elapsedTime = 0L;
        this.startTime = System.currentTimeMillis();
    }

    public double percentageRemaining() {
        elapsedTime = (new Date().getTime()) - startTime;
        return 1.0 -  (double)elapsedTime/(double)timeLimit;
    }
}
