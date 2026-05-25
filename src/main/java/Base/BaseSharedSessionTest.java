package Base;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import Utils.RecordFlowHelper;

/**
 * Base class chia session: mo man hinh 1 lan @BeforeClass,
 * @BeforeMethod chi re-navigate khi state khong dung.
 *
 * Khac BaseTest:
 *   - setupSession() chay sau setUp() (TestNG goi parent's @BeforeClass truoc)
 *   - ensureCorrectScreen() chi tao re-nav khi can (tiet kiem nav redundant)
 *   - KHONG co @AfterClass: BaseTest.tearDown() khong quit driver nua (suite-level)
 *
 * L2+L3: setupSession() check `isAtExpectedScreen()` truoc khi navigate.
 * Khi suite chay theo thu tu group (vd Setting01 -> Setting02 -> ...), class sau
 * thua huong screen state cua class truoc, skip navigate -> tiet kiem 2-5s/class.
 *
 * Subclass phai implement:
 *   - navigateToScreen(): mo man hinh can test
 *   - isAtExpectedScreen(): check state hien tai
 */
public abstract class BaseSharedSessionTest extends BaseTest {

    @BeforeClass(dependsOnMethods = "setUp", alwaysRun = true)
    public void setupSession() {
        logger.info("=== Setup shared session: " + this.getClass().getSimpleName() + " ===");
        // QUAN TRONG: Luon goi navigateToScreen() de KHOI TAO page object fields.
        // Tan dung screen state cross-class (L3) duoc thuc hien qua idempotency
        // trong cac navigate method cua RecordFlowHelper (return ngay neu da o target).
        try {
            navigateToScreen();
        } catch (Exception e) {
            logger.error("Setup error, force reset: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            navigateToScreen();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void ensureCorrectScreen() {
        if (isAtExpectedScreen()) {
            logger.info("Da o man can test, skip re-navigate");
            return;
        }
        logger.info("State khong dung, re-navigate");
        try {
            RecordFlowHelper.smartResetToHome(driver);
            navigateToScreen();
        } catch (Exception e) {
            logger.error("Re-navigate error, force reset: " + e.getMessage());
            RecordFlowHelper.forceResetToHome(driver);
            navigateToScreen();
        }
    }

    /** Subclass implement: mo man hinh dich. */
    protected abstract void navigateToScreen();

    /** Subclass implement: check dang o man dich. */
    protected abstract boolean isAtExpectedScreen();
}
