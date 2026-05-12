package Pages.Components;

import io.appium.java_client.AppiumDriver;

/**
 * Component xu ly bottom sheet Open With.
 * Cung dung com.android.intentresolver nhu ShareBottomSheet.
 */
public class OpenWithBottomSheet extends ShareBottomSheet {

    public OpenWithBottomSheet(AppiumDriver driver) {
        super(driver);
    }

    // Ke thua hoan toan tu ShareBottomSheet vi DOM giong nhau
    // Co the override neu can hanh vi khac
}