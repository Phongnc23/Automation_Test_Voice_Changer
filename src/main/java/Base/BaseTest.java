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

        logger.info("Current package: " + getCurrentPackage());
    }

    @AfterClass
    public void tearDown() {
        logger.info("TEARDOWN: " + this.getClass().getSimpleName());

        if (driver != null) {
            try {
                if (driver instanceof AndroidDriver) {
                    ((AndroidDriver) driver).terminateApp(AppConstants.APP_PACKAGE);
                }
            } catch (Exception e) {
                logger.warn("Cannot terminate app: " + e.getMessage());
            }
        }

        if (driverManager != null) {
            driverManager.quitDriver();
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