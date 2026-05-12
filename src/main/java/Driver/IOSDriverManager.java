package Driver;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IOSDriverManager implements DriverManager {

    private static final Logger logger = LogManager.getLogger(IOSDriverManager.class);

    @Override
    public AppiumDriver createDriver() {
        logger.warn("iOS driver chua duoc implement");
        throw new UnsupportedOperationException("iOS driver not implemented yet");
    }

    @Override
    public AppiumDriver getDriver() {
        return null;
    }

    @Override
    public void quitDriver() {
        // To be implemented
    }
}