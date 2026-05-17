package com.airxx.core.constants;

/**
 * FrameworkConstants - Central location for all framework constants.
 * Provides easy access to paths, timeouts, and other static values.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public final class FrameworkConstants {
    
    private FrameworkConstants() {
        // Private constructor to prevent instantiation
    }
    
    // ===================== PROJECT PATHS =====================
    
    /** Project root directory */
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    
    /** Source resources path */
    public static final String RESOURCES_PATH = PROJECT_PATH + "/src/com/airnz/resources/";
    
    /** Configuration files path */
    public static final String CONFIG_PATH = RESOURCES_PATH + "config/";
    
    /** Test data path */
    public static final String TESTDATA_PATH = RESOURCES_PATH + "testdata/";
    
    /** Reports output path */
    public static final String REPORTS_PATH = PROJECT_PATH + "/reports/";
    
    /** Screenshots path */
    public static final String SCREENSHOTS_PATH = REPORTS_PATH + "screenshots/";
    
    /** Logs path */
    public static final String LOGS_PATH = PROJECT_PATH + "/logs/";
    
    /** Feature files path for BDD */
    public static final String FEATURES_PATH = PROJECT_PATH + "/src/com/airnz/tests/bdd/features/";
    
    // ===================== TIMEOUTS =====================
    
    /** Default explicit wait timeout in seconds */
    public static final int DEFAULT_EXPLICIT_WAIT = 10;
    
    /** Default implicit wait timeout in seconds */
    public static final int DEFAULT_IMPLICIT_WAIT = 10;
    
    /** Default page load timeout in seconds */
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT = 30;
    
    /** Short wait for quick operations */
    public static final int SHORT_WAIT = 5;
    
    /** Long wait for slow operations */
    public static final int LONG_WAIT = 30;
    
    /** Polling interval for fluent wait in milliseconds */
    public static final int POLLING_INTERVAL = 500;
    
    // ===================== TEST DATA FILES =====================
    
    /** Login test data file */
    public static final String LOGIN_DATA_FILE = "loginData.json";
    
    /** Booking test data file */
    public static final String BOOKING_DATA_FILE = "BookingData.json";
    
    /** Ticket test data file */
    public static final String TICKET_DATA_FILE = "TicketData.json";
    
    // ===================== API CONSTANTS =====================
    
    /** API connection timeout in seconds */
    public static final int API_CONNECTION_TIMEOUT = 30;
    
    /** API read timeout in seconds */
    public static final int API_READ_TIMEOUT = 30;
    
    /** Content type for JSON */
    public static final String CONTENT_TYPE_JSON = "application/json";
    
    /** Accept header for JSON */
    public static final String ACCEPT_JSON = "application/json";
    
    // ===================== REPORT CONSTANTS =====================
    
    /** Extent report file name */
    public static final String EXTENT_REPORT_NAME = "XAC_Test_Report.html";
    
    /** Screenshot format */
    public static final String SCREENSHOT_FORMAT = "png";
    
    /** Date format for reports */
    public static final String REPORT_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    
    // ===================== BROWSER CONSTANTS =====================
    
    /** Chrome browser identifier */
    public static final String CHROME = "chrome";
    
    /** Firefox browser identifier */
    public static final String FIREFOX = "firefox";
    
    /** Edge browser identifier */
    public static final String EDGE = "edge";
    
    /** Safari browser identifier */
    public static final String SAFARI = "safari";
    
    // ===================== APPLICATION SPECIFIC CONSTANTS =====================
    
    /** XXX Air Company official website */
    public static final String APP_WEBSITE = "https://www.xxxaircompany.com";
    
    /** Default currency */
    public static final String DEFAULT_CURRENCY = "USD";
    
    /** Default country code */
    public static final String DEFAULT_COUNTRY = "XX";
    
    /** Date format for flight searches */
    public static final String FLIGHT_DATE_FORMAT = "dd MMM yyyy";
    
    /** Passenger types */
    public static final String PASSENGER_ADULT = "Adult";
    public static final String PASSENGER_CHILD = "Child";
    public static final String PASSENGER_INFANT = "Infant";
    
    /** Fare classes */
    public static final String FARE_SEAT = "Seat";
    public static final String FARE_SEAT_BAG = "Seat + Bag";
    public static final String FARE_WORKS = "The Works";
    public static final String FARE_WORKS_DELUXE = "Works Deluxe";
    
    /** Cabin classes */
    public static final String CABIN_ECONOMY = "Economy";
    public static final String CABIN_PREMIUM_ECONOMY = "Premium Economy";
    public static final String CABIN_BUSINESS = "Business Premier";
}