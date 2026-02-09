# Spring Boot Microservices & Advanced - Complete Guide

**Target:** Senior Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Microservices Architecture, Spring Cloud, Service Discovery, API Gateway, Feign Client, Circuit Breaker, Security, and Messaging.

--

## Table of Contents

1. [Microservices Architecture](#microservices-architecture)
2. [Spring Cloud Overview](#spring-cloud-overview)
3. [Service Discovery (Eureka)](#service-discovery-eureka)
4. [API Gateway (Spring Cloud Gateway)](#api-gateway-spring-cloud-gateway)
5. [Inter-Service Communication (Feign, RestTemplate)](#inter-service-communication-feign-resttemplate)
6. [Resilience & Fault Tolerance (Resilience4j)](#resilience-fault-tolerance-resilience4j)
7. [Distributed Tracing (Sleuth, Zipkin)](#distributed-tracing-sleuth-zipkin)
8. [Spring Security (OAuth2, JWT)](#spring-security-oauth2-jwt)
9. [Message Queues (Kafka, RabbitMQ)](#message-queues-kafka-rabbitmq)
10. [Complex Technical Scenarios](#complex-technical-scenarios)
11. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. Microservices Architecture

Architectural style that structures an application as a collection of services that are:
*   Highly maintainable and testable
*   Loosely coupled
*   Independently deployable
*   Organized around business capabilities
*   Owned by a small team

### Monolithic vs Microservices

| Feature | Monolithic | Microservices |
|-----|------|--------|
| **Codebase** | Single large codebase | Multiple small codebases |
| **Database** | Shared Database | Database per Service (Polyglot Persistence) |
| **Deployment** | "All or Nothing" (Big Bang) | Independent deployment |
| **Scalability** | Scale entire app (Vertical) | Scale specific services (Horizontal) |
| **Technology** | Single stack (Java) | Mix of technologies (Java, Node, Python) |
| **Complexity** | Simple to start, hard to maintain | Complex distributed system challenges |

--

## 2. Spring Cloud Overview

Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems (e.g., configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus).

**Key Components:**
1.  **Spring Cloud Config**: Centralized configuration management.
2.  **Spring Cloud Netflix (Eureka)**: Service Discovery.
3.  **Spring Cloud Gateway**: API Gateway.
4.  **Spring Cloud OpenFeign**: Declarative REST Client.
5.  **Spring Cloud Resilience4j**: Circuit Breaker.
6.  **Spring Cloud Sleuth**: Distributed Tracing.

--

## 3. Service Discovery (Eureka)

In a dynamic environment (Cloud/Container), service instances have dynamic network locations (IP/Port). A **Service Registry** keeps track of available instances.

### Eureka Server (`@EnableEurekaServer`)
Ideally a standalone Spring Boot app.
```yaml
eureka:
  client:
    register-with-eureka: false # It's a server!
    fetch-registry: false
```

### Eureka Client (`@EnableDiscoveryClient`)
Other microservices register themselves here.
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

--

## 4. API Gateway (Spring Cloud Gateway)

Acts as a single entry point for all clients.
*   **Routing**: Forwards requests to appropriate microservice.
*   **Filtering**: Authentication, Rate Limiting, Logging.
*   **Load Balancing**: Client-side load balancing.

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://USER-SERVICE # Use service name from Eureka!
          predicates:
            - Path=/users/**
```

--

## 5. Inter-Service Communication (Feign, RestTemplate)

### RestTemplate (Deprecated/Legacy)
Synchronous HTTP client.
```java
String url = "http://USER-SERVICE/users/" + id;
User user = restTemplate.getForObject(url, User.class);
```

### WebClient (Reactive - Recommended for Modern)
Non-blocking, reactive client.

### Feign Client (Declarative - Most Popular)
Interface-based client. Spring creates implementation at runtime.
1.  Add `@EnableFeignClients` to main class.
2.  Create Interface:
    ```java
    @FeignClient(name = "USER-SERVICE")
    public interface UserClient {
        @GetMapping("/users/{id}")
        User getUser(@PathVariable("id") Long id);
    }
    ```
3.  Inject and use: `userClient.getUser(1)`.

--

## 6. Resilience & Fault Tolerance (Resilience4j)

Handling failures gracefully in distributed systems.

### Circuit Breaker Pattern
Prevents a network or service failure from cascading to other services.
*   **Closed**: Normal operation. Requests go through.
*   **Open**: Failure threshold reached. Requests fail immediately (Fast Fail).
*   **Half-Open**: Periodic "test" requests to check if service recovered.

```java
@CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUser")
public User getUser(Long id) {
    return userClient.getUser(id);
}

public User fallbackGetUser(Long id, Throwable t) {
    return new User(id, "Default User from Cache");
}
```

--

## 7. Distributed Tracing (Sleuth, Zipkin)

Technique to track a request as it flows through multiple microservices.

*   **Spring Cloud Sleuth**: Adds Trace ID and Span ID to logs.
    *   `Trace ID`: Unique ID for the entire request chain.
    *   `Span ID`: Unique ID for a specific operation within a service.
*   **Zipkin**: Visualization tool (UI) to see the trace path and latency.

Logs look like: `[app-name, trace-id, span-id, exportable]`

--

## 8. Spring Security (OAuth2, JWT)

Securing microservices usually involves passing a token (JWT).

### JWT (JSON Web Token) Flow
1.  Client performs login (POST /login) on **Auth Service**.
2.  Auth Service validates credentials and issues a **Signed JWT**.
3.  Client sends JWT in `Authorization: Bearer <token>` header for subsequent requests.
4.  **API Gateway** or Resource Services validate the JWT signature (using public key or secret).

`spring-boot-starter-oauth2-resource-server`

--

## 9. Message Queues (Kafka, RabbitMQ)

Asynchronous communication for decoupling services.

### Kafka vs RabbitMQ

| Feature | Kafka | RabbitMQ |
|-----|----|-----|
| **Model** | Log-based (Pub/Sub) | Queue-based (Exchange/Queue) |
| **Throughput**| Extremely High | Medium/High |
| **Persistence**| Durable (Retention period) | Transient (Ack removes msg) |
| **Ordering** | Guaranteed per partition | Not strictly guaranteed |
| **Use Case** | Event Sourcing, Logs, Analytics | Complex Routing, Task Queues |

--

## 10. Complex Technical Scenarios

### Topic 1: CAP Theorem - What does Eureka prioritize?
**Answer:** AP (Availability and Partition Tolerance).
Eureka prioritizes availability. If partitions occur, peers might have different views of the registry, but the registry remains available. (Consistency is Eventual).
**Consul/Zookeeper** prioritize CP (Consistency).

### Topic 2: What happens if the Config Server goes down?
**Answer:**
1.  Clients (microservices) usually cache the configuration on startup. They keep running with cached config.
2.  If config changes are needed, they will fail until server is back.
3.  **BootstrapContext**: Load config from local file as fallback.

### Topic 3: How to handle distributed transactions (SAGA Pattern)?
**Answer:**
In Microservices, Two-Phase Commit (2PC) is too slow/locking.
Use **SAGA Pattern**: Sequence of local transactions.
1.  **Choreography**: Each service publishes events triggers next step.
2.  **Orchestration**: Central coordinator tells services what to do.
Failure triggers **Compensating Transactions** (Undo operations).

### Topic 4: Idempotency in REST?
**Answer:**
Safe retry mechanism. Same request executed multiple times must have same effect as execution once.
*   `GET`, `PUT`, `DELETE`: Idempotent.
*   `POST`: Not Idempotent (Creates duplicates). Solution: Client sends `Idempotency-Key` header.

--

## 11. Key Topics & Explanations

### Topic 1: Why API Gateway?
**Answer:**
*   Security (Authentication/SSL Termination).
*   Routing & Load Balancing.
*   Rate Limiting.
*   Request Aggregation (Graph).
*   Protocol Translation (HTTP to AMQP).

### Topic 2: Config Server Refresh Scope?
**Answer:**
`@RefreshScope` allows beans to be reloaded with new configuration at runtime without restarting the service (via `/actuator/refresh` endpoint).

### Topic 3: Service Mesh vs API Gateway?
**Answer:**
*   **API Gateway**: Handles North-South traffic (Client to Cluster). Focuses on business logic routing.
*   **Service Mesh** (Istio/Linkerd): Handles East-West traffic (Service to Service). Infrastructure layer (Sidecar proxy). Focuses on observability, security, reliability.

### Topic 4: Feign Client Error Handling?
**Answer:**
Define an `ErrorDecoder`. It intercepts non-2xx responses and maps them to custom Java Exceptions.

### Topic 5: Blue-Green Deployment?
**Answer:**
Deployment strategy with two identical environments.
*   **Blue**: Live (Current Version).
*   **Green**: Staging (New Version).
1.  Deploy to Green. Test it.
2.  Switch Router/Load Balancer to point to Green.
3.  Green becomes new Blue.
Zero downtime. Easy rollback.

### Topic 6: What is a Sidecar Pattern?
**Answer:**
Deploying a helper service (container) alongside the main service container.
Example: Logging agent, Proxy (Envoy), Config watcher. They share the same network namespace/pod.

### Topic 7: Database per Service vs Shared Database?
**Answer:**
**Database per Service** is the rigorous microservices approach. Ensures loose coupling.
**Shared Database** is an anti-pattern (tight coupling, inability to scale independently) but easier to start with.
Transition: Break monolithic DB using Schema separation first.

### Topic 8: What is Hystrix?
**Answer:**
Latency and fault tolerance library from Netflix (Circuit Breaker). It is now **Maintenance Mode**. Spring Cloud recommends **Resilience4j** as the replacement.
