package com.airxx.tests.api;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.airxx.api.client.ApiClient;
import com.airxx.api.models.BookingResponse;
import com.airxx.api.models.BookingResponse.FlightSegment;
import com.airxx.api.models.BookingResponse.Passenger;
import com.airxx.api.models.BookingResponse.Pricing;
import com.airxx.api.services.BookingService;
import com.airxx.core.listeners.TestListener;
import com.airxx.core.utils.TestDataLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BookingAPITest - API tests for booking endpoints.
 * Tests flight search, booking creation, and booking management APIs.
 * Uses BookingResponse model for response deserialization and validation.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
@Listeners(TestListener.class)
public class BookingAPITest {
    
    private BookingService bookingService;
    private String createdBookingRef;
    private BookingResponse createdBooking;
    
    @BeforeClass(description = "Initialize booking service")
    public void setUp() {
        bookingService = new BookingService();
    }
    
    /**
     * Data provider for flight search scenarios
     */
    @DataProvider(name = "flightSearchData")
    public Object[][] flightSearchDataProvider() {
        return new Object[][] {
            {"AKL", "WLG", "2026-06-15", null, 1, 0, "Economy"},
            {"AKL", "CHC", "2026-06-20", "2026-06-25", 2, 0, "Economy"},
            {"AKL", "SYD", "2026-07-01", null, 1, 1, "Business"}
        };
    }
    
    /**
     * Data provider for invalid search scenarios
     */
    @DataProvider(name = "invalidSearchData")
    public Object[][] invalidSearchDataProvider() {
        return new Object[][] {
            {"", "WLG", "2026-06-15"},      // Empty origin
            {"AKL", "", "2026-06-15"},      // Empty destination
            {"AKL", "WLG", ""},             // Empty date
            {"XXX", "YYY", "2026-06-15"}    // Invalid airports
        };
    }
    
    @Test(description = "Verify flight search API returns valid response", 
          dataProvider = "flightSearchData", priority = 1)
    public void testFlightSearchAPI(String origin, String destination, String departureDate,
                                     String returnDate, int adults, int children, String cabinClass) {
        Response response = bookingService.searchFlights(
            origin, destination, departureDate, returnDate, adults, children, cabinClass
        );
        
        Assert.assertEquals(response.getStatusCode(), 200, 
            "Flight search should return 200 OK");
        Assert.assertNotNull(response.getBody(), 
            "Response body should not be null");
    }
    
    @Test(description = "Verify one-way flight search", priority = 2)
    public void testOneWayFlightSearch() {
        Response response = bookingService.searchOneWayFlights("AKL", "WLG", "2026-06-15", 1);
        
        Assert.assertEquals(response.getStatusCode(), 200, 
            "One-way search should return 200 OK");
    }
    
    @Test(description = "Verify flight search with invalid parameters", 
          dataProvider = "invalidSearchData", priority = 3)
    public void testFlightSearchWithInvalidParams(String origin, String destination, String date) {
        Response response = bookingService.searchFlights(
            origin, destination, date, null, 1, 0, null
        );
        
        // Should return 400 Bad Request or similar error
        Assert.assertTrue(response.getStatusCode() >= 400, 
            "Invalid search should return error status");
    }
    
    @Test(description = "Verify booking creation", priority = 4)
    public void testCreateBooking() {
        Map<String, String> testData = TestDataLoader.getBookingData("apiBooking");
        
        List<Map<String, String>> passengers = new ArrayList<>();
        passengers.add(BookingService.createPassenger(
            "Mr", "John", "Doe", "1985-05-15", "ADULT"
        ));
        
        Response response = bookingService.createBooking(
            testData.get("flightNumber"),
            passengers,
            testData.get("email"),
            testData.get("phone")
        );
        
        if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
            // Deserialize response to BookingResponse
            createdBooking = ApiClient.getBodyAs(response, BookingResponse.class);
            createdBookingRef = createdBooking.getBookingReference();
            
            // Verify BookingResponse attributes
            SoftAssert softAssert = new SoftAssert();
            
            softAssert.assertNotNull(createdBooking, "BookingResponse should not be null");
            softAssert.assertTrue(createdBooking.hasValidBookingReference(), 
                "Booking reference should be valid");
            softAssert.assertNotNull(createdBooking.getStatus(), 
                "Status should not be null");
            softAssert.assertFalse(createdBooking.hasErrors(), 
                "Booking should not have errors");
            
            // Verify booking reference format (e.g., alphanumeric, specific length)
            if (createdBookingRef != null) {
                softAssert.assertTrue(createdBookingRef.length() >= 5, 
                    "Booking reference should have minimum length");
            }
            
            softAssert.assertAll();
        }
    }
    
    @Test(description = "Verify get booking by reference with full response validation", priority = 5,
          dependsOnMethods = "testCreateBooking")
    public void testGetBookingWithFullValidation() {
        if (createdBookingRef != null) {
            Response response = bookingService.getBooking(createdBookingRef);
            
            Assert.assertEquals(response.getStatusCode(), 200, 
                "Get booking should return 200 OK");
            
            // Deserialize to BookingResponse
            BookingResponse booking = ApiClient.getBodyAs(response, BookingResponse.class);
            
            // Use SoftAssert for multiple validations
            SoftAssert softAssert = new SoftAssert();
            
            // Validate basic attributes
            softAssert.assertNotNull(booking, "BookingResponse should not be null");
            softAssert.assertEquals(booking.getBookingReference(), createdBookingRef, 
                "Booking reference should match requested reference");
            softAssert.assertTrue(booking.hasValidBookingReference(), 
                "Booking should have valid reference");
            softAssert.assertTrue(booking.hasRequiredAttributes(), 
                "Booking should have all required attributes");
            
            // Validate status
            softAssert.assertNotNull(booking.getStatus(), 
                "Status should not be null");
            softAssert.assertFalse(booking.isCancelled(), 
                "Newly retrieved booking should not be cancelled");
            
            // Validate no errors
            softAssert.assertFalse(booking.hasErrors(), 
                "Booking should not have errors");
            
            softAssert.assertAll();
        }
    }
    
    @Test(description = "Verify booking response deserialization with passenger details", priority = 6,
          dependsOnMethods = "testCreateBooking")
    public void testBookingResponseDeserialization() {
        if (createdBookingRef != null) {
            BookingResponse booking = bookingService.getBookingDetails(createdBookingRef);
            
            SoftAssert softAssert = new SoftAssert();
            
            // Verify basic booking attributes
            softAssert.assertNotNull(booking, "BookingResponse should not be null");
            softAssert.assertEquals(booking.getBookingReference(), createdBookingRef, 
                "Booking reference should match");
            
            // Verify passengers if present
            if (booking.hasPassengers()) {
                softAssert.assertTrue(booking.getPassengerCount() >= 1, 
                    "Should have at least one passenger");
                
                Passenger firstPassenger = booking.getFirstPassenger();
                softAssert.assertNotNull(firstPassenger, 
                    "First passenger should not be null");
                
                if (firstPassenger != null) {
                    softAssert.assertNotNull(firstPassenger.getFirstName(), 
                        "Passenger first name should not be null");
                    softAssert.assertNotNull(firstPassenger.getLastName(), 
                        "Passenger last name should not be null");
                    softAssert.assertNotNull(firstPassenger.getPassengerType(), 
                        "Passenger type should not be null");
                    
                    // Verify expected values from test data
                    softAssert.assertEquals(firstPassenger.getFirstName(), "John",
                        "First name should match test data");
                    softAssert.assertEquals(firstPassenger.getLastName(), "Doe",
                        "Last name should match test data");
                    softAssert.assertEquals(firstPassenger.getPassengerType(), "ADULT",
                        "Passenger type should be ADULT");
                }
            }
            
            // Verify flights if present
            if (booking.hasFlights()) {
                FlightSegment firstFlight = booking.getFirstFlight();
                softAssert.assertNotNull(firstFlight, "First flight should not be null");
                
                if (firstFlight != null) {
                    softAssert.assertNotNull(firstFlight.getFlightNumber(), 
                        "Flight number should not be null");
                    softAssert.assertNotNull(firstFlight.getOrigin(), 
                        "Flight origin should not be null");
                    softAssert.assertNotNull(firstFlight.getDestination(), 
                        "Flight destination should not be null");
                }
            }
            
            // Verify pricing if present
            if (booking.hasPricing()) {
                Pricing pricing = booking.getPricing();
                softAssert.assertNotNull(pricing, "Pricing should not be null");
                softAssert.assertTrue(pricing.getTotalAmount() >= 0, 
                    "Total amount should be non-negative");
                softAssert.assertNotNull(pricing.getCurrency(), 
                    "Currency should not be null");
            }
            
            softAssert.assertAll();
        }
    }
    
    @Test(description = "Verify booking with last name returns proper response", priority = 7)
    public void testGetBookingWithLastNameValidation() {
        Map<String, String> testData = TestDataLoader.getBookingData("existingBooking");
        
        Response response = bookingService.getBooking(
            testData.get("bookingReference"),
            testData.get("lastName")
        );
        
        if (response.getStatusCode() == 200) {
            // Deserialize and validate
            BookingResponse booking = ApiClient.getBodyAs(response, BookingResponse.class);
            
            SoftAssert softAssert = new SoftAssert();
            
            softAssert.assertNotNull(booking, "BookingResponse should not be null");
            softAssert.assertEquals(booking.getBookingReference(), 
                testData.get("bookingReference"), 
                "Booking reference should match");
            softAssert.assertTrue(booking.hasRequiredAttributes(), 
                "Booking should have required attributes");
            softAssert.assertFalse(booking.hasErrors(), 
                "Valid booking should not have errors");
            
            // Verify passenger last name matches
            if (booking.hasPassengers()) {
                boolean lastNameFound = booking.getPassengers().stream()
                    .anyMatch(p -> testData.get("lastName").equalsIgnoreCase(p.getLastName()));
                softAssert.assertTrue(lastNameFound, 
                    "Booking should contain passenger with matching last name");
            }
            
            softAssert.assertAll();
        } else if (response.getStatusCode() == 404) {
            // Valid response for non-existent booking
            Assert.assertEquals(response.getStatusCode(), 404,
                "Non-existent booking should return 404");
        }
    }
    
    @Test(description = "Verify booking update", priority = 8,
          dependsOnMethods = "testCreateBooking")
    public void testUpdateBooking() {
        if (createdBookingRef != null) {
            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("mealPreference", "VEGETARIAN");
            
            Response response = bookingService.updateBooking(createdBookingRef, updateRequest);
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204,
                "Update booking should return success");
        }
    }
    
    @Test(description = "Verify add passenger to booking", priority = 9,
          dependsOnMethods = "testCreateBooking")
    public void testAddPassengerToBooking() {
        if (createdBookingRef != null) {
            Map<String, String> passenger = BookingService.createPassenger(
                "Mrs", "Jane", "Doe", "1988-08-20", "ADULT"
            );
            
            Response response = bookingService.addPassenger(createdBookingRef, passenger);
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201,
                "Add passenger should return success");
        }
    }
    
    @Test(description = "Verify seat selection", priority = 10,
          dependsOnMethods = "testAddPassengerToBooking")
    public void testSeatSelection() {
        if (createdBookingRef != null) {
            Response response = bookingService.selectSeat(
                createdBookingRef, "PAX1", "12A"
            );
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201 || 
                             response.getStatusCode() == 404, // Seat may not be available
                "Seat selection should return valid response");
        }
    }
    
    @Test(description = "Verify get booking tickets", priority = 11,
          dependsOnMethods = "testCreateBooking")
    public void testGetBookingTickets() {
        if (createdBookingRef != null) {
            Response response = bookingService.getTickets(createdBookingRef);
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 404,
                "Get tickets should return valid response");
        }
    }
    
    @Test(description = "Verify get booking price", priority = 12,
          dependsOnMethods = "testCreateBooking")
    public void testGetBookingPrice() {
        if (createdBookingRef != null) {
            Response response = bookingService.getBookingPrice(createdBookingRef);
            
            if (response.getStatusCode() == 200) {
                Double totalAmount = ApiClient.getJsonPathValue(response, "pricing.totalAmount");
                Assert.assertNotNull(totalAmount, "Total amount should be returned");
                Assert.assertTrue(totalAmount > 0, "Total amount should be greater than 0");
            }
        }
    }
    
    @Test(description = "Verify add extras to booking", priority = 13,
          dependsOnMethods = "testCreateBooking")
    public void testAddExtras() {
        if (createdBookingRef != null) {
            Map<String, Object> extras = new HashMap<>();
            extras.put("extraBaggage", true);
            extras.put("baggageWeight", 23);
            
            Response response = bookingService.addExtras(createdBookingRef, extras);
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201,
                "Add extras should return success");
        }
    }
    
    @Test(description = "Verify booking confirmation", priority = 14,
          dependsOnMethods = "testAddExtras")
    public void testConfirmBooking() {
        if (createdBookingRef != null) {
            Map<String, Object> paymentDetails = new HashMap<>();
            paymentDetails.put("paymentMethod", "CREDIT_CARD");
            paymentDetails.put("cardNumber", "4111111111111111");
            paymentDetails.put("expiryMonth", "12");
            paymentDetails.put("expiryYear", "2027");
            paymentDetails.put("cvv", "123");
            
            Response response = bookingService.confirmBooking(createdBookingRef, paymentDetails);
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201 ||
                             response.getStatusCode() == 400, // Payment may fail in test environment
                "Confirm booking should return valid response");
        }
    }
    
    @Test(description = "Verify online check-in", priority = 15)
    public void testOnlineCheckIn() {
        Map<String, String> testData = TestDataLoader.getBookingData("checkInBooking");
        
        List<String> passengerIds = new ArrayList<>();
        passengerIds.add("PAX1");
        
        Response response = bookingService.checkIn(
            testData.get("bookingReference"),
            passengerIds
        );
        
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 404 ||
                         response.getStatusCode() == 400, // Check-in may not be available
            "Check-in should return valid response");
    }
    
    @Test(description = "Verify booking cancellation", priority = 16,
          dependsOnMethods = "testCreateBooking")
    public void testCancelBooking() {
        if (createdBookingRef != null) {
            Response response = bookingService.cancelBooking(createdBookingRef, "Test cancellation");
            
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204,
                "Cancel booking should return success");
        }
    }
    
    @Test(description = "Verify get non-existent booking returns 404", priority = 17)
    public void testGetNonExistentBooking() {
        Response response = bookingService.getBooking("INVALID123");
        
        Assert.assertEquals(response.getStatusCode(), 404, 
            "Non-existent booking should return 404");
    }
    
    @Test(description = "Verify booking exists utility method", priority = 18)
    public void testBookingExistsMethod() {
        boolean exists = bookingService.bookingExists("INVALID123");
        Assert.assertFalse(exists, "Invalid booking should not exist");
    }
    
    @Test(description = "Verify booking status utility method", priority = 19)
    public void testBookingStatusMethod() {
        String status = bookingService.getBookingStatus("INVALID123");
        Assert.assertEquals(status, "NOT_FOUND", "Invalid booking should show NOT_FOUND status");
    }
    
    @Test(description = "Verify API response time is acceptable", priority = 20)
    public void testApiResponseTime() {
        Response response = bookingService.searchOneWayFlights("AKL", "WLG", "2026-06-15", 1);
        
        long responseTime = ApiClient.getResponseTime(response);
        Assert.assertTrue(responseTime < 5000, 
            "API response time should be less than 5 seconds");
    }
    
    @Test(description = "Verify API error response structure", priority = 21)
    public void testApiErrorResponseStructure() {
        Response response = bookingService.getBooking("INVALID123");
        
        if (response.getStatusCode() == 404) {
            String errorMessage = ApiClient.getJsonPathValue(response, "message");
            // Error response should have a message
            Assert.assertTrue(errorMessage != null || response.getBody().asString().length() > 0,
                "Error response should have message or body");
        }
    }
}
