package testcases.Setting;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.SettingsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * ST_01: Verify UI man Settings (5 tests).
 * Pattern: Share session - mo Settings 1 lan.
 */
public class Setting01_Verify_UI_Display extends BaseTest {

    private SettingsPage settingsPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP SETTINGS UI SUITE ===");
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

    @Test(priority = 1, description = "ST_01_01: Mo Settings tu Drawer")
    public void test_ST_01_01_open_settings() {
        Assert.assertTrue(settingsPage.isDisplayed(),
                "Khong vao duoc Settings");
        ExtentReportManager.getTest().log(Status.PASS,
                "Vao Settings thanh cong");
    }

    @Test(priority = 2, description = "ST_01_02: Verify Back va title")
    public void test_ST_01_02_verify_back_and_title() {
        Assert.assertTrue(settingsPage.isBackButtonDisplayed(),
                "btnBack khong hien thi");

        String title = settingsPage.getTitle();
        ExtentReportManager.getTest().log(Status.INFO,
                "Title: " + title);
        Assert.assertNotNull(title, "Title null");
        Assert.assertEquals(title, "Settings",
                "Title sai: " + title);
        ExtentReportManager.getTest().log(Status.PASS,
                "Back va title dung");
    }

    @Test(priority = 3, description = "ST_01_03: Verify nhom General")
    public void test_ST_01_03_verify_general_group() {
        Assert.assertTrue(settingsPage.isRowOutputPathDisplayed(),
                "rowOutputPath khong hien thi");
        Assert.assertTrue(settingsPage.isRowLanguageDisplayed(),
                "rowLanguage khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS,
                "Nhom General co Output Path va Language");
    }

    @Test(priority = 4, description = "ST_01_04: Verify nhom About")
    public void test_ST_01_04_verify_about_group() {
        Assert.assertTrue(settingsPage.isRowPrivacyDisplayed(),
                "rowPrivacy khong hien thi");
        Assert.assertTrue(settingsPage.isRowVersionDisplayed(),
                "rowVersion khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS,
                "Nhom About co Privacy va Version");
    }

    @Test(priority = 5, description = "ST_01_05: Back ve Home")
    public void test_ST_01_05_back_to_home() throws InterruptedException {
        settingsPage.clickBack();
        Thread.sleep(1500);

        boolean atHome = driver.findElements(
                By.id("com.bluesoftware.voicechanger:id/layout_record")
        ).size() > 0;
        Assert.assertTrue(atHome, "Khong ve Home");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da ve Home");
    }
}