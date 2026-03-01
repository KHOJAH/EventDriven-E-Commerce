package com.learning.kafka.producer;

import com.learning.kafka.model.Inventory;
import com.learning.kafka.service.InventoryEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryProducer implements InventoryEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String INVENTORY_RESERVED_TOPIC = "inventory-reserved";
    private static final String INVENTORY_RELEASED_TOPIC = "inventory-released";

    @Override
    public void publishInventoryReserved(Inventory inventory) {
        log.info("Publishing inventory reserved event: {}", inventory.getReservationId());
        sendMessage(INVENTORY_RESERVED_TOPIC, inventory.getOrderId(), inventory, inventory.getReservationId());
    }

    @Override
    public void publishInventoryReleased(Inventory inventory) {
        log.info("Publishing inventory released event: {}", inventory.getReservationId());
        sendMessage(INVENTORY_RELEASED_TOPIC, inventory.getOrderId(), inventory, inventory.getReservationId());
    }

    private void sendMessage(String topic, String key, Object payload, String referenceId) {
        kafkaTemplate.send(topic, key, payload)
                .whenComplete(handleSendCompletion(referenceId, topic));
    }

    private BiConsumer<SendResult<String, Object>, Throwable> handleSendCompletion(String referenceId, String topic) {
        return (result, ex) -> {
            if (ex == null) {
                log.info("Inventory event sent successfully - Topic: {}, Partition: {}, Offset: {}, ReservationId: {}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        referenceId);
            } else {
                log.error("Failed to send inventory event: {}", ex.getMessage(), ex);
            }
        };
    }

    // ============================================================================
    // Legacy methods for Saga Orchestrator compatibility (DO NOT REMOVE)
    // These methods are used by the saga pattern implementation
    // ============================================================================

    public void sendInventoryReserved(Inventory inventory) {
        log.info("Sending inventory reserved event (legacy): {}", inventory.getReservationId());
        sendMessage(INVENTORY_RESERVED_TOPIC, inventory.getOrderId(), inventory, inventory.getReservationId());
    }

    public void sendInventoryReleased(Inventory inventory) {
        log.info("Sending inventory released event (legacy): {}", inventory.getReservationId());
        sendMessage(INVENTORY_RELEASED_TOPIC, inventory.getOrderId(), inventory, inventory.getReservationId());
    }
}
