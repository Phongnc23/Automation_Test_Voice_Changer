package testcases.Setting;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.SettingsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * ST_04: Verify Version (2 tests).
 */
public class Setting04_Verify_Version extends BaseTest {

    private SettingsPage settingsPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP VERSION SUITE ===");
        try {
            settingsPage = RecordFlowHelper.navigateToSettings(driver);
        } catch (Exception e) {
            logger.error("Setup error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            settingsPage = RecordFlowHelper.navigateToSettings(driver);
        }
    }

    @BeforeMethod
    public void ensureCleanState() {
        if (!settingsPage.isDisplayed()) {
            try {
                settingsPage = RecordFlowHelper.navigateToSettings(driver);
            } catch (Exception e) {
                logger.error("Re-navigate error: " + e.getMessage());
            }
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    @Test(priority = 1, description = "ST_04_01: Verify Version hien thi")
    public void test_ST_04_01_verify_version_display() {
        String version = settingsPage.getVersionValue();
        ExtentReportManager.getTest().log(Status.INFO,
                "Version: " + version);

        Assert.assertNotNull(version, "Version null");
        Assert.assertTrue(version.length() > 0, "Version rong");

        // Verify format (vd: "1.0", "1.0.1")
        Assert.assertTrue(version.matches("\\d+(\\.\\d+)+"),
                "Version sai format: " + version);

        ExtentReportManager.getTest().log(Status.PASS,
                "Version dung format: " + version);
    }

    @Test(priority = 2, description = "ST_04_02: Click Version - khong crash")
    public void test_ST_04_02_click_version_no_crash()
            throws InterruptedException {
        String versionBefore = settingsPage.getVersionValue();

        settingsPage.clickVersion();
        Thread.sleep(1500);

        // Verify app khong crash, van o Settings
        Assert.assertTrue(settingsPage.isDisplayed(),
                "App crash hoac roi khoi Settings");

        String versionAfter = settingsPage.getVersionValue();
        Assert.assertEquals(versionAfter, versionBefore,
                "Version thay doi sau click");

        ExtentReportManager.getTest().log(Status.PASS,
                "Click Version khong crash");
    }
}