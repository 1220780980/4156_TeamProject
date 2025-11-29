package com.example.nutriflow.mealplan.model;

import com.example.nutriflow.model.enums.MealType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a meal in a meal plan.
 */
@Entity
@Table(name = "meals", schema = "nutriflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    /** Precision for calorie values. */
    private static final int CAL_PRECISION = 7;

    /** Precision for nutrient values. */
    private static final int NUTRI_PRECISION = 6;

    /** Scale for decimal values. */
    private static final int DECI_SCALE = 2;

    /** Maximum length for string fields. */
    private static final int MAX_STRING_LENGTH = 255;

    /** Unique identifier for the meal. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Integer mealId;

    /** Daily meal plan ID this meal belongs to. */
    @Column(name = "daily_plan_id", nullable = false)
    private Integer dailyPlanId;

    /** Recipe ID associated with this meal. */
    @Column(name = "recipe_id")
    private Integer recipeId;

    /** Type of meal. */
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    /** Name of the meal. */
    @Column(name = "meal_name", length = MAX_STRING_LENGTH)
    private String mealName;

    /** Total calories in this meal. */
    @Column(name = "calories", precision = CAL_PRECISION, scale = DECI_SCALE)
    private BigDecimal calories;

    /** Protein content in grams. */
    @Column(name = "protein", precision = NUTRI_PRECISION, scale = DECI_SCALE)
    private BigDecimal protein;

    /** Carbohydrate content in grams. */
    @Column(name = "carbs", precision = NUTRI_PRECISION, scale = DECI_SCALE)
    private BigDecimal carbs;

    /** Fat content in grams. */
    @Column(name = "fat", precision = NUTRI_PRECISION, scale = DECI_SCALE)
    private BigDecimal fat;

    /** Fiber content in grams. */
    @Column(name = "fiber", precision = NUTRI_PRECISION, scale = DECI_SCALE)
    private BigDecimal fiber;

    /** Timestamp when the meal was created. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when the meal was last updated. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Set the creation timestamp before persisting the entity.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Update the timestamp before updating the entity.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

