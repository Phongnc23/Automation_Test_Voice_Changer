package testcases.ImportAudio;

import Base.BaseSharedSessionTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.ImportAudioFilePickerPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * IA_01: Verify File Picker (4 tests).
 *
 * Expected screen = Home. Moi test tu mo File Picker khi can.
 * Sau test mo File Picker, @BeforeMethod re-nav neu khong o Home.
 */
public class ImportAudio01_Verify_File_Picker extends BaseSharedSessionTest {

    private ImportAudioFilePickerPage filePicker;

    @Override
    protected void navigateToScreen() {
        // Quick activate neu o File Picker
        if (RecordFlowHelper.isAtFilePicker(driver)) {
            try {
                ((AndroidDriver) driver).activateApp(
                        "com.bluesoftware.voicechanger");
                Thread.sleep(800);
            } catch (Exception e) {
                // skip
            }
        }
        if (!RecordFlowHelper.isAtHome(driver)) {
            RecordFlowHelper.smartResetToHome(driver);
        }
        filePicker = new ImportAudioFilePickerPage(driver);
    }

    @Override
    protected boolean isAtExpectedScreen() {
        return RecordFlowHelper.isAtHome(driver);
    }

    @Test(priority = 1, description = "IA_01_01: Mo File Picker tu Home")
    public void test_IA_01_01_open_file_picker()
            throws InterruptedException {
        filePicker = RecordFlowHelper.navigateToFilePicker(driver);

        // navigateToFilePicker da co smart wait, khong can sleep them
        Assert.assertTrue(filePicker.isDisplayed(),
                "Khong vao duoc File Picker");
        ExtentReportManager.getTest().log(Status.PASS,
                "Mo File Picker: " + filePicker.getCurrentPackage());
    }

    @Test(priority = 2, description = "IA_01_02: File Picker la system app")
    public void test_IA_01_02_file_picker_is_system_app()
            throws InterruptedException {
        // Neu test 01 vua chay -> co the dang o File Picker, dung luon
        if (!filePicker.isDisplayed()) {
            filePicker = RecordFlowHelper.navigateToFilePicker(driver);
        }

        String currentPkg = filePicker.getCurrentPackage();
        ExtentReportManager.getTest().log(Status.INFO,
                "Package: " + currentPkg);

        Assert.assertNotNull(currentPkg, "Khong lay duoc package");
        Assert.assertNotEquals(currentPkg, "com.bluesoftware.voicechanger",
                "File Picker phai la system app");
        ExtentReportManager.getTest().log(Status.PASS,
                "File Picker la system app: " + currentPkg);
    }

    @Test(priority = 3, description = "IA_01_03: Back tu File Picker ve Voice Changer")
    public void test_IA_01_03_back_from_file_picker()
            throws InterruptedException {
        // Reuse File Picker neu dang mo, neu khong thi mo lai
        if (!filePicker.isDisplayed()) {
            filePicker = RecordFlowHelper.navigateToFilePicker(driver);
        }

        Assert.assertTrue(filePicker.isDisplayed(),
                "Khong vao duoc File Picker");

        // pressBack() da co smart wait noi tai - dung ngay khi ve Voice Changer.
        // Khong can sleep them de tranh nhan BACK thua va out app ve system home.
        filePicker.pressBack();

        String currentPkg = ((AndroidDriver) driver).getCurrentPackage();
        ExtentReportManager.getTest().log(Status.INFO,
                "Package sau BACK: " + currentPkg);
        Assert.assertEquals(currentPkg, "com.bluesoftware.voicechanger",
                "Khong ve Voice Changer");
        ExtentReportManager.getTest().log(Status.PASS,
                "BACK thanh cong");
    }

    @Test(priority = 4, description = "IA_01_04: Re-open File Picker sau khi back")
    public void test_IA_01_04_reopen_file_picker_after_back()
            throws InterruptedException {
        // Test 03 da BACK ve Voice Changer, co the dang o Home
        // Hoac neu chay rieng -> can mo File Picker lan 1

        // Lan 1: dam bao da o Voice Changer va mo File Picker
        if (!filePicker.isDisplayed()) {
            filePicker = RecordFlowHelper.navigateToFilePicker(driver);
        }
        Assert.assertTrue(filePicker.isDisplayed(), "Lan 1: khong mo duoc");

        // BACK ve Voice Changer (smart wait noi tai cua pressBack)
        filePicker.pressBack();

        // Re-open lan 2
        filePicker = RecordFlowHelper.navigateToFilePicker(driver);
        Assert.assertTrue(filePicker.isDisplayed(),
                "Lan 2: khong mo lai duoc");
        ExtentReportManager.getTest().log(Status.PASS,
                "Mo lai File Picker thanh cong");
    }
}
