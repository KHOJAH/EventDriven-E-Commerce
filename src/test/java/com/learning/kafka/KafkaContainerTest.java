package com.learning.kafka;

import com.learning.kafka.model.Order;
import com.learning.kafka.producer.OrderProducer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testcontainers Kafka Test - Module 7: Testing Strategies
 * 
 * This test demonstrates using Testcontainers for Kafka integration testing.
 * Testcontainers provides real Kafka containers (not embedded) for more realistic testing.
 * 
 * Learning Objectives:
 * - Set up Testcontainers for Kafka
 * - Test with real Kafka container
 * - Verify message production and consumption
 * - Test container lifecycle management
 * 
 * CHALLENGE 7.12: Understand Testcontainers vs EmbeddedKafka
 * TODO: Research the difference between Testcontainers and @EmbeddedKafka
 * 💡 Hint: One uses Docker, the other is in-memory
 * 
 * 📝 Solution:
 * - @EmbeddedKafka: In-memory Kafka, faster but less realistic
 * - Testcontainers: Real Kafka in Docker, slower but more realistic
 * 
 * When to use each:
 * - Unit tests: @EmbeddedKafka
 * - Integration tests: Testcontainers
 * - CI/CD: Testcontainers (matches production)
 * 
 * @author Kafka Mastery Project
 */
@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
    topics = { "test-orders" }
)
@DirtiesContext
class KafkaContainerTest {
    
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;
    
    @Autowired
    private OrderProducer orderProducer;
    
    private Consumer<String, Object> consumer;
    
    /**
     * Set up consumer before each test
     * 
     * CHALLENGE 7.13: Configure test consumer
     * TODO: Create a consumer for testing
     * TODO: Configure with appropriate deserializers
     * TODO: Subscribe to test topic
     * 
     * 💡 Hint: Use DefaultKafkaConsumerFactory
     * 
     * 📝 SOLUTION:
     * ```java
     * @BeforeEach
     * void setUp() {
     *     Map<String, Object> consumerProps = new HashMap<>();
     *     consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString());
     *     consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
     *     consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
     *     consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
     *     
     *     ConsumerFactory<String, Object> consumerFactory = 
     *         new DefaultKafkaConsumerFactory<>(
     *             consumerProps,
     *             new StringDeserializer(),
     *             new JsonDeserializer<>(Object.class)
     *         );
     *     
     *     consumer = consumerFactory.createConsumer();
     *     embeddedKafka.consumeFromAllEmbeddedTopics(consumer);
     * }
     * ```
     */
    @BeforeEach
    void setUp() {
        // TODO: Configure test consumer (see challenge above)
        // Write your code here
        
        
        
        
    }
    
    /**
     * Clean up consumer after each test
     */
    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }
    
    /**
     * CHALLENGE 7.14: Test message production with consumer verification
     * TODO: Send a message using producer
     * TODO: Consume the message using test consumer
     * TODO: Verify message content
     * 
     * 💡 Hint: Use KafkaTestUtils.getRecords()
     * 
     * 📝 SOLUTION:
     * ```java
     * @Test
     * void testMessageProduction() throws InterruptedException {
     *     // Given
     *     Order order = Order.createNew(
     *         "TEST_CUST",
     *         "test@example.com",
     *         new BigDecimal("50.00"),
     *         "TEST_ITEM",
     *         "Test Address"
     *     );
     *     
     *     // When
     *     orderProducer.sendOrderCreated(order);
     *     
     *     // Wait for message to be available
     *     Thread.sleep(100);
     *     
     *     // Then
     *     ConsumerRecords<String, Object> records = 
     *         KafkaTestUtils.getRecords(consumer, 5000, false);
     *     
     *     assertNotNull(records);
     *     assertFalse(records.isEmpty());
     *     
     *     ConsumerRecord<String, Object> record = records.records("test-orders").iterator().next();
     *     Order receivedOrder = (Order) record.value();
     *     
     *     assertEquals(order.getOrderId(), receivedOrder.getOrderId());
     *     assertEquals(order.getCustomerId(), receivedOrder.getCustomerId());
     * }
     * ```
     */
    @Test
    void testMessageProduction() throws InterruptedException {
        // TODO: Implement test (see challenge above)
        // Write your code here
        
        
        
        
        assertTrue(true, "Test not implemented yet");
    }
    
    /**
     * CHALLENGE 7.15: Test message with specific key
     * TODO: Send a message with a specific partition key
     * TODO: Verify the key is preserved
     * TODO: Verify the message goes to correct partition
     * 
     * 💡 Hint: Use record.partition() to verify partition
     * 
     * 📝 SOLUTION:
     * ```java
     * @Test
     * void testMessageWithPartitionKey() throws InterruptedException {
     *     // Given
     *     Order order = Order.createNew(...);
     *     String partitionKey = order.getOrderId();
     *     
     *     // When
     *     kafkaTemplate.send("test-orders", partitionKey, order);
     *     
     *     // Then
     *     ConsumerRecords<String, Object> records = KafkaTestUtils.getRecords(consumer);
     *     ConsumerRecord<String, Object> record = records.iterator().next();
     *     
     *     assertEquals(partitionKey, record.key());
     *     // Partition depends on key hash and partition count
     * }
     * ```
     */
    @Test
    void testMessageWithPartitionKey() throws InterruptedException {
        // TODO: Implement test (see challenge above)
        // Write your code here
        
        
        
        
        assertTrue(true, "Test not implemented yet");
    }
    
    /**
     * CHALLENGE 7.16: Test multiple messages
     * TODO: Send multiple messages
     * TODO: Verify all messages are received
     * TODO: Verify message order (within partition)
     * 
     * 💡 Hint: Use loop to send multiple messages
     * 
     * 📝 SOLUTION:
     * ```java
     * @Test
     * void testMultipleMessages() throws InterruptedException {
     *     // Given
     *     int messageCount = 5;
     *     
     *     // When: Send multiple messages
     *     for (int i = 0; i < messageCount; i++) {
     *         Order order = Order.createNew(
     *             "CUST_" + i,
     *             "test" + i + "@example.com",
     *             new BigDecimal("10.00"),
     *             "ITEM_" + i,
     *             "Address " + i
     *         );
     *         orderProducer.sendOrderCreated(order);
     *     }
     *     
     *     // Then: Verify all messages received
     *     ConsumerRecords<String, Object> records = KafkaTestUtils.getRecords(consumer, 5000, false);
     *     assertEquals(messageCount, records.count());
     * }
     * ```
     */
    @Test
    void testMultipleMessages() throws InterruptedException {
        // TODO: Implement test (see challenge above)
        // Write your code here
        
        
        
        
        assertTrue(true, "Test not implemented yet");
    }
    
    /**
     * CHALLENGE 7.17: Test message headers
     * TODO: Send a message with custom headers
     * TODO: Verify headers are preserved
     * TODO: Use headers for correlation ID
     * 
     * 💡 Hint: Use ProducerRecord with headers
     * 
     * 📝 SOLUTION:
     * ```java
     * @Test
     * void testMessageHeaders() throws InterruptedException {
     *     // Given
     *     Order order = Order.createNew(...);
     *     String correlationId = "CORR_" + System.currentTimeMillis();
     *     
     *     ProducerRecord<String, Object> record = 
     *         new ProducerRecord<>("test-orders", order.getOrderId(), order);
     *     record.headers().add("correlationId", correlationId.getBytes());
     *     
     *     // When
     *     kafkaTemplate.send(record);
     *     
     *     // Then
     *     ConsumerRecords<String, Object> records = KafkaTestUtils.getRecords(consumer);
     *     ConsumerRecord<String, Object> received = records.iterator().next();
     *     
     *     Header header = received.headers().lastHeader("correlationId");
     *     assertNotNull(header);
     *     assertEquals(correlationId, new String(header.value()));
     * }
     * ```
     */
    @Test
    void testMessageHeaders() throws InterruptedException {
        // TODO: Implement test (see challenge above)
        // Write your code here
        
        
        
        
        assertTrue(true, "Test not implemented yet");
    }
}
