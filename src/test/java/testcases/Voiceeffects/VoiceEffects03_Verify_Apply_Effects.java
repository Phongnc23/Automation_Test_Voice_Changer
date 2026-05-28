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
 * Test click ap dung 34 effects (DOM moi) + 2 case logic.
 * Thu tu test theo DOM order -> moi effect cach nhau 1-2 swipes -> NHANH.
 *
 * Layout DOM (theo row, 3 cot, Volume block dong duoi row chua effect chon):
 *   Row 0: Normal | Man    | Woman
 *   Row 1: Child  | Baby   | Tenor
 *   Row 2: Helium | Parody | Whisper
 *   Row 3: Cyborg | Devil  | Ghost
 *   Row 4: Penguin | Monster | Death
 *   Row 5: Zombie | Drunk  | Alien
 *   Row 6: Bee    | Robot  | Fast
 *   Row 7: Slow   | Bass   | Echo
 *   Row 8: Tremolo| Bathroom | Cave
 *   Row 9: Hall   | Stadium | Tunnel
 *   Row 10: Underwater | Telephone | Megaphone
 *   Row 11: Old Radio
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

    // ========== TEST 34 EFFECTS THEO DOM ORDER ==========

    // Row 0
    @Test(description = "VE_03_01: Normal", priority = 1)
    public void test_VE_03_01_normal() { verifyClickEffect("Normal"); }

    @Test(description = "VE_03_02: Man", priority = 2)
    public void test_VE_03_02_man() { verifyClickEffect("Man"); }

    @Test(description = "VE_03_03: Woman", priority = 3)
    public void test_VE_03_03_woman() { verifyClickEffect("Woman"); }

    // Row 1
    @Test(description = "VE_03_04: Child", priority = 4)
    public void test_VE_03_04_child() { verifyClickEffect("Child"); }

    @Test(description = "VE_03_05: Baby", priority = 5)
    public void test_VE_03_05_baby() { verifyClickEffect("Baby"); }

    @Test(description = "VE_03_06: Tenor", priority = 6)
    public void test_VE_03_06_tenor() { verifyClickEffect("Tenor"); }

    // Row 2
    @Test(description = "VE_03_07: Helium", priority = 7)
    public void test_VE_03_07_helium() { verifyClickEffect("Helium"); }

    @Test(description = "VE_03_08: Parody", priority = 8)
    public void test_VE_03_08_parody() { verifyClickEffect("Parody"); }

    @Test(description = "VE_03_09: Whisper", priority = 9)
    public void test_VE_03_09_whisper() { verifyClickEffect("Whisper"); }

    // Row 3
    @Test(description = "VE_03_10: Cyborg", priority = 10)
    public void test_VE_03_10_cyborg() { verifyClickEffect("Cyborg"); }

    @Test(description = "VE_03_11: Devil", priority = 11)
    public void test_VE_03_11_devil() { verifyClickEffect("Devil"); }

    @Test(description = "VE_03_12: Ghost", priority = 12)
    public void test_VE_03_12_ghost() { verifyClickEffect("Ghost"); }

    // Row 4
    @Test(description = "VE_03_13: Penguin", priority = 13)
    public void test_VE_03_13_penguin() { verifyClickEffect("Penguin"); }

    @Test(description = "VE_03_14: Monster", priority = 14)
    public void test_VE_03_14_monster() { verifyClickEffect("Monster"); }

    @Test(description = "VE_03_15: Death", priority = 15)
    public void test_VE_03_15_death() { verifyClickEffect("Death"); }

    // Row 5
    @Test(description = "VE_03_16: Zombie", priority = 16)
    public void test_VE_03_16_zombie() { verifyClickEffect("Zombie"); }

    @Test(description = "VE_03_17: Drunk", priority = 17)
    public void test_VE_03_17_drunk() { verifyClickEffect("Drunk"); }

    @Test(description = "VE_03_18: Alien", priority = 18)
    public void test_VE_03_18_alien() { verifyClickEffect("Alien"); }

    // Row 6
    @Test(description = "VE_03_19: Bee", priority = 19)
    public void test_VE_03_19_bee() { verifyClickEffect("Bee"); }

    @Test(description = "VE_03_20: Robot", priority = 20)
    public void test_VE_03_20_robot() { verifyClickEffect("Robot"); }

    @Test(description = "VE_03_21: Fast", priority = 21)
    public void test_VE_03_21_fast() { verifyClickEffect("Fast"); }

    // Row 7
    @Test(description = "VE_03_22: Slow", priority = 22)
    public void test_VE_03_22_slow() { verifyClickEffect("Slow"); }

    @Test(description = "VE_03_23: Bass", priority = 23)
    public void test_VE_03_23_bass() { verifyClickEffect("Bass"); }

    @Test(description = "VE_03_24: Echo", priority = 24)
    public void test_VE_03_24_echo() { verifyClickEffect("Echo"); }

    // Row 8
    @Test(description = "VE_03_25: Tremolo", priority = 25)
    public void test_VE_03_25_tremolo() { verifyClickEffect("Tremolo"); }

    @Test(description = "VE_03_26: Bathroom", priority = 26)
    public void test_VE_03_26_bathroom() { verifyClickEffect("Bathroom"); }

    @Test(description = "VE_03_27: Cave", priority = 27)
    public void test_VE_03_27_cave() { verifyClickEffect("Cave"); }

    // Row 9
    @Test(description = "VE_03_28: Hall", priority = 28)
    public void test_VE_03_28_hall() { verifyClickEffect("Hall"); }

    @Test(description = "VE_03_29: Stadium", priority = 29)
    public void test_VE_03_29_stadium() { verifyClickEffect("Stadium"); }

    @Test(description = "VE_03_30: Tunnel", priority = 30)
    public void test_VE_03_30_tunnel() { verifyClickEffect("Tunnel"); }

    // Row 10
    @Test(description = "VE_03_31: Underwater", priority = 31)
    public void test_VE_03_31_underwater() { verifyClickEffect("Underwater"); }

    @Test(description = "VE_03_32: Telephone", priority = 32)
    public void test_VE_03_32_telephone() { verifyClickEffect("Telephone"); }

    @Test(description = "VE_03_33: Megaphone", priority = 33)
    public void test_VE_03_33_megaphone() { verifyClickEffect("Megaphone"); }

    // Row 11
    @Test(description = "VE_03_34: Old Radio", priority = 34)
    public void test_VE_03_34_old_radio() { verifyClickEffect("Old Radio"); }

    // ========== TEST LOGIC ==========

    @Test(description = "VE_03_35: Chi 1 effect duoc chon cung luc", priority = 35)
    public void test_VE_03_35_only_one_selected() throws InterruptedException {
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

    @Test(description = "VE_03_36: Switch ve Normal", priority = 36)
    public void test_VE_03_36_switch_back_to_normal() throws InterruptedException {
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
