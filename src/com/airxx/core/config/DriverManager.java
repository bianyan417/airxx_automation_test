package com.airxx.core.config;

import org.openqa.selenium.WebDriver;

import com.airxx.core.factory.BrowserFactory;

/**
 * DriverManager - Thread-safe WebDriver management using ThreadLocal.
 * Ensures each test thread has its own WebDriver instance for parallel execution.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class DriverManager {
    
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    private DriverManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Gets the WebDriver instance for the current thread
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    /**
     * Sets the WebDriver instance for the current thread
     * @param driver WebDriver instance to set
     */
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }
    
    /**
     * Initializes a new WebDriver instance based on configuration
     * @return Initialized WebDriver
     */
    public static WebDriver initDriver() {
        String browser = ConfigManager.getInstance().getBrowser();
        boolean headless = ConfigManager.getInstance().isHeadless();
        
        WebDriver driver = BrowserFactory.createDriver(browser, headless);
        setDriver(driver);
        
        configureDriver(driver);
        
        return driver;
    }
    
    /**
     * Initializes a WebDriver with specific browser
     * @param browser Browser type
     * @return Initialized WebDriver
     */
    public static WebDriver initDriver(String browser) {
        boolean headless = ConfigManager.getInstance().isHeadless();
        
        WebDriver driver = BrowserFactory.createDriver(browser, headless);
        setDriver(driver);
        
        configureDriver(driver);
        
        return driver;
    }
    
    /**
     * Configures the WebDriver with timeouts and window settings
     * @param driver WebDriver to configure
     */
    private static void configureDriver(WebDriver driver) {
        ConfigManager config = ConfigManager.getInstance();
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(
            java.time.Duration.ofSeconds(config.getImplicitWait())
        );
        
        // Set page load timeout
        driver.manage().timeouts().pageLoadTimeout(
            java.time.Duration.ofSeconds(config.getPageLoadTimeout())
        );
        
        // Maximize window
        driver.manage().window().maximize();
        
        // Delete all cookies
        driver.manage().deleteAllCookies();
    }
    
    /**
     * Quits the WebDriver and removes it from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error while quitting driver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
    
    /**
     * Checks if a WebDriver instance exists for the current thread
     * @return true if WebDriver exists
     */
    public static boolean hasDriver() {
        return driverThreadLocal.get() != null;
    }
    
    /**
     * Navigates to the base URL defined in configuration
     */
    public static void navigateToBaseUrl() {
        String baseUrl = ConfigManager.getInstance().getBaseUrl();
        getDriver().get(baseUrl);
    }
    
    /**
     * Navigates to a specific URL
     * @param url URL to navigate to
     */
    public static void navigateTo(String url) {
        getDriver().get(url);
    }
}
