package com.airxx.pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.airxx.core.config.ConfigManager;
import com.airxx.core.constants.FrameworkConstants;
import com.airxx.core.utils.WaitUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BasePage - Base class for all Page Objects.
 * Contains common methods and utilities shared across all pages.
 * Implements Page Object Model design pattern.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor jsExecutor;
    protected final Logger logger = LogManager.getLogger(this.getClass());
    
    /**
     * Constructor initializing common page components
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getInstance().getExplicitWait()));
        this.actions = new Actions(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }
    
    // ===================== CLICK OPERATIONS =====================
    
    /**
     * Clicks on element after waiting for it to be clickable
     * @param locator Element locator
     */
    protected void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        highlightElement(element);
        element.click();
        logger.debug("Clicked on element: {}", locator);
    }
    
    /**
     * Clicks on WebElement after ensuring it's clickable
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        highlightElement(element);
        element.click();
        logger.debug("Clicked on element: {}", element);
    }
    
    /**
     * Clicks element using JavaScript (useful for hidden elements)
     * @param locator Element locator
     */
    protected void jsClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        jsExecutor.executeScript("arguments[0].click();", element);
        logger.debug("JS clicked on element: {}", locator);
    }
    
    /**
     * Clicks element using JavaScript
     * @param element WebElement to click
     */
    protected void jsClick(WebElement element) {
        jsExecutor.executeScript("arguments[0].click();", element);
        logger.debug("JS clicked on element: {}", element);
    }
    
    /**
     * Double-clicks on element
     * @param locator Element locator
     */
    protected void doubleClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        actions.doubleClick(element).perform();
        logger.debug("Double-clicked on element: {}", locator);
    }
    
    /**
     * Right-clicks on element
     * @param locator Element locator
     */
    protected void rightClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        actions.contextClick(element).perform();
        logger.debug("Right-clicked on element: {}", locator);
    }
    
    // ===================== TYPE OPERATIONS =====================
    
    /**
     * Types text into element after clearing existing text
     * @param locator Element locator
     * @param text Text to type
     */
    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        highlightElement(element);
        element.clear();
        element.sendKeys(text);
        logger.debug("Typed '{}' into element: {}", text, locator);
    }
    
    /**
     * Types text into WebElement after clearing
     * @param element WebElement
     * @param text Text to type
     */
    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        highlightElement(element);
        element.clear();
        element.sendKeys(text);
        logger.debug("Typed '{}' into element", text);
    }
    
    /**
     * Types text using JavaScript
     * @param locator Element locator
     * @param text Text to type
     */
    protected void jsType(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        jsExecutor.executeScript("arguments[0].value=arguments[1];", element, text);
        logger.debug("JS typed '{}' into element: {}", text, locator);
    }
    
    /**
     * Sends keys to element
     * @param locator Element locator
     * @param keys Keys to send
     */
    protected void sendKeys(By locator, Keys keys) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.sendKeys(keys);
        logger.debug("Sent keys to element: {}", locator);
    }
    
    // ===================== GET OPERATIONS =====================
    
    /**
     * Gets visible text from element
     * @param locator Element locator
     * @return Text content
     */
    protected String getText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText();
    }
    
    /**
     * Gets visible text from WebElement
     * @param element WebElement
     * @return Text content
     */
    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }
    
    /**
     * Gets attribute value from element
     * @param locator Element locator
     * @param attribute Attribute name
     * @return Attribute value
     */
    protected String getAttribute(By locator, String attribute) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return element.getAttribute(attribute);
    }
    
    /**
     * Gets value attribute from input element
     * @param locator Element locator
     * @return Input value
     */
    protected String getValue(By locator) {
        return getAttribute(locator, "value");
    }
    
    // ===================== ELEMENT STATE CHECKS =====================
    
    /**
     * Checks if element is displayed
     * @param locator Element locator
     * @return true if element is displayed
     */
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Checks if element is enabled
     * @param locator Element locator
     * @return true if element is enabled
     */
    protected boolean isEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Checks if element is selected (checkbox/radio)
     * @param locator Element locator
     * @return true if element is selected
     */
    protected boolean isSelected(By locator) {
        try {
            return driver.findElement(locator).isSelected();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Checks if element exists in DOM
     * @param locator Element locator
     * @return true if element exists
     */
    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // ===================== DROPDOWN OPERATIONS =====================
    
    /**
     * Selects dropdown option by visible text
     * @param locator Dropdown locator
     * @param text Option text
     */
    protected void selectByVisibleText(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        Select select = new Select(element);
        select.selectByVisibleText(text);
        logger.debug("Selected '{}' from dropdown: {}", text, locator);
    }
    
    /**
     * Selects dropdown option by value
     * @param locator Dropdown locator
     * @param value Option value
     */
    protected void selectByValue(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        Select select = new Select(element);
        select.selectByValue(value);
        logger.debug("Selected value '{}' from dropdown: {}", value, locator);
    }
    
    /**
     * Selects dropdown option by index
     * @param locator Dropdown locator
     * @param index Option index
     */
    protected void selectByIndex(By locator, int index) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        Select select = new Select(element);
        select.selectByIndex(index);
        logger.debug("Selected index {} from dropdown: {}", index, locator);
    }
    
    /**
     * Gets selected option text from dropdown
     * @param locator Dropdown locator
     * @return Selected option text
     */
    protected String getSelectedOption(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText();
    }
    
    // ===================== SCROLL OPERATIONS =====================
    
    /**
     * Scrolls element into view
     * @param locator Element locator
     */
    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        logger.debug("Scrolled to element: {}", locator);
    }
    
    /**
     * Scrolls element into view
     * @param element WebElement
     */
    protected void scrollToElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }
    
    /**
     * Scrolls to top of page
     */
    protected void scrollToTop() {
        jsExecutor.executeScript("window.scrollTo(0, 0);");
    }
    
    /**
     * Scrolls to bottom of page
     */
    protected void scrollToBottom() {
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
    
    // ===================== HOVER OPERATIONS =====================
    
    /**
     * Hovers over element
     * @param locator Element locator
     */
    protected void hover(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        actions.moveToElement(element).perform();
        logger.debug("Hovered over element: {}", locator);
    }
    
    /**
     * Hovers over WebElement
     * @param element WebElement
     */
    protected void hover(WebElement element) {
        actions.moveToElement(element).perform();
    }
    
    // ===================== WAIT OPERATIONS =====================
    
    /**
     * Waits for element to be visible
     * @param locator Element locator
     * @return WebElement when visible
     */
    protected WebElement waitForVisible(By locator) {
        return WaitUtils.waitForElementVisible(driver, locator);
    }
    
    /**
     * Waits for element to be clickable
     * @param locator Element locator
     * @return WebElement when clickable
     */
    protected WebElement waitForClickable(By locator) {
        return WaitUtils.waitForElementClickable(driver, locator);
    }
    
    /**
     * Waits for page to fully load
     */
    protected void waitForPageLoad() {
        WaitUtils.waitForPageLoad(driver, FrameworkConstants.DEFAULT_PAGE_LOAD_TIMEOUT);
    }
    
    // ===================== UTILITY OPERATIONS =====================
    
    /**
     * Finds element
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Finds all elements
     * @param locator Element locator
     * @return List of WebElements
     */
    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
    
    /**
     * Gets current page URL
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Gets current page title
     * @return Page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Refreshes current page
     */
    protected void refreshPage() {
        driver.navigate().refresh();
        waitForPageLoad();
    }
    
    /**
     * Navigates back
     */
    protected void navigateBack() {
        driver.navigate().back();
    }
    
    /**
     * Highlights element for debugging
     * @param element WebElement to highlight
     */
    protected void highlightElement(WebElement element) {
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red; background: yellow;');", element);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        jsExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, originalStyle);
    }
    
    /**
     * Accepts alert dialog
     */
    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }
    
    /**
     * Dismisses alert dialog
     */
    protected void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
    }
    
    /**
     * Gets alert text
     * @return Alert text
     */
    protected String getAlertText() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }
    
    /**
     * Switches to frame
     * @param locator Frame locator
     */
    protected void switchToFrame(By locator) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }
    
    /**
     * Switches to default content
     */
    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }
    
    /**
     * Abstract method to verify page has loaded
     * Must be implemented by each page class
     * @return true if page is loaded
     */
    public abstract boolean isPageLoaded();
}
