package Base;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import Constants.AppConstants;
import Driver.DriverFactory;
import Driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import Report.ExtentReportManager;
import Utils.ScreenshotUtils;

public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected AppiumDriver driver;
    protected DriverManager driverManager;

    @BeforeClass
    public void setUp() {
        logger.info("SETUP: " + this.getClass().getSimpleName());

        driverManager = DriverFactory.getDriverManager(AppConstants.PLATFORM_NAME);
        driver = driverManager.createDriver();

        // KHONG terminate+activate o day vi:
        // - createDriver() da mo app fresh
        // - Test class nao can reset them thi tu goi resetAppToFreshState()
        logger.info("Driver created, app launched fresh");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        logger.info("TEARDOWN: " + this.getClass().getSimpleName());

        // Buoc 1: Terminate app de dam bao app khong con chay tren device
        if (driver != null) {
            try {
                ((AndroidDriver) driver).terminateApp(AppConstants.APP_PACKAGE);
                Thread.sleep(800);
                logger.info("App terminated");
            } catch (Exception e) {
                logger.warn("Cannot terminate app: " + e.getMessage());
            }
        }

        // Buoc 2: Quit driver
        if (driverManager != null) {
            try {
                driverManager.quitDriver();
                logger.info("Driver quit completed");
            } catch (Exception e) {
                logger.warn("Error quitting driver: " + e.getMessage());
            }
        }
    }

    /**
     * Reset app ve trang thai fresh.
     * Goi method nay trong @BeforeClass cua test class can app fresh:
     * - Record (ghi am, state phuc tap)
     * - Voice Effects, Audio Saved (sub-screens)
     *
     * KHONG can goi neu test class chi test Home (cap nhat truc tiep).
     */
    protected void resetAppToFreshState() {
        if (driver == null) return;

        try {
            logger.info("Reset app to fresh state");
            ((AndroidDriver) driver).terminateApp(AppConstants.APP_PACKAGE);
            Thread.sleep(1000);
            ((AndroidDriver) driver).activateApp(AppConstants.APP_PACKAGE);
            Thread.sleep(1500);
            logger.info("App is fresh");
        } catch (Exception e) {
            logger.warn("Cannot reset app: " + e.getMessage());
        }
    }

    protected String getCurrentPackage() {
        if (driver instanceof AndroidDriver) {
            return ((AndroidDriver) driver).getCurrentPackage();
        }
        return null;
    }

    protected void attachScreenshot(String message) {
        try {
            String base64 = ScreenshotUtils.captureScreenshotAsBase64(driver);
            if (base64 != null && ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.INFO, message,
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            }
        } catch (Exception e) {
            logger.warn("Cannot attach screenshot: " + e.getMessage());
        }
    }
}