package testcases.ImportAudio;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.Components.DiscardDialog;
import Pages.ImportAudioFilePickerPage;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * IA_02: Verify chon file audio -> Voice Effects.
 *
 * LUU Y: File Picker cua app DA FILTER chi hien thi file audio.
 * Cac file khong phai audio (.csv, .txt, .pdf...) khong xuat hien trong picker
 * -> KHONG can test "file invalid bi disable".
 */
public class ImportAudio02_Verify_Select_File extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;
    private DiscardDialog discardDialog;

    @BeforeMethod
    public void navigateToScreen() {
        try {
            // Quick reset neu khong o Home
            if (!RecordFlowHelper.isAtHome(driver)) {
                if (RecordFlowHelper.isAtFilePicker(driver)) {
                    ((AndroidDriver) driver).activateApp(
                            "com.bluesoftware.voicechanger");
                    Thread.sleep(800);
                }
                if (!RecordFlowHelper.isAtHome(driver)) {
                    RecordFlowHelper.smartResetToHome(driver);
                    Thread.sleep(500);
                }
            }
            voiceEffectsPage = new VoiceEffectsPage(driver);
            discardDialog = new DiscardDialog(driver);
        } catch (Exception e) {
            logger.error("Navigate error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            voiceEffectsPage = new VoiceEffectsPage(driver);
            discardDialog = new DiscardDialog(driver);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void resetState() {
        try {
            // Dong discard dialog neu lo mo
            if (discardDialog.isDisplayed()) {
                discardDialog.clickDiscard();
                Thread.sleep(1200);
            }
            // Quick activate app neu o File Picker
            if (RecordFlowHelper.isAtFilePicker(driver)) {
                ((AndroidDriver) driver).activateApp(
                        "com.bluesoftware.voicechanger");
                Thread.sleep(800);
            }
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            try {
                RecordFlowHelper.forceResetToHome(driver);
            } catch (Exception ex) {
                logger.error("Force reset failed: " + ex.getMessage());
            }
        }
    }

    @Test(priority = 1, description = "IA_02_01: Chon file audio hop le -> Voice Effects")
    public void test_IA_02_01_select_audio_file()
            throws InterruptedException {
        ImportAudioFilePickerPage filePicker =
                RecordFlowHelper.navigateToFilePicker(driver);
        Assert.assertTrue(filePicker.isDisplayed(),
                "Khong vao duoc File Picker");

        boolean clicked = filePicker.clickFirstAudioFile();
        if (!clicked) {
            ExtentReportManager.getTest().log(Status.SKIP,
                    "Khong tim thay file audio tren device -> SKIP");
            throw new SkipException(
                    "Device khong co file .mp3/.m4a/.wav, skip test");
        }

        Thread.sleep(4000);  // Load file mat thoi gian

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong chuyen sang Voice Effects sau khi chon file");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da chon file va vao Voice Effects");
    }

    @Test(priority = 2, description = "IA_02_02: Verify Voice Effects load file da chon")
    public void test_IA_02_02_verify_voice_effects_loaded()
            throws InterruptedException {
        VoiceEffectsPage ve = RecordFlowHelper
                .navigateToVoiceEffectsFromImport(driver);
        if (ve == null) {
            ExtentReportManager.getTest().log(Status.SKIP,
                    "Khong load duoc file -> SKIP");
            throw new SkipException("Khong load duoc file audio");
        }

        Assert.assertTrue(ve.isDisplayed(),
                "Khong o Voice Effects");

        String fileName = ve.getAudioFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten file tren VE: " + fileName);

        Assert.assertNotNull(fileName, "Ten file null");
        Assert.assertTrue(fileName.length() > 0, "Ten file rong");
        ExtentReportManager.getTest().log(Status.PASS,
                "Voice Effects load file: " + fileName);
    }

    @Test(priority = 3, description = "IA_02_03: Discard dialog -> ve Home")
    public void test_IA_02_03_discard_from_voice_effects()
            throws InterruptedException {
        VoiceEffectsPage ve = RecordFlowHelper
                .navigateToVoiceEffectsFromImport(driver);
        if (ve == null) {
            throw new SkipException("Khong load duoc file");
        }

        Assert.assertTrue(ve.isDisplayed(), "Khong o Voice Effects");

        // Click X
        ve.clickClose();
        Thread.sleep(1200);

        // Verify dialog hien
        Assert.assertTrue(discardDialog.isDisplayed(),
                "Discard dialog khong hien");

        // Click Discard
        discardDialog.clickDiscard();
        Thread.sleep(1800);

        // Verify ve Home
        boolean atHome = driver.findElements(
                org.openqa.selenium.By.id(
                        "com.bluesoftware.voicechanger:id/layout_record")
        ).size() > 0;
        Assert.assertTrue(atHome, "Khong ve Home sau Discard");
        ExtentReportManager.getTest().log(Status.PASS,
                "Discard thanh cong, ve Home");
    }

    @Test(priority = 4, description = "IA_02_04: Verify File Picker chi hien file audio")
    public void test_IA_02_04_only_audio_files_displayed()
            throws InterruptedException {
        ImportAudioFilePickerPage filePicker =
                RecordFlowHelper.navigateToFilePicker(driver);
        Assert.assertTrue(filePicker.isDisplayed(),
                "Khong vao duoc File Picker");

        // Verify khong co file khong phai audio
        // Tim cac extension non-audio thong dung
        int csvCount = driver.findElements(
                io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().textContains(\".csv\")")).size();
        int txtCount = driver.findElements(
                io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().textContains(\".txt\")")).size();
        int pdfCount = driver.findElements(
                io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().textContains(\".pdf\")")).size();
        int jpgCount = driver.findElements(
                io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().textContains(\".jpg\")")).size();
        int pngCount = driver.findElements(
                io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().textContains(\".png\")")).size();

        ExtentReportManager.getTest().log(Status.INFO,
                "File non-audio: .csv=" + csvCount + ", .txt=" + txtCount
                        + ", .pdf=" + pdfCount + ", .jpg=" + jpgCount
                        + ", .png=" + pngCount);

        Assert.assertEquals(csvCount, 0, "File .csv hien thi (KHONG mong doi)");
        Assert.assertEquals(txtCount, 0, "File .txt hien thi (KHONG mong doi)");
        Assert.assertEquals(pdfCount, 0, "File .pdf hien thi (KHONG mong doi)");
        Assert.assertEquals(jpgCount, 0, "File .jpg hien thi (KHONG mong doi)");
        Assert.assertEquals(pngCount, 0, "File .png hien thi (KHONG mong doi)");

        ExtentReportManager.getTest().log(Status.PASS,
                "File Picker da filter dung - chi hien file audio");
    }

    @Test(priority = 5, description = "IA_02_05: Click Play audio sau khi import")
    public void test_IA_02_05_play_imported_audio()
            throws InterruptedException {
        VoiceEffectsPage ve = RecordFlowHelper
                .navigateToVoiceEffectsFromImport(driver);
        if (ve == null) {
            ExtentReportManager.getTest().log(Status.SKIP,
                    "Khong load duoc file -> SKIP");
            throw new SkipException("Khong load duoc file audio");
        }

        Assert.assertTrue(ve.isDisplayed(),
                "Khong o Voice Effects");

        // Ghi nho time truoc khi play
        String timeBefore = ve.getCurrentTime();
        ExtentReportManager.getTest().log(Status.INFO,
                "Time truoc play: " + timeBefore);

        // Click Play
        ve.clickPlayPause();
        Thread.sleep(2000);  // Wait audio bat dau phat va seekbar di chuyen

        // Verify time da thay doi (audio dang phat)
        String timeAfter = ve.getCurrentTime();
        ExtentReportManager.getTest().log(Status.INFO,
                "Time sau play: " + timeAfter);

        Assert.assertNotEquals(timeAfter, timeBefore,
                "Time khong thay doi - audio KHONG phat duoc");

        // Verify time KHONG phai "00:00 / 00:00" (file load thanh cong)
        Assert.assertNotEquals(timeAfter, "00:00 / 00:00",
                "Time van la 00:00 / 00:00 - file co the bi loi");

        // Click Pause de dung phat (cleanup)
        ve.clickPlayPause();
        Thread.sleep(500);

        ExtentReportManager.getTest().log(Status.PASS,
                "Audio phat thanh cong: " + timeBefore + " -> " + timeAfter);
    }
}