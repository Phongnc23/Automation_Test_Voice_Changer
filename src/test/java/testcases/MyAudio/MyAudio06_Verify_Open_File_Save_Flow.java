package testcases.MyAudio;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.MyAudioPage;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * MA_06: Test luong My Audio -> click -> Voice Effects -> Save (4 tests).
 * UNIQUE: file name co prefix 'src_'.
 */
public class MyAudio06_Verify_Open_File_Save_Flow extends BaseTest {

    private MyAudioPage myAudioPage;
    private VoiceEffectsPage voiceEffectsPage;
    private AudioSavedPage audioSavedPage;

    @BeforeMethod
    public void navigateToScreen() {
        try {
            MyAudioPage temp = RecordFlowHelper.navigateToMyAudio(driver);
            if (!temp.hasAtLeastOneFile()) {
                RecordFlowHelper.smartResetToHome(driver);
                Thread.sleep(800);
                RecordFlowHelper.navigateToAudioSaved(driver, 1);
                Thread.sleep(800);
                RecordFlowHelper.smartResetToHome(driver);
                Thread.sleep(800);
                temp = RecordFlowHelper.navigateToMyAudio(driver);
            }
            myAudioPage = temp;
            voiceEffectsPage = new VoiceEffectsPage(driver);
            audioSavedPage = new AudioSavedPage(driver);
        } catch (Exception e) {
            logger.error("Navigate error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            voiceEffectsPage = new VoiceEffectsPage(driver);
            audioSavedPage = new AudioSavedPage(driver);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void resetState() {
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            try {
                RecordFlowHelper.forceResetToHome(driver);
            } catch (Exception ex) {
                logger.error("Force reset failed: " + ex.getMessage());
            }
        }
    }

    @Test(description = "MA_06_01: Click card -> Voice Effects")
    public void test_MA_06_01_click_item_opens_voice_effects()
            throws InterruptedException {
        Assert.assertTrue(myAudioPage.hasAtLeastOneFile(),
                "Khong co file");

        String fileName = myAudioPage.getFirstFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Click file: " + fileName);

        myAudioPage.clickItemAt(1);
        Thread.sleep(1500);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong chuyen sang Voice Effects");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da chuyen Voice Effects");
    }

    @Test(description = "MA_06_02: Ten file VE co prefix 'src_'")
    public void test_MA_06_02_verify_voice_effects_filename()
            throws InterruptedException {
        // FIX 1: Lay ten file INDEX 1 (file se mo)
        String originalName = myAudioPage.getAllFileNames().get(1);
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten goc (file index 1): " + originalName);

        myAudioPage.clickItemAt(1);
        Thread.sleep(2500);  // Tang sleep cho VE load

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong vao Voice Effects");

        String veFileName = voiceEffectsPage.getAudioFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten file VE: " + veFileName);

        Assert.assertNotNull(veFileName, "Ten null");

        // FIX 2: Check format src_<timestamp>_<original>.<ext>
        Assert.assertTrue(veFileName.startsWith("src_"),
                "Phai bat dau 'src_': " + veFileName);

        // Verify ten goc xuat hien trong ten moi
        // Loai bo extension cua originalName neu co (de match linh hoat)
        String originalCore = originalName;
        if (originalName.endsWith(".m4a") || originalName.endsWith(".wav")) {
            originalCore = originalName.substring(0, originalName.lastIndexOf("."));
        }

        Assert.assertTrue(veFileName.contains(originalCore),
                "Ten file VE phai chua ten goc.\n" +
                        "  Ten goc: " + originalName + "\n" +
                        "  Ten core (bo ext): " + originalCore + "\n" +
                        "  Ten VE: " + veFileName);

        ExtentReportManager.getTest().log(Status.PASS,
                "File VE format dung: " + veFileName);
    }

    @Test(description = "MA_06_03: Save -> Audio Saved")
    public void test_MA_06_03_save_navigates_to_audio_saved()
            throws InterruptedException {
        myAudioPage.clickItemAt(1);
        Thread.sleep(1500);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong vao Voice Effects");

        voiceEffectsPage.clickSave();
        Thread.sleep(1000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong chuyen Audio Saved");
        ExtentReportManager.getTest().log(Status.PASS,
                "Save thanh cong");
    }

    @Test(description = "MA_06_04: Ten Audio Saved co prefix 'src_'")
    public void test_MA_06_04_verify_audio_saved_filename()
            throws InterruptedException {
        myAudioPage.clickItemAt(1);
        Thread.sleep(1500);

        voiceEffectsPage.clickSave();
        Thread.sleep(1000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong chuyen Audio Saved");

        String savedFileName = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten file Audio Saved: " + savedFileName);

        Assert.assertNotNull(savedFileName);
        Assert.assertTrue(savedFileName.startsWith("src_"),
                "Phai bat dau 'src_': " + savedFileName);

        boolean validExt = savedFileName.endsWith(".m4a")
                || savedFileName.endsWith(".wav");
        Assert.assertTrue(validExt, "Extension sai: " + savedFileName);

        ExtentReportManager.getTest().log(Status.PASS,
                "File dung format: " + savedFileName);
    }
}