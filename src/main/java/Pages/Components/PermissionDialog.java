package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;


public class PermissionDialog extends BasePage {

    private static final By GRANT_DIALOG =
            By.id("com.android.permissioncontroller:id/grant_dialog");
    private static final By PERMISSION_MESSAGE =
            By.id("com.android.permissioncontroller:id/permission_message");
    private static final By PERMISSION_ICON =
            By.id("com.android.permissioncontroller:id/permission_icon");

    private static final By ALLOW_FOREGROUND_BUTTON =
            By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
    private static final By ALLOW_ONE_TIME_BUTTON =
            By.id("com.android.permissioncontroller:id/permission_allow_one_time_button");
    private static final By DENY_BUTTON =
            By.id("com.android.permissioncontroller:id/permission_deny_button");

    public PermissionDialog(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return driver.findElements(GRANT_DIALOG).size() > 0
                    || driver.findElements(PERMISSION_MESSAGE).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getMessage() {
        try {
            return driver.findElement(PERMISSION_MESSAGE).getText();
        } catch (Exception e) {
            logger.warn("Get message error: " + e.getMessage());
            return null;
        }
    }

    public boolean isAllowForegroundButtonDisplayed() {
        return driver.findElements(ALLOW_FOREGROUND_BUTTON).size() > 0;
    }

    public boolean isAllowOneTimeButtonDisplayed() {
        return driver.findElements(ALLOW_ONE_TIME_BUTTON).size() > 0;
    }

    public boolean isDenyButtonDisplayed() {
        return driver.findElements(DENY_BUTTON).size() > 0;
    }

    public String getAllowForegroundButtonText() {
        try {
            return driver.findElement(ALLOW_FOREGROUND_BUTTON).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getAllowOneTimeButtonText() {
        try {
            return driver.findElement(ALLOW_ONE_TIME_BUTTON).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getDenyButtonText() {
        try {
            return driver.findElement(DENY_BUTTON).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public void clickAllowForeground() {
        logger.info("Click 'Trong khi dung ung dung'");
        click(ALLOW_FOREGROUND_BUTTON);
    }

    public void clickAllowOneTime() {
        logger.info("Click 'Chi lan nay'");
        click(ALLOW_ONE_TIME_BUTTON);
    }

    public void clickDeny() {
        logger.info("Click 'Khong cho phep'");
        click(DENY_BUTTON);
    }

    /**
     * Helper: xu ly dialog neu hien thi (click Allow Foreground).
     * Dung khi muon bypass permission de tiep tuc flow.
     */
    public void handlePermissionIfPresent() {
        if (isDisplayed()) {
            logger.info("Permission dialog hien thi -> Allow");
            clickAllowForeground();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                // skip
            }
        }
    }
}