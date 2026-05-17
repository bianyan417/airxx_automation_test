package com.airxx.core.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.airxx.core.constants.FrameworkConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * TestDataLoader - Utility class for loading and managing test data.
 * Supports scenario-based test data retrieval from JSON files.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class TestDataLoader {
    
    private static final Logger logger = LogManager.getLogger(TestDataLoader.class);
    private static final Map<String, JsonObject> dataCache = new HashMap<>();
    
    private TestDataLoader() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Loads test data from JSON file
     * @param fileName JSON file name
     * @return JsonObject containing test data
     */
    public static JsonObject loadTestData(String fileName) {
        if (dataCache.containsKey(fileName)) {
            logger.debug("Returning cached test data for: {}", fileName);
            return dataCache.get(fileName);
        }
        
        String filePath = FrameworkConstants.TESTDATA_PATH + fileName;
        JsonObject data = JsonUtils.readJsonFile(filePath);
        dataCache.put(fileName, data);
        logger.info("Loaded test data from: {}", fileName);
        
        return data;
    }
    
    /**
     * Gets login test data for a specific scenario
     * @param scenario Scenario name (e.g., "validLogin", "invalidPassword")
     * @return Map of test data key-value pairs
     */
    public static Map<String, String> getLoginData(String scenario) {
        JsonObject loginData = loadTestData(FrameworkConstants.LOGIN_DATA_FILE);
        JsonObject scenarioData = loginData.getAsJsonObject(scenario);
        
        if (scenarioData == null) {
            throw new RuntimeException("Login scenario not found: " + scenario);
        }
        
        return jsonObjectToMap(scenarioData);
    }
    
    /**
     * Gets booking test data for a specific scenario
     * @param scenario Scenario name
     * @return Map of test data key-value pairs
     */
    public static Map<String, String> getBookingData(String scenario) {
        JsonObject bookingData = loadTestData(FrameworkConstants.BOOKING_DATA_FILE);
        JsonObject scenarioData = bookingData.getAsJsonObject(scenario);
        
        if (scenarioData == null) {
            throw new RuntimeException("Booking scenario not found: " + scenario);
        }
        
        return jsonObjectToMap(scenarioData);
    }
    
    /**
     * Gets ticket test data for a specific scenario
     * @param scenario Scenario name
     * @return Map of test data key-value pairs
     */
    public static Map<String, String> getTicketData(String scenario) {
        JsonObject ticketData = loadTestData(FrameworkConstants.TICKET_DATA_FILE);
        JsonObject scenarioData = ticketData.getAsJsonObject(scenario);
        
        if (scenarioData == null) {
            throw new RuntimeException("Ticket scenario not found: " + scenario);
        }
        
        return jsonObjectToMap(scenarioData);
    }
    
    /**
     * Gets a specific field from test data
     * @param fileName JSON file name
     * @param scenario Scenario name
     * @param field Field name
     * @return Field value as String
     */
    public static String getField(String fileName, String scenario, String field) {
        JsonObject data = loadTestData(fileName);
        JsonObject scenarioData = data.getAsJsonObject(scenario);
        
        if (scenarioData == null || !scenarioData.has(field)) {
            throw new RuntimeException("Field not found: " + field + " in scenario: " + scenario);
        }
        
        return scenarioData.get(field).getAsString();
    }
    
    /**
     * Gets array data from test data file
     * @param fileName JSON file name
     * @param arrayKey Key of the array
     * @return JsonArray
     */
    public static JsonArray getArrayData(String fileName, String arrayKey) {
        JsonObject data = loadTestData(fileName);
        return data.getAsJsonArray(arrayKey);
    }
    
    /**
     * Gets nested object from test data
     * @param fileName JSON file name
     * @param path Dot-separated path
     * @return JsonObject
     */
    public static JsonObject getNestedData(String fileName, String path) {
        JsonObject data = loadTestData(fileName);
        return JsonUtils.getNestedObject(data, path);
    }
    
    /**
     * Converts JsonObject to Map<String, String>
     * @param jsonObject JsonObject to convert
     * @return Map representation
     */
    private static Map<String, String> jsonObjectToMap(JsonObject jsonObject) {
        Map<String, String> map = new HashMap<>();
        
        jsonObject.entrySet().forEach(entry -> {
            if (entry.getValue().isJsonPrimitive()) {
                map.put(entry.getKey(), entry.getValue().getAsString());
            } else if (entry.getValue().isJsonObject()) {
                // Handle nested objects as JSON string
                map.put(entry.getKey(), entry.getValue().toString());
            }
        });
        
        return map;
    }
    
    /**
     * Clears the test data cache
     */
    public static void clearCache() {
        dataCache.clear();
        logger.debug("Test data cache cleared");
    }
    
    /**
     * Reloads test data from file (bypassing cache)
     * @param fileName JSON file name
     * @return JsonObject containing test data
     */
    public static JsonObject reloadTestData(String fileName) {
        dataCache.remove(fileName);
        return loadTestData(fileName);
    }
    
    /**
     * Gets all scenarios from a test data file
     * @param fileName JSON file name
     * @return Array of scenario names
     */
    public static String[] getScenarios(String fileName) {
        JsonObject data = loadTestData(fileName);
        return data.keySet().toArray(new String[0]);
    }
    
    /**
     * Checks if a scenario exists in test data
     * @param fileName JSON file name
     * @param scenario Scenario name
     * @return true if scenario exists
     */
    public static boolean scenarioExists(String fileName, String scenario) {
        JsonObject data = loadTestData(fileName);
        return data.has(scenario);
    }
}
