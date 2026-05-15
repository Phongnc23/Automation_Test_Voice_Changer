package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Page Object cho man Settings.
 * Truy cap: Home -> hamburger menu (drawer) -> click 'Settings'.
 *
 * DOM elements:
 * - btnBack, tvTitle "Settings"
 * - Nhom General: rowOutputPath (co tvOutputPath), rowLanguage (co tvLanguageValue)
 * - Nhom About: rowPrivacy, rowVersion (co tvVersion)
 */
public class SettingsPage extends BasePage {

    private static final By BACK_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnBack");
    private static final By TITLE =
            By.id("com.bluesoftware.voicechanger:id/tvTitle");

    // Group General
    private static final By ROW_OUTPUT_PATH =
            By.id("com.bluesoftware.voicechanger:id/rowOutputPath");
    private static final By TV_OUTPUT_PATH =
            By.id("com.bluesoftware.voicechanger:id/tvOutputPath");
    private static final By ROW_LANGUAGE =
            By.id("com.bluesoftware.voicechanger:id/rowLanguage");
    private static final By TV_LANGUAGE_VALUE =
            By.id("com.bluesoftware.voicechanger:id/tvLanguageValue");

    // Group About
    private static final By ROW_PRIVACY =
            By.id("com.bluesoftware.voicechanger:id/rowPrivacy");
    private static final By ROW_VERSION =
            By.id("com.bluesoftware.voicechanger:id/rowVersion");
    private static final By TV_VERSION =
            By.id("com.bluesoftware.voicechanger:id/tvVersion");

    public SettingsPage(AppiumDriver driver) {
        super(driver);
    }

    // ========== UI VERIFY ==========

    public boolean isDisplayed() {
        try {
            return driver.findElements(TITLE).size() > 0
                    && driver.findElements(ROW_OUTPUT_PATH).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        try {
            return driver.findElement(TITLE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isBackButtonDisplayed() {
        return driver.findElements(BACK_BUTTON).size() > 0;
    }

    // ========== GENERAL GROUP ==========

    public boolean isRowOutputPathDisplayed() {
        return driver.findElements(ROW_OUTPUT_PATH).size() > 0;
    }

    public boolean isRowLanguageDisplayed() {
        return driver.findElements(ROW_LANGUAGE).size() > 0;
    }

    public String getOutputPath() {
        try {
            return driver.findElement(TV_OUTPUT_PATH).getText();
        } catch (Exception e) {
            logger.warn("Get output path error: " + e.getMessage());
            return null;
        }
    }

    public String getLanguageValue() {
        try {
            return driver.findElement(TV_LANGUAGE_VALUE).getText();
        } catch (Exception e) {
            logger.warn("Get language value error: " + e.getMessage());
            return null;
        }
    }

    // ========== ABOUT GROUP ==========

    public boolean isRowPrivacyDisplayed() {
        return driver.findElements(ROW_PRIVACY).size() > 0;
    }

    public boolean isRowVersionDisplayed() {
        return driver.findElements(ROW_VERSION).size() > 0;
    }

    public String getVersionValue() {
        try {
            return driver.findElement(TV_VERSION).getText();
        } catch (Exception e) {
            logger.warn("Get version error: " + e.getMessage());
            return null;
        }
    }

    // ========== ACTIONS ==========

    public void clickBack() {
        logger.info("Click nut Back tren Settings");
        click(BACK_BUTTON);
    }

    public void clickOutputPath() {
        logger.info("Click row Output Path");
        click(ROW_OUTPUT_PATH);
    }

    public void clickLanguage() {
        logger.info("Click row Language");
        click(ROW_LANGUAGE);
    }

    public void clickPrivacy() {
        logger.info("Click row Privacy");
        click(ROW_PRIVACY);
    }

    public void clickVersion() {
        logger.info("Click row Version");
        click(ROW_VERSION);
    }
}