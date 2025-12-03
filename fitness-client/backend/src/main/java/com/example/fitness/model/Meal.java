/**
 * Model representing a single meal within a meal plan.
 */
package com.example.fitness.model;

/**
 * Represents one meal including its metadata such as name, calories,
 * macronutrients, and any relevant notes or instructions.
 */
public final class Meal {

    /** Name of the meal (e.g., "Grilled Chicken Bowl"). */
    private String name;

    /** Total calorie count for the meal. */
    private int calories;

    /** Amount of protein in grams. */
    private int protein;

    /** Amount of carbohydrates in grams. */
    private int carbs;

    /** Amount of fat in grams. */
    private int fat;

    /** Estimated preparation time in minutes. */
    private int prepTime;

    /** Optional text description or notes about the meal. */
    private String description;

    /** Optional cooking instructions for preparing the meal. */
    private String instructions;

    /**
     * Returns the name of the meal.
     *
     * @return meal name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the meal name.
     *
     * @param nameValue name of the meal
     */
    public void setName(final String nameValue) {
        this.name = nameValue;
    }

    /**
     * Returns the calories for this meal.
     *
     * @return calories
     */
    public int getCalories() {
        return calories;
    }

    /**
     * Sets the total calories for the meal.
     *
     * @param caloriesValue number of calories
     */
    public void setCalories(final int caloriesValue) {
        this.calories = caloriesValue;
    }

    /**
     * Returns the protein content in grams.
     *
     * @return protein value
     */
    public int getProtein() {
        return protein;
    }

    /**
     * Sets the protein amount.
     *
     * @param proteinValue protein in grams
     */
    public void setProtein(final int proteinValue) {
        this.protein = proteinValue;
    }

    /**
     * Returns the carbohydrates content in grams.
     *
     * @return carbs value
     */
    public int getCarbs() {
        return carbs;
    }

    /**
     * Sets the carbohydrates amount.
     *
     * @param carbsValue carbohydrates in grams
     */
    public void setCarbs(final int carbsValue) {
        this.carbs = carbsValue;
    }

    /**
     * Returns the fat content in grams.
     *
     * @return fat value
     */
    public int getFat() {
        return fat;
    }

    /**
     * Sets the fat amount.
     *
     * @param fatValue fat in grams
     */
    public void setFat(final int fatValue) {
        this.fat = fatValue;
    }

    /**
     * Returns the meal's preparation time.
     *
     * @return prep time in minutes
     */
    public int getPrepTime() {
        return prepTime;
    }

    /**
     * Sets the preparation time for the meal.
     *
     * @param prepTimeValue time in minutes
     */
    public void setPrepTime(final int prepTimeValue) {
        this.prepTime = prepTimeValue;
    }

    /**
     * Returns additional description text for the meal.
     *
     * @return description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets an optional description for the meal.
     *
     * @param descriptionValue text describing the meal
     */
    public void setDescription(final String descriptionValue) {
        this.description = descriptionValue;
    }

    /**
     * Returns the cooking instructions for preparing the meal.
     *
     * @return instructions text
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets cooking instructions for the meal.
     *
     * @param instructionsValue cooking steps
     */
    public void setInstructions(final String instructionsValue) {
        this.instructions = instructionsValue;
    }
}
