package com.learning.kafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Payment Model - Represents a payment event in the order processing flow
 * 
 * Used in topics:
 * - payment-processed: When payment is successfully processed
 * - payment-failed: When payment fails
 * 
 * @author Kafka Mastery Project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    /**
     * Unique payment identifier
     */
    private String paymentId;
    
    /**
     * Reference to the order
     */
    private String orderId;
    
    /**
     * Correlation ID from the original order (for distributed tracing)
     * CHALLENGE 6.1: Ensure this matches the order's correlationId
     */
    private String correlationId;
    
    /**
     * Payment amount
     */
    private BigDecimal amount;
    
    /**
     * Payment method
     */
    private PaymentMethod paymentMethod;
    
    /**
     * Payment status
     */
    private PaymentStatus status;
    
    /**
     * Failure reason (if payment failed)
     */
    private String failureReason;
    
    /**
     * Transaction ID from payment gateway
     */
    private String transactionId;
    
    /**
     * Timestamp when payment was processed
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant processedAt;
    
    /**
     * Enum for Payment Methods
     */
    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        PAYPAL,
        BANK_TRANSFER,
        CRYPTO
    }
    
    /**
     * Enum for Payment Status
     */
    public enum PaymentStatus {
        PENDING,      // Payment initiated
        PROCESSING,   // Being processed by gateway
        COMPLETED,    // Payment successful
        FAILED,       // Payment failed
        REFUNDED      // Payment refunded
    }
    
    /**
     * Factory method to create a new payment
     * 
     * CHALLENGE 1.6: Implement payment creation
     * TODO: Generate paymentId and transactionId
     * TODO: Set initial status to PENDING
     * TODO: Set processedAt to current instant
     * 
     * 💡 Hint: Use UUID for IDs, PaymentStatus.PENDING for initial status
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public static Payment create(String orderId, String correlationId,
     *                              BigDecimal amount, PaymentMethod method) {
     *     String paymentId = UUID.randomUUID().toString();
     *     String transactionId = "TXN_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
     *     
     *     return Payment.builder()
     *         .paymentId(paymentId)
     *         .orderId(orderId)
     *         .correlationId(correlationId)
     *         .amount(amount)
     *         .paymentMethod(method)
     *         .status(PaymentStatus.PENDING)
     *         .transactionId(transactionId)
     *         .processedAt(Instant.now())
     *         .build();
     * }
     * ```
     */
    public static Payment create(String orderId, String correlationId,
                                 BigDecimal amount, PaymentMethod method) {
        // TODO: Implement payment creation (see challenge above)
        // Write your code here
        
        
        
        // Placeholder - replace with your implementation
        return null;
    }
    
    /**
     * Mark payment as completed
     * 
     * CHALLENGE 1.7: Implement payment completion
     * TODO: Return new Payment with status COMPLETED
     * TODO: Update processedAt timestamp
     * 
     * 📝 SOLUTION: Similar to Order.confirm() but with PaymentStatus.COMPLETED
     */
    public Payment complete() {
        // TODO: Implement payment completion
        // Write your code here
        
        
        
        return null;
    }
    
    /**
     * Mark payment as failed
     * 
     * CHALLENGE 1.8: Implement payment failure
     * TODO: Return new Payment with status FAILED
     * TODO: Set the failure reason
     * TODO: Update processedAt timestamp
     * 
     * 📝 SOLUTION: Similar to complete() but with FAILED status and failureReason
     */
    public Payment fail(String reason) {
        // TODO: Implement payment failure (see challenge above)
        // Write your code here
        
        
        
        return null;
    }
}
