package testcases.Voiceeffects;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * Test audio player tren Voice Effects.
 * Audio 8s - du de test pause/resume an toan.
 */
public class VoiceEffects02_Verify_Audio_Player extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;
    private static final int RECORDING_SECONDS = 8;

//    @BeforeClass(dependsOnMethods = "setUp")
//    public void resetBeforeClass() {
//        resetAppToFreshState();
//    }

    @BeforeMethod
    public void navigateToScreen() {
        try {
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(
                    driver, RECORDING_SECONDS);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(
                    driver, RECORDING_SECONDS);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void resetState() {
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            try {
                RecordFlowHelper.forceResetToHome(driver);
            } catch (Exception ex) {
                logger.error("Force reset failed: " + ex.getMessage());
            }
        }
    }

    /**
     * VE_02_01: Verify audio phat sau click play.
     */
    @Test(description = "VE_02_01: Play audio")
    public void test_VE_02_01_play_audio() throws InterruptedException {
        String timeBefore = voiceEffectsPage.getTimeText();

        voiceEffectsPage.clickPlayPause();
        Thread.sleep(2000);  // Cho audio phat 2s

        String timeAfter = voiceEffectsPage.getTimeText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Before: " + timeBefore + ", After 2s: " + timeAfter);

        Assert.assertNotEquals(timeBefore, timeAfter,
                "Audio khong phat (time khong tang)");
        ExtentReportManager.getTest().log(Status.PASS, "Audio dang phat");
    }

    /**
     * VE_02_02: Verify pause dung audio.
     * Timing chi tiet:
     *   0.0s - Click play
     *   0.3s - Return tu clickPlayPause
     *   0.3s -> 2.3s - Sleep 2s (audio phat den ~2s)
     *   2.3s - Click pause
     *   2.6s - Return, lay timeAtPause
     *   2.6s -> 4.6s - Sleep 2s (verify pause)
     *   4.6s - Lay timeAfter
     * Audio 8s -> con xa cuoi.
     */
    @Test(description = "VE_02_02: Pause audio")
    public void test_VE_02_02_pause_audio() throws InterruptedException {
        String timeBeforePlay = voiceEffectsPage.getTimeText();

        // Buoc 1: Click play
        voiceEffectsPage.clickPlayPause();

        // Buoc 2: Cho audio phat 2s
        Thread.sleep(2000);
        String timeAfterPlay = voiceEffectsPage.getTimeText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Time sau play 2s: " + timeAfterPlay);

        // Verify audio thuc su phat
        Assert.assertNotEquals(timeAfterPlay, timeBeforePlay,
                "Audio chua phat truoc khi pause");

        // Buoc 3: Click pause
        voiceEffectsPage.clickPlayPause();

        // Buoc 4: Lay time NGAY sau click pause
        String timeAtPause = voiceEffectsPage.getTimeText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Time NGAY sau pause: " + timeAtPause);

        // Buoc 5: Cho 2s -> audio phai dung
        Thread.sleep(2000);
        String timeAfter = voiceEffectsPage.getTimeText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Time sau pause 2s: " + timeAfter);

        Assert.assertEquals(timeAfter, timeAtPause,
                "Audio van phat khi da pause. " +
                        "AtPause: " + timeAtPause + ", After: " + timeAfter);
        ExtentReportManager.getTest().log(Status.PASS,
                "Audio da pause: " + timeAtPause);
    }

    /**
     * VE_02_03: Verify resume audio sau pause.
     */
    @Test(description = "VE_02_03: Resume after pause")
    public void test_VE_02_03_resume_after_pause() throws InterruptedException {
        // Play
        voiceEffectsPage.clickPlayPause();
        Thread.sleep(2000);

        // Pause
        voiceEffectsPage.clickPlayPause();
        String timeAtPause = voiceEffectsPage.getTimeText();

        // Cho 1.5s verify pause on dinh
        Thread.sleep(1500);
        String timeStillPaused = voiceEffectsPage.getTimeText();

        // Resume
        voiceEffectsPage.clickPlayPause();
        Thread.sleep(2000);

        String timeAfterResume = voiceEffectsPage.getTimeText();
        ExtentReportManager.getTest().log(Status.INFO,
                "AtPause: " + timeAtPause +
                        ", StillPaused: " + timeStillPaused +
                        ", AfterResume: " + timeAfterResume);

        Assert.assertNotEquals(timeAfterResume, timeStillPaused,
                "Audio khong resume");
        ExtentReportManager.getTest().log(Status.PASS,
                "Audio resume: " + timeStillPaused + " -> " + timeAfterResume);
    }

    /**
     * VE_02_04: Audio tu dung khi het file (8s + buffer 2s = 10s).
     */
    @Test(description = "VE_02_04: Audio auto stop khi het")
    public void test_VE_02_04_audio_auto_stop_at_end() throws InterruptedException {
        voiceEffectsPage.clickPlayPause();

        // Cho audio het (8s + 2s buffer)
        Thread.sleep(10000);
        String time1 = voiceEffectsPage.getTimeText();

        // Cho them 1.5s, time khong duoc tang
        Thread.sleep(1500);
        String time2 = voiceEffectsPage.getTimeText();

        ExtentReportManager.getTest().log(Status.INFO,
                "Time1: " + time1 + ", Time2: " + time2);

        Assert.assertEquals(time1, time2,
                "Audio van phat sau khi het file");
        ExtentReportManager.getTest().log(Status.PASS,
                "Audio tu dung: " + time1);
    }
}