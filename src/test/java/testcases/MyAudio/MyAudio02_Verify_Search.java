package testcases.MyAudio;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.MyAudioPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * MA_02: Test search functionality (4 tests).
 * Pattern: Share session + clear search sau moi test.
 */
public class MyAudio02_Verify_Search extends BaseTest {

    private MyAudioPage myAudioPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP SEARCH SUITE ===");
        try {
            // M6: Dung helper trung tam
            RecordFlowHelper.ensureAtLeastOneFile(driver);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
        } catch (Exception e) {
            logger.error("Setup error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
        }
    }

    @BeforeMethod
    public void ensureCleanState() {
        if (!myAudioPage.isDisplayed()) {
            try {
                myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            } catch (Exception e) {
                logger.error("Re-navigate error: " + e.getMessage());
            }
        }
    }

    @AfterMethod
    public void clearSearch() {
        try {
            myAudioPage.clearSearch();
            Thread.sleep(300);
            myAudioPage.hideKeyboard();
            Thread.sleep(300);
        } catch (Exception e) {
            // skip
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    @Test(priority = 1, description = "MA_02_01: Search co ket qua")
    public void test_MA_02_01_search_with_results()
            throws InterruptedException {
        int totalBefore = myAudioPage.getItemCount();
        ExtentReportManager.getTest().log(Status.INFO,
                "Tong truoc search: " + totalBefore);

        myAudioPage.enterSearchText("record");
        Thread.sleep(1000);

        int totalAfter = myAudioPage.getItemCount();
        ExtentReportManager.getTest().log(Status.INFO,
                "Tong sau search 'record': " + totalAfter);

        if (totalAfter > 0) {
            for (String name : myAudioPage.getAllFileNames()) {
                Assert.assertTrue(name.toLowerCase().contains("record"),
                        "File khong khop search: " + name);
            }
            ExtentReportManager.getTest().log(Status.PASS,
                    "Search dung: " + totalAfter + " file khop");
        } else {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Khong co file record_*, list rong la dung");
        }
    }

    @Test(priority = 2, description = "MA_02_02: Search khong co ket qua")
    public void test_MA_02_02_search_no_results()
            throws InterruptedException {
        myAudioPage.enterSearchText("xyz_khong_co_file_999");
        Thread.sleep(1000);

        int total = myAudioPage.getItemCount();
        Assert.assertEquals(total, 0,
                "List phai rong khi search khong khop");

        if (myAudioPage.isEmptyTextDisplayed()) {
            ExtentReportManager.getTest().log(Status.INFO,
                    "Empty text: " + myAudioPage.getEmptyText());
        }
        ExtentReportManager.getTest().log(Status.PASS,
                "Search khong khop -> list rong");
    }

    @Test(priority = 3, description = "MA_02_03: Clear search restore list")
    public void test_MA_02_03_clear_search_restore()
            throws InterruptedException {
        int totalOriginal = myAudioPage.getItemCount();
        ExtentReportManager.getTest().log(Status.INFO,
                "Tong goc: " + totalOriginal);

        myAudioPage.enterSearchText("record");
        Thread.sleep(1000);
        int filteredCount = myAudioPage.getItemCount();
        ExtentReportManager.getTest().log(Status.INFO,
                "Sau filter: " + filteredCount);

        myAudioPage.clearSearch();
        Thread.sleep(800);
        myAudioPage.hideKeyboard();
        Thread.sleep(500);

        int totalAfterClear = myAudioPage.getItemCount();
        ExtentReportManager.getTest().log(Status.INFO,
                "Sau clear: " + totalAfterClear);

        Assert.assertEquals(totalAfterClear, totalOriginal,
                "List khong restore day du");
        ExtentReportManager.getTest().log(Status.PASS, "Restore thanh cong");
    }

    @Test(priority = 4, description = "MA_02_04: Search ky tu dac biet")
    public void test_MA_02_04_search_special_chars()
            throws InterruptedException {
        myAudioPage.enterSearchText("@#$%^&");
        Thread.sleep(1000);

        Assert.assertTrue(myAudioPage.isDisplayed(),
                "App crash hoac roi khoi My Audio");

        int count = myAudioPage.getItemCount();
        Assert.assertEquals(count, 0,
                "Search ky tu dac biet phai cho list rong");
        ExtentReportManager.getTest().log(Status.PASS,
                "Search ky tu dac biet xu ly dung");
    }
}