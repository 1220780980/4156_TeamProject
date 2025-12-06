package com.example.fitness.model.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * DTO used when requesting a meal plan from NutriFlow.
 */
@Data
public class MealPlanRequestDTO {

    /** Meals per day requested. */
    private int mealsPerDay;

    /** Maximum prep time in minutes. */
    private int maxPrepTime;

    /** List of allergy keywords. */
    private List<String> allergies;

    /** List of meals the user dislikes. */
    private List<String> dislikedMeals;

    /** List of preferred ingredients. */
    private List<String> preferredIngredients;

    /** NutriFlow user ID (set automatically on backend). */
    private Long nutriflowUserId;

    /**
     * Convert this DTO to a map for NutriFlow API.
     *
     * @return map representing this DTO
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("mealsPerDay", mealsPerDay);
        map.put("maxPrepTime", maxPrepTime);
        map.put("allergies", allergies);
        map.put("dislikedMeals", dislikedMeals);
        map.put("preferredIngredients", preferredIngredients);
        map.put("nutriflowUserId", nutriflowUserId);

        return map;
    }
}
