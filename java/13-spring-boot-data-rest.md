# Spring Boot Data & REST - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** REST APIs, Spring Data JPA, Hibernate, Validation, Exception Handling, and tricky scenarios.

--

## Table of Contents

1. [Building REST APIs](#building-rest-apis)
2. [Spring Data JPA & Hibernate](#spring-data-jpa-hibernate)
3. [JPA Repository Interfaces](#jpa-repository-interfaces)
4. [Custom Queries (JPQL & Native)](#custom-queries-jpql-native)
5. [Relationships (OneToOne, OneToMany)](#relationships-onetoone-onetomany)
6. [Validation](#validation)
7. [Exception Handling (@ControllerAdvice)](#exception-handling-controlleradvice)
8. [Actuator & Monitoring](#actuator-monitoring)
9. [Complex Technical Scenarios](#complex-technical-scenarios)
10. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. Building REST APIs

REST (Representational State Transfer) is an architectural style for designing networked applications. It relies on a stateless, client-server, cacheable communications protocol - usually HTTP.

### Http Methods & Status Codes

| Method | Idempotent | Safe | Usage | Success Code |
|----|------|---|----|-------|
| `GET` | Yes | Yes | Retrieve resource | 200 OK |
| `POST` | No | No | Create resource | 201 Created |
| `PUT` | Yes | No | Update/Create resource (Full replacement) | 200 OK / 204 No Content |
| `PATCH` | No | No | Partial update | 200 OK |
| `DELETE`| Yes | No | Delete resource | 204 No Content |

### Annotations

*   `@RestController`: Combines `@Controller` and `@ResponseBody`.
*   `@RequestMapping("/api/users")`: Maps requests to controller methods.
*   `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`.
*   `@PathVariable`: Extracts value from URI (`/users/{id}`).
*   `@RequestParam`: Extracts query parameter (`/users?role=admin`).
*   `@RequestBody`: Maps JSON body to Java object.

### Example Controller

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public List<Product> getAllProducts() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product saved = service.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
```

--

## 2. Spring Data JPA & Hibernate

**Spring Data JPA** provides a layer of abstraction over JPA (Java Persistence API) to reduce boilerplate code required to implement data access layers.

**Hibernate** is the most popular JPA implementation. It maps Java classes to database tables (ORM).

### Entity Mapping (`@Entity`)

```java
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    // Getters and Setters
}
```

### Generation Strategies

*   `IDENTITY`: Relies on Auto-Increment column (MySQL).
*   `SEQUENCE`: Uses database sequence (PostgreSQL, Oracle).
*   `TABLE`: Simulates sequence using a table (Portable but slow).
*   `AUTO`: Picks appropriate strategy based on dialect.

--

## 3. JPA Repository Interfaces

Instead of writing DAO implementation (`EntityManager`), just extend an interface!

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Basic CRUD operations are provided automatically!
    // save(), findById(), findAll(), deleteById(), existsById()
}
```

### Derived Query Methods

Spring Data automatically generates queries from method names!

```java
List<Product> findByName(String name);
List<Product> findByPriceGreaterThan(BigDecimal price);
List<Product> findByNameContainingIgnoreCase(String name);
Optional<Product> findByNameAndPrice(String name, BigDecimal price);
```

--

## 4. Custom Queries (JPQL & Native)

When derived queries become too complex or inefficient.

### JPQL (Java Persistence Query Language)

Operates on Entities, not Tables. Database agnostic.

```java
@Query("SELECT p FROM Product p WHERE p.price > :minPrice")
List<Product> findExpensiveProducts(@Param("minPrice") BigDecimal minPrice);
```

### Native SQL

Operates directly on Tables. Database specific.

```java
@Query(value = "SELECT * FROM products WHERE price > ?1", nativeQuery = true)
List<Product> findExpensiveNative(BigDecimal minPrice);
```

### Modifying Queries (`@Modifying`)

Required for update/delete operations in custom queries.

```java
@Modifying
@Transactional
@Query("UPDATE Product p SET p.price = p.price * 1.1 WHERE p.id = :id")
int increasePrice(@Param("id") Long id);
```

--

## 5. Relationships (OneToOne, OneToMany)

### Fetch Types (Use `Lazy`!)

*   `FetchType.LAZY`: Data loaded ONLY when accessed (Recommended for ToMany).
*   `FetchType.EAGER`: Data loaded immediately with parent (Default for ToOne).

### Common Relationships

```java
@Entity
public class User {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private UserProfile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
}

@Entity
public class Order {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
```

**N+1 Select Problem**: Loading a list of N entities, then accessing a related entity for each triggers N additional queries.
**Solution**: Use `JOIN FETCH` in JPQL.
```sql
SELECT u FROM User u JOIN FETCH u.orders
```

--

## 6. Validation

Uses Bean Validation API (`javax.validation` / `jakarta.validation`).

### Dependencies

`spring-boot-starter-validation`

### Usage

1.  **Entity/DTO**:
    ```java
    public class UserDto {
        @NotNull(message = "Name cannot be null")
        @Size(min = 2, max = 30)
        private String name;

        @Email(message = "Email must be valid")
        private String email;
    }
    ```
2.  **Controller**:
    ```java
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        // ...
    }
    ```

--

## 7. Exception Handling (@ControllerAdvice)

Global handling for exceptions across all controllers.

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ProductNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
```

--

## 8. Actuator & Monitoring

Provides production-ready endpoints.

`spring-boot-starter-actuator`

*   `/actuator/health`: Application health status (UP/DOWN).
*   `/actuator/info`: Application info (git commit, build version).
*   `/actuator/metrics`: Detailed metrics (JVM memory, CPU, HTTP requests).
*   `/actuator/env`: Environment properties.
*   `/actuator/beans`: All beans in context.

**Security**: By default, only `/health` and `/info` are exposed. Others must be enabled in `application.properties`:
`management.endpoints.web.exposure.include=*`

--

## 9. Complex Technical Scenarios

### Topic 1: What is the N+1 problem in Hibernate?
**Answer:**
It happens when fetching a list of Parent entities, then accessing a Child association for each parent.
Query 1: Select all Parents (N rows).
Query 2...N+1: Select Child for Parent 1, Child for Parent 2...
**Solution**: Use `JOIN FETCH` (eager fetching in query).

### Topic 2: `JpaRepository` vs `CrudRepository` vs `PagingAndSortingRepository`?
**Answer:**
*   `CrudRepository`: Basic CRUD.
*   `PagingAndSortingRepository`: Extends Crud, adds pagination/sorting.
*   `JpaRepository`: Extends PagingAndSorting, adds JPA-specific methods (`flush`, `saveAndFlush`, `deleteInBatch`). Best choice usually!

### Topic 3: How does `@Transactional` work?
**Answer:**
It uses AOP (Aspect Oriented Programming). It creates a proxy around the bean.
*   Start Transaction before method execution.
*   Commit Transaction after successful execution.
*   Rollback Transaction on RuntimeException (unchecked).
*   Does **NOT** rollback on checked exceptions by default! (Unless `rollbackFor = Exception.class`).
*   Only works on **public** methods calls from **external** beans (self-invocation bypasses proxy).

### Topic 4: Put vs Patch?
**Answer:**
*   **PUT**: Replaces the entire resource. If fields are missing in the request, they are set to null.
*   **PATCH**: Updates only specific fields provided in the request.

--

## 10. Key Topics & Explanations

### Topic 1: Difference between Hibernate `save()` and `persist()`?
**Answer:** `save()` returns the generated ID immediately (might execute INSERT). `persist()` makes the instance managed but doesn't guarantee immediate ID generation (might wait for flush). JPA standard uses `persist()`. Spring Data uses `save()`.

### Topic 2: What is the use of `BaseEntity` with `@MappedSuperclass`?
**Answer:** To share common fields like `id`, `createdAt`, `updatedAt` across multiple entities without creating a separate table for the parent class.

### Topic 3: Initializing Database with data?
**Answer:**
1.  `data.sql`: Spring Boot executes this on startup if found in classpath.
2.  `CommandLineRunner`: Manually save data using repositories.

### Topic 4: Pagination in Spring Data JPA?
**Answer:**
Pass a `Pageable` object to repository method.
```java
Pageable pageable = PageRequest.of(0, 10, Sort.by("price"));
Page<Product> page = repository.findAll(pageable);
```

### Topic 5: Optimistic vs Pessimistic Locking?
**Answer:**
*   **Optimistic**: Uses `@Version` column. Throws `OptimisticLockException` if version mismatch on update. No DB locks held. Good for high concurrency.
*   **Pessimistic**: Uses DB locks (`SELECT ... FOR UPDATE`). Prevents others from reading/writing. Good for strict consistency but lower throughput.
