package com.example.nutriflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user creation response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponseDTO {

    /**
     * The ID of the newly created user.
     */
    private Integer userId;

    /**
     * The name of the newly created user.
     */
    private String name;

    /**
     * Message indicating successful creation.
     */
    private String message;
}
