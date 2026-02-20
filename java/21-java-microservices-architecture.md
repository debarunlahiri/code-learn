# Microservices Architecture in Java - Complete Guide

## Table of Contents
1. [Microservices Fundamentals](#microservices-fundamentals)
2. [Service Discovery & Registration](#service-discovery--registration)
3. [API Gateway Pattern](#api-gateway-pattern)
4. [Inter-Service Communication](#inter-service-communication)
5. [Circuit Breaker Pattern](#circuit-breaker-pattern)
6. [Event-Driven Microservices](#event-driven-microservices)
7. [Distributed Tracing](#distributed-tracing)
8. [Saga Pattern](#saga-pattern)

---

## 1. Microservices Fundamentals

### What it does
Decomposes a monolithic application into small, independently deployable services, each responsible for a specific business capability.

### Why it matters
- **Independent deployment**: Update one service without affecting others
- **Technology diversity**: Each service can use different tech stacks
- **Scalability**: Scale individual services based on demand
- **Fault isolation**: Failure in one service doesn't cascade

### Intuition
Think of a restaurant: kitchen (cooking), cashier (payments), and waiter (orders) are separate roles. Each can be scaled independently.

### When to use
- Large, complex applications with multiple business domains
- Teams that need to deploy independently
- Applications with varying load on different components

### Microservice Structure

```java
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;
}

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        User saved = userRepository.save(user);
        eventPublisher.publishEvent(new UserCreatedEvent(saved.getId(), saved.getEmail()));
        return UserDTO.from(saved);
    }

    public UserDTO getUserById(String id) {
        return userRepository.findById(id)
            .map(UserDTO::from)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }
}

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable String id) {
        return userService.getUserById(id);
    }
}
```

---

## 2. Service Discovery & Registration

### What it does
Allows microservices to find each other dynamically using a registry (Eureka) without hardcoded URLs.

### Why it matters
- Services scale up/down dynamically
- Automatic load balancing across instances
- Health-check based routing

### Intuition
Like a phone book for services — register your location, others look you up by name.

### Eureka Server

```java
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
    }
}
```

```yaml
# Eureka Server application.yml
server:
  port: 8761
spring:
  application:
    name: service-registry
eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
```

### Client-Side Load Balancing

```java
@Configuration
public class LoadBalancerConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}

@Service
public class OrderService {
    private final RestTemplate restTemplate;

    public UserDTO getUserForOrder(String userId) {
        // Uses service name - load balancer resolves to actual instance
        return restTemplate.getForObject(
            "http://user-service/api/v1/users/" + userId, UserDTO.class);
    }
}
```

---

## 3. API Gateway Pattern

### What it does
Single entry point for all client requests, routing to appropriate microservices and handling cross-cutting concerns.

### Why it matters
- Centralized authentication/authorization
- Rate limiting and throttling
- Request aggregation reduces round trips
- SSL termination

### Intuition
Like a hotel concierge — clients talk to one person who routes to the right department.

### Spring Cloud Gateway

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/v1/users/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
            - name: CircuitBreaker
              args:
                name: userServiceCB
                fallbackUri: forward:/fallback/users
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/v1/orders/**
```

### JWT Authentication Filter

```java
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final JwtTokenProvider jwtTokenProvider;
    private final List<String> publicPaths = List.of("/api/v1/auth/login", "/api/v1/auth/register");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (publicPaths.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        String token = extractToken(exchange.getRequest());
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
            .header("X-User-Id", userId)
            .build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private String extractToken(ServerHttpRequest request) {
        List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null || authHeaders.isEmpty()) return null;
        String header = authHeaders.get(0);
        return header.startsWith("Bearer ") ? header.substring(7) : null;
    }

    @Override
    public int getOrder() { return -100; }
}
```

---

## 4. Inter-Service Communication

### What it does
Microservices communicate via synchronous (REST/gRPC) or asynchronous (messaging) channels.

### Why it matters
- **Synchronous**: Immediate response needed (user-facing operations)
- **Asynchronous**: Decoupled, resilient, for long-running operations

### Intuition
- **Synchronous**: Phone call — you wait for an answer
- **Asynchronous**: Email — send and continue; they reply when ready

### Feign Client

```java
@FeignClient(name = "user-service", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {
    @GetMapping("/api/v1/users/{id}")
    UserDTO getUserById(@PathVariable("id") String id);

    @GetMapping("/api/v1/users")
    Page<UserDTO> getUsers(@RequestParam int page, @RequestParam int size);
}

@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public UserDTO getUserById(String id) {
                return UserDTO.defaultUser(id);
            }
            @Override
            public Page<UserDTO> getUsers(int page, int size) {
                return Page.empty();
            }
        };
    }
}
```

---

## 5. Circuit Breaker Pattern

### What it does
Monitors service calls and "opens" the circuit when failures exceed a threshold, preventing cascading failures.

### Why it matters
- Prevents cascading failures across services
- Fast failure instead of waiting for timeouts
- Graceful degradation with fallbacks
- Automatic recovery when service comes back

### Intuition
Like an electrical circuit breaker — when too many failures occur, it trips open. After cooldown, it tests recovery (half-open state).

### States
- **Closed**: Normal operation, calls pass through
- **Open**: Circuit tripped, calls fail fast with fallback
- **Half-Open**: Testing if service recovered

### Resilience4j

```java
@Service
public class OrderService {
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final UserClient userClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
    @TimeLimiter(name = "userService")
    @Retry(name = "userService")
    public CompletableFuture<UserDTO> getUser(String userId) {
        return CompletableFuture.supplyAsync(() -> userClient.getUserById(userId));
    }

    public CompletableFuture<UserDTO> getUserFallback(String userId, Exception ex) {
        log.warn("Circuit breaker triggered for user {}: {}", userId, ex.getMessage());
        return CompletableFuture.completedFuture(UserDTO.anonymous());
    }
}
```

```yaml
resilience4j:
  circuitbreaker:
    instances:
      userService:
        failure-rate-threshold: 50
        wait-duration-in-open-state: 30s
        sliding-window-size: 10
        minimum-number-of-calls: 5
  retry:
    instances:
      userService:
        max-attempts: 3
        wait-duration: 500ms
  timelimiter:
    instances:
      userService:
        timeout-duration: 3s
```

---

## 6. Event-Driven Microservices

### What it does
Uses asynchronous messages/events to communicate between services, enabling loose coupling and eventual consistency.

### Why it matters
- Services are fully decoupled
- Better resilience — producer doesn't need consumer available
- Natural audit trail via event log
- Enables event sourcing and CQRS

### Intuition
Like a newspaper — publisher prints news without knowing who reads it. Subscribers read what interests them at their own pace.

### Kafka Integration

```java
@Service
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "order-events";

    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = OrderCreatedEvent.builder()
            .orderId(order.getId())
            .userId(order.getUserId())
            .totalAmount(order.getTotalAmount())
            .items(order.getItems())
            .createdAt(order.getCreatedAt())
            .build();
        kafkaTemplate.send(TOPIC, order.getId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) log.error("Failed to publish event for order {}", order.getId(), ex);
                else log.info("Published event for order {}", order.getId());
            });
    }
}

@Service
public class InventoryEventConsumer {
    private final InventoryService inventoryService;

    @KafkaListener(topics = "order-events", groupId = "inventory-service")
    public void handleOrderEvent(@Payload Object event, Acknowledgment acknowledgment) {
        try {
            if (event instanceof OrderCreatedEvent e) {
                inventoryService.reserveItems(e.getItems());
            } else if (event instanceof OrderCancelledEvent e) {
                inventoryService.releaseItems(e.getOrderId());
            }
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("Failed to process event", ex);
            // Don't acknowledge - message will be retried
        }
    }
}
```

---

## 7. Distributed Tracing

### What it does
Tracks requests as they flow through multiple microservices, providing end-to-end visibility for debugging and performance analysis.

### Why it matters
- Identify bottlenecks across service boundaries
- Debug failures in distributed systems
- Understand service dependencies
- Measure latency at each hop

### Intuition
Like a package tracking number — you can see every step of the journey from sender to receiver.

### Micrometer + Zipkin

```java
// Add dependencies: micrometer-tracing-bridge-brave, zipkin-reporter-brave

// application.yml
/*
management:
  tracing:
    sampling:
      probability: 1.0  # Sample 100% in dev, lower in prod
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans
*/

// Tracing is automatic - just inject ObservationRegistry for custom spans
@Service
public class UserService {
    private final ObservationRegistry observationRegistry;
    private final UserRepository userRepository;

    public UserDTO getUserById(String id) {
        return Observation.createNotStarted("user.lookup", observationRegistry)
            .lowCardinalityKeyValue("user.id.prefix", id.substring(0, 4))
            .observe(() -> {
                return userRepository.findById(id)
                    .map(UserDTO::from)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
            });
    }
}

// MDC logging with trace context
@Component
public class TraceLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String traceId = request.getHeader("X-B3-TraceId");
        if (traceId != null) {
            MDC.put("traceId", traceId);
        }
        try {
            chain.doFilter(req, res);
        } finally {
            MDC.clear();
        }
    }
}
```

---

## 8. Saga Pattern

### What it does
Manages distributed transactions across multiple microservices using a sequence of local transactions, with compensating transactions for rollback.

### Why it matters
- Maintains data consistency without distributed transactions (2PC)
- Each service manages its own data
- Compensating transactions handle failures gracefully

### Intuition
Like booking a trip — book flight, then hotel, then car. If car booking fails, cancel hotel, then cancel flight. Each step has an undo action.

### Types
- **Choreography**: Each service publishes events and reacts to others' events
- **Orchestration**: Central coordinator tells each service what to do

### Choreography-based Saga

```java
// Order Service - starts the saga
@Service
public class OrderSagaService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Order order = Order.builder()
            .userId(request.getUserId())
            .items(request.getItems())
            .totalAmount(request.getTotalAmount())
            .status(OrderStatus.PENDING)
            .build();
        Order saved = orderRepository.save(order);
        // Trigger saga by publishing event
        kafkaTemplate.send("order-created", saved.getId(),
            new OrderCreatedEvent(saved.getId(), saved.getUserId(), saved.getItems()));
        return saved;
    }

    @KafkaListener(topics = "payment-processed")
    public void onPaymentProcessed(PaymentProcessedEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
            kafkaTemplate.send("order-confirmed", order.getId(),
                new OrderConfirmedEvent(order.getId()));
        });
    }

    @KafkaListener(topics = "payment-failed")
    public void onPaymentFailed(PaymentFailedEvent event) {
        // Compensating transaction
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            kafkaTemplate.send("order-cancelled", order.getId(),
                new OrderCancelledEvent(order.getId(), event.getReason()));
        });
    }
}

// Payment Service - reacts to order events
@Service
public class PaymentSagaService {
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-created")
    public void onOrderCreated(OrderCreatedEvent event) {
        try {
            Payment payment = processPayment(event.getUserId(), event.getTotalAmount());
            paymentRepository.save(payment);
            kafkaTemplate.send("payment-processed", event.getOrderId(),
                new PaymentProcessedEvent(event.getOrderId(), payment.getId()));
        } catch (PaymentException ex) {
            kafkaTemplate.send("payment-failed", event.getOrderId(),
                new PaymentFailedEvent(event.getOrderId(), ex.getMessage()));
        }
    }

    @KafkaListener(topics = "order-cancelled")
    public void onOrderCancelled(OrderCancelledEvent event) {
        // Compensating transaction - refund payment
        paymentRepository.findByOrderId(event.getOrderId())
            .ifPresent(payment -> {
                payment.setStatus(PaymentStatus.REFUNDED);
                paymentRepository.save(payment);
                log.info("Refunded payment for cancelled order {}", event.getOrderId());
            });
    }
}
```

### Orchestration-based Saga

```java
@Service
public class OrderSagaOrchestrator {
    private final SagaStateRepository sagaStateRepository;
    private final UserClient userClient;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;

    @Transactional
    public void startOrderSaga(String orderId, CreateOrderRequest request) {
        SagaState state = SagaState.builder()
            .sagaId(UUID.randomUUID().toString())
            .orderId(orderId)
            .currentStep(SagaStep.VALIDATE_USER)
            .status(SagaStatus.STARTED)
            .build();
        sagaStateRepository.save(state);
        executeStep(state, request);
    }

    private void executeStep(SagaState state, CreateOrderRequest request) {
        try {
            switch (state.getCurrentStep()) {
                case VALIDATE_USER -> {
                    userClient.validateUser(request.getUserId());
                    state.setCurrentStep(SagaStep.RESERVE_INVENTORY);
                    sagaStateRepository.save(state);
                    executeStep(state, request);
                }
                case RESERVE_INVENTORY -> {
                    inventoryClient.reserveItems(request.getItems());
                    state.setCurrentStep(SagaStep.PROCESS_PAYMENT);
                    sagaStateRepository.save(state);
                    executeStep(state, request);
                }
                case PROCESS_PAYMENT -> {
                    paymentClient.processPayment(request.getUserId(), request.getTotalAmount());
                    state.setCurrentStep(SagaStep.COMPLETED);
                    state.setStatus(SagaStatus.COMPLETED);
                    sagaStateRepository.save(state);
                }
                case COMPLETED -> log.info("Saga {} completed", state.getSagaId());
            }
        } catch (Exception ex) {
            log.error("Saga step {} failed: {}", state.getCurrentStep(), ex.getMessage());
            compensate(state, request);
        }
    }

    private void compensate(SagaState state, CreateOrderRequest request) {
        state.setStatus(SagaStatus.COMPENSATING);
        sagaStateRepository.save(state);
        // Execute compensating transactions in reverse order
        switch (state.getCurrentStep()) {
            case PROCESS_PAYMENT -> {
                inventoryClient.releaseItems(request.getItems());
                state.setCurrentStep(SagaStep.VALIDATE_USER);
                compensate(state, request);
            }
            case RESERVE_INVENTORY -> {
                // Nothing to compensate for user validation
                state.setStatus(SagaStatus.FAILED);
                sagaStateRepository.save(state);
            }
        }
    }
}
```

---

## Edge Cases & Best Practices

### Edge Cases
- **Network partitions**: Services may be unreachable — always implement timeouts and fallbacks
- **Duplicate messages**: Kafka/messaging may deliver messages more than once — implement idempotency
- **Out-of-order events**: Events may arrive out of sequence — use event versioning or timestamps
- **Data consistency**: Eventual consistency means temporary inconsistency is normal — design for it
- **Service startup order**: Services may start before dependencies are ready — use retry with backoff

### Best Practices

```java
// Idempotent consumer - handle duplicate messages
@Service
public class IdempotentOrderConsumer {
    private final ProcessedEventRepository processedEventRepository;
    private final OrderService orderService;

    @KafkaListener(topics = "order-events")
    @Transactional
    public void handleEvent(OrderEvent event, Acknowledgment ack) {
        String eventId = event.getEventId();
        if (processedEventRepository.existsById(eventId)) {
            log.info("Duplicate event {}, skipping", eventId);
            ack.acknowledge();
            return;
        }
        orderService.processEvent(event);
        processedEventRepository.save(new ProcessedEvent(eventId, LocalDateTime.now()));
        ack.acknowledge();
    }
}

// Health check for microservice
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    private final DataSource dataSource;

    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            conn.isValid(1);
            return Health.up().withDetail("database", "Available").build();
        } catch (SQLException ex) {
            return Health.down().withDetail("database", ex.getMessage()).build();
        }
    }
}

// Structured logging for microservices
@Aspect
@Component
public class ServiceLoggingAspect {
    @Around("@within(org.springframework.stereotype.Service)")
    public Object logServiceCall(ProceedingJoinPoint pjp) throws Throwable {
        String method = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            log.info("Service call {} completed in {}ms", method, System.currentTimeMillis() - start);
            return result;
        } catch (Exception ex) {
            log.error("Service call {} failed after {}ms: {}",
                method, System.currentTimeMillis() - start, ex.getMessage());
            throw ex;
        }
    }
}
```

---

## Practice Topics
- Build a simple e-commerce system with User, Order, Inventory, and Payment microservices
- Implement the Saga pattern for order processing with compensating transactions
- Set up Eureka + API Gateway + Config Server
- Add distributed tracing with Zipkin
- Implement Circuit Breaker with Resilience4j and test failure scenarios
- Use Kafka for async communication between services
