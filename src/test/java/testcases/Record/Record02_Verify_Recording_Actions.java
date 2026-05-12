package testcases.Record;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Pages.RecorderPage;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * Test class kiem tra recording actions.
 * Pattern: Mo Recorder 1 lan, chay nhieu test verify khac nhau.
 * Tu test gan tu so de TestNG chay theo thu tu.
 *
 * Test order:
 *   1. Navigate vao Recorder + bat dau ghi am (BeforeClass)
 *   2. Test REC_02_01: Timer thay doi (verify ngay sau click mic)
 *   3. Test REC_02_03: Waveform hien thi (van dang ghi)
 *   4. Test REC_02_02: Timer dem (sau 3s)
 *   5. Test REC_02_06: Timer chinh xac (sau 5s)
 *   6. Test REC_02_04: SKIP
 *   7. Test REC_02_05: Long recording (CAN reset truoc - test rieng)
 *   8. Test REC_02_07: Stop -> Voice Effects
 *   9. Test REC_02_08: File name (van o Voice Effects)
 */
public class Record02_Verify_Recording_Actions extends BaseTest {

    private RecorderPage recorderPage;
    private VoiceEffectsPage voiceEffectsPage;
    private long recordingStartTime;
    private String initialTimer;

    @BeforeClass(dependsOnMethods = "setUp")
    public void resetBeforeClass() {
        resetAppToFreshState();
    }

    /**
     * Navigate vao Recorder va bat dau ghi am.
     * Chay 1 lan cho toan class -> tiet kiem thoi gian.
     */
    @BeforeClass(dependsOnMethods = "resetBeforeClass")
    public void setupRecordingSession() throws InterruptedException {
        logger.info("=== SETUP RECORDING SESSION ===");

        try {
            recorderPage = RecordFlowHelper.navigateToRecorder(driver);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            recorderPage = RecordFlowHelper.navigateToRecorder(driver);
        }

        // Luu timer ban dau truoc khi ghi am
        initialTimer = recorderPage.getTimerText();
        logger.info("Initial timer: " + initialTimer);

        // Bat dau ghi am
        recordingStartTime = System.currentTimeMillis();
        recorderPage.clickRecordButton();
        logger.info("Started recording");
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        logger.info("=== CLEANUP AFTER CLASS ===");
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
            try {
                RecordFlowHelper.forceResetToHome(driver);
            } catch (Exception ex) {
                logger.error("Force reset failed: " + ex.getMessage());
            }
        }
    }

    // ====== GROUP 1: TESTS WHILE RECORDING (share recording session) ======

    @Test(description = "REC_02_01: Bat dau ghi am khi nhan microphone", priority = 1)
    public void test_REC_02_01_start_recording() {
        ExtentReportManager.getTest().log(Status.INFO,
                "Verify timer thay doi sau khi click microphone");

        // Verify timer da thay doi tu 00:00:00
        boolean timerChanged = recorderPage.waitForTimerChange(initialTimer, 5);
        Assert.assertTrue(timerChanged, "Timer khong thay doi sau khi bat dau ghi");

        String currentTimer = recorderPage.getTimerText();
        ExtentReportManager.getTest().log(Status.PASS,
                "Da bat dau ghi am, timer: " + currentTimer);
    }

    @Test(description = "REC_02_03: Waveform xuat hien khi ghi am", priority = 2)
    public void test_REC_02_03_waveform_appears() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify waveform hien thi");

        Assert.assertTrue(recorderPage.isWaveformDisplayed(),
                "Waveform khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Waveform hien thi");
    }

    @Test(description = "REC_02_02: Timer bat dau dem khi ghi am", priority = 3)
    public void test_REC_02_02_timer_starts_counting() throws InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO, "Verify timer dem sau 3s");

        // Cho them de timer co the dem den >= 3s
        long elapsed = (System.currentTimeMillis() - recordingStartTime) / 1000;
        if (elapsed < 3) {
            Thread.sleep((3 - elapsed) * 1000);
        }

        String timer = recorderPage.getTimerText();
        ExtentReportManager.getTest().log(Status.INFO, "Timer: " + timer);

        Assert.assertNotEquals(timer, "00:00:00", "Timer chua dem");
        Assert.assertNotNull(timer, "Timer null");

        ExtentReportManager.getTest().log(Status.PASS, "Timer dem dung");
    }

    @Test(description = "REC_02_06: Timer dem chinh xac thoi gian", priority = 4)
    public void test_REC_02_06_timer_accuracy() throws InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO,
                "Verify timer chinh xac so voi thoi gian thuc");

        // Cho them de timer dem den >= 5s
        long elapsed = (System.currentTimeMillis() - recordingStartTime) / 1000;
        if (elapsed < 5) {
            Thread.sleep((5 - elapsed) * 1000);
            elapsed = (System.currentTimeMillis() - recordingStartTime) / 1000;
        }

        String timer = recorderPage.getTimerText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Elapsed: " + elapsed + "s, Timer: " + timer);

        String[] parts = timer.split(":");
        int timerSec = Integer.parseInt(parts[2]);

        // Sai so cho phep 2s
        Assert.assertTrue(Math.abs(timerSec - elapsed) <= 2,
                "Timer khong chinh xac. Expected ~" + elapsed +
                        "s, actual: " + timerSec + "s");

        ExtentReportManager.getTest().log(Status.PASS,
                "Timer chinh xac (" + timerSec + "s ~ " + elapsed + "s)");
    }

    // ====== GROUP 2: SKIP TEST ======

    @Test(description = "REC_02_04: Waveform phan ung am thanh - SKIP", priority = 5)
    public void test_REC_02_04_waveform_responds_to_sound() {
        throw new SkipException("Khong the test waveform animation tu dong");
    }

    // ====== GROUP 3: LONG RECORDING (RESET TRUOC vi recording session da dang chay) ======

    @Test(description = "REC_02_05: Ghi am 1 phut khong crash", priority = 6)
    public void test_REC_02_05_long_recording_1_minute() throws InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO,
                "Reset session, bat dau ghi am 65 giay");

        // Reset session truoc test nay vi can do thoi gian rieng
        RecordFlowHelper.smartResetToHome(driver);
        recorderPage = RecordFlowHelper.navigateToRecorder(driver);

        recorderPage.clickRecordButton();
        ExtentReportManager.getTest().log(Status.INFO, "Ghi am 65 giay...");
        Thread.sleep(65000);

        String timer = recorderPage.getTimerText();
        ExtentReportManager.getTest().log(Status.INFO, "Timer: " + timer);

        Assert.assertTrue(timer != null && timer.startsWith("00:01:"),
                "Timer khong dung dang 00:01:xx, actual: " + timer);
        Assert.assertTrue(recorderPage.isDisplayed(),
                "App da crash sau khi ghi 1 phut");

        ExtentReportManager.getTest().log(Status.PASS,
                "Ghi am 1 phut thanh cong, timer: " + timer);
    }

    // ====== GROUP 4: AFTER STOP (share Voice Effects session) ======

    @Test(description = "REC_02_07: Stop ghi am chuyen sang Voice Effects", priority = 7)
    public void test_REC_02_07_stop_recording_navigates_to_voice_effects()
            throws InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO,
                "Reset, ghi am, Stop -> verify Voice Effects");

        // Reset va setup recording moi
        RecordFlowHelper.smartResetToHome(driver);
        recorderPage = RecordFlowHelper.navigateToRecorder(driver);

        recorderPage.clickRecordButton();
        Thread.sleep(2500);

        // Stop ghi am
        recorderPage.clickRecordButton();
        Thread.sleep(2500);

        // Verify da o Voice Effects
        voiceEffectsPage = new VoiceEffectsPage(driver);
        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong chuyen sang man Voice Effects");

        ExtentReportManager.getTest().log(Status.PASS,
                "Da chuyen sang Voice Effects");
    }

    @Test(description = "REC_02_08: Ten file dung format record_xxx.m4a", priority = 8)
    public void test_REC_02_08_recorded_file_has_correct_format() {
        ExtentReportManager.getTest().log(Status.INFO,
                "Verify file name format - tan dung Voice Effects session");

        // Tai su dung voiceEffectsPage tu test 02_07
        // Neu null (test 02_07 fail) thi tao moi
        if (voiceEffectsPage == null) {
            voiceEffectsPage = new VoiceEffectsPage(driver);
        }

        String fileName = voiceEffectsPage.getAudioFileName();
        ExtentReportManager.getTest().log(Status.INFO, "File name: " + fileName);

        Assert.assertNotNull(fileName, "Ten file null");
        Assert.assertTrue(fileName.matches("record_\\d+\\.m4a"),
                "Ten file khong dung format: " + fileName);

        ExtentReportManager.getTest().log(Status.PASS, "Format ten file dung");
    }
}