package com.mobiletest.framework.core;

import com.mobiletest.framework.config.CapabilityManager;
import com.mobiletest.framework.config.ConfigReader;
import com.mobiletest.framework.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * DeviceManager is responsible for initializing and managing the Appium driver
 * for both Android and iOS platforms.
 */
public class DeviceManager {

    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static String APPIUM_SERVER_URL;

    static {
        // Initialize the CapabilityManager
        CapabilityManager.initialize();

        // Get the Appium server URL from the configuration
        APPIUM_SERVER_URL = ConfigReader.getProperty("appium.server.url", "http://localhost:4723/wd/hub");
    }

    /**
     * Initialize the Appium driver for Android using a specific device from capabilities.json
     * 
     * @param deviceName The name of the Android device as defined in capabilities.json
     * @return The initialized AndroidDriver
     */
    public static AndroidDriver initializeAndroidDriver(String deviceName) {
        TestUtils.logInfo("Initializing Android driver for device: " + deviceName);

        // Get capabilities from the CapabilityManager
        DesiredCapabilities capabilities = CapabilityManager.getCapabilities("android", deviceName);

        try {
            AndroidDriver androidDriver = new AndroidDriver(new URL(APPIUM_SERVER_URL), capabilities);
            driver.set(androidDriver);
            androidDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            TestUtils.logInfo("Android driver initialized successfully");
            return androidDriver;
        } catch (MalformedURLException e) {
            TestUtils.logError("Failed to initialize Android driver", e);
            throw new RuntimeException("Failed to initialize Android driver", e);
        }
    }

    /**
     * Initialize the Appium driver for Android with custom capabilities
     * 
     * @param deviceName The name of the Android device or emulator
     * @param platformVersion The Android OS version
     * @param appPackage The package name of the app
     * @param appActivity The activity name to launch
     * @return The initialized AndroidDriver
     */
    public static AndroidDriver initializeAndroidDriver(String deviceName, String platformVersion, 
                                                      String appPackage, String appActivity) {
        TestUtils.logInfo("Initializing Android driver with custom capabilities");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("noReset", false);

        try {
            AndroidDriver androidDriver = new AndroidDriver(new URL(APPIUM_SERVER_URL), capabilities);
            driver.set(androidDriver);
            androidDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            TestUtils.logInfo("Android driver initialized successfully with custom capabilities");
            return androidDriver;
        } catch (MalformedURLException e) {
            TestUtils.logError("Failed to initialize Android driver with custom capabilities", e);
            throw new RuntimeException("Failed to initialize Android driver", e);
        }
    }

    /**
     * Initialize the Appium driver for iOS using a specific device from capabilities.json
     * 
     * @param deviceName The name of the iOS device as defined in capabilities.json
     * @return The initialized IOSDriver
     */
    public static IOSDriver initializeIOSDriver(String deviceName) {
        TestUtils.logInfo("Initializing iOS driver for device: " + deviceName);

        // Get capabilities from the CapabilityManager
        DesiredCapabilities capabilities = CapabilityManager.getCapabilities("ios", deviceName);

        try {
            IOSDriver iosDriver = new IOSDriver(new URL(APPIUM_SERVER_URL), capabilities);
            driver.set(iosDriver);
            iosDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            TestUtils.logInfo("iOS driver initialized successfully");
            return iosDriver;
        } catch (MalformedURLException e) {
            TestUtils.logError("Failed to initialize iOS driver", e);
            throw new RuntimeException("Failed to initialize iOS driver", e);
        }
    }

    /**
     * Initialize the Appium driver for iOS with custom capabilities
     * 
     * @param deviceName The name of the iOS device or simulator
     * @param platformVersion The iOS version
     * @param bundleId The bundle ID of the app
     * @return The initialized IOSDriver
     */
    public static IOSDriver initializeIOSDriver(String deviceName, String platformVersion, String bundleId) {
        TestUtils.logInfo("Initializing iOS driver with custom capabilities");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("bundleId", bundleId);
        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("noReset", false);

        try {
            IOSDriver iosDriver = new IOSDriver(new URL(APPIUM_SERVER_URL), capabilities);
            driver.set(iosDriver);
            iosDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            return iosDriver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to initialize iOS driver", e);
        }
    }

    /**
     * Get the current driver instance
     * 
     * @return The current AppiumDriver instance
     */
    public static AppiumDriver getDriver() {
        return driver.get();
    }

    /**
     * Quit the driver and clear the ThreadLocal
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
