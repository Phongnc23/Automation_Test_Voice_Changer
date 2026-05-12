package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Page Object cho man hinh Modify System Settings cua Android.
 * Package: com.android.settings
 */
public class SystemSettingsPage extends BasePage {

    public static final String PACKAGE = "com.android.settings";

    private static final By BACK_BUTTON =
            By.id("com.android.settings:id/coui_toolbar_back_view");
    private static final By SCREEN_TITLE = By.xpath(
            "//android.widget.TextView[contains(@text,'Chinh sua') " +
                    "or contains(@text,'Modify system')]");
    private static final By APP_TITLE =
            By.id("com.android.settings:id/entity_header_title");
    private static final By APP_VERSION =
            By.id("com.android.settings:id/entity_header_summary");
    private static final By TOGGLE_SWITCH =
            By.id("android:id/switch_widget");

    public SystemSettingsPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
            return PACKAGE.equals(currentPackage)
                    && driver.findElements(TOGGLE_SWITCH).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isVoiceChangerEntryDisplayed() {
        try {
            String appName = driver.findElement(APP_TITLE).getText();
            return appName != null && appName.toLowerCase().contains("voice");
        } catch (Exception e) {
            return false;
        }
    }

    public String getAppName() {
        try {
            return driver.findElement(APP_TITLE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getAppVersion() {
        try {
            return driver.findElement(APP_VERSION).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isToggleOn() {
        try {
            WebElement toggle = driver.findElement(TOGGLE_SWITCH);
            String text = toggle.getText();
            // OPPO hien "Bat"/"Tat", Android goc dung attribute checked
            if (text != null) {
                return text.equalsIgnoreCase("Bat") || text.equalsIgnoreCase("On");
            }
            String checked = toggle.getAttribute("checked");
            return "true".equalsIgnoreCase(checked);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isToggleOff() {
        return !isToggleOn();
    }

    public void clickToggle() {
        logger.info("Click toggle Modify System Settings");
        click(TOGGLE_SWITCH);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clickBack() {
        logger.info("Click back button");
        click(BACK_BUTTON);
    }
}