package com.learning.kafka.dto;

import com.learning.kafka.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Order Response DTO - Used for returning order details via REST API
 * 
 * This is a simplified response that doesn't expose internal fields
 * like correlationId or idempotencyKey to the client.
 * 
 * @author Kafka Mastery Project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private String orderId;
    private String customerId;
    private String customerEmail;
    private BigDecimal totalAmount;
    private String status;
    private String items;
    private String shippingAddress;
    private String createdAt;
    
    /**
     * Factory method to convert Order model to OrderResponse DTO
     * 
     * CHALLENGE 1.22: Implement the conversion
     * TODO: Map all relevant fields from Order to OrderResponse
     * TODO: Convert OrderStatus enum to String
     * TODO: Format Instant to String for createdAt
     * 
     * 💡 Hint: Use order.getStatus().name() and order.getCreatedAt().toString()
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public static OrderResponse fromOrder(Order order) {
     *     return OrderResponse.builder()
     *         .orderId(order.getOrderId())
     *         .customerId(order.getCustomerId())
     *         .customerEmail(order.getCustomerEmail())
     *         .totalAmount(order.getTotalAmount())
     *         .status(order.getStatus().name())
     *         .items(order.getItems())
     *         .shippingAddress(order.getShippingAddress())
     *         .createdAt(order.getCreatedAt().toString())
     *         .build();
     * }
     * ```
     */
    public static OrderResponse fromOrder(Order order) {
        // TODO: Implement the conversion (see challenge above)
        // Write your code here
        
        
        
        // Placeholder - replace with your implementation
        return null;
    }
}
