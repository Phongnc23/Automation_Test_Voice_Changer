package Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class cho logging.
 * - logger thuong: chi ghi vao file (khong hien console)
 * - RESULT logger: ghi ca file va console (de hien thi ket qua test)
 */
public class LogUtils {

    // Logger cho ket qua test (hien thi console)
    private static final Logger resultLogger = LogManager.getLogger("RESULT");

    /**
     * Log ket qua test - hien thi tren console
     */
    public static void result(String message) {
        resultLogger.info(message);
    }

    /**
     * Log header (separator)
     */
    public static void separator() {
        resultLogger.info("==========================================");
    }

    /**
     * Log header voi title
     */
    public static void header(String title) {
        resultLogger.info("==========================================");
        resultLogger.info(title);
        resultLogger.info("==========================================");
    }
}