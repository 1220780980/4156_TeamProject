package com.example.nutriflow.service;

import com.example.nutriflow.model.PantryItem;
import com.example.nutriflow.service.repository.PantryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for user pantry operations.
 * Provides read and replace-all behaviors for a user's pantry.
 */
@Service
public class PantryService {

    /** Repository for accessing pantry item persistence. */
    @Autowired
    private PantryRepository pantryRepository;

    /**
     * Retrieves all pantry items for the given user.
     *
     * @param userId the user ID
     * @return list of pantry items owned by the user (may be empty)
     */
    public List<PantryItem> getPantryItems(final Integer userId) {
        return pantryRepository.findByUserId(userId);
    }

    /**
     * Replaces the user's pantry with the provided items.
     * Existing items for the user are deleted and replaced
     * with the given list; each item is bound to the userId.
     *
     * @param userId the user ID
     * @param items  the new set of pantry items to persist for the user
     * @return the persisted list of pantry items
     */
    public List<PantryItem> updatePantryItems(
            final Integer userId,
            final List<PantryItem> items) {

        items.forEach(item -> item.setUserId(userId));
        pantryRepository.deleteAll(
                pantryRepository.findByUserId(userId));
        return pantryRepository.saveAll(items);
    }
}
