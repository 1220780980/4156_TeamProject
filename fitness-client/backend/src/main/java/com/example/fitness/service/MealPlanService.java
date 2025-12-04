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
     * @param appUserRepository the user repository
     * @param nutriflowClientParam the NutriFlow client
     */
    @Autowired
    public MealPlanService(
            final AppUserRepository appUserRepository,
            final NutriflowClient nutriflowClientParam) {
        this.userRepository = appUserRepository;
        this.nutriflowClient = nutriflowClientParam;
    }

    /**
     * Build meal plan request with user preferences.
     *
     * @param appUserId the app user ID
     * @param preferences the meal plan preferences
     * @return the meal plan request DTO
     */
    public MealPlanRequestDTO buildMealPlanRequest(
            final Long appUserId,
            final MealPlanRequestDTO preferences) {

        AppUser user = userRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        preferences.setNutriflowUserId(user.getNutriflowUserId());
        return preferences;
    }

    /**
     * Generate a weekly meal plan for a user.
     *
     * @param appUserId the app user ID
     * @param request the meal plan request
     * @return the generated weekly meal plan
     */
    public WeeklyMealPlan generateMealPlan(
            final Long appUserId,
            final MealPlanRequestDTO request) {

        AppUser user = userRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long nutriflowUserId = user.getNutriflowUserId();
        if (nutriflowUserId == null) {
            throw new RuntimeException(
                    "User does not have a NutriFlow account");
        }

        Integer mealsPerDayObj = request.getMealsPerDay();
        int mealsPerDay = mealsPerDayObj != null
                ? mealsPerDayObj : DEFAULT_MEALS_PER_DAY;
        int numDays = DAYS_IN_WEEK;

        List<DailyMealPlan> dailyPlans = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        String[] dayNames = {"Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday", "Sunday"};

        for (int day = 0; day < numDays; day++) {
            DailyMealPlan dailyPlan = new DailyMealPlan();
            dailyPlan.setDay(dayNames[day % DAYS_IN_WEEK]);

            List<Meal> meals = new ArrayList<>();
            for (int mealNum = 0; mealNum < mealsPerDay; mealNum++) {
                Map<String, Object> recipeData = nutriflowClient
                        .getAIRecipeForUser(nutriflowUserId, appUserId);
                Meal meal = convertRecipeToMeal(recipeData);
                meals.add(meal);
            }

            dailyPlan.setMeals(meals);
            dailyPlans.add(dailyPlan);
        }

        WeeklyMealPlan weeklyPlan = new WeeklyMealPlan();
        weeklyPlan.setStartDate(startDate);
        weeklyPlan.setDays(dailyPlans);

        return weeklyPlan;
    }

    /**
     * Convert recipe data from NutriFlow to a Meal object.
     *
     * @param recipeData the recipe data map
     * @return the converted Meal object
     */
    private Meal convertRecipeToMeal(
            final Map<String, Object> recipeData) {
        Meal meal = new Meal();

        if (recipeData.containsKey("title")) {
            meal.setName((String) recipeData.get("title"));
        }

        if (recipeData.containsKey("calories")) {
            Object cal = recipeData.get("calories");
            meal.setCalories(convertToInt(cal));
        }

        if (recipeData.containsKey("protein")) {
            Object prot = recipeData.get("protein");
            meal.setProtein(convertToInt(prot));
        }

        if (recipeData.containsKey("carbohydrates")) {
            Object carbs = recipeData.get("carbohydrates");
            meal.setCarbs(convertToInt(carbs));
        }

        if (recipeData.containsKey("fat")) {
            Object fat = recipeData.get("fat");
            meal.setFat(convertToInt(fat));
        }

        if (recipeData.containsKey("cookTime")) {
            Object cookTime = recipeData.get("cookTime");
            meal.setPrepTime(convertToInt(cookTime));
        }

        if (recipeData.containsKey("instructions")) {
            meal.setInstructions((String) recipeData.get("instructions"));
        }

        return meal;
    }

    /**
     * Convert a value to integer.
     *
     * @param value the value to convert
     * @return the integer value, or 0 if conversion fails
     */
    private int convertToInt(final Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
