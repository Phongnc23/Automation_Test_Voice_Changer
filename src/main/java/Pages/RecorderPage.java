package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import Pages.Components.DiscardDialog;

import java.time.Duration;

/**
 * Page Object cho man hinh Recorder.
 * Truy cap tu: Home -> click Record card.
 */
public class RecorderPage extends BasePage {

    private static final By CLOSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/image_close");
    private static final By TITLE =
            By.xpath("//android.widget.TextView[@text='Recorder']");
    private static final By TIMER =
            By.id("com.bluesoftware.voicechanger:id/text_duration");
    private static final By WAVEFORM =
            By.id("com.bluesoftware.voicechanger:id/waveform_view");
    private static final By READY_TO_RECORD =
            By.xpath("//android.widget.TextView[@text='Ready to record']");
    private static final By RECORD_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/image_record");

    public RecorderPage(AppiumDriver driver) {
        super(driver);
    }

    // ========== UI VERIFY ==========

    public boolean isDisplayed() {
        try {
            return driver.findElements(RECORD_BUTTON).size() > 0
                    && driver.findElements(TIMER).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCloseButtonDisplayed() {
        return driver.findElements(CLOSE_BUTTON).size() > 0;
    }

    public boolean isTitleDisplayed() {
        return driver.findElements(TITLE).size() > 0;
    }

    public boolean isTimerDisplayed() {
        return driver.findElements(TIMER).size() > 0;
    }

    public String getTimerText() {
        try {
            return driver.findElement(TIMER).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isReadyToRecordDisplayed() {
        return driver.findElements(READY_TO_RECORD).size() > 0;
    }

    public boolean isMicrophoneButtonDisplayed() {
        return driver.findElements(RECORD_BUTTON).size() > 0;
    }

    public boolean isWaveformDisplayed() {
        return driver.findElements(WAVEFORM).size() > 0;
    }

    public boolean isRecording() {
        // Dang ghi am neu timer khac 00:00:00
        String timer = getTimerText();
        return timer != null && !timer.equals("00:00:00");
    }

    // ========== ACTIONS ==========

    public void clickClose() {
        logger.info("Click nut X (close) tren Recorder");
        click(CLOSE_BUTTON);
    }

    public void clickRecordButton() {
        logger.info("Click nut microphone (record/stop)");
        click(RECORD_BUTTON);
    }

    /**
     * Cho dong ho thay doi tu gia tri initial trong timeout giay.
     */
    public boolean waitForTimerChange(String initialValue, int timeoutSec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
            wait.until(d -> {
                try {
                    String current = d.findElement(TIMER).getText();
                    return current != null && !current.equals(initialValue);
                } catch (Exception e) {
                    return false;
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ========== DISCARD DIALOG ==========
    // Dialog hien khi nhan X luc dang ghi am

    public DiscardDialog getDiscardDialog() {
        return new DiscardDialog(driver);
    }

    public boolean isDiscardDialogDisplayed() {
        return new DiscardDialog(driver).isDisplayed();
    }

    /**
     * Click Cancel tren dialog -> tiep tuc ghi am.
     */
    public void clickDiscardDialogCancel() {
        new DiscardDialog(driver).clickCancel();
    }

    /**
     * Click Discard tren dialog -> huy ghi am, ve Home.
     */
    public void clickDiscardDialogConfirm() {
        new DiscardDialog(driver).clickDiscard();
    }

    // ========== LEGACY METHODS (giu backward compat voi test cu) ==========

    public boolean isConfirmCancelDialogDisplayed() {
        return isDiscardDialogDisplayed();
    }

    public void clickConfirmCancel() {
        clickDiscardDialogConfirm();
    }

    public void clickDismissCancelDialog() {
        clickDiscardDialogCancel();
    }
}