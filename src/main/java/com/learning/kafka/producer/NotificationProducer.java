package com.learning.kafka.producer;

import com.learning.kafka.model.Notification;
import com.learning.kafka.service.NotificationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer implements NotificationEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String NOTIFICATION_EMAIL_TOPIC = "notification-email";
    private static final String NOTIFICATION_SMS_TOPIC = "notification-sms";

    @Override
    public void publishEmailNotification(Notification notification) {
        log.info("Publishing email notification: {}", notification.getNotificationId());
        sendMessage(NOTIFICATION_EMAIL_TOPIC, notification.getOrderId(), notification, notification.getNotificationId());
    }

    @Override
    public void publishSmsNotification(Notification notification) {
        log.info("Publishing SMS notification: {}", notification.getNotificationId());
        sendMessage(NOTIFICATION_SMS_TOPIC, notification.getOrderId(), notification, notification.getNotificationId());
    }

    private void sendMessage(String topic, String key, Object payload, String notificationId) {
        kafkaTemplate.send(topic, key, payload)
                .whenComplete(handleSendCompletion(notificationId, topic));
    }

    private BiConsumer<SendResult<String, Object>, Throwable> handleSendCompletion(String notificationId, String topic) {
        return (result, ex) -> {
            if (ex == null) {
                log.info("Notification sent successfully - Topic: {}, Partition: {}, Offset: {}, NotificationId: {}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        notificationId);
            } else {
                log.error("Failed to send notification: {}", ex.getMessage(), ex);
            }
        };
    }
}
