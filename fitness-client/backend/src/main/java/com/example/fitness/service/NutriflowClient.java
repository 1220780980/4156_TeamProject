package com.example.fitness.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Client service for communicating with NutriFlow API.
 */
@Service
public class NutriflowClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${nutriflow.api.base-url:http://localhost:8080/api}")
    private String nutriflowBaseUrl;

    /**
     * Create a user in NutriFlow system.
     *
     * @param appUserId the ID of the app user
     * @param name user's name
     * @param height user's height
     * @param weight user's weight
     * @param age user's age
     * @param sex user's sex
     * @return the NutriFlow user ID
     */
    public Long createNutriflowUser(
            Long appUserId,
            String name,
            Object height,
            Object weight,
            Integer age,
            String sex) {

        String url = nutriflowBaseUrl + "/users";

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name != null ? name : "User");
        if (height != null) requestBody.put("height", height);
        if (weight != null) requestBody.put("weight", weight);
        if (age != null) requestBody.put("age", age);
        
        // Convert sex to match NutriFlow's SexType enum
        // NutriFlow expects: MALE, FEMALE, OTHER
        if (sex != null) {
            String sexUpper = sex.toUpperCase();
            if (sexUpper.equals("MALE") || sexUpper.equals("FEMALE") || sexUpper.equals("OTHER")) {
                requestBody.put("sex", sexUpper);
            }
        }

        // Create headers
        HttpHeaders headers = createHeaders(appUserId);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("userId")) {
                return ((Number) responseBody.get("userId")).longValue();
            }
            throw new RuntimeException("Failed to get userId from NutriFlow response");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user in NutriFlow: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getAIRecipeForUser(Long nutriflowUserId, Long appUserId) {
        String url = nutriflowBaseUrl + "/ai/recipes/user/" + nutriflowUserId;
        HttpHeaders headers = createHeaders(appUserId);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get AI recipe from NutriFlow: " + e.getMessage(), e);
        }
    }

    private HttpHeaders createHeaders(Long appUserId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Client-Id", "fitness-app");
        headers.set("X-End-User-Id", String.valueOf(appUserId));
        return headers;
    }
}
