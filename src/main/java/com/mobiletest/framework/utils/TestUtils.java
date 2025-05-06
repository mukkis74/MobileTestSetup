package com.mobiletest.framework.utils;

import com.mobiletest.framework.core.DeviceManager;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestUtils provides utility methods for logging and taking screenshots.
 */
public class TestUtils {
    
    private static final Logger logger = LogManager.getLogger(TestUtils.class);
    
    /**
     * Log an info message
     * 
     * @param message The message to log
     */
    public static void logInfo(String message) {
        logger.info(message);
    }
    
    /**
     * Log an error message
     * 
     * @param message The message to log
     */
    public static void logError(String message) {
        logger.error(message);
    }
    
    /**
     * Log an error message with an exception
     * 
     * @param message The message to log
     * @param e The exception to log
     */
    public static void logError(String message, Throwable e) {
        logger.error(message, e);
    }
    
    /**
     * Take a screenshot and save it to the specified directory
     * 
     * @param screenshotName The name of the screenshot
     * @return The path to the saved screenshot
     */
    public static String takeScreenshot(String screenshotName) {
        AppiumDriver driver = DeviceManager.getDriver();
        if (driver == null) {
            logError("Driver is null, cannot take screenshot");
            return null;
        }
        
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";
        String directory = System.getProperty("user.dir") + "/target/screenshots/";
        
        File screenshotDirectory = new File(directory);
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdirs();
        }
        
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filePath = directory + fileName;
        
        try {
            FileUtils.copyFile(screenshotFile, new File(filePath));
            logInfo("Screenshot saved to: " + filePath);
            return filePath;
        } catch (IOException e) {
            logError("Failed to save screenshot", e);
            return null;
        }
    }
    
    /**
     * Take a screenshot and attach it to the Allure report
     * 
     * @param screenshotName The name of the screenshot
     * @return The screenshot as a byte array
     */
    @Attachment(value = "{0}", type = "image/png")
    public static byte[] takeScreenshotForAllure(String screenshotName) {
        AppiumDriver driver = DeviceManager.getDriver();
        if (driver == null) {
            logError("Driver is null, cannot take screenshot for Allure");
            return null;
        }
        
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    
    /**
     * Get the current timestamp as a string
     * 
     * @param format The format of the timestamp (default: "yyyyMMdd_HHmmss")
     * @return The formatted timestamp
     */
    public static String getTimestamp(String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyyMMdd_HHmmss";
        }
        return new SimpleDateFormat(format).format(new Date());
    }
}