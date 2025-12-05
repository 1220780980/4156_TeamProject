package com.example.fitness.service;

import com.example.fitness.model.AppUser;
import com.example.fitness.model.Meal;
import com.example.fitness.model.DailyMealPlan;
import com.example.fitness.model.WeeklyMealPlan;
import com.example.fitness.model.dto.MealPlanRequestDTO;
import com.example.fitness.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service for generating meal plans for users.
 */
@Service
public final class MealPlanService {

    /** Default number of meals per day. */
    private static final int DEFAULT_MEALS_PER_DAY = 3;
    /** Number of days in a week. */
    private static final int DAYS_IN_WEEK = 7;

    /** Repository for accessing user data. */
    private final AppUserRepository userRepository;

    /** Client for communicating with NutriFlow service. */
    private final NutriflowClient nutriflowClient;

    /**
     * Constructor for MealPlanService.
     *
     * @param appUserRepository    the user repository
     * @param nutriflowClientParam the NutriFlow client
     */
    @Autowired
    public MealPlanService(AppUserRepository userRepository, NutriflowClient nutriflowClient) {
        this.userRepository = userRepository;
        this.nutriflowClient = nutriflowClient;
    }

    /**
     * Build meal plan request with user preferences.
     *
     * @param appUserId   the app user ID
     * @param preferences the meal plan preferences
     * @return the meal plan request DTO
     */
    public MealPlanRequestDTO buildMealPlanRequest(Long appUserId, MealPlanRequestDTO preferences) {
        AppUser user = userRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        preferences.setNutriflowUserId(user.getNutriflowUserId());
        return preferences;
    }

    /**
     * Generate a weekly meal plan for a user.
     *
     * @param appUserId the app user ID
     * @param request   the meal plan request
     * @return the generated weekly meal plan
     */
    public WeeklyMealPlan generateMealPlan(Long appUserId, MealPlanRequestDTO request) {

        AppUser user = userRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long nutriflowUserId = user.getNutriflowUserId();
        if (nutriflowUserId == null) {
            throw new RuntimeException("User does not have a NutriFlow account");
        }

        int mealsPerDay = request.getMealsPerDay() > 0
                ? request.getMealsPerDay()
                : DEFAULT_MEALS_PER_DAY;

        List<DailyMealPlan> week = new ArrayList<>();
        String[] dayNames = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

        for (int d = 0; d < DAYS_IN_WEEK; d++) {

            DailyMealPlan day = new DailyMealPlan();
            day.setDay(dayNames[d]);

            List<Meal> meals = new ArrayList<>();

            for (int m = 0; m < mealsPerDay; m++) {
                Map<String, Object> recipe = nutriflowClient.getAIRecipeForUser(nutriflowUserId, appUserId);
                meals.add(convertRecipeToMeal(recipe));
            }

            day.setMeals(meals);
            week.add(day);
        }

        WeeklyMealPlan plan = new WeeklyMealPlan();
        plan.setStartDate(LocalDate.now());
        plan.setDays(week);
        return plan;
    }

    /**
     * Convert recipe data from NutriFlow to a Meal object.
     *
     * @param recipeData the recipe data map
     * @return the converted Meal object
     */
    private Meal convertRecipeToMeal(Map<String, Object> recipeData) {
        Meal meal = new Meal();

        meal.setName((String) recipeData.getOrDefault("title", "Unnamed Meal"));
        meal.setCalories(convertToInt(recipeData.get("calories")));
        meal.setProtein(convertToInt(recipeData.get("protein")));
        meal.setCarbs(convertToInt(recipeData.get("carbohydrates")));
        meal.setFat(convertToInt(recipeData.get("fat")));
        meal.setPrepTime(convertToInt(recipeData.get("cookTime")));
        meal.setInstructions((String) recipeData.getOrDefault("instructions", "No instructions available."));

        return meal;
    }

    /**
     * Convert a value to integer.
     *
     * @param value the value to convert
     * @return the integer value, or 0 if conversion fails
     */
    private int convertToInt(Object v) {
        if (v == null)
            return 0;
        if (v instanceof Number)
            return ((Number) v).intValue();
        try {
            return Integer.parseInt(v.toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
