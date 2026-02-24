# 🚀 Kafka Mastery Project

**Master Apache Kafka in Spring Boot through hands-on challenges and real-world e-commerce scenarios**

---

## 📖 Table of Contents

- [Overview](#overview)
- [What You'll Learn](#what-youll-learn)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Learning Modules](#learning-modules)
- [Architecture](#architecture)
- [Development Workflow](#development-workflow)
- [Troubleshooting](#troubleshooting)
- [Resources](#resources)

---

## 📋 Overview

This is a **comprehensive, hands-on learning project** designed to take you from Kafka beginner to production-ready developer. You'll build an **event-driven e-commerce platform** with real-world patterns used by companies worldwide.

### 🎯 Project Philosophy

> **"Don't just read about Kafka - build with it, break it, debug it, master it."**

Every module contains **deliberately incomplete code** with challenges marked by `TODO` comments. Solutions are provided but **dimmed with comments** - try solving them yourself first!

---

## 🎓 What You'll Learn

### Core Kafka Concepts
- ✅ Producers, Consumers, Topics, Partitions
- ✅ Consumer Groups & Offset Management
- ✅ Serialization/Deserialization (JSON, Avro)
- ✅ Kafka Transactions

### Advanced Patterns
- ✅ **Retry Topics** with exponential backoff
- ✅ **Dead Letter Topics (DLT)** for failed messages
- ✅ **Saga Pattern** for distributed transactions
- ✅ **Outbox Pattern** for reliable event publishing
- ✅ **Idempotency** handling

### Production-Ready Skills
- ✅ Error handling strategies
- ✅ Monitoring & observability
- ✅ Performance tuning
- ✅ Testing with EmbeddedKafka & Testcontainers

---

## 🛠️ Prerequisites

### Required
- **Java 21+** (latest LTS version)
- **Maven 3.8+** or Gradle 8+
- **Docker Desktop** (for Kafka infrastructure)
- **IDE** (IntelliJ IDEA recommended)

### Recommended
- Basic Spring Boot knowledge
- Understanding of microservices architecture
- Familiarity with REST APIs

---

## 🚀 Quick Start

### Step 1: Clone and Setup

```bash
# Navigate to project directory
cd Learning-Kafka

# Start Kafka infrastructure (takes 2-3 minutes first time)
docker-compose up -d

# Verify all containers are running
docker-compose ps
```

### Step 2: Verify Infrastructure

Access these URLs in your browser:

| Service | URL | Purpose |
|---------|-----|---------|
| **Kafka UI** | http://localhost:8080 | Manage topics, view messages |
| **H2 Console** | http://localhost:8080/h2-console | View outbox tables |
| **Actuator** | http://localhost:8080/actuator | Health & metrics |
| **Prometheus Metrics** | http://localhost:8080/actuator/prometheus | Kafka metrics |

**H2 Console Login:**
- JDBC URL: `jdbc:h2:mem:kafkadb`
- Username: `sa`
- Password: (leave empty)

### Step 3: Run the Application

```bash
# Build and run
mvn clean spring-boot:run

# Or build JAR first
mvn clean package
java -jar target/kafka-mastery-project-1.0.0.jar
```

### Step 4: Start Learning!

1. Open the project in your IDE
2. Navigate to `src/main/java/com/learning/kafka`
3. Start with **Module 1** in the `consumer` and `producer` packages
4. Complete challenges in order
5. Use Kafka UI to inspect messages

---

## 📁 Project Structure

```
Learning-Kafka/
├── docker-compose.yml              # Kafka, Zookeeper, Schema Registry, UI
├── pom.xml                         # Maven dependencies
├── README.md                       # This file
├── ARCHITECTURE.md                 # System design diagrams
├── KAFKA_CONCEPTS.md               # Theoretical background
├── CHEATSHEET.md                   # Quick reference guide
├── TROUBLESHOOTING.md              # Common issues & solutions
│
├── src/main/java/com/learning/kafka/
│   ├── KafkaMasteryApplication.java
│   │
│   ├── config/                     # Kafka configurations
│   │   ├── KafkaConfig.java
│   │   ├── KafkaProducerConfig.java
│   │   ├── KafkaConsumerConfig.java
│   │   ├── KafkaErrorHandlingConfig.java
│   │   └── KafkaMonitoringConfig.java
│   │
│   ├── model/                      # Domain models
│   │   ├── Order.java
│   │   ├── Payment.java
│   │   ├── Inventory.java
│   │   └── Notification.java
│   │
│   ├── dto/                        # Data Transfer Objects
│   │   ├── OrderRequest.java
│   │   ├── OrderResponse.java
│   │   └── ErrorResponse.java
│   │
│   ├── producer/                   # Kafka Producers (Module 1-2)
│   │   ├── OrderProducer.java
│   │   ├── PaymentProducer.java
│   │   └── NotificationProducer.java
│   │
│   ├── consumer/                   # Kafka Consumers (Module 1-4)
│   │   ├── OrderConsumer.java
│   │   ├── PaymentConsumer.java
│   │   ├── InventoryConsumer.java
│   │   └── NotificationConsumer.java
│   │
│   ├── service/                    # Business logic
│   │   ├── OrderService.java
│   │   ├── PaymentService.java
│   │   ├── InventoryService.java
│   │   └── NotificationService.java
│   │
│   ├── controller/                 # REST controllers
│   │   └── OrderController.java
│   │
│   ├── saga/                       # Saga pattern (Module 5)
│   │   ├── OrderSagaOrchestrator.java
│   │   └── compensating/
│   │
│   ├── outbox/                     # Outbox pattern (Module 5)
│   │   ├── OutboxEvent.java
│   │   └── OutboxPublisher.java
│   │
│   └── exception/                  # Custom exceptions
│       ├── RetryableException.java
│       └── NonRetryableException.java
│
└── src/test/java/com/learning/kafka/  # Integration tests (Module 7)
    ├── OrderIntegrationTest.java
    ├── PaymentIntegrationTest.java
    └── TestcontainersKafkaTest.java
```

---

## 📚 Learning Modules

### **Module 1: Kafka Fundamentals** ⭐ Beginner
**Location:** `producer/OrderProducer.java`, `consumer/OrderConsumer.java`

**Topics:**
- Creating your first producer
- Creating your first consumer
- Topic auto-creation
- JSON message serialization
- Synchronous vs async sending

**Challenges:** 5 exercises  
**Estimated Time:** 2-3 hours

---

### **Module 2: Advanced Producer Patterns** ⭐⭐ Intermediate
**Location:** `producer/PaymentProducer.java`, `config/KafkaProducerConfig.java`

**Topics:**
- Async sending with callbacks
- Producer acknowledgments
- Batching optimization
- Compression
- Transactional producers

**Challenges:** 6 exercises  
**Estimated Time:** 3-4 hours

---

### **Module 3: Advanced Consumer Patterns** ⭐⭐ Intermediate
**Location:** `consumer/InventoryConsumer.java`, `config/KafkaConsumerConfig.java`

**Topics:**
- Manual partition assignment
- Consumer groups
- Manual offset commits
- Concurrent consumers
- Message filtering

**Challenges:** 6 exercises  
**Estimated Time:** 3-4 hours

---

### **Module 4: Error Handling & Retry** ⭐⭐⭐ Advanced
**Location:** `consumer/PaymentConsumer.java`, `config/KafkaErrorHandlingConfig.java`

**Topics:**
- Default error handlers
- **Retry Topics** (non-blocking retry)
- **Dead Letter Topics (DLT)**
- Exception classification
- Exponential backoff

**Challenges:** 8 exercises  
**Estimated Time:** 4-5 hours

---

### **Module 5: Event-Driven Architecture** ⭐⭐⭐ Advanced
**Location:** `saga/`, `outbox/`, `service/OrderService.java`

**Topics:**
- **Outbox Pattern** (reliable events)
- **Saga Pattern** (distributed transactions)
- Compensating transactions
- Idempotency handling
- Event choreography vs orchestration

**Challenges:** 10 exercises  
**Estimated Time:** 6-8 hours

---

### **Module 6: Monitoring & Observability** ⭐⭐ Intermediate
**Location:** `config/KafkaMonitoringConfig.java`

**Topics:**
- Consumer lag monitoring
- Producer metrics
- JMX exposure
- Custom Micrometer metrics
- Distributed tracing

**Challenges:** 4 exercises  
**Estimated Time:** 2-3 hours

---

### **Module 7: Testing Strategies** ⭐⭐ Intermediate
**Location:** `src/test/java/`

**Topics:**
- Unit testing with mocks
- Integration testing with `@EmbeddedKafka`
- Testcontainers for Kafka
- Testing retry scenarios
- Testing DLT flows

**Challenges:** 5 exercises  
**Estimated Time:** 3-4 hours

---

## 🏗️ Architecture

### E-Commerce Order Flow

```
┌─────────────┐     order-created     ┌─────────────┐
│   Client    │ ────────────────────> │   Order     │
│  (REST API) │                       │  Consumer   │
└─────────────┘                       └──────┬──────┘
                                             │
                                             v
                                    ┌─────────────────┐
                                    │ order-confirmed │
                                    └────────┬────────┘
                                             │
                    ┌────────────────────────┼────────────────────────┐
                    │                        │                        │
                    v                        v                        v
           ┌───────────────┐       ┌────────────────┐       ┌────────────────┐
           │    Payment    │       │   Inventory    │       │  Notification  │
           │   Consumer    │       │    Consumer    │       │    Consumer    │
           └───────┬───────┘       └───────┬────────┘       └───────┬────────┘
                   │                       │                        │
                   v                       v                        v
         payment-processed      inventory-reserved       notification-sent
```

### Retry & Dead Letter Pattern

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│  Original    │───>│   Retry 0    │───>│   Retry 1    │───>│  Dead Letter │
│    Topic     │    │   (1s delay) │    │  (2s delay)  │    │    Topic     │
└──────────────┘    └──────────────┘    └──────────────┘    └──────────────┘
       │                   │                   │                   │
       v                   v                   v                   v
  @KafkaListener     @RetryableTopic     @RetryableTopic      @DltHandler
   (primary)         (attempt 1)         (attempt 2)        (final handler)
```

---

## 💻 Development Workflow

### Recommended Workflow for Each Module

1. **Read the theory** in `KAFKA_CONCEPTS.md` for the module
2. **Open the challenge file** (e.g., `OrderProducer.java`)
3. **Find the `TODO` comments** with challenge descriptions
4. **Try to solve it yourself** (15-30 min per challenge)
5. **Test your solution** by running the app and using Kafka UI
6. **Compare with solution** (commented at bottom of file)
7. **Move to next challenge**

### Testing Your Solutions

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=OrderIntegrationTest

# Run with coverage
mvn clean test jacoco:report
```

### Using Kafka UI

1. Go to http://localhost:8080
2. Click on your cluster (local-cluster)
3. View topics, messages, consumer groups
4. Produce test messages from the UI
5. Monitor consumer lag

---

## 🐛 Troubleshooting

### Common Issues

| Issue | Solution |
|-------|----------|
| **Kafka container won't start** | Check ports 9092, 2181, 8080 are not in use |
| **Consumer not receiving messages** | Verify `bootstrap-servers` and `group-id` |
| **JSON deserialization error** | Check `spring.json.trusted.packages` |
| **Topic not auto-created** | Set `auto.create.topics.enable=true` |
| **Connection refused to Kafka** | Use `PLAINTEXT_HOST` listener on localhost:9092 |

See **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** for detailed solutions.

---

## 📖 Additional Resources

### Documentation
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - System design and diagrams
- **[KAFKA_CONCEPTS.md](KAFKA_CONCEPTS.md)** - Kafka theory and concepts
- **[CHEATSHEET.md](CHEATSHEET.md)** - Quick reference for annotations
- **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** - Common issues

### External Resources
- [Spring Kafka Documentation](https://docs.spring.io/spring-kafka/reference/)
- [Apache Kafka Official Docs](https://kafka.apache.org/documentation/)
- [Confluent Kafka Tutorials](https://developer.confluent.io/quickstart/kafka-spring/)

---

## 🎯 Success Criteria

You'll know you've mastered Kafka when you can:

- ✅ Design and implement producers/consumers from scratch
- ✅ Handle errors with retry and DLT patterns
- ✅ Implement Saga/Outbox for distributed transactions
- ✅ Monitor consumer lag and optimize performance
- ✅ Write comprehensive tests for Kafka integrations
- ✅ Debug production Kafka issues confidently

---

## 🚀 Ready to Start?

```bash
# Start Kafka
docker-compose up -d

# Run application
mvn spring-boot:run

# Open Kafka UI
open http://localhost:8080

# Start with Module 1!
```

**Happy Learning! 🎓**

---

## 📝 License

This project is for educational purposes. Feel free to use it for your learning journey!
