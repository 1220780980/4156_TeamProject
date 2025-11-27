package com.example.fitness.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.fitness.model.AppUser;
import com.example.fitness.model.dto.LoginResponse;
import com.example.fitness.model.dto.RegistrationRequest;
import com.example.fitness.model.dto.RegistrationResponse;
import com.example.fitness.model.dto.UserProfileResponse;
import com.example.fitness.model.dto.UpdateUserProfileRequest;
import com.example.fitness.repository.AppUserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling app user operations.
 */
@Service
public class AppUserService {

    /**
     * Repository for app user data.
     */
    @Autowired
    private AppUserRepository appUserRepository;

    /**
     * Client for communicating with NutriFlow.
     */
    @Autowired
    private NutriflowClient nutriflowClient;

    /**
     * Register a new user.
     *
     * @param request the registration request
     * @return RegistrationResponse containing user information
     * @throws RuntimeException if email already exists or registration fails
     */
    @Transactional
    public RegistrationResponse registerUser(RegistrationRequest request) {
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        // Hash the password using BCrypt
        String hashedPassword = BCrypt.withDefaults()
            .hashToString(12, request.getPassword().toCharArray());

        // Create AppUser entity
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setPasswordHash(hashedPassword);
        appUser.setAge(request.getAge());
        appUser.setHeight(request.getHeight());
        appUser.setWeight(request.getWeight());
        appUser.setSex(request.getSex());
        appUser.setActivityLevel(request.getActivityLevel());
        appUser.setFitnessGoal(request.getFitnessGoal());

        AppUser savedAppUser = appUserRepository.save(appUser);

        try {
            // Call NutriFlow to create user
            Long nutriflowUserId = nutriflowClient.createNutriflowUser(
                    savedAppUser.getId(),
                    request.getEmail(), // Use email as name
                    request.getHeight(),
                    request.getWeight(),
                    request.getAge(),
                    request.getSex()
            );

            // Update AppUser with nutriflowUserId
            savedAppUser.setNutriflowUserId(nutriflowUserId);
            savedAppUser = appUserRepository.save(savedAppUser);

        } catch (Exception e) {
            // If NutriFlow creation fails, keep the local user but log error
            System.err.println("Failed to create NutriFlow user: " 
                + e.getMessage());
        }

        // Build and return response
        RegistrationResponse response = new RegistrationResponse();
        response.setAppUserId(savedAppUser.getId());
        response.setEmail(savedAppUser.getEmail());
        response.setAge(savedAppUser.getAge());
        response.setHeight(savedAppUser.getHeight());
        response.setWeight(savedAppUser.getWeight());
        response.setSex(savedAppUser.getSex());
        response.setActivityLevel(savedAppUser.getActivityLevel());
        response.setFitnessGoal(savedAppUser.getFitnessGoal());
        response.setNutriflowUserId(savedAppUser.getNutriflowUserId());
        response.setMessage("User registered successfully");

        return response;
    }

    /**
     * Authenticate a user with email and password.
     *
     * @param email the user's email
     * @param password the user's plain text password
     * @return LoginResponse containing user information
     * @throws RuntimeException if authentication fails
     */
    public LoginResponse loginUser(String email, String password) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify password using BCrypt
        BCrypt.Result result = BCrypt.verifyer()
                .verify(password.toCharArray(), user.getPasswordHash());

        if (!result.verified) {
            throw new RuntimeException("Invalid email or password");
        }

        // Build and return response
        LoginResponse response = new LoginResponse();
        response.setAppUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setAge(user.getAge());
        response.setHeight(user.getHeight());
        response.setWeight(user.getWeight());
        response.setSex(user.getSex());
        response.setActivityLevel(user.getActivityLevel());
        response.setFitnessGoal(user.getFitnessGoal());
        response.setNutriflowUserId(user.getNutriflowUserId());
        response.setMessage("Login successful");

        return response;
    }

    /**
     * Get user profile by app user ID.
     *
     * @param appUserId the app user ID
     * @return Optional containing UserProfileResponse if found
     */
    public Optional<UserProfileResponse> getUserProfile(Long appUserId) {
        return appUserRepository.findById(appUserId)
                .map(this::convertToProfileResponse);
    }

    /**
     * Update user profile.
     *
     * @param appUserId the app user ID
     * @param request the update request containing fields to update
     * @return Optional containing updated UserProfileResponse if successful
     */
    @Transactional
    public Optional<UserProfileResponse> updateUserProfile(
            Long appUserId, 
            UpdateUserProfileRequest request) {
        
        return appUserRepository.findById(appUserId)
                .map(user -> {
                    // Update fields if they are provided (not null)
                    if (request.getAge() != null) {
                        user.setAge(request.getAge());
                    }
                    if (request.getHeight() != null) {
                        user.setHeight(request.getHeight());
                    }
                    if (request.getWeight() != null) {
                        user.setWeight(request.getWeight());
                    }
                    if (request.getSex() != null) {
                        user.setSex(request.getSex());
                    }
                    if (request.getActivityLevel() != null) {
                        user.setActivityLevel(request.getActivityLevel());
                    }
                    if (request.getFitnessGoal() != null) {
                        user.setFitnessGoal(request.getFitnessGoal());
                    }
                    
                    // Save and return
                    AppUser updatedUser = appUserRepository.save(user);
                    return convertToProfileResponse(updatedUser);
                });
    }

    /**
     * Helper method to convert AppUser entity to UserProfileResponse DTO.
     *
     * @param user the AppUser entity
     * @return UserProfileResponse DTO
     */
    private UserProfileResponse convertToProfileResponse(AppUser user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setAppUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setAge(user.getAge());
        response.setHeight(user.getHeight());
        response.setWeight(user.getWeight());
        response.setSex(user.getSex());
        response.setActivityLevel(user.getActivityLevel());
        response.setFitnessGoal(user.getFitnessGoal());
        response.setNutriflowUserId(user.getNutriflowUserId());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
