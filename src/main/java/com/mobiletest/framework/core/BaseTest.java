package com.mobiletest.framework.core;

import com.mobiletest.framework.config.ConfigReader;
import com.mobiletest.framework.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;

/**
 * BaseTest is the parent class for all test classes in the framework.
 * It handles setup and teardown operations, including initializing the Appium driver,
 * taking screenshots on test failures, and generating Allure reports.
 */
public abstract class BaseTest {

    protected AppiumDriver driver;

    /**
     * Setup operations to be performed before the test suite runs
     */
    @BeforeSuite
    public void beforeSuite() {
        TestUtils.logInfo("Starting test suite execution");
        ConfigReader.initialize();
    }

    /**
     * Setup operations to be performed before each test method
     * 
     * @param method The test method being executed
     * @param platform The platform to run the test on (android or ios)
     */
    @BeforeMethod
    @Parameters({"platform"})
    public void beforeMethod(Method method, String platform) {
        TestUtils.logInfo("Starting test: " + method.getName() + " on platform: " + platform);

        if ("android".equalsIgnoreCase(platform)) {
            setupAndroidDriver();
        } else if ("ios".equalsIgnoreCase(platform)) {
            setupIOSDriver();
        } else {
            throw new IllegalArgumentException("Invalid platform: " + platform);
        }

        driver = DeviceManager.getDriver();
    }

    /**
     * Teardown operations to be performed after each test method
     * 
     * @param result The result of the test method
     */
    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            TestUtils.logError("Test failed: " + result.getName());
            takeScreenshotOnFailure(result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            TestUtils.logInfo("Test passed: " + result.getName());
        } else {
            TestUtils.logInfo("Test skipped: " + result.getName());
        }

        DeviceManager.quitDriver();
    }

    /**
     * Teardown operations to be performed after the test suite runs
     */
    @AfterSuite
    public void afterSuite() {
        TestUtils.logInfo("Finished test suite execution");
    }

    /**
     * Setup the Android driver
     */
    private void setupAndroidDriver() {
        // Get the device name from the configuration or use a default device
        String deviceName = ConfigReader.getProperty("android.deviceName", "Pixel_4_API_30");

        TestUtils.logInfo("Setting up Android driver with deviceName: " + deviceName);

        // Use the new method that gets capabilities from capabilities.json
        DeviceManager.initializeAndroidDriver(deviceName);
    }

    /**
     * Setup the iOS driver
     */
    private void setupIOSDriver() {
        // Get the device name from the configuration or use a default device
        String deviceName = ConfigReader.getProperty("ios.deviceName", "iPhone 12");

        TestUtils.logInfo("Setting up iOS driver with deviceName: " + deviceName);

        // Use the new method that gets capabilities from capabilities.json
        DeviceManager.initializeIOSDriver(deviceName);
    }

    /**
     * Take a screenshot when a test fails and attach it to the Allure report
     * 
     * @param testName The name of the failed test
     */
    @Attachment(value = "Screenshot on failure", type = "image/png")
    private byte[] takeScreenshotOnFailure(String testName) {
        TestUtils.logInfo("Taking screenshot for failed test: " + testName);
        return TestUtils.takeScreenshotForAllure("Failure_" + testName);
    }
}
