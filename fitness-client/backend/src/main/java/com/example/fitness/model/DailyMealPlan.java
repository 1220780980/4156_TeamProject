/**
 * Models for daily meal plan data structures.
 */
package com.example.fitness.model;

import java.util.List;

/**
 * Represents a single day's meal plan.
 */
public final class DailyMealPlan {

    /** The day of the week or date associated with this meal plan. */
    private String day;

    /** List of meals planned for this date. */
    private List<Meal> meals;

    /**
     * Returns the label for the day (e.g., "Monday", "2025-12-01").
     *
     * @return the day label
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the day label.
     *
     * @param dayValue the day label to set
     */
    public void setDay(final String dayValue) {
        this.day = dayValue;
    }

    /**
     * Returns the list of meals for the given day.
     *
     * @return list of meals
     */
    public List<Meal> getMeals() {
        return meals;
    }

    /**
     * Sets the list of meals for the day.
     *
     * @param mealsList the list of meals to assign
     */
    public void setMeals(final List<Meal> mealsList) {
        this.meals = mealsList;
    }
}
