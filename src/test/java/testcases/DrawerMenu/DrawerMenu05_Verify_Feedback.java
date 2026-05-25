package testcases.DrawerMenu;

import Base.BaseSharedSessionTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.Components.IntentResolverBottomSheet;
import Pages.DrawerMenuPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

import java.time.Duration;

/**
 * DM_05: Verify Feedback mo email chooser (3 tests).
 *
 * DOM: com.android.intentresolver chooser voi Gmail, Outlook, etc.
 */
public class DrawerMenu05_Verify_Feedback extends BaseSharedSessionTest {

    private DrawerMenuPage drawerMenu;
    private IntentResolverBottomSheet sheet;

    @Override
    protected void navigateToScreen() {
        RecordFlowHelper.smartResetToHome(driver);
        drawerMenu = RecordFlowHelper.openDrawer(driver);
        sheet = new IntentResolverBottomSheet(driver);
    }

    @Override
    protected boolean isAtExpectedScreen() {
        return RecordFlowHelper.isDrawerOpen(driver);
    }

    @Test(priority = 1, description = "DM_05_01: Click Feedback -> mo email chooser")
    public void test_DM_05_01_feedback_opens_chooser()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        drawerMenu.clickFeedback();
        // M2: Wait email chooser xuat hien thay sleep(3000) co dinh, max 4s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            IntentResolverBottomSheet.getCancelButtonLocator()));
        } catch (Exception e) {
            // Timeout
        }

        Assert.assertTrue(sheet.isDisplayed(),
                "Email chooser khong mo");

        ExtentReportManager.getTest().log(Status.PASS,
                "Email chooser mo thanh cong");
    }

    @Test(priority = 2, description = "DM_05_02: Verify co email app trong chooser")
    public void test_DM_05_02_email_apps_displayed()
            throws InterruptedException {
        drawerMenu.clickFeedback();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            IntentResolverBottomSheet.getCancelButtonLocator()));
        } catch (Exception e) {
            // Timeout
        }

        int appCount = sheet.countApps();
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
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            IntentResolverBottomSheet.getCancelButtonLocator()));
        } catch (Exception e) {
            // Timeout
        }

        Assert.assertTrue(sheet.isDisplayed(),
                "Chooser khong mo");

        sheet.clickCancel();
        Thread.sleep(2000);

        // Verify chooser dong
        Assert.assertFalse(sheet.isDisplayed(),
                "Chooser khong dong sau Cancel");

        String pkgAfter = drawerMenu.getCurrentPackage();
        Assert.assertEquals(pkgAfter, "com.bluesoftware.voicechanger",
                "Khong ve Voice Changer");

        ExtentReportManager.getTest().log(Status.PASS,
                "Cancel hoat dong dung");
    }
}
