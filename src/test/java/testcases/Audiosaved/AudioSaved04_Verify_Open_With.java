package testcases.Audiosaved;

import Base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.AudioSavedPage;
import Pages.Components.EditMenu;
import Pages.Components.OpenWithBottomSheet;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

public class AudioSaved04_Verify_Open_With extends BaseTest {

    private AudioSavedPage audioSavedPage;
    private EditMenu editMenu;
    private OpenWithBottomSheet openWithSheet;

    @BeforeMethod
    public void navigateToScreen() {
        audioSavedPage = RecordFlowHelper.navigateToAudioSaved(driver, 3);
        editMenu = new EditMenu(driver);
        openWithSheet = new OpenWithBottomSheet(driver);
    }

    @AfterMethod
    public void resetState() {
        RecordFlowHelper.resetToHome(driver);
    }

    @Test(description = "SAV_06_01: Mo Open With bottom sheet")
    public void test_SAV_06_01_open_with_bottom_sheet_displayed()
            throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickOpenWith();
        Thread.sleep(3000);

        Assert.assertTrue(openWithSheet.isDisplayed(),
                "Open With bottom sheet khong mo");
        ExtentReportManager.getTest().log(Status.PASS, "Bottom sheet mo");
    }

    @Test(description = "SAV_06_02: Cancel Open With")
    public void test_SAV_06_02_cancel_open_with() throws InterruptedException {
        audioSavedPage.clickThreeDotMenu();
        Thread.sleep(1000);
        editMenu.clickOpenWith();
        Thread.sleep(3000);

        openWithSheet.clickCancel();
        Thread.sleep(2000);

        Assert.assertTrue(audioSavedPage.isDisplayed(),
                "Khong quay lai Audio Saved");
        ExtentReportManager.getTest().log(Status.PASS, "Da quay lai");
    }
}