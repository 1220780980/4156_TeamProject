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

    /** Number of days in a plan. */
    private static final int DAYS_IN_PLAN = 1;

    /** Repository for accessing user data. */
    private final AppUserRepository userRepository;

    /** Client for communicating with NutriFlow service. */
    private final NutriflowClient nutriflowClient;

    /**
     * Constructor for MealPlanService.
     *
     * @param userRepository  the user repository
     * @param nutriflowClient the NutriFlow client
     */
    @Autowired
    public MealPlanService(AppUserRepository userRepository,
            NutriflowClient nutriflowClient) {
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
    public MealPlanRequestDTO buildMealPlanRequest(
            Long appUserId,
            MealPlanRequestDTO preferences) {

        AppUser user = userRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        preferences.setNutriflowUserId(user.getNutriflowUserId());
        return preferences;
    }

    /**
     * Generate a one day meal plan for a user.
     *
     * @param appUserId the app user ID
     * @param request   the meal plan request
     * @return the generated one day meal plan
     */
    public WeeklyMealPlan generateMealPlan(
            Long appUserId,
            MealPlanRequestDTO request) {

        AppUser user = userRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long nutriflowUserId = user.getNutriflowUserId();

        // Create NutriFlow user if missing
        if (nutriflowUserId == null ||
                !nutriflowClient.nutriflowUserExists(nutriflowUserId)) {

            nutriflowUserId = nutriflowClient.createNutriflowUser(
                    appUserId,
                    user.getEmail(),
                    user.getHeight(),
                    user.getWeight(),
                    user.getAge(),
                    user.getSex());

            user.setNutriflowUserId(nutriflowUserId);
            userRepository.save(user);
        }

        int mealsPerDay = request.getMealsPerDay() > 0
                ? request.getMealsPerDay()
                : DEFAULT_MEALS_PER_DAY;

        // Construct a WeeklyMealPlan container (even though it's one day)
        WeeklyMealPlan weekly = new WeeklyMealPlan();
        weekly.setStartDate(LocalDate.now());

        List<DailyMealPlan> days = new ArrayList<>();

        DailyMealPlan dayPlan = new DailyMealPlan();
        dayPlan.setDay(LocalDate.now().getDayOfWeek().name());

        List<Meal> meals = new ArrayList<>();

        for (int m = 0; m < mealsPerDay; m++) {
            Map<String, Object> recipeData = nutriflowClient.getAIRecipeForUser(nutriflowUserId, appUserId);

            meals.add(convertRecipeToMeal(recipeData));
        }

        dayPlan.setMeals(meals);
        days.add(dayPlan);

        weekly.setDays(days);

        return weekly;
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
     * @param v the value to convert
     * @return the integer value, or 0 if conversion fails
     */
    private int convertToInt(Object value) {
        if (value == null)
            return 0;

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
