package com.example.nutriflow.controller;

import com.example.nutriflow.service.AIRecipeService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * GET endpoint to retrieve a recipe with the given ingredient.
     * First, it checks whether a recipe with the given ingredient
     * already exists in the database;
     * If not, then ask an LLM to recommend a recipe with the given ingredient.
     *
     * Example:
     * /api/ai/recipes/ingredient/{ingredient} - returns a recipe
     * with the given ingredient.
     * @param ingredient ingredient that the user wants to use
     * @return ResponseEntity containing the appropriate recipe
     */
    @GetMapping("ingredient/{ingredient}")
    public ResponseEntity<?> getAIRecipe(
        final @PathVariable String ingredient) {
        try {
            return ResponseEntity.ok(
                aiRecipeService.getAIRecipe(ingredient));
        } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET endpoint to retrieve an AI recommended recipe.
     * An LLM recommends a randomly generated delicious recipe.
     * Example:
     * /api/ai/recipes/recommendation - returns some AI recommended recipe
     * @return ResponseEntity containing a recommended recipe
     */
    @GetMapping("/recommendation")
    public ResponseEntity<?> getAIRecommendedRecipe() {
        try {
            return ResponseEntity.ok(
                aiRecipeService.getAIRecommendedRecipe());
        } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET endpoint to generate a recipe given the user's information.
     * Example:
     * /api/ai/recipes/user/{userId} - returns a recipe
     * using the user's information
     * @param userId user identifier
     * @return ResponseEntity containing the appropriate recipe
     */
    @GetMapping("user/{userId}")
    public ResponseEntity<?> getUserRecipe(
        final @PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(
                aiRecipeService.getUserRecipe(userId));
        } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));
        }
    }

}
