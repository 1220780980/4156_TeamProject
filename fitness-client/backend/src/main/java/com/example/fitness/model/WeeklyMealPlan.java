package com.example.fitness.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * Represents a weekly meal plan starting from a given date and
 * containing a list of daily meal plans.
 */
@Data
public final class WeeklyMealPlan {

    /** Start date of this weekly meal plan. */
    private LocalDate startDate;

    /** List of daily meal plans for each day in the week. */
    private List<DailyMealPlan> days;

    @SuppressWarnings("unchecked")
    public static WeeklyMealPlan fromMap(Map<String, Object> map) {
        WeeklyMealPlan weekly = new WeeklyMealPlan();
        weekly.setStartDate(LocalDate.now());

        // Safely extract "days"
        List<Map<String, Object>> dayMaps = (List<Map<String, Object>>) map.get("days");

        List<DailyMealPlan> dayList = new ArrayList<>();

        if (dayMaps != null) {
            for (Map<String, Object> d : dayMaps) {

                DailyMealPlan day = new DailyMealPlan();
                day.setDay((String) d.getOrDefault("day", "Unknown"));

                List<Map<String, Object>> mealMaps = (List<Map<String, Object>>) d.get("meals");

                List<Meal> meals = new ArrayList<>();

                if (mealMaps != null) {
                    for (Map<String, Object> m : mealMaps) {
                        Meal meal = new Meal();
                        meal.setName((String) m.getOrDefault("title", "Untitled Meal"));
                        meal.setCalories(toInt(m.get("calories")));
                        meal.setProtein(toInt(m.get("protein")));
                        meal.setCarbs(toInt(m.get("carbohydrates")));
                        meal.setFat(toInt(m.get("fat")));
                        meal.setPrepTime(toInt(m.get("cookTime")));
                        meal.setInstructions((String) m.getOrDefault("instructions", "No instructions"));
                        meals.add(meal);
                    }
                }

                day.setMeals(meals);
                dayList.add(day);
            }
        }

        weekly.setDays(dayList);
        return weekly;
    }

    private static int toInt(Object v) {
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
