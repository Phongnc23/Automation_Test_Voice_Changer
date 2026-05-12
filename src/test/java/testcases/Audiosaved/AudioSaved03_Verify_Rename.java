package testcases.Audiosaved;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.Components.EditMenu;
import Pages.Components.RenamePopup;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

public class AudioSaved03_Verify_Rename extends BaseTest {

    private AudioSavedPage audioSavedPage;
    private EditMenu editMenu;
    private RenamePopup renamePopup;

    @BeforeMethod
    public void navigateToScreen() {
        audioSavedPage = RecordFlowHelper.navigateToAudioSaved(driver, 3);
        editMenu = new EditMenu(driver);
        renamePopup = new RenamePopup(driver);
    }

    @AfterMethod
    public void resetState() {
        RecordFlowHelper.resetToHome(driver);
    }

    @Test(description = "SAV_04_01: Mo menu 3 cham")
    public void test_SAV_04_01_three_dot_menu_opens() throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1500);

        Assert.assertTrue(editMenu.isDisplayed(), "Menu khong mo");
        Assert.assertTrue(editMenu.isRenameItemDisplayed());
        Assert.assertTrue(editMenu.isOpenWithItemDisplayed());
        ExtentReportManager.getTest().log(Status.PASS, "Menu mo voi 2 item");
    }

    @Test(description = "SAV_04_02: Tap outside dong menu")
    public void test_SAV_04_02_menu_closes_on_tap_outside()
            throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1500);

        // Tap goc tren cua man hinh
        org.openqa.selenium.Dimension size = driver.manage().window().getSize();
        Utils.GestureUtils.tap(driver, size.width / 2, 100);
        Thread.sleep(1500);

        Assert.assertFalse(editMenu.isDisplayed(), "Menu chua dong");
        ExtentReportManager.getTest().log(Status.PASS, "Menu dong");
    }

    @Test(description = "SAV_05_01: Popup Rename hien thi")
    public void test_SAV_05_01_rename_popup_displayed() throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickRename();
        Thread.sleep(1500);

        Assert.assertTrue(renamePopup.isDisplayed(), "Popup khong hien");
        ExtentReportManager.getTest().log(Status.PASS, "Popup hien thi");
    }

    @Test(description = "SAV_05_02: Textbox co ten file hien tai")
    public void test_SAV_05_02_textbox_shows_current_filename()
            throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickRename();
        Thread.sleep(1500);

        String text = renamePopup.getCurrentText();
        ExtentReportManager.getTest().log(Status.INFO, "Text: " + text);
        Assert.assertNotNull(text);
        Assert.assertTrue(text.startsWith("record_"),
                "Text khong dung format: " + text);
        ExtentReportManager.getTest().log(Status.PASS, "Text dung");
    }

    @Test(description = "SAV_05_03: Nhap ten moi")
    public void test_SAV_05_03_enter_new_name() throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickRename();
        Thread.sleep(1500);

        renamePopup.clearText();
        renamePopup.enterText("my_test_audio");
        Thread.sleep(500);

        String current = renamePopup.getCurrentText();
        Assert.assertEquals(current, "my_test_audio", "Text khong khop");
        ExtentReportManager.getTest().log(Status.PASS, "Da nhap: " + current);
    }

    @Test(description = "SAV_05_04: Clear text bang icon X / clear()")
    public void test_SAV_05_04_clear_text() throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickRename();
        Thread.sleep(1500);

        renamePopup.clearText();
        Thread.sleep(500);

        String text = renamePopup.getCurrentText();
        ExtentReportManager.getTest().log(Status.INFO, "Text after clear: '" + text + "'");
        Assert.assertTrue(text == null || text.isEmpty(),
                "Text khong rong sau clear: " + text);
        ExtentReportManager.getTest().log(Status.PASS, "Da clear");
    }

    @Test(description = "SAV_05_05: Rename thanh cong")
    public void test_SAV_05_05_rename_success() throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickRename();
        Thread.sleep(1500);

        renamePopup.clearText();
        renamePopup.enterText("test_audio_renamed");
        Thread.sleep(500);
        renamePopup.clickDone();
        Thread.sleep(2500);

        String fileName = audioSavedPage.getFileName();
        ExtentReportManager.getTest().log(Status.INFO, "After rename: " + fileName);
        Assert.assertNotNull(fileName);
        Assert.assertTrue(fileName.contains("test_audio_renamed"),
                "Ten file chua doi: " + fileName);
        ExtentReportManager.getTest().log(Status.PASS, "Rename thanh cong");
    }

    @Test(description = "SAV_05_06: Rename voi ky tu dac biet")
    public void test_SAV_05_06_rename_with_special_characters()
            throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickRename();
        Thread.sleep(1500);

        renamePopup.clearText();
        renamePopup.enterText("audio/test:bad");
        Thread.sleep(500);
        renamePopup.clickDone();
        Thread.sleep(2500);

        // Log ket qua: hoac error message, hoac file da rename (loc ky tu)
        if (renamePopup.isErrorMessageDisplayed()) {
            String err = renamePopup.getErrorMessage();
            ExtentReportManager.getTest().log(Status.PASS,
                    "App hien loi: " + err);
        } else {
            String fileName = audioSavedPage.getFileName();
            ExtentReportManager.getTest().log(Status.PASS,
                    "App accept (loc ky tu): " + fileName);
            // Verify khong co ky tu / : trong ten file
            Assert.assertFalse(fileName.contains("/"),
                    "File chua ky tu /");
            Assert.assertFalse(fileName.contains(":"),
                    "File chua ky tu :");
        }
    }

    @Test(description = "SAV_05_07: Rename voi ten trong")
    public void test_SAV_05_07_rename_empty() throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickRename();
        Thread.sleep(1500);

        renamePopup.clearText();
        Thread.sleep(500);
        renamePopup.clickDone();
        Thread.sleep(2000);

        // Phai hien error hoac popup van mo
        boolean popupStillOpen = renamePopup.isDisplayed();
        boolean errorShown = renamePopup.isErrorMessageDisplayed();

        ExtentReportManager.getTest().log(Status.INFO,
                "Popup open: " + popupStillOpen + ", Error: " + errorShown);

        Assert.assertTrue(popupStillOpen || errorShown,
                "App accept ten rong (loi: popup dong va khong co error)");
        ExtentReportManager.getTest().log(Status.PASS,
                "App khong accept ten rong");

        // Cleanup: click Cancel de dong popup
        if (popupStillOpen) {
            renamePopup.clickCancel();
        }
    }

    @Test(description = "SAV_05_08: Cancel khi khong nhap")
    public void test_SAV_05_08_cancel_keeps_original_name()
            throws InterruptedException {
        String originalName = audioSavedPage.getFileName();

        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickRename();
        Thread.sleep(1500);

        renamePopup.clickCancel();
        Thread.sleep(1500);

        String afterName = audioSavedPage.getFileName();
        Assert.assertEquals(afterName, originalName, "Ten file bi doi");
        ExtentReportManager.getTest().log(Status.PASS, "Ten file giu nguyen");
    }

    @Test(description = "SAV_05_09: Cancel sau khi nhap")
    public void test_SAV_05_09_cancel_after_typing() throws InterruptedException {
        String originalName = audioSavedPage.getFileName();

        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickRename();
        Thread.sleep(1500);

        renamePopup.clearText();
        renamePopup.enterText("new_name_not_saved");
        Thread.sleep(500);
        renamePopup.clickCancel();
        Thread.sleep(1500);

        String afterName = audioSavedPage.getFileName();
        Assert.assertEquals(afterName, originalName,
                "Ten file bi doi du da Cancel");
        ExtentReportManager.getTest().log(Status.PASS,
                "Ten file giu nguyen: " + afterName);
    }
}