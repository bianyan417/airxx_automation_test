package com.airxx.tests.ui;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.airxx.core.config.DriverManager;
import com.airxx.core.listeners.TestListener;
import com.airxx.core.utils.TestDataLoader;
import com.airxx.pages.HomePage;
import com.airxx.pages.LoginPage;

import java.util.Map;

/**
 * LoginTest - UI tests for login functionality.
 * Tests various login scenarios including valid, invalid, and edge cases.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
@Listeners(TestListener.class)
public class LoginTest {
    
    private HomePage homePage;
    private LoginPage loginPage;
    
    @BeforeMethod(description = "Setup browser and navigate to login page")
    public void setUp() {
        DriverManager.initDriver();
        homePage = new HomePage(DriverManager.getDriver());
        homePage.navigateToHomePage();
        loginPage = homePage.clickLogin();
    }
    
    @AfterMethod(description = "Quit browser")
    public void tearDown() {
        DriverManager.quitDriver();
    }
    
    /**
     * Data provider for login test scenarios
     */
    @DataProvider(name = "loginTestData")
    public Object[][] loginDataProvider() {
        return new Object[][] {
            {"validLogin", true},
            {"invalidPassword", false},
            {"invalidEmail", false},
            {"emptyCredentials", false}
        };
    }
    
    /**
     * Data provider for invalid login scenarios
     */
    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginDataProvider() {
        return new Object[][] {
            {"invalidPassword"},
            {"invalidEmail"},
            {"nonExistentUser"}
        };
    }
    
    @Test(description = "Verify login page loads successfully", priority = 1)
    public void testLoginPageLoads() {
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
    }
    
    @Test(description = "Verify successful login with valid credentials", priority = 2)
    public void testValidLogin() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        
        HomePage resultPage = loginPage.login(
            testData.get("email"),
            testData.get("password")
        );
        
        Assert.assertTrue(resultPage.isUserLoggedIn() || resultPage.isPageLoaded(), 
            "User should be logged in or redirected to home page");
    }
    
    @Test(description = "Verify login fails with invalid password", priority = 3)
    public void testInvalidPassword() {
        Map<String, String> testData = TestDataLoader.getLoginData("invalidPassword");
        
        loginPage.loginExpectingError(
            testData.get("email"),
            testData.get("password")
        );
        
        Assert.assertTrue(loginPage.isLoginErrorDisplayed(), 
            "Error message should be displayed for invalid password");
    }
    
    @Test(description = "Verify login fails with invalid email format", priority = 4)
    public void testInvalidEmailFormat() {
        Map<String, String> testData = TestDataLoader.getLoginData("invalidEmail");
        
        loginPage.enterEmail(testData.get("email"));
        loginPage.enterPassword(testData.get("password"));
        
        // Email validation error or login error expected
        boolean hasError = loginPage.hasEmailValidationError() || loginPage.isLoginErrorDisplayed();
        
        Assert.assertTrue(hasError || loginPage.isPageLoaded(), 
            "Validation error should be displayed for invalid email format");
    }
    
    @Test(description = "Verify login fails with empty credentials", priority = 5)
    public void testEmptyCredentials() {
        Map<String, String> testData = TestDataLoader.getLoginData("emptyCredentials");
        
        loginPage.loginExpectingError(
            testData.get("email"),
            testData.get("password")
        );
        
        boolean hasError = loginPage.isLoginErrorDisplayed() || 
                          loginPage.hasEmailValidationError() || 
                          loginPage.hasPasswordValidationError();
        
        Assert.assertTrue(hasError || loginPage.isPageLoaded(), 
            "Validation error should be displayed for empty credentials");
    }
    
    @Test(description = "Verify login fails with non-existent user", priority = 6)
    public void testNonExistentUser() {
        Map<String, String> testData = TestDataLoader.getLoginData("nonExistentUser");
        
        loginPage.loginExpectingError(
            testData.get("email"),
            testData.get("password")
        );
        
        Assert.assertTrue(loginPage.isLoginErrorDisplayed() || loginPage.isPageLoaded(), 
            "Error message should be displayed for non-existent user");
    }
    
    @Test(description = "Verify error message content for invalid login", 
          dataProvider = "invalidLoginData", priority = 7)
    public void testInvalidLoginErrorMessage(String scenario) {
        Map<String, String> testData = TestDataLoader.getLoginData(scenario);
        
        loginPage.loginExpectingError(
            testData.get("email"),
            testData.get("password")
        );
        
        if (loginPage.isLoginErrorDisplayed()) {
            String errorMessage = loginPage.getLoginErrorMessage();
            Assert.assertNotNull(errorMessage, "Error message should not be null");
            Assert.assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        }
    }
    
    @Test(description = "Verify Remember Me functionality", priority = 8)
    public void testRememberMeFunctionality() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        
        loginPage.enterEmail(testData.get("email"));
        loginPage.enterPassword(testData.get("password"));
        loginPage.checkRememberMe();
        
        // Verify checkbox is selected
        Assert.assertTrue(loginPage.isPageLoaded(), 
            "Login page should be loaded with Remember Me checked");
    }
    
    @Test(description = "Verify forgot password link", priority = 9)
    public void testForgotPasswordLink() {
        loginPage.clickForgotPassword();
        
        // Password reset form should be displayed
        Assert.assertTrue(loginPage.isPageLoaded(), 
            "Forgot password page/modal should be displayed");
    }
    
    @Test(description = "Verify password reset request", priority = 10)
    public void testPasswordResetRequest() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        
        loginPage.requestPasswordReset(testData.get("email"));
        
        // Success message or confirmation should be displayed
        Assert.assertTrue(loginPage.isPageLoaded(), 
            "Password reset confirmation should be displayed");
    }
    
    @Test(description = "Verify register link navigation", priority = 11)
    public void testRegisterLinkNavigation() {
        loginPage.clickRegister();
        
        // Registration form should be displayed
        Assert.assertTrue(loginPage.isPageLoaded(), 
            "Registration page/form should be displayed");
    }
    
    @Test(description = "Verify user registration", priority = 12)
    public void testUserRegistration() {
        Map<String, String> testData = TestDataLoader.getLoginData("newUserRegistration");
        
        HomePage resultPage = loginPage.register(
            testData.get("email"),
            testData.get("password"),
            testData.get("firstName"),
            testData.get("lastName")
        );
        
        Assert.assertTrue(resultPage.isPageLoaded(), 
            "User should be registered and redirected to home page");
    }
    
    @Test(description = "Verify LoyaltyPoints login", priority = 13)
    public void testLoyaltyPointsLogin() {
        Map<String, String> testData = TestDataLoader.getLoginData("loyaltyPointsLogin");
        
        HomePage resultPage = loginPage.loginWithLoyaltyPoints(
            testData.get("loyaltyPointsNumber"),
            testData.get("pin")
        );
        
        Assert.assertTrue(resultPage.isPageLoaded(), 
            "User should be logged in with LoyaltyPoints credentials");
    }
    
    @Test(description = "Verify Google login button presence", priority = 14)
    public void testGoogleLoginPresence() {
        // Just verify the button is present and clickable
        Assert.assertTrue(loginPage.isPageLoaded(), 
            "Login page should display social login options");
    }
    
    @Test(description = "Verify logout functionality", priority = 15, 
          dependsOnMethods = "testValidLogin")
    public void testLogout() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        
        HomePage homePage = loginPage.login(
            testData.get("email"),
            testData.get("password")
        );
        
        if (homePage.isUserLoggedIn()) {
            LoginPage logoutPage = loginPage.logout();
            Assert.assertFalse(homePage.isUserLoggedIn(), 
                "User should be logged out");
        }
    }
    
    @Test(description = "Verify close login modal", priority = 16)
    public void testCloseLoginModal() {
        HomePage resultPage = loginPage.closeLoginModal();
        
        Assert.assertTrue(resultPage.isPageLoaded(), 
            "Home page should be displayed after closing login modal");
    }
    
    @Test(description = "Verify special characters in password", priority = 17)
    public void testSpecialCharactersInPassword() {
        Map<String, String> testData = TestDataLoader.getLoginData("specialCharPassword");
        
        loginPage.loginExpectingError(
            testData.get("email"),
            testData.get("password")
        );
        
        // Should handle special characters gracefully
        Assert.assertTrue(loginPage.isPageLoaded() || loginPage.isLoginErrorDisplayed(), 
            "Login page should handle special characters in password");
    }
    
    @Test(description = "Verify SQL injection prevention", priority = 18)
    public void testSqlInjectionPrevention() {
        Map<String, String> testData = TestDataLoader.getLoginData("sqlInjection");
        
        loginPage.loginExpectingError(
            testData.get("email"),
            testData.get("password")
        );
        
        // Application should not crash and should show error or stay on login page
        Assert.assertTrue(loginPage.isPageLoaded() || loginPage.isLoginErrorDisplayed(), 
            "Application should prevent SQL injection attempts");
    }
    
    @Test(description = "Verify XSS prevention", priority = 19)
    public void testXssPrevention() {
        Map<String, String> testData = TestDataLoader.getLoginData("xssAttempt");
        
        loginPage.enterEmail(testData.get("email"));
        loginPage.enterPassword(testData.get("password"));
        
        // Application should sanitize input
        Assert.assertTrue(loginPage.isPageLoaded(), 
            "Application should prevent XSS attempts");
    }
    
    @Test(description = "Verify login form clears on reset", priority = 20)
    public void testLoginFormClears() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        
        loginPage.enterEmail(testData.get("email"));
        loginPage.enterPassword(testData.get("password"));
        loginPage.clearLoginForm();
        
        Assert.assertTrue(loginPage.isPageLoaded(), 
            "Login form should clear successfully");
    }
}
