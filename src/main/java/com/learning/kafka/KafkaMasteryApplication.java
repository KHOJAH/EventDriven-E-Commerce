package com.learning.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Kafka Mastery Project - Main Application Class
 * 
 * This application is designed to help you master Apache Kafka in Spring Boot
 * through hands-on challenges and real-world e-commerce scenarios.
 * 
 * 📚 Learning Path:
 * - Module 1: Kafka Fundamentals (Basic Producer/Consumer)
 * - Module 2: Advanced Producer Patterns
 * - Module 3: Advanced Consumer Patterns
 * - Module 4: Error Handling & Retry Patterns
 * - Module 5: Event-Driven Architecture (Saga, Outbox)
 * - Module 6: Monitoring & Observability
 * - Module 7: Testing Strategies
 * 
 * 🚀 Getting Started:
 * 1. Run: docker-compose up -d
 * 2. Access Kafka UI: http://localhost:8080
 * 3. Run this application
 * 4. Follow the challenges in each module
 * 
 * @author Kafka Mastery Project
 */
@SpringBootApplication
public class KafkaMasteryApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(KafkaMasteryApplication.class, args);
        System.out.println("""
            
            ╔═══════════════════════════════════════════════════════════╗
            ║     🚀 Kafka Mastery Project Started Successfully!        ║
            ╠═══════════════════════════════════════════════════════════╣
            ║  Kafka UI: http://localhost:8080                          ║
            ║  H2 Console: http://localhost:8080/h2-console             ║
            ║  Metrics: http://localhost:8080/actuator/prometheus       ║
            ║                                                           ║
            ║  📚 Open README.md to start your learning journey!        ║
            ╚═══════════════════════════════════════════════════════════╝
            
            """);
    }
}
