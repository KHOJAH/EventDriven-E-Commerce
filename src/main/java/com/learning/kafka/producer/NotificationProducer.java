package com.learning.kafka.producer;

import com.learning.kafka.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Notification Producer - Module 2: Advanced Producer Patterns
 * 
 * This producer sends notification events to Kafka topics.
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String NOTIFICATION_EMAIL_TOPIC = "notification-email";
    private static final String NOTIFICATION_SMS_TOPIC = "notification-sms";
    
    /**
     * Send email notification event
     * 
     * CHALLENGE 2.17: Implement email notification sending
     * TODO: Use async sending with CompletableFuture
     * TODO: Use orderId as the partition key
     * TODO: Add callback for logging
     * 
     * 📝 SOLUTION:
     * ```java
     * public void sendEmailNotification(Notification notification) {
     *     log.info("Sending email notification: {}", notification.getNotificationId());
     *     
     *     CompletableFuture<SendResult<String, Object>> future = 
     *         kafkaTemplate.send(NOTIFICATION_EMAIL_TOPIC, notification.getOrderId(), notification);
     *     
     *     future.whenComplete((result, ex) -> {
     *         if (ex == null) {
     *             log.info("Email notification sent - Partition: {}, Offset: {}",
     *                 result.getRecordMetadata().partition(),
     *                 result.getRecordMetadata().offset());
     *         } else {
     *             log.error("Failed to send email notification: {}", ex.getMessage(), ex);
     *         }
     *     });
     * }
     * ```
     */
    public void sendEmailNotification(Notification notification) {
        // TODO: Implement email notification sending (see challenge above)
        // Write your code here
        
        
        
        
        log.warn("sendEmailNotification not implemented - {}", notification.getNotificationId());
    }
    
    /**
     * Send SMS notification event
     * 
     * CHALLENGE 2.18: Implement SMS notification sending
     * TODO: Send SMS notification to NOTIFICATION_SMS_TOPIC
     * TODO: Use async sending with callback
     * 
     * 📝 SOLUTION: Similar to sendEmailNotification but with NOTIFICATION_SMS_TOPIC
     */
    public void sendSmsNotification(Notification notification) {
        // TODO: Implement SMS notification sending (see challenge above)
        // Write your code here
        
        
        
        
        log.warn("sendSmsNotification not implemented - {}", notification.getNotificationId());
    }
}
