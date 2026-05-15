package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object cho man File Picker (system app).
 * Mo khi click 'Import audio' tu Home.
 *
 * LUU Y QUAN TRONG:
 * - File Picker la system app, KHONG phai com.bluesoftware.voicechanger.
 * - Moi device co File Picker khac nhau (Files by Google, Oppo Files, etc.).
 * - Resource-id phu thuoc he dieu hanh, khong stable.
 * - Page nay chi cung cap basic verify (mo duoc, co the back).
 */
public class ImportAudioFilePickerPage extends BasePage {

    // Locator chung cho cac File Picker thong dung.
    // Tim file co extension .mp3 hoac .m4a bang text contain.
    private static final By FILE_MP3_TEXT_CONTAINS =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\".mp3\")");

    private static final By FILE_M4A_TEXT_CONTAINS =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\".m4a\")");

    private static final By FILE_WAV_TEXT_CONTAINS =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\".wav\")");

    public ImportAudioFilePickerPage(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Kiem tra dang o File Picker bang cach check current package.
     * File Picker KHONG phai com.bluesoftware.voicechanger.
     */
    public boolean isDisplayed() {
        try {
            String currentPkg = ((AndroidDriver) driver).getCurrentPackage();
            logger.info("Current package: " + currentPkg);
            return currentPkg != null
                    && !currentPkg.equals("com.bluesoftware.voicechanger");
        } catch (Exception e) {
            logger.warn("Cannot get current package: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lay current package de log/debug.
     */
    public String getCurrentPackage() {
        try {
            return ((AndroidDriver) driver).getCurrentPackage();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Tim va click file audio dau tien (uu tien .mp3 -> .m4a -> .wav).
     * Return true neu click duoc, false neu khong tim thay file.
     */
    public boolean clickFirstAudioFile() {
        logger.info("Tim va click file audio dau tien");

        // Thu .mp3 truoc
        List<WebElement> mp3Files = driver.findElements(FILE_MP3_TEXT_CONTAINS);
        if (!mp3Files.isEmpty()) {
            String name = mp3Files.get(0).getText();
            logger.info("Tim thay file .mp3: " + name);
            mp3Files.get(0).click();
            return true;
        }

        // Thu .m4a
        List<WebElement> m4aFiles = driver.findElements(FILE_M4A_TEXT_CONTAINS);
        if (!m4aFiles.isEmpty()) {
            String name = m4aFiles.get(0).getText();
            logger.info("Tim thay file .m4a: " + name);
            m4aFiles.get(0).click();
            return true;
        }

        // Thu .wav
        List<WebElement> wavFiles = driver.findElements(FILE_WAV_TEXT_CONTAINS);
        if (!wavFiles.isEmpty()) {
            String name = wavFiles.get(0).getText();
            logger.info("Tim thay file .wav: " + name);
            wavFiles.get(0).click();
            return true;
        }

        logger.error("Khong tim thay file audio nao trong File Picker");
        return false;
    }

    /**
     * Press BACK button de quay ve Voice Changer.
     */
    public void pressBack() {
        logger.info("Press BACK toi da 3 lan de ve Voice Changer");

        AndroidDriver androidDriver = (AndroidDriver) driver;

        for (int i = 1; i <= 3; i++) {
            // Check da ve Voice Changer chua
            try {
                String currentPkg = androidDriver.getCurrentPackage();
                if ("com.bluesoftware.voicechanger".equals(currentPkg)) {
                    logger.info("Da ve Voice Changer sau " + (i - 1) + " lan BACK");
                    return;
                }
            } catch (Exception e) {
                // Skip, continue press BACK
            }

            // Press BACK
            try {
                androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
                Thread.sleep(1200);
            } catch (Exception e) {
                logger.warn("Press BACK lan " + i + " loi: " + e.getMessage());
            }
        }

        // Final check
        try {
            String finalPkg = androidDriver.getCurrentPackage();
            logger.info("Sau 3 lan BACK, current package: " + finalPkg);
        } catch (Exception e) {
            // Skip
        }
    }
}