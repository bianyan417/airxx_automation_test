package com.airxx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.airxx.core.config.ConfigManager;

/**
 * HomePage - Page Object for XXX Air Company Home Page.
 * Contains elements and actions for the main landing page including
 * flight search functionality.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class HomePage extends BasePage {
    
    // ===================== PAGE ELEMENTS =====================
    
    // Header Elements
    @FindBy(css = "[data-testid='airnz-logo']")
    private WebElement airNzLogo;
    
    @FindBy(css = "[data-testid='login-button'], .login-btn, a[href*='login']")
    private WebElement loginButton;
    
    @FindBy(css = "[data-testid='user-menu'], .user-menu")
    private WebElement userMenu;
    
    // Flight Search Form Elements
    @FindBy(css = "[data-testid='trip-type-return'], input[value='return']")
    private WebElement returnTripRadio;
    
    @FindBy(css = "[data-testid='trip-type-oneway'], input[value='oneway']")
    private WebElement oneWayTripRadio;
    
    @FindBy(css = "[data-testid='trip-type-multi'], input[value='multi']")
    private WebElement multiCityTripRadio;
    
    @FindBy(css = "[data-testid='origin-input'], input[name='origin'], #origin")
    private WebElement originInput;
    
    @FindBy(css = "[data-testid='destination-input'], input[name='destination'], #destination")
    private WebElement destinationInput;
    
    @FindBy(css = "[data-testid='departure-date'], input[name='departureDate']")
    private WebElement departureDateInput;
    
    @FindBy(css = "[data-testid='return-date'], input[name='returnDate']")
    private WebElement returnDateInput;
    
    @FindBy(css = "[data-testid='passengers-selector'], .passenger-selector")
    private WebElement passengersSelector;
    
    @FindBy(css = "[data-testid='adults-count'], .adults-count")
    private WebElement adultsCount;
    
    @FindBy(css = "[data-testid='children-count'], .children-count")
    private WebElement childrenCount;
    
    @FindBy(css = "[data-testid='infants-count'], .infants-count")
    private WebElement infantsCount;
    
    @FindBy(css = "[data-testid='adults-increase'], .adults-increase")
    private WebElement adultsIncreaseBtn;
    
    @FindBy(css = "[data-testid='adults-decrease'], .adults-decrease")
    private WebElement adultsDecreaseBtn;
    
    @FindBy(css = "[data-testid='children-increase'], .children-increase")
    private WebElement childrenIncreaseBtn;
    
    @FindBy(css = "[data-testid='search-flights-btn'], button[type='submit'], .search-flights")
    private WebElement searchFlightsButton;
    
    // Promo Code
    @FindBy(css = "[data-testid='promo-code-link'], .promo-code-link")
    private WebElement promoCodeLink;
    
    @FindBy(css = "[data-testid='promo-code-input'], input[name='promoCode']")
    private WebElement promoCodeInput;
    
    // Navigation Links
    @FindBy(css = "[data-testid='book-nav'], a[href*='book']")
    private WebElement bookNav;
    
    @FindBy(css = "[data-testid='manage-nav'], a[href*='manage']")
    private WebElement manageBookingNav;
    
    @FindBy(css = "[data-testid='checkin-nav'], a[href*='checkin']")
    private WebElement checkInNav;
    
    @FindBy(css = "[data-testid='airpoints-nav'], a[href*='airpoints']")
    private WebElement airpointsNav;
    
    // Cookie Banner
    @FindBy(css = "[data-testid='cookie-accept'], .cookie-accept, #onetrust-accept-btn-handler")
    private WebElement acceptCookiesButton;
    
    // Locators
    private final By originSuggestionsLocator = By.cssSelector("[data-testid='origin-suggestions'] li, .origin-suggestions li");
    private final By destinationSuggestionsLocator = By.cssSelector("[data-testid='destination-suggestions'] li, .destination-suggestions li");
    private final By loadingSpinnerLocator = By.cssSelector(".loading-spinner, [data-testid='loading']");
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    // ===================== PAGE ACTIONS =====================
    
    /**
     * Navigates to XXX Air Company home page
     * @return HomePage instance for chaining
     */
    public HomePage navigateToHomePage() {
        String baseUrl = ConfigManager.getInstance().getBaseUrl();
        driver.get(baseUrl);
        waitForPageLoad();
        handleCookieBanner();
        logger.info("Navigated to XXX Air Company home page");
        return this;
    }
    
    /**
     * Handles cookie consent banner if present
     */
    public void handleCookieBanner() {
        try {
            if (isDisplayed(By.cssSelector("[data-testid='cookie-accept'], .cookie-accept, #onetrust-accept-btn-handler"))) {
                click(acceptCookiesButton);
                logger.info("Accepted cookie consent");
            }
        } catch (Exception e) {
            logger.debug("Cookie banner not present or already dismissed");
        }
    }
    
    /**
     * Clicks on login button
     * @return LoginPage instance
     */
    public LoginPage clickLogin() {
        click(loginButton);
        logger.info("Clicked login button");
        return new LoginPage(driver);
    }
    
    /**
     * Selects return trip type
     * @return HomePage instance for chaining
     */
    public HomePage selectReturnTrip() {
        click(returnTripRadio);
        logger.info("Selected return trip");
        return this;
    }
    
    /**
     * Selects one-way trip type
     * @return HomePage instance for chaining
     */
    public HomePage selectOneWayTrip() {
        click(oneWayTripRadio);
        logger.info("Selected one-way trip");
        return this;
    }
    
    /**
     * Selects multi-city trip type
     * @return HomePage instance for chaining
     */
    public HomePage selectMultiCityTrip() {
        click(multiCityTripRadio);
        logger.info("Selected multi-city trip");
        return this;
    }
    
    /**
     * Enters origin airport
     * @param origin Origin airport code or name
     * @return HomePage instance for chaining
     */
    public HomePage enterOrigin(String origin) {
        click(originInput);
        type(originInput, origin);
        waitForSuggestions(originSuggestionsLocator);
        selectFirstSuggestion(originSuggestionsLocator);
        logger.info("Entered origin: {}", origin);
        return this;
    }
    
    /**
     * Enters destination airport
     * @param destination Destination airport code or name
     * @return HomePage instance for chaining
     */
    public HomePage enterDestination(String destination) {
        click(destinationInput);
        type(destinationInput, destination);
        waitForSuggestions(destinationSuggestionsLocator);
        selectFirstSuggestion(destinationSuggestionsLocator);
        logger.info("Entered destination: {}", destination);
        return this;
    }
    
    /**
     * Waits for autocomplete suggestions to appear
     * @param locator Suggestions locator
     */
    private void waitForSuggestions(By locator) {
        waitForVisible(locator);
    }
    
    /**
     * Selects first suggestion from autocomplete
     * @param locator Suggestions locator
     */
    private void selectFirstSuggestion(By locator) {
        WebElement firstSuggestion = findElements(locator).get(0);
        click(firstSuggestion);
    }
    
    /**
     * Enters departure date
     * @param date Departure date in format "dd MMM yyyy"
     * @return HomePage instance for chaining
     */
    public HomePage enterDepartureDate(String date) {
        click(departureDateInput);
        type(departureDateInput, date);
        logger.info("Entered departure date: {}", date);
        return this;
    }
    
    /**
     * Enters return date
     * @param date Return date in format "dd MMM yyyy"
     * @return HomePage instance for chaining
     */
    public HomePage enterReturnDate(String date) {
        click(returnDateInput);
        type(returnDateInput, date);
        logger.info("Entered return date: {}", date);
        return this;
    }
    
    /**
     * Opens passenger selector
     * @return HomePage instance for chaining
     */
    public HomePage openPassengerSelector() {
        click(passengersSelector);
        logger.info("Opened passenger selector");
        return this;
    }
    
    /**
     * Sets number of adult passengers
     * @param count Number of adults
     * @return HomePage instance for chaining
     */
    public HomePage setAdults(int count) {
        openPassengerSelector();
        // Assuming default is 1 adult
        for (int i = 1; i < count; i++) {
            click(adultsIncreaseBtn);
        }
        logger.info("Set adults count to: {}", count);
        return this;
    }
    
    /**
     * Adds children to the booking
     * @param count Number of children
     * @return HomePage instance for chaining
     */
    public HomePage setChildren(int count) {
        for (int i = 0; i < count; i++) {
            click(childrenIncreaseBtn);
        }
        logger.info("Set children count to: {}", count);
        return this;
    }
    
    /**
     * Enters promo code
     * @param promoCode Promo code
     * @return HomePage instance for chaining
     */
    public HomePage enterPromoCode(String promoCode) {
        click(promoCodeLink);
        type(promoCodeInput, promoCode);
        logger.info("Entered promo code: {}", promoCode);
        return this;
    }
    
    /**
     * Clicks search flights button
     * @return BookingPage instance
     */
    public BookingPage searchFlights() {
        scrollToElement(searchFlightsButton);
        click(searchFlightsButton);
        waitForPageLoad();
        logger.info("Clicked search flights button");
        return new BookingPage(driver);
    }
    
    /**
     * Performs complete flight search
     * @param origin Origin airport
     * @param destination Destination airport
     * @param departureDate Departure date
     * @param returnDate Return date (null for one-way)
     * @param adults Number of adults
     * @return BookingPage instance
     */
    public BookingPage searchFlight(String origin, String destination, 
                                     String departureDate, String returnDate, int adults) {
        if (returnDate != null) {
            selectReturnTrip();
        } else {
            selectOneWayTrip();
        }
        
        enterOrigin(origin);
        enterDestination(destination);
        enterDepartureDate(departureDate);
        
        if (returnDate != null) {
            enterReturnDate(returnDate);
        }
        
        if (adults > 1) {
            setAdults(adults);
        }
        
        return searchFlights();
    }
    
    /**
     * Navigates to Manage Booking section
     * @return BookingPage for managing bookings
     */
    public BookingPage navigateToManageBooking() {
        click(manageBookingNav);
        waitForPageLoad();
        logger.info("Navigated to Manage Booking");
        return new BookingPage(driver);
    }
    
    /**
     * Navigates to Check-In section
     */
    public void navigateToCheckIn() {
        click(checkInNav);
        waitForPageLoad();
        logger.info("Navigated to Check-In");
    }
    
    /**
     * Navigates to LoyaltyPoints section
     */
    public void navigateToLoyaltyPoints() {
        click(airpointsNav);
        waitForPageLoad();
        logger.info("Navigated to LoyaltyPoints");
    }
    
    /**
     * Gets the displayed origin value
     * @return Origin value
     */
    public String getOriginValue() {
        return getValue(By.cssSelector("[data-testid='origin-input'], input[name='origin']"));
    }
    
    /**
     * Gets the displayed destination value
     * @return Destination value
     */
    public String getDestinationValue() {
        return getValue(By.cssSelector("[data-testid='destination-input'], input[name='destination']"));
    }
    
    /**
     * Checks if user is logged in
     * @return true if user menu is visible
     */
    public boolean isUserLoggedIn() {
        try {
            return isDisplayed(By.cssSelector("[data-testid='user-menu'], .user-menu"));
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            waitForPageLoad();
            return isDisplayed(By.cssSelector("[data-testid='airnz-logo']")) || 
                   isDisplayed(By.cssSelector("[data-testid='search-flights-btn']")) ||
                   getPageTitle().toLowerCase().contains("air new zealand");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets page header text
     * @return Header text
     */
    public String getHeaderText() {
        return getText(By.cssSelector("h1, .hero-heading"));
    }
}
