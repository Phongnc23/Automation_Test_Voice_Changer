package Pages.Components;

import Base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Bottom sheet Language Selection.
 * Mo bang: Settings -> click row Language.
 *
 * DOM:
 * - design_bottom_sheet
 * - TextView "Select Language" (header)
 * - rvLanguage (RecyclerView)
 *   - Moi item co tvLanguageName + imgRadio
 * - btnOk
 * - touch_outside (de dong)
 */
public class LanguageDialog extends BasePage {

    private static final By BOTTOM_SHEET =
            By.id("com.bluesoftware.voicechanger:id/design_bottom_sheet");
    private static final By LANGUAGE_RECYCLER =
            By.id("com.bluesoftware.voicechanger:id/rvLanguage");
    private static final By LANGUAGE_NAME =
            By.id("com.bluesoftware.voicechanger:id/tvLanguageName");
    private static final By OK_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnOk");
    private static final By TOUCH_OUTSIDE =
            By.id("com.bluesoftware.voicechanger:id/touch_outside");

    public LanguageDialog(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return driver.findElements(BOTTOM_SHEET).size() > 0
                    && driver.findElements(LANGUAGE_RECYCLER).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lay tat ca ten ngon ngu trong list.
     */
    public List<String> getAllLanguages() {
        List<String> names = new ArrayList<>();
        try {
            List<WebElement> items = driver.findElements(LANGUAGE_NAME);
            for (WebElement el : items) {
                try {
                    names.add(el.getText());
                } catch (Exception e) {
                    // skip
                }
            }
        } catch (Exception e) {
            logger.warn("Get languages error: " + e.getMessage());
        }
        return names;
    }

    /**
     * Lay so ngon ngu trong list.
     */
    public int getLanguageCount() {
        return driver.findElements(LANGUAGE_NAME).size();
    }

    /**
     * Click chon ngon ngu theo ten.
     * Return true neu click duoc, false neu khong tim thay.
     */
    public boolean selectLanguage(String languageName) {
        logger.info("Chon ngon ngu: " + languageName);
        try {
            List<WebElement> items = driver.findElements(LANGUAGE_NAME);
            for (WebElement el : items) {
                if (languageName.equalsIgnoreCase(el.getText())) {
                    el.click();
                    return true;
                }
            }
            logger.warn("Khong tim thay language: " + languageName);
            return false;
        } catch (Exception e) {
            logger.error("Select language error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Kiem tra ngon ngu co trong list khong.
     */
    public boolean hasLanguage(String languageName) {
        return getAllLanguages().stream()
                .anyMatch(n -> n.equalsIgnoreCase(languageName));
    }

    public void clickOk() {
        logger.info("Click OK button");
        click(OK_BUTTON);
    }

    public void closeByTapOutside() {
        logger.info("Tap outside de dong dialog");
        try {
            click(TOUCH_OUTSIDE);
            Thread.sleep(600);
        } catch (Exception e) {
            logger.warn("Tap outside error: " + e.getMessage());
        }
    }
}