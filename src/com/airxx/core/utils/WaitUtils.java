package com.airxx.core.utils;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.airxx.core.constants.FrameworkConstants;

/**
 * WaitUtils - Utility class for various wait operations.
 * Provides explicit, implicit, fluent waits and custom wait conditions.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class WaitUtils {
    
    private WaitUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Waits for element to be visible
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement when visible
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Waits for element to be visible with default timeout
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return WebElement when visible
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        return waitForElementVisible(driver, locator, FrameworkConstants.DEFAULT_EXPLICIT_WAIT);
    }
    
    /**
     * Waits for element to be clickable
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement when clickable
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Waits for element to be clickable with default timeout
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return WebElement when clickable
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        return waitForElementClickable(driver, locator, FrameworkConstants.DEFAULT_EXPLICIT_WAIT);
    }
    
    /**
     * Waits for element to be present in DOM
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement when present
     */
    public static WebElement waitForElementPresent(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Waits for element to be invisible/disappear
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return true when element is invisible
     */
    public static boolean waitForElementInvisible(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Waits for all elements to be visible
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return List of visible WebElements
     */
    public static List<WebElement> waitForAllElementsVisible(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
    
    /**
     * Waits for text to be present in element
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param text Text to wait for
     * @param timeoutInSeconds Timeout in seconds
     * @return true when text is present
     */
    public static boolean waitForTextPresent(WebDriver driver, By locator, String text, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    /**
     * Waits for URL to contain specific text
     * @param driver WebDriver instance
     * @param urlPart URL part to wait for
     * @param timeoutInSeconds Timeout in seconds
     * @return true when URL contains the text
     */
    public static boolean waitForUrlContains(WebDriver driver, String urlPart, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.urlContains(urlPart));
    }
    
    /**
     * Waits for page title to contain specific text
     * @param driver WebDriver instance
     * @param titlePart Title part to wait for
     * @param timeoutInSeconds Timeout in seconds
     * @return true when title contains the text
     */
    public static boolean waitForTitleContains(WebDriver driver, String titlePart, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.titleContains(titlePart));
    }
    
    /**
     * Waits for element attribute to have specific value
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param attribute Attribute name
     * @param value Expected value
     * @param timeoutInSeconds Timeout in seconds
     * @return true when attribute has the value
     */
    public static boolean waitForAttributeValue(WebDriver driver, By locator, String attribute, 
                                                  String value, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }
    
    /**
     * Fluent wait with custom polling interval
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @param pollingInMillis Polling interval in milliseconds
     * @return WebElement when found
     */
    public static WebElement fluentWait(WebDriver driver, By locator, int timeoutInSeconds, int pollingInMillis) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(timeoutInSeconds))
            .pollingEvery(Duration.ofMillis(pollingInMillis))
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class);
        
        return wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });
    }
    
    /**
     * Waits for page to fully load (JavaScript document.readyState)
     * @param driver WebDriver instance
     * @param timeoutInSeconds Timeout in seconds
     */
    public static void waitForPageLoad(WebDriver driver, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete");
            }
        });
    }
    
    /**
     * Waits for AJAX calls to complete (jQuery)
     * @param driver WebDriver instance
     * @param timeoutInSeconds Timeout in seconds
     */
    public static void waitForAjax(WebDriver driver, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("return jQuery.active == 0");
            }
        });
    }
    
    /**
     * Hard wait (Thread.sleep) - Use sparingly
     * @param milliseconds Time to wait in milliseconds
     */
    public static void hardWait(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Waits for frame and switches to it
     * @param driver WebDriver instance
     * @param locator Frame locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebDriver after switching to frame
     */
    public static WebDriver waitForFrameAndSwitch(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }
    
    /**
     * Waits for alert and switches to it
     * @param driver WebDriver instance
     * @param timeoutInSeconds Timeout in seconds
     * @return Alert object
     */
    public static org.openqa.selenium.Alert waitForAlert(WebDriver driver, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.alertIsPresent());
    }
    
    /**
     * Waits for number of windows/tabs to be specific count
     * @param driver WebDriver instance
     * @param numberOfWindows Expected number of windows
     * @param timeoutInSeconds Timeout in seconds
     * @return true when window count matches
     */
    public static boolean waitForNumberOfWindows(WebDriver driver, int numberOfWindows, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
    }
}
