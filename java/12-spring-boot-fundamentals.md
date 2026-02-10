# Spring Boot Fundamentals - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Spring vs Spring Boot, Auto-configuration, Starters, Dependency Injection (Deep Dive), IoC, Design Patterns in Spring Boot, Core Annotations, Bean Lifecycle, and tricky technical scenarios.

---

## Table of Contents

1. [Spring Boot Overview](#spring-boot-overview)
2. [Spring Boot Architecture](#spring-boot-architecture)
3. [Dependency Injection & IoC (Deep Dive)](#dependency-injection-ioc-deep-dive)
4. [Design Patterns in Spring Boot](#design-patterns-in-spring-boot)
5. [Spring Boot Starters](#spring-boot-starters)
6. [Auto Configuration](#auto-configuration)
7. [Core Annotations](#core-annotations)
8. [Spring Beans, Scopes & Lifecycle](#spring-beans-scopes-lifecycle)
9. [Application Properties & Profiles](#application-properties-profiles)
10. [Complex Technical Scenarios](#complex-technical-scenarios)
11. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Spring Boot Overview

Spring Boot is an extension of the Spring Framework that eliminates boilerplate configuration required for setting up a Spring application.

> **Analogy:** Spring Framework is like having all the tools and raw materials to build a house — you CAN build anything, but you must wire the plumbing, weld the frames, and connect the electricity yourself. **Spring Boot** is like a pre-fabricated house — the plumbing, wiring, and structure are already done. You just move in and customize.

### Key Features

1.  **Standalone**: Creates executable JARs with embedded servers (Tomcat, Jetty, Undertow) — no need to deploy to an external server.
2.  **Opinionated**: Provides "starter" dependencies to simplify build configuration — sensible defaults that work out of the box.
3.  **Auto-configuration**: Automatically configures Spring beans based on classpath dependencies — detects what you have and configures it.
4.  **Production-ready**: Includes metrics, health checks, and externalized configuration (Actuator).
5.  **No XML**: No code generation and no XML configuration requirement.

### Spring vs Spring Boot (Classic Scenario)

| Feature | Spring Framework | Spring Boot |
|---------|-----------------|-------------|
| **Configuration** | Requires extensive XML or Java config | Zero or minimal configuration (Auto-config) |
| **Server** | Needs external server (Tomcat/JBoss) | Embedded server (Tomcat default) |
| **Dependencies** | Must manage individual JAR versions | "Starters" handle dependencies and versions |
| **Deployment** | WAR file deployed to server | Executable JAR run with `java -jar` |
| **Boilerplate** | High boilerplate code | Reduced boilerplate |
| **Setup time** | Hours to configure a basic app | Minutes — `start.spring.io` → ready |

---

## 2. Spring Boot Architecture

### Execution Flow (What happens when you run your app)

```
main() method
    ↓
SpringApplication.run(MyApp.class, args)
    ↓
┌─────────────────────────────────────────────────────┐
│ 1. Create ApplicationContext (Spring Container)      │
│    - The "brain" that manages all your beans         │
│                                                      │
│ 2. Component Scan (@ComponentScan)                   │
│    - Scans current package + sub-packages            │
│    - Finds @Component, @Service, @Repository, etc.   │
│                                                      │
│ 3. Auto-Configuration (@EnableAutoConfiguration)     │
│    - Detects libraries on classpath                   │
│    - Configures beans automatically                  │
│    - e.g., H2 on classpath → configures DataSource   │
│                                                      │
│ 4. Run Bean Lifecycle                                │
│    - Create beans → Inject dependencies → Initialize │
│                                                      │
│ 5. Start Embedded Server (Tomcat on port 8080)       │
│    - Application is ready to receive requests        │
└─────────────────────────────────────────────────────┘
```

---

## 3. Dependency Injection & IoC (Deep Dive)

### 3.1 What is Inversion of Control (IoC)?

**IoC** is a design principle where the **control of object creation and lifecycle** is transferred from your code to a **container** (Spring Container / ApplicationContext).

> **Without IoC (Traditional):**
> You go to a pizza shop, enter the kitchen, gather ingredients, knead the dough, set the oven temperature, bake the pizza, and serve it to yourself. You **control everything**.
> 
> **With IoC (Spring):**
> You walk into the shop and say "I want a Margherita." The shop (container) handles the kitchen, ingredients, baking, and serving. You just consume the result. **Control is inverted** — the shop manages the process, not you.

```java
// ══════════════════════════════════════════════════════
// WITHOUT IoC — You create and manage everything
// ══════════════════════════════════════════════════════
public class OrderService {
    // YOU create the dependency — tightly coupled!
    private PaymentService paymentService = new PaymentService();
    private EmailService emailService = new EmailService();
    private InventoryService inventoryService = new InventoryService();
    
    // Problem 1: If PaymentService constructor changes, this class breaks
    // Problem 2: Cannot swap PaymentService with MockPaymentService for testing
    // Problem 3: OrderService "knows" how to create PaymentService — not its job!
}

// ══════════════════════════════════════════════════════
// WITH IoC — Spring creates and injects everything
// ══════════════════════════════════════════════════════
@Service
public class OrderService {
    // Spring provides these — OrderService doesn't know HOW they're created
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final InventoryService inventoryService;
    
    // Spring sees this constructor and says:
    // "I have a PaymentService bean, an EmailService bean, 
    //  and an InventoryService bean — let me inject them"
    public OrderService(PaymentService paymentService,
                        EmailService emailService,
                        InventoryService inventoryService) {
        this.paymentService = paymentService;
        this.emailService = emailService;
        this.inventoryService = inventoryService;
    }
}
```

### 3.2 What is Dependency Injection (DI)?

**DI** is the **implementation mechanism** of IoC. It's HOW the container provides dependencies to your objects. IoC is the principle; DI is the practice.

> **Analogy:** IoC says "Don't cook your own meal." DI is the **delivery service** that brings the meal to your door.

### 3.3 Three Types of Dependency Injection

#### Type 1: Constructor Injection ✅ (RECOMMENDED)

Dependencies are provided through the class **constructor**. Spring resolves and injects them when creating the bean.

```java
@Service
public class UserService {
    
    // 'final' means this MUST be set in constructor — cannot be null
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    // @Autowired is OPTIONAL when there's only ONE constructor (Spring 4.3+)
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
    
    public User createUser(User user) {
        User saved = userRepository.save(user);
        emailService.sendWelcome(saved.getEmail());
        return saved;
    }
}
```

**Why Constructor Injection is best:**

| Benefit | Explanation |
|---------|-------------|
| **Immutability** | Fields can be `final` — once set, they can never change |
| **Mandatory dependencies** | If a dependency is missing, the app **fails at startup** (not at runtime) |
| **Testability** | Easy to create instances with mock dependencies in tests |
| **No reflection tricks** | Clean, standard Java — no `@Autowired` annotation magic |
| **No null surprises** | Dependencies are guaranteed to be non-null after construction |

```java
// Testing is easy with Constructor Injection:
@Test
void testCreateUser() {
    // Create mocks
    UserRepository mockRepo = mock(UserRepository.class);
    EmailService mockEmail = mock(EmailService.class);
    
    // Inject via constructor — clean, simple, no Spring needed!
    UserService service = new UserService(mockRepo, mockEmail);
    
    // Test
    service.createUser(new User("Rahul"));
    verify(mockRepo).save(any());
}
```

#### Type 2: Setter Injection (For Optional Dependencies)

Dependencies are injected via **setter methods** after the object is created.

```java
@Service
public class ReportService {
    
    private ReportFormatter formatter;  // Optional — may not be configured
    
    // Spring calls this setter after creating the bean
    @Autowired(required = false)  // required=false makes it optional
    public void setFormatter(ReportFormatter formatter) {
        this.formatter = formatter;
    }
    
    public String generateReport() {
        if (formatter != null) {
            return formatter.format(data);
        }
        return data.toString();  // Fallback if no formatter configured
    }
}
```

**When to use:** Only for truly **optional** dependencies — things that have a reasonable default if not provided.

#### Type 3: Field Injection ❌ (NOT RECOMMENDED)

Dependencies are injected directly into **fields** using `@Autowired`.

```java
@Service
public class OrderService {
    
    @Autowired
    private PaymentService paymentService;  // Injected by reflection
    
    @Autowired
    private InventoryService inventoryService;
    
    // No constructor, no setter — Spring uses reflection to set fields
}
```

**Why it's bad:**

| Problem | Explanation |
|---------|-------------|
| **Hidden dependencies** | Looking at the class, you can't tell what it needs without reading every field |
| **Hard to test** | You can't create the object with mock dependencies via `new` — you NEED Spring or reflection |
| **Null risk** | Fields are null until Spring injects them — if called before injection, NPE |
| **No immutability** | Fields can't be `final` — they're set after construction |
| **Violates SRP** | Easy to keep adding `@Autowired` fields → God class with 20 dependencies |

> **Rule of thumb:** Use **Constructor Injection** for mandatory dependencies (99% of cases). Use **Setter Injection** for optional dependencies. **Never** use Field Injection in production code.

### 3.4 @Autowired — How does Spring "know" what to inject?

Spring follows this resolution order:

```
Step 1: Find beans by TYPE
    └── How many beans match the type?
    
    ├── Exactly ONE → Inject it ✅
    │
    ├── ZERO → Error: NoSuchBeanDefinitionException ❌
    │   (unless @Autowired(required=false))
    │
    └── MORE THAN ONE → Ambiguity! Try to resolve:
        ├── Check @Primary → inject the primary bean ✅
        ├── Check @Qualifier("name") → inject by name ✅
        └── Match by parameter name → inject if name matches ✅
        └── Still ambiguous → Error: NoUniqueBeanDefinitionException ❌
```

```java
// SCENARIO: Two beans of the same type
interface PaymentGateway { void pay(double amount); }

@Service("razorpay")
class RazorpayGateway implements PaymentGateway {
    public void pay(double amount) { System.out.println("Razorpay: ₹" + amount); }
}

@Service("stripe")
class StripeGateway implements PaymentGateway {
    public void pay(double amount) { System.out.println("Stripe: $" + amount); }
}

// ═══════════════════════════════════════
// FIX 1: @Primary — mark one as default
// ═══════════════════════════════════════
@Service
@Primary  // Spring will choose this by default
class RazorpayGateway implements PaymentGateway { ... }

@Service
class OrderService {
    private final PaymentGateway gateway;  // Injects RazorpayGateway (it's @Primary)
    public OrderService(PaymentGateway gateway) { this.gateway = gateway; }
}

// ═══════════════════════════════════════
// FIX 2: @Qualifier — explicitly choose
// ═══════════════════════════════════════
@Service
class OrderService {
    private final PaymentGateway gateway;
    
    public OrderService(@Qualifier("stripe") PaymentGateway gateway) {
        this.gateway = gateway;  // Injects StripeGateway specifically
    }
}

// ═══════════════════════════════════════
// FIX 3: Inject ALL implementations
// ═══════════════════════════════════════
@Service
class PaymentRouter {
    private final Map<String, PaymentGateway> gateways;
    
    // Spring injects ALL PaymentGateway beans as a Map (bean name → bean)
    public PaymentRouter(Map<String, PaymentGateway> gateways) {
        this.gateways = gateways;
    }
    
    public void pay(String provider, double amount) {
        gateways.get(provider).pay(amount);  // Dynamic routing!
    }
}
```

### 3.5 Circular Dependencies

A circular dependency happens when **Bean A depends on Bean B**, and **Bean B depends on Bean A**. Spring can't create either — it's a chicken-and-egg problem.

```java
// ❌ CIRCULAR DEPENDENCY — Spring throws BeanCurrentlyInCreationException
@Service
class ServiceA {
    private final ServiceB serviceB;
    ServiceA(ServiceB serviceB) { this.serviceB = serviceB; }
    // To create ServiceA, Spring needs ServiceB...
}

@Service
class ServiceB {
    private final ServiceA serviceA;
    ServiceB(ServiceA serviceA) { this.serviceA = serviceA; }
    // But to create ServiceB, Spring needs ServiceA! → Deadlock!
}
```

**How to fix:**

| Fix | How | When to use |
|-----|-----|------------|
| **Redesign** | Extract common logic into a third service that both depend on | Best solution — usually means your design is wrong |
| **@Lazy** | `ServiceA(@Lazy ServiceB b)` — creates a proxy, resolves later | Quick fix when redesign is too expensive |
| **Setter Injection** | Use setter instead of constructor for one dependency | Breaks immutability — not ideal |
| **ApplicationEventPublisher** | Use events instead of direct dependency | When services need loose coupling |

```java
// ✅ FIX with @Lazy
@Service
class ServiceA {
    private final ServiceB serviceB;
    
    ServiceA(@Lazy ServiceB serviceB) {
        this.serviceB = serviceB;
        // Spring creates a PROXY for ServiceB (not the real thing yet)
        // Real ServiceB is created when first method is called on it
    }
}
```

---

## 4. Design Patterns in Spring Boot

Spring Boot is **built on** design patterns. Understanding them helps you understand HOW Spring works internally and WHY it's designed the way it is.

### 4.1 Singleton Pattern (Default Bean Scope)

**Where:** Every Spring Bean is a Singleton by default — ONE instance per Spring container, shared across the entire application.

```java
@Service
public class UserService { }  // Only ONE UserService instance exists

// Both controllers get the SAME UserService instance:
@RestController
class UserController {
    @Autowired private UserService userService;  // Instance #1
}

@RestController
class AdminController {
    @Autowired private UserService userService;  // Same Instance #1!
}
```

> **Important:** Spring's "Singleton" is per-container, not per-JVM. If you create two ApplicationContexts, each will have its own instance.

**Why Singleton is default:** Creating new objects for every request is expensive. A service class usually has no state (it just has methods), so sharing one instance is safe and efficient.

### 4.2 Factory Pattern (BeanFactory / ApplicationContext)

**Where:** Spring itself IS a Factory! `ApplicationContext` is a factory that creates, configures, and manages all your beans.

```java
// YOU don't create beans — Spring's factory does it for you
ApplicationContext context = SpringApplication.run(MyApp.class);

// Spring is the factory — it knows how to create UserService
UserService service = context.getBean(UserService.class);

// Behind the scenes, Spring reads your @Service, @Component annotations,
// resolves dependencies, and creates objects — that's Factory Pattern!
```

> **Analogy:** You don't bake your own bread. You go to a bakery (ApplicationContext) and ask for bread (getBean). The bakery knows the recipe, ingredients, and baking process.

### 4.3 Proxy Pattern (AOP — Aspect Oriented Programming)

**Where:** `@Transactional`, `@Async`, `@Cacheable`, Spring Security — all work through **proxies**. Spring wraps your bean with a proxy object that adds behavior before/after your method runs.

```
Your Code calls: userService.createUser()

What ACTUALLY happens:
┌─────────────────────────────────────────────────────────┐
│                   PROXY (Spring-generated)                │
│                                                          │
│  1. Begin Transaction (because @Transactional)           │
│  2. Check Security (because @Secured)                    │
│  3. Check Cache (because @Cacheable)                     │
│                                                          │
│     ┌──────────────────────────────┐                    │
│     │  YOUR ACTUAL METHOD          │                    │
│     │  userService.createUser()    │                    │
│     └──────────────────────────────┘                    │
│                                                          │
│  4. Commit/Rollback Transaction                          │
│  5. Update Cache                                         │
│  6. Log execution time (if AOP logging configured)       │
└─────────────────────────────────────────────────────────┘
```

```java
@Service
public class PaymentService {
    
    @Transactional  // Spring creates a PROXY around this method
    public void processPayment(Order order) {
        // Your code — just business logic
        deductBalance(order.getAmount());
        updateOrderStatus(order, "PAID");
        sendReceipt(order);
    }
    // The proxy does: BEGIN TX → your code → COMMIT (or ROLLBACK on exception)
    // You never write transaction management code!
}
```

**⚠️ Critical gotcha — Self-invocation bypasses proxy:**

```java
@Service
public class PaymentService {
    
    @Transactional
    public void processPayment(Order order) { ... }
    
    public void handleOrder(Order order) {
        // ❌ This calls processPayment DIRECTLY — bypasses the proxy!
        // @Transactional will NOT work!
        this.processPayment(order);
    }
}

// ✅ Fix: Call from another bean (goes through proxy)
@Service
public class OrderService {
    @Autowired private PaymentService paymentService;
    
    public void handleOrder(Order order) {
        paymentService.processPayment(order);  // Goes through proxy → @Transactional works!
    }
}
```

### 4.4 Template Method Pattern (JdbcTemplate, RestTemplate)

**Where:** `JdbcTemplate`, `RestTemplate`, `JmsTemplate`, `KafkaTemplate` — they handle the boilerplate (open connection, execute, close, handle errors), and YOU just provide the custom logic.

```java
// WITHOUT Template — You handle ALL boilerplate
Connection conn = null;
PreparedStatement ps = null;
ResultSet rs = null;
try {
    conn = dataSource.getConnection();          // Get connection
    ps = conn.prepareStatement("SELECT ...");   // Create statement
    rs = ps.executeQuery();                      // Execute
    while (rs.next()) {                          // Process results
        // Your logic
    }
} catch (SQLException e) {
    // Handle error
} finally {
    // Close rs, ps, conn — in reverse order, checking nulls
    if (rs != null) rs.close();
    if (ps != null) ps.close();
    if (conn != null) conn.close();
}

// WITH JdbcTemplate — 1 line! Template handles everything else
List<User> users = jdbcTemplate.query(
    "SELECT * FROM users WHERE active = ?",    // SQL
    new Object[]{true},                         // Parameters
    (rs, rowNum) -> new User(                   // Row mapper — YOUR logic only
        rs.getLong("id"),
        rs.getString("name")
    )
);
```

> **What the Template handles for you:** Getting connection from pool → Preparing statement → Executing → Mapping results → Handling exceptions → Closing connection → Returning connection to pool.

### 4.5 Observer Pattern (Application Events)

**Where:** `ApplicationEventPublisher` and `@EventListener` — components publish events, and other components react without knowing about each other.

```java
// Publisher — doesn't know who listens
@Service
public class UserService {
    @Autowired private ApplicationEventPublisher publisher;
    
    public User register(User user) {
        User saved = userRepository.save(user);
        publisher.publishEvent(new UserRegisteredEvent(saved));  // Fire event
        return saved;
    }
}

// Listener 1 — sends email (doesn't know about UserService)
@Component
class EmailListener {
    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        emailService.sendWelcome(event.getUser().getEmail());
    }
}

// Listener 2 — logs audit (doesn't know about UserService)
@Component
class AuditListener {
    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        auditLog.record("USER_REGISTERED", event.getUser().getId());
    }
}
// Adding Listener 3 next month? Just create a new class — NO changes to UserService!
```

### 4.6 Strategy Pattern (Multiple Implementations)

**Where:** When you have an interface with multiple implementations and swap them based on context — Spring makes this natural with DI.

```java
interface FileStorage {
    void store(String filename, byte[] data);
}

@Service
@Profile("local")  // Active when profile is "local"
class LocalFileStorage implements FileStorage {
    public void store(String filename, byte[] data) {
        // Save to local filesystem
    }
}

@Service
@Profile("prod")  // Active when profile is "prod"
class S3FileStorage implements FileStorage {
    public void store(String filename, byte[] data) {
        // Upload to AWS S3
    }
}

@Service
class UploadService {
    private final FileStorage storage;
    
    // Spring injects LocalFileStorage OR S3FileStorage based on active profile
    // UploadService doesn't know or care which one — Strategy Pattern!
    UploadService(FileStorage storage) {
        this.storage = storage;
    }
}
```

### 4.7 Front Controller Pattern (DispatcherServlet)

**Where:** `DispatcherServlet` is the single entry point for ALL HTTP requests in Spring MVC. It routes requests to the appropriate controller.

```
Client Request: GET /api/users/1
         ↓
   DispatcherServlet (Front Controller — receives ALL requests)
         ↓
   HandlerMapping (finds which @Controller handles /api/users/{id})
         ↓
   UserController.getUser(1)
         ↓
   ViewResolver / MessageConverter (converts return value to JSON)
         ↓
   Response: { "id": 1, "name": "Rahul" }
```

### 4.8 Chain of Responsibility (Filter Chain)

**Where:** Spring Security uses a chain of **Security Filters**. Each filter decides to handle the request or pass it to the next filter in the chain.

```
HTTP Request
    ↓
┌─────────────────────────────────────┐
│ Filter 1: CORS Filter               │ → Check origin headers
│ Filter 2: CSRF Filter               │ → Check CSRF token
│ Filter 3: Authentication Filter     │ → Validate credentials / JWT
│ Filter 4: Authorization Filter      │ → Check user roles & permissions
│ Filter 5: Exception Translation     │ → Convert security exceptions
└─────────────────────────────────────┘
    ↓
Your Controller (only reached if ALL filters pass!)
```

### 4.9 Summary: All Design Patterns in Spring Boot

| # | Pattern | Where in Spring | What it does |
|---|---------|----------------|-------------|
| 1 | **Singleton** | Default Bean scope | ONE instance per container, shared everywhere |
| 2 | **Factory** | `ApplicationContext`, `BeanFactory` | Creates and manages all bean instances |
| 3 | **Proxy** | `@Transactional`, `@Async`, `@Cacheable`, AOP | Wraps beans with cross-cutting concerns |
| 4 | **Template Method** | `JdbcTemplate`, `RestTemplate`, `KafkaTemplate` | Handles boilerplate, you provide custom logic |
| 5 | **Observer** | `ApplicationEvent`, `@EventListener` | Publish-subscribe decoupled communication |
| 6 | **Strategy** | Multiple `@Service` implementations + `@Profile` | Swap algorithms/implementations at runtime |
| 7 | **Front Controller** | `DispatcherServlet` | Single entry point for all HTTP requests |
| 8 | **Chain of Responsibility** | Security Filter Chain, Servlet Filters | Request passes through a chain of handlers |
| 9 | **Prototype** | `@Scope("prototype")` | New instance every time `getBean()` is called |
| 10 | **Adapter** | `HandlerAdapter`, `MessageConverter` | Makes incompatible interfaces work together |
| 11 | **Decorator** | `BeanPostProcessor` | Adds behavior to beans during creation |
| 12 | **Builder** | `UriComponentsBuilder`, `ResponseEntity.status().body()` | Fluent step-by-step object construction |

---

## 5. Spring Boot Starters

Starters are **curated dependency bundles** — instead of adding 10 individual JAR files, you add ONE starter and it pulls in everything you need with compatible versions.

| Starter | What it includes |
|---------|-----------------|
| `spring-boot-starter-web` | Spring MVC, Tomcat, Jackson (JSON), Validation |
| `spring-boot-starter-data-jpa` | Spring Data JPA, Hibernate, JDBC |
| `spring-boot-starter-security` | Spring Security (authentication, authorization) |
| `spring-boot-starter-test` | JUnit 5, Mockito, AssertJ, Hamcrest |
| `spring-boot-starter-actuator` | Health checks, metrics, monitoring endpoints |
| `spring-boot-starter-validation` | Hibernate Validator (`@Valid`, `@NotNull`) |
| `spring-boot-starter-data-redis` | Redis client, Spring Data Redis |
| `spring-boot-starter-mail` | JavaMail, email sending support |

> **Why starters matter:** Without starters, you'd manually manage 20+ JAR versions and hope they're compatible. Starters guarantee version compatibility and include sensible defaults.

---

## 6. Auto Configuration

The **magic** behind Spring Boot — it automatically configures your application based on what libraries are on the classpath.

### How it works

```
You add spring-boot-starter-data-jpa to pom.xml
    ↓
Spring Boot detects Hibernate and DataSource classes on classpath
    ↓
Auto-configuration kicks in:
    ├── Creates DataSource bean (from application.properties)
    ├── Creates EntityManagerFactory bean
    ├── Creates TransactionManager bean
    └── Configures Hibernate properties
    ↓
You just write @Repository interfaces — everything else is done!
```

### Key Annotations

| Annotation | What it does |
|-----------|-------------|
| `@EnableAutoConfiguration` | Turns on auto-configuration (included in `@SpringBootApplication`) |
| `@ConditionalOnClass(X.class)` | Only configure if class X is on classpath |
| `@ConditionalOnMissingBean` | Only configure if YOU haven't defined the bean yourself |
| `@ConditionalOnProperty` | Only configure if a property has a specific value |

```java
// How to DISABLE specific auto-configuration:
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MyApp { }

// Or in application.properties:
// spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

> **Key principle:** `@ConditionalOnMissingBean` means YOUR configuration always wins. If you define your own `DataSource` bean, Spring Boot's auto-configuration steps aside.

---

## 7. Core Annotations

### @SpringBootApplication (The Entry Point)

It's a **combination of 3 annotations**:

```java
@SpringBootApplication  // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}

// Equivalent to:
@Configuration               // This class can define @Bean methods
@EnableAutoConfiguration      // Turn on auto-configuration
@ComponentScan                // Scan this package + sub-packages for @Component beans
public class MyApp { }
```

### Stereotype Annotations

| Annotation | Layer | Extra Behavior |
|-----------|-------|---------------|
| `@Component` | Generic | Base annotation — any Spring-managed bean |
| `@Service` | Business Logic | No extra behavior — indicates service layer intent |
| `@Repository` | Data Access | **Translates** DB exceptions to Spring's `DataAccessException` |
| `@Controller` | Web (MVC) | Returns **views** (HTML/JSP) |
| `@RestController` | Web (REST) | `@Controller` + `@ResponseBody` — returns **data** (JSON/XML) |

> **Are `@Service` and `@Component` technically different?** No! `@Service` has NO extra behavior. But using the right annotation communicates **intent** — other developers know this class holds business logic, not database access or web handling.

### Configuration Annotations

```java
// @Bean — Register a bean that YOU create (instead of @Component scan)
@Configuration
public class AppConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }
}

// @Value — Inject values from properties file
@Service
public class NotificationService {
    @Value("${app.smtp.host}")
    private String smtpHost;
    
    @Value("${app.max-retries:3}")  // Default value = 3 if property not found
    private int maxRetries;
}

// @Qualifier — Choose specific bean when multiple exist
@Autowired
public OrderService(@Qualifier("razorpay") PaymentGateway gateway) { }

// @Primary — Mark one bean as the default choice
@Bean
@Primary
public DataSource primaryDataSource() { ... }

// @Lazy — Don't create bean until first use
@Service
@Lazy
public class ExpensiveService { }  // Created only when first injected/called
```

---

## 8. Spring Beans, Scopes & Lifecycle

### 8.1 Bean Scopes

| Scope | Instances | Lifetime | Use Case |
|-------|-----------|----------|----------|
| **Singleton** (Default) | ONE per container | Entire application lifetime | Stateless services (most beans) |
| **Prototype** | NEW instance per injection | Until no references remain (GC'd) | Stateful beans, builders |
| **Request** | ONE per HTTP request | Single HTTP request | Request-scoped data (Web only) |
| **Session** | ONE per HTTP session | User session duration | User preferences, cart (Web only) |
| **Application** | ONE per ServletContext | Entire web app lifetime | Shared web-app state |

```java
@Service
@Scope("singleton")  // Default — one instance shared by all
public class UserService { }

@Component
@Scope("prototype")  // New instance every time it's requested
public class ShoppingCart { }

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestTracker {
    private final String requestId = UUID.randomUUID().toString();
}
```

> **⚠️ Trap:** If a Singleton bean depends on a Prototype bean, the Prototype is only created ONCE (when the Singleton is created). It does NOT create a new Prototype for each use. Fix: Use `@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)` or `ObjectFactory<T>`.

### 8.2 Bean Lifecycle

```
┌──────────────────────────────────────────────────────────┐
│                    Bean Lifecycle                          │
│                                                          │
│  1. Instantiate (call constructor)                       │
│     ↓                                                    │
│  2. Populate Properties (inject dependencies)            │
│     ↓                                                    │
│  3. BeanNameAware.setBeanName() — if implemented         │
│     ↓                                                    │
│  4. BeanFactoryAware.setBeanFactory() — if implemented   │
│     ↓                                                    │
│  5. ApplicationContextAware.setApplicationContext()       │
│     ↓                                                    │
│  6. BeanPostProcessor.postProcessBeforeInitialization()  │
│     ↓                                                    │
│  7. @PostConstruct method                                │  ← Your init logic here
│     ↓                                                    │
│  8. InitializingBean.afterPropertiesSet()                │
│     ↓                                                    │
│  9. @Bean(initMethod = "init")                           │
│     ↓                                                    │
│  10. BeanPostProcessor.postProcessAfterInitialization()  │
│     ↓                                                    │
│  ══════════ BEAN IS READY TO USE ═══════════             │
│     ↓                                                    │
│  (Application runs...)                                   │
│     ↓                                                    │
│  11. @PreDestroy method                                  │  ← Your cleanup here
│     ↓                                                    │
│  12. DisposableBean.destroy()                            │
│     ↓                                                    │
│  13. @Bean(destroyMethod = "cleanup")                    │
└──────────────────────────────────────────────────────────┘
```

```java
@Service
public class CacheService {
    
    private Map<String, Object> cache;
    
    @PostConstruct  // Runs AFTER dependencies are injected
    public void init() {
        cache = new ConcurrentHashMap<>();
        System.out.println("Cache initialized!");
        // Load initial data, warm up cache, verify connections, etc.
    }
    
    @PreDestroy  // Runs BEFORE bean is destroyed (app shutdown)
    public void cleanup() {
        cache.clear();
        System.out.println("Cache cleared on shutdown!");
        // Close connections, flush buffers, release resources
    }
}
```

---

## 9. Application Properties & Profiles

### 9.1 Configuration Sources

```properties
# application.properties (or application.yml)
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=secret

app.name=MyApplication
app.max-upload-size=10MB
app.feature.notifications=true
```

### 9.2 Profiles

Profiles let you have **different configurations for different environments** (dev, staging, prod):

```
application.properties          ← Common config (used by ALL profiles)
application-dev.properties      ← Development overrides
application-prod.properties     ← Production overrides
application-test.properties     ← Test overrides
```

```properties
# application-dev.properties
spring.datasource.url=jdbc:h2:mem:devdb
logging.level.root=DEBUG

# application-prod.properties
spring.datasource.url=jdbc:mysql://prod-server:3306/mydb
logging.level.root=WARN
```

```bash
# Activate profile
java -jar myapp.jar --spring.profiles.active=prod
# or in application.properties: spring.profiles.active=dev
```

### 9.3 @ConfigurationProperties (Type-safe config)

Instead of scattered `@Value` annotations, bind all related properties to a POJO:

```java
@Component
@ConfigurationProperties(prefix = "app.mail")
public class MailConfig {
    private String host;      // Binds to app.mail.host
    private int port;         // Binds to app.mail.port
    private String from;      // Binds to app.mail.from
    private boolean enabled;  // Binds to app.mail.enabled
    
    // Getters and setters required
}

// Usage — inject like any other Spring bean
@Service
class EmailService {
    private final MailConfig config;
    EmailService(MailConfig config) { this.config = config; }
}
```

---

## 10. Complex Technical Scenarios

### Scenario 1: Can we override the Embedded Tomcat server?

**Answer:** Yes. Exclude `tomcat-starter` and add an alternative:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

### Scenario 2: @Service vs @Component — is there a real difference?

**Answer:** Functionally, NO. Both register a bean in the container. The difference is **semantic** — `@Service` communicates that this class contains business logic. `@Repository` adds exception translation. `@Controller` adds web handling. Use the right one for the right layer.

### Scenario 3: What happens when you inject a Prototype bean into a Singleton?

**Answer:** The Prototype bean is created ONCE when the Singleton is initialized. After that, the same Prototype instance is reused — defeating its purpose!

```java
@Service  // Singleton
class OrderProcessor {
    @Autowired
    private ShoppingCart cart;  // Prototype — but created ONCE!
    
    // Every call uses the SAME cart instance 😱
}

// ✅ Fix using ObjectFactory
@Service
class OrderProcessor {
    @Autowired
    private ObjectFactory<ShoppingCart> cartFactory;
    
    public void process() {
        ShoppingCart cart = cartFactory.getObject();  // NEW instance each time!
    }
}
```

### Scenario 4: How does `@Transactional` work internally?

**Answer:** Spring creates a **Proxy** around your bean. When a `@Transactional` method is called:

1. Proxy intercepts the call
2. Opens a database transaction (`BEGIN`)
3. Calls your actual method
4. If method succeeds → `COMMIT`
5. If method throws RuntimeException → `ROLLBACK`
6. If method throws checked exception → `COMMIT` (by default! — use `rollbackFor` to change)

```java
@Transactional(rollbackFor = Exception.class)  // Rollback on ALL exceptions
public void transferMoney(Long from, Long to, double amount) {
    debit(from, amount);
    credit(to, amount);  // If this fails → both debit and credit are rolled back
}
```

### Scenario 5: How to handle exceptions globally?

**Answer:** Use `@ControllerAdvice` with `@ExceptionHandler`:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(404, ex.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)  // Catch-all fallback
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(500, "Something went wrong"));
    }
}
```

---

## 11. Key Topics & Explanations

### Topic 1: What is Spring Boot Actuator?

**Answer:** Provides production-ready monitoring endpoints:

| Endpoint | What it shows |
|----------|--------------|
| `/actuator/health` | Application health status (UP/DOWN) |
| `/actuator/metrics` | JVM metrics (memory, threads, CPU) |
| `/actuator/info` | Application info (version, description) |
| `/actuator/beans` | All beans registered in the container |
| `/actuator/env` | Environment variables and config properties |
| `/actuator/loggers` | View/change log levels at runtime |

### Topic 2: What is Constructor Injection and why is it recommended?

**Answer:** Constructor Injection passes all dependencies through the constructor. It's recommended because: fields can be `final` (immutable), dependencies are guaranteed non-null at construction time, it's easy to test (just pass mocks to constructor), and it makes dependencies explicit (visible in the constructor signature).

### Topic 3: Difference between `@Controller` and `@RestController`?

**Answer:**
- `@Controller`: Returns **view names** (HTML pages via template engines). Requires `@ResponseBody` on each method to return data directly.
- `@RestController`: Combines `@Controller` + `@ResponseBody`. Every method returns **data** (JSON/XML) by default. Used for REST APIs.

### Topic 4: What is the default port of Tomcat? How to change it?

**Answer:** Default is **8080**. Change via `server.port=9090` in `application.properties`. Use `server.port=0` for a random available port.

### Topic 5: Spring Boot directory structure?

```
my-project/
├── src/
│   ├── main/
│   │   ├── java/com/example/myapp/
│   │   │   ├── MyAppApplication.java     ← Main class (@SpringBootApplication)
│   │   │   ├── controller/               ← REST Controllers
│   │   │   ├── service/                  ← Business logic
│   │   │   ├── repository/              ← Database access
│   │   │   ├── model/                    ← Entity/DTO classes
│   │   │   ├── config/                   ← Configuration classes
│   │   │   └── exception/               ← Custom exceptions & handlers
│   │   └── resources/
│   │       ├── application.properties    ← Configuration
│   │       ├── static/                   ← CSS, JS, images
│   │       └── templates/                ← HTML templates (Thymeleaf)
│   └── test/java/                        ← Test classes
└── pom.xml                               ← Maven dependencies
```

### Topic 6: Can we create a non-web application in Spring Boot?

**Answer:** Yes. Implement `CommandLineRunner` or `ApplicationRunner` and set `spring.main.web-application-type=none`:

```java
@SpringBootApplication
public class BatchApp implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("Running as a non-web application!");
        // Batch processing, data migration, etc.
    }
}
```
