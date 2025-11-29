package com.example.nutriflow.mealplan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a weekly meal plan request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWeeklyMealPlanRequestDTO {

    /** User ID. */
    private Integer userId;

    /** Start date of the week. */
    private LocalDate startDate;

    /** Optional plan name. */
    private String planName;

    /** Optional dietary restrictions. */
    private String[] dietaryRestrictions;

    /** Optional allergen list. */
    private String[] allergens;
}

