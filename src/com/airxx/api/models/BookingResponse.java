package com.airxx.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Objects;

/**
 * BookingResponse - Model class representing a booking API response.
 * Contains booking confirmation details, flights, passengers, and pricing.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class BookingResponse {
    
    @SerializedName("bookingReference")
    private String bookingReference;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("confirmationNumber")
    private String confirmationNumber;
    
    @SerializedName("bookingDate")
    private String bookingDate;
    
    @SerializedName("expiryDate")
    private String expiryDate;
    
    @SerializedName("flights")
    private List<FlightSegment> flights;
    
    @SerializedName("passengers")
    private List<Passenger> passengers;
    
    @SerializedName("contactDetails")
    private ContactDetails contactDetails;
    
    @SerializedName("pricing")
    private Pricing pricing;
    
    @SerializedName("paymentStatus")
    private String paymentStatus;
    
    @SerializedName("tickets")
    private List<TicketTestData> tickets;
    
    @SerializedName("errors")
    private List<ApiError> errors;
    
    @SerializedName("warnings")
    private List<String> warnings;
    
    // Default constructor
    public BookingResponse() {}
    
    // Getters and Setters
    public String getBookingReference() {
        return bookingReference;
    }
    
    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getConfirmationNumber() {
        return confirmationNumber;
    }
    
    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }
    
    public String getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public String getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public List<FlightSegment> getFlights() {
        return flights;
    }
    
    public void setFlights(List<FlightSegment> flights) {
        this.flights = flights;
    }
    
    public List<Passenger> getPassengers() {
        return passengers;
    }
    
    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
    
    public ContactDetails getContactDetails() {
        return contactDetails;
    }
    
    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }
    
    public Pricing getPricing() {
        return pricing;
    }
    
    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public List<TicketTestData> getTickets() {
        return tickets;
    }
    
    public void setTickets(List<TicketTestData> tickets) {
        this.tickets = tickets;
    }
    
    public List<ApiError> getErrors() {
        return errors;
    }
    
    public void setErrors(List<ApiError> errors) {
        this.errors = errors;
    }
    
    public List<String> getWarnings() {
        return warnings;
    }
    
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
    
    // Utility methods
    public boolean isSuccessful() {
        return "CONFIRMED".equalsIgnoreCase(status) || "SUCCESS".equalsIgnoreCase(status);
    }
    
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
    
    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }
    
    public int getPassengerCount() {
        return passengers != null ? passengers.size() : 0;
    }
    
    public int getFlightCount() {
        return flights != null ? flights.size() : 0;
    }
    
    // ===================== VALIDATION METHODS =====================
    
    /**
     * Validates that the booking reference is present and not empty
     * @return true if booking reference is valid
     */
    public boolean hasValidBookingReference() {
        return bookingReference != null && !bookingReference.trim().isEmpty();
    }
    
    /**
     * Validates that the booking has required basic attributes
     * @return true if all required attributes are present
     */
    public boolean hasRequiredAttributes() {
        return hasValidBookingReference() && 
               status != null && 
               !status.trim().isEmpty();
    }
    
    /**
     * Validates that the booking has at least one passenger
     * @return true if passengers list is not empty
     */
    public boolean hasPassengers() {
        return passengers != null && !passengers.isEmpty();
    }
    
    /**
     * Validates that the booking has at least one flight
     * @return true if flights list is not empty
     */
    public boolean hasFlights() {
        return flights != null && !flights.isEmpty();
    }
    
    /**
     * Validates that pricing information is present
     * @return true if pricing is available
     */
    public boolean hasPricing() {
        return pricing != null;
    }
    
    /**
     * Validates that total amount is greater than zero
     * @return true if total amount is positive
     */
    public boolean hasValidTotalAmount() {
        return pricing != null && pricing.getTotalAmount() > 0;
    }
    
    /**
     * Validates that currency is present
     * @return true if currency is set
     */
    public boolean hasValidCurrency() {
        return pricing != null && 
               pricing.getCurrency() != null && 
               !pricing.getCurrency().trim().isEmpty();
    }
    
    /**
     * Gets the first passenger if available
     * @return First passenger or null
     */
    public Passenger getFirstPassenger() {
        return hasPassengers() ? passengers.get(0) : null;
    }
    
    /**
     * Gets the first flight segment if available
     * @return First flight segment or null
     */
    public FlightSegment getFirstFlight() {
        return hasFlights() ? flights.get(0) : null;
    }
    
    /**
     * Gets passenger by index
     * @param index Passenger index
     * @return Passenger at index or null
     */
    public Passenger getPassengerAt(int index) {
        if (passengers != null && index >= 0 && index < passengers.size()) {
            return passengers.get(index);
        }
        return null;
    }
    
    /**
     * Gets flight by index
     * @param index Flight index
     * @return Flight segment at index or null
     */
    public FlightSegment getFlightAt(int index) {
        if (flights != null && index >= 0 && index < flights.size()) {
            return flights.get(index);
        }
        return null;
    }
    
    /**
     * Checks if the booking is in pending status
     * @return true if status is pending
     */
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }
    
    /**
     * Checks if the booking is confirmed
     * @return true if status is confirmed
     */
    public boolean isConfirmed() {
        return "CONFIRMED".equalsIgnoreCase(status);
    }
    
    /**
     * Checks if the booking is cancelled
     * @return true if status is cancelled
     */
    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }
    
    /**
     * Validates contact details are present
     * @return true if contact details exist
     */
    public boolean hasContactDetails() {
        return contactDetails != null;
    }
    
    /**
     * Validates email is present in contact details
     * @return true if email is set
     */
    public boolean hasValidEmail() {
        return contactDetails != null && 
               contactDetails.getEmail() != null && 
               !contactDetails.getEmail().trim().isEmpty();
    }
    
    /**
     * Gets total ticket count
     * @return Number of tickets
     */
    public int getTicketCount() {
        return tickets != null ? tickets.size() : 0;
    }
    
    /**
     * Validates the booking has tickets issued
     * @return true if tickets are present
     */
    public boolean hasTickets() {
        return tickets != null && !tickets.isEmpty();
    }
    
    /**
     * Gets first error message if errors exist
     * @return First error message or null
     */
    public String getFirstErrorMessage() {
        if (hasErrors()) {
            return errors.get(0).getMessage();
        }
        return null;
    }
    
    /**
     * Gets first error code if errors exist
     * @return First error code or null
     */
    public String getFirstErrorCode() {
        if (hasErrors()) {
            return errors.get(0).getCode();
        }
        return null;
    }
    
    /**
     * Validates the complete booking structure
     * @return true if booking has all expected data for a complete booking
     */
    public boolean isCompleteBooking() {
        return hasValidBookingReference() &&
               isConfirmed() &&
               hasPassengers() &&
               hasFlights() &&
               hasPricing() &&
               hasValidTotalAmount();
    }
    
    @Override
    public String toString() {
        return "BookingResponse{" +
               "bookingReference='" + bookingReference + '\'' +
               ", status='" + status + '\'' +
               ", passengers=" + getPassengerCount() +
               ", flights=" + getFlightCount() +
               '}';
    }
    
    // ===================== NESTED CLASSES =====================
    
    /**
     * Flight segment in the booking
     */
    public static class FlightSegment {
        @SerializedName("flightNumber")
        private String flightNumber;
        
        @SerializedName("origin")
        private String origin;
        
        @SerializedName("originName")
        private String originName;
        
        @SerializedName("destination")
        private String destination;
        
        @SerializedName("destinationName")
        private String destinationName;
        
        @SerializedName("departureDate")
        private String departureDate;
        
        @SerializedName("departureTime")
        private String departureTime;
        
        @SerializedName("arrivalDate")
        private String arrivalDate;
        
        @SerializedName("arrivalTime")
        private String arrivalTime;
        
        @SerializedName("duration")
        private String duration;
        
        @SerializedName("aircraft")
        private String aircraft;
        
        @SerializedName("cabinClass")
        private String cabinClass;
        
        @SerializedName("fareClass")
        private String fareClass;
        
        @SerializedName("operatedBy")
        private String operatedBy;
        
        @SerializedName("status")
        private String status;
        
        // Getters and Setters
        public String getFlightNumber() { return flightNumber; }
        public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
        
        public String getOrigin() { return origin; }
        public void setOrigin(String origin) { this.origin = origin; }
        
        public String getOriginName() { return originName; }
        public void setOriginName(String originName) { this.originName = originName; }
        
        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }
        
        public String getDestinationName() { return destinationName; }
        public void setDestinationName(String destinationName) { this.destinationName = destinationName; }
        
        public String getDepartureDate() { return departureDate; }
        public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
        
        public String getDepartureTime() { return departureTime; }
        public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
        
        public String getArrivalDate() { return arrivalDate; }
        public void setArrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; }
        
        public String getArrivalTime() { return arrivalTime; }
        public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
        
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        
        public String getAircraft() { return aircraft; }
        public void setAircraft(String aircraft) { this.aircraft = aircraft; }
        
        public String getCabinClass() { return cabinClass; }
        public void setCabinClass(String cabinClass) { this.cabinClass = cabinClass; }
        
        public String getFareClass() { return fareClass; }
        public void setFareClass(String fareClass) { this.fareClass = fareClass; }
        
        public String getOperatedBy() { return operatedBy; }
        public void setOperatedBy(String operatedBy) { this.operatedBy = operatedBy; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getRoute() {
            return origin + " → " + destination;
        }
        
        @Override
        public String toString() {
            return "FlightSegment{" +
                   "flight='" + flightNumber + '\'' +
                   ", route='" + getRoute() + '\'' +
                   ", departure='" + departureDate + " " + departureTime + '\'' +
                   '}';
        }
    }
    
    /**
     * Passenger in the booking
     */
    public static class Passenger {
        @SerializedName("passengerId")
        private String passengerId;
        
        @SerializedName("title")
        private String title;
        
        @SerializedName("firstName")
        private String firstName;
        
        @SerializedName("lastName")
        private String lastName;
        
        @SerializedName("dateOfBirth")
        private String dateOfBirth;
        
        @SerializedName("passengerType")
        private String passengerType;
        
        @SerializedName("nationality")
        private String nationality;
        
        @SerializedName("airpointsNumber")
        private String loyaltyPointsNumber;
        
        @SerializedName("seatAssignment")
        private String seatAssignment;
        
        @SerializedName("mealPreference")
        private String mealPreference;
        
        @SerializedName("specialRequests")
        private List<String> specialRequests;
        
        // Getters and Setters
        public String getPassengerId() { return passengerId; }
        public void setPassengerId(String passengerId) { this.passengerId = passengerId; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        
        public String getPassengerType() { return passengerType; }
        public void setPassengerType(String passengerType) { this.passengerType = passengerType; }
        
        public String getNationality() { return nationality; }
        public void setNationality(String nationality) { this.nationality = nationality; }
        
        public String getLoyaltyPointsNumber() { return loyaltyPointsNumber; }
        public void setLoyaltyPointsNumber(String loyaltyPointsNumber) { this.loyaltyPointsNumber = loyaltyPointsNumber; }
        
        public String getSeatAssignment() { return seatAssignment; }
        public void setSeatAssignment(String seatAssignment) { this.seatAssignment = seatAssignment; }
        
        public String getMealPreference() { return mealPreference; }
        public void setMealPreference(String mealPreference) { this.mealPreference = mealPreference; }
        
        public List<String> getSpecialRequests() { return specialRequests; }
        public void setSpecialRequests(List<String> specialRequests) { this.specialRequests = specialRequests; }
        
        public String getFullName() {
            return (title != null ? title + " " : "") + firstName + " " + lastName;
        }
        
        @Override
        public String toString() {
            return "Passenger{" +
                   "name='" + getFullName() + '\'' +
                   ", type='" + passengerType + '\'' +
                   '}';
        }
    }
    
    /**
     * Contact details for the booking
     */
    public static class ContactDetails {
        @SerializedName("email")
        private String email;
        
        @SerializedName("phone")
        private String phone;
        
        @SerializedName("countryCode")
        private String countryCode;
        
        @SerializedName("address")
        private Address address;
        
        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
        
        public Address getAddress() { return address; }
        public void setAddress(Address address) { this.address = address; }
    }
    
    /**
     * Address details
     */
    public static class Address {
        @SerializedName("street")
        private String street;
        
        @SerializedName("city")
        private String city;
        
        @SerializedName("state")
        private String state;
        
        @SerializedName("postalCode")
        private String postalCode;
        
        @SerializedName("country")
        private String country;
        
        // Getters and Setters
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
        
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
    }
    
    /**
     * Pricing details
     */
    public static class Pricing {
        @SerializedName("baseFare")
        private double baseFare;
        
        @SerializedName("taxes")
        private double taxes;
        
        @SerializedName("fees")
        private double fees;
        
        @SerializedName("extras")
        private double extras;
        
        @SerializedName("discount")
        private double discount;
        
        @SerializedName("totalAmount")
        private double totalAmount;
        
        @SerializedName("currency")
        private String currency;
        
        @SerializedName("airpointsUsed")
        private int loyaltyPointsUsed;
        
        @SerializedName("airpointsEarned")
        private int loyaltyPointsEarned;
        
        // Getters and Setters
        public double getBaseFare() { return baseFare; }
        public void setBaseFare(double baseFare) { this.baseFare = baseFare; }
        
        public double getTaxes() { return taxes; }
        public void setTaxes(double taxes) { this.taxes = taxes; }
        
        public double getFees() { return fees; }
        public void setFees(double fees) { this.fees = fees; }
        
        public double getExtras() { return extras; }
        public void setExtras(double extras) { this.extras = extras; }
        
        public double getDiscount() { return discount; }
        public void setDiscount(double discount) { this.discount = discount; }
        
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
        
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        
        public int getLoyaltyPointsUsed() { return loyaltyPointsUsed; }
        public void setLoyaltyPointsUsed(int loyaltyPointsUsed) { this.loyaltyPointsUsed = loyaltyPointsUsed; }
        
        public int getLoyaltyPointsEarned() { return loyaltyPointsEarned; }
        public void setLoyaltyPointsEarned(int loyaltyPointsEarned) { this.loyaltyPointsEarned = loyaltyPointsEarned; }
        
        public String getFormattedTotal() {
            return currency + " " + String.format("%.2f", totalAmount);
        }
    }
    
    /**
     * API error details
     */
    public static class ApiError {
        @SerializedName("code")
        private String code;
        
        @SerializedName("message")
        private String message;
        
        @SerializedName("field")
        private String field;
        
        // Getters and Setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        
        @Override
        public String toString() {
            return "ApiError{code='" + code + "', message='" + message + "', field='" + field + "'}";
        }
    }
}