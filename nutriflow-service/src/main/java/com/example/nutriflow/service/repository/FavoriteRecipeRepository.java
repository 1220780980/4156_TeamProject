package com.example.nutriflow.service.repository;

import com.example.nutriflow.model.FavoriteRecipe;
import com.example.nutriflow.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing favorite recipe associations.
 */
@Repository
public interface FavoriteRecipeRepository extends
    JpaRepository<FavoriteRecipe, Integer> {

    /**
     * Find recipes favorited by a specific user,
     *      ordered by times_used descending.
     *
     * @param userId the user ID
     * @return list of {@link Recipe} favorited by the user, most-used first
     */
    @Query(value = """
             SELECT r.*
             FROM nutriflow.recipes r
             JOIN nutriflow.favorite_recipes f
               ON r.recipe_id = f.recipe_id
            WHERE f.user_id = :userId
            ORDER BY f.times_used DESC
            """, nativeQuery = true)
    List<Recipe> findUserFavoriteRecipes(@Param("userId") Integer userId);
}
