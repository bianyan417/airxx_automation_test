package com.airxx.api.client;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.airxx.core.config.ConfigManager;
import com.airxx.core.constants.FrameworkConstants;

import java.util.Map;

/**
 * ApiClient - REST API client using RestAssured.
 * Provides methods for making HTTP requests (GET, POST, PUT, DELETE, PATCH).
 * Handles authentication, headers, and response parsing.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
public class ApiClient {
    
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private RequestSpecification requestSpec;
    private ResponseSpecification responseSpec;
    private String baseUri;
    private String authToken;
    
    /**
     * Default constructor - initializes with config base URL
     */
    public ApiClient() {
        this.baseUri = ConfigManager.getInstance().getApiBaseUrl();
        initializeSpecs();
    }
    
    /**
     * Constructor with custom base URI
     * @param baseUri API base URI
     */
    public ApiClient(String baseUri) {
        this.baseUri = baseUri;
        initializeSpecs();
    }
    
    /**
     * Initializes request and response specifications
     */
    private void initializeSpecs() {
        requestSpec = new RequestSpecBuilder()
            .setBaseUri(baseUri)
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
        
        responseSpec = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();
        
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        logger.info("ApiClient initialized with base URI: {}", baseUri);
    }
    
    /**
     * Sets authentication token for subsequent requests
     * @param token Bearer token
     * @return ApiClient instance for chaining
     */
    public ApiClient setAuthToken(String token) {
        this.authToken = token;
        requestSpec = new RequestSpecBuilder()
            .addRequestSpecification(requestSpec)
            .addHeader("Authorization", "Bearer " + token)
            .build();
        logger.info("Auth token set");
        return this;
    }
    
    /**
     * Sets API key authentication
     * @param apiKey API key
     * @param headerName Header name for API key
     * @return ApiClient instance for chaining
     */
    public ApiClient setApiKey(String apiKey, String headerName) {
        requestSpec = new RequestSpecBuilder()
            .addRequestSpecification(requestSpec)
            .addHeader(headerName, apiKey)
            .build();
        logger.info("API key set");
        return this;
    }
    
    /**
     * Adds custom header to requests
     * @param name Header name
     * @param value Header value
     * @return ApiClient instance for chaining
     */
    public ApiClient addHeader(String name, String value) {
        requestSpec = new RequestSpecBuilder()
            .addRequestSpecification(requestSpec)
            .addHeader(name, value)
            .build();
        return this;
    }
    
    /**
     * Adds multiple headers to requests
     * @param headers Map of headers
     * @return ApiClient instance for chaining
     */
    public ApiClient addHeaders(Map<String, String> headers) {
        requestSpec = new RequestSpecBuilder()
            .addRequestSpecification(requestSpec)
            .addHeaders(headers)
            .build();
        return this;
    }
    
    /**
     * Performs GET request
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response get(String endpoint) {
        logger.info("GET request to: {}", endpoint);
        return RestAssured.given()
            .spec(requestSpec)
            .when()
            .get(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs GET request with query parameters
     * @param endpoint API endpoint
     * @param queryParams Query parameters
     * @return Response object
     */
    public Response get(String endpoint, Map<String, ?> queryParams) {
        logger.info("GET request to: {} with params: {}", endpoint, queryParams);
        return RestAssured.given()
            .spec(requestSpec)
            .queryParams(queryParams)
            .when()
            .get(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs GET request with path parameter
     * @param endpoint API endpoint with placeholder
     * @param pathParam Path parameter value
     * @return Response object
     */
    public Response getWithPathParam(String endpoint, Object pathParam) {
        logger.info("GET request to: {} with path param: {}", endpoint, pathParam);
        return RestAssured.given()
            .spec(requestSpec)
            .when()
            .get(endpoint, pathParam)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs POST request with body
     * @param endpoint API endpoint
     * @param body Request body object
     * @return Response object
     */
    public Response post(String endpoint, Object body) {
        logger.info("POST request to: {}", endpoint);
        return RestAssured.given()
            .spec(requestSpec)
            .body(body)
            .when()
            .post(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs POST request with JSON string body
     * @param endpoint API endpoint
     * @param jsonBody JSON body string
     * @return Response object
     */
    public Response post(String endpoint, String jsonBody) {
        logger.info("POST request to: {}", endpoint);
        return RestAssured.given()
            .spec(requestSpec)
            .body(jsonBody)
            .when()
            .post(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs POST request without body
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response post(String endpoint) {
        logger.info("POST request to: {} (no body)", endpoint);
        return RestAssured.given()
            .spec(requestSpec)
            .when()
            .post(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs PUT request with body
     * @param endpoint API endpoint
     * @param body Request body object
     * @return Response object
     */
    public Response put(String endpoint, Object body) {
        logger.info("PUT request to: {}", endpoint);
        return RestAssured.given()
            .spec(requestSpec)
            .body(body)
            .when()
            .put(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs PUT request with path parameter
     * @param endpoint API endpoint
     * @param pathParam Path parameter value
     * @param body Request body object
     * @return Response object
     */
    public Response put(String endpoint, Object pathParam, Object body) {
        logger.info("PUT request to: {} with path param: {}", endpoint, pathParam);
        return RestAssured.given()
            .spec(requestSpec)
            .body(body)
            .when()
            .put(endpoint, pathParam)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs PATCH request with body
     * @param endpoint API endpoint
     * @param body Request body object
     * @return Response object
     */
    public Response patch(String endpoint, Object body) {
        logger.info("PATCH request to: {}", endpoint);
        return RestAssured.given()
            .spec(requestSpec)
            .body(body)
            .when()
            .patch(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs DELETE request
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response delete(String endpoint) {
        logger.info("DELETE request to: {}", endpoint);
        return RestAssured.given()
            .spec(requestSpec)
            .when()
            .delete(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs DELETE request with path parameter
     * @param endpoint API endpoint
     * @param pathParam Path parameter value
     * @return Response object
     */
    public Response delete(String endpoint, Object pathParam) {
        logger.info("DELETE request to: {} with path param: {}", endpoint, pathParam);
        return RestAssured.given()
            .spec(requestSpec)
            .when()
            .delete(endpoint, pathParam)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs POST request with form parameters
     * @param endpoint API endpoint
     * @param formParams Form parameters
     * @return Response object
     */
    public Response postForm(String endpoint, Map<String, String> formParams) {
        logger.info("POST form request to: {}", endpoint);
        return RestAssured.given()
            .spec(requestSpec)
            .contentType(ContentType.URLENC)
            .formParams(formParams)
            .when()
            .post(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Performs multipart file upload
     * @param endpoint API endpoint
     * @param filePath Path to file
     * @param controlName Form control name
     * @return Response object
     */
    public Response uploadFile(String endpoint, String filePath, String controlName) {
        logger.info("File upload to: {}", endpoint);
        return RestAssured.given()
            .spec(requestSpec)
            .contentType(ContentType.MULTIPART)
            .multiPart(controlName, new java.io.File(filePath))
            .when()
            .post(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Gets response status code
     * @param response Response object
     * @return Status code
     */
    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }
    
    /**
     * Gets response body as string
     * @param response Response object
     * @return Body string
     */
    public static String getBody(Response response) {
        return response.getBody().asString();
    }
    
    /**
     * Gets response body as specified type
     * @param response Response object
     * @param clazz Target class type
     * @param <T> Generic type
     * @return Deserialized object
     */
    public static <T> T getBodyAs(Response response, Class<T> clazz) {
        return response.getBody().as(clazz);
    }
    
    /**
     * Gets header value from response
     * @param response Response object
     * @param headerName Header name
     * @return Header value
     */
    public static String getHeader(Response response, String headerName) {
        return response.getHeader(headerName);
    }
    
    /**
     * Gets JSON path value from response
     * @param response Response object
     * @param jsonPath JSON path expression
     * @param <T> Return type
     * @return Value at JSON path
     */
    public static <T> T getJsonPathValue(Response response, String jsonPath) {
        return response.jsonPath().get(jsonPath);
    }
    
    /**
     * Gets response time in milliseconds
     * @param response Response object
     * @return Response time
     */
    public static long getResponseTime(Response response) {
        return response.getTime();
    }
    
    /**
     * Validates response status code
     * @param response Response object
     * @param expectedStatus Expected status code
     * @return true if status matches
     */
    public static boolean validateStatus(Response response, int expectedStatus) {
        return response.getStatusCode() == expectedStatus;
    }
    
    /**
     * Gets base URI
     * @return Base URI
     */
    public String getBaseUri() {
        return baseUri;
    }
    
    /**
     * Resets client to initial state
     */
    public void reset() {
        this.authToken = null;
        initializeSpecs();
        logger.info("ApiClient reset to initial state");
    }
}
