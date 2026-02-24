package com.learning.kafka.saga;

import com.learning.kafka.model.Inventory;
import com.learning.kafka.model.Order;
import com.learning.kafka.model.Payment;
import com.learning.kafka.producer.InventoryProducer;
import com.learning.kafka.producer.PaymentProducer;
import com.learning.kafka.service.InventoryService;
import com.learning.kafka.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Order Saga Orchestrator - Module 5: Event-Driven Architecture Patterns
 * 
 * Implements the Saga Pattern for distributed order processing.
 * 
 * Saga Flow:
 * 1. Order Created → Process Payment
 * 2. Payment Success → Reserve Inventory
 * 3. Inventory Reserved → Send Confirmation
 * 4. Any Step Fails → Execute Compensating Transactions
 * 
 * Compensating Transactions:
 * - Payment Failed → Cancel Order
 * - Inventory Failed → Refund Payment
 * 
 * CHALLENGE 5.18: Understand the Saga Pattern
 * TODO: Research why we need Saga for distributed transactions
 * 💡 Hint: Think about ACID properties across microservices
 * 
 * 📝 Solution:
 * Traditional transactions (ACID) don't work across microservices.
 * Saga provides eventual consistency through:
 * 1. Sequence of local transactions
 * 2. Compensating transactions for rollback
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaOrchestrator {
    
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final PaymentProducer paymentProducer;
    private final InventoryProducer inventoryProducer;
    
    /**
     * Step 1: Process payment when order is created
     * 
     * CHALLENGE 5.19: Implement saga step 1 - payment processing
     * TODO: Add @KafkaListener for "order-created" topic
     * TODO: Call paymentService.processPayment()
     * TODO: On success: send to payment-processed topic
     * TODO: On failure: send to payment-failed topic (triggers compensation)
     * 
     * 💡 Hint: This starts the saga flow
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * @KafkaListener(topics = "order-created", groupId = "saga-orchestrator-group")
     * public void processOrder(Order order) {
     *     log.info("Saga Step 1: Processing payment for order: {}", order.getOrderId());
     *     
     *     try {
     *         Payment payment = paymentService.processPayment(order);
     *         paymentProducer.sendPaymentProcessed(payment);
     *         log.info("Saga Step 1 Complete: Payment processed");
     *     } catch (Exception e) {
     *         log.error("Saga Step 1 Failed: Payment failed", e);
     *         
     *         // Trigger compensation
     *         Payment failedPayment = Payment.create(
     *             order.getOrderId(),
     *             order.getCorrelationId(),
     *             order.getTotalAmount(),
     *             Payment.PaymentMethod.CREDIT_CARD
     *         ).fail(e.getMessage());
     *         
     *         paymentProducer.sendPaymentFailed(failedPayment);
     *     }
     * }
     * ```
     */
    // TODO: Implement saga step 1 (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * Step 2: Reserve inventory when payment is successful
     * 
     * CHALLENGE 5.20: Implement saga step 2 - inventory reservation
     * TODO: Add @KafkaListener for "payment-processed" topic
     * TODO: Call inventoryService.reserveInventory()
     * TODO: On success: send to inventory-reserved topic
     * TODO: On failure: initiate compensation (refund payment)
     * 
     * 📝 SOLUTION: Similar to step 1 but for inventory
     */
    // TODO: Implement saga step 2 (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * CHALLENGE 5.21: Implement compensation - refund payment
     * TODO: Create a method to handle inventory reservation failure
     * TODO: Send refund request to payment service
     * TODO: Update order status to FAILED
     * 
     * 💡 Hint: Compensation is the reverse of the original action
     * 
     * 📝 SOLUTION:
     * ```java
     * private void compensatePayment(Order order, String reason) {
     *     log.info("Compensating payment for order: {}", order.getOrderId());
     *     
     *     // In a real system, this would call a refund API
     *     Payment refund = Payment.create(
     *         order.getOrderId(),
     *         order.getCorrelationId(),
     *         order.getTotalAmount(),
     *         Payment.PaymentMethod.CREDIT_CARD
     *     ).fail("Refund: " + reason);
     *     
     *     paymentProducer.sendPaymentFailed(refund);
     * }
     * ```
     */
    // TODO: Implement payment compensation (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 5.22: Add saga timeout handling
     * TODO: Implement timeout for each saga step
     * TODO: If inventory not reserved within 5 minutes, cancel payment
     * TODO: Use @Scheduled to check for timed-out sagas
     * 
     * 💡 Hint: Store saga state in a database with timestamps
     * 
     * 📝 SOLUTION: Create SagaState entity and timeout checker
     */
    // TODO: Add saga timeout handling (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 5.23: Implement saga state persistence
     * TODO: Create SagaState entity to track saga progress
     * TODO: Store: sagaId, orderId, currentStep, status, timestamps
     * TODO: Update state after each successful step
     * TODO: Use for recovery and timeout handling
     * 
     * 💡 Hint: This enables saga recovery after service restart
     * 
     * 📝 SOLUTION: Similar to OutboxEvent entity
     */
    // TODO: Implement saga state persistence (see challenge above)
    
    
    
    
}
