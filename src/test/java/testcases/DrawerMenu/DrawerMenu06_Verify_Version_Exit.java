package testcases.DrawerMenu;

import Base.BaseSharedSessionTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.Components.ExitDialog;
import Pages.DrawerMenuPage;
import Report.ExtentReportManager;
import Utils.RecordFlowHelper;

/**
 * DM_06: Verify Version va Exit App (5 tests).
 *
 * Note: Exit -> dialog confirm "Exit app?" voi Cancel/Exit buttons.
 * Test 5 destructive (priority 10) - dong app, chay cuoi cung.
 */
public class DrawerMenu06_Verify_Version_Exit extends BaseSharedSessionTest {

    private DrawerMenuPage drawerMenu;
    private ExitDialog exitDialog;

    @Override
    protected void navigateToScreen() {
        RecordFlowHelper.smartResetToHome(driver);
        drawerMenu = RecordFlowHelper.openDrawer(driver);
        exitDialog = new ExitDialog(driver);
    }

    @Override
    protected boolean isAtExpectedScreen() {
        return RecordFlowHelper.isDrawerOpen(driver);
    }

    @Test(priority = 1, description = "DM_06_01: Verify Version hien thi dung format")
    public void test_DM_06_01_verify_version_text() {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        String versionText = drawerMenu.getVersionText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Version text: " + versionText);

        Assert.assertNotNull(versionText, "Version text null");
        Assert.assertTrue(versionText.toLowerCase().contains("version"),
                "Khong chua 'version': " + versionText);
        Assert.assertTrue(versionText.matches(".*\\d+(\\.\\d+)+.*"),
                "Khong co so version: " + versionText);

        ExtentReportManager.getTest().log(Status.PASS,
                "Version dung format: " + versionText);
    }

    @Test(priority = 2, description = "DM_06_02: Click Version - khong crash")
    public void test_DM_06_02_click_version_no_crash()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        drawerMenu.clickVersion();
        Thread.sleep(1500);

        String pkgAfter = drawerMenu.getCurrentPackage();
        Assert.assertEquals(pkgAfter, "com.bluesoftware.voicechanger",
                "App roi khoi Voice Changer sau click Version");

        ExtentReportManager.getTest().log(Status.PASS,
                "Click Version khong crash");
    }

    @Test(priority = 3, description = "DM_06_03: Click Exit -> hien dialog confirm")
    public void test_DM_06_03_exit_shows_dialog()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        drawerMenu.clickExit();
        Thread.sleep(1500);

        // Verify dialog mo
        Assert.assertTrue(exitDialog.isDisplayed(),
                "Exit dialog khong mo");

        // Verify title, message
        String title = exitDialog.getTitleText();
        String message = exitDialog.getMessageText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Title: " + title + " | Message: " + message);

        Assert.assertNotNull(title, "Title null");
        Assert.assertTrue(title.toLowerCase().contains("exit"),
                "Title sai: " + title);

        Assert.assertNotNull(message, "Message null");
        Assert.assertTrue(message.toLowerCase().contains("exit"),
                "Message sai: " + message);

        // Verify buttons
        String cancelText = exitDialog.getCancelButtonText();
        String exitText = exitDialog.getExitButtonText();
        ExtentReportManager.getTest().log(Status.INFO,
                "Buttons: " + cancelText + " / " + exitText);

        Assert.assertNotNull(cancelText, "Cancel button null");
        Assert.assertNotNull(exitText, "Exit button null");

        ExtentReportManager.getTest().log(Status.PASS,
                "Exit dialog hien thi day du");
    }

    @Test(priority = 4, description = "DM_06_04: Click Cancel - giu app o foreground")
    public void test_DM_06_04_exit_cancel_keeps_app()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        // Mo exit dialog
        drawerMenu.clickExit();
        Thread.sleep(1500);
        Assert.assertTrue(exitDialog.isDisplayed(),
                "Exit dialog khong mo");

        // Click Cancel
        exitDialog.clickCancel();
        Thread.sleep(1500);

        // Verify dialog dong
        Assert.assertFalse(exitDialog.isDisplayed(),
                "Dialog KHONG dong sau Cancel");

        // Verify app van o foreground
        String pkgAfter = drawerMenu.getCurrentPackage();
        Assert.assertEquals(pkgAfter, "com.bluesoftware.voicechanger",
                "App khong con o foreground sau Cancel");

        ExtentReportManager.getTest().log(Status.PASS,
                "Cancel giu app o foreground dung");
    }

    @Test(priority = 10, description = "DM_06_05: Click Exit confirm - dong app (DESTRUCTIVE)")
    public void test_DM_06_05_exit_confirm_closes_app()
            throws InterruptedException {
        Assert.assertTrue(drawerMenu.isDisplayed(),
                "Drawer khong mo");

        // Mo exit dialog
        drawerMenu.clickExit();
        Thread.sleep(1500);
        Assert.assertTrue(exitDialog.isDisplayed(),
                "Exit dialog khong mo");

        // Click Exit confirm
        exitDialog.clickExit();
        Thread.sleep(1500);  // M2: Giam tu 3000ms - app dong nhanh tren Android

        // Verify app khong con o foreground
        String pkgAfter = drawerMenu.getCurrentPackage();
        ExtentReportManager.getTest().log(Status.INFO,
                "Package sau Exit: " + pkgAfter);

        // Sau Exit, package se la launcher (vd: com.android.launcher)
        // hoac null/khac voicechanger
        Assert.assertNotEquals(pkgAfter, "com.bluesoftware.voicechanger",
                "App van con o foreground sau Exit confirm");

        ExtentReportManager.getTest().log(Status.PASS,
                "Exit app thanh cong, current: " + pkgAfter);
    }
}
