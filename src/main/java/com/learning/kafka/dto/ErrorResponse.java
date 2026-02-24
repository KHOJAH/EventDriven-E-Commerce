package com.learning.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Error Response DTO - Standardized error response for REST API
 * 
 * Used for returning error details to clients in a consistent format.
 * 
 * @author Kafka Mastery Project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * Error message
     */
    private String message;
    
    /**
     * Timestamp when error occurred
     */
    private Instant timestamp;
    
    /**
     * Additional error details (optional)
     */
    private String details;
    
    /**
     * Factory method to create error response
     * 
     * CHALLENGE 1.23: Implement factory method
     * TODO: Create ErrorResponse with status, message, and current timestamp
     * 
     * 💡 Hint: Use Instant.now() for timestamp
     * 
     * 📝 SOLUTION:
     * ```java
     * public static ErrorResponse create(int status, String message) {
     *     return ErrorResponse.builder()
     *         .status(status)
     *         .message(message)
     *         .timestamp(Instant.now())
     *         .build();
     * }
     * ```
     */
    public static ErrorResponse create(int status, String message) {
        // TODO: Implement factory method (see challenge above)
        // Write your code here
        
        
        
        return null;
    }
    
    /**
     * Factory method to create error response with details
     * 
     * CHALLENGE 1.24: Implement factory method with details
     * TODO: Create ErrorResponse with all fields populated
     * 
     * 📝 SOLUTION:
     * ```java
     * public static ErrorResponse create(int status, String message, String details) {
     *     return ErrorResponse.builder()
     *         .status(status)
     *         .message(message)
     *         .details(details)
     *         .timestamp(Instant.now())
     *         .build();
     * }
     * ```
     */
    public static ErrorResponse create(int status, String message, String details) {
        // TODO: Implement factory method with details
        // Write your code here
        
        
        
        return null;
    }
}
