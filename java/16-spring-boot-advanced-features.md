# Spring Boot Advanced Features - Complete Guide

**Target:** Skill enhancement for WITCH companies and beyond (Senior Roles).  
**Covers:** Async Processing, Scheduling, Caching (Redis), Batch Processing, and Events.

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

Executes methods in a separate thread.

### Configuration
1.  Enable: `@EnableAsync` on Main or Config class.
2.  Usage:
    ```java
    @Async
    public CompletableFuture<String> processData() {
        Thread.sleep(1000); // Simulate blocking work
        return CompletableFuture.completedFuture("Done");
    }
    ```

### Thread Pool Executor
By default, Spring uses `SimpleAsyncTaskExecutor` (No pool, new thread every time!). **Always configure a custom `Executor` bean.**

```java
@Bean(name = "taskExecutor")
public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("Async-");
    executor.initialize();
    return executor;
}
```

---

## 2. Scheduling (@Scheduled)

Executes methods periodically.

### Configuration
1.  Enable: `@EnableScheduling`.
2.  Usage:
    *   **Fixed Rate**: Always runs every `N` milliseconds. (If execution takes long, subsequent executions are delayed).
        ```java
        @Scheduled(fixedRate = 5000) // Every 5s
        public void fixedRate() { ... }
        ```
    *   **Fixed Delay**: Waits `N` milliseconds *after* completion of previous execution.
        ```java
        @Scheduled(fixedDelay = 5000)
        public void fixedDelay() { ... }
        ```
    *   **Cron**: Unix-like syntax.
        ```java
        @Scheduled(cron = "0 0 12 * * ?") // Daily at 12:00 PM
        public void cronJob() { ... }
        ```

---

## 3. Spring Caching (Redis)

Improves performance by storing results of expensive operations.

### Configuration
1.  Enable: `@EnableCaching`.
2.  Dependencies: `spring-boot-starter-data-redis` + `commons-pool2`.
3.  Redis Config (`application.properties`):
    ```properties
    spring.cache.type=redis
    spring.redis.host=localhost
    spring.redis.port=6379
    ```

### Annotations
*   `@Cacheable("users")`: Check cache before executing method. If key exists, return value. If not, execute method and store result.
*   `@CachePut("users")`: Always execute method and update cache.
*   `@CacheEvict("users")`: Remove entry from cache. (`allEntries=true` to clear all).

---

## 4. Spring Batch

Framework for robust execution of batch jobs (Large volume data processing).

### Components
1.  **Job**: Encapsulates the entire batch process.
2.  **Step**: Distinct phase of a job (Read -> Process -> Write).
3.  **ItemReader**: Reads data (CSV, DB).
4.  **ItemProcessor**: Transforms data (Business logic).
5.  **ItemWriter**: Writes data (DB, File).
6.  **JobLauncher**: Starts a job.

### Chunk Processing
Reads items one by one, processes them, aggregates them into chunks, then writes the chunk in a single transaction.

---

## 5. Spring Events (ApplicationEvent)

Decouple components using Observer pattern.

1.  **Event**: Define a class extending `ApplicationEvent` (or any POJO).
2.  **Publisher**: `ApplicationEventPublisher.publishEvent(new MyEvent(...))`.
3.  **Listener**: `@EventListener` annotation on method.

```java
@EventListener
@Async // Good practice to handle events asynchronously
public void handleEvent(UserCreatedEvent event) {
    emailService.sendWelcomeEmail(event.getUser());
}
```

---

## 6. WebFlux (Reactive Stack)

Non-blocking, event-driven alternative to Spring MVC.

*   **Mono**: 0 or 1 item.
*   **Flux**: 0 to N items.
*   **Backpressure**: Consumer controls the flow rate.
*   **Netty**: Default embedded server (High concurrency).

---

## 7. Complex Technical Scenarios

### Topic 1: `@Async` not working?
**Answer:**
Common pitfalls:
1.  Calling `@Async` method from within the **same class** (Self-invocation bypasses proxy).
2.  Method is not `public`.
3.  `@EnableAsync` not added.

### Topic 2: `Cacheable` not updating?
**Answer:**
Same as Async. Self-invocation bypasses proxy. Also, check if object keys implement `hashCode()` and `equals()` correctly.

### Topic 3: Transaction Management in Batch?
**Answer:**
Spring Batch handles transactions at the **Chunk** level. If an error occurs during writing, the entire chunk is rolled back. You can configure `retryLimit` and `skipLimit` for fault tolerance.

---

## 8. Key Topics & Explanations

### Topic 1: Why Redis over Map?
**Answer:**
*   **Distributed**: Multiple instances of your app can share the cache.
*   **Persistence**: Data survives app restart.
*   **Memory Management**: Sophisticated eviction policies (LRU, LFU).
*   **Size**: Can handle GBs of data.

### Topic 2: Scheduler Thread Pool?
**Answer:**
By default, Spring uses a **single-threaded** scheduler. If one task hangs, all scheduled tasks delay. Always configure a `ThreadPoolTaskScheduler` bean with pool size > 1.
