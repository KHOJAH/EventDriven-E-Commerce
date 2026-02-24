package com.learning.kafka.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Outbox Repository - Module 5: Event-Driven Architecture Patterns
 * 
 * Spring Data JPA repository for OutboxEvent entity.
 * 
 * @author Kafka Mastery Project
 */
@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {
    
    /**
     * Find all pending events
     * 
     * CHALLENGE 5.15: Implement query for pending events
     * TODO: Add method to find events by status
     * 💡 Hint: Spring Data JPA will generate query from method name
     * 
     * 📝 SOLUTION:
     * List<OutboxEvent> findByStatus(OutboxEvent.EventStatus status);
     */
    // TODO: Implement find by status (see challenge above)
    
    
    
    
    /**
     * Find events due for retry
     * 
     * CHALLENGE 5.16: Implement query for retry-eligible events
     * TODO: Find events with RETRYING status
     * TODO: Filter by retryCount < maxRetries
     * TODO: Filter by createdAt < dueDate (for backoff delay)
     * 
     * 💡 Hint: Use @Query with JPQL
     * 
     * 📝 SOLUTION:
     * ```java
     * @Query("SELECT e FROM OutboxEvent e WHERE e.status = 'RETRYING' " +
     *        "AND e.retryCount < :maxRetries " +
     *        "AND e.createdAt < :dueDate")
     * List<OutboxEvent> findEventsDueForRetry(@Param("maxRetries") int maxRetries,
     *                                         @Param("dueDate") Instant dueDate);
     * ```
     */
    // TODO: Implement find events due for retry (see challenge above)
    
    
    
    
    /**
     * Delete old published events
     * 
     * CHALLENGE 5.17: Implement delete query for old events
     * TODO: Delete events with PUBLISHED status
     * TODO: Filter by publishedAt before cutoff date
     * TODO: Return count of deleted events
     * 
     * 💡 Hint: Use @Modifying and @Query with DELETE statement
     * 
     * 📝 SOLUTION:
     * ```java
     * @Modifying
     * @Query("DELETE FROM OutboxEvent e WHERE e.status = 'PUBLISHED' " +
     *        "AND e.publishedAt < :cutoffDate")
     * int deleteByStatusAndPublishedBefore(@Param("cutoffDate") Instant cutoffDate);
     * ```
     */
    // TODO: Implement delete old events (see challenge above)
    
    
    
    
}
