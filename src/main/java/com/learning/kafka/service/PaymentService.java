package com.learning.kafka.service;

import com.learning.kafka.exception.NonRetryableException;
import com.learning.kafka.exception.RetryableException;
import com.learning.kafka.model.Order;
import com.learning.kafka.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Payment Service - Module 1 & 4: Kafka Fundamentals & Error Handling
 * 
 * Business logic for payment processing.
 * This service simulates payment gateway interactions.
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Service
public class PaymentService {
    
    private final Random random = new Random();
    
    /**
     * Process payment for an order
     * 
     * CHALLENGE 1.45: Implement payment processing
     * TODO: Create a Payment using Payment.create()
     * TODO: Simulate payment processing (can succeed or fail)
     * TODO: For simulation, use random to determine success/failure
     * TODO: Return the payment with appropriate status
     * 
     * 💡 Hint: 
     * - 70% chance of success
     * - 20% chance of retryable error (temporary failure)
     * - 10% chance of non-retryable error (validation failure)
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public Payment processPayment(Order order) {
     *     log.info("Processing payment for order: {}", order.getOrderId());
     *     
     *     Payment payment = Payment.create(
     *         order.getOrderId(),
     *         order.getCorrelationId(),
     *         order.getTotalAmount(),
     *         Payment.PaymentMethod.CREDIT_CARD
     *     );
     *     
     *     // Simulate payment processing
     *     int outcome = random.nextInt(100);
     *     
     *     if (outcome < 70) {
     *         // 70% success rate
     *         log.info("Payment successful: {}", payment.getPaymentId());
     *         return payment.complete();
     *     } else if (outcome < 90) {
     *         // 20% retryable error
     *         log.warn("Temporary payment failure - will retry: {}", payment.getPaymentId());
     *         throw new RetryableException("Payment gateway temporarily unavailable");
     *     } else {
     *         // 10% non-retryable error
     *         log.error("Permanent payment failure: {}", payment.getPaymentId());
     *         throw new NonRetryableException("Invalid payment method");
     *     }
     * }
     * ```
     */
    public Payment processPayment(Order order) {
        // TODO: Implement payment processing (see challenge above)
        // Write your code here
        
        
        
        
        // Placeholder - replace with your implementation
        log.warn("processPayment not implemented");
        return null;
    }
    
    /**
     * CHALLENGE 1.46: Add payment failure handling
     * TODO: Create a method to handle payment failures
     * TODO: Return a Payment with FAILED status
     * TODO: Include the failure reason
     * 
     * 📝 SOLUTION:
     * ```java
     * public Payment handlePaymentFailure(Order order, String reason) {
     *     Payment payment = Payment.create(
     *         order.getOrderId(),
     *         order.getCorrelationId(),
     *         order.getTotalAmount(),
     *         Payment.PaymentMethod.CREDIT_CARD
     *     );
     *     return payment.fail(reason);
     * }
     * ```
     */
    // TODO: Add payment failure handling method (see challenge above)
    
    
    
    
}
