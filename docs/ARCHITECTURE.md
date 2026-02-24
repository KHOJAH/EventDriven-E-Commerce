# 🏗️ Architecture Documentation

System design and architecture for the Kafka Mastery Project.

---

## Table of Contents

1. [System Overview](#system-overview)
2. [Architecture Diagram](#architecture-diagram)
3. [Data Flow](#data-flow)
4. [Service Architecture](#service-architecture)
5. [Event Flow](#event-flow)
6. [Deployment Architecture](#deployment-architecture)

---

## System Overview

### E-Commerce Order Processing System

This project implements an **event-driven e-commerce platform** using Apache Kafka and Spring Boot.

**Core Business Process:**
```
Customer places order → Payment processed → Inventory reserved → Notification sent → Order shipped
```

### Key Architectural Decisions

| Decision | Rationale |
|----------|-----------|
| **Event-Driven Architecture** | Loose coupling, scalability, resilience |
| **Apache Kafka** | High throughput, durability, replayability |
| **Microservices** | Independent deployment, technology diversity |
| **Saga Pattern** | Distributed transactions without 2PC |
| **Outbox Pattern** | Reliable event publishing |

---

## Architecture Diagram

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         Client Layer                                 │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐                       │
│  │   Web     │  │  Mobile   │  │   Admin   │                       │
│  │   App     │  │    App    │  │  Portal   │                       │
│  └─────┬─────┘  └─────┬─────┘  └─────┬─────┘                       │
│        │              │              │                               │
│        └──────────────┴──────────────┘                               │
│                       │                                              │
│                  REST API                                            │
└───────────────────────┼──────────────────────────────────────────────┘
                        │
┌───────────────────────▼──────────────────────────────────────────────┐
│                      API Gateway                                      │
│                  (Spring Boot REST)                                   │
│                        │                                              │
└────────────────────────┼──────────────────────────────────────────────┘
                         │
┌────────────────────────▼──────────────────────────────────────────────┐
│                     Order Service                                     │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │  OrderController → OrderService → OrderProducer             │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                        │                                              │
│                   Kafka: order-created                               │
└────────────────────────┼──────────────────────────────────────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ▼                ▼                ▼
┌───────────────┐ ┌───────────────┐ ┌───────────────┐
│   Payment     │ │   Inventory   │ │  Notification │
│   Service     │ │    Service    │ │    Service    │
│               │ │               │ │               │
│  @KafkaListener│ │  @KafkaListener│ │  @KafkaListener│
│  processPayment│ │ reserveInventory│ │ sendEmail     │
│               │ │               │ │               │
│  payment-     │ │  inventory-   │ │  notification-│
│  processed    │ │  reserved     │ │  email        │
└───────┬───────┘ └───────┬───────┘ └───────┬───────┘
        │                 │                 │
        └────────────────┼─────────────────┘
                         │
              ┌──────────▼──────────┐
              │  Saga Orchestrator  │
              │                     │
              │  Coordinates flow   │
              │  Handles failures   │
              │  (Compensation)     │
              └─────────────────────┘
```

---

## Data Flow

### Order Creation Flow

```
┌─────────┐     ┌───────────┐     ┌──────────┐     ┌──────────┐
│ Client  │────>│   Order   │────>│  Kafka   │────>│ Payment  │
│         │     │  Service  │     │  Topics  │     │ Service  │
└─────────┘     └───────────┘     └──────────┘     └──────────┘
   │                │                  │                  │
   │ POST /orders   │                  │                  │
   │───────────────>│                  │                  │
   │                │                  │                  │
   │                │ order-created    │                  │
   │                │─────────────────>│                  │
   │                │                  │                  │
   │                │                  │ consume          │
   │                │                  │─────────────────>│
   │                │                  │                  │
   │                │                  │                  │ Process payment
   │                │                  │                  │
   │                │                  │                  │ payment-processed
   │                │                  │<─────────────────│
   │                │                  │                  │
   │                │ consume          │                  │
   │                │<─────────────────│                  │
   │                │                  │                  │
   │ 201 Created   │                  │                  │
   │<───────────────│                  │                  │
   │                │                  │                  │
```

### Sequence Diagram: Successful Order

```
Client      OrderService    Kafka       Payment     Inventory   Notification
  │            │             │             │            │            │
  │──POST─────>│             │             │            │            │
  │            │             │             │            │            │
  │            │order-created│             │            │            │
  │            │────────────>│             │            │            │
  │            │             │             │            │            │
  │            │             │  consume    │            │            │
  │            │             │────────────>│            │            │
  │            │             │             │            │            │
  │            │             │payment-proc │            │            │
  │            │             │────────────>│            │            │
  │            │             │             │            │            │
  │            │             │ consume     │            │            │
  │            │             │<────────────│            │            │
  │            │             │             │            │            │
  │            │             │payment-proc │            │            │
  │            │             │────────────>│            │            │
  │            │             │             │            │            │
  │            │             │ consume     │            │            │
  │            │             │─────────────────────────>│            │
  │            │             │             │            │            │
  │            │             │inventory-rsv│            │            │
  │            │             │─────────────────────────>│            │
  │            │             │             │            │            │
  │            │             │ consume     │            │            │
  │            │             │<─────────────────────────│            │
  │            │             │             │            │            │
  │            │             │inventory-rsv│            │            │
  │            │             │─────────────────────────>│            │
  │            │             │             │            │            │
  │            │             │ consume     │            │            │
  │            │             │─────────────────────────────────────>│
  │            │             │             │            │            │
  │            │             │notification │            │            │
  │            │             │─────────────────────────────────────>│
  │            │             │             │            │            │
  │<───────────│             │             │            │            │
  │ 201 Created│             │             │            │            │
  │            │             │             │            │            │
```

---

## Service Architecture

### Order Service

**Responsibilities:**
- Accept order requests via REST API
- Validate order data
- Publish order-created events
- Track order status

**Components:**
```
OrderController (REST Layer)
    ↓
OrderService (Business Logic)
    ↓
OrderProducer (Kafka Producer)
```

**Database Schema:**
```sql
CREATE TABLE orders (
    order_id VARCHAR(36) PRIMARY KEY,
    customer_id VARCHAR(36) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    items TEXT,
    shipping_address TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    correlation_id VARCHAR(36),
    idempotency_key VARCHAR(100)
);
```

### Payment Service

**Responsibilities:**
- Process payments
- Handle payment failures
- Publish payment events
- Support refunds (compensation)

**Components:**
```
PaymentConsumer (Kafka Consumer)
    ↓
PaymentService (Business Logic)
    ↓
PaymentProducer (Kafka Producer)
```

**State Machine:**
```
PENDING → PROCESSING → COMPLETED
                    ↓
                  FAILED
```

### Inventory Service

**Responsibilities:**
- Reserve inventory for orders
- Release reservations (on failure)
- Track stock levels
- Handle reservation timeouts

**Components:**
```
InventoryConsumer (Kafka Consumer)
    ↓
InventoryService (Business Logic)
    ↓
InventoryProducer (Kafka Producer)
```

### Notification Service

**Responsibilities:**
- Send email notifications
- Send SMS notifications
- Handle notification failures
- Retry failed notifications

**Components:**
```
NotificationConsumer (Kafka Consumer)
    ↓
NotificationService (Business Logic)
    ↓
NotificationProducer (Kafka Producer)
```

---

## Event Flow

### Topic Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Order Events                             │
│  order-created → order-confirmed → order-cancelled          │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                    Payment Events                            │
│  payment-process → payment-processed → payment-failed       │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                   Inventory Events                           │
│  inventory-reservation → inventory-reserved → inventory-released
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                  Notification Events                         │
│  notification-email → notification-sent                     │
│  notification-sms → notification-sent                       │
└─────────────────────────────────────────────────────────────┘
```

### Event Schema

**Order Created Event:**
```json
{
  "orderId": "ORD-123456",
  "customerId": "CUST-789",
  "customerEmail": "customer@example.com",
  "totalAmount": 99.99,
  "status": "PENDING",
  "items": "ITEM1,ITEM2,ITEM3",
  "shippingAddress": "123 Main St, City, State 12345",
  "createdAt": "2025-02-25T10:30:00Z",
  "correlationId": "CORR-ABC123",
  "idempotencyKey": "ORDER_ORD-123456_1708851000000"
}
```

---

## Deployment Architecture

### Local Development (Docker Compose)

```
┌─────────────────────────────────────────────────────────────┐
│                   Docker Host                                │
│                                                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Kafka     │  │  Zookeeper  │  │SchemaRegistry│        │
│  │  :9092      │  │  :2181      │  │   :8081     │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│                                                              │
│  ┌─────────────┐  ┌─────────────┐                          │
│  │  Kafka UI   │  │Kafka Connect│                          │
│  │  :8080      │  │   :8083     │                          │
│  └─────────────┘  └─────────────┘                          │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐   │
│  │           Spring Boot Application                    │   │
│  │           (Your Code)                                │   │
│  │           :8080 (REST)                               │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Production Deployment (Kubernetes)

```
┌─────────────────────────────────────────────────────────────┐
│                  Kubernetes Cluster                          │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Kafka Cluster (3 brokers)                │   │
│  │              (StatefulSet)                            │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Order     │  │   Payment   │  │  Inventory  │        │
│  │  Service    │  │   Service   │  │   Service   │        │
│  │  (Deploy)   │  │  (Deploy)   │  │  (Deploy)   │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│                                                              │
│  ┌─────────────┐  ┌─────────────┐                          │
│  │Notification │  │    API      │                          │
│  │   Service   │  │   Gateway   │                          │
│  │  (Deploy)   │  │  (Ingress)  │                          │
│  └─────────────┘  └─────────────┘                          │
└─────────────────────────────────────────────────────────────┘
```

---

## Scaling Considerations

### Horizontal Scaling

**Order Service:**
- Scale based on CPU/Memory
- Stateless (session in Redis)
- Multiple instances behind load balancer

**Kafka Consumers:**
- Scale by increasing consumer instances
- Limited by number of partitions
- Use consumer groups for parallelism

### Partition Strategy

```
# Recommended partition count:
order-created: 6 partitions (scale to 6 consumers)
payment-processed: 6 partitions
inventory-reserved: 6 partitions
notification-email: 3 partitions
```

**Rule of Thumb:**
- Start with 3-6 partitions
- Can increase but not decrease
- Plan for 10x traffic growth

---

## Resilience Patterns

### Retry Pattern

```
Consumer receives message
    ↓
Processing fails (temporary error)
    ↓
Retry after 1s (Retry Topic 0)
    ↓
Still fails
    ↓
Retry after 2s (Retry Topic 1)
    ↓
Still fails
    ↓
Send to Dead Letter Topic
    ↓
Alert operations team
```

### Circuit Breaker (Future Enhancement)

```java
// Future: Add Resilience4j circuit breaker
@CircuitBreaker(name = "paymentService", fallbackMethod = "fallback")
public Payment processPayment(Order order) {
    return paymentService.process(order);
}
```

### Bulkhead Pattern

```java
// Separate thread pools for different operations
@Bulkhead(name = "kafkaConsumer", type = Bulkhead.Type.THREADPOOL)
public void consumeMessage(Order order) {
    // Process message
}
```

---

## Security Considerations

### Kafka Security

1. **SSL/TLS**: Encrypt data in transit
2. **SASL**: Authentication (SCRAM-SHA-256)
3. **ACLs**: Authorization for topics
4. **Quotas**: Prevent abuse

### Data Security

1. **PII Encryption**: Encrypt customer data
2. **Tokenization**: Use tokens instead of card numbers
3. **Audit Logging**: Log all access
4. **Data Retention**: Configure topic retention

---

## Monitoring Strategy

### Key Metrics

| Metric | Threshold | Alert |
|--------|-----------|-------|
| Consumer Lag | > 1000 | Warning |
| Consumer Lag | > 5000 | Critical |
| Producer Latency | > 100ms | Warning |
| DLT Message Count | > 0 | Warning |
| Error Rate | > 1% | Critical |

### Logging

```
Format: JSON
Level: INFO (production), DEBUG (development)
Aggregation: ELK Stack or Splunk
```

### Tracing

```
Correlation ID: Passed through all services
Tool: Zipkin, Jaeger, or AWS X-Ray
```

---

Happy Building! 🚀
