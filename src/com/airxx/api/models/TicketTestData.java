package com.airxx.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * TicketTestData - Concrete test data model representing a flight ticket.
 *
 * <p>Extends {@link TestData} to participate in the shared generic builder
 * infrastructure.  All ticket fields are populated through the inner
 * {@link Builder}, which extends {@code TestData.Builder<TicketTestData, Builder>}
 * so every fluent setter returns the correct {@code Builder} type without casting.
 *
 * <h3>Usage:</h3>
 * <pre>
 * TicketTestData ticket = TicketTestData.builder()
 *     .ticketNumber("086-1234567890")
 *     .bookingReference("ABC123")
 *     .passengerTitle("Mr")
 *     .passengerFirstName("John")
 *     .passengerLastName("Smith")
 *     .passengerEmail("john.smith@example.com")
 *     .flightNumber("XA123")
 *     .origin("AKL")
 *     .destination("WLG")
 *     .departureDate("2026-06-15")
 *     .departureTime("08:00")
 *     .cabinClass("Economy")
 *     .fareType("Seat + Bag")
 *     .price(199.00)
 *     .currency("USD")
 *     .status("CONFIRMED")
 *     .build();
 * </pre>
 *
 * @author XAC Automation Team
 * @version 1.0
 */
public class TicketTestData extends TestData {

    @SerializedName("ticketNumber")
    private String ticketNumber;

    @SerializedName("bookingReference")
    private String bookingReference;

    @SerializedName("passengerName")
    private String passengerName;

    @SerializedName("passengerTitle")
    private String passengerTitle;

    @SerializedName("passengerFirstName")
    private String passengerFirstName;

    @SerializedName("passengerLastName")
    private String passengerLastName;

    @SerializedName("passengerEmail")
    private String passengerEmail;

    @SerializedName("passengerPhone")
    private String passengerPhone;

    @SerializedName("flightNumber")
    private String flightNumber;

    @SerializedName("origin")
    private String origin;

    @SerializedName("destination")
    private String destination;

    @SerializedName("departureDate")
    private String departureDate;

    @SerializedName("departureTime")
    private String departureTime;

    @SerializedName("arrivalDate")
    private String arrivalDate;

    @SerializedName("arrivalTime")
    private String arrivalTime;

    @SerializedName("cabinClass")
    private String cabinClass;

    @SerializedName("fareType")
    private String fareType;

    @SerializedName("seatNumber")
    private String seatNumber;

    @SerializedName("mealPreference")
    private String mealPreference;

    @SerializedName("baggageAllowance")
    private String baggageAllowance;

    @SerializedName("price")
    private double price;

    @SerializedName("currency")
    private String currency;

    @SerializedName("status")
    private String status;

    @SerializedName("airpointsNumber")
    private String loyaltyPointsNumber;

    @SerializedName("airpointsEarned")
    private int loyaltyPointsEarned;

    @SerializedName("issuedDate")
    private String issuedDate;

    @SerializedName("specialRequests")
    private String specialRequests;

    /** Package-private no-arg constructor – use {@link #builder()} to construct. */
    TicketTestData() {}

    // ===================== STATIC FACTORY =====================

    /**
     * Returns a new {@link Builder} for constructing a {@link TicketTestData} instance.
     *
     * @return a fresh Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    // ===================== TestData CONTRACT =====================

    /**
     * Validates that the minimum required fields for a usable ticket are set.
     * Required: ticketNumber, bookingReference, passengerFirstName,
     * passengerLastName, flightNumber, origin, destination.
     *
     * @return {@code true} if all required fields are non-null and non-empty
     */
    @Override
    public boolean validate() {
        return isNotEmpty(ticketNumber)
                && isNotEmpty(bookingReference)
                && isNotEmpty(passengerFirstName)
                && isNotEmpty(passengerLastName)
                && isNotEmpty(flightNumber)
                && isNotEmpty(origin)
                && isNotEmpty(destination);
    }

    /**
     * Returns a short description of this ticket, suitable for log messages.
     *
     * @return descriptive string
     */
    @Override
    public String describe() {
        return String.format("TicketTestData[ticket=%s, booking=%s, passenger=%s, flight=%s, route=%s]",
                ticketNumber, bookingReference, getFullPassengerName(),
                flightNumber, getRoute());
    }

    // ===================== DERIVED GETTERS =====================

    /**
     * Returns the full name of the passenger: title + first name + last name.
     *
     * @return full passenger name
     */
    public String getFullPassengerName() {
        StringBuilder sb = new StringBuilder();
        if (isNotEmpty(passengerTitle)) sb.append(passengerTitle).append(" ");
        if (isNotEmpty(passengerFirstName)) sb.append(passengerFirstName).append(" ");
        if (isNotEmpty(passengerLastName)) sb.append(passengerLastName);
        return sb.toString().trim();
    }

    /**
     * Returns the route as "{origin} → {destination}".
     *
     * @return route string
     */
    public String getRoute() {
        return (origin != null ? origin : "") + " → " + (destination != null ? destination : "");
    }

    // ===================== GETTERS & SETTERS =====================

    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }

    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getPassengerTitle() { return passengerTitle; }
    public void setPassengerTitle(String passengerTitle) { this.passengerTitle = passengerTitle; }

    public String getPassengerFirstName() { return passengerFirstName; }
    public void setPassengerFirstName(String passengerFirstName) { this.passengerFirstName = passengerFirstName; }

    public String getPassengerLastName() { return passengerLastName; }
    public void setPassengerLastName(String passengerLastName) { this.passengerLastName = passengerLastName; }

    public String getPassengerEmail() { return passengerEmail; }
    public void setPassengerEmail(String passengerEmail) { this.passengerEmail = passengerEmail; }

    public String getPassengerPhone() { return passengerPhone; }
    public void setPassengerPhone(String passengerPhone) { this.passengerPhone = passengerPhone; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDepartureDate() { return departureDate; }
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getCabinClass() { return cabinClass; }
    public void setCabinClass(String cabinClass) { this.cabinClass = cabinClass; }

    public String getFareType() { return fareType; }
    public void setFareType(String fareType) { this.fareType = fareType; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getMealPreference() { return mealPreference; }
    public void setMealPreference(String mealPreference) { this.mealPreference = mealPreference; }

    public String getBaggageAllowance() { return baggageAllowance; }
    public void setBaggageAllowance(String baggageAllowance) { this.baggageAllowance = baggageAllowance; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLoyaltyPointsNumber() { return loyaltyPointsNumber; }
    public void setLoyaltyPointsNumber(String loyaltyPointsNumber) { this.loyaltyPointsNumber = loyaltyPointsNumber; }

    public int getLoyaltyPointsEarned() { return loyaltyPointsEarned; }
    public void setLoyaltyPointsEarned(int loyaltyPointsEarned) { this.loyaltyPointsEarned = loyaltyPointsEarned; }

    public String getIssuedDate() { return issuedDate; }
    public void setIssuedDate(String issuedDate) { this.issuedDate = issuedDate; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    // ===================== OBJECT METHODS =====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketTestData that = (TicketTestData) o;
        return Objects.equals(ticketNumber, that.ticketNumber)
                && Objects.equals(bookingReference, that.bookingReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketNumber, bookingReference);
    }

    // ===================== HELPERS =====================

    private static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // ===================== BUILDER =====================

    /**
     * Fluent builder for {@link TicketTestData}.
     *
     * <p>Extends {@code TestData.Builder<TicketTestData, Builder>} so:
     * <ul>
     *   <li>{@code T = TicketTestData} — type that is being built</li>
     *   <li>{@code B = Builder}        — concrete builder type returned by every setter</li>
     * </ul>
     *
     * <p>The inherited {@link TestData.Builder#self()} method ensures every
     * setter returns this exact {@code Builder} instance without casts,
     * and {@link TestData.Builder#buildAndValidate()} runs {@link #validate()}
     * before returning the finished object.
     */
    public static class Builder extends TestData.Builder<TicketTestData, Builder> {

        private final TicketTestData ticket = new TicketTestData();

        /** {@inheritDoc} */
        @Override
        protected TicketTestData instance() {
            return ticket;
        }

        /** Builds and returns the {@link TicketTestData} instance without validation. */
        @Override
        public TicketTestData build() {
            return ticket;
        }

        // --- Passenger ---

        public Builder passengerTitle(String title) {
            ticket.setPassengerTitle(title);
            return self();
        }

        public Builder passengerFirstName(String firstName) {
            ticket.setPassengerFirstName(firstName);
            return self();
        }

        public Builder passengerLastName(String lastName) {
            ticket.setPassengerLastName(lastName);
            return self();
        }

        public Builder passengerEmail(String email) {
            ticket.setPassengerEmail(email);
            return self();
        }

        public Builder passengerPhone(String phone) {
            ticket.setPassengerPhone(phone);
            return self();
        }

        // --- Ticket / Booking ---

        public Builder ticketNumber(String ticketNumber) {
            ticket.setTicketNumber(ticketNumber);
            return self();
        }

        public Builder bookingReference(String bookingReference) {
            ticket.setBookingReference(bookingReference);
            return self();
        }

        public Builder issuedDate(String issuedDate) {
            ticket.setIssuedDate(issuedDate);
            return self();
        }

        public Builder status(String status) {
            ticket.setStatus(status);
            return self();
        }

        // --- Flight ---

        public Builder flightNumber(String flightNumber) {
            ticket.setFlightNumber(flightNumber);
            return self();
        }

        public Builder origin(String origin) {
            ticket.setOrigin(origin);
            return self();
        }

        public Builder destination(String destination) {
            ticket.setDestination(destination);
            return self();
        }

        public Builder departureDate(String date) {
            ticket.setDepartureDate(date);
            return self();
        }

        public Builder departureTime(String time) {
            ticket.setDepartureTime(time);
            return self();
        }

        public Builder arrivalDate(String date) {
            ticket.setArrivalDate(date);
            return self();
        }

        public Builder arrivalTime(String time) {
            ticket.setArrivalTime(time);
            return self();
        }

        // --- Fare / Seat ---

        public Builder cabinClass(String cabinClass) {
            ticket.setCabinClass(cabinClass);
            return self();
        }

        public Builder fareType(String fareType) {
            ticket.setFareType(fareType);
            return self();
        }

        public Builder seatNumber(String seatNumber) {
            ticket.setSeatNumber(seatNumber);
            return self();
        }

        public Builder mealPreference(String mealPreference) {
            ticket.setMealPreference(mealPreference);
            return self();
        }

        public Builder baggageAllowance(String baggageAllowance) {
            ticket.setBaggageAllowance(baggageAllowance);
            return self();
        }

        // --- Pricing ---

        public Builder price(double price) {
            ticket.setPrice(price);
            return self();
        }

        public Builder currency(String currency) {
            ticket.setCurrency(currency);
            return self();
        }

        // --- LoyaltyPoints ---

        public Builder loyaltyPointsNumber(String loyaltyPointsNumber) {
            ticket.setLoyaltyPointsNumber(loyaltyPointsNumber);
            return self();
        }

        public Builder loyaltyPointsEarned(int loyaltyPointsEarned) {
            ticket.setLoyaltyPointsEarned(loyaltyPointsEarned);
            return self();
        }

        // --- Misc ---

        public Builder specialRequests(String specialRequests) {
            ticket.setSpecialRequests(specialRequests);
            return self();
        }
    }
}
