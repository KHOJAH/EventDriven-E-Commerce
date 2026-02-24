package com.learning.kafka.controller;

import com.learning.kafka.dto.ErrorResponse;
import com.learning.kafka.dto.OrderRequest;
import com.learning.kafka.dto.OrderResponse;
import com.learning.kafka.model.Order;
import com.learning.kafka.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Order Controller - Module 1: Kafka Fundamentals
 * 
 * REST API for order management.
 * This controller receives HTTP requests and triggers Kafka events.
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * Create a new order
     * 
     * POST /api/orders
     * Content-Type: application/json
     * 
     * Request Body:
     * {
     *   "customerId": "CUST123",
     *   "customerEmail": "customer@example.com",
     *   "totalAmount": 99.99,
     *   "items": "ITEM1,ITEM2",
     *   "shippingAddress": "123 Main St"
     * }
     * 
     * CHALLENGE 1.35: Implement create order endpoint
     * TODO: Call orderService.createOrder() with the request
     * TODO: Convert Order to OrderResponse using OrderResponse.fromOrder()
     * TODO: Return ResponseEntity with HTTP 201 Created
     * 
     * 💡 Hint: Use @Valid annotation for validation
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * @PostMapping
     * public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
     *     log.info("Received order creation request: {}", request.getCustomerId());
     *     
     *     Order order = orderService.createOrder(request);
     *     OrderResponse response = OrderResponse.fromOrder(order);
     *     
     *     return ResponseEntity.status(HttpStatus.CREATED).body(response);
     * }
     * ```
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        // TODO: Implement create order endpoint (see challenge above)
        // Write your code here
        
        
        
        
        // Placeholder - replace with your implementation
        log.warn("createOrder endpoint not implemented");
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
    
    /**
     * CHALLENGE 1.36: Add exception handling
     * TODO: Add @ExceptionHandler method for validation errors
     * TODO: Return ErrorResponse with HTTP 400 Bad Request
     * 
     * 💡 Hint: Use MethodArgumentNotValidException for validation errors
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * @ExceptionHandler(MethodArgumentNotValidException.class)
     * public ResponseEntity<ErrorResponse> handleValidationException(
     *         MethodArgumentNotValidException ex) {
     *     
     *     String errorMessage = ex.getBindingResult()
     *         .getFieldErrors()
     *         .stream()
     *         .map(error -> error.getField() + ": " + error.getDefaultMessage())
     *         .findFirst()
     *         .orElse("Validation failed");
     *     
     *     ErrorResponse error = ErrorResponse.create(HttpStatus.BAD_REQUEST.value(), errorMessage);
     *     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
     * }
     * ```
     */
    // TODO: Add exception handling (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 1.37: Add global exception handler
     * TODO: Add @ExceptionHandler for generic Exception
     * TODO: Return ErrorResponse with HTTP 500 Internal Server Error
     * 
     * 📝 SOLUTION: Similar to above but with HttpStatus.INTERNAL_SERVER_ERROR
     */
    // TODO: Add global exception handler (see challenge above)
    
    
    
    
}
