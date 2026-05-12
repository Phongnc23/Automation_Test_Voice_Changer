package Utils;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility de chup va luu screenshot.
 */
public class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";

    /**
     * Chup screenshot va luu vao file.
     * @return Duong dan file da luu, hoac null neu loi.
     */
    public static String captureScreenshot(AppiumDriver driver, String testName) {
        if (driver == null) {
            logger.warn("Driver is null, cannot capture screenshot");
            return null;
        }

        try {
            // Tao folder screenshots neu chua co
            File dir = new File(SCREENSHOT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Ten file: testName_yyyyMMdd_HHmmss.png
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;

            // Chup screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Copy file
            Files.copy(screenshot.toPath(), Paths.get(filePath));

            logger.info("Screenshot saved: " + filePath);
            return filePath;

        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error capturing screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Chup screenshot va tra ve duoi dang base64 string.
     * Dung de nhung truc tiep vao ExtentReport (khong can luu file).
     */
    public static String captureScreenshotAsBase64(AppiumDriver driver) {
        if (driver == null) {
            logger.warn("Driver is null, cannot capture screenshot");
            return null;
        }

        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as base64: " + e.getMessage());
            return null;
        }
    }
}