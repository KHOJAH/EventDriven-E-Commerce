# Kafka Mastery Project

A hands-on project to learn Apache Kafka with Spring Boot by building an event-driven e-commerce platform.

---

## Quick Start

```bash
# 1. Start Kafka infrastructure
docker-compose up -d

# 2. Run the application
mvn spring-boot:run
```

**Access Kafka UI:** http://localhost:8080

---

## Prerequisites

- Java 21+
- Maven 3.8+
- Docker Desktop

---

## Learning Modules

| Module | Topic | Level |
|--------|-------|-------|
| 1 | Kafka Fundamentals (Producers & Consumers) | Beginner |
| 2 | Advanced Producer Patterns | Intermediate |
| 3 | Advanced Consumer Patterns | Intermediate |
| 4 | Error Handling & Retry (DLT) | Advanced |
| 5 | Saga & Outbox Patterns | Advanced |
| 6 | Monitoring & Observability | Intermediate |
| 7 | Testing Strategies | Intermediate |

Each module has `TODO` challenges in the code. Try solving them before checking the commented solutions.

---

## Project Structure

```
src/main/java/com/learning/kafka/
├── config/          # Kafka configurations
├── producer/        # Modules 1-2
├── consumer/        # Modules 1-4
├── saga/            # Module 5
├── outbox/          # Module 5
└── service/         # Business logic
```

---

## Documentation

- **[KAFKA_CONCEPTS.md](docs/KAFKA_CONCEPTS.md)** - Theory and concepts
- **[CHEATSHEET.md](docs/CHEATSHEET.md)** - Quick reference
- **[ARCHITECTURE.md](docs/ARCHITECTURE.md)** - System design
- **[TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md)** - Common issues

---

## Testing

```bash
mvn test
```

---

**Happy Learning!**
