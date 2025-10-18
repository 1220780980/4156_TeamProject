package com.example.nutriflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for updating user's daily nutritional targets
 * All fields are optional to support partial updates
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserTargetRequestDTO {
    
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal fiber;
    private BigDecimal fat;
    private BigDecimal carbs;
    private BigDecimal iron;
    private BigDecimal calcium;
    private BigDecimal vitaminA;
    private BigDecimal vitaminC;
    private BigDecimal vitaminD;
    private BigDecimal sodium;
    private BigDecimal potassium;
}