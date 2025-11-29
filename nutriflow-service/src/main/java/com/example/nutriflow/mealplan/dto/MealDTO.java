package com.example.nutriflow.mealplan.dto;

import com.example.nutriflow.model.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for meal information in responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealDTO {

    /** Meal ID. */
    private Integer mealId;

    /** Recipe ID if associated. */
    private Integer recipeId;

    /** Type of meal. */
    private MealType mealType;

    /** Name of the meal. */
    private String mealName;

    /** Total calories. */
    private BigDecimal calories;

    /** Protein in grams. */
    private BigDecimal protein;

    /** Carbs in grams. */
    private BigDecimal carbs;

    /** Fat in grams. */
    private BigDecimal fat;

    /** Fiber in grams. */
    private BigDecimal fiber;
}

