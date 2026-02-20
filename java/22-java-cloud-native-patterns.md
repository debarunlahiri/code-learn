# Cloud-Native Patterns in Java - Complete Guide

## Table of Contents
1. [Cloud-Native Fundamentals](#cloud-native-fundamentals)
2. [Containerization with Docker](#containerization-with-docker)
3. [Kubernetes Deployment](#kubernetes-deployment)
4. [Health Checks & Readiness Probes](#health-checks--readiness-probes)
5. [Configuration Management](#configuration-management)
6. [Observability Stack](#observability-stack)
7. [Graceful Shutdown](#graceful-shutdown)
8. [Cloud-Native Testing](#cloud-native-testing)

---

## 1. Cloud-Native Fundamentals

### What it does
Cloud-native is an approach to building applications that exploits cloud advantages — scalability, elasticity, and resilience — using containers, microservices, and declarative APIs.

### Why it matters
- **Elasticity**: Scale up/down based on demand automatically
- **Resilience**: Self-healing, fault-tolerant by design
- **Portability**: Run anywhere — on-prem, AWS, GCP, Azure
- **Speed**: Faster deployments with CI/CD pipelines

### Intuition
Traditional apps are like pet dogs — you name them, care for them. Cloud-native apps are like cattle — interchangeable, replaceable.

### The 12-Factor App Principles
1. **Codebase** — One codebase in VCS, many deploys
2. **Dependencies** — Explicitly declare and isolate dependencies
3. **Config** — Store config in the environment (not code)
4. **Backing services** — Treat as attached resources
5. **Build/Release/Run** — Strictly separate stages
6. **Processes** — Stateless processes
7. **Port binding** — Export services via port binding
8. **Concurrency** — Scale out via process model
9. **Disposability** — Fast startup, graceful shutdown
10. **Dev/prod parity** — Keep environments similar
11. **Logs** — Treat logs as event streams
12. **Admin processes** — Run as one-off processes

### Stateless Cloud-Native Service

```java
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Product> redisTemplate;

    public Product getProduct(String id) {
        String cacheKey = "product:" + id;
        Product cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return cached;

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
        redisTemplate.opsForValue().set(cacheKey, product, Duration.ofMinutes(30));
        return product;
    }
}
```

---

## 2. Containerization with Docker

### What it does
Packages the application and all its dependencies into a portable container image that runs consistently across any environment.

### Why it matters
- Eliminates "works on my machine" problem
- Consistent environments from dev to prod
- Efficient resource utilization
- Fast startup and deployment

### Intuition
Like a shipping container — standardized box that loads onto any ship, truck, or train without repacking.

### Optimized Dockerfile

```dockerfile
# Multi-stage build for minimal image size
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B
COPY src src
RUN ./mvnw package -DskipTests -B
RUN java -Djarmode=layertools -jar target/*.jar extract

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
```

### Docker Compose for Local Development

```yaml
version: '3.8'
services:
  user-service:
    build: ./user-service
    ports:
      - "8081:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=postgres
      - REDIS_HOST=redis
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - app-network

  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: userdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: secret
    volumes:
      - postgres-data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - app-network

  redis:
    image: redis:7-alpine
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
    networks:
      - app-network

volumes:
  postgres-data:
networks:
  app-network:
```

---

## 3. Kubernetes Deployment

### What it does
Kubernetes orchestrates containers, handling deployment, scaling, self-healing, and service discovery automatically.

### Why it matters
- Automatic scaling based on CPU/memory metrics
- Self-healing: restarts failed containers
- Rolling updates with zero downtime
- Built-in service discovery and load balancing

### Intuition
Like an air traffic controller — manages many planes (containers), reroutes when there are problems, scales capacity based on demand.

### Kubernetes Manifests

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
  namespace: production
spec:
  replicas: 3
  selector:
    matchLabels:
      app: user-service
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
        - name: user-service
          image: your-registry/user-service:1.2.0
          ports:
            - containerPort: 8080
          env:
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: user-service-secrets
                  key: db-password
          resources:
            requests:
              memory: "256Mi"
              cpu: "250m"
            limits:
              memory: "512Mi"
              cpu: "500m"
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 8080
            initialDelaySeconds: 30
            periodSeconds: 10
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8080
            initialDelaySeconds: 20
            periodSeconds: 5
          lifecycle:
            preStop:
              exec:
                command: ["/bin/sh", "-c", "sleep 10"]
      terminationGracePeriodSeconds: 60

---
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: user-service-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: user-service
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
```

---

## 4. Health Checks & Readiness Probes

### What it does
Reports application state to Kubernetes/load balancers, enabling automatic traffic routing and self-healing.

### Why it matters
- **Liveness**: Is the app alive? Restart if not
- **Readiness**: Is the app ready to serve traffic? Remove from LB if not
- **Startup**: Has the app finished starting?

### Intuition
Like a doctor's check-up — liveness is "are you breathing?", readiness is "are you fit to work?"

### Custom Health Indicators

```java
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    private final DataSource dataSource;

    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            conn.prepareStatement("SELECT 1").execute();
            return Health.up().withDetail("database", "Available").build();
        } catch (SQLException ex) {
            return Health.down().withDetail("error", ex.getMessage()).build();
        }
    }
}

@Component
public class KafkaHealthIndicator implements HealthIndicator {
    private final KafkaAdmin kafkaAdmin;

    @Override
    public Health health() {
        try {
            kafkaAdmin.describeTopics("order-events");
            return Health.up().withDetail("kafka", "Available").build();
        } catch (Exception ex) {
            return Health.down().withDetail("kafka", ex.getMessage()).build();
        }
    }
}
```

```yaml
management:
  endpoint:
    health:
      show-details: always
      probes:
        enabled: true
      group:
        liveness:
          include: livenessState
        readiness:
          include: readinessState,db,kafka
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
```

---

## 5. Configuration Management

### What it does
Manages application configuration externally, supporting environment-specific values, secrets, and dynamic updates.

### Why it matters
- Separate config from code (12-factor principle)
- Environment-specific settings without code changes
- Secure secrets management

### Configuration Properties with Validation

```java
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {

    @NotNull
    private Database database = new Database();

    @NotNull
    private Security security = new Security();

    @Data
    public static class Database {
        @NotBlank
        private String host;
        @Min(1) @Max(65535)
        private int port = 5432;
        @Min(1) @Max(100)
        private int maxPoolSize = 10;
        private Duration connectionTimeout = Duration.ofSeconds(30);
    }

    @Data
    public static class Security {
        @NotBlank
        private String jwtSecret;
        private Duration jwtExpiration = Duration.ofHours(24);
        private List<String> allowedOrigins = List.of("http://localhost:3000");
    }
}
```

```yaml
# application-prod.yml
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 50
      connection-timeout: 30000

app:
  security:
    jwt-secret: ${JWT_SECRET}
    allowed-origins: ${ALLOWED_ORIGINS:https://app.example.com}
```

---

## 6. Observability Stack

### What it does
Combines metrics, logs, and traces to provide full visibility into system behavior — the three pillars of observability.

### Why it matters
- Proactively detect issues before users report them
- Quickly diagnose root cause of failures
- Track SLOs and error budgets

### Intuition
Like a car dashboard — speedometer (metrics), warning lights (alerts), and diagnostic tool (traces) together give full visibility.

### Metrics with Micrometer

```java
@Service
public class OrderService {
    private final MeterRegistry meterRegistry;
    private final Counter orderCreatedCounter;
    private final Timer orderProcessingTimer;
    private final AtomicInteger activeOrders = new AtomicInteger(0);

    public OrderService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.orderCreatedCounter = Counter.builder("orders.created")
            .description("Total orders created")
            .register(meterRegistry);
        this.orderProcessingTimer = Timer.builder("orders.processing.duration")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry);
        Gauge.builder("orders.active", activeOrders, AtomicInteger::get)
            .register(meterRegistry);
    }

    public Order createOrder(CreateOrderRequest request) {
        return orderProcessingTimer.record(() -> {
            activeOrders.incrementAndGet();
            try {
                Order order = processOrder(request);
                orderCreatedCounter.increment();
                return order;
            } catch (Exception ex) {
                meterRegistry.counter("orders.failed",
                    "reason", ex.getClass().getSimpleName()).increment();
                throw ex;
            } finally {
                activeOrders.decrementAndGet();
            }
        });
    }
}
```

### Distributed Tracing

```java
// application.yml
/*
management:
  tracing:
    sampling:
      probability: 1.0
  zipkin:
    tracing:
      endpoint: http://zipkin:9411/api/v2/spans
*/

@Service
public class UserService {
    private final ObservationRegistry observationRegistry;

    public UserDTO getUserById(String id) {
        return Observation.createNotStarted("user.lookup", observationRegistry)
            .lowCardinalityKeyValue("service", "user-service")
            .observe(() -> userRepository.findById(id)
                .map(UserDTO::from)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }
}
```

### Structured Logging

```java
@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    public void logUserAction(String userId, String action, Object details) {
        MDC.put("userId", userId);
        try {
            log.info("User action: {} | details: {}", action, details);
        } finally {
            MDC.clear();
        }
    }
}
```

---

## 7. Graceful Shutdown

### What it does
Ensures the application completes in-flight requests and cleans up resources before shutting down.

### Why it matters
- Zero data loss during deployments
- No 502 errors for in-flight requests during rolling updates
- Clean resource cleanup (DB connections, Kafka consumers)

### Intuition
Like a restaurant closing — finish serving current customers before locking the door.

### Spring Boot Graceful Shutdown

```java
// application.yml
/*
server:
  shutdown: graceful
spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s
*/

@Component
public class GracefulShutdownHandler {
    private final KafkaListenerEndpointRegistry kafkaListenerRegistry;
    private final ThreadPoolTaskExecutor taskExecutor;

    @PreDestroy
    public void onShutdown() {
        log.info("Starting graceful shutdown...");
        kafkaListenerRegistry.stop();
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(30);
        taskExecutor.shutdown();
        log.info("Graceful shutdown complete");
    }
}
```

---

## 8. Cloud-Native Testing

### What it does
Tests cloud-native applications using real infrastructure (Testcontainers) and contract testing to ensure services work correctly together.

### Why it matters
- Test with real databases, not mocks
- Contract tests prevent integration failures
- Catch environment-specific issues early

### Intuition
Like a dress rehearsal — test with real costumes and props, not substitutes.

### Testcontainers

```java
@SpringBootTest
@Testcontainers
class UserServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
        .withExposedPorts(6379);

    @Container
    static KafkaContainer kafka = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateAndRetrieveUser() {
        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", "password123");
        UserDTO created = userService.createUser(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("John Doe");
        assertThat(created.getEmail()).isEqualTo("john@example.com");

        UserDTO retrieved = userService.getUserById(created.getId());
        assertThat(retrieved).isEqualTo(created);
    }

    @Test
    void shouldThrowExceptionForDuplicateEmail() {
        CreateUserRequest request = new CreateUserRequest("Jane Doe", "jane@example.com", "password");
        userService.createUser(request);

        assertThatThrownBy(() -> userService.createUser(request))
            .isInstanceOf(UserAlreadyExistsException.class)
            .hasMessageContaining("jane@example.com");
    }
}
```

### Consumer-Driven Contract Testing with Pact

```java
// Consumer test (Order Service)
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "user-service")
class UserClientContractTest {

    @Pact(consumer = "order-service")
    public RequestResponsePact getUserPact(PactDslWithProvider builder) {
        return builder
            .given("user with id 123 exists")
            .uponReceiving("a request for user 123")
                .path("/api/v1/users/123")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                    .stringType("id", "123")
                    .stringType("name", "John Doe")
                    .stringType("email", "john@example.com")
                    .stringType("status", "ACTIVE"))
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getUserPact")
    void shouldGetUserById(MockServer mockServer) {
        UserClient client = new UserClient(mockServer.getUrl());
        UserDTO user = client.getUserById("123");

        assertThat(user.getId()).isEqualTo("123");
        assertThat(user.getName()).isEqualTo("John Doe");
    }
}

// Provider verification (User Service)
@Provider("user-service")
@PactBroker(url = "http://pact-broker:9292")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceProviderTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("user with id 123 exists")
    void userExists() {
        // Set up test data
    }
}
```

---

## Edge Cases & Best Practices

### Edge Cases
- **OOMKilled**: Set `MaxRAMPercentage` for JVM to respect container limits
- **Slow startup**: Use startup probes to avoid liveness probe killing during init
- **Config injection timing**: Env vars are set before app starts; secrets may need init containers
- **Rolling update traffic**: Use `preStop` sleep to drain connections before pod termination
- **Image pull failures**: Always use specific image tags, never `latest` in production

### Best Practices

```java
// Environment-aware bean configuration
@Configuration
public class EnvironmentConfig {

    @Bean
    @Profile("!test")
    public DataSource productionDataSource(AppProperties props) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getDatabase().getUrl());
        config.setMaximumPoolSize(props.getDatabase().getMaxPoolSize());
        config.setConnectionTimeout(props.getDatabase().getConnectionTimeout().toMillis());
        config.setLeakDetectionThreshold(60000);
        return new HikariDataSource(config);
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}

// Startup validation
@Component
public class StartupValidator implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        validateRequiredEnvVars();
        validateDatabaseConnectivity();
        log.info("All startup validations passed");
    }

    private void validateRequiredEnvVars() {
        List<String> required = List.of("DB_HOST", "DB_PASSWORD", "JWT_SECRET");
        List<String> missing = required.stream()
            .filter(var -> System.getenv(var) == null)
            .collect(Collectors.toList());
        if (!missing.isEmpty()) {
            throw new IllegalStateException("Missing required environment variables: " + missing);
        }
    }
}
```

---

## Practice Topics
- Containerize a Spring Boot app with multi-stage Docker build
- Deploy to Kubernetes with HPA and rolling updates
- Set up Prometheus + Grafana for metrics visualization
- Implement Testcontainers for integration tests
- Configure graceful shutdown with 30s drain period
- Set up consumer-driven contract tests with Pact
