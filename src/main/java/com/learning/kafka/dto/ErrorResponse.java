package com.learning.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Error Response DTO - Standardized error response for REST API
 * <p>
 * Used for returning error details to clients in a consistent format.
 *
 * @author Kafka Mastery Project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private Instant timestamp;
    private String details;

    public static ErrorResponse create(int status, String message) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static ErrorResponse create(int status, String message, String details) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .timestamp(Instant.now())
                .details(details)
                .build();
    }
}
