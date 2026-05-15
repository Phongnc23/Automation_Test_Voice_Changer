package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object cho man hinh My Audio.
 * Truy cap: Home -> click card 'My audio'.
 *
 * DOM elements:
 * - btnBack, tvTitle "My audio", edtSearch, rvAudio, tvEmpty
 *
 * Item structure (trong rvAudio):
 * - image_icon, tvName, tvInfo, btnMore
 */
public class MyAudioPage extends BasePage {

    private static final By BACK_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnBack");
    private static final By TITLE =
            By.id("com.bluesoftware.voicechanger:id/tvTitle");
    private static final By SEARCH_BOX =
            By.id("com.bluesoftware.voicechanger:id/edtSearch");
    private static final By RECYCLER_VIEW =
            By.id("com.bluesoftware.voicechanger:id/rvAudio");
    private static final By EMPTY_TEXT =
            By.id("com.bluesoftware.voicechanger:id/tvEmpty");

    private static final By ITEM_NAME =
            By.id("com.bluesoftware.voicechanger:id/tvName");
    private static final By ITEM_INFO =
            By.id("com.bluesoftware.voicechanger:id/tvInfo");
    private static final By ITEM_MORE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnMore");

    public MyAudioPage(AppiumDriver driver) {
        super(driver);
    }

    // ========== UI VERIFY ==========

    public boolean isDisplayed() {
        try {
            return driver.findElements(TITLE).size() > 0
                    && driver.findElements(SEARCH_BOX).size() > 0;
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

    public boolean isBackButtonDisplayed() {
        return driver.findElements(BACK_BUTTON).size() > 0;
    }

    public boolean isSearchBoxDisplayed() {
        return driver.findElements(SEARCH_BOX).size() > 0;
    }

    public boolean isRecyclerViewDisplayed() {
        return driver.findElements(RECYCLER_VIEW).size() > 0;
    }

    public boolean isEmptyTextDisplayed() {
        return driver.findElements(EMPTY_TEXT).size() > 0;
    }

    public String getEmptyText() {
        try {
            return driver.findElement(EMPTY_TEXT).getText();
        } catch (Exception e) {
            return null;
        }
    }

    // ========== ITEM LIST OPERATIONS ==========

    public int getItemCount() {
        return driver.findElements(ITEM_NAME).size();
    }

    public List<String> getAllFileNames() {
        List<String> names = new ArrayList<>();
        try {
            List<WebElement> items = driver.findElements(ITEM_NAME);
            for (WebElement el : items) {
                try {
                    names.add(el.getText());
                } catch (Exception e) {
                    // skip
                }
            }
        } catch (Exception e) {
            logger.warn("Get file names error: " + e.getMessage());
        }
        return names;
    }

    public String getFirstFileName() {
        List<String> names = getAllFileNames();
        return names.isEmpty() ? null : names.get(0);
    }

    public String getFirstFileInfo() {
        try {
            List<WebElement> infos = driver.findElements(ITEM_INFO);
            if (!infos.isEmpty()) {
                return infos.get(0).getText();
            }
        } catch (Exception e) {
            logger.warn("Get file info error: " + e.getMessage());
        }
        return null;
    }

    public boolean hasAtLeastOneFile() {
        return getItemCount() > 0;
    }

    public boolean hasFileContaining(String text) {
        for (String name : getAllFileNames()) {
            if (name != null && name.contains(text)) {
                return true;
            }
        }
        return false;
    }

    // ========== ACTIONS ==========

    public void clickBack() {
        logger.info("Click nut Back tren My Audio");
        click(BACK_BUTTON);
    }

    public void clickItemAt(int index) {
        logger.info("Click item tai index " + index);
        try {
            List<WebElement> items = driver.findElements(ITEM_NAME);
            if (index >= items.size()) {
                logger.error("Index " + index + " out of bound: " + items.size());
                return;
            }
            items.get(index).click();
        } catch (Exception e) {
            logger.error("Click item error: " + e.getMessage());
        }
    }

    public void clickMoreAt(int index) {
        logger.info("Click btnMore tai index " + index);
        try {
            List<WebElement> moreButtons = driver.findElements(ITEM_MORE_BUTTON);
            if (index >= moreButtons.size()) {
                logger.error("Index " + index + " out of bound: " + moreButtons.size());
                return;
            }
            moreButtons.get(index).click();
        } catch (Exception e) {
            logger.error("Click more error: " + e.getMessage());
        }
    }

    // ========== SEARCH ==========

    public void clickSearchBox() {
        logger.info("Click search box");
        click(SEARCH_BOX);
    }

    public void enterSearchText(String text) {
        logger.info("Nhap search: " + text);
        try {
            WebElement search = driver.findElement(SEARCH_BOX);
            search.click();
            Thread.sleep(300);
            search.sendKeys(text);
        } catch (Exception e) {
            logger.error("Search error: " + e.getMessage());
        }
    }

    public void clearSearch() {
        logger.info("Clear search box");
        try {
            WebElement search = driver.findElement(SEARCH_BOX);
            search.clear();
        } catch (Exception e) {
            logger.error("Clear search error: " + e.getMessage());
        }
    }

    public String getSearchText() {
        try {
            return driver.findElement(SEARCH_BOX).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public void hideKeyboard() {
        try {
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            Thread.sleep(500);
        } catch (Exception e) {
            logger.warn("Hide keyboard error: " + e.getMessage());
        }
    }
}