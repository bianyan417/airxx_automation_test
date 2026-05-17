package com.airxx.core.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * BrowserFactory - Factory class for creating WebDriver instances.
 * Supports Chrome, Firefox, Edge, and Safari browsers.
 * Uses WebDriverManager for automatic driver management.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class BrowserFactory {
    
    private BrowserFactory() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Creates a WebDriver instance based on browser type
     * @param browser Browser type (chrome, firefox, edge, safari)
     * @param headless Whether to run in headless mode
     * @return WebDriver instance
     */
    public static WebDriver createDriver(String browser, boolean headless) {
        WebDriver driver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                driver = createChromeDriver(headless);
                break;
            case "firefox":
                driver = createFirefoxDriver(headless);
                break;
            case "edge":
                driver = createEdgeDriver(headless);
                break;
            case "safari":
                driver = createSafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        
        return driver;
    }
    
    /**
     * Creates Chrome WebDriver with configured options
     * @param headless Whether to run in headless mode
     * @return ChromeDriver instance
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Common options for stability
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        
        // Headless mode
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        // Disable automation flags
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        return new ChromeDriver(options);
    }
    
    /**
     * Creates Firefox WebDriver with configured options
     * @param headless Whether to run in headless mode
     * @return FirefoxDriver instance
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        
        // Common options
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        // Headless mode
        if (headless) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Creates Edge WebDriver with configured options
     * @param headless Whether to run in headless mode
     * @return EdgeDriver instance
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        
        // Common options
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=1920,1080");
        
        // Headless mode
        if (headless) {
            options.addArguments("--headless");
        }
        
        return new EdgeDriver(options);
    }
    
    /**
     * Creates Safari WebDriver
     * Note: Safari does not support headless mode
     * @return SafariDriver instance
     */
    private static WebDriver createSafariDriver() {
        // Safari requires SafariDriver to be enabled via safaridriver --enable
        return new SafariDriver();
    }
    
    /**
     * Creates a Chrome driver optimized for mobile testing
     * @param deviceName Mobile device name to emulate
     * @param headless Whether to run in headless mode
     * @return ChromeDriver with mobile emulation
     */
    public static WebDriver createMobileDriver(String deviceName, boolean headless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        java.util.Map<String, String> mobileEmulation = new java.util.HashMap<>();
        mobileEmulation.put("deviceName", deviceName);
        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        return new ChromeDriver(options);
    }
}
