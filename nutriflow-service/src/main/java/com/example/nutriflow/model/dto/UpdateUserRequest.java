package com.example.nutriflow.model.dto;

import com.example.nutriflow.model.enums.CookingSkillLevel;
import com.example.nutriflow.model.enums.SexType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    
    private BigDecimal height;
    private BigDecimal weight;
    private Integer age;
    private SexType sex;
    private String[] allergies;
    private String[] dislikes;
    private BigDecimal budget;
    private CookingSkillLevel cookingSkill;
    private String[] equipments;
}