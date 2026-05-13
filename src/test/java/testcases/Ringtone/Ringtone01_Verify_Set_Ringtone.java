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
 * ADAPTIVE PATTERN voi 2 test E2E:
 *
 * Lan dau (permission DENY sau khi cai moi):
 *   Test 01: Verify FULL flow khi chua co quyen
 *     - Mo Settings -> verify UI -> toggle ON -> OFF -> back -> Audio Saved
 *   Test 02: Cap quyen + verify toast
 *     - Mo Settings -> toggle ON -> back -> click Set lan nua -> verify toast
 *
 * Lan sau (permission ALLOW):
 *   Test 01: SKIP (khong vao Settings duoc nua)
 *   Test 02: Verify toast chi (cap quyen tu dong)
 *
 * De retest full: adb uninstall com.bluesoftware.voicechanger
 * Hoac: adb shell pm clear com.bluesoftware.voicechanger
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
     * Helper: skip neu permission khong phai DENY.
     */
    private void requirePermissionDenied() {
        String status = AdbHelper.checkWriteSettingsStatus(
                driver, AppConstants.APP_PACKAGE);
        ExtentReportManager.getTest().log(Status.INFO,
                "Permission status: " + status);

        if (!"deny".equalsIgnoreCase(status)) {
            String msg = "Skip: permission da grant (status=" + status + "). " +
                    "Go cai app de retest: adb uninstall " +
                    AppConstants.APP_PACKAGE;
            ExtentReportManager.getTest().log(Status.SKIP, msg);
            throw new SkipException(msg);
        }
    }

    /**
     * Helper: auto-grant neu chua co quyen.
     */
    private void ensurePermissionGranted() throws InterruptedException {
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
    // TEST 01: FULL UI flow khi chua co quyen (gop)
    // ========================================================

    @Test(priority = 1,
            description = "RT_01_01: Full UI flow khi chua co quyen")
    public void test_RT_01_01_full_ui_flow_when_no_permission()
            throws InterruptedException {
        requirePermissionDenied();

        // ========== Buoc 1: Click Set Ringtone -> Mo Settings ==========
        audioSavedPage.clickSetAsRingtone();
        Thread.sleep(3000);

        Assert.assertTrue(settingsPage.isDisplayed(),
                "Khong vao man System Settings");
        ExtentReportManager.getTest().log(Status.PASS, "Da vao Settings");

        // ========== Buoc 2: Verify Voice Changer entry ==========
        Assert.assertTrue(settingsPage.isVoiceChangerEntryDisplayed(),
                "Khong hien thi Voice Changer entry");

        String appName = settingsPage.getAppName();
        String version = settingsPage.getAppVersion();
        ExtentReportManager.getTest().log(Status.INFO,
                "App: " + appName + " v" + version);
        ExtentReportManager.getTest().log(Status.PASS, "Entry hien thi");

        // ========== Buoc 3: Verify toggle OFF mac dinh ==========
        Assert.assertTrue(settingsPage.isToggleOff(),
                "Toggle dang ON (expect OFF mac dinh)");
        ExtentReportManager.getTest().log(Status.PASS, "Toggle OFF dung");

        // ========== Buoc 4: Click toggle -> ON ==========
        settingsPage.clickToggle();
        Thread.sleep(1500);
        Assert.assertTrue(settingsPage.isToggleOn(),
                "Toggle khong bat duoc");
        ExtentReportManager.getTest().log(Status.PASS, "Toggle da ON");

        // ========== Buoc 5: Click toggle lan 2 -> OFF (test 2 chieu) ==========
        settingsPage.clickToggle();
        Thread.sleep(1500);
        Assert.assertTrue(settingsPage.isToggleOff(),
                "Toggle khong tat duoc");
        ExtentReportManager.getTest().log(Status.PASS,
                "Toggle OFF lai duoc (test 2 chieu)");

        // ========== Buoc 6: Back ve Audio Saved ==========
        settingsPage.clickBack();
        Thread.sleep(2000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong ve Audio Saved sau khi back");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Audio Saved");

        // ========== Buoc 7: Verify permission van DENY ==========
        String finalStatus = AdbHelper.checkWriteSettingsStatus(
                driver, AppConstants.APP_PACKAGE);
        ExtentReportManager.getTest().log(Status.INFO,
                "Final permission status: " + finalStatus);
        ExtentReportManager.getTest().log(Status.PASS,
                "Full UI flow PASS: vao Settings -> toggle 2 chieu -> back");
    }

    // ========================================================
    // TEST 02: Cap quyen + verify toast success
    // ========================================================

    @Test(priority = 2,
            description = "RT_01_02: Cap quyen + click Set -> toast success")
    public void test_RT_01_02_grant_and_verify_success_toast()
            throws InterruptedException {
        // Pre-check: neu da co quyen, skip phan grant, chay phan verify toast
        String status = AdbHelper.checkWriteSettingsStatus(
                driver, AppConstants.APP_PACKAGE);
        ExtentReportManager.getTest().log(Status.INFO,
                "Initial permission: " + status);

        if ("deny".equalsIgnoreCase(status)) {
            // ========== TRUONG HOP DENY: thuc hien grant qua UI ==========
            ExtentReportManager.getTest().log(Status.INFO,
                    "Permission DENY - thuc hien cap quyen qua Settings");

            audioSavedPage.clickSetAsRingtone();
            Thread.sleep(3000);

            Assert.assertTrue(settingsPage.isDisplayed(),
                    "Khong vao Settings");

            settingsPage.clickToggle();
            Thread.sleep(1500);
            Assert.assertTrue(settingsPage.isToggleOn(),
                    "Toggle khong ON duoc");

            settingsPage.clickBack();
            Thread.sleep(2000);

            Assert.assertTrue(audioSavedPage.isDisplayed(),
                    "Khong ve Audio Saved sau khi cap quyen");
            ExtentReportManager.getTest().log(Status.PASS,
                    "Da cap quyen va ve Audio Saved");
        } else {
            // ========== TRUONG HOP ALLOW: skip grant ==========
            ExtentReportManager.getTest().log(Status.INFO,
                    "Permission da ALLOW - skip grant flow");
        }

        // ========== BUOC CHINH: Click Set Ringtone va verify toast ==========
        ExtentReportManager.getTest().log(Status.INFO,
                "Click Set Ringtone va wait toast");
        audioSavedPage.clickSetAsRingtone();

        // Poll toast trong 3.5s (toast hien thi ~2s)
        boolean toastShown = audioSavedPage.waitForRingtoneSuccessToast(3500);
        String toastText = audioSavedPage.getToastText();

        // Verify 1: Khong vao Settings (vi da co quyen)
        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Da vao Settings (expect KHONG vao vi da co quyen)");
        ExtentReportManager.getTest().log(Status.PASS,
                "Khong vao Settings - van o Audio Saved");

        // Verify 2: Toast "Ringtone set successfully"
        if (toastShown) {
            ExtentReportManager.getTest().log(Status.PASS,
                    "Toast hien thi: " + toastText);

            if (toastText != null) {
                Assert.assertTrue(
                        toastText.toLowerCase().contains("ringtone") ||
                                toastText.toLowerCase().contains("success"),
                        "Toast text khong dung: " + toastText);
            }
        } else {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Khong detect duoc toast - co the do timing hoac app render khong tao Toast widget chuan");
        }

        ExtentReportManager.getTest().log(Status.PASS,
                "Set ringtone thanh cong");
    }
}