# Technical IT Training Center Management System - Backend

Microservices-based backend system for managing a technical IT training center, following domain-driven design and event-driven architecture patterns.

## 📋 Project Overview

### Service Architecture
| Service           | Responsibilities                                                                 | Status        |
|--------------------|-----------------------------------------------------------------------------------|---------------|
| **Common Service** | Authentication, User Management (Keycloak), API Gateway, Core Configurations     | Implemented   |
| **Training Service**| Course Management, Material Management, Enrollment/Assignment Management         | Partial       |
| **Admin Service**  | Training Session Management, User Assignment, Schedule Management                | Partial       |

### Key Implemented Features
- ✅ **Authentication & Authorization**  
  JWT-based authentication with role-based access control (Trainee, Trainer, Admin, Partner)
- ✅ **User Management**  
  CRUD operations for Trainees/Trainers/Partners via Keycloak integration
- ✅ **Event-Driven User Syncing**  
  Kafka-based synchronization between Keycloak User Creation and application databases
- ✅ **API Gateway**  
  Route requests to appropriate services (common service acts as entry point)
- ✅ **Centralized Configuration**  
  Shared configuration properties across services

## 🛠 Technologies Used
- **Core Framework**: Spring Boot 3.x, Spring Cloud
- **Security**: Keycloak (OpenID Connect), Spring Security
- **Messaging**: Apache Kafka (Event-driven architecture)
- **Database**: PostgreSQL (Main datastore)
- **Infrastructure**: Docker, Docker Compose
- **CI**: GitLab CI (See `.gitlab-ci.yml`)
- **CD**: Please refer to the **deployment repository**

## 🚀 Getting Started

### Prerequisites
- Docker 20.10+
- Java 17+
- Maven 3.9+
- Kafka 3.4+
- Keycloak 21+

### Environment Setup
1. **Start all the service**:
   ```bash
   docker compose up -d
    ```
   
2. **Log in to Keycloak**:
   - URL: `http://localhost:8443`
   - Credentials: `admin/admin`
   - And create a new realm named `ipmac`
   - Enable `admin-cli` client and create a new client named `ipmac-fe` with `openid-connect` protocol
   - Create some users and roles (trainee, trainer, admin, partner)
   - Assign roles to users

3. **Start the services**:
   - Common Service: `http://localhost:8000`
   - Training Service: `http://localhost:8080`
   - Admin Service: `http://localhost:8081`

### Key Flow : User Creation
   - Admin creates user via Common Service `(POST /api/common/users)` which creates a user in Keycloak
   - Common Service publishes `UserCreationEvent` to Kafka
   - Training Service consumes event:
```java
public class KeycloakUserConsumer {
    private final UserFactory userFactory;
    private final IUserService userService;

    @KafkaListener(topics = "${kafka.topics.user-creation}", groupId = "${kafka.group-id}")
    public void consumeUserCreationEvent(UserCreationEvent event) {
        User u = userFactory.getUser(event.getRole());
        u.setUserId(event.getUserId());
        u.setUsername(event.getUsername());
        u.setEmail(event.getEmail());
        u.setRole(event.getRole());
        u.setFirstName(event.getFirstName());
        u.setLastName(event.getLastName());

        userService.handleUserCreationEvent(u);
    }
}
```