package com.example.fitness.controller;

import com.example.fitness.model.dto.LoginRequest;
import com.example.fitness.model.dto.LoginResponse;
import com.example.fitness.model.dto.RegistrationRequest;
import com.example.fitness.model.dto.RegistrationResponse;
import com.example.fitness.model.dto.UpdateUserProfileRequest;
import com.example.fitness.service.AppUserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for handling app user operations.
 */
@RestController
@RequestMapping("/api/users")
public class AppUserController {

    /**
     * Service for app user operations.
     */
    @Autowired
    private AppUserService appUserService;

    /**
     * POST endpoint for user registration.
     *
     * @param request the registration request containing user information
     * @return ResponseEntity containing registration response
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            RegistrationResponse response = appUserService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST endpoint for user login.
     *
     * @param request the login request containing email and password
     * @return ResponseEntity containing login response with user info
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = appUserService.loginUser(
                    request.getEmail(), 
                    request.getPassword()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET endpoint to retrieve user profile by app user ID.
     *
     * @param appUserId the app user ID
     * @return ResponseEntity contain user profile if found, 404 if not found
     */
    @GetMapping("/{appUserId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long appUserId) {
        return appUserService.getUserProfile(appUserId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found with ID: " 
                            + appUserId)));
    }

    /**
     * PUT endpoint to update user profile.
     *
     * @param appUserId the app user ID
     * @param request the update request containing fields to update
     * @return ResponseEntity containing updated profile if successful, 
     *         404 if not found
     */
    @PutMapping("/{appUserId}")
    public ResponseEntity<?> updateUserProfile(
        @PathVariable Long appUserId,
        @RequestBody UpdateUserProfileRequest request) {
    
    return appUserService.updateUserProfile(appUserId, request)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found with ID: " 
                        + appUserId)));
}
}
