# Java Technical Proficiency Bank - Ultimate Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Purpose:** A comprehensive repository of 50+ technical inquiries and scenarios covering the entire Java ecosystem.

---

## Table of Contents

1. [Core Java Fundamentals](#1-core-java-fundamentals)
2. [String Handling](#2-string-handling)
3. [Exception Handling](#3-exception-handling)
4. [Collections Framework](#4-collections-framework)
5. [Multithreading & Concurrency](#5-multithreading--concurrency)
6. [Java 8+ Features](#6-java-8-features)
7. [Spring Framework Core](#7-spring-framework-core)
8. [Spring Boot](#8-spring-boot)
9. [Microservices & System Design](#9-microservices--system-design)

---

## 1. Core Java Fundamentals

### Inquiry 1: OOP Principles
**Scenario:** Explain Polymorphism and Encapsulation with real-world analogy.
**Explanation:** 
*   **Encapsulation**: Wrapping data (variables) and code (methods) together as a single unit. *Analogy*: A capsule hiding medicine. You only see the capsule, not the chemicals inside.
*   **Polymorphism**: One task is performed in different ways. *Analogy*: A man acts as a father at home, an employee at office, and a customer at a shop.

### Inquiry 2: Java Pass-by-Value
**Scenario:** Is Java pass-by-reference?
**Explanation:** No. Java is strictly **pass-by-value**.
*   **Primitives**: Value is copied.
*   **Objects**: The **reference (address)** is passed by value. If you change the object's fields, the original object changes. If you reassign the reference `obj = new Class()`, the original reference is untouched.

### Inquiry 3: Object Class Methods
**Scenario:** List important methods of `Object` class.
**Explanation:** `toString()`, `equals()`, `hashCode()`, `getClass()`, `clone()`, `finalize()`, `wait()`, `notify()`, `notifyAll()`.

### Inquiry 4: Marker Interface
**Scenario:** What is it? Examples?
**Explanation:** An empty interface signaling the JVM to perform specific operations.
*   `Serializable`, `Cloneable`, `Remote`, `RandomAccess`.

### Inquiry 5: Shallow vs Deep Copy
**Scenario:** Difference in cloning?
**Explanation:**
*   **Shallow**: Copies field values. If field is a reference, copies the reference (both objects point to same dependency). Default `clone()`.
*   **Deep**: Creates new instance for referenced objects too. Implemented manually or via Serialization.

### Inquiry 6: `static` keyword
**Scenario:** Can we override static methods?
**Explanation:** No. Static methods are bound at **compile time** (Method Hiding). Instance methods are bound at runtime (Overriding).

---

## 2. String Handling

### Inquiry 7: String Immutability
**Scenario:** Why is String immutable?
**Explanation:** Security (DB/Network connections), Thread Safety, Caching (HashCode), and String Pool efficiency.

### Inquiry 8: `String` vs `StringBuffer` vs `StringBuilder`
**Scenario:** When to use which?
**Explanation:**
*   `String`: Immutable. Slow for concatenation.
*   `StringBuffer`: Mutable. **Synchronized** (Thread-safe). Slower.
*   `StringBuilder`: Mutable. **Not Synchronized**. Fastest. Use for local string manipulation.

### Inquiry 9: String Pool (Interning)
**Scenario:** `new String("abc")` vs `"abc"`?
**Explanation:**
*   `String s = "abc"` checks Pool. If exists, returns reference.
*   `String s = new String("abc")` forces creation of new object in Heap, *and* keeps "abc" literal in Pool. (Creates 2 objects if "abc" wasn't in pool).

---

## 3. Exception Handling

### Inquiry 10: Checked vs Unchecked
**Scenario:** Difference?
**Explanation:**
*   **Checked** (`IOException`, `SQLException`): Compiler forces you to handle (`try-catch`) or declare (`throws`). Represents external failures.
*   **Unchecked** (`NullPointerException`, `IndexOutOfBounds`): Runtime exceptions. Programming errors. Compiler ignores them.

### Inquiry 11: `throw` vs `throws`
**Scenario:** Usage?
**Explanation:**
*   `throw`: Used *inside* a method to explicitly throw an exception instance.
*   `throws`: Used in *method signature* to declare potential exceptions.

### Inquiry 12: `finally` block
**Scenario:** When does it NOT execute?
**Explanation:** 
1.  `System.exit(0)` is called.
2.  JVM crashes.
3.  Infinite loop in `try` block.

---

## 4. Collections Framework

### Inquiry 13: `HashMap` Internals
**Scenario:** Collision handling?
**Explanation:** Uses Array of Nodes (Buckets). `hashCode()` determines index. `equals()` checks key uniqueness. Collisions handled via LinkedList (Java 8+: Balanced Tree if >8 nodes) bucket.

### Inquiry 14: `ArrayList` vs `LinkedList`
**Scenario:** Performance difference?
**Explanation:**
*   `ArrayList`: Dynamic Array. Fast Access (O(1)). Slow Insert/Delete (Shifting needed).
*   `LinkedList`: Doubly Linked List. Slow Access (O(n)). Fast Insert/Delete.

### Inquiry 15: `ConcurrentHashMap`
**Scenario:** How is it thread-safe?
**Explanation:** 
*   Java 7: Segment locking.
*   Java 8+: **CAS (Compare And Swap)** and `synchronized` only on specific bucket nodes. Allows concurrent reads without locking.

### Inquiry 16: `fail-fast` vs `fail-safe`
**Scenario:** Iterators?
**Explanation:**
*   **Fail-fast** (`ArrayList`): Throws `ConcurrentModificationException` if modified during iteration.
*   **Fail-safe** (`CopyOnWriteArrayList`): Iterates over a snapshot. No exception.

### Inquiry 17: `Comparator` vs `Comparable`
**Scenario:** Difference?
**Explanation:**
*   `Comparable`: Natural ordering. Modifies class (`impediments Comparable`). Method: `compareTo()`.
*   `Comparator`: Custom ordering. Separate class/lambda. Method: `compare()`.

---

## 5. Multithreading & Concurrency

### Inquiry 18: Thread Lifecycle
**Scenario:** States?
**Explanation:** New -> Runnable -> Running -> Blocked/Waiting -> Terminated.

### Inquiry 19: `wait()` vs `sleep()`
**Scenario:** Key differences?
**Explanation:**
*   `wait()`: Releases lock. Object method. Inter-thread communication.
*   `sleep()`: Holds lock. Thread method. Time delay.

### Inquiry 20: Deadlock
**Scenario:** How to fix it?
**Explanation:** Occurs when two threads wait for each other's locks forever.
**Prevention**: Avoid nested locks, Lock Ordering (always acquire Lock A then Lock B), usage of `tryLock()`.

### Inquiry 21: `ExecutorService`
**Scenario:** Why use it over `new Thread()`?
**Explanation:** Thread pooling. Reuses threads (expensive to create). Manages lifecycle. `FixedThreadPool`, `CachedThreadPool`.

### Inquiry 22: `volatile` keyword
**Scenario:** Visibility vs Atomicity?
**Explanation:** `volatile` guarantees visibility (read from RAM, not cache). Does **not** guarantee atomicity `count++`.

---

## 6. Java 8+ Features

### Inquiry 23: Functional Interfaces
**Scenario:** What are they?
**Explanation:** Interface with exactly one abstract method. Can have default/static methods. Used for Lambda expressions. (`Runnnable`, `Callable`, `Comparator`).

### Inquiry 24: `Stream` API
**Scenario:** `map()` vs `flatMap()`?
**Explanation:**
*   `map`: Transforms one-to-one (`Stream<String>` -> `Stream<Integer>`).
*   `flatMap`: Transforms one-to-many (`Stream<List<String>>` -> `Stream<String>`). Flattens nested structures.

### Inquiry 25: `Optional` Class
**Scenario:** Purpose?
**Explanation:** Avoids `NullPointerException`. Forces developer to handle empty cases (`isPresent()`, `orElse()`, `orElseThrow()`).

### Inquiry 26: Method References
**Scenario:** Syntax `::`?
**Explanation:** Shorthand for Lambdas calling a specific method. `System.out::println`.

---

## 7. Spring Framework Core

### Inquiry 27: Bean Scopes
**Scenario:** List all.
**Explanation:** Singleton (Default), Prototype, Request, Session, GlobalSession, Application, WebSocket.

### Inquiry 28: Dependency Injection Types
**Scenario:** Constructor vs Setter?
**Explanation:**
*   **Constructor**: Mandatory dependencies. Immutable. (Best Practice).
*   **Setter**: Optional dependencies.

### Inquiry 29: Bean Lifecycle
**Scenario:** Order of execution?
**Explanation:** Instantiate -> Populate Properties -> `setBeanName` -> `setBeanFactory` -> `postProcessBeforeInitialization` -> `@PostConstruct` -> `afterPropertiesSet` -> Custom Init -> `postProcessAfterInitialization`.

### Inquiry 30: Circular Dependency
**Scenario:** How does Spring handle it?
**Explanation:**
*   Constructor Injection: Fails at startup.
*   Setter Injection: Solved by creating beans first, then injecting.
*   **Solution**: Use `@Lazy`.

---

## 8. Spring Boot

### Inquiry 31: `@SpringBootApplication`
**Scenario:** Internals?
**Explanation:** `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`.

### Inquiry 32: Auto-Configuration
**Scenario:** How does it work?
**Explanation:** Checks classpath (libs present). Checks `META-INF/spring.factories`. Configures beans if conditions met (`@ConditionalOnClass`).

### Inquiry 33: Profiles
**Scenario:** Managing Dev/Prod configs?
**Explanation:** `application-dev.properties`. Activate with `-Dspring.profiles.active=dev`.

### Inquiry 34: Embedded Server
**Scenario:** Can we change it?
**Explanation:** Yes. Exclude `tomcat-starter` dependency, add `jetty-starter`.

---

## 9. Microservices & System Design

### Inquiry 35: CAP Theorem
**Scenario:** Implications?
**Explanation:** Consistency, Availability, Partition Tolerance. Pick two. Microservices usually pick AP (Availability) over CP.

### Inquiry 36: SAGA Pattern
**Scenario:** Distributed Transactions?
**Explanation:** Sequence of local transactions. If one fails, execute Compensating Transactions to undo changes.

### Inquiry 37: Circuit Breaker
**Scenario:** Resilience?
**Explanation:** Stops cascading failures. Open state (fail fast) -> Half-Open (test) -> Closed (normal). Resilience4j.

### Inquiry 38: Service Discovery
**Scenario:** Why needed?
**Explanation:** Dynamic IP addresses in cloud. Services register to Eureka/Consul. Clients lookup generic names.

### Inquiry 39: API Gateway
**Scenario:** Role?
**Explanation:** Single entry point. Routing, Auth, Rate Limiting, Logging.

### Inquiry 40: Config Server
**Scenario:** External Config?
**Explanation:** Centralized configuration for all microservices. dynamic refresh (`@RefreshScope`).

### Inquiry 41: Idempotency
**Scenario:** HTTP Methods?
**Explanation:** GET/PUT/DELETE are idempotent. POST is not.

### Inquiry 42: OAuth2 Flows
**Scenario:** Types?
**Explanation:** Authorization Code (Standard), Client Credentials (Machine-to-Machine), Password (Deprecated).

### Inquiry 43: JWT Anatomy
**Scenario:** Parts?
**Explanation:** Header . Payload (Claims) . Signature.

### Inquiry 44: Solid Principles
**Scenario:** Liskov Substitution?
**Explanation:** Subtypes must be substitutable for base types without breaking behavior.

### Inquiry 45: Design Patterns
**Scenario:** Singleton Implementation?
**Explanation:** Enum Singleton is best (Thread safe, Serialization safe).

### Inquiry 46: 12 Factor App
**Scenario:** Name a few?
**Explanation:** Codebase, Dependencies, Config, Backing Services, Build/Release/Run, Logs, Admin Processes.

### Inquiry 47: Docker vs VM
**Scenario:** Difference?
**Explanation:** VM has full OS. Docker shares Host OS Kernel (Lightweight).

### Inquiry 48: Kubernetes Pod
**Scenario:** Definition?
**Explanation:** Smallest deployable unit. Can hold multiple containers sharing network/storage.

### Inquiry 49: Rate Limiting Algorithms
**Scenario:** Examples?
**Explanation:** Token Bucket, Leaky Bucket, Fixed Window, Sliding Window.

### Inquiry 50: Caching Strategies
**Scenario:** Write-Through vs Write-Back?
**Explanation:**
*   **Write-Through**: Write to Cache AND DB. consistency. data safe.
*   **Write-Back**: Write to Cache only. Async write to DB. High throughput. Risk of data loss.
