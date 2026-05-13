package Pages.Components;

import Base.BasePage;
import Utils.GestureUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Component cho bottom sheet share (Android intent chooser - OPPO/ColorOS).
 */
public class ShareBottomSheet extends BasePage {

    private static final By CANCEL_CONTAINER =
            By.id("com.android.intentresolver:id/ll_cancel");
    private static final By CANCEL_BUTTON =
            By.id("com.android.intentresolver:id/oplus_resolve_close_icon");
    private static final By PREVIEW_FILENAME =
            By.id("com.android.intentresolver:id/content_preview_filename");
    private static final By APP_ITEMS =
            By.id("android:id/item");
    private static final By RESOLVER_ITEMS =
            By.id("com.android.intentresolver:id/resolver_item_layout");

    public ShareBottomSheet(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return driver.findElements(PREVIEW_FILENAME).size() > 0
                    || driver.findElements(CANCEL_BUTTON).size() > 0
                    || driver.findElements(CANCEL_CONTAINER).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPreviewFileName() {
        try {
            return driver.findElement(PREVIEW_FILENAME).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isPreviewFileNameDisplayed() {
        return driver.findElements(PREVIEW_FILENAME).size() > 0;
    }

    public List<String> getAvailableApps() {
        List<String> appNames = new ArrayList<>();
        try {
            List<WebElement> items = driver.findElements(APP_ITEMS);
            for (WebElement item : items) {
                try {
                    String desc = item.getAttribute("content-desc");
                    if (desc != null && !desc.isEmpty()) {
                        appNames.add(desc);
                    }
                } catch (Exception e) {
                    // skip
                }
            }
        } catch (Exception e) {
            logger.warn("Error getting apps: " + e.getMessage());
        }
        return appNames;
    }

    public int countAvailableApps() {
        int count = 0;
        try {
            count += driver.findElements(APP_ITEMS).size();
            count += driver.findElements(RESOLVER_ITEMS).size();
        } catch (Exception e) {
            logger.warn("Count error: " + e.getMessage());
        }
        return count;
    }

    /**
     * Dong bottom sheet - thu nhieu chien luoc theo thu tu uu tien.
     * Bottom sheet chiem 50-70% chieu cao man hinh tu duoi.
     */
    public void clickCancel() {
        logger.info("Click Huy tren share bottom sheet");
        try {
            if (driver.findElements(CANCEL_BUTTON).size() > 0) {
                driver.findElement(CANCEL_BUTTON).click();
            } else if (driver.findElements(CANCEL_CONTAINER).size() > 0) {
                driver.findElement(CANCEL_CONTAINER).click();
            }
            Thread.sleep(500);  // Giam tu 1500 xuong 500
        } catch (Exception e) {
            logger.warn("Click Cancel error: " + e.getMessage());
        }
    }

    /**
     * Strategy 1: Tap vao vung trong suot phia tren bottom sheet.
     * Bottom sheet thuong chiem 50-70% chieu cao tu duoi -> vung 10% top trong suot.
     */
    private boolean tapOutsideBottomSheet() {
        try {
            Dimension size = driver.manage().window().getSize();
            int tapX = size.getWidth() / 2;
            int tapY = (int) (size.getHeight() * 0.05);  // 5% top

            logger.info("Strategy 1: Tap outside (x=" + tapX + ", y=" + tapY + ")");
            GestureUtils.tap(driver, tapX, tapY);
            Thread.sleep(1500);

            if (!isDisplayed()) {
                logger.info("Tap outside thanh cong");
                return true;
            }
            logger.warn("Tap outside KHONG dong sheet, thu strategy khac");
            return false;
        } catch (Exception e) {
            logger.warn("Tap outside error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Strategy 2: Press hardware back button.
     */
    private boolean pressBackButton() {
        try {
            logger.info("Strategy 2: Press BACK button");
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            Thread.sleep(1500);

            if (!isDisplayed()) {
                logger.info("Press BACK thanh cong");
                return true;
            }
            logger.warn("Press BACK KHONG dong sheet");
            return false;
        } catch (Exception e) {
            logger.warn("Press BACK error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Strategy 3: Swipe back tu canh trai cua man hinh (gesture iOS-like).
     */
    private boolean swipeBackGesture() {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = 5;
            int endX = size.getWidth() / 2;
            int y = size.getHeight() / 2;

            logger.info("Strategy 3: Swipe back tu canh trai");
            GestureUtils.swipe(driver, startX, y, endX, y, 500);
            Thread.sleep(1500);

            if (!isDisplayed()) {
                logger.info("Swipe back thanh cong");
                return true;
            }
            logger.warn("Swipe back KHONG dong sheet");
            return false;
        } catch (Exception e) {
            logger.warn("Swipe back error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Strategy 4: Click element Cancel button (last resort).
     */
    private boolean clickCancelElement() {
        try {
            logger.info("Strategy 4: Click element Cancel");
            if (driver.findElements(CANCEL_BUTTON).size() > 0) {
                driver.findElement(CANCEL_BUTTON).click();
            } else if (driver.findElements(CANCEL_CONTAINER).size() > 0) {
                driver.findElement(CANCEL_CONTAINER).click();
            } else {
                logger.warn("Khong tim thay element Cancel");
                return false;
            }
            Thread.sleep(1500);

            if (!isDisplayed()) {
                logger.info("Click element Cancel thanh cong");
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.warn("Click element error: " + e.getMessage());
            return false;
        }
    }

    public boolean isCancelButtonDisplayed() {
        return driver.findElements(CANCEL_BUTTON).size() > 0
                || driver.findElements(CANCEL_CONTAINER).size() > 0;
    }
}