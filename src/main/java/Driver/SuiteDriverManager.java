package Driver;

import Constants.AppConstants;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Quan ly 1 Appium driver duy nhat cho TOAN BO suite.
 *
 * Truoc day moi test class tao driver moi qua BaseTest.@BeforeClass,
 * gay overhead ~10-15s/class (terminate app + quit driver + tao session moi).
 * Voi 40+ class -> ~7-10 phut lang phi.
 *
 * Driver duoc tao lazy o lan goi getOrCreateDriver() dau tien (boi SuiteListener
 * hoac BaseTest.setUp neu chay rieng le qua --tests filter).
 * Driver duoc quit boi SuiteListener.onFinish khi suite ket thuc.
 */
public class SuiteDriverManager {

    private static final Logger logger = LogManager.getLogger(SuiteDriverManager.class);

    private static volatile AppiumDriver driver;
    private static DriverManager manager;

    public static synchronized AppiumDriver getOrCreateDriver() {
        if (driver == null) {
            logger.info("=== CREATE SUITE-LEVEL DRIVER (1 lan duy nhat cho ca suite) ===");
            manager = DriverFactory.getDriverManager(AppConstants.PLATFORM_NAME);
            driver = manager.createDriver();
        }
        return driver;
    }

    public static synchronized void quitDriver() {
        if (driver != null) {
            logger.info("=== QUIT SUITE-LEVEL DRIVER (ket thuc suite) ===");
            try {
                manager.quitDriver();
            } catch (Exception e) {
                logger.warn("Loi quit driver: " + e.getMessage());
            }
            driver = null;
            manager = null;
        }
    }

    public static synchronized boolean isAlive() {
        return driver != null;
    }
}
