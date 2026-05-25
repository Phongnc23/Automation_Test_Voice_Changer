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
 * DM_04: Verify Share App mo share bottom sheet (3 tests).
 *
 * DOM: com.android.intentresolver chooser voi list app + preview text.
 */
public class DrawerMenu04_Verify_Share_App extends BaseSharedSessionTest {

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

    @Test(priority = 1, description = "DM_04_01: Click Share App -> mo share sheet")
    public void test_DM_04_01_share_opens_bottom_sheet()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        drawerMenu.clickShareApp();
        // M2: Wait share sheet xuat hien thay sleep(3000) co dinh, max 4s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            IntentResolverBottomSheet.getCancelButtonLocator()));
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

        Assert.assertTrue(sheet.isDisplayed(),
                "Share bottom sheet khong mo");

        ExtentReportManager.getTest().log(Status.PASS,
                "Share sheet mo thanh cong");
    }

    @Test(priority = 2, description = "DM_04_02: Verify content preview text")
    public void test_DM_04_02_verify_preview_text()
            throws InterruptedException {
        drawerMenu.clickShareApp();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            IntentResolverBottomSheet.getCancelButtonLocator()));
        } catch (Exception e) {
            // Timeout
        }

        // Verify preview text
        Assert.assertTrue(sheet.isPreviewTextDisplayed(),
                "Khong co preview text");

        String previewText = sheet.getPreviewText();
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
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            IntentResolverBottomSheet.getCancelButtonLocator()));
        } catch (Exception e) {
            // Timeout
        }

        // Verify co app share
        int appCount = sheet.countApps();
        ExtentReportManager.getTest().log(Status.INFO,
                "So app trong share sheet: " + appCount);
        Assert.assertTrue(appCount > 0,
                "Phai co toi thieu 1 app de share");

        // Click Huy
        sheet.clickCancel();
        Thread.sleep(2000);

        // Verify sheet dong, ve Voice Changer
        Assert.assertFalse(sheet.isDisplayed(),
                "Share sheet khong dong sau Cancel");

        String pkgAfter = drawerMenu.getCurrentPackage();
        Assert.assertEquals(pkgAfter, "com.bluesoftware.voicechanger",
                "Khong ve Voice Changer");

        ExtentReportManager.getTest().log(Status.PASS,
                "Co " + appCount + " app, Cancel hoat dong dung");
    }
}
