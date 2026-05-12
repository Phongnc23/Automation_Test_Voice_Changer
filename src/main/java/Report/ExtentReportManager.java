package Report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import Constants.AppConstants;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private static final String TIMESTAMP =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    private static final String REPORT_DIR = "test-output/";
    private static final String REPORT_PATH = REPORT_DIR + "ExtentReport_" + TIMESTAMP + ".html";
    private static final String SCREENSHOT_DIR = REPORT_DIR + "screenshots/";

    /**
     * Khoi tao ExtentReports.
     * Goi 1 lan duy nhat khi suite bat dau.
     */
    public static void initializeExtentReports() {
        // Tao folder report neu chua co
        File reportDir = new File(REPORT_DIR);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Voice Changer Automation Report");
        sparkReporter.config().setReportName("Voice Changer Test Results");
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");
        sparkReporter.config().setEncoding("utf-8");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // System info
        extent.setSystemInfo("Application", "Voice Changer");
        extent.setSystemInfo("App Package", AppConstants.APP_PACKAGE);
        extent.setSystemInfo("Platform",
                AppConstants.PLATFORM_NAME + " " + AppConstants.PLATFORM_VERSION);
        extent.setSystemInfo("Device", AppConstants.DEVICE_NAME);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("User", System.getProperty("user.name"));

        logger.info("ExtentReport initialized: " + REPORT_PATH);
    }

    /**
     * Tao test mới
     */
    public static void createTest(String testName, String description) {
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
    }

    public static void createTest(String testName) {
        createTest(testName, "");
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    // ====== LOG METHODS ======

    public static void info(String msg) {
        if (getTest() != null) getTest().info(msg);
    }

    public static void pass(String msg) {
        if (getTest() != null) getTest().pass(msg);
    }

    public static void fail(String msg) {
        if (getTest() != null) getTest().fail(msg);
    }

    public static void skip(String msg) {
        if (getTest() != null) getTest().skip(msg);
    }

    // ====== SCREENSHOT METHOD ======

    /**
     * Chup screenshot va dinh kem vao Extent Report.
     * - Luu file png ra folder screenshots/
     * - Nhung anh vao bao cao HTML qua duong dan tuong doi
     *
     * @param driver AppiumDriver
     * @param testName Ten test (de dat ten file)
     */
    public static void captureScreenshot(AppiumDriver driver, String testName) {
        if (driver == null) {
            logger.warn("Driver is null, cannot capture screenshot");
            return;
        }

        try {
            // Tao folder screenshots neu chua co
            File destFolder = new File(SCREENSHOT_DIR);
            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }

            // Chup screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            // Dat ten file
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            File destFile = new File(SCREENSHOT_DIR + fileName);

            // Copy file
            Files.copy(sourceFile.toPath(), destFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            // Duong dan tuong doi de hien thi trong HTML
            String relativePath = "screenshots/" + fileName;

            // Dinh kem vao Extent Report
            if (getTest() != null) {
                getTest().fail("Screenshot captured at failure",
                        MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());
            }

            logger.info("Screenshot saved: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error capturing screenshot: " + e.getMessage());
        }
    }

    /**
     * Dinh kem screenshot dang base64 (nhung truc tiep, khong can file ngoai).
     * Su dung khi muon report self-contained.
     */
    public static void captureScreenshotBase64(AppiumDriver driver, String message) {
        if (driver == null || getTest() == null) return;

        try {
            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            getTest().log(Status.INFO, message,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
        } catch (Exception e) {
            logger.warn("Cannot capture base64 screenshot: " + e.getMessage());
        }
    }

    /**
     * Ghi file HTML report. BAT BUOC GOI KHI KET THUC SUITE!
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            logger.info("Report saved at: " + new File(REPORT_PATH).getAbsolutePath());
        } else {
            logger.warn("Extent is null, cannot flush. Did you call initializeExtentReports()?");
        }
    }

    /**
     * Lay duong dan file report
     */
    public static String getReportPath() {
        return REPORT_PATH;
    }
}