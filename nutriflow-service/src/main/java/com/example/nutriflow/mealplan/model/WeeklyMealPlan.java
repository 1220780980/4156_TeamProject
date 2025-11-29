package com.example.nutriflow.mealplan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity class representing a weekly meal plan for a user.
 */
@Entity
@Table(name = "weekly_meal_plans", schema = "nutriflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyMealPlan {

    /** Maximum length for string fields. */
    private static final int MAX_STRING_LENGTH = 255;

    /** Unique identifier for the weekly meal plan. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekly_plan_id")
    private Integer weeklyPlanId;

    /** User ID this meal plan belongs to. */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** Start date of the weekly plan. */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** End date of the weekly plan. */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /** Name or description of the weekly plan. */
    @Column(name = "plan_name", length = MAX_STRING_LENGTH)
    private String planName;

    /** Timestamp when the plan was created. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when the plan was last updated. */
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

