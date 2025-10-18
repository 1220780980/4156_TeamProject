package com.example.nutriflow.service;

import com.example.nutriflow.model.User;
import com.example.nutriflow.model.dto.UpdateUserRequest;
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
    public Optional<User> updateUser(Integer userId, UpdateUserRequest request) {
        return userRepository.findUserById(userId)
                .map(existingUser -> {
                    // Update fields if they are provided
                    if (request.getHeight() != null) {
                        existingUser.setHeight(request.getHeight());
                    }
                    if (request.getWeight() != null) {
                        existingUser.setWeight(request.getWeight());
                    }
                    if (request.getAge() != null) {
                        existingUser.setAge(request.getAge());
                    }
                    if (request.getSex() != null) {
                        existingUser.setSex(request.getSex());
                    }
                    if (request.getAllergies() != null) {
                        existingUser.setAllergies(request.getAllergies());
                    }
                    if (request.getDislikes() != null) {
                        existingUser.setDislikes(request.getDislikes());
                    }
                    if (request.getBudget() != null) {
                        existingUser.setBudget(request.getBudget());
                    }
                    if (request.getCookingSkill() != null) {
                        existingUser.setCookingSkillLevel(request.getCookingSkill());
                    }
                    if (request.getEquipments() != null) {
                        existingUser.setEquipments(request.getEquipments());
                    }
                    
                    return userRepository.save(existingUser);
                });
    }
}