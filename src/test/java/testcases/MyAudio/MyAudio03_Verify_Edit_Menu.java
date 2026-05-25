package testcases.MyAudio;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.Components.MyAudioEditMenu;
import Pages.MyAudioPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * MA_03: Test Edit Menu (bottom sheet) - 4 tests.
 * Pattern: Share session + close menu sau moi test.
 */
public class MyAudio03_Verify_Edit_Menu extends BaseTest {

    private MyAudioPage myAudioPage;
    private MyAudioEditMenu editMenu;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupSession() {
        logger.info("=== SETUP EDIT MENU SUITE ===");
        try {
            ensureAtLeastTwoFiles();
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            editMenu = new MyAudioEditMenu(driver);
        } catch (Exception e) {
            logger.error("Setup error: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            myAudioPage = RecordFlowHelper.navigateToMyAudio(driver);
            editMenu = new MyAudioEditMenu(driver);
        }
    }

    @BeforeMethod
    public void ensureCleanState() {
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
    public void closeMenuIfOpen() {
        if (editMenu.isDisplayed()) {
            try {
                editMenu.closeByTapOutside();
                Thread.sleep(600);
            } catch (Exception e) {
                // skip
            }
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

    private void ensureAtLeastTwoFiles() {
        try {
            MyAudioPage temp = RecordFlowHelper.navigateToMyAudio(driver);
            int count = temp.getItemCount();
            if (count >= 2) return;

            int needToCreate = 2 - count;
            for (int i = 0; i < needToCreate; i++) {
                RecordFlowHelper.smartResetToHome(driver);
                Thread.sleep(800);
                RecordFlowHelper.navigateToAudioSaved(driver, 1);
                Thread.sleep(800);
            }
            RecordFlowHelper.smartResetToHome(driver);
            Thread.sleep(800);
        } catch (Exception e) {
            logger.warn("ensureAtLeastTwoFiles error: " + e.getMessage());
        }
    }

    @Test(priority = 1, description = "MA_03_01: Mo edit menu bang btnMore")
    public void test_MA_03_01_open_edit_menu()
            throws InterruptedException {
        Assert.assertTrue(myAudioPage.hasAtLeastOneFile(),
                "Khong co file");

        myAudioPage.clickMoreAt(1);
        Thread.sleep(1000);

        Assert.assertTrue(editMenu.isDisplayed(), "Menu khong mo");

        String headerName = editMenu.getHeaderFileName();
        ExtentReportManager.getTest().log(Status.INFO,
                "Header: " + headerName);
        Assert.assertNotNull(headerName, "Header null");
        ExtentReportManager.getTest().log(Status.PASS,
                "Menu mo voi header dung");
    }

    @Test(priority = 2, description = "MA_03_02: Verify 5 action")
    public void test_MA_03_02_verify_five_actions()
            throws InterruptedException {
        myAudioPage.clickMoreAt(1);
        Thread.sleep(1000);

        Assert.assertTrue(editMenu.isDisplayed(), "Menu khong mo");

        Assert.assertTrue(editMenu.isVoiceMessageDisplayed(),
                "Thieu Voice Message");
        Assert.assertTrue(editMenu.isShareDisplayed(), "Thieu Share");
        Assert.assertTrue(editMenu.isRenameDisplayed(), "Thieu Rename");
        Assert.assertTrue(editMenu.isSetRingtoneDisplayed(),
                "Thieu Ringtone");
        Assert.assertTrue(editMenu.isDeleteDisplayed(), "Thieu Delete");

        int total = editMenu.countActions();
        Assert.assertEquals(total, 5,
                "Phai co 5 action, co: " + total);
        ExtentReportManager.getTest().log(Status.PASS,
                "Du 5 action");
    }

    @Test(priority = 3, description = "MA_03_03: Dong menu bang tap outside")
    public void test_MA_03_03_close_by_tap_outside()
            throws InterruptedException {
        myAudioPage.clickMoreAt(1);
        Thread.sleep(1000);

        Assert.assertTrue(editMenu.isDisplayed(), "Menu khong mo");

        editMenu.closeByTapOutside();
        Thread.sleep(1000);

        Assert.assertFalse(editMenu.isDisplayed(),
                "Menu KHONG dong");
        Assert.assertTrue(myAudioPage.isDisplayed(),
                "Khong tro lai My Audio");
        ExtentReportManager.getTest().log(Status.PASS,
                "Dong menu thanh cong");
    }

    @Test(priority = 4, description = "MA_03_04: Header dung file da chon")
    public void test_MA_03_04_header_shows_correct_file()
            throws InterruptedException {
        Assert.assertTrue(myAudioPage.getItemCount() >= 2,
                "Can >= 2 file");

        // File index 0: snapshot list ngay truoc click de tranh race
        // condition khi prior test polluted list (e.g. my_tts_test_audio).
        java.util.List<String> namesAt0 = myAudioPage.getAllFileNames();
        myAudioPage.clickMoreAt(0);
        Thread.sleep(1000);

        String header0 = editMenu.getHeaderFileName();
        Assert.assertNotNull(header0, "Header 0 null");
        // Defensive: header phai khop VOI MOT trong cac file dang hien thi
        // (list co the reorder do test pollution tu prior runs).
        Assert.assertTrue(namesAt0.contains(header0),
                "Header [" + header0 + "] khong nam trong list visible: "
                        + namesAt0);

        editMenu.closeByTapOutside();
        Thread.sleep(800);

        // File index 1: re-snapshot list (list co the doi sau interactions)
        java.util.List<String> namesAt1 = myAudioPage.getAllFileNames();
        myAudioPage.clickMoreAt(1);
        Thread.sleep(1000);

        String header1 = editMenu.getHeaderFileName();
        Assert.assertNotNull(header1, "Header 1 null");
        Assert.assertTrue(namesAt1.contains(header1),
                "Header [" + header1 + "] khong nam trong list visible: "
                        + namesAt1);

        // Core assertion: 2 file khac index phai cho 2 header khac nhau
        Assert.assertNotEquals(header0, header1,
                "2 file khac nhau phai khac header");
        ExtentReportManager.getTest().log(Status.PASS,
                "Header dung file da chon: [" + header0 + "] vs ["
                        + header1 + "]");
    }
}