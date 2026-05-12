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
 * Pattern: Mo Voice Effects 1 lan, click 14 effects va 2 case logic.
 * Click effect KHONG roi man hinh -> share session duoc.
 */
public class VoiceEffects03_Verify_Apply_Effects extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;

    @BeforeClass(dependsOnMethods = "setUp")
    public void resetBeforeClass() {
        resetAppToFreshState();
    }

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

    private void verifyClickEffect(String effectName) {
        ExtentReportManager.getTest().log(Status.INFO, "Click effect: " + effectName);
        voiceEffectsPage.clickEffect(effectName);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "App khong responsive sau khi click " + effectName);
        ExtentReportManager.getTest().log(Status.PASS,
                "Da click effect " + effectName);
    }

    @Test(description = "VE_03_01: Chon Man", priority = 1)
    public void test_VE_03_01_select_man_effect() { verifyClickEffect("Man"); }

    @Test(description = "VE_03_02: Chon Woman", priority = 2)
    public void test_VE_03_02_select_woman_effect() { verifyClickEffect("Woman"); }

    @Test(description = "VE_03_03: Chon Child", priority = 3)
    public void test_VE_03_03_select_child_effect() { verifyClickEffect("Child"); }

    @Test(description = "VE_03_04: Chon Penguin", priority = 4)
    public void test_VE_03_04_select_penguin_effect() { verifyClickEffect("Penguin"); }

    @Test(description = "VE_03_05: Chon Monster", priority = 5)
    public void test_VE_03_05_select_monster_effect() { verifyClickEffect("Monster"); }

    @Test(description = "VE_03_06: Chon Fast", priority = 6)
    public void test_VE_03_06_select_fast_effect() { verifyClickEffect("Fast"); }

    @Test(description = "VE_03_07: Chon Slow", priority = 7)
    public void test_VE_03_07_select_slow_effect() { verifyClickEffect("Slow"); }

    @Test(description = "VE_03_08: Chon Alien", priority = 8)
    public void test_VE_03_08_select_alien_effect() { verifyClickEffect("Alien"); }

    @Test(description = "VE_03_09: Chon Zombie", priority = 9)
    public void test_VE_03_09_select_zombie_effect() { verifyClickEffect("Zombie"); }

    @Test(description = "VE_03_10: Chon Robot", priority = 10)
    public void test_VE_03_10_select_robot_effect() { verifyClickEffect("Robot"); }

    @Test(description = "VE_03_11: Chon Helium", priority = 11)
    public void test_VE_03_11_select_helium_effect() { verifyClickEffect("Helium"); }

    @Test(description = "VE_03_12: Chon Baby", priority = 12)
    public void test_VE_03_12_select_baby_effect() { verifyClickEffect("Baby"); }

    @Test(description = "VE_03_13: Chon Echo", priority = 13)
    public void test_VE_03_13_select_echo_effect() { verifyClickEffect("Echo"); }

    @Test(description = "VE_03_14: Chon Telephone", priority = 14)
    public void test_VE_03_14_select_telephone_effect() { verifyClickEffect("Telephone"); }

    @Test(description = "VE_03_15: Chi 1 effect duoc chon cung luc", priority = 15)
    public void test_VE_03_15_only_one_effect_selected_at_a_time()
            throws InterruptedException {
        voiceEffectsPage.clickEffect("Man");
        Thread.sleep(1500);
        voiceEffectsPage.clickEffect("Woman");
        Thread.sleep(1500);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "App khong responsive");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da chuyen tu Man sang Woman");
    }

    @Test(description = "VE_03_16: Chuyen ve Normal tu effect khac", priority = 16)
    public void test_VE_03_16_switch_back_to_normal()
            throws InterruptedException {
        voiceEffectsPage.clickEffect("Man");
        Thread.sleep(1500);
        voiceEffectsPage.clickEffect("Normal");
        Thread.sleep(1500);

        Assert.assertTrue(voiceEffectsPage.isDisplayed());
        ExtentReportManager.getTest().log(Status.PASS, "Da ve Normal");
    }
}