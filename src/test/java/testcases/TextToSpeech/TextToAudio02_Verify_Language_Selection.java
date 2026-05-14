package testcases.TextToSpeech;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import Pages.LanguageDropdownPage;
import Pages.TextToAudioPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

import java.util.List;

/**
 * Test chon ngon ngu - SHARE SESSION pattern.
 * Setup man Text to Audio 1 lan, dam bao state truoc moi test.
 */
public class TextToAudio02_Verify_Language_Selection extends BaseTest {

    private TextToAudioPage textToAudioPage;
    private LanguageDropdownPage langPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP TTS Language Suite ===");
        try {
            textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
            langPage = new LanguageDropdownPage(driver);
        } catch (Exception e) {
            RecordFlowHelper.forceResetToHome(driver);
            textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
            langPage = new LanguageDropdownPage(driver);
        }
    }

    @BeforeMethod
    public void ensureCleanState() {
        // Neu lo o man Language Selection -> press BACK ve Text to Audio
        if (langPage.isDisplayed()) {
            try {
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                Thread.sleep(800);
            } catch (Exception e) {
                logger.warn("Back error: " + e.getMessage());
            }
        }

        // Neu khong o Text to Audio -> navigate lai
        if (!textToAudioPage.isDisplayed()) {
            try {
                RecordFlowHelper.smartResetToHome(driver);
                textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
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

    @Test(priority = 1, description = "TTS_02_01: Mo dropdown ngon ngu")
    public void test_TTS_02_01_open_language_dropdown()
            throws InterruptedException {
        textToAudioPage.clickLanguageDropdown();
        Thread.sleep(800);  // Giam tu 1500

        Assert.assertTrue(langPage.isDisplayed(),
                "Man Language Selection khong mo");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da mo man chon ngon ngu");
    }

    @Test(priority = 2, description = "TTS_02_02: Verify danh sach ngon ngu")
    public void test_TTS_02_02_verify_languages_listed()
            throws InterruptedException {
        textToAudioPage.clickLanguageDropdown();
        Thread.sleep(800);

        Assert.assertTrue(langPage.isDisplayed(), "Man chua mo");

        List<String> langs = langPage.getVisibleLanguages();
        int totalCount = langPage.countVisibleLanguages();

        ExtentReportManager.getTest().log(Status.INFO,
                "Tong: " + totalCount + " ngon ngu");

        Assert.assertTrue(totalCount >= 3,
                "Qua it ngon ngu: " + totalCount);
        ExtentReportManager.getTest().log(Status.PASS,
                totalCount + " ngon ngu visible");
    }

    @Test(priority = 3, description = "TTS_02_03: Chon Tieng Anh (Hoa Ky)")
    public void test_TTS_02_03_select_english_us()
            throws InterruptedException {
        textToAudioPage.clickLanguageDropdown();
        Thread.sleep(800);

        langPage.selectLanguageContains("Anh (Hoa K");
        Thread.sleep(800);  // Giam tu 1500

        Assert.assertTrue(textToAudioPage.isDisplayed(),
                "Khong quay lai Text to Audio");

        String currentLang = textToAudioPage.getCurrentLanguage();
        Assert.assertTrue(
                currentLang.contains("Anh") || currentLang.contains("English"),
                "Ngon ngu khong thay doi: " + currentLang);
        ExtentReportManager.getTest().log(Status.PASS,
                "Da chon: " + currentLang);
    }

    @Test(priority = 4, description = "TTS_02_04: Chon nhieu ngon ngu khac nhau")
    public void test_TTS_02_04_select_multiple_languages()
            throws InterruptedException {
        // Lan 1
        textToAudioPage.clickLanguageDropdown();
        Thread.sleep(800);
        langPage.selectLanguageContains("Australia");
        Thread.sleep(800);

        String lang1 = textToAudioPage.getCurrentLanguage();
        ExtentReportManager.getTest().log(Status.INFO, "Lan 1: " + lang1);

        // Lan 2
        textToAudioPage.clickLanguageDropdown();
        Thread.sleep(800);
        langPage.selectLanguageContains("Vi\u1ec7t");
        Thread.sleep(800);

        String lang2 = textToAudioPage.getCurrentLanguage();
        ExtentReportManager.getTest().log(Status.INFO, "Lan 2: " + lang2);

        Assert.assertNotEquals(lang1, lang2,
                "Ngon ngu khong thay doi");
        ExtentReportManager.getTest().log(Status.PASS,
                lang1 + " -> " + lang2);
    }
}