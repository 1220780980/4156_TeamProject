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

@Service
public final class MealPlanService {

    private final AppUserRepository userRepository;
    private final NutriflowClient nutriflowClient;

    @Autowired
    public MealPlanService(
            final AppUserRepository appUserRepository,
            final NutriflowClient nutriflowClient) {
        this.userRepository = appUserRepository;
        this.nutriflowClient = nutriflowClient;
    }

    public MealPlanRequestDTO buildMealPlanRequest(
            final Long appUserId,
            final MealPlanRequestDTO preferences) {

        AppUser user = userRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        preferences.setNutriflowUserId(user.getNutriflowUserId());
        return preferences;
    }

    public WeeklyMealPlan generateMealPlan(
            final Long appUserId,
            final MealPlanRequestDTO request) {

        AppUser user = userRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long nutriflowUserId = user.getNutriflowUserId();
        if (nutriflowUserId == null) {
            throw new RuntimeException("User does not have a NutriFlow account");
        }

        Integer mealsPerDayObj = request.getMealsPerDay();
        int mealsPerDay = mealsPerDayObj != null ? mealsPerDayObj : 3;
        int numDays = 7;

        List<DailyMealPlan> dailyPlans = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (int day = 0; day < numDays; day++) {
            DailyMealPlan dailyPlan = new DailyMealPlan();
            dailyPlan.setDay(dayNames[day % 7]);

            List<Meal> meals = new ArrayList<>();
            for (int mealNum = 0; mealNum < mealsPerDay; mealNum++) {
                Map<String, Object> recipeData = nutriflowClient.getAIRecipeForUser(nutriflowUserId, appUserId);
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

    private Meal convertRecipeToMeal(Map<String, Object> recipeData) {
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

    private int convertToInt(Object value) {
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
