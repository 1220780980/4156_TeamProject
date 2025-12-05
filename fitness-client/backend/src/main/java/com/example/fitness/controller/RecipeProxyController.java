package com.example.fitness.controller;

import com.example.fitness.service.NutriflowClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/proxy/recipes")
@CrossOrigin(
        origins = "http://localhost:3000", allowedHeaders = "*",
        methods = { RequestMethod.GET }, allowCredentials = "true")

public class RecipeProxyController {

    @Autowired
    private NutriflowClient nutriflowClient;

    @GetMapping("/ingredient/{ingredient}")
    public ResponseEntity<?> searchByIngredient(@PathVariable String ingredient) {
        try {
            return ResponseEntity.ok(
                    nutriflowClient.searchRecipeByIngredient(ingredient));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
