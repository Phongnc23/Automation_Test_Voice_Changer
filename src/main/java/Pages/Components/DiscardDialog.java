package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Dialog "Discard changes?" - hien khi:
 * - Nhan X tren man Recorder luc dang ghi am
 * - Nhan X tren man Voice Effects
 */
public class DiscardDialog extends BasePage {

    private static final By DIALOG_TITLE =
            By.id("com.bluesoftware.voicechanger:id/tvTitle");
    private static final By DIALOG_MESSAGE =
            By.id("com.bluesoftware.voicechanger:id/tvMessage");
    private static final By CANCEL_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnCancel");
    private static final By DISCARD_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnConfirm");

    public DiscardDialog(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            if (driver.findElements(DIALOG_TITLE).size() == 0) {
                return false;
            }
            String title = driver.findElement(DIALOG_TITLE).getText();
            return title != null && title.contains("Discard")
                    && driver.findElements(DISCARD_BUTTON).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        try {
            return driver.findElement(DIALOG_TITLE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getMessage() {
        try {
            return driver.findElement(DIALOG_MESSAGE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isCancelButtonDisplayed() {
        return driver.findElements(CANCEL_BUTTON).size() > 0;
    }

    public boolean isDiscardButtonDisplayed() {
        return driver.findElements(DISCARD_BUTTON).size() > 0;
    }

    public void clickCancel() {
        logger.info("Click Cancel tren Discard dialog");
        click(CANCEL_BUTTON);
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clickDiscard() {
        logger.info("Click Discard tren Discard dialog");
        click(DISCARD_BUTTON);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}