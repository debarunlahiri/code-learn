# Spring Boot Fundamentals - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Spring vs Spring Boot, Auto-configuration, Starters, Dependency Injection, IoC, Core Annotations, and tricky technical scenarios.

--

## Table of Contents

1. [Spring Boot Overview](#spring-boot-overview)
2. [Spring Boot Architecture](#spring-boot-architecture)
3. [Dependency Injection & IoC](#dependency-injection-ioc)
4. [Spring Boot Starters](#spring-boot-starters)
5. [Auto Configuration](#auto-configuration)
6. [Core Annotations](#core-annotations)
7. [Spring Beans & Scopes](#spring-beans-scopes)
8. [Application Properties & Profiles](#application-properties-profiles)
9. [Complex Technical Scenarios](#complex-technical-scenarios)
10. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. Spring Boot Overview

Spring Boot is an extension of the Spring Framework that eliminates boilerplate configuration required for setting up a Spring application.

### Key Features
1.  **Standalone**: Creates executable JARs with embedded servers (Tomcat, Jetty, Undertow).
2.  **Opinionated**: Provides "starter" dependencies to simplify build configuration.
3.  **Auto-configuration**: Automatically configures Spring based on jar dependencies.
4.  **Production-ready**: Includes metrics, health checks, and externalized configuration (Actuator).
5.  **No XML**: No code generation and no XML configuration requirement.

### Spring vs Spring Boot (Classic Concept Check)

| Feature | Spring Framework | Spring Boot |
|-----|---------|-------|
| **Configuration** | Requires extensive XML or Java config | Zero or minimal configuration (Auto-config) |
| **Server** | Needs external server (Tomcat/JBoss) | Embedded server (Tomcat default) |
| **Dependencies** | Must manage individual JAR versions | "Starters" handle dependencies and versions |
| **Deployment** | WAR file deployed to server | Executable JAR run with `java -jar` |
| **Boilerplate** | High boilerplate code | Reduced boilerplate |

--

## 2. Spring Boot Architecture

1.  **Spring Boot Starter**: Aggregates dependencies (e.g., `spring-boot-starter-web`).
2.  **Auto Configuration**: Scans runtime classpath and configures beans (e.g., if H2 is in classpath, configures in-memory DB).
3.  **Spring Boot CLI**: Command line interface (optional).
4.  **Actuator**: Monitoring and metrics.

### Execution Flow
1.  `main()` method runs `SpringApplication.run()`.
2.  Starts the Application Context.
3.  Performs Classpath Scan.
4.  Triggers Auto-configuration (`@EnableAutoConfiguration`).
5.  Starts Embedded Server (Tomcat).

--

## 3. Dependency Injection & IoC

### Inversion of Control (IoC)
IoC is a design principle where the control of object creation and management is transferred from the programmer to a container (Spring Container).

### Dependency Injection (DI)
DI is the implementation of IoC. The container "injects" dependencies into objects.

**Types of DI:**
1.  **Constructor Injection** (Recommended): Mandatory dependencies.
    ```java
    @Service
    public class UserService {
        private final UserRepository repo;
        
        @Autowired // Optional in newer versions
        public UserService(UserRepository repo) {
            this.repo = repo;
        }
    }
    ```
2.  **Setter Injection**: Optional dependencies.
    ```java
    @Autowired
    public void setRepo(UserRepository repo) { ... }
    ```
3.  **Field Injection** (Not Recommended):
    ```java
    @Autowired
    private UserRepository repo;
    ```
    *Drawbacks*: Difficult to test (cannot mock easily), hides dependencies.

--

## 4. Spring Boot Starters

Starters are a set of convenient dependency descriptors.

*   `spring-boot-starter-web`: For building web, including RESTful, apps using Spring MVC. Uses Tomcat.
*   `spring-boot-starter-data-jpa`: For JPA and Hibernate.
*   `spring-boot-starter-security`: For Spring Security.
*   `spring-boot-starter-test`: For testing (JUnit, Hamcrest, Mockito).
*   `spring-boot-starter-actuator`: For production-ready features (metrics, health).

--

## 5. Auto Configuration

The magic behind Spring Boot. It attempts to configure your Spring application based on the jar dependencies that you have added.

*   **Annotation**: `@EnableAutoConfiguration` (included in `@SpringBootApplication`).
*   **Mechanism**: Looks for `META-INF/spring.factories`.
*   **Conditional Annotations**:
    *   `@ConditionalOnClass`: Configures if a class is present.
    *   `@ConditionalOnMissingBean`: Configures if a bean is NOT already defined.
    *   `@ConditionalOnProperty`: Configures based on property value.

--

## 6. Core Annotations

### @SpringBootApplication
It is a combination of 3 annotations:
1.  `@Configuration`: Marks the class as a source of bean definitions.
2.  `@EnableAutoConfiguration`: Enables auto-configuration mechanism.
3.  `@ComponentScan`: Scans for components (`@Component`, `@Service`, etc.) in the current package and sub-packages.

### Stereotype Annotations
*   `@Component`: Generic stereotype for any Spring-managed component.
*   `@Service`: Specialized `@Component` for Service layer (Business logic).
*   `@Repository`: Specialized `@Component` for DAO layer (Database access). Handles DB exceptions conversion.
*   `@Controller`: Specialized `@Component` for Web layer (MVC).
*   `@RestController`: Combination of `@Controller` + `@ResponseBody`.

### Configuration Annotations
*   `@Bean`: Marks a method to produce a bean managed by Spring container.
*   `@Value`: Injects values from properties files.
    ```java
    @Value("${app.name}")
    private String appName;
    ```
*   `@Qualifier`: Used with `@Autowired` to avoid ambiguity when multiple beans of same type exist.
*   `@Primary`: Gives preference to a bean when multiple candidates are qualified.
*   `@Lazy`: Bean is initialized only when requested (default is Eager).

--

## 7. Spring Beans & Scopes

A **Bean** is an object that is instantiated, assembled, and managed by a Spring IoC container.

### Bean Scopes (`@Scope`)
1.  **Singleton** (Default): Single instance per Spring container.
2.  **Prototype**: New instance every time it is requested.
3.  **Request**: Single instance per HTTP request (Web only).
4.  **Session**: Single instance per HTTP session (Web only).
5.  **GlobalSession**: Global session scope (Portlet only).

**Singleton Bean is NOT thread-safe by default!** Since it's shared, stateful fields can cause concurrency issues.

--

## 8. Application Properties & Profiles

Configuration is externalized in `application.properties` or `application.yml`.

### Profiles
Used to segregate parts of application configuration and make it only available in certain environments (dev, test, prod).

1.  Create `application-dev.properties` and `application-prod.properties`.
2.  Active profile:
    *   `spring.profiles.active=dev` in `application.properties`.
    *   Command line: `java -jar app.jar -spring.profiles.active=prod`.

### @ConfigurationProperties
Binds related properties to a POJO.
```java
@Component
@ConfigurationProperties(prefix = "mail")
public class MailConfig {
    private String host;
    private int port;
    // getters and setters
}
```

--

## 9. Complex Technical Scenarios

### Topic 1: Can we override/replace the Embedded Tomcat server?
**Answer:** Yes. You can exclude `tomcat-starter` dependency in `pom.xml` and add `jetty-starter` or `undertow-starter`.

### Topic 2: How runs the main() method in Spring Boot?
**Answer:** It calls `SpringApplication.run()`, which bootstraps the application, creates the ApplicationContext, triggers auto-configuration, and starts the embedded server.

### Topic 3: Difference between @Component, @Repository, and @Service?
**Answer:**
*   `@Component` is the generic annotation.
*   `@Repository` adds automatic persistence exception translation (DB errors -> Spring DataAccessException).
*   `@Service` currently adds no extra behavior but clarifies intent (Business Logic).

### Topic 4: How to disable a specific Auto-configuration class?
**Answer:**
```java
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
```

### Topic 5: What is the difference between @Controller and @RestController?
**Answer:**
*   `@Controller`: Returns a View (HTML/JSP) typically. You need `@ResponseBody` on each method to return JSON/XML.
*   `@RestController`: Convenience annotation that combines `@Controller` and `@ResponseBody`. Returns data directly (JSON/XML).

--

## 10. Key Topics & Explanations

### Topic 1: What is the Spring Boot Actuator?
**Answer:** Provides production-ready features to monitor and manage the application. It exposes endpoints like `/health`, `/metrics`, `/info`, `/env`, `/beans`.

### Topic 2: What is Dependency Injection?
**Answer:** A design pattern where dependencies (objects an object needs) are provided ('injected') by an external entity (Container) rather than the object creating them itself. This promotes loose coupling and easier testing.

### Topic 3: What is the default port of Tomcat? How to change it?
**Answer:** Default is 8080. Change it in `application.properties`: `server.port=9090`.

### Topic 4: Explain the Spring Boot directory structure.
**Answer:**
*   `src/main/java`: Source code.
*   `src/main/resources`: Config files (`application.properties`), static assets (`/static`), templates (`/templates`).
*   `src/test/java`: Test code.
*   `pom.xml`: Maven dependencies.

### Topic 5: How to handle exceptions globally in Spring Boot?
**Answer:** Use `@ControllerAdvice` class with `@ExceptionHandler` methods. This acts as an interceptor for exceptions thrown by any controller.

### Topic 6: Can we create a non-web application in Spring Boot?
**Answer:** Yes. Disable the web environment in `application.properties` (or via code) and implement `CommandLineRunner` or `ApplicationRunner` interface to execute code at startup.

### Topic 7: What is the purpose of `mvn spring-boot:run`?
**Answer:** It's a Maven goal provided by the Spring Boot Maven Plugin to run the application directly from the source without packaging it into a JAR first.

### Topic 8: How does Spring Boot handle logging?
**Answer:** Uses Commons Logging for internal logging but leaves the underlying log implementation open. Default is **Logback**. Output can be console or file (`logging.file.name`).
