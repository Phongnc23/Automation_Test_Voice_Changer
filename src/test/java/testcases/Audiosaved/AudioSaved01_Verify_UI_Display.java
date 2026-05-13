package testcases.Audiosaved;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;


public class AudioSaved01_Verify_UI_Display extends BaseTest {

    private AudioSavedPage audioSavedPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupAudioSavedSession() {
        logger.info("=== SETUP AUDIO SAVED SESSION ===");
        try {
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(driver, 3);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(driver, 3);
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        logger.info("=== CLEANUP AFTER CLASS ===");
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    @Test(description = "SAV_01_01: Icon check xanh la", priority = 1)
    public void test_SAV_01_01_verify_success_check_icon() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify success icon");
        Assert.assertTrue(audioSavedPage.isSuccessIconDisplayed(),
                "Icon check khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Icon hien thi");
    }

    @Test(description = "SAV_01_02: Title 'Audio Saved Successfully'", priority = 2)
    public void test_SAV_01_02_verify_title() {
        String title = audioSavedPage.getTitle();
        ExtentReportManager.getTest().log(Status.INFO, "Title: " + title);
        Assert.assertEquals(title, "Audio Saved Successfully", "Title sai");
        ExtentReportManager.getTest().log(Status.PASS, "Title dung");
    }

    @Test(description = "SAV_01_03: Card thong tin file", priority = 3)
    public void test_SAV_01_03_verify_file_info_card() {
        Assert.assertTrue(audioSavedPage.isAudioInfoCardDisplayed(),
                "Card thong tin khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Card hien thi");
    }

    @Test(description = "SAV_01_04: Format ten file", priority = 4)
    public void test_SAV_01_04_verify_file_name_format() {
        String fileName = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO, "File: " + fileName);
        Assert.assertNotNull(fileName, "File name null");
        Assert.assertTrue(fileName.matches("record_\\d+\\.m4a"),
                "File name format sai: " + fileName);
        ExtentReportManager.getTest().log(Status.PASS, "Format dung: " + fileName);
    }

    @Test(description = "SAV_01_05: Thoi luong file", priority = 5)
    public void test_SAV_01_05_verify_file_duration() {
        String duration = audioSavedPage.parseDuration();
        ExtentReportManager.getTest().log(Status.INFO, "Duration: " + duration);
        Assert.assertNotNull(duration, "Duration null");
        Assert.assertTrue(duration.matches("\\d+:\\d+"),
                "Duration format sai: " + duration);
        ExtentReportManager.getTest().log(Status.PASS, "Duration dung: " + duration);
    }

    @Test(description = "SAV_01_06: Dung luong file", priority = 6)
    public void test_SAV_01_06_verify_file_size() {
        String size = audioSavedPage.parseFileSize();
        ExtentReportManager.getTest().log(Status.INFO, "Size: " + size);
        Assert.assertNotNull(size, "Size null");
        Assert.assertTrue(size.contains("KB") || size.contains("MB"),
                "Size khong co don vi: " + size);
        ExtentReportManager.getTest().log(Status.PASS, "Size dung: " + size);
    }

    @Test(description = "SAV_01_07: Nut Set as ringtone", priority = 7)
    public void test_SAV_01_07_verify_set_ringtone_button() {
        Assert.assertTrue(audioSavedPage.isSetRingtoneButtonDisplayed(),
                "Nut Set as ringtone khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut Set ringtone hien thi");
    }

    @Test(description = "SAV_01_08: Nut Share", priority = 8)
    public void test_SAV_01_08_verify_share_button() {
        Assert.assertTrue(audioSavedPage.isShareButtonDisplayed(),
                "Nut Share khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut Share hien thi");
    }

    @Test(description = "SAV_01_09: Icon Home goc tren phai", priority = 9)
    public void test_SAV_01_09_verify_home_icon() {
        Assert.assertTrue(audioSavedPage.isHomeButtonDisplayed(),
                "Icon Home khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Icon Home hien thi");
    }
}