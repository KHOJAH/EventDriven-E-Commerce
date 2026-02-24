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
 * Order Model - Represents an e-commerce order event
 * 
 * This model is used across different Kafka topics:
 * - order-created: When a new order is placed
 * - order-confirmed: When order is confirmed after payment
 * - order-cancelled: When order is cancelled
 * 
 * @author Kafka Mastery Project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    /**
     * Unique order identifier
     * CHALLENGE 1.1: Why do we use UUID instead of auto-increment ID in event-driven systems?
     * 💡 Hint: Think about distributed systems and message deduplication
     * 📝 Solution: See KAFKA_CONCEPTS.md - Event Design section
     */
    private String orderId;
    
    /**
     * Customer who placed the order
     */
    private String customerId;
    
    /**
     * Customer email for notifications
     */
    private String customerEmail;
    
    /**
     * Total order amount
     */
    private BigDecimal totalAmount;
    
    /**
     * Order status
     * CHALLENGE 1.2: What are the possible order statuses in our flow?
     * 💡 Hint: Think about the order lifecycle
     * 📝 Solution: Commented at bottom of this file
     */
    private OrderStatus status;
    
    /**
     * List of items (simplified as comma-separated for this example)
     * In production, this would be a List<OrderItem>
     */
    private String items;
    
    /**
     * Shipping address
     */
    private String shippingAddress;
    
    /**
     * Timestamp when order was created
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant createdAt;
    
    /**
     * Timestamp when order was last updated
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant updatedAt;
    
    /**
     * Correlation ID for distributed tracing
     * CHALLENGE 6.1: Implement distributed tracing using this field
     * 💡 Hint: Pass this ID in Kafka headers across all events
     * 📝 Solution: See OrderProducer.java
     */
    private String correlationId;
    
    /**
     * Idempotency key to prevent duplicate processing
     * CHALLENGE 5.3: Use this key to implement idempotent consumers
     * 💡 Hint: Store processed keys in Redis/DB, check before processing
     * 📝 Solution: See InventoryConsumer.java
     */
    private String idempotencyKey;
    
    /**
     * Enum for Order Status
     */
    public enum OrderStatus {
        PENDING,      // Order created, awaiting payment
        CONFIRMED,    // Payment successful, order confirmed
        PROCESSING,   // Being prepared for shipment
        SHIPPED,      // Order shipped
        DELIVERED,    // Order delivered to customer
        CANCELLED,    // Order cancelled
        FAILED        // Payment or processing failed
    }
    
    /**
     * Helper method to create a new order with generated IDs
     * 
     * CHALLENGE 1.3: Complete this factory method
     * TODO: Generate UUID for orderId and correlationId if not provided
     * TODO: Set createdAt to current instant
     * TODO: Set initial status to PENDING
     * 
     * 💡 Hint: Use UUID.randomUUID() and Instant.now()
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public static Order createNew(String customerId, String customerEmail, 
     *                               BigDecimal totalAmount, String items, 
     *                               String shippingAddress) {
     *     String orderId = UUID.randomUUID().toString();
     *     String correlationId = UUID.randomUUID().toString();
     *     Instant now = Instant.now();
     *     
     *     return Order.builder()
     *         .orderId(orderId)
     *         .customerId(customerId)
     *         .customerEmail(customerEmail)
     *         .totalAmount(totalAmount)
     *         .items(items)
     *         .shippingAddress(shippingAddress)
     *         .status(OrderStatus.PENDING)
     *         .createdAt(now)
     *         .updatedAt(now)
     *         .correlationId(correlationId)
     *         .idempotencyKey("ORDER_" + orderId + "_" + now.toEpochMilli())
     *         .build();
     * }
     * ```
     */
    public static Order createNew(String customerId, String customerEmail,
                                  BigDecimal totalAmount, String items,
                                  String shippingAddress) {
        // TODO: Implement this factory method (see challenge above)
        // Write your code here
        
        
        
        // Placeholder - replace with your implementation
        return null;
    }
    
    /**
     * Helper method to confirm an order
     * 
     * CHALLENGE 1.4: Implement order confirmation
     * TODO: Return a new Order with status CONFIRMED
     * TODO: Update the updatedAt timestamp
     * TODO: Keep the same correlationId for tracing
     * 
     * 💡 Hint: Use the builder pattern to create a modified copy
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public Order confirm() {
     *     return Order.builder()
     *         .orderId(this.orderId)
     *         .customerId(this.customerId)
     *         .customerEmail(this.customerEmail)
     *         .totalAmount(this.totalAmount)
     *         .items(this.items)
     *         .shippingAddress(this.shippingAddress)
     *         .status(OrderStatus.CONFIRMED)
     *         .createdAt(this.createdAt)
     *         .updatedAt(Instant.now())
     *         .correlationId(this.correlationId)
     *         .idempotencyKey(this.idempotencyKey)
     *         .build();
     * }
     * ```
     */
    public Order confirm() {
        // TODO: Implement order confirmation (see challenge above)
        // Write your code here
        
        
        
        // Placeholder - replace with your implementation
        return null;
    }
    
    /**
     * Helper method to cancel an order
     * 
     * CHALLENGE 1.5: Implement order cancellation
     * TODO: Return a new Order with status CANCELLED
     * TODO: Update the updatedAt timestamp
     * 
     * 📝 SOLUTION: Implement similar to confirm() but with CANCELLED status
     */
    public Order cancel() {
        // TODO: Implement order cancellation
        // Write your code here
        
        
        
        return null;
    }
}
