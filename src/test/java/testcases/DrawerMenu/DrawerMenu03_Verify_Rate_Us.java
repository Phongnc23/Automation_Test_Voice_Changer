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
 * DM_03: Verify Rate Us mo CH Play + back (1 test).
 */
public class DrawerMenu03_Verify_Rate_Us extends BaseSharedSessionTest {

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

    @Test(description = "DM_03_01: Click Rate Us mo CH Play va back ve Voice Changer")
    public void test_DM_03_01_rate_us_open_and_back()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        // Click Rate Us
        drawerMenu.clickRateUs();
        // M2: Wait package doi (CH Play mo) thay sleep(4000) co dinh, max 5s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> !"com.bluesoftware.voicechanger".equals(
                            ((AndroidDriver) d).getCurrentPackage()));
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

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
