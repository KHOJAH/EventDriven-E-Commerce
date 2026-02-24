# 📚 Kafka Concepts & Theory

A comprehensive guide to Apache Kafka concepts used in this project.

---

## Table of Contents

1. [Kafka Fundamentals](#kafka-fundamentals)
2. [Core Concepts](#core-concepts)
3. [Producer Patterns](#producer-patterns)
4. [Consumer Patterns](#consumer-patterns)
5. [Error Handling](#error-handling)
6. [Event-Driven Architecture](#event-driven-architecture)
7. [Best Practices](#best-practices)

---

## Kafka Fundamentals

### What is Apache Kafka?

Apache Kafka is a **distributed event streaming platform** used by thousands of companies for high-performance data pipelines, streaming analytics, and data integration.

### Key Use Cases

- **Messaging**: Decouple systems with reliable message delivery
- **Event Sourcing**: Store state changes as a sequence of events
- **Stream Processing**: Process data in real-time as it arrives
- **Microservices Communication**: Enable event-driven architecture

### Architecture Overview

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Producer   │────>│   Kafka     │────>│  Consumer   │
│  (Publisher)│     │   Broker    │     │  (Subscriber)│
└─────────────┘     └─────────────┘     └─────────────┘
                           │
                    ┌──────┴──────┐
                    │   Zookeeper │
                    │  (Metadata) │
                    └─────────────┘
```

---

## Core Concepts

### Topics

A **topic** is a logical channel to which producers write and from which consumers read.

- **Analogy**: Like a table in a database or a channel in Slack
- **Durability**: Messages are persisted to disk
- **Retention**: Configurable (default: 7 days)

```yaml
# Example topics in our project:
order-created      # New orders
payment-processed  # Successful payments
inventory-reserved # Reserved inventory
notification-email # Email notifications
```

### Partitions

Topics are split into **partitions** for scalability and parallelism.

```
Topic: order-created

Partition 0: [Msg1] [Msg4] [Msg7]
Partition 1: [Msg2] [Msg5] [Msg8]
Partition 2: [Msg3] [Msg6] [Msg9]
```

**Key Points:**
- Messages within a partition are ordered
- Different partitions can be processed in parallel
- Partition key determines which partition a message goes to

### CHALLENGE SOLUTION: Why 3 partitions for order topics?

**Answer:** 
1. **Parallelism**: 3 partitions allow 3 concurrent consumers
2. **Ordering**: Orders with same ID always go to same partition
3. **Balance**: Good trade-off between parallelism and complexity

```java
// Using orderId as partition key ensures ordering
kafkaTemplate.send("order-created", order.getOrderId(), order);
```

### Consumer Groups

A **consumer group** is a set of consumers that cooperate to consume data from a topic.

```
Consumer Group: order-processor-group

┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│ Consumer 1  │  │ Consumer 2  │  │ Consumer 3  │
│ (Partition 0)│  │ (Partition 1)│  │ (Partition 2)│
└─────────────┘  └─────────────┘  └─────────────┘
```

**Key Points:**
- Each partition is consumed by only ONE consumer in a group
- Consumers in different groups ALL receive the message (pub/sub)
- Scaling: Add more consumers to increase throughput

### CHALLENGE SOLUTION: Why different group IDs?

```java
// Different groups = each service gets ALL messages (pub/sub)
@KafkaListener(topics = "order-created", groupId = "payment-service-group")
@KafkaListener(topics = "order-created", groupId = "inventory-service-group")
@KafkaListener(topics = "order-created", groupId = "notification-service-group")
```

**Answer:**
- **Different groups**: Each service receives ALL messages
- **Same group**: Messages are load-balanced (competing consumers)

### Offsets

An **offset** is a unique identifier for each message within a partition.

```
Partition 0:
Offset 0: [Msg1]
Offset 1: [Msg4]
Offset 2: [Msg7]
         ^
         Consumer is here (offset = 2)
```

**Commit Strategies:**
- **Auto-commit**: Kafka automatically commits offsets (simpler, risk of loss)
- **Manual commit**: Application controls when to commit (more reliable)

---

## Producer Patterns

### Synchronous vs Asynchronous Sending

```java
// Synchronous (NOT recommended)
SendResult result = kafkaTemplate.send(topic, message).get(); // Blocks!

// Asynchronous (recommended)
CompletableFuture<SendResult<String, Object>> future = 
    kafkaTemplate.send(topic, key, message);

future.whenComplete((result, ex) -> {
    if (ex == null) {
        // Success
    } else {
        // Failure
    }
});
```

### CHALLENGE SOLUTION: Producer Configurations

```java
// Linger MS: Wait to batch messages
configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);

// Compression: Reduce network usage
configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

// Idempotence: Prevent duplicates on retry
configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
```

### Partition Key Strategy

**Why use orderId as partition key?**

```java
kafkaTemplate.send("order-created", order.getOrderId(), order);
```

**Answer:**
1. **Ordering**: All events for same order go to same partition
2. **Processing order**: Created → Confirmed → Shipped (in order)
3. **No race conditions**: Prevents "confirmed" before "created"

### Transactional Producer

```java
kafkaTemplate.executeInTransaction(operations -> {
    operations.send("payment-processed", orderId, payment);
    operations.send("order-confirmed", orderId, order);
    return null;
});
```

**Use Case:** Ensure multiple messages are sent atomically.

---

## Consumer Patterns

### Manual Offset Commit

```java
@KafkaListener(
    topics = "order-created",
    groupId = "order-processor-group",
    containerFactory = "kafkaListenerContainerFactory" // Configured for MANUAL ack
)
public void listen(Order order, Acknowledgment ack) {
    try {
        // Process message
        processOrder(order);
        
        // Commit offset AFTER successful processing
        ack.acknowledge();
    } catch (Exception e) {
        // Don't commit - message will be redelivered
        throw e;
    }
}
```

**Why manual commit?**
- Prevents message loss
- Ensures at-least-once delivery
- Allows retry on failure

### Concurrent Consumers

```java
@Bean
public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setConcurrency(3); // 3 parallel consumers
    return factory;
}
```

**Scaling Rule:**
- Concurrency ≤ Number of partitions
- More consumers than partitions = idle consumers

### Manual Partition Assignment

```java
@KafkaListener(
    topicPartitions = @TopicPartition(
        topic = "order-created",
        partitions = "#{@partitionFinder.partitions('order-created')}"
    )
)
public void listen(Order order) {
    // Process message
}
```

**Use Case:** When you need dynamic partition discovery.

---

## Error Handling

### Default Error Handler

```java
@Bean
public DefaultErrorHandler defaultErrorHandler(KafkaOperations<String, Object> kafkaTemplate) {
    DeadLetterPublishingRecoverer recoverer = 
        new DeadLetterPublishingRecoverer(kafkaTemplate);
    
    FixedBackOff backOff = new FixedBackOff(1000L, 3L); // 1s delay, 3 attempts
    
    return new DefaultErrorHandler(recoverer, backOff);
}
```

### Retry Topics Pattern

```
Original Topic → Retry-0 (1s) → Retry-1 (2s) → DLT
     ↓               ↓             ↓           ↓
  Consumer      Consumer      Consumer     DLT Handler
```

**Non-blocking retry:** Other messages continue processing while retry waits.

### CHALLENGE SOLUTION: Exception Classification

```java
errorHandler.addRetryableExceptions(RetryableException.class);
errorHandler.addNotRetryableExceptions(NonRetryableException.class);
```

**Retryable Examples:**
- Database connection timeout
- External service unavailable
- Network timeout

**Non-Retryable Examples:**
- Invalid JSON
- Validation error
- Business rule violation

### Dead Letter Topic (DLT)

```java
@DltHandler
public void handleDlt(Payment payment, 
                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    log.error("Message sent to DLT from topic: {}", topic);
    log.error("Payment ID: {}", payment.getPaymentId());
    log.error("Correlation ID: {}", payment.getCorrelationId());
    
    // Alert operations team
    // Store for manual review
}
```

**DLT Naming Convention:** `{original-topic}.DLT`

---

## Event-Driven Architecture

### Outbox Pattern

**Problem:** Dual write inconsistency

```
1. Write to database → Success
2. Publish to Kafka → Failure (crash)
Result: Database has data, no event published ❌
```

**Solution:** Transactional Outbox

```
1. Write to database + outbox table (same transaction) → Success
2. Separate publisher polls outbox and sends to Kafka
Result: Either both succeed or both fail ✅
```

```java
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    // Stores events pending publication
}

@Scheduled(fixedDelay = 5000)
public void publishPendingEvents() {
    List<OutboxEvent> pending = outboxRepository.findByStatus(PENDING);
    for (OutboxEvent event : pending) {
        kafkaTemplate.send(event.getTopic(), event.getAggregateId(), payload);
        event.markAsPublished();
    }
}
```

### Saga Pattern

**Problem:** Distributed transactions across microservices

```
Order Service → Payment Service → Inventory Service
     ↓                ↓                 ↓
  MySQL DB        PostgreSQL        MongoDB
```

**Solution:** Saga with compensating transactions

```
Saga Flow (Happy Path):
1. Order Created
2. → Payment Processed
3. → Inventory Reserved
4. → Order Confirmed

Saga Flow (Failure):
1. Order Created
2. → Payment Processed
3. → Inventory Reservation FAILED
4. → Refund Payment (compensation)
5. → Order Cancelled
```

### Idempotency

**Problem:** Duplicate message processing

```
Consumer receives same message twice (network retry)
→ Order processed twice
→ Customer charged twice ❌
```

**Solution:** Idempotency key

```java
private final Set<String> processedKeys = ConcurrentHashMap.newKeySet();

public void listen(Order order, Acknowledgment ack) {
    // Check for duplicate
    if (processedKeys.contains(order.getIdempotencyKey())) {
        log.warn("Duplicate detected - skipping");
        ack.acknowledge();
        return;
    }
    
    // Process message
    processOrder(order);
    
    // Mark as processed
    processedKeys.add(order.getIdempotencyKey());
    ack.acknowledge();
}
```

---

## Best Practices

### Producer Best Practices

1. **Use async sending** with callbacks
2. **Set acks=all** for durability
3. **Enable idempotence** to prevent duplicates
4. **Use compression** (snappy, gzip)
5. **Batch messages** (linger.ms = 5-10)
6. **Use meaningful partition keys**

### Consumer Best Practices

1. **Use manual offset commit** for reliability
2. **Configure concurrency** based on partitions
3. **Handle exceptions** properly
4. **Monitor consumer lag**
5. **Implement idempotency** for critical operations

### Topic Design

1. **Name topics clearly**: `order-created` not `orders`
2. **Use separate topics** for different event types
3. **Configure retention** based on use case
4. **Set appropriate partitions** for scalability

### Error Handling

1. **Classify exceptions** (retryable vs non-retryable)
2. **Use retry topics** for transient errors
3. **Send to DLT** after max retries
4. **Monitor DLT** and alert on failures
5. **Log correlation IDs** for tracing

### Monitoring

1. **Track consumer lag**
2. **Monitor producer latency**
3. **Alert on DLT messages**
4. **Track message throughput**
5. **Use distributed tracing** (correlation IDs)

---

## Quick Reference

### Common Annotations

| Annotation | Purpose |
|------------|---------|
| `@KafkaListener` | Listen to Kafka topics |
| `@RetryableTopic` | Automatic retry with backoff |
| `@DltHandler` | Handle dead letter messages |
| `@Payload` | Mark message payload parameter |
| `@Header` | Access Kafka headers |

### Common Configurations

| Config | Recommended Value |
|--------|------------------|
| `acks` | `all` |
| `retries` | `3` |
| `linger.ms` | `5` |
| `compression.type` | `snappy` |
| `enable.idempotence` | `true` |
| `auto-offset-reset` | `earliest` |
| `enable-auto-commit` | `false` |

---

Happy Learning! 🚀
