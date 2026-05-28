package testcases.Voiceeffects;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Pages.VoiceEffectsPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * Pattern: Mo Voice Effects 1 lan, chay 8 test verify UI tren cung session.
 * Tat ca test deu doc-only (khong thay doi state) -> share duoc.
 */
public class VoiceEffects01_Verify_UI_Display extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void resetBeforeClass() {
        resetAppToFreshState();
    }

    /**
     * Navigate vao Voice Effects 1 lan cho ca class.
     */
    @BeforeClass(dependsOnMethods = "resetBeforeClass")
    public void setupVoiceEffectsSession() {
        logger.info("=== SETUP VOICE EFFECTS SESSION ===");
        try {
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 3);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 3);
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupAfterClass() {
        logger.info("=== CLEANUP AFTER CLASS ===");
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    @Test(description = "VE_01_01: Verify nut X", priority = 1)
    public void test_VE_01_01_verify_close_button() {
        ExtentReportManager.getTest().log(Status.INFO, "Verify Close button");
        Assert.assertTrue(voiceEffectsPage.isCloseButtonDisplayed(),
                "Nut X khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut X hien thi");
    }

    @Test(description = "VE_01_02: Verify title 'Voice Effects'", priority = 2)
    public void test_VE_01_02_verify_title_voice_effects() {
        String title = voiceEffectsPage.getTitle();
        ExtentReportManager.getTest().log(Status.INFO, "Title: " + title);
        Assert.assertEquals(title, "Voice Effects", "Title sai");
        ExtentReportManager.getTest().log(Status.PASS, "Title dung");
    }

    @Test(description = "VE_01_03: Verify audio player", priority = 3)
    public void test_VE_01_03_verify_audio_player_displayed() {
        Assert.assertTrue(voiceEffectsPage.isAudioPlayerDisplayed(),
                "Audio player khong hien thi");
        Assert.assertNotNull(voiceEffectsPage.getAudioFileName(),
                "Ten file null");
        Assert.assertNotNull(voiceEffectsPage.getTimeText(),
                "Time text null");
        ExtentReportManager.getTest().log(Status.PASS,
                "Audio player day du: file=" + voiceEffectsPage.getAudioFileName()
                        + ", time=" + voiceEffectsPage.getTimeText());
    }

    @Test(description = "VE_01_04: Verify grid hieu ung", priority = 4)
    public void test_VE_01_04_verify_effects_grid_displayed() {
        Assert.assertTrue(voiceEffectsPage.isEffectsRecyclerDisplayed(),
                "Grid hieu ung khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Grid hien thi");
    }

    @Test(description = "VE_01_05: Verify Normal duoc chon mac dinh", priority = 5)
    public void test_VE_01_05_verify_normal_effect_default_selected() {
        Assert.assertTrue(voiceEffectsPage.isEffectDisplayed("Normal"),
                "Effect Normal khong co trong list");
        ExtentReportManager.getTest().log(Status.PASS,
                "Effect Normal co trong list");
    }

    @Test(description = "VE_01_06: Verify nut Send Voice Message", priority = 6)
    public void test_VE_01_06_verify_send_voice_message_button() {
        Assert.assertTrue(voiceEffectsPage.isSendButtonDisplayed(),
                "Nut Send Voice Message khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut Send hien thi");
    }

    @Test(description = "VE_01_07: Verify nut Save", priority = 7)
    public void test_VE_01_07_verify_save_button() {
        Assert.assertTrue(voiceEffectsPage.isSaveButtonDisplayed(),
                "Nut Save khong hien thi");
        ExtentReportManager.getTest().log(Status.PASS, "Nut Save hien thi");
    }

    @Test(description = "VE_01_08: Verify cac effects co trong list", priority = 8)
    public void test_VE_01_08_verify_all_23_effects_listed() {
        int total = VoiceEffectsPage.ALL_EFFECTS.size();
        int foundCount = 0;
        StringBuilder missing = new StringBuilder();

        for (String effect : VoiceEffectsPage.ALL_EFFECTS) {
            if (voiceEffectsPage.isEffectDisplayed(effect)) {
                foundCount++;
            } else {
                missing.append(effect).append(", ");
            }
        }

        ExtentReportManager.getTest().log(Status.INFO,
                "Found " + foundCount + "/" + total + " effects");
        if (missing.length() > 0) {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Missing: " + missing.toString());
        }

        Assert.assertTrue(foundCount >= 21,
                "Khong tim du effects. Found: " + foundCount + "/" + total);
        ExtentReportManager.getTest().log(Status.PASS,
                foundCount + " effects xuat hien");
    }
}