package testcases.TextToSpeech;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.TextToAudioPage;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * Test nhap text + nhan Next.
 * HYBRID PATTERN:
 * - Test 02, 06 (validation, KHONG chuyen man): share session
 * - Test 01, 03, 04, 05 (E2E chuyen Voice Effects): reset
 *
 * Priority:
 * - 1-2: Validation tests (share session, chay truoc)
 * - 10-13: E2E tests (reset each, chay sau)
 */
public class TextToAudio03_Verify_Text_Input extends BaseTest {

    private TextToAudioPage textToAudioPage;
    private VoiceEffectsPage voiceEffectsPage;

    private boolean sessionSetup = false;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP TTS Text Input Suite ===");
        try {
            textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
            voiceEffectsPage = new VoiceEffectsPage(driver);
            sessionSetup = true;
        } catch (Exception e) {
            RecordFlowHelper.forceResetToHome(driver);
            textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
            voiceEffectsPage = new VoiceEffectsPage(driver);
            sessionSetup = true;
        }
    }

    @BeforeMethod
    public void ensureState() {
        // Cho validation tests (priority < 10): chi can o man TTS
        // Cho E2E tests (priority >= 10): can fresh state

        if (!textToAudioPage.isDisplayed()) {
            try {
                RecordFlowHelper.smartResetToHome(driver);
                textToAudioPage = RecordFlowHelper.navigateToTextToAudio(driver);
                voiceEffectsPage = new VoiceEffectsPage(driver);
            } catch (Exception e) {
                logger.error("Re-navigate error: " + e.getMessage());
            }
        }

        // Clear text neu lo nhap tu test truoc
        try {
            textToAudioPage.clearEditText();
        } catch (Exception e) {
            // skip
        }
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupBetweenTests() {
        // Sau E2E test, can ve Audio Saved -> Home -> TTS lai
        // Cho cleanup logic xu ly trong @BeforeMethod
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    // ========================================
    // VALIDATION TESTS (priority 1-2, share session)
    // ========================================

    @Test(priority = 1, description = "TTS_03_02: Nhan Next khi text rong")
    public void test_TTS_03_02_click_next_with_empty_text()
            throws InterruptedException {
        textToAudioPage.clickNext();
        Thread.sleep(1500);  // Giam tu 2000

        Assert.assertTrue(textToAudioPage.isDisplayed(),
                "Da chuyen man dau du text rong");
        Assert.assertFalse(voiceEffectsPage.isDisplayed(),
                "Da vao Voice Effects khi text rong");
        ExtentReportManager.getTest().log(Status.PASS,
                "App khong Next khi text rong");
    }

    @Test(priority = 2, description = "TTS_03_06: Clear text + nhan Next")
    public void test_TTS_03_06_clear_text_then_next()
            throws InterruptedException {
        textToAudioPage.clickEditText();
        Thread.sleep(300);  // Giam tu 500
        textToAudioPage.enterText("Test text se bi xoa");
        Thread.sleep(200);  // Giam tu 300

        textToAudioPage.clearEditText();
        Thread.sleep(300);
        textToAudioPage.hideKeyboard();
        Thread.sleep(300);

        textToAudioPage.clickNext();
        Thread.sleep(1500);

        Assert.assertTrue(textToAudioPage.isDisplayed(),
                "Da chuyen man du text rong");
        Assert.assertFalse(voiceEffectsPage.isDisplayed(),
                "Da vao Voice Effects");
        ExtentReportManager.getTest().log(Status.PASS,
                "App khong Next sau clear");
    }

    // ========================================
    // E2E TESTS (priority 10+, reset state)
    // ========================================

    @Test(priority = 10, description = "TTS_03_01: Nhap text va nhan Next")
    public void test_TTS_03_01_enter_text_and_click_next()
            throws InterruptedException {
        String testText = "Xin chao toi la tro ly ao";

        textToAudioPage.clickEditText();
        Thread.sleep(300);
        textToAudioPage.enterText(testText);
        Thread.sleep(200);
        textToAudioPage.hideKeyboard();
        Thread.sleep(300);

        textToAudioPage.clickNext();
        Thread.sleep(2500);  // Giam tu 4000

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong chuyen sang Voice Effects");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da chuyen Voice Effects");
    }

    @Test(priority = 11, description = "TTS_03_03: Text co dau tieng Viet")
    public void test_TTS_03_03_enter_vietnamese_text()
            throws InterruptedException {
        String testText = "Tieng Viet co o, u, a, a, e, o";

        textToAudioPage.clickEditText();
        Thread.sleep(300);
        textToAudioPage.enterText(testText);
        Thread.sleep(200);
        textToAudioPage.hideKeyboard();
        Thread.sleep(300);

        textToAudioPage.clickNext();
        Thread.sleep(2500);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong chuyen Voice Effects");
        ExtentReportManager.getTest().log(Status.PASS,
                "Generate voi text co dau");
    }

    @Test(priority = 12, description = "TTS_03_04: Text dai (>200 ky tu)")
    public void test_TTS_03_04_enter_long_text()
            throws InterruptedException {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            longText.append("Day la text thu nghiem rat dai. ");
        }

        textToAudioPage.clickEditText();
        Thread.sleep(300);
        textToAudioPage.enterText(longText.toString());
        Thread.sleep(200);
        textToAudioPage.hideKeyboard();
        Thread.sleep(300);

        textToAudioPage.clickNext();
        Thread.sleep(4000);  // Text dai mat thoi gian generate hon

        boolean atVE = voiceEffectsPage.isDisplayed();
        boolean atTTS = textToAudioPage.isDisplayed();
        Assert.assertTrue(atVE || atTTS, "App stuck");

        if (atVE) {
            ExtentReportManager.getTest().log(Status.PASS,
                    "Generate text dai thanh cong");
        } else {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Co gioi han ky tu");
        }
    }

    @Test(priority = 13, description = "TTS_03_05: Text co so va ky tu dac biet")
    public void test_TTS_03_05_enter_text_with_numbers()
            throws InterruptedException {
        String testText = "Goi 0912345678 hoac email test@email.com";

        textToAudioPage.clickEditText();
        Thread.sleep(300);
        textToAudioPage.enterText(testText);
        Thread.sleep(200);
        textToAudioPage.hideKeyboard();
        Thread.sleep(300);

        textToAudioPage.clickNext();
        Thread.sleep(2500);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong chuyen Voice Effects");
        ExtentReportManager.getTest().log(Status.PASS,
                "Generate voi so + ky tu dac biet");
    }
}