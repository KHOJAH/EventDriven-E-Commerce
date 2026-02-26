package com.learning.kafka.dto;

import com.learning.kafka.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String orderId;
    private String customerId;
    private String customerEmail;
    private BigDecimal totalAmount;
    private String status;
    private String items;
    private String shippingAddress;
    private String createdAt;

    public static OrderResponse fromOrder(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .customerEmail(order.getCustomerEmail())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .items(order.getItems())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt().toString())
                .build();
    }
}
