package testcases.DrawerMenu;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.DrawerMenuPage;
import Report.ExtentReportManager;
import Utils.GestureUtils;
import Utils.RecordFlowHelper;

/**
 * DM_03: Verify Rate Us mo CH Play + back (1 test).
 */
public class DrawerMenu03_Verify_Rate_Us extends BaseTest {

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
            for (int i = 1; i <= 5; i++) {
                String pkg = ((AndroidDriver) driver).getCurrentPackage();
                if ("com.bluesoftware.voicechanger".equals(pkg)) break;
                GestureUtils.swipeFromLeftEdgeToBack(driver);
                Thread.sleep(1000);
            }

            String pkgFinal = ((AndroidDriver) driver).getCurrentPackage();
            if (!"com.bluesoftware.voicechanger".equals(pkgFinal)) {
                ((AndroidDriver) driver).activateApp(
                        "com.bluesoftware.voicechanger");
                Thread.sleep(1500);
            }

            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            try {
                RecordFlowHelper.forceResetToHome(driver);
            } catch (Exception ex) {
                logger.error("Force reset failed: " + ex.getMessage());
            }
        }
    }

    @Test(description = "DM_03_01: Click Rate Us mo CH Play va back ve Voice Changer")
    public void test_DM_03_01_rate_us_open_and_back()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        // Click Rate Us
        drawerMenu.clickRateUs();
        Thread.sleep(4000);

        // Verify mo external app
        String pkgPlayStore = drawerMenu.getCurrentPackage();
        ExtentReportManager.getTest().log(Status.INFO,
                "Package sau click: " + pkgPlayStore);

        Assert.assertNotNull(pkgPlayStore, "Khong lay duoc package");
        Assert.assertNotEquals(pkgPlayStore, "com.bluesoftware.voicechanger",
                "Khong mo external app");

        if ("com.android.vending".equals(pkgPlayStore)) {
            ExtentReportManager.getTest().log(Status.INFO,
                    "Mo CH Play: " + pkgPlayStore);
        } else {
            ExtentReportManager.getTest().log(Status.INFO,
                    "Mo external app: " + pkgPlayStore);
        }

        // Swipe back
        int swipeCount = 0;
        for (int i = 1; i <= 5; i++) {
            String pkg = drawerMenu.getCurrentPackage();
            if ("com.bluesoftware.voicechanger".equals(pkg)) {
                ExtentReportManager.getTest().log(Status.INFO,
                        "Da ve Voice Changer sau " + (i - 1) + " lan swipe");
                break;
            }
            GestureUtils.swipeFromLeftEdgeToBack(driver);
            Thread.sleep(1200);
            swipeCount++;
        }

        // Verify ve Voice Changer
        String pkgAfter = drawerMenu.getCurrentPackage();
        Assert.assertEquals(pkgAfter, "com.bluesoftware.voicechanger",
                "Khong ve Voice Changer sau " + swipeCount + " lan swipe");

        ExtentReportManager.getTest().log(Status.PASS,
                "Mo CH Play + back ve Voice Changer thanh cong");
    }
}