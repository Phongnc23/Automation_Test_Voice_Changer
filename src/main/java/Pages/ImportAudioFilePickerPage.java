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
     * Press BACK key (tuc thi, KHONG phai gesture giu lau) - smart wait.
     *
     * Yeu cau: cu chi back PHAI nhanh va dut khoat. Khong nhan giu lau vi
     * Android co the hieu sai (vd dropdown notification) va out app ve
     * man hinh he thong.
     *
     * Cach tiep can:
     *   1. Su dung KeyEvent.BACK (key event, KHONG phai swipe gesture)
     *      -> Khong the bi interpret nham la long-press hay swipe.
     *   2. Sau moi lan press, poll current package moi 100ms toi da 600ms
     *      -> Phat hien app da quay ve Voice Changer la dung ngay,
     *         tranh nhan BACK thua lam out app sang system home.
     *   3. Toi da 3 lan press (file picker thuong chi can 1-2 lan).
     */
    public void pressBack() {
        logger.info("Press BACK (key event - nhanh, dut khoat)");

        AndroidDriver androidDriver = (AndroidDriver) driver;
        final String APP_PKG = "com.bluesoftware.voicechanger";

        for (int i = 1; i <= 3; i++) {
            // Da ve Voice Changer truoc khi press BACK -> dung ngay
            if (isAtApp(androidDriver, APP_PKG)) {
                logger.info("Da o Voice Changer (sau " + (i - 1) + " BACK), dung");
                return;
            }

            // Press BACK key (tuc thi, KHONG giu)
            try {
                androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
            } catch (Exception e) {
                logger.warn("Press BACK lan " + i + " loi: " + e.getMessage());
                continue;
            }

            // Smart poll: cho package change toi da 600ms, kiem tra moi 100ms.
            // Dung ngay khi app quay ve -> tranh nhan BACK lan tiep theo
            // dan toi out app sang system home.
            long deadline = System.currentTimeMillis() + 600;
            while (System.currentTimeMillis() < deadline) {
                if (isAtApp(androidDriver, APP_PKG)) {
                    logger.info("Da ve Voice Changer sau press lan " + i);
                    return;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        try {
            logger.info("Sau 3 BACK, current package: "
                    + androidDriver.getCurrentPackage());
        } catch (Exception e) {
            // skip
        }
    }

    /** Helper check current package == app pkg, swallow exception. */
    private boolean isAtApp(AndroidDriver androidDriver, String appPkg) {
        try {
            return appPkg.equals(androidDriver.getCurrentPackage());
        } catch (Exception e) {
            return false;
        }
    }
}