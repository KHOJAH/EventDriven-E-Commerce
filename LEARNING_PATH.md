# 🎓 Kafka Mastery Learning Path

Your complete guide to mastering Apache Kafka in Spring Boot.

---

## 📍 Where to Start

### Step 1: Setup (30 minutes)

```bash
# 1. Start Kafka infrastructure
docker-compose up -d

# 2. Verify Kafka UI is accessible
# Open: http://localhost:8080

# 3. Run the application
mvn spring-boot:run
```

✅ **Success Check:** Application starts without errors, Kafka UI shows topics

---

## 📚 Recommended Learning Order

### Week 1: Kafka Fundamentals (Modules 1-2)

**Day 1-2: Module 1 - Basic Producer/Consumer**
- [ ] Read: `docs/KAFKA_CONCEPTS.md` - Kafka Fundamentals section
- [ ] Complete: `Order.java` challenges (1.1-1.5)
- [ ] Complete: `Payment.java` challenges (1.6-1.8)
- [ ] Complete: `OrderProducer.java` challenges (1.25-1.31)
- [ ] Complete: `OrderConsumer.java` challenges (1.38-1.44)
- [ ] Complete: `OrderService.java` challenges (1.32-1.34)
- [ ] Complete: `OrderController.java` challenges (1.35-1.37)

**Day 3-4: Continue Model Classes**
- [ ] Complete: `Inventory.java` challenges (1.9-1.12)
- [ ] Complete: `Notification.java` challenges (1.13-1.16)
- [ ] Complete: DTO challenges (1.17-1.24)
- [ ] Complete: Service challenges (1.45-1.51)

**Day 5-7: Module 2 - Advanced Producer**
- [ ] Read: `docs/KAFKA_CONCEPTS.md` - Producer Patterns section
- [ ] Complete: `KafkaProducerConfig.java` challenges (2.1-2.11)
- [ ] Complete: `PaymentProducer.java` challenges (2.12-2.14)
- [ ] Complete: `InventoryProducer.java` challenges (2.15-2.16)
- [ ] Complete: `NotificationProducer.java` challenges (2.17-2.18)
- [ ] Complete: `KafkaTopicConfig.java` challenges (2.1-2.4)

**Testing:**
```bash
# Test your implementation
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST123",
    "customerEmail": "test@example.com",
    "totalAmount": 99.99,
    "items": "ITEM1,ITEM2",
    "shippingAddress": "123 Main St"
  }'

# Check Kafka UI for messages
# http://localhost:8080
```

---

### Week 2: Consumer Patterns & Error Handling (Modules 3-4)

**Day 1-3: Module 3 - Advanced Consumer**
- [ ] Read: `docs/KAFKA_CONCEPTS.md` - Consumer Patterns section
- [ ] Complete: `KafkaConsumerConfig.java` challenges (3.1-3.7)
- [ ] Complete: `OrderConsumer.java` challenges
- [ ] Complete: `InventoryConsumer.java` challenges (3.9-3.14)
- [ ] Complete: `NotificationConsumer.java` challenges (3.12-3.14)
- [ ] Complete: `PaymentConsumer.java` challenges (3.8)

**Day 4-7: Module 4 - Error Handling**
- [ ] Read: `docs/KAFKA_CONCEPTS.md` - Error Handling section
- [ ] Complete: `KafkaErrorHandlingConfig.java` challenges (4.1-4.8)
- [ ] Complete: `PaymentConsumer.java` challenges (4.9-4.10)
- [ ] Complete: `NotificationConsumer.java` challenges (4.11)

**Testing Retry:**
```bash
# Simulate failures by modifying PaymentService
# Watch retry attempts in logs
# Check DLT topic in Kafka UI after max retries
```

---

### Week 3: Event-Driven Architecture (Module 5)

**Day 1-3: Outbox Pattern**
- [ ] Read: `docs/KAFKA_CONCEPTS.md` - Outbox Pattern section
- [ ] Complete: `OutboxEvent.java` challenges (5.6-5.10)
- [ ] Complete: `OutboxPublisher.java` challenges (5.11-5.14)
- [ ] Complete: `OutboxRepository.java` challenges (5.15-5.17)

**Day 4-7: Saga Pattern**
- [ ] Read: `docs/KAFKA_CONCEPTS.md` - Saga Pattern section
- [ ] Complete: `OrderSagaOrchestrator.java` challenges (5.18-5.23)
- [ ] Read: `docs/ARCHITECTURE.md` - Saga Flow section

**Testing:**
```bash
# Watch outbox_events table in H2 Console
# http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:kafkadb

# Observe saga flow in logs
```

---

### Week 4: Monitoring & Testing (Modules 6-7)

**Day 1-2: Module 6 - Monitoring**
- [ ] Read: `docs/KAFKA_CONCEPTS.md` - Monitoring section
- [ ] Complete: `KafkaMonitoringConfig.java` challenges (6.2-6.7)
- [ ] Access metrics: http://localhost:8080/actuator/prometheus

**Day 3-7: Module 7 - Testing**
- [ ] Complete: `OrderIntegrationTest.java` challenges (7.1-7.6)
- [ ] Complete: `PaymentIntegrationTest.java` challenges (7.7-7.11)
- [ ] Complete: `KafkaContainerTest.java` challenges (7.12-7.17)

**Run Tests:**
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=OrderIntegrationTest

# Run with coverage
mvn clean test jacoco:report
```

---

## 🎯 Challenge Completion Checklist

### Module 1: Kafka Fundamentals
- [ ] Challenge 1.1-1.5: Order model
- [ ] Challenge 1.6-1.8: Payment model
- [ ] Challenge 1.9-1.12: Inventory model
- [ ] Challenge 1.13-1.16: Notification model
- [ ] Challenge 1.17-1.24: DTOs
- [ ] Challenge 1.25-1.31: OrderProducer
- [ ] Challenge 1.32-1.34: OrderService
- [ ] Challenge 1.35-1.37: OrderController
- [ ] Challenge 1.38-1.44: OrderConsumer
- [ ] Challenge 1.45-1.51: Services

### Module 2: Advanced Producer
- [ ] Challenge 2.1-2.4: TopicConfig
- [ ] Challenge 2.5-2.11: ProducerConfig
- [ ] Challenge 2.12-2.14: PaymentProducer
- [ ] Challenge 2.15-2.16: InventoryProducer
- [ ] Challenge 2.17-2.18: NotificationProducer

### Module 3: Advanced Consumer
- [ ] Challenge 3.1-3.7: ConsumerConfig
- [ ] Challenge 3.8: PaymentConsumer
- [ ] Challenge 3.9-3.11: InventoryConsumer
- [ ] Challenge 3.12-3.14: NotificationConsumer

### Module 4: Error Handling
- [ ] Challenge 4.1-4.8: ErrorHandlingConfig
- [ ] Challenge 4.9-4.10: PaymentConsumer retry
- [ ] Challenge 4.11: NotificationConsumer error handler

### Module 5: Event-Driven Architecture
- [ ] Challenge 5.6-5.10: OutboxEvent
- [ ] Challenge 5.11-5.14: OutboxPublisher
- [ ] Challenge 5.15-5.17: OutboxRepository
- [ ] Challenge 5.18-5.23: OrderSagaOrchestrator

### Module 6: Monitoring
- [ ] Challenge 6.2-6.7: MonitoringConfig

### Module 7: Testing
- [ ] Challenge 7.1-7.6: OrderIntegrationTest
- [ ] Challenge 7.7-7.11: PaymentIntegrationTest
- [ ] Challenge 7.12-7.17: KafkaContainerTest

---

## 📖 Daily Study Routine

### Morning (1 hour)
1. Read theory from `docs/KAFKA_CONCEPTS.md`
2. Review challenges for the day
3. Understand the "why" behind each challenge

### Afternoon (1-2 hours)
1. Implement challenges
2. Don't look at solutions immediately!
3. Struggle with the problem (that's how you learn)

### Evening (30 minutes)
1. Compare your solution with provided solutions
2. Test your implementation
3. Review what you learned
4. Note down questions for next day

---

## 🏆 Milestone Projects

### Milestone 1: Basic Order Flow (End of Week 1)
**Goal:** Create and process orders through Kafka

**Test:**
```bash
# Create order via REST API
# Verify message appears in Kafka UI
# Verify consumer processes message
# Verify order status changes
```

### Milestone 2: Error Handling (End of Week 2)
**Goal:** Implement retry and DLT

**Test:**
```bash
# Simulate payment failure
# Watch retry attempts in logs
# Verify message goes to DLT after max retries
# Check DLT topic in Kafka UI
```

### Milestone 3: Saga Pattern (End of Week 3)
**Goal:** Implement distributed transaction

**Test:**
```bash
# Create order
# Watch saga flow: Order → Payment → Inventory → Notification
# Simulate inventory failure
# Watch compensation: Refund payment
```

### Milestone 4: Complete System (End of Week 4)
**Goal:** Full event-driven e-commerce platform

**Test:**
```bash
# Run all integration tests
# Monitor metrics in Prometheus
# Verify all challenges completed
```

---

## 💡 Tips for Success

### Do's ✅
- Try to solve challenges BEFORE looking at solutions
- Read error messages carefully
- Use Kafka UI to inspect messages
- Test after each challenge
- Take breaks when stuck
- Review KAFKA_CONCEPTS.md when confused

### Don'ts ❌
- Don't copy-paste solutions without understanding
- Don't skip challenges
- Don't rush (quality over speed)
- Don't ignore error messages
- Don't forget to commit your progress

---

## 🆘 Getting Help

### When Stuck:
1. **Read the error message** - It usually tells you what's wrong
2. **Check the hints** - Each challenge has hints
3. **Review KAFKA_CONCEPTS.md** - Theory often explains the solution
4. **Check TROUBLESHOOTING.md** - Common issues and solutions
5. **Look at the solution** - But try to understand, not copy

### Common Roadblocks:

**"Messages not appearing in Kafka UI"**
- Check topic name matches
- Verify Kafka is running: `docker-compose ps`
- Check producer logs for errors

**"Consumer not receiving messages"**
- Verify groupId is correct
- Check auto-offset-reset setting
- Try resetting consumer offsets

**"JSON deserialization error"**
- Set `spring.json.trusted.packages=*`
- Verify model class exists in consumer

---

## 📊 Progress Tracking

Copy this section and update daily:

```
Week 1:
  Day 1: [ ] Module 1 started
  Day 2: [ ] Order model complete
  Day 3: [ ] Payment/Inventory models complete
  Day 4: [ ] Notification model complete
  Day 5: [ ] Producer challenges complete
  Day 6: [ ] Consumer challenges complete
  Day 7: [ ] Week 1 review

Week 2:
  Day 1: [ ] Module 3 started
  Day 2: [ ] ConsumerConfig complete
  Day 3: [ ] All consumers complete
  Day 4: [ ] Module 4 started
  Day 5: [ ] ErrorHandlingConfig complete
  Day 6: [ ] Retry patterns complete
  Day 7: [ ] Week 2 review

Week 3:
  Day 1: [ ] Module 5 started
  Day 2: [ ] Outbox pattern complete
  Day 3: [ ] Outbox testing
  Day 4: [ ] Saga pattern started
  Day 5: [ ] Saga orchestrator complete
  Day 6: [ ] Saga compensation complete
  Day 7: [ ] Week 3 review

Week 4:
  Day 1: [ ] Module 6 started
  Day 2: [ ] Monitoring complete
  Day 3: [ ] Module 7 started
  Day 4: [ ] OrderIntegrationTest complete
  Day 5: [ ] PaymentIntegrationTest complete
  Day 6: [ ] KafkaContainerTest complete
  Day 7: [ ] Project complete! 🎉
```

---

## 🎓 What You'll Know After Completing

After completing this learning path, you will be able to:

✅ Design and implement Kafka producers and consumers  
✅ Configure Kafka for production use  
✅ Implement retry patterns with exponential backoff  
✅ Handle errors with Dead Letter Topics  
✅ Implement Outbox Pattern for reliable events  
✅ Implement Saga Pattern for distributed transactions  
✅ Monitor Kafka with Micrometer and Prometheus  
✅ Write comprehensive integration tests  
✅ Debug Kafka issues in production  
✅ Design event-driven microservices architectures  

---

## 🚀 Next Steps After Completion

1. **Build Your Own Project**
   - Apply what you learned to a real project
   - Start small, iterate quickly

2. **Explore Advanced Topics**
   - Kafka Streams for stream processing
   - KSQL for querying streams
   - Schema Registry with Avro

3. **Learn Related Technologies**
   - Apache Flink for complex event processing
   - Redis for caching and state
   - Elasticsearch for event sourcing

4. **Contribute Back**
   - Share your learnings
   - Help others on Stack Overflow
   - Contribute to open source

---

**Happy Learning! 🎉**

Remember: The key to mastery is consistent practice. Code every day, even if it's just for 30 minutes!
