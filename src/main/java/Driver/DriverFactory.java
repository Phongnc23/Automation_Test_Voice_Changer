package Driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    public static DriverManager getDriverManager(String platform) {
        logger.info("Getting driver manager for platform: " + platform);

        switch (platform.toLowerCase()) {
            case "android":
                return new AndroidDriverManager();
            case "ios":
                return new IOSDriverManager();
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }
}