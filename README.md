# Gadgetry - Device Management API

A REST API for managing device resources with full CRUD operations, built with Spring Boot and PostgreSQL.

## Prerequisites

- Java 21+
- Docker & Docker Compose

## How to Run Project Locally

1. **Start PostgreSQL database**:
```bash
docker-compose up -d db
```

2. **Build and run the application**:
```bash
./gradlew clean build
./gradlew bootRun
```

3. **Access the API**:
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs
- Health check: http://localhost:8080/actuator/health

## How to Run Tests

```bash
# Run all integration tests
./gradlew test

# Run a specific test class
./gradlew test --tests DeviceCreateIntegrationTest

# Run tests with coverage report
./gradlew jacocoTestReport
```

Coverage report: `build/reports/jacoco/index.html`

## How to Install Git Pre-Commit Hooks

This project uses [pre-commit](https://pre-commit.com) for automated code formatting checks.

1. **Install pre-commit**:
```bash
brew install pre-commit
```

2. **Install the git hooks**:
```bash
pre-commit install
```

3. **Run hooks manually** (optional):
```bash
pre-commit run --all-files

# Staged files only
pre-commit run
```

The hooks automatically run on `git commit` and will fix formatting issues before allowing commits.

## How to Build the App

```bash
# Clean build
./gradlew clean build

# Build without running tests
./gradlew assemble

# Build Docker image
docker build -t gadgetry:latest .

# Run in Docker
docker-compose up
```

Build artifacts: `build/libs/gadgetry-*.jar`

## Project Structure

```
src/main/java/com/gadgetry/
├── api/              # REST controllers, DTOs, exception handling
├── domain/           # Business logic, entities, services
├── persistence/      # Repositories, database specifications
├── config/           # Spring & app configuration
└── util/             # Utility classes
```

## Database Migrations

Flyway manages schema migrations. Migration files are in `src/main/resources/db/migration/`.
