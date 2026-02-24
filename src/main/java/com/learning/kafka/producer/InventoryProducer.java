package com.learning.kafka.producer;

import com.learning.kafka.model.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Inventory Producer - Module 2: Advanced Producer Patterns
 * 
 * This producer sends inventory events to Kafka topics.
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String INVENTORY_RESERVED_TOPIC = "inventory-reserved";
    private static final String INVENTORY_RELEASED_TOPIC = "inventory-released";
    
    /**
     * Send inventory reserved event
     * 
     * CHALLENGE 2.15: Implement inventory reservation sending
     * TODO: Use async sending with CompletableFuture
     * TODO: Use orderId as the partition key
     * TODO: Add callback for logging
     * 
     * 📝 SOLUTION:
     * ```java
     * public void sendInventoryReserved(Inventory inventory) {
     *     log.info("Sending inventory reserved event: {}", inventory.getReservationId());
     *     
     *     CompletableFuture<SendResult<String, Object>> future = 
     *         kafkaTemplate.send(INVENTORY_RESERVED_TOPIC, inventory.getOrderId(), inventory);
     *     
     *     future.whenComplete((result, ex) -> {
     *         if (ex == null) {
     *             log.info("Inventory reserved event sent - Partition: {}, Offset: {}",
     *                 result.getRecordMetadata().partition(),
     *                 result.getRecordMetadata().offset());
     *         } else {
     *             log.error("Failed to send inventory reserved event: {}", ex.getMessage(), ex);
     *         }
     *     });
     * }
     * ```
     */
    public void sendInventoryReserved(Inventory inventory) {
        // TODO: Implement inventory reservation sending (see challenge above)
        // Write your code here
        
        
        
        
        log.warn("sendInventoryReserved not implemented - {}", inventory.getReservationId());
    }
    
    /**
     * Send inventory released event
     * 
     * CHALLENGE 2.16: Implement inventory release sending
     * TODO: Send released inventory to INVENTORY_RELEASED_TOPIC
     * TODO: Use async sending with callback
     * 
     * 📝 SOLUTION: Similar to sendInventoryReserved but with INVENTORY_RELEASED_TOPIC
     */
    public void sendInventoryReleased(Inventory inventory) {
        // TODO: Implement inventory release sending (see challenge above)
        // Write your code here
        
        
        
        
        log.warn("sendInventoryReleased not implemented - {}", inventory.getReservationId());
    }
}
