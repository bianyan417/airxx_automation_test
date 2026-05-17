package com.airxx.core.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.airxx.core.config.DriverManager;
import com.airxx.core.utils.ScreenshotUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TestListener - TestNG listener for test lifecycle events.
 * Handles logging, screenshots on failure, and test reporting.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class TestListener implements ITestListener {
    
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    
    @Override
    public void onStart(ITestContext context) {
        logger.info("============================================");
        logger.info("Test Suite Started: {}", context.getName());
        logger.info("============================================");
    }
    
    @Override
    public void onFinish(ITestContext context) {
        logger.info("============================================");
        logger.info("Test Suite Finished: {}", context.getName());
        logger.info("Passed Tests: {}", context.getPassedTests().size());
        logger.info("Failed Tests: {}", context.getFailedTests().size());
        logger.info("Skipped Tests: {}", context.getSkippedTests().size());
        logger.info("============================================");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("--------------------------------------------");
        logger.info("Test Started: {}", getTestMethodName(result));
        logger.info("Description: {}", result.getMethod().getDescription());
        logger.info("--------------------------------------------");
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("✓ Test PASSED: {}", getTestMethodName(result));
        logger.info("Duration: {} ms", getTestDuration(result));
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("✗ Test FAILED: {}", getTestMethodName(result));
        logger.error("Failure Reason: {}", result.getThrowable().getMessage());
        
        // Capture screenshot on failure
        if (DriverManager.hasDriver()) {
            try {
                String screenshotPath = ScreenshotUtils.captureScreenshot(
                    DriverManager.getDriver(),
                    getTestMethodName(result)
                );
                logger.info("Screenshot captured: {}", screenshotPath);
            } catch (Exception e) {
                logger.error("Failed to capture screenshot: {}", e.getMessage());
            }
        }
        
        // Log stack trace
        logger.error("Stack Trace:", result.getThrowable());
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⊘ Test SKIPPED: {}", getTestMethodName(result));
        if (result.getThrowable() != null) {
            logger.warn("Skip Reason: {}", result.getThrowable().getMessage());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test Failed within Success Percentage: {}", getTestMethodName(result));
    }
    
    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        logger.error("✗ Test FAILED with Timeout: {}", getTestMethodName(result));
        onTestFailure(result);
    }
    
    /**
     * Gets the fully qualified test method name
     * @param result Test result
     * @return Test method name in format ClassName.methodName
     */
    private String getTestMethodName(ITestResult result) {
        return result.getTestClass().getName() + "." + result.getMethod().getMethodName();
    }
    
    /**
     * Calculates test duration
     * @param result Test result
     * @return Duration in milliseconds
     */
    private long getTestDuration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
