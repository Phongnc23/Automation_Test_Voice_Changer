package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    // ====== LOCATORS ======
    // Header
    private final By hamburgerMenuIcon = By.id("com.bluesoftware.voicechanger:id/image_menu");
    private final By welcomeText = By.id("com.bluesoftware.voicechanger:id/text_welcome");
    private final By voiceChangerTitle = By.id("com.bluesoftware.voicechanger:id/text_app_name");

    // Record card
    private final By recordCard = By.id("com.bluesoftware.voicechanger:id/layout_record");
    private final By recordIcon = By.id("com.bluesoftware.voicechanger:id/image_record");
    private final By recordText = By.id("com.bluesoftware.voicechanger:id/text_record");
    private final By recordSubtitle = By.xpath("//android.widget.TextView[@text='Tap to record and transform your voice']");

    // Text to Speech card
    private final By textToSpeechCard = By.id("com.bluesoftware.voicechanger:id/layout_text_to_speech");
    private final By textToSpeechLabel = By.xpath("//android.widget.TextView[@text='Text to speech']");
    private static final By LAYOUT_TEXT_TO_SPEECH = By.id("com.bluesoftware.voicechanger:id/layout_text_to_speech");
    // My Audio card
    private final By myAudioCard = By.id("com.bluesoftware.voicechanger:id/layout_my_audio");
    private final By myAudioLabel = By.xpath("//android.widget.TextView[@text='My audio']");

    // Import Audio card
    private final By importAudioCard = By.id("com.bluesoftware.voicechanger:id/layout_choose_file");
    private final By importAudioIcon = By.id("com.bluesoftware.voicechanger:id/image_file");
    private final By importAudioText = By.id("com.bluesoftware.voicechanger:id/text_folder");
    private final By importAudioSubtitle = By.xpath("//android.widget.TextView[@text='Transform your voice with effects']");

    public HomePage(AppiumDriver driver) {
        super(driver);
    }

    // ====== VERIFY DISPLAY METHODS ======

    public boolean isHomePageDisplayed() {
        logger.info("Verify Home page is displayed");
        return isDisplayed(voiceChangerTitle);
    }

    public boolean isHamburgerMenuDisplayed() {
        return isDisplayed(hamburgerMenuIcon);
    }

    public boolean isWelcomeTextDisplayed() {
        return isDisplayed(welcomeText);
    }

    public boolean isVoiceChangerTitleDisplayed() {
        return isDisplayed(voiceChangerTitle);
    }

    // Record card
    public boolean isRecordCardDisplayed() {
        return isDisplayed(recordCard);
    }

    public boolean isRecordIconDisplayed() {
        return isDisplayed(recordIcon);
    }

    public boolean isRecordTextDisplayed() {
        return isDisplayed(recordText);
    }

    public boolean isRecordSubtitleDisplayed() {
        return isDisplayed(recordSubtitle);
    }

    // Text to Speech
    public boolean isTextToSpeechCardDisplayed() {
        return driver.findElements(LAYOUT_TEXT_TO_SPEECH).size() > 0;
    }

    public boolean isTextToSpeechLabelDisplayed() {
        return isDisplayed(textToSpeechLabel);
    }

    // My Audio
    public boolean isMyAudioCardDisplayed() {
        return isDisplayed(myAudioCard);
    }

    public boolean isMyAudioLabelDisplayed() {
        return isDisplayed(myAudioLabel);
    }

    // Import Audio
    public boolean isImportAudioCardDisplayed() {
        return isDisplayed(importAudioCard);
    }

    public boolean isImportAudioIconDisplayed() {
        return isDisplayed(importAudioIcon);
    }

    public boolean isImportAudioTextDisplayed() {
        return isDisplayed(importAudioText);
    }

    public boolean isImportAudioSubtitleDisplayed() {
        return isDisplayed(importAudioSubtitle);
    }

    // ====== GET TEXT METHODS ======

    public String getWelcomeText() {
        return getText(welcomeText);
    }

    public String getAppNameText() {
        return getText(voiceChangerTitle);
    }

    public String getRecordCardText() {
        return getText(recordText);
    }

    public String getRecordSubtitleText() {
        return getText(recordSubtitle);
    }

    public String getTextToSpeechLabelText() {
        return getText(textToSpeechLabel);
    }

    public String getMyAudioLabelText() {
        return getText(myAudioLabel);
    }

    public String getImportAudioText() {
        return getText(importAudioText);
    }

    public String getImportAudioSubtitleText() {
        return getText(importAudioSubtitle);
    }

    // ====== ACTION METHODS ======

    public void clickHamburgerMenu() {
        logger.info("Click hamburger menu icon");
        click(hamburgerMenuIcon);
    }

    public void clickRecord() {
        logger.info("Click Record card");
        click(recordCard);
    }

    public void clickTextToSpeech() {
        logger.info("Click Text to Speech card");
        click(textToSpeechCard);
    }

    public void clickMyAudio() {
        logger.info("Click My Audio card");
        click(myAudioCard);
    }

    public void clickImportAudio() {
        logger.info("Click Import Audio card");
        click(importAudioCard);
    }

}