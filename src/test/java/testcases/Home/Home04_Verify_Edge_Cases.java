package testcases.Home;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import Constants.AppConstants;
import Constants.TimeOutConstants;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import Pages.HomePage;
import Report.ExtentReportManager;

/**
 * Test class cho edge cases man hinh Home.
 * Cover: TC_19, TC_21
 *
 * Note: TC_22 (xoay man hinh) bi SKIP do app khoa orientation o portrait
 *       (do developer set "screenOrientation=portrait" trong AndroidManifest).
 */
public class Home04_Verify_Edge_Cases extends BaseTest {

    @Test(description = "TC_19: Kiem tra ung dung khi chay nen va quay lai")
    public void TC_19_background_and_resume() throws InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO, "Test background and resume app");

        Thread.sleep(TimeOutConstants.SLEEP_SHORT);
        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home khong hien thi");

        // Dua app xuong nen (press Home)
        ExtentReportManager.getTest().log(Status.INFO, "Press Home key");
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.HOME));
        Thread.sleep(2000);

        // Activate app lai
        ExtentReportManager.getTest().log(Status.INFO, "Activate app again");
        ((AndroidDriver) driver).activateApp(AppConstants.APP_PACKAGE);
        Thread.sleep(TimeOutConstants.SLEEP_MEDIUM);

        // Verify Home van hien thi dung
        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(),
                "Home khong hien thi sau khi resume");
        Assert.assertTrue(homePage.isRecordCardDisplayed(),
                "Record card khong hien thi sau resume");
        Assert.assertTrue(homePage.isImportAudioCardDisplayed(),
                "Import Audio card khong hien thi sau resume");

        ExtentReportManager.getTest().log(Status.PASS, "App resume dung trang thai");
    }

    @Test(description = "TC_21: Kiem tra toc do tai man hinh Home (cold start)")
    public void TC_21_cold_start_performance() throws InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO, "Test cold start performance");

        // Terminate app
        ((AndroidDriver) driver).terminateApp(AppConstants.APP_PACKAGE);
        Thread.sleep(2000);

        // Do thoi gian tu khi launch den khi Home hien thi
        long startTime = System.currentTimeMillis();

        ((AndroidDriver) driver).activateApp(AppConstants.APP_PACKAGE);

        // Doi den khi Home hien thi
        HomePage homePage = new HomePage(driver);
        homePage.isHomePageDisplayed(); // co wait ben trong

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        ExtentReportManager.getTest().log(Status.INFO,
                "Cold start time: " + loadTime + " ms");

        // Test case yeu cau: Home hien thi trong vong 3 giay
        Assert.assertTrue(loadTime < 3000,
                "Cold start qua cham: " + loadTime + "ms (> 3000ms)");

        ExtentReportManager.getTest().log(Status.PASS,
                "Cold start dat yeu cau (" + loadTime + "ms < 3000ms)");
    }

    /**
     * TC_22: Xoay man hinh - BI SKIP
     * Ly do: App khoa orientation = portrait trong AndroidManifest.xml
     *        => khong the xoay landscape duoc.
     *        Day la feature cua app, khong phai bug.
     */
    @Test(description = "TC_22: Kiem tra xoay ngang man hinh - SKIPPED (app locked portrait)")
    public void TC_22_landscape_orientation() {
        ExtentReportManager.getTest().log(Status.SKIP,
                "Skip TC_22: App khoa orientation = portrait (developer config)");

        throw new SkipException("App locked at portrait orientation - cannot test landscape");
    }
}