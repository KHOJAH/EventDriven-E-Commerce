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

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryConsumer {

    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;
    private final Set<String> processedKeys = ConcurrentHashMap.newKeySet();


    @KafkaListener(topics = "inventory-reservation", groupId = "inventory-reservation-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenInventoryReservation(Order order, Acknowledgment ack) {
        log.info("Received inventory reservation request: {}", order.getOrderId());

        if (processedKeys.contains(order.getIdempotencyKey())) {
            log.warn("Duplicate message detected - skipping: {}", order.getIdempotencyKey());
            ack.acknowledge(); // Acknowledge but don't process
            return;
        }

        try {
            Inventory result = inventoryService.reserveInventory(order);

            if (result.getStatus() == Inventory.ReservationStatus.RESERVED) {
                inventoryProducer.sendInventoryReserved(result);
            } else {
                inventoryProducer.sendInventoryReleased(result);
            }

            processedKeys.add(order.getIdempotencyKey());
            ack.acknowledge();
            log.info("Inventory reservation processed: {}", result.getReservationId());
        } catch (Exception e) {
            log.error("Inventory reservation failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "inventory-release", groupId = "inventory-release-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenInventoryRelease(Order order, Acknowledgment ack) {
        log.info("Received inventory release request: {}", order.getOrderId());

        if (processedKeys.contains(order.getIdempotencyKey())) {
            log.warn("Duplicate message detected - skipping: {}", order.getIdempotencyKey());
            ack.acknowledge(); // Acknowledge but don't process
            return;
        }

        try {
            Inventory result = inventoryService.releaseInventory(order);

            if (result.getStatus() == Inventory.ReservationStatus.RELEASED)
                inventoryProducer.sendInventoryReleased(result);
            else
                inventoryProducer.sendInventoryReserved(result);

            processedKeys.add(order.getIdempotencyKey());
            ack.acknowledge();
            log.info("Inventory release processed: {}", result.getReservationId());
        } catch (Exception e) {
            log.error("Inventory release failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}

