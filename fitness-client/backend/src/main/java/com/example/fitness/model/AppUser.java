package com.example.fitness.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing an app user in the fitness client system.
 */
@Entity
@Table(name = "app_user", schema = "fitness_client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    /** Unique identifier for the app user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Email address of the user (used for login). */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /** Hashed password for authentication. */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /** Age of the user. */
    @Column(name = "age")
    private Integer age;

    /** Height of the user. */
    @Column(name = "height", precision = 5, scale = 2)
    private BigDecimal height;

    /** Weight of the user. */
    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;

    /** Biological sex of the user. */
    @Column(name = "sex", length = 10)
    private String sex;

    /** Activity level of the user. */
    @Column(name = "activity_level", length = 20)
    private String activityLevel;

    /** Fitness goal of the user. */
    @Column(name = "fitness_goal", length = 20)
    private String fitnessGoal;

    /** Associated Nutriflow user ID for integration. */
    @Column(name = "nutriflow_user_id")
    private Long nutriflowUserId;

    /** Timestamp when the user record was created. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when the user record was last updated. */
    @Column(name = "updated_at", nullable = false)
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
