package com.learning.kafka.producer;

import com.learning.kafka.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Payment Producer - Module 2: Advanced Producer Patterns
 * 
 * This producer sends payment events to Kafka topics.
 * 
 * Learning Objectives:
 * - Async sending with CompletableFuture
 * - Transactional sending
 * - Error handling with callbacks
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String PAYMENT_PROCESSED_TOPIC = "payment-processed";
    private static final String PAYMENT_FAILED_TOPIC = "payment-failed";
    
    /**
     * Send payment processed event
     * 
     * CHALLENGE 2.12: Implement async payment sending
     * TODO: Use kafkaTemplate.send() with CompletableFuture
     * TODO: Add callback to log success/failure
     * TODO: Use orderId as the partition key
     * 
     * 📝 SOLUTION:
     * ```java
     * public void sendPaymentProcessed(Payment payment) {
     *     log.info("Sending payment processed event: {}", payment.getPaymentId());
     *     
     *     CompletableFuture<SendResult<String, Object>> future = 
     *         kafkaTemplate.send(PAYMENT_PROCESSED_TOPIC, payment.getOrderId(), payment);
     *     
     *     future.whenComplete((result, ex) -> {
     *         if (ex == null) {
     *             log.info("Payment event sent - Partition: {}, Offset: {}",
     *                 result.getRecordMetadata().partition(),
     *                 result.getRecordMetadata().offset());
     *         } else {
     *             log.error("Failed to send payment event: {}", ex.getMessage(), ex);
     *         }
     *     });
     * }
     * ```
     */
    public void sendPaymentProcessed(Payment payment) {
        // TODO: Implement async payment sending (see challenge above)
        // Write your code here
        
        
        
        
        log.warn("sendPaymentProcessed not implemented - {}", payment.getPaymentId());
    }
    
    /**
     * Send payment failed event
     * 
     * CHALLENGE 2.13: Implement payment failure sending
     * TODO: Send failed payment to PAYMENT_FAILED_TOPIC
     * TODO: Use async sending with callback
     * 
     * 📝 SOLUTION: Similar to sendPaymentProcessed but with PAYMENT_FAILED_TOPIC
     */
    public void sendPaymentFailed(Payment payment) {
        // TODO: Implement payment failure sending (see challenge above)
        // Write your code here
        
        
        
        
        log.warn("sendPaymentFailed not implemented - {}", payment.getPaymentId());
    }
    
    /**
     * CHALLENGE 2.14: Implement transactional sending
     * TODO: Create a method that sends payment in a transaction
     * TODO: Use kafkaTemplate.executeInTransaction()
     * TODO: Send both payment processed and order confirmed events atomically
     * 
     * 💡 Hint: 
     * kafkaTemplate.executeInTransaction(operations -> {
     *     operations.send(...);
     *     operations.send(...);
     *     return null;
     * });
     * 
     * 📝 SOLUTION:
     * ```java
     * public void sendPaymentProcessedTransactional(Payment payment, Order confirmedOrder) {
     *     log.info("Sending payment in transaction: {}", payment.getPaymentId());
     *     
     *     kafkaTemplate.executeInTransaction(operations -> {
     *         operations.send(PAYMENT_PROCESSED_TOPIC, payment.getOrderId(), payment);
     *         operations.send("order-confirmed", confirmedOrder.getOrderId(), confirmedOrder);
     *         return null;
     *     });
     *     
     *     log.info("Transaction completed successfully");
     * }
     * ```
     */
    // TODO: Implement transactional sending (see challenge above)
    
    
    
    
}
