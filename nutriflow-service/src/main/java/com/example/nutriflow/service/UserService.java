package com.example.nutriflow.service;

import com.example.nutriflow.model.User;
import com.example.nutriflow.model.dto.UpdateUserRequestDTO;
import com.example.nutriflow.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for handling user-related business logic
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Find a user by their user ID
     * @param userId the ID of the user to find
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findUserById(userId);
    }
    
    /**
     * Update an existing user with new information
     * @param userId the ID of the user to update
     * @param request the request containing updated information
     * @return Optional containing the updated user if successful, empty if user not found
     */
    @Transactional
    public Optional<User> updateUser(Integer userId, UpdateUserRequestDTO request) {
        return userRepository.findUserById(userId)
                .map(existingUser -> {
                    // Update fields if they are provided
                    updateIfNotNull(request.getHeight(), existingUser::setHeight);
                    updateIfNotNull(request.getWeight(), existingUser::setWeight);
                    updateIfNotNull(request.getAge(), existingUser::setAge);
                    updateIfNotNull(request.getSex(), existingUser::setSex);
                    updateIfNotNull(request.getAllergies(), existingUser::setAllergies);
                    updateIfNotNull(request.getDislikes(), existingUser::setDislikes);
                    updateIfNotNull(request.getBudget(), existingUser::setBudget);
                    updateIfNotNull(request.getCookingSkill(), existingUser::setCookingSkillLevel);
                    updateIfNotNull(request.getEquipments(), existingUser::setEquipments);
                    
                    return userRepository.save(existingUser);
                });
    }
    
    /**
     * Helper method to update a field only if the new value is not null
     * 
     * @param value the new value (can be null)
     * @param setter the setter method to call if value is not null
     * @param <T> the type of the value
     */
    private <T> void updateIfNotNull(T value, java.util.function.Consumer<T> setter) {
        Optional.ofNullable(value).ifPresent(setter);
    }
}