package com.example.nutriflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Nutriflow Spring Boot application.
 */
@SpringBootApplication
public class WebServiceApplication {

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(final String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }

    /**
     * Instance method to clarify that this class is not a utility class.
     */
    public void init() {
        // intentionally empty
    }
}
