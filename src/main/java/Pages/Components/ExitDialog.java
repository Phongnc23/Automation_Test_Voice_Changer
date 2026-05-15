package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Bottom sheet confirm Exit App.
 * Mo bang: Drawer Menu -> click 'Exit app'.
 *
 * DOM:
 * - design_bottom_sheet
 * - TextView "Exit app?" (title)
 * - TextView "Are you sure you want to exit Voice Changer?" (message)
 * - btnCancel
 * - btnExit
 */
public class ExitDialog extends BasePage {

    private static final By BOTTOM_SHEET =
            By.id("com.bluesoftware.voicechanger:id/design_bottom_sheet");
    private static final By BUTTON_CANCEL =
            By.id("com.bluesoftware.voicechanger:id/btnCancel");
    private static final By BUTTON_EXIT =
            By.id("com.bluesoftware.voicechanger:id/btnExit");
    private static final By TITLE_TEXT =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Exit app\")");
    private static final By MESSAGE_TEXT =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"sure you want to exit\")");

    public ExitDialog(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Verify dialog hien thi bang cach check ca 2 button.
     */
    public boolean isDisplayed() {
        try {
            return driver.findElements(BUTTON_CANCEL).size() > 0
                    && driver.findElements(BUTTON_EXIT).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitleText() {
        try {
            List<WebElement> els = driver.findElements(TITLE_TEXT);
            if (!els.isEmpty()) return els.get(0).getText();
        } catch (Exception e) {
            logger.warn("Get title error: " + e.getMessage());
        }
        return null;
    }

    public String getMessageText() {
        try {
            List<WebElement> els = driver.findElements(MESSAGE_TEXT);
            if (!els.isEmpty()) return els.get(0).getText();
        } catch (Exception e) {
            logger.warn("Get message error: " + e.getMessage());
        }
        return null;
    }

    public String getCancelButtonText() {
        try {
            return driver.findElement(BUTTON_CANCEL).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getExitButtonText() {
        try {
            return driver.findElement(BUTTON_EXIT).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public void clickCancel() {
        logger.info("Click Cancel - giu app");
        click(BUTTON_CANCEL);
    }

    public void clickExit() {
        logger.info("Click Exit - dong app");
        click(BUTTON_EXIT);
    }
}