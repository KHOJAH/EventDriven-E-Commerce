package com.learning.kafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Notification Model - Represents notification events to be sent to customers
 * 
 * Used in topics:
 * - notification-email: Email notifications
 * - notification-sms: SMS notifications
 * 
 * @author Kafka Mastery Project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    /**
     * Unique notification identifier
     */
    private String notificationId;
    
    /**
     * Reference to the order
     */
    private String orderId;
    
    /**
     * Correlation ID from the original order
     */
    private String correlationId;
    
    /**
     * Notification type
     */
    private NotificationType type;
    
    /**
     * Recipient (email or phone number)
     */
    private String recipient;
    
    /**
     * Notification subject (for emails)
     */
    private String subject;
    
    /**
     * Notification message body
     */
    private String message;
    
    /**
     * Notification status
     */
    private NotificationStatus status;
    
    /**
     * Error message if notification failed
     */
    private String errorMessage;
    
    /**
     * Number of retry attempts
     * CHALLENGE 4.1: Use this field to track retry attempts in consumer
     */
    private Integer retryCount;
    
    /**
     * Timestamp when notification was created
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant createdAt;
    
    /**
     * Timestamp when notification was sent
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant sentAt;
    
    /**
     * Enum for Notification Types
     */
    public enum NotificationType {
        EMAIL,
        SMS,
        PUSH,
        WHATSAPP
    }
    
    /**
     * Enum for Notification Status
     */
    public enum NotificationStatus {
        PENDING,      // Notification created, awaiting send
        SENT,         // Successfully sent
        FAILED,       // Failed to send
        RETRYING      // Being retried
    }
    
    /**
     * Factory method to create an order confirmation email
     * 
     * CHALLENGE 1.13: Implement order confirmation email creation
     * TODO: Generate notificationId
     * TODO: Set type to EMAIL
     * TODO: Set subject to "Order Confirmation - {orderId}"
     * TODO: Set message with order details
     * TODO: Set status to PENDING
     * 
     * 💡 Hint: Use String.format or text blocks for message template
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public static Notification createOrderConfirmation(Order order) {
     *     String subject = "Order Confirmation - " + order.getOrderId();
     *     String message = String.format("""
     *         Dear Valued Customer,
     *         
     *         Your order has been confirmed!
     *         
     *         Order ID: %s
     *         Total Amount: $%.2f
     *         Items: %s
     *         Shipping Address: %s
     *         
     *         Thank you for your purchase!
     *         
     *         Best regards,
     *         E-Commerce Team
     *         """, 
     *         order.getOrderId(),
     *         order.getTotalAmount(),
     *         order.getItems(),
     *         order.getShippingAddress());
     *     
     *     return Notification.builder()
     *         .notificationId(UUID.randomUUID().toString())
     *         .orderId(order.getOrderId())
     *         .correlationId(order.getCorrelationId())
     *         .type(NotificationType.EMAIL)
     *         .recipient(order.getCustomerEmail())
     *         .subject(subject)
     *         .message(message)
     *         .status(NotificationStatus.PENDING)
     *         .createdAt(Instant.now())
     *         .build();
     * }
     * ```
     */
    public static Notification createOrderConfirmation(Order order) {
        // TODO: Implement order confirmation email (see challenge above)
        // Write your code here
        
        
        
        // Placeholder - replace with your implementation
        return null;
    }
    
    /**
     * Factory method to create payment failure notification
     * 
     * CHALLENGE 1.14: Implement payment failure email creation
     * TODO: Set subject to "Payment Failed - Order {orderId}"
     * TODO: Set message explaining payment failure
     * TODO: Include the failure reason in the message
     * 
     * 📝 SOLUTION: Similar to createOrderConfirmation but for payment failure
     */
    public static Notification createPaymentFailure(Order order, String failureReason) {
        // TODO: Implement payment failure email (see challenge above)
        // Write your code here
        
        
        
        return null;
    }
    
    /**
     * Mark notification as sent
     * 
     * CHALLENGE 1.15: Implement notification sent status
     * TODO: Return new Notification with status SENT
     * TODO: Set sentAt to current instant
     * 
     * 📝 SOLUTION: Similar to previous confirm() methods
     */
    public Notification markAsSent() {
        // TODO: Implement mark as sent
        // Write your code here
        
        
        
        return null;
    }
    
    /**
     * Mark notification as failed
     * 
     * CHALLENGE 1.16: Implement notification failure
     * TODO: Return new Notification with status FAILED
     * TODO: Set error message
     * TODO: Increment retry count
     * 
     * 📝 SOLUTION: Similar to Payment.fail() but also increment retryCount
     */
    public Notification markAsFailed(String error) {
        // TODO: Implement mark as failed (see challenge above)
        // Write your code here
        
        
        
        return null;
    }
}
