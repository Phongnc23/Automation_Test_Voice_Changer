package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Component xu ly bottom sheet chia se cua Android.
 * Dung cho ca Send Voice Message va Share.
 */
public class ShareBottomSheet extends BasePage {

    public static final String PACKAGE = "com.android.intentresolver";

    private static final By CANCEL_BUTTON =
            By.id("com.android.intentresolver:id/oplus_resolve_close_icon");
    private static final By RESOLVER_ITEMS =
            By.id("com.android.intentresolver:id/resolver_item_layout");
    private static final By PREVIEW_FILENAME =
            By.id("com.android.intentresolver:id/content_preview_filename");
    private static final By APP_NAME_TEXT = By.id("android:id/text1");

    public ShareBottomSheet(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Check bottom sheet hien thi (current package = intentresolver).
     */
    public boolean isDisplayed() {
        try {
            String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
            return PACKAGE.equals(currentPackage);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lay ten file dang chia se.
     */
    public String getPreviewFileName() {
        try {
            return driver.findElement(PREVIEW_FILENAME).getText();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Lay danh sach ten cac app trong bottom sheet.
     */
    public List<String> getAvailableApps() {
        List<String> apps = new ArrayList<>();
        try {
            List<WebElement> items = driver.findElements(APP_NAME_TEXT);
            for (WebElement item : items) {
                String name = item.getText();
                if (name != null && !name.isEmpty()) {
                    apps.add(name);
                }
            }
        } catch (Exception e) {
            logger.warn("Cannot get app list: " + e.getMessage());
        }
        return apps;
    }

    public void clickCancel() {
        logger.info("Click Cancel on Share bottom sheet");
        click(CANCEL_BUTTON);
    }
}