package github.andredimaz.plugin.core.utils.objects;

import java.util.concurrent.TimeUnit;

public class CooldownUtils {
    private long start;
    private long end;
    private int seconds;

    public CooldownUtils(int seconds) {
        this.seconds = seconds;
        this.start = System.currentTimeMillis();
        this.end = this.start + TimeUnit.SECONDS.toMillis((long)seconds);
    }

    public int getSeconds() {
        return this.seconds;
    }

    public long getEnd() {
        return this.end;
    }

    public long getStart() {
        return this.start;
    }

    public int getSecondsLeft() {
        return (int)TimeUnit.MILLISECONDS.toSeconds(this.end - System.currentTimeMillis());
    }

    public boolean alreadyPassed() {
        return System.currentTimeMillis() > this.end;
    }
}
