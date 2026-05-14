package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object cho man hinh Language Selection.
 * Truy cap: Text to Audio -> click btnLanguage.
 */
public class LanguageDropdownPage extends BasePage {

    private static final By CONTAINER =
            By.id("com.bluesoftware.voicechanger:id/container");

    public LanguageDropdownPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return driver.findElements(CONTAINER).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lay danh sach ten ngon ngu visible tren man hinh.
     */
    public List<String> getVisibleLanguages() {
        List<String> langs = new ArrayList<>();
        try {
            List<WebElement> textViews = driver.findElements(
                    By.xpath("//android.widget.TextView"));
            for (WebElement tv : textViews) {
                try {
                    String text = tv.getText();
                    if (text != null && (text.startsWith("Tieng")
                            || text.startsWith("Ti\u1ebfng"))) {
                        langs.add(text);
                    }
                } catch (Exception e) {
                    // skip
                }
            }
        } catch (Exception e) {
            logger.warn("Get languages error: " + e.getMessage());
        }
        return langs;
    }

    public int countVisibleLanguages() {
        return getVisibleLanguages().size();
    }

    public boolean isLanguageDisplayed(String languageName) {
        try {
            List<WebElement> elements = driver.findElements(
                    By.xpath("//android.widget.TextView[@text='" +
                            languageName + "']"));
            return !elements.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectLanguage(String languageName) {
        logger.info("Chon ngon ngu: " + languageName);
        try {
            WebElement lang = driver.findElement(
                    By.xpath("//android.widget.TextView[@text='" +
                            languageName + "']"));
            lang.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error("Select language error: " + e.getMessage());
        }
    }

    public void selectLanguageContains(String partialName) {
        logger.info("Chon ngon ngu chua: " + partialName);
        try {
            WebElement lang = driver.findElement(
                    By.xpath("//android.widget.TextView[contains(@text, '" +
                            partialName + "')]"));
            lang.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error("Select language contains error: " + e.getMessage());
        }
    }
}