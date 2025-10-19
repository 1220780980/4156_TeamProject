package com.example.nutriflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Entity class representing a recipe in the NutriFlow system.
 * Stores basic recipe details such as title, duration, and tags.
 */
@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    /** Unique identifier for the recipe. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The title or name of the recipe. */
    private String title;

    /** Cooking time in minutes required to prepare the recipe. */
    @Column(name = "cook_time_minutes")
    private Integer cookTimeMinutes;

    /** List of cuisines associated with the recipe (e.g., Italian, Asian). */
    @Column(columnDefinition = "text[]")
    private String[] cuisines;

    /** Tags describing the recipe type or meal category (e.g., breakfast, vegan). */
    @Column(columnDefinition = "text[]")
    private String[] tags;
}