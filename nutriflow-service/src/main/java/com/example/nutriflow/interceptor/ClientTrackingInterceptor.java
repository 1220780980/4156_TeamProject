package com.example.nutriflow.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor for tracking client requests.
 * Extracts X-Client-Id and X-End-User-Id headers for logging purposes.
 */
@Component
public final class ClientTrackingInterceptor implements HandlerInterceptor {

    /** Logger for this class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(ClientTrackingInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        String clientId = request.getHeader("X-Client-Id");
        String endUserId = request.getHeader("X-End-User-Id");

        if (clientId != null || endUserId != null) {
            LOGGER.info("Request from client: {}, end user: {}, path: {} {}",
                    clientId, endUserId, request.getMethod(),
                    request.getRequestURI());
        }

        return true;
    }
}
