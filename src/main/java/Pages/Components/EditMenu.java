package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Menu hien thi khi click icon 3 cham tren file card.
 * Co 2 item: Rename va Open with.
 */
public class EditMenu extends BasePage {

    private static final By ITEM_RENAME =
            By.id("com.bluesoftware.voicechanger:id/itemRename");
    private static final By ITEM_OPEN_WITH =
            By.id("com.bluesoftware.voicechanger:id/itemOpenWith");

    public EditMenu(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return driver.findElements(ITEM_RENAME).size() > 0
                    && driver.findElements(ITEM_OPEN_WITH).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRenameItemDisplayed() {
        try {
            return driver.findElements(ITEM_RENAME).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOpenWithItemDisplayed() {
        try {
            return driver.findElements(ITEM_OPEN_WITH).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickRename() {
        logger.info("Click Rename item");
        click(ITEM_RENAME);
    }

    public void clickOpenWith() {
        logger.info("Click Open with item");
        click(ITEM_OPEN_WITH);
    }
}