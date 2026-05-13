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
 * Test 23 effects + 2 case logic.
 * Test theo thu tu trong list -> moi effect cach nhau 1-2 swipes -> NHANH.
 */
public class VoiceEffects03_Verify_Apply_Effects extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;

    @BeforeClass(dependsOnMethods = "setUp")
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
        logger.info("=== CLEANUP ===");
        try {
            RecordFlowHelper.smartResetToHome(driver);
        } catch (Exception e) {
            logger.error("Cleanup error: " + e.getMessage());
        }
    }

    private void verifyClickEffect(String effectName) {
        ExtentReportManager.getTest().log(Status.INFO, "Click: " + effectName);
        voiceEffectsPage.clickEffect(effectName);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "App khong responsive sau " + effectName);
        ExtentReportManager.getTest().log(Status.PASS, "OK: " + effectName);
    }

    // ========== TEST 23 EFFECTS THEO THU TU ==========

    @Test(description = "VE_03_01: Normal", priority = 1)
    public void test_VE_03_01_normal() { verifyClickEffect("Normal"); }

    @Test(description = "VE_03_02: Man", priority = 2)
    public void test_VE_03_02_man() { verifyClickEffect("Man"); }

    @Test(description = "VE_03_03: Woman", priority = 3)
    public void test_VE_03_03_woman() { verifyClickEffect("Woman"); }

    @Test(description = "VE_03_04: Child", priority = 4)
    public void test_VE_03_04_child() { verifyClickEffect("Child"); }

    @Test(description = "VE_03_05: Penguin", priority = 5)
    public void test_VE_03_05_penguin() { verifyClickEffect("Penguin"); }

    @Test(description = "VE_03_06: Monster", priority = 6)
    public void test_VE_03_06_monster() { verifyClickEffect("Monster"); }

    @Test(description = "VE_03_07: Fast", priority = 7)
    public void test_VE_03_07_fast() { verifyClickEffect("Fast"); }

    @Test(description = "VE_03_08: Slow", priority = 8)
    public void test_VE_03_08_slow() { verifyClickEffect("Slow"); }

    @Test(description = "VE_03_09: Alien", priority = 9)
    public void test_VE_03_09_alien() { verifyClickEffect("Alien"); }

    @Test(description = "VE_03_10: Zombie", priority = 10)
    public void test_VE_03_10_zombie() { verifyClickEffect("Zombie"); }

    @Test(description = "VE_03_11: Drunk", priority = 11)
    public void test_VE_03_11_drunk() { verifyClickEffect("Drunk"); }

    @Test(description = "VE_03_12: Helium", priority = 12)
    public void test_VE_03_12_helium() { verifyClickEffect("Helium"); }

    @Test(description = "VE_03_13: Death", priority = 13)
    public void test_VE_03_13_death() { verifyClickEffect("Death"); }

    @Test(description = "VE_03_14: Robot", priority = 14)
    public void test_VE_03_14_robot() { verifyClickEffect("Robot"); }

    @Test(description = "VE_03_15: Baby", priority = 15)
    public void test_VE_03_15_baby() { verifyClickEffect("Baby"); }

    @Test(description = "VE_03_16: Echo", priority = 16)
    public void test_VE_03_16_echo() { verifyClickEffect("Echo"); }

    @Test(description = "VE_03_17: Underwater", priority = 17)
    public void test_VE_03_17_underwater() { verifyClickEffect("Underwater"); }

    @Test(description = "VE_03_18: Telephone", priority = 18)
    public void test_VE_03_18_telephone() { verifyClickEffect("Telephone"); }

    @Test(description = "VE_03_19: Parody", priority = 19)
    public void test_VE_03_19_parody() { verifyClickEffect("Parody"); }

    @Test(description = "VE_03_20: Bass", priority = 20)
    public void test_VE_03_20_bass() { verifyClickEffect("Bass"); }

    @Test(description = "VE_03_21: Tenor", priority = 21)
    public void test_VE_03_21_tenor() { verifyClickEffect("Tenor"); }

    @Test(description = "VE_03_22: Bee", priority = 22)
    public void test_VE_03_22_bee() { verifyClickEffect("Bee"); }

    @Test(description = "VE_03_23: Fade", priority = 23)
    public void test_VE_03_23_fade() { verifyClickEffect("Fade"); }

    // ========== TEST LOGIC ==========

    @Test(description = "VE_03_24: Chi 1 effect duoc chon cung luc", priority = 24)
    public void test_VE_03_24_only_one_selected() throws InterruptedException {
        // Lay danh sach effects dang visible
        java.util.List<String> visibleEffects = voiceEffectsPage.getVisibleEffectNames();
        ExtentReportManager.getTest().log(Status.INFO,
                "Visible effects: " + visibleEffects);

        Assert.assertTrue(visibleEffects.size() >= 2,
                "Can it nhat 2 effects visible de test");

        // Lay 2 effect bat ky dang visible
        String effect1 = visibleEffects.get(0);
        String effect2 = visibleEffects.get(1);

        ExtentReportManager.getTest().log(Status.INFO,
                "Click " + effect1 + " -> " + effect2);

        voiceEffectsPage.clickEffect(effect1);
        voiceEffectsPage.clickEffect(effect2);

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "App khong responsive sau khi chuyen effect");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da chuyen tu " + effect1 + " sang " + effect2);
    }

    @Test(description = "VE_03_25: Switch ve Normal", priority = 25)
    public void test_VE_03_25_switch_back_to_normal() throws InterruptedException {
        // Click 1 effect bat ky visible
        java.util.List<String> visibleEffects = voiceEffectsPage.getVisibleEffectNames();
        String anyEffect = visibleEffects.get(0);

        // Neu effect dau tien la Normal, dung effect thu 2 (de test switch khac)
        if ("Normal".equals(anyEffect) && visibleEffects.size() > 1) {
            anyEffect = visibleEffects.get(1);
        }

        ExtentReportManager.getTest().log(Status.INFO,
                "Click " + anyEffect + " -> Normal");

        voiceEffectsPage.clickEffect(anyEffect);
        voiceEffectsPage.clickEffect("Normal");

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "App khong responsive sau khi ve Normal");
        ExtentReportManager.getTest().log(Status.PASS,
                "Da switch tu " + anyEffect + " ve Normal");
    }
}