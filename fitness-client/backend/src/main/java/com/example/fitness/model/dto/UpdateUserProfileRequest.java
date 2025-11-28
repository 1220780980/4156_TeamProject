package com.example.fitness.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for updating user profile.
 * All fields are optional - only provided fields will be updated.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {

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
}
