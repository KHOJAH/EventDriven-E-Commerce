package com.learning.kafka.producer;

import com.learning.kafka.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Order Producer - Module 1: Kafka Fundamentals
 * 
 * This producer sends order events to Kafka topics.
 * 
 * Learning Objectives:
 * - Understand KafkaTemplate usage
 * - Learn synchronous vs asynchronous message sending
 * - Understand callbacks and error handling
 * - Learn about Kafka headers for tracing
 * 
 * CHALLENGE 1.25: Study the KafkaTemplate API
 * TODO: Read the Spring Kafka documentation on KafkaTemplate
 * 💡 Hint: https://docs.spring.io/spring-kafka/reference/kafka.html
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    /**
     * Topic name for order-created events
     */
    private static final String ORDER_CREATED_TOPIC = "order-created";
    
    /**
     * Topic name for order-confirmed events
     */
    private static final String ORDER_CONFIRMED_TOPIC = "order-confirmed";
    
    /**
     * Topic name for order-cancelled events
     */
    private static final String ORDER_CANCELLED_TOPIC = "order-cancelled";
    
    /**
     * Send order created event
     * 
     * CHALLENGE 1.26: Implement basic message sending
     * TODO: Use kafkaTemplate.send() to send the order to ORDER_CREATED_TOPIC
     * TODO: Use orderId as the key (ensures ordering)
     * TODO: Use the order object as the value
     * 
     * 💡 Hint: kafkaTemplate.send(topic, key, value)
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public void sendOrderCreated(Order order) {
     *     log.info("Sending order created event: {}", order.getOrderId());
     *     kafkaTemplate.send(ORDER_CREATED_TOPIC, order.getOrderId(), order);
     * }
     * ```
     */
    public void sendOrderCreated(Order order) {
        // TODO: Implement basic message sending (see challenge above)
        // Write your code here
        
        
        
        // Placeholder - replace with your implementation
        log.warn("sendOrderCreated not implemented - {}", order.getOrderId());
    }
    
    /**
     * Send order created event with callback
     * 
     * CHALLENGE 1.27: Implement async sending with callback
     * TODO: Use kafkaTemplate.send() which returns CompletableFuture<SendResult>
     * TODO: Add whenComplete callback to log success or failure
     * TODO: Log the offset and partition on success
     * TODO: Log the error on failure
     * 
     * 💡 Hint: 
     * CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(...);
     * future.whenComplete((result, ex) -> { ... });
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public void sendOrderCreatedAsync(Order order) {
     *     log.info("Sending order created event (async): {}", order.getOrderId());
     *     
     *     CompletableFuture<SendResult<String, Object>> future = 
     *         kafkaTemplate.send(ORDER_CREATED_TOPIC, order.getOrderId(), order);
     *     
     *     future.whenComplete((result, ex) -> {
     *         if (ex == null) {
     *             log.info("Order created event sent successfully - Topic: {}, Partition: {}, Offset: {}",
     *                 result.getRecordMetadata().topic(),
     *                 result.getRecordMetadata().partition(),
     *                 result.getRecordMetadata().offset());
     *         } else {
     *             log.error("Failed to send order created event: {}", ex.getMessage(), ex);
     *         }
     *     });
     * }
     * ```
     */
    public void sendOrderCreatedAsync(Order order) {
        // TODO: Implement async sending with callback (see challenge above)
        // Write your code here
        
        
        
        
        // Placeholder - replace with your implementation
        log.warn("sendOrderCreatedAsync not implemented - {}", order.getOrderId());
    }
    
    /**
     * Send order confirmed event
     * 
     * CHALLENGE 1.28: Implement order confirmation sending
     * TODO: Send the confirmed order to ORDER_CONFIRMED_TOPIC
     * TODO: Use async sending with callback (like challenge 1.27)
     * 
     * 📝 SOLUTION: Similar to sendOrderCreatedAsync but with ORDER_CONFIRMED_TOPIC
     */
    public void sendOrderConfirmed(Order order) {
        // TODO: Implement order confirmation sending
        // Write your code here
        
        
        
        
        log.warn("sendOrderConfirmed not implemented - {}", order.getOrderId());
    }
    
    /**
     * Send order cancelled event
     * 
     * CHALLENGE 1.29: Implement order cancellation sending
     * TODO: Send the cancelled order to ORDER_CANCELLED_TOPIC
     * TODO: Use async sending with callback
     * 
     * 📝 SOLUTION: Similar to previous methods but with ORDER_CANCELLED_TOPIC
     */
    public void sendOrderCancelled(Order order) {
        // TODO: Implement order cancellation sending
        // Write your code here
        
        
        
        
        log.warn("sendOrderCancelled not implemented - {}", order.getOrderId());
    }
    
    /**
     * CHALLENGE 1.30: Add correlation ID to Kafka headers
     * TODO: Modify sendOrderCreatedAsync to add correlationId as a Kafka header
     * TODO: Use RecordHeader to add the header
     * TODO: Use correlationId as the header value (for distributed tracing)
     * 
     * 💡 Hint: 
     * import org.apache.kafka.common.header.Header;
     * import org.apache.kafka.common.header.Headers;
     * import org.apache.kafka.common.header.internals.RecordHeader;
     * 
     * List<Header> headers = new ArrayList<>();
     * headers.add(new RecordHeader("correlationId", order.getCorrelationId().getBytes()));
     * 
     * ProducerRecord<String, Object> record = new ProducerRecord<>(topic, key, value);
     * headers.forEach(record::addHeader);
     * kafkaTemplate.send(record);
     * 
     * 📝 SOLUTION: See the implementation hint above
     */
    // TODO: Implement correlation ID headers (see challenge above)
    // This is an advanced challenge - modify sendOrderCreatedAsync
    
    
    
    
    
    /**
     * CHALLENGE 1.31: Understand partition key importance
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
