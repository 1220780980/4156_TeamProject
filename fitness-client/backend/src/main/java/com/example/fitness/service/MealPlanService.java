package com.example.fitness.service;

import com.example.fitness.model.AppUser;
import com.example.fitness.model.dto.MealPlanRequestDTO;
import com.example.fitness.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsible for constructing meal plan request payloads that will be
 * sent to the NutriFlow service. It enriches client-provided preferences with
 * stored user information such as the NutriFlow user identifier.
 */
@Service
public final class MealPlanService {

    /** Repository for looking up app users by their identifier. */
    private final AppUserRepository userRepository;

    /**
     * Creates a new MealPlanService with the required dependencies.
     *
     * @param appUserRepository repository used to retrieve user information
     */
    @Autowired
    public MealPlanService(final AppUserRepository appUserRepository) {
        this.userRepository = appUserRepository;
    }

    /**
     * Builds a MealPlanRequestDTO by attaching the user's NutriFlow ID to the
     * preferences provided by the client.
     *
     * @param appUserId   the app user identifier
     * @param preferences the user preferences for the meal plan
     * @return a MealPlanRequestDTO including the NutriFlow user identifier
     */
    public MealPlanRequestDTO buildMealPlanRequest(
            final Long appUserId,
            final MealPlanRequestDTO preferences) {

        AppUser user = userRepository.findById(appUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        preferences.setNutriflowUserId(user.getNutriflowUserId());
        return preferences;
    }
}
