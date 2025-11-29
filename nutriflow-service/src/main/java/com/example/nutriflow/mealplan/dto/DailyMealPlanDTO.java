package com.example.nutriflow.mealplan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for daily meal plan responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyMealPlanDTO {

    /** Daily plan ID. */
    private Integer dailyPlanId;

    /** User ID. */
    private Integer userId;

    /** Date of the plan. */
    private LocalDate planDate;

    /** Plan name. */
    private String planName;

    /** List of meals in this plan. */
    private List<MealDTO> meals;
}

