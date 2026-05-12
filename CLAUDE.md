# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this project is

Appium-based mobile automation framework targeting the Android app **Voice Changer** (`com.bluesoftware.voicechanger`). Driver, device, and app coordinates are hardcoded in `Constants/AppConstants.java` (Oppo Pad Neo, Android 14, UiAutomator2, Appium server at `http://127.0.0.1:4723`). An Appium server must be running before any test executes — there is no programmatic server lifecycle.

Many page objects (`RecordPage`, `MyAudioPage`, `SettingsPage`, `VoiceEffectsPage`, `ImportAudioPage`, `Pages/Components/*`) and test classes (`ImportAudio01_*`, `MyAudio01_*`, `Record01_*`, `Settings01_*`) exist as **empty stubs** — only the Home flow is wired end-to-end and registered in the suite XML. Filling these in is the active work.

Code comments, log messages, and test descriptions are written in unaccented Vietnamese; preserve that style when adding to existing files.

## Build & test commands

```powershell
.\gradlew.bat test                                                # runs the suite defined in src/test/resources/Regression.xml
.\gradlew.bat test --tests "testcases.Home.Home01_Verify_App_Launched"          # single class
.\gradlew.bat test --tests "testcases.Home.Home01_Verify_App_Launched.test_app_launched_successfully"   # single method
.\gradlew.bat build
.\gradlew.bat clean
```

No lint task is configured.

### Suite XML caveat

`build.gradle` passes `src/test/resources/regression.xml` (lowercase) but the file on disk is `Regression.xml`. This only works because Windows/NTFS is case-insensitive — the build will fail on Linux/macOS CI. If you add a CI runner, either rename the file or fix the path.

### Adding a new test

1. Create the test class under `src/test/java/testcases/<Feature>/` extending `Base.BaseTest`.
2. **Register it in `src/test/resources/Regression.xml`** — TestNG only runs classes listed in the suite XML; classes are not auto-discovered. Adding a `@Test` method without editing the XML is a silent no-op.

## Architecture

### Driver lifecycle
`Base.BaseTest` owns `@BeforeClass` / `@AfterClass` — **one Appium session per test class**, not per method. `DriverFactory.getDriverManager(AppConstants.PLATFORM_NAME)` dispatches to `AndroidDriverManager` or `IOSDriverManager` (iOS is stubbed). Teardown calls `terminateApp` before `driver.quit()` to release the app cleanly. `setNoReset(true)` is set, so app state persists across the session — tests must not assume a fresh install.

### Page objects
`Base.BasePage` wraps an `AppiumDriver` with a `WebDriverWait` (default `TimeOutConstants.MEDIUM_WAIT` = 15s) and exposes `findElement`, `click`, `sendKeys`, `getText`, `isDisplayed`, `waitForElementVisible`, `sleep`. All page classes should extend `BasePage` and take `AppiumDriver` in the constructor; locator constants and high-level actions go in the page class.

### Reporting & logging — dual-channel
`Listeners.TestListener` is the integration point. It is registered in `Regression.xml` (`<listener class-name="Listeners.TestListener"/>`) — not via `@Listeners` on test classes. It:
- creates one `ExtentTest` per `@Test` method through `Report.ExtentReportManager` (HTML report at `reports/ExtentReport_<timestamp>.html`),
- routes test-result lines to `Utils.LogUtils.result(...)` which uses a **dedicated `"RESULT"` Log4j logger** so they appear on the console, while everything else (root logger) only goes to the log file.

`src/main/resources/log4j2.xml` enforces the split: console accepts `RESULT`-logger INFO + root-logger WARN/ERROR; file appenders (`test-output/automation.log` + rolling) capture everything at INFO. Selenium/Appium/Netty loggers are pinned at ERROR to suppress noise. When adding logs, use `LogUtils.result(...)` only for output the user is meant to see in the console; use a class-level Log4j `Logger` for everything else.

### Constants
`Constants/AppConstants.java` holds device/app/server coordinates as `public static final` strings — change these to retarget a different device or app build. `Constants/TimeOutConstants.java` centralizes wait/sleep durations; prefer these over inline literals.