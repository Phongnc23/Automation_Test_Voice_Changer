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
 * DM_02: Verify Privacy Policy mo browser + back (1 test).
 */
public class DrawerMenu02_Verify_Privacy_Policy extends BaseTest {  

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
            // Swipe back ra khoi browser (toi da 5 lan)
            for (int i = 1; i <= 5; i++) {
                String pkg = ((AndroidDriver) driver).getCurrentPackage();
                if ("com.bluesoftware.voicechanger".equals(pkg)) break;
                GestureUtils.swipeFromLeftEdgeToBack(driver);
                Thread.sleep(1000);
            }

            // Fallback activate neu swipe khong work
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

    @Test(description = "DM_02_01: Click Privacy Policy mo browser va back ve Voice Changer")
    public void test_DM_02_01_privacy_policy_open_and_back()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        // === Buoc 1: Click Privacy Policy ===
        String pkgBefore = drawerMenu.getCurrentPackage();
        ExtentReportManager.getTest().log(Status.INFO,
                "Package truoc click: " + pkgBefore);

        drawerMenu.clickPrivacy();
        Thread.sleep(4000);  // Cho browser mo

        // === Buoc 2: Verify mo external app ===
        String pkgBrowser = drawerMenu.getCurrentPackage();
        ExtentReportManager.getTest().log(Status.INFO,
                "Package sau click: " + pkgBrowser);

        Assert.assertNotNull(pkgBrowser, "Khong lay duoc package");
        Assert.assertNotEquals(pkgBrowser, "com.bluesoftware.voicechanger",
                "Khong mo external app (van o Voice Changer)");
        ExtentReportManager.getTest().log(Status.INFO,
                "Browser mo: " + pkgBrowser);

        // === Buoc 3: Swipe back ra khoi browser ===
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

        // === Buoc 4: Verify ve Voice Changer ===
        String pkgAfter = drawerMenu.getCurrentPackage();
        Assert.assertEquals(pkgAfter, "com.bluesoftware.voicechanger",
                "Khong ve Voice Changer sau " + swipeCount + " lan swipe");

        ExtentReportManager.getTest().log(Status.PASS,
                "Mo browser + back ve Voice Changer thanh cong");
    }
}