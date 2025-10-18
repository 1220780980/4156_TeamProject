package com.example.nutriflow.model.enums;

/**
 * Enum representing BMI (Body Mass Index) categories based on WHO classification
 */
public enum BMICategory {
    
    UNDERWEIGHT(
        "Underweight",
        "Your BMI indicates you are underweight. Consider consulting with a healthcare provider about a healthy weight gain plan."
    ),
    
    NORMAL_WEIGHT(
        "Normal weight",
        "Your BMI is in the healthy range. Maintain your current weight through a balanced diet and regular physical activity."
    ),
    
    OVERWEIGHT(
        "Overweight",
        "Your BMI indicates you are overweight. Consider adopting healthier eating habits and increasing physical activity."
    ),
    
    OBESE(
        "Obese",
        "Your BMI indicates obesity. It's recommended to consult with a healthcare provider for a personalized health plan."
    ),
    
    UNKNOWN(
        "Unknown",
        "Insufficient data to calculate BMI"
    );
    
    private final String displayName;
    private final String interpretation;
    
    BMICategory(String displayName, String interpretation) {
        this.displayName = displayName;
        this.interpretation = interpretation;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getInterpretation() {
        return interpretation;
    }
    
    /**
     * Determine BMI category based on BMI value using WHO classification
     * 
     * @param bmi the calculated BMI value
     * @return the corresponding BMI category
     */
    public static BMICategory fromBMI(java.math.BigDecimal bmi) {
        if (bmi == null) {
            return UNKNOWN;
        }
        
        if (bmi.compareTo(new java.math.BigDecimal("18.5")) < 0) {
            return UNDERWEIGHT;
        } else if (bmi.compareTo(new java.math.BigDecimal("25.0")) < 0) {
            return NORMAL_WEIGHT;
        } else if (bmi.compareTo(new java.math.BigDecimal("30.0")) < 0) {
            return OVERWEIGHT;
        } else {
            return OBESE;
        }
    }
}