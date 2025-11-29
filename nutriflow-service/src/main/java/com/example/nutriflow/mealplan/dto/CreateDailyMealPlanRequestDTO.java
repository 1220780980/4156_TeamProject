package com.example.nutriflow.mealplan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a daily meal plan request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDailyMealPlanRequestDTO {

    /** User ID. */
    private Integer userId;

    /** Date for the meal plan. */
    private LocalDate planDate;

    /** Optional plan name. */
    private String planName;

    /** Optional dietary restrictions. */
    private String[] dietaryRestrictions;

    /** Optional allergen list. */
    private String[] allergens;
}

