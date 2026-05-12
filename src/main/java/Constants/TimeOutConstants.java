package Constants;

public class TimeOutConstants {

    // Timeout cho driver session
    public static final int NEW_COMMAND_TIMEOUT = 120;

    // Timeout cho explicit wait (smart wait)
    public static final int SHORT_WAIT = 3;             // 3s
    public static final int MEDIUM_WAIT = 8;            // 8s
    public static final int LONG_WAIT = 15;             // 15s

    // Sleep (chi dung khi BAT BUOC, han che toi da)
    public static final int SLEEP_SHORT = 500;          // 0.5s (giam tu 1s)
    public static final int SLEEP_MEDIUM = 1500;        // 1.5s (giam tu 3s)
    public static final int SLEEP_LONG = 3000;          // 3s (giam tu 5s)

    // Sleep cho animation (drawer, transition...)
    public static final int ANIMATION_WAIT = 800;       // 0.8s
}