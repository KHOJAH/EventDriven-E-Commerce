package com.learning.kafka.exception;

/**
 * Retryable Exception - Indicates an error that may be temporary and should be retried
 * 
 * Used in Module 4: Error Handling & Retry Patterns
 * 
 * Examples of retryable errors:
 * - Database connection timeout
 * - External service temporarily unavailable
 * - Network timeout
 * - Resource temporarily locked
 * 
 * @author Kafka Mastery Project
 */
public class RetryableException extends RuntimeException {
    
    /**
     * Constructs a new retryable exception with the specified detail message
     * 
     * @param message the detail message
     */
    public RetryableException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new retryable exception with the specified detail message and cause
     * 
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method)
     */
    public RetryableException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new retryable exception with the specified cause
     * 
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method)
     */
    public RetryableException(Throwable cause) {
        super(cause);
    }
}
