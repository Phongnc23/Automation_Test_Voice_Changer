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
 * DM_04: Verify Share App mo share bottom sheet (3 tests).
 *
 * DOM: com.android.intentresolver chooser voi list app + preview text.
 */
public class DrawerMenu04_Verify_Share_App extends BaseTest {

    private static final By SHARE_CANCEL =
            By.id("com.android.intentresolver:id/oplus_resolve_close_icon");
    private static final By SHARE_PREVIEW_TEXT =
            By.id("android:id/content_preview_text");
    private static final By SHARE_APP_ITEMS =
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
            // Click Huy neu share sheet con mo
            if (driver.findElements(SHARE_CANCEL).size() > 0) {
                driver.findElement(SHARE_CANCEL).click();
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

    @Test(priority = 1, description = "DM_04_01: Click Share App -> mo share sheet")
    public void test_DM_04_01_share_opens_bottom_sheet()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        drawerMenu.clickShareApp();
        Thread.sleep(3000);

        // Verify share sheet mo
        boolean shareSheetVisible =
                driver.findElements(SHARE_CANCEL).size() > 0;
        Assert.assertTrue(shareSheetVisible,
                "Share bottom sheet khong mo");

        ExtentReportManager.getTest().log(Status.PASS,
                "Share sheet mo thanh cong");
    }

    @Test(priority = 2, description = "DM_04_02: Verify content preview text")
    public void test_DM_04_02_verify_preview_text()
            throws InterruptedException {
        drawerMenu.clickShareApp();
        Thread.sleep(3000);

        // Verify preview text
        Assert.assertTrue(driver.findElements(SHARE_PREVIEW_TEXT).size() > 0,
                "Khong co preview text");

        String previewText = driver.findElement(SHARE_PREVIEW_TEXT).getText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Preview text: " + previewText);

        Assert.assertNotNull(previewText, "Preview text null");
        Assert.assertTrue(previewText.toLowerCase().contains("voice changer"),
                "Preview khong chua 'Voice Changer': " + previewText);
        Assert.assertTrue(previewText.contains("https://"),
                "Preview khong co URL: " + previewText);

        ExtentReportManager.getTest().log(Status.PASS,
                "Preview text dung format");
    }

    @Test(priority = 3, description = "DM_04_03: Verify danh sach app share + Cancel")
    public void test_DM_04_03_apps_list_and_cancel()
            throws InterruptedException {
        drawerMenu.clickShareApp();
        Thread.sleep(3000);

        // Verify co app share
        int appCount = driver.findElements(SHARE_APP_ITEMS).size();
        ExtentReportManager.getTest().log(Status.INFO,
                "So app trong share sheet: " + appCount);
        Assert.assertTrue(appCount > 0,
                "Phai co toi thieu 1 app de share");

        // Click Huy
        driver.findElement(SHARE_CANCEL).click();
        Thread.sleep(2000);

        // Verify sheet dong, ve Voice Changer
        boolean sheetClosed = driver.findElements(SHARE_CANCEL).size() == 0;
        Assert.assertTrue(sheetClosed, "Share sheet khong dong sau Cancel");

        String pkgAfter = drawerMenu.getCurrentPackage();
        Assert.assertEquals(pkgAfter, "com.bluesoftware.voicechanger",
                "Khong ve Voice Changer");

        ExtentReportManager.getTest().log(Status.PASS,
                "Co " + appCount + " app, Cancel hoat dong dung");
    }
}