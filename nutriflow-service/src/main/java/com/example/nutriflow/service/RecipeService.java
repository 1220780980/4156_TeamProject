package com.example.nutriflow.service;

import com.example.nutriflow.model.Recipe;
import com.example.nutriflow.service.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling business logic related to recipes.
 * Provides methods for fetching recipes, popular recipes, and user favorites.
 */
@Service
public class RecipeService {

    /** Repository for accessing recipe data. */
    @Autowired
    private RecipeRepository recipeRepository;

    /** Default number of recipes to return when limit is not specified. */
    private static final int DEFAULT_POPULAR_LIMIT = 5;

    /**
     * Retrieves a recipe by its unique ID.
     *
     * @param id the ID of the recipe
     * @return Optional containing the recipe if found, or empty if not
     */
    public Optional<Recipe> getRecipeById(final Integer id) {
        return recipeRepository.findById(id);
    }

    /**
     * Retrieves the top {@value #DEFAULT_POPULAR_LIMIT} most popular recipes
     * based on popularity score.
     *
     * @return list of the most popular recipes
     */
    public List<Recipe> getPopularRecipesDefault() {
        return recipeRepository.findPopularRecipes(
                PageRequest.of(0, DEFAULT_POPULAR_LIMIT));
    }

    /**
     * Retrieves a custom number of popular recipes based on the provided limit.
     * Defaults to {@value #DEFAULT_POPULAR_LIMIT} if the limit is invalid.
     *
     * @param limit the number of recipes to return
     * @return list of popular recipes up to the specified limit
     */
    public List<Recipe> getPopularRecipes(final int limit) {
        int validLimit = limit > 0 ? limit : DEFAULT_POPULAR_LIMIT;
        return recipeRepository.findPopularRecipes(
                PageRequest.of(0, validLimit));
    }

    /**
     * Retrieves all recipes from the database.
     *
     * @return list of all recipes
     */
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    /**
     * Retrieves all favorite recipes for a given user.
     * Currently returns an empty list until
     *      the favorites feature is integrated.
     *
     * @param userId the ID of the user
     * @return list of the user's favorite recipes (currently empty)
     */
    public List<Recipe> getUserFavoriteRecipes(final Integer userId) {
        // Placeholder until FavoriteRecipeRepository is implemented
        return List.of();
    }
}
