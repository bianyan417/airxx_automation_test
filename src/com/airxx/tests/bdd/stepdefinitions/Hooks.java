package com.airxx.tests.bdd.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.airxx.core.config.DriverManager;
import com.airxx.core.utils.ScreenshotUtils;

/**
 * Hooks - Cucumber hooks for test lifecycle management.
 * Handles setup and teardown for each scenario.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class Hooks {
    
    private static final Logger logger = LogManager.getLogger(Hooks.class);
    
    /**
     * Runs before each scenario - initializes WebDriver
     * @param scenario Current scenario
     */
    @Before(order = 0)
    public void setUp(Scenario scenario) {
        logger.info("========================================");
        logger.info("Starting Scenario: {}", scenario.getName());
        logger.info("Tags: {}", scenario.getSourceTagNames());
        logger.info("========================================");
        
        // Initialize WebDriver
        DriverManager.initDriver();
        logger.info("WebDriver initialized");
    }
    
    /**
     * Runs before UI scenarios specifically
     * @param scenario Current scenario
     */
    @Before(value = "@ui", order = 1)
    public void setUpUI(Scenario scenario) {
        logger.info("Setting up UI test: {}", scenario.getName());
        // Additional UI-specific setup if needed
    }
    
    /**
     * Runs before API scenarios specifically
     * @param scenario Current scenario
     */
    @Before(value = "@api", order = 1)
    public void setUpAPI(Scenario scenario) {
        logger.info("Setting up API test: {}", scenario.getName());
        // Skip WebDriver initialization for API tests
        if (DriverManager.hasDriver()) {
            DriverManager.quitDriver();
        }
    }
    
    /**
     * Runs after each step - captures screenshot on failure
     * @param scenario Current scenario
     */
    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed() && DriverManager.hasDriver()) {
            try {
                // Capture screenshot as Base64 and attach to scenario
                byte[] screenshot = ScreenshotUtils.captureScreenshotAsBytes(DriverManager.getDriver());
                scenario.attach(screenshot, "image/png", "Screenshot on failure");
                logger.info("Screenshot captured for failed step");
            } catch (Exception e) {
                logger.error("Failed to capture screenshot: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Runs after each scenario - cleanup
     * @param scenario Current scenario
     */
    @After(order = 0)
    public void tearDown(Scenario scenario) {
        logger.info("----------------------------------------");
        logger.info("Scenario Finished: {}", scenario.getName());
        logger.info("Status: {}", scenario.getStatus());
        logger.info("----------------------------------------");
        
        // Capture final screenshot for failed scenarios
        if (scenario.isFailed() && DriverManager.hasDriver()) {
            try {
                String screenshotName = scenario.getName().replaceAll(" ", "_");
                String screenshotPath = ScreenshotUtils.captureScreenshot(
                    DriverManager.getDriver(),
                    screenshotName
                );
                logger.info("Final screenshot saved: {}", screenshotPath);
                
                // Also attach to Cucumber report
                byte[] screenshot = ScreenshotUtils.captureScreenshotAsBytes(DriverManager.getDriver());
                scenario.attach(screenshot, "image/png", "Final Screenshot");
            } catch (Exception e) {
                logger.error("Failed to capture final screenshot: {}", e.getMessage());
            }
        }
        
        // Quit WebDriver
        if (DriverManager.hasDriver()) {
            DriverManager.quitDriver();
            logger.info("WebDriver closed");
        }
    }
    
    /**
     * Runs after UI scenarios specifically
     * @param scenario Current scenario
     */
    @After(value = "@ui", order = 1)
    public void tearDownUI(Scenario scenario) {
        logger.info("Cleaning up UI test: {}", scenario.getName());
    }
    
    /**
     * Runs after API scenarios specifically
     * @param scenario Current scenario
     */
    @After(value = "@api", order = 1)
    public void tearDownAPI(Scenario scenario) {
        logger.info("Cleaning up API test: {}", scenario.getName());
    }
    
    /**
     * Runs after smoke test scenarios
     * @param scenario Current scenario
     */
    @After(value = "@smoke")
    public void afterSmokeTest(Scenario scenario) {
        logger.info("Smoke test completed: {} - {}", scenario.getName(), scenario.getStatus());
    }
    
    /**
     * Runs after regression test scenarios
     * @param scenario Current scenario
     */
    @After(value = "@regression")
    public void afterRegressionTest(Scenario scenario) {
        logger.info("Regression test completed: {} - {}", scenario.getName(), scenario.getStatus());
    }
}
