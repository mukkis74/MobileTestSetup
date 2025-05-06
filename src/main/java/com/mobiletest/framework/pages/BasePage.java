package com.mobiletest.framework.pages;

import com.mobiletest.framework.core.DeviceManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage is the parent class for all page objects in the framework.
 * It provides common methods for interacting with elements on the page.
 */
public abstract class BasePage {

    protected AppiumDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor for BasePage
     */
    public BasePage() {
        this.driver = DeviceManager.getDriver();

        // Skip initialization if driver is null (when running tests directly)
        if (driver != null) {
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        }
    }

    /**
     * Wait for an element to be visible
     * 
     * @param element The element to wait for
     * @return The visible element
     */
    @Step("Wait for element to be visible")
    protected WebElement waitForElementVisible(WebElement element) {
        if (driver == null || wait == null) {
            return null; // Return null if driver is not initialized
        }
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for an element to be clickable
     * 
     * @param element The element to wait for
     * @return The clickable element
     */
    @Step("Wait for element to be clickable")
    protected WebElement waitForElementClickable(WebElement element) {
        if (driver == null || wait == null) {
            return null; // Return null if driver is not initialized
        }
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Click on an element
     * 
     * @param element The element to click
     */
    @Step("Click on element")
    protected void click(WebElement element) {
        if (driver == null || wait == null) {
            return; // Do nothing if driver is not initialized
        }
        waitForElementClickable(element).click();
    }

    /**
     * Enter text into an element
     * 
     * @param element The element to enter text into
     * @param text The text to enter
     */
    @Step("Enter text: {1}")
    protected void sendKeys(WebElement element, String text) {
        if (driver == null || wait == null) {
            return; // Do nothing if driver is not initialized
        }
        waitForElementVisible(element).clear();
        element.sendKeys(text);
    }

    /**
     * Get text from an element
     * 
     * @param element The element to get text from
     * @return The text of the element
     */
    @Step("Get text from element")
    protected String getText(WebElement element) {
        if (driver == null || wait == null) {
            return ""; // Return empty string if driver is not initialized
        }
        return waitForElementVisible(element).getText();
    }

    /**
     * Check if an element is displayed
     * 
     * @param element The element to check
     * @return True if the element is displayed, false otherwise
     */
    @Step("Check if element is displayed")
    protected boolean isElementDisplayed(WebElement element) {
        if (driver == null || wait == null) {
            return false; // Return false if driver is not initialized
        }
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Swipe from one point to another
     * 
     * @param startX The starting X coordinate
     * @param startY The starting Y coordinate
     * @param endX The ending X coordinate
     * @param endY The ending Y coordinate
     */
    @Step("Swipe from ({0},{1}) to ({2},{3})")
    protected void swipe(int startX, int startY, int endX, int endY) {
        // Implementation will depend on the Appium version and platform
        // This is a placeholder for the swipe functionality
    }

    /**
     * Wait for a specific duration
     * 
     * @param seconds The number of seconds to wait
     */
    protected void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
