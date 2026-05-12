package testcases.Audiosaved;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.Components.ShareBottomSheet;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

public class AudioSaved02_Verify_Navigation_Share extends BaseTest {

    private AudioSavedPage audioSavedPage;
    private ShareBottomSheet shareSheet;

    @BeforeMethod
    public void navigateToScreen() {
        audioSavedPage = RecordFlowHelper.navigateToAudioSaved(driver, 3);
        shareSheet = new ShareBottomSheet(driver);
    }

    @AfterMethod
    public void resetState() {
        RecordFlowHelper.resetToHome(driver);
    }

    @Test(description = "SAV_02_01: Click Home -> ve Home")
    public void test_SAV_02_01_click_home_navigates_to_home_screen()
            throws InterruptedException {
        audioSavedPage.clickHomeButton();
        Thread.sleep(2000);

        boolean atHome = driver.findElements(
                By.id("com.bluesoftware.voicechanger:id/layout_record")).size() > 0;
        Assert.assertTrue(atHome, "Khong ve Home");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home");
    }

    @Test(description = "SAV_02_02: File luu trong My Audio - SKIP")
    public void test_SAV_02_02_saved_file_in_my_audio() {
        throw new SkipException("Can MyAudioPage de verify (chua co)");
    }

    @Test(description = "SAV_03_01: Click Share mo bottom sheet")
    public void test_SAV_03_01_click_share_opens_bottom_sheet()
            throws InterruptedException {
        audioSavedPage.clickShare();
        Thread.sleep(3000);

        Assert.assertTrue(shareSheet.isDisplayed(),
                "Bottom sheet khong mo");
        ExtentReportManager.getTest().log(Status.PASS, "Bottom sheet da mo");
    }

    @Test(description = "SAV_03_02: Bottom sheet hien ten file")
    public void test_SAV_03_02_share_sheet_shows_filename()
            throws InterruptedException {
        String fileNameBefore = audioSavedPage.getFileName();

        audioSavedPage.clickShare();
        Thread.sleep(3000);

        String previewName = shareSheet.getPreviewFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Before: " + fileNameBefore + ", Preview: " + previewName);

        Assert.assertNotNull(previewName);
        Assert.assertTrue(previewName.contains("record_"),
                "Preview name khong dung");
        ExtentReportManager.getTest().log(Status.PASS, "File name dung");
    }

    @Test(description = "SAV_03_03: Cancel bottom sheet")
    public void test_SAV_03_03_close_share_bottom_sheet()
            throws InterruptedException {
        audioSavedPage.clickShare();
        Thread.sleep(3000);

        shareSheet.clickCancel();
        Thread.sleep(2000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong quay lai Audio Saved");
        ExtentReportManager.getTest().log(Status.PASS, "Da quay lai");
    }
}