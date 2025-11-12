package com.nebulohub.exception;

import java.time.Instant;

/**
 * Standard JSON response for errors.
 * Used by both GlobalExceptionHandler and SecurityConfig.
 * Based on br.com.fiap.otmav.exception.ErrorResponse
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
    /**
     * Constructor for building the response from inside the GlobalExceptionHandler.
     * (We will add a path parameter later)
     */
    public ErrorResponse(int status, String error, String message) {
        this(Instant.now(), status, error, message, null);
    }

    /**
     * Constructor for building the response from SecurityConfig.
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this(Instant.now(), status, error, message, path);
    }
}