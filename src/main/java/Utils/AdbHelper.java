package Utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper de chay ADB commands qua Appium.
 * Dung cho cleanup permission, key press...
 */
public class AdbHelper {

    private static final Logger logger = LogManager.getLogger(AdbHelper.class);

    /**
     * Revoke quyen WRITE_SETTINGS cua app.
     */
    public static void revokeWriteSettings(AppiumDriver driver, String packageName) {
        executeShell(driver, "appops set " + packageName + " WRITE_SETTINGS deny");
        logger.info("Revoked WRITE_SETTINGS for " + packageName);
    }

    /**
     * Grant quyen WRITE_SETTINGS cho app.
     */
    public static void grantWriteSettings(AppiumDriver driver, String packageName) {
        executeShell(driver, "appops set " + packageName + " WRITE_SETTINGS allow");
        logger.info("Granted WRITE_SETTINGS for " + packageName);
    }

    /**
     * Check trang thai WRITE_SETTINGS cua app.
     * Return "allow" hoac "deny".
     */
    public static String checkWriteSettingsStatus(AppiumDriver driver, String packageName) {
        Object result = executeShell(driver,
                "appops get " + packageName + " WRITE_SETTINGS");
        return result != null ? result.toString() : "unknown";
    }

    /**
     * Press BACK key.
     */
    public static void pressBackKey(AppiumDriver driver) {
        executeShell(driver, "input keyevent KEYCODE_BACK");
        logger.info("Pressed BACK key");
    }

    /**
     * Press HOME key.
     */
    public static void pressHomeKey(AppiumDriver driver) {
        executeShell(driver, "input keyevent KEYCODE_HOME");
        logger.info("Pressed HOME key");
    }

    /**
     * Helper de execute shell command.
     */
    private static Object executeShell(AppiumDriver driver, String command) {
        try {
            Map<String, Object> args = new HashMap<>();
            args.put("command", command);
            return ((AndroidDriver) driver).executeScript("mobile: shell", args);
        } catch (Exception e) {
            logger.warn("Shell command failed: " + command + " - " + e.getMessage());
            return null;
        }
    }
}