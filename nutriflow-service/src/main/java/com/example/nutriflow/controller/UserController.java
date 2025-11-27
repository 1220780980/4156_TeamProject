package com.example.nutriflow.controller;

import com.example.nutriflow.model.User;
import com.example.nutriflow.model.UserTarget;
import com.example.nutriflow.model.dto.CreateUserRequestDTO;
import com.example.nutriflow.model.dto.CreateUserResponseDTO;
import com.example.nutriflow.model.dto.HealthStatisticsResponseDTO;
import com.example.nutriflow.model.dto.UpdateUserRequestDTO;
import com.example.nutriflow.model.dto.UpdateUserTargetRequestDTO;
import com.example.nutriflow.service.HealthStatisticsService;
import com.example.nutriflow.service.UserService;
import com.example.nutriflow.service.UserTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling user-related operations.
 * Provides endpoints for retrieving, updating, and deleting users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Service for user-related business logic.
     */
    @Autowired
    private UserService userService;

    /**
     * Service for user target-related business logic.
     */
    @Autowired
    private UserTargetService userTargetService;

    /**
     * Service for health statistics calculations.
     */
    @Autowired
    private HealthStatisticsService healthStatisticsService;


    /**
     * POST endpoint to create a new user.
     * Supports client identification via headers for tracking purposes.
     * 
     * @param request the request body containing user information
     * @param clientId optional header identifying the calling client
     * @param endUserId optional header identifying the end user in client system
     * @return ResponseEntity containing the created user information
     */
    @PostMapping
    public ResponseEntity<CreateUserResponseDTO> createUser(
            @RequestBody final CreateUserRequestDTO request,
            @RequestHeader(value = "X-Client-Id", required = false) final String clientId,
            @RequestHeader(value = "X-End-User-Id", required = false) final String endUserId) {

        if (clientId != null && endUserId != null) {
            // Log the client and end user information
            System.out.println("User creation requested by client: " 
            + clientId + ", end user: " + endUserId);
        }

        try {
            CreateUserResponseDTO response = userService.createUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CreateUserResponseDTO(null, null, 
                    "Error creating user"));
        }
    }

    /**
     * GET endpoint to retrieve a user by their ID.
     * @param userId the ID of the user to retrieve
     * @return ResponseEntity containing the user if found,
     *         404 if not found
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(
            @PathVariable final Integer userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT endpoint to update an existing user.
     * @param userId the ID of the user to update
     * @param request the request body containing the fields
     *                to update
     * @return ResponseEntity containing the updated user
     *         if successful, 404 if user not found
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable final Integer userId,
            @RequestBody final UpdateUserRequestDTO request) {

        return userService.updateUser(userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET endpoint to retrieve a user's daily nutritional targets.
     * Returns the most recent target settings for the user.
     *
     * @param userId the ID of the user
     * @return ResponseEntity containing the user's targets
     *         if found, 404 if not found
     */
    @GetMapping("/{userId}/targets")
    public ResponseEntity<UserTarget> getUserTargets(
            @PathVariable final Integer userId) {
        return userTargetService.getUserTargets(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT endpoint to update a user's daily nutritional targets.
     * Creates new targets if none exist, otherwise updates
     * existing targets.
     * Supports partial updates - only provided fields will
     * be updated.
     *
     * @param userId the ID of the user
     * @param request the request body containing the target
     *                values to update
     * @return ResponseEntity containing the updated targets
     *         if successful, 404 if user not found
     */
    @PutMapping("/{userId}/targets")
    public ResponseEntity<UserTarget> updateUserTargets(
            @PathVariable final Integer userId,
            @RequestBody final UpdateUserTargetRequestDTO request) {

        return userTargetService.updateUserTargets(userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET endpoint to retrieve comprehensive health statistics
     * for a user.
     * Calculates BMI and provides historical weight/BMI data
     * for progress tracking.
     *
     * @param userId the ID of the user
     * @return ResponseEntity containing the user's health
     *         statistics if found, 404 if not found
     */
    @GetMapping("/{userId}/health_statistics")
    public ResponseEntity<HealthStatisticsResponseDTO>
            getHealthStatistics(@PathVariable final Integer userId) {
        return healthStatisticsService.getHealthStatistics(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
