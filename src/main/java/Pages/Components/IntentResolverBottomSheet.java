package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Page Object cho Android system intent resolver (chooser) khi share TEXT/URL.
 *
 * Dung cho:
 *   - DrawerMenu Share App: chia se text co URL voi cac app
 *   - DrawerMenu Feedback: chia se mail intent voi email apps
 *
 * Khac voi {@link ShareBottomSheet} (dung cho share FILE audio voi preview filename).
 *
 * Resource-id: com.android.intentresolver (OPPO/ColorOS system).
 */
public class IntentResolverBottomSheet extends BasePage {

    private static final By CANCEL_BUTTON =
            By.id("com.android.intentresolver:id/oplus_resolve_close_icon");
    private static final By PREVIEW_TEXT =
            By.id("android:id/content_preview_text");
    private static final By APP_ITEMS =
            By.id("com.android.intentresolver:id/resolver_item_layout");

    public IntentResolverBottomSheet(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return driver.findElements(CANCEL_BUTTON).size() > 0;
    }

    public boolean isPreviewTextDisplayed() {
        return driver.findElements(PREVIEW_TEXT).size() > 0;
    }

    public String getPreviewText() {
        try {
            return driver.findElement(PREVIEW_TEXT).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public int countApps() {
        try {
            return driver.findElements(APP_ITEMS).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickCancel() {
        logger.info("Click Cancel tren intent resolver bottom sheet");
        click(CANCEL_BUTTON);
    }

    /**
     * Locator de smart wait tu test class.
     */
    public static By getCancelButtonLocator() {
        return CANCEL_BUTTON;
    }
}
