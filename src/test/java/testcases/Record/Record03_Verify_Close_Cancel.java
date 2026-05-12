package testcases.Record;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import Constants.TimeOutConstants;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.RecorderPage;
import Report.ExtentReportManager;
import Utils.GestureUtils;
import Utils.RecordFlowHelper;

public class Record03_Verify_Close_Cancel extends BaseTest {

    private RecorderPage recorderPage;

    private static final By HOME_RECORD_CARD =
            By.id("com.bluesoftware.voicechanger:id/layout_record");

    @BeforeClass(dependsOnMethods = "setUp")
    public void resetBeforeClass() {
        resetAppToFreshState();
    }

    @BeforeMethod
    public void navigateToScreen() {   // ← Doi ten method
        recorderPage = RecordFlowHelper.navigateToRecorder(driver);
    }

    @AfterMethod
    public void resetState() {
        RecordFlowHelper.resetToHome(driver);
    }

    private boolean isAtHome() {
        return driver.findElements(HOME_RECORD_CARD).size() > 0;
    }

    @Test(description = "REC_03_01: Dong Recorder bang nut X (chua ghi)")
    public void test_REC_03_01_close_recorder_without_recording()
            throws InterruptedException {
        recorderPage.clickClose();
        Thread.sleep(2000);

        Assert.assertTrue(isAtHome(), "Khong ve duoc Home");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home");
    }

    @Test(description = "REC_03_02: Dong Recorder bang swipe back")
    public void test_REC_03_02_close_recorder_with_swipe_back()
            throws InterruptedException {
        GestureUtils.swipeFromLeftEdgeToBack(driver);
        Thread.sleep(2000);

        Assert.assertTrue(isAtHome(), "Khong ve duoc Home bang swipe");
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Home qua swipe");
    }

    @Test(description = "REC_03_03: Nhan X khi dang ghi -> hien dialog")
    public void test_REC_03_03_close_during_recording_shows_dialog()
            throws InterruptedException {
        recorderPage.clickRecordButton();
        Thread.sleep(2000);

        recorderPage.clickClose();
        Thread.sleep(1500);

        boolean dialogShown = recorderPage.isConfirmCancelDialogDisplayed();
        ExtentReportManager.getTest().log(Status.INFO,
                "Dialog shown: " + dialogShown);

        // Note: tuy thuoc UI, dialog co the khong hien neu app khong implement
        // -> log warn neu khong co dialog, dung Assert.assertTrue
        if (!dialogShown) {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "App khong hien dialog confirm cancel khi nhan X luc ghi");
        }
        Assert.assertTrue(dialogShown,
                "App khong hien dialog xac nhan khi nhan X luc dang ghi");
    }

    @Test(description = "REC_03_04: Xac nhan huy ghi am")
    public void test_REC_03_04_confirm_cancel_recording()
            throws InterruptedException {
        recorderPage.clickRecordButton();
        Thread.sleep(2000);
        recorderPage.clickClose();
        Thread.sleep(1500);

        if (recorderPage.isConfirmCancelDialogDisplayed()) {
            recorderPage.clickConfirmCancel();
            Thread.sleep(2000);

            Assert.assertTrue(isAtHome(), "Khong ve Home sau khi confirm cancel");
            ExtentReportManager.getTest().log(Status.PASS,
                    "Da huy ghi am va ve Home");
        } else {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Khong co dialog hien thi - skip verify");
        }
    }

    @Test(description = "REC_03_05: Dismiss dialog huy ghi am")
    public void test_REC_03_05_dismiss_cancel_dialog()
            throws InterruptedException {
        recorderPage.clickRecordButton();
        Thread.sleep(2000);
        recorderPage.clickClose();
        Thread.sleep(1500);

        if (recorderPage.isConfirmCancelDialogDisplayed()) {
            recorderPage.clickDismissCancelDialog();
            Thread.sleep(2000);

            // Verify van o man Recorder
            Assert.assertTrue(recorderPage.isDisplayed(),
                    "Khong con o Recorder sau dismiss dialog");
            ExtentReportManager.getTest().log(Status.PASS,
                    "Van o Recorder, ghi am tiep tuc");
        } else {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Khong co dialog - skip verify");
        }
    }
}