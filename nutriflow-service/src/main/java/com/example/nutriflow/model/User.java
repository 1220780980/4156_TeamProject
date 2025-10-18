package com.example.nutriflow.model;

import com.example.nutriflow.model.enums.SexType;
import com.example.nutriflow.model.enums.CookingSkillLevel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a user in the nutriflow system
 */
@Entity
@Table(name = "users", schema = "nutriflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "height", precision = 5, scale = 2)
    private BigDecimal height;
    
    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;
    
    @Column(name = "age")
    private Integer age;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", columnDefinition = "sex_type")
    private SexType sex;
    
    @Column(name = "allergies", columnDefinition = "text[]")
    private String[] allergies;
    
    @Column(name = "dislikes", columnDefinition = "text[]")
    private String[] dislikes;
    
    @Column(name = "budget", precision = 10, scale = 2)
    private BigDecimal budget;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "cooking_skill_level", columnDefinition = "cooking_skill_level")
    private CookingSkillLevel cookingSkillLevel;
    
    @Column(name = "equipments", columnDefinition = "text[]")
    private String[] equipments;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Set the creation timestamp before persisting the entity
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Update the timestamp before updating the entity
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}