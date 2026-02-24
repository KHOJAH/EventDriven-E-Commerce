package com.learning.kafka.consumer;

import com.learning.kafka.model.Order;
import com.learning.kafka.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Order Consumer - Module 1: Kafka Fundamentals
 * 
 * This consumer listens to order events and processes them.
 * 
 * Learning Objectives:
 * - Understand @KafkaListener annotation
 * - Learn about consumer groups
 * - Understand message deserialization
 * - Learn basic error handling
 * 
 * CHALLENGE 1.38: Study @KafkaListener annotation
 * TODO: Read the Spring Kafka documentation on @KafkaListener
 * 💡 Hint: https://docs.spring.io/spring-kafka/reference/api/annotations.html
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {
    
    private final OrderService orderService;
    
    /**
     * Listen to order-created events
     * 
     * CHALLENGE 1.39: Implement basic consumer
     * TODO: Add @KafkaListener annotation with topics = "order-created"
     * TODO: Add groupId = "order-created-group"
     * TODO: Log the received order
     * TODO: Call orderService.confirmOrder() to process the order
     * 
     * 💡 Hint: 
     * @KafkaListener(topics = "order-created", groupId = "order-created-group")
     * public void listen(Order order) { ... }
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * @KafkaListener(topics = "order-created", groupId = "order-created-group")
     * public void listenOrderCreated(Order order) {
     *     log.info("Received order created event: {}", order.getOrderId());
     *     log.info("Customer: {}, Amount: {}", order.getCustomerId(), order.getTotalAmount());
     *     
     *     // Process the order (confirm it)
     *     Order confirmedOrder = orderService.confirmOrder(order);
     *     
     *     log.info("Order processed successfully: {}", confirmedOrder.getOrderId());
     * }
     * ```
     */
    // TODO: Implement basic consumer (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * CHALLENGE 1.40: Add container factory configuration
     * TODO: Modify @KafkaListener to use containerFactory
     * TODO: Use containerFactory = "kafkaListenerContainerFactory"
     * 
     * 💡 Hint: This enables manual offset commit and concurrency
     * 
     * 📝 SOLUTION: Add containerFactory attribute to @KafkaListener
     */
    // TODO: Add container factory to the listener above
    
    
    
    
    
    /**
     * Listen to order-confirmed events (for notification)
     * 
     * CHALLENGE 1.41: Implement order confirmed listener
     * TODO: Add @KafkaListener for "order-confirmed" topic
     * TODO: Use groupId = "order-notification-group"
     * TODO: Log the confirmed order
     * TODO: In a real application, this would trigger notification sending
     * 
     * 📝 SOLUTION: Similar to listenOrderCreated but for order-confirmed topic
     */
    // TODO: Implement order confirmed listener (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * Listen to order-cancelled events
     * 
     * CHALLENGE 1.42: Implement order cancelled listener
     * TODO: Add @KafkaListener for "order-cancelled" topic
     * TODO: Use groupId = "order-cancellation-group"
     * TODO: Log the cancelled order
     * TODO: In a real application, this would trigger refund processing
     * 
     * 📝 SOLUTION: Similar to previous listeners but for order-cancelled topic
     */
    // TODO: Implement order cancelled listener (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * CHALLENGE 1.43: Understand consumer groups
     * TODO: Research why we use different group IDs for different listeners
     * 💡 Hint: Think about message delivery semantics
     * 
     * 📝 Solution:
     * Different group IDs ensure:
     * 1. Each service receives ALL messages (pub/sub pattern)
     * 2. Services can scale independently
     * 3. Failure in one service doesn't affect others
     * 
     * Same group ID would mean:
     * - Messages are load-balanced across consumers (competing consumers)
     * - Each message is processed by only ONE consumer in the group
     */
    
    /**
     * CHALLENGE 1.44: Add payload annotation
     * TODO: Research @Payload annotation usage
     * 💡 Hint: It explicitly marks the message payload parameter
     * 
     * 📝 Solution:
     * @KafkaListener(topics = "order-created", groupId = "order-created-group")
     * public void listen(@Payload Order order) { ... }
     * 
     * @Payload is optional but makes the code more explicit
     */
}
