package com.example.nutriflow.controller;

import com.example.nutriflow.model.User;
import com.example.nutriflow.model.UserTarget;
import com.example.nutriflow.model.dto.HealthStatisticsResponseDTO;
import com.example.nutriflow.model.dto.UpdateUserRequestDTO;
import com.example.nutriflow.model.dto.UpdateUserTargetRequestDTO;
import com.example.nutriflow.service.HealthStatisticsService;
import com.example.nutriflow.service.UserService;
import com.example.nutriflow.service.UserTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling user-related operations
 * Provides endpoints for retrieving, updating, and deleting users
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserTargetService userTargetService;

    @Autowired
    private HealthStatisticsService healthStatisticsService;
    
    /**
     * GET endpoint to retrieve a user by their ID
     * @param userId the ID of the user to retrieve
     * @return ResponseEntity containing the user if found, 404 if not found
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * PUT endpoint to update an existing user
     * @param userId the ID of the user to update
     * @param request the request body containing the fields to update
     * @return ResponseEntity containing the updated user if successful, 404 if user not found
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Integer userId,
            @RequestBody UpdateUserRequestDTO request) {
        
        return userService.updateUser(userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET endpoint to retrieve a user's daily nutritional targets
     * Returns the most recent target settings for the user
     * 
     * @param userId the ID of the user
     * @return ResponseEntity containing the user's targets if found, 404 if not found
     */
    @GetMapping("/{userId}/targets")
    public ResponseEntity<UserTarget> getUserTargets(@PathVariable Integer userId) {
        return userTargetService.getUserTargets(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * PUT endpoint to update a user's daily nutritional targets
     * Creates new targets if none exist, otherwise updates existing targets
     * Supports partial updates - only provided fields will be updated
     * 
     * @param userId the ID of the user
     * @param request the request body containing the target values to update
     * @return ResponseEntity containing the updated targets if successful, 404 if user not found
     */
    @PutMapping("/{userId}/targets")
    public ResponseEntity<UserTarget> updateUserTargets(
            @PathVariable Integer userId,
            @RequestBody UpdateUserTargetRequestDTO request) {
        
        return userTargetService.updateUserTargets(userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET endpoint to retrieve comprehensive health statistics for a user
     * Calculates BMI and provides historical weight/BMI data for progress tracking
     * 
     * @param userId the ID of the user
     * @return ResponseEntity containing the user's health statistics if found, 404 if not found
     */
    @GetMapping("/{userId}/health_statistics")
    public ResponseEntity<HealthStatisticsResponseDTO> getHealthStatistics(@PathVariable Integer userId) {
        return healthStatisticsService.getHealthStatistics(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}