package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Component xu ly dialog xin quyen cua Android.
 * Hien khi app lan dau yeu cau quyen (microphone, storage...).
 */
public class PermissionDialog extends BasePage {

    private static final By PERMISSION_MESSAGE =
            By.id("com.android.permissioncontroller:id/permission_message");
    private static final By ALLOW_WHILE_USING =
            By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
    private static final By ALLOW_ONCE =
            By.id("com.android.permissioncontroller:id/permission_allow_one_time_button");
    private static final By DENY =
            By.id("com.android.permissioncontroller:id/permission_deny_button");

    public PermissionDialog(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Check dialog co hien thi khong (timeout ngan 2s).
     */
    public boolean isDisplayed() {
        try {
            return driver.findElements(PERMISSION_MESSAGE).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAllowWhileUsing() {
        logger.info("Click 'Trong khi dung ung dung'");
        click(ALLOW_WHILE_USING);
    }

    public void clickAllowOnce() {
        logger.info("Click 'Chi lan nay'");
        click(ALLOW_ONCE);
    }

    public void clickDeny() {
        logger.info("Click 'Khong cho phep'");
        click(DENY);
    }

    /**
     * Tu dong allow neu dialog hien (no-op neu khong hien).
     * Goi sau cac action co the trigger permission.
     */
    public void handlePermissionIfPresent() {
        try {
            Thread.sleep(1000);
            if (isDisplayed()) {
                logger.info("Permission dialog detected, click Allow While Using");
                clickAllowWhileUsing();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            logger.warn("Error handling permission: " + e.getMessage());
        }
    }
}