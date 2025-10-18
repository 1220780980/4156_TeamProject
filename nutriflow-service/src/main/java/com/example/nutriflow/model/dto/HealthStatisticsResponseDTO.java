package com.example.nutriflow.model.dto;

import com.example.nutriflow.model.enums.BMICategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for health statistics response
 * Contains current health metrics and historical data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthStatisticsResponseDTO {
    

    private CurrentHealthMetrics currentMetrics;
    private List<HealthHistoryEntry> history;
    
    /**
     * representing current health metrics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentHealthMetrics {
        private BigDecimal weight;
        private BigDecimal height;
        private BigDecimal bmi;
        private BMICategory bmiCategory;
        private String bmiCategoryDisplay;
        private String bmiInterpretation;
    }
    
    /**
     * representing a single health history entry
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthHistoryEntry {
        private Integer historyId;
        private BigDecimal weight;
        private BigDecimal height;
        private BigDecimal bmi;
        private LocalDateTime recordedAt;
    }
}