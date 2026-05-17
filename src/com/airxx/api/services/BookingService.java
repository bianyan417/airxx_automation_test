package com.airxx.api.services;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.airxx.api.client.ApiClient;
import com.airxx.api.models.BookingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BookingService - Service class for booking-related API operations.
 * Encapsulates all booking API endpoints and business logic.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class BookingService {
    
    private static final Logger logger = LogManager.getLogger(BookingService.class);
    
    private final ApiClient apiClient;
    
    // API Endpoints
    private static final String SEARCH_FLIGHTS = "/api/v1/flights/search";
    private static final String CREATE_BOOKING = "/api/v1/bookings";
    private static final String GET_BOOKING = "/api/v1/bookings/{bookingRef}";
    private static final String UPDATE_BOOKING = "/api/v1/bookings/{bookingRef}";
    private static final String CANCEL_BOOKING = "/api/v1/bookings/{bookingRef}/cancel";
    private static final String CONFIRM_BOOKING = "/api/v1/bookings/{bookingRef}/confirm";
    private static final String GET_TICKETS = "/api/v1/bookings/{bookingRef}/tickets";
    private static final String ADD_PASSENGER = "/api/v1/bookings/{bookingRef}/passengers";
    private static final String SELECT_SEAT = "/api/v1/bookings/{bookingRef}/seats";
    private static final String ADD_EXTRAS = "/api/v1/bookings/{bookingRef}/extras";
    private static final String GET_PRICE = "/api/v1/bookings/{bookingRef}/price";
    private static final String CHECK_IN = "/api/v1/bookings/{bookingRef}/checkin";
    
    /**
     * Default constructor - uses default ApiClient
     */
    public BookingService() {
        this.apiClient = new ApiClient();
    }
    
    /**
     * Constructor with custom ApiClient
     * @param apiClient ApiClient instance
     */
    public BookingService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }
    
    /**
     * Searches for available flights
     * @param origin Origin airport code
     * @param destination Destination airport code
     * @param departureDate Departure date
     * @param returnDate Return date (optional)
     * @param adults Number of adult passengers
     * @param children Number of child passengers
     * @param cabinClass Cabin class preference
     * @return Response with flight search results
     */
    public Response searchFlights(String origin, String destination, String departureDate,
                                   String returnDate, int adults, int children, String cabinClass) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("origin", origin);
        queryParams.put("destination", destination);
        queryParams.put("departureDate", departureDate);
        queryParams.put("adults", adults);
        
        if (returnDate != null && !returnDate.isEmpty()) {
            queryParams.put("returnDate", returnDate);
        }
        if (children > 0) {
            queryParams.put("children", children);
        }
        if (cabinClass != null && !cabinClass.isEmpty()) {
            queryParams.put("cabinClass", cabinClass);
        }
        
        logger.info("Searching flights: {} to {} on {}", origin, destination, departureDate);
        return apiClient.get(SEARCH_FLIGHTS, queryParams);
    }
    
    /**
     * Searches for one-way flights
     * @param origin Origin airport code
     * @param destination Destination airport code
     * @param departureDate Departure date
     * @param adults Number of adult passengers
     * @return Response with flight search results
     */
    public Response searchOneWayFlights(String origin, String destination, 
                                         String departureDate, int adults) {
        return searchFlights(origin, destination, departureDate, null, adults, 0, null);
    }
    
    /**
     * Creates a new booking
     * @param bookingRequest Booking request object
     * @return Response with booking confirmation
     */
    public Response createBooking(Object bookingRequest) {
        logger.info("Creating new booking");
        return apiClient.post(CREATE_BOOKING, bookingRequest);
    }
    
    /**
     * Creates a booking with specified details
     * @param flightNumber Flight number
     * @param passengers List of passengers
     * @param contactEmail Contact email
     * @param contactPhone Contact phone
     * @return Response with booking confirmation
     */
    public Response createBooking(String flightNumber, List<Map<String, String>> passengers,
                                   String contactEmail, String contactPhone) {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", flightNumber);
        request.put("passengers", passengers);
        request.put("contactEmail", contactEmail);
        request.put("contactPhone", contactPhone);
        
        logger.info("Creating booking for flight: {}", flightNumber);
        return apiClient.post(CREATE_BOOKING, request);
    }
    
    /**
     * Gets booking details by reference
     * @param bookingRef Booking reference
     * @return Response with booking details
     */
    public Response getBooking(String bookingRef) {
        logger.info("Getting booking: {}", bookingRef);
        String endpoint = GET_BOOKING.replace("{bookingRef}", bookingRef);
        return apiClient.get(endpoint);
    }
    
    /**
     * Gets booking details by reference and last name
     * @param bookingRef Booking reference
     * @param lastName Passenger last name
     * @return Response with booking details
     */
    public Response getBooking(String bookingRef, String lastName) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("lastName", lastName);
        
        logger.info("Getting booking: {} for {}", bookingRef, lastName);
        String endpoint = GET_BOOKING.replace("{bookingRef}", bookingRef);
        return apiClient.get(endpoint, queryParams);
    }
    
    /**
     * Gets booking as BookingResponse object
     * @param bookingRef Booking reference
     * @return BookingResponse object
     */
    public BookingResponse getBookingDetails(String bookingRef) {
        Response response = getBooking(bookingRef);
        return ApiClient.getBodyAs(response, BookingResponse.class);
    }
    
    /**
     * Updates an existing booking
     * @param bookingRef Booking reference
     * @param updateRequest Update request object
     * @return Response with updated booking
     */
    public Response updateBooking(String bookingRef, Object updateRequest) {
        logger.info("Updating booking: {}", bookingRef);
        String endpoint = UPDATE_BOOKING.replace("{bookingRef}", bookingRef);
        return apiClient.put(endpoint, updateRequest);
    }
    
    /**
     * Cancels a booking
     * @param bookingRef Booking reference
     * @return Response with cancellation confirmation
     */
    public Response cancelBooking(String bookingRef) {
        logger.info("Cancelling booking: {}", bookingRef);
        String endpoint = CANCEL_BOOKING.replace("{bookingRef}", bookingRef);
        return apiClient.post(endpoint);
    }
    
    /**
     * Cancels a booking with reason
     * @param bookingRef Booking reference
     * @param reason Cancellation reason
     * @return Response with cancellation confirmation
     */
    public Response cancelBooking(String bookingRef, String reason) {
        Map<String, String> request = new HashMap<>();
        request.put("reason", reason);
        
        logger.info("Cancelling booking: {} with reason: {}", bookingRef, reason);
        String endpoint = CANCEL_BOOKING.replace("{bookingRef}", bookingRef);
        return apiClient.post(endpoint, request);
    }
    
    /**
     * Confirms and pays for a booking
     * @param bookingRef Booking reference
     * @param paymentDetails Payment details
     * @return Response with confirmation
     */
    public Response confirmBooking(String bookingRef, Map<String, Object> paymentDetails) {
        logger.info("Confirming booking: {}", bookingRef);
        String endpoint = CONFIRM_BOOKING.replace("{bookingRef}", bookingRef);
        return apiClient.post(endpoint, paymentDetails);
    }
    
    /**
     * Gets tickets for a booking
     * @param bookingRef Booking reference
     * @return Response with tickets
     */
    public Response getTickets(String bookingRef) {
        logger.info("Getting tickets for booking: {}", bookingRef);
        String endpoint = GET_TICKETS.replace("{bookingRef}", bookingRef);
        return apiClient.get(endpoint);
    }
    
    /**
     * Adds a passenger to an existing booking
     * @param bookingRef Booking reference
     * @param passengerDetails Passenger details
     * @return Response with updated booking
     */
    public Response addPassenger(String bookingRef, Map<String, String> passengerDetails) {
        logger.info("Adding passenger to booking: {}", bookingRef);
        String endpoint = ADD_PASSENGER.replace("{bookingRef}", bookingRef);
        return apiClient.post(endpoint, passengerDetails);
    }
    
    /**
     * Selects seat for a passenger
     * @param bookingRef Booking reference
     * @param passengerId Passenger ID
     * @param seatNumber Seat number
     * @return Response with seat selection
     */
    public Response selectSeat(String bookingRef, String passengerId, String seatNumber) {
        Map<String, String> request = new HashMap<>();
        request.put("passengerId", passengerId);
        request.put("seatNumber", seatNumber);
        
        logger.info("Selecting seat {} for passenger {} in booking {}", seatNumber, passengerId, bookingRef);
        String endpoint = SELECT_SEAT.replace("{bookingRef}", bookingRef);
        return apiClient.post(endpoint, request);
    }
    
    /**
     * Adds extras to a booking (baggage, meals, etc.)
     * @param bookingRef Booking reference
     * @param extras Extras details
     * @return Response with updated booking
     */
    public Response addExtras(String bookingRef, Map<String, Object> extras) {
        logger.info("Adding extras to booking: {}", bookingRef);
        String endpoint = ADD_EXTRAS.replace("{bookingRef}", bookingRef);
        return apiClient.post(endpoint, extras);
    }
    
    /**
     * Gets current price for a booking
     * @param bookingRef Booking reference
     * @return Response with pricing details
     */
    public Response getBookingPrice(String bookingRef) {
        logger.info("Getting price for booking: {}", bookingRef);
        String endpoint = GET_PRICE.replace("{bookingRef}", bookingRef);
        return apiClient.get(endpoint);
    }
    
    /**
     * Performs online check-in
     * @param bookingRef Booking reference
     * @param passengerIds List of passenger IDs to check in
     * @return Response with check-in confirmation
     */
    public Response checkIn(String bookingRef, List<String> passengerIds) {
        Map<String, Object> request = new HashMap<>();
        request.put("passengerIds", passengerIds);
        
        logger.info("Checking in passengers for booking: {}", bookingRef);
        String endpoint = CHECK_IN.replace("{bookingRef}", bookingRef);
        return apiClient.post(endpoint, request);
    }
    
    /**
     * Validates if a booking exists
     * @param bookingRef Booking reference
     * @return true if booking exists
     */
    public boolean bookingExists(String bookingRef) {
        Response response = getBooking(bookingRef);
        return response.getStatusCode() == 200;
    }
    
    /**
     * Gets booking status
     * @param bookingRef Booking reference
     * @return Booking status string
     */
    public String getBookingStatus(String bookingRef) {
        Response response = getBooking(bookingRef);
        if (response.getStatusCode() == 200) {
            return ApiClient.getJsonPathValue(response, "status");
        }
        return "NOT_FOUND";
    }
    
    /**
     * Gets total price for a booking
     * @param bookingRef Booking reference
     * @return Total price as double
     */
    public double getTotalPrice(String bookingRef) {
        Response response = getBookingPrice(bookingRef);
        if (response.getStatusCode() == 200) {
            return ApiClient.getJsonPathValue(response, "pricing.totalAmount");
        }
        return 0.0;
    }
    
    /**
     * Creates a simple passenger map
     * @param title Passenger title
     * @param firstName First name
     * @param lastName Last name
     * @param dateOfBirth Date of birth
     * @param type Passenger type
     * @return Passenger map
     */
    public static Map<String, String> createPassenger(String title, String firstName, 
                                                        String lastName, String dateOfBirth, String type) {
        Map<String, String> passenger = new HashMap<>();
        passenger.put("title", title);
        passenger.put("firstName", firstName);
        passenger.put("lastName", lastName);
        passenger.put("dateOfBirth", dateOfBirth);
        passenger.put("passengerType", type);
        return passenger;
    }
}
