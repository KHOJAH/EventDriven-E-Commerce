package com.learning.kafka.consumer;

import com.learning.kafka.model.Notification;
import com.learning.kafka.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Notification Consumer - Module 3 & 4: Advanced Consumer Patterns & Error Handling
 * 
 * This consumer processes notification events (email, SMS).
 * 
 * Learning Objectives:
 * - Multiple topic listening
 * - Custom error handler per consumer
 * - Retry configuration for non-critical messages
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    
    private final NotificationService notificationService;
    
    /**
     * Listen to email notification events
     * 
     * CHALLENGE 3.12: Implement email notification consumer
     * TODO: Add @KafkaListener for "notification-email" topic
     * TODO: Use groupId = "notification-email-group"
     * TODO: Call notificationService.sendOrderConfirmation() or appropriate method
     * TODO: Log the result
     * 
     * 📝 SOLUTION:
     * ```java
     * @KafkaListener(topics = "notification-email", groupId = "notification-email-group")
     * public void listenEmailNotification(Notification notification) {
     *     log.info("Received email notification request: {}", notification.getNotificationId());
     *     
     *     try {
     *         Notification result = notificationService.sendOrderConfirmation(
     *             Order.builder()
     *                 .orderId(notification.getOrderId())
     *                 .customerEmail(notification.getRecipient())
     *                 .build()
     *         );
     *         
     *         log.info("Email notification sent: {}", result.getNotificationId());
     *     } catch (Exception e) {
     *         log.error("Failed to send email notification: {}", e.getMessage(), e);
     *         throw e;
     *     }
     * }
     * ```
     */
    // TODO: Implement email notification consumer (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * Listen to SMS notification events
     * 
     * CHALLENGE 3.13: Implement SMS notification consumer
     * TODO: Add @KafkaListener for "notification-sms" topic
     * TODO: Use groupId = "notification-sms-group"
     * TODO: Implement SMS sending logic
     * 
     * 📝 SOLUTION: Similar to email consumer but for SMS topic
     */
    // TODO: Implement SMS notification consumer (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * CHALLENGE 3.14: Listen to multiple topics with single listener
     * TODO: Modify @KafkaListener to listen to both email and SMS topics
     * TODO: Use topics = {"notification-email", "notification-sms"}
     * TODO: Differentiate between topics using @Header(KafkaHeaders.RECEIVED_TOPIC)
     * 
     * 💡 Hint:
     * @KafkaListener(
     *     topics = {"notification-email", "notification-sms"},
     *     groupId = "notification-group"
     * )
     * public void listen(Notification notification, 
     *                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
     *     // Process based on topic
     * }
     * 
     * 📝 SOLUTION: Use topic to determine notification type
     */
    // TODO: Implement multi-topic listener (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 4.11: Configure custom error handler for notifications
     * TODO: Create a separate error handler bean for notifications
     * TODO: Use longer retry delays (notifications are less time-critical)
     * TODO: Reference it using errorHandler attribute in @KafkaListener
     * 
     * 💡 Hint:
     * @KafkaListener(
     *     topics = "notification-email",
     *     groupId = "notification-email-group",
     *     errorHandler = "notificationErrorHandler"
     * )
     * 
     * 📝 SOLUTION: Create notificationErrorHandler bean in KafkaErrorHandlingConfig
     */
    // TODO: Add custom error handler (see challenge above)
    
    
    
    
}
