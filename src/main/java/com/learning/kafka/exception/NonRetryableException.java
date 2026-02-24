package com.learning.kafka.exception;

/**
 * Non-Retryable Exception - Indicates an error that should NOT be retried
 * 
 * Used in Module 4: Error Handling & Retry Patterns
 * 
 * Examples of non-retryable errors:
 * - Validation errors (invalid data)
 * - Business rule violations
 * - Authentication/authorization failures
 * - Resource not found (won't appear magically)
 * - Malformed JSON
 * 
 * When these exceptions occur, the message should go directly to Dead Letter Topic (DLT)
 * 
 * @author Kafka Mastery Project
 */
public class NonRetryableException extends RuntimeException {
    
    /**
     * Constructs a new non-retryable exception with the specified detail message
     * 
     * @param message the detail message
     */
    public NonRetryableException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new non-retryable exception with the specified detail message and cause
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public NonRetryableException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new non-retryable exception with the specified cause
     * 
     * @param cause the cause
     */
    public NonRetryableException(Throwable cause) {
        super(cause);
    }
}
