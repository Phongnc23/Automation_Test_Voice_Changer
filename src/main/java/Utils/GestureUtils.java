package Utils;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

public class GestureUtils {

    private static final Logger logger = LogManager.getLogger(GestureUtils.class);

    /**
     * Vuot tu canh trai sang giua de back 1 lan.
     * Toi uu: duration 400ms (tu 800ms), sleep 500ms (tu 1000ms).
     */
    public static void swipeFromLeftEdgeToBack(AppiumDriver driver) {
        logger.info("Swipe back from left edge");

        Dimension size = driver.manage().window().getSize();
        int startX = 5;
        int endX = (int) (size.width * 0.8);
        int y = size.height / 2;

        performSwipe(driver, startX, y, endX, y, 400);  // giam tu 800ms

        try {
            Thread.sleep(500);  // giam tu 1000ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Swipe back nhieu lan lien tiep.
     */
    public static void swipeBackMultipleTimes(AppiumDriver driver, int times) {
        logger.info("Swipe back " + times + " times");

        for (int i = 1; i <= times; i++) {
            swipeFromLeftEdgeToBack(driver);
        }
    }

    /**
     * Tap vung ngoai drawer de dong.
     */
    public static void tapOutsideDrawer(AppiumDriver driver) {
        logger.info("Tap outside drawer to close");

        Dimension size = driver.manage().window().getSize();
        int x = (int) (size.width * 0.9);
        int y = size.height / 2;

        tap(driver, x, y);

        try {
            Thread.sleep(500);  // giam tu 800ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void tap(AppiumDriver driver, int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerMove(Duration.ofMillis(50),  // giam tu 100ms
                PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    public static void performSwipe(AppiumDriver driver,
                                    int startX, int startY,
                                    int endX, int endY,
                                    int durationMs) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(durationMs),
                PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    public static void swipeVertical(AppiumDriver driver, boolean swipeUp) {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = swipeUp ? (int)(size.height * 0.8) : (int)(size.height * 0.2);
        int endY = swipeUp ? (int)(size.height * 0.2) : (int)(size.height * 0.8);

        performSwipe(driver, startX, startY, startX, endY, 300);  // giam tu 500ms
    }
}