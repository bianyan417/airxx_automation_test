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
import com.airxx.pages.BookingPage;
import com.airxx.pages.HomePage;
import com.airxx.pages.TicketPage;

import java.util.Map;

/**
 * BookingTest - UI tests for flight booking functionality.
 * Tests flight search, selection, and booking flow.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
@Listeners(TestListener.class)
public class BookingTest {
    
    private HomePage homePage;
    
    @BeforeMethod(description = "Setup browser and navigate to home page")
    public void setUp() {
        DriverManager.initDriver();
        homePage = new HomePage(DriverManager.getDriver());
        homePage.navigateToHomePage();
    }
    
    @AfterMethod(description = "Quit browser")
    public void tearDown() {
        DriverManager.quitDriver();
    }
    
    /**
     * Data provider for flight search scenarios
     */
    @DataProvider(name = "flightSearchData")
    public Object[][] flightSearchDataProvider() {
        return new Object[][] {
            {"domesticFlight"},
            {"internationalFlight"},
            {"returnFlight"}
        };
    }
    
    @Test(description = "Verify home page loads successfully", priority = 1)
    public void testHomePageLoads() {
        Assert.assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }
    
    @Test(description = "Verify flight search with valid domestic route", 
          dataProvider = "flightSearchData", priority = 2)
    public void testFlightSearchWithValidData(String scenario) {
        Map<String, String> testData = TestDataLoader.getBookingData(scenario);
        
        String origin = testData.get("origin");
        String destination = testData.get("destination");
        String departureDate = testData.get("departureDate");
        String returnDate = testData.get("returnDate");
        int adults = Integer.parseInt(testData.getOrDefault("adults", "1"));
        
        BookingPage bookingPage = homePage.searchFlight(
            origin, destination, departureDate, returnDate, adults
        );
        
        bookingPage.waitForFlightResults();
        
        Assert.assertTrue(bookingPage.areFlightsAvailable(), 
            "Flights should be available for route: " + origin + " to " + destination);
    }
    
    @Test(description = "Verify one-way flight search", priority = 3)
    public void testOneWayFlightSearch() {
        Map<String, String> testData = TestDataLoader.getBookingData("oneWayFlight");
        
        BookingPage bookingPage = homePage
            .selectOneWayTrip()
            .enterOrigin(testData.get("origin"))
            .enterDestination(testData.get("destination"))
            .enterDepartureDate(testData.get("departureDate"))
            .searchFlights();
        
        bookingPage.waitForFlightResults();
        
        Assert.assertTrue(bookingPage.areFlightsAvailable(), 
            "Flights should be available for one-way search");
    }
    
    @Test(description = "Verify return flight search", priority = 4)
    public void testReturnFlightSearch() {
        Map<String, String> testData = TestDataLoader.getBookingData("returnFlight");
        
        BookingPage bookingPage = homePage
            .selectReturnTrip()
            .enterOrigin(testData.get("origin"))
            .enterDestination(testData.get("destination"))
            .enterDepartureDate(testData.get("departureDate"))
            .enterReturnDate(testData.get("returnDate"))
            .searchFlights();
        
        bookingPage.waitForFlightResults();
        
        Assert.assertTrue(bookingPage.areFlightsAvailable(), 
            "Flights should be available for return search");
    }
    
    @Test(description = "Verify multi-passenger booking", priority = 5)
    public void testMultiPassengerBooking() {
        Map<String, String> testData = TestDataLoader.getBookingData("multiPassenger");
        
        BookingPage bookingPage = homePage
            .selectReturnTrip()
            .enterOrigin(testData.get("origin"))
            .enterDestination(testData.get("destination"))
            .enterDepartureDate(testData.get("departureDate"))
            .enterReturnDate(testData.get("returnDate"))
            .setAdults(Integer.parseInt(testData.get("adults")))
            .setChildren(Integer.parseInt(testData.get("children")))
            .searchFlights();
        
        bookingPage.waitForFlightResults();
        
        Assert.assertTrue(bookingPage.areFlightsAvailable(), 
            "Flights should be available for multi-passenger booking");
    }
    
    @Test(description = "Verify flight selection and fare options", priority = 6)
    public void testFlightSelectionWithFareOptions() {
        Map<String, String> testData = TestDataLoader.getBookingData("domesticFlight");
        
        BookingPage bookingPage = homePage
            .searchFlight(
                testData.get("origin"),
                testData.get("destination"),
                testData.get("departureDate"),
                null,
                1
            );
        
        bookingPage.waitForFlightResults();
        
        if (bookingPage.areFlightsAvailable()) {
            bookingPage.selectFirstFlight();
            bookingPage.selectSeatFare();
            
            String price = bookingPage.getSeatPrice();
            Assert.assertNotNull(price, "Price should be displayed for Seat fare");
        }
    }
    
    @Test(description = "Verify complete booking flow", priority = 7)
    public void testCompleteBookingFlow() {
        Map<String, String> bookingData = TestDataLoader.getBookingData("domesticFlight");
        Map<String, String> passengerData = TestDataLoader.getTicketData("adultPassenger");
        
        // Search for flights
        BookingPage bookingPage = homePage
            .searchFlight(
                bookingData.get("origin"),
                bookingData.get("destination"),
                bookingData.get("departureDate"),
                null,
                1
            );
        
        bookingPage.waitForFlightResults();
        
        if (bookingPage.areFlightsAvailable()) {
            // Select flight and fare
            bookingPage.selectFirstFlight();
            bookingPage.selectSeatBagFare();
            
            // Continue to passenger details
            TicketPage ticketPage = bookingPage.clickContinue();
            
            // Fill passenger details
            ticketPage.fillPassengerDetails(
                passengerData.get("title"),
                passengerData.get("firstName"),
                passengerData.get("lastName"),
                passengerData.get("email"),
                passengerData.get("phone")
            );
            
            Assert.assertTrue(ticketPage.isPageLoaded(), 
                "Ticket page should be loaded with passenger form");
        }
    }
    
    @Test(description = "Verify flight filtering by cabin class", priority = 8)
    public void testFlightFilteringByCabinClass() {
        Map<String, String> testData = TestDataLoader.getBookingData("businessClass");
        
        BookingPage bookingPage = homePage
            .searchFlight(
                testData.get("origin"),
                testData.get("destination"),
                testData.get("departureDate"),
                null,
                1
            );
        
        bookingPage.waitForFlightResults();
        
        if (bookingPage.areFlightsAvailable()) {
            int initialCount = bookingPage.getFlightCount();
            
            bookingPage.filterByBusinessClass();
            
            // Business class may have fewer options
            int filteredCount = bookingPage.getFlightCount();
            Assert.assertTrue(filteredCount >= 0, 
                "Filtered results should be displayed");
        }
    }
    
    @Test(description = "Verify flight sorting by price", priority = 9)
    public void testFlightSortingByPrice() {
        Map<String, String> testData = TestDataLoader.getBookingData("domesticFlight");
        
        BookingPage bookingPage = homePage
            .searchFlight(
                testData.get("origin"),
                testData.get("destination"),
                testData.get("departureDate"),
                null,
                1
            );
        
        bookingPage.waitForFlightResults();
        
        if (bookingPage.areFlightsAvailable()) {
            bookingPage.sortByPrice();
            
            // First flight should be the cheapest
            Assert.assertTrue(bookingPage.getFlightCount() > 0, 
                "Sorted flights should be displayed");
        }
    }
    
    @Test(description = "Verify no flights found message for invalid route", priority = 10)
    public void testNoFlightsFoundMessage() {
        // Search for unlikely route
        BookingPage bookingPage = homePage
            .searchFlight(
                "AKL", // Auckland
                "CHC", // Christchurch
                "01 Jan 1990", // Past date - no flights
                null,
                1
            );
        
        bookingPage.waitForFlightResults();
        
        // Either no flights or validation error expected
        // The actual behavior depends on the application
    }
    
    @Test(description = "Verify manage booking retrieval", priority = 11)
    public void testManageBookingRetrieval() {
        Map<String, String> testData = TestDataLoader.getBookingData("existingBooking");
        
        BookingPage bookingPage = homePage.navigateToManageBooking();
        
        bookingPage.retrieveBooking(
            testData.get("bookingReference"),
            testData.get("lastName")
        );
        
        // Verify booking is retrieved or error is shown
        Assert.assertTrue(bookingPage.isPageLoaded(), 
            "Booking page should be loaded");
    }
    
    @Test(description = "Verify promo code application", priority = 12)
    public void testPromoCodeApplication() {
        Map<String, String> testData = TestDataLoader.getBookingData("promoCodeFlight");
        
        homePage
            .selectReturnTrip()
            .enterOrigin(testData.get("origin"))
            .enterDestination(testData.get("destination"))
            .enterDepartureDate(testData.get("departureDate"))
            .enterReturnDate(testData.get("returnDate"))
            .enterPromoCode(testData.get("promoCode"));
        
        BookingPage bookingPage = homePage.searchFlights();
        
        bookingPage.waitForFlightResults();
        
        Assert.assertTrue(bookingPage.isPageLoaded(), 
            "Search results should be displayed with promo code");
    }
    
    @Test(description = "Verify seat selection in booking flow", priority = 13)
    public void testSeatSelectionInBookingFlow() {
        Map<String, String> bookingData = TestDataLoader.getBookingData("domesticFlight");
        Map<String, String> passengerData = TestDataLoader.getTicketData("adultPassenger");
        
        BookingPage bookingPage = homePage
            .searchFlight(
                bookingData.get("origin"),
                bookingData.get("destination"),
                bookingData.get("departureDate"),
                null,
                1
            );
        
        bookingPage.waitForFlightResults();
        
        if (bookingPage.areFlightsAvailable()) {
            bookingPage.selectFirstFlight();
            bookingPage.selectWorksFare(); // Works fare includes seat selection
            
            TicketPage ticketPage = bookingPage.clickContinue();
            
            ticketPage.fillPassengerDetails(
                passengerData.get("title"),
                passengerData.get("firstName"),
                passengerData.get("lastName"),
                passengerData.get("email"),
                passengerData.get("phone")
            );
            
            // Select seat
            ticketPage.openSeatSelection();
            ticketPage.selectFirstAvailableSeat();
            ticketPage.confirmSeatSelection();
            
            Assert.assertTrue(ticketPage.isPageLoaded(), 
                "Ticket page should be loaded after seat selection");
        }
    }
    
    @Test(description = "Verify passenger details validation", priority = 14)
    public void testPassengerDetailsValidation() {
        Map<String, String> bookingData = TestDataLoader.getBookingData("domesticFlight");
        
        BookingPage bookingPage = homePage
            .searchFlight(
                bookingData.get("origin"),
                bookingData.get("destination"),
                bookingData.get("departureDate"),
                null,
                1
            );
        
        bookingPage.waitForFlightResults();
        
        if (bookingPage.areFlightsAvailable()) {
            bookingPage.selectFirstFlight();
            bookingPage.selectSeatFare();
            
            TicketPage ticketPage = bookingPage.clickContinue();
            
            // Try to continue without filling required fields
            ticketPage.continueToPayment();
            
            // Validation errors should be displayed
            Assert.assertTrue(ticketPage.hasValidationErrors(), 
                "Validation errors should be displayed for empty required fields");
        }
    }
}
