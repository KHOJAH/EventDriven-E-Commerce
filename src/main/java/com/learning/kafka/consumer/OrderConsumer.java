package com.learning.kafka.consumer;

import com.learning.kafka.model.Order;
import com.learning.kafka.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "order-created", groupId = "order-created-group", containerFactory = "kafkaListenerContainerFactory")
    public void processOrderCreated(@Payload Order order) {
        log.info("Received order created event: {}", order.getOrderId());
        log.info("Customer: {}, Amount: {}", order.getCustomerId(), order.getTotalAmount());

        Order confirmedOrder = orderService.confirmOrder(order);

        log.info("Order processed successfully: {}", confirmedOrder.getOrderId());
    }

    @KafkaListener(topics = "order-confirmed", groupId = "order-notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void processOrderConfirmed(@Payload Order order) {
        log.info("Received order confirmed event: {}", order.getOrderId());
        log.info("Customer: {}, Amount: {}", order.getCustomerId(), order.getTotalAmount());

        log.info("Processing order confirmation notifications for: {}", order.getOrderId());
    }

    @KafkaListener(topics = "order-cancelled", groupId = "order-cancellation-group", containerFactory = "kafkaListenerContainerFactory")
    public void processOrderCancelled(@Payload Order order) {
        log.info("Received order cancelled event: {}", order.getOrderId());
        log.info("Customer: {}, Amount: {}", order.getCustomerId(), order.getTotalAmount());

        Order cancelledOrder = orderService.cancelOrder(order);

        log.info("Order processed successfully: {}", cancelledOrder.getOrderId());
    }
}
