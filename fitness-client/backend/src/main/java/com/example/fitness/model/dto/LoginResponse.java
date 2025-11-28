package com.example.fitness.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for user login response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /**
     * The app user ID (acts as session token).
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
     * Success message.
     */
    private String message;
}
