package com.example.fitness.repository;

import com.example.fitness.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for AppUser entity.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Find an app user by email.
     *
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<AppUser> findByEmail(String email);

    /**
     * Check if a user with the given email exists.
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}
