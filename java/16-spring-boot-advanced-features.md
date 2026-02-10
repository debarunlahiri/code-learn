# Spring Boot Advanced Features - Complete Guide

**Target:** Skill enhancement for WITCH companies and beyond (Senior Roles).  
**Covers:** Async Processing, Scheduling, Caching (Redis), Batch Processing, Events, and Reactive Programming.

---

## Table of Contents

1. [Async Processing (@Async)](#async-processing-async)
2. [Scheduling (@Scheduled)](#scheduling-scheduled)
3. [Spring Caching (Redis)](#spring-caching-redis)
4. [Spring Batch](#spring-batch)
5. [Spring Events (ApplicationEvent)](#spring-events-applicationevent)
6. [WebFlux (Reactive Stack)](#webflux-reactive-stack)
7. [Complex Technical Scenarios](#complex-technical-scenarios)
8. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Async Processing (@Async)

### 1.1 What is Async Processing?

Normally, when you call a method, the calling thread **waits** until the method finishes (synchronous). With `@Async`, the method runs in a **separate thread**, so the calling thread can continue doing other work without waiting.

> **Analogy:** Imagine ordering food at a restaurant. **Synchronous** = you stand at the counter waiting until the food is ready, blocking everyone behind you. **Asynchronous** = you get a token number, sit down, and the kitchen calls you when it's done. Meanwhile, you can read a book or check your phone.

**When to use:**
- Sending emails (don't make the user wait for email delivery)
- Processing file uploads
- Calling external APIs that might be slow
- Generating reports in the background

### 1.2 Configuration

```java
// Step 1: Enable @Async on your main or config class
@SpringBootApplication
@EnableAsync  // ← This is required!
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### 1.3 Usage

```java
@Service
public class NotificationService {
    
    // ══════════════════════════════════════════════
    // Fire-and-forget (void return — no result)
    // ══════════════════════════════════════════════
    @Async
    public void sendWelcomeEmail(String email) {
        // This runs in a SEPARATE thread
        // The calling method doesn't wait for this to finish
        emailClient.send(email, "Welcome!", "Thanks for joining...");
        System.out.println("Email sent on thread: " + Thread.currentThread().getName());
    }
    
    // ══════════════════════════════════════════════
    // With result (use CompletableFuture)
    // ══════════════════════════════════════════════
    @Async
    public CompletableFuture<Report> generateReport(Long userId) {
        // Long-running task in background thread
        Report report = reportBuilder.build(userId);  // Takes 30 seconds
        return CompletableFuture.completedFuture(report);
    }
}

// Calling code:
@Service
public class UserService {
    @Autowired
    private NotificationService notificationService;
    
    public User createUser(User user) {
        User saved = userRepository.save(user);
        
        // This returns IMMEDIATELY — email sent in background
        notificationService.sendWelcomeEmail(saved.getEmail());
        
        // This returns a Future — you can get the result later
        CompletableFuture<Report> future = notificationService.generateReport(saved.getId());
        // ... do other work ...
        Report report = future.get();  // Block here only when you NEED the result
        
        return saved;
    }
}
```

### 1.4 Custom Thread Pool (MUST DO for Production!)

By default, Spring uses `SimpleAsyncTaskExecutor` which creates a **new thread for every task** — no pooling, no limit. This can crash your app under load!

> **Danger:** Without a custom thread pool, 10,000 async calls = 10,000 threads = **OOM crash**. Always configure a bounded pool.

```java
@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);       // Always keep 5 threads alive
        executor.setMaxPoolSize(10);       // Scale up to 10 under load
        executor.setQueueCapacity(500);    // Queue 500 tasks before rejecting
        executor.setThreadNamePrefix("Async-");  // For debugging in logs
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // ↑ If queue is full, run the task on the calling thread instead of dropping it
        executor.initialize();
        return executor;
    }
}

// Use specific executor
@Async("taskExecutor")  // Reference the bean name
public void processFile(String filePath) { ... }
```

### 1.5 Exception Handling in Async Methods

```java
// For void methods — exceptions are SILENTLY SWALLOWED by default!
// You MUST configure an exception handler:
@Configuration
public class AsyncExceptionConfig implements AsyncConfigurer {
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            log.error("Async method {} failed: {}", method.getName(), ex.getMessage());
            // Send alert, log to monitoring system, etc.
        };
    }
}

// For CompletableFuture — use exceptionally()
@Async
public CompletableFuture<String> riskyOperation() {
    return CompletableFuture.supplyAsync(() -> {
        // ... might throw
        return "result";
    }).exceptionally(ex -> {
        log.error("Operation failed", ex);
        return "fallback";
    });
}
```

---

## 2. Scheduling (@Scheduled)

### 2.1 What is Scheduling?

Scheduling lets you **automatically run methods at specific times or intervals** — like cron jobs in Linux, but managed by Spring.

> **Analogy:** An alarm clock that goes off every day at 7 AM, or a reminder that pops up every 30 minutes. You set it once, and it runs automatically.

**When to use:**
- Clean up expired sessions every hour
- Send daily digest emails
- Sync data with external APIs every 15 minutes
- Generate nightly reports

### 2.2 Configuration

```java
@SpringBootApplication
@EnableScheduling  // ← Required to activate @Scheduled methods
public class MyApplication { ... }
```

### 2.3 Three Scheduling Modes

```java
@Component
public class ScheduledTasks {

    // ══════════════════════════════════════════════
    // 1. Fixed Rate — Run every N ms (regardless of execution time)
    // ══════════════════════════════════════════════
    // Timeline: |---Start---Finish|---Start---Finish|---Start---Finish|
    //           0s              2s  5s            7s  10s           12s
    //           ←── 5000ms ──→  ←── 5000ms ──→
    
    @Scheduled(fixedRate = 5000)  // Every 5 seconds
    public void checkHealth() {
        log.info("Health check at {}", Instant.now());
        // If this takes 3 seconds, next run starts 5s after LAST START
        // If this takes 7 seconds, next run starts immediately after (delayed)
    }
    
    // ══════════════════════════════════════════════
    // 2. Fixed Delay — Wait N ms AFTER completion, then run again
    // ══════════════════════════════════════════════
    // Timeline: |---Start---Finish|     |---Start---Finish|
    //           0s              3s  3+5s=8s           11s    11+5s=16s
    //                            ←── 5000ms wait ──→
    
    @Scheduled(fixedDelay = 5000)  // 5 seconds AFTER last run finishes
    public void syncData() {
        log.info("Syncing data...");
        // Takes 3 seconds to complete
        // Next run starts 5s after this one FINISHES (at 8s, not 5s)
    }
    
    // ══════════════════════════════════════════════
    // 3. Cron Expression — Unix-like schedule
    // ══════════════════════════════════════════════
    // Format: second minute hour day-of-month month day-of-week
    
    @Scheduled(cron = "0 0 9 * * MON-FRI")  // Weekdays at 9:00 AM
    public void sendDailyReport() {
        log.info("Sending daily report...");
    }
    
    @Scheduled(cron = "0 0 */2 * * *")  // Every 2 hours
    public void cleanupSessions() {
        log.info("Cleaning expired sessions...");
    }
    
    @Scheduled(cron = "0 30 23 * * *")  // Daily at 11:30 PM
    public void nightlyBackup() {
        log.info("Starting nightly backup...");
    }
}
```

### 2.4 Cron Expression Cheat Sheet

```
┌──────── second (0-59)
│ ┌────── minute (0-59)
│ │ ┌──── hour (0-23)
│ │ │ ┌── day of month (1-31)
│ │ │ │ ┌─ month (1-12)
│ │ │ │ │ ┌─ day of week (0-7, SUN-SAT)
│ │ │ │ │ │
* * * * * *
```

| Expression | Meaning |
|-----------|---------|
| `0 0 12 * * ?` | Every day at noon |
| `0 0 9 * * MON-FRI` | Weekdays at 9 AM |
| `0 */15 * * * *` | Every 15 minutes |
| `0 0 0 1 * *` | First day of each month at midnight |
| `0 0 */2 * * *` | Every 2 hours |

### 2.5 Fixed Rate vs Fixed Delay

| Feature | fixedRate | fixedDelay |
|---------|-----------|-----------|
| **Timer starts** | From last **start** time | From last **end** time |
| **Overlapping?** | Possible if task runs longer than interval | Never — waits for completion |
| **Use case** | Regular heartbeats, monitoring | Data sync, cleanup jobs |
| **initialDelay** | ✅ Supported (wait N ms before first run) | ✅ Supported |

---

## 3. Spring Caching (Redis)

### 3.1 What is Caching?

Caching stores the **results of expensive operations** so they can be reused without re-computing. Instead of hitting the database every time someone views the same product page, you store the result and return it instantly.

> **Analogy:** Imagine you're a librarian. Someone asks for the same book 100 times a day. Instead of walking to the back shelf every time, you **keep a copy on your desk** (cache). When asked, you hand it from your desk instantly.

**When to use:**
- Frequently accessed data that rarely changes (user profiles, product catalogs)
- Expensive computations (report generation, complex queries)
- External API responses (weather data, exchange rates)

### 3.2 Configuration

```java
// Step 1: Enable caching
@SpringBootApplication
@EnableCaching  // ← Required!
public class MyApplication { ... }
```

```properties
# Step 2: application.properties
spring.cache.type=redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.cache.redis.time-to-live=600000  # 10 minutes TTL (in ms)
```

```xml
<!-- Step 3: Dependencies in pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

### 3.3 Caching Annotations

| Annotation | What it does | When to use |
|-----------|-------------|------------|
| `@Cacheable` | Check cache first → if found, return cached value. If not, execute method and cache result | **Read** operations |
| `@CachePut` | Always execute method AND update cache with new result | **Update** operations |
| `@CacheEvict` | Remove entry from cache | **Delete** operations or when data changes |

```java
@Service
public class ProductService {
    
    // ══════════════════════════════════════════════
    // @Cacheable — Check cache before executing
    // ══════════════════════════════════════════════
    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {
        log.info("Fetching from database...");  // This log won't appear on cache hit
        return productRepository.findById(id).orElseThrow();
    }
    // First call:  DB query → result stored in cache → returned
    // Second call: Cache hit → result returned directly (NO DB query!)
    
    // ══════════════════════════════════════════════
    // @CachePut — Always execute AND update cache
    // ══════════════════════════════════════════════
    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
    // Always runs the method → updates the cached value
    // So the next @Cacheable call gets fresh data
    
    // ══════════════════════════════════════════════
    // @CacheEvict — Remove from cache
    // ══════════════════════════════════════════════
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    // Removes the cached entry → next read will hit DB
    
    // Clear ALL entries in the "products" cache
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() { }
    
    // ══════════════════════════════════════════════
    // Conditional caching
    // ══════════════════════════════════════════════
    @Cacheable(value = "products", key = "#id", condition = "#id > 0")
    public Product getProductConditional(Long id) {
        return productRepository.findById(id).orElseThrow();
    }
    // Only cache if id > 0 (don't cache invalid lookups)
}
```

### 3.4 Cache Flow Diagram

```
Request: getProduct(42)
    ↓
  Is #42 in cache?
    ├── YES → Return cached value instantly (no DB hit)
    └── NO  → Execute method → Query DB → Store result in cache → Return
```

---

## 4. Spring Batch

### 4.1 What is Spring Batch?

Spring Batch is a framework for **processing large volumes of data** reliably — reading from a source, transforming the data, and writing to a destination, all with built-in error handling, restartability, and transaction management.

> **Analogy:** Think of a bottling factory assembly line. Bottles come in on a conveyor belt (**Reader**), get filled and labeled (**Processor**), then packed into boxes (**Writer**). If a bottle breaks, the line handles it without stopping everything.

**When to use:**
- Processing millions of database records
- ETL (Extract, Transform, Load) jobs
- Nightly batch processing (payroll, billing)
- Data migration between systems
- Report generation from large datasets

### 4.2 Core Components

```
┌────────────────────────────────────────────────────────────┐
│                        JOB                                  │
│  (Encapsulates the entire batch process)                   │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                     STEP 1                            │  │
│  │                                                      │  │
│  │  ┌──────────┐    ┌──────────────┐    ┌────────────┐ │  │
│  │  │  READER   │ →  │  PROCESSOR   │ →  │   WRITER   │ │  │
│  │  │(Read CSV) │    │(Transform)   │    │(Write to DB)│ │  │
│  │  └──────────┘    └──────────────┘    └────────────┘ │  │
│  │                                                      │  │
│  │  Items read one-by-one → Processed → Written in CHUNK│  │
│  └──────────────────────────────────────────────────────┘  │
│                          ↓                                  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                     STEP 2                            │  │
│  │  (Another read-process-write cycle or Tasklet)       │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────┘
```

| Component | Role | Example |
|-----------|------|---------|
| **Job** | Top-level container for the entire batch process | "Import all users from CSV" |
| **Step** | Individual phase of a Job | "Read CSV → Process → Insert into DB" |
| **ItemReader** | Reads one item at a time from the source | `FlatFileItemReader` (CSV), `JdbcCursorItemReader` (DB) |
| **ItemProcessor** | Transforms/validates each item | Convert date formats, validate email, filter invalid records |
| **ItemWriter** | Writes a batch (chunk) of processed items to the destination | `JdbcBatchItemWriter` (DB), `FlatFileItemWriter` (File) |
| **JobLauncher** | Triggers execution of a Job | `jobLauncher.run(job, parameters)` |

### 4.3 Chunk Processing

**Chunk processing** is the heart of Spring Batch. Instead of processing one item at a time (slow) or loading everything into memory (risky), it processes items in **chunks** — read N items, process them, then write them all in one transaction.

```
Chunk Size = 100

Read:    📖📖📖📖📖...📖  (100 items read one by one)
Process: ⚙️⚙️⚙️⚙️⚙️...⚙️  (100 items processed one by one)
Write:   📝📝📝📝📝...📝  (100 items written in ONE transaction)
         ← If ANY write fails, the entire chunk is rolled back →
```

> **Why chunks?** If you process 1 million records and item 500,001 fails, you lose everything without chunks. With chunk size 100, you only lose the last 100 items — the first 500,000 are already committed.

---

## 5. Spring Events (ApplicationEvent)

### 5.1 What are Spring Events?

Spring Events implement the **Observer pattern** — components can publish events, and other components can listen for and react to those events without knowing about each other. This **decouples** your code.

> **Analogy:** A newspaper publishing system. The newspaper (publisher) doesn't know who reads it. Subscribers (listeners) sign up to receive it. When a new edition is published, ALL subscribers get notified automatically.

**When to use:**
- Send welcome email when user registers (without coupling UserService to EmailService)
- Update search index when product is updated
- Log audit trail when important actions happen
- Notify multiple systems about a single action

### 5.2 Implementation

```java
// ══════════════════════════════════════════════
// Step 1: Define the Event (what happened)
// ══════════════════════════════════════════════
public class UserCreatedEvent {
    private final User user;
    private final Instant timestamp;
    
    public UserCreatedEvent(User user) {
        this.user = user;
        this.timestamp = Instant.now();
    }
    
    public User getUser() { return user; }
    public Instant getTimestamp() { return timestamp; }
}

// ══════════════════════════════════════════════
// Step 2: Publish the Event (when it happens)
// ══════════════════════════════════════════════
@Service
public class UserService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public User createUser(User user) {
        User saved = userRepository.save(user);
        
        // Publish event — UserService doesn't know/care who listens
        eventPublisher.publishEvent(new UserCreatedEvent(saved));
        
        return saved;
    }
}

// ══════════════════════════════════════════════
// Step 3: Listen for the Event (react to it)
// ══════════════════════════════════════════════
@Component
public class WelcomeEmailListener {
    
    @EventListener
    @Async  // Handle asynchronously — don't block the publisher
    public void handleUserCreated(UserCreatedEvent event) {
        emailService.sendWelcomeEmail(event.getUser().getEmail());
        log.info("Welcome email sent to {}", event.getUser().getEmail());
    }
}

@Component
public class AuditLogger {
    
    @EventListener
    public void logUserCreation(UserCreatedEvent event) {
        auditService.log("USER_CREATED", event.getUser().getId(), event.getTimestamp());
    }
}

@Component
public class SearchIndexUpdater {
    
    @EventListener
    @Async
    public void updateSearchIndex(UserCreatedEvent event) {
        searchService.indexUser(event.getUser());
    }
}
```

> **Big benefit:** `UserService` doesn't import `EmailService`, `AuditService`, or `SearchService`. If you add a 4th listener next month, you don't touch `UserService` at all. That's the power of decoupling!

### 5.3 Conditional Event Listeners

```java
// Only handle event under certain conditions
@EventListener(condition = "#event.user.role == 'ADMIN'")
public void handleAdminCreated(UserCreatedEvent event) {
    // Only fires for admin users
}

// Handle multiple event types
@EventListener({UserCreatedEvent.class, UserUpdatedEvent.class})
public void handleUserChanges(Object event) { ... }
```

---

## 6. WebFlux (Reactive Stack)

### 6.1 What is WebFlux?

WebFlux is Spring's **non-blocking, event-driven** alternative to Spring MVC. Instead of assigning one thread per request (which blocks while waiting for DB/API responses), WebFlux uses a **small pool of threads** that handle many requests concurrently by never blocking.

> **Analogy:** 
> - **Spring MVC** = A restaurant with 100 waiters. Each waiter takes an order, goes to the kitchen, WAITS for the food, then delivers it. If all 100 waiters are waiting, new customers can't be served.
> - **WebFlux** = A restaurant with 5 waiters and a ticket system. A waiter takes an order, gives it to the kitchen, and immediately serves another customer. When the food is ready, a buzzer goes off and the next available waiter delivers it. Those 5 waiters handle hundreds of customers.

**When to use:**
- High-concurrency applications (10k+ simultaneous connections)
- Streaming data (real-time dashboards, live feeds)
- Microservices with many inter-service calls
- Applications where threads are a bottleneck

### 6.2 Key Types

| Type | What it represents | Similar to |
|------|-------------------|-----------|
| **Mono<T>** | 0 or 1 item (like `Optional<T>` but async) | A single future value |
| **Flux<T>** | 0 to N items (like `List<T>` but async) | A stream of future values |

```java
// Mono — single value (like one API response)
Mono<User> userMono = userRepository.findById(1L);

// Flux — multiple values (like a list of users)
Flux<User> usersFlux = userRepository.findAll();

// Key difference from normal code:
// Data doesn't flow until someone SUBSCRIBES to it
usersFlux.subscribe(user -> System.out.println(user.getName()));
```

### 6.3 Backpressure

**Backpressure** is how a consumer tells a producer to slow down when it can't keep up.

> **Analogy:** You're drinking from a fire hose. Backpressure is telling the firefighter "slower please!" so you don't drown.

```java
// Without backpressure: producer sends 1 million items → consumer overwhelmed
// With backpressure: consumer requests 100 items at a time → controlled flow
```

### 6.4 Netty (Default Server)

WebFlux uses **Netty** as its default embedded server instead of Tomcat. Netty is designed for non-blocking I/O and can handle thousands of connections with very few threads.

---

## 7. Complex Technical Scenarios

### 7.1 `@Async` is not working — why?

**Explanation:** This is one of the most common Spring issues. Three main causes:

| # | Cause | Why it happens | Fix |
|---|-------|---------------|-----|
| 1 | **Self-invocation** | Calling `@Async` method from the **same class** bypasses the proxy | Call it from a **different** class (inject the service) |
| 2 | **Method not public** | Spring AOP proxies can only intercept public methods | Make the method `public` |
| 3 | **Missing @EnableAsync** | Spring doesn't know to create async proxies | Add `@EnableAsync` to a `@Configuration` class |

```java
@Service
public class MyService {
    @Async
    public void asyncMethod() { ... }
    
    public void callerMethod() {
        // ❌ This calls asyncMethod directly — NOT through proxy!
        // It runs SYNCHRONOUSLY despite @Async
        this.asyncMethod();
    }
}

// ✅ Fix: Inject the service into another class
@Service
public class OtherService {
    @Autowired private MyService myService;
    
    public void doWork() {
        myService.asyncMethod();  // Goes through proxy → truly async!
    }
}
```

> **Key concept:** `@Async`, `@Transactional`, `@Cacheable` all work through **Spring AOP proxies**. When you call a method within the same class, you bypass the proxy and the annotation has no effect.

### 7.2 `@Cacheable` not updating — why?

**Explanation:** Same root cause as `@Async` — **self-invocation bypasses the proxy**. Additionally:

- Ensure cache key objects implement `hashCode()` and `equals()` correctly
- Check if TTL (Time-To-Live) has expired
- Verify Redis is running and connected
- Use `@CachePut` for updates, not `@Cacheable`

### 7.3 Transaction Management in Spring Batch

**Explanation:** Spring Batch manages transactions at the **chunk level**, not at the item level:

- Each chunk (e.g., 100 items) is processed in **one transaction**
- If writing fails for any item in the chunk → the **entire chunk rolls back**
- Successfully written chunks are **already committed** and safe
- You can configure `retryLimit` and `skipLimit` for fault tolerance

```java
@Bean
public Step step() {
    return stepBuilderFactory.get("step1")
        .<Input, Output>chunk(100)        // Process 100 items per transaction
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .faultTolerant()
        .skipLimit(10)                     // Skip up to 10 bad records
        .skip(ParseException.class)        // Skip only ParseExceptions
        .retryLimit(3)                     // Retry up to 3 times on failure
        .retry(DeadlockLoserDataAccessException.class)  // Retry on deadlocks
        .build();
}
```

---

## 8. Key Topics & Explanations

### 8.1 Why Redis over a simple Map for caching?

**Explanation:**

| Feature | `HashMap` (In-memory) | Redis |
|---------|----------------------|-------|
| **Shared across instances** | ❌ Each app instance has its own | ✅ ALL instances share one cache |
| **Survives restart** | ❌ Lost when JVM restarts | ✅ Persisted to disk (configurable) |
| **Eviction policies** | ❌ Manual (or use LRU map) | ✅ LRU, LFU, TTL built-in |
| **Memory limit** | Limited by JVM heap | Can handle GBs on a dedicated server |
| **Data structures** | Only Map | Strings, Lists, Sets, Sorted Sets, Hashes, Streams |

> **Rule of thumb:** Use `HashMap` for single-instance apps or caching within a single request. Use **Redis** when you have multiple app instances (microservices), need cache to survive restarts, or need sophisticated eviction.

### 8.2 Scheduler Thread Pool — critical warning

**Explanation:** By default, Spring uses a **single-threaded** scheduler. If one scheduled task takes too long or hangs, **ALL other scheduled tasks stop running.**

```java
// ❌ Default: One thread does everything
// Task A hangs → Task B, Task C never execute

// ✅ Fix: Configure a multi-threaded scheduler
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.setScheduler(Executors.newScheduledThreadPool(5));
        // Now 5 threads handle scheduled tasks independently
    }
}
```

> **Always do this in production.** A health check that hangs should NOT prevent your nightly cleanup job from running.

### 8.3 When to use @Async vs Spring Events vs Message Queues?

**Explanation:**

| Approach | Scope | Persistence | When to use |
|----------|-------|-------------|-------------|
| **@Async** | Same JVM | ❌ No | Fire-and-forget tasks within the same app (send email, process file) |
| **Spring Events** | Same JVM | ❌ No | Decouple components within the same app (Observer pattern) |
| **Message Queue** (RabbitMQ, Kafka) | Across services | ✅ Yes | Cross-service communication, guaranteed delivery, retry, scaling |

> Use `@Async` for simple background tasks. Use Spring Events when you want decoupling. Use a message queue when you need reliability, cross-service communication, or the task is critical enough that it must not be lost if the server crashes.
