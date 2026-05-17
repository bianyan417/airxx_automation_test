package com.airxx.core.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.airxx.core.constants.FrameworkConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * JsonUtils - Utility class for JSON operations.
 * Provides methods to read, write, and manipulate JSON data.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class JsonUtils {
    
    private static final Logger logger = LogManager.getLogger(JsonUtils.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private JsonUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Reads JSON file and returns as JsonObject
     * @param filePath Path to JSON file
     * @return JsonObject representation
     */
    public static JsonObject readJsonFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", e.getMessage());
            throw new RuntimeException("JSON file read failed: " + filePath, e);
        }
    }
    
    /**
     * Reads JSON file from test data directory
     * @param fileName JSON file name
     * @return JsonObject representation
     */
    public static JsonObject readTestDataFile(String fileName) {
        String filePath = FrameworkConstants.TESTDATA_PATH + fileName;
        return readJsonFile(filePath);
    }
    
    /**
     * Reads JSON file and converts to specified class type
     * @param filePath Path to JSON file
     * @param clazz Target class type
     * @param <T> Generic type
     * @return Object of specified type
     */
    public static <T> T readJsonFile(String filePath, Class<T> clazz) {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", e.getMessage());
            throw new RuntimeException("JSON file read failed: " + filePath, e);
        }
    }
    
    /**
     * Reads JSON file and converts to list of specified type
     * @param filePath Path to JSON file
     * @param typeToken TypeToken for generic type
     * @param <T> Generic type
     * @return List of objects
     */
    public static <T> List<T> readJsonFileAsList(String filePath, TypeToken<List<T>> typeToken) {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, typeToken.getType());
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", e.getMessage());
            throw new RuntimeException("JSON file read failed: " + filePath, e);
        }
    }
    
    /**
     * Reads JSON array from file
     * @param filePath Path to JSON file
     * @return JsonArray
     */
    public static JsonArray readJsonArray(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonArray();
        } catch (IOException e) {
            logger.error("Failed to read JSON array: {}", e.getMessage());
            throw new RuntimeException("JSON array read failed: " + filePath, e);
        }
    }
    
    /**
     * Writes object as JSON to file
     * @param filePath Path to output file
     * @param object Object to serialize
     */
    public static void writeJsonFile(String filePath, Object object) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(object, writer);
            logger.debug("JSON written to file: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to write JSON file: {}", e.getMessage());
            throw new RuntimeException("JSON file write failed: " + filePath, e);
        }
    }
    
    /**
     * Converts object to JSON string
     * @param object Object to convert
     * @return JSON string
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }
    
    /**
     * Converts object to pretty-printed JSON string
     * @param object Object to convert
     * @return Pretty-printed JSON string
     */
    public static String toPrettyJson(Object object) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }
    
    /**
     * Parses JSON string to object of specified type
     * @param json JSON string
     * @param clazz Target class type
     * @param <T> Generic type
     * @return Object of specified type
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
    
    /**
     * Parses JSON string to JsonObject
     * @param json JSON string
     * @return JsonObject
     */
    public static JsonObject parseJson(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
    
    /**
     * Gets a specific value from JSON by key path
     * @param jsonObject Source JsonObject
     * @param keyPath Dot-separated key path (e.g., "user.address.city")
     * @return Value as String, or null if not found
     */
    public static String getValue(JsonObject jsonObject, String keyPath) {
        String[] keys = keyPath.split("\\.");
        JsonElement current = jsonObject;
        
        for (String key : keys) {
            if (current == null || !current.isJsonObject()) {
                return null;
            }
            current = current.getAsJsonObject().get(key);
        }
        
        return current != null && current.isJsonPrimitive() ? current.getAsString() : null;
    }
    
    /**
     * Gets a nested JsonObject by key path
     * @param jsonObject Source JsonObject
     * @param keyPath Dot-separated key path
     * @return Nested JsonObject, or null if not found
     */
    public static JsonObject getNestedObject(JsonObject jsonObject, String keyPath) {
        String[] keys = keyPath.split("\\.");
        JsonElement current = jsonObject;
        
        for (String key : keys) {
            if (current == null || !current.isJsonObject()) {
                return null;
            }
            current = current.getAsJsonObject().get(key);
        }
        
        return current != null && current.isJsonObject() ? current.getAsJsonObject() : null;
    }
    
    /**
     * Checks if JSON file exists
     * @param filePath Path to JSON file
     * @return true if file exists
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
    
    /**
     * Merges two JsonObjects
     * @param base Base JsonObject
     * @param override Override JsonObject (values take precedence)
     * @return Merged JsonObject
     */
    public static JsonObject mergeJsonObjects(JsonObject base, JsonObject override) {
        JsonObject merged = base.deepCopy();
        for (String key : override.keySet()) {
            merged.add(key, override.get(key));
        }
        return merged;
    }
}
