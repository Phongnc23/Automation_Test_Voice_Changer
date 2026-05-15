package testcases.DrawerMenu;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.DrawerMenuPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * DM_05: Verify Feedback mo email chooser (3 tests).
 *
 * DOM: com.android.intentresolver chooser voi Gmail, Outlook, etc.
 */
public class DrawerMenu05_Verify_Feedback extends BaseTest {

    private static final By FEEDBACK_CANCEL =
            By.id("com.android.intentresolver:id/oplus_resolve_close_icon");
    private static final By FEEDBACK_APP_ITEMS =
            By.id("com.android.intentresolver:id/resolver_item_layout");

    private DrawerMenuPage drawerMenu;

    @BeforeMethod
    public void navigateToScreen() {
        try {
            RecordFlowHelper.smartResetToHome(driver);
            Thread.sleep(800);
            drawerMenu = RecordFlowHelper.openDrawer(driver);
        } catch (Exception e) {
            logger.error("Navigate error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            drawerMenu = RecordFlowHelper.openDrawer(driver);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void resetState() {
        try {
            if (driver.findElements(FEEDBACK_CANCEL).size() > 0) {
                driver.findElement(FEEDBACK_CANCEL).click();
                Thread.sleep(1500);
            }
            ((AndroidDriver) driver).activateApp(
                    "com.bluesoftware.voicechanger");
            Thread.sleep(1500);
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            try {
                RecordFlowHelper.forceResetToHome(driver);
            } catch (Exception ex) {
                logger.error("Force reset failed: " + ex.getMessage());
            }
        }
    }

    @Test(priority = 1, description = "DM_05_01: Click Feedback -> mo email chooser")
    public void test_DM_05_01_feedback_opens_chooser()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        drawerMenu.clickFeedback();
        Thread.sleep(3000);

        // Verify chooser mo
        boolean chooserVisible =
                driver.findElements(FEEDBACK_CANCEL).size() > 0;
        Assert.assertTrue(chooserVisible,
                "Email chooser khong mo");

        ExtentReportManager.getTest().log(Status.PASS,
                "Email chooser mo thanh cong");
    }

    @Test(priority = 2, description = "DM_05_02: Verify co email app trong chooser")
    public void test_DM_05_02_email_apps_displayed()
            throws InterruptedException {
        drawerMenu.clickFeedback();
        Thread.sleep(3000);

        int appCount = driver.findElements(FEEDBACK_APP_ITEMS).size();
        ExtentReportManager.getTest().log(Status.INFO,
                "So email app: " + appCount);

        Assert.assertTrue(appCount > 0,
                "Phai co toi thieu 1 email app");

        ExtentReportManager.getTest().log(Status.PASS,
                "Co " + appCount + " email app");
    }

    @Test(priority = 3, description = "DM_05_03: Cancel chooser ve Voice Changer")
    public void test_DM_05_03_cancel_chooser()
            throws InterruptedException {
        drawerMenu.clickFeedback();
        Thread.sleep(3000);

        Assert.assertTrue(driver.findElements(FEEDBACK_CANCEL).size() > 0,
                "Chooser khong mo");

        driver.findElement(FEEDBACK_CANCEL).click();
        Thread.sleep(2000);

        // Verify chooser dong
        boolean closed = driver.findElements(FEEDBACK_CANCEL).size() == 0;
        Assert.assertTrue(closed, "Chooser khong dong sau Cancel");

        String pkgAfter = drawerMenu.getCurrentPackage();
        Assert.assertEquals(pkgAfter, "com.bluesoftware.voicechanger",
                "Khong ve Voice Changer");

        ExtentReportManager.getTest().log(Status.PASS,
                "Cancel hoat dong dung");
    }
}