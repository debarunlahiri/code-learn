# Java Technical Proficiency Bank - Ultimate Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Purpose:** A comprehensive repository of 150+ technical inquiries and scenarios covering the entire Java ecosystem.

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
10. [JVM Internals & Memory](#10-jvm-internals--memory)
11. [Serialization & Cloning](#11-serialization--cloning)
12. [JPA & Hibernate](#12-jpa--hibernate)
13. [REST API & HTTP](#13-rest-api--http)
14. [Tricky Java Output Questions](#14-tricky-java-output-questions)
15. [Coding Problems](#15-coding-problems)
16. [Interview Questions & Answers](#16-interview-questions--answers)
17. [Testing (JUnit, Mockito)](#17-testing-junit-mockito)
18. [Design Patterns](#18-design-patterns)
19. [Spring Data & Queries](#19-spring-data--queries)
20. [Messaging (Kafka, RabbitMQ)](#20-messaging-kafka-rabbitmq)
21. [More Coding Problems](#21-more-coding-problems)
22. [Java Gotchas & Best Practices](#22-java-gotchas--best-practices)

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

---

## 10. JVM Internals & Memory

### Inquiry 51: JVM Architecture
**Scenario:** Explain JVM components.
**Explanation:**

```
Java Source (.java)
    ↓ javac (compiler)
Bytecode (.class)
    ↓
┌─────────────── JVM ───────────────┐
│  Class Loader Subsystem           │
│    ├── Loading (Bootstrap,        │
│    │   Extension, Application)    │
│    ├── Linking (Verify, Prepare,  │
│    │   Resolve)                   │
│    └── Initialization             │
│                                   │
│  Runtime Data Areas               │
│    ├── Method Area (shared)       │
│    ├── Heap (shared)              │
│    ├── Stack (per thread)         │
│    ├── PC Register (per thread)   │
│    └── Native Method Stack        │
│                                   │
│  Execution Engine                 │
│    ├── Interpreter                │
│    ├── JIT Compiler               │
│    └── Garbage Collector          │
└───────────────────────────────────┘
```

**Simple way to remember:** Code is loaded → stored in memory → executed by engine.

### Inquiry 52: Heap vs Stack
**Scenario:** What goes where?
**Explanation:**

| Feature | Stack | Heap |
|---------|-------|------|
| **Stores** | Local variables, method calls, references | Objects, instance variables |
| **Thread** | One per thread (private) | Shared across all threads |
| **Size** | Small (default ~512KB-1MB) | Large (configurable via `-Xmx`) |
| **Speed** | Fast (LIFO push/pop) | Slower (GC manages) |
| **Error** | `StackOverflowError` | `OutOfMemoryError` |
| **Lifetime** | Method scope (auto-removed) | Until GC collects |

```java
public void example() {
    int x = 10;              // x → Stack
    String name = "Rahul";   // name (reference) → Stack, "Rahul" → String Pool (Heap)
    Employee emp = new Employee(); // emp (reference) → Stack, Employee object → Heap
}
// When method ends: x, name, emp references removed from Stack
// Employee object stays in Heap until GC collects it
```

### Inquiry 53: Garbage Collection
**Scenario:** How does GC work?
**Explanation:**

**Heap is divided into generations:**
*   **Young Generation** — New objects. Minor GC (fast, frequent).
    *   **Eden** — Objects created here first.
    *   **Survivor S0, S1** — Objects that survive Minor GC move here.
*   **Old Generation (Tenured)** — Long-lived objects. Major GC (slow, infrequent).
*   **Metaspace** (Java 8+) — Class metadata. Replaced PermGen.

**GC Process (Simplified):**
1. Object created in Eden.
2. Eden full → Minor GC → alive objects move to S0.
3. Next Minor GC → alive from Eden + S0 move to S1.
4. After surviving ~15 GCs → promoted to Old Gen.
5. Old Gen full → Major GC (Stop-the-World pause).

```java
// Force GC request (NOT guaranteed)
System.gc();
Runtime.getRuntime().gc();

// Check memory
Runtime rt = Runtime.getRuntime();
System.out.println("Max: " + rt.maxMemory());     // -Xmx
System.out.println("Total: " + rt.totalMemory());  // Currently allocated
System.out.println("Free: " + rt.freeMemory());    // Free in allocated
```

### Inquiry 54: GC Algorithms
**Scenario:** Types of Garbage Collectors?
**Explanation:**

| GC | Description | Best For |
|----|-------------|----------|
| **Serial GC** | Single thread. Stop-the-world. `-XX:+UseSerialGC` | Small apps, single CPU |
| **Parallel GC** | Multiple threads for GC. Default in Java 8. `-XX:+UseParallelGC` | Throughput-focused apps |
| **G1 GC** | Divides heap into regions. Default in Java 9+. `-XX:+UseG1GC` | Large heaps, low latency |
| **ZGC** | Ultra-low pause (<10ms). Java 11+. `-XX:+UseZGC` | Very large heaps (TB scale) |
| **Shenandoah** | Concurrent compaction. Java 12+. | Low-latency apps |

### Inquiry 55: Memory Leaks in Java
**Scenario:** How can Java have memory leaks if GC exists?
**Explanation:** GC only collects objects with **no references**. If you hold references unnecessarily, GC can't collect them.

**Common causes:**
*   **Static collections** — `static List<Object> cache = new ArrayList<>();` keeps growing.
*   **Unclosed resources** — Streams, connections not closed.
*   **Listeners not removed** — Event listeners holding references.
*   **ThreadLocal not cleaned** — In thread pools, thread is reused but ThreadLocal value persists.
*   **Inner class holding outer reference** — Anonymous inner class holds implicit reference to outer class.

```java
// Memory leak example
public class Cache {
    private static final Map<String, Object> cache = new HashMap<>();

    public void addToCache(String key, Object value) {
        cache.put(key, value); // Never removed! Grows forever.
    }
}

// Fix: Use WeakHashMap or set max size
private static final Map<String, Object> cache = new WeakHashMap<>();
```

### Inquiry 56: JVM Flags
**Scenario:** Important JVM parameters?
**Explanation:**

| Flag | Purpose | Example |
|------|---------|---------|
| `-Xms` | Initial heap size | `-Xms256m` |
| `-Xmx` | Maximum heap size | `-Xmx1024m` |
| `-Xss` | Stack size per thread | `-Xss512k` |
| `-XX:MaxMetaspaceSize` | Max metaspace | `-XX:MaxMetaspaceSize=256m` |
| `-XX:+PrintGCDetails` | Print GC logs | Debug GC behavior |
| `-XX:+HeapDumpOnOutOfMemoryError` | Dump heap on OOM | Post-mortem analysis |

### Inquiry 57: ClassLoader
**Scenario:** Types and delegation model?
**Explanation:**

```
Bootstrap ClassLoader (C/C++)
    ↓ loads rt.jar (java.lang, java.util)
Extension ClassLoader (Java)
    ↓ loads jre/lib/ext/*.jar
Application ClassLoader (Java)
    ↓ loads classpath (your code)
Custom ClassLoader (optional)
```

**Delegation Model:** Child asks Parent first. If Parent can't load, Child tries.
*   Prevents duplicate class loading.
*   Ensures core Java classes (like `String`) are always loaded by Bootstrap (can't be tampered).

### Inquiry 58: `finalize()` vs `try-with-resources`
**Scenario:** Which to use for cleanup?
**Explanation:**
*   `finalize()` — **Deprecated** (Java 9). Called by GC before collecting object. No guarantee when/if it runs. Never rely on it.
*   `try-with-resources` (Java 7+) — **Always use this.** Auto-closes resources implementing `AutoCloseable`.

```java
// BAD: finalize (don't use)
@Override
protected void finalize() throws Throwable {
    connection.close(); // May never execute!
}

// GOOD: try-with-resources
try (Connection conn = getConnection();
     PreparedStatement ps = conn.prepareStatement(sql);
     ResultSet rs = ps.executeQuery()) {
    while (rs.next()) { /* process */ }
} // All three auto-closed here, even if exception occurs
```

---

## 11. Serialization & Cloning

### Inquiry 59: What is Serialization?
**Scenario:** Why do we need it?
**Explanation:** Converting an object into a **byte stream** so it can be saved to file, sent over network, or stored in cache.

```java
// Object → Bytes (Serialize)
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("emp.ser"));
oos.writeObject(employee);

// Bytes → Object (Deserialize)
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("emp.ser"));
Employee emp = (Employee) ois.readObject();
```

**Requirements:**
*   Class must implement `Serializable` (marker interface).
*   All fields must be serializable OR marked `transient`.

### Inquiry 60: `transient` Keyword
**Scenario:** What does it do?
**Explanation:** Marks a field to be **excluded** from serialization. The field gets default value after deserialization.

```java
class User implements Serializable {
    String username;           // Serialized
    transient String password; // NOT serialized (security)
    transient int loginCount;  // NOT serialized
}

// After deserialization:
// username = "rahul" (preserved)
// password = null (default for String)
// loginCount = 0 (default for int)
```

### Inquiry 61: `serialVersionUID`
**Scenario:** Why is it important?
**Explanation:** A version number for the serialized class. If you serialize with one version and deserialize with a different version (class changed), Java throws `InvalidClassException`.

```java
class Employee implements Serializable {
    private static final long serialVersionUID = 1L; // Always define explicitly
    String name;
    int age;
}
```

**What happens without it?**
*   JVM auto-generates one based on class structure.
*   If you add/remove a field, the auto-generated UID changes → deserialization fails.
*   Explicitly defining it lets you control backward compatibility.

### Inquiry 62: Serialization Gotchas
**Scenario:** Tricky interview questions.
**Explanation:**

```java
// Q: Can static fields be serialized?
// A: NO. Static belongs to class, not object. Not part of object state.

// Q: What if parent class is NOT Serializable?
// A: Parent fields get DEFAULT values after deserialization.
//    Parent must have a NO-ARG constructor (else RuntimeException).

// Q: Can we customize serialization?
// A: Yes. Define these methods in your class:
private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    oos.writeObject(encrypt(password)); // Custom: encrypt before saving
}

private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    this.password = decrypt((String) ois.readObject()); // Custom: decrypt after reading
}
```

### Inquiry 63: Externalizable vs Serializable
**Scenario:** Difference?
**Explanation:**

| Feature | Serializable | Externalizable |
|---------|-------------|----------------|
| **Marker** | Yes (empty interface) | No (has methods) |
| **Control** | JVM handles everything | You write `readExternal()` / `writeExternal()` |
| **Performance** | Slower (serializes all non-transient fields) | Faster (you choose what to serialize) |
| **Constructor** | Not called during deserialization | **Public no-arg constructor required** |
| **Default** | All fields serialized | Nothing serialized unless you code it |

### Inquiry 64: Cloning Deep Dive
**Scenario:** How to implement deep clone?
**Explanation:**

```java
// Method 1: Override clone() manually
class Address implements Cloneable {
    String city;

    @Override
    protected Address clone() throws CloneNotSupportedException {
        return (Address) super.clone();
    }
}

class Employee implements Cloneable {
    String name;
    Address address;

    @Override
    protected Employee clone() throws CloneNotSupportedException {
        Employee cloned = (Employee) super.clone(); // Shallow copy
        cloned.address = this.address.clone();       // Deep copy the reference
        return cloned;
    }
}

// Method 2: Using Serialization (easiest for complex objects)
public static <T extends Serializable> T deepClone(T obj) {
    try {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(obj);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        return (T) new ObjectInputStream(bis).readObject();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

// Method 3: Copy Constructor (recommended)
class Employee {
    String name;
    Address address;

    // Copy constructor
    public Employee(Employee other) {
        this.name = other.name;
        this.address = new Address(other.address); // Deep copy
    }
}
```

### Inquiry 65: Singleton & Serialization Problem
**Scenario:** How does serialization break Singleton?
**Explanation:** Deserializing creates a **new object**, breaking the single-instance guarantee.

```java
// Problem:
Singleton s1 = Singleton.getInstance();
// Serialize s1 to file, then deserialize
Singleton s2 = (Singleton) ois.readObject();
System.out.println(s1 == s2); // FALSE! Two different objects

// Fix: Add readResolve() method
class Singleton implements Serializable {
    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() { return INSTANCE; }

    // This method is called during deserialization
    private Object readResolve() {
        return INSTANCE; // Return existing instance instead of new one
    }
}
```

**Best approach:** Use `enum` Singleton — immune to serialization and reflection attacks.

```java
enum Singleton {
    INSTANCE;
    // methods here
}
```

---

## 12. JPA & Hibernate

### Inquiry 66: JPA vs Hibernate
**Scenario:** What's the difference?
**Explanation:**
*   **JPA** (Java Persistence API) — A **specification** (set of interfaces/annotations). Defines rules for ORM.
*   **Hibernate** — An **implementation** of JPA. The actual library that does the work.
*   *Analogy:* JPA is the driving license rules. Hibernate is the actual car you drive.

### Inquiry 67: Entity Lifecycle States
**Scenario:** What are the states of an entity?
**Explanation:**

```
New/Transient ──persist()──► Managed ──detach()/close()──► Detached
                               │                              │
                            flush()                       merge()
                               │                              │
                               ▼                              ▼
                           Database                        Managed
                               │
                          remove()
                               │
                               ▼
                           Removed
```

| State | Description |
|-------|-------------|
| **Transient** | New object. Not associated with any session. Not in DB. |
| **Managed/Persistent** | Attached to session. Changes auto-synced to DB on flush. |
| **Detached** | Was managed, but session closed. Changes NOT tracked. |
| **Removed** | Marked for deletion. Will be deleted on flush/commit. |

### Inquiry 68: `get()` vs `load()` (Hibernate)
**Scenario:** When to use which?
**Explanation:**
*   `get()` — Hits DB **immediately**. Returns `null` if not found. Use when you're not sure if record exists.
*   `load()` — Returns a **proxy** (lazy). Hits DB only when you access a property. Throws `ObjectNotFoundException` if not found. Use when you're sure record exists.

```java
// get() — immediate DB hit
Employee emp = session.get(Employee.class, 1); // SQL fired NOW
if (emp == null) { /* handle not found */ }

// load() — lazy proxy
Employee emp = session.load(Employee.class, 1); // No SQL yet
System.out.println(emp.getName()); // SQL fired NOW (when accessed)
```

### Inquiry 69: Lazy vs Eager Loading
**Scenario:** What's the N+1 problem?
**Explanation:**
*   **Lazy** (`FetchType.LAZY`) — Related data loaded **only when accessed**. Default for `@OneToMany`, `@ManyToMany`.
*   **Eager** (`FetchType.EAGER`) — Related data loaded **immediately** with parent. Default for `@ManyToOne`, `@OneToOne`.

```java
@Entity
class Department {
    @OneToMany(fetch = FetchType.LAZY) // Employees loaded only when dept.getEmployees() called
    List<Employee> employees;
}
```

**N+1 Problem:**
```java
// 1 query to fetch 10 departments
List<Department> depts = session.createQuery("FROM Department").list();

// N queries (one per department) to fetch employees — SLOW!
for (Department d : depts) {
    d.getEmployees().size(); // Each call fires a separate SQL
}
// Total: 1 + 10 = 11 queries!

// Fix 1: JOIN FETCH
"SELECT d FROM Department d JOIN FETCH d.employees"

// Fix 2: @BatchSize
@BatchSize(size = 10)
List<Employee> employees;

// Fix 3: Entity Graph
@EntityGraph(attributePaths = {"employees"})
List<Department> findAll();
```

### Inquiry 70: Hibernate Caching
**Scenario:** First Level vs Second Level Cache?
**Explanation:**

| Feature | First Level (L1) | Second Level (L2) |
|---------|------------------|-------------------|
| **Scope** | Session (per transaction) | SessionFactory (shared across sessions) |
| **Default** | Always ON (can't disable) | OFF (must configure) |
| **Provider** | Built-in | EhCache, Redis, Hazelcast |
| **Eviction** | Session close | TTL / manual eviction |

```java
// L1 Cache demo
Session session = factory.openSession();
Employee e1 = session.get(Employee.class, 1); // SQL fired
Employee e2 = session.get(Employee.class, 1); // NO SQL — from L1 cache
System.out.println(e1 == e2); // true (same object)

// L2 Cache config
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Employee { }
```

### Inquiry 71: `@Transactional` Annotation
**Scenario:** How does it work?
**Explanation:** Spring creates a **proxy** around the method. Proxy starts transaction before method, commits after, rollbacks on exception.

```java
@Transactional
public void transferMoney(int from, int to, double amount) {
    accountRepo.debit(from, amount);   // If this succeeds...
    accountRepo.credit(to, amount);    // ...but this fails → BOTH rolled back
}
```

**Important rules:**
*   Only works on **public** methods (proxy limitation).
*   **Self-invocation doesn't work** — calling `this.method()` bypasses proxy.
*   Default: rolls back only on **unchecked exceptions** (`RuntimeException`).
*   Use `@Transactional(rollbackFor = Exception.class)` for checked exceptions.

### Inquiry 72: Propagation Types
**Scenario:** What if a transactional method calls another transactional method?
**Explanation:**

| Propagation | Behavior |
|-------------|----------|
| **REQUIRED** (default) | Join existing transaction. Create new if none exists. |
| **REQUIRES_NEW** | Always create new transaction. Suspend existing. |
| **MANDATORY** | Must have existing transaction. Throws exception if none. |
| **SUPPORTS** | Join if exists. Run without transaction if none. |
| **NOT_SUPPORTED** | Suspend existing transaction. Run without. |
| **NEVER** | Throws exception if transaction exists. |
| **NESTED** | Create savepoint within existing transaction. |

```java
@Transactional(propagation = Propagation.REQUIRED) // default
public void methodA() {
    methodB(); // Joins methodA's transaction
}

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void methodB() {
    // Runs in its OWN transaction (methodA's is suspended)
}
```

### Inquiry 73: JPA Relationships
**Scenario:** Mapping types?
**Explanation:**

```java
// One-to-One
@Entity
class User {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    Address address;
}

// One-to-Many / Many-to-One
@Entity
class Department {
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    List<Employee> employees;
}

@Entity
class Employee {
    @ManyToOne
    @JoinColumn(name = "dept_id")
    Department department; // Owning side (has FK)
}

// Many-to-Many
@Entity
class Student {
    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    List<Course> courses;
}
```

**Key rule:** `mappedBy` goes on the **non-owning** side (the side without the foreign key).

---

## 13. REST API & HTTP

### Inquiry 74: REST Principles
**Scenario:** What makes an API RESTful?
**Explanation:**
*   **Stateless** — Server doesn't store client state between requests.
*   **Client-Server** — Separation of concerns.
*   **Uniform Interface** — Consistent URL patterns, HTTP methods.
*   **Cacheable** — Responses can be cached.
*   **Layered System** — Client doesn't know if talking to server or proxy.

### Inquiry 75: HTTP Methods
**Scenario:** When to use which?
**Explanation:**

| Method | Purpose | Idempotent | Safe | Request Body |
|--------|---------|-----------|------|-------------|
| **GET** | Read/Fetch data | Yes | Yes | No |
| **POST** | Create new resource | No | No | Yes |
| **PUT** | Update entire resource (replace) | Yes | No | Yes |
| **PATCH** | Update partial resource | No | No | Yes |
| **DELETE** | Remove resource | Yes | No | Optional |

**Idempotent** = Same request multiple times → same result.
**Safe** = Doesn't modify server state.

```
GET    /api/employees          → List all
GET    /api/employees/1        → Get employee 1
POST   /api/employees          → Create new employee
PUT    /api/employees/1        → Replace employee 1 entirely
PATCH  /api/employees/1        → Update specific fields of employee 1
DELETE /api/employees/1        → Delete employee 1
```

### Inquiry 76: HTTP Status Codes
**Scenario:** Important codes to know?
**Explanation:**

| Code | Meaning | When to Use |
|------|---------|-------------|
| **200** | OK | Successful GET/PUT/PATCH |
| **201** | Created | Successful POST (resource created) |
| **204** | No Content | Successful DELETE (nothing to return) |
| **400** | Bad Request | Invalid input / validation failure |
| **401** | Unauthorized | Not authenticated (no/invalid token) |
| **403** | Forbidden | Authenticated but no permission |
| **404** | Not Found | Resource doesn't exist |
| **405** | Method Not Allowed | Wrong HTTP method for endpoint |
| **409** | Conflict | Duplicate resource / version conflict |
| **500** | Internal Server Error | Unhandled server exception |
| **502** | Bad Gateway | Upstream server failed |
| **503** | Service Unavailable | Server overloaded / maintenance |

### Inquiry 77: `@RestController` vs `@Controller`
**Scenario:** Difference?
**Explanation:**
*   `@Controller` — Returns a **view name** (HTML page). Used with Thymeleaf/JSP.
*   `@RestController` — Returns **data directly** (JSON/XML). Equivalent to `@Controller` + `@ResponseBody` on every method.

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @GetMapping
    public List<Employee> getAll() {
        return service.findAll(); // Returns JSON automatically
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee emp) {
        Employee saved = service.save(emp);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
```

### Inquiry 78: `@RequestBody` vs `@RequestParam` vs `@PathVariable`
**Scenario:** When to use which?
**Explanation:**

```java
// @PathVariable — from URL path
// GET /api/employees/42
@GetMapping("/{id}")
public Employee get(@PathVariable Long id) { } // id = 42

// @RequestParam — from query string
// GET /api/employees?dept=IT&page=2
@GetMapping
public List<Employee> search(
    @RequestParam String dept,           // required
    @RequestParam(defaultValue = "1") int page) { }

// @RequestBody — from request body (JSON)
// POST /api/employees with JSON body
@PostMapping
public Employee create(@RequestBody Employee emp) { }
```

### Inquiry 79: Exception Handling in REST
**Scenario:** How to return proper error responses?
**Explanation:**

```java
// Global exception handler
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        ErrorResponse error = new ErrorResponse(400, message, LocalDateTime.now());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(500, "Internal Server Error", LocalDateTime.now());
        return ResponseEntity.status(500).body(error);
    }
}
```

### Inquiry 80: Validation Annotations
**Scenario:** How to validate request data?
**Explanation:**

```java
class EmployeeDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 65, message = "Age must be at most 65")
    private int age;

    @NotNull
    @Size(min = 2, max = 10, message = "Department must be 2-10 characters")
    private String department;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;
}

// Use @Valid in controller
@PostMapping
public Employee create(@Valid @RequestBody EmployeeDTO dto) { }
```

### Inquiry 81: PUT vs PATCH
**Scenario:** Real difference with example?
**Explanation:**

```json
// Existing resource: { "id": 1, "name": "Rahul", "age": 25, "dept": "IT" }

// PUT /api/employees/1 — REPLACE entire resource
// Request body: { "name": "Rahul Kumar", "age": 26, "dept": "IT" }
// Must send ALL fields. Missing fields become null.

// PATCH /api/employees/1 — UPDATE specific fields only
// Request body: { "age": 26 }
// Only age changes. name and dept remain unchanged.
```

**In practice:** Most WITCH company projects use PUT for updates. PATCH is technically more correct for partial updates.

---

## 14. Tricky Java Output Questions

### Inquiry 82: String Pool Trap
**Scenario:** What is the output?

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = new String("Hello");
String s4 = new String("Hello").intern();

System.out.println(s1 == s2);       // ?
System.out.println(s1 == s3);       // ?
System.out.println(s1 == s4);       // ?
System.out.println(s1.equals(s3));  // ?
```

**Output:**
```
true    // Both point to same String Pool object
false   // s3 is a new object in Heap, different address
true    // intern() returns the Pool reference
true    // equals() compares content, not address
```

**Technical:** `==` compares **references** (memory address). `equals()` compares **content**. `intern()` returns the canonical Pool reference.

### Inquiry 83: Integer Caching
**Scenario:** What is the output?

```java
Integer a = 127;
Integer b = 127;
Integer c = 128;
Integer d = 128;

System.out.println(a == b);  // ?
System.out.println(c == d);  // ?
```

**Output:**
```
true    // Cached! Java caches Integer values -128 to 127
false   // 128 is outside cache range → two different objects
```

**Technical:** Java caches `Integer` objects for values **-128 to 127** (`IntegerCache`). Beyond this range, `new Integer()` is called internally. Always use `.equals()` for wrapper comparisons.

### Inquiry 84: Post-increment in Print
**Scenario:** What is the output?

```java
int i = 0;
i = i++;
System.out.println(i);  // ?

int j = 0;
j = ++j;
System.out.println(j);  // ?
```

**Output:**
```
0   // i++ returns OLD value (0), then increments, but assignment overwrites with 0
1   // ++j increments FIRST, then returns new value (1)
```

**Technical:** `i = i++` → temp = i (0), i = i+1 (1), i = temp (0). The assignment overwrites the increment.

### Inquiry 85: Finally Block Return
**Scenario:** What is the output?

```java
public static int getValue() {
    try {
        return 1;
    } finally {
        return 2;
    }
}

System.out.println(getValue());  // ?
```

**Output:**
```
2   // finally block's return OVERRIDES try block's return
```

**Technical:** `finally` always executes. If `finally` has a `return`, it replaces the `try`/`catch` return value. **Never return from finally** — it's bad practice and suppresses exceptions too.

### Inquiry 86: Exception in Catch
**Scenario:** What is the output?

```java
try {
    System.out.println("try");
    throw new RuntimeException();
} catch (Exception e) {
    System.out.println("catch");
    throw new RuntimeException();
} finally {
    System.out.println("finally");
}
```

**Output:**
```
try
catch
finally
// Then RuntimeException is thrown (from catch block)
```

**Technical:** `finally` executes even when `catch` throws an exception. The exception from `catch` is propagated after `finally` completes.

### Inquiry 87: Method Overloading with null
**Scenario:** What is the output?

```java
public static void print(Object obj) {
    System.out.println("Object");
}
public static void print(String str) {
    System.out.println("String");
}

print(null);  // ?
```

**Output:**
```
String   // Most specific type wins. String is more specific than Object.
```

**Technical:** Java picks the **most specific** matching method. `String` extends `Object`, so `String` is more specific. If you add `print(Integer i)`, it becomes **ambiguous** → compile error (both `String` and `Integer` are equally specific for `null`).

### Inquiry 88: Array Covariance
**Scenario:** What happens?

```java
Object[] arr = new String[3];
arr[0] = "Hello";    // ?
arr[1] = 42;         // ?
```

**Output:**
```
arr[0] = "Hello"  → Works fine (String into String[])
arr[1] = 42       → ArrayStoreException at RUNTIME!
```

**Technical:** Arrays are **covariant** in Java (String[] IS-A Object[]). Compiler allows it, but JVM checks actual array type at runtime. This is why generics use **type erasure** and are **invariant** (`List<String>` is NOT `List<Object>`).

### Inquiry 89: HashMap with Mutable Key
**Scenario:** What is the output?

```java
List<String> key = new ArrayList<>();
key.add("Hello");

Map<List<String>, String> map = new HashMap<>();
map.put(key, "World");

System.out.println(map.get(key));  // ?

key.add("Java");  // Mutate the key!

System.out.println(map.get(key));  // ?
```

**Output:**
```
World   // Key found — hashCode matches
null    // Key NOT found! hashCode changed after mutation
```

**Technical:** `HashMap` stores entries by `hashCode()`. When you mutate the key, its `hashCode()` changes, but the entry is still in the old bucket. The lookup searches the wrong bucket → `null`. **Never use mutable objects as HashMap keys.**

### Inquiry 90: Short-circuit Evaluation
**Scenario:** What is the output?

```java
int x = 5;
if (x > 3 || ++x > 6) {
    System.out.println(x);  // ?
}

int y = 5;
if (y > 3 | ++y > 6) {
    System.out.println(y);  // ?
}
```

**Output:**
```
5   // || short-circuits: x > 3 is true, so ++x is NEVER evaluated
6   // | does NOT short-circuit: both sides always evaluated, so ++y runs
```

**Technical:** `||` (logical OR) stops evaluating if first condition is `true`. `|` (bitwise OR) always evaluates both sides. Same applies to `&&` vs `&`.

### Inquiry 91: Autoboxing & Unboxing Trap
**Scenario:** What is the output?

```java
Integer a = null;
int b = a;  // ?
```

**Output:**
```
NullPointerException!
```

**Technical:** Unboxing `null` Integer to `int` calls `a.intValue()` → NPE. Always null-check before unboxing.

### Inquiry 92: String Concatenation with +
**Scenario:** What is the output?

```java
System.out.println(1 + 2 + "3");    // ?
System.out.println("1" + 2 + 3);    // ?
System.out.println("1" + (2 + 3));  // ?
```

**Output:**
```
33     // (1+2)=3, then 3+"3" = "33"
123    // "1"+2 = "12", then "12"+3 = "123"
15     // (2+3)=5, then "1"+5 = "15"
```

**Technical:** `+` is evaluated **left to right**. Once a `String` is encountered, everything after becomes string concatenation. Parentheses force arithmetic first.

### Inquiry 93: try-with-resources Order
**Scenario:** What is the output?

```java
class A implements AutoCloseable {
    public void close() { System.out.println("Close A"); }
}
class B implements AutoCloseable {
    public void close() { System.out.println("Close B"); }
}

try (A a = new A(); B b = new B()) {
    System.out.println("Body");
}
```

**Output:**
```
Body
Close B
Close A
```

**Technical:** Resources are closed in **reverse order** of declaration (LIFO — like a stack). B was declared last, so it's closed first.

### Inquiry 94: Polymorphism with Fields
**Scenario:** What is the output?

```java
class Parent {
    String name = "Parent";
    String getName() { return "Parent"; }
}

class Child extends Parent {
    String name = "Child";
    String getName() { return "Child"; }
}

Parent obj = new Child();
System.out.println(obj.name);       // ?
System.out.println(obj.getName());  // ?
```

**Output:**
```
Parent   // Fields are NOT polymorphic — resolved by REFERENCE type
Child    // Methods ARE polymorphic — resolved by OBJECT type (runtime)
```

**Technical:** **Field access** is determined at compile time (reference type = `Parent`). **Method calls** use dynamic dispatch at runtime (actual object = `Child`). This is a very common WITCH interview question.

### Inquiry 95: equals() and hashCode() Contract
**Scenario:** What happens if you override `equals()` but not `hashCode()`?

```java
class Employee {
    int id;
    Employee(int id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        return this.id == ((Employee) o).id;
    }
    // hashCode() NOT overridden!
}

Employee e1 = new Employee(1);
Employee e2 = new Employee(1);

Set<Employee> set = new HashSet<>();
set.add(e1);
set.add(e2);

System.out.println(e1.equals(e2));  // ?
System.out.println(set.size());     // ?
```

**Output:**
```
true   // equals() says they're equal
2      // But HashSet sees them as DIFFERENT! (different hashCode)
```

**Technical:** `HashSet`/`HashMap` use `hashCode()` first to find the bucket, then `equals()` to check equality. If `hashCode()` differs, `equals()` is never called. **Rule:** If you override `equals()`, you MUST override `hashCode()`.

---

## 15. Coding Problems

### Inquiry 96: Reverse a String (Multiple Ways)
**Scenario:** Write code to reverse a string.

```java
// Method 1: StringBuilder (simplest)
String reversed = new StringBuilder("Hello").reverse().toString(); // "olleH"

// Method 2: char array
public static String reverse(String str) {
    char[] chars = str.toCharArray();
    int left = 0, right = chars.length - 1;
    while (left < right) {
        char temp = chars[left];
        chars[left] = chars[right];
        chars[right] = temp;
        left++;
        right--;
    }
    return new String(chars);
}

// Method 3: Recursion
public static String reverseRecursive(String str) {
    if (str.isEmpty()) return str;
    return reverseRecursive(str.substring(1)) + str.charAt(0);
}

// Method 4: Stream (Java 8)
String reversed = "Hello".chars()
    .mapToObj(c -> String.valueOf((char) c))
    .reduce("", (a, b) -> b + a);
```

### Inquiry 97: Check Palindrome
**Scenario:** Check if a string is palindrome.

```java
// Method 1: Two pointers (most efficient)
public static boolean isPalindrome(String str) {
    str = str.toLowerCase().replaceAll("[^a-z0-9]", "");
    int left = 0, right = str.length() - 1;
    while (left < right) {
        if (str.charAt(left) != str.charAt(right)) return false;
        left++;
        right--;
    }
    return true;
}

// Method 2: StringBuilder
public static boolean isPalindrome2(String str) {
    String cleaned = str.toLowerCase().replaceAll("[^a-z0-9]", "");
    return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
}

System.out.println(isPalindrome("madam"));       // true
System.out.println(isPalindrome("A man a plan a canal Panama")); // true
```

### Inquiry 98: Find Duplicates in Array
**Scenario:** Find duplicate elements.

```java
// Method 1: Using Set
public static List<Integer> findDuplicates(int[] arr) {
    Set<Integer> seen = new HashSet<>();
    Set<Integer> duplicates = new LinkedHashSet<>();
    for (int num : arr) {
        if (!seen.add(num)) { // add() returns false if already exists
            duplicates.add(num);
        }
    }
    return new ArrayList<>(duplicates);
}

// Method 2: Using Stream (Java 8)
List<Integer> duplicates = Arrays.stream(arr)
    .boxed()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
    .entrySet().stream()
    .filter(e -> e.getValue() > 1)
    .map(Map.Entry::getKey)
    .collect(Collectors.toList());

// Method 3: Frequency map
Map<Integer, Integer> freq = new HashMap<>();
for (int num : arr) {
    freq.merge(num, 1, Integer::sum);
}
freq.entrySet().stream()
    .filter(e -> e.getValue() > 1)
    .forEach(e -> System.out.println(e.getKey() + " appears " + e.getValue() + " times"));
```

### Inquiry 99: Two Sum Problem
**Scenario:** Find two numbers that add up to target.

```java
// O(n) using HashMap
public static int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>(); // value → index
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
        map.put(nums[i], i);
    }
    return new int[]{}; // not found
}

// Example: nums = [2, 7, 11, 15], target = 9
// i=0: complement=7, map={2:0}
// i=1: complement=2, map has 2! → return [0, 1]
```

### Inquiry 100: FizzBuzz
**Scenario:** Classic interview problem.

```java
// Standard
for (int i = 1; i <= 100; i++) {
    if (i % 15 == 0)      System.out.println("FizzBuzz");
    else if (i % 3 == 0)  System.out.println("Fizz");
    else if (i % 5 == 0)  System.out.println("Buzz");
    else                   System.out.println(i);
}

// Java 8 Stream
IntStream.rangeClosed(1, 100)
    .mapToObj(i -> i % 15 == 0 ? "FizzBuzz" :
                   i % 3 == 0  ? "Fizz" :
                   i % 5 == 0  ? "Buzz" : String.valueOf(i))
    .forEach(System.out::println);
```

### Inquiry 101: Fibonacci Series
**Scenario:** Print first N Fibonacci numbers.

```java
// Method 1: Iterative (best performance)
public static void fibonacci(int n) {
    int a = 0, b = 1;
    for (int i = 0; i < n; i++) {
        System.out.print(a + " ");
        int temp = a + b;
        a = b;
        b = temp;
    }
}
// Output: 0 1 1 2 3 5 8 13 21 34

// Method 2: Recursive (slow — O(2^n), asked in interviews)
public static int fib(int n) {
    if (n <= 1) return n;
    return fib(n - 1) + fib(n - 2);
}

// Method 3: Stream (Java 8)
Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0] + f[1]})
    .limit(10)
    .map(f -> f[0])
    .forEach(System.out::println);
```

### Inquiry 102: Sort a Map by Value
**Scenario:** Sort HashMap by values.

```java
Map<String, Integer> map = new HashMap<>();
map.put("Rahul", 85);
map.put("Amit", 92);
map.put("Sneha", 78);
map.put("Priya", 95);

// Sort by value ascending
Map<String, Integer> sorted = map.entrySet().stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (e1, e2) -> e1,
        LinkedHashMap::new  // Preserve insertion order
    ));
// {Sneha=78, Rahul=85, Amit=92, Priya=95}

// Sort by value descending
Map<String, Integer> sortedDesc = map.entrySet().stream()
    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
    .collect(Collectors.toMap(
        Map.Entry::getKey, Map.Entry::getValue,
        (e1, e2) -> e1, LinkedHashMap::new
    ));
```

### Inquiry 103: Find First Non-Repeated Character
**Scenario:** Find first unique character in a string.

```java
// Method 1: LinkedHashMap (preserves insertion order)
public static char firstNonRepeated(String str) {
    Map<Character, Integer> map = new LinkedHashMap<>();
    for (char c : str.toCharArray()) {
        map.merge(c, 1, Integer::sum);
    }
    return map.entrySet().stream()
        .filter(e -> e.getValue() == 1)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse('\0');
}

// Method 2: Stream (Java 8)
public static char firstNonRepeated2(String str) {
    return str.chars()
        .mapToObj(c -> (char) c)
        .filter(c -> str.indexOf(c) == str.lastIndexOf(c))
        .findFirst()
        .orElse('\0');
}

System.out.println(firstNonRepeated("aabbcde")); // c
```

### Inquiry 104: Producer-Consumer using BlockingQueue
**Scenario:** Implement thread-safe producer-consumer.

```java
BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

// Producer
Thread producer = new Thread(() -> {
    for (int i = 1; i <= 10; i++) {
        try {
            queue.put(i); // Blocks if queue is full
            System.out.println("Produced: " + i);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
});

// Consumer
Thread consumer = new Thread(() -> {
    for (int i = 1; i <= 10; i++) {
        try {
            int val = queue.take(); // Blocks if queue is empty
            System.out.println("Consumed: " + val);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
});

producer.start();
consumer.start();
```

### Inquiry 105: Singleton Pattern (Thread-Safe)
**Scenario:** Implement all variations.

```java
// Method 1: Eager Initialization
class EagerSingleton {
    private static final EagerSingleton INSTANCE = new EagerSingleton();
    private EagerSingleton() {}
    public static EagerSingleton getInstance() { return INSTANCE; }
}

// Method 2: Double-Checked Locking (Lazy)
class DCLSingleton {
    private static volatile DCLSingleton instance;
    private DCLSingleton() {}

    public static DCLSingleton getInstance() {
        if (instance == null) {                    // First check (no lock)
            synchronized (DCLSingleton.class) {
                if (instance == null) {            // Second check (with lock)
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }
}

// Method 3: Bill Pugh (Inner Static Class — recommended)
class BillPughSingleton {
    private BillPughSingleton() {}

    private static class Holder {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return Holder.INSTANCE; // Loaded only when getInstance() called
    }
}

// Method 4: Enum (BEST — thread-safe, serialization-safe, reflection-safe)
enum EnumSingleton {
    INSTANCE;
    public void doSomething() { }
}
```

### Inquiry 106: Immutable Class
**Scenario:** Create a fully immutable class.

```java
public final class ImmutableEmployee {  // final class — can't extend
    private final String name;           // final fields
    private final int age;
    private final List<String> skills;   // mutable field — needs defensive copy

    public ImmutableEmployee(String name, int age, List<String> skills) {
        this.name = name;
        this.age = age;
        this.skills = new ArrayList<>(skills); // Defensive copy in constructor
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    public List<String> getSkills() {
        return Collections.unmodifiableList(skills); // Return unmodifiable copy
    }

    // No setters!
}

// Rules for Immutable Class:
// 1. Class is final
// 2. All fields are private final
// 3. No setters
// 4. Defensive copy of mutable fields in constructor
// 5. Return unmodifiable copies from getters
```

---

## 16. Interview Questions & Answers

### Inquiry 107: Why is Java Platform Independent?
**Answer:** Java source code is compiled to **bytecode** (.class files), not machine code. Bytecode runs on the **JVM**, which is platform-specific. So you write once, and the JVM on each OS interprets the same bytecode. *"Write Once, Run Anywhere."*

### Inquiry 108: What is the difference between JDK, JRE, and JVM?
**Answer:**
*   **JVM** — Executes bytecode. Abstract specification.
*   **JRE** — JVM + core libraries (java.lang, java.util). Enough to **run** Java programs.
*   **JDK** — JRE + development tools (javac, debugger, javadoc). Needed to **develop** Java programs.

### Inquiry 109: Can we have multiple main methods?
**Answer:** Yes. Each class can have its own `main()`. JVM calls the `main()` of the class you specify when running `java ClassName`.

### Inquiry 110: What happens if main() is not static?
**Answer:** Program compiles but throws `NoSuchMethodError` at runtime. JVM looks for `public static void main(String[] args)` — all keywords are mandatory.

### Inquiry 111: Difference between `==` and `equals()`?
**Answer:**
*   `==` — Compares **references** (memory addresses) for objects, **values** for primitives.
*   `equals()` — Compares **content/state** of objects (if properly overridden).

### Inquiry 112: What is Diamond Problem? How does Java solve it?
**Answer:** If class C extends A and B, and both have same method → ambiguity. Java **doesn't allow multiple class inheritance**. With interfaces (Java 8+ default methods), if two interfaces have same default method, the implementing class **must override** it.

```java
interface A { default void show() { System.out.println("A"); } }
interface B { default void show() { System.out.println("B"); } }

class C implements A, B {
    @Override
    public void show() {
        A.super.show(); // Explicitly choose which to call
    }
}
```

### Inquiry 113: What is the difference between Abstract Class and Interface?
**Answer:**

| Feature | Abstract Class | Interface |
|---------|---------------|-----------|
| **Methods** | Abstract + concrete | Abstract + default + static (Java 8+) |
| **Variables** | Any type | Only `public static final` |
| **Constructor** | Yes | No |
| **Multiple inheritance** | No | Yes |
| **Access modifiers** | Any | Only public (methods) |
| **When to use** | IS-A relationship with shared code | CAN-DO capability / contract |

### Inquiry 114: What is Spring Boot Actuator?
**Answer:** Provides production-ready features: health checks (`/actuator/health`), metrics (`/actuator/metrics`), info, environment details. Used for monitoring in production.

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health, info, metrics, env
  endpoint:
    health:
      show-details: always
```

### Inquiry 115: Explain @Autowired
**Answer:** Spring's dependency injection annotation. Injects beans automatically by type.

```java
// Field injection (works but NOT recommended)
@Autowired
private UserService userService;

// Constructor injection (RECOMMENDED — testable, immutable)
private final UserService userService;

@Autowired // Optional in Spring 4.3+ if single constructor
public UserController(UserService userService) {
    this.userService = userService;
}

// Setter injection (optional dependencies)
@Autowired(required = false)
public void setNotificationService(NotificationService ns) {
    this.notificationService = ns;
}
```

### Inquiry 116: What is @Qualifier?
**Answer:** When multiple beans of same type exist, `@Qualifier` specifies which one to inject.

```java
@Component("mysqlRepo")
class MySQLUserRepo implements UserRepo { }

@Component("mongoRepo")
class MongoUserRepo implements UserRepo { }

@Autowired
@Qualifier("mysqlRepo") // Without this → NoUniqueBeanDefinitionException
private UserRepo userRepo;
```

### Inquiry 117: Explain Spring Security Authentication Flow
**Answer:**
1. Request hits `AuthenticationFilter`.
2. Filter extracts credentials → creates `UsernamePasswordAuthenticationToken`.
3. `AuthenticationManager` delegates to `AuthenticationProvider`.
4. Provider uses `UserDetailsService` to load user from DB.
5. `PasswordEncoder` verifies password.
6. If valid → `SecurityContext` stores `Authentication` object.
7. Subsequent requests check `SecurityContext`.

### Inquiry 118: What is Connection Pooling?
**Answer:** Creating DB connections is expensive (~200-500ms). Connection pool maintains a set of **pre-created connections** that are reused. Spring Boot uses **HikariCP** (fastest) by default.

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
```

### Inquiry 119: How to Handle Concurrent Requests in Spring Boot?
**Answer:** Spring Boot uses **embedded Tomcat** with a thread pool (default 200 threads). Each request gets a thread. For high concurrency:
*   Increase thread pool: `server.tomcat.threads.max=400`
*   Use `@Async` for non-blocking operations.
*   Use `WebFlux` (reactive) for truly non-blocking I/O.
*   Use `CompletableFuture` for async processing.

### Inquiry 120: What Questions Should YOU Ask the Interviewer?
**Answer:** Always ask questions — shows interest and maturity.
*   "What does the tech stack look like for this project?"
*   "How is the team structured? How many developers?"
*   "What's the deployment process? CI/CD?"
*   "What's the biggest technical challenge the team is facing?"
*   "How do you handle code reviews?"

---

## 17. Testing (JUnit, Mockito)

### Inquiry 121: JUnit 5 Basics
**Scenario:** Write a simple test.
**Explanation:**

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calc;

    @BeforeEach  // Runs before EACH test method
    void setUp() {
        calc = new Calculator();
    }

    @Test
    @DisplayName("Addition of two positive numbers")
    void testAdd() {
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
    }

    @Test
    @Disabled("Bug #123 — fix pending")
    void testSubtract() {
        assertEquals(2, calc.subtract(5, 3));
    }

    @AfterEach   // Runs after EACH test
    void tearDown() { calc = null; }

    @BeforeAll   // Runs ONCE before all tests (must be static)
    static void init() { System.out.println("Tests starting"); }

    @AfterAll    // Runs ONCE after all tests
    static void cleanup() { System.out.println("Tests done"); }
}
```

### Inquiry 122: Common Assertions
**Scenario:** What assertions are available?
**Explanation:**

```java
assertEquals(expected, actual);
assertNotEquals(a, b);
assertTrue(condition);
assertFalse(condition);
assertNull(obj);
assertNotNull(obj);
assertThrows(Exception.class, () -> riskyMethod());
assertDoesNotThrow(() -> safeMethod());
assertAll("grouped",
    () -> assertEquals(1, calc.add(0, 1)),
    () -> assertEquals(4, calc.add(2, 2)),
    () -> assertEquals(0, calc.add(-1, 1))
);
assertTimeout(Duration.ofSeconds(2), () -> slowMethod());
```

### Inquiry 123: Mockito Basics
**Scenario:** What is mocking? Why do we need it?
**Explanation:** Mocking creates **fake objects** that simulate real dependencies. This lets you test a class **in isolation** without needing a real database, API, etc.

```java
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepo;  // Fake repository

    @InjectMocks
    private UserService userService;  // Real service with mocked dependencies

    @Test
    void testGetUserById() {
        // Arrange: Define mock behavior
        User mockUser = new User(1L, "Rahul", "rahul@test.com");
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act: Call the method
        User result = userService.getUserById(1L);

        // Assert: Verify result
        assertEquals("Rahul", result.getName());

        // Verify: Check interaction
        verify(userRepo, times(1)).findById(1L);
        verify(userRepo, never()).deleteById(anyLong());
    }
}
```

### Inquiry 124: Mockito — when/thenReturn vs doReturn/when
**Scenario:** When to use which?
**Explanation:**

```java
// when/thenReturn — standard, type-safe
when(repo.findById(1L)).thenReturn(Optional.of(user));
when(repo.save(any(User.class))).thenReturn(savedUser);
when(repo.findAll()).thenReturn(List.of(user1, user2));

// Throw exception
when(repo.findById(999L)).thenThrow(new RuntimeException("Not found"));

// doReturn/when — use for void methods or spies
doReturn(Optional.of(user)).when(repo).findById(1L);
doThrow(new RuntimeException()).when(repo).deleteById(1L);
doNothing().when(repo).deleteById(1L);  // void method

// Argument matchers
when(repo.findByName(anyString())).thenReturn(user);
when(repo.findByAge(eq(25))).thenReturn(user);  // eq() for exact match with matchers
```

### Inquiry 125: @MockBean vs @Mock
**Scenario:** Difference?
**Explanation:**
*   `@Mock` (Mockito) — Creates mock in **unit tests**. No Spring context loaded. Fast.
*   `@MockBean` (Spring Boot) — Replaces a bean in **Spring Application Context**. Used in integration tests with `@SpringBootTest`. Slower.

```java
// Unit test — fast, no Spring
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository repo;
    @InjectMocks UserService service;
}

// Integration test — loads Spring context
@SpringBootTest
class UserControllerIntegrationTest {
    @MockBean UserRepository repo;  // Replaces real repo bean in context
    @Autowired UserController controller;
}
```

### Inquiry 126: Testing REST Controllers
**Scenario:** How to test a controller?
**Explanation:**

```java
@WebMvcTest(UserController.class)  // Loads only web layer
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetUser() throws Exception {
        User user = new User(1L, "Rahul", "rahul@test.com");
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Rahul"))
            .andExpect(jsonPath("$.email").value("rahul@test.com"));
    }

    @Test
    void testCreateUser() throws Exception {
        String json = """
            {"name": "Amit", "email": "amit@test.com"}
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated());
    }

    @Test
    void testGetUser_NotFound() throws Exception {
        when(userService.getUserById(999L)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/users/999"))
            .andExpect(status().isNotFound());
    }
}
```

---

## 18. Design Patterns

### Inquiry 127: Design Pattern Categories
**Scenario:** How are they classified?
**Explanation:**

| Category | Purpose | Patterns |
|----------|---------|----------|
| **Creational** | Object creation | Singleton, Factory, Abstract Factory, Builder, Prototype |
| **Structural** | Object composition | Adapter, Decorator, Proxy, Facade, Composite, Bridge |
| **Behavioral** | Object communication | Strategy, Observer, Template, Command, Iterator, State |

### Inquiry 128: Factory Pattern
**Scenario:** When to use?
**Explanation:** When you want to create objects **without exposing creation logic** to the client. The client uses a common interface.

```java
// Product interface
interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    public void send(String msg) { System.out.println("Email: " + msg); }
}

class SMSNotification implements Notification {
    public void send(String msg) { System.out.println("SMS: " + msg); }
}

class PushNotification implements Notification {
    public void send(String msg) { System.out.println("Push: " + msg); }
}

// Factory
class NotificationFactory {
    public static Notification create(String type) {
        return switch (type.toUpperCase()) {
            case "EMAIL" -> new EmailNotification();
            case "SMS"   -> new SMSNotification();
            case "PUSH"  -> new PushNotification();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}

// Usage — client doesn't know concrete classes
Notification n = NotificationFactory.create("EMAIL");
n.send("Hello!");
```

**Used in:** `Calendar.getInstance()`, `NumberFormat.getInstance()`, Spring's `BeanFactory`.

### Inquiry 129: Builder Pattern
**Scenario:** When to use?
**Explanation:** When an object has **many optional parameters**. Avoids telescoping constructors.

```java
class Employee {
    private final String name;      // required
    private final String email;     // required
    private final int age;          // optional
    private final String dept;      // optional
    private final String phone;     // optional

    private Employee(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
        this.dept = builder.dept;
        this.phone = builder.phone;
    }

    public static class Builder {
        private final String name;   // required
        private final String email;  // required
        private int age;
        private String dept;
        private String phone;

        public Builder(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public Builder age(int age) { this.age = age; return this; }
        public Builder dept(String dept) { this.dept = dept; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }

        public Employee build() { return new Employee(this); }
    }
}

// Usage — clean, readable
Employee emp = new Employee.Builder("Rahul", "rahul@test.com")
    .age(25)
    .dept("IT")
    .build();
```

**Used in:** `StringBuilder`, `Stream.Builder`, Lombok's `@Builder`.

### Inquiry 130: Strategy Pattern
**Scenario:** When to use?
**Explanation:** When you want to **switch algorithms at runtime** without changing the client code.

```java
// Strategy interface
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("Credit Card: ₹" + amount); }
}

class UPIPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("UPI: ₹" + amount); }
}

class WalletPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("Wallet: ₹" + amount); }
}

// Context
class ShoppingCart {
    private PaymentStrategy strategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void checkout(double amount) {
        strategy.pay(amount);
    }
}

// Usage — switch strategy at runtime
ShoppingCart cart = new ShoppingCart();
cart.setPaymentStrategy(new UPIPayment());
cart.checkout(500.0);  // UPI: ₹500.0

cart.setPaymentStrategy(new CreditCardPayment());
cart.checkout(1000.0); // Credit Card: ₹1000.0
```

**Used in:** `Comparator`, `Collections.sort()`, Spring's `Resource` interface.

### Inquiry 131: Observer Pattern
**Scenario:** When to use?
**Explanation:** When one object changes state and **all dependents should be notified** automatically. Publish-Subscribe model.

```java
// Observer interface
interface OrderObserver {
    void update(String orderId, String status);
}

class EmailService implements OrderObserver {
    public void update(String orderId, String status) {
        System.out.println("Email sent: Order " + orderId + " is " + status);
    }
}

class SMSService implements OrderObserver {
    public void update(String orderId, String status) {
        System.out.println("SMS sent: Order " + orderId + " is " + status);
    }
}

// Subject
class OrderService {
    private List<OrderObserver> observers = new ArrayList<>();

    public void addObserver(OrderObserver o) { observers.add(o); }

    public void updateOrderStatus(String orderId, String status) {
        // Business logic...
        observers.forEach(o -> o.update(orderId, status)); // Notify all
    }
}

// Usage
OrderService orderService = new OrderService();
orderService.addObserver(new EmailService());
orderService.addObserver(new SMSService());
orderService.updateOrderStatus("ORD-123", "SHIPPED");
// Email sent: Order ORD-123 is SHIPPED
// SMS sent: Order ORD-123 is SHIPPED
```

**Used in:** Spring's `ApplicationEvent`, Kafka consumers, GUI event listeners.

### Inquiry 132: Decorator Pattern
**Scenario:** When to use?
**Explanation:** Add **extra behavior** to an object dynamically without modifying the original class.

```java
interface Coffee {
    double getCost();
    String getDescription();
}

class BasicCoffee implements Coffee {
    public double getCost() { return 50; }
    public String getDescription() { return "Basic Coffee"; }
}

// Decorators
abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    CoffeeDecorator(Coffee coffee) { this.coffee = coffee; }
}

class MilkDecorator extends CoffeeDecorator {
    MilkDecorator(Coffee coffee) { super(coffee); }
    public double getCost() { return coffee.getCost() + 20; }
    public String getDescription() { return coffee.getDescription() + " + Milk"; }
}

class SugarDecorator extends CoffeeDecorator {
    SugarDecorator(Coffee coffee) { super(coffee); }
    public double getCost() { return coffee.getCost() + 10; }
    public String getDescription() { return coffee.getDescription() + " + Sugar"; }
}

// Usage — stack decorators
Coffee coffee = new SugarDecorator(new MilkDecorator(new BasicCoffee()));
System.out.println(coffee.getDescription()); // Basic Coffee + Milk + Sugar
System.out.println(coffee.getCost());        // 80.0
```

**Used in:** `BufferedReader(new FileReader())`, `Collections.unmodifiableList()`, Spring's `BeanPostProcessor`.

### Inquiry 133: Proxy Pattern
**Scenario:** When to use?
**Explanation:** Provide a **surrogate/placeholder** for another object to control access.

```java
interface DatabaseService {
    void query(String sql);
}

class RealDatabaseService implements DatabaseService {
    public void query(String sql) {
        System.out.println("Executing: " + sql);
    }
}

// Proxy — adds access control and logging
class DatabaseProxy implements DatabaseService {
    private RealDatabaseService realService;
    private String userRole;

    DatabaseProxy(String userRole) {
        this.userRole = userRole;
        this.realService = new RealDatabaseService();
    }

    public void query(String sql) {
        if (sql.toUpperCase().startsWith("DELETE") && !"ADMIN".equals(userRole)) {
            System.out.println("ACCESS DENIED: Only admins can delete");
            return;
        }
        System.out.println("[LOG] User role: " + userRole + ", SQL: " + sql);
        realService.query(sql);
    }
}

// Usage
DatabaseService db = new DatabaseProxy("USER");
db.query("SELECT * FROM Emp");     // Works
db.query("DELETE FROM Emp");        // ACCESS DENIED
```

**Used in:** Spring AOP (`@Transactional`, `@Cacheable`), Hibernate lazy loading, JDK dynamic proxy.

---

## 19. Spring Data & Queries

### Inquiry 134: Spring Data JPA Repository Hierarchy
**Scenario:** What interfaces are available?
**Explanation:**

```
Repository (marker)
    └── CrudRepository (CRUD operations)
        └── PagingAndSortingRepository (pagination + sorting)
            └── JpaRepository (JPA-specific: flush, batch, example queries)
```

```java
// Most commonly used
public interface UserRepository extends JpaRepository<User, Long> {
    // Inherits: save(), findById(), findAll(), deleteById(), count(), existsById()
    // Plus: flush(), saveAndFlush(), findAll(Sort), findAll(Pageable)
}
```

### Inquiry 135: Derived Query Methods
**Scenario:** How does Spring generate SQL from method names?
**Explanation:** Spring parses method name and auto-generates the query.

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // SELECT * FROM users WHERE email = ?
    User findByEmail(String email);

    // SELECT * FROM users WHERE name = ? AND age = ?
    List<User> findByNameAndAge(String name, int age);

    // SELECT * FROM users WHERE age > ?
    List<User> findByAgeGreaterThan(int age);

    // SELECT * FROM users WHERE name LIKE '%keyword%'
    List<User> findByNameContaining(String keyword);

    // SELECT * FROM users WHERE name LIKE 'keyword%'
    List<User> findByNameStartingWith(String prefix);

    // SELECT * FROM users WHERE active = true ORDER BY name ASC
    List<User> findByActiveTrueOrderByNameAsc();

    // SELECT * FROM users WHERE dept IN (?, ?, ?)
    List<User> findByDeptIn(List<String> depts);

    // SELECT * FROM users WHERE age BETWEEN ? AND ?
    List<User> findByAgeBetween(int min, int max);

    // COUNT(*) WHERE dept = ?
    long countByDept(String dept);

    // EXISTS WHERE email = ?
    boolean existsByEmail(String email);

    // DELETE FROM users WHERE active = false
    void deleteByActiveFalse();

    // Top N results
    List<User> findTop5ByOrderBySalaryDesc();
    User findFirstByOrderByCreatedAtDesc();
}
```

### Inquiry 136: @Query Annotation
**Scenario:** When derived methods aren't enough?
**Explanation:**

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // JPQL (Java Persistence Query Language — uses entity/field names)
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmailJPQL(@Param("email") String email);

    // Native SQL (uses table/column names)
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    User findByEmailNative(@Param("email") String email);

    // Update query
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.active = false WHERE u.lastLogin < :date")
    int deactivateInactiveUsers(@Param("date") LocalDate date);

    // Projection — return specific fields
    @Query("SELECT u.name AS name, u.email AS email FROM User u WHERE u.dept = :dept")
    List<UserProjection> findNameAndEmailByDept(@Param("dept") String dept);
}

// Projection interface
interface UserProjection {
    String getName();
    String getEmail();
}
```

### Inquiry 137: Pagination and Sorting
**Scenario:** How to paginate results?
**Explanation:**

```java
// Controller
@GetMapping("/users")
public Page<User> getUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "name") String sortBy) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    return userRepository.findAll(pageable);
}

// Response includes:
// content: [...],      ← actual data
// totalElements: 100,  ← total records
// totalPages: 10,      ← total pages
// number: 0,           ← current page (0-indexed)
// size: 10,            ← page size
// first: true,         ← is first page?
// last: false          ← is last page?

// Custom paginated query
@Query("SELECT u FROM User u WHERE u.dept = :dept")
Page<User> findByDept(@Param("dept") String dept, Pageable pageable);

// Multiple sort fields
Pageable pageable = PageRequest.of(0, 10, Sort.by("dept").ascending().and(Sort.by("name").descending()));
```

### Inquiry 138: Specification (Dynamic Queries)
**Scenario:** How to build queries dynamically based on filters?
**Explanation:**

```java
// Repository extends JpaSpecificationExecutor
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {}

// Specification builder
class UserSpecification {
    public static Specification<User> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<User> hasDept(String dept) {
        return (root, query, cb) -> dept == null ? null : cb.equal(root.get("dept"), dept);
    }

    public static Specification<User> ageGreaterThan(Integer age) {
        return (root, query, cb) -> age == null ? null : cb.greaterThan(root.get("age"), age);
    }
}

// Usage — combine dynamically
Specification<User> spec = Specification
    .where(UserSpecification.hasName("Rahul"))
    .and(UserSpecification.hasDept("IT"))
    .and(UserSpecification.ageGreaterThan(25));

List<User> results = userRepository.findAll(spec);
```

---

## 20. Messaging (Kafka, RabbitMQ)

### Inquiry 139: Why Messaging?
**Scenario:** When do we need message queues?
**Explanation:**

| Problem | Solution with Messaging |
|---------|------------------------|
| **Tight coupling** | Services communicate via messages, not direct calls |
| **Synchronous bottleneck** | Async processing — producer doesn't wait for consumer |
| **Spike handling** | Queue buffers messages during traffic spikes |
| **Retry/Resilience** | Failed messages can be retried from queue |
| **Event-driven** | React to events (order placed → send email, update inventory) |

### Inquiry 140: Kafka Basics
**Scenario:** Core concepts?
**Explanation:**

```
Producer ──► Topic (Partition 0) ──► Consumer Group A (Consumer 1)
                   (Partition 1) ──► Consumer Group A (Consumer 2)
                   (Partition 2) ──► Consumer Group B (Consumer 3)
```

| Concept | Description |
|---------|-------------|
| **Topic** | Named channel/category for messages (like a table) |
| **Partition** | Topic is split into partitions for parallelism |
| **Producer** | Sends messages to a topic |
| **Consumer** | Reads messages from a topic |
| **Consumer Group** | Group of consumers sharing work. Each partition → 1 consumer in group |
| **Offset** | Position of a message in a partition (like array index) |
| **Broker** | Kafka server instance |
| **Zookeeper/KRaft** | Manages broker metadata and leader election |

### Inquiry 141: Kafka with Spring Boot
**Scenario:** Producer and Consumer code?
**Explanation:**

```yaml
# application.yml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      group-id: my-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
```

```java
// Producer
@Service
class OrderProducer {
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderEvent(OrderEvent event) {
        kafkaTemplate.send("order-topic", event.getOrderId(), event);
        // topic, key (for partitioning), value
    }
}

// Consumer
@Service
class OrderConsumer {
    @KafkaListener(topics = "order-topic", groupId = "notification-group")
    public void handleOrderEvent(OrderEvent event) {
        System.out.println("Received order: " + event.getOrderId());
        // Send email, update inventory, etc.
    }
}
```

### Inquiry 142: Kafka vs RabbitMQ
**Scenario:** When to use which?
**Explanation:**

| Feature | Kafka | RabbitMQ |
|---------|-------|----------|
| **Model** | Distributed log (pull-based) | Message broker (push-based) |
| **Throughput** | Very high (millions/sec) | Moderate (thousands/sec) |
| **Message retention** | Retains messages (configurable TTL) | Deletes after consumption |
| **Ordering** | Guaranteed within partition | Guaranteed within queue |
| **Replay** | Yes (consumers can re-read) | No (once consumed, gone) |
| **Use case** | Event streaming, logs, analytics | Task queues, RPC, routing |
| **Complexity** | Higher (partitions, offsets) | Simpler (queues, exchanges) |

### Inquiry 143: RabbitMQ with Spring Boot
**Scenario:** Basic producer/consumer?
**Explanation:**

```java
// Config
@Configuration
class RabbitConfig {
    @Bean
    public Queue orderQueue() {
        return new Queue("order-queue", true); // durable
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("order-exchange");
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("order.#");
    }
}

// Producer
@Service
class OrderProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrder(OrderEvent event) {
        rabbitTemplate.convertAndSend("order-exchange", "order.created", event);
    }
}

// Consumer
@Service
class OrderConsumer {
    @RabbitListener(queues = "order-queue")
    public void handleOrder(OrderEvent event) {
        System.out.println("Processing order: " + event.getOrderId());
    }
}
```

### Inquiry 144: Dead Letter Queue (DLQ)
**Scenario:** What happens when a message fails?
**Explanation:** A DLQ is a separate queue where **failed messages** are sent after max retries. Prevents poison messages from blocking the main queue.

```
Main Queue ──► Consumer (fails 3 times) ──► Dead Letter Queue
                                                    │
                                              Manual review / retry
```

```java
// Kafka DLQ with Spring
@KafkaListener(topics = "order-topic")
@RetryableTopic(
    attempts = "3",
    backoff = @Backoff(delay = 1000, multiplier = 2),
    dltTopicSuffix = "-dlt"
)
public void handleOrder(OrderEvent event) {
    // If this throws exception 3 times → message goes to "order-topic-dlt"
    processOrder(event);
}

@DltHandler
public void handleDlt(OrderEvent event) {
    log.error("Failed after retries: " + event.getOrderId());
    // Save to DB for manual investigation
}
```

---

## 21. More Coding Problems

### Inquiry 145: Anagram Check
**Scenario:** Check if two strings are anagrams.

```java
// Method 1: Sort and compare
public static boolean isAnagram(String s1, String s2) {
    if (s1.length() != s2.length()) return false;
    char[] a = s1.toLowerCase().toCharArray();
    char[] b = s2.toLowerCase().toCharArray();
    Arrays.sort(a);
    Arrays.sort(b);
    return Arrays.equals(a, b);
}

// Method 2: Frequency count (O(n) — faster)
public static boolean isAnagram2(String s1, String s2) {
    if (s1.length() != s2.length()) return false;
    int[] freq = new int[26];
    for (char c : s1.toLowerCase().toCharArray()) freq[c - 'a']++;
    for (char c : s2.toLowerCase().toCharArray()) freq[c - 'a']--;
    for (int f : freq) if (f != 0) return false;
    return true;
}

System.out.println(isAnagram("listen", "silent")); // true
System.out.println(isAnagram("hello", "world"));   // false
```

### Inquiry 146: Remove Duplicates from List
**Scenario:** Multiple approaches.

```java
List<Integer> list = Arrays.asList(1, 2, 3, 2, 4, 3, 5);

// Method 1: LinkedHashSet (preserves order)
List<Integer> unique = new ArrayList<>(new LinkedHashSet<>(list));
// [1, 2, 3, 4, 5]

// Method 2: Stream distinct()
List<Integer> unique2 = list.stream().distinct().collect(Collectors.toList());

// Method 3: Remove duplicates from list of objects by field
List<Employee> uniqueByEmail = employees.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Employee::getEmail))),
        ArrayList::new
    ));
```

### Inquiry 147: Merge Two Sorted Arrays
**Scenario:** Without using extra space (in-place concept).

```java
// Using extra array (simple)
public static int[] merge(int[] a, int[] b) {
    int[] result = new int[a.length + b.length];
    int i = 0, j = 0, k = 0;
    while (i < a.length && j < b.length) {
        if (a[i] <= b[j]) result[k++] = a[i++];
        else result[k++] = b[j++];
    }
    while (i < a.length) result[k++] = a[i++];
    while (j < b.length) result[k++] = b[j++];
    return result;
}

// a = [1, 3, 5], b = [2, 4, 6] → [1, 2, 3, 4, 5, 6]
```

### Inquiry 148: Find Missing Number
**Scenario:** Array of 1 to N with one missing.

```java
// Method 1: Math formula — O(n) time, O(1) space
public static int findMissing(int[] arr, int n) {
    int expectedSum = n * (n + 1) / 2;
    int actualSum = Arrays.stream(arr).sum();
    return expectedSum - actualSum;
}

// Method 2: XOR — handles overflow better
public static int findMissingXOR(int[] arr, int n) {
    int xor = 0;
    for (int i = 1; i <= n; i++) xor ^= i;
    for (int num : arr) xor ^= num;
    return xor;
}

// arr = [1, 2, 4, 5], n = 5 → missing = 3
```

### Inquiry 149: Check if String has All Unique Characters
**Scenario:** Without using extra data structure.

```java
// Method 1: HashSet — O(n) time, O(n) space
public static boolean allUnique(String str) {
    Set<Character> set = new HashSet<>();
    for (char c : str.toCharArray()) {
        if (!set.add(c)) return false;
    }
    return true;
}

// Method 2: Bit manipulation — O(n) time, O(1) space (only lowercase a-z)
public static boolean allUniqueBit(String str) {
    int checker = 0;
    for (char c : str.toCharArray()) {
        int bit = 1 << (c - 'a');
        if ((checker & bit) > 0) return false;
        checker |= bit;
    }
    return true;
}

System.out.println(allUnique("abcde"));  // true
System.out.println(allUnique("hello"));  // false (l repeats)
```

### Inquiry 150: Group Anagrams
**Scenario:** Group strings that are anagrams of each other.

```java
public static Map<String, List<String>> groupAnagrams(String[] words) {
    return Arrays.stream(words)
        .collect(Collectors.groupingBy(word -> {
            char[] chars = word.toLowerCase().toCharArray();
            Arrays.sort(chars);
            return new String(chars);
        }));
}

String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};
// Result: {abt=[bat], aet=[eat, tea, ate], ant=[tan, nat]}
```

### Inquiry 151: Longest Substring Without Repeating Characters
**Scenario:** Sliding window technique.

```java
public static int longestUniqueSubstring(String s) {
    Map<Character, Integer> map = new HashMap<>();
    int maxLen = 0, start = 0;

    for (int end = 0; end < s.length(); end++) {
        char c = s.charAt(end);
        if (map.containsKey(c) && map.get(c) >= start) {
            start = map.get(c) + 1; // Move window start past the duplicate
        }
        map.put(c, end);
        maxLen = Math.max(maxLen, end - start + 1);
    }
    return maxLen;
}

System.out.println(longestUniqueSubstring("abcabcbb")); // 3 ("abc")
System.out.println(longestUniqueSubstring("bbbbb"));    // 1 ("b")
System.out.println(longestUniqueSubstring("pwwkew"));   // 3 ("wke")
```

### Inquiry 152: Flatten Nested List using Streams
**Scenario:** Convert List of Lists into single List.

```java
List<List<String>> nested = List.of(
    List.of("Rahul", "Amit"),
    List.of("Sneha", "Priya"),
    List.of("Ravi")
);

// flatMap flattens the nested structure
List<String> flat = nested.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList());
// [Rahul, Amit, Sneha, Priya, Ravi]

// Flatten and sort
List<String> sorted = nested.stream()
    .flatMap(Collection::stream)
    .sorted()
    .collect(Collectors.toList());
// [Amit, Priya, Rahul, Ravi, Sneha]
```

### Inquiry 153: Employee Salary Operations with Streams
**Scenario:** Common interview stream problems.

```java
List<Employee> employees = List.of(
    new Employee("Rahul", "IT", 60000),
    new Employee("Amit", "HR", 45000),
    new Employee("Sneha", "IT", 75000),
    new Employee("Priya", "HR", 50000),
    new Employee("Ravi", "IT", 55000)
);

// 1. Highest paid employee
Employee highest = employees.stream()
    .max(Comparator.comparingDouble(Employee::getSalary))
    .orElseThrow();

// 2. Average salary per department
Map<String, Double> avgByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.averagingDouble(Employee::getSalary)));
// {IT=63333.33, HR=47500.0}

// 3. Second highest salary
double secondHighest = employees.stream()
    .map(Employee::getSalary)
    .distinct()
    .sorted(Comparator.reverseOrder())
    .skip(1)
    .findFirst()
    .orElseThrow();

// 4. Count employees per department
Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));

// 5. Employees earning above average
double avg = employees.stream().mapToDouble(Employee::getSalary).average().orElse(0);
List<Employee> aboveAvg = employees.stream()
    .filter(e -> e.getSalary() > avg)
    .collect(Collectors.toList());

// 6. Department with highest total salary
String richestDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.summingDouble(Employee::getSalary)))
    .entrySet().stream()
    .max(Map.Entry.comparingByValue())
    .map(Map.Entry::getKey)
    .orElse("N/A");
```

---

## 22. Java Gotchas & Best Practices

### Inquiry 154: Common Mistakes in Java

```java
// 1. Comparing strings with ==
String a = new String("hello");
String b = new String("hello");
if (a == b) { }          // WRONG — compares references
if (a.equals(b)) { }     // CORRECT — compares content

// 2. Forgetting to close resources
// WRONG
FileReader fr = new FileReader("file.txt");
// ... if exception occurs, fr is never closed

// CORRECT
try (FileReader fr = new FileReader("file.txt")) {
    // auto-closed
}

// 3. Modifying collection during iteration
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
for (String s : list) {
    if (s.equals("b")) list.remove(s); // ConcurrentModificationException!
}
// CORRECT: Use iterator.remove() or removeIf()
list.removeIf(s -> s.equals("b"));

// 4. Ignoring exceptions
try {
    riskyMethod();
} catch (Exception e) { } // WRONG — silent failure, impossible to debug

try {
    riskyMethod();
} catch (Exception e) {
    log.error("Failed to process", e); // CORRECT — log it
    throw new ServiceException("Processing failed", e); // or re-throw
}

// 5. Using raw types
List list = new ArrayList();     // WRONG — no type safety
List<String> list = new ArrayList<>(); // CORRECT

// 6. Returning null from collections
public List<User> getUsers() {
    return null;  // WRONG — caller must null-check
}
public List<User> getUsers() {
    return Collections.emptyList(); // CORRECT — return empty collection
}
```

### Inquiry 155: Best Practices Summary

| Practice | Bad | Good |
|----------|-----|------|
| **String comparison** | `s1 == s2` | `s1.equals(s2)` |
| **Null check** | `if (obj != null)` everywhere | Use `Optional` |
| **Resource cleanup** | Manual `close()` in finally | `try-with-resources` |
| **Immutability** | Mutable POJOs | `final` fields, no setters, `record` (Java 16+) |
| **Exception handling** | Catch `Exception` | Catch specific exceptions |
| **Logging** | `System.out.println()` | SLF4J + Logback |
| **Collection init** | `new ArrayList<>()` then `add()` | `List.of()`, `Map.of()` (Java 9+) |
| **Null return** | Return `null` | Return `Optional.empty()` or empty collection |
| **DI** | Field injection | Constructor injection |
| **Concatenation** | `+` in loops | `StringBuilder` |

### Inquiry 156: Java Records (Java 16+)
**Scenario:** Simplify data classes.
**Explanation:** Records auto-generate `constructor`, `getters`, `equals()`, `hashCode()`, `toString()`.

```java
// Before: 50+ lines of boilerplate
class Employee {
    private final String name;
    private final int age;
    // constructor, getters, equals, hashCode, toString...
}

// After: 1 line
record Employee(String name, int age) {}

// Usage
Employee emp = new Employee("Rahul", 25);
System.out.println(emp.name());  // "Rahul" (not getName())
System.out.println(emp);         // Employee[name=Rahul, age=25]

// Records are:
// - Immutable (final fields)
// - Cannot extend other classes (implicitly extends Record)
// - Can implement interfaces
// - Can have custom methods and static fields
record Employee(String name, int age) {
    // Compact constructor (validation)
    Employee {
        if (age < 0) throw new IllegalArgumentException("Age can't be negative");
    }

    // Custom method
    String nameUpperCase() { return name.toUpperCase(); }
}
```

### Inquiry 157: Sealed Classes (Java 17)
**Scenario:** Restrict which classes can extend a class.
**Explanation:**

```java
// Only Circle, Rectangle, Triangle can extend Shape
sealed interface Shape permits Circle, Rectangle, Triangle {}

final class Circle implements Shape {
    double radius;
}

final class Rectangle implements Shape {
    double width, height;
}

non-sealed class Triangle implements Shape {
    // non-sealed allows further extension
    double base, height;
}

// Pattern matching with sealed classes (Java 21)
double area(Shape shape) {
    return switch (shape) {
        case Circle c    -> Math.PI * c.radius * c.radius;
        case Rectangle r -> r.width * r.height;
        case Triangle t  -> 0.5 * t.base * t.height;
    };
    // No default needed — compiler knows all subtypes!
}
```

### Inquiry 158: Text Blocks (Java 15)
**Scenario:** Multi-line strings.
**Explanation:**

```java
// Before: Ugly concatenation
String json = "{\n" +
    "  \"name\": \"Rahul\",\n" +
    "  \"age\": 25\n" +
    "}";

// After: Clean text block
String json = """
    {
      "name": "Rahul",
      "age": 25
    }
    """;

// SQL query
String sql = """
    SELECT e.name, d.dept_name
    FROM employees e
    JOIN departments d ON e.dept_id = d.id
    WHERE e.salary > 50000
    ORDER BY e.name
    """;
```

### Inquiry 159: Pattern Matching (Java 16+)
**Scenario:** Simplify instanceof checks.
**Explanation:**

```java
// Before: Verbose
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// After: Pattern matching
if (obj instanceof String s) {
    System.out.println(s.length()); // s already cast
}

// Switch pattern matching (Java 21)
String describe(Object obj) {
    return switch (obj) {
        case Integer i  -> "Integer: " + i;
        case String s   -> "String of length " + s.length();
        case null       -> "null";
        case int[] arr  -> "Array of length " + arr.length;
        default         -> "Unknown: " + obj.getClass();
    };
}
```

### Inquiry 160: Virtual Threads (Java 21)
**Scenario:** What are they and why do they matter?
**Explanation:** Lightweight threads managed by JVM (not OS). Use a few KB of memory vs ~1MB for platform threads. Ideal for I/O-bound tasks.

```java
// Before: Platform threads (expensive)
ExecutorService executor = Executors.newFixedThreadPool(200);

// After: Virtual threads (cheap — create millions)
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

// Simple usage
Thread.startVirtualThread(() -> {
    System.out.println("Running on: " + Thread.currentThread());
});

// Structured concurrency (Preview)
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<User> user = scope.fork(() -> fetchUser(id));
    Future<Order> order = scope.fork(() -> fetchOrder(id));
    scope.join();
    scope.throwIfFailed();
    return new UserOrder(user.resultNow(), order.resultNow());
}
```

| Feature | Platform Thread | Virtual Thread |
|---------|----------------|---------------|
| **Memory** | ~1 MB stack | ~few KB |
| **Created by** | OS | JVM |
| **Max count** | ~thousands | ~millions |
| **Best for** | CPU-bound tasks | I/O-bound tasks |
| **Blocking cost** | Expensive (blocks OS thread) | Cheap (unmounts from carrier) |
