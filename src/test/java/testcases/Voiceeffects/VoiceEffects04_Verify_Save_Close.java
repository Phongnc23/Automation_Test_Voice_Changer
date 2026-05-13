package testcases.Voiceeffects;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * Pattern: Reset moi test vi Save/Discard se roi khoi Voice Effects.
 * Khong share state duoc.
 */
public class VoiceEffects04_Verify_Save_Close extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;

//    @BeforeClass(dependsOnMethods = "setUp")
//    public void resetBeforeClass() {
//        resetAppToFreshState();
//    }

    @BeforeMethod
    public void navigateToScreen() {
        try {
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 3);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 3);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void resetState() {
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
            try {
                RecordFlowHelper.forceResetToHome(driver);
            } catch (Exception ex) {
                logger.error("Force reset failed: " + ex.getMessage());
            }
        }
    }

    @Test(description = "VE_04_01: Save voi effect Normal")
    public void test_VE_04_01_save_with_normal_effect() throws InterruptedException {
        voiceEffectsPage.clickSave();
        Thread.sleep(1000);

        AudioSavedPage saved = new AudioSavedPage(driver);
        Assert.assertTrue(saved.isDisplayed(), "Khong chuyen sang Audio Saved");
        ExtentReportManager.getTest().log(Status.PASS, "Da luu va vao Audio Saved");
    }

    @Test(description = "VE_04_02: Save voi effect Robot")
    public void test_VE_04_02_save_with_robot_effect() throws InterruptedException {
        voiceEffectsPage.clickEffect("Robot");
        Thread.sleep(1000);
        voiceEffectsPage.clickSave();
        Thread.sleep(1000);

        AudioSavedPage saved = new AudioSavedPage(driver);
        Assert.assertTrue(saved.isDisplayed(), "Khong chuyen sang Audio Saved");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da luu voi Robot: " + saved.getFileName());
    }

    @Test(description = "VE_04_03: Ten file sau Save dung format")
    public void test_VE_04_03_saved_file_name_format() throws InterruptedException {
        voiceEffectsPage.clickSave();
        Thread.sleep(1000);

        AudioSavedPage saved = new AudioSavedPage(driver);
        String fileName = saved.getFileName();
        ExtentReportManager.getTest().log(Status.INFO, "File: " + fileName);

        Assert.assertNotNull(fileName);
        Assert.assertTrue(fileName.matches("record_\\d+\\.m4a"),
                "Ten file khong dung format: " + fileName);
        ExtentReportManager.getTest().log(Status.PASS, "Format dung");
    }

    @Test(description = "VE_05_01: Nhan X hien dialog Discard")
    public void test_VE_05_01_close_shows_discard_dialog() throws InterruptedException {
        voiceEffectsPage.clickClose();
        Thread.sleep(1000);

        Assert.assertTrue(voiceEffectsPage.isDiscardDialogDisplayed(),
                "Dialog Discard khong hien");
        ExtentReportManager.getTest().log(Status.PASS, "Dialog hien thi");
    }

    @Test(description = "VE_05_02: Click Cancel tren Discard dialog")
    public void test_VE_05_02_discard_dialog_cancel() throws InterruptedException {
        voiceEffectsPage.clickClose();
        Thread.sleep(1000);

        if (voiceEffectsPage.isDiscardDialogDisplayed()) {
            voiceEffectsPage.clickDiscardCancel();
            Thread.sleep(1000);

            Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                    "Khong quay lai Voice Effects sau Cancel");
            ExtentReportManager.getTest().log(Status.PASS,
                    "Da quay lai Voice Effects");
        } else {
            ExtentReportManager.getTest().log(Status.WARNING, "Khong co dialog");
        }
    }

    @Test(description = "VE_05_03: Click Discard tren dialog")
    public void test_VE_05_03_discard_dialog_confirm() throws InterruptedException {
        voiceEffectsPage.clickClose();
        Thread.sleep(1000);

        if (voiceEffectsPage.isDiscardDialogDisplayed()) {
            voiceEffectsPage.clickDiscardConfirm();
            Thread.sleep(1000);

            boolean atHome = driver.findElements(
                    org.openqa.selenium.By.id(
                            "com.bluesoftware.voicechanger:id/layout_record")).size() > 0;
            Assert.assertTrue(atHome, "Khong ve Home");
            ExtentReportManager.getTest().log(Status.PASS, "Da ve Home");
        } else {
            ExtentReportManager.getTest().log(Status.WARNING, "Khong co dialog");
        }
    }
}