package com.learning.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Producer Configuration
 * 
 * Configures the Kafka producer with optimal settings for reliability and performance.
 * 
 * Key Configuration Concepts:
 * - acks=all: Ensures all replicas acknowledge the write (strongest durability)
 * - retries=3: Retry failed sends up to 3 times
 * - linger.ms=5: Wait 5ms to batch messages (improves throughput)
 * - compression.type=snappy: Compress messages to reduce network usage
 * - enable.idempotence=true: Prevent duplicate messages on retry
 * 
 * CHALLENGE 2.5: Research producer configurations
 * TODO: Look up what each configuration does in Kafka documentation
 * 💡 Hint: https://kafka.apache.org/documentation/#producerconfigs
 * 
 * @author Kafka Mastery Project
 */
@Configuration
public class KafkaProducerConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    @Value("${spring.kafka.producer.acks:all}")
    private String acks;
    
    @Value("${spring.kafka.producer.retries:3}")
    private int retries;
    
    /**
     * Producer Factory
     * 
     * Creates and configures the producer factory with all necessary settings.
     * 
     * CHALLENGE 2.6: Understand JsonSerializer
     * TODO: Research how JsonSerializer converts Java objects to JSON bytes
     * 💡 Hint: It uses Jackson ObjectMapper internally
     * 
     * 📝 Solution: JsonSerializer delegates to Jackson for serialization
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        
        // Basic configuration
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        // Reliability configuration
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        
        // Performance optimization
        // CHALLENGE 2.7: Add linger.ms configuration
        // TODO: Set linger.ms to 5 (wait 5ms to batch messages)
        // 💡 Hint: configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        // 📝 SOLUTION: Uncomment the line below
        // configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        
        // CHALLENGE 2.8: Add compression configuration
        // TODO: Set compression.type to "snappy"
        // 💡 Hint: Use ProducerConfig.COMPRESSION_TYPE_CONFIG
        // 📝 SOLUTION: Uncomment and complete the line below
        // configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "___");
        
        // CHALLENGE 2.9: Enable idempotence
        // TODO: Set enable.idempotence to true
        // 💡 Hint: Use ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG
        // 📝 SOLUTION: Uncomment and complete the line below
        // configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, ___);
        
        // TODO: Add the configurations above (challenges 2.7-2.9)
        // Write your code here
        
        
        
        
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    /**
     * Kafka Template
     * 
     * The main class used to send messages to Kafka topics.
     * Provides convenient methods for sending with callbacks.
     * 
     * CHALLENGE 2.10: Understand KafkaTemplate
     * TODO: Research the difference between send() and sendAndReceive()
     * 💡 Hint: One returns CompletableFuture, the other returns SendResult directly
     * 
     * 📝 Solution: 
     * - send() returns CompletableFuture<SendResult> (async, recommended)
     * - sendAndReceive() blocks and returns SendResult (sync, not recommended)
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    /**
     * CHALLENGE 2.11: Create transactional producer factory
     * TODO: Create a @Bean method that returns a ProducerFactory with transaction enabled
     * TODO: Set transactional.id prefix using ProducerConfig.TRANSACTIONAL_ID_CONFIG
     * TODO: Set acks to "all" and enable idempotence
     * 
     * 💡 Hint: Transactional producers are used for exactly-once semantics
     * 
     * 📝 SOLUTION (DON'T LOOK YET!):
     * ```java
     * @Bean
     * public ProducerFactory<String, Object> transactionalProducerFactory() {
     *     Map<String, Object> configProps = new HashMap<>();
     *     configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
     *     configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
     *     configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
     *     configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx-");
     *     configProps.put(ProducerConfig.ACKS_CONFIG, "all");
     *     configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
     *     return new DefaultKafkaProducerFactory<>(configProps);
     * }
     * 
     * @Bean
     * public KafkaTemplate<String, Object> transactionalKafkaTemplate() {
     *     return new KafkaTemplate<>(transactionalProducerFactory());
     * }
     * ```
     */
    // TODO: Add transactional producer factory (see challenge above)
    
    
    
    
}
