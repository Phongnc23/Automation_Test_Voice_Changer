package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Bottom sheet Edit Menu - mo khi click btnMore tren item My Audio.
 *
 * DOM moi: 4 action layouts (Send Voice Message da bi remove khoi DOM):
 * - layout_share ("Share")
 * - layout_rename ("Rename")
 * - layout_set_as_ringtone ("Set as ringtone")
 * - layout_delete ("Delete")
 */
public class MyAudioEditMenu extends BasePage {

    private static final By BOTTOM_SHEET =
            By.id("com.bluesoftware.voicechanger:id/design_bottom_sheet");
    private static final By HEADER_NAME =
            By.id("com.bluesoftware.voicechanger:id/tvName");
    private static final By HEADER_INFO =
            By.id("com.bluesoftware.voicechanger:id/tvInfo");
    private static final By TOUCH_OUTSIDE =
            By.id("com.bluesoftware.voicechanger:id/touch_outside");

    private static final By SHARE =
            By.id("com.bluesoftware.voicechanger:id/layout_share");
    private static final By RENAME =
            By.id("com.bluesoftware.voicechanger:id/layout_rename");
    private static final By SET_RINGTONE =
            By.id("com.bluesoftware.voicechanger:id/layout_set_as_ringtone");
    private static final By DELETE =
            By.id("com.bluesoftware.voicechanger:id/layout_delete");

    public MyAudioEditMenu(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return driver.findElements(BOTTOM_SHEET).size() > 0
                    && driver.findElements(SHARE).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getHeaderFileName() {
        try {
            return driver.findElement(HEADER_NAME).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getHeaderFileInfo() {
        try {
            return driver.findElement(HEADER_INFO).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isShareDisplayed() {
        return driver.findElements(SHARE).size() > 0;
    }

    public boolean isRenameDisplayed() {
        return driver.findElements(RENAME).size() > 0;
    }

    public boolean isSetRingtoneDisplayed() {
        return driver.findElements(SET_RINGTONE).size() > 0;
    }

    public boolean isDeleteDisplayed() {
        return driver.findElements(DELETE).size() > 0;
    }

    public int countActions() {
        int count = 0;
        if (isShareDisplayed()) count++;
        if (isRenameDisplayed()) count++;
        if (isSetRingtoneDisplayed()) count++;
        if (isDeleteDisplayed()) count++;
        return count;
    }

    public void clickShare() {
        logger.info("Click Share");
        click(SHARE);
    }

    public void clickRename() {
        logger.info("Click Rename");
        click(RENAME);
    }

    public void clickSetRingtone() {
        logger.info("Click Set as ringtone");
        click(SET_RINGTONE);
    }

    public void clickDelete() {
        logger.info("Click Delete");
        click(DELETE);
    }

    public void closeByTapOutside() {
        logger.info("Dong edit menu bang tap outside");
        try {
            click(TOUCH_OUTSIDE);
            Thread.sleep(600);
        } catch (Exception e) {
            logger.warn("Tap outside error: " + e.getMessage());
        }
    }
}
