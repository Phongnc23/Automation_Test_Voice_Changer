package Listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import Utils.LogUtils;

/**
 * Auto-retry tests bi fail tu dong (chong inherent flakiness cua mobile UI).
 *
 * Max retry: 1 lan (test chay toi da 2 lan total).
 * - Lan 1 fail -> retry lan 2.
 * - Lan 2 PASS -> final status PASS, log warn.
 * - Lan 2 fail -> final status FAIL (real failure).
 *
 * Apply tu dong cho TAT CA @Test method via {@link RetryListener}
 * (registered trong Regression.xml).
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);

    /** Max retry attempts (1 = chay toi da 2 lan total). */
    private static final int MAX_RETRIES = 1;

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRIES) {
            retryCount++;
            String testName = result.getMethod().getMethodName();
            String reason = result.getThrowable() != null
                    ? result.getThrowable().getMessage()
                    : "unknown";

            logger.warn("RETRY {}/{} - {} (reason: {})",
                    retryCount, MAX_RETRIES, testName, reason);
            LogUtils.result("  ↻ RETRY " + retryCount + "/" + MAX_RETRIES
                    + ": " + testName);
            return true;
        }
        return false;
    }
}
