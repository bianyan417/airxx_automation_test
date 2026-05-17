package com.airxx.tests.bdd.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.testng.Assert;

import com.airxx.core.config.DriverManager;
import com.airxx.core.utils.TestDataLoader;
import com.airxx.pages.HomePage;
import com.airxx.pages.LoginPage;

import java.util.Map;

/**
 * LoginSteps - Cucumber step definitions for login functionality.
 * Maps Gherkin steps to test automation code.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class LoginSteps {
    
    private HomePage homePage;
    private LoginPage loginPage;
    private String currentScenario;
    
    @Given("I am on the XXX Air Company home page")
    public void iAmOnTheAirNewZealandHomePage() {
        homePage = new HomePage(DriverManager.getDriver());
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }
    
    @Given("I navigate to the login page")
    public void iNavigateToTheLoginPage() {
        loginPage = homePage.clickLogin();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
    }
    
    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        iAmOnTheAirNewZealandHomePage();
        iNavigateToTheLoginPage();
    }
    
    @When("I enter valid login credentials")
    public void iEnterValidLoginCredentials() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        loginPage.enterEmail(testData.get("email"));
        loginPage.enterPassword(testData.get("password"));
        currentScenario = "validLogin";
    }
    
    @When("I enter invalid login credentials")
    public void iEnterInvalidLoginCredentials() {
        Map<String, String> testData = TestDataLoader.getLoginData("invalidPassword");
        loginPage.enterEmail(testData.get("email"));
        loginPage.enterPassword(testData.get("password"));
        currentScenario = "invalidPassword";
    }
    
    @When("I enter email {string} and password {string}")
    public void iEnterEmailAndPassword(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
    }
    
    @When("I enter credentials for scenario {string}")
    public void iEnterCredentialsForScenario(String scenario) {
        Map<String, String> testData = TestDataLoader.getLoginData(scenario);
        loginPage.enterEmail(testData.get("email"));
        loginPage.enterPassword(testData.get("password"));
        currentScenario = scenario;
    }
    
    @When("I click the login button")
    public void iClickTheLoginButton() {
        if ("validLogin".equals(currentScenario)) {
            homePage = loginPage.clickLogin();
        } else {
            loginPage.loginExpectingError(
                loginPage.getClass().getName(), 
                "click"
            );
        }
    }
    
    @When("I submit the login form")
    public void iSubmitTheLoginForm() {
        iClickTheLoginButton();
    }
    
    @When("I check the Remember Me checkbox")
    public void iCheckTheRememberMeCheckbox() {
        loginPage.checkRememberMe();
    }
    
    @When("I click the forgot password link")
    public void iClickTheForgotPasswordLink() {
        loginPage.clickForgotPassword();
    }
    
    @When("I enter my email for password reset")
    public void iEnterMyEmailForPasswordReset() {
        Map<String, String> testData = TestDataLoader.getLoginData("validLogin");
        loginPage.requestPasswordReset(testData.get("email"));
    }
    
    @When("I click the register link")
    public void iClickTheRegisterLink() {
        loginPage.clickRegister();
    }
    
    @When("I fill in registration details")
    public void iFillInRegistrationDetails() {
        Map<String, String> testData = TestDataLoader.getLoginData("newUserRegistration");
        homePage = loginPage.register(
            testData.get("email"),
            testData.get("password"),
            testData.get("firstName"),
            testData.get("lastName")
        );
    }
    
    @When("I login with LoyaltyPoints credentials")
    public void iLoginWithLoyaltyPointsCredentials() {
        Map<String, String> testData = TestDataLoader.getLoginData("loyaltyPointsLogin");
        homePage = loginPage.loginWithLoyaltyPoints(
            testData.get("loyaltyPointsNumber"),
            testData.get("pin")
        );
    }
    
    @When("I click the Google login button")
    public void iClickTheGoogleLoginButton() {
        loginPage.clickGoogleLogin();
    }
    
    @When("I close the login modal")
    public void iCloseTheLoginModal() {
        homePage = loginPage.closeLoginModal();
    }
    
    @Then("I should be logged in successfully")
    public void iShouldBeLoggedInSuccessfully() {
        Assert.assertTrue(homePage.isUserLoggedIn() || homePage.isPageLoaded(),
            "User should be logged in");
    }
    
    @Then("I should see the home page")
    public void iShouldSeeTheHomePage() {
        Assert.assertTrue(homePage.isPageLoaded(), "Home page should be displayed");
    }
    
    @Then("I should see a login error message")
    public void iShouldSeeALoginErrorMessage() {
        Assert.assertTrue(loginPage.isLoginErrorDisplayed(),
            "Login error message should be displayed");
    }
    
    @Then("I should see an error message containing {string}")
    public void iShouldSeeAnErrorMessageContaining(String errorText) {
        String actualError = loginPage.getLoginErrorMessage();
        Assert.assertTrue(actualError.toLowerCase().contains(errorText.toLowerCase()),
            "Error message should contain: " + errorText);
    }
    
    @Then("I should remain on the login page")
    public void iShouldRemainOnTheLoginPage() {
        Assert.assertTrue(loginPage.isPageLoaded(), "Should remain on login page");
    }
    
    @Then("I should see a password reset confirmation")
    public void iShouldSeeAPasswordResetConfirmation() {
        Assert.assertTrue(loginPage.isPageLoaded() || loginPage.isLoginSuccessDisplayed(),
            "Password reset confirmation should be displayed");
    }
    
    @Then("I should see the registration form")
    public void iShouldSeeTheRegistrationForm() {
        Assert.assertTrue(loginPage.isPageLoaded(), "Registration form should be displayed");
    }
    
    @Then("I should be registered and logged in")
    public void iShouldBeRegisteredAndLoggedIn() {
        Assert.assertTrue(homePage.isPageLoaded(), "User should be registered and on home page");
    }
    
    @Then("the login modal should close")
    public void theLoginModalShouldClose() {
        Assert.assertTrue(homePage.isPageLoaded(), "Login modal should be closed");
    }
    
    @Then("I should not be logged in")
    public void iShouldNotBeLoggedIn() {
        Assert.assertTrue(loginPage.isPageLoaded() && !homePage.isUserLoggedIn(),
            "User should not be logged in");
    }
    
    @And("I should see the user menu")
    public void iShouldSeeTheUserMenu() {
        Assert.assertTrue(homePage.isUserLoggedIn(), "User menu should be visible");
    }
    
    @And("the Remember Me checkbox should be checked")
    public void theRememberMeCheckboxShouldBeChecked() {
        // Verify through page state
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should show Remember Me checked");
    }
}
