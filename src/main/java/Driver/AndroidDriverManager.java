package Driver;

import Constants.AppConstants;
import Constants.TimeOutConstants;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.Duration;

public class AndroidDriverManager implements DriverManager {

    private static final Logger logger = LogManager.getLogger(AndroidDriverManager.class);
    private AndroidDriver driver;

    @Override
    public AppiumDriver createDriver() {
        logger.info("Creating Android driver for " + AppConstants.DEVICE_NAME);

        UiAutomator2Options options = new UiAutomator2Options();

        // Device info
        options.setPlatformName(AppConstants.PLATFORM_NAME);
        options.setPlatformVersion(AppConstants.PLATFORM_VERSION);
        options.setDeviceName(AppConstants.DEVICE_NAME);

        // App info
        options.setAppPackage(AppConstants.APP_PACKAGE);
        options.setAppActivity(AppConstants.APP_ACTIVITY);

        // Automation config
        options.setAutomationName(AppConstants.AUTOMATION_NAME);
        options.setNoReset(true);
        options.setAutoGrantPermissions(true);
        options.setNewCommandTimeout(Duration.ofSeconds(TimeOutConstants.NEW_COMMAND_TIMEOUT));

        // Bypass loi tren Oppo
        options.setCapability("appium:ignoreHiddenApiPolicyError", true);

        try {
            URL serverUrl = new URL(AppConstants.APPIUM_SERVER_URL);
            driver = new AndroidDriver(serverUrl, options);
            logger.info("Android driver created successfully");
        } catch (Exception e) {
            logger.error("Failed to create Android driver: " + e.getMessage());
            throw new RuntimeException("Cannot create Android driver", e);
        }

        return driver;
    }

    @Override
    public AppiumDriver getDriver() {
        return driver;
    }

    @Override
    public void quitDriver() {
        if (driver != null) {
            logger.info("Quitting Android driver");
            driver.quit();
            driver = null;
        }
    }
}