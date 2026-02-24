package com.learning.kafka.service;

import com.learning.kafka.model.Notification;
import com.learning.kafka.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Notification Service - Module 1: Kafka Fundamentals
 * 
 * Business logic for sending notifications.
 * This service simulates email/SMS sending.
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Service
public class NotificationService {
    
    /**
     * Send order confirmation email
     * 
     * CHALLENGE 1.49: Implement notification sending
     * TODO: Create a Notification using Notification.createOrderConfirmation()
     * TODO: Simulate sending the email (add a small delay)
     * TODO: Log the notification details
     * TODO: Return the notification with SENT status
     * 
     * 💡 Hint: Use Thread.sleep(100) to simulate network delay
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public Notification sendOrderConfirmation(Order order) {
     *     log.info("Sending order confirmation email to: {}", order.getCustomerEmail());
     *     
     *     Notification notification = Notification.createOrderConfirmation(order);
     *     
     *     try {
     *         // Simulate email sending delay
     *         Thread.sleep(100);
     *         log.info("Order confirmation email sent: {}", notification.getNotificationId());
     *         return notification.markAsSent();
     *     } catch (InterruptedException e) {
     *         log.error("Failed to send email: {}", e.getMessage());
     *         Thread.currentThread().interrupt();
     *         return notification.markAsFailed("Email service unavailable");
     *     }
     * }
     * ```
     */
    public Notification sendOrderConfirmation(Order order) {
        // TODO: Implement notification sending (see challenge above)
        // Write your code here
        
        
        
        
        // Placeholder - replace with your implementation
        log.warn("sendOrderConfirmation not implemented");
        return null;
    }
    
    /**
     * Send payment failure notification
     * 
     * CHALLENGE 1.50: Implement payment failure notification
     * TODO: Create a Notification using Notification.createPaymentFailure()
     * TODO: Simulate sending the email
     * TODO: Return the notification with appropriate status
     * 
     * 📝 SOLUTION: Similar to sendOrderConfirmation but uses createPaymentFailure()
     */
    public Notification sendPaymentFailure(Order order, String failureReason) {
        // TODO: Implement payment failure notification (see challenge above)
        // Write your code here
        
        
        
        
        log.warn("sendPaymentFailure not implemented");
        return null;
    }
    
    /**
     * CHALLENGE 1.51: Add SMS notification support
     * TODO: Create a method to send SMS notifications
     * TODO: Use NotificationType.SMS instead of EMAIL
     * TODO: Simulate SMS gateway integration
     * 
     * 💡 Hint: SMS notifications are typically shorter than emails
     * 
     * 📝 SOLUTION:
     * ```java
     * public Notification sendOrderConfirmationSms(Order order, String phoneNumber) {
     *     String message = "Your order " + order.getOrderId() + " has been confirmed!";
     *     
     *     Notification notification = Notification.builder()
     *         .notificationId(UUID.randomUUID().toString())
     *         .orderId(order.getOrderId())
     *         .correlationId(order.getCorrelationId())
     *         .type(Notification.NotificationType.SMS)
     *         .recipient(phoneNumber)
     *         .message(message)
     *         .status(Notification.NotificationStatus.PENDING)
     *         .createdAt(Instant.now())
     *         .build();
     *     
     *     // Simulate SMS sending
     *     log.info("Sending SMS to: {}", phoneNumber);
     *     return notification.markAsSent();
     * }
     * ```
     */
    // TODO: Add SMS notification support (see challenge above)
    
    
    
    
}
