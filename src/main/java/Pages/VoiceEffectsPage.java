package Pages;

import Base.BasePage;
import Pages.Components.DiscardDialog;
import Utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Page Object cho man hinh Voice Effects.
 * Truy cap tu: Recorder -> click Stop (sau khi ghi am).
 *
 * Layout cua grid effects:
 * - Grid 3 cot x N hang (Normal, Man, Woman... Bee, Fade)
 * - Scroll DOC
 * - Phia tren: audio player (file name, time, play/pause)
 * - Phia duoi: nut Send Voice Message + Save
 *
 * Vung scroll cua grid effects: ~35% - 75% chieu cao man hinh.
 */
public class VoiceEffectsPage extends BasePage {

    /**
     * Danh sach 23 effects co the chon tren Voice Effects.
     */
    public static final List<String> ALL_EFFECTS = Arrays.asList(
            "Normal", "Man", "Woman", "Child", "Penguin", "Monster",
            "Fast", "Slow", "Alien", "Zombie", "Drunk", "Helium",
            "Death", "Robot", "Baby", "Echo", "Underwater", "Telephone",
            "Parody", "Bass", "Tenor", "Bee", "Fade"
    );

    // ========== LOCATORS ==========

    private static final By HEADER_LAYOUT =
            By.id("com.bluesoftware.voicechanger:id/layoutHeader");
    private static final By CLOSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnClose");
    private static final By TITLE =
            By.id("com.bluesoftware.voicechanger:id/tvTitle");
    private static final By AUDIO_LAYOUT =
            By.id("com.bluesoftware.voicechanger:id/layoutAudio");
    private static final By AUDIO_NAME =
            By.id("com.bluesoftware.voicechanger:id/tvAudioName");
    private static final By IMAGE_WAVE =
            By.id("com.bluesoftware.voicechanger:id/image_wave");
    private static final By PLAY_PAUSE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnPlayPause");
    private static final By SEEK_BAR =
            By.id("com.bluesoftware.voicechanger:id/seekBar");
    private static final By TIME_TEXT =
            By.id("com.bluesoftware.voicechanger:id/tvTime");
    private static final By EFFECTS_RECYCLER =
            By.id("com.bluesoftware.voicechanger:id/rvEffects");
    private static final By EFFECT_NAME_ITEMS =
            By.id("com.bluesoftware.voicechanger:id/tv_effect_name");
    private static final By BOTTOM_LAYOUT =
            By.id("com.bluesoftware.voicechanger:id/layoutBottom");
    private static final By SEND_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnSend");
    private static final By SAVE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnSave");

    // UiAutomator selector cho Play/Pause - tranh stale element
    private static final String PLAY_PAUSE_UIAUTOMATOR =
            "new UiSelector().resourceId(\"com.bluesoftware.voicechanger:id/btnPlayPause\")";

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

    public boolean isHeaderDisplayed() {
        return driver.findElements(HEADER_LAYOUT).size() > 0;
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

    public boolean isTitleDisplayed() {
        return driver.findElements(TITLE).size() > 0;
    }

    // ========== AUDIO PLAYER ==========

    public boolean isAudioPlayerDisplayed() {
        return driver.findElements(AUDIO_LAYOUT).size() > 0
                && driver.findElements(PLAY_PAUSE_BUTTON).size() > 0;
    }

    public boolean isAudioLayoutDisplayed() {
        return driver.findElements(AUDIO_LAYOUT).size() > 0;
    }

    public String getAudioFileName() {
        try {
            return driver.findElement(AUDIO_NAME).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isImageWaveDisplayed() {
        return driver.findElements(IMAGE_WAVE).size() > 0;
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

    public boolean isSeekBarDisplayed() {
        return driver.findElements(SEEK_BAR).size() > 0;
    }

    public boolean isTimeTextDisplayed() {
        return driver.findElements(TIME_TEXT).size() > 0;
    }

    /**
     * Click Play/Pause button.
     * Dung UiAutomator selector de tim element TUOI MOI moi lan click,
     * tranh stale element exception khi icon doi tu Play sang Pause.
     */
    public void clickPlayPause() {
        logger.info("Click Play/Pause button");

        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    PLAY_PAUSE_UIAUTOMATOR)).click();
            Thread.sleep(300);
        } catch (Exception e) {
            logger.warn("Loi click PlayPause: " + e.getMessage());
            try {
                driver.findElement(PLAY_PAUSE_BUTTON).click();
                Thread.sleep(300);
            } catch (Exception ex) {
                throw new RuntimeException("Cannot click Play/Pause button", ex);
            }
        }
    }

    /**
     * Verify audio dang phat bang cach so sanh time tang sau khoang thoi gian.
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

    // ========== EFFECTS GRID ==========

    public boolean isEffectsRecyclerDisplayed() {
        return driver.findElements(EFFECTS_RECYCLER).size() > 0;
    }

    /**
     * Lay locator cua effect theo ten.
     */
    public By getEffectLocator(String effectName) {
        return By.xpath(
                "//android.widget.TextView[@resource-id=" +
                        "'com.bluesoftware.voicechanger:id/tv_effect_name' " +
                        "and @text='" + effectName + "']");
    }

    /**
     * Lay danh sach effects dang visible tren man hinh.
     * Helper de debug va xac dinh vi tri trong list.
     */
    public List<String> getVisibleEffectNames() {
        List<WebElement> elements = driver.findElements(EFFECT_NAME_ITEMS);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            try {
                String text = el.getText();
                if (text != null && !text.isEmpty()) {
                    names.add(text);
                }
            } catch (Exception e) {
                // skip stale element
            }
        }
        return names;
    }

    /**
     * Click effect theo ten - su dung smart swipe.
     * 1. Check visible truoc -> click ngay
     * 2. Smart swipe (xuong truoc, neu stuck -> chuyen len)
     */
    public void clickEffect(String effectName) {
        logger.info("Click effect: " + effectName);

        try {
            // Strategy 0: Check visible truoc
            if (driver.findElements(getEffectLocator(effectName)).size() > 0) {
                click(getEffectLocator(effectName));
                Thread.sleep(500);
                return;
            }

            // Strategy 1: Smart swipe (xuong + len thong minh)
            if (smartSwipeToFind(effectName)) {
                click(getEffectLocator(effectName));
                Thread.sleep(500);
                return;
            }

            throw new RuntimeException("Khong tim thay effect: " + effectName);
        } catch (Exception e) {
            logger.error("clickEffect failed for " + effectName + ": " + e.getMessage());
            throw new RuntimeException("Cannot click effect: " + effectName, e);
        }
    }

    public boolean isEffectDisplayed(String effectName) {
        if (driver.findElements(getEffectLocator(effectName)).size() > 0) {
            return true;
        }
        return smartSwipeToFind(effectName);
    }

    /**
     * Smart swipe - tu dong quyet dinh scroll len hay xuong.
     *
     * Logic:
     * 1. Lay snapshot effects dang visible
     * 2. Swipe XUONG, sau moi swipe so sanh snapshot
     * 3. Neu 2 swipes lien tiep ma list KHONG thay doi -> dang o CUOI list
     *    -> Chuyen sang swipe LEN
     * 4. Swipe LEN den khi tim thay effect
     */
    private boolean smartSwipeToFind(String effectName) {
        By locator = getEffectLocator(effectName);
        Dimension size = driver.manage().window().getSize();

        int centerX = size.getWidth() / 2;
        int yDown = (int) (size.getHeight() * 0.75);   // Gan duoi grid
        int yUp = (int) (size.getHeight() * 0.35);     // Gan tren grid

        String snapshotBefore = String.join(",", getVisibleEffectNames());
        int unchangedCount = 0;

        // ===== Phase 1: Swipe XUONG =====
        for (int i = 0; i < 30; i++) {
            // Check tim thay chua
            if (driver.findElements(locator).size() > 0) {
                logger.info("Found " + effectName + " sau " + i + " swipes XUONG");
                return true;
            }

            try {
                // Swipe tu duoi len tren -> grid scroll xuong
                GestureUtils.swipe(driver, centerX, yDown, centerX, yUp, 500);
                Thread.sleep(400);

                // Check list co thay doi khong (detect stuck)
                String snapshotAfter = String.join(",", getVisibleEffectNames());
                if (snapshotAfter.equals(snapshotBefore)) {
                    unchangedCount++;
                    if (unchangedCount >= 2) {
                        // 2 lan lien tiep khong doi -> dang o CUOI list
                        logger.info("Dang o CUOI list (list khong doi sau 2 swipes), " +
                                "chuyen sang swipe LEN");
                        break;
                    }
                } else {
                    unchangedCount = 0;
                    snapshotBefore = snapshotAfter;
                }
            } catch (Exception e) {
                logger.warn("Swipe XUONG error: " + e.getMessage());
                return false;
            }
        }

        // ===== Phase 2: Swipe LEN =====
        logger.info("Phase 2: Swipe LEN tim " + effectName);
        snapshotBefore = String.join(",", getVisibleEffectNames());
        unchangedCount = 0;

        for (int i = 0; i < 30; i++) {
            if (driver.findElements(locator).size() > 0) {
                logger.info("Found " + effectName + " sau " + i + " swipes LEN");
                return true;
            }

            try {
                // Swipe tu tren xuong duoi -> grid scroll len
                GestureUtils.swipe(driver, centerX, yUp, centerX, yDown, 500);
                Thread.sleep(400);

                // Check stuck o dau list
                String snapshotAfter = String.join(",", getVisibleEffectNames());
                if (snapshotAfter.equals(snapshotBefore)) {
                    unchangedCount++;
                    if (unchangedCount >= 2) {
                        logger.warn("Dang o DAU list, khong the swipe them");
                        break;
                    }
                } else {
                    unchangedCount = 0;
                    snapshotBefore = snapshotAfter;
                }
            } catch (Exception e) {
                logger.warn("Swipe LEN error: " + e.getMessage());
                return false;
            }
        }

        logger.error("Khong tim thay " + effectName +
                " sau khi smart swipe ca XUONG va LEN");
        return false;
    }

    /**
     * Dem so effect dang hien thi tren man (chua scroll).
     */
    public int countVisibleEffects() {
        return driver.findElements(EFFECT_NAME_ITEMS).size();
    }

    // ========== ACTIONS ==========

    public void clickClose() {
        logger.info("Click nut X tren Voice Effects");
        click(CLOSE_BUTTON);
    }

    public boolean isSaveButtonDisplayed() {
        return driver.findElements(SAVE_BUTTON).size() > 0;
    }

    public void clickSave() {
        logger.info("Click Save");
        click(SAVE_BUTTON);
    }

    public boolean isSendButtonDisplayed() {
        return driver.findElements(SEND_BUTTON).size() > 0;
    }

    public void clickSendVoiceMessage() {
        logger.info("Click Send Voice Message");
        click(SEND_BUTTON);
    }

    // ========== DISCARD DIALOG ==========
    // Khi nhan X tren Voice Effects -> hien dialog Discard

    public DiscardDialog getDiscardDialog() {
        return new DiscardDialog(driver);
    }

    public boolean isDiscardDialogDisplayed() {
        return new DiscardDialog(driver).isDisplayed();
    }

    public void clickDiscardCancel() {
        new DiscardDialog(driver).clickCancel();
    }

    public void clickDiscardConfirm() {
        new DiscardDialog(driver).clickDiscard();
    }
}