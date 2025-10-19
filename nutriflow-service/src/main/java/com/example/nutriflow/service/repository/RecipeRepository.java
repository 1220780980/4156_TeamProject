package com.example.nutriflow.service.repository;

import com.example.nutriflow.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Recipe} entities.
 * Provides basic CRUD operations through Spring Data JPA.
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // No custom methods yet; inherits standard CRUD operations from JpaRepository
}