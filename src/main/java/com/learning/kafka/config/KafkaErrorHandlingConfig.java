package com.learning.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Kafka Error Handling Configuration
 * 
 * Configures error handling with retry mechanisms and Dead Letter Topics (DLT).
 * 
 * Key Concepts:
 * - DefaultErrorHandler: Handles exceptions during message processing
 * - DeadLetterPublishingRecoverer: Sends failed messages to DLT
 * - FixedBackOff: Waits between retry attempts
 * - Retry vs Non-Retry exceptions: Classify which errors should be retried
 * 
 * CHALLENGE 4.1: Understand error handling flow
 * TODO: Trace through what happens when a consumer throws an exception
 * 💡 Hint: Exception → ErrorHandler → Retry or DLT
 * 
 * @author Kafka Mastery Project
 */
@Configuration
public class KafkaErrorHandlingConfig {
    
    /**
     * Default Error Handler with Dead Letter Topic
     * 
     * This error handler:
     * 1. Catches exceptions during message processing
     * 2. Retries up to 3 times with 1 second delay
     * 3. Sends to Dead Letter Topic after all retries exhausted
     * 
     * CHALLENGE 4.2: Configure retry attempts
     * TODO: Set max attempts to 3 in FixedBackOff
     * 💡 Hint: new FixedBackOff(1000L, 3L) - 1 second delay, 3 attempts
     * 
     * 📝 Solution: See the implementation below (study it carefully)
     */
    @Bean
    public DefaultErrorHandler defaultErrorHandler(KafkaOperations<String, Object> kafkaTemplate) {
        // Dead Letter Publishing Recoverer - sends failed messages to DLT
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        
        // Fixed backoff: 1 second delay between retries, 3 attempts max
        // CHALLENGE 4.3: Understand FixedBackOff parameters
        // TODO: Research what the two parameters mean
        // 💡 Hint: First is delay, second is max attempts
        // 📝 Solution: FixedBackOff(delayMs, maxAttempts)
        FixedBackOff backOff = new FixedBackOff(1000L, 3L);
        
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);
        
        // CHALLENGE 4.4: Add exception classification
        // TODO: Add retryable exceptions (exceptions that SHOULD be retried)
        // 💡 Hint: errorHandler.addRetryableExceptions(RetryableException.class);
        // 📝 SOLUTION: Uncomment the line below
        // errorHandler.addRetryableExceptions(___Exception.class);
        
        // CHALLENGE 4.5: Add non-retryable exceptions
        // TODO: Add exceptions that should NOT be retried (go directly to DLT)
        // 💡 Hint: errorHandler.addNotRetryableExceptions(NonRetryableException.class);
        // 📝 SOLUTION: Uncomment the line below
        // errorHandler.addNotRetryableExceptions(___Exception.class);
        
        // TODO: Add exception classification (challenges 4.4-4.5)
        // Write your code here
        
        
        
        
        return errorHandler;
    }
    
    /**
     * CHALLENGE 4.6: Create error handler with exponential backoff
     * TODO: Create a @Bean method that uses ExponentialBackOff instead of FixedBackOff
     * TODO: Set initial interval to 1000ms, multiplier to 2.0, max interval to 10000ms
     * 
     * 💡 Hint: ExponentialBackOff provides increasing delays between retries
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * @Bean
     * public DefaultErrorHandler exponentialBackoffErrorHandler(KafkaOperations<String, Object> kafkaTemplate) {
     *     DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
     *     
     *     ExponentialBackOff backOff = new ExponentialBackOff();
     *     backOff.setInitialInterval(1000L);      // 1 second initial delay
     *     backOff.setMultiplier(2.0);             // Double delay each retry
     *     backOff.setMaxInterval(10000L);         // Max 10 seconds
     *     backOff.setMaxAttempts(3L);             // 3 attempts max
     *     
     *     DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);
     *     errorHandler.addRetryableExceptions(RetryableException.class);
     *     errorHandler.addNotRetryableExceptions(NonRetryableException.class);
     *     
     *     return errorHandler;
     * }
     * ```
     */
    // TODO: Add exponential backoff error handler (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 4.7: Understand Dead Letter Topic naming convention
     * TODO: Research how DeadLetterPublishingRecoverer names the DLT
     * 💡 Hint: It appends a suffix to the original topic name
     * 
     * 📝 Solution: Default suffix is ".DLT" (e.g., "order-created.DLT")
     *              Can be customized with withDeadLetterPublishingRecoverer()
     */
    
    /**
     * CHALLENGE 4.8: Create custom error handler for notification consumer
     * TODO: Create a separate error handler for notification processing
     * TODO: Use longer retry delays (notifications are less time-critical)
     * TODO: Set initial delay to 5000ms (5 seconds) and 5 attempts
     * 
     * 💡 Hint: Notifications can tolerate longer delays than payments
     * 
     * 📝 SOLUTION: Similar to defaultErrorHandler but with different backoff parameters
     */
    // TODO: Add custom notification error handler (see challenge above)
    
    
    
    
}
