package com.example.nutriflow.mealplan.repository;

import com.example.nutriflow.mealplan.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Meal entity.
 */
@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    /**
     * Find all meals for a specific daily meal plan.
     *
     * @param dailyPlanId the daily meal plan ID
     * @return list of meals
     */
    @Query("SELECT m FROM Meal m WHERE m.dailyPlanId = :dailyPlanId")
    List<Meal> findByDailyPlanId(@Param("dailyPlanId") Integer dailyPlanId);
}

