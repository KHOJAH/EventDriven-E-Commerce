package com.learning.kafka.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Order Request DTO - Used for creating orders via REST API
 * 
 * This is the request body that clients send to create an order.
 * It gets validated and then converted to an Order model for Kafka publishing.
 * 
 * @author Kafka Mastery Project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    
    /**
     * Customer ID
     * CHALLENGE 1.17: Add validation annotations
     * TODO: Add @NotBlank to ensure customerId is not null or empty
     * 
     * 💡 Hint: @NotBlank(message = "Customer ID is required")
     * 
     * 📝 SOLUTION:
     * @NotBlank(message = "Customer ID is required")
     * private String customerId;
     */
    private String customerId;
    
    /**
     * Customer email
     * CHALLENGE 1.18: Add validation annotations
     * TODO: Add @NotBlank and @Email validation
     * 
     * 💡 Hint: @Email(message = "Invalid email format")
     * 
     * 📝 SOLUTION:
     * @NotBlank(message = "Customer email is required")
     * @Email(message = "Invalid email format")
     * private String customerEmail;
     */
    private String customerEmail;
    
    /**
     * Total order amount
     * CHALLENGE 1.19: Add validation annotations
     * TODO: Add @NotNull and @Positive validation
     * 
     * 💡 Hint: @Positive(message = "Amount must be positive")
     * 
     * 📝 SOLUTION:
     * @NotNull(message = "Total amount is required")
     * @Positive(message = "Total amount must be positive")
     * private BigDecimal totalAmount;
     */
    private BigDecimal totalAmount;
    
    /**
     * Order items (comma-separated for simplicity)
     * CHALLENGE 1.20: Add validation annotations
     * TODO: Add @NotBlank validation
     * 
     * 📝 SOLUTION:
     * @NotBlank(message = "Items are required")
     * private String items;
     */
    private String items;
    
    /**
     * Shipping address
     * CHALLENGE 1.21: Add validation annotations
     * TODO: Add @NotBlank validation
     * 
     * 📝 SOLUTION:
     * @NotBlank(message = "Shipping address is required")
     * private String shippingAddress;
     */
    private String shippingAddress;
}
