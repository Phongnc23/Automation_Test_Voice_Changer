package testcases.MyAudio;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.Components.MyAudioEditMenu;
import Pages.Components.RenamePopup;
import Pages.MyAudioPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

import java.time.Duration;

/**
 * MA_04: Test Rename function tu Edit Menu (4 tests).
 * Test 01-03 non-destructive, test 04 (success) priority 10.
 */
public class MyAudio04_Verify_Rename extends BaseTest {

    private MyAudioPage myAudioPage;
    private MyAudioEditMenu editMenu;
    private RenamePopup renamePopup;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP RENAME SUITE ===");
        try {
            RecordFlowHelper.ensureAtLeastOneFile(driver);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            editMenu = new MyAudioEditMenu(driver);
            renamePopup = new RenamePopup(driver);
        } catch (Exception e) {
            logger.error("Setup error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            editMenu = new MyAudioEditMenu(driver);
            renamePopup = new RenamePopup(driver);
        }
    }

    @BeforeMethod
    public void ensureCleanState() {
        if (renamePopup.isDisplayed()) {
            try {
                renamePopup.clickCancel();
                Thread.sleep(800);
            } catch (Exception e) {
                // skip
            }
        }
        if (editMenu.isDisplayed()) {
            try {
                editMenu.closeByTapOutside();
                Thread.sleep(600);
            } catch (Exception e) {
                // skip
            }
        }
        if (!myAudioPage.isDisplayed()) {
            try {
                RecordFlowHelper.smartResetToHome(driver);
                myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            } catch (Exception e) {
                logger.error("Re-navigate error: " + e.getMessage());
            }
        }
    }

    @AfterMethod
    public void cleanupBetweenTests() {
        try {
            if (renamePopup.isDisplayed()) {
                renamePopup.clickCancel();
                Thread.sleep(600);
            }
            if (editMenu.isDisplayed()) {
                editMenu.closeByTapOutside();
                Thread.sleep(600);
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

    private void openRenamePopup(int itemIndex) throws InterruptedException {
        // Smart wait + retry up to 3 attempts (flaky: bottom sheet animation
        // sometimes drops click). 500ms settle between attempts.
        for (int attempt = 1; attempt <= 3; attempt++) {
            myAudioPage.clickMoreAt(itemIndex);
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .ignoring(Exception.class)
                        .until(d -> editMenu.isDisplayed());
                break;
            } catch (Exception e) {
                if (attempt < 3) {
                    logger.warn("Edit menu khong mo lan " + attempt
                            + ", retry sau 500ms");
                    Thread.sleep(500);
                }
            }
        }
        Assert.assertTrue(editMenu.isDisplayed(), "Edit menu khong mo");

        editMenu.clickRename();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> renamePopup.isDisplayed());
        } catch (Exception e) {
            // Will fail at assertion below
        }
        Assert.assertTrue(renamePopup.isDisplayed(),
                "Rename popup khong mo");
    }

    @Test(priority = 1, description = "MA_04_01: Mo rename popup")
    public void test_MA_04_01_open_rename_popup()
            throws InterruptedException {
        openRenamePopup(1);

        String currentName = renamePopup.getCurrentName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten trong popup: " + currentName);
        Assert.assertNotNull(currentName, "edtName null");
        Assert.assertTrue(currentName.length() > 0, "edtName rong");
        ExtentReportManager.getTest().log(Status.PASS,
                "Rename popup mo dung");
    }

    @Test(priority = 2, description = "MA_04_02: Rename voi ten rong")
    public void test_MA_04_02_rename_empty_name()
            throws InterruptedException {
        openRenamePopup(1);

        renamePopup.clearText();
        Thread.sleep(300);
        renamePopup.clickDone();
        Thread.sleep(1000);  // M1: Giam tu 1500

        Assert.assertTrue(renamePopup.isDisplayed(),
                "Popup phai van mo khi ten rong");
        ExtentReportManager.getTest().log(Status.PASS,
                "Rename ten rong bi chan dung");
    }

    @Test(priority = 3, description = "MA_04_03: Rename - click Cancel")
    public void test_MA_04_03_rename_cancel()
            throws InterruptedException {
        String originalName = myAudioPage.getFirstFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten goc: " + originalName);

        openRenamePopup(1);

        renamePopup.clearText();
        Thread.sleep(300);
        renamePopup.enterText("temp_should_not_save");
        Thread.sleep(300);
        renamePopup.clickCancel();
        Thread.sleep(1000);  // M1: Giam tu 1500

        String afterCancel = myAudioPage.getFirstFileName();
        Assert.assertEquals(afterCancel, originalName,
                "Ten file bi doi du da Cancel");
        ExtentReportManager.getTest().log(Status.PASS,
                "Cancel hoat dong dung");
    }

    @Test(priority = 10, description = "MA_04_04: Rename thanh cong (DESTRUCTIVE)")
    public void test_MA_04_04_rename_success()
            throws InterruptedException {
        String originalName = myAudioPage.getFirstFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten goc: " + originalName);

        openRenamePopup(1);

        String newName = "ma_test_" + System.currentTimeMillis() % 100000;
        renamePopup.clearText();
        Thread.sleep(300);
        renamePopup.enterText(newName);
        Thread.sleep(300);
        renamePopup.clickDone();
        Thread.sleep(1200);  // M1: Giam tu 2000

        Assert.assertFalse(renamePopup.isDisplayed(),
                "Popup phai dong sau Done");

        boolean foundRenamed = myAudioPage.hasFileContaining(newName);
        Assert.assertTrue(foundRenamed,
                "Khong thay file rename: " + newName);
        ExtentReportManager.getTest().log(Status.PASS,
                "Rename: " + originalName + " -> " + newName);
    }
}