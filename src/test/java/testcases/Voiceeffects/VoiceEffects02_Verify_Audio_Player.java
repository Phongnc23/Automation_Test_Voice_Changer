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
 * Test class kiem tra audio player tren Voice Effects.
 * Pattern: Reset moi test vi audio player state phuc tap
 * (play/pause/resume/auto-stop can state khac nhau).
 */
public class VoiceEffects02_Verify_Audio_Player extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void resetBeforeClass() {
        resetAppToFreshState();
    }

    @BeforeMethod
    public void navigateToScreen() {
        try {
            // Ghi 5s cho test player co du thoi gian
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 5);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 5);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void resetState() {
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

    @Test(description = "VE_02_01: Play audio")
    public void test_VE_02_01_play_audio() throws InterruptedException {
        String timeBefore = voiceEffectsPage.getTimeText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Time truoc khi play: " + timeBefore);

        voiceEffectsPage.clickPlayPause();
        Thread.sleep(2500);

        String timeAfter = voiceEffectsPage.getTimeText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Time sau khi play 2.5s: " + timeAfter);

        Assert.assertNotEquals(timeBefore, timeAfter,
                "Audio khong phat (time khong thay doi)");
        ExtentReportManager.getTest().log(Status.PASS, "Audio dang phat");
    }

    @Test(description = "VE_02_02: Pause audio")
    public void test_VE_02_02_pause_audio() throws InterruptedException {
        voiceEffectsPage.clickPlayPause(); // play
        Thread.sleep(1500);

        voiceEffectsPage.clickPlayPause(); // pause
        Thread.sleep(500);

        String timeAtPause = voiceEffectsPage.getTimeText();
        Thread.sleep(2000);
        String timeAfter = voiceEffectsPage.getTimeText();

        Assert.assertEquals(timeAtPause, timeAfter,
                "Audio van phat khi da pause");
        ExtentReportManager.getTest().log(Status.PASS, "Audio da pause");
    }

    @Test(description = "VE_02_03: Resume after pause")
    public void test_VE_02_03_resume_after_pause() throws InterruptedException {
        voiceEffectsPage.clickPlayPause(); // play
        Thread.sleep(1500);
        voiceEffectsPage.clickPlayPause(); // pause
        Thread.sleep(1000);

        String timeAtPause = voiceEffectsPage.getTimeText();
        voiceEffectsPage.clickPlayPause(); // resume
        Thread.sleep(2000);

        String timeAfter = voiceEffectsPage.getTimeText();
        Assert.assertNotEquals(timeAtPause, timeAfter,
                "Audio khong resume");
        ExtentReportManager.getTest().log(Status.PASS, "Audio da resume");
    }

    @Test(description = "VE_02_04: Audio auto stop khi het")
    public void test_VE_02_04_audio_auto_stop_at_end() throws InterruptedException {
        voiceEffectsPage.clickPlayPause();
        // Cho audio het (recording 5s + buffer)
        Thread.sleep(7000);

        String time1 = voiceEffectsPage.getTimeText();
        Thread.sleep(2000);
        String time2 = voiceEffectsPage.getTimeText();

        Assert.assertEquals(time1, time2, "Audio van phat sau khi het file");
        ExtentReportManager.getTest().log(Status.PASS,
                "Audio tu dung khi het: " + time1);
    }
}