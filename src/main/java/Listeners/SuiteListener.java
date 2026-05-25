package Listeners;

import Driver.SuiteDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * Tao driver mot lan vao luc bat dau suite va quit khi ket thuc.
 *
 * Phai duoc dang ky trong Regression.xml:
 *   <listener class-name="Listeners.SuiteListener"/>
 *
 * BaseTest.setUp khong tao driver nua - chi lay tu SuiteDriverManager.
 */
public class SuiteListener implements ISuiteListener {

    private static final Logger logger = LogManager.getLogger(SuiteListener.class);

    @Override
    public void onStart(ISuite suite) {
        logger.info("=== SUITE START: " + suite.getName() + " ===");
        SuiteDriverManager.getOrCreateDriver();
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("=== SUITE FINISH: " + suite.getName() + " ===");
        SuiteDriverManager.quitDriver();
    }
}
