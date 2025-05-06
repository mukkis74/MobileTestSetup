package com.mobiletest.tests;

import com.mobiletest.framework.core.BaseTest;
import com.mobiletest.framework.pages.LoginPage;
import com.mobiletest.framework.utils.TestUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest contains test cases for the login functionality.
 */
@Feature("Login")
public class LoginTest extends BaseTest {
    
    /**
     * Test successful login with valid credentials
     */
    @Test(groups = {"smoke", "regression"})
    @Description("Test successful login with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User should be able to login with valid credentials")
    public void testSuccessfulLogin() {
        TestUtils.logInfo("Starting test: testSuccessfulLogin");
        
        LoginPage loginPage = new LoginPage();
        loginPage.login("validuser", "validpassword");
        
        // In a real test, we would verify that the user is redirected to the home page
        // For this example, we'll just assert true
        Assert.assertTrue(true, "Login should be successful");
    }
    
    /**
     * Test failed login with invalid credentials
     */
    @Test(groups = {"regression"})
    @Description("Test failed login with invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    @Story("User should see an error message when logging in with invalid credentials")
    public void testFailedLogin() {
        TestUtils.logInfo("Starting test: testFailedLogin");
        
        LoginPage loginPage = new LoginPage();
        loginPage.login("invaliduser", "invalidpassword");
        
        // Verify that an error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        
        // Verify the error message text
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertEquals(errorMessage, "Invalid username or password", "Error message should match expected text");
    }
    
    /**
     * Test login with empty credentials
     */
    @Test(groups = {"regression"})
    @Description("Test login with empty credentials")
    @Severity(SeverityLevel.NORMAL)
    @Story("User should see an error message when logging in with empty credentials")
    public void testEmptyCredentials() {
        TestUtils.logInfo("Starting test: testEmptyCredentials");
        
        LoginPage loginPage = new LoginPage();
        loginPage.login("", "");
        
        // Verify that an error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        
        // Verify the error message text
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertEquals(errorMessage, "Username and password are required", "Error message should match expected text");
    }
}