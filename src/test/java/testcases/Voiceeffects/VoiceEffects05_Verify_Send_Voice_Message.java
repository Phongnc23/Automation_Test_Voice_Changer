package testcases.Voiceeffects;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Pages.VoiceEffectsPage;
import Pages.Components.ShareBottomSheet;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

import java.time.Duration;
import java.util.List;

public class VoiceEffects05_Verify_Send_Voice_Message extends BaseTest {

    private VoiceEffectsPage voiceEffectsPage;
    private ShareBottomSheet shareSheet;

    @BeforeClass(dependsOnMethods = "setUp")
    public void setupVoiceEffectsSession() {
        logger.info("=== SETUP VOICE EFFECTS SESSION ===");
        try {
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 3);
            shareSheet = new ShareBottomSheet(driver);
        } catch (Exception e) {
            logger.error("Loi navigate: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            voiceEffectsPage = RecordFlowHelper.navigateToVoiceEffects(driver, 3);
            shareSheet = new ShareBottomSheet(driver);
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

    private void ensureShareSheetOpen() throws InterruptedException {
        if (!shareSheet.isDisplayed()) {
            voiceEffectsPage.clickSendVoiceMessage();
            // L1: Smart wait thay sleep(3000) co dinh
            try {
                new WebDriverWait(driver, Duration.ofSeconds(4))
                        .ignoring(Exception.class)
                        .until(d -> shareSheet.isDisplayed());
            } catch (Exception e) {
                // se fail o assertion phia duoi neu sheet khong mo
            }
        }
    }

    @Test(description = "VE_06_01: Mo bottom sheet chia se", priority = 1)
    public void test_VE_06_01_open_share_bottom_sheet() throws InterruptedException {
        voiceEffectsPage.clickSendVoiceMessage();
        // M2: Wait share sheet thay sleep(3000) co dinh, max 4s
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .ignoring(Exception.class)
                    .until(d -> shareSheet.isDisplayed());
        } catch (Exception e) {
            // Timeout - se fail o assertion phia duoi
        }

        Assert.assertTrue(shareSheet.isDisplayed(),
                "Bottom sheet khong mo");

        String fileName = shareSheet.getPreviewFileName();
        ExtentReportManager.getTest().log(Status.INFO, "File: " + fileName);
        Assert.assertNotNull(fileName, "Preview filename null");

        ExtentReportManager.getTest().log(Status.PASS,
                "Bottom sheet mo voi file: " + fileName);
    }

    @Test(description = "VE_06_02: Hien thi danh sach app", priority = 2)
    public void test_VE_06_02_share_sheet_lists_apps() throws InterruptedException {
        ensureShareSheetOpen();

        List<String> apps = shareSheet.getAvailableApps();
        ExtentReportManager.getTest().log(Status.INFO,
                "Apps: " + apps.size());

        Assert.assertTrue(apps.size() > 0, "Khong co app nao");
        ExtentReportManager.getTest().log(Status.PASS,
                "Co " + apps.size() + " app");
    }

    @Test(description = "VE_06_03: Click Cancel tren bottom sheet", priority = 3)
    public void test_VE_06_03_cancel_share_bottom_sheet() throws InterruptedException {
        ensureShareSheetOpen();

        shareSheet.clickCancel();
        // L1: Smart wait thay sleep(2000) co dinh
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .ignoring(Exception.class)
                    .until(d -> voiceEffectsPage.isDisplayed());
        } catch (Exception e) {
            // se fail o assertion phia duoi
        }

        Assert.assertTrue(voiceEffectsPage.isDisplayed(),
                "Khong quay lai Voice Effects sau Cancel");
        ExtentReportManager.getTest().log(Status.PASS, "Da quay lai Voice Effects");
    }
}