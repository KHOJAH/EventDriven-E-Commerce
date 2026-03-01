package com.learning.kafka.producer;

import com.learning.kafka.model.Payment;
import com.learning.kafka.service.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer implements PaymentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String PAYMENT_PROCESSED_TOPIC = "payment-processed";
    private static final String PAYMENT_FAILED_TOPIC = "payment-failed";

    @Override
    public void publishPaymentProcessed(Payment payment) {
        log.info("Publishing payment processed event: {}", payment.getPaymentId());
        sendMessage(PAYMENT_PROCESSED_TOPIC, payment.getOrderId(), payment, payment.getPaymentId());
    }

    @Override
    public void publishPaymentFailed(Payment payment) {
        log.info("Publishing payment failed event: {}", payment.getPaymentId());
        sendMessage(PAYMENT_FAILED_TOPIC, payment.getOrderId(), payment, payment.getPaymentId());
    }

    private void sendMessage(String topic, String key, Object payload, String referenceId) {
        kafkaTemplate.send(topic, key, payload)
                .whenComplete(handleSendCompletion(referenceId, topic));
    }

    private BiConsumer<SendResult<String, Object>, Throwable> handleSendCompletion(String referenceId, String topic) {
        return (result, ex) -> {
            if (ex == null) {
                log.info("Payment event sent successfully - Topic: {}, Partition: {}, Offset: {}, PaymentId: {}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        referenceId);
            } else {
                log.error("Failed to send payment event: {}", ex.getMessage(), ex);
            }
        };
    }

    // ============================================================================
    // Legacy methods for Saga Orchestrator compatibility (DO NOT REMOVE)
    // These methods are used by the saga pattern implementation
    // ============================================================================

    public void sendPaymentProcessed(Payment payment) {
        log.info("Sending payment processed event (legacy): {}", payment.getPaymentId());
        sendMessage(PAYMENT_PROCESSED_TOPIC, payment.getOrderId(), payment, payment.getPaymentId());
    }

    public void sendPaymentFailed(Payment payment) {
        log.info("Sending payment failed event (legacy): {}", payment.getPaymentId());
        sendMessage(PAYMENT_FAILED_TOPIC, payment.getOrderId(), payment, payment.getPaymentId());
    }
}
