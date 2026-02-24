package com.learning.kafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Inventory Model - Represents inventory reservation events
 * 
 * Used in topics:
 * - inventory-reserved: When items are successfully reserved
 * - inventory-released: When reserved items are released (e.g., payment failed)
 * 
 * @author Kafka Mastery Project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    
    /**
     * Unique inventory reservation identifier
     */
    private String reservationId;
    
    /**
     * Reference to the order
     */
    private String orderId;
    
    /**
     * Correlation ID from the original order
     */
    private String correlationId;
    
    /**
     * SKU (Stock Keeping Unit) of the item
     * In production, this would be a List<InventoryItem>
     */
    private String sku;
    
    /**
     * Quantity to reserve
     */
    private Integer quantity;
    
    /**
     * Reservation status
     */
    private ReservationStatus status;
    
    /**
     * Reason for failure (if reservation failed)
     */
    private String failureReason;
    
    /**
     * Warehouse location
     */
    private String warehouseId;
    
    /**
     * Timestamp when reservation was made
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant reservedAt;
    
    /**
     * Expiration time for reservation
     * CHALLENGE 5.1: Implement reservation timeout in Saga pattern
     * 💡 Hint: If payment not received by this time, release inventory
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant expiresAt;
    
    /**
     * Enum for Reservation Status
     */
    public enum ReservationStatus {
        PENDING,      // Reservation requested
        RESERVED,     // Items successfully reserved
        RELEASED,     // Reservation cancelled, items released
        EXPIRED,      // Reservation timed out
        FAILED        // Reservation failed (insufficient stock)
    }
    
    /**
     * Factory method to create a new inventory reservation
     * 
     * CHALLENGE 1.9: Implement inventory reservation creation
     * TODO: Generate reservationId
     * TODO: Set status to PENDING
     * TODO: Set reservedAt to current instant
     * TODO: Set expiresAt to 15 minutes from now (reservation timeout)
     * 
     * 💡 Hint: Use Instant.now().plusSeconds(900) for 15 minutes
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public static Inventory create(String orderId, String correlationId,
     *                                String sku, Integer quantity, String warehouseId) {
     *     Instant now = Instant.now();
     *     
     *     return Inventory.builder()
     *         .reservationId(UUID.randomUUID().toString())
     *         .orderId(orderId)
     *         .correlationId(correlationId)
     *         .sku(sku)
     *         .quantity(quantity)
     *         .warehouseId(warehouseId)
     *         .status(ReservationStatus.PENDING)
     *         .reservedAt(now)
     *         .expiresAt(now.plusSeconds(900)) // 15 minutes
     *         .build();
     * }
     * ```
     */
    public static Inventory create(String orderId, String correlationId,
                                   String sku, Integer quantity, String warehouseId) {
        // TODO: Implement inventory reservation creation (see challenge above)
        // Write your code here
        
        
        
        // Placeholder - replace with your implementation
        return null;
    }
    
    /**
     * Confirm inventory reservation
     * 
     * CHALLENGE 1.10: Implement reservation confirmation
     * TODO: Return new Inventory with status RESERVED
     * 
     * 📝 SOLUTION: Similar to previous confirm() methods
     */
    public Inventory reserve() {
        // TODO: Implement reservation confirmation
        // Write your code here
        
        
        
        return null;
    }
    
    /**
     * Release inventory reservation
     * 
     * CHALLENGE 1.11: Implement reservation release
     * TODO: Return new Inventory with status RELEASED
     * TODO: Update reservedAt timestamp
     * 
     * 📝 SOLUTION: Similar to previous methods but with RELEASED status
     */
    public Inventory release() {
        // TODO: Implement reservation release
        // Write your code here
        
        
        
        return null;
    }
    
    /**
     * Mark reservation as failed
     * 
     * CHALLENGE 1.12: Implement reservation failure
     * TODO: Return new Inventory with status FAILED
     * TODO: Set failure reason
     * 
     * 📝 SOLUTION: Similar to Payment.fail() method
     */
    public Inventory fail(String reason) {
        // TODO: Implement reservation failure
        // Write your code here
        
        
        
        return null;
    }
}
