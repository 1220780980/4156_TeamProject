package com.example.fitness.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for user profile response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    /**
     * The app user ID.
     */
    private Long appUserId;

    /**
     * User's email address.
     */
    private String email;

    /**
     * User's age.
     */
    private Integer age;

    /**
     * User's height.
     */
    private BigDecimal height;

    /**
     * User's weight.
     */
    private BigDecimal weight;

    /**
     * User's sex.
     */
    private String sex;

    /**
     * User's activity level.
     */
    private String activityLevel;

    /**
     * User's fitness goal.
     */
    private String fitnessGoal;

    /**
     * The associated NutriFlow user ID.
     */
    private Long nutriflowUserId;

    /**
     * Timestamp when the user was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user was last updated.
     */
    private LocalDateTime updatedAt;
}
