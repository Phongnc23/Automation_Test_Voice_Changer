package Base;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import Constants.AppConstants;
import Driver.SuiteDriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import Report.ExtentReportManager;
import Utils.AdbHelper;
import Utils.RecordFlowHelper;
import Utils.ScreenshotUtils;

public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected AppiumDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws InterruptedException {
        logger.info("SETUP: " + this.getClass().getSimpleName());

        // L2: Lay driver tu suite-level singleton (tao 1 lan, tai su dung cho moi class).
        driver = SuiteDriverManager.getOrCreateDriver();

        if (driver != null) {
            try {
                // 1. Bring app to foreground neu khong o Voice Changer
                if (!RecordFlowHelper.isAtVoiceChanger(driver)) {
                    ((AndroidDriver) driver).activateApp(AppConstants.APP_PACKAGE);
                    Thread.sleep(500);
                }

                // 2. Dam bao app o Home truoc khi class chay - clean state.
                // smartResetToHome cheap (~50ms) khi da o Home, dat nhung dam bao
                // an toan khi class truoc roi app o sub-screen (vd Settings, MyAudio).
                // Cross-class screen share van duoc thuc hien qua idempotent
                // navigate methods trong RecordFlowHelper (vd navigateToSettings
                // detect dang o Settings -> return ngay).
                RecordFlowHelper.smartResetToHome(driver);

                // 3. Verify reached Home; neu khong, force reset (terminate + relaunch)
                if (!RecordFlowHelper.isAtHome(driver)) {
                    logger.warn("Khong ve duoc Home sau smart reset, force reset");
                    resetAppToFreshState();
                }
            } catch (Exception e) {
                logger.warn("Setup error, try force reset: " + e.getMessage());
                resetAppToFreshState();
            }
        }
        logger.info("Driver ready, app o Home cho "
                + this.getClass().getSimpleName());
    }

    /**
     * Chay TRUOC moi @Test method (truoc ca @BeforeMethod cua subclass).
     * Dismiss heads-up notification + collapse status bar de overlay
     * (Teams, Zalo, ...) khong lam sai lech UiAutomator2 dump UI tree.
     */
    @BeforeMethod(alwaysRun = true)
    public void dismissPendingNotifications() {
        if (driver == null) return;
        AdbHelper.dismissNotifications();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        logger.info("TEARDOWN: " + this.getClass().getSimpleName());
        // L2: KHONG quit driver - SuiteListener.onFinish lo. Cung KHONG terminate
        // app de class tiep theo co the reuse screen state neu cung target.
        // Neu test class can clean state, no se goi resetAppToFreshState() rieng.
    }

    /**
     * Reset app ve trang thai fresh (terminate + activate).
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
