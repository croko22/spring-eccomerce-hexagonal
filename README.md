# E-commerce Application

A Spring Boot 4.0.5 e-commerce API using Clean Architecture / Hexagonal Architecture with Java 21.

## Tech Stack

- Java 21
- Spring Boot 4.0.5
- Spring Data JPA
- Spring Web MVC
- PostgreSQL
- JUnit 5 + Mockito

## Prerequisites

- Java 21+
- Docker & Docker Compose

## Quick Start

```bash
# Start PostgreSQL
docker-compose up -d

# Run the application
mvn spring-boot:run
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /products | Create a product |

### Create Product

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Laptop", "description": "Gaming Laptop", "price": 1500.0}'
```

## Commands

```bash
mvn compile          # Compile
mvn package          # Build JAR
mvn spring-boot:run  # Run
mvn test             # Run tests
mvn test -Dtest=ProductTest#shouldCreateProductWithValidData  # Single test
```

## Project Structure

```
src/main/java/com/example/ecommerce/product/
├── domain/           # Business entities & exceptions
├── application/      # Use cases & ports
└── infrastructure/   # Adapters (REST, persistence)
```

## Stopping

```bash
docker-compose down
```