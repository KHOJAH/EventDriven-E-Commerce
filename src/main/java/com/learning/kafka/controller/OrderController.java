package com.learning.kafka.controller;

import com.learning.kafka.dto.OrderRequest;
import com.learning.kafka.dto.OrderResponse;
import com.learning.kafka.model.Order;
import com.learning.kafka.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Request Body:
     * {
     * "customerId": "CUST123",
     * "customerEmail": "customer@example.com",
     * "totalAmount": 99.99,
     * "items": "ITEM1,ITEM2",
     * "shippingAddress": "123 Main St"
     * }
     * 💡 Hint: Use @Valid annotation for validation
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        log.info("Received order creation request: {}", request.getCustomerId());

        Order order = orderService.createOrder(request);
        OrderResponse response = OrderResponse.fromOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
