package com.example.nutriflow.mealplan.controller;

import com.example.nutriflow.mealplan.dto.CreateDailyMealPlanRequestDTO;
import com.example.nutriflow.mealplan.dto.CreateWeeklyMealPlanRequestDTO;
import com.example.nutriflow.mealplan.dto.DailyMealPlanDTO;
import com.example.nutriflow.mealplan.dto.WeeklyMealPlanDTO;
import com.example.nutriflow.mealplan.service.MealPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for handling meal plan related operations.
 */
@RestController
@RequestMapping("/api/mealplans")
public class MealPlanController {

    /** Service for meal plan related business logic. */
    @Autowired
    private MealPlanService mealPlanService;

    /**
     * POST endpoint to create a daily meal plan.
     *
     * @param request the request body containing meal plan info
     * @return ResponseEntity containing the created daily meal plan
     */
    @PostMapping("/daily")
    public ResponseEntity<DailyMealPlanDTO> createDailyMealPlan(
            @RequestBody final CreateDailyMealPlanRequestDTO
                    request) {

        try {
            DailyMealPlanDTO response =
                    mealPlanService.createDailyMealPlan(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET endpoint to retrieve a daily meal plan by ID.
     *
     * @param dailyPlanId the ID of the daily meal plan
     * @return ResponseEntity containing the daily meal plan
     */
    @GetMapping("/daily/{dailyPlanId}")
    public ResponseEntity<DailyMealPlanDTO> getDailyMealPlan(
            @PathVariable final Integer dailyPlanId) {

        return mealPlanService.getDailyMealPlan(dailyPlanId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET endpoint to retrieve all daily meal plans for a user.
     *
     * @param userId the ID of the user
     * @return ResponseEntity containing list of daily meal plans
     */
    @GetMapping("/daily/user/{userId}")
    public ResponseEntity<List<DailyMealPlanDTO>>
            getDailyMealPlansByUser(
            @PathVariable final Integer userId) {

        List<DailyMealPlanDTO> plans =
                mealPlanService.getDailyMealPlansByUserId(userId);
        return ResponseEntity.ok(plans);
    }

    /**
     * POST endpoint to create a weekly meal plan.
     *
     * @param request the request body containing weekly meal plan
     * @return ResponseEntity containing the created weekly meal plan
     */
    @PostMapping("/weekly")
    public ResponseEntity<WeeklyMealPlanDTO> createWeeklyMealPlan(
            @RequestBody final CreateWeeklyMealPlanRequestDTO
                    request) {

        try {
            WeeklyMealPlanDTO response =
                    mealPlanService.createWeeklyMealPlan(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET endpoint to retrieve a weekly meal plan by ID.
     *
     * @param weeklyPlanId the ID of the weekly meal plan
     * @return ResponseEntity containing the weekly meal plan
     */
    @GetMapping("/weekly/{weeklyPlanId}")
    public ResponseEntity<WeeklyMealPlanDTO> getWeeklyMealPlan(
            @PathVariable final Integer weeklyPlanId) {

        return mealPlanService.getWeeklyMealPlan(weeklyPlanId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET endpoint to retrieve all weekly meal plans for a user.
     *
     * @param userId the ID of the user
     * @return ResponseEntity containing list of weekly meal plans
     */
    @GetMapping("/weekly/user/{userId}")
    public ResponseEntity<List<WeeklyMealPlanDTO>>
            getWeeklyMealPlansByUser(
            @PathVariable final Integer userId) {

        List<WeeklyMealPlanDTO> plans =
                mealPlanService.getWeeklyMealPlansByUserId(userId);
        return ResponseEntity.ok(plans);
    }
}
