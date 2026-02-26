package com.learning.kafka.service;

import com.learning.kafka.exception.NonRetryableException;
import com.learning.kafka.model.Inventory;
import com.learning.kafka.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class InventoryService {

    private final Random random = new Random();
    private final Set<String> processedReservations = ConcurrentHashMap.newKeySet();

    public Inventory reserveInventory(Order order) {
        log.info("Reserving inventory for order: {}", order.getOrderId());

        if (processedReservations.contains(order.getIdempotencyKey())) {
            log.warn("Duplicate reservation request - ignoring: {}", order.getIdempotencyKey());
            throw new NonRetryableException("Duplicate reservation: " + order.getIdempotencyKey());
        }

        Inventory inventory = Inventory.create(
                order.getOrderId(),
                order.getCorrelationId(),
                order.getItems(),
                1,
                "WAREHOUSE-001"
        );

        if (random.nextInt(100) < 90) {
            processedReservations.add(order.getIdempotencyKey());
            return inventory.reserve();
        } else {
            log.error("Out of stock for order: {}", order.getOrderId());
            return inventory.fail("Insufficient stock");
        }
    }

    public Inventory releaseInventory(Order order) {
        log.info("Releasing inventory for order: {}", order.getOrderId());
        processedReservations.remove(order.getIdempotencyKey());
        Inventory inventory = Inventory.create(order.getOrderId(),
                order.getCorrelationId(),
                order.getItems(),
                1,
                "WAREHOUSE-001");

        return inventory.release();
    }
}
