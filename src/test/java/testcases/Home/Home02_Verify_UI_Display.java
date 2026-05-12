package testcases.Home;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import Constants.TimeOutConstants;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.HomePage;
import Report.ExtentReportManager;

/**
 * Test class kiem tra hien thi UI cua man hinh Home.
 * Cover: TC_01 -> TC_10, TC_20
 */
public class Home02_Verify_UI_Display extends BaseTest {

    private HomePage homePage;

    @BeforeMethod
    public void initPage() throws InterruptedException {
        Thread.sleep(TimeOutConstants.SLEEP_SHORT);
        homePage = new HomePage(driver);
    }

    @Test(description = "TC_01: Kiem tra hien thi man hinh Home dung bo cuc")
    public void TC_01_verify_home_layout() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify Home page layout");

        Assert.assertTrue(homePage.isHamburgerMenuDisplayed(), "Hamburger menu khong hien thi");
        Assert.assertTrue(homePage.isWelcomeTextDisplayed(), "Welcome text khong hien thi");
        Assert.assertTrue(homePage.isVoiceChangerTitleDisplayed(), "Voice Changer title khong hien thi");
        Assert.assertTrue(homePage.isRecordCardDisplayed(), "Record card khong hien thi");
        Assert.assertTrue(homePage.isTextToSpeechCardDisplayed(), "Text to Speech card khong hien thi");
        Assert.assertTrue(homePage.isMyAudioCardDisplayed(), "My Audio card khong hien thi");
        Assert.assertTrue(homePage.isImportAudioCardDisplayed(), "Import Audio card khong hien thi");

        ExtentReportManager.getTest().log(Status.PASS, "Tat ca element hien thi day du");
    }

    @Test(description = "TC_02: Kiem tra hien thi Header va tieu de ung dung")
    public void TC_02_verify_header_and_title() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify header and app title");

        Assert.assertTrue(homePage.isHamburgerMenuDisplayed(), "Hamburger menu khong hien thi");
        Assert.assertTrue(homePage.isWelcomeTextDisplayed(), "Welcome text khong hien thi");

        String appName = homePage.getAppNameText();
        ExtentReportManager.getTest().log(Status.INFO, "App name: " + appName);
        Assert.assertEquals(appName, "Voice Changer", "Ten app khong dung");

        ExtentReportManager.getTest().log(Status.PASS, "Header va title hien thi dung");
    }

    @Test(description = "TC_03: Kiem tra hien thi card Record")
    public void TC_03_verify_record_card() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify Record card display");

        Assert.assertTrue(homePage.isRecordCardDisplayed(), "Record card khong hien thi");
        Assert.assertTrue(homePage.isRecordIconDisplayed(), "Record icon khong hien thi");
        Assert.assertTrue(homePage.isRecordTextDisplayed(), "Record text khong hien thi");
        Assert.assertTrue(homePage.isRecordSubtitleDisplayed(), "Record subtitle khong hien thi");

        String recordText = homePage.getRecordCardText();
        String subtitle = homePage.getRecordSubtitleText();
        ExtentReportManager.getTest().log(Status.INFO, "Record text: " + recordText);
        ExtentReportManager.getTest().log(Status.INFO, "Subtitle: " + subtitle);

        Assert.assertEquals(recordText, "Record", "Text 'Record' khong dung");
        Assert.assertEquals(subtitle, "Tap to record and transform your voice",
                "Subtitle Record card khong dung");

        ExtentReportManager.getTest().log(Status.PASS, "Card Record hien thi day du");
    }

    @Test(description = "TC_04: Kiem tra hien thi card Text to Speech")
    public void TC_04_verify_text_to_speech_card() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify Text to Speech card");

        Assert.assertTrue(homePage.isTextToSpeechCardDisplayed(),
                "Text to Speech card khong hien thi");

        String label = homePage.getTextToSpeechLabelText();
        ExtentReportManager.getTest().log(Status.INFO, "Label: " + label);
        Assert.assertEquals(label, "Text to speech", "Label khong dung");

        ExtentReportManager.getTest().log(Status.PASS, "Card Text to Speech hien thi dung");
    }

    @Test(description = "TC_05: Kiem tra hien thi card My Audio")
    public void TC_05_verify_my_audio_card() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify My Audio card");

        Assert.assertTrue(homePage.isMyAudioCardDisplayed(), "My Audio card khong hien thi");

        String label = homePage.getMyAudioLabelText();
        ExtentReportManager.getTest().log(Status.INFO, "Label: " + label);
        Assert.assertEquals(label, "My audio", "Label khong dung");

        ExtentReportManager.getTest().log(Status.PASS, "Card My Audio hien thi dung");
    }

    @Test(description = "TC_06: Kiem tra hien thi card Import Audio")
    public void TC_06_verify_import_audio_card() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify Import Audio card");

        Assert.assertTrue(homePage.isImportAudioCardDisplayed(),
                "Import Audio card khong hien thi");
        Assert.assertTrue(homePage.isImportAudioIconDisplayed(),
                "Import Audio icon khong hien thi");
        Assert.assertTrue(homePage.isImportAudioTextDisplayed(),
                "Import Audio text khong hien thi");
        Assert.assertTrue(homePage.isImportAudioSubtitleDisplayed(),
                "Import Audio subtitle khong hien thi");

        String text = homePage.getImportAudioText();
        String subtitle = homePage.getImportAudioSubtitleText();
        ExtentReportManager.getTest().log(Status.INFO, "Text: " + text);
        ExtentReportManager.getTest().log(Status.INFO, "Subtitle: " + subtitle);

        Assert.assertEquals(text, "Import audio", "Text khong dung");
        Assert.assertEquals(subtitle, "Transform your voice with effects",
                "Subtitle khong dung");

        ExtentReportManager.getTest().log(Status.PASS, "Card Import Audio hien thi day du");
    }

    @Test(description = "TC_07: Kiem tra layout 2 card ngang hang (Text to Speech & My Audio)")
    public void TC_07_verify_two_cards_same_row() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify Text to Speech and My Audio on same row");

        // Lay ket qua y-coordinate cua 2 card
        int ttsY = driver.findElement(By.id("com.bluesoftware.voicechanger:id/layout_text_to_speech"))
                .getLocation().getY();
        int myAudioY = driver.findElement(By.id("com.bluesoftware.voicechanger:id/layout_my_audio"))
                .getLocation().getY();

        ExtentReportManager.getTest().log(Status.INFO,
                "Text to Speech Y: " + ttsY + ", My Audio Y: " + myAudioY);

        // 2 card cung hang nghia la y-coordinate gan nhau (chenh lech < 10px)
        Assert.assertTrue(Math.abs(ttsY - myAudioY) < 10,
                "2 card Text to Speech va My Audio khong cung 1 hang");

        ExtentReportManager.getTest().log(Status.PASS, "2 card cung 1 hang");
    }

    @Test(description = "TC_10: Kiem tra icon watermark tren cac card co hien thi")
    public void TC_10_verify_watermark_icons() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify watermark icons exist on cards");

        // Kiem tra ImageView co ben trong tung card
        Assert.assertTrue(homePage.isRecordIconDisplayed(),
                "Record card thieu icon");
        Assert.assertTrue(homePage.isImportAudioIconDisplayed(),
                "Import Audio card thieu icon");

        ExtentReportManager.getTest().log(Status.PASS, "Cac card co icon hien thi");
    }

    @Test(description = "TC_20: Kiem tra hien thi chinh xac text tren man hinh Home")
    public void TC_20_verify_all_texts() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify all texts on Home screen");

        // Verify tat ca text
        Assert.assertEquals(homePage.getAppNameText(), "Voice Changer",
                "App name sai");
        Assert.assertEquals(homePage.getRecordCardText(), "Record",
                "Record text sai");
        Assert.assertEquals(homePage.getRecordSubtitleText(),
                "Tap to record and transform your voice",
                "Record subtitle sai");
        Assert.assertEquals(homePage.getTextToSpeechLabelText(), "Text to speech",
                "Text to Speech label sai");
        Assert.assertEquals(homePage.getMyAudioLabelText(), "My audio",
                "My Audio label sai");
        Assert.assertEquals(homePage.getImportAudioText(), "Import audio",
                "Import Audio text sai");
        Assert.assertEquals(homePage.getImportAudioSubtitleText(),
                "Transform your voice with effects",
                "Import Audio subtitle sai");

        // Verify "Welcome back" - vi co emoji nen dung contains
        String welcome = homePage.getWelcomeText();
        ExtentReportManager.getTest().log(Status.INFO, "Welcome text: " + welcome);
        Assert.assertTrue(welcome.contains("Welcome back"),
                "Welcome text sai");

        ExtentReportManager.getTest().log(Status.PASS, "Tat ca text hien thi dung");
    }
}