package com.example.fitness.controller;

import com.example.fitness.model.WeeklyMealPlan;
import com.example.fitness.model.dto.MealPlanRequestDTO;
import com.example.fitness.service.MealPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling meal plan operations.
 */
@RestController
@RequestMapping("/api/mealplans")
public final class MealPlanController {

    /** Service for meal plan operations. */
    @Autowired
    private MealPlanService mealPlanService;

    /**
     * Request a meal plan with user preferences.
     *
     * @param appUserId the app user ID
     * @param dto the meal plan request preferences
     * @return ResponseEntity containing enriched meal plan request
     */
    @PostMapping("/request/{appUserId}")
    public ResponseEntity<MealPlanRequestDTO> requestMealPlan(
            @PathVariable final Long appUserId,
            @RequestBody final MealPlanRequestDTO dto) {

        MealPlanRequestDTO enriched =
            mealPlanService.buildMealPlanRequest(appUserId, dto);

        return ResponseEntity.ok(enriched);
    }

    /**
     * Generate a weekly meal plan for a user.
     *
     * @param appUserId the app user ID
     * @param request the meal plan request with preferences
     * @return ResponseEntity containing weekly meal plan or error
     */
    @PostMapping("/generate/{appUserId}")
    public ResponseEntity<?> generateMealPlan(
            @PathVariable final Long appUserId,
            @RequestBody final MealPlanRequestDTO request) {

        try {
            WeeklyMealPlan weeklyPlan =
                    mealPlanService.generateMealPlan(appUserId, request);
            return ResponseEntity.ok(weeklyPlan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
