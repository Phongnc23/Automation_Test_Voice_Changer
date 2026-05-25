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
import Pages.Components.ShareBottomSheet;
import Pages.MyAudioPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

import java.time.Duration;

/**
 * MA_08: Test Share tu Edit Menu (4 tests).
 * Luong: My Audio -> btnMore -> Edit Menu -> Share -> Share sheet.
 */
public class MyAudio08_Verify_Share_From_Edit_Menu extends BaseTest {

    private MyAudioPage myAudioPage;
    private MyAudioEditMenu editMenu;
    private ShareBottomSheet shareSheet;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP SHARE FROM EDIT MENU ===");
        try {
            RecordFlowHelper.ensureAtLeastOneFile(driver);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            editMenu = new MyAudioEditMenu(driver);
            shareSheet = new ShareBottomSheet(driver);
        } catch (Exception e) {
            logger.error("Setup error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            editMenu = new MyAudioEditMenu(driver);
            shareSheet = new ShareBottomSheet(driver);
        }
    }

    @BeforeMethod
    public void ensureCleanState() {
        if (shareSheet.isDisplayed()) {
            try {
                shareSheet.clickCancel();
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
            if (shareSheet.isDisplayed()) {
                shareSheet.clickCancel();
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


    private void openShareSheet(int itemIndex) throws InterruptedException {
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

        editMenu.clickShare();
        // L1: Smart wait share sheet thay sleep(2500)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .ignoring(Exception.class)
                    .until(d -> shareSheet.isDisplayed());
        } catch (Exception e) {
            // se fail o assertion phia duoi
        }
    }

    @Test(priority = 1, description = "MA_08_01: Click Share -> mo share sheet")
    public void test_MA_08_01_open_share_sheet_via_share()
            throws InterruptedException {
        openShareSheet(1);

        Assert.assertTrue(shareSheet.isDisplayed(),
                "Share sheet khong mo");
        ExtentReportManager.getTest().log(Status.PASS,
                "Share sheet mo thanh cong");
    }

    @Test(priority = 2, description = "MA_08_02: Share sheet hien thi ten file dung")
    public void test_MA_08_02_share_sheet_shows_correct_filename()
            throws InterruptedException {
        // Lay ten file INDEX 1 (file se mo share)
        String originalName = myAudioPage.getAllFileNames().get(1);
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten file goc (index 1): " + originalName);

        // Buoc 1: Click btnMore tai index 1
        myAudioPage.clickMoreAt(1);
        // L1: Smart wait edit menu thay sleep(1500)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> editMenu.isDisplayed());
        } catch (Exception e) {
            // se fail o assertion phia duoi
        }

        // Buoc 2: Verify edit menu mo bang header text (linh hoat hon isDisplayed)
        String headerInMenu = editMenu.getHeaderFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Header trong edit menu: " + headerInMenu);
        Assert.assertNotNull(headerInMenu,
                "Edit menu khong mo - header null");
        Assert.assertEquals(headerInMenu, originalName,
                "Header menu khong khop file da click");

        editMenu.clickShare();
        // L1: Smart wait share sheet thay sleep(1000)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> shareSheet.isDisplayed());
        } catch (Exception e) {
            // se fail o assertion phia duoi
        }

        // Buoc 4: Verify share sheet mo bang preview file name
        String fileNameInSheet = shareSheet.getPreviewFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Ten file trong share sheet: " + fileNameInSheet);

        Assert.assertNotNull(fileNameInSheet,
                "Share sheet khong mo - preview name null");
        Assert.assertEquals(fileNameInSheet, originalName,
                "Ten file trong share sheet khong khop file da chon");

        ExtentReportManager.getTest().log(Status.PASS,
                "Share sheet hien thi dung: " + fileNameInSheet);
    }

    @Test(priority = 3, description = "MA_08_03: Share sheet hien thi danh sach app")
    public void test_MA_08_03_share_sheet_lists_apps()
            throws InterruptedException {
        openShareSheet(1);

        Assert.assertTrue(shareSheet.isDisplayed(), "Share sheet khong mo");

        int appCount = shareSheet.getAppsCount();
        Assert.assertTrue(appCount > 0, "Phai co toi thieu 1 app");
        ExtentReportManager.getTest().log(Status.PASS,
                "Co " + appCount + " app");
    }

    @Test(priority = 4, description = "MA_08_04: Dong share sheet bang Huy")
    public void test_MA_08_04_close_share_sheet_with_cancel()
            throws InterruptedException {
        openShareSheet(1);

        Assert.assertTrue(shareSheet.isDisplayed(), "Share sheet khong mo");

        shareSheet.clickCancel();
        // L1: Smart wait sheet close thay sleep(1500)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> !shareSheet.isDisplayed());
        } catch (Exception e) {
            // se fail o assertion phia duoi
        }

        Assert.assertFalse(shareSheet.isDisplayed(),
                "Sheet KHONG dong");
        Assert.assertTrue(myAudioPage.isDisplayed(),
                "Khong tro lai My Audio");
        Assert.assertTrue(myAudioPage.hasAtLeastOneFile(),
                "File bi xoa? Phai van con");
        ExtentReportManager.getTest().log(Status.PASS,
                "Dong share sheet thanh cong");
    }
}