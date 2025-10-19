package com.example.nutriflow.model;

import com.example.nutriflow.model.enums.SexType;
import com.example.nutriflow.model.enums.CookingSkillLevel;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a user in the nutriflow system.
 */
@Entity
@Table(name = "users", schema = "nutriflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

     /** Unique identifier for the user. */
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "user_id")
     private Integer userId;

     /** Name of the user. */
     @Column(name = "name", nullable = false, length = 255)
     private String name;

    /** Height of the user in centimeters. */
    @Column(name = "height", precision = 5, scale = 2)
    private BigDecimal height;

    /** Weight of the user in kilograms. */
    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;

    /** Age of the user. */
    @Column(name = "age")
    private Integer age;

    /** Biological sex of the user. */
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", columnDefinition = "sex_type")
    private SexType sex;

    /** List of user's food allergies. */
    @Column(name = "allergies", columnDefinition = "text[]")
    private String[] allergies;

    /** List of foods the user dislikes. */
    @Column(name = "dislikes", columnDefinition = "text[]")
    private String[] dislikes;

    /** User's budget for meals. */
    @Column(name = "budget", precision = 10, scale = 2)
    private BigDecimal budget;

    /** User's cooking skill level. */
    @Enumerated(EnumType.STRING)
    @Column(name = "cooking_skill_level",
            columnDefinition = "cooking_skill_level")
    private CookingSkillLevel cookingSkillLevel;

    /** List of cooking equipment available to the user. */
    @Column(name = "equipments", columnDefinition = "text[]")
    private String[] equipments;

    /** Timestamp when the user record was created. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when the user record was last updated. */
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
