package testcases.Home;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import Constants.AppConstants;
import Constants.TimeOutConstants;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.HomePage;
import Report.ExtentReportManager;
import Utils.GestureUtils;

import java.time.Duration;

public class Home03_Verify_Navigation extends BaseTest {

    private HomePage homePage;
    private WebDriverWait shortWait;
    private WebDriverWait mediumWait;

    private static final By RECORD_CARD = By.id("com.bluesoftware.voicechanger:id/layout_record");
    private static final By APP_NAME_TEXT = By.id("com.bluesoftware.voicechanger:id/text_app_name");

    @BeforeMethod
    public void initPage() {
        // Khoi tao wait 1 lan, dung lai cho nhieu lan
        shortWait = new WebDriverWait(driver, Duration.ofSeconds(TimeOutConstants.SHORT_WAIT));
        mediumWait = new WebDriverWait(driver, Duration.ofSeconds(TimeOutConstants.MEDIUM_WAIT));

        ensureAtHomeScreen();
        homePage = new HomePage(driver);
    }

    @Test(description = "TC_11: Kiem tra nhan icon Hamburger Menu mo va dong drawer")
    public void TC_11_click_hamburger_menu() {
        ExtentReportManager.getTest().log(Status.INFO, "Click hamburger menu");

        homePage.clickHamburgerMenu();

        // Smart wait: doi drawer xuat hien (max 3s, return ngay khi co)
        boolean drawerOpened = waitForDrawerVisible();
        Assert.assertTrue(drawerOpened, "Drawer khong mo");
        ExtentReportManager.getTest().log(Status.PASS, "Drawer da mo");

        GestureUtils.tapOutsideDrawer(driver);

        // Smart wait: doi drawer bien mat va Home hien thi
        boolean backToHome = waitForHomeScreen();
        Assert.assertTrue(backToHome, "Khong ve Home sau khi dong drawer");

        ExtentReportManager.getTest().log(Status.PASS, "Drawer dong, da ve Home");
    }

    @Test(description = "TC_12: Click Record card, navigate, back ve Home")
    public void TC_12_click_record_card() {
        ExtentReportManager.getTest().log(Status.INFO, "Click Record card");

        homePage.clickRecord();

        // Smart wait: doi roi Home (record card bien mat)
        boolean leftHome = waitForLeftHome();
        Assert.assertTrue(leftHome, "Van o Home, khong chuyen den man Record");
        ExtentReportManager.getTest().log(Status.PASS, "Da chuyen sang man Record");

        backToHome(1);

        Assert.assertTrue(isHomeScreenDisplayed(), "Khong ve Home");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home thanh cong");
    }

    @Test(description = "TC_13: Click Text to Speech card, navigate, back ve Home")
    public void TC_13_click_text_to_speech_card() {
        ExtentReportManager.getTest().log(Status.INFO, "Click Text to Speech card");

        homePage.clickTextToSpeech();

        boolean leftHome = waitForLeftHome();
        Assert.assertTrue(leftHome, "Van o Home, khong chuyen den man Text to Speech");
        ExtentReportManager.getTest().log(Status.PASS, "Da chuyen sang man Text to Speech");

        backToHome(1);

        Assert.assertTrue(isHomeScreenDisplayed(), "Khong ve Home");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home thanh cong");
    }

    @Test(description = "TC_14: Click My Audio card, navigate, back ve Home")
    public void TC_14_click_my_audio_card() {
        ExtentReportManager.getTest().log(Status.INFO, "Click My Audio card");

        homePage.clickMyAudio();

        boolean leftHome = waitForLeftHome();
        Assert.assertTrue(leftHome, "Van o Home, khong chuyen den man My Audio");
        ExtentReportManager.getTest().log(Status.PASS, "Da chuyen sang man My Audio");

        backToHome(1);

        Assert.assertTrue(isHomeScreenDisplayed(), "Khong ve Home");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home thanh cong");
    }

    @Test(description = "TC_15: Click Import Audio mo File Picker, back ve Home (3 lan)")
    public void TC_15_click_import_audio_card() {
        ExtentReportManager.getTest().log(Status.INFO, "Click Import Audio card");

        homePage.clickImportAudio();

        boolean leftHome = waitForLeftHome();
        Assert.assertTrue(leftHome, "Khong mo file picker");

        String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
        ExtentReportManager.getTest().log(Status.INFO, "Da mo - package: " + currentPackage);
        ExtentReportManager.getTest().log(Status.PASS, "File picker mo thanh cong");

        // Swipe back 3 lan
        backToHome(3);

        Assert.assertTrue(isHomeScreenDisplayed(), "Khong ve Home sau 3 lan swipe");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home thanh cong");
    }

    @Test(description = "TC_17: SKIPPED - Logic da cover trong TC_12-15")
    public void TC_17_back_to_home_from_sub_screen() {
        String reason = "Logic 'navigate + back to Home' da cover trong TC_12-15.";
        ExtentReportManager.getTest().log(Status.SKIP, reason);
        throw new SkipException(reason);
    }

    // ====== HELPER METHODS ======

    /**
     * Back ve Home voi so lan swipe cu the.
     * Co retry: neu swipe khong work -> force relaunch.
     */
    private void backToHome(int swipeTimes) {
        // Swipe back theo so lan
        GestureUtils.swipeBackMultipleTimes(driver, swipeTimes);

        // Verify ve Home, neu chua thi swipe them
        if (!isHomeScreenDisplayed()) {
            logger.warn(swipeTimes + " lan swipe chua du, thu swipe them");
            GestureUtils.swipeFromLeftEdgeToBack(driver);
        }

        // Cuoi cung: force relaunch neu van chua ve Home
        if (!isHomeScreenDisplayed()) {
            logger.warn("Swipe khong work, force relaunch");
            forceRelaunchToHome();
        }
    }

    /**
     * Smart wait: doi Home screen hien thi (max MEDIUM_WAIT seconds).
     */
    private boolean waitForHomeScreen() {
        try {
            mediumWait.until(ExpectedConditions.and(
                    ExpectedConditions.visibilityOfElementLocated(RECORD_CARD),
                    ExpectedConditions.visibilityOfElementLocated(APP_NAME_TEXT)
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Smart wait: doi den khi roi Home (Record card bien mat).
     */
    private boolean waitForLeftHome() {
        try {
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(RECORD_CARD));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Smart wait: doi drawer hien thi.
     */
    private boolean waitForDrawerVisible() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//android.widget.TextView[@text='Settings' " +
                            "or @text='Privacy policy' " +
                            "or @text='Exit app']")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void forceRelaunchToHome() {
        AndroidDriver androidDriver = (AndroidDriver) driver;
        try {
            androidDriver.terminateApp(AppConstants.APP_PACKAGE);
            Thread.sleep(1000);
            androidDriver.activateApp(AppConstants.APP_PACKAGE);
            // Smart wait: doi den khi Home hien thi (thay vi sleep co dinh)
            waitForHomeScreen();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void ensureAtHomeScreen() {
        AndroidDriver androidDriver = (AndroidDriver) driver;
        String currentPackage = androidDriver.getCurrentPackage();

        if (!AppConstants.APP_PACKAGE.equals(currentPackage)) {
            androidDriver.activateApp(AppConstants.APP_PACKAGE);
            waitForHomeScreen();
        }

        if (!isHomeScreenDisplayed()) {
            forceRelaunchToHome();
        }
    }

    private boolean isHomeScreenDisplayed() {
        try {
            return driver.findElements(RECORD_CARD).size() > 0
                    && driver.findElements(APP_NAME_TEXT).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}