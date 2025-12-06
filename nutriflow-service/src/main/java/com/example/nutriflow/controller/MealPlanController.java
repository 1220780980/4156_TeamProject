package com.example.nutriflow.controller;

import com.example.nutriflow.service.AIRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Generates weekly meal plans by repeatedly calling the AI recipe generator.
 */
@RestController
@RequestMapping("/api/mealplan")
@CrossOrigin
public class MealPlanController {

    @Autowired
    private AIRecipeService aiRecipeService;

    private static final int DAYS_IN_WEEK = 7;

    private static final String[] DAY_NAMES = {
            "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"
    };

    /**
     * Generate a weekly meal plan for a user.
     * 
     * @param nutriflowUserId the user ID in NutriFlow
     * @param request         preferences map from fitness-client
     * @return weekly meal plan JSON
     */
    @PostMapping("/generate/{nutriflowUserId}")
    public ResponseEntity<?> generateWeeklyPlan(
            @PathVariable Long nutriflowUserId,
            @RequestBody Map<String, Object> request) {

        int mealsPerDay = request.get("mealsPerDay") != null
                ? ((Number) request.get("mealsPerDay")).intValue()
                : 3;

        List<Map<String, Object>> week = new ArrayList<>();

        for (int d = 0; d < DAYS_IN_WEEK; d++) {

            Map<String, Object> dayObject = new HashMap<>();
            dayObject.put("day", DAY_NAMES[d]);

            List<Map<String, Object>> meals = new ArrayList<>();

            for (int m = 0; m < mealsPerDay; m++) {

                // call EXISTING AI recipe generator
                Map<String, Object> recipe = aiRecipeService.generateRecipeForUser(nutriflowUserId);

                if (recipe == null) {
                    recipe = new HashMap<>();
                    recipe.put("title", "AI Recipe Not Available");
                    recipe.put("calories", 0);
                    recipe.put("protein", 0);
                    recipe.put("carbohydrates", 0);
                    recipe.put("fat", 0);
                    recipe.put("cookTime", 0);
                    recipe.put("instructions", "No instructions available.");
                }

                meals.add(recipe);
            }

            dayObject.put("meals", meals);
            week.add(dayObject);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("days", week);

        return ResponseEntity.ok(response);
    }
}
