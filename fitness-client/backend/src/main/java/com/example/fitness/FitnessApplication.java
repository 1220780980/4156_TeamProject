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

    public static void main(String[] args) {
        SpringApplication.run(FitnessApplication.class, args);
    }

    /**
     * Bean for making HTTP requests to external services.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
