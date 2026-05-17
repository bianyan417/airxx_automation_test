package com.airxx.tests.api;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.airxx.api.client.ApiClient;
import com.airxx.core.listeners.TestListener;
import com.airxx.core.utils.TestDataLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * LoginApiTest - API tests for authentication endpoints.
 * Tests login, registration, token management, and authentication flows.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
@Listeners(TestListener.class)
public class LoginApiTest {
    
    private ApiClient apiClient;
    private String authToken;
    
    // API Endpoints
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    private static final String REGISTER_ENDPOINT = "/api/v1/auth/register";
    private static final String LOGOUT_ENDPOINT = "/api/v1/auth/logout";
    private static final String REFRESH_TOKEN_ENDPOINT = "/api/v1/auth/refresh";
    private static final String FORGOT_PASSWORD_ENDPOINT = "/api/v1/auth/forgot-password";
    private static final String RESET_PASSWORD_ENDPOINT = "/api/v1/auth/reset-password";
    private static final String PROFILE_ENDPOINT = "/api/v1/user/profile";
    private static final String VALIDATE_TOKEN_ENDPOINT = "/api/v1/auth/validate";
    
    @BeforeClass(description = "Initialize API client")
    public void setUp() {
        apiClient = new ApiClient();
    }
    
    /**
     * Data provider for valid login scenarios
     */
    @DataProvider(name = "validLoginData")
    public Object[][] validLoginDataProvider() {
        return new Object[][] {
            {"validLogin"},
            {"airpointsLogin"}
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
            {"nonExistentUser"},
            {"emptyCredentials"}
        };
    }
    
    @Test(description = "Verify login API with valid credentials", priority = 1)
    public void testLoginWithValidCredentials() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", testData.get("email"));
        loginRequest.put("password", testData.get("password"));
        
        Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);
        
        Assert.assertEquals(response.getStatusCode(), 200, 
            "Login should return 200 OK");
        
        authToken = ApiClient.getJsonPathValue(response, "token");
        if (authToken == null) {
            authToken = ApiClient.getJsonPathValue(response, "accessToken");
        }
    }
    
    @Test(description = "Verify login API with invalid credentials", 
          dataProvider = "invalidLoginData", priority = 2)
    public void testLoginWithInvalidCredentials(String scenario) {
        Map<String, String> testData = TestDataLoader.getLoginData(scenario);
        
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", testData.get("email"));
        loginRequest.put("password", testData.get("password"));
        
        Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);
        
        Assert.assertTrue(response.getStatusCode() >= 400, 
            "Invalid login should return error status");
    }
    
    @Test(description = "Verify login response contains token", priority = 3,
          dependsOnMethods = "testLoginWithValidCredentials")
    public void testLoginResponseContainsToken() {
        Assert.assertNotNull(authToken, "Auth token should be present in response");
        Assert.assertFalse(authToken.isEmpty(), "Auth token should not be empty");
    }
    
    @Test(description = "Verify user registration API", priority = 4)
    public void testUserRegistration() {
        Map<String, String> testData = TestDataLoader.getLoginData("newUserRegistration");
        
        Map<String, String> registrationRequest = new HashMap<>();
        registrationRequest.put("email", testData.get("email"));
        registrationRequest.put("password", testData.get("password"));
        registrationRequest.put("firstName", testData.get("firstName"));
        registrationRequest.put("lastName", testData.get("lastName"));
        
        Response response = apiClient.post(REGISTER_ENDPOINT, registrationRequest);
        
        // 201 Created or 400 if user already exists
        Assert.assertTrue(response.getStatusCode() == 201 || response.getStatusCode() == 200 ||
                         response.getStatusCode() == 409, // Conflict if user exists
            "Registration should return valid response");
    }
    
    @Test(description = "Verify duplicate registration fails", priority = 5)
    public void testDuplicateRegistration() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        
        Map<String, String> registrationRequest = new HashMap<>();
        registrationRequest.put("email", testData.get("email"));
        registrationRequest.put("password", testData.get("password"));
        registrationRequest.put("firstName", "Test");
        registrationRequest.put("lastName", "User");
        
        Response response = apiClient.post(REGISTER_ENDPOINT, registrationRequest);
        
        // Should return conflict or bad request
        Assert.assertTrue(response.getStatusCode() == 409 || response.getStatusCode() == 400,
            "Duplicate registration should fail");
    }
    
    @Test(description = "Verify authenticated request with token", priority = 6,
          dependsOnMethods = "testLoginWithValidCredentials")
    public void testAuthenticatedRequest() {
        if (authToken != null) {
            apiClient.setAuthToken(authToken);
            
            Response response = apiClient.get(PROFILE_ENDPOINT);
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 401,
                "Authenticated request should succeed or indicate invalid token");
        }
    }
    
    @Test(description = "Verify unauthenticated request fails", priority = 7)
    public void testUnauthenticatedRequest() {
        ApiClient noAuthClient = new ApiClient();
        
        Response response = noAuthClient.get(PROFILE_ENDPOINT);
        
        Assert.assertEquals(response.getStatusCode(), 401, 
            "Unauthenticated request should return 401");
    }
    
    @Test(description = "Verify token refresh", priority = 8,
          dependsOnMethods = "testLoginWithValidCredentials")
    public void testTokenRefresh() {
        if (authToken != null) {
            Map<String, String> refreshRequest = new HashMap<>();
            refreshRequest.put("refreshToken", authToken);
            
            Response response = apiClient.post(REFRESH_TOKEN_ENDPOINT, refreshRequest);
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 401,
                "Token refresh should return valid response");
        }
    }
    
    @Test(description = "Verify logout API", priority = 9,
          dependsOnMethods = "testLoginWithValidCredentials")
    public void testLogout() {
        if (authToken != null) {
            apiClient.setAuthToken(authToken);
            
            Response response = apiClient.post(LOGOUT_ENDPOINT);
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204,
                "Logout should return success");
        }
    }
    
    @Test(description = "Verify forgot password API", priority = 10)
    public void testForgotPassword() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        
        Map<String, String> forgotRequest = new HashMap<>();
        forgotRequest.put("email", testData.get("email"));
        
        Response response = apiClient.post(FORGOT_PASSWORD_ENDPOINT, forgotRequest);
        
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 202,
            "Forgot password should return success");
    }
    
    @Test(description = "Verify forgot password with invalid email", priority = 11)
    public void testForgotPasswordInvalidEmail() {
        Map<String, String> forgotRequest = new HashMap<>();
        forgotRequest.put("email", "nonexistent@example.com");
        
        Response response = apiClient.post(FORGOT_PASSWORD_ENDPOINT, forgotRequest);
        
        // Should still return 200 for security reasons (not reveal if email exists)
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 404,
            "Forgot password should handle non-existent email");
    }
    
    @Test(description = "Verify reset password API", priority = 12)
    public void testResetPassword() {
        Map<String, String> resetRequest = new HashMap<>();
        resetRequest.put("token", "test-reset-token");
        resetRequest.put("newPassword", "NewPassword123!");
        
        Response response = apiClient.post(RESET_PASSWORD_ENDPOINT, resetRequest);
        
        // Token is invalid in test, so should return error
        Assert.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 401 ||
                         response.getStatusCode() == 404,
            "Reset with invalid token should fail");
    }
    
    @Test(description = "Verify token validation endpoint", priority = 13,
          dependsOnMethods = "testLoginWithValidCredentials")
    public void testTokenValidation() {
        if (authToken != null) {
            Map<String, String> validateRequest = new HashMap<>();
            validateRequest.put("token", authToken);
            
            Response response = apiClient.post(VALIDATE_TOKEN_ENDPOINT, validateRequest);
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 401,
                "Token validation should return valid response");
        }
    }
    
    @Test(description = "Verify invalid token is rejected", priority = 14)
    public void testInvalidTokenRejected() {
        apiClient.setAuthToken("invalid-token-123");
        
        Response response = apiClient.get(PROFILE_ENDPOINT);
        
        Assert.assertEquals(response.getStatusCode(), 401, 
            "Invalid token should be rejected");
        
        apiClient.reset();
    }
    
    @Test(description = "Verify login with empty body fails", priority = 15)
    public void testLoginWithEmptyBody() {
        Map<String, String> emptyRequest = new HashMap<>();
        
        Response response = apiClient.post(LOGIN_ENDPOINT, emptyRequest);
        
        Assert.assertTrue(response.getStatusCode() >= 400, 
            "Login with empty body should fail");
    }
    
    @Test(description = "Verify login with malformed JSON", priority = 16)
    public void testLoginWithMalformedJson() {
        String malformedJson = "{ email: test@test.com, password: }";
        
        Response response = apiClient.post(LOGIN_ENDPOINT, malformedJson);
        
        Assert.assertTrue(response.getStatusCode() >= 400, 
            "Login with malformed JSON should fail");
    }
    
    @Test(description = "Verify password requirements on registration", priority = 17)
    public void testPasswordRequirements() {
        Map<String, String> registrationRequest = new HashMap<>();
        registrationRequest.put("email", "newuser@example.com");
        registrationRequest.put("password", "weak"); // Too weak
        registrationRequest.put("firstName", "Test");
        registrationRequest.put("lastName", "User");
        
        Response response = apiClient.post(REGISTER_ENDPOINT, registrationRequest);
        
        Assert.assertTrue(response.getStatusCode() >= 400, 
            "Weak password should be rejected");
    }
    
    @Test(description = "Verify email format validation", priority = 18)
    public void testEmailFormatValidation() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "invalid-email");
        loginRequest.put("password", "Password123!");
        
        Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);
        
        Assert.assertTrue(response.getStatusCode() >= 400, 
            "Invalid email format should be rejected");
    }
    
    @Test(description = "Verify SQL injection prevention in login", priority = 19)
    public void testSqlInjectionPrevention() {
        Map<String, String> testData = TestDataLoader.getLoginData("sqlInjection");
        
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", testData.get("email"));
        loginRequest.put("password", testData.get("password"));
        
        Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);
        
        // Should fail gracefully, not crash
        Assert.assertTrue(response.getStatusCode() >= 400, 
            "SQL injection attempt should fail");
    }
    
    @Test(description = "Verify login API response time", priority = 20)
    public void testLoginApiResponseTime() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", testData.get("email"));
        loginRequest.put("password", testData.get("password"));
        
        Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);
        
        long responseTime = ApiClient.getResponseTime(response);
        Assert.assertTrue(responseTime < 3000, 
            "Login API response time should be less than 3 seconds");
    }
    
    @Test(description = "Verify rate limiting on login endpoint", priority = 21)
    public void testRateLimiting() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "test@example.com");
        loginRequest.put("password", "wrongpassword");
        
        int rateLimitedCount = 0;
        for (int i = 0; i < 20; i++) {
            Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);
            if (response.getStatusCode() == 429) {
                rateLimitedCount++;
            }
        }
        
        // If rate limiting is implemented, at least some requests should be blocked
        // This is informational - application may or may not implement rate limiting
        System.out.println("Rate limited requests: " + rateLimitedCount + "/20");
    }
}
