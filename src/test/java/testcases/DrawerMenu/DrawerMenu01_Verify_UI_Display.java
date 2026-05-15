package testcases.DrawerMenu;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.DrawerMenuPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * DM_01: Verify Drawer Menu UI (3 tests).
 */
public class DrawerMenu01_Verify_UI_Display extends BaseTest {

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
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            Thread.sleep(800);
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            try {
                RecordFlowHelper.forceResetToHome(driver);
            } catch (Exception ex) {
                logger.error("Force reset failed: " + ex.getMessage());
            }
        }
    }

    @Test(priority = 1, description = "DM_01_01: Mo drawer menu tu Home")
    public void test_DM_01_01_open_drawer() {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer menu khong mo");
        ExtentReportManager.getTest().log(Status.PASS,
                "Drawer menu mo thanh cong");
    }

    @Test(priority = 2, description = "DM_01_02: Verify cac action trong drawer")
    public void test_DM_01_02_verify_all_actions() {
        Assert.assertTrue(drawerMenu.isPrivacyDisplayed(),
                "Thieu Privacy policy");
        Assert.assertTrue(drawerMenu.isRateUsDisplayed(),
                "Thieu Rate Us");
        Assert.assertTrue(drawerMenu.isShareAppDisplayed(),
                "Thieu Share app");
        Assert.assertTrue(drawerMenu.isFeedbackDisplayed(),
                "Thieu Feedback");
        Assert.assertTrue(drawerMenu.isSettingsDisplayed(),
                "Thieu Settings");
        Assert.assertTrue(drawerMenu.isVersionDisplayed(),
                "Thieu Version");
        Assert.assertTrue(drawerMenu.isExitDisplayed(),
                "Thieu Exit app");

        ExtentReportManager.getTest().log(Status.PASS,
                "Du 7 action trong drawer");
    }

    @Test(priority = 3, description = "DM_01_03: Dong drawer bang BACK")
    public void test_DM_01_03_close_drawer_by_back()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        Thread.sleep(1500);

        // Verify drawer dong - khong con Privacy, Settings hien thi
        Assert.assertFalse(drawerMenu.isDisplayed(),
                "Drawer van mo sau BACK");

        // Verify ve Home
        boolean atHome = driver.findElements(
                org.openqa.selenium.By.id(
                        "com.bluesoftware.voicechanger:id/layout_record")
        ).size() > 0;
        Assert.assertTrue(atHome, "Khong ve Home");
        ExtentReportManager.getTest().log(Status.PASS,
                "Dong drawer thanh cong");
    }
}