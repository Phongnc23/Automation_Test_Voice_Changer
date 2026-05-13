package testcases.Audiosaved;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.Components.EditMenu;
import Pages.Components.OpenWithBottomSheet;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;


public class AudioSaved04_Verify_Open_With extends BaseTest {

    private AudioSavedPage audioSavedPage;
    private EditMenu editMenu;
    private OpenWithBottomSheet openWithSheet;

    private static final int RECORDING_SECONDS = 1;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupAudioSavedSession() {
        logger.info("=== SETUP AUDIO SAVED SESSION ===");
        try {
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(
                    driver, RECORDING_SECONDS);
            editMenu = new EditMenu(driver);
            openWithSheet = new OpenWithBottomSheet(driver);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(
                    driver, RECORDING_SECONDS);
            editMenu = new EditMenu(driver);
            openWithSheet = new OpenWithBottomSheet(driver);
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        logger.info("=== CLEANUP ===");
        try {
            // Safety: dong sheet/menu neu con mo
            if (openWithSheet != null && openWithSheet.isDisplayed()) {
                openWithSheet.clickCancel();
                Thread.sleep(500);
            }
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    /**
     * Safety net truoc moi test: dong sheet neu lo van mo.
     */
    @BeforeMethod
    public void ensureCleanState() throws InterruptedException {
        if (openWithSheet != null && openWithSheet.isDisplayed()) {
            logger.info("Open With sheet con mo, dong");
            openWithSheet.clickCancel();
            Thread.sleep(500);
        }
    }

    /**
     * Helper: mo Open With sheet (click 3 cham -> Open With).
     */
    private void openOpenWithSheet() throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(800);
        editMenu.clickOpenWith();
        Thread.sleep(2000);
    }

    @Test(priority = 1, description = "SAV_06_01: Mo Open With bottom sheet")
    public void test_SAV_06_01_open_with_bottom_sheet_displayed()
            throws InterruptedException {
        openOpenWithSheet();

        Assert.assertTrue(openWithSheet.isDisplayed(),
                "Open With bottom sheet khong mo");
        ExtentReportManager.getTest().log(Status.PASS, "Bottom sheet mo");

        // Cleanup ngay trong test: Cancel -> ve Audio Saved
        openWithSheet.clickCancel();
        Thread.sleep(800);
    }

    @Test(priority = 2, description = "SAV_06_02: Cancel Open With")
    public void test_SAV_06_02_cancel_open_with()
            throws InterruptedException {
        openOpenWithSheet();

        Assert.assertTrue(openWithSheet.isDisplayed(),
                "Bottom sheet khong mo");

        // Test chinh: click Cancel va verify quay lai
        openWithSheet.clickCancel();
        Thread.sleep(1000);

        Assert.assertFalse(openWithSheet.isDisplayed(),
                "Bottom sheet van con hien thi");
        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong quay lai Audio Saved");
        ExtentReportManager.getTest().log(Status.PASS, "Da quay lai Audio Saved");
    }
}