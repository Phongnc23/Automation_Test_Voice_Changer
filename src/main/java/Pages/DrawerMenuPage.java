package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Page Object cho Drawer Menu (mo khi click hamburger).
 *
 * DOM:
 * - layout_privacy: Privacy policy -> mo browser
 * - layout_rate_us: Rate Us -> mo CH Play
 * - layout_share_app: Share app -> mo share bottom sheet
 * - layout_feedback: Feedback -> mo email chooser
 * - layout_setting: Settings -> mo man Settings (da co o SettingsPage)
 * - layout_version: Version -> toast/no-op
 * - layout_exit: Exit app -> dong app
 */
public class DrawerMenuPage extends BasePage {

    private static final By DRAWER_VIEW =
            By.id("com.bluesoftware.voicechanger:id/drawer_view");

    private static final By PRIVACY =
            By.id("com.bluesoftware.voicechanger:id/layout_privacy");
    private static final By RATE_US =
            By.id("com.bluesoftware.voicechanger:id/layout_rate_us");
    private static final By SHARE_APP =
            By.id("com.bluesoftware.voicechanger:id/layout_share_app");
    private static final By FEEDBACK =
            By.id("com.bluesoftware.voicechanger:id/layout_feedback");
    private static final By SETTINGS =
            By.id("com.bluesoftware.voicechanger:id/layout_setting");
    private static final By VERSION =
            By.id("com.bluesoftware.voicechanger:id/layout_version");
    private static final By VERSION_TEXT =
            By.id("com.bluesoftware.voicechanger:id/text_version");
    private static final By EXIT =
            By.id("com.bluesoftware.voicechanger:id/layout_exit");

    public DrawerMenuPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return driver.findElements(SETTINGS).size() > 0
                    && driver.findElements(PRIVACY).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPrivacyDisplayed() {
        return driver.findElements(PRIVACY).size() > 0;
    }

    public boolean isRateUsDisplayed() {
        return driver.findElements(RATE_US).size() > 0;
    }

    public boolean isShareAppDisplayed() {
        return driver.findElements(SHARE_APP).size() > 0;
    }

    public boolean isFeedbackDisplayed() {
        return driver.findElements(FEEDBACK).size() > 0;
    }

    public boolean isSettingsDisplayed() {
        return driver.findElements(SETTINGS).size() > 0;
    }

    public boolean isVersionDisplayed() {
        return driver.findElements(VERSION).size() > 0;
    }

    public boolean isExitDisplayed() {
        return driver.findElements(EXIT).size() > 0;
    }

    public String getVersionText() {
        try {
            return driver.findElement(VERSION_TEXT).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public void clickPrivacy() {
        logger.info("Click Privacy policy");
        click(PRIVACY);
    }

    public void clickRateUs() {
        logger.info("Click Rate Us");
        click(RATE_US);
    }

    public void clickShareApp() {
        logger.info("Click Share app");
        click(SHARE_APP);
    }

    public void clickFeedback() {
        logger.info("Click Feedback");
        click(FEEDBACK);
    }

    public void clickSettings() {
        logger.info("Click Settings");
        click(SETTINGS);
    }

    public void clickVersion() {
        logger.info("Click Version");
        click(VERSION);
    }

    public void clickExit() {
        logger.info("Click Exit app");
        click(EXIT);
    }

    public String getCurrentPackage() {
        try {
            return ((AndroidDriver) driver).getCurrentPackage();
        } catch (Exception e) {
            return null;
        }
    }
}