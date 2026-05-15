package testcases.Setting;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.Components.LanguageDialog;
import Pages.SettingsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

import java.util.List;

/**
 * ST_03: Verify Language selection (4 tests).
 * Test 03 destructive (doi sang English), test 04 cleanup (ve Default).
 */
public class Setting03_Verify_Language extends BaseTest {

    private SettingsPage settingsPage;
    private LanguageDialog languageDialog;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP LANGUAGE SUITE ===");
        try {
            settingsPage = RecordFlowHelper.navigateToSettings(driver);
            languageDialog = new LanguageDialog(driver);
        } catch (Exception e) {
            logger.error("Setup error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            settingsPage = RecordFlowHelper.navigateToSettings(driver);
            languageDialog = new LanguageDialog(driver);
        }
    }

    @BeforeMethod
    public void ensureCleanState() {
        // Dong dialog neu lo mo
        if (languageDialog.isDisplayed()) {
            try {
                languageDialog.closeByTapOutside();
                Thread.sleep(800);
            } catch (Exception e) {
                // skip
            }
        }
        // Verify dang o Settings
        if (!settingsPage.isDisplayed()) {
            try {
                settingsPage = RecordFlowHelper.navigateToSettings(driver);
            } catch (Exception e) {
                logger.error("Re-navigate error: " + e.getMessage());
            }
        }
    }

    @AfterMethod
    public void closeDialogIfOpen() {
        if (languageDialog.isDisplayed()) {
            try {
                languageDialog.closeByTapOutside();
                Thread.sleep(600);
            } catch (Exception e) {
                // skip
            }
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        try {
            // Dam bao language ve Default truoc khi roi
            if (settingsPage.isDisplayed()) {
                String currentLang = settingsPage.getLanguageValue();
                if (currentLang != null && !"Default".equalsIgnoreCase(currentLang)) {
                    logger.info("Khoi phuc language ve Default");
                    settingsPage.clickLanguage();
                    Thread.sleep(1000);
                    if (languageDialog.isDisplayed()) {
                        languageDialog.selectLanguage("Default");
                        Thread.sleep(500);
                        languageDialog.clickOk();
                        Thread.sleep(1500);
                    }
                }
            }
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    @Test(priority = 1, description = "ST_03_01: Click Language - mo dialog")
    public void test_ST_03_01_open_language_dialog()
            throws InterruptedException {
        settingsPage.clickLanguage();
        Thread.sleep(1500);

        Assert.assertTrue(languageDialog.isDisplayed(),
                "Language dialog khong mo");
        ExtentReportManager.getTest().log(Status.PASS,
                "Dialog Language mo thanh cong");
    }

    @Test(priority = 2, description = "ST_03_02: Verify danh sach ngon ngu")
    public void test_ST_03_02_verify_language_list()
            throws InterruptedException {
        settingsPage.clickLanguage();
        Thread.sleep(1500);

        Assert.assertTrue(languageDialog.isDisplayed(),
                "Dialog khong mo");

        List<String> languages = languageDialog.getAllLanguages();
        ExtentReportManager.getTest().log(Status.INFO,
                "Languages: " + languages);

        Assert.assertTrue(languages.size() >= 4,
                "Phai co toi thieu 4 ngon ngu: " + languages.size());

        // Verify co Default va English
        Assert.assertTrue(languageDialog.hasLanguage("Default"),
                "Thieu ngon ngu Default");
        Assert.assertTrue(languageDialog.hasLanguage("English"),
                "Thieu ngon ngu English");

        ExtentReportManager.getTest().log(Status.PASS,
                "Co " + languages.size() + " ngon ngu, du Default + English");
    }

    @Test(priority = 3, description = "ST_03_03: Doi ngon ngu sang English (DESTRUCTIVE)")
    public void test_ST_03_03_change_to_english()
            throws InterruptedException {
        // Mo dialog
        settingsPage.clickLanguage();
        Thread.sleep(1500);
        Assert.assertTrue(languageDialog.isDisplayed(), "Dialog khong mo");

        // Chon English
        boolean selected = languageDialog.selectLanguage("English");
        Assert.assertTrue(selected, "Khong chon duoc English");
        Thread.sleep(500);

        // Click OK
        languageDialog.clickOk();
        Thread.sleep(2000);

        // Verify dialog dong
        Assert.assertFalse(languageDialog.isDisplayed(),
                "Dialog KHONG dong sau OK");

        // Verify Language value chuyen sang English
        String langValue = settingsPage.getLanguageValue();
        ExtentReportManager.getTest().log(Status.INFO,
                "Language value sau khi doi: " + langValue);
        Assert.assertEquals(langValue, "English",
                "Language value chua chuyen sang English");

        ExtentReportManager.getTest().log(Status.PASS,
                "Doi sang English thanh cong");
    }

    @Test(priority = 4, description = "ST_03_04: Khoi phuc Language ve Default")
    public void test_ST_03_04_restore_default()
            throws InterruptedException {
        // Mo dialog
        settingsPage.clickLanguage();
        Thread.sleep(1500);
        Assert.assertTrue(languageDialog.isDisplayed(), "Dialog khong mo");

        // Chon Default
        boolean selected = languageDialog.selectLanguage("Default");
        Assert.assertTrue(selected, "Khong chon duoc Default");
        Thread.sleep(500);

        // Click OK
        languageDialog.clickOk();
        Thread.sleep(2000);

        // Verify ve Default
        String langValue = settingsPage.getLanguageValue();
        ExtentReportManager.getTest().log(Status.INFO,
                "Language value sau khi khoi phuc: " + langValue);
        Assert.assertEquals(langValue, "Default",
                "Language khong khoi phuc ve Default");

        ExtentReportManager.getTest().log(Status.PASS,
                "Khoi phuc Default thanh cong");
    }
}