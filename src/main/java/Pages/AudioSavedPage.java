package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AudioSavedPage extends BasePage {

    // ... (giu nguyen cac locator hien tai) ...

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

    // ========== TOAST/SNACKBAR DETECTION ==========

    /**
     * Locator cho toast "Ringtone set successfully" - dung UiAutomator
     * textContains de match toast.
     */
    private static final By RINGTONE_SUCCESS_TOAST =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Ringtone set\")");

    /**
     * Locator generic cho toast bat ky.
     */
    private static final By ANY_TOAST =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().className(\"android.widget.Toast\")");

    public AudioSavedPage(AppiumDriver driver) {
        super(driver);
    }

    // ... (giu nguyen cac method UI verify, get info, click) ...

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

    public String parseDuration() {
        String info = getFileInfo();
        if (info == null) return null;
        String[] parts = info.split("·|\\.");
        return parts.length > 0 ? parts[0].trim() : null;
    }

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
        logger.info("Click icon home");
        click(HOME_BUTTON);
    }

    public void clickThreeDotMenu() {
        logger.info("Click 3 cham (more icon)");
        click(MORE_ICON);
    }

    public void clickMoreOptions() {
        clickThreeDotMenu();
    }

    public void clickSetAsRingtone() {
        logger.info("Click Set as Ringtone");
        click(SET_RINGTONE_BUTTON);
    }

    public void clickShare() {
        logger.info("Click Share");
        click(SHARE_BUTTON);
    }


    // ========== TOAST DETECTION ==========


    public boolean waitForRingtoneSuccessToast(int timeoutMs) {
        logger.info("Wait for ringtone success toast (max " + timeoutMs + "ms)");
        long startTime = System.currentTimeMillis();
        int pollInterval = 200;

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            try {
                // Cach 1: Tim toast bang text contains
                List<WebElement> elements = driver.findElements(
                        RINGTONE_SUCCESS_TOAST);
                if (!elements.isEmpty()) {
                    String text = elements.get(0).getText();
                    logger.info("Phat hien toast: " + text);
                    return true;
                }

                // Cach 2: Tim toast widget bat ky
                List<WebElement> toasts = driver.findElements(ANY_TOAST);
                if (!toasts.isEmpty()) {
                    String text = toasts.get(0).getText();
                    logger.info("Phat hien toast widget: " + text);
                    if (text != null && text.toLowerCase().contains("ringtone")) {
                        return true;
                    }
                }
            } catch (Exception e) {
                // Continue polling
            }

            try {
                Thread.sleep(pollInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        logger.warn("Khong phat hien toast trong " + timeoutMs + "ms");
        return false;
    }

    /**
     * Wait toast voi default timeout 3000ms.
     */
    public boolean waitForRingtoneSuccessToast() {
        return waitForRingtoneSuccessToast(3000);
    }

    /**
     * Snapshot text cua toast neu co.
     */
    public String getToastText() {
        try {
            List<WebElement> elements = driver.findElements(ANY_TOAST);
            if (!elements.isEmpty()) {
                return elements.get(0).getText();
            }
            elements = driver.findElements(RINGTONE_SUCCESS_TOAST);
            if (!elements.isEmpty()) {
                return elements.get(0).getText();
            }
        } catch (Exception e) {
            logger.warn("Get toast text error: " + e.getMessage());
        }
        return null;
    }
}