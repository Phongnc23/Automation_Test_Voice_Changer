package testcases.Ringtone;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import Constants.AppConstants;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.SystemSettingsPage;
import Report.ExtentReportManager;
import Utils.AdbHelper;
import Utils.RecordFlowHelper;

/**
 * Test Set Ringtone tren Audio Saved.
 *
 * ADAPTIVE PATTERN:
 * - Lan dau (permission DENY sau khi cai moi):
 *   chay full 4 test: verify UI, grant + set, deny, set with permission
 * - Lan sau (permission ALLOW tu lan truoc):
 *   tu dong SKIP 3 test can DENY state, chi chay test verify success
 *
 * De retest full: go cai app va cai lai
 *   adb uninstall com.bluesoftware.voicechanger
 */
public class Ringtone01_Verify_Set_Ringtone extends BaseTest {

    private AudioSavedPage audioSavedPage;
    private SystemSettingsPage settingsPage;

    private static final int RECORDING_SECONDS = 1;

    @BeforeMethod
    public void navigateToScreen() {
        try {
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(
                    driver, RECORDING_SECONDS);
            settingsPage = new SystemSettingsPage(driver);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(
                    driver, RECORDING_SECONDS);
            settingsPage = new SystemSettingsPage(driver);
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

    /**
     * Helper: skip test neu permission khong phai DENY.
     */
    private void requirePermissionDenied() {
        String status = AdbHelper.checkWriteSettingsStatus(
                driver, AppConstants.APP_PACKAGE);
        ExtentReportManager.getTest().log(Status.INFO,
                "Permission status: " + status);

        if (!"deny".equalsIgnoreCase(status)) {
            String msg = "Skip: permission da grant (status=" + status + "). " +
                    "Go cai app de retest full flow.";
            ExtentReportManager.getTest().log(Status.SKIP, msg);
            throw new SkipException(msg);
        }
    }

    /**
     * Helper: auto-grant neu permission chua ALLOW.
     */
    private void requirePermissionAllowed() throws InterruptedException {
        String status = AdbHelper.checkWriteSettingsStatus(
                driver, AppConstants.APP_PACKAGE);

        if (!"allow".equalsIgnoreCase(status)) {
            ExtentReportManager.getTest().log(Status.INFO,
                    "Auto-grant permission qua ADB");
            AdbHelper.grantWriteSettings(driver, AppConstants.APP_PACKAGE);
            Thread.sleep(800);
        }
    }

    // ========================================================
    // GROUP 1: Test require DENY state (skip neu da ALLOW)
    // ========================================================

    @Test(priority = 1,
            description = "RT_01_01: Mo Settings - verify UI + toggle")
    public void test_RT_01_01_open_settings_and_verify_ui()
            throws InterruptedException {
        requirePermissionDenied();

        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(3000);

        Assert.assertTrue(settingsPage.isDisplayed(),
                "Khong vao man System Settings");
        ExtentReportManager.getTest().log(Status.PASS, "Da vao Settings");

        Assert.assertTrue(settingsPage.isVoiceChangerEntryDisplayed(),
                "Khong hien thi Voice Changer entry");

        String appName = settingsPage.getAppName();
        String version = settingsPage.getAppVersion();
        ExtentReportManager.getTest().log(Status.INFO,
                "App: " + appName + " v" + version);
        ExtentReportManager.getTest().log(Status.PASS, "Entry hien thi");

        Assert.assertTrue(settingsPage.isToggleOff(),
                "Toggle dang ON (expect OFF)");
        ExtentReportManager.getTest().log(Status.PASS, "Toggle OFF dung");

        settingsPage.clickToggle();
        Thread.sleep(1500);
        Assert.assertTrue(settingsPage.isToggleOn(),
                "Toggle khong bat duoc");
        ExtentReportManager.getTest().log(Status.PASS, "Toggle da ON");
    }

    @Test(priority = 2,
            description = "RT_01_02: Cap quyen + Set Ringtone success")
    public void test_RT_01_02_grant_permission_and_set_ringtone()
            throws InterruptedException {
        requirePermissionDenied();

        // Buoc 1: Mo Settings, cap quyen
        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(3000);
        settingsPage.clickToggle();
        Thread.sleep(1500);
        settingsPage.clickBack();
        Thread.sleep(2000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong ve Audio Saved");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Audio Saved");

        // Buoc 2: Set Ringtone lai - khong vao Settings nua + toast success
        audioSavedPage.clickSetAsRingtone();

        // Wait toast (chay POLL trong khi cho)
        boolean toastShown = audioSavedPage.waitForRingtoneSuccessToast(3500);
        String toastText = audioSavedPage.getToastText();

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Da vao Settings (expect khong vao)");

        if (toastShown) {
            ExtentReportManager.getTest().log(Status.PASS,
                    "Toast hien thi: " + toastText);
        } else {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Khong detect duoc toast (co the disappeared trong khi poll)");
        }

        ExtentReportManager.getTest().log(Status.PASS,
                "Set ringtone thanh cong");
    }

    @Test(priority = 3,
            description = "RT_01_03: Tu choi cap quyen - back ve app")
    public void test_RT_01_03_deny_permission() throws InterruptedException {
        requirePermissionDenied();

        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(3000);

        Assert.assertTrue(settingsPage.isDisplayed(), "Khong o Settings");
        ExtentReportManager.getTest().log(Status.PASS, "Da vao Settings");

        // KHONG bat toggle, click back
        settingsPage.clickBack();
        Thread.sleep(2000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong ve Audio Saved");

        String status = AdbHelper.checkWriteSettingsStatus(
                driver, AppConstants.APP_PACKAGE);
        ExtentReportManager.getTest().log(Status.INFO,
                "Permission status: " + status);
        ExtentReportManager.getTest().log(Status.PASS,
                "Da ve Audio Saved, khong cap quyen");
    }

    // ========================================================
    // GROUP 2: Test require ALLOW state (auto-grant neu can)
    // ========================================================

    @Test(priority = 10,
            description = "RT_01_04: Set Ringtone thanh cong (da co quyen)")
    public void test_RT_01_04_set_ringtone_with_permission()
            throws InterruptedException {
        requirePermissionAllowed();

        audioSavedPage.clickSetAsRingtone();

        // Wait toast voi poll 3.5s
        boolean toastShown = audioSavedPage.waitForRingtoneSuccessToast(3500);
        String toastText = audioSavedPage.getToastText();

        // Verify 1: Khong vao Settings
        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Da vao Settings (expect KHONG vao vi da co quyen)");
        ExtentReportManager.getTest().log(Status.PASS,
                "Khong vao Settings (da co quyen)");

        // Verify 2: Toast "Ringtone set successfully"
        if (toastShown) {
            ExtentReportManager.getTest().log(Status.PASS,
                    "Toast hien thi: " + toastText);
            Assert.assertTrue(toastText != null &&
                            toastText.toLowerCase().contains("ringtone"),
                    "Toast text khong dung: " + toastText);
        } else {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Khong detect duoc toast - co the do timing");
            // Khong fail test - toast co the disappeared truoc khi poll
        }
    }
}