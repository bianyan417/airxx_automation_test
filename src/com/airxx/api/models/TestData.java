package com.airxx.api.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * TestData - Abstract base class for all test data models.
 *
 * <p>Provides two complementary construction styles:
 * <ol>
 *   <li><strong>Direct builder</strong> – call the concrete class's own
 *       {@code builder()} for full compile-time type safety:
 *       <pre>
 *       TicketTestData t = TicketTestData.builder()
 *           .ticketNumber("086-1234567890")
 *           .origin("AKL")
 *           .build();
 *       </pre></li>
 *   <li><strong>Factory method</strong> – call
 *       {@link #generateTestData(TestDataType, Map)} when the concrete type is
 *       chosen at runtime (e.g. driven by a JSON test-data file):
 *       <pre>
 *       Map&lt;String, String&gt; params = TestDataLoader.getTicketData("adultPassenger");
 *       TicketTestData t = TestData.generateTestData(TestDataType.TICKET, params);
 *       </pre></li>
 * </ol>
 *
 * <h3>Adding a new test data type:</h3>
 * <ol>
 *   <li>Create a class that extends {@link TestData} (e.g. {@code PassengerTestData}).</li>
 *   <li>Add a value to {@link TestDataType}.</li>
 *   <li>Register a mapping function in the {@code static} initialiser of this class.</li>
 * </ol>
 *
 * @author XAC Automation Team
 * @version 1.0
 */
public abstract class TestData {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    // ===================== TEST DATA TYPE REGISTRY =====================

    /**
     * Enumeration of every concrete {@link TestData} subtype that the
     * {@link #generateTestData} factory knows how to build.
     *
     * <p>Add a new constant here whenever a new {@link TestData} subclass is
     * introduced, then register its mapping function in the static initialiser
     * of {@link TestData}.
     */
    public enum TestDataType {
        /**
         * Produces a {@link TicketTestData} instance.
         * Recognised param keys: ticketNumber, bookingReference, passengerTitle,
         * passengerFirstName, passengerLastName, passengerEmail, passengerPhone,
         * flightNumber, origin, destination, departureDate, departureTime,
         * arrivalDate, arrivalTime, cabinClass, fareType, seatNumber,
         * mealPreference, baggageAllowance, price, currency, status,
         * loyaltyPointsNumber, loyaltyPointsEarned, issuedDate, specialRequests.
         */
        TICKET

        // Future types — add new values here and register in the static block below:
        // BOOKING,
        // PASSENGER,
    }

    /**
     * Registry that maps each {@link TestDataType} to a factory function.
     * The function accepts a {@code Map<String, String>} of field-name → value
     * pairs and returns a populated {@link TestData} instance.
     *
     * <p>Register new types in the static initialiser below.
     */
    private static final Map<TestDataType, Function<Map<String, String>, TestData>> FACTORY_REGISTRY;

    static {
        Map<TestDataType, Function<Map<String, String>, TestData>> registry = new HashMap<>();

        // --- TICKET ---
        registry.put(TestDataType.TICKET, TestData::buildTicketTestData);

        // Future registrations:
        // registry.put(TestDataType.BOOKING,   TestData::buildBookingTestData);
        // registry.put(TestDataType.PASSENGER, TestData::buildPassengerTestData);

        FACTORY_REGISTRY = Collections.unmodifiableMap(registry);
    }

    // ===================== FACTORY METHOD =====================

    /**
     * Creates and returns a concrete {@link TestData} subclass instance
     * identified by {@code type}, populated from the supplied {@code params} map.
     *
     * <p>Param keys are <em>field-name strings</em> that match the
     * {@code SerializedName} annotations of the target class (e.g.
     * {@code "ticketNumber"}, {@code "origin"}).  Unrecognised keys are silently
     * ignored; unset optional fields are left at their Java default values.
     *
     * <h3>Example</h3>
     * <pre>
     * // Load from JSON test-data file via TestDataLoader
     * Map&lt;String, String&gt; params = TestDataLoader.getTicketData("adultPassenger");
     * TicketTestData ticket = TestData.generateTestData(TestDataType.TICKET, params);
     *
     * // Or build inline
     * TicketTestData ticket = TestData.generateTestData(
     *     TestDataType.TICKET,
     *     Map.of("ticketNumber", "086-1234", "origin", "AKL", "destination", "WLG",
     *            "passengerFirstName", "John", "passengerLastName", "Smith",
     *            "flightNumber", "NZ101", "bookingReference", "REF001")
     * );
     * </pre>
     *
     * @param <T>    the expected {@link TestData} subtype (inferred by the caller)
     * @param type   which concrete subclass to create; must not be {@code null}
     * @param params field-name → string-value map; must not be {@code null}
     * @return a populated {@link TestData} subclass instance cast to {@code T}
     * @throws NullPointerException          if {@code type} or {@code params} is {@code null}
     * @throws UnsupportedOperationException if {@code type} has no registered factory
     */
    @SuppressWarnings("unchecked")
    public static <T extends TestData> T generateTestData(
            TestDataType type, Map<String, String> params) {

        Objects.requireNonNull(type,   "TestDataType must not be null");
        Objects.requireNonNull(params, "params map must not be null");

        Function<Map<String, String>, TestData> factory = FACTORY_REGISTRY.get(type);
        if (factory == null) {
            throw new UnsupportedOperationException(
                    "No factory registered for TestDataType: " + type
                    + ". Register one in TestData's static initialiser.");
        }
        return (T) factory.apply(params);
    }

    // ===================== PRIVATE BUILDER HELPERS =====================

    /**
     * Builds a {@link TicketTestData} from a flat string-value map.
     * Every entry is optional; missing keys simply leave the corresponding
     * field at its default value.
     *
     * @param p the parameters map
     * @return populated {@link TicketTestData}
     */
    private static TicketTestData buildTicketTestData(Map<String, String> p) {
        TicketTestData.Builder builder = TicketTestData.builder();

        // Passenger
        applyIfPresent(p, "passengerTitle",     builder::passengerTitle);
        applyIfPresent(p, "passengerFirstName", builder::passengerFirstName);
        applyIfPresent(p, "passengerLastName",  builder::passengerLastName);
        applyIfPresent(p, "passengerEmail",     builder::passengerEmail);
        applyIfPresent(p, "passengerPhone",     builder::passengerPhone);

        // Ticket / booking
        applyIfPresent(p, "ticketNumber",       builder::ticketNumber);
        applyIfPresent(p, "bookingReference",   builder::bookingReference);
        applyIfPresent(p, "issuedDate",         builder::issuedDate);
        applyIfPresent(p, "status",             builder::status);

        // Flight
        applyIfPresent(p, "flightNumber",       builder::flightNumber);
        applyIfPresent(p, "origin",             builder::origin);
        applyIfPresent(p, "destination",        builder::destination);
        applyIfPresent(p, "departureDate",      builder::departureDate);
        applyIfPresent(p, "departureTime",      builder::departureTime);
        applyIfPresent(p, "arrivalDate",        builder::arrivalDate);
        applyIfPresent(p, "arrivalTime",        builder::arrivalTime);

        // Fare / seat
        applyIfPresent(p, "cabinClass",         builder::cabinClass);
        applyIfPresent(p, "fareType",           builder::fareType);
        applyIfPresent(p, "seatNumber",         builder::seatNumber);
        applyIfPresent(p, "mealPreference",     builder::mealPreference);
        applyIfPresent(p, "baggageAllowance",   builder::baggageAllowance);

        // Pricing
        if (p.containsKey("price") && p.get("price") != null) {
            try { builder.price(Double.parseDouble(p.get("price"))); }
            catch (NumberFormatException ignored) { /* leave at default 0.0 */ }
        }
        applyIfPresent(p, "currency", builder::currency);

        // LoyaltyPoints
        applyIfPresent(p, "loyaltyPointsNumber", builder::loyaltyPointsNumber);
        if (p.containsKey("loyaltyPointsEarned") && p.get("loyaltyPointsEarned") != null) {
            try { builder.loyaltyPointsEarned(Integer.parseInt(p.get("loyaltyPointsEarned"))); }
            catch (NumberFormatException ignored) { /* leave at default 0 */ }
        }

        // Misc
        applyIfPresent(p, "specialRequests", builder::specialRequests);

        return builder.build();
    }

    // ===================== SHARED UTILITIES =====================

    /**
     * Calls {@code setter} with {@code params.get(key)} only when the map
     * contains a non-null, non-blank value for that key.
     *
     * @param params  the source map
     * @param key     field name to look up
     * @param setter  consumer that writes the value onto a builder
     */
    private static void applyIfPresent(
            Map<String, String> params,
            String key,
            java.util.function.Consumer<String> setter) {

        String value = params.get(key);
        if (value != null && !value.trim().isEmpty()) {
            setter.accept(value);
        }
    }

    // ===================== ABSTRACT CONTRACT =====================

    /**
     * Validates that all mandatory fields are populated and consistent.
     *
     * @return {@code true} if this instance is ready for use in a test
     */
    public abstract boolean validate();

    /**
     * Returns a human-readable one-line description of this instance,
     * used in log messages and assertion failure messages.
     *
     * @return descriptive string
     */
    public abstract String describe();

    // ===================== COMMON BEHAVIOUR =====================

    /**
     * Serialises this test data object to a pretty-printed JSON string.
     *
     * @return JSON representation
     */
    public String toJson() {
        return GSON.toJson(this);
    }

    /**
     * Creates a copy of this instance by round-tripping through JSON.
     * Subclasses may override for a more efficient implementation.
     *
     * @param <T> the concrete TestData type
     * @return a new instance with the same field values
     */
    @SuppressWarnings("unchecked")
    public <T extends TestData> T copy() {
        return (T) GSON.fromJson(toJson(), this.getClass());
    }

    @Override
    public String toString() {
        return describe();
    }

    // ===================== GENERIC BUILDER (CRTP) =====================

    /**
     * Generic abstract builder using the <em>Curiously Recurring Template Pattern</em>.
     *
     * <p>Subclass template:
     * <pre>
     * public static class Builder extends TestData.Builder&lt;TicketTestData, Builder&gt; {
     *     private final TicketTestData instance = new TicketTestData();
     *     &#64;Override protected TicketTestData instance() { return instance; }
     *     &#64;Override public TicketTestData build()       { return instance; }
     *     public Builder someField(String v) { instance.setSomeField(v); return self(); }
     * }
     * </pre>
     *
     * @param <T> concrete TestData type to build
     * @param <B> concrete Builder type (self-type for fluent chaining)
     */
    public static abstract class Builder<T extends TestData, B extends Builder<T, B>> {

        /**
         * Returns {@code this} cast to the concrete builder type {@code B}.
         *
         * @return this builder as type B
         */
        @SuppressWarnings("unchecked")
        protected final B self() {
            return (B) this;
        }

        /**
         * Returns the mutable in-progress instance being constructed.
         * Used by {@link #buildAndValidate()} to invoke {@link TestData#validate()}.
         *
         * @return the instance under construction
         */
        protected abstract T instance();

        /**
         * Finalises and returns the built instance without validation.
         *
         * @return constructed T instance
         */
        public abstract T build();

        /**
         * Builds the instance and immediately validates it via
         * {@link TestData#validate()}.
         *
         * @return validated T instance
         * @throws IllegalStateException if validation fails
         */
        public T buildAndValidate() {
            T result = build();
            if (!result.validate()) {
                throw new IllegalStateException(
                        "TestData validation failed for: " + result.describe());
            }
            return result;
        }
    }
}
