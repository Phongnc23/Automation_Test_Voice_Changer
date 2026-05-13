package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Page Object cho man System Settings - "Modify system settings" (com.android.settings).
 * Vao tu app khi click Set Ringtone va chua co quyen WRITE_SETTINGS.
 *
 * Layout co:
 * - Toggle Modify system settings
 * - App name + version
 * - Back button
 */
public class SystemSettingsPage extends BasePage {

    // System Settings package
    private static final String SETTINGS_PACKAGE = "com.android.settings";

    // Toggle switch (UiAutomator)
    private static final By TOGGLE_SWITCH =
            By.xpath("//android.widget.Switch | //android.widget.ToggleButton");

    // App name in title bar
    private static final By APP_NAME_TITLE =
            By.xpath("//android.widget.TextView[contains(@text, 'Voice Changer') " +
                    "or contains(@text, 'voicechanger')]");

    // Version text
    private static final By VERSION_TEXT =
            By.xpath("//android.widget.TextView[contains(@text, 'version') " +
                    "or contains(@text, 'Version') or matches(@text, '\\d+\\.\\d+.*')]");

    // Container layout
    private static final By SETTINGS_CONTAINER =
            By.id("android:id/content");

    public SystemSettingsPage(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Kiem tra dang o man System Settings.
     */
    public boolean isDisplayed() {
        try {
            // Cach 1: check current package
            if (driver instanceof AndroidDriver) {
                String currentPkg = ((AndroidDriver) driver).getCurrentPackage();
                logger.info("Current package: " + currentPkg);
                if (currentPkg != null && currentPkg.contains("settings")) {
                    return true;
                }
            }

            // Cach 2: check element dac trung
            return driver.findElements(TOGGLE_SWITCH).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Kiem tra Voice Changer entry hien thi (app name/version).
     */
    public boolean isVoiceChangerEntryDisplayed() {
        return driver.findElements(APP_NAME_TITLE).size() > 0;
    }

    /**
     * Lay ten app hien thi.
     */
    public String getAppName() {
        try {
            return driver.findElement(APP_NAME_TITLE).getText();
        } catch (Exception e) {
            logger.warn("Cannot get app name: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lay version hien thi.
     */
    public String getAppVersion() {
        try {
            if (driver.findElements(VERSION_TEXT).size() > 0) {
                return driver.findElement(VERSION_TEXT).getText();
            }
        } catch (Exception e) {
            logger.warn("Cannot get version: " + e.getMessage());
        }
        return "unknown";
    }

    /**
     * Kiem tra toggle dang OFF.
     */
    public boolean isToggleOff() {
        try {
            WebElement toggle = driver.findElement(TOGGLE_SWITCH);
            String checked = toggle.getAttribute("checked");
            logger.info("Toggle checked: " + checked);
            return "false".equalsIgnoreCase(checked);
        } catch (Exception e) {
            logger.warn("Check toggle off error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Kiem tra toggle dang ON.
     */
    public boolean isToggleOn() {
        try {
            WebElement toggle = driver.findElement(TOGGLE_SWITCH);
            String checked = toggle.getAttribute("checked");
            logger.info("Toggle checked: " + checked);
            return "true".equalsIgnoreCase(checked);
        } catch (Exception e) {
            logger.warn("Check toggle on error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Click toggle de bat/tat permission.
     */
    public void clickToggle() {
        logger.info("Click toggle Modify system settings");
        try {
            driver.findElement(TOGGLE_SWITCH).click();
        } catch (Exception e) {
            logger.error("Click toggle error: " + e.getMessage());
        }
    }

    /**
     * Click back de quay lai app.
     * Dung hardware back button cua Android.
     */
    public void clickBack() {
        logger.info("Press BACK to return to app");
        try {
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        } catch (Exception e) {
            logger.error("Press back error: " + e.getMessage());
        }
    }
}