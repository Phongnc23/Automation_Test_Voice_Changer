package testcases.DrawerMenu;

import Base.BaseSharedSessionTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.DrawerMenuPage;
import Report.ExtentReportManager;
import Utils.GestureUtils;
import Utils.RecordFlowHelper;

import java.time.Duration;

/**
 * DM_02: Verify Privacy Policy mo browser + back (1 test).
 */
public class DrawerMenu02_Verify_Privacy_Policy extends BaseSharedSessionTest {

    private DrawerMenuPage drawerMenu;

    @Override
    protected void navigateToScreen() {
        RecordFlowHelper.smartResetToHome(driver);
        drawerMenu = RecordFlowHelper.openDrawer(driver);
    }

    @Override
    protected boolean isAtExpectedScreen() {
        return RecordFlowHelper.isDrawerOpen(driver);
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
        // M2: Wait package doi (browser mo) thay sleep(4000) co dinh, max 5s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> !"com.bluesoftware.voicechanger".equals(
                            ((AndroidDriver) d).getCurrentPackage()));
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi voi context day du
        }

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
