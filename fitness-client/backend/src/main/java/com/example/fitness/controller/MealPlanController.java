package com.example.fitness.controller;

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
 * Controller for handling meal plan requests.
 */
@RestController
@RequestMapping("/api/mealplans")
public final class MealPlanController {

    /** Service that builds requests for NutriFlow meal plan generation. */
    @Autowired
    private MealPlanService mealPlanService;

    /**
     * Accepts user preferences and attaches their NutriFlow user ID
     * before returning the fully built request payload.
     *
     * @param appUserId the fitness app's internal user ID
     * @param dto       the preference object (meals/day, allergies, etc.)
     * @return enriched MealPlanRequestDTO including nutriflowUserId
     */
    @PostMapping("/request/{appUserId}")
    public ResponseEntity<MealPlanRequestDTO> requestMealPlan(
            @PathVariable final Long appUserId,
            @RequestBody final MealPlanRequestDTO dto) {

        MealPlanRequestDTO enriched =
            mealPlanService.buildMealPlanRequest(appUserId, dto);

        return ResponseEntity.ok(enriched);
    }
}
