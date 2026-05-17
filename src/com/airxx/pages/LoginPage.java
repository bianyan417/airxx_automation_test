package com.airxx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object for XXX Air Company Login Page.
 * Handles user authentication including login, registration, and password recovery.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class LoginPage extends BasePage {
    
    // ===================== PAGE ELEMENTS =====================
    
    // Login Form
    @FindBy(css = "[data-testid='login-form'], .login-form, form[name='login']")
    private WebElement loginForm;
    
    @FindBy(css = "[data-testid='email-input'], input[name='email'], input[type='email'], #email")
    private WebElement emailInput;
    
    @FindBy(css = "[data-testid='password-input'], input[name='password'], input[type='password'], #password")
    private WebElement passwordInput;
    
    @FindBy(css = "[data-testid='login-btn'], button[type='submit'], .login-submit")
    private WebElement loginButton;
    
    @FindBy(css = "[data-testid='remember-me'], input[name='rememberMe'], .remember-me")
    private WebElement rememberMeCheckbox;
    
    // Social Login
    @FindBy(css = "[data-testid='google-login'], .google-login, a[href*='google']")
    private WebElement googleLoginButton;
    
    @FindBy(css = "[data-testid='facebook-login'], .facebook-login, a[href*='facebook']")
    private WebElement facebookLoginButton;
    
    @FindBy(css = "[data-testid='apple-login'], .apple-login, a[href*='apple']")
    private WebElement appleLoginButton;
    
    // Forgot Password
    @FindBy(css = "[data-testid='forgot-password-link'], a[href*='forgot'], .forgot-password")
    private WebElement forgotPasswordLink;
    
    @FindBy(css = "[data-testid='reset-email-input'], input[name='resetEmail']")
    private WebElement resetEmailInput;
    
    @FindBy(css = "[data-testid='reset-password-btn'], .reset-password-btn")
    private WebElement resetPasswordButton;
    
    // Registration
    @FindBy(css = "[data-testid='register-link'], a[href*='register'], .create-account")
    private WebElement registerLink;
    
    @FindBy(css = "[data-testid='register-email'], input[name='registerEmail']")
    private WebElement registerEmailInput;
    
    @FindBy(css = "[data-testid='register-password'], input[name='registerPassword']")
    private WebElement registerPasswordInput;
    
    @FindBy(css = "[data-testid='confirm-password'], input[name='confirmPassword']")
    private WebElement confirmPasswordInput;
    
    @FindBy(css = "[data-testid='first-name-reg'], input[name='firstName']")
    private WebElement firstNameRegInput;
    
    @FindBy(css = "[data-testid='last-name-reg'], input[name='lastName']")
    private WebElement lastNameRegInput;
    
    @FindBy(css = "[data-testid='register-btn'], .register-submit")
    private WebElement registerButton;
    
    // LoyaltyPoints Login
    @FindBy(css = "[data-testid='airpoints-tab'], .airpoints-login-tab")
    private WebElement loyaltyPointsTab;
    
    @FindBy(css = "[data-testid='airpoints-number'], input[name='airpointsNumber']")
    private WebElement loyaltyPointsNumberInput;
    
    @FindBy(css = "[data-testid='airpoints-pin'], input[name='airpointsPin']")
    private WebElement loyaltyPointsPinInput;
    
    // Messages
    @FindBy(css = "[data-testid='login-error'], .login-error, .error-message")
    private WebElement loginErrorMessage;
    
    @FindBy(css = "[data-testid='login-success'], .login-success, .success-message")
    private WebElement loginSuccessMessage;
    
    @FindBy(css = "[data-testid='validation-error'], .validation-error")
    private WebElement validationError;
    
    // User Info (after login)
    @FindBy(css = "[data-testid='user-name'], .user-name, .welcome-user")
    private WebElement userNameDisplay;
    
    @FindBy(css = "[data-testid='logout-btn'], .logout-btn, a[href*='logout']")
    private WebElement logoutButton;
    
    // Page Elements
    @FindBy(css = "[data-testid='login-heading'], .login-heading, h1")
    private WebElement loginHeading;
    
    @FindBy(css = "[data-testid='close-login'], .close-modal, .close-btn")
    private WebElement closeButton;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // ===================== PAGE ACTIONS =====================
    
    /**
     * Enters email address
     * @param email Email address
     * @return LoginPage instance for chaining
     */
    public LoginPage enterEmail(String email) {
        waitForVisible(By.cssSelector("[data-testid='email-input'], input[name='email'], input[type='email'], #email"));
        type(emailInput, email);
        logger.info("Entered email: {}", email);
        return this;
    }
    
    /**
     * Enters password
     * @param password Password
     * @return LoginPage instance for chaining
     */
    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        logger.info("Entered password");
        return this;
    }
    
    /**
     * Clicks login button
     * @return HomePage after successful login
     */
    public HomePage clickLogin() {
        click(loginButton);
        waitForPageLoad();
        logger.info("Clicked login button");
        return new HomePage(driver);
    }
    
    /**
     * Performs complete login
     * @param email Email address
     * @param password Password
     * @return HomePage after successful login
     */
    public HomePage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLogin();
    }
    
    /**
     * Performs login and stays on login page (for error scenarios)
     * @param email Email address
     * @param password Password
     * @return LoginPage for verification
     */
    public LoginPage loginExpectingError(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        click(loginButton);
        logger.info("Attempted login expecting error");
        return this;
    }
    
    /**
     * Checks "Remember Me" checkbox
     * @return LoginPage instance for chaining
     */
    public LoginPage checkRememberMe() {
        if (!isSelected(By.cssSelector("[data-testid='remember-me'], input[name='rememberMe']"))) {
            click(rememberMeCheckbox);
            logger.info("Checked Remember Me");
        }
        return this;
    }
    
    /**
     * Clicks forgot password link
     * @return LoginPage showing password reset
     */
    public LoginPage clickForgotPassword() {
        click(forgotPasswordLink);
        logger.info("Clicked forgot password");
        return this;
    }
    
    /**
     * Requests password reset
     * @param email Email for password reset
     * @return LoginPage with confirmation
     */
    public LoginPage requestPasswordReset(String email) {
        clickForgotPassword();
        waitForVisible(By.cssSelector("[data-testid='reset-email-input'], input[name='resetEmail']"));
        type(resetEmailInput, email);
        click(resetPasswordButton);
        logger.info("Requested password reset for: {}", email);
        return this;
    }
    
    /**
     * Clicks register link
     * @return LoginPage showing registration form
     */
    public LoginPage clickRegister() {
        click(registerLink);
        logger.info("Clicked register link");
        return this;
    }
    
    /**
     * Registers a new user
     * @param email Email
     * @param password Password
     * @param firstName First name
     * @param lastName Last name
     * @return HomePage after registration
     */
    public HomePage register(String email, String password, String firstName, String lastName) {
        clickRegister();
        
        waitForVisible(By.cssSelector("[data-testid='register-email'], input[name='registerEmail']"));
        type(registerEmailInput, email);
        type(registerPasswordInput, password);
        type(confirmPasswordInput, password);
        type(firstNameRegInput, firstName);
        type(lastNameRegInput, lastName);
        
        click(registerButton);
        waitForPageLoad();
        
        logger.info("Registered new user: {} {}", firstName, lastName);
        return new HomePage(driver);
    }
    
    /**
     * Switches to LoyaltyPoints login tab
     * @return LoginPage instance for chaining
     */
    public LoginPage switchToLoyaltyPointsLogin() {
        click(loyaltyPointsTab);
        logger.info("Switched to LoyaltyPoints login");
        return this;
    }
    
    /**
     * Performs LoyaltyPoints login
     * @param loyaltyPointsNumber LoyaltyPoints number
     * @param pin PIN
     * @return HomePage after successful login
     */
    public HomePage loginWithLoyaltyPoints(String loyaltyPointsNumber, String pin) {
        switchToLoyaltyPointsLogin();
        waitForVisible(By.cssSelector("[data-testid='airpoints-number'], input[name='airpointsNumber']"));
        type(loyaltyPointsNumberInput, loyaltyPointsNumber);
        type(loyaltyPointsPinInput, pin);
        click(loginButton);
        waitForPageLoad();
        logger.info("Logged in with LoyaltyPoints: {}", loyaltyPointsNumber);
        return new HomePage(driver);
    }
    
    /**
     * Clicks Google login button
     * @return LoginPage for Google OAuth flow
     */
    public LoginPage clickGoogleLogin() {
        click(googleLoginButton);
        logger.info("Clicked Google login");
        return this;
    }
    
    /**
     * Clicks Facebook login button
     * @return LoginPage for Facebook OAuth flow
     */
    public LoginPage clickFacebookLogin() {
        click(facebookLoginButton);
        logger.info("Clicked Facebook login");
        return this;
    }
    
    /**
     * Clicks Apple login button
     * @return LoginPage for Apple OAuth flow
     */
    public LoginPage clickAppleLogin() {
        click(appleLoginButton);
        logger.info("Clicked Apple login");
        return this;
    }
    
    /**
     * Checks if login error is displayed
     * @return true if error message is visible
     */
    public boolean isLoginErrorDisplayed() {
        try {
            return isDisplayed(By.cssSelector("[data-testid='login-error'], .login-error, .error-message"));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets login error message
     * @return Error message text
     */
    public String getLoginErrorMessage() {
        if (isLoginErrorDisplayed()) {
            return getText(loginErrorMessage);
        }
        return "";
    }
    
    /**
     * Checks if login success message is displayed
     * @return true if success message is visible
     */
    public boolean isLoginSuccessDisplayed() {
        try {
            return isDisplayed(By.cssSelector("[data-testid='login-success'], .login-success, .success-message"));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets validation error message
     * @return Validation error text
     */
    public String getValidationError() {
        if (isDisplayed(By.cssSelector("[data-testid='validation-error'], .validation-error"))) {
            return getText(validationError);
        }
        return "";
    }
    
    /**
     * Gets the logged-in user's name
     * @return User name
     */
    public String getLoggedInUserName() {
        return getText(userNameDisplay);
    }
    
    /**
     * Checks if user is logged in
     * @return true if user is logged in
     */
    public boolean isUserLoggedIn() {
        try {
            return isDisplayed(By.cssSelector("[data-testid='user-name'], .user-name, .welcome-user")) ||
                   isDisplayed(By.cssSelector("[data-testid='logout-btn'], .logout-btn"));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Logs out the current user
     * @return LoginPage after logout
     */
    public LoginPage logout() {
        click(logoutButton);
        waitForPageLoad();
        logger.info("User logged out");
        return this;
    }
    
    /**
     * Closes the login modal
     * @return HomePage after closing
     */
    public HomePage closeLoginModal() {
        click(closeButton);
        logger.info("Closed login modal");
        return new HomePage(driver);
    }
    
    /**
     * Gets the login page heading
     * @return Heading text
     */
    public String getLoginHeading() {
        return getText(loginHeading);
    }
    
    /**
     * Checks if email field has validation error
     * @return true if validation error exists
     */
    public boolean hasEmailValidationError() {
        try {
            String classes = getAttribute(By.cssSelector("[data-testid='email-input'], input[name='email']"), "class");
            return classes.contains("error") || classes.contains("invalid");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Checks if password field has validation error
     * @return true if validation error exists
     */
    public boolean hasPasswordValidationError() {
        try {
            String classes = getAttribute(By.cssSelector("[data-testid='password-input'], input[name='password']"), "class");
            return classes.contains("error") || classes.contains("invalid");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Clears all login fields
     * @return LoginPage instance for chaining
     */
    public LoginPage clearLoginForm() {
        emailInput.clear();
        passwordInput.clear();
        logger.info("Cleared login form");
        return this;
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            waitForPageLoad();
            return isDisplayed(By.cssSelector("[data-testid='login-form'], .login-form, form[name='login']")) ||
                   isDisplayed(By.cssSelector("[data-testid='email-input'], input[name='email'], input[type='email']")) ||
                   getCurrentUrl().contains("login") || getCurrentUrl().contains("signin");
        } catch (Exception e) {
            return false;
        }
    }
}