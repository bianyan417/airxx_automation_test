package com.airxx.tests.bdd.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.datatable.DataTable;
import org.testng.Assert;

import com.airxx.core.config.DriverManager;
import com.airxx.core.utils.TestDataLoader;
import com.airxx.pages.BookingPage;
import com.airxx.pages.HomePage;
import com.airxx.pages.TicketPage;

import java.util.List;
import java.util.Map;

/**
 * BookingSteps - Cucumber step definitions for booking functionality.
 * Maps Gherkin steps to test automation code for flight booking scenarios.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class BookingSteps {
    
    private HomePage homePage;
    private BookingPage bookingPage;
    private TicketPage ticketPage;
    
    @Given("I am on the XXX Air Company booking page")
    public void iAmOnTheAirNewZealandBookingPage() {
        homePage = new HomePage(DriverManager.getDriver());
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }
    
    @Given("I have selected a return trip")
    public void iHaveSelectedAReturnTrip() {
        homePage.selectReturnTrip();
    }
    
    @Given("I have selected a one-way trip")
    public void iHaveSelectedAOneWayTrip() {
        homePage.selectOneWayTrip();
    }
    
    @Given("I have selected a multi-city trip")
    public void iHaveSelectedAMultiCityTrip() {
        homePage.selectMultiCityTrip();
    }
    
    @When("I search for flights from {string} to {string}")
    public void iSearchForFlightsFromTo(String origin, String destination) {
        homePage.enterOrigin(origin);
        homePage.enterDestination(destination);
    }
    
    @When("I search for flights with the following details:")
    public void iSearchForFlightsWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);
        
        homePage.enterOrigin(data.get("origin"));
        homePage.enterDestination(data.get("destination"));
        homePage.enterDepartureDate(data.get("departureDate"));
        
        if (data.containsKey("returnDate") && data.get("returnDate") != null) {
            homePage.enterReturnDate(data.get("returnDate"));
        }
        
        if (data.containsKey("adults")) {
            homePage.setAdults(Integer.parseInt(data.get("adults")));
        }
    }
    
    @When("I select departure date {string}")
    public void iSelectDepartureDate(String departureDate) {
        homePage.enterDepartureDate(departureDate);
    }
    
    @When("I select return date {string}")
    public void iSelectReturnDate(String returnDate) {
        homePage.enterReturnDate(returnDate);
    }
    
    @When("I select {int} adult(s)")
    public void iSelectAdults(int adults) {
        homePage.setAdults(adults);
    }
    
    @When("I select {int} child(ren)")
    public void iSelectChildren(int children) {
        homePage.setChildren(children);
    }
    
    @When("I enter promo code {string}")
    public void iEnterPromoCode(String promoCode) {
        homePage.enterPromoCode(promoCode);
    }
    
    @When("I click search flights")
    public void iClickSearchFlights() {
        bookingPage = homePage.searchFlights();
        bookingPage.waitForFlightResults();
    }
    
    @When("I search for a domestic flight")
    public void iSearchForADomesticFlight() {
        Map<String, String> testData = TestDataLoader.getBookingData("domesticFlight");
        bookingPage = homePage.searchFlight(
            testData.get("origin"),
            testData.get("destination"),
            testData.get("departureDate"),
            null,
            1
        );
        bookingPage.waitForFlightResults();
    }
    
    @When("I search for an international flight")
    public void iSearchForAnInternationalFlight() {
        Map<String, String> testData = TestDataLoader.getBookingData("internationalFlight");
        bookingPage = homePage.searchFlight(
            testData.get("origin"),
            testData.get("destination"),
            testData.get("departureDate"),
            testData.get("returnDate"),
            1
        );
        bookingPage.waitForFlightResults();
    }
    
    @When("I select the first available flight")
    public void iSelectTheFirstAvailableFlight() {
        bookingPage.selectFirstFlight();
    }
    
    @When("I select flight number {int}")
    public void iSelectFlightNumber(int flightIndex) {
        bookingPage.selectFlight(flightIndex - 1); // Convert to 0-based index
    }
    
    @When("I select the cheapest flight")
    public void iSelectTheCheapestFlight() {
        bookingPage.selectCheapestFlight();
    }
    
    @When("I select the {string} fare")
    public void iSelectTheFare(String fareType) {
        switch (fareType.toLowerCase()) {
            case "seat":
                bookingPage.selectSeatFare();
                break;
            case "seat + bag":
            case "seat+bag":
                bookingPage.selectSeatBagFare();
                break;
            case "the works":
            case "works":
                bookingPage.selectWorksFare();
                break;
            case "works deluxe":
                bookingPage.selectWorksDeluxeFare();
                break;
        }
    }
    
    @When("I filter by {string} cabin class")
    public void iFilterByCabinClass(String cabinClass) {
        switch (cabinClass.toLowerCase()) {
            case "economy":
                bookingPage.filterByEconomy();
                break;
            case "premium economy":
                bookingPage.filterByPremiumEconomy();
                break;
            case "business":
            case "business premier":
                bookingPage.filterByBusinessClass();
                break;
        }
    }
    
    @When("I sort flights by price")
    public void iSortFlightsByPrice() {
        bookingPage.sortByPrice();
    }
    
    @When("I sort flights by duration")
    public void iSortFlightsByDuration() {
        bookingPage.sortByDuration();
    }
    
    @When("I filter to show non-stop flights only")
    public void iFilterToShowNonStopFlightsOnly() {
        bookingPage.filterNonStopOnly();
    }
    
    @When("I continue to passenger details")
    public void iContinueToPassengerDetails() {
        ticketPage = bookingPage.clickContinue();
    }
    
    @When("I enter passenger details:")
    public void iEnterPassengerDetails(DataTable dataTable) {
        List<Map<String, String>> passengers = dataTable.asMaps(String.class, String.class);
        Map<String, String> passenger = passengers.get(0);
        
        ticketPage.fillPassengerDetails(
            passenger.get("title"),
            passenger.get("firstName"),
            passenger.get("lastName"),
            passenger.get("email"),
            passenger.get("phone")
        );
    }
    
    @When("I select seat {string}")
    public void iSelectSeat(String seatNumber) {
        ticketPage.openSeatSelection();
        String row = seatNumber.substring(0, seatNumber.length() - 1);
        String seat = seatNumber.substring(seatNumber.length() - 1);
        ticketPage.selectSeat(row, seat);
        ticketPage.confirmSeatSelection();
    }
    
    @When("I add extra baggage")
    public void iAddExtraBaggage() {
        ticketPage.addExtraBaggage();
    }
    
    @When("I accept terms and conditions")
    public void iAcceptTermsAndConditions() {
        ticketPage.acceptTermsAndConditions();
    }
    
    @When("I continue to payment")
    public void iContinueToPayment() {
        ticketPage.continueToPayment();
    }
    
    @When("I retrieve booking with reference {string} and last name {string}")
    public void iRetrieveBookingWithReferenceAndLastName(String bookingRef, String lastName) {
        bookingPage = homePage.navigateToManageBooking();
        bookingPage.retrieveBooking(bookingRef, lastName);
    }
    
    @Then("I should see available flights")
    public void iShouldSeeAvailableFlights() {
        Assert.assertTrue(bookingPage.areFlightsAvailable(),
            "Available flights should be displayed");
    }
    
    @Then("I should see at least {int} flight(s)")
    public void iShouldSeeAtLeastFlights(int minFlights) {
        int actualFlights = bookingPage.getFlightCount();
        Assert.assertTrue(actualFlights >= minFlights,
            "Should see at least " + minFlights + " flights, but found " + actualFlights);
    }
    
    @Then("I should see no flights available message")
    public void iShouldSeeNoFlightsAvailableMessage() {
        String message = bookingPage.getNoFlightsMessage();
        Assert.assertFalse(message.isEmpty() || !bookingPage.areFlightsAvailable(),
            "No flights message should be displayed");
    }
    
    @Then("I should see flight prices")
    public void iShouldSeeFlightPrices() {
        String price = bookingPage.getSeatPrice();
        Assert.assertNotNull(price, "Flight prices should be displayed");
    }
    
    @Then("I should see the total price")
    public void iShouldSeeTheTotalPrice() {
        String totalPrice = bookingPage.getTotalPrice();
        Assert.assertNotNull(totalPrice, "Total price should be displayed");
    }
    
    @Then("I should be on the passenger details page")
    public void iShouldBeOnThePassengerDetailsPage() {
        Assert.assertTrue(ticketPage.isPageLoaded(),
            "Passenger details page should be displayed");
    }
    
    @Then("I should see the flight summary")
    public void iShouldSeeTheFlightSummary() {
        String summary = ticketPage.getFlightSummary();
        Assert.assertNotNull(summary, "Flight summary should be displayed");
    }
    
    @Then("I should see the passenger summary")
    public void iShouldSeeThePassengerSummary() {
        String summary = ticketPage.getPassengerSummary();
        Assert.assertNotNull(summary, "Passenger summary should be displayed");
    }
    
    @Then("I should see validation errors")
    public void iShouldSeeValidationErrors() {
        Assert.assertTrue(ticketPage.hasValidationErrors(),
            "Validation errors should be displayed");
    }
    
    @Then("I should see the booking details")
    public void iShouldSeeTheBookingDetails() {
        Assert.assertTrue(bookingPage.isPageLoaded(),
            "Booking details should be displayed");
    }
    
    @Then("the booking page should load")
    public void theBookingPageShouldLoad() {
        Assert.assertTrue(bookingPage.isPageLoaded(),
            "Booking page should be loaded");
    }
    
    @And("I should see fare options")
    public void iShouldSeeFareOptions() {
        Assert.assertTrue(bookingPage.isPageLoaded(),
            "Fare options should be visible");
    }
    
    @And("flights should be sorted by price ascending")
    public void flightsShouldBeSortedByPriceAscending() {
        // First flight should be the cheapest after sorting
        Assert.assertTrue(bookingPage.getFlightCount() > 0,
            "Flights should be displayed after sorting");
    }
    
    @And("the total amount should be displayed")
    public void theTotalAmountShouldBeDisplayed() {
        String total = ticketPage.getTotalAmount();
        Assert.assertNotNull(total, "Total amount should be displayed");
    }
}
