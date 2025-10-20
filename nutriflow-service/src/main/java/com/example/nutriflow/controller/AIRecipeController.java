package com.example.nutriflow.controller;

import com.example.nutriflow.model.Recipe;
import com.example.nutriflow.service.AIRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing AI recipe-related operations.
 * Provides endpoints for retrieving ai generated recipes.
 */
@RestController
@RequestMapping("/api/ai/recipes")
public class AIRecipeController {
    /** Service handling recipe-related logic. */
    @Autowired
    private AIRecipeService aiRecipeService;

    public AIRecipeController(AIRecipeService aiRecipeService) { 
        this.aiRecipeService = aiRecipeService; 
    }
    /**
     * GET endpoint to retrieve an AI recommended recipe with given ingredients.
     *
     * Example:
     * - /api/ai/recipes/{ingredients} - returns some ai recommended recipe
     * @param ingredients ingredients that the user wants to use
     * @return ResponseEntity containing a recommended recipe
     */
    @GetMapping("/{ingredients}")
    public ResponseEntity<Optional<Recipe>> getAIRecipe(final @PathVariable String ingredients) {
        try {
            return ResponseEntity.ok(
                AIrecipeService.getAIRecipe(ingredients));
        } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of("error", "some error occured"));
        }
    }

    /**
     * GET endpoint to retrieve an AI recommended recipe.
     *
     * Example:
     * - /api/ai/recipes/recommendation - returns some ai recommended recipe
     * @return ResponseEntity containing a recommended recipe
     */
    @GetMapping("/recommendation")
    public ResponseEntity<Optional<Recipe>> getAIRecommendedRecipe() {
        try {
            return ResponseEntity.ok(
                AIrecipeService.getAIRecommendedRecipe(ingredients));
        } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of("error", "some error occured"));
        }
    }
}