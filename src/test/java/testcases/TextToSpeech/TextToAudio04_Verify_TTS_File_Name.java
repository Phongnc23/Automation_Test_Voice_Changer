package testcases.TextToSpeech;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.Components.EditMenu;
import Pages.Components.RenamePopup;
import Pages.TextToAudioPage;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

public class TextToAudio04_Verify_TTS_File_Name extends BaseTest {

    private TextToAudioPage textToAudioPage;
    private VoiceEffectsPage voiceEffectsPage;
    private AudioSavedPage audioSavedPage;

    private static final String DEFAULT_TEXT = "Xin chao test";

    @BeforeMethod
    public void navigateToScreen() {
        try {
            textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
            voiceEffectsPage = new VoiceEffectsPage(driver);
            audioSavedPage = new AudioSavedPage(driver);
        } catch (Exception e) {
            RecordFlowHelper.forceResetToHome(driver);
            textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
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

    private void enterTextAndNext(String text) throws InterruptedException {
        textToAudioPage.clickEditText();
        Thread.sleep(300);  // Giam tu 500
        textToAudioPage.enterText(text);
        Thread.sleep(200);  // Giam tu 500
        textToAudioPage.hideKeyboard();
        Thread.sleep(300);  // Giam tu 500
        textToAudioPage.clickNext();
        Thread.sleep(2500);  // Giam tu 4000
    }

    @Test(description = "TTS_04_01: Ten file tren Voice Effects la 'tts_'")
    public void test_TTS_04_01_verify_filename_on_voice_effects()
            throws InterruptedException {
        enterTextAndNext(DEFAULT_TEXT);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong vao Voice Effects");

        String fileName = voiceEffectsPage.getAudioFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten file VE: " + fileName);

        Assert.assertNotNull(fileName, "Ten file null");
        Assert.assertTrue(fileName.startsWith("tts_"),
                "Phai bat dau 'tts_': " + fileName);
        Assert.assertFalse(fileName.startsWith("record_"),
                "KHONG duoc 'record_': " + fileName);
        ExtentReportManager.getTest().log(Status.PASS,
                "File dung: " + fileName);
    }

    @Test(description = "TTS_04_02: Ten file tren Audio Saved la 'tts_'")
    public void test_TTS_04_02_verify_filename_on_audio_saved()
            throws InterruptedException {
        enterTextAndNext(DEFAULT_TEXT);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong vao Voice Effects");

        voiceEffectsPage.clickSave();
        Thread.sleep(2000);  // Giam tu 3000

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong chuyen Audio Saved");

        String fileName = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten Audio Saved: " + fileName);

        Assert.assertNotNull(fileName, "Ten file null");
        Assert.assertTrue(fileName.startsWith("tts_"),
                "Phai bat dau 'tts_': " + fileName);

        boolean validExt = fileName.endsWith(".wav") ||
                fileName.endsWith(".m4a");
        Assert.assertTrue(validExt,
                "Extension khong dung: " + fileName);
        ExtentReportManager.getTest().log(Status.PASS,
                "File dung: " + fileName);
    }

    @Test(description = "TTS_04_03: Rename giu extension")
    public void test_TTS_04_03_rename_preserves_extension()
            throws InterruptedException {
        enterTextAndNext(DEFAULT_TEXT);

        voiceEffectsPage.clickSave();
        Thread.sleep(2000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong o Audio Saved");

        String originalName = audioSavedPage.getFileName();

        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(500);  // Giam tu 800

        EditMenu editMenu = new EditMenu(driver);
        Assert.assertTrue(editMenu.isDisplayed(), "Edit menu khong mo");
        editMenu.clickRename();
        Thread.sleep(600);  // Giam tu 1000

        RenamePopup renamePopup = new RenamePopup(driver);
        Assert.assertTrue(renamePopup.isDisplayed(), "Rename popup khong mo");

        String newName = "my_tts_test_audio";
        renamePopup.clearText();
        Thread.sleep(200);  // Giam tu 300
        renamePopup.enterText(newName);
        Thread.sleep(200);
        renamePopup.clickDone();
        Thread.sleep(1500);  // Giam tu 2000

        String renamedFile = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten sau rename: " + renamedFile);

        Assert.assertNotNull(renamedFile);
        Assert.assertTrue(renamedFile.contains(newName),
                "Ten moi khong xuat hien: " + renamedFile);

        boolean validExt = renamedFile.endsWith(".wav") ||
                renamedFile.endsWith(".m4a");
        Assert.assertTrue(validExt,
                "Extension bi mat: " + renamedFile);
        ExtentReportManager.getTest().log(Status.PASS,
                "Rename giu extension: " + renamedFile);
    }
}