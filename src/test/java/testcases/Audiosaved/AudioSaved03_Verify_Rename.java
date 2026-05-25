package testcases.Audiosaved;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.Components.EditMenu;
import Pages.Components.RenamePopup;
import Report.ExtentReportManager;
import Utils.GestureUtils;
import Utils.RecordFlowHelper;


public class AudioSaved03_Verify_Rename extends BaseTest {

    private AudioSavedPage audioSavedPage;
    private EditMenu editMenu;
    private RenamePopup renamePopup;

    private static final int RECORDING_SECONDS = 1;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupAudioSavedSession() {
        logger.info("=== SETUP AUDIO SAVED SESSION ===");
        try {
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(
                    driver, RECORDING_SECONDS);
            editMenu = new EditMenu(driver);
            renamePopup = new RenamePopup(driver);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            audioSavedPage = RecordFlowHelper.navigateToAudioSaved(
                    driver, RECORDING_SECONDS);
            editMenu = new EditMenu(driver);
            renamePopup = new RenamePopup(driver);
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        logger.info("=== CLEANUP ===");
        try {
            // Dong moi popup/menu con mo
            if (renamePopup != null && renamePopup.isDisplayed()) {
                renamePopup.clickCancel();
                Thread.sleep(500);
            }
            if (editMenu != null && editMenu.isDisplayed()) {
                tapOutsideToCloseMenu();
                Thread.sleep(500);
            }
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    /**
     * Safety net truoc moi test: dong moi popup neu lo van mo.
     */
    @BeforeMethod
    public void ensureCleanState() throws InterruptedException {
        if (renamePopup != null && renamePopup.isDisplayed()) {
            logger.info("Popup rename con mo, dong");
            renamePopup.clickCancel();
            Thread.sleep(500);
        }
        if (editMenu != null && editMenu.isDisplayed()) {
            logger.info("Menu edit con mo, dong");
            tapOutsideToCloseMenu();
            Thread.sleep(500);
        }
    }

    /**
     * Tap vung trong suot phia tren de dong menu.
     */
    private void tapOutsideToCloseMenu() {
        Dimension size = driver.manage().window().getSize();
        GestureUtils.tap(driver, size.getWidth() / 2, 100);
    }

    /**
     * Helper: mo Edit menu (click 3 cham).
     */
    private void openEditMenu() throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(500);  // M1: Giam tu 800 - menu xuat hien nhanh
    }

    /**
     * Helper: mo Rename popup (click 3 cham -> Rename).
     */
    private void openRenamePopup() throws InterruptedException {
        openEditMenu();
        editMenu.clickRename();
        Thread.sleep(700);  // M1: Giam tu 1000 - popup hien thi nhanh
    }

    // ========================================================
    // GROUP 1: Test menu (NON-DESTRUCTIVE)
    // ========================================================

    @Test(priority = 1, description = "SAV_04_01: Mo menu 3 cham")
    public void test_SAV_04_01_three_dot_menu_opens()
            throws InterruptedException {
        openEditMenu();

        Assert.assertTrue(editMenu.isDisplayed(), "Menu khong mo");
        Assert.assertTrue(editMenu.isRenameItemDisplayed());
        Assert.assertTrue(editMenu.isOpenWithItemDisplayed());
        ExtentReportManager.getTest().log(Status.PASS, "Menu mo voi 2 item");

        // Cleanup: tap outside de dong menu
        tapOutsideToCloseMenu();
        Thread.sleep(500);
    }

    @Test(priority = 2, description = "SAV_04_02: Tap outside dong menu")
    public void test_SAV_04_02_menu_closes_on_tap_outside()
            throws InterruptedException {
        openEditMenu();
        Assert.assertTrue(editMenu.isDisplayed(), "Menu khong mo");

        tapOutsideToCloseMenu();
        Thread.sleep(1000);

        Assert.assertFalse(editMenu.isDisplayed(), "Menu chua dong");
        ExtentReportManager.getTest().log(Status.PASS, "Menu dong");
    }

    // ========================================================
    // GROUP 2: Test rename popup verify (NON-DESTRUCTIVE)
    // ========================================================

    @Test(priority = 3, description = "SAV_05_01: Popup Rename hien thi")
    public void test_SAV_05_01_rename_popup_displayed()
            throws InterruptedException {
        openRenamePopup();

        Assert.assertTrue(renamePopup.isDisplayed(), "Popup khong hien");
        ExtentReportManager.getTest().log(Status.PASS, "Popup hien thi");

        // Cleanup
        renamePopup.clickCancel();
        Thread.sleep(500);
    }

    @Test(priority = 4, description = "SAV_05_02: Textbox co ten file hien tai")
    public void test_SAV_05_02_textbox_shows_current_filename()
            throws InterruptedException {
        openRenamePopup();

        String text = renamePopup.getCurrentText();
        ExtentReportManager.getTest().log(Status.INFO, "Text: " + text);
        Assert.assertNotNull(text);
        Assert.assertTrue(text.startsWith("record_") || text.contains("audio"),
                "Text khong dung format: " + text);
        ExtentReportManager.getTest().log(Status.PASS, "Text dung");

        // Cleanup
        renamePopup.clickCancel();
        Thread.sleep(500);
    }

    @Test(priority = 5, description = "SAV_05_03: Nhap ten moi")
    public void test_SAV_05_03_enter_new_name()
            throws InterruptedException {
        openRenamePopup();

        renamePopup.clearText();
        renamePopup.enterText("my_test_audio");
        Thread.sleep(300);

        String current = renamePopup.getCurrentText();
        Assert.assertEquals(current, "my_test_audio", "Text khong khop");
        ExtentReportManager.getTest().log(Status.PASS, "Da nhap: " + current);

        // Cleanup: KHONG click Done (de tranh rename) -> Cancel
        renamePopup.clickCancel();
        Thread.sleep(500);
    }

    @Test(priority = 6, description = "SAV_05_04: Clear text bang icon X")
    public void test_SAV_05_04_clear_text() throws InterruptedException {
        openRenamePopup();

        renamePopup.clearText();
        Thread.sleep(300);

        String text = renamePopup.getCurrentText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Text after clear: '" + text + "'");
        Assert.assertTrue(text == null || text.isEmpty(),
                "Text khong rong sau clear: " + text);
        ExtentReportManager.getTest().log(Status.PASS, "Da clear");

        // Cleanup
        renamePopup.clickCancel();
        Thread.sleep(500);
    }

    // ========================================================
    // GROUP 3: Test rename empty / cancel (NON-DESTRUCTIVE)
    // ========================================================

    @Test(priority = 7, description = "SAV_05_07: Rename voi ten trong")
    public void test_SAV_05_07_rename_empty() throws InterruptedException {
        openRenamePopup();

        renamePopup.clearText();
        Thread.sleep(300);
        renamePopup.clickDone();
        Thread.sleep(1000);  // M1: Giam tu 1500

        boolean popupStillOpen = renamePopup.isDisplayed();
        boolean errorShown = renamePopup.isErrorMessageDisplayed();

        ExtentReportManager.getTest().log(Status.INFO,
                "Popup open: " + popupStillOpen + ", Error: " + errorShown);

        Assert.assertTrue(popupStillOpen || errorShown,
                "App accept ten rong (loi)");
        ExtentReportManager.getTest().log(Status.PASS,
                "App khong accept ten rong");

        // Cleanup
        if (popupStillOpen) {
            renamePopup.clickCancel();
            Thread.sleep(500);
        }
    }

    @Test(priority = 8, description = "SAV_05_08: Cancel khi khong nhap")
    public void test_SAV_05_08_cancel_keeps_original_name()
            throws InterruptedException {
        String originalName = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Original: " + originalName);

        openRenamePopup();
        renamePopup.clickCancel();
        Thread.sleep(1000);

        String afterName = audioSavedPage.getFileName();
        Assert.assertEquals(afterName, originalName, "Ten file bi doi");
        ExtentReportManager.getTest().log(Status.PASS, "Ten file giu nguyen");
    }

    @Test(priority = 9, description = "SAV_05_09: Cancel sau khi nhap")
    public void test_SAV_05_09_cancel_after_typing()
            throws InterruptedException {
        String originalName = audioSavedPage.getFileName();

        openRenamePopup();
        renamePopup.clearText();
        renamePopup.enterText("new_name_not_saved");
        Thread.sleep(300);
        renamePopup.clickCancel();
        Thread.sleep(1000);

        String afterName = audioSavedPage.getFileName();
        Assert.assertEquals(afterName, originalName,
                "Ten file bi doi du da Cancel");
        ExtentReportManager.getTest().log(Status.PASS,
                "Ten file giu nguyen: " + afterName);
    }

    // ========================================================
    // GROUP 4: Test rename SUCCESS (DESTRUCTIVE - chay cuoi)
    // ========================================================

    @Test(priority = 10, description = "SAV_05_05: Rename thanh cong")
    public void test_SAV_05_05_rename_success() throws InterruptedException {
        openRenamePopup();

        renamePopup.clearText();
        renamePopup.enterText("test_audio_renamed");
        Thread.sleep(300);
        renamePopup.clickDone();
        Thread.sleep(1200);  // M1: Giam tu 2000 - rename xong nhanh

        String fileName = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "After rename: " + fileName);
        Assert.assertNotNull(fileName);
        Assert.assertTrue(fileName.contains("test_audio_renamed"),
                "Ten file chua doi: " + fileName);
        ExtentReportManager.getTest().log(Status.PASS, "Rename thanh cong");
    }

    @Test(priority = 11, description = "SAV_05_06: Rename voi ky tu dac biet")
    public void test_SAV_05_06_rename_with_special_characters()
            throws InterruptedException {
        openRenamePopup();

        renamePopup.clearText();
        renamePopup.enterText("audio/test:bad");
        Thread.sleep(300);
        renamePopup.clickDone();
        Thread.sleep(1000);

        if (renamePopup.isErrorMessageDisplayed()) {
            String err = renamePopup.getErrorMessage();
            ExtentReportManager.getTest().log(Status.PASS,
                    "App hien loi: " + err);
            // Cleanup
            renamePopup.clickCancel();
            Thread.sleep(500);
        } else {
            String fileName = audioSavedPage.getFileName();
            ExtentReportManager.getTest().log(Status.PASS,
                    "App accept (loc ky tu): " + fileName);
            Assert.assertFalse(fileName.contains("/"),
                    "File chua ky tu /");
            Assert.assertFalse(fileName.contains(":"),
                    "File chua ky tu :");
        }
    }
}