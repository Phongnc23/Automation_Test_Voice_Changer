package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Dialog xac nhan Delete file (Android AlertDialog).
 * Mo bang: edit menu -> click Delete.
 */
public class MyAudioDeleteDialog extends BasePage {

    private static final By ALERT_TITLE = By.id("android:id/alertTitle");
    private static final By MESSAGE = By.id("android:id/message");
    private static final By BUTTON_DELETE = By.id("android:id/button1");
    private static final By BUTTON_CANCEL = By.id("android:id/button2");

    public MyAudioDeleteDialog(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return driver.findElements(ALERT_TITLE).size() > 0
                    && driver.findElements(BUTTON_DELETE).size() > 0
                    && driver.findElements(BUTTON_CANCEL).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        try {
            return driver.findElement(ALERT_TITLE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getMessage() {
        try {
            return driver.findElement(MESSAGE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getDeleteButtonText() {
        try {
            return driver.findElement(BUTTON_DELETE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getCancelButtonText() {
        try {
            return driver.findElement(BUTTON_CANCEL).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public void clickDelete() {
        logger.info("Click DELETE (confirm)");
        click(BUTTON_DELETE);
    }

    public void clickCancel() {
        logger.info("Click CANCEL");
        click(BUTTON_CANCEL);
    }
}