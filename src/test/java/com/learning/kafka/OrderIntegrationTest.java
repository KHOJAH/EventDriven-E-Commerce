package com.learning.kafka;

import com.learning.kafka.dto.OrderRequest;
import com.learning.kafka.model.Order;
import com.learning.kafka.producer.OrderProducer;
import com.learning.kafka.consumer.OrderConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Order Integration Tests - Module 7: Testing Strategies
 * 
 * This test class demonstrates integration testing with @EmbeddedKafka.
 * 
 * Learning Objectives:
 * - Set up EmbeddedKafka for testing
 * - Test producer functionality
 * - Test consumer functionality
 * - Test end-to-end message flow
 * 
 * CHALLENGE 7.1: Understand @EmbeddedKafka
 * TODO: Research what @EmbeddedKafka annotation does
 * 💡 Hint: It starts an in-memory Kafka broker for testing
 * 
 * 📝 Solution:
 * @EmbeddedKafka creates a temporary Kafka broker that:
 * 1. Starts before tests run
 * 2. Creates specified topics
 * 3. Stops after tests complete
 * 4. Provides bootstrap servers via system property
 * 
 * @author Kafka Mastery Project
 */
@SpringBootTest
@EmbeddedKafka(
    partitions = 3,
    brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
    topics = { "order-created", "order-confirmed", "order-cancelled" }
)
@DirtiesContext // Clean context after each test
class OrderIntegrationTest {
    
    @Autowired
    private OrderProducer orderProducer;
    
    @Autowired
    private OrderConsumer orderConsumer;
    
    /**
     * CHALLENGE 7.2: Test basic message sending
     * TODO: Create a test that sends an order and verifies it was sent
     * TODO: Use CountDownLatch to wait for async operation
     * TODO: Verify message arrives in topic
     * 
     * 💡 Hint: 
     * CountDownLatch latch = new CountDownLatch(1);
     * latch.await(10, TimeUnit.SECONDS);
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * @Test
     * void testOrderCreatedEvent() throws InterruptedException {
     *     // Given
     *     CountDownLatch latch = new CountDownLatch(1);
     *     Order order = Order.createNew(
     *         "CUST123",
     *         "customer@example.com",
     *         new BigDecimal("99.99"),
     *         "ITEM1,ITEM2",
     *         "123 Main St"
     *     );
     *     
     *     // When
     *     orderProducer.sendOrderCreatedAsync(order);
     *     
     *     // Then
     *     boolean completed = latch.await(10, TimeUnit.SECONDS);
     *     assertTrue(completed, "Message should be sent within timeout");
     * }
     * ```
     */
    @Test
    void testOrderCreatedEvent() throws InterruptedException {
        // TODO: Implement test (see challenge above)
        // Write your code here
        
        
        
        
        // Placeholder assertion
        assertTrue(true, "Test not implemented yet");
    }
    
    /**
     * CHALLENGE 7.3: Test message consumption
     * TODO: Create a test that verifies consumer receives and processes messages
     * TODO: Use a spy or mock to verify processing
     * TODO: Verify order status changes from PENDING to CONFIRMED
     * 
     * 💡 Hint: Use consumer to track received messages
     * 
     * 📝 SOLUTION:
     * ```java
     * @Test
     * void testOrderConsumerProcessesMessage() throws InterruptedException {
     *     // Given
     *     CountDownLatch latch = new CountDownLatch(1);
     *     Order order = Order.createNew(
     *         "CUST456",
     *         "test@example.com",
     *         new BigDecimal("149.99"),
     *         "ITEM3",
     *         "456 Oak Ave"
     *     );
     *     
     *     // When
     *     orderProducer.sendOrderCreatedAsync(order);
     *     
     *     // Wait for consumer to process
     *     boolean completed = latch.await(10, TimeUnit.SECONDS);
     *     
     *     // Then
     *     assertTrue(completed, "Message should be processed");
     *     // Verify order was confirmed (check database or output topic)
     * }
     * ```
     */
    @Test
    void testOrderConsumerProcessesMessage() throws InterruptedException {
        // TODO: Implement test (see challenge above)
        // Write your code here
        
        
        
        
        assertTrue(true, "Test not implemented yet");
    }
    
    /**
     * CHALLENGE 7.4: Test end-to-end order flow
     * TODO: Create a test that verifies complete order flow
     * TODO: Order Created → Order Confirmed
     * TODO: Verify both messages are sent to respective topics
     * 
     * 💡 Hint: Use KafkaTestUtils.getRecords() to consume from topic
     * 
     * 📝 SOLUTION:
     * ```java
     * @Test
     * void testEndToEndOrderFlow() throws InterruptedException {
     *     // Given
     *     OrderRequest request = OrderRequest.builder()
     *         .customerId("CUST789")
     *         .customerEmail("e2e@example.com")
     *         .totalAmount(new BigDecimal("199.99"))
     *         .items("ITEM1,ITEM2,ITEM3")
     *         .shippingAddress("789 Pine St")
     *         .build();
     *     
     *     // When: Create order via service
     *     // Then: Verify order-created event is sent
     *     // And: Verify order-confirmed event is sent
     *     // Use ConsumerRecord to verify message content
     * }
     * ```
     */
    @Test
    void testEndToEndOrderFlow() throws InterruptedException {
        // TODO: Implement test (see challenge above)
        // Write your code here
        
        
        
        
        assertTrue(true, "Test not implemented yet");
    }
    
    /**
     * CHALLENGE 7.5: Test with different message payloads
     * TODO: Create parameterized tests for different order scenarios
     * TODO: Test small orders, large orders, international orders
     * TODO: Verify all are processed correctly
     * 
     * 💡 Hint: Use @ParameterizedTest with @ValueSource or @CsvSource
     * 
     * 📝 SOLUTION:
     * ```java
     * @ParameterizedTest
     * @ValueSource(doubles = {9.99, 99.99, 999.99, 9999.99})
     * void testDifferentOrderAmounts(double amount) throws InterruptedException {
     *     Order order = Order.createNew(
     *         "CUST_PARAM",
     *         "param@example.com",
     *         new BigDecimal(amount),
     *         "ITEM",
     *         "Address"
     *     );
     *     
     *     orderProducer.sendOrderCreatedAsync(order);
     *     
     *     // Verify processing
     * }
     * ```
     */
    // TODO: Add parameterized tests (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 7.6: Test error scenarios
     * TODO: Create a test that verifies error handling
     * TODO: Send invalid JSON and verify it goes to DLT
     * TODO: Send message that causes exception and verify retry
     * 
     * 💡 Hint: Use mock to simulate failures
     * 
     * 📝 SOLUTION:
     * ```java
     * @Test
     * void testErrorMessageHandling() throws InterruptedException {
     *     // Given: Order that will cause processing failure
     *     Order invalidOrder = Order.builder()
     *         .orderId(null) // Invalid - null ID
     *         .build();
     *     
     *     // When: Send invalid order
     *     // Then: Verify retry attempts
     *     // And: Verify message ends up in DLT
     * }
     * ```
     */
    // TODO: Add error scenario tests (see challenge above)
    
    
    
    
}
