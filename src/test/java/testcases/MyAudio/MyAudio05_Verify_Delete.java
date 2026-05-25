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
import Pages.Components.MyAudioDeleteDialog;
import Pages.Components.MyAudioEditMenu;
import Pages.MyAudioPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

import java.time.Duration;

/**
 * MA_05: Test Delete function (3 tests).
 * Test 01-02 non-destructive, test 03 (confirm delete) priority 10.
 * Tao file moi truoc khi delete de tranh xoa file goc.
 */
public class MyAudio05_Verify_Delete extends BaseTest {

    private MyAudioPage myAudioPage;
    private MyAudioEditMenu editMenu;
    private MyAudioDeleteDialog deleteDialog;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP DELETE SUITE ===");
        try {
            ensureBufferFiles(2);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            editMenu = new MyAudioEditMenu(driver);
            deleteDialog = new MyAudioDeleteDialog(driver);
        } catch (Exception e) {
            logger.error("Setup error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            editMenu = new MyAudioEditMenu(driver);
            deleteDialog = new MyAudioDeleteDialog(driver);
        }
    }

    @BeforeMethod
    public void ensureCleanState() {
        if (deleteDialog.isDisplayed()) {
            try {
                deleteDialog.clickCancel();
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
            if (deleteDialog.isDisplayed()) {
                deleteDialog.clickCancel();
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

    private void ensureBufferFiles(int n) {
        try {
            MyAudioPage temp = RecordFlowHelper.navigateToMyAudio(driver);
            int count = temp.getItemCount();
            if (count >= n) return;

            int needToCreate = n - count;
            for (int i = 0; i < needToCreate; i++) {
                RecordFlowHelper.smartResetToHome(driver);
                Thread.sleep(800);
                RecordFlowHelper.navigateToAudioSaved(driver, 1);
                Thread.sleep(800);
            }
            RecordFlowHelper.smartResetToHome(driver);
            Thread.sleep(800);
        } catch (Exception e) {
            logger.warn("ensureBufferFiles error: " + e.getMessage());
        }
    }

    private void openDeleteDialog(int itemIndex) throws InterruptedException {
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

        editMenu.clickDelete();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> deleteDialog.isDisplayed());
        } catch (Exception e) {
            // Will fail at assertion below
        }
        Assert.assertTrue(deleteDialog.isDisplayed(),
                "Delete dialog khong mo");
    }

    @Test(priority = 1, description = "MA_05_01: Mo delete dialog")
    public void test_MA_05_01_open_delete_dialog()
            throws InterruptedException {
        openDeleteDialog(1);

        String title = deleteDialog.getTitle();
        String message = deleteDialog.getMessage();
        String deleteBtn = deleteDialog.getDeleteButtonText();
        String cancelBtn = deleteDialog.getCancelButtonText();

        ExtentReportManager.getTest().log(Status.INFO,
                "Title: " + title + " | Buttons: " + deleteBtn + "/" + cancelBtn);

        Assert.assertNotNull(title, "Title null");
        Assert.assertTrue(title.toLowerCase().contains("delete"),
                "Title sai: " + title);
        Assert.assertNotNull(message, "Message null");
        Assert.assertTrue(message.toLowerCase().contains("delete"),
                "Message khong chua 'delete': " + message);
        Assert.assertTrue(deleteBtn != null
                        && deleteBtn.toUpperCase().contains("DELETE"),
                "Button delete sai: " + deleteBtn);
        Assert.assertTrue(cancelBtn != null
                        && cancelBtn.toUpperCase().contains("CANCEL"),
                "Button cancel sai: " + cancelBtn);

        ExtentReportManager.getTest().log(Status.PASS,
                "Delete dialog hien thi day du");
    }

    @Test(priority = 2, description = "MA_05_02: Delete - click Cancel")
    public void test_MA_05_02_delete_cancel()
            throws InterruptedException {
        String originalName = myAudioPage.getFirstFileName();
        int originalCount = myAudioPage.getItemCount();

        openDeleteDialog(1);

        deleteDialog.clickCancel();
        Thread.sleep(1500);

        Assert.assertFalse(deleteDialog.isDisplayed(),
                "Dialog phai dong");

        Assert.assertEquals(myAudioPage.getItemCount(), originalCount,
                "So file thay doi du Cancel");
        Assert.assertEquals(myAudioPage.getFirstFileName(), originalName,
                "Ten file thay doi du Cancel");
        ExtentReportManager.getTest().log(Status.PASS,
                "Cancel hoat dong dung");
    }

    @Test(priority = 10, description = "MA_05_03: Confirm DELETE (DESTRUCTIVE)")
    public void test_MA_05_03_delete_confirm()
            throws InterruptedException {
        // Khong can tao file moi - da co nhieu file tu test truoc

        // Ghi nho ten file INDEX 1 (file se bi xoa)
        java.util.List<String> namesBefore = myAudioPage.getAllFileNames();
        int countBefore = namesBefore.size();

        Assert.assertTrue(countBefore >= 2,
                "Can >= 2 file de test");

        String fileToDelete = namesBefore.get(1);  // ← File INDEX 1
        ExtentReportManager.getTest().log(Status.INFO,
                "Xoa file: " + fileToDelete + ", tong truoc: " + countBefore);

        // Mo dialog delete cho file index 1
        openDeleteDialog(1);

        final String fileToDeleteFinal = fileToDelete;
        deleteDialog.clickDelete();

        // Smart wait: doi file da xoa BIEN MAT khoi visible list.
        // Khong dung count check vi RecyclerView virtualized:
        // delete 1 item -> item khac tu duoi scroll vao -> count co the GIU NGUYEN.
        // File existence check moi la authoritative.
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .ignoring(Exception.class)
                    .until(d -> !myAudioPage.hasFileContaining(fileToDeleteFinal));
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

        // Verify dialog dong
        Assert.assertFalse(deleteDialog.isDisplayed(),
                "Dialog phai dong sau Delete");

        // PRIMARY check: file da xoa khoi list
        boolean stillThere = myAudioPage.hasFileContaining(fileToDelete);
        Assert.assertFalse(stillThere,
                "File van con sau Delete: " + fileToDelete);

        // INFO: count co the giam HOAC giu nguyen (RecyclerView virtualization).
        int countAfter = myAudioPage.getItemCount();
        ExtentReportManager.getTest().log(Status.INFO,
                "Visible items: " + countBefore + " -> " + countAfter
                        + " (count co the khong giam neu list virtualized)");

        ExtentReportManager.getTest().log(Status.PASS,
                "Delete thanh cong: " + fileToDelete);
    }
}