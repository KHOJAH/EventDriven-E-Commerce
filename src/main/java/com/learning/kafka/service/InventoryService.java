package com.learning.kafka.service;

import com.learning.kafka.exception.NonRetryableException;
import com.learning.kafka.model.Inventory;
import com.learning.kafka.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Inventory Service - Module 1 & 5: Kafka Fundamentals & Idempotency
 * 
 * Business logic for inventory reservation.
 * This service manages stock levels and reservations.
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Service
public class InventoryService {
    
    private final Random random = new Random();
    
    /**
     * Simulated inventory store (in production, this would be a database)
     * CHALLENGE 5.3: Use this for idempotency checking
     */
    private final Set<String> processedReservations = ConcurrentHashMap.newKeySet();
    
    /**
     * Reserve inventory for an order
     * 
     * CHALLENGE 1.47: Implement inventory reservation
     * TODO: Create an Inventory using Inventory.create()
     * TODO: Check if reservation was already processed (idempotency)
     * TODO: Simulate stock check (90% success, 10% out of stock)
     * TODO: Return the inventory with appropriate status
     * 
     * 💡 Hint: Use idempotencyKey to prevent duplicate processing
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public Inventory reserveInventory(Order order) {
     *     log.info("Reserving inventory for order: {}", order.getOrderId());
     *     
     *     // Idempotency check - CHALLENGE 5.3
     *     if (processedReservations.contains(order.getIdempotencyKey())) {
     *         log.warn("Duplicate reservation request - ignoring: {}", order.getIdempotencyKey());
     *         throw new NonRetryableException("Duplicate reservation: " + order.getIdempotencyKey());
     *     }
     *     
     *     Inventory inventory = Inventory.create(
     *         order.getOrderId(),
     *         order.getCorrelationId(),
     *         order.getItems(),
     *         1, // quantity
     *         "WAREHOUSE-001"
     *     );
     *     
     *     // Simulate stock check
     *     if (random.nextInt(100) < 90) {
     *         // 90% success rate
     *         log.info("Inventory reserved: {}", inventory.getReservationId());
     *         processedReservations.add(order.getIdempotencyKey()); // Mark as processed
     *         return inventory.reserve();
     *     } else {
     *         // 10% out of stock
     *         log.error("Out of stock for order: {}", order.getOrderId());
     *         return inventory.fail("Insufficient stock");
     *     }
     * }
     * ```
     */
    public Inventory reserveInventory(Order order) {
        // TODO: Implement inventory reservation (see challenge above)
        // Write your code here
        
        
        
        
        // Placeholder - replace with your implementation
        log.warn("reserveInventory not implemented");
        return null;
    }
    
    /**
     * Release inventory reservation (when order is cancelled or payment fails)
     * 
     * CHALLENGE 1.48: Implement inventory release
     * TODO: Create an Inventory object
     * TODO: Call inventory.release() to mark as released
     * TODO: Remove from processedReservations set
     * TODO: Return the released inventory
     * 
     * 📝 SOLUTION: Similar to reserveInventory but calls release() instead
     */
    public Inventory releaseInventory(Order order) {
        // TODO: Implement inventory release (see challenge above)
        // Write your code here
        
        
        
        
        log.warn("releaseInventory not implemented");
        return null;
    }
    
    /**
     * CHALLENGE 5.4: Add stock level checking
     * TODO: Create a method to check actual stock levels
     * TODO: Use a Map<String, Integer> to store SKU -> quantity
     * TODO: Decrement stock on reservation, increment on release
     * 
     * 💡 Hint: This makes the simulation more realistic
     * 
     * 📝 SOLUTION: Implement a simple in-memory stock management system
     */
    // TODO: Add stock level checking (see challenge above)
    
    
    
    
}
