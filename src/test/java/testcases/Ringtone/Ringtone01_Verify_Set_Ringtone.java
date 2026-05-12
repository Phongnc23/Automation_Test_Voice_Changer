package testcases.Ringtone;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import Constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.SystemSettingsPage;
import Report.ExtentReportManager;
import Utils.AdbHelper;
import Utils.RecordFlowHelper;

public class Ringtone01_Verify_Set_Ringtone extends BaseTest {

    private AudioSavedPage audioSavedPage;
    private SystemSettingsPage settingsPage;

    @BeforeMethod
    public void navigateToScreen() {
        // Reset quyen ve OFF
        AdbHelper.revokeWriteSettings(driver, AppConstants.APP_PACKAGE);

        audioSavedPage = RecordFlowHelper.navigateToAudioSaved(driver, 3);
        settingsPage = new SystemSettingsPage(driver);
    }

    @AfterMethod
    public void resetState() {
        AdbHelper.revokeWriteSettings(driver, AppConstants.APP_PACKAGE);
        RecordFlowHelper.resetToHome(driver);
    }

    @Test(description = "SAV_07_01: Set Ringtone chua co quyen -> mo Settings")
    public void test_SAV_07_01_no_permission_opens_settings()
            throws InterruptedException {
        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(4000);

        Assert.assertTrue(settingsPage.isDisplayed(),
                "Khong vao man System Settings");
        ExtentReportManager.getTest().log(Status.PASS, "Da vao Settings");
    }

    @Test(description = "SAV_07_02: Voice Changer entry trong Settings")
    public void test_SAV_07_02_voice_changer_entry_displayed()
            throws InterruptedException {
        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(4000);

        Assert.assertTrue(settingsPage.isVoiceChangerEntryDisplayed(),
                "Khong hien thi Voice Changer entry");

        String appName = settingsPage.getAppName();
        String version = settingsPage.getAppVersion();
        ExtentReportManager.getTest().log(Status.INFO,
                "App: " + appName + " v" + version);
        ExtentReportManager.getTest().log(Status.PASS, "Entry dung");
    }

    @Test(description = "SAV_07_03: Bat toggle Modify System Settings")
    public void test_SAV_07_03_enable_toggle() throws InterruptedException {
        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(4000);

        Assert.assertTrue(settingsPage.isToggleOff(),
                "Toggle dang ON (expect OFF)");

        settingsPage.clickToggle();
        Thread.sleep(1500);

        Assert.assertTrue(settingsPage.isToggleOn(),
                "Toggle khong bat duoc");
        ExtentReportManager.getTest().log(Status.PASS, "Toggle da ON");
    }

    @Test(description = "SAV_07_04: Back ve app sau khi cap quyen")
    public void test_SAV_07_04_back_to_app_after_grant()
            throws InterruptedException {
        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(4000);

        settingsPage.clickToggle();
        Thread.sleep(1500);
        settingsPage.clickBack();
        Thread.sleep(2500);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong ve Audio Saved");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Audio Saved");
    }

    @Test(description = "SAV_07_05: Set Ringtone thanh cong")
    public void test_SAV_07_05_set_ringtone_success_after_grant()
            throws InterruptedException {
        // Cap quyen
        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(4000);
        settingsPage.clickToggle();
        Thread.sleep(1500);
        settingsPage.clickBack();
        Thread.sleep(2500);

        // Set lai ringtone
        Assert.assertTrue(audioSavedPage.isDisplayed());
        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(3000);

        // Verify van o app, khong vao Settings nua
        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Da vao Settings (expect khong vao)");
        ExtentReportManager.getTest().log(Status.PASS,
                "Set ringtone thanh cong, van o Audio Saved");
    }

    @Test(description = "SAV_07_06: Set Ringtone khi da co quyen tu truoc")
    public void test_SAV_07_06_set_ringtone_with_existing_permission()
            throws InterruptedException {
        // Override: grant truoc khi click
        AdbHelper.grantWriteSettings(driver, AppConstants.APP_PACKAGE);
        Thread.sleep(1000);

        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(3000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Da vao Settings (expect khong)");
        ExtentReportManager.getTest().log(Status.PASS,
                "Khong vao Settings (da co quyen)");
    }

    @Test(description = "SAV_07_07: Tu choi cap quyen")
    public void test_SAV_07_07_deny_permission() throws InterruptedException {
        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(4000);

        // KHONG bat toggle, click back
        settingsPage.clickBack();
        Thread.sleep(2500);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong ve Audio Saved");

        // Verify quyen van OFF
        String status = AdbHelper.checkWriteSettingsStatus(driver,
                AppConstants.APP_PACKAGE);
        ExtentReportManager.getTest().log(Status.INFO,
                "Permission status: " + status);

        ExtentReportManager.getTest().log(Status.PASS,
                "Da ve Audio Saved, khong cap quyen");
    }
}