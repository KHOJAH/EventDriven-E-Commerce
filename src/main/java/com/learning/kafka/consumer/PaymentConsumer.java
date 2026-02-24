package com.learning.kafka.consumer;

import com.learning.kafka.model.Payment;
import com.learning.kafka.producer.PaymentProducer;
import com.learning.kafka.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Payment Consumer - Module 3 & 4: Advanced Consumer Patterns & Error Handling
 * 
 * This consumer processes payment events with retry and error handling.
 * 
 * Learning Objectives:
 * - Manual offset commit
 * - Retry Topics pattern
 * - Dead Letter Topic (DLT) handling
 * - Exception classification
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    
    private final PaymentService paymentService;
    private final PaymentProducer paymentProducer;
    
    /**
     * Listen to payment processing events
     * 
     * CHALLENGE 3.8: Implement payment consumer with manual ack
     * TODO: Add @KafkaListener for "payment-process" topic (you need to create this topic)
     * TODO: Use containerFactory = "kafkaListenerContainerFactory"
     * TODO: Use groupId = "payment-processor-group"
     * TODO: Add Acknowledgment parameter for manual commit
     * TODO: Call paymentService.processPayment()
     * TODO: Send result to payment-processed or payment-failed topic
     * TODO: Acknowledge the message after successful processing
     * 
     * 💡 Hint: 
     * @KafkaListener(topics = "payment-process", groupId = "payment-processor-group", 
     *               containerFactory = "kafkaListenerContainerFactory")
     * public void listen(Payment payment, Acknowledgment ack) {
     *     // Process payment
     *     ack.acknowledge(); // Manual commit
     * }
     * 
     * 📝 SOLUTION:
     * ```java
     * @KafkaListener(topics = "payment-process", groupId = "payment-processor-group",
     *               containerFactory = "kafkaListenerContainerFactory")
     * public void listenPaymentProcess(Payment payment, Acknowledgment ack) {
     *     log.info("Received payment processing request: {}", payment.getPaymentId());
     *     
     *     try {
     *         Payment result = paymentService.processPayment(
     *             Order.builder()
     *                 .orderId(payment.getOrderId())
     *                 .correlationId(payment.getCorrelationId())
     *                 .totalAmount(payment.getAmount())
     *                 .build()
     *         );
     *         
     *         paymentProducer.sendPaymentProcessed(result);
     *         ack.acknowledge(); // Manual offset commit
     *         
     *         log.info("Payment processed successfully: {}", result.getPaymentId());
     *     } catch (Exception e) {
     *         log.error("Payment processing failed: {}", e.getMessage(), e);
     *         // Don't acknowledge - will be retried or sent to DLT
     *         throw e; // Let error handler deal with it
     *     }
     * }
     * ```
     */
    // TODO: Implement payment consumer with manual ack (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * CHALLENGE 4.9: Implement @RetryableTopic for payment processing
     * TODO: Add @RetryableTopic annotation for automatic retry handling
     * TODO: Configure 3 retry attempts with exponential backoff
     * TODO: Include RetryableException, exclude NonRetryableException
     * TODO: Add @DltHandler method to handle dead letters
     * 
     * 💡 Hint:
     * @RetryableTopic(
     *     attempts = "3",
     *     backoff = @Backoff(delay = 1000, multiplier = 2),
     *     include = {RetryableException.class},
     *     exclude = {NonRetryableException.class}
     * )
     * 
     * 📝 SOLUTION:
     * ```java
     * @RetryableTopic(
     *     attempts = "3",
     *     backoff = @Backoff(delay = 1000, multiplier = 2),
     *     include = {RetryableException.class},
     *     exclude = {NonRetryableException.class},
     *     autoCreateTopics = "true"
     * )
     * @KafkaListener(topics = "payment-process", groupId = "payment-processor-group",
     *               containerFactory = "kafkaListenerContainerFactory")
     * public void listenPaymentWithRetry(Payment payment, Acknowledgment ack) {
     *     log.info("Received payment (with retry): {}", payment.getPaymentId());
     *     
     *     Payment result = paymentService.processPayment(
     *         Order.builder()
     *             .orderId(payment.getOrderId())
     *             .correlationId(payment.getCorrelationId())
     *             .totalAmount(payment.getAmount())
     *             .build()
     *     );
     *     
     *     paymentProducer.sendPaymentProcessed(result);
     *     ack.acknowledge();
     * }
     * 
     * @DltHandler
     * public void handleDlt(Payment payment, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
     *     log.error("Payment message sent to DLT after all retries: {}", payment.getPaymentId());
     *     // In production: alert operations team, store for manual review
     * }
     * ```
     */
    // TODO: Implement @RetryableTopic (see challenge above)
    // This is Module 4 - come back after completing Module 3
    
    
    
    
    
    /**
     * CHALLENGE 4.10: Add correlation ID tracking in DLT handler
     * TODO: Modify @DltHandler to include correlationId in the log
     * TODO: This helps with distributed tracing and debugging
     * 
     * 💡 Hint: Access correlationId from the payment object
     * 
     * 📝 SOLUTION: Add correlationId to the DLT handler log message
     */
    // TODO: Add correlation ID tracking (see challenge above)
    
    
    
    
}
