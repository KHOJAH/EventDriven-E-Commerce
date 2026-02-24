package com.learning.kafka.consumer;

import com.learning.kafka.model.Inventory;
import com.learning.kafka.model.Order;
import com.learning.kafka.producer.InventoryProducer;
import com.learning.kafka.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Inventory Consumer - Module 3: Advanced Consumer Patterns
 * 
 * This consumer processes inventory reservation events.
 * 
 * Learning Objectives:
 * - Manual partition assignment
 * - Concurrent consumers
 * - Idempotency handling
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryConsumer {
    
    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;
    
    /**
     * Listen to inventory reservation events
     * 
     * CHALLENGE 3.9: Implement inventory consumer with concurrency
     * TODO: Add @KafkaListener for "inventory-reservation" topic
     * TODO: Use containerFactory = "kafkaListenerContainerFactory" (configured for concurrency)
     * TODO: Use groupId = "inventory-reservation-group"
     * TODO: Call inventoryService.reserveInventory()
     * TODO: Send result to inventory-reserved or inventory-released topic
     * 
     * 💡 Hint: The containerFactory is configured with setConcurrency(3)
     * 
     * 📝 SOLUTION:
     * ```java
     * @KafkaListener(topics = "inventory-reservation", groupId = "inventory-reservation-group",
     *               containerFactory = "kafkaListenerContainerFactory")
     * public void listenInventoryReservation(Order order, Acknowledgment ack) {
     *     log.info("Received inventory reservation request: {}", order.getOrderId());
     *     
     *     try {
     *         Inventory result = inventoryService.reserveInventory(order);
     *         
     *         if (result.getStatus() == Inventory.ReservationStatus.RESERVED) {
     *             inventoryProducer.sendInventoryReserved(result);
     *         } else {
     *             inventoryProducer.sendInventoryReleased(result);
     *         }
     *         
     *         ack.acknowledge();
     *         log.info("Inventory reservation processed: {}", result.getReservationId());
     *     } catch (Exception e) {
     *         log.error("Inventory reservation failed: {}", e.getMessage(), e);
     *         throw e;
     *     }
     * }
     * ```
     */
    // TODO: Implement inventory consumer (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * Listen to inventory release events
     * 
     * CHALLENGE 3.10: Implement inventory release consumer
     * TODO: Add @KafkaListener for "inventory-release" topic
     * TODO: Use groupId = "inventory-release-group"
     * TODO: Call inventoryService.releaseInventory()
     * TODO: Send result to inventory-released topic
     * 
     * 📝 SOLUTION: Similar to listenInventoryReservation but for release topic
     */
    // TODO: Implement inventory release consumer (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * CHALLENGE 3.11: Add manual partition assignment
     * TODO: Research how to manually assign partitions using SpEL
     * TODO: Create a method that dynamically discovers partitions
     * TODO: Use @TopicPartition annotation
     * 
     * 💡 Hint:
     * @KafkaListener(topicPartitions = @TopicPartition(
     *     topic = "inventory-reservation",
     *     partitions = "#{@partitionFinder.partitions('inventory-reservation')}"
     * ))
     * 
     * 📝 SOLUTION: See PartitionFinder example in KAFKA_CONCEPTS.md
     */
    // TODO: Add manual partition assignment (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 5.5: Implement idempotency check in consumer
     * TODO: Before processing, check if message was already processed
     * TODO: Use a Set or Redis to store processed message IDs
     * TODO: Skip processing if duplicate is detected
     * 
     * 💡 Hint: Use order.getIdempotencyKey() for deduplication
     * 
     * 📝 SOLUTION:
     * ```java
     * private final Set<String> processedKeys = ConcurrentHashMap.newKeySet();
     * 
     * public void listen(Order order, Acknowledgment ack) {
     *     if (processedKeys.contains(order.getIdempotencyKey())) {
     *         log.warn("Duplicate message detected - skipping: {}", order.getIdempotencyKey());
     *         ack.acknowledge(); // Acknowledge but don't process
     *         return;
     *     }
     *     
     *     // Process the message
     *     // ...
     *     
     *     processedKeys.add(order.getIdempotencyKey());
     *     ack.acknowledge();
     * }
     * ```
     */
    // TODO: Implement idempotency check (see challenge above)
    
    
    
    
}
