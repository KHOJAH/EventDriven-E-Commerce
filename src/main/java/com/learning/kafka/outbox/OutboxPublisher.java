package com.learning.kafka.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Outbox Publisher - Module 5: Event-Driven Architecture Patterns
 * 
 * This component polls the outbox_events table and publishes pending events to Kafka.
 * Uses scheduled polling to ensure eventual consistency.
 * 
 * CHALLENGE 5.11: Understand the polling mechanism
 * TODO: Research why we use polling instead of immediate publishing
 * 💡 Hint: Think about transaction boundaries and reliability
 * 
 * 📝 Solution:
 * Polling ensures:
 * 1. Events are published after transaction commits
 * 2. Failed events can be retried
 * 3. Service can recover from crashes (pending events remain in table)
 * 
 * @author Kafka Mastery Project
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    /**
     * Maximum retry attempts before marking event as failed
     */
    private static final int MAX_RETRY_ATTEMPTS = 3;
    
    /**
     * Poll outbox and publish pending events
     * 
     * CHALLENGE 5.12: Implement outbox polling
     * TODO: Add @Scheduled annotation with fixedDelay = 5000 (5 seconds)
     * TODO: Query pending events from outboxRepository
     * TODO: For each event, publish to Kafka
     * TODO: Update event status based on success/failure
     * 
     * 💡 Hint:
     * @Scheduled(fixedDelay = 5000)
     * @Transactional
     * public void publishPendingEvents() { ... }
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * @Scheduled(fixedDelay = 5000)
     * @Transactional
     * public void publishPendingEvents() {
     *     log.info("Polling outbox for pending events...");
     *     
     *     List<OutboxEvent> pendingEvents = outboxRepository.findByStatus(OutboxEvent.EventStatus.PENDING);
     *     
     *     for (OutboxEvent event : pendingEvents) {
     *         try {
     *             log.info("Publishing event: {} - {}", event.getEventType(), event.getAggregateId());
     *             
     *             // Deserialize payload
     *             Object payload = objectMapper.readValue(event.getPayload(), Object.class);
     *             
     *             // Publish to Kafka
     *             kafkaTemplate.send(event.getTopic(), event.getAggregateId(), payload).get();
     *             
     *             // Mark as published
     *             event.markAsPublished();
     *             outboxRepository.save(event);
     *             
     *             log.info("Event published successfully: {}", event.getId());
     *         } catch (Exception e) {
     *             log.error("Failed to publish event: {} - {}", event.getId(), e.getMessage());
     *             
     *             // Retry logic
     *             if (event.getRetryCount() < MAX_RETRY_ATTEMPTS) {
     *                 event.markForRetry();
     *             } else {
     *                 event.markAsFailed(e.getMessage());
     *             }
     *             outboxRepository.save(event);
     *         }
     *     }
     * }
     * ```
     */
    // TODO: Implement outbox polling (see challenge above)
    // Write your code here
    
    
    
    
    
    /**
     * CHALLENGE 5.13: Add retry with exponential backoff
     * TODO: Modify the polling method to only process events that are due for retry
     * TODO: Use a query that filters by retryCount and createdAt
     * TODO: Implement exponential backoff (delay = baseDelay * 2^retryCount)
     * 
     * 💡 Hint: Add a method to OutboxRepository to find events due for retry
     * 
     * 📝 SOLUTION: Add repository method with JPQL query
     */
    // TODO: Add retry with exponential backoff (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 5.14: Add cleanup of old published events
     * TODO: Create a scheduled method that deletes published events older than 24 hours
     * TODO: Use @Scheduled with cron expression or fixedRate
     * TODO: Use outboxRepository.deleteByStatusAndPublishedBefore()
     * 
     * 💡 Hint: Keep published events for debugging/auditing, but clean up periodically
     * 
     * 📝 SOLUTION:
     * ```java
     * @Scheduled(cron = "0 0 2 * * *") // Daily at 2 AM
     * @Transactional
     * public void cleanupOldEvents() {
     *     Instant cutoff = Instant.now().minusSeconds(86400); // 24 hours ago
     *     int deleted = outboxRepository.deleteByStatusAndPublishedBefore(
     *         OutboxEvent.EventStatus.PUBLISHED, cutoff);
     *     log.info("Cleaned up {} old published events", deleted);
     * }
     * ```
     */
    // TODO: Add cleanup of old events (see challenge above)
    
    
    
    
}
