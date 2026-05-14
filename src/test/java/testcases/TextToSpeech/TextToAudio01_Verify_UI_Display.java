package testcases.TextToSpeech;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Pages.TextToAudioPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * Pattern: Share session - mo man Text to Audio 1 lan, chay 8 test verify UI.
 */
public class TextToAudio01_Verify_UI_Display extends BaseTest {

    private TextToAudioPage textToAudioPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP TEXT TO AUDIO SESSION ===");
        try {
            textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
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

    @Test(priority = 1, description = "TTS_01_01: Mo man Text to Audio tu Home")
    public void test_TTS_01_01_navigate_to_text_to_audio() {
        Assert.assertTrue(textToAudioPage.isDisplayed(),
                "Khong vao duoc man Text to Audio");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da vao man Text to Audio");
    }

    @Test(priority = 2, description = "TTS_01_02: Verify nut Back goc tren trai")
    public void test_TTS_01_02_verify_back_button() {
        Assert.assertTrue(textToAudioPage.isBackButtonDisplayed(),
                "Nut Back khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut Back hien thi");
    }

    @Test(priority = 3, description = "TTS_01_03: Verify title 'Text to Audio'")
    public void test_TTS_01_03_verify_title() {
        String title = textToAudioPage.getTitle();
        ExtentReportManager.getTest().log(Status.INFO, "Title: " + title);
        Assert.assertEquals(title, "Text to Audio", "Title sai");
        ExtentReportManager.getTest().log(Status.PASS, "Title dung");
    }

    @Test(priority = 4, description = "TTS_01_04: Verify dropdown ngon ngu mac dinh")
    public void test_TTS_01_04_verify_default_language() {
        Assert.assertTrue(textToAudioPage.isLanguageDropdownDisplayed(),
                "Dropdown ngon ngu khong hien thi");

        String currentLang = textToAudioPage.getCurrentLanguage();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ngon ngu mac dinh: " + currentLang);
        Assert.assertNotNull(currentLang, "Khong lay duoc ngon ngu");
        Assert.assertFalse(currentLang.isEmpty(), "Ngon ngu rong");
        ExtentReportManager.getTest().log(Status.PASS,
                "Dropdown hien thi voi ngon ngu: " + currentLang);
    }

    @Test(priority = 5, description = "TTS_01_05: Verify label 'Enter text' va edit text")
    public void test_TTS_01_05_verify_label_and_edit_text() {
        Assert.assertTrue(textToAudioPage.isLabelEnterTextDisplayed(),
                "Label 'Enter text' khong hien thi");
        Assert.assertTrue(textToAudioPage.isEditTextDisplayed(),
                "Edit text khong hien thi");

        String placeholder = textToAudioPage.getPlaceholderText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Placeholder: " + placeholder);
        ExtentReportManager.getTest().log(Status.PASS,
                "Label va edit text hien thi day du");
    }

    @Test(priority = 6, description = "TTS_01_06: Verify nut Next phia duoi")
    public void test_TTS_01_06_verify_next_button() {
        Assert.assertTrue(textToAudioPage.isNextButtonDisplayed(),
                "Nut Next khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut Next hien thi");
    }

    @Test(priority = 7, description = "TTS_01_07: Verify placeholder/text behavior")
    public void test_TTS_01_07_verify_placeholder_behavior()
            throws InterruptedException {
        String placeholderBefore = textToAudioPage.getPlaceholderText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Text truoc focus: " + placeholderBefore);

        textToAudioPage.clickEditText();
        Thread.sleep(400);  // Giam tu 800

        textToAudioPage.enterText("test");
        Thread.sleep(200);  // Giam tu 300

        String afterEnter = textToAudioPage.getEnteredText();
        Assert.assertTrue(afterEnter != null && afterEnter.contains("test"),
                "Text khong duoc nhap");

        textToAudioPage.clearEditText();
        Thread.sleep(200);
        textToAudioPage.hideKeyboard();

        ExtentReportManager.getTest().log(Status.PASS,
                "Placeholder behavior hoat dong dung");
    }


    @Test(priority = 8, description = "TTS_01_08: Verify nhan Back tu Text to Audio")
    public void test_TTS_01_08_back_to_home() throws InterruptedException {
        textToAudioPage.clickBack();
        Thread.sleep(1000);  // Giam tu 1500

        boolean atHome = driver.findElements(
                By.id("com.bluesoftware.voicechanger:id/layout_record")).size() > 0;
        Assert.assertTrue(atHome, "Khong ve Home");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home");
    }
}