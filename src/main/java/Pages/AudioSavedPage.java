package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Page Object cho man hinh Audio Saved Successfully.
 * Truy cap: Voice Effects -> click Save.
 */
public class AudioSavedPage extends BasePage {

    private static final By CLOSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnClose");
    private static final By HOME_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnHome");
    private static final By SUCCESS_ICON =
            By.id("com.bluesoftware.voicechanger:id/imgSuccess");
    private static final By TITLE =
            By.id("com.bluesoftware.voicechanger:id/tvTitle");
    private static final By AUDIO_LAYOUT =
            By.id("com.bluesoftware.voicechanger:id/layoutAudio");
    private static final By FILE_NAME =
            By.id("com.bluesoftware.voicechanger:id/tvFileName");
    private static final By FILE_INFO =
            By.id("com.bluesoftware.voicechanger:id/tvInfo");
    private static final By MORE_ICON =
            By.id("com.bluesoftware.voicechanger:id/image_more");
    private static final By SET_RINGTONE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnSetRingtone");
    private static final By SHARE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnSend");

    public AudioSavedPage(AppiumDriver driver) {
        super(driver);
    }

    // ========== UI VERIFY ==========

    public boolean isDisplayed() {
        try {
            return driver.findElements(TITLE).size() > 0
                    && driver.findElements(SUCCESS_ICON).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSuccessIconDisplayed() {
        return driver.findElements(SUCCESS_ICON).size() > 0;
    }

    public String getTitle() {
        try {
            return driver.findElement(TITLE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isAudioInfoCardDisplayed() {
        return driver.findElements(AUDIO_LAYOUT).size() > 0;
    }

    public String getFileName() {
        try {
            return driver.findElement(FILE_NAME).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getFileInfo() {
        try {
            return driver.findElement(FILE_INFO).getText();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parse duration tu fileInfo "00:03 . 56 KB" -> "00:03"
     */
    public String parseDuration() {
        String info = getFileInfo();
        if (info == null) return null;
        String[] parts = info.split("·|\\.");
        return parts.length > 0 ? parts[0].trim() : null;
    }

    /**
     * Parse size tu fileInfo "00:03 . 56 KB" -> "56 KB"
     */
    public String parseFileSize() {
        String info = getFileInfo();
        if (info == null) return null;
        String[] parts = info.split("·|\\.");
        return parts.length > 1 ? parts[1].trim() : null;
    }

    public boolean isMoreIconDisplayed() {
        return driver.findElements(MORE_ICON).size() > 0;
    }

    public boolean isSetRingtoneButtonDisplayed() {
        return driver.findElements(SET_RINGTONE_BUTTON).size() > 0;
    }

    public boolean isShareButtonDisplayed() {
        return driver.findElements(SHARE_BUTTON).size() > 0;
    }

    public boolean isHomeButtonDisplayed() {
        return driver.findElements(HOME_BUTTON).size() > 0;
    }

    // ========== ACTIONS ==========

    public void clickClose() {
        logger.info("Click nut X tren Audio Saved");
        click(CLOSE_BUTTON);
    }

    public void clickHomeButton() {
        logger.info("Click nut Home");
        click(HOME_BUTTON);
    }

    public void clickThreeDotMenu() {
        logger.info("Click 3 cham (more icon)");
        click(MORE_ICON);
    }

    public void clickSetAsRingtone() {
        logger.info("Click Set as Ringtone");
        click(SET_RINGTONE_BUTTON);
    }

    public void clickShare() {
        logger.info("Click Share");
        click(SHARE_BUTTON);
    }
}