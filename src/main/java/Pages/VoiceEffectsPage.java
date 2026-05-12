package Pages;

import Base.BasePage;
import Constants.TimeOutConstants;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Page Object cho man hinh Voice Effects.
 * Truy cap: Recorder -> Stop ghi am.
 */
public class VoiceEffectsPage extends BasePage {

    // Tat ca effects co trong app
    public static final List<String> ALL_EFFECTS = Arrays.asList(
            "Normal", "Man", "Woman", "Child", "Penguin", "Monster",
            "Fast", "Slow", "Alien", "Zombie", "Drunk", "Helium",
            "Death", "Robot", "Baby", "Echo", "Underwater", "Telephone",
            "Parody", "Bass", "Tenor", "Bee", "Fade"
    );

    private static final By HEADER_LAYOUT =
            By.id("com.bluesoftware.voicechanger:id/layoutHeader");
    private static final By CLOSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnClose");
    private static final By TITLE =
            By.id("com.bluesoftware.voicechanger:id/tvTitle");
    private static final By AUDIO_LAYOUT =
            By.id("com.bluesoftware.voicechanger:id/layoutAudio");
    private static final By WAVE_ICON =
            By.id("com.bluesoftware.voicechanger:id/image_wave");
    private static final By AUDIO_NAME =
            By.id("com.bluesoftware.voicechanger:id/tvAudioName");
    private static final By PLAY_PAUSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnPlayPause");
    private static final By SEEK_BAR =
            By.id("com.bluesoftware.voicechanger:id/seekBar");
    private static final By TIME_TEXT =
            By.id("com.bluesoftware.voicechanger:id/tvTime");
    private static final By EFFECTS_RECYCLER =
            By.id("com.bluesoftware.voicechanger:id/rvEffects");
    private static final By SEND_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnSend");
    private static final By SAVE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnSave");

    // Discard dialog - dung XPath theo text
    private static final By DISCARD_DIALOG_TITLE = By.xpath(
            "//*[contains(@text,'Discard') or contains(@text,'unsaved')]");
    private static final By DISCARD_CANCEL_BTN =
            By.xpath("//*[@text='Cancel']");
    private static final By DISCARD_CONFIRM_BTN =
            By.xpath("//*[@text='Discard']");

    public VoiceEffectsPage(AppiumDriver driver) {
        super(driver);
    }

    // ========== UI VERIFY ==========

    public boolean isDisplayed() {
        try {
            return driver.findElements(TITLE).size() > 0
                    && driver.findElements(SAVE_BUTTON).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCloseButtonDisplayed() {
        return driver.findElements(CLOSE_BUTTON).size() > 0;
    }

    public String getTitle() {
        try {
            return driver.findElement(TITLE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isAudioPlayerDisplayed() {
        return driver.findElements(AUDIO_LAYOUT).size() > 0
                && driver.findElements(PLAY_PAUSE_BUTTON).size() > 0;
    }

    public String getAudioFileName() {
        try {
            return driver.findElement(AUDIO_NAME).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getTimeText() {
        try {
            return driver.findElement(TIME_TEXT).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isPlayPauseButtonDisplayed() {
        return driver.findElements(PLAY_PAUSE_BUTTON).size() > 0;
    }

    public boolean isEffectsRecyclerDisplayed() {
        return driver.findElements(EFFECTS_RECYCLER).size() > 0;
    }

    public boolean isSendButtonDisplayed() {
        return driver.findElements(SEND_BUTTON).size() > 0;
    }

    public boolean isSaveButtonDisplayed() {
        return driver.findElements(SAVE_BUTTON).size() > 0;
    }

    // ========== ACTIONS ==========

    public void clickClose() {
        logger.info("Click nut X tren Voice Effects");
        click(CLOSE_BUTTON);
    }

    public void clickPlayPause() {
        logger.info("Click Play/Pause button");
        click(PLAY_PAUSE_BUTTON);
    }

    public void clickSave() {
        logger.info("Click Save");
        click(SAVE_BUTTON);
    }

    public void clickSendVoiceMessage() {
        logger.info("Click Send Voice Message");
        click(SEND_BUTTON);
    }

    /**
     * Check audio dang phat bang cach so sanh tvTime sau 2s.
     */
    public boolean isAudioPlaying() {
        try {
            String time1 = getTimeText();
            Thread.sleep(2000);
            String time2 = getTimeText();
            return time1 != null && time2 != null && !time1.equals(time2);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Locator dynamic cho 1 effect cu the.
     */
    public By getEffectLocator(String effectName) {
        return By.xpath(
                "//android.widget.TextView[@resource-id=" +
                        "'com.bluesoftware.voicechanger:id/tv_effect_name' " +
                        "and @text='" + effectName + "']");
    }

    /**
     * Click chon 1 effect. Tu dong scroll trong RecyclerView neu can.
     */
    public void clickEffect(String effectName) {
        logger.info("Click effect: " + effectName);

        // Scroll trong rvEffects de tim effect
        try {
            String uiAutomator = String.format(
                    "new UiScrollable(new UiSelector()" +
                            ".resourceId(\"com.bluesoftware.voicechanger:id/rvEffects\"))" +
                            ".scrollIntoView(new UiSelector().text(\"%s\"))",
                    effectName);
            driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(uiAutomator));
        } catch (Exception e) {
            logger.warn("Cannot scroll to effect " + effectName + ": " + e.getMessage());
        }

        // Click effect
        click(getEffectLocator(effectName));

        // Doi effect apply
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isEffectDisplayed(String effectName) {
        try {
            // Thu scroll de tim
            String uiAutomator = String.format(
                    "new UiScrollable(new UiSelector()" +
                            ".resourceId(\"com.bluesoftware.voicechanger:id/rvEffects\"))" +
                            ".scrollIntoView(new UiSelector().text(\"%s\"))",
                    effectName);
            driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(uiAutomator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ========== DISCARD DIALOG ==========

    public boolean isDiscardDialogDisplayed() {
        try {
            Thread.sleep(1000);
            return driver.findElements(DISCARD_DIALOG_TITLE).size() > 0
                    || driver.findElements(DISCARD_CONFIRM_BTN).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickDiscardCancel() {
        logger.info("Click Cancel tren Discard dialog");
        click(DISCARD_CANCEL_BTN);
    }

    public void clickDiscardConfirm() {
        logger.info("Click Discard confirm");
        click(DISCARD_CONFIRM_BTN);
    }
}