package testcases.MyAudio;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.MyAudioPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * MA_01: Verify UI man My Audio (6 tests).
 * Pattern: Share session - mo My Audio 1 lan.
 */
public class MyAudio01_Verify_UI_Display extends BaseTest {

    private MyAudioPage myAudioPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP MY AUDIO SESSION ===");
        try {
            ensureAtLeastOneFile();
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
        try {
            if (!"Search".equals(myAudioPage.getSearchText())) {
                myAudioPage.clearSearch();
                Thread.sleep(300);
                myAudioPage.hideKeyboard();
            }
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

    private void ensureAtLeastOneFile() {
        try {
            MyAudioPage temp = RecordFlowHelper.navigateToMyAudio(driver);
            if (temp.hasAtLeastOneFile()) return;

            RecordFlowHelper.smartResetToHome(driver);
            Thread.sleep(800);
            RecordFlowHelper.navigateToAudioSaved(driver, 1);
            Thread.sleep(800);
            RecordFlowHelper.smartResetToHome(driver);
            Thread.sleep(800);
        } catch (Exception e) {
            logger.warn("ensureAtLeastOneFile error: " + e.getMessage());
        }
    }

    @Test(priority = 1, description = "MA_01_01: Mo My Audio tu Home")
    public void test_MA_01_01_navigate_to_my_audio() {
        Assert.assertTrue(myAudioPage.isDisplayed(),
                "Khong vao duoc man My Audio");
        Assert.assertTrue(myAudioPage.hasAtLeastOneFile(),
                "List rong, can co toi thieu 1 file");
        ExtentReportManager.getTest().log(Status.PASS,
                "Vao My Audio thanh cong, " + myAudioPage.getItemCount() + " file");
    }

    @Test(priority = 2, description = "MA_01_02: Verify nut Back")
    public void test_MA_01_02_verify_back_button() {
        Assert.assertTrue(myAudioPage.isBackButtonDisplayed(),
                "Nut Back khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut Back hien thi");
    }

    @Test(priority = 3, description = "MA_01_03: Verify title 'My audio'")
    public void test_MA_01_03_verify_title() {
        String title = myAudioPage.getTitle();
        ExtentReportManager.getTest().log(Status.INFO, "Title: " + title);
        Assert.assertNotNull(title, "Title null");
        Assert.assertTrue(title.equalsIgnoreCase("My audio"),
                "Title sai: " + title);
        ExtentReportManager.getTest().log(Status.PASS, "Title dung");
    }

    @Test(priority = 4, description = "MA_01_04: Verify thanh search")
    public void test_MA_01_04_verify_search_box() {
        Assert.assertTrue(myAudioPage.isSearchBoxDisplayed(),
                "Search box khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Search box hien thi");
    }

    @Test(priority = 5, description = "MA_01_05: Verify cau truc card file")
    public void test_MA_01_05_verify_card_structure() {
        String firstName = myAudioPage.getFirstFileName();
        String firstInfo = myAudioPage.getFirstFileInfo();

        ExtentReportManager.getTest().log(Status.INFO,
                "Ten: " + firstName + " | Info: " + firstInfo);

        Assert.assertNotNull(firstName, "tvName khong hien thi");
        Assert.assertNotNull(firstInfo, "tvInfo khong hien thi");
        Assert.assertTrue(firstName.length() > 0, "Ten file rong");
        Assert.assertTrue(firstInfo.contains("KB") || firstInfo.contains("MB"),
                "Info khong co dung luong: " + firstInfo);
        ExtentReportManager.getTest().log(Status.PASS,
                "Card structure dung");
    }

    @Test(priority = 6, description = "MA_01_06: Click Back -> ve Home")
    public void test_MA_01_06_back_to_home() throws InterruptedException {
        myAudioPage.clickBack();
        Thread.sleep(1200);

        boolean atHome = driver.findElements(
                By.id("com.bluesoftware.voicechanger:id/layout_record")
        ).size() > 0;
        Assert.assertTrue(atHome, "Khong ve Home");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home");
    }
}