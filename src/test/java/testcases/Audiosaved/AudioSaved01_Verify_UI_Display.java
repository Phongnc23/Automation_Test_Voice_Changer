package testcases.Audiosaved;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

public class AudioSaved01_Verify_UI_Display extends BaseTest {

    private AudioSavedPage audioSavedPage;

    @BeforeMethod
    public void navigateToScreen() {
        audioSavedPage = RecordFlowHelper.navigateToAudioSaved(driver, 3);
    }

    @AfterMethod
    public void resetState() {
        RecordFlowHelper.resetToHome(driver);
    }

    @Test(description = "SAV_01_01: Icon check xanh la")
    public void test_SAV_01_01_verify_success_check_icon() {
        Assert.assertTrue(audioSavedPage.isSuccessIconDisplayed(),
                "Icon check khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Icon hien thi");
    }

    @Test(description = "SAV_01_02: Title 'Audio Saved Successfully'")
    public void test_SAV_01_02_verify_title() {
        String title = audioSavedPage.getTitle();
        Assert.assertEquals(title, "Audio Saved Successfully", "Title sai");
        ExtentReportManager.getTest().log(Status.PASS, "Title: " + title);
    }

    @Test(description = "SAV_01_03: Card thong tin file hien thi")
    public void test_SAV_01_03_verify_file_info_card() {
        Assert.assertTrue(audioSavedPage.isAudioInfoCardDisplayed(),
                "Card thong tin khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Card hien thi");
    }

    @Test(description = "SAV_01_04: Format ten file")
    public void test_SAV_01_04_verify_file_name_format() {
        String fileName = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO, "File: " + fileName);
        Assert.assertNotNull(fileName);
        Assert.assertTrue(fileName.matches("record_\\d+\\.m4a"),
                "File name format sai: " + fileName);
        ExtentReportManager.getTest().log(Status.PASS, "Format dung");
    }

    @Test(description = "SAV_01_05: Thoi luong file")
    public void test_SAV_01_05_verify_file_duration() {
        String duration = audioSavedPage.parseDuration();
        ExtentReportManager.getTest().log(Status.INFO, "Duration: " + duration);
        Assert.assertNotNull(duration, "Duration null");
        Assert.assertTrue(duration.matches("\\d+:\\d+"),
                "Duration format sai: " + duration);
        ExtentReportManager.getTest().log(Status.PASS, "Duration dung");
    }

    @Test(description = "SAV_01_06: Dung luong (KB)")
    public void test_SAV_01_06_verify_file_size() {
        String size = audioSavedPage.parseFileSize();
        ExtentReportManager.getTest().log(Status.INFO, "Size: " + size);
        Assert.assertNotNull(size);
        Assert.assertTrue(size.contains("KB") || size.contains("MB"),
                "Size khong co don vi: " + size);
        ExtentReportManager.getTest().log(Status.PASS, "Size dung");
    }

    @Test(description = "SAV_01_07: Nut Set as ringtone")
    public void test_SAV_01_07_verify_set_ringtone_button() {
        Assert.assertTrue(audioSavedPage.isSetRingtoneButtonDisplayed(),
                "Nut Set as ringtone khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut hien thi");
    }

    @Test(description = "SAV_01_08: Nut Share")
    public void test_SAV_01_08_verify_share_button() {
        Assert.assertTrue(audioSavedPage.isShareButtonDisplayed(),
                "Nut Share khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut hien thi");
    }

    @Test(description = "SAV_01_09: Icon Home goc tren phai")
    public void test_SAV_01_09_verify_home_icon() {
        Assert.assertTrue(audioSavedPage.isHomeButtonDisplayed(),
                "Icon Home khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Icon Home hien thi");
    }
}