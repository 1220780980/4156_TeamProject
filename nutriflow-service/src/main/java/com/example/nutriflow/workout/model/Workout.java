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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a workout session.
 */
@Entity
@Table(name = "workouts", schema = "nutriflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workout {

    /** Precision for calorie values. */
    private static final int CAL_PRECISION = 7;

    /** Scale for decimal values. */
    private static final int DECI_SCALE = 2;

    /** Maximum length for string fields. */
    private static final int MAX_STRING_LENGTH = 255;

    /** Maximum length for workout type field. */
    private static final int MAX_TYPE_LENGTH = 100;

    /** Unique identifier for the workout. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_id")
    private Integer workoutId;

    /** User ID this workout belongs to. */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** Workout plan ID if part of a plan. */
    @Column(name = "workout_plan_id")
    private Integer workoutPlanId;

    /** Name of the workout. */
    @Column(name = "workout_name", length = MAX_STRING_LENGTH)
    private String workoutName;

    /** Type of workout. */
    @Column(name = "workout_type", length = MAX_TYPE_LENGTH)
    private String workoutType;

    /** Duration in minutes. */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /** Estimated calories burned. */
    @Column(name = "calories_burned",
            precision = CAL_PRECISION, scale = DECI_SCALE)
    private BigDecimal caloriesBurned;

    /** Whether the workout has been completed. */
    @Column(name = "completed")
    private Boolean completed;

    /** Timestamp when the workout was created. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when the workout was last updated. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Set the creation timestamp before persisting the entity.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (completed == null) {
            completed = false;
        }
    }

    /**
     * Update the timestamp before updating the entity.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

