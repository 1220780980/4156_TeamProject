package com.example.nutriflow.service.repository;

import com.example.nutriflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserById(Integer userId);

}
