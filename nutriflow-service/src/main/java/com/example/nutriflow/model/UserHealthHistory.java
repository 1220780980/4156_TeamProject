package com.example.nutriflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing user health history records
 * Stores historical weight, height, and calculated BMI values over time
 */
@Entity
@Table(name = "user_health_history", schema = "nutriflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHealthHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;
    
    @Column(name = "height", nullable = false, precision = 5, scale = 2)
    private BigDecimal height;
    
    // BMI is auto-calculated by database as a generated column
    @Column(name = "bmi", precision = 5, scale = 2, insertable = false, updatable = false)
    private BigDecimal bmi;
    
    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;
    
    /**
     * Set the recorded timestamp before persisting the entity
     */
    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
    }
}