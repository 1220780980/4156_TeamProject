package com.example.nutriflow.mealplan.repository;

import com.example.nutriflow.mealplan.model.DailyMealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DailyMealPlan entity.
 */
@Repository
public interface DailyMealPlanRepository
        extends JpaRepository<DailyMealPlan, Integer> {

    /**
     * Find all daily meal plans for a specific user.
     *
     * @param userId the user ID
     * @return list of daily meal plans
     */
    @Query("SELECT d FROM DailyMealPlan d "
            + "WHERE d.userId = :userId")
    List<DailyMealPlan> findByUserId(
            @Param("userId") Integer userId);

    /**
     * Find a daily meal plan for a specific user and date.
     *
     * @param userId the user ID
     * @param planDate the plan date
     * @return optional daily meal plan
     */
    @Query("SELECT d FROM DailyMealPlan d "
            + "WHERE d.userId = :userId "
            + "AND d.planDate = :planDate")
    Optional<DailyMealPlan> findByUserIdAndPlanDate(
            @Param("userId") Integer userId,
            @Param("planDate") LocalDate planDate);

    /**
     * Find all daily meal plans for a specific weekly plan.
     *
     * @param weeklyPlanId the weekly plan ID
     * @return list of daily meal plans
     */
    @Query("SELECT d FROM DailyMealPlan d "
            + "WHERE d.weeklyPlanId = :weeklyPlanId")
    List<DailyMealPlan> findByWeeklyPlanId(
            @Param("weeklyPlanId") Integer weeklyPlanId);
}
