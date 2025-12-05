package com.example.nutriflow.config;

import com.example.nutriflow.interceptor.ClientTrackingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration for registering interceptors.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /** Interceptor for tracking client requests. */
    @Autowired
    private ClientTrackingInterceptor clientTrackingInterceptor;

    /**
     * Registers interceptors for tracking client requests.
     * @param registry the interceptor registry
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(clientTrackingInterceptor)
                .addPathPatterns("/api/**");
    }
}
