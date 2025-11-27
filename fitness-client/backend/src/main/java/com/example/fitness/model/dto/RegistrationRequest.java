package com.example.fitness.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for user registration request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    /**
     * User's email address (required).
     */
    private String email;

    /**
     * User's password (required).
     */
    private String password;

    /**
     * User's age.
     */
    private Integer age;

    /**
     * User's height in centimeters.
     */
    private BigDecimal height;

    /**
     * User's weight in kilograms.
     */
    private BigDecimal weight;

    /**
     * User's biological sex.
     */
    private String sex;

    /**
     * User's activity level (e.g., SEDENTARY, MODERATE, ACTIVE).
     */
    private String activityLevel;

    /**
     * User's fitness goal (e.g., LOSE_WEIGHT, GAIN_MUSCLE, MAINTAIN).
     */
    private String fitnessGoal;
}
