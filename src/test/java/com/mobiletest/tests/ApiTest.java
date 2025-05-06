package com.mobiletest.tests;

import com.mobiletest.framework.core.BaseTest;
import com.mobiletest.framework.utils.TestUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * ApiTest contains test cases for API testing.
 * This demonstrates how to perform API testing within the mobile test framework.
 */
@Feature("API Testing")
public class ApiTest extends BaseTest {
    
    private final String BASE_URL = "https://jsonplaceholder.typicode.com";
    
    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }
    
    /**
     * Test GET request to retrieve a list of users
     */
    @Test(groups = {"api", "smoke"})
    @Description("Test GET request to retrieve a list of users")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User should be able to retrieve a list of users via API")
    public void testGetUsers() {
        TestUtils.logInfo("Starting API test: testGetUsers");
        
        Response response = given()
            .contentType(ContentType.JSON)
            .when()
            .get("/users")
            .then()
            .statusCode(200)
            .extract().response();
        
        // Verify the response contains multiple users
        int userCount = response.jsonPath().getList("$").size();
        TestUtils.logInfo("Number of users retrieved: " + userCount);
        Assert.assertTrue(userCount > 0, "User list should not be empty");
    }
    
    /**
     * Test GET request to retrieve a specific user
     */
    @Test(groups = {"api", "regression"})
    @Description("Test GET request to retrieve a specific user")
    @Severity(SeverityLevel.NORMAL)
    @Story("User should be able to retrieve a specific user via API")
    public void testGetSpecificUser() {
        TestUtils.logInfo("Starting API test: testGetSpecificUser");
        
        int userId = 1;
        
        Response response = given()
            .contentType(ContentType.JSON)
            .pathParam("userId", userId)
            .when()
            .get("/users/{userId}")
            .then()
            .statusCode(200)
            .extract().response();
        
        // Verify the user details
        String username = response.jsonPath().getString("username");
        String email = response.jsonPath().getString("email");
        
        TestUtils.logInfo("User details - Username: " + username + ", Email: " + email);
        
        Assert.assertNotNull(username, "Username should not be null");
        Assert.assertNotNull(email, "Email should not be null");
        Assert.assertTrue(email.contains("@"), "Email should be in valid format");
    }
    
    /**
     * Test POST request to create a new user
     */
    @Test(groups = {"api", "regression"})
    @Description("Test POST request to create a new user")
    @Severity(SeverityLevel.NORMAL)
    @Story("User should be able to create a new user via API")
    public void testCreateUser() {
        TestUtils.logInfo("Starting API test: testCreateUser");
        
        String requestBody = "{"
            + "\"name\": \"John Doe\","
            + "\"username\": \"johndoe\","
            + "\"email\": \"john.doe@example.com\""
            + "}";
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post("/users")
            .then()
            .statusCode(201)
            .extract().response();
        
        // Verify the created user
        String name = response.jsonPath().getString("name");
        String username = response.jsonPath().getString("username");
        String email = response.jsonPath().getString("email");
        
        TestUtils.logInfo("Created user - Name: " + name + ", Username: " + username + ", Email: " + email);
        
        Assert.assertEquals(name, "John Doe", "Name should match the request");
        Assert.assertEquals(username, "johndoe", "Username should match the request");
        Assert.assertEquals(email, "john.doe@example.com", "Email should match the request");
    }
}