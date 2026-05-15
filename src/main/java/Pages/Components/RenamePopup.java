package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Popup Rename file. Hien khi click Rename trong EditMenu.
 */
public class RenamePopup extends BasePage {

    private static final By TITLE =
            By.id("com.bluesoftware.voicechanger:id/tvTitle");
    private static final By EDIT_NAME =
            By.id("com.bluesoftware.voicechanger:id/edtName");
    private static final By CANCEL_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnCancel");
    private static final By DONE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnDone");

    // Icon X clear text - tim theo XPath neu khong co id chinh xac
    // Co the la ImageView ben canh EditText
    private static final By CLEAR_ICON = By.xpath(
            "//android.widget.EditText[@resource-id='com.bluesoftware.voicechanger:id/edtName']" +
                    "/following-sibling::android.widget.ImageView[1]");

    // Error message - tim theo text
    private static final By ERROR_MESSAGE = By.xpath(
            "//*[contains(@text,'required') or contains(@text,'empty') " +
                    "or contains(@text,'Name')]");

    public RenamePopup(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return driver.findElements(EDIT_NAME).size() > 0
                    && driver.findElements(DONE_BUTTON).size() > 0;
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

    public String getCurrentText() {
        try {
            return driver.findElement(EDIT_NAME).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getCurrentName() {
        try {
            return driver.findElement(EDIT_NAME).getText();
        } catch (Exception e) {
            logger.warn("Cannot get current name: " + e.getMessage());
            return null;
        }
    }

    /**
     * Xoa toan bo text trong textbox.
     * Uu tien click icon X, fallback dung EditText.clear().
     */
    public void clearText() {
        try {
            if (driver.findElements(CLEAR_ICON).size() > 0) {
                logger.info("Clear text bang cach click icon X");
                click(CLEAR_ICON);
            } else {
                logger.info("Clear text bang EditText.clear()");
                driver.findElement(EDIT_NAME).clear();
            }
            Thread.sleep(500);
        } catch (Exception e) {
            logger.warn("Loi clear text: " + e.getMessage());
            try {
                driver.findElement(EDIT_NAME).clear();
            } catch (Exception ex) {
                logger.error("Khong the clear text: " + ex.getMessage());
            }
        }
    }

    public void enterText(String text) {
        logger.info("Nhap ten: " + text);
        WebElement editText = driver.findElement(EDIT_NAME);
        editText.sendKeys(text);
    }

    public void clickCancel() {
        logger.info("Click Cancel");
        click(CANCEL_BUTTON);
    }

    public void clickDone() {
        logger.info("Click Done");
        click(DONE_BUTTON);
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return driver.findElements(ERROR_MESSAGE).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return driver.findElement(ERROR_MESSAGE).getText();
        } catch (Exception e) {
            return null;
        }
    }
}