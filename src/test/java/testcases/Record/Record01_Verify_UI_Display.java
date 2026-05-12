package testcases.Record;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.RecorderPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

public class Record01_Verify_UI_Display extends BaseTest {

    private RecorderPage recorderPage;

    /**
     * Reset app ve fresh state khi bat dau class.
     * Can thiet vi class truoc co the de lai state lan
     * (vd: dang o Voice Effects sau Record02).
     */
    @BeforeClass(dependsOnMethods = "setUp")
    public void resetBeforeClass() {
        resetAppToFreshState();
    }

    @BeforeMethod
    public void navigateToScreen() {
        recorderPage = RecordFlowHelper.navigateToRecorder(driver);
    }

    @Test(description = "REC_01_01: Verify nut X (close) hien thi")
    public void test_REC_01_01_verify_close_button_displayed() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify Close button");
        Assert.assertTrue(recorderPage.isCloseButtonDisplayed(),
                "Nut X khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut X hien thi");
    }

    @Test(description = "REC_01_02: Verify tieu de 'Recorder'")
    public void test_REC_01_02_verify_recorder_title_displayed() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify title");
        Assert.assertTrue(recorderPage.isTitleDisplayed(),
                "Title 'Recorder' khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Title hien thi dung");
    }

    @Test(description = "REC_01_03: Verify dong ho ban dau 00:00:00")
    public void test_REC_01_03_verify_initial_timer_value() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify initial timer");
        Assert.assertTrue(recorderPage.isTimerDisplayed(), "Dong ho khong hien thi");

        String timer = recorderPage.getTimerText();
        ExtentReportManager.getTest().log(Status.INFO, "Timer text: " + timer);
        Assert.assertEquals(timer, "00:00:00", "Timer ban dau khong dung");
        ExtentReportManager.getTest().log(Status.PASS, "Timer = 00:00:00");
    }

    @Test(description = "REC_01_04: Verify text 'Ready to record'")
    public void test_REC_01_04_verify_ready_to_record_text() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify 'Ready to record' text");
        Assert.assertTrue(recorderPage.isReadyToRecordDisplayed(),
                "Text 'Ready to record' khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Text hien thi");
    }

    @Test(description = "REC_01_05: Verify nut microphone")
    public void test_REC_01_05_verify_microphone_button_displayed() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify microphone button");
        Assert.assertTrue(recorderPage.isMicrophoneButtonDisplayed(),
                "Nut microphone khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut microphone hien thi");
    }
}