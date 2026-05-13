package Utils;

import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper de execute ADB shell command.
 *
 * Tinh nang:
 * - Grant/Revoke quyen WRITE_SETTINGS cho app
 * - Check trang thai permission
 * - Clear app data (reset triet de)
 * - Reset permission bang nhieu method (fallback)
 *
 * Yeu cau: ADB phai co trong PATH (adb --version OK).
 */
public class AdbHelper {

    private static final Logger logger = LoggerFactory.getLogger(AdbHelper.class);

    // ========================================================
    // PUBLIC API - PERMISSION MANAGEMENT
    // ========================================================

    /**
     * Revoke quyen WRITE_SETTINGS cua app (don gian).
     */
    public static void revokeWriteSettings(AppiumDriver driver, String packageName) {
        logger.info("REVOKE WRITE_SETTINGS cho: " + packageName);
        executeAdbShell("appops set " + packageName + " WRITE_SETTINGS deny");
    }

    /**
     * Grant quyen WRITE_SETTINGS cho app.
     */
    public static void grantWriteSettings(AppiumDriver driver, String packageName) {
        logger.info("GRANT WRITE_SETTINGS cho: " + packageName);
        executeAdbShell("appops set " + packageName + " WRITE_SETTINGS allow");
    }

    /**
     * Reset WRITE_SETTINGS bang nhieu cach (fallback).
     * Dam bao revoke duoc tren nhieu device khac nhau.
     *
     * Method 1: appops set ... deny (chuan)
     * Method 2: pm revoke (Android version moi)
     * Method 3: cmd appops set ... deny (alternative syntax)
     */
    public static void resetWriteSettings(AppiumDriver driver, String packageName) {
        logger.info("RESET WRITE_SETTINGS (multi-method) cho: " + packageName);

        // Method 1: appops
        executeAdbShell("appops set " + packageName + " WRITE_SETTINGS deny");

        // Method 2: pm revoke (some Android versions)
        executeAdbShell("pm revoke " + packageName +
                " android.permission.WRITE_SETTINGS");

        // Method 3: cmd appops (newer Android)
        executeAdbShell("cmd appops set " + packageName + " WRITE_SETTINGS deny");
    }

    /**
     * Verify permission da duoc revoke chua, neu chua thi CLEAR APP DATA.
     * Dam bao test ke tiep bat dau voi state sach hoan toan.
     *
     * Su dung khi: dam bao state cho test setup.
     */
    public static void ensurePermissionRevoked(AppiumDriver driver,
                                               String packageName) {
        logger.info("=== Ensure permission revoked ===");

        // Buoc 1: Thu reset bang nhieu method
        resetWriteSettings(driver, packageName);

        // Buoc 2: Verify
        String status = checkWriteSettingsStatus(driver, packageName);
        logger.info("Status sau revoke: " + status);

        // Buoc 3: Neu van con allow -> clear data
        if ("allow".equalsIgnoreCase(status)) {
            logger.warn("Revoke KHONG work, clear app data");
            clearAppData(driver, packageName);

            // Verify lai sau clear
            String statusAfterClear = checkWriteSettingsStatus(driver, packageName);
            logger.info("Status sau clear data: " + statusAfterClear);
        }
    }

    /**
     * Check trang thai WRITE_SETTINGS cua app.
     * Tra ve "allow" | "deny" | "unknown".
     */
    public static String checkWriteSettingsStatus(AppiumDriver driver,
                                                  String packageName) {
        String output = executeAdbShell("appops get " + packageName +
                " WRITE_SETTINGS");

        if (output == null || output.isEmpty()) {
            return "unknown";
        }

        String lowerOutput = output.toLowerCase();
        if (lowerOutput.contains("allow")) {
            return "allow";
        } else if (lowerOutput.contains("deny") ||
                lowerOutput.contains("default")) {
            return "deny";
        }
        return "unknown";
    }

    // ========================================================
    // PUBLIC API - APP MANAGEMENT
    // ========================================================

    /**
     * Clear toan bo app data (reset app ve trang thai ban dau).
     * Mat tat ca config, permission, cache, files.
     *
     * Su dung khi: appops revoke khong work.
     */
    public static void clearAppData(AppiumDriver driver, String packageName) {
        logger.info("CLEAR APP DATA cho: " + packageName);
        executeAdbShell("pm clear " + packageName);
    }

    /**
     * Force stop app (dung lai process app).
     */
    public static void forceStopApp(AppiumDriver driver, String packageName) {
        logger.info("FORCE STOP: " + packageName);
        executeAdbShell("am force-stop " + packageName);
    }

    /**
     * Launch app bang activity.
     */
    public static void launchApp(AppiumDriver driver, String packageName,
                                 String activityName) {
        logger.info("LAUNCH APP: " + packageName + "/" + activityName);
        executeAdbShell("am start -n " + packageName + "/" + activityName);
    }

    // ========================================================
    // PRIVATE - SHELL EXECUTION
    // ========================================================

    /**
     * Execute ADB shell command bang Runtime.
     * Tra ve output cua command.
     */
    private static String executeAdbShell(String shellCommand) {
        try {
            String fullCommand = "adb shell " + shellCommand;
            logger.info("Execute: " + fullCommand);

            ProcessBuilder pb = new ProcessBuilder();
            if (isWindows()) {
                pb.command("cmd.exe", "/c", fullCommand);
            } else {
                pb.command("bash", "-c", fullCommand);
            }
            pb.redirectErrorStream(true);

            Process process = pb.start();
            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Cho command hoan thanh (max 10s)
            boolean finished = process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS);
            if (!finished) {
                process.destroy();
                logger.warn("Command timeout: " + fullCommand);
            }

            String result = output.toString().trim();
            if (!result.isEmpty()) {
                logger.info("Output: " + result);
            }
            return result;
        } catch (Exception e) {
            logger.error("ADB shell error [" + shellCommand + "]: " + e.getMessage());
            return null;
        }
    }

    /**
     * Execute ADB shell qua Appium mobile:shell (alternative).
     * Dung khi shell tu OS khong work.
     *
     * Vi du:
     *   executeAdbShellViaAppium(driver, "appops",
     *       new String[]{"set", packageName, "WRITE_SETTINGS", "deny"});
     */
    public static String executeAdbShellViaAppium(AppiumDriver driver,
                                                  String command,
                                                  String[] args) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("command", command);
            params.put("args", args);

            Object result = driver.executeScript("mobile: shell", params);
            String output = result != null ? result.toString() : null;
            logger.info("Appium shell output: " + output);
            return output;
        } catch (Exception e) {
            logger.error("Appium shell error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Check OS hien tai co phai Windows khong.
     */
    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}