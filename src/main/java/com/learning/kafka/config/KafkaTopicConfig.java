package com.learning.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka Topic Configuration
 * 
 * This class defines all Kafka topics used in the application.
 * Topics are auto-created when the application starts.
 * 
 * CHALLENGE 2.1: Configure topic partitions and replication
 * TODO: Research why we use 3 partitions for order topics
 * 💡 Hint: Think about parallelism and ordering guarantees
 * 
 * @author Kafka Mastery Project
 */
@Configuration
public class KafkaTopicConfig {
    
    @Value("${kafka.topics.order.created:order-created}")
    private String orderCreatedTopic;
    
    @Value("${kafka.topics.order.confirmed:order-confirmed}")
    private String orderConfirmedTopic;
    
    @Value("${kafka.topics.order.cancelled:order-cancelled}")
    private String orderCancelledTopic;
    
    @Value("${kafka.topics.payment.processed:payment-processed}")
    private String paymentProcessedTopic;
    
    @Value("${kafka.topics.payment.failed:payment-failed}")
    private String paymentFailedTopic;
    
    @Value("${kafka.topics.inventory.reserved:inventory-reserved}")
    private String inventoryReservedTopic;
    
    @Value("${kafka.topics.inventory.released:inventory-released}")
    private String inventoryReleasedTopic;
    
    @Value("${kafka.topics.notification.email:notification-email}")
    private String notificationEmailTopic;
    
    @Value("${kafka.topics.notification.sms:notification-sms}")
    private String notificationSmsTopic;
    
    /**
     * Order Created Topic
     * 
     * CHALLENGE 2.2: Why 3 partitions?
     * TODO: Experiment with different partition counts in Kafka UI
     * 💡 Hint: More partitions = more parallelism but no ordering guarantee across partitions
     * 
     * 📝 Solution: See KAFKA_CONCEPTS.md - Partitions and Parallelism section
     */
    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name(orderCreatedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Order Confirmed Topic
     */
    @Bean
    public NewTopic orderConfirmedTopic() {
        return TopicBuilder.name(orderConfirmedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Order Cancelled Topic
     */
    @Bean
    public NewTopic orderCancelledTopic() {
        return TopicBuilder.name(orderCancelledTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Payment Processed Topic
     * 
     * CHALLENGE 2.3: Why same partition count as order topics?
     * TODO: Think about correlation between orders and payments
     * 💡 Hint: Order ID is used as partition key
     * 
     * 📝 Solution: Same partition count ensures orders and payments with same ID go to same partition
     */
    @Bean
    public NewTopic paymentProcessedTopic() {
        return TopicBuilder.name(paymentProcessedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Payment Failed Topic
     */
    @Bean
    public NewTopic paymentFailedTopic() {
        return TopicBuilder.name(paymentFailedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Inventory Reserved Topic
     */
    @Bean
    public NewTopic inventoryReservedTopic() {
        return TopicBuilder.name(inventoryReservedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Inventory Released Topic
     */
    @Bean
    public NewTopic inventoryReleasedTopic() {
        return TopicBuilder.name(inventoryReleasedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Notification Email Topic
     */
    @Bean
    public NewTopic notificationEmailTopic() {
        return TopicBuilder.name(notificationEmailTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Notification SMS Topic
     */
    @Bean
    public NewTopic notificationSmsTopic() {
        return TopicBuilder.name(notificationSmsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * CHALLENGE 2.4: Create Dead Letter Topic beans
     * TODO: Create @Bean methods for DLT topics (order-created-dlt, payment-processed-dlt, etc.)
     * TODO: Use TopicBuilder to create topics with -dlt suffix
     * 
     * 💡 Hint: Dead Letter Topics store messages that failed processing after all retries
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * @Bean
     * public NewTopic orderCreatedDlt() {
     *     return TopicBuilder.name(orderCreatedTopic + "-dlt")
     *             .partitions(3)
     *             .replicas(1)
     *             .build();
     * }
     * 
     * @Bean
     * public NewTopic paymentProcessedDlt() {
     *     return TopicBuilder.name(paymentProcessedTopic + "-dlt")
     *             .partitions(3)
     *             .replicas(1)
     *             .build();
     * }
     * ```
     */
    // TODO: Add Dead Letter Topic beans here (see challenge above)
    
    
    
    
}
