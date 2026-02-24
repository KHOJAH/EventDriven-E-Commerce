package com.learning.kafka.outbox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Outbox Event Entity - Module 5: Event-Driven Architecture Patterns
 * 
 * This entity stores events that need to be published to Kafka.
 * Used in the Transactional Outbox Pattern to ensure atomic writes.
 * 
 * The Outbox Pattern ensures:
 * 1. Database transaction and event publishing are atomic
 * 2. No events are lost if service crashes after DB commit
 * 3. No duplicate events on retry (idempotency)
 * 
 * CHALLENGE 5.6: Understand the Outbox Pattern
 * TODO: Research why we need the Outbox Pattern in event-driven systems
 * 💡 Hint: Think about the dual write problem
 * 
 * 📝 Solution:
 * Without Outbox:
 * 1. Write to DB → Success
 * 2. Publish to Kafka → Failure (service crashes)
 * Result: DB has data, but no event was published (inconsistency)
 * 
 * With Outbox:
 * 1. Write to DB + Outbox table (same transaction) → Success
 * 2. Separate publisher polls outbox and sends to Kafka
 * Result: Either both succeed or both fail (consistency)
 * 
 * @author Kafka Mastery Project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    
    /**
     * Unique event identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    /**
     * Event type (e.g., "ORDER_CREATED", "PAYMENT_PROCESSED")
     */
    @Column(nullable = false)
    private String eventType;
    
    /**
     * Aggregate ID (e.g., orderId) - used for partitioning
     */
    @Column(nullable = false)
    private String aggregateId;
    
    /**
     * Event payload (JSON)
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;
    
    /**
     * Target Kafka topic
     */
    @Column(nullable = false)
    private String topic;
    
    /**
     * Event status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;
    
    /**
     * Number of retry attempts
     */
    @Column(nullable = false)
    private Integer retryCount;
    
    /**
     * Error message (if publishing failed)
     */
    private String errorMessage;
    
    /**
     * Timestamp when event was created
     */
    @Column(nullable = false)
    private Instant createdAt;
    
    /**
     * Timestamp when event was published
     */
    private Instant publishedAt;
    
    /**
     * Enum for Event Status
     */
    public enum EventStatus {
        PENDING,      // Event created, awaiting publication
        PUBLISHED,    // Event successfully published to Kafka
        FAILED,       // Event publication failed after retries
        RETRYING      // Event is being retried
    }
    
    /**
     * Factory method to create a new outbox event
     * 
     * CHALLENGE 5.7: Implement outbox event creation
     * TODO: Create OutboxEvent with PENDING status
     * TODO: Set retryCount to 0
     * TODO: Set createdAt to current instant
     * 
     * 💡 Hint: Use builder pattern
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * public static OutboxEvent create(String eventType, String aggregateId,
     *                                  String payload, String topic) {
     *     return OutboxEvent.builder()
     *         .eventType(eventType)
     *         .aggregateId(aggregateId)
     *         .payload(payload)
     *         .topic(topic)
     *         .status(EventStatus.PENDING)
     *         .retryCount(0)
     *         .createdAt(Instant.now())
     *         .build();
     * }
     * ```
     */
    public static OutboxEvent create(String eventType, String aggregateId,
                                     String payload, String topic) {
        // TODO: Implement outbox event creation (see challenge above)
        // Write your code here
        
        
        
        
        // Placeholder - replace with your implementation
        return null;
    }
    
    /**
     * Mark event as published
     * 
     * CHALLENGE 5.8: Implement mark as published
     * TODO: Set status to PUBLISHED
     * TODO: Set publishedAt to current instant
     * 
     * 📝 SOLUTION:
     * ```java
     * public void markAsPublished() {
     *     this.status = EventStatus.PUBLISHED;
     *     this.publishedAt = Instant.now();
     * }
     * ```
     */
    public void markAsPublished() {
        // TODO: Implement mark as published (see challenge above)
        // Write your code here
        
        
        
        
    }
    
    /**
     * Mark event as failed
     * 
     * CHALLENGE 5.9: Implement mark as failed
     * TODO: Set status to FAILED
     * TODO: Set errorMessage
     * TODO: Increment retryCount
     * 
     * 📝 SOLUTION:
     * ```java
     * public void markAsFailed(String error) {
     *     this.status = EventStatus.FAILED;
     *     this.errorMessage = error;
     *     this.retryCount = this.retryCount + 1;
     * }
     * ```
     */
    public void markAsFailed(String error) {
        // TODO: Implement mark as failed (see challenge above)
        // Write your code here
        
        
        
        
    }
    
    /**
     * Mark event for retry
     * 
     * CHALLENGE 5.10: Implement mark for retry
     * TODO: Set status to RETRYING
     * TODO: Increment retryCount
     * 
     * 📝 SOLUTION: Similar to above but with RETRYING status
     */
    public void markForRetry() {
        // TODO: Implement mark for retry (see challenge above)
        // Write your code here
        
        
        
        
    }
}
