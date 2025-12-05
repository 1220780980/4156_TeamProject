package com.example.fitness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main application class for Fitness Client Backend.
 */
@SpringBootApplication
public class FitnessApplication {

    /**
     * Main entry point for the Fitness application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(FitnessApplication.class, args);
    }

    /**
     * Bean for making HTTP requests to external services.
     *
     * @return RestTemplate instance for HTTP requests
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
