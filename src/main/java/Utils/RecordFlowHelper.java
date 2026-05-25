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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Pages.AudioSavedPage;
import Pages.DrawerMenuPage;
import Pages.HomePage;
import Pages.ImportAudioFilePickerPage;
import Pages.MyAudioPage;
import Pages.RecorderPage;
import Pages.SettingsPage;
import Pages.TextToAudioPage;
import Pages.VoiceEffectsPage;
import Pages.Components.DiscardDialog;
import Pages.Components.PermissionDialog;

import java.time.Duration;
import java.util.List;

/**
 * Helper navigate va reset giua cac man hinh trong Voice Changer app.
 *
 * Structure:
 *   1. Locators
 *   2. State checks (isAtX)
 *   3. Dialog handlers
 *   4. Reset methods (force/smart)
 *   5. Navigation methods (Record, TTS, MyAudio, Import, Settings, Drawer)
 *   6. Utility methods (sleep, smart waits)
 */
public class RecordFlowHelper {

    private static final Logger logger =
            LogManager.getLogger(RecordFlowHelper.class);

    // ==========================================================================
    // 1. LOCATORS
    // ==========================================================================

    // Home
    private static final By RECORD_CARD =
            By.id("com.bluesoftware.voicechanger:id/layout_record");

    // Recorder
    private static final By RECORDER_MIC_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/image_record");
    private static final By RECORDER_TIMER =
            By.id("com.bluesoftware.voicechanger:id/text_duration");
    private static final By RECORDER_CLOSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/image_close");

    // Voice Effects
    private static final By VOICE_EFFECTS_SAVE =
            By.id("com.bluesoftware.voicechanger:id/btnSave");
    private static final By VOICE_EFFECTS_CLOSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnClose");

    // Audio Saved
    private static final By AUDIO_SAVED_SUCCESS =
            By.id("com.bluesoftware.voicechanger:id/imgSuccess");
    private static final By AUDIO_SAVED_HOME_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnHome");

    // Common buttons
    private static final By BACK_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnBack");
    private static final By TITLE =
            By.id("com.bluesoftware.voicechanger:id/tvTitle");

    // Bottom Sheets / Dialogs
    private static final By BOTTOM_SHEET =
            By.id("com.bluesoftware.voicechanger:id/design_bottom_sheet");
    private static final By TOUCH_OUTSIDE =
            By.id("com.bluesoftware.voicechanger:id/touch_outside");
    private static final By LANGUAGE_RECYCLER =
            By.id("com.bluesoftware.voicechanger:id/rvLanguage");
    private static final By DELETE_DIALOG_TITLE =
            By.id("android:id/alertTitle");
    private static final By DELETE_DIALOG_CANCEL =
            By.id("android:id/button2");

    // Share Sheet (intentresolver)
    private static final By SHARE_SHEET_CANCEL =
            By.id("com.android.intentresolver:id/oplus_resolve_close_icon");

    // Drawer
    private static final By DRAWER_VIEW =
            By.id("com.bluesoftware.voicechanger:id/drawer_view");

    // Screen-specific
    private static final By TTS_EDIT_TEXT =
            By.id("com.bluesoftware.voicechanger:id/edtText");
    private static final By TTS_NEXT_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnNext");
    private static final By LANGUAGE_SELECTION_CONTAINER =
            By.id("com.bluesoftware.voicechanger:id/container");
    private static final By MY_AUDIO_RECYCLER =
            By.id("com.bluesoftware.voicechanger:id/rvAudio");
    private static final By MY_AUDIO_SEARCH =
            By.id("com.bluesoftware.voicechanger:id/edtSearch");
    private static final By SETTINGS_OUTPUT_PATH =
            By.id("com.bluesoftware.voicechanger:id/rowOutputPath");

    // ==========================================================================
    // 2. STATE CHECKS (isAtX)
    // ==========================================================================

    public static boolean isAtVoiceChanger(AppiumDriver driver) {
        try {
            return AppConstants.APP_PACKAGE.equals(
                    ((AndroidDriver) driver).getCurrentPackage());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAtHome(AppiumDriver driver) {
        return driver.findElements(RECORD_CARD).size() > 0;
    }

    public static boolean isAtRecorder(AppiumDriver driver) {
        return driver.findElements(RECORDER_MIC_BUTTON).size() > 0
                && driver.findElements(RECORDER_TIMER).size() > 0;
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

    public static boolean isAtVoiceEffects(AppiumDriver driver) {
        return driver.findElements(VOICE_EFFECTS_SAVE).size() > 0;
    }

    public static boolean isAtAudioSaved(AppiumDriver driver) {
        return driver.findElements(AUDIO_SAVED_SUCCESS).size() > 0;
    }

    public static boolean isAtTextToAudio(AppiumDriver driver) {
        try {
            return driver.findElements(TTS_EDIT_TEXT).size() > 0
                    && driver.findElements(TTS_NEXT_BUTTON).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAtLanguageSelection(AppiumDriver driver) {
        try {
            return driver.findElements(LANGUAGE_SELECTION_CONTAINER).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAtMyAudio(AppiumDriver driver) {
        try {
            return driver.findElements(MY_AUDIO_RECYCLER).size() > 0
                    && driver.findElements(MY_AUDIO_SEARCH).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAtFilePicker(AppiumDriver driver) {
        try {
            String currentPkg =
                    ((AndroidDriver) driver).getCurrentPackage();
            return currentPkg != null
                    && !currentPkg.equals(AppConstants.APP_PACKAGE);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAtSettings(AppiumDriver driver) {
        try {
            if (driver.findElements(SETTINGS_OUTPUT_PATH).size() == 0) {
                return false;
            }
            List<WebElement> titles = driver.findElements(TITLE);
            for (WebElement el : titles) {
                try {
                    if ("Settings".equalsIgnoreCase(el.getText())) {
                        return true;
                    }
                } catch (Exception e) {
                    // skip
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDrawerOpen(AppiumDriver driver) {
        return driver.findElements(DRAWER_VIEW).size() > 0;
    }

    // ==========================================================================
    // 3. DIALOG HANDLERS
    // ==========================================================================

    public static void dismissDiscardDialogIfPresent(AppiumDriver driver) {
        DiscardDialog dialog = new DiscardDialog(driver);
        if (dialog.isDisplayed()) {
            logger.info("Discard dialog -> click Discard");
            dialog.clickDiscard();
            sleep(1000);
        }
    }

    private static void dismissBottomSheetIfPresent(AppiumDriver driver) {
        if (driver.findElements(BOTTOM_SHEET).size() == 0) return;
        try {
            driver.findElement(TOUCH_OUTSIDE).click();
            sleep(800);
        } catch (Exception e) {
            try {
                ((AndroidDriver) driver).pressKey(
                        new KeyEvent(AndroidKey.BACK));
                sleep(800);
            } catch (Exception ex) {
                // skip
            }
        }
    }

    private static void dismissLanguageDialogIfPresent(AppiumDriver driver) {
        if (driver.findElements(LANGUAGE_RECYCLER).size() == 0) return;
        try {
            driver.findElement(TOUCH_OUTSIDE).click();
            sleep(800);
        } catch (Exception e) {
            try {
                ((AndroidDriver) driver).pressKey(
                        new KeyEvent(AndroidKey.BACK));
                sleep(800);
            } catch (Exception ex) {
                // skip
            }
        }
    }

    private static void dismissShareSheetIfPresent(AppiumDriver driver) {
        if (driver.findElements(SHARE_SHEET_CANCEL).size() == 0) return;
        try {
            driver.findElement(SHARE_SHEET_CANCEL).click();
            sleep(1000);
        } catch (Exception e) {
            try {
                ((AndroidDriver) driver).pressKey(
                        new KeyEvent(AndroidKey.BACK));
                sleep(800);
            } catch (Exception ex) {
                // skip
            }
        }
    }

    private static void dismissDeleteDialogIfPresent(AppiumDriver driver) {
        if (driver.findElements(DELETE_DIALOG_CANCEL).size() == 0
                || driver.findElements(DELETE_DIALOG_TITLE).size() == 0) {
            return;
        }
        try {
            driver.findElement(DELETE_DIALOG_CANCEL).click();
            sleep(800);
        } catch (Exception e) {
            // skip
        }
    }

    private static void closeDrawerIfOpen(AppiumDriver driver) {
        if (!isDrawerOpen(driver)) return;
        try {
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            sleep(800);
        } catch (Exception e) {
            // skip
        }
    }

    // ==========================================================================
    // 4. RESET METHODS
    // ==========================================================================

    /**
     * Force reset: terminate + activate app.
     * Dung khi smart reset khong giai quyet duoc.
     */
    public static void forceResetToHome(AppiumDriver driver) {
        logger.info("Force reset (terminate + activate)");
        AndroidDriver androidDriver = (AndroidDriver) driver;
        try {
            androidDriver.terminateApp(AppConstants.APP_PACKAGE);
            sleep(1000);
            androidDriver.activateApp(AppConstants.APP_PACKAGE);
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            RECORD_CARD));
        } catch (Exception e) {
            logger.warn("Reset error: " + e.getMessage());
        }
    }

    /**
     * Smart reset: try cac cach nhe nhang truoc, fallback force reset.
     * Xu ly:
     *   - Dialog/bottom sheet
     *   - Cac man (Recorder, VE, AS, TTS, MyAudio, Settings, FilePicker)
     *   - Drawer mo
     *   - App khong o foreground
     */
    public static void smartResetToHome(AppiumDriver driver) {
        if (isAtHome(driver)) {
            logger.info("Da o Home, skip");
            return;
        }

        // === 1. Dismiss dialogs / bottom sheets ===
        if (new DiscardDialog(driver).isDisplayed()) {
            dismissDiscardDialogIfPresent(driver);
            if (isAtHome(driver)) return;
        }
        dismissLanguageDialogIfPresent(driver);
        dismissBottomSheetIfPresent(driver);
        dismissShareSheetIfPresent(driver);
        dismissDeleteDialogIfPresent(driver);

        // === 2. Recorder (uu tien check recording) ===
        if (isAtRecorder(driver) && isRecording(driver)) {
            logger.info("Recorder dang ghi am -> click X");
            try {
                driver.findElement(RECORDER_CLOSE_BUTTON).click();
                sleep(800);
                dismissDiscardDialogIfPresent(driver);
            } catch (Exception e) {
                logger.warn("Loi close recorder: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        if (isAtRecorder(driver)) {
            try {
                driver.findElement(RECORDER_CLOSE_BUTTON).click();
                waitForHome(driver, 3);
            } catch (Exception e) {
                logger.warn("Loi close recorder: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // === 3. Voice Effects ===
        if (isAtVoiceEffects(driver)) {
            logger.info("Voice Effects -> click X");
            try {
                driver.findElement(VOICE_EFFECTS_CLOSE_BUTTON).click();
                sleep(800);
                dismissDiscardDialogIfPresent(driver);
            } catch (Exception e) {
                logger.warn("Loi close VE: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // === 4. Audio Saved ===
        if (isAtAudioSaved(driver)) {
            try {
                driver.findElement(AUDIO_SAVED_HOME_BUTTON).click();
                waitForHome(driver, 3);
            } catch (Exception e) {
                logger.warn("Loi home from AS: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // === 5. Text to Audio ===
        if (isAtTextToAudio(driver)) {
            try {
                driver.findElement(BACK_BUTTON).click();
                waitForHome(driver, 3);
            } catch (Exception e) {
                logger.warn("Loi back from TTS: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // === 6. Language Selection ===
        if (isAtLanguageSelection(driver)) {
            try {
                ((AndroidDriver) driver).pressKey(
                        new KeyEvent(AndroidKey.BACK));
                waitForHome(driver, 3);
            } catch (Exception e) {
                logger.warn("Loi back from Language: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // === 7. My Audio ===
        if (isAtMyAudio(driver)) {
            try {
                driver.findElement(BACK_BUTTON).click();
                waitForHome(driver, 3);
            } catch (Exception e) {
                logger.warn("Loi back from My Audio: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // === 8. Settings ===
        if (isAtSettings(driver)) {
            try {
                driver.findElement(BACK_BUTTON).click();
                waitForHome(driver, 3);
            } catch (Exception e) {
                logger.warn("Loi back from Settings: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // === 9. Drawer mo ===
        closeDrawerIfOpen(driver);
        if (isAtHome(driver)) return;

        // === 10. File Picker (external) ===
        if (isAtFilePicker(driver)) {
            logger.info("O File Picker, activate app");
            try {
                ((AndroidDriver) driver).activateApp(AppConstants.APP_PACKAGE);
                waitForHome(driver, 5);
            } catch (Exception e) {
                logger.warn("Activate app error: " + e.getMessage());
            }
            if (isAtHome(driver)) return;
        }

        // === 11. Khong o Voice Changer -> activate ===
        if (!isAtVoiceChanger(driver)) {
            try {
                logger.info("Khong o Voice Changer, activate");
                ((AndroidDriver) driver).activateApp(AppConstants.APP_PACKAGE);
                waitForHome(driver, 5);
            } catch (Exception e) {
                // skip
            }
            if (isAtHome(driver)) return;
        }

        // === 12. Fallback: swipe back nhieu lan ===
        for (int i = 0; i < 5; i++) {
            if (isAtHome(driver)) return;
            if (new DiscardDialog(driver).isDisplayed()) {
                dismissDiscardDialogIfPresent(driver);
                continue;
            }
            GestureUtils.swipeFromLeftEdgeToBack(driver);
            sleep(700);
        }

        // === 13. Final fallback: force reset ===
        if (!isAtHome(driver)) {
            forceResetToHome(driver);
        }
    }

    public static void resetToHome(AppiumDriver driver) {
        smartResetToHome(driver);
    }

    // ==========================================================================
    // 5. NAVIGATION METHODS
    // ==========================================================================

    // -------- Record flow --------

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
        // Wait recorder mo, permission dialog co the xuat hien -> handle
        waitForRecorderOrPermission(driver, 3);
        new PermissionDialog(driver).handlePermissionIfPresent();
        waitForRecorderOrPermission(driver, 3);

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
        // H4: thay sleep(2500) bang smart wait Voice Effects
        waitForVoiceEffects(driver, 5);

        return new VoiceEffectsPage(driver);
    }

    public static AudioSavedPage navigateToAudioSaved(AppiumDriver driver,
                                                      int recordSeconds) {
        if (isAtAudioSaved(driver)) {
            return new AudioSavedPage(driver);
        }

        VoiceEffectsPage vePage = navigateToVoiceEffects(driver, recordSeconds);
        vePage.clickSave();
        // H4: thay sleep(2500) bang smart wait Audio Saved
        waitForAudioSaved(driver, 5);

        return new AudioSavedPage(driver);
    }

    // -------- Text to Speech flow --------

    public static TextToAudioPage navigateToTextToAudio(AppiumDriver driver) {
        logger.info("=== Navigate to Text to Audio ===");

        // L3: idempotent - tan dung neu class truoc da o TTS
        if (isAtTextToAudio(driver)) {
            logger.info("Da o Text to Audio, skip nav");
            return new TextToAudioPage(driver);
        }

        if (!isAtHome(driver)) {
            smartResetToHome(driver);
        }

        new HomePage(driver).clickTextToSpeech();
        // H6 (partial): thay sleep(2000) bang smart wait
        waitForTextToAudio(driver, 4);
        return new TextToAudioPage(driver);
    }

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
        // H1: thay sleep(4000) bang smart wait Voice Effects (TTS processing co the cham)
        waitForVoiceEffects(driver, 6);

        return new VoiceEffectsPage(driver);
    }

    public static AudioSavedPage navigateToAudioSavedFromTTS(
            AppiumDriver driver, String text) {
        logger.info("=== Navigate to Audio Saved via TTS ===");

        VoiceEffectsPage vePage = navigateToVoiceEffectsFromTTS(driver, text);
        vePage.clickSave();
        // H3: thay sleep(3000) bang smart wait Audio Saved
        waitForAudioSaved(driver, 6);

        return new AudioSavedPage(driver);
    }

    // -------- My Audio flow --------

    /**
     * Dam bao My Audio co it nhat 1 file - tao file moi neu chua co.
     * Side effect: navigate qua Recorder -> VE -> Audio Saved -> Home,
     * cuoi cung de driver o Home (KHONG mo My Audio).
     *
     * Goi tu @BeforeClass cua MyAudio* test classes thay vi viet helper rieng.
     */
    public static void ensureAtLeastOneFile(AppiumDriver driver) {
        try {
            MyAudioPage temp = navigateToMyAudio(driver);
            if (temp.hasAtLeastOneFile()) {
                logger.info("My Audio da co file, skip tao");
                return;
            }
            logger.info("My Audio rong, tao 1 file");
            smartResetToHome(driver);
            navigateToAudioSaved(driver, 1);
            smartResetToHome(driver);
        } catch (Exception e) {
            logger.warn("ensureAtLeastOneFile error: " + e.getMessage());
        }
    }

    public static MyAudioPage navigateToMyAudio(AppiumDriver driver) {
        logger.info("=== Navigate to My Audio ===");

        // L3: idempotent
        if (isAtMyAudio(driver)) {
            logger.info("Da o My Audio, skip nav");
            return new MyAudioPage(driver);
        }

        if (!isAtHome(driver)) {
            smartResetToHome(driver);
        }

        new HomePage(driver).clickMyAudio();
        // H6 (partial): thay sleep(1500) bang smart wait
        waitForMyAudio(driver, 4);
        return new MyAudioPage(driver);
    }

    // -------- Import Audio flow --------

    public static ImportAudioFilePickerPage navigateToFilePicker(
            AppiumDriver driver) {
        logger.info("=== Navigate to File Picker (Import Audio) ===");

        if (!isAtHome(driver)) {
            smartResetToHome(driver);
        }

        new HomePage(driver).clickImportAudio();
        // H5: thay sleep(2500) bang smart wait File Picker (package thay doi)
        waitForFilePicker(driver, 5);
        return new ImportAudioFilePickerPage(driver);
    }

    public static VoiceEffectsPage navigateToVoiceEffectsFromImport(
            AppiumDriver driver) {
        logger.info("=== Navigate to Voice Effects via Import Audio ===");

        ImportAudioFilePickerPage picker = navigateToFilePicker(driver);
        if (!picker.isDisplayed()) {
            logger.error("Khong vao duoc File Picker");
            return null;
        }

        boolean clicked = picker.clickFirstAudioFile();
        if (!clicked) {
            logger.error("Khong tim thay file audio");
            picker.pressBack();
            return null;
        }

        // H2: thay sleep(4000) bang smart wait Voice Effects (file co the lon)
        waitForVoiceEffects(driver, 8);
        return new VoiceEffectsPage(driver);
    }

    // -------- Settings & Drawer flow --------

    public static SettingsPage navigateToSettings(AppiumDriver driver) {
        logger.info("=== Navigate to Settings ===");

        // L3: idempotent - tan dung neu class truoc da o Settings
        if (isAtSettings(driver)) {
            logger.info("Da o Settings, skip nav");
            return new SettingsPage(driver);
        }

        if (!isAtHome(driver)) {
            smartResetToHome(driver);
        }

        new HomePage(driver).openSettingsViaDrawer();
        // H6 (partial): thay sleep(1500) bang smart wait
        waitForSettings(driver, 4);
        return new SettingsPage(driver);
    }

    public static DrawerMenuPage openDrawer(AppiumDriver driver) {
        logger.info("=== Open Drawer Menu ===");

        // L3: idempotent - tan dung neu class truoc da mo drawer
        if (isDrawerOpen(driver)) {
            logger.info("Drawer da mo, skip");
            return new DrawerMenuPage(driver);
        }

        if (!isAtHome(driver)) {
            smartResetToHome(driver);
        }
        return new HomePage(driver).openDrawer();
    }

    // ==========================================================================
    // 6. UTILITY (sleep, smart waits)
    // ==========================================================================

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // -------- Smart waits (H1-H6: thay sleep co dinh bang wait theo state) --------

    /**
     * Wait cho ve Home (RECORD_CARD xuat hien).
     * Max maxSeconds (3-5s tuy context).
     */
    public static void waitForHome(AppiumDriver driver, int maxSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(maxSeconds))
                    .ignoring(Exception.class)
                    .until(d -> isAtHome((AppiumDriver) d));
        } catch (Exception e) {
            logger.warn("Wait timeout: Home khong xuat hien sau " + maxSeconds + "s");
        }
    }

    /**
     * Wait cho Voice Effects (VOICE_EFFECTS_SAVE button xuat hien).
     */
    public static void waitForVoiceEffects(AppiumDriver driver, int maxSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(maxSeconds))
                    .ignoring(Exception.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            VOICE_EFFECTS_SAVE));
        } catch (Exception e) {
            logger.warn("Wait timeout: Voice Effects khong xuat hien sau "
                    + maxSeconds + "s");
        }
    }

    /**
     * Wait cho Audio Saved (AUDIO_SAVED_SUCCESS image xuat hien).
     */
    public static void waitForAudioSaved(AppiumDriver driver, int maxSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(maxSeconds))
                    .ignoring(Exception.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            AUDIO_SAVED_SUCCESS));
        } catch (Exception e) {
            logger.warn("Wait timeout: Audio Saved khong xuat hien sau "
                    + maxSeconds + "s");
        }
    }

    /**
     * Wait cho File Picker (package doi khac com.bluesoftware.voicechanger).
     */
    private static void waitForFilePicker(AppiumDriver driver, int maxSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(maxSeconds))
                    .ignoring(Exception.class)
                    .until(d -> {
                        try {
                            String pkg = ((AndroidDriver) d).getCurrentPackage();
                            return pkg != null
                                    && !pkg.equals(AppConstants.APP_PACKAGE);
                        } catch (Exception e) {
                            return false;
                        }
                    });
        } catch (Exception e) {
            logger.warn("Wait timeout: File Picker khong xuat hien sau "
                    + maxSeconds + "s");
        }
    }

    /**
     * Wait cho Text to Audio screen.
     */
    private static void waitForTextToAudio(AppiumDriver driver, int maxSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(maxSeconds))
                    .ignoring(Exception.class)
                    .until(d -> isAtTextToAudio((AppiumDriver) d));
        } catch (Exception e) {
            logger.warn("Wait timeout: TTS khong xuat hien sau "
                    + maxSeconds + "s");
        }
    }

    /**
     * Wait cho My Audio screen.
     */
    private static void waitForMyAudio(AppiumDriver driver, int maxSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(maxSeconds))
                    .ignoring(Exception.class)
                    .until(d -> isAtMyAudio((AppiumDriver) d));
        } catch (Exception e) {
            logger.warn("Wait timeout: My Audio khong xuat hien sau "
                    + maxSeconds + "s");
        }
    }

    /**
     * Wait cho Settings screen.
     */
    private static void waitForSettings(AppiumDriver driver, int maxSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(maxSeconds))
                    .ignoring(Exception.class)
                    .until(d -> isAtSettings((AppiumDriver) d));
        } catch (Exception e) {
            logger.warn("Wait timeout: Settings khong xuat hien sau "
                    + maxSeconds + "s");
        }
    }

    /**
     * Wait cho Recorder hoac Permission dialog (cai nao xuat hien truoc).
     */
    private static void waitForRecorderOrPermission(AppiumDriver driver,
                                                     int maxSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(maxSeconds))
                    .ignoring(Exception.class)
                    .until(d -> {
                        AppiumDriver ap = (AppiumDriver) d;
                        return isAtRecorder(ap)
                                || new PermissionDialog(ap).isDisplayed();
                    });
        } catch (Exception e) {
            logger.warn("Wait timeout: Recorder/Permission khong xuat hien sau "
                    + maxSeconds + "s");
        }
    }
}
