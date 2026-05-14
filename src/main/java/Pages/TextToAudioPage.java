package Pages;

import Base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class TextToAudioPage extends BasePage {

    private static final By BACK_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnBack");
    private static final By TITLE =
            By.id("com.bluesoftware.voicechanger:id/tvTitle");
    private static final By LANGUAGE_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnLanguage");
    private static final By LANGUAGE_TEXT =
            By.id("com.bluesoftware.voicechanger:id/tvLanguage");
    private static final By LABEL_ENTER_TEXT =
            By.id("com.bluesoftware.voicechanger:id/labelEnterText");
    private static final By EDIT_TEXT =
            By.id("com.bluesoftware.voicechanger:id/edtText");
    private static final By NEXT_BUTTON =
            By.id("com.bluesoftware.voicechanger:id/btnNext");

    public TextToAudioPage(AppiumDriver driver) {
        super(driver);
    }

    // ========== UI VERIFY ==========

    public boolean isDisplayed() {
        try {
            return driver.findElements(TITLE).size() > 0
                    && driver.findElements(EDIT_TEXT).size() > 0
                    && driver.findElements(NEXT_BUTTON).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBackButtonDisplayed() {
        return driver.findElements(BACK_BUTTON).size() > 0;
    }

    public boolean isTitleDisplayed() {
        return driver.findElements(TITLE).size() > 0;
    }

    public String getTitle() {
        try {
            return driver.findElement(TITLE).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isLanguageDropdownDisplayed() {
        return driver.findElements(LANGUAGE_BUTTON).size() > 0;
    }

    public String getCurrentLanguage() {
        try {
            return driver.findElement(LANGUAGE_TEXT).getText();
        } catch (Exception e) {
            logger.warn("Cannot get language: " + e.getMessage());
            return null;
        }
    }

    public boolean isLabelEnterTextDisplayed() {
        return driver.findElements(LABEL_ENTER_TEXT).size() > 0;
    }

    public boolean isEditTextDisplayed() {
        return driver.findElements(EDIT_TEXT).size() > 0;
    }

    public boolean isNextButtonDisplayed() {
        return driver.findElements(NEXT_BUTTON).size() > 0;
    }

    public String getEnteredText() {
        try {
            WebElement edt = driver.findElement(EDIT_TEXT);
            String text = edt.getText();
            if (text != null && text.equals(
                    "Enter text to generate audio, then add effects")) {
                return "";
            }
            return text;
        } catch (Exception e) {
            return null;
        }
    }

    public String getPlaceholderText() {
        try {
            WebElement edt = driver.findElement(EDIT_TEXT);
            return edt.getAttribute("text");
        } catch (Exception e) {
            return null;
        }
    }

    // ========== ACTIONS ==========

    public void clickBack() {
        logger.info("Click nut Back tren Text to Audio");
        click(BACK_BUTTON);
    }

    public void clickLanguageDropdown() {
        logger.info("Click dropdown ngon ngu");
        click(LANGUAGE_BUTTON);
    }

    public void clickNext() {
        logger.info("Click nut Next");
        click(NEXT_BUTTON);
    }

    public void clickEditText() {
        logger.info("Click edit text de focus");
        click(EDIT_TEXT);
    }

    public void enterText(String text) {
        logger.info("Nhap text: " + text);
        try {
            WebElement edt = driver.findElement(EDIT_TEXT);
            edt.sendKeys(text);
        } catch (Exception e) {
            logger.error("Enter text error: " + e.getMessage());
        }
    }

    public void clearAndEnterText(String text) {
        logger.info("Clear va nhap text: " + text);
        try {
            WebElement edt = driver.findElement(EDIT_TEXT);
            edt.clear();
            Thread.sleep(300);
            edt.sendKeys(text);
        } catch (Exception e) {
            logger.error("Clear+enter error: " + e.getMessage());
        }
    }

    public void clearEditText() {
        logger.info("Clear edit text");
        try {
            WebElement edt = driver.findElement(EDIT_TEXT);
            edt.clear();
        } catch (Exception e) {
            logger.error("Clear error: " + e.getMessage());
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