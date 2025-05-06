package com.mobiletest.framework.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

/**
 * LoginPage represents the login page of the mobile application.
 * It provides methods to interact with the login page elements.
 */
public class LoginPage extends BasePage {
    
    @AndroidFindBy(id = "com.example.app:id/username_input")
    @iOSXCUITFindBy(accessibility = "username_input")
    private WebElement usernameInput;
    
    @AndroidFindBy(id = "com.example.app:id/password_input")
    @iOSXCUITFindBy(accessibility = "password_input")
    private WebElement passwordInput;
    
    @AndroidFindBy(id = "com.example.app:id/login_button")
    @iOSXCUITFindBy(accessibility = "login_button")
    private WebElement loginButton;
    
    @AndroidFindBy(id = "com.example.app:id/error_message")
    @iOSXCUITFindBy(accessibility = "error_message")
    private WebElement errorMessage;
    
    /**
     * Enter username in the username input field
     * 
     * @param username The username to enter
     * @return The LoginPage instance for method chaining
     */
    @Step("Enter username: {0}")
    public LoginPage enterUsername(String username) {
        sendKeys(usernameInput, username);
        return this;
    }
    
    /**
     * Enter password in the password input field
     * 
     * @param password The password to enter
     * @return The LoginPage instance for method chaining
     */
    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        sendKeys(passwordInput, password);
        return this;
    }
    
    /**
     * Click the login button
     * 
     * @return The HomePage instance if login is successful
     */
    @Step("Click login button")
    public void clickLoginButton() {
        click(loginButton);
    }
    
    /**
     * Login with the specified username and password
     * 
     * @param username The username to enter
     * @param password The password to enter
     */
    @Step("Login with username: {0}")
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
    
    /**
     * Get the error message text
     * 
     * @return The error message text
     */
    @Step("Get error message")
    public String getErrorMessage() {
        return getText(errorMessage);
    }
    
    /**
     * Check if the error message is displayed
     * 
     * @return True if the error message is displayed, false otherwise
     */
    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }
}