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
     * GET endpoint to retrieve an AI recommended recipe with given ingredients.
     *
     * Example:
     * - /api/ai/recipes/{ingredients} - returns some ai recommended recipe
     * @param ingredients ingredients that the user wants to use
     * @return ResponseEntity containing a recommended recipe
     */
    @GetMapping("/{ingredients}")
    public ResponseEntity<?> getAIRecipe(
        final @PathVariable String ingredients) {
        try {
            return ResponseEntity.ok(
                aiRecipeService.getAIRecipe(ingredients));
        } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));
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
    public ResponseEntity<?> getAIRecommendedRecipe() {
        try {
            return ResponseEntity.ok(
                aiRecipeService.getAIRecommendedRecipe());
        } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));
        }
    }
}
