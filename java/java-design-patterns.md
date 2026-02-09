# Java Design Patterns - Complete Guide (GoF)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** All 23 GoF Patterns (Creational, Structural, Behavioral) and Real-world usage in Spring Framework.

---

## Table of Contents

1. [Creational Patterns](#creational-patterns)
2. [Structural Patterns](#structural-patterns)
3. [Behavioral Patterns](#behavioral-patterns)
4. [Design Patterns in Spring](#design-patterns-in-spring)
5. [Anti-Patterns](#anti-patterns)
6. [Complex Technical Scenarios](#complex-technical-scenarios)
7. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Creational Patterns

Object creation mechanisms, increasing flexibility.

### 1. Singleton
Ensures a class has only one instance and provides a global access point.
*   **Usage**: Logging, Database connections.
*   **Thread-Safe**: Double-Checked Locking or Enum Singleton (Best).

### 2. Factory Method
Defines an interface for creating objects, but lets subclasses decide which class to instantiate.
*   **Usage**: `Calendar.getInstance()`, `NumberFormat.getInstance()`.

### 3. Builder
Separates the construction of a complex object from its representation.
*   **Usage**: `StringBuilder`, `Lombok @Builder`.

### 4. Abstract Factory
Provides an interface for creating families of related or dependent objects without specifying their concrete classes.
*   **Usage**: `javax.xml.parsers.DocumentBuilderFactory`.

### 5. Prototype
Specifies the kind of objects to create using a prototypical instance, and creates new objects by copying this prototype.
*   **Usage**: `java.lang.Object#clone()`.

---

## 2. Structural Patterns

How classes and objects are composed to form larger structures.

### 1. Adapter
Allows incompatible interfaces to work together.
*   **Usage**: `Arrays.asList()` (Adapts Array to List).

### 2. Decorator
Adds new functionality to an object dynamically without altering its structure.
*   **Usage**: `java.io` (`BufferedReader(new FileReader(...))`).

### 3. Proxy
Provides a surrogate or placeholder for another object to control access to it.
*   **Usage**: Spring `@Transactional` (AOP), Hibernate Lazy Loading.

### 4. Facade
Provides a simplified interface to a complex subsystem.
*   **Usage**: Slf4j (Facade for logging frameworks).

### 5. Bridge
Decouples an abstraction from its implementation so that the two can vary independently.
*   **Usage**: JDBC Driver API.

### 6. Composite
Composes objects into tree structures to represent part-whole hierarchies.
*   **Usage**: `java.awt.Container` (Component/Container).

### 7. Flyweight
Uses sharing to support large numbers of fine-grained objects efficiently.
*   **Usage**: `String` Constant Pool, `Integer.valueOf` cache.

---

## 3. Behavioral Patterns

Communication between objects.

### 1. Observer
Defines a one-to-many dependency so that when one object changes state, all its dependents are notified automatically.
*   **Usage**: `java.util.EventListener`, RxJava.

### 2. Strategy
Defines a family of algorithms, encapsulates each one, and makes them interchangeable.
*   **Usage**: `Collections.sort(list, Comparator)`.

### 3. Template Method
Defines the skeleton of an algorithm in a method, deferring some steps to subclasses.
*   **Usage**: `AbstractList`, `JdbcTemplate`.

### 4. Chain of Responsibility
Passes a request along a chain of handlers.
*   **Usage**: Servlet Filters, Spring Security Filter Chain.

### 5. Command
Encapsulates a request as an object.
*   **Usage**: `Runnable` interface, GUI Action listeners.

### 6. Iterator
Accesses elements of a collection without exposing underlying representation.
*   **Usage**: `java.util.Iterator`.

### 7. Mediator
Encapsulates how a set of objects interact.
*   **Usage**: `java.util.Timer` (schedules tasks).

### 8. Memento
Captures and externalizes an object's internal state so it can be restored later.
*   **Usage**: `java.io.Serializable`.

### 9. State
Allows an object to alter its behavior when its internal state changes.
*   **Usage**: Connection pool management.

### 10. Visitor
Separates an algorithm from an object structure.
*   **Usage**: `FileVisitor` API in NIO.

### 11. Interpreter
Given a language, defines a representation for its grammar.
*   **Usage**: `SpEL` (Spring Expression Language), Regular Expressions.

---

## 4. Design Patterns in Spring

The Spring Framework is built on Design Patterns.

1.  **Singleton**: Default scope for Spring Beans.
2.  **Factory**: `BeanFactory`, `ApplicationContext` create beans.
3.  **Prototype**: Bean scope for new instances.
4.  **Before/After Advice (Proxy)**: AOP for cross-cutting concerns (Logging, Transaction).
5.  **Template Method**: `JdbcTemplate`, `RestTemplate`, `JmsTemplate`.
6.  **Front Controller**: `DispatcherServlet` handles all incoming requests in Spring MVC.
7.  **Model View Controller**: Pattern for separating concerns.
8.  **Chain of Responsibility**: Spring Security Filter Chain/Interceptors.

---

## 5. Anti-Patterns
Common solutions to common problems that are ineffective and risk being highly counterproductive.

1.  **God Object**: A class that knows too much or does too much. Solution: S.O.L.I.D principles.
2.  **Spaghetti Code**: Unstructured and difficult-to-maintain source code.
3.  **Gold Plating**: Adding unnecessary features or complexity beyond requirements.
4.  **Magic Numbers**: Using literal numbers in code. Solution: Use Constants.

---

## 6. Complex Technical Scenarios

### Topic 1: Singleton vs Static Class?
**Answer:**
*   **Singleton**: Can implement interfaces, extend classes, be passed as a parameter (Object), be lazy loaded. Ideal for stateful services.
*   **Static Class**: Cannot be passed as object, hard to mock/test (except PowerMock), procedural. Ideal for stateless utility methods (`Math.abs`).

### Topic 2: Dependency Injection vs Factory Pattern?
**Answer:**
*   **Factory**: Client proactively asks Factory for dependency (`Factory.create()`). High coupling to Factory.
*   **DI**: Container pushes dependency to Client (Constructor/Setter). Low coupling ("Don't call us, we'll call you").

---

## 7. Key Topics & Explanations

### Topic 1: Why use Builder Pattern?
**Answer:**
When a class has too many constructor parameters (telescoping constructor problem). It makes code readable and immutable.

### Topic 2: Explain SOLID Principles?
**Answer:**
*   **S**ingle Responsibility: Class should have one reason to change.
*   **O**pen/Closed: Open for extension, closed for modification.
*   **L**iskov Substitution: Subtypes must be substitutable for base types.
*   **I**nterface Segregation: Many specific interfaces are better than one general-purpose interface.
*   **D**ependency Inversion: Depend on abstractions, not concretions.
