package com.example.nutriflow.mealplan.repository;

import com.example.nutriflow.mealplan.model.WeeklyMealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for WeeklyMealPlan entity.
 */
@Repository
public interface WeeklyMealPlanRepository
        extends JpaRepository<WeeklyMealPlan, Integer> {

    /**
     * Find all weekly meal plans for a specific user.
     *
     * @param userId the user ID
     * @return list of weekly meal plans
     */
    @Query("SELECT w FROM WeeklyMealPlan w "
            + "WHERE w.userId = :userId")
    List<WeeklyMealPlan> findByUserId(
            @Param("userId") Integer userId);
}
