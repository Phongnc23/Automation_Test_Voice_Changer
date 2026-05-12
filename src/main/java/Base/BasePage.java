package Base;

import Constants.TimeOutConstants;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {

    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TimeOutConstants.MEDIUM_WAIT));
    }

    protected WebElement findElement(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element not found: " + locator);
            throw e;
        }
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        logger.info("Click element: " + locator);
        element.click();
    }

    protected void sendKeys(By locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        logger.info("Type '" + text + "' into: " + locator);
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        String text = findElement(locator).getText();
        logger.info("Get text from " + locator + ": " + text);
        return text;
    }

    protected boolean isDisplayed(By locator) {
        try {
            return findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitForElementVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getCurrentPackage() {
        return ((AndroidDriver) driver).getCurrentPackage();
    }

    public String getCurrentActivity() {
        return ((AndroidDriver) driver).currentActivity();
    }

    protected void swipeBack() {
        Utils.GestureUtils.swipeFromLeftEdgeToBack(driver);
        sleep(Constants.TimeOutConstants.SLEEP_SHORT);
    }
}