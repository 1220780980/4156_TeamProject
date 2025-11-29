package com.example.nutriflow.workout.model;

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
 * Entity class representing a workout plan for a user.
 */
@Entity
@Table(name = "workout_plans", schema = "nutriflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlan {

    /** Maximum length for string fields. */
    private static final int MAX_STRING_LENGTH = 255;

    /** Unique identifier for the workout plan. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_plan_id")
    private Integer workoutPlanId;

    /** User ID this workout plan belongs to. */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** Start date of the plan. */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** End date of the plan. */
    @Column(name = "end_date")
    private LocalDate endDate;

    /** Name or description of the workout plan. */
    @Column(name = "plan_name", length = MAX_STRING_LENGTH)
    private String planName;

    /** Description of the workout plan. */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

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

