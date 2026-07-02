# Spring Boot Confusing Technical Topics - Complete Guide

**Target:** Spring Boot backend technical assessments and project discussion rounds.  
**Focus:** Topics that look similar, are easy to misuse, or are commonly asked through
real-world backend scenarios.

## Table of Contents

1. [Spring vs Spring Boot](#1-spring-vs-spring-boot)
2. [`@Component` vs `@Service` vs `@Repository`](#2-component-vs-service-vs-repository)
3. [`@Controller` vs `@RestController`](#3-controller-vs-restcontroller)
4. [`@Autowired` vs Constructor Injection](#4-autowired-vs-constructor-injection)
5. [`@Bean` vs `@Component`](#5-bean-vs-component)
6. [`@Configuration` vs `@Component`](#6-configuration-vs-component)
7. [`@SpringBootApplication` Internals](#7-springbootapplication-internals)
8. [Component Scan Confusions](#8-component-scan-confusions)
9. [Bean Scope Confusions](#9-bean-scope-confusions)
10. [Singleton Bean vs Singleton Pattern](#10-singleton-bean-vs-singleton-pattern)
11. [Bean Lifecycle](#11-bean-lifecycle)
12. [`@PostConstruct` vs `InitializingBean`](#12-postconstruct-vs-initializingbean)
13. [`application.properties` vs `application.yml`](#13-applicationproperties-vs-applicationyml)
14. [`@Value` vs `@ConfigurationProperties`](#14-value-vs-configurationproperties)
15. [Profiles](#15-profiles)
16. [`@RequestParam` vs `@PathVariable` vs `@RequestBody`](#16-requestparam-vs-pathvariable-vs-requestbody)
17. [`@RequestBody` vs `@ResponseBody`](#17-requestbody-vs-responsebody)
18. [`PUT` vs `PATCH`](#18-put-vs-patch)
19. [`@ControllerAdvice` vs `@ExceptionHandler`](#19-controlleradvice-vs-exceptionhandler)
20. [`ResponseEntity` vs Direct Response Object](#20-responseentity-vs-direct-response-object)
21. [DTO vs Entity](#21-dto-vs-entity)
22. [`CrudRepository` vs `JpaRepository`](#22-crudrepository-vs-jparepository)
23. [`findById()` vs `getReferenceById()`](#23-findbyid-vs-getreferencebyid)
24. [`save()` Insert vs Update](#24-save-insert-vs-update)
25. [`persist()` vs `merge()`](#25-persist-vs-merge)
26. [Lazy Loading vs Eager Loading](#26-lazy-loading-vs-eager-loading)
27. [N+1 Query Problem](#27-n1-query-problem)
28. [`@OneToMany` vs `@ManyToOne`](#28-onetomany-vs-manytoone)
29. [Owning Side vs Inverse Side](#29-owning-side-vs-inverse-side)
30. [Cascade vs Orphan Removal](#30-cascade-vs-orphan-removal)
31. [`@Transactional` on Class vs Method](#31-transactional-on-class-vs-method)
32. [Transaction Propagation](#32-transaction-propagation)
33. [Transaction Isolation](#33-transaction-isolation)
34. [Checked Exceptions and Rollback](#34-checked-exceptions-and-rollback)
35. [`readOnly = true`](#35-readonly--true)
36. [Security Filter Chain](#36-security-filter-chain)
37. [Authentication vs Authorization](#37-authentication-vs-authorization)
38. [JWT vs Session](#38-jwt-vs-session)
39. [CSRF Confusions](#39-csrf-confusions)
40. [CORS Confusions](#40-cors-confusions)
41. [`OncePerRequestFilter` vs Generic Filter](#41-onceperrequestfilter-vs-generic-filter)
42. [`@Mock` vs `@MockBean`](#42-mock-vs-mockbean)
43. [`@SpringBootTest` vs Slice Tests](#43-springboottest-vs-slice-tests)
44. [`@WebMvcTest` vs `@DataJpaTest`](#44-webmvctest-vs-datajpatest)
45. [Actuator](#45-actuator)
46. [Health Check vs Readiness vs Liveness](#46-health-check-vs-readiness-vs-liveness)
47. [Spring Boot Logging](#47-spring-boot-logging)
48. [Caching with `@Cacheable`](#48-caching-with-cacheable)
49. [`@Async` Confusions](#49-async-confusions)
50. [Scheduling with `@Scheduled`](#50-scheduling-with-scheduled)

---

## 1. Spring vs Spring Boot

Spring is the core framework. Spring Boot is an opinionated way to build Spring
applications quickly with auto-configuration, embedded servers, starters, and production
features.

Key answer:

- Spring gives the building blocks.
- Spring Boot reduces boilerplate and provides sensible defaults.

## 2. `@Component` vs `@Service` vs `@Repository`

All three create Spring beans through component scanning.

| Annotation | Intended layer |
| --- | --- |
| `@Component` | Generic bean |
| `@Service` | Business logic |
| `@Repository` | Data access |

`@Repository` also helps translate persistence exceptions into Spring's
`DataAccessException` hierarchy.

## 3. `@Controller` vs `@RestController`

`@RestController` is equivalent to `@Controller` plus `@ResponseBody`.

```java
@RestController
class UserController {
    @GetMapping("/users")
    List<UserDto> findAll() {
        return userService.findAll();
    }
}
```

Use `@Controller` for server-rendered views. Use `@RestController` for REST APIs.

## 4. `@Autowired` vs Constructor Injection

Constructor injection is preferred because dependencies are required, immutable, and easy
to test.

```java
@Service
class OrderService {
    private final OrderRepository repository;

    OrderService(OrderRepository repository) {
        this.repository = repository;
    }
}
```

Field injection works, but it hides dependencies and makes unit testing harder.

## 5. `@Bean` vs `@Component`

Use `@Component` on your own classes. Use `@Bean` when you need to create a bean from
configuration code, often for third-party classes.

```java
@Configuration
class AppConfig {
    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
```

## 6. `@Configuration` vs `@Component`

`@Configuration` is a specialized component for bean definitions. Spring enhances full
`@Configuration` classes so calls between `@Bean` methods return managed singleton
beans.

```java
@Configuration
class Config {
    @Bean
    ServiceA serviceA() {
        return new ServiceA();
    }
}
```

## 7. `@SpringBootApplication` Internals

`@SpringBootApplication` combines:

- `@Configuration`
- `@EnableAutoConfiguration`
- `@ComponentScan`

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

## 8. Component Scan Confusions

Spring scans from the package where the main application class is located downward.

Common issue:

If your controller or service is outside the base package, Spring may not detect it.

```java
@SpringBootApplication(scanBasePackages = "com.example")
class App {}
```

## 9. Bean Scope Confusions

| Scope | Meaning |
| --- | --- |
| `singleton` | One bean instance per Spring container |
| `prototype` | New instance when requested from container |
| `request` | One instance per HTTP request |
| `session` | One instance per HTTP session |

Default scope is `singleton`.

## 10. Singleton Bean vs Singleton Pattern

Spring singleton means one object per application context. Singleton pattern means one
object per JVM classloader using code-level control.

Key answer:

Spring singleton is container-managed, not the same as the GoF Singleton pattern.

## 11. Bean Lifecycle

Common lifecycle flow:

1. Instantiate bean.
2. Populate dependencies.
3. Run aware callbacks.
4. Run bean post-processors.
5. Run initialization callbacks.
6. Bean is ready.
7. Run destruction callbacks during shutdown.

## 12. `@PostConstruct` vs `InitializingBean`

Both run after dependency injection.

```java
@PostConstruct
void init() {
    loadCache();
}
```

Prefer `@PostConstruct` for simple initialization. Avoid putting heavy startup logic
inside it unless the application should fail fast.

## 13. `application.properties` vs `application.yml`

Both configure Spring Boot. YAML is better for nested configuration. Properties is
simple and explicit.

```yaml
server:
  port: 8081
```

```properties
server.port=8081
```

## 14. `@Value` vs `@ConfigurationProperties`

Use `@Value` for one or two simple values. Use `@ConfigurationProperties` for grouped
configuration.

```java
@ConfigurationProperties(prefix = "payment")
record PaymentProperties(String provider, int timeoutSeconds) {}
```

`@ConfigurationProperties` is easier to validate and maintain.

## 15. Profiles

Profiles activate environment-specific beans and properties.

```java
@Profile("dev")
@Bean
DataSource devDataSource() {
    return dataSource;
}
```

Run with:

```bash
java -jar app.jar --spring.profiles.active=dev
```

## 16. `@RequestParam` vs `@PathVariable` vs `@RequestBody`

| Annotation | Reads from |
| --- | --- |
| `@RequestParam` | Query parameter |
| `@PathVariable` | URL path |
| `@RequestBody` | Request body |

```java
@GetMapping("/users/{id}")
UserDto find(@PathVariable long id, @RequestParam boolean active) {
    return service.find(id, active);
}
```

## 17. `@RequestBody` vs `@ResponseBody`

`@RequestBody` converts request JSON into a Java object. `@ResponseBody` converts return
value into HTTP response body.

```java
@PostMapping("/users")
UserDto create(@RequestBody CreateUserRequest request) {
    return service.create(request);
}
```

In `@RestController`, `@ResponseBody` is already included.

## 18. `PUT` vs `PATCH`

`PUT` usually replaces the full resource. `PATCH` partially updates the resource.

```java
@PutMapping("/users/{id}")
UserDto replace(@PathVariable long id, @RequestBody UserDto body) {}

@PatchMapping("/users/{id}")
UserDto updatePartially(@PathVariable long id, @RequestBody Map<String, Object> patch) {}
```

## 19. `@ControllerAdvice` vs `@ExceptionHandler`

`@ExceptionHandler` handles exceptions in one controller. `@ControllerAdvice` applies
exception handling globally.

```java
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
```

## 20. `ResponseEntity` vs Direct Response Object

Return direct objects when status is always standard. Use `ResponseEntity` when you need
to control status, headers, or body.

```java
return ResponseEntity.status(HttpStatus.CREATED).body(user);
```

## 21. DTO vs Entity

Entity represents database state. DTO represents API input/output.

Key answer:

Do not expose JPA entities directly from controllers. DTOs protect API contracts and
avoid lazy loading or serialization issues.

## 22. `CrudRepository` vs `JpaRepository`

`CrudRepository` provides basic CRUD. `JpaRepository` adds JPA-specific operations like
flush, batch deletes, and pagination/sorting through parent interfaces.

```java
interface UserRepository extends JpaRepository<User, Long> {}
```

Use `JpaRepository` for most Spring Data JPA projects.

## 23. `findById()` vs `getReferenceById()`

`findById()` hits the database and returns `Optional`. `getReferenceById()` returns a
lazy proxy and may fail later when accessed.

```java
Optional<User> user = repository.findById(id);
User ref = repository.getReferenceById(id);
```

Use `findById()` when you need to verify existence immediately.

## 24. `save()` Insert vs Update

`save()` can insert or update depending on entity state and identifier.

```java
User saved = repository.save(user);
```

Common trap:

Calling `save()` with an existing id may update an existing row.

## 25. `persist()` vs `merge()`

`persist()` makes a new entity managed. `merge()` copies detached state into a managed
entity and returns that managed entity.

```java
User managed = entityManager.merge(detachedUser);
```

Use the object returned by `merge()`.

## 26. Lazy Loading vs Eager Loading

Lazy loading fetches related data when accessed. Eager loading fetches it immediately.

Common issues:

- Lazy loading outside transaction can throw `LazyInitializationException`.
- Eager loading can fetch too much data.

## 27. N+1 Query Problem

N+1 happens when one query loads parent rows and then one extra query is executed for
each parent row.

Fix options:

- `JOIN FETCH`
- Entity graph
- Batch fetching
- DTO projection

```java
@Query("select u from User u join fetch u.orders")
List<User> findAllWithOrders();
```

## 28. `@OneToMany` vs `@ManyToOne`

`@ManyToOne` is usually the owning side because it holds the foreign key.

```java
@ManyToOne
@JoinColumn(name = "department_id")
private Department department;
```

Prefer modeling the owning side carefully to avoid unexpected SQL.

## 29. Owning Side vs Inverse Side

The owning side controls the foreign key update. `mappedBy` marks the inverse side.

```java
@OneToMany(mappedBy = "department")
private List<Employee> employees;
```

Updating only the inverse collection may not update the database relationship.

## 30. Cascade vs Orphan Removal

Cascade propagates entity operations. Orphan removal deletes children removed from the
parent collection.

```java
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
private List<OrderItem> items = new ArrayList<>();
```

Use carefully. Accidental deletes are common when relationships are modeled casually.

## 31. `@Transactional` on Class vs Method

Class-level `@Transactional` applies defaults to all public methods. Method-level
annotation overrides it.

```java
@Service
@Transactional(readOnly = true)
class UserService {
    @Transactional
    UserDto create(CreateUserRequest request) {}
}
```

## 32. Transaction Propagation

| Propagation | Meaning |
| --- | --- |
| `REQUIRED` | Join existing or create new transaction |
| `REQUIRES_NEW` | Suspend current and create new transaction |
| `MANDATORY` | Existing transaction required |
| `SUPPORTS` | Join if present, otherwise run without |

Default is `REQUIRED`.

## 33. Transaction Isolation

Isolation controls how transactions see each other's changes.

| Isolation | Prevents |
| --- | --- |
| `READ_COMMITTED` | Dirty reads |
| `REPEATABLE_READ` | Dirty and non-repeatable reads |
| `SERIALIZABLE` | Dirty, non-repeatable, and phantom reads |

Higher isolation can reduce concurrency.

## 34. Checked Exceptions and Rollback

By default, Spring rolls back on unchecked exceptions, not checked exceptions.

```java
@Transactional(rollbackFor = IOException.class)
void importFile() throws IOException {
    throw new IOException("failed");
}
```

## 35. `readOnly = true`

`readOnly = true` gives a hint that the transaction is read-only. It can improve
performance depending on JPA provider and database behavior.

```java
@Transactional(readOnly = true)
List<UserDto> findAll() {
    return repository.findAll().stream().map(mapper::toDto).toList();
}
```

Do not use it for methods that modify data.

## 36. Security Filter Chain

Spring Security processes requests through a chain of filters. Authentication filters,
authorization filters, CSRF filters, and custom filters run before the controller.

```java
@Bean
SecurityFilterChain security(HttpSecurity http) throws Exception {
    return http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll()
                    .anyRequest().authenticated())
            .build();
}
```

## 37. Authentication vs Authorization

Authentication verifies identity. Authorization checks permissions.

Example:

- Username/password login is authentication.
- Checking `ROLE_ADMIN` for an endpoint is authorization.

## 38. JWT vs Session

| Topic | JWT | Session |
| --- | --- | --- |
| State | Usually stateless | Server-side state |
| Revocation | Harder before expiry | Easier |
| Scaling | Easier across nodes | Needs shared session storage |
| Storage | Client stores token | Client stores session id |

JWT works well for stateless APIs. Sessions are simpler when revocation matters.

## 39. CSRF Confusions

CSRF matters mainly for browser-based session authentication where cookies are sent
automatically.

For stateless APIs using bearer tokens in the `Authorization` header, CSRF is usually
less relevant.

Do not disable CSRF blindly. Understand the authentication mechanism first.

## 40. CORS Confusions

CORS is enforced by browsers, not by backend-to-backend calls.

```java
@CrossOrigin(origins = "https://app.example.com")
@RestController
class ApiController {}
```

Server must return allowed origins, methods, and headers for browser requests.

## 41. `OncePerRequestFilter` vs Generic Filter

`OncePerRequestFilter` ensures the filter runs once per request dispatch.

```java
class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        chain.doFilter(request, response);
    }
}
```

Use it for JWT authentication filters.

## 42. `@Mock` vs `@MockBean`

`@Mock` is a Mockito mock in a unit test. `@MockBean` replaces a Spring bean inside the
application context.

```java
@Mock
UserRepository repository;

@MockBean
PaymentClient paymentClient;
```

Use `@Mock` for fast unit tests. Use `@MockBean` in Spring context tests.

## 43. `@SpringBootTest` vs Slice Tests

`@SpringBootTest` loads the full application context. Slice tests load only part of the
application.

Key answer:

Use slice tests for focused controller/repository tests. Use `@SpringBootTest` when you
need full integration wiring.

## 44. `@WebMvcTest` vs `@DataJpaTest`

| Test | Loads |
| --- | --- |
| `@WebMvcTest` | MVC layer only |
| `@DataJpaTest` | JPA repositories and persistence layer |

```java
@WebMvcTest(UserController.class)
class UserControllerTest {}

@DataJpaTest
class UserRepositoryTest {}
```

## 45. Actuator

Actuator exposes production-ready endpoints like health, metrics, info, and environment
details.

```properties
management.endpoints.web.exposure.include=health,info,metrics
```

Expose only what is needed. Sensitive endpoints should be secured.

## 46. Health Check vs Readiness vs Liveness

| Check | Meaning |
| --- | --- |
| Health | General application health |
| Readiness | Can receive traffic |
| Liveness | Should process be restarted |

In Kubernetes, readiness and liveness should not always be the same check.

## 47. Spring Boot Logging

Spring Boot uses Commons Logging internally and typically Logback by default.

```java
private static final Logger log = LoggerFactory.getLogger(UserService.class);

log.info("User created: {}", userId);
```

Avoid string concatenation in logs; use placeholders.

## 48. Caching with `@Cacheable`

`@Cacheable` stores method results based on cache name and key.

```java
@Cacheable(value = "users", key = "#id")
UserDto findById(long id) {
    return repository.findById(id).map(mapper::toDto).orElseThrow();
}
```

Common trap:

Self-invocation does not trigger Spring proxy-based caching.

## 49. `@Async` Confusions

`@Async` runs a method on another thread when async support is enabled.

```java
@EnableAsync
@Configuration
class AsyncConfig {}

@Async
CompletableFuture<Void> sendEmail() {
    return CompletableFuture.completedFuture(null);
}
```

Common trap:

Self-invocation does not trigger `@Async` because Spring proxies are bypassed.

## 50. Scheduling with `@Scheduled`

`@Scheduled` runs methods on a schedule when scheduling is enabled.

```java
@EnableScheduling
@Configuration
class SchedulingConfig {}

@Scheduled(fixedRate = 60000)
void refreshCache() {
    cache.refresh();
}
```

Use distributed locks if the same scheduled job runs on multiple application instances.

