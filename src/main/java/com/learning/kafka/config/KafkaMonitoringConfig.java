package com.learning.kafka.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Monitoring Configuration - Module 6: Observability
 * 
 * Configures monitoring and metrics for Kafka producers and consumers.
 * 
 * Key Metrics to Monitor:
 * - Producer: record-send-rate, request-latency-avg, compression-rate
 * - Consumer: records-consumed-rate, fetch-latency, commit-latency
 * - Consumer Lag: difference between latest offset and current position
 * 
 * CHALLENGE 6.2: Understand Kafka metrics
 * TODO: Research important Kafka metrics for production monitoring
 * 💡 Hint: https://kafka.apache.org/documentation/#monitoring
 * 
 * @author Kafka Mastery Project
 */
@Configuration
public class KafkaMonitoringConfig {
    
    /**
     * CHALLENGE 6.3: Configure producer metrics
     * TODO: Create a ProducerFactory with metrics enabled
     * TODO: Add metrics to Micrometer registry
     * 
     * 💡 Hint: Use KafkaClientMetrics binder
     * 
     * 📝 SOLUTION:
     * ```java
     * @Bean
     * public KafkaClientMetrics producerMetrics(MeterRegistry meterRegistry,
     *                                           ProducerFactory<String, Object> producerFactory) {
     *     KafkaClientMetrics metrics = new KafkaClientMetrics(
     *         producerFactory.getConfigurationProperties(),
     *         "producer"
     *     );
     *     metrics.bindTo(meterRegistry);
     *     return metrics;
     * }
     * ```
     */
    // TODO: Configure producer metrics (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 6.4: Configure consumer metrics
     * TODO: Create a ConsumerFactory with metrics enabled
     * TODO: Add metrics to Micrometer registry
     * 
     * 💡 Hint: Similar to producer metrics but for consumer
     * 
     * 📝 SOLUTION:
     * ```java
     * @Bean
     * public KafkaClientMetrics consumerMetrics(MeterRegistry meterRegistry,
     *                                           ConsumerFactory<String, Object> consumerFactory) {
     *     KafkaClientMetrics metrics = new KafkaClientMetrics(
     *         consumerFactory.getConfigurationProperties(),
     *         "consumer"
     *     );
     *     metrics.bindTo(meterRegistry);
     *     return metrics;
     * }
     * ```
     */
    // TODO: Configure consumer metrics (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 6.5: Add custom business metrics
     * TODO: Create a Gauge for orders processed per minute
     * TODO: Create a Counter for payment failures
     * TODO: Create a Timer for message processing duration
     * 
     * 💡 Hint: Use MeterRegistry to create custom metrics
     * 
     * 📝 SOLUTION:
     * ```java
     * @Component
     * public class KafkaMetricsBinder {
     *     
     *     private final MeterRegistry meterRegistry;
     *     private final AtomicInteger ordersProcessed = new AtomicInteger();
     *     
     *     public KafkaMetricsBinder(MeterRegistry meterRegistry) {
     *         this.meterRegistry = meterRegistry;
     *         
     *         Gauge.builder("orders.processed.total", ordersProcessed, AtomicInteger::get)
     *             .description("Total orders processed")
     *             .register(meterRegistry);
     *     }
     *     
     *     public void incrementOrdersProcessed() {
     *         ordersProcessed.incrementAndGet();
     *     }
     * }
     * ```
     */
    // TODO: Add custom business metrics (see challenge above)
    
    
    
    
    /**
     * CHALLENGE 6.6: Configure consumer lag monitoring
     * TODO: Research how to monitor consumer lag in Spring Kafka
     * TODO: Use ConsumerOffsetManager or KafkaAdmin
     * TODO: Expose lag as a metric
     * 
     * 💡 Hint: Consumer lag = log end offset - committed offset
     * 
     * 📝 Solution: Spring Kafka provides KafkaListenerEndpointRegistry
     *              which can access consumer lag information
     */
    
    
    
    
    /**
     * Enable Kafka metrics for Prometheus
     * 
     * CHALLENGE 6.7: Understand Prometheus integration
     * TODO: Research how Prometheus scrapes metrics from Spring Boot
     * 💡 Hint: Actuator exposes /actuator/prometheus endpoint
     * 
     * 📝 Solution:
     * 1. Add micrometer-registry-prometheus dependency
     * 2. Enable prometheus endpoint in application.yml
     * 3. Configure Prometheus to scrape /actuator/prometheus
     */
}
