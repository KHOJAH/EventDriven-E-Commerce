# 📋 Kafka Cheatsheet

Quick reference guide for Spring Boot Kafka development.

---

## Annotations

### @KafkaListener

```java
@KafkaListener(
    topics = "my-topic",
    groupId = "my-group",
    containerFactory = "kafkaListenerContainerFactory",
    errorHandler = "customErrorHandler"
)
public void listen(MyMessage message) {
    // Process message
}
```

### @RetryableTopic

```java
@RetryableTopic(
    attempts = "3",
    backoff = @Backoff(delay = 1000, multiplier = 2),
    include = {RetryableException.class},
    exclude = {NonRetryableException.class},
    autoCreateTopics = "true",
    dltStrategy = DltStrategy.FAIL_ON_ERROR
)
@KafkaListener(topics = "my-topic", groupId = "my-group")
public void listenWithRetry(MyMessage message) {
    // Process message with retry
}
```

### @DltHandler

```java
@DltHandler
public void handleDlt(
    MyMessage message,
    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exception
) {
    log.error("Message failed after all retries");
}
```

### Other Annotations

| Annotation | Purpose |
|------------|---------|
| `@KafkaHandler` | Route messages based on payload type |
| `@Payload` | Mark message payload parameter |
| `@Header` | Access Kafka headers |
| `@Headers` | Access all headers |
| `@TopicPartition` | Manual partition assignment |
| `@SendTo` | Send response to another topic |

---

## Configuration Properties

### Producer

```yaml
spring:
  kafka:
    producer:
      bootstrap-servers: localhost:9092
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all
      retries: 3
      properties:
        linger.ms: 5
        compression.type: snappy
        enable.idempotence: true
        batch.size: 16384
        buffer.memory: 33554432
```

### Consumer

```yaml
spring:
  kafka:
    consumer:
      bootstrap-servers: localhost:9092
      group-id: my-group
      auto-offset-reset: earliest
      enable-auto-commit: false
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "*"
        isolation.level: read_committed
        max.poll.records: 500
        session.timeout.ms: 30000
        heartbeat.interval.ms: 10000
```

### Admin

```yaml
spring:
  kafka:
    admin:
      properties:
        bootstrap.servers: localhost:9092
```

---

## KafkaTemplate Operations

### Basic Send

```java
// Fire and forget
kafkaTemplate.send("topic", "key", "value");

// With callback
CompletableFuture<SendResult<String, Object>> future = 
    kafkaTemplate.send("topic", "key", "value");

future.whenComplete((result, ex) -> {
    if (ex == null) {
        log.info("Sent: offset={}", result.getRecordMetadata().offset());
    } else {
        log.error("Failed: {}", ex.getMessage());
    }
});
```

### Transactional Send

```java
kafkaTemplate.executeInTransaction(operations -> {
    operations.send("topic1", "key1", "value1");
    operations.send("topic2", "key2", "value2");
    return null;
});
```

### Send with Headers

```java
ProducerRecord<String, Object> record = 
    new ProducerRecord<>("topic", "key", "value");
record.headers().add("correlationId", correlationId.getBytes());
kafkaTemplate.send(record);
```

---

## Error Handling

### DefaultErrorHandler

```java
@Bean
public DefaultErrorHandler errorHandler(KafkaOperations<String, Object> kafkaTemplate) {
    DeadLetterPublishingRecoverer recoverer = 
        new DeadLetterPublishingRecoverer(kafkaTemplate);
    
    FixedBackOff backOff = new FixedBackOff(1000L, 3L);
    
    DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);
    
    // Classify exceptions
    handler.addRetryableExceptions(RetryableException.class);
    handler.addNotRetryableExceptions(NonRetryableException.class);
    
    return handler;
}
```

### Exponential Backoff

```java
ExponentialBackOff backOff = new ExponentialBackOff();
backOff.setInitialInterval(1000L);
backOff.setMultiplier(2.0);
backOff.setMaxInterval(10000L);
backOff.setMaxAttempts(5L);
```

### Common Exceptions

| Exception | When to Use |
|-----------|-------------|
| `RetryableException` | Temporary failures (network, DB connection) |
| `NonRetryableException` | Permanent failures (validation, business rules) |
| `ListenerExecutionFailedException` | Processing errors |
| `SerializationException` | JSON/Avro serialization errors |

---

## Container Factory

### Basic Configuration

```java
@Bean
public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
        new ConcurrentKafkaListenerContainerFactory<>();
    
    factory.setConsumerFactory(consumerFactory);
    
    // Manual offset commit
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    
    // Concurrency
    factory.setConcurrency(3);
    
    return factory;
}
```

### Acknowledgment Modes

| Mode | Description |
|------|-------------|
| `RECORD` | Commit after each record |
| `BATCH` | Commit after processing batch |
| `MANUAL` | Application controls commit |
| `MANUAL_IMMEDIATE` | Commit immediately on ack.acknowledge() |

---

## Topic Management

### Programmatic Topic Creation

```java
@Bean
public NewTopic myTopic() {
    return TopicBuilder.name("my-topic")
            .partitions(6)
            .replicas(3)
            .configs(Map.of(
                "retention.ms", "604800000", // 7 days
                "cleanup.policy", "delete"
            ))
            .build();
}
```

### Topic Configuration Options

```java
TopicBuilder.name("topic")
    .partitions(6)
    .replicas(3)
    .config("retention.ms", "604800000")
    .config("segment.bytes", "1073741824")
    .config("min.insync.replicas", "2")
    .config("max.message.bytes", "1048576")
    .build();
```

---

## Serialization

### JSON Serializer

```java
// Producer
configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

// Consumer
configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.model");
```

### Custom Serializer

```java
public class CustomSerializer implements Serializer<MyObject> {
    @Override
    public byte[] serialize(String topic, MyObject data) {
        // Custom serialization logic
        return objectMapper.writeValueAsBytes(data);
    }
}
```

### Avro with Schema Registry

```java
// Producer
configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
configProps.put("schema.registry.url", "http://localhost:8081");

// Consumer
configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
```

---

## Testing

### @EmbeddedKafka

```java
@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" }
)
class KafkaIntegrationTest {
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Test
    void testMessageSending() {
        kafkaTemplate.send("test-topic", "key", "value");
        // Verify message was sent
    }
}
```

### Testcontainers

```java
@Testcontainers
@SpringBootTest
class KafkaContainerTest {
    
    @Container
    static KafkaContainer kafka = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    );
    
    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
}
```

---

## Monitoring

### KafkaClientMetrics

```java
@Bean
public KafkaClientMetrics producerMetrics(
        MeterRegistry meterRegistry,
        ProducerFactory<String, Object> producerFactory) {
    
    KafkaClientMetrics metrics = new KafkaClientMetrics(
        producerFactory.getConfigurationProperties(),
        "producer"
    );
    metrics.bindTo(meterRegistry);
    return metrics;
}
```

### Key Metrics to Monitor

| Metric | Type | Description |
|--------|------|-------------|
| `records-send-rate` | Gauge | Messages sent per second |
| `request-latency-avg` | Timer | Average request latency |
| `compression-rate` | Gauge | Compression ratio |
| `records-consumed-rate` | Gauge | Messages consumed per second |
| `fetch-latency` | Timer | Fetch request latency |
| `consumer-lag` | Gauge | Messages behind latest |

---

## Common Patterns

### Request-Reply Pattern

```java
@KafkaListener(topics = "request-topic")
@SendTo("reply-topic")
public Reply handleRequest(Request request) {
    return process(request);
}
```

### Fan-Out Pattern

```java
// Multiple consumers with different group IDs
@KafkaListener(topics = "events", groupId = "service-a")
public void listenA(Event event) { }

@KafkaListener(topics = "events", groupId = "service-b")
public void listenB(Event event) { }
```

### Partition Key Strategy

```java
// Ensure ordering for related messages
String partitionKey = order.getOrderId();
kafkaTemplate.send("orders", partitionKey, order);
```

---

## Troubleshooting

### Common Issues

| Issue | Solution |
|-------|----------|
| Consumer not receiving messages | Check bootstrap-servers, group-id, topic name |
| JSON deserialization error | Set trusted.packages property |
| Connection refused | Verify Kafka is running and listeners configured |
| Duplicate messages | Check idempotence, exactly-once settings |
| High consumer lag | Increase concurrency, optimize processing |

### Debug Commands

```bash
# List topics
kafka-topics --bootstrap-server localhost:9092 --list

# Describe topic
kafka-topics --bootstrap-server localhost:9092 --describe --topic my-topic

# List consumer groups
kafka-consumer-groups --bootstrap-server localhost:9092 --list

# Describe consumer group
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-group

# Reset offsets
kafka-consumer-groups --bootstrap-server localhost:9092 \
    --group my-group --reset-offsets --to-earliest --execute --topic my-topic
```

---

## Performance Tuning

### Producer Tuning

```properties
# Increase batch size
batch.size=65536

# Wait longer to batch
linger.ms=10

# More buffer memory
buffer.memory=67108864

# Enable compression
compression.type=snappy
```

### Consumer Tuning

```properties
# Fetch more records per poll
max.poll.records=1000

# Increase fetch size
fetch.max.bytes=52428800

# Adjust session timeout
session.timeout.ms=45000

# Increase heartbeat interval
heartbeat.interval.ms=15000
```

---

## Quick Reference: Enum Values

### Acknowledgment Mode

```java
ContainerProperties.AckMode.RECORD
ContainerProperties.AckMode.BATCH
ContainerProperties.AckMode.MANUAL
ContainerProperties.AckMode.MANUAL_IMMEDIATE
```

### Auto Offset Reset

```java
AutoOffsetReset.EARLIEST  // Start from beginning
AutoOffsetReset.LATEST    // Start from now
AutoOffsetReset.NONE      // Throw exception if no offset
```

### Isolation Level

```java
IsolationLevel.READ_UNCOMMITTED  // Read all messages
IsolationLevel.READ_COMMITTED    // Only committed transactions
```

---

Happy Coding! 🚀
