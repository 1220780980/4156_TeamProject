package com.example.fitness.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

/**
 * Represents a weekly meal plan starting from a given date and
 * containing a list of daily meal plans.
 */
@Data
public final class WeeklyMealPlan {

    /** Start date of this weekly meal plan. */
    private LocalDate startDate;

    /** List of daily meal plans for each day in the week. */
    private List<DailyMealPlan> days;
}
