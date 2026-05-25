package testcases.Setting;

import Base.BaseSharedSessionTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.SettingsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * ST_02: Verify Output Path (2 tests).
 */
public class Setting02_Verify_Output_Path extends BaseSharedSessionTest {

    private SettingsPage settingsPage;

    @Override
    protected void navigateToScreen() {
        settingsPage = RecordFlowHelper.navigateToSettings(driver);
    }

    @Override
    protected boolean isAtExpectedScreen() {
        return RecordFlowHelper.isAtSettings(driver);
    }

    @Test(priority = 1, description = "ST_02_01: Verify Output Path hien thi day du")
    public void test_ST_02_01_verify_output_path_display() {
        String outputPath = settingsPage.getOutputPath();
        ExtentReportManager.getTest().log(Status.INFO,
                "Output Path: " + outputPath);

        Assert.assertNotNull(outputPath, "Output Path null");
        Assert.assertTrue(outputPath.length() > 0, "Output Path rong");

        // Verify format dung (chua "storage" va "VoiceChanger")
        Assert.assertTrue(outputPath.contains("storage")
                        || outputPath.contains("Music"),
                "Output Path khong dung format: " + outputPath);
        Assert.assertTrue(outputPath.contains("VoiceChanger"),
                "Output Path khong chua 'VoiceChanger': " + outputPath);

        ExtentReportManager.getTest().log(Status.PASS,
                "Output Path hien dung: " + outputPath);
    }

    @Test(priority = 2, description = "ST_02_02: Click Output Path ")
    public void test_ST_02_02_click_output_path()
            throws InterruptedException {
        String pathBefore = settingsPage.getOutputPath();

        settingsPage.clickOutputPath();
        Thread.sleep(1500);

        // Verify app khong crash
        Assert.assertTrue(settingsPage.isDisplayed(),
                "App crash hoac roi khoi Settings");

        // Verify path khong doi
        String pathAfter = settingsPage.getOutputPath();
        Assert.assertEquals(pathAfter, pathBefore,
                "Output Path khong duoc thay doi sau click");

        ExtentReportManager.getTest().log(Status.PASS,
                "Click Output Path khong crash");
    }
}
