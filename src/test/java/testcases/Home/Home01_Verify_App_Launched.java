package testcases.Home;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import Constants.AppConstants;
import Constants.TimeOutConstants;
import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.HomePage;
import Report.ExtentReportManager;

public class Home01_Verify_App_Launched extends BaseTest {

    @Test(description = "Kiem tra app Voice Changer mo thanh cong")
    public void test_app_launched_successfully() throws InterruptedException {
        logger.info("Test: Verify app launched successfully");

        // Doi app load
        Thread.sleep(TimeOutConstants.SLEEP_MEDIUM);

        HomePage homePage = new HomePage(driver);

        // Verify current package
        String currentPackage = homePage.getCurrentPackage();
        logger.info("Current package: " + currentPackage);
        ExtentReportManager.getTest().log(Status.INFO, "Current package: " + currentPackage);

        Assert.assertEquals(currentPackage, AppConstants.APP_PACKAGE,
                "App khong khoi chay dung package!");

        // Verify Home page hien thi
        Assert.assertTrue(homePage.isHomePageDisplayed(),
                "Man hinh Home khong hien thi!");

        ExtentReportManager.getTest().log(Status.PASS, "App Voice Changer da mo thanh cong");
        logger.info("✓ App Voice Changer da mo thanh cong");
    }

    @Test(description = "Kiem tra activity hien tai cua app")
    public void test_get_current_activity() {
        logger.info("Test: Get current activity");

        HomePage homePage = new HomePage(driver);
        String currentActivity = homePage.getCurrentActivity();

        logger.info("Current activity: " + currentActivity);
        ExtentReportManager.getTest().log(Status.INFO, "Current activity: " + currentActivity);

        Assert.assertNotNull(currentActivity, "Activity khong duoc null!");

        ExtentReportManager.getTest().log(Status.PASS, "Activity hien tai: " + currentActivity);
        logger.info("✓ Get activity thanh cong: " + currentActivity);
    }
}