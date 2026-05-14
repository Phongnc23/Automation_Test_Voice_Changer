package Utils;

import Constants.AppConstants;
import Constants.TimeOutConstants;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Pages.AudioSavedPage;
import Pages.HomePage;
import Pages.RecorderPage;
import Pages.VoiceEffectsPage;
import Pages.Components.DiscardDialog;
import Pages.Components.PermissionDialog;
import Pages.TextToAudioPage;

import java.time.Duration;

public class RecordFlowHelper {

    private static final Logger logger = LogManager.getLogger(RecordFlowHelper.class);

    private static final By RECORD_CARD =
            By.id("com.bluesoftware.voicechanger:id/layout_record");
    private static final By RECORDER_MIC_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/image_record");
    private static final By RECORDER_TIMER =
            By.id("com.bluesoftware.voicechanger:id/text_duration");
    private static final By VOICE_EFFECTS_SAVE =
            By.id("com.bluesoftware.voicechanger:id/btnSave");
    private static final By AUDIO_SAVED_SUCCESS =
            By.id("com.bluesoftware.voicechanger:id/imgSuccess");
    private static final By RECORDER_CLOSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/image_close");
    private static final By VOICE_EFFECTS_CLOSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnClose");

    public static boolean isAtHome(AppiumDriver driver) {
        return driver.findElements(RECORD_CARD).size() > 0;
    }

    public static boolean isAtRecorder(AppiumDriver driver) {
        return driver.findElements(RECORDER_MIC_BUTTON).size() > 0
                && driver.findElements(RECORDER_TIMER).size() > 0;
    }

    public static boolean isAtVoiceEffects(AppiumDriver driver) {
        return driver.findElements(VOICE_EFFECTS_SAVE).size() > 0;
    }

    public static boolean isAtAudioSaved(AppiumDriver driver) {
        return driver.findElements(AUDIO_SAVED_SUCCESS).size() > 0;
    }

    public static boolean isAtVoiceChanger(AppiumDriver driver) {
        try {
            return AppConstants.APP_PACKAGE.equals(
                    ((AndroidDriver) driver).getCurrentPackage());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isRecording(AppiumDriver driver) {
        try {
            if (!isAtRecorder(driver)) return false;
            String timer = driver.findElement(RECORDER_TIMER).getText();
            return timer != null && !timer.equals("00:00:00");
        } catch (Exception e) {
            return false;
        }
    }

    public static void dismissDiscardDialogIfPresent(AppiumDriver driver) {
        DiscardDialog dialog = new DiscardDialog(driver);
        if (dialog.isDisplayed()) {
            logger.info("Discard dialog -> click Discard");
            dialog.clickDiscard();
            sleep(1000);
        }
    }

    public static void forceResetToHome(AppiumDriver driver) {
        logger.info("Force reset (terminate + activate)");
        AndroidDriver androidDriver = (AndroidDriver) driver;

        try {
            androidDriver.terminateApp(AppConstants.APP_PACKAGE);
            sleep(1000);
            androidDriver.activateApp(AppConstants.APP_PACKAGE);

            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfElementLocated(RECORD_CARD));
        } catch (Exception e) {
            logger.warn("Reset error: " + e.getMessage());
        }
    }

    public static void smartResetToHome(AppiumDriver driver) {
        if (isAtHome(driver)) {
            logger.info("Da o Home, skip");
            return;
        }

        // Co Discard dialog -> click Discard
        if (new DiscardDialog(driver).isDisplayed()) {
            dismissDiscardDialogIfPresent(driver);
            if (isAtHome(driver)) return;
        }

        // O Recorder dang ghi am
        if (isAtRecorder(driver) && isRecording(driver)) {
            logger.info("Recorder dang ghi am -> click X");
            try {
                driver.findElement(RECORDER_CLOSE_BUTTON).click();
                sleep(800);
                dismissDiscardDialogIfPresent(driver);
            } catch (Exception e) {
                logger.warn("Loi: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // O Recorder chua ghi
        if (isAtRecorder(driver)) {
            try {
                driver.findElement(RECORDER_CLOSE_BUTTON).click();
                sleep(1000);
            } catch (Exception e) {
                logger.warn("Loi: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // O Voice Effects
        if (isAtVoiceEffects(driver)) {
            logger.info("Voice Effects -> click X");
            try {
                driver.findElement(VOICE_EFFECTS_CLOSE_BUTTON).click();
                sleep(800);
                dismissDiscardDialogIfPresent(driver);
            } catch (Exception e) {
                logger.warn("Loi: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // O Audio Saved
        if (isAtAudioSaved(driver)) {
            try {
                driver.findElement(By.id(
                        "com.bluesoftware.voicechanger:id/btnHome")).click();
                sleep(1500);
            } catch (Exception e) {
                logger.warn("Loi: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // O man Text to Audio -> click Back ve Home
        if (isAtTextToAudio(driver)) {
            try {
                driver.findElement(By.id(
                        "com.bluesoftware.voicechanger:id/btnBack")).click();
                sleep(1500);
            } catch (Exception e) {
                logger.warn("Loi: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // O man Language Selection -> press BACK
        if (isAtLanguageSelection(driver)) {
            try {
                ((AndroidDriver) driver).pressKey(
                        new KeyEvent(AndroidKey.BACK));
                sleep(1500);
            } catch (Exception e) {
                logger.warn("Loi: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // Khong o Voice Changer
        if (!isAtVoiceChanger(driver)) {
            forceResetToHome(driver);
            return;
        }

        // Swipe back
        for (int i = 0; i < 5; i++) {
            if (isAtHome(driver)) return;
            if (new DiscardDialog(driver).isDisplayed()) {
                dismissDiscardDialogIfPresent(driver);
                continue;
            }
            GestureUtils.swipeFromLeftEdgeToBack(driver);
            sleep(700);
        }

        if (!isAtHome(driver)) {
            forceResetToHome(driver);
        }
    }

    public static RecorderPage navigateToRecorder(AppiumDriver driver) {
        if (isAtRecorder(driver)) {
            if (isRecording(driver)) {
                smartResetToHome(driver);
            } else {
                return new RecorderPage(driver);
            }
        }

        if (!isAtHome(driver)) {
            smartResetToHome(driver);
        }

        new HomePage(driver).clickRecord();
        sleep(1500);

        new PermissionDialog(driver).handlePermissionIfPresent();

        return new RecorderPage(driver);
    }

    public static VoiceEffectsPage navigateToVoiceEffects(AppiumDriver driver,
                                                          int recordSeconds) {
        if (isAtVoiceEffects(driver)) {
            return new VoiceEffectsPage(driver);
        }

        RecorderPage recorderPage = navigateToRecorder(driver);
        recorderPage.clickRecordButton();
        sleep(recordSeconds * 1000L);
        recorderPage.clickRecordButton();
        sleep(2500);

        return new VoiceEffectsPage(driver);
    }

    public static AudioSavedPage navigateToAudioSaved(AppiumDriver driver,
                                                      int recordSeconds) {
        if (isAtAudioSaved(driver)) {
            return new AudioSavedPage(driver);
        }

        VoiceEffectsPage voiceEffectsPage = navigateToVoiceEffects(driver, recordSeconds);
        voiceEffectsPage.clickSave();
        sleep(2500);

        return new AudioSavedPage(driver);
    }

    public static void resetToHome(AppiumDriver driver) {
        smartResetToHome(driver);
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static TextToAudioPage navigateToTextToAudio(AppiumDriver driver) {
        logger.info("=== Navigate to Text to Audio ===");

        // Bao dam dang o Home
        if (!isAtHome(driver)) {
            smartResetToHome(driver);
            sleep(1000);
        }

        // Click card Text to Speech
        HomePage homePage = new HomePage(driver);
        homePage.clickTextToSpeech();
        sleep(2000);

        return new TextToAudioPage(driver);
    }

    /**
     * Navigate tu Home -> Text to Audio -> nhap text -> Next -> Voice Effects.
     */
    public static VoiceEffectsPage navigateToVoiceEffectsFromTTS(
            AppiumDriver driver, String text) {
        logger.info("=== Navigate to Voice Effects via TTS ===");

        TextToAudioPage ttsPage = navigateToTextToAudio(driver);

        ttsPage.clickEditText();
        sleep(500);
        ttsPage.enterText(text);
        sleep(500);
        ttsPage.hideKeyboard();
        sleep(500);
        ttsPage.clickNext();
        sleep(4000);

        return new VoiceEffectsPage(driver);
    }

    /**
     * Navigate tu Home -> Text to Audio -> ... -> Audio Saved.
     */
    public static AudioSavedPage navigateToAudioSavedFromTTS(
            AppiumDriver driver, String text) {
        logger.info("=== Navigate to Audio Saved via TTS ===");

        VoiceEffectsPage vePage = navigateToVoiceEffectsFromTTS(driver, text);
        vePage.clickSave();
        sleep(3000);

        return new AudioSavedPage(driver);
    }

    public static boolean isAtTextToAudio(AppiumDriver driver) {
        try {
            return driver.findElements(By.id(
                    "com.bluesoftware.voicechanger:id/edtText")).size() > 0
                    && driver.findElements(By.id(
                    "com.bluesoftware.voicechanger:id/btnNext")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAtLanguageSelection(AppiumDriver driver) {
        try {
            return driver.findElements(By.id(
                    "com.bluesoftware.voicechanger:id/container")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}