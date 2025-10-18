package com.example.nutriflow.controller;

import com.example.nutriflow.model.User;
import com.example.nutriflow.model.dto.UpdateUserRequest;
import com.example.nutriflow.service.UserService;
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
            @RequestBody UpdateUserRequest request) {
        
        return userService.updateUser(userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}