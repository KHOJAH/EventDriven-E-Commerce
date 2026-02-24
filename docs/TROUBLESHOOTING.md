# 🐛 Troubleshooting Guide

Common issues and solutions for Kafka development.

---

## Table of Contents

1. [Startup Issues](#startup-issues)
2. [Connection Issues](#connection-issues)
3. [Producer Issues](#producer-issues)
4. [Consumer Issues](#consumer-issues)
5. [Serialization Issues](#serialization-issues)
6. [Error Handling Issues](#error-handling-issues)
7. [Performance Issues](#performance-issues)
8. [Docker Issues](#docker-issues)

---

## Startup Issues

### Application Won't Start

**Symptom:** Application fails on startup with Kafka-related errors

**Common Causes:**
1. Kafka broker not running
2. Wrong bootstrap-servers configuration
3. Port conflicts

**Solutions:**

```bash
# Check if Kafka is running
docker-compose ps

# Check Kafka logs
docker logs kafka

# Verify port is not in use (Windows)
netstat -ano | findstr :9092

# Kill process using port 9092
taskkill /PID <PID> /F
```

**Prevention:**
```yaml
# application.yml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
```

### Topic Auto-Creation Fails

**Symptom:** `UnknownTopicOrPartitionException`

**Causes:**
- `auto.create.topics.enable=false` in broker config
- Insufficient permissions

**Solutions:**

1. Enable auto-creation in docker-compose.yml:
```yaml
environment:
  KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
```

2. Or manually create topics:
```java
@Bean
public NewTopic myTopic() {
    return TopicBuilder.name("my-topic").build();
}
```

---

## Connection Issues

### Connection Refused

**Symptom:** 
```
org.apache.kafka.common.errors.TimeoutException: 
Failed to update metadata after 60000 ms.
```

**Causes:**
- Kafka broker not running
- Wrong advertised listeners
- Firewall blocking connection

**Solutions:**

1. Verify Kafka is running:
```bash
docker-compose ps
# Should show kafka container as "Up"
```

2. Check advertised listeners in docker-compose.yml:
```yaml
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
```

3. Test connection:
```bash
# From inside container
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list

# From host
kafka-topics --bootstrap-server localhost:9092 --list
```

### Broker Not Available

**Symptom:**
```
org.apache.kafka.common.errors.NotEnoughReplicasException: 
The size of the current ISR Set is insufficient
```

**Causes:**
- Broker crashed
- Network partition
- min.insync.replicas too high

**Solutions:**

1. Check broker status:
```bash
docker logs kafka
```

2. For local development, reduce replication:
```yaml
KAFKA_DEFAULT_REPLICATION_FACTOR: 1
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

---

## Producer Issues

### Messages Not Being Sent

**Symptom:** No errors but messages don't appear in topic

**Causes:**
- Async send without waiting
- Acks configuration too strict
- Network issues

**Solutions:**

1. Add callback to verify send:
```java
kafkaTemplate.send("topic", "key", "value")
    .whenComplete((result, ex) -> {
        if (ex == null) {
            log.info("Sent: offset={}", result.getRecordMetadata().offset());
        } else {
            log.error("Failed: {}", ex.getMessage(), ex);
        }
    });
```

2. Check acks configuration:
```yaml
spring:
  kafka:
    producer:
      acks: all  # Try changing to '1' for testing
```

3. Enable idempotence:
```yaml
spring:
  kafka:
    producer:
      properties:
        enable.idempotence: true
```

### Slow Producer Performance

**Symptom:** High latency when sending messages

**Solutions:**

1. Enable batching:
```yaml
spring:
  kafka:
    producer:
      properties:
        linger.ms: 10
        batch.size: 65536
```

2. Enable compression:
```yaml
spring:
  kafka:
    producer:
      properties:
        compression.type: snappy
```

3. Increase buffer memory:
```yaml
spring:
  kafka:
    producer:
      properties:
        buffer.memory: 67108864
```

---

## Consumer Issues

### Consumer Not Receiving Messages

**Symptom:** @KafkaListener not triggered

**Causes:**
- Wrong topic name
- Wrong group-id
- Consumer lag
- Partition assignment issues

**Solutions:**

1. Verify topic exists:
```bash
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list
```

2. Check consumer group:
```bash
docker exec -it kafka \
  kafka-consumer-groups --bootstrap-server localhost:9092 --list
```

3. Reset consumer offsets:
```bash
docker exec -it kafka \
  kafka-consumer-groups --bootstrap-server localhost:9092 \
  --group my-group --reset-offsets --to-earliest --execute --topic my-topic
```

4. Check auto-offset-reset:
```yaml
spring:
  kafka:
    consumer:
      auto-offset-reset: earliest  # or 'latest'
```

### Messages Processed Multiple Times

**Symptom:** Duplicate processing of same message

**Causes:**
- Consumer rebalancing
- Manual commit not called
- Processing takes too long

**Solutions:**

1. Implement idempotency:
```java
private final Set<String> processedKeys = ConcurrentHashMap.newKeySet();

@KafkaListener(topics = "my-topic", groupId = "my-group")
public void listen(Message message, Acknowledgment ack) {
    if (processedKeys.contains(message.getIdempotencyKey())) {
        log.warn("Duplicate detected");
        ack.acknowledge();
        return;
    }
    
    process(message);
    processedKeys.add(message.getIdempotencyKey());
    ack.acknowledge();
}
```

2. Increase session timeout:
```yaml
spring:
  kafka:
    consumer:
      properties:
        session.timeout.ms: 45000
        heartbeat.interval.ms: 15000
```

3. Reduce max poll records:
```yaml
spring:
  kafka:
    consumer:
      properties:
        max.poll.records: 100
```

### Consumer Lag Increasing

**Symptom:** Consumer falling behind producer

**Solutions:**

1. Increase concurrency:
```java
factory.setConcurrency(6); // Must be <= partitions
```

2. Optimize processing logic:
```java
@KafkaListener(topics = "my-topic")
public void listen(Message message) {
    // Move slow operations to async
    CompletableFuture.runAsync(() -> processSlow(message));
}
```

3. Add more consumer instances:
```bash
# Scale horizontally by running multiple instances
```

---

## Serialization Issues

### JSON Deserialization Error

**Symptom:**
```
org.springframework.kafka.support.serializer.JsonDeserializer$ErrorWhileDeserializingHeader:
Problem while deserializing
```

**Causes:**
- Untrusted package
- Class not found
- JSON structure mismatch

**Solutions:**

1. Set trusted packages:
```yaml
spring:
  kafka:
    consumer:
      properties:
        spring.json.trusted.packages: "com.learning.kafka.model"
        # Or for all packages (development only)
        spring.json.trusted.packages: "*"
```

2. Verify class exists in consumer application:
```java
// Ensure Order class exists in both producer and consumer
package com.learning.kafka.model;
```

3. Use type mapping:
```yaml
spring:
  kafka:
    consumer:
      properties:
        spring.json.type.mapping: "com.example.Order:com.learning.kafka.model.Order"
```

### ClassCastException

**Symptom:**
```
java.lang.ClassCastException: 
Cannot cast java.util.LinkedHashMap to com.learning.kafka.model.Order
```

**Causes:**
- Using wrong deserializer
- Missing type information

**Solutions:**

1. Use JsonDeserializer with type:
```java
@JsonDeserialize(using = JsonDeserializer.class)
public class Order { ... }
```

2. Configure deserializer properly:
```java
JsonDeserializer<Order> deserializer = new JsonDeserializer<>(Order.class);
```

---

## Error Handling Issues

### Retry Not Working

**Symptom:** Messages not retried on failure

**Causes:**
- Wrong exception type
- ErrorHandler not configured
- Retry configuration incorrect

**Solutions:**

1. Verify exception classification:
```java
errorHandler.addRetryableExceptions(RetryableException.class);
errorHandler.addNotRetryableExceptions(NonRetryableException.class);
```

2. Check @RetryableTopic configuration:
```java
@RetryableTopic(
    attempts = "3",
    backoff = @Backoff(delay = 1000),
    include = {RetryableException.class}
)
```

3. Verify error handler bean:
```java
@Bean
public DefaultErrorHandler errorHandler() {
    // Ensure this bean is created
}
```

### DLT Not Receiving Messages

**Symptom:** Failed messages disappear instead of going to DLT

**Causes:**
- DLT not configured
- Wrong DLT naming
- Recoverer not set up

**Solutions:**

1. Verify DLT naming:
```
Default DLT name: {original-topic}.DLT
```

2. Configure DeadLetterPublishingRecoverer:
```java
@Bean
public DefaultErrorHandler errorHandler(KafkaOperations<String, Object> kafkaTemplate) {
    DeadLetterPublishingRecoverer recoverer = 
        new DeadLetterPublishingRecoverer(kafkaTemplate);
    return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3L));
}
```

3. Add @DltHandler:
```java
@DltHandler
public void handleDlt(Message message) {
    log.error("Message failed: {}", message);
}
```

---

## Performance Issues

### High Latency

**Symptom:** Messages take too long to process

**Solutions:**

1. Profile message processing:
```java
@KafkaListener(topics = "my-topic")
public void listen(Message message) {
    Instant start = Instant.now();
    process(message);
    log.info("Processing took: {}ms", Duration.between(start, Instant.now()).toMillis());
}
```

2. Tune consumer:
```yaml
spring:
  kafka:
    consumer:
      properties:
        fetch.min.bytes: 1048576
        fetch.max.wait.ms: 500
```

3. Use concurrent processing:
```java
factory.setConcurrency(6);
factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
```

### Memory Issues

**Symptom:** OutOfMemoryError

**Solutions:**

1. Reduce batch size:
```yaml
spring:
  kafka:
    consumer:
      properties:
        max.poll.records: 100  # Reduce from 500
```

2. Increase JVM heap:
```bash
java -Xmx2G -jar application.jar
```

3. Clear caches periodically:
```java
@Scheduled(fixedRate = 300000) // Every 5 minutes
public void clearCaches() {
    processedKeys.clear();
}
```

---

## Docker Issues

### Kafka Container Won't Start

**Symptom:** Container exits immediately

**Solutions:**

1. Check logs:
```bash
docker logs kafka
```

2. Verify Zookeeper is running first:
```bash
docker-compose up zookeeper
docker-compose up kafka
```

3. Check port conflicts:
```bash
# Windows
netstat -ano | findstr :9092
```

4. Recreate containers:
```bash
docker-compose down -v
docker-compose up -d
```

### Kafka UI Not Accessible

**Symptom:** Can't access http://localhost:8080

**Solutions:**

1. Check if container is running:
```bash
docker-compose ps
```

2. Verify port mapping:
```yaml
ports:
  - "8080:8080"
```

3. Check logs:
```bash
docker logs kafka-ui
```

4. Access via Docker network:
```bash
docker network ls
docker inspect kafka-ui
```

---

## Debugging Tips

### Enable Debug Logging

```yaml
logging:
  level:
    root: INFO
    com.learning.kafka: DEBUG
    org.springframework.kafka: DEBUG
    org.apache.kafka: DEBUG
```

### Use Kafka UI

1. Access: http://localhost:8080
2. View topics and messages
3. Monitor consumer groups
4. Produce test messages

### Monitor with Actuator

```bash
# View metrics
curl http://localhost:8080/actuator/metrics

# View Kafka-specific metrics
curl http://localhost:8080/actuator/prometheus
```

### Test with Console Producer/Consumer

```bash
# Console producer
docker exec -it kafka \
  kafka-console-producer --bootstrap-server localhost:9092 --topic my-topic

# Console consumer
docker exec -it kafka \
  kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic my-topic --from-beginning
```

---

## Getting Help

### Useful Commands

```bash
# View all topics
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list

# Describe topic
docker exec -it kafka \
  kafka-topics --bootstrap-server localhost:9092 \
  --describe --topic order-created

# View consumer groups
docker exec -it kafka \
  kafka-consumer-groups --bootstrap-server localhost:9092 --list

# Describe consumer group
docker exec -it kafka \
  kafka-consumer-groups --bootstrap-server localhost:9092 \
  --describe --group kafka-mastery-group

# Reset offsets
docker exec -it kafka \
  kafka-consumer-groups --bootstrap-server localhost:9092 \
  --group my-group --reset-offsets --to-earliest --execute --topic my-topic
```

### Log Files

```bash
# Application logs
docker logs kafka-mastery-project

# Kafka broker logs
docker logs kafka

# Zookeeper logs
docker logs zookeeper
```

---

Still having issues? Check:
- Spring Kafka Documentation: https://docs.spring.io/spring-kafka/reference/
- Apache Kafka Documentation: https://kafka.apache.org/documentation/
- Stack Overflow: https://stackoverflow.com/questions/tagged/spring-kafka
