package com.airxx.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * BookingPage - Page Object for XXX Air Company Flight Booking Page.
 * Handles flight selection, fare options, and booking flow.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class BookingPage extends BasePage {
    
    // ===================== PAGE ELEMENTS =====================
    
    // Flight Results
    @FindBy(css = "[data-testid='flight-results'], .flight-results")
    private WebElement flightResultsContainer;
    
    @FindBy(css = "[data-testid='flight-card'], .flight-card")
    private List<WebElement> flightCards;
    
    @FindBy(css = "[data-testid='outbound-flights'], .outbound-flights")
    private WebElement outboundFlightsSection;
    
    @FindBy(css = "[data-testid='return-flights'], .return-flights")
    private WebElement returnFlightsSection;
    
    @FindBy(css = "[data-testid='no-flights-message'], .no-flights")
    private WebElement noFlightsMessage;
    
    // Flight Details
    @FindBy(css = "[data-testid='departure-time'], .departure-time")
    private List<WebElement> departureTimes;
    
    @FindBy(css = "[data-testid='arrival-time'], .arrival-time")
    private List<WebElement> arrivalTimes;
    
    @FindBy(css = "[data-testid='flight-duration'], .flight-duration")
    private List<WebElement> flightDurations;
    
    @FindBy(css = "[data-testid='flight-number'], .flight-number")
    private List<WebElement> flightNumbers;
    
    @FindBy(css = "[data-testid='stops-info'], .stops-info")
    private List<WebElement> stopsInfo;
    
    // Fare Options
    @FindBy(css = "[data-testid='fare-seat'], .fare-seat")
    private WebElement fareSeat;
    
    @FindBy(css = "[data-testid='fare-seat-bag'], .fare-seat-bag")
    private WebElement fareSeatBag;
    
    @FindBy(css = "[data-testid='fare-works'], .fare-works")
    private WebElement fareWorks;
    
    @FindBy(css = "[data-testid='fare-works-deluxe'], .fare-works-deluxe")
    private WebElement fareWorksDeluxe;
    
    // Fare Prices
    @FindBy(css = "[data-testid='seat-price'], .seat-price")
    private WebElement seatPrice;
    
    @FindBy(css = "[data-testid='seat-bag-price'], .seat-bag-price")
    private WebElement seatBagPrice;
    
    @FindBy(css = "[data-testid='works-price'], .works-price")
    private WebElement worksPrice;
    
    // Cabin Class Filter
    @FindBy(css = "[data-testid='cabin-economy'], .cabin-economy")
    private WebElement cabinEconomy;
    
    @FindBy(css = "[data-testid='cabin-premium'], .cabin-premium")
    private WebElement cabinPremiumEconomy;
    
    @FindBy(css = "[data-testid='cabin-business'], .cabin-business")
    private WebElement cabinBusiness;
    
    // Sort and Filter
    @FindBy(css = "[data-testid='sort-dropdown'], .sort-dropdown")
    private WebElement sortDropdown;
    
    @FindBy(css = "[data-testid='filter-stops'], .filter-stops")
    private WebElement filterStops;
    
    @FindBy(css = "[data-testid='filter-times'], .filter-times")
    private WebElement filterTimes;
    
    // Booking Summary
    @FindBy(css = "[data-testid='total-price'], .total-price")
    private WebElement totalPrice;
    
    @FindBy(css = "[data-testid='price-breakdown'], .price-breakdown")
    private WebElement priceBreakdown;
    
    // Action Buttons
    @FindBy(css = "[data-testid='continue-btn'], .continue-btn, button.continue")
    private WebElement continueButton;
    
    @FindBy(css = "[data-testid='back-btn'], .back-btn")
    private WebElement backButton;
    
    @FindBy(css = "[data-testid='modify-search'], .modify-search")
    private WebElement modifySearchButton;
    
    // Manage Booking Elements
    @FindBy(css = "[data-testid='booking-ref-input'], input[name='bookingRef']")
    private WebElement bookingRefInput;
    
    @FindBy(css = "[data-testid='last-name-input'], input[name='lastName']")
    private WebElement lastNameInput;
    
    @FindBy(css = "[data-testid='find-booking-btn'], .find-booking")
    private WebElement findBookingButton;
    
    // Loading
    @FindBy(css = "[data-testid='loading-spinner'], .loading-spinner")
    private WebElement loadingSpinner;
    
    // Locators
    private final By flightCardLocator = By.cssSelector("[data-testid='flight-card'], .flight-card");
    private final By fareOptionLocator = By.cssSelector("[data-testid='fare-option'], .fare-option");
    private final By selectedFlightLocator = By.cssSelector("[data-testid='selected-flight'], .flight-card.selected");
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public BookingPage(WebDriver driver) {
        super(driver);
    }
    
    // ===================== PAGE ACTIONS =====================
    
    /**
     * Waits for flight results to load
     * @return BookingPage instance for chaining
     */
    public BookingPage waitForFlightResults() {
        waitForPageLoad();
        // Wait for loading spinner to disappear if present
        try {
            if (isDisplayed(By.cssSelector("[data-testid='loading-spinner'], .loading-spinner"))) {
                waitForInvisible(loadingSpinner);
            }
        } catch (Exception e) {
            // Loading spinner may not be present
        }
        logger.info("Flight results loaded");
        return this;
    }
    
    /**
     * Waits for element to become invisible
     * @param element WebElement to wait for
     */
    private void waitForInvisible(WebElement element) {
        wait.until(driver -> !element.isDisplayed());
    }
    
    /**
     * Gets the number of available flights
     * @return Number of flight options
     */
    public int getFlightCount() {
        List<WebElement> flights = findElements(flightCardLocator);
        logger.info("Found {} flights", flights.size());
        return flights.size();
    }
    
    /**
     * Checks if flights are available
     * @return true if flights are found
     */
    public boolean areFlightsAvailable() {
        try {
            return getFlightCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets the "no flights" message if displayed
     * @return No flights message text
     */
    public String getNoFlightsMessage() {
        if (isDisplayed(By.cssSelector("[data-testid='no-flights-message'], .no-flights"))) {
            return getText(noFlightsMessage);
        }
        return "";
    }
    
    /**
     * Selects a flight by index (0-based)
     * @param index Flight index
     * @return BookingPage instance for chaining
     */
    public BookingPage selectFlight(int index) {
        List<WebElement> flights = findElements(flightCardLocator);
        if (index >= 0 && index < flights.size()) {
            WebElement flight = flights.get(index);
            scrollToElement(flight);
            click(flight);
            logger.info("Selected flight at index: {}", index);
        } else {
            throw new IndexOutOfBoundsException("Flight index out of bounds: " + index);
        }
        return this;
    }
    
    /**
     * Selects the first available flight
     * @return BookingPage instance for chaining
     */
    public BookingPage selectFirstFlight() {
        return selectFlight(0);
    }
    
    /**
     * Selects the cheapest flight
     * @return BookingPage instance for chaining
     */
    public BookingPage selectCheapestFlight() {
        // Assuming flights are sorted by price by default or using sort
        sortByPrice();
        return selectFirstFlight();
    }
    
    /**
     * Selects "Seat" fare option
     * @return BookingPage instance for chaining
     */
    public BookingPage selectSeatFare() {
        click(fareSeat);
        logger.info("Selected Seat fare");
        return this;
    }
    
    /**
     * Selects "Seat + Bag" fare option
     * @return BookingPage instance for chaining
     */
    public BookingPage selectSeatBagFare() {
        click(fareSeatBag);
        logger.info("Selected Seat + Bag fare");
        return this;
    }
    
    /**
     * Selects "The Works" fare option
     * @return BookingPage instance for chaining
     */
    public BookingPage selectWorksFare() {
        click(fareWorks);
        logger.info("Selected The Works fare");
        return this;
    }
    
    /**
     * Selects "Works Deluxe" fare option
     * @return BookingPage instance for chaining
     */
    public BookingPage selectWorksDeluxeFare() {
        click(fareWorksDeluxe);
        logger.info("Selected Works Deluxe fare");
        return this;
    }
    
    /**
     * Gets the price for Seat fare
     * @return Price as string
     */
    public String getSeatPrice() {
        return getText(seatPrice);
    }
    
    /**
     * Gets the total booking price
     * @return Total price as string
     */
    public String getTotalPrice() {
        return getText(totalPrice);
    }
    
    /**
     * Filters flights by cabin class - Economy
     * @return BookingPage instance for chaining
     */
    public BookingPage filterByEconomy() {
        click(cabinEconomy);
        waitForFlightResults();
        logger.info("Filtered by Economy class");
        return this;
    }
    
    /**
     * Filters flights by cabin class - Premium Economy
     * @return BookingPage instance for chaining
     */
    public BookingPage filterByPremiumEconomy() {
        click(cabinPremiumEconomy);
        waitForFlightResults();
        logger.info("Filtered by Premium Economy class");
        return this;
    }
    
    /**
     * Filters flights by cabin class - Business Premier
     * @return BookingPage instance for chaining
     */
    public BookingPage filterByBusinessClass() {
        click(cabinBusiness);
        waitForFlightResults();
        logger.info("Filtered by Business class");
        return this;
    }
    
    /**
     * Sorts flights by price
     * @return BookingPage instance for chaining
     */
    public BookingPage sortByPrice() {
        click(sortDropdown);
        click(By.cssSelector("[data-value='price'], .sort-price"));
        waitForFlightResults();
        logger.info("Sorted flights by price");
        return this;
    }
    
    /**
     * Sorts flights by duration
     * @return BookingPage instance for chaining
     */
    public BookingPage sortByDuration() {
        click(sortDropdown);
        click(By.cssSelector("[data-value='duration'], .sort-duration"));
        waitForFlightResults();
        logger.info("Sorted flights by duration");
        return this;
    }
    
    /**
     * Sorts flights by departure time
     * @return BookingPage instance for chaining
     */
    public BookingPage sortByDepartureTime() {
        click(sortDropdown);
        click(By.cssSelector("[data-value='departure'], .sort-departure"));
        waitForFlightResults();
        logger.info("Sorted flights by departure time");
        return this;
    }
    
    /**
     * Filters to show only non-stop flights
     * @return BookingPage instance for chaining
     */
    public BookingPage filterNonStopOnly() {
        click(filterStops);
        click(By.cssSelector("[data-value='nonstop'], .filter-nonstop"));
        waitForFlightResults();
        logger.info("Filtered to non-stop flights only");
        return this;
    }
    
    /**
     * Gets departure time for a specific flight
     * @param index Flight index
     * @return Departure time as string
     */
    public String getFlightDepartureTime(int index) {
        if (index < departureTimes.size()) {
            return getText(departureTimes.get(index));
        }
        return "";
    }
    
    /**
     * Gets arrival time for a specific flight
     * @param index Flight index
     * @return Arrival time as string
     */
    public String getFlightArrivalTime(int index) {
        if (index < arrivalTimes.size()) {
            return getText(arrivalTimes.get(index));
        }
        return "";
    }
    
    /**
     * Gets flight duration for a specific flight
     * @param index Flight index
     * @return Duration as string
     */
    public String getFlightDuration(int index) {
        if (index < flightDurations.size()) {
            return getText(flightDurations.get(index));
        }
        return "";
    }
    
    /**
     * Gets flight number for a specific flight
     * @param index Flight index
     * @return Flight number
     */
    public String getFlightNumber(int index) {
        if (index < flightNumbers.size()) {
            return getText(flightNumbers.get(index));
        }
        return "";
    }
    
    /**
     * Clicks continue to proceed with booking
     * @return TicketPage for passenger details
     */
    public TicketPage clickContinue() {
        scrollToElement(continueButton);
        click(continueButton);
        waitForPageLoad();
        logger.info("Clicked continue button");
        return new TicketPage(driver);
    }
    
    /**
     * Clicks back button
     * @return HomePage instance
     */
    public HomePage clickBack() {
        click(backButton);
        waitForPageLoad();
        logger.info("Clicked back button");
        return new HomePage(driver);
    }
    
    /**
     * Clicks modify search to change flight search
     * @return HomePage instance
     */
    public HomePage modifySearch() {
        click(modifySearchButton);
        logger.info("Clicked modify search");
        return new HomePage(driver);
    }
    
    /**
     * Enters booking reference to find existing booking
     * @param bookingRef Booking reference number
     * @return BookingPage instance for chaining
     */
    public BookingPage enterBookingReference(String bookingRef) {
        type(bookingRefInput, bookingRef);
        logger.info("Entered booking reference: {}", bookingRef);
        return this;
    }
    
    /**
     * Enters last name to find booking
     * @param lastName Passenger last name
     * @return BookingPage instance for chaining
     */
    public BookingPage enterLastName(String lastName) {
        type(lastNameInput, lastName);
        logger.info("Entered last name: {}", lastName);
        return this;
    }
    
    /**
     * Clicks find booking button
     * @return BookingPage instance showing booking details
     */
    public BookingPage findBooking() {
        click(findBookingButton);
        waitForPageLoad();
        logger.info("Clicked find booking");
        return this;
    }
    
    /**
     * Retrieves an existing booking
     * @param bookingRef Booking reference
     * @param lastName Passenger last name
     * @return BookingPage with booking details
     */
    public BookingPage retrieveBooking(String bookingRef, String lastName) {
        enterBookingReference(bookingRef);
        enterLastName(lastName);
        return findBooking();
    }
    
    /**
     * Completes outbound flight selection
     * @param flightIndex Flight index to select
     * @param fareType Fare type to select
     * @return BookingPage for return flight selection
     */
    public BookingPage selectOutboundFlight(int flightIndex, String fareType) {
        selectFlight(flightIndex);
        selectFareByType(fareType);
        return this;
    }
    
    /**
     * Selects fare by type name
     * @param fareType Fare type (seat, seatbag, works, worksdeluxe)
     */
    private void selectFareByType(String fareType) {
        switch (fareType.toLowerCase().replace(" ", "").replace("+", "")) {
            case "seat":
                selectSeatFare();
                break;
            case "seatbag":
                selectSeatBagFare();
                break;
            case "works":
            case "theworks":
                selectWorksFare();
                break;
            case "worksdeluxe":
                selectWorksDeluxeFare();
                break;
            default:
                logger.warn("Unknown fare type: {}, defaulting to Seat", fareType);
                selectSeatFare();
        }
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            waitForPageLoad();
            return isDisplayed(By.cssSelector("[data-testid='flight-results'], .flight-results")) ||
                   isDisplayed(By.cssSelector("[data-testid='booking-ref-input']")) ||
                   getCurrentUrl().contains("book") || getCurrentUrl().contains("flight");
        } catch (Exception e) {
            return false;
        }
    }
}
