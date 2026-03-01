# Event Flow Diagrams - E-Commerce Order Processing

This document provides detailed Mermaid flow diagrams for the Kafka-based e-commerce order processing system with Spring Boot.

**Last Updated:** March 2026

---

## Table of Contents

1. [Architecture Overview](#1-architecture-overview)
2. [Order Service Flow](#2-order-service-flow)
3. [Payment Service Flow](#3-payment-service-flow)
4. [Inventory Service Flow](#4-inventory-service-flow)
5. [Notification Service Flow](#5-notification-service-flow)
6. [Complete End-to-End Flow](#6-complete-end-to-end-flow)
7. [Saga Orchestrator Flow](#7-saga-orchestrator-flow)
8. [Error Handling Flows](#8-error-handling-flows)

---

## 1. Architecture Overview

### 1.1 Consumer Modes

The application supports two consumer modes configurable via `application.yml`:

| Mode | Description | Active Components |
|------|-------------|-------------------|
| **standard** (default) | Independent consumers for each processing stage | `OrderConsumer`, `PaymentConsumer`, `InventoryConsumer`, `NotificationConsumer` |
| **saga** | Central orchestrator coordinates distributed transactions | `OrderSagaOrchestrator` |

### 1.2 Topic Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Kafka Topics                              │
├─────────────────────────────────────────────────────────────────┤
│ Order Events:                                                    │
│   • order-created (3 partitions)                                 │
│   • order-confirmed (3 partitions)                               │
│   • order-cancelled (3 partitions)                               │
│   • order-failed (3 partitions)                                  │
│                                                                  │
│ Payment Events:                                                  │
│   • payment-processed (3 partitions)                             │
│   • payment-failed (3 partitions)                                │
│                                                                  │
│ Inventory Events:                                                │
│   • inventory-reservation (3 partitions)                         │
│   • inventory-reserved (3 partitions)                            │
│   • inventory-released (3 partitions)                            │
│                                                                  │
│ Notification Events:                                             │
│   • notification-email (3 partitions)                            │
│   • notification-sms (3 partitions)                              │
│                                                                  │
│ Dead Letter Topics:                                              │
│   • order-created-dlt                                            │
│   • payment-processed-dlt                                        │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. Order Service Flow

### 2.1 Order Creation - REST to Kafka

```mermaid
flowchart TB
    subgraph "Client Layer"
        A[POST /api/orders]
    end

    subgraph "Order Service"
        B[OrderController]
        C[OrderService.createOrder]
        D[OrderProducer]
    end

    subgraph "Kafka Broker"
        E[(order-created<br/>topic)]
    end

    subgraph "Event Structure"
        F[Order Event]
        F1[orderId: String]
        F2[customerId: String]
        F3[customerEmail: String]
        F4[totalAmount: Decimal]
        F5[status: PENDING]
        F6[correlationId: String]
        F7[idempotencyKey: String]
        F --- F1 & F2 & F3 & F4 & F5 & F6 & F7
    end

    A --> B
    B --> C
    C --> D
    D --> E
    D --> F

    style A fill:#90EE90
    style B fill:#87CEEB
    style C fill:#87CEEB
    style D fill:#FFB6C1
    style E fill:#DDA0DD
    style F fill:#FFE4B5
```

### 2.2 Order Consumer - Processing Order Created Events

```mermaid
flowchart TD
    A[(order-created<br/>topic)] -->|consume| B[OrderConsumer<br/>processOrderCreated]

    B --> C{Duplicate Check<br/>idempotencyKey}
    C -->|Yes| D[Log Warning & Acknowledge]
    C -->|No| E[OrderService.processOrder]

    E --> F{Order Valid?}
    F -->|No - Amount < 50| G[Cancel Order]
    F -->|Yes| H[PaymentService.processPayment]

    G --> I[Publish order-cancelled]
    H --> J[Publish payment-processed]

    I --> K[Acknowledge & Add to processedKeys]
    J --> K
    K --> L[Message Committed]

    D --> L

    style A fill:#DDA0DD
    style B fill:#FFE4B5
    style C fill:#FFD700
    style E fill:#87CEEB
    style F fill:#FFD700
    style H fill:#87CEEB
    style K fill:#98FB98
    style L fill:#90EE90
```

### 2.3 Order Consumer - Multiple Event Handlers

```mermaid
flowchart TB
    subgraph "Order Topics"
        T1[(order-created)]
        T2[(order-confirmed)]
        T3[(order-cancelled)]
        T4[(order-failed)]
    end

    subgraph "OrderConsumer"
        H1["@KafkaListener<br/>groupId: order-processor-group"]
        H2["@KafkaListener<br/>groupId: order-notification-group"]
        H3["@KafkaListener<br/>groupId: order-cancellation-group"]
        H4["@KafkaListener<br/>groupId: order-failure-group"]
    end

    T1 --> H1
    T2 --> H2
    T3 --> H3
    T4 --> H4

    H1 --> P1[Process Order + Initiate Payment]
    H2 --> P2[Log for Notification]
    H3 --> P3[Acknowledge Cancellation]
    H4 --> P4[Acknowledge Failure]

    style T1 fill:#DDA0DD
    style T2 fill:#DDA0DD
    style T3 fill:#DDA0DD
    style T4 fill:#DDA0DD
    style H1 fill:#FFE4B5
    style H2 fill:#FFE4B5
    style H3 fill:#FFE4B5
    style H4 fill:#FFE4B5
    style P1 fill:#87CEEB
    style P2 fill:#87CEEB
    style P3 fill:#87CEEB
    style P4 fill:#87CEEB
```

---

## 3. Payment Service Flow

### 3.1 Payment Processing Flow

```mermaid
flowchart TB
    subgraph "Payment Service"
        A[OrderConsumer.processOrderCreated]
        B[PaymentService.processPayment]
        C[PaymentProducer]
    end

    subgraph "Kafka Broker"
        D[(payment-processed<br/>topic)]
        E[(payment-failed<br/>topic)]
    end

    subgraph "Payment Event"
        F[Payment Event]
        F1[paymentId: String]
        F2[orderId: String]
        F3[amount: Decimal]
        F4[status: COMPLETED/FAILED]
        F5[correlationId: String]
        F --- F1 & F2 & F3 & F4 & F5
    end

    A --> B
    B --> C
    C --> D
    C --> E
    C --> F

    style A fill:#FFE4B5
    style B fill:#87CEEB
    style C fill:#FFB6C1
    style D fill:#DDA0DD
    style E fill:#DDA0DD
    style F fill:#FFE4B5
```

### 3.2 Payment Consumer - Payment Processed Flow

```mermaid
flowchart TD
    A[(payment-processed<br/>topic)] -->|consume| B[PaymentConsumer<br/>listenPaymentProcessed]

    B --> C{Duplicate Check<br/>paymentId}
    C -->|Yes| D[Log Warning & Acknowledge]
    C -->|No| E{Payment Status?}

    E -->|COMPLETED| F[Build Order from Payment]
    E -->|FAILED| G[Log Error]

    D --> H[Acknowledge]
    G --> H

    F --> I[OrderEventPublisher<br/>publishInventoryReservationRequest]
    I --> J[(inventory-reservation<br/>topic)]

    I --> K[Add to processedKeys]
    K --> L[Acknowledge]
    L --> M[Message Committed]

    style A fill:#DDA0DD
    style B fill:#FFE4B5
    style C fill:#FFD700
    style E fill:#FFD700
    style F fill:#87CEEB
    style I fill:#FFB6C1
    style J fill:#DDA0DD
    style L fill:#98FB98
    style M fill:#90EE90
```

### 3.3 Payment Consumer - Payment Failed Flow with DLT

```mermaid
flowchart TD
    A[(payment-failed<br/>topic)] -->|consume| B[PaymentConsumer<br/>listenPaymentFailed]

    B --> C{Duplicate Check<br/>paymentId}
    C -->|Yes| D[Log Warning & Acknowledge]
    C -->|No| E[Build Order from Payment]

    D --> F[Acknowledge]

    E --> G[OrderEventPublisher<br/>publishOrderFailed]
    G --> H[(order-failed<br/>topic)]

    G --> I[Add to processedKeys]
    I --> F
    F --> J[Message Committed]

    subgraph "DLT Handler"
        K["@DltHandler<br/>handleDlt"]
        L[Log Payment Details]
        M[Alert Operations]
        K --> L --> M
    end

    H -.->|on failure| K

    style A fill:#DDA0DD
    style B fill:#FFE4B5
    style C fill:#FFD700
    style E fill:#87CEEB
    style G fill:#FFB6C1
    style H fill:#DDA0DD
    style F fill:#98FB98
    style J fill:#90EE90
    style K fill:#FF6B6B
```

---

## 4. Inventory Service Flow

### 4.1 Inventory Reservation Request Flow

```mermaid
flowchart TD
    A[(inventory-reservation<br/>topic)] -->|consume| B[InventoryConsumer<br/>listenInventoryReservation]

    B --> C{Duplicate Check<br/>idempotencyKey}
    C -->|Yes| D[Log Warning & Acknowledge]
    C -->|No| E[InventoryService<br/>reserveInventoryAndPublish]

    D --> F[Acknowledge]

    E --> G{Reservation Success?}
    G -->|Yes| H[InventoryProducer<br/>publishInventoryReserved]
    G -->|No| I[InventoryProducer<br/>publishInventoryReleased]

    H --> J[(inventory-reserved<br/>topic)]
    I --> K[(inventory-released<br/>topic)]

    E --> L[Add to processedKeys]
    L --> M[Acknowledge]
    M --> N[Message Committed]

    style A fill:#DDA0DD
    style B fill:#FFE4B5
    style C fill:#FFD700
    style E fill:#87CEEB
    style H fill:#FFB6C1
    style I fill:#FFB6C1
    style J fill:#DDA0DD
    style K fill:#DDA0DD
    style M fill:#98FB98
    style N fill:#90EE90
```

### 4.2 Inventory Reserved - Order Confirmation Flow

```mermaid
flowchart TD
    A[(inventory-reserved<br/>topic)] -->|consume| B[InventoryConsumer<br/>processInventoryReserved]

    B --> C{Duplicate Check<br/>reservationId}
    C -->|Yes| D[Log Warning & Acknowledge]
    C -->|No| E[Build Order from Inventory]

    D --> F[Acknowledge]

    E --> G[OrderService<br/>confirmOrder]
    G --> H[NotificationService<br/>sendOrderConfirmation]
    H --> I[NotificationEventPublisher<br/>publishEmailNotification]

    I --> J[(notification-email<br/>topic)]

    I --> K[Add to processedKeys]
    K --> L[Acknowledge]
    L --> M[Message Committed]

    style A fill:#DDA0DD
    style B fill:#FFE4B5
    style C fill:#FFD700
    style E fill:#87CEEB
    style G fill:#87CEEB
    style H fill:#87CEEB
    style I fill:#FFB6C1
    style J fill:#DDA0DD
    style L fill:#98FB98
    style M fill:#90EE90
```

### 4.3 Inventory Released - Order Failure Flow

```mermaid
flowchart TD
    A[(inventory-released<br/>topic)] -->|consume| B[InventoryConsumer<br/>processInventoryReleased]

    B --> C{Duplicate Check<br/>reservationId}
    C -->|Yes| D[Log Warning & Acknowledge]
    C -->|No| E[Build Order from Inventory]

    D --> F[Acknowledge]

    E --> G[OrderService<br/>failOrder<br/>with failureReason]
    G --> H[(order-failed<br/>topic)]

    G --> I[Add to processedKeys]
    I --> L[Acknowledge]
    L --> M[Message Committed]

    subgraph "Failure Reasons"
        R1[OUT_OF_STOCK]
        R2[RESERVATION_TIMEOUT]
        R3[INVALID_ITEM]
    end

    E -.-> R1
    E -.-> R2
    E -.-> R3

    style A fill:#DDA0DD
    style B fill:#FFE4B5
    style C fill:#FFD700
    style E fill:#87CEEB
    style G fill:#87CEEB
    style H fill:#DDA0DD
    style L fill:#98FB98
    style M fill:#90EE90
    style R1 fill:#FF6B6B
    style R2 fill:#FF6B6B
    style R3 fill:#FF6B6B
```

---

## 5. Notification Service Flow

### 5.1 Email Notification Flow

```mermaid
flowchart TD
    A[(notification-email<br/>topic)] -->|consume| B[NotificationConsumer<br/>listenEmailNotification]

    B --> C{Duplicate Check<br/>notificationId}
    C -->|Yes| D[Log Warning & Acknowledge]
    C -->|No| E[Log Notification Details]

    D --> F[Acknowledge]

    E --> G[Simulate Send Email<br/>Thread.sleep 100ms]
    G --> H{Success?}

    H -->|Yes| I[Log Success]
    H -->|No| J[Throw Exception]

    I --> K[Add to processedNotifications]
    K --> L[Acknowledge]
    L --> M[Message Committed]

    J --> N[Retry via Spring Kafka<br/>3 attempts with backoff]
    N -->|after retries| O[(notification-email-dlt<br/>topic)]

    style A fill:#DDA0DD
    style B fill:#FFE4B5
    style C fill:#FFD700
    style E fill:#87CEEB
    style G fill:#98FB98
    style I fill:#90EE90
    style L fill:#98FB98
    style M fill:#90EE90
    style O fill:#FF6B6B
```

### 5.2 SMS Notification Flow

```mermaid
flowchart TD
    A[(notification-sms<br/>topic)] -->|consume| B[NotificationConsumer<br/>listenSMSNotification]

    B --> C{Duplicate Check<br/>notificationId}
    C -->|Yes| D[Log Warning & Acknowledge]
    C -->|No| E[Log Notification Details]

    D --> F[Acknowledge]

    E --> G[Simulate Send SMS<br/>Thread.sleep 50ms]
    G --> H{Success?}

    H -->|Yes| I[Log Success]
    H -->|No| J[Throw Exception]

    I --> K[Add to processedNotifications]
    K --> L[Acknowledge]
    L --> M[Message Committed]

    J --> N[Retry via Spring Kafka<br/>3 attempts with backoff]
    N -->|after retries| O[(notification-sms-dlt<br/>topic)]

    style A fill:#DDA0DD
    style B fill:#FFE4B5
    style C fill:#FFD700
    style E fill:#87CEEB
    style G fill:#98FB98
    style I fill:#90EE90
    style L fill:#98FB98
    style M fill:#90EE90
    style O fill:#FF6B6B
```

---

## 6. Complete End-to-End Flow

### 6.1 Happy Path - Standard Consumer Mode

```mermaid
sequenceDiagram
    participant Client
    participant OrderCtrl as OrderController
    participant OrderSvc as OrderService
    participant OrderProd as OrderProducer
    participant OrderTopic as order-created
    participant OrderCons as OrderConsumer
    participant PaymentSvc as PaymentService
    participant PaymentProd as PaymentProducer
    participant PaymentTopic as payment-processed
    participant PaymentCons as PaymentConsumer
    participant OrderEventPub as OrderEventPublisher
    participant InvReservation as inventory-reservation
    participant InvCons as InventoryConsumer
    participant InvSvc as InventoryService
    participant InvProd as InventoryProducer
    participant InvReserved as inventory-reserved
    participant NotifSvc as NotificationService
    participant NotifProd as NotificationProducer
    participant NotifEmail as notification-email
    participant NotifCons as NotificationConsumer

    Client->>OrderCtrl: POST /api/orders
    OrderCtrl->>OrderSvc: createOrder
    OrderSvc->>OrderProd: publishOrderCreated
    OrderProd->>OrderTopic: publish event

    OrderTopic->>OrderCons: consume
    OrderCons->>OrderSvc: processOrder
    OrderCons->>PaymentSvc: processPayment
    PaymentSvc->>PaymentProd: publishPaymentProcessed
    PaymentProd->>PaymentTopic: publish event

    PaymentTopic->>PaymentCons: consume
    PaymentCons->>OrderEventPub: publishInventoryReservationRequest
    OrderEventPub->>InvReservation: publish event

    InvReservation->>InvCons: consume
    InvCons->>InvSvc: reserveInventoryAndPublish
    InvSvc->>InvProd: publishInventoryReserved
    InvProd->>InvReserved: publish event

    InvReserved->>InvCons: consume
    InvCons->>OrderSvc: confirmOrder
    OrderSvc->>OrderProd: publishOrderConfirmed
    InvCons->>NotifSvc: sendOrderConfirmation
    NotifSvc->>NotifProd: publishEmailNotification
    NotifProd->>NotifEmail: publish event

    NotifEmail->>NotifCons: consume
    NotifCons->>NotifCons: Send email

    Note over Client,NotifCons: ✅ Order Complete!
```

### 6.2 Component Architecture Overview

```mermaid
graph TB
    subgraph "Controller Layer 🟢"
        OrderCtrl[OrderController]
    end

    subgraph "Service Layer 🔵"
        OrderSvc[OrderService]
        PaymentSvc[PaymentService]
        InventorySvc[InventoryService]
        NotificationSvc[NotificationService]
    end

    subgraph "Producer Layer 🔴"
        OrderProd[OrderProducer]
        PaymentProd[PaymentProducer]
        InventoryProd[InventoryProducer]
        NotificationProd[NotificationProducer]
    end

    subgraph "Kafka Topics 🟣"
        OrderCreated[(order-created)]
        OrderConfirmed[(order-confirmed)]
        OrderFailed[(order-failed)]
        PaymentProcessed[(payment-processed)]
        PaymentFailed[(payment-failed)]
        InventoryReservation[(inventory-reservation)]
        InventoryReserved[(inventory-reserved)]
        InventoryReleased[(inventory-released)]
        NotificationEmail[(notification-email)]
        NotificationSMS[(notification-sms)]
    end

    subgraph "Consumer Layer 🟠"
        OrderCons[OrderConsumer]
        PaymentCons[PaymentConsumer]
        InventoryCons[InventoryConsumer]
        NotificationCons[NotificationConsumer]
    end

    OrderCtrl --> OrderSvc
    OrderSvc --> OrderProd
    OrderProd --> OrderCreated
    OrderCreated --> OrderCons
    OrderCons --> PaymentSvc
    PaymentSvc --> PaymentProd
    PaymentProd --> PaymentProcessed
    PaymentProcessed --> PaymentCons
    PaymentCons --> OrderProd
    OrderProd --> InventoryReservation
    InventoryReservation --> InventoryCons
    InventoryCons --> InventorySvc
    InventorySvc --> InventoryProd
    InventoryProd --> InventoryReserved
    InventoryProd --> InventoryReleased
    InventoryReserved --> InventoryCons
    InventoryReleased --> InventoryCons
    InventoryCons --> OrderSvc
    InventoryCons --> NotificationSvc
    NotificationSvc --> NotificationProd
    NotificationProd --> NotificationEmail
    NotificationProd --> NotificationSMS
    NotificationEmail --> NotificationCons
    NotificationSMS --> NotificationCons

    style OrderCtrl fill:#90EE90
    style OrderSvc fill:#87CEEB
    style PaymentSvc fill:#87CEEB
    style InventorySvc fill:#87CEEB
    style NotificationSvc fill:#87CEEB
    style OrderProd fill:#FFB6C1
    style PaymentProd fill:#FFB6C1
    style InventoryProd fill:#FFB6C1
    style NotificationProd fill:#FFB6C1
    style OrderCreated fill:#DDA0DD
    style OrderConfirmed fill:#DDA0DD
    style OrderFailed fill:#DDA0DD
    style PaymentProcessed fill:#DDA0DD
    style PaymentFailed fill:#DDA0DD
    style InventoryReservation fill:#DDA0DD
    style InventoryReserved fill:#DDA0DD
    style InventoryReleased fill:#DDA0DD
    style NotificationEmail fill:#DDA0DD
    style NotificationSMS fill:#DDA0DD
    style OrderCons fill:#FFE4B5
    style PaymentCons fill:#FFE4B5
    style InventoryCons fill:#FFE4B5
    style NotificationCons fill:#FFE4B5
```

---

## 7. Saga Orchestrator Flow

### 7.1 Saga Pattern Overview

When `kafka.consumer.mode=saga`, the `OrderSagaOrchestrator` coordinates the entire transaction with compensation logic.

```mermaid
flowchart TD
    A[(order-created<br/>topic)] -->|consume| B[OrderSagaOrchestrator<br/>processOrder]

    B --> C[Saga Step 1:<br/>Process Payment]
    C --> D{Payment Success?}

    D -->|Yes| E[Save Saga State:<br/>PAYMENT_PROCESSED]
    D -->|No| F[Compensate:<br/>Log Failure]

    E --> G[Publish payment-processed]
    G --> H[(payment-processed<br/>topic)]

    H -->|consume| I[OrderSagaOrchestrator<br/>reserveInventory]

    I --> J[Saga Step 2:<br/>Reserve Inventory]
    J --> K{Inventory Available?}

    K -->|Yes| L[Save Saga State:<br/>INVENTORY_RESERVED]
    K -->|No| M[Compensate Payment<br/>Release Inventory]

    L --> N[Publish inventory-reserved]
    N --> O[(inventory-reserved<br/>topic)]

    M --> P[Publish payment-failed]
    P --> Q[(payment-failed<br/>topic)]

    O --> R[Saga Complete:<br/>Order Confirmed]
    Q --> S[Saga Failed:<br/>Compensation Applied]

    style A fill:#DDA0DD
    style B fill:#FFE4B5
    style C fill:#87CEEB
    style D fill:#FFD700
    style E fill:#98FB98
    style F fill:#FF6B6B
    style G fill:#FFB6C1
    style H fill:#DDA0DD
    style I fill:#FFE4B5
    style J fill:#87CEEB
    style K fill:#FFD700
    style L fill:#98FB98
    style M fill:#FF6B6B
    style N fill:#FFB6C1
    style O fill:#DDA0DD
    style R fill:#90EE90
    style S fill:#FF6B6B
```

### 7.2 Saga Compensation Flow

```mermaid
flowchart TD
    A[Saga Failure Detected] --> B{Current Step?}

    B -->|PAYMENT_PROCESSED| C[Compensate Payment]
    B -->|INVENTORY_RESERVED| D[Compensate Inventory]

    C --> E[Create Refund Payment]
    E --> F[Save Saga State: FAILED]
    F --> G[Publish payment-failed]
    G --> H[(payment-failed<br/>topic)]

    D --> I[Release Reserved Inventory]
    I --> J[Save Saga State: FAILED]
    J --> K[Publish inventory-released]
    K --> L[(inventory-released<br/>topic)]

    H --> M[Alert Operations]
    L --> M

    style A fill:#FF6B6B
    style B fill:#FFD700
    style C fill:#87CEEB
    style D fill:#87CEEB
    style E fill:#FFB6C1
    style F fill:#FF6B6B
    style G fill:#FFB6C1
    style H fill:#DDA0DD
    style I fill:#FFB6C1
    style J fill:#FF6B6B
    style K fill:#FFB6C1
    style L fill:#DDA0DD
    style M fill:#FFA500
```

### 7.3 Saga Timeout Handling

```mermaid
flowchart TD
    A[@Scheduled<br/>checkSagaTimeouts<br/>every 60s] --> B[Query SagaStateRepository<br/>status=IN_PROGRESS<br/>updatedAt < 5min ago]

    B --> C{Timed Out Sagas?}
    C -->|No| D[Continue Monitoring]
    C -->|Yes| E[handleSagaTimeout]

    E --> F{Current Step?}

    F -->|PAYMENT_PROCESSED| G[Timeout: Payment done,<br/>inventory not reserved]
    G --> H[Compensate Payment]

    F -->|INVENTORY_RESERVED| I[Timeout: Inventory reserved,<br/>order not completed]
    I --> J[Compensate Inventory]

    H --> K[Update Saga State: FAILED]
    J --> K
    K --> L[Log Timeout Event]
    L --> M[Alert Operations]

    style A fill:#FFE4B5
    style B fill:#87CEEB
    style C fill:#FFD700
    style E fill:#87CEEB
    style F fill:#FFD700
    style G fill:#FFA500
    style H fill:#FFB6C1
    style I fill:#FFA500
    style J fill:#FFB6C1
    style K fill:#FF6B6B
    style L fill:#98FB98
    style M fill:#FFA500
```

---

## 8. Error Handling Flows

### 8.1 Retry Configuration

```mermaid
flowchart LR
    subgraph "Error Handler Configuration"
        A[DefaultErrorHandler]
        B["DefaultBackOff<br/>initialInterval: 1s<br/>multiplier: 2.0<br/>maxInterval: 10s<br/>maxAttempts: 3"]
        A --> B
    end

    subgraph "Message Flow"
        C[topic]
        D[topic.RETRY.0]
        E[topic.RETRY.1]
        F[topic.DLT]
    end

    C -->|failure| D
    D -->|failure| E
    E -->|failure| F

    style A fill:#FFE4B5
    style B fill:#FFE4B5
    style C fill:#DDA0DD
    style D fill:#FFA500
    style E fill:#FFA500
    style F fill:#DC143C
```

### 8.2 Dead Letter Topic Handling

```mermaid
flowchart TD
    A[Consumer receives message] --> B{Processing Success?}

    B -->|Yes| C[Acknowledge]
    B -->|No| D[Retry Attempt 1<br/>after 1s]

    D --> E{Success?}
    E -->|Yes| C
    E -->|No| F[Retry Attempt 2<br/>after 2s]

    F --> G{Success?}
    G -->|Yes| C
    G -->|No| H[Retry Attempt 3<br/>after 4s]

    H --> I{Success?}
    I -->|Yes| C
    I -->|No| J[Send to DLT]

    J --> K[(topic-dlt<br/>topic)]

    subgraph "DLT Monitoring"
        L[Alert Operations Team]
        M[Log Error Details]
        N[Manual Intervention Required]
        O[Replay Messages if needed]
    end

    K --> L
    K --> M
    K --> N
    K --> O

    style A fill:#FFE4B5
    style B fill:#FFD700
    style C fill:#90EE90
    style D fill:#FFA500
    style E fill:#FFD700
    style F fill:#FFA500
    style G fill:#FFD700
    style H fill:#FFA500
    style I fill:#FFD700
    style J fill:#FF6B6B
    style K fill:#DC143C
    style L fill:#FF6B6B
    style M fill:#FF6B6B
    style N fill:#FF6B6B
    style O fill:#FFA500
```

### 8.3 Idempotency Pattern

```mermaid
flowchart TD
    A[Message Received] --> B[Extract idempotencyKey]
    B --> C{Key in<br/>processedKeys Set?}

    C -->|Yes - Duplicate| D[Log Warning]
    D --> E[Acknowledge & Skip]

    C -->|No - New| F[Process Message]
    F --> G{Processing Success?}

    G -->|Yes| H[Add key to<br/>processedKeys]
    H --> I[Acknowledge]

    G -->|No| J[Throw Exception<br/>for Retry]

    style A fill:#FFE4B5
    style B fill:#87CEEB
    style C fill:#FFD700
    style D fill:#FFA500
    style E fill:#98FB98
    style F fill:#87CEEB
    style G fill:#FFD700
    style H fill:#90EE90
    style I fill:#90EE90
    style J fill:#FF6B6B
```

---

## 9. Topic Summary Table

| Topic | Partitions | Replicas | Producer | Consumer Group(s) |
|-------|-----------|----------|----------|-------------------|
| `order-created` | 3 | 1 | OrderProducer | order-processor-group, saga-orchestrator-group |
| `order-confirmed` | 3 | 1 | OrderProducer | order-notification-group |
| `order-cancelled` | 3 | 1 | OrderProducer | order-cancellation-group |
| `order-failed` | 3 | 1 | OrderProducer | order-failure-group, inventory-failure-group |
| `payment-processed` | 3 | 1 | PaymentProducer | payment-confirmation-group, saga-orchestrator-group |
| `payment-failed` | 3 | 1 | PaymentProducer | payment-failure-group |
| `inventory-reservation` | 3 | 1 | OrderEventPublisher | inventory-reservation-group |
| `inventory-reserved` | 3 | 1 | InventoryProducer | order-confirmation-group |
| `inventory-released` | 3 | 1 | InventoryProducer | inventory-failure-group |
| `notification-email` | 3 | 1 | NotificationProducer | notification-email-group |
| `notification-sms` | 3 | 1 | NotificationProducer | notification-sms-group |

**DLT Topics:**
- `order-created-dlt`
- `payment-processed-dlt`

---

## 10. Legend

| Color | Component Type |
|-------|---------------|
| 🟢 Green | Controller Layer (REST endpoints) |
| 🔵 Blue | Service Layer (Business logic) |
| 🔴 Red | Producer Layer (Event publishing) |
| 🟣 Purple | Kafka Topics |
| 🟠 Orange | Consumer Layer (Event handling) |
| 🟡 Yellow | Decision Points / Conditions |
| 🟢 Light Green | Success / Acknowledge |
| 🔴 Light Red | Error / Failure / DLT |
| 🟠 Orange | Retry / Warning |
