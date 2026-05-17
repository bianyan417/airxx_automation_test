package com.airxx.core.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.airxx.core.constants.FrameworkConstants;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ScreenshotUtils - Utility class for capturing and managing screenshots.
 * Supports full page and element-level screenshots.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class ScreenshotUtils {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    
    private ScreenshotUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Captures screenshot and saves to default location
     * @param driver WebDriver instance
     * @param screenshotName Screenshot name (without extension)
     * @return Path to saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        return captureScreenshot(driver, screenshotName, FrameworkConstants.SCREENSHOTS_PATH);
    }
    
    /**
     * Captures screenshot and saves to specified location
     * @param driver WebDriver instance
     * @param screenshotName Screenshot name (without extension)
     * @param destinationPath Destination directory path
     * @return Path to saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName, String destinationPath) {
        try {
            // Create directory if it doesn't exist
            createDirectoryIfNotExists(destinationPath);
            
            // Generate unique filename with timestamp
            String timestamp = new SimpleDateFormat(FrameworkConstants.REPORT_DATE_FORMAT).format(new Date());
            String fileName = sanitizeFileName(screenshotName) + "_" + timestamp + "." + FrameworkConstants.SCREENSHOT_FORMAT;
            String fullPath = destinationPath + fileName;
            
            // Capture screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(fullPath);
            
            FileUtils.copyFile(source, destination);
            
            logger.info("Screenshot captured: {}", fullPath);
            return fullPath;
            
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
            throw new RuntimeException("Screenshot capture failed", e);
        }
    }
    
    /**
     * Captures screenshot as Base64 encoded string
     * @param driver WebDriver instance
     * @return Base64 encoded screenshot
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        return ts.getScreenshotAs(OutputType.BASE64);
    }
    
    /**
     * Captures screenshot as byte array
     * @param driver WebDriver instance
     * @return Screenshot as byte array
     */
    public static byte[] captureScreenshotAsBytes(WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        return ts.getScreenshotAs(OutputType.BYTES);
    }
    
    /**
     * Captures screenshot of a specific element
     * @param element WebElement to capture
     * @param screenshotName Screenshot name (without extension)
     * @return Path to saved screenshot
     */
    public static String captureElementScreenshot(WebElement element, String screenshotName) {
        try {
            String destinationPath = FrameworkConstants.SCREENSHOTS_PATH;
            createDirectoryIfNotExists(destinationPath);
            
            String timestamp = new SimpleDateFormat(FrameworkConstants.REPORT_DATE_FORMAT).format(new Date());
            String fileName = sanitizeFileName(screenshotName) + "_element_" + timestamp + "." + FrameworkConstants.SCREENSHOT_FORMAT;
            String fullPath = destinationPath + fileName;
            
            File source = element.getScreenshotAs(OutputType.FILE);
            File destination = new File(fullPath);
            
            FileUtils.copyFile(source, destination);
            
            logger.info("Element screenshot captured: {}", fullPath);
            return fullPath;
            
        } catch (IOException e) {
            logger.error("Failed to capture element screenshot: {}", e.getMessage());
            throw new RuntimeException("Element screenshot capture failed", e);
        }
    }
    
    /**
     * Captures element screenshot as Base64 encoded string
     * @param element WebElement to capture
     * @return Base64 encoded screenshot
     */
    public static String captureElementScreenshotAsBase64(WebElement element) {
        return element.getScreenshotAs(OutputType.BASE64);
    }
    
    /**
     * Converts a screenshot file to Base64 string
     * @param filePath Path to screenshot file
     * @return Base64 encoded string
     */
    public static String convertToBase64(String filePath) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            logger.error("Failed to convert screenshot to Base64: {}", e.getMessage());
            throw new RuntimeException("Base64 conversion failed", e);
        }
    }
    
    /**
     * Creates directory if it doesn't exist
     * @param directoryPath Directory path to create
     */
    private static void createDirectoryIfNotExists(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.debug("Created directory: {}", directoryPath);
            } catch (IOException e) {
                logger.error("Failed to create directory: {}", e.getMessage());
                throw new RuntimeException("Directory creation failed", e);
            }
        }
    }
    
    /**
     * Sanitizes filename by removing invalid characters
     * @param fileName Original filename
     * @return Sanitized filename
     */
    private static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }
    
    /**
     * Deletes old screenshots older than specified days
     * @param daysOld Number of days to keep
     */
    public static void cleanupOldScreenshots(int daysOld) {
        File screenshotDir = new File(FrameworkConstants.SCREENSHOTS_PATH);
        if (screenshotDir.exists() && screenshotDir.isDirectory()) {
            File[] files = screenshotDir.listFiles();
            if (files != null) {
                long cutoffTime = System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000L);
                for (File file : files) {
                    if (file.isFile() && file.lastModified() < cutoffTime) {
                        if (file.delete()) {
                            logger.debug("Deleted old screenshot: {}", file.getName());
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Gets the screenshots directory path
     * @return Screenshots directory path
     */
    public static String getScreenshotsDirectory() {
        return FrameworkConstants.SCREENSHOTS_PATH;
    }
}
