package com.learning.kafka.service;

import com.learning.kafka.dto.OrderRequest;
import com.learning.kafka.model.Order;
import com.learning.kafka.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Order Service - Module 1: Kafka Fundamentals
 * 
 * Business logic for order processing.
 * This service creates orders and publishes events via Kafka.
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderProducer orderProducer;
    
    /**
     * Create a new order
     * 
     * CHALLENGE 1.32: Implement order creation flow
     * TODO: Use Order.createNew() factory method to create the order
     * TODO: Send order created event using orderProducer.sendOrderCreatedAsync()
     * TODO: Return the created order
     * 
     * 💡 Hint: Call the factory method you implemented in Order.java
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public Order createOrder(OrderRequest request) {
     *     log.info("Creating order for customer: {}", request.getCustomerId());
     *     
     *     Order order = Order.createNew(
     *         request.getCustomerId(),
     *         request.getCustomerEmail(),
     *         request.getTotalAmount(),
     *         request.getItems(),
     *         request.getShippingAddress()
     *     );
     *     
     *     orderProducer.sendOrderCreatedAsync(order);
     *     
     *     log.info("Order created successfully: {}", order.getOrderId());
     *     return order;
     * }
     * ```
     */
    public Order createOrder(OrderRequest request) {
        // TODO: Implement order creation flow (see challenge above)
        // Write your code here
        
        
        
        
        // Placeholder - replace with your implementation
        log.warn("createOrder not implemented");
        return null;
    }
    
    /**
     * Confirm an order (called after successful payment)
     * 
     * CHALLENGE 1.33: Implement order confirmation flow
     * TODO: Create a confirmed order using order.confirm()
     * TODO: Send order confirmed event using orderProducer.sendOrderConfirmed()
     * TODO: Return the confirmed order
     * 
     * 📝 SOLUTION: Similar to createOrder but uses order.confirm()
     */
    public Order confirmOrder(Order order) {
        // TODO: Implement order confirmation flow (see challenge above)
        // Write your code here
        
        
        
        
        log.warn("confirmOrder not implemented");
        return null;
    }
    
    /**
     * Cancel an order
     * 
     * CHALLENGE 1.34: Implement order cancellation flow
     * TODO: Create a cancelled order using order.cancel()
     * TODO: Send order cancelled event using orderProducer.sendOrderCancelled()
     * TODO: Return the cancelled order
     * 
     * 📝 SOLUTION: Similar to confirmOrder but uses order.cancel()
     */
    public Order cancelOrder(Order order) {
        // TODO: Implement order cancellation flow
        // Write your code here
        
        
        
        
        log.warn("cancelOrder not implemented");
        return null;
    }
}
