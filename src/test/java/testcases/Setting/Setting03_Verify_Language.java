package testcases.Setting;

import Base.BaseSharedSessionTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import Pages.Components.LanguageDialog;
import Pages.SettingsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

import java.time.Duration;
import java.util.List;

/**
 * ST_03: Verify Language selection (4 tests).
 * Test 03 destructive (doi sang English), test 04 cleanup (ve Default).
 *
 * @AfterClass restoreDefaultLanguage la safety net - dam bao language
 * tro ve Default ke ca khi test 04 fail.
 */
public class Setting03_Verify_Language extends BaseSharedSessionTest {

    private SettingsPage settingsPage;
    private LanguageDialog languageDialog;

    @Override
    protected void navigateToScreen() {
        settingsPage = RecordFlowHelper.navigateToSettings(driver);
        languageDialog = new LanguageDialog(driver);
    }

    @Override
    protected boolean isAtExpectedScreen() {
        if (!RecordFlowHelper.isAtSettings(driver)) return false;
        // Dong dialog neu lo mo
        if (languageDialog != null && languageDialog.isDisplayed()) {
            try {
                languageDialog.closeByTapOutside();
                Thread.sleep(800);
            } catch (Exception e) {
                // skip
            }
        }
        return true;
    }

    @AfterClass(alwaysRun = true)
    public void restoreDefaultLanguage() {
        try {
            // Dam bao language ve Default truoc khi roi
            if (settingsPage != null && settingsPage.isDisplayed()) {
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
        } catch (Exception e) {
            logger.error("Restore default lang error: " + e.getMessage());
        }
    }

    /** L1: Smart wait language dialog mo - thay sleep(1500) co dinh. */
    private void waitForLanguageDialog(int maxSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(maxSeconds))
                    .ignoring(Exception.class)
                    .until(d -> languageDialog.isDisplayed());
        } catch (Exception e) {
            // se fail o assertion phia duoi
        }
    }

    @Test(priority = 1, description = "ST_03_01: Click Language - mo dialog")
    public void test_ST_03_01_open_language_dialog()
            throws InterruptedException {
        settingsPage.clickLanguage();
        waitForLanguageDialog(3);

        Assert.assertTrue(languageDialog.isDisplayed(),
                "Language dialog khong mo");
        ExtentReportManager.getTest().log(Status.PASS,
                "Dialog Language mo thanh cong");
    }

    @Test(priority = 2, description = "ST_03_02: Verify danh sach ngon ngu")
    public void test_ST_03_02_verify_language_list()
            throws InterruptedException {
        settingsPage.clickLanguage();
        waitForLanguageDialog(3);

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
        waitForLanguageDialog(3);
        Assert.assertTrue(languageDialog.isDisplayed(), "Dialog khong mo");

        // Chon English
        boolean selected = languageDialog.selectLanguage("English");
        Assert.assertTrue(selected, "Khong chon duoc English");
        Thread.sleep(500);

        // Click OK - smart wait dialog dong
        languageDialog.clickOk();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> !languageDialog.isDisplayed());
        } catch (Exception e) {
            // se fail o assertion phia duoi
        }

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
        waitForLanguageDialog(3);
        Assert.assertTrue(languageDialog.isDisplayed(), "Dialog khong mo");

        // Chon Default
        boolean selected = languageDialog.selectLanguage("Default");
        Assert.assertTrue(selected, "Khong chon duoc Default");
        Thread.sleep(500);

        // Click OK - smart wait dialog dong
        languageDialog.clickOk();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> !languageDialog.isDisplayed());
        } catch (Exception e) {
            // se fail o assertion phia duoi
        }

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
