package com.learning.kafka.consumer;

import com.learning.kafka.model.Order;
import com.learning.kafka.model.Payment;
import com.learning.kafka.producer.PaymentProducer;
import com.learning.kafka.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;
    private final PaymentProducer paymentProducer;

    @KafkaListener(topics = "payment-process", groupId = "payment-processor-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenPaymentProcess(Payment payment, Acknowledgment ack) {
        log.info("Received payment processing request: {}", payment.getPaymentId());

        try {
            Payment result = paymentService.processPayment(
                    Order.builder()
                            .orderId(payment.getOrderId())
                            .correlationId(payment.getCorrelationId())
                            .totalAmount(payment.getAmount())
                            .build()
            );

            paymentProducer.sendPaymentProcessed(result);
            ack.acknowledge(); // Manual offset commit

            log.info("Payment processed successfully: {}", result.getPaymentId());
        } catch (Exception e) {
            log.error("Payment processing failed: {}", e.getMessage(), e);
            // Don't acknowledge - will be retried or sent to DLT
            throw e; // Let error handler deal with it
        }
    }

//    @RetryableTopic(
//            attempts = "3",
//            backoff = @Backoff(delay = 1000, multiplier = 2),
//            include = {RetryableException.class},
//            exclude = {NonRetryableException.class},
//            autoCreateTopics = "true"
//    )
//    @KafkaListener(topics = "payment-process", groupId = "payment-processor-group",
//            containerFactory = "kafkaListenerContainerFactory")
//    public void listenPaymentWithRetry(Payment payment, Acknowledgment ack) {
//        log.info("Received payment (with retry): {}", payment.getPaymentId());
//
//        Payment result = paymentService.processPayment(
//                Order.builder()
//                        .orderId(payment.getOrderId())
//                        .correlationId(payment.getCorrelationId())
//                        .totalAmount(payment.getAmount())
//                        .build()
//        );
//
//        paymentProducer.sendPaymentProcessed(result);
//        ack.acknowledge();
//    }
//
//    @DltHandler
//    public void handleDlt(Payment payment, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        log.error("Payment message sent to DLT after all retries: {}", payment.getPaymentId());
//        // In production: alert operations team, store for manual review
//    }

    /**
     * CHALLENGE 4.10: Add correlation ID tracking in DLT handler
     * TODO: Modify @DltHandler to include correlationId in the log
     * TODO: This helps with distributed tracing and debugging
     *
     * 💡 Hint: Access correlationId from the payment object
     *
     * 📝 SOLUTION: Add correlationId to the DLT handler log message
     */
    // TODO: Add correlation ID tracking (see challenge above)


}
