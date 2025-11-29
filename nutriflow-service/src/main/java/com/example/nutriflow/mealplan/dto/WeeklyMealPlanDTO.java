package com.example.nutriflow.mealplan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for weekly meal plan responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyMealPlanDTO {

    /** Weekly plan ID. */
    private Integer weeklyPlanId;

    /** User ID. */
    private Integer userId;

    /** Start date. */
    private LocalDate startDate;

    /** End date. */
    private LocalDate endDate;

    /** Plan name. */
    private String planName;

    /** List of daily meal plans. */
    private List<DailyMealPlanDTO> dailyPlans;
}

