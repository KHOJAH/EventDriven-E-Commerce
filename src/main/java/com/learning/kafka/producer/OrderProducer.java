package com.learning.kafka.producer;

import com.learning.kafka.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Order Producer - Module 1: Kafka Fundamentals
 * <p>
 * This producer sends order events to Kafka topics.
 * <p>
 * Learning Objectives:
 * - Understand KafkaTemplate usage
 * - Learn synchronous vs asynchronous message sending
 * - Understand callbacks and error handling
 * - Learn about Kafka headers for tracing
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String ORDER_CREATED_TOPIC = "order-created";
    private static final String ORDER_CONFIRMED_TOPIC = "order-confirmed";
    private static final String ORDER_CANCELLED_TOPIC = "order-cancelled";

    /**
     * TODO: Use kafkaTemplate.send() to send the order to ORDER_CREATED_TOPIC
     * TODO: Use orderId as the key (ensures ordering)
     * TODO: Use the order object as the value
     */
    public void sendOrderCreated(Order order) {
        log.info("Sending order created event: {}", order.getOrderId());
        kafkaTemplate.send(ORDER_CREATED_TOPIC, order.getOrderId(), order);
    }

    /**
     * TODO: Use kafkaTemplate.send() which returns CompletableFuture<SendResult>
     * TODO: Add whenComplete callback to log success or failure
     * TODO: Log the offset and partition on success
     * TODO: Log the error on failure
     */
    public void sendOrderCreatedAsync(Order order) {
        processOrder("Sending order created event (async): {}", order, ORDER_CREATED_TOPIC, "Order created event sent successfully - Topic: {}, Partition: {}, Offset: {}");
    }

    /**
     * TODO: Send the confirmed order to ORDER_CONFIRMED_TOPIC
     * TODO: Use async sending with callback (like challenge 1.27)
     */
    public void sendOrderConfirmed(Order order) {
        processOrder("Sending order confirmed event: {}", order, ORDER_CONFIRMED_TOPIC, "Order confirmed event sent successfully - Topic: {}, Partition: {}, Offset: {}");
    }

    /**
     * TODO: Send the cancelled order to ORDER_CANCELLED_TOPIC
     * TODO: Use async sending with callback
     */
    public void sendOrderCancelled(Order order) {
        processOrder("Sending order cancelled event: {}", order, ORDER_CANCELLED_TOPIC, "Order cancelled event sent successfully - Topic: {}, Partition: {}, Offset: {}");
    }

    /*
    *      * CHALLENGE 1.30: Add correlation ID to Kafka headers
     * TODO: Modify sendOrderCreatedAsync to add correlationId as a Kafka header
     * TODO: Use RecordHeader to add the header
     * TODO: Use correlationId as the header value (for distributed tracing)
     // TODO: Implement correlation ID headers (see challenge above)
     // This is an advanced challenge - modify sendOrderCreatedAsync
     * */
    private void processOrder(String s, Order order, String orderCancelledTopic, String s1) {
        MessageHeaders headers = new MessageHeaders(Map.of("correlationId", order.getCorrelationId()));
        GenericMessage<Order> orderMessage = new GenericMessage<>(order, headers);
        log.info(s, order.getOrderId());
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(orderCancelledTopic, order.getOrderId(), orderMessage);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info(s1,
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send order created event: {}", ex.getMessage(), ex);
            }
        });
    }
    /**
     * TODO: Research why we use orderId as the partition key
     * 💡 Hint: Think about message ordering and related events
     *
     * 📝 Solution: 
     * Using orderId as key ensures:
     * 1. All events for the same order go to the same partition
     * 2. Events are processed in order (FIFO) for that order
     * 3. No race conditions between order created/confirmed/cancelled
     */
}
