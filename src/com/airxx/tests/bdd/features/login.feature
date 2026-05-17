@login @ui
Feature: User Login
  As a registered user
  I want to log in to my XXX Air Company account
  So that I can manage my bookings and access member benefits

  Background:
    Given I am on the login page

  @smoke @positive
  Scenario: Successful login with valid credentials
    When I enter valid login credentials
    And I click the login button
    Then I should be logged in successfully
    And I should see the user menu

  @smoke @negative
  Scenario: Failed login with invalid password
    When I enter invalid login credentials
    And I click the login button
    Then I should see a login error message
    And I should remain on the login page

  @regression @negative
  Scenario: Failed login with invalid email format
    When I enter email "invalid-email" and password "Password123"
    And I click the login button
    Then I should see a login error message

  @regression @negative
  Scenario: Failed login with empty credentials
    When I enter email "" and password ""
    And I click the login button
    Then I should see a login error message
    And I should remain on the login page

  @regression @negative
  Scenario: Failed login with non-existent user
    When I enter email "nonexistent@example.com" and password "Password123"
    And I click the login button
    Then I should see a login error message

  @regression @negative
  Scenario Outline: Failed login with various invalid credentials
    When I enter credentials for scenario "<scenario>"
    And I click the login button
    Then I should see a login error message

    Examples:
      | scenario         |
      | invalidPassword  |
      | invalidEmail     |
      | nonExistentUser  |

  @regression @remember-me
  Scenario: Login with Remember Me option
    When I enter valid login credentials
    And I check the Remember Me checkbox
    And I click the login button
    Then I should be logged in successfully

  @regression @forgot-password
  Scenario: Request password reset
    When I click the forgot password link
    And I enter my email for password reset
    Then I should see a password reset confirmation

  @regression @registration
  Scenario: Navigate to registration page
    When I click the register link
    Then I should see the registration form

  @regression @registration
  Scenario: Register new user
    When I click the register link
    And I fill in registration details
    Then I should be registered and logged in

  @regression @airpoints
  Scenario: Login with LoyaltyPoints credentials
    When I login with LoyaltyPoints credentials
    Then I should be logged in successfully

  @regression @social-login
  Scenario: View social login options
    Then I should see the Google login button is visible

  @regression @modal
  Scenario: Close login modal
    When I close the login modal
    Then the login modal should close
    And I should see the home page

  @security @negative
  Scenario Outline: Security - Invalid input handling
    When I enter credentials for scenario "<scenario>"
    And I click the login button
    Then I should see a login error message
    And I should not be logged in

    Examples:
      | scenario       |
      | sqlInjection   |
      | xssAttempt     |

  @regression @error-messages
  Scenario: Verify error message content for invalid password
    When I enter credentials for scenario "invalidPassword"
    And I click the login button
    Then I should see an error message containing "invalid"

  @regression @error-messages
  Scenario: Verify error message content for non-existent user
    When I enter credentials for scenario "nonExistentUser"
    And I click the login button
    Then I should see an error message containing "not found"
