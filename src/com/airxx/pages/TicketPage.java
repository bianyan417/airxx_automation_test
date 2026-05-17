package com.airxx.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * TicketPage - Page Object for XXX Air Company Ticket/Passenger Details Page.
 * Handles passenger information, seat selection, and extras.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class TicketPage extends BasePage {
    
    // ===================== PAGE ELEMENTS =====================
    
    // Passenger Details Form
    @FindBy(css = "[data-testid='passenger-form'], .passenger-form")
    private WebElement passengerForm;
    
    @FindBy(css = "[data-testid='title-select'], select[name='title']")
    private WebElement titleSelect;
    
    @FindBy(css = "[data-testid='first-name'], input[name='firstName']")
    private WebElement firstNameInput;
    
    @FindBy(css = "[data-testid='middle-name'], input[name='middleName']")
    private WebElement middleNameInput;
    
    @FindBy(css = "[data-testid='last-name'], input[name='lastName']")
    private WebElement lastNameInput;
    
    @FindBy(css = "[data-testid='dob-day'], input[name='dobDay']")
    private WebElement dobDayInput;
    
    @FindBy(css = "[data-testid='dob-month'], select[name='dobMonth']")
    private WebElement dobMonthSelect;
    
    @FindBy(css = "[data-testid='dob-year'], input[name='dobYear']")
    private WebElement dobYearInput;
    
    @FindBy(css = "[data-testid='gender-select'], select[name='gender']")
    private WebElement genderSelect;
    
    @FindBy(css = "[data-testid='nationality-select'], select[name='nationality']")
    private WebElement nationalitySelect;
    
    // Contact Details
    @FindBy(css = "[data-testid='email-input'], input[name='email']")
    private WebElement emailInput;
    
    @FindBy(css = "[data-testid='confirm-email'], input[name='confirmEmail']")
    private WebElement confirmEmailInput;
    
    @FindBy(css = "[data-testid='phone-input'], input[name='phone']")
    private WebElement phoneInput;
    
    @FindBy(css = "[data-testid='country-code'], select[name='countryCode']")
    private WebElement countryCodeSelect;
    
    // Passport Details
    @FindBy(css = "[data-testid='passport-number'], input[name='passportNumber']")
    private WebElement passportNumberInput;
    
    @FindBy(css = "[data-testid='passport-country'], select[name='passportCountry']")
    private WebElement passportCountrySelect;
    
    @FindBy(css = "[data-testid='passport-expiry-day'], input[name='passportExpiryDay']")
    private WebElement passportExpiryDayInput;
    
    @FindBy(css = "[data-testid='passport-expiry-month'], select[name='passportExpiryMonth']")
    private WebElement passportExpiryMonthSelect;
    
    @FindBy(css = "[data-testid='passport-expiry-year'], input[name='passportExpiryYear']")
    private WebElement passportExpiryYearInput;
    
    // LoyaltyPoints / Loyalty
    @FindBy(css = "[data-testid='airpoints-number'], input[name='airpointsNumber']")
    private WebElement loyaltyPointsNumberInput;
    
    @FindBy(css = "[data-testid='add-airpoints-link'], .add-airpoints")
    private WebElement addLoyaltyPointsLink;
    
    // Seat Selection
    @FindBy(css = "[data-testid='select-seats-btn'], .select-seats")
    private WebElement selectSeatsButton;
    
    @FindBy(css = "[data-testid='seat-map'], .seat-map")
    private WebElement seatMap;
    
    @FindBy(css = "[data-testid='available-seat'], .seat.available")
    private List<WebElement> availableSeats;
    
    @FindBy(css = "[data-testid='selected-seat'], .seat.selected")
    private WebElement selectedSeat;
    
    @FindBy(css = "[data-testid='seat-price'], .seat-price")
    private WebElement seatPrice;
    
    @FindBy(css = "[data-testid='confirm-seat-btn'], .confirm-seat")
    private WebElement confirmSeatButton;
    
    // Extras / Add-ons
    @FindBy(css = "[data-testid='add-baggage'], .add-baggage")
    private WebElement addBaggageButton;
    
    @FindBy(css = "[data-testid='baggage-options'], .baggage-options")
    private WebElement baggageOptions;
    
    @FindBy(css = "[data-testid='extra-bag-23kg'], .bag-23kg")
    private WebElement extraBag23kg;
    
    @FindBy(css = "[data-testid='add-meal'], .add-meal")
    private WebElement addMealButton;
    
    @FindBy(css = "[data-testid='meal-options'], .meal-options")
    private WebElement mealOptions;
    
    @FindBy(css = "[data-testid='add-insurance'], .add-insurance")
    private WebElement addInsuranceButton;
    
    // Summary
    @FindBy(css = "[data-testid='flight-summary'], .flight-summary")
    private WebElement flightSummary;
    
    @FindBy(css = "[data-testid='passenger-summary'], .passenger-summary")
    private WebElement passengerSummary;
    
    @FindBy(css = "[data-testid='price-summary'], .price-summary")
    private WebElement priceSummary;
    
    @FindBy(css = "[data-testid='total-amount'], .total-amount")
    private WebElement totalAmount;
    
    // Action Buttons
    @FindBy(css = "[data-testid='continue-to-payment'], .continue-payment")
    private WebElement continueToPaymentButton;
    
    @FindBy(css = "[data-testid='back-to-flights'], .back-flights")
    private WebElement backToFlightsButton;
    
    @FindBy(css = "[data-testid='save-and-continue'], .save-continue")
    private WebElement saveAndContinueButton;
    
    // Validation Messages
    @FindBy(css = "[data-testid='error-message'], .error-message")
    private WebElement errorMessage;
    
    @FindBy(css = "[data-testid='field-error'], .field-error")
    private List<WebElement> fieldErrors;
    
    // Terms and Conditions
    @FindBy(css = "[data-testid='terms-checkbox'], input[name='acceptTerms']")
    private WebElement termsCheckbox;
    
    @FindBy(css = "[data-testid='privacy-checkbox'], input[name='acceptPrivacy']")
    private WebElement privacyCheckbox;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public TicketPage(WebDriver driver) {
        super(driver);
    }
    
    // ===================== PAGE ACTIONS =====================
    
    /**
     * Enters passenger title
     * @param title Title (Mr, Mrs, Ms, etc.)
     * @return TicketPage instance for chaining
     */
    public TicketPage selectTitle(String title) {
        selectByVisibleText(By.cssSelector("[data-testid='title-select'], select[name='title']"), title);
        logger.info("Selected title: {}", title);
        return this;
    }
    
    /**
     * Enters passenger first name
     * @param firstName First name
     * @return TicketPage instance for chaining
     */
    public TicketPage enterFirstName(String firstName) {
        type(firstNameInput, firstName);
        logger.info("Entered first name: {}", firstName);
        return this;
    }
    
    /**
     * Enters passenger middle name
     * @param middleName Middle name
     * @return TicketPage instance for chaining
     */
    public TicketPage enterMiddleName(String middleName) {
        type(middleNameInput, middleName);
        logger.info("Entered middle name: {}", middleName);
        return this;
    }
    
    /**
     * Enters passenger last name
     * @param lastName Last name
     * @return TicketPage instance for chaining
     */
    public TicketPage enterLastName(String lastName) {
        type(lastNameInput, lastName);
        logger.info("Entered last name: {}", lastName);
        return this;
    }
    
    /**
     * Enters passenger date of birth
     * @param day Day of birth
     * @param month Month of birth
     * @param year Year of birth
     * @return TicketPage instance for chaining
     */
    public TicketPage enterDateOfBirth(String day, String month, String year) {
        type(dobDayInput, day);
        selectByVisibleText(By.cssSelector("[data-testid='dob-month'], select[name='dobMonth']"), month);
        type(dobYearInput, year);
        logger.info("Entered date of birth: {}/{}/{}", day, month, year);
        return this;
    }
    
    /**
     * Selects passenger gender
     * @param gender Gender (Male, Female)
     * @return TicketPage instance for chaining
     */
    public TicketPage selectGender(String gender) {
        selectByVisibleText(By.cssSelector("[data-testid='gender-select'], select[name='gender']"), gender);
        logger.info("Selected gender: {}", gender);
        return this;
    }
    
    /**
     * Selects passenger nationality
     * @param nationality Nationality
     * @return TicketPage instance for chaining
     */
    public TicketPage selectNationality(String nationality) {
        selectByVisibleText(By.cssSelector("[data-testid='nationality-select'], select[name='nationality']"), nationality);
        logger.info("Selected nationality: {}", nationality);
        return this;
    }
    
    /**
     * Enters contact email
     * @param email Email address
     * @return TicketPage instance for chaining
     */
    public TicketPage enterEmail(String email) {
        type(emailInput, email);
        type(confirmEmailInput, email);
        logger.info("Entered email: {}", email);
        return this;
    }
    
    /**
     * Enters contact phone number
     * @param countryCode Country code
     * @param phoneNumber Phone number
     * @return TicketPage instance for chaining
     */
    public TicketPage enterPhone(String countryCode, String phoneNumber) {
        selectByVisibleText(By.cssSelector("[data-testid='country-code'], select[name='countryCode']"), countryCode);
        type(phoneInput, phoneNumber);
        logger.info("Entered phone: {} {}", countryCode, phoneNumber);
        return this;
    }
    
    /**
     * Enters passport details
     * @param passportNumber Passport number
     * @param country Issuing country
     * @param expiryDay Expiry day
     * @param expiryMonth Expiry month
     * @param expiryYear Expiry year
     * @return TicketPage instance for chaining
     */
    public TicketPage enterPassportDetails(String passportNumber, String country, 
                                            String expiryDay, String expiryMonth, String expiryYear) {
        type(passportNumberInput, passportNumber);
        selectByVisibleText(By.cssSelector("[data-testid='passport-country'], select[name='passportCountry']"), country);
        type(passportExpiryDayInput, expiryDay);
        selectByVisibleText(By.cssSelector("[data-testid='passport-expiry-month'], select[name='passportExpiryMonth']"), expiryMonth);
        type(passportExpiryYearInput, expiryYear);
        logger.info("Entered passport details");
        return this;
    }
    
    /**
     * Enters LoyaltyPoints number
     * @param loyaltyPointsNumber LoyaltyPoints number
     * @return TicketPage instance for chaining
     */
    public TicketPage enterLoyaltyPointsNumber(String loyaltyPointsNumber) {
        click(addLoyaltyPointsLink);
        type(loyaltyPointsNumberInput, loyaltyPointsNumber);
        logger.info("Entered LoyaltyPoints number: {}", loyaltyPointsNumber);
        return this;
    }
    
    /**
     * Fills complete passenger details
     * @param title Title
     * @param firstName First name
     * @param lastName Last name
     * @param email Email
     * @param phone Phone number
     * @return TicketPage instance for chaining
     */
    public TicketPage fillPassengerDetails(String title, String firstName, String lastName, 
                                            String email, String phone) {
        selectTitle(title);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPhone("+64", phone);
        return this;
    }
    
    /**
     * Opens seat selection
     * @return TicketPage instance for chaining
     */
    public TicketPage openSeatSelection() {
        click(selectSeatsButton);
        waitForVisible(By.cssSelector("[data-testid='seat-map'], .seat-map"));
        logger.info("Opened seat selection");
        return this;
    }
    
    /**
     * Selects first available seat
     * @return TicketPage instance for chaining
     */
    public TicketPage selectFirstAvailableSeat() {
        if (!availableSeats.isEmpty()) {
            click(availableSeats.get(0));
            logger.info("Selected first available seat");
        }
        return this;
    }
    
    /**
     * Selects a specific seat by row and seat letter
     * @param row Seat row number
     * @param seat Seat letter
     * @return TicketPage instance for chaining
     */
    public TicketPage selectSeat(String row, String seat) {
        String seatId = row + seat;
        By seatLocator = By.cssSelector("[data-seat-id='" + seatId + "'], .seat-" + seatId);
        click(seatLocator);
        logger.info("Selected seat: {}", seatId);
        return this;
    }
    
    /**
     * Confirms seat selection
     * @return TicketPage instance for chaining
     */
    public TicketPage confirmSeatSelection() {
        click(confirmSeatButton);
        logger.info("Confirmed seat selection");
        return this;
    }
    
    /**
     * Adds extra baggage
     * @return TicketPage instance for chaining
     */
    public TicketPage addExtraBaggage() {
        click(addBaggageButton);
        waitForVisible(By.cssSelector("[data-testid='baggage-options'], .baggage-options"));
        click(extraBag23kg);
        logger.info("Added extra 23kg baggage");
        return this;
    }
    
    /**
     * Opens meal selection
     * @return TicketPage instance for chaining
     */
    public TicketPage openMealSelection() {
        click(addMealButton);
        waitForVisible(By.cssSelector("[data-testid='meal-options'], .meal-options"));
        logger.info("Opened meal selection");
        return this;
    }
    
    /**
     * Adds travel insurance
     * @return TicketPage instance for chaining
     */
    public TicketPage addTravelInsurance() {
        click(addInsuranceButton);
        logger.info("Added travel insurance");
        return this;
    }
    
    /**
     * Accepts terms and conditions
     * @return TicketPage instance for chaining
     */
    public TicketPage acceptTermsAndConditions() {
        if (!isSelected(By.cssSelector("[data-testid='terms-checkbox'], input[name='acceptTerms']"))) {
            click(termsCheckbox);
        }
        if (!isSelected(By.cssSelector("[data-testid='privacy-checkbox'], input[name='acceptPrivacy']"))) {
            click(privacyCheckbox);
        }
        logger.info("Accepted terms and conditions");
        return this;
    }
    
    /**
     * Gets the total amount
     * @return Total amount as string
     */
    public String getTotalAmount() {
        return getText(totalAmount);
    }
    
    /**
     * Gets flight summary text
     * @return Flight summary
     */
    public String getFlightSummary() {
        return getText(flightSummary);
    }
    
    /**
     * Gets passenger summary text
     * @return Passenger summary
     */
    public String getPassengerSummary() {
        return getText(passengerSummary);
    }
    
    /**
     * Checks if there are validation errors
     * @return true if errors exist
     */
    public boolean hasValidationErrors() {
        return !fieldErrors.isEmpty() || isDisplayed(By.cssSelector("[data-testid='error-message'], .error-message"));
    }
    
    /**
     * Gets validation error messages
     * @return List of error messages
     */
    public List<String> getValidationErrors() {
        return fieldErrors.stream()
                .map(WebElement::getText)
                .toList();
    }
    
    /**
     * Clicks continue to payment
     * @return TicketPage (for payment flow)
     */
    public TicketPage continueToPayment() {
        scrollToElement(continueToPaymentButton);
        click(continueToPaymentButton);
        waitForPageLoad();
        logger.info("Clicked continue to payment");
        return this;
    }
    
    /**
     * Clicks back to flights
     * @return BookingPage instance
     */
    public BookingPage backToFlights() {
        click(backToFlightsButton);
        waitForPageLoad();
        logger.info("Clicked back to flights");
        return new BookingPage(driver);
    }
    
    /**
     * Saves and continues
     * @return TicketPage instance for chaining
     */
    public TicketPage saveAndContinue() {
        click(saveAndContinueButton);
        waitForPageLoad();
        logger.info("Clicked save and continue");
        return this;
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            waitForPageLoad();
            return isDisplayed(By.cssSelector("[data-testid='passenger-form'], .passenger-form")) ||
                   isDisplayed(By.cssSelector("[data-testid='first-name'], input[name='firstName']")) ||
                   getCurrentUrl().contains("passenger") || getCurrentUrl().contains("traveller");
        } catch (Exception e) {
            return false;
        }
    }
}
