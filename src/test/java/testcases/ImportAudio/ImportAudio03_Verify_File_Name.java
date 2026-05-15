package testcases.ImportAudio;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * IA_03: Verify file naming format 'import_<timestamp>_<original>.<ext>'.
 *
 * Tu DOM: import_1778812016361_Âm Thầm Bên Em..._1775440616123.mp3
 * Day la UNIQUE cua Import Audio (khac record_, tts_, src_).
 */
public class ImportAudio03_Verify_File_Name extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;
    private AudioSavedPage audioSavedPage;

    @BeforeMethod
    public void navigateToScreen() {
        try {
            RecordFlowHelper.smartResetToHome(driver);
            Thread.sleep(800);
            voiceEffectsPage = new VoiceEffectsPage(driver);
            audioSavedPage = new AudioSavedPage(driver);
        } catch (Exception e) {
            logger.error("Navigate error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
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

    @Test(priority = 1, description = "IA_03_01: Ten file Voice Effects co prefix 'import_'")
    public void test_IA_03_01_verify_voice_effects_filename()
            throws InterruptedException {
        VoiceEffectsPage ve = RecordFlowHelper
                .navigateToVoiceEffectsFromImport(driver);
        if (ve == null) {
            ExtentReportManager.getTest().log(Status.SKIP,
                    "Khong load duoc file -> SKIP");
            throw new SkipException("Khong load duoc file audio");
        }

        Assert.assertTrue(ve.isDisplayed(), "Khong o Voice Effects");

        String fileName = ve.getAudioFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten file tren VE: " + fileName);

        Assert.assertNotNull(fileName, "Ten file null");
        Assert.assertTrue(fileName.startsWith("import_"),
                "Phai bat dau 'import_': " + fileName);

        // Verify khong phai prefix khac
        Assert.assertFalse(fileName.startsWith("record_"),
                "KHONG duoc bat dau 'record_': " + fileName);
        Assert.assertFalse(fileName.startsWith("tts_"),
                "KHONG duoc bat dau 'tts_': " + fileName);
        Assert.assertFalse(fileName.startsWith("src_"),
                "KHONG duoc bat dau 'src_': " + fileName);

        ExtentReportManager.getTest().log(Status.PASS,
                "File VE dung format 'import_': " + fileName);
    }

    @Test(priority = 2, description = "IA_03_02: Save -> Audio Saved giu prefix 'import_'")
    public void test_IA_03_02_verify_audio_saved_filename()
            throws InterruptedException {
        VoiceEffectsPage ve = RecordFlowHelper
                .navigateToVoiceEffectsFromImport(driver);
        if (ve == null) {
            throw new SkipException("Khong load duoc file audio");
        }

        Assert.assertTrue(ve.isDisplayed(), "Khong o Voice Effects");

        // Save
        ve.clickSave();
        Thread.sleep(3000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong chuyen sang Audio Saved");

        String savedFileName = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten file tren Audio Saved: " + savedFileName);

        Assert.assertNotNull(savedFileName, "Ten file null");
        Assert.assertTrue(savedFileName.startsWith("import_"),
                "Phai bat dau 'import_': " + savedFileName);

        ExtentReportManager.getTest().log(Status.PASS,
                "File Audio Saved dung format: " + savedFileName);
    }

    @Test(priority = 3, description = "IA_03_03: Extension goc duoc giu")
    public void test_IA_03_03_extension_preserved()
            throws InterruptedException {
        VoiceEffectsPage ve = RecordFlowHelper
                .navigateToVoiceEffectsFromImport(driver);
        if (ve == null) {
            throw new SkipException("Khong load duoc file audio");
        }

        ve.clickSave();
        Thread.sleep(3000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong o Audio Saved");

        String savedFileName = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten file: " + savedFileName);

        Assert.assertNotNull(savedFileName);

        // Verify extension hop le
        boolean validExt = savedFileName.endsWith(".mp3")
                || savedFileName.endsWith(".m4a")
                || savedFileName.endsWith(".wav")
                || savedFileName.endsWith(".aac");
        Assert.assertTrue(validExt,
                "Extension khong dung: " + savedFileName);

        ExtentReportManager.getTest().log(Status.PASS,
                "Extension giu nguyen: " + savedFileName);
    }
}