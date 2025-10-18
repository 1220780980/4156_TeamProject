package com.example.nutriflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing daily macro/micro nutrient targets for a user
 */
@Entity
@Table(name = "user_targets", schema = "nutriflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTarget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "target_id")
    private Integer targetId;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "calories", precision = 7, scale = 2)
    private BigDecimal calories;
    
    @Column(name = "protein", precision = 6, scale = 2)
    private BigDecimal protein;
    
    @Column(name = "fiber", precision = 6, scale = 2)
    private BigDecimal fiber;
    
    @Column(name = "fat", precision = 6, scale = 2)
    private BigDecimal fat;
    
    @Column(name = "carbs", precision = 6, scale = 2)
    private BigDecimal carbs;
    
    @Column(name = "iron", precision = 6, scale = 2)
    private BigDecimal iron;
    
    @Column(name = "calcium", precision = 6, scale = 2)
    private BigDecimal calcium;
    
    @Column(name = "vitamin_a", precision = 6, scale = 2)
    private BigDecimal vitaminA;
    
    @Column(name = "vitamin_c", precision = 6, scale = 2)
    private BigDecimal vitaminC;
    
    @Column(name = "vitamin_d", precision = 6, scale = 2)
    private BigDecimal vitaminD;
    
    @Column(name = "sodium", precision = 6, scale = 2)
    private BigDecimal sodium;
    
    @Column(name = "potassium", precision = 6, scale = 2)
    private BigDecimal potassium;
    
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