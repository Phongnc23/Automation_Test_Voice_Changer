package testcases.Voiceeffects;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * Test Volume control (DOM moi).
 * Volume block hien thi DONG: ngay duoi row chua effect dang chon.
 *
 * Pattern: share session - reset volume/effect ve mac dinh giua cac test
 * de moi test idempotent.
 */
public class VoiceEffects06_Verify_Volume extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void resetBeforeClass() {
        resetAppToFreshState();
    }

    @BeforeClass(dependsOnMethods = "resetBeforeClass")
    public void setupVoiceEffectsSession() {
        logger.info("=== SETUP VOICE EFFECTS SESSION (Volume) ===");
        try {
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 3);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 3);
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        logger.info("=== CLEANUP ===");
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    @Test(description = "VE_07_01: Verify Volume section hien thi (label/seek/value)",
            priority = 1)
    public void test_VE_07_01_verify_volume_section_displayed() {
        Assert.assertTrue(voiceEffectsPage.isVolumeLabelDisplayed(),
                "Label 'Volume' khong hien thi");
        Assert.assertTrue(voiceEffectsPage.isVolumeResetButtonDisplayed(),
                "Nut Reset Volume khong hien thi");
        Assert.assertTrue(voiceEffectsPage.isVolumeSeekBarDisplayed(),
                "Volume SeekBar khong hien thi");
        Assert.assertTrue(voiceEffectsPage.isVolumeValueTextDisplayed(),
                "Text gia tri Volume khong hien thi");

        ExtentReportManager.getTest().log(Status.PASS,
                "Volume section day du 4 element");
    }

    @Test(description = "VE_07_02: Verify Volume default value = 100",
            priority = 2)
    public void test_VE_07_02_verify_volume_default_value() {
        String value = voiceEffectsPage.getVolumeValue();
        ExtentReportManager.getTest().log(Status.INFO,
                "Volume value: " + value);

        Assert.assertEquals(value, "100",
                "Volume default phai = 100, found: " + value);
        ExtentReportManager.getTest().log(Status.PASS,
                "Volume default = 100");
    }

    @Test(description = "VE_07_03: Drag Volume seekbar -> gia tri thay doi",
            priority = 3)
    public void test_VE_07_03_drag_volume_changes_value() throws InterruptedException {
        String before = voiceEffectsPage.getVolumeValue();
        ExtentReportManager.getTest().log(Status.INFO,
                "Volume truoc khi drag: " + before);

        voiceEffectsPage.dragVolumeLeft();
        Thread.sleep(500);

        String after = voiceEffectsPage.getVolumeValue();
        ExtentReportManager.getTest().log(Status.INFO,
                "Volume sau drag: " + after);

        Assert.assertNotEquals(after, before,
                "Volume khong thay doi sau drag. before=" + before + ", after=" + after);
        ExtentReportManager.getTest().log(Status.PASS,
                "Volume da thay doi: " + before + " -> " + after);

        // Reset ve mac dinh cho test sau
        voiceEffectsPage.clickResetVolume();
        Thread.sleep(500);
    }

    @Test(description = "VE_07_04: Click Reset Volume -> ve 100",
            priority = 4)
    public void test_VE_07_04_reset_volume_button() throws InterruptedException {
        // B1: Drag de Volume khac 100
        voiceEffectsPage.dragVolumeLeft();
        Thread.sleep(500);
        String afterDrag = voiceEffectsPage.getVolumeValue();
        ExtentReportManager.getTest().log(Status.INFO,
                "Volume sau drag: " + afterDrag);
        Assert.assertNotEquals(afterDrag, "100",
                "Drag chua lam Volume thay doi - test setup loi");

        // B2: Click Reset
        voiceEffectsPage.clickResetVolume();
        Thread.sleep(500);

        String afterReset = voiceEffectsPage.getVolumeValue();
        ExtentReportManager.getTest().log(Status.INFO,
                "Volume sau reset: " + afterReset);

        Assert.assertEquals(afterReset, "100",
                "Volume sau reset phai = 100, found: " + afterReset);
        ExtentReportManager.getTest().log(Status.PASS,
                "Reset thanh cong: " + afterDrag + " -> " + afterReset);
    }

    @Test(description = "VE_07_05: Volume block DI CHUYEN khi chon effect khac",
            priority = 5)
    public void test_VE_07_05_volume_moves_with_selected_effect()
            throws InterruptedException {
        // Dam bao state khoi dau: Normal duoc chon
        voiceEffectsPage.clickEffect("Normal");
        Thread.sleep(800);

        int initialY = voiceEffectsPage.getVolumeSeekBarY();
        ExtentReportManager.getTest().log(Status.INFO,
                "Volume Y khi chon Normal: " + initialY);
        Assert.assertTrue(initialY > 0,
                "Khong lay duoc Y position cua seekVolume");

        // Click Tenor - nam o row 1 (sau Normal/Man/Woman) -> Volume se move xuong
        voiceEffectsPage.clickEffect("Tenor");
        Thread.sleep(800);

        int newY = voiceEffectsPage.getVolumeSeekBarY();
        ExtentReportManager.getTest().log(Status.INFO,
                "Volume Y khi chon Tenor: " + newY);

        Assert.assertTrue(newY > initialY,
                "Volume block KHONG di chuyen xuong sau khi chon Tenor. "
                        + "InitialY=" + initialY + ", NewY=" + newY);
        ExtentReportManager.getTest().log(Status.PASS,
                "Volume block di chuyen: Y=" + initialY + " -> Y=" + newY);

        // Cleanup: ve Normal cho clean state
        voiceEffectsPage.clickEffect("Normal");
    }
}
