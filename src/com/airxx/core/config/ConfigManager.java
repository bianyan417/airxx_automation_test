package com.airxx.core.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.airxx.core.constants.FrameworkConstants;

/**
 * ConfigManager - Singleton class responsible for loading and managing configuration properties.
 * Supports multiple environments (qa, staging, prod) through property files.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class ConfigManager {
    
    private static ConfigManager instance;
    private Properties properties;
    private Properties seleniumProperties;
    private String environment;
    
    private ConfigManager() {
        properties = new Properties();
        seleniumProperties = new Properties();
        environment = System.getProperty("env", "qa");
        loadProperties();
    }
    
    /**
     * Returns the singleton instance of ConfigManager
     * @return ConfigManager instance
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Loads environment-specific properties and selenium properties
     */
    private void loadProperties() {
        try {
            // Load environment-specific properties
            String envPropertyPath = FrameworkConstants.CONFIG_PATH + environment + ".properties";
            FileInputStream envFile = new FileInputStream(envPropertyPath);
            properties.load(envFile);
            envFile.close();
            
            // Load selenium properties
            String seleniumPropertyPath = FrameworkConstants.CONFIG_PATH + "selenium.properties";
            FileInputStream seleniumFile = new FileInputStream(seleniumPropertyPath);
            seleniumProperties.load(seleniumFile);
            seleniumFile.close();
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties: " + e.getMessage());
        }
    }
    
    /**
     * Gets a property value from environment properties
     * @param key Property key
     * @return Property value
     */
    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        return properties.getProperty(key);
    }
    
    /**
     * Gets a property value with default fallback
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Gets a selenium-specific property
     * @param key Property key
     * @return Property value
     */
    public String getSeleniumProperty(String key) {
        return seleniumProperties.getProperty(key);
    }
    
    /**
     * Gets the base URL for the current environment
     * @return Base URL
     */
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    /**
     * Gets the API base URL for the current environment
     * @return API base URL
     */
    public String getApiBaseUrl() {
        return getProperty("api.base.url");
    }
    
    /**
     * Gets the browser type from configuration
     * @return Browser type (chrome, firefox, edge)
     */
    public String getBrowser() {
        String browser = System.getProperty("browser");
        return browser != null ? browser : getSeleniumProperty("browser");
    }
    
    /**
     * Checks if headless mode is enabled
     * @return true if headless mode is enabled
     */
    public boolean isHeadless() {
        return Boolean.parseBoolean(getSeleniumProperty("headless"));
    }
    
    /**
     * Gets the implicit wait timeout
     * @return Implicit wait timeout in seconds
     */
    public int getImplicitWait() {
        return Integer.parseInt(getSeleniumProperty("implicit.wait"));
    }
    
    /**
     * Gets the explicit wait timeout
     * @return Explicit wait timeout in seconds
     */
    public int getExplicitWait() {
        return Integer.parseInt(getSeleniumProperty("explicit.wait"));
    }
    
    /**
     * Gets the page load timeout
     * @return Page load timeout in seconds
     */
    public int getPageLoadTimeout() {
        return Integer.parseInt(getSeleniumProperty("page.load.timeout"));
    }
    
    /**
     * Gets the current environment name
     * @return Environment name
     */
    public String getEnvironment() {
        return environment;
    }
    
    /**
     * Reloads configuration properties (useful for dynamic config changes)
     */
    public void reloadProperties() {
        loadProperties();
    }
}
