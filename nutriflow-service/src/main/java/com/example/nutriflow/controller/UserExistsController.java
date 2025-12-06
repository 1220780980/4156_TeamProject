package com.example.nutriflow.controller;

import com.example.nutriflow.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Utility controller providing endpoints to check
 * whether a NutriFlow user exists.
 */
@RestController
@RequestMapping("/api/users")
public class UserExistsController {

    /**
     * Service for user-related business logic.
     */
    @Autowired
    private UserService userService;

    /**
     * GET endpoint to check whether a user exists in NutriFlow.
     *
     * @param userId the ID of the user
     * @return ResponseEntity containing a boolean result
     */
    @GetMapping("/exists/{userId}")
    public ResponseEntity<Map<String, Object>> userExists(
            @PathVariable final Integer userId) {
        boolean exists = userService.getUserById(userId).isPresent();
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
