package Listeners;

import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import Report.ExtentReportManager;
import Utils.LogUtils;

import java.lang.reflect.Field;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("==========================================");
        logger.info("START SUITE: " + context.getName());
        logger.info("==========================================");

        LogUtils.header("STARTING SUITE: " + context.getName());

        // ====== QUAN TRONG: Khoi tao ExtentReports ======
        ExtentReportManager.initializeExtentReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        logger.info("------------------------------------------");
        logger.info("START TEST: " + testName);
        if (description != null) {
            logger.info("Description: " + description);
        }

        LogUtils.result("");
        LogUtils.result("▶ Running: " + testName);
        if (description != null) {
            LogUtils.result("  Description: " + description);
        }

        // Tao test trong ExtentReport
        ExtentReportManager.createTest(testName,
                description != null ? description : "No description");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        logger.info("PASSED: " + testName);
        LogUtils.result("  ✓ PASSED");

        ExtentReportManager.pass("Test passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();

        logger.error("FAILED: " + testName);
        logger.error("Reason: " + throwable.getMessage());

        LogUtils.result("  ✗ FAILED");
        LogUtils.result("    Reason: " + throwable.getMessage());

        // Log fail vao report
        ExtentReportManager.fail("Test failed: " + throwable.getMessage());

        // ====== TU DONG CHUP SCREENSHOT KHI FAIL ======
        try {
            AppiumDriver driver = getDriverFromTest(result);
            if (driver != null) {
                logger.info("Capturing screenshot for failed test: " + testName);
                ExtentReportManager.captureScreenshot(driver, testName);
            } else {
                logger.warn("Driver is null, cannot capture screenshot");
            }
        } catch (Exception e) {
            logger.error("Error capturing screenshot: " + e.getMessage());
        }

        // Log stacktrace
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.FAIL, throwable);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        logger.warn("SKIPPED: " + testName);
        LogUtils.result("  ⊘ SKIPPED");

        String skipReason = result.getThrowable() != null
                ? result.getThrowable().getMessage()
                : "Test was skipped";
        ExtentReportManager.skip("⊘ Skipped: " + skipReason);
    }

    @Override
    public void onFinish(ITestContext context) {
        int total = context.getAllTestMethods().length;
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();

        logger.info("==========================================");
        logger.info("END SUITE: " + context.getName());
        logger.info("Total: " + total + ", Passed: " + passed
                + ", Failed: " + failed + ", Skipped: " + skipped);
        logger.info("==========================================");

        LogUtils.result("");
        LogUtils.separator();
        LogUtils.result("TEST RESULTS:");
        LogUtils.result("  Total:   " + total);
        LogUtils.result("  Passed:  " + passed);
        LogUtils.result("  Failed:  " + failed);
        LogUtils.result("  Skipped: " + skipped);
        LogUtils.separator();

        // ====== QUAN TRONG: Ghi file HTML ======
        ExtentReportManager.flushReports();

        LogUtils.result("Report:       " + ExtentReportManager.getReportPath());
        LogUtils.result("Log file:     test-output/automation.log");
        if (failed > 0) {
            LogUtils.result("Screenshots:  test-output/screenshots/");
        }
        LogUtils.separator();
    }

    // ====== HELPER METHODS ======

    /**
     * Lay AppiumDriver tu test instance dang chay (qua reflection).
     */
    private AppiumDriver getDriverFromTest(ITestResult result) {
        try {
            Object testInstance = result.getInstance();
            if (testInstance == null) return null;

            Class<?> clazz = testInstance.getClass();
            while (clazz != null) {
                try {
                    Field driverField = clazz.getDeclaredField("driver");
                    driverField.setAccessible(true);
                    Object driverObj = driverField.get(testInstance);
                    if (driverObj instanceof AppiumDriver) {
                        return (AppiumDriver) driverObj;
                    }
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                    continue;
                }
                break;
            }
        } catch (Exception e) {
            logger.error("Cannot get driver from test: " + e.getMessage());
        }
        return null;
    }
}