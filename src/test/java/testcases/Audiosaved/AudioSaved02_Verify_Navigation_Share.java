package testcases.Audiosaved;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.Components.ShareBottomSheet;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

import java.time.Duration;


public class AudioSaved02_Verify_Navigation_Share extends BaseTest {

    private AudioSavedPage audioSavedPage;
    private ShareBottomSheet shareSheet;

    private static final int RECORDING_SECONDS = 1;

    private static final By HOME_RECORD_CARD =
            By.id("com.bluesoftware.voicechanger:id/layout_record");

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupAudioSavedSession() {
        logger.info("=== SETUP AUDIO SAVED SESSION ===");
        try {
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(
                    driver, RECORDING_SECONDS);
            shareSheet = new ShareBottomSheet(driver);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(
                    driver, RECORDING_SECONDS);
            shareSheet = new ShareBottomSheet(driver);
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        logger.info("=== CLEANUP ===");
        try {
            // Safety: dong sheet neu con mo
            if (shareSheet != null && shareSheet.isDisplayed()) {
                shareSheet.clickCancel();
                Thread.sleep(500);
            }
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    /**
     * Safety net + setup lai cho test destructive (Test 01, 02).
     * - Voi test 03-06: dong sheet neu lo van mo
     * - Voi test 01, 02: phai dam bao o Audio Saved (vi test truoc co the
     *   da ve Home)
     */
    @BeforeMethod
    public void ensureCleanState() throws InterruptedException {
        // Dong sheet neu con mo
        if (shareSheet != null && shareSheet.isDisplayed()) {
            logger.info("Bottom sheet con mo, click Huy");
            shareSheet.clickCancel();
            Thread.sleep(500);
        }

        // Neu khong o Audio Saved -> setup lai
        if (!isAtAudioSaved()) {
            logger.info("Khong o Audio Saved, setup lai");
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(
                    driver, RECORDING_SECONDS);
            shareSheet = new ShareBottomSheet(driver);
        }
    }

    private boolean isAtHome() {
        return driver.findElements(HOME_RECORD_CARD).size() > 0;
    }

    private boolean isAtAudioSaved() {
        return audioSavedPage != null && audioSavedPage.isDisplayed();
    }

    // ========================================================
    // GROUP 1: NON-DESTRUCTIVE (share session) - chay TRUOC
    // ========================================================

    @Test(priority = 1, description = "SAV_02_03: Click Share -> mo bottom sheet")
    public void test_SAV_02_03_click_share_opens_bottom_sheet()
            throws InterruptedException {
        audioSavedPage.clickShare();
        // M1: Wait share sheet thay sleep(2000), max 3s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> shareSheet.isDisplayed());
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

        Assert.assertTrue(shareSheet.isDisplayed(),
                "Bottom sheet chia se khong mo");
        ExtentReportManager.getTest().log(Status.PASS, "Bottom sheet da mo");

        // Cleanup ngay: click Huy -> Audio Saved
        shareSheet.clickCancel();
        Thread.sleep(800);

        Assert.assertFalse(shareSheet.isDisplayed(),
                "Bottom sheet KHONG dong sau click Huy");
        ExtentReportManager.getTest().log(Status.PASS, "Da dong bottom sheet");
    }

    @Test(priority = 2, description = "SAV_02_04: Preview file name")
    public void test_SAV_02_04_share_sheet_shows_filename()
            throws InterruptedException {
        String expectedFileName = audioSavedPage.getFileName();

        audioSavedPage.clickShare();
        // M1: Wait share sheet thay sleep(2000), max 3s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> shareSheet.isDisplayed());
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

        Assert.assertTrue(shareSheet.isDisplayed(), "Bottom sheet khong mo");

        String previewName = shareSheet.getPreviewFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Expected: " + expectedFileName + ", Preview: " + previewName);

        Assert.assertNotNull(previewName, "Preview filename null");
        Assert.assertEquals(previewName, expectedFileName,
                "Ten file preview khong khop");
        ExtentReportManager.getTest().log(Status.PASS, "Filename khop");

        // Cleanup
        shareSheet.clickCancel();
        Thread.sleep(800);

        Assert.assertFalse(shareSheet.isDisplayed(), "Sheet KHONG dong");
        ExtentReportManager.getTest().log(Status.PASS, "Da dong sheet");
    }

    @Test(priority = 3, description = "SAV_02_05: Danh sach app")
    public void test_SAV_02_05_share_sheet_lists_apps()
            throws InterruptedException {
        audioSavedPage.clickShare();
        // M1: Wait share sheet thay sleep(2000), max 3s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> shareSheet.isDisplayed());
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

        Assert.assertTrue(shareSheet.isDisplayed(), "Bottom sheet khong mo");

        int totalCount = shareSheet.countAvailableApps();
        ExtentReportManager.getTest().log(Status.INFO,
                "Tong: " + totalCount + " app");

        Assert.assertTrue(totalCount > 0, "Khong co app nao");
        ExtentReportManager.getTest().log(Status.PASS,
                "Co " + totalCount + " app share");

        // Cleanup
        shareSheet.clickCancel();
        Thread.sleep(800);

        Assert.assertFalse(shareSheet.isDisplayed(), "Sheet KHONG dong");
        ExtentReportManager.getTest().log(Status.PASS, "Da dong sheet");
    }

    @Test(priority = 4, description = "SAV_02_06: Click Cancel -> dong sheet")
    public void test_SAV_02_06_cancel_share_bottom_sheet()
            throws InterruptedException {
        audioSavedPage.clickShare();
        // M1: Wait share sheet thay sleep(2000), max 3s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> shareSheet.isDisplayed());
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

        Assert.assertTrue(shareSheet.isDisplayed(), "Bottom sheet khong mo");

        // Test chinh: click Cancel
        shareSheet.clickCancel();
        Thread.sleep(1000);

        Assert.assertFalse(shareSheet.isDisplayed(),
                "Bottom sheet van con hien thi");
        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong quay lai Audio Saved sau Cancel");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da dong bottom sheet, ve Audio Saved");
    }

    // ========================================================
    // GROUP 2: DESTRUCTIVE (ve Home) - chay CUOI
    // ========================================================

    @Test(priority = 10, description = "SAV_02_01: Click Home -> ve Home")
    public void test_SAV_02_01_click_home_navigates_to_home()
            throws InterruptedException {

        audioSavedPage.clickClose();
        // M1: Wait Home thay sleep(1500), max 3s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> isAtHome());
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

        Assert.assertTrue(isAtHome(), "Khong ve Home");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home");
    }

    @Test(priority = 11, description = "SAV_02_02: Click X -> ve Home")
    public void test_SAV_02_02_click_close_navigates_to_home()
            throws InterruptedException {

        audioSavedPage.clickClose();
        // M1: Wait Home thay sleep(1500), max 3s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> isAtHome());
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

        Assert.assertTrue(isAtHome(), "Khong ve Home sau click X");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home qua X");
    }
}