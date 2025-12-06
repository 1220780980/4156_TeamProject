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
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Client service for communicating with NutriFlow API.
 */
@Service
public final class NutriflowClient {

    /** REST template for making HTTP calls. */
    @Autowired
    private RestTemplate restTemplate;

    /** Base URL for NutriFlow API. */
    @Value("${nutriflow.api.base-url:http://localhost:8080/api}")
    private String nutriflowBaseUrl;

    /**
     * Create HTTP headers for NutriFlow API calls.
     *
     * @param appUserId the app user ID for tracking
     * @return configured HTTP headers
     */
    private HttpHeaders createHeaders(final Long appUserId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Client-Id", "fitness-app");

        if (appUserId != null) {
            headers.set("X-End-User-Id", String.valueOf(appUserId));
        }
        return headers;
    }

    /**
     * Check whether a NutriFlow user exists.
     *
     * @param userId NutriFlow user ID
     * @return true if user exists, otherwise false
     */
    public boolean nutriflowUserExists(final Long userId) {
        try {
            String url = nutriflowBaseUrl + "/users/exists/" + userId;

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders(null)),
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            return Boolean.TRUE.equals(response.getBody().get("exists"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Create a user in NutriFlow system.
     *
     * @param appUserId the ID of the app user
     * @param name      user's name
     * @param height    user's height
     * @param weight    user's weight
     * @param age       user's age
     * @param sex       user's sex
     * @return the NutriFlow user ID
     */
    public Long createNutriflowUser(
            final Long appUserId,
            final String name,
            final Object height,
            final Object weight,
            final Integer age,
            final String sex) {

        String url = nutriflowBaseUrl + "/users";

        Map<String, Object> body = new HashMap<>();
        body.put("name", name != null ? name : "User");

        if (height != null) {
            body.put("height", height);
        }
        if (weight != null) {
            body.put("weight", weight);
        }
        if (age != null) {
            body.put("age", age);
        }
        if (sex != null) {
            body.put("sex", sex.toUpperCase());
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, createHeaders(appUserId));

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> responseBody = response.getBody();

            return ((Number) responseBody.get("userId")).longValue();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create NutriFlow user: " + e.getMessage(), e);
        }
    }

    /**
     * Search for a recipe by ingredient using the NutriFlow AI engine.
     *
     * @param ingredient ingredient name
     * @return map representing recipe details
     */
    public Map<String, Object> searchRecipeByIngredient(
            final String ingredient) {

        String safeIngredient = UriUtils.encode(ingredient, StandardCharsets.UTF_8);

        String url = nutriflowBaseUrl
                + "/ai/recipes/ingredient/" + safeIngredient;

        HttpEntity<Void> request = new HttpEntity<>(createHeaders(null));

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to search recipe by ingredient: "
                            + e.getMessage(),
                    e);
        }
    }

    /**
     * Get AI-generated recipe for a user.
     *
     * @param nutriflowUserId NutriFlow internal user ID
     * @param appUserId       app user ID for tracking
     * @return AI-generated recipe map
     */
    public Map<String, Object> getAIRecipeForUser(
            final Long nutriflowUserId,
            final Long appUserId) {

        String url = nutriflowBaseUrl
                + "/ai/recipes/user/" + nutriflowUserId;

        HttpEntity<Void> request = new HttpEntity<>(createHeaders(appUserId));

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to get AI recipe for user: "
                            + e.getMessage(),
                    e);
        }
    }
}
