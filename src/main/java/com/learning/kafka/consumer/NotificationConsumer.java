package com.learning.kafka.consumer;

import com.learning.kafka.model.Notification;
import com.learning.kafka.model.Order;
import com.learning.kafka.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = {"notification-email", "notification-sms"},
            groupId = "notification-group")
    public void listenNotification(Notification notification, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Received notification request: {}", notification.getNotificationId());
        log.info("Topic: {}", topic);

        if ("notification-email".equals(topic))
            listenEmailNotification(notification);
        else if ("notification-sms".equals(topic))
            listenSMSNotification(notification);
    }

    public void listenEmailNotification(Notification notification) {
        log.info("Received email notification request: {}", notification.getNotificationId());

        try {
            Notification result = notificationService.sendOrderConfirmation(
                    Order.builder()
                            .orderId(notification.getOrderId())
                            .customerEmail(notification.getRecipient())
                            .build()
            );

            log.info("Email notification sent: {}", result.getNotificationId());
        } catch (Exception e) {
            log.error("Failed to send email notification: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void listenSMSNotification(Notification notification) {
        log.info("Received sms notification request: {}", notification.getNotificationId());

        try {
            Notification result = notificationService.sendOrderConfirmationSms(
                    Order.builder()
                            .orderId(notification.getOrderId())
                            .build(), notification.getRecipient()
            );

            log.info("sms notification sent: {}", result.getNotificationId());
        } catch (Exception e) {
            log.error("Failed to send sms notification: {}", e.getMessage(), e);
            throw e;
        }
    }


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
