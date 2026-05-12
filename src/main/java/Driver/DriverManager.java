package Driver;

import io.appium.java_client.AppiumDriver;

public interface DriverManager {

    AppiumDriver createDriver();

    AppiumDriver getDriver();

    void quitDriver();
}