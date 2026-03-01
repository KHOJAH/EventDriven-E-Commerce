# Event Flow Diagram

## Complete Order Processing Flow

```mermaid
sequenceDiagram
    participant Client
    participant OrderCtrl as OrderController
    participant OrderSvc as OrderService
    participant OrderProd as OrderProducer
    participant Kafka as Kafka Topics
    participant OrderCons as OrderConsumer
    participant PaymentSvc as PaymentService
    participant PaymentProd as PaymentProducer
    participant PaymentCons as PaymentConsumer
    participant InventoryCons as InventoryConsumer
    participant InventorySvc as InventoryService
    participant InventoryProd as InventoryProducer
    participant InventoryEventCons as InventoryEventConsumer
    participant NotificationSvc as NotificationService
    participant NotificationProd as NotificationProducer
    participant NotificationCons as NotificationConsumer

    Client->>OrderCtrl: POST /api/orders
    OrderCtrl->>OrderSvc: createOrder(request)
    OrderSvc->>OrderProd: publishOrderCreated(order)
    OrderProd->>Kafka: order-created topic
    
    Kafka->>OrderCons: consume order-created
    OrderCons->>OrderSvc: processOrder(order)
    OrderSvc-->>OrderCons: validated order
    OrderCons->>PaymentSvc: processPayment(order)
    PaymentSvc->>PaymentProd: publishPaymentProcessed(payment)
    PaymentProd->>Kafka: payment-processed topic
    
    Kafka->>PaymentCons: consume payment-processed
    PaymentCons->>OrderProd: publishInventoryReservationRequest(order)
    OrderProd->>Kafka: inventory-reservation topic
    
    Kafka->>InventoryCons: consume inventory-reservation
    InventoryCons->>InventorySvc: reserveInventoryAndPublish(order)
    InventorySvc->>InventoryProd: publishInventoryReserved(inventory)
    InventoryProd->>Kafka: inventory-reserved topic
    
    Kafka->>InventoryEventCons: consume inventory-reserved
    InventoryEventCons->>OrderSvc: confirmOrder(order)
    OrderSvc->>OrderProd: publishOrderConfirmed(order)
    OrderProd->>Kafka: order-confirmed topic
    InventoryEventCons->>NotificationSvc: sendOrderConfirmation(order)
    NotificationSvc->>NotificationProd: publishEmailNotification(notification)
    NotificationProd->>Kafka: notification-email topic
    
    Kafka->>NotificationCons: consume notification-email
    NotificationCons-->>NotificationCons: Send email to customer
```

## Error Flow - Payment Failure

```mermaid
sequenceDiagram
    participant OrderCons as OrderConsumer
    participant PaymentSvc as PaymentService
    participant PaymentProd as PaymentProducer
    participant Kafka as Kafka Topics
    participant PaymentCons as PaymentConsumer
    participant OrderProd as OrderProducer

    OrderCons->>PaymentSvc: processPayment(order)
    PaymentSvc->>PaymentSvc: executePayment()
    
    alt Payment Fails
        PaymentSvc->>PaymentProd: publishPaymentFailed(payment)
        PaymentProd->>Kafka: payment-failed topic
        
        Kafka->>PaymentCons: consume payment-failed
        PaymentCons->>OrderProd: publishOrderFailed(order)
        OrderProd->>Kafka: order-failed topic
        
        Note over OrderProd: Order failure event<br/>triggers cleanup
    end
```

## Error Flow - Inventory Reservation Failure

```mermaid
sequenceDiagram
    participant InventoryCons as InventoryConsumer
    participant InventorySvc as InventoryService
    participant InventoryProd as InventoryProducer
    participant Kafka as Kafka Topics
    participant InventoryEventCons as InventoryEventConsumer
    participant OrderSvc as OrderService

    InventoryCons->>InventorySvc: reserveInventoryAndPublish(order)
    InventorySvc->>InventorySvc: executeReservation()
    
    alt Inventory Reservation Fails
        InventorySvc->>InventoryProd: publishInventoryReleased(inventory)
        InventoryProd->>Kafka: inventory-released topic
        
        Kafka->>InventoryEventCons: consume inventory-released
        InventoryEventCons->>OrderSvc: failOrder(order, reason)
        OrderSvc->>OrderProd: publishOrderFailed(order)
        OrderProd->>Kafka: order-failed topic
        
        Note over OrderProd: Order failure event<br/>triggers cleanup
    end
```

## Component Architecture

```mermaid
graph TB
    subgraph "Controller Layer"
        OrderCtrl[OrderController]
    end
    
    subgraph "Service Layer (Business Logic)"
        OrderSvc[OrderService]
        PaymentSvc[PaymentService]
        InventorySvc[InventoryService]
        NotificationSvc[NotificationService]
    end
    
    subgraph "Producer Layer (Event Publishing)"
        OrderProd[OrderProducer]
        PaymentProd[PaymentProducer]
        InventoryProd[InventoryProducer]
        NotificationProd[NotificationProducer]
    end
    
    subgraph "Kafka Topics"
        Kafka[(Kafka Broker)]
    end
    
    subgraph "Consumer Layer (Event Handling)"
        OrderCons[OrderConsumer]
        PaymentCons[PaymentConsumer]
        InventoryCons[InventoryConsumer]
        InventoryEventCons[InventoryEventConsumer]
        NotificationCons[NotificationConsumer]
    end
    
    OrderCtrl --> OrderSvc
    OrderSvc --> OrderProd
    OrderProd --> Kafka
    Kafka --> OrderCons
    OrderCons --> PaymentSvc
    PaymentSvc --> PaymentProd
    PaymentProd --> Kafka
    Kafka --> PaymentCons
    PaymentCons --> OrderProd
    Kafka --> InventoryCons
    InventoryCons --> InventorySvc
    InventorySvc --> InventoryProd
    InventoryProd --> Kafka
    Kafka --> InventoryEventCons
    InventoryEventCons --> OrderSvc
    InventoryEventCons --> NotificationSvc
    NotificationSvc --> NotificationProd
    NotificationProd --> Kafka
    Kafka --> NotificationCons
    
    style OrderCtrl fill:#90EE90
    style OrderSvc fill:#87CEEB
    style PaymentSvc fill:#87CEEB
    style InventorySvc fill:#87CEEB
    style NotificationSvc fill:#87CEEB
    style OrderProd fill:#FFB6C1
    style PaymentProd fill:#FFB6C1
    style InventoryProd fill:#FFB6C1
    style NotificationProd fill:#FFB6C1
    style Kafka fill:#DDA0DD
    style OrderCons fill:#FFE4B5
    style PaymentCons fill:#FFE4B5
    style InventoryCons fill:#FFE4B5
    style InventoryEventCons fill:#FFE4B5
    style NotificationCons fill:#FFE4B5
```

## Legend
- 🟢 **Green**: Controller Layer (HTTP endpoints)
- 🔵 **Blue**: Service Layer (Business logic)
- 🔴 **Red**: Producer Layer (Event publishing)
- 🟣 **Purple**: Kafka Topics
- 🟠 **Orange**: Consumer Layer (Event handling)
