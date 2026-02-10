# Spring Boot Data & REST - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** REST APIs, Spring Data JPA (Deep Dive), Hibernate Internals, Entity Lifecycle, Relationships, N+1 Problem, Handling Large Data (Pagination, Batch, Streaming), Locking, and tricky scenarios.

---

## Table of Contents

1. [Building REST APIs](#building-rest-apis)
2. [Spring Data JPA & Hibernate (Deep Dive)](#spring-data-jpa-hibernate-deep-dive)
3. [JPA Repository Hierarchy](#jpa-repository-hierarchy)
4. [Derived Query Methods](#derived-query-methods)
5. [Custom Queries (JPQL & Native)](#custom-queries-jpql-native)
6. [Entity Relationships (Deep Dive)](#entity-relationships-deep-dive)
7. [Hibernate Internals](#hibernate-internals)
8. [Handling Large Data](#handling-large-data)
9. [Validation](#validation)
10. [Exception Handling (@ControllerAdvice)](#exception-handling-controlleradvice)
11. [Complex Technical Scenarios](#complex-technical-scenarios)
12. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Building REST APIs

REST (Representational State Transfer) is an architectural style for designing networked applications. It relies on a stateless, client-server, cacheable communications protocol — usually HTTP.

> **Analogy:** REST is like a restaurant menu. The menu (API documentation) lists available dishes (resources). You tell the waiter (HTTP) what you want using standard actions: "Get me the soup" (GET), "Add this new dish" (POST), "Replace my order" (PUT), "Cancel my order" (DELETE). You don't need to know HOW the kitchen prepares it.

### HTTP Methods & Status Codes

| Method | Idempotent | Safe | Usage | Success Code |
|--------|-----------|------|-------|-------------|
| `GET` | Yes | Yes | Retrieve resource | 200 OK |
| `POST` | No | No | Create resource | 201 Created |
| `PUT` | Yes | No | Update/Replace entire resource | 200 OK / 204 No Content |
| `PATCH` | No | No | Partial update | 200 OK |
| `DELETE` | Yes | No | Delete resource | 204 No Content |

> **Idempotent** = calling it 10 times has the same effect as calling it once. GET `/users/1` always returns the same user. DELETE `/users/1` deletes once — calling it again is a no-op (or 404). POST `/users` creates a NEW user each time — **not** idempotent.

### Key Annotations

| Annotation | Purpose | Example |
|-----------|---------|---------|
| `@RestController` | Combines `@Controller` + `@ResponseBody` | Class-level |
| `@RequestMapping("/api/users")` | Base URL for all methods in the controller | Class-level |
| `@GetMapping("/{id}")` | Handle GET requests | Method-level |
| `@PostMapping` | Handle POST requests | Method-level |
| `@PathVariable` | Extract value from URI path | `/users/{id}` → `@PathVariable Long id` |
| `@RequestParam` | Extract query parameter | `/users?role=admin` → `@RequestParam String role` |
| `@RequestBody` | Map JSON request body to Java object | `@RequestBody UserDto dto` |
| `@ResponseStatus` | Set HTTP status code | `@ResponseStatus(HttpStatus.CREATED)` |

### Example Controller (Production-Style)

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    
    // Constructor injection (recommended)
    public ProductController(ProductService service) {
        this.service = service;
    }

    // GET /api/products — list all (with pagination)
    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return service.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
    }

    // GET /api/products/5 — get one by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)                      // 200 if found
                .orElse(ResponseEntity.notFound().build());    // 404 if not
    }

    // POST /api/products — create new
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductDto dto) {
        Product saved = service.save(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);  // 201 + Location header
    }

    // PUT /api/products/5 — full update
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id,
                                          @Valid @RequestBody ProductDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // DELETE /api/products/5
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 204
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
```

### ResponseEntity — Why use it?

`ResponseEntity` gives you **full control** over the HTTP response — status code, headers, and body:

```java
// Without ResponseEntity — always returns 200
@GetMapping("/{id}")
public Product get(@PathVariable Long id) {
    return service.findById(id);  // What if not found? Still returns 200 with null!
}

// With ResponseEntity — proper HTTP semantics
@GetMapping("/{id}")
public ResponseEntity<Product> get(@PathVariable Long id) {
    return service.findById(id)
        .map(product -> ResponseEntity.ok(product))       // 200 + body
        .orElse(ResponseEntity.notFound().build());        // 404, no body
}

// Custom headers
return ResponseEntity
    .status(HttpStatus.CREATED)
    .header("X-Custom-Header", "value")
    .body(saved);
```

---

## 2. Spring Data JPA & Hibernate (Deep Dive)

### 2.1 What is JPA?

**JPA** (Java Persistence API) is a **specification** (like an interface) — it defines HOW Java objects should be mapped to database tables. It does NOT provide the actual code.

### 2.2 What is Hibernate?

**Hibernate** is the most popular **implementation** of JPA — it provides the actual code that does the ORM (Object-Relational Mapping).

### 2.3 What is Spring Data JPA?

**Spring Data JPA** is a layer ON TOP of JPA/Hibernate that **eliminates boilerplate** — you write interfaces, and Spring generates the implementation at runtime.

```
Your Code
    ↓ uses
Spring Data JPA (convenience layer — auto-generates implementations)
    ↓ uses
JPA (specification — defines the API/rules)
    ↓ implemented by
Hibernate (actual engine — generates SQL, manages connections)
    ↓ talks to
Database (MySQL, PostgreSQL, Oracle, etc.)
```

> **Analogy:**  
> - **JPA** = The recipe book (defines how to cook).  
> - **Hibernate** = The chef (actually cooks following the recipe).  
> - **Spring Data JPA** = The restaurant manager who says "just tell me what you want" and coordinates the chef behind the scenes.

### 2.4 Entity Mapping

An `@Entity` class maps to a **database table**. Each instance maps to a **row**. Each field maps to a **column**.

```java
@Entity                          // Marks this class as a JPA entity (maps to a table)
@Table(name = "products")       // Table name (optional — defaults to class name)
public class Product {
    
    @Id                          // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;

    @Column(nullable = false, unique = true, length = 100)  // Column constraints
    private String name;

    @Column(precision = 10, scale = 2)  // For decimal types
    private BigDecimal price;
    
    @Column(name = "is_active")  // Map to different column name
    private boolean active = true;

    @Enumerated(EnumType.STRING)  // Store enum as string, not ordinal
    private Category category;

    @CreationTimestamp            // Auto-set on INSERT
    private LocalDateTime createdAt;
    
    @UpdateTimestamp              // Auto-update on every UPDATE
    private LocalDateTime updatedAt;
    
    // Getters and Setters
}
```

### 2.5 ID Generation Strategies

| Strategy | How it works | Best for | Gotcha |
|----------|-------------|----------|--------|
| `IDENTITY` | Database auto-increment (`AUTO_INCREMENT`) | MySQL | Hibernate cannot batch inserts (needs ID immediately) |
| `SEQUENCE` | Database sequence object (`CREATE SEQUENCE`) | PostgreSQL, Oracle | Best performance — allows batching |
| `TABLE` | Simulates sequences using a table | Portability | Slowest — contention on the table |
| `AUTO` | Hibernate picks based on DB dialect | Quick start | May surprise you — check what it picks! |

```java
// Best for PostgreSQL/Oracle — allows batch inserts
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
@SequenceGenerator(name = "product_seq", sequenceName = "product_sequence", allocationSize = 50)
private Long id;
// allocationSize = 50 means Hibernate grabs 50 IDs at once from the sequence
// → fewer DB calls when inserting many records
```

> **Why `SEQUENCE` is better:** With `IDENTITY`, Hibernate MUST execute an INSERT immediately to get the generated ID, which prevents batch inserts. With `SEQUENCE`, it can pre-fetch IDs and batch multiple INSERTs in one transaction.

---

## 3. JPA Repository Hierarchy

### 3.1 The Hierarchy

```
Repository<T, ID>                    (Marker interface — no methods)
    ↓
CrudRepository<T, ID>               (Basic CRUD — 11 methods)
    ↓
ListCrudRepository<T, ID>           (Returns List instead of Iterable)
    ↓
PagingAndSortingRepository<T, ID>   (Adds pagination + sorting)
    ↓
JpaRepository<T, ID>                (JPA-specific: flush, batch, examples)
```

### 3.2 What each level provides

| Interface | Key Methods | When to use |
|-----------|-------------|-------------|
| **CrudRepository** | `save()`, `findById()`, `findAll()`, `deleteById()`, `existsById()`, `count()` | Simple apps with basic CRUD |
| **PagingAndSortingRepository** | + `findAll(Pageable)`, `findAll(Sort)` | When you need pagination |
| **JpaRepository** | + `flush()`, `saveAndFlush()`, `deleteAllInBatch()`, `findAll(Example)`, `getReferenceById()` | **Most common choice** — gives you everything |

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // You get ALL of these for FREE (no implementation needed):
    
    // From CrudRepository:
    // save(entity)            → INSERT or UPDATE (checks if ID exists)
    // saveAll(entities)       → Bulk save
    // findById(id)            → Returns Optional<Product>
    // existsById(id)          → Returns boolean
    // findAll()               → Returns List<Product>
    // count()                 → Returns long
    // deleteById(id)          → Delete by primary key
    // delete(entity)          → Delete by entity
    // deleteAll()             → Delete everything (careful!)
    
    // From JpaRepository:
    // flush()                 → Force pending changes to DB immediately
    // saveAndFlush(entity)    → Save + flush in one call
    // deleteAllInBatch()      → Single DELETE query (faster than deleteAll)
    // getReferenceById(id)    → Returns a PROXY (lazy — no DB hit until accessed)
}
```

### 3.3 `save()` — Does it INSERT or UPDATE?

```java
Product p = new Product();
p.setName("iPhone");
productRepository.save(p);     // ID is null → Hibernate does INSERT
                                // After save, p.getId() has the generated ID

Product existing = productRepository.findById(1L).orElseThrow();
existing.setPrice(new BigDecimal("999"));
productRepository.save(existing);  // ID is NOT null → Hibernate does UPDATE
```

> `save()` checks `isNew()` — if the entity has no ID (or ID is 0/null), it's treated as new (INSERT). If the entity has an ID, it merges (UPDATE).

### 3.4 `findById()` vs `getReferenceById()`

| Method | Returns | DB Hit | Use When |
|--------|---------|--------|----------|
| `findById(id)` | `Optional<T>` (actual entity loaded from DB) | **Yes** — executes SELECT immediately | You need to read the entity's data |
| `getReferenceById(id)` | Proxy (lazy reference — no data loaded) | **No** — until you access a field | You just need a reference to set a relationship |

```java
// Scenario: Creating an Order linked to an existing User

// ❌ Wasteful — loads entire User just to set a foreign key
User user = userRepository.findById(5L).orElseThrow();  // SELECT * FROM users WHERE id=5
order.setUser(user);

// ✅ Efficient — creates a proxy with just the ID, no DB query
User userRef = userRepository.getReferenceById(5L);  // No SQL executed!
order.setUser(userRef);  // Hibernate only needs the ID for the foreign key
orderRepository.save(order);  // INSERT INTO orders (user_id, ...) VALUES (5, ...)
```

---

## 4. Derived Query Methods

Spring Data reads the method name and **auto-generates** the SQL query. No SQL, no `@Query` — just a well-named method.

### 4.1 Keyword Reference

```java
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ── Equality ──
    List<Product> findByName(String name);
    // → WHERE name = ?

    List<Product> findByNameAndCategory(String name, Category cat);
    // → WHERE name = ? AND category = ?

    List<Product> findByNameOrCategory(String name, Category cat);
    // → WHERE name = ? OR category = ?

    // ── Comparison ──
    List<Product> findByPriceGreaterThan(BigDecimal price);
    // → WHERE price > ?

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
    // → WHERE price BETWEEN ? AND ?

    List<Product> findByPriceLessThanEqual(BigDecimal price);
    // → WHERE price <= ?

    // ── String matching ──
    List<Product> findByNameContaining(String fragment);
    // → WHERE name LIKE '%fragment%'

    List<Product> findByNameStartingWith(String prefix);
    // → WHERE name LIKE 'prefix%'

    List<Product> findByNameContainingIgnoreCase(String fragment);
    // → WHERE LOWER(name) LIKE LOWER('%fragment%')

    // ── Null checks ──
    List<Product> findByDescriptionIsNull();
    // → WHERE description IS NULL

    List<Product> findByDescriptionIsNotNull();
    // → WHERE description IS NOT NULL

    // ── Boolean ──
    List<Product> findByActiveTrue();
    // → WHERE active = true

    // ── Collections ──
    List<Product> findByCategoryIn(List<Category> categories);
    // → WHERE category IN (?, ?, ?)

    // ── Ordering ──
    List<Product> findByActiveTrueOrderByPriceDesc();
    // → WHERE active = true ORDER BY price DESC

    // ── Limiting ──
    List<Product> findTop5ByOrderByPriceDesc();
    // → ... ORDER BY price DESC LIMIT 5

    Optional<Product> findFirstByNameContaining(String name);
    // → ... LIMIT 1

    // ── Counting & Existence ──
    long countByCategory(Category category);
    // → SELECT COUNT(*) WHERE category = ?

    boolean existsByName(String name);
    // → SELECT EXISTS(... WHERE name = ?)

    // ── Deleting ──
    void deleteByName(String name);
    // → DELETE FROM products WHERE name = ?
}
```

### 4.2 How it works internally

Spring Data at startup:
1. Scans all interfaces extending `Repository`
2. Reads each method name
3. Parses keywords (`findBy`, `And`, `GreaterThan`, `OrderBy`, etc.)
4. Generates a JPQL query
5. Creates a proxy implementation class

> **If you misspell a keyword**, the app will **fail at startup** with an error like: `No property 'nme' found for type 'Product'` — this is a safety feature.

---

## 5. Custom Queries (JPQL & Native)

When derived queries become too complex or inefficient, write your own.

### 5.1 JPQL (Java Persistence Query Language)

JPQL operates on **entities** (Java classes), not tables. It's database-agnostic — works on any DB.

```java
// Named parameters (recommended — clear and safe)
@Query("SELECT p FROM Product p WHERE p.price > :minPrice AND p.active = true")
List<Product> findExpensiveActive(@Param("minPrice") BigDecimal minPrice);

// Positional parameters
@Query("SELECT p FROM Product p WHERE p.category = ?1 AND p.price < ?2")
List<Product> findByCategoryAndMaxPrice(Category category, BigDecimal maxPrice);

// Return specific fields as a DTO (Projection)
@Query("SELECT new com.example.dto.ProductSummary(p.id, p.name, p.price) " +
       "FROM Product p WHERE p.category = :cat")
List<ProductSummary> findSummariesByCategory(@Param("cat") Category category);

// Aggregate queries
@Query("SELECT p.category, COUNT(p), AVG(p.price) FROM Product p GROUP BY p.category")
List<Object[]> getCategoryStats();
```

### 5.2 Native SQL

Uses raw SQL — database-specific. Use when you need DB features that JPQL doesn't support.

```java
@Query(value = "SELECT * FROM products WHERE price > ?1 ORDER BY price DESC LIMIT 10",
       nativeQuery = true)
List<Product> findTopExpensive(BigDecimal minPrice);

// With pagination
@Query(value = "SELECT * FROM products WHERE category = :cat",
       countQuery = "SELECT COUNT(*) FROM products WHERE category = :cat",
       nativeQuery = true)
Page<Product> findByCategory(@Param("cat") String category, Pageable pageable);
```

### 5.3 Modifying Queries

For UPDATE and DELETE operations — requires `@Modifying` and `@Transactional`:

```java
@Modifying
@Transactional
@Query("UPDATE Product p SET p.price = p.price * :multiplier WHERE p.category = :cat")
int updatePriceByCategory(@Param("multiplier") double multiplier,
                          @Param("cat") Category cat);
// Returns number of rows affected

@Modifying
@Transactional
@Query("DELETE FROM Product p WHERE p.active = false AND p.updatedAt < :date")
int deleteInactiveOlderThan(@Param("date") LocalDateTime date);
```

> **Important:** `@Modifying` queries bypass the persistence context. If you've loaded entities before the update, they will be **stale** (out of sync). Add `@Modifying(clearAutomatically = true)` to clear the cache after execution.

---

## 6. Entity Relationships (Deep Dive)

### 6.1 Relationship Types

| Annotation | Relationship | Example |
|-----------|-------------|---------|
| `@OneToOne` | 1:1 | User ↔ UserProfile |
| `@OneToMany` | 1:N | User → Orders |
| `@ManyToOne` | N:1 | Order → User |
| `@ManyToMany` | N:N | Student ↔ Course |

### 6.2 Fetch Types — LAZY vs EAGER

| | LAZY | EAGER |
|--|------|-------|
| **When loaded** | Only when you **access** the collection/association | **Immediately** with the parent query |
| **Default for** | `@OneToMany`, `@ManyToMany` | `@ManyToOne`, `@OneToOne` |
| **Pros** | Saves memory, faster parent loading | Simple — no LazyInitializationException |
| **Cons** | Can cause LazyInitializationException outside transaction | Loads data you might not need → wasted memory/queries |
| **Recommendation** | ✅ Use for collections | ⚠️ Consider LAZY even for `@ManyToOne` |

```java
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    // ── One-to-One ──
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")  // FK column in 'users' table
    private UserProfile profile;

    // ── One-to-Many (parent side) ──
    @OneToMany(mappedBy = "user",      // 'user' field in Order entity owns the FK
               cascade = CascadeType.ALL,
               orphanRemoval = true,    // Delete orders if removed from list
               fetch = FetchType.LAZY)  // Default for @OneToMany
    private List<Order> orders = new ArrayList<>();
    
    // Helper methods for bidirectional sync
    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);    // Set BOTH sides!
    }
    
    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
    }
}

@Entity
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal total;
    
    // ── Many-to-One (child/owning side — has the FK column) ──
    @ManyToOne(fetch = FetchType.LAZY)  // Override default EAGER!
    @JoinColumn(name = "user_id")       // FK column in 'orders' table
    private User user;
}
```

> **`mappedBy`** = "I don't own the relationship. The other entity has the foreign key." Always set on the **parent** side (`@OneToMany`). The **child** side (`@ManyToOne`) owns the relationship because it has the FK column.

### 6.3 Cascade Types

| Cascade | What it does |
|---------|-------------|
| `PERSIST` | When you save Parent → also save new Children |
| `MERGE` | When you update Parent → also update Children |
| `REMOVE` | When you delete Parent → also delete Children |
| `ALL` | All of the above + DETACH + REFRESH |

```java
// With CascadeType.ALL:
User user = new User("Rahul");
user.addOrder(new Order(500));
user.addOrder(new Order(1200));

userRepository.save(user);  // ONE save() → saves User + both Orders!
// Without cascade, you'd need: save user, then save each order separately

userRepository.delete(user);  // Deletes User + both Orders
```

### 6.4 The N+1 Problem (Critical!)

The **most common performance issue** in JPA/Hibernate. Understanding and fixing it is a common interview question.

```
PROBLEM: You want to display a list of Users with their Orders.

Query 1: SELECT * FROM users                          → Returns 100 users
Query 2: SELECT * FROM orders WHERE user_id = 1       → Orders for user 1
Query 3: SELECT * FROM orders WHERE user_id = 2       → Orders for user 2
...
Query 101: SELECT * FROM orders WHERE user_id = 100   → Orders for user 100

TOTAL: 1 + 100 = 101 queries! (N+1 where N = number of users)
```

> **Why does this happen?** Because `@OneToMany` is LAZY by default. When you load users, orders are NOT loaded. When your code accesses `user.getOrders()` for each user (e.g., in a loop or during JSON serialization), Hibernate fires a SEPARATE query for each user.

**Solutions:**

```java
// ═══ Solution 1: JOIN FETCH in JPQL (most common) ═══
@Query("SELECT u FROM User u JOIN FETCH u.orders")
List<User> findAllWithOrders();
// Generates: SELECT u.*, o.* FROM users u JOIN orders o ON u.id = o.user_id
// ONE query gets everything!

// ═══ Solution 2: @EntityGraph ═══
@EntityGraph(attributePaths = {"orders"})
List<User> findAll();
// Same result — Spring adds the JOIN for you

// ═══ Solution 3: @BatchSize (Hibernate-specific) ═══
@OneToMany(mappedBy = "user")
@BatchSize(size = 20)  // Load orders for 20 users at a time
private List<Order> orders;
// Instead of 100 queries, fires 5 queries (100/20)
// SELECT * FROM orders WHERE user_id IN (1,2,3,...,20)
// SELECT * FROM orders WHERE user_id IN (21,22,...,40)
// etc.

// ═══ Solution 4: Global batch fetching ═══
// application.properties
// spring.jpa.properties.hibernate.default_batch_fetch_size=20
```

| Solution | Pros | Cons |
|----------|------|------|
| **JOIN FETCH** | One query, most control | Can cause Cartesian product with multiple collections |
| **@EntityGraph** | Clean, declarative | Less flexible than JPQL for complex queries |
| **@BatchSize** | Works globally, no query changes | Still multiple queries (just fewer) |

---

## 7. Hibernate Internals

### 7.1 Entity Lifecycle / States

Every entity goes through **4 states** in Hibernate. Understanding this is ESSENTIAL for debugging unexpected behavior.

```
┌─────────────────────────────────────────────────────────────┐
│                     Entity Lifecycle                         │
│                                                             │
│  ┌──────────┐     save()/persist()     ┌──────────────┐   │
│  │ TRANSIENT │ ──────────────────────→ │   MANAGED     │   │
│  │ (new obj) │                         │ (in context)  │   │
│  └──────────┘                          └──────────────┘   │
│       ↑                                    │       ↑       │
│       │                              evict()│     merge() │
│       │                              clear()│       │      │
│       │                                    ↓       │      │
│       │                               ┌──────────┐│      │
│       │                               │ DETACHED  ││      │
│       │                               │(out of ctx)│      │
│       │                               └──────────┘│      │
│       │                                            │      │
│       │         remove()                           │      │
│       │    ┌──────────┐                            │      │
│       └─── │ REMOVED   │ ←─────────────────────────┘      │
│            │(marked del)│                                  │
│            └──────────┘                                    │
└─────────────────────────────────────────────────────────────┘
```

| State | What it means | Tracked by Hibernate? | Has DB row? |
|-------|--------------|----------------------|-------------|
| **Transient** | `new Product()` — just a Java object, Hibernate doesn't know about it | ❌ No | ❌ No |
| **Managed** | After `save()`/`persist()` or after loading from DB — Hibernate tracks ALL changes | ✅ Yes | ✅ Yes |
| **Detached** | After transaction ends, session closes, or `evict()` — exists in DB but Hibernate stopped tracking | ❌ No | ✅ Yes |
| **Removed** | After `delete()`/`remove()` — marked for deletion, will be removed on flush | ✅ Yes | ✅ Yes (until flush) |

### 7.2 Persistence Context (First-Level Cache)

The **Persistence Context** (managed by `EntityManager` / Hibernate Session) is a cache that stores all **Managed** entities during a transaction.

> **Analogy:** Think of a shopping cart. When you add items (load entities), they go into your cart (persistence context). You can modify items in your cart freely. When you "checkout" (flush/commit), ALL your changes are sent to the database at once.

```java
@Transactional
public void demonstratePersistenceContext() {
    
    // 1. Load entity → goes into Persistence Context (Managed state)
    Product p = productRepository.findById(1L).orElseThrow();
    
    // 2. Dirty checking — Hibernate detects changes automatically!
    p.setPrice(new BigDecimal("999"));
    // You DON'T need to call save()! ← This surprises many developers
    // Hibernate compares current state with original snapshot
    // and auto-generates UPDATE on flush/commit
    
    // 3. Identity guarantee — same entity loaded twice = same object
    Product p2 = productRepository.findById(1L).orElseThrow();
    System.out.println(p == p2);  // true! Same object from cache, no second query
    
    // 4. At end of @Transactional method:
    //    - Hibernate flushes (sends all SQL to DB)
    //    - Transaction commits
    //    - Persistence context is cleared
}
```

**Key behaviors:**

| Behavior | What happens |
|----------|-------------|
| **Dirty checking** | Hibernate auto-detects changes to Managed entities — no explicit `save()` needed |
| **Identity guarantee** | `findById(1L)` called twice returns the SAME Java object (from cache) |
| **Write-behind** | SQL is not sent immediately — it's batched and sent on `flush()` or `commit()` |
| **First-level cache** | Persistence context IS the first-level cache — it's per-transaction, not global |

### 7.3 Hibernate Caching Levels

```
Request → First-Level Cache (Persistence Context) → Per-transaction, automatic
              ↓ (cache miss)
         Second-Level Cache (SessionFactory level) → Shared across transactions
              ↓ (cache miss)
         Query Cache → Caches query results, not entities
              ↓ (cache miss)
         Database
```

| Cache Level | Scope | Enabled by default | Configuration |
|------------|-------|-------------------|---------------|
| **First-Level** | Per EntityManager/Session (per transaction) | ✅ Always on | Cannot disable |
| **Second-Level** | Per SessionFactory (shared across all sessions) | ❌ Off | Needs EhCache/Redis + `@Cacheable` on entities |
| **Query Cache** | Caches the IDs returned by specific queries | ❌ Off | `hibernate.cache.use_query_cache=true` |

```java
// Enable Second-Level Cache on an entity
@Entity
@Cacheable                                    // JPA standard
@org.hibernate.annotations.Cache(
    usage = CacheConcurrencyStrategy.READ_WRITE  // Hibernate-specific
)
public class Product {
    // Frequently read, rarely updated entities benefit most
}
```

### 7.4 `save()` vs `persist()` vs `merge()`

| Method | Returns | When to use |
|--------|---------|-------------|
| `persist()` | void | New entity → make it Managed. Doesn't guarantee immediate INSERT |
| `save()` (Spring Data) | The saved entity | Spring wrapper — calls `persist()` or `merge()` depending on `isNew()` |
| `merge()` | The managed copy | Detached entity → re-attach and update. Returns a NEW managed copy |
| `saveAndFlush()` | The saved entity | `save()` + immediately send SQL to DB (don't wait for transaction end) |

```java
// merge() returns a DIFFERENT object!
Product detached = entityManager.find(Product.class, 1L);
entityManager.detach(detached);

detached.setPrice(new BigDecimal("500"));
Product managed = entityManager.merge(detached);

System.out.println(detached == managed);  // false! Different objects
// 'managed' is tracked by Hibernate, 'detached' is not
```

---

## 8. Handling Large Data

This is a critical section for production applications and a common interview topic.

### 8.1 Pagination (Don't `findAll()`!)

**Never** load all records into memory. For 1 million rows, `findAll()` loads all 1M objects into the JVM heap — guaranteed OOM or extreme slowness.

```java
// ❌ NEVER DO THIS IN PRODUCTION
List<Product> allProducts = productRepository.findAll();  // 1M objects in memory!

// ✅ Always paginate
@GetMapping("/products")
public Page<Product> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
    
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return productRepository.findAll(pageable);
    // SQL: SELECT * FROM products ORDER BY created_at DESC LIMIT 20 OFFSET 0
}
```

**Response format of `Page<T>`:**

```json
{
  "content": [ /* 20 products */ ],
  "totalElements": 50000,
  "totalPages": 2500,
  "number": 0,
  "size": 20,
  "first": true,
  "last": false,
  "hasNext": true
}
```

#### `Page` vs `Slice` vs `List`

| Return Type | COUNT query? | Knows total? | Use when |
|-------------|------------|-------------|----------|
| `Page<T>` | ✅ Yes (extra query) | ✅ Yes — totalElements, totalPages | Traditional pagination with page numbers |
| `Slice<T>` | ❌ No | ❌ No — only knows if `hasNext` | Infinite scroll / "Load More" button |
| `List<T>` | ❌ No | ❌ No | You just want the data, no metadata |

```java
// Slice — better for infinite scroll (no expensive COUNT query)
Slice<Product> findByCategory(Category cat, Pageable pageable);

// Fetches N+1 records to determine if there's a next page
// SELECT * FROM products WHERE category = ? LIMIT 21  (if page size is 20)
// If 21 rows returned → hasNext = true, return first 20
```

> **Performance tip:** `Page` executes TWO queries — one for data and one for `SELECT COUNT(*)`. For tables with millions of rows, the COUNT query can be very slow. Use `Slice` when you don't need total count.

### 8.2 Cursor-Based Pagination (for very large datasets)

Offset-based pagination (`LIMIT 20 OFFSET 999980`) gets **slower as pages increase** because the DB must scan and skip all the offset rows.

```java
// ❌ Offset pagination is slow for deep pages
// Page 50,000: SELECT * FROM products ORDER BY id LIMIT 20 OFFSET 999980
// DB scans 999,980 rows just to discard them!

// ✅ Cursor-based (keyset) pagination — constant performance
@Query("SELECT p FROM Product p WHERE p.id > :lastId ORDER BY p.id ASC")
List<Product> findNextPage(@Param("lastId") Long lastId, Pageable pageable);

// First page: GET /products?size=20           → returns products with id 1-20
// Next page:  GET /products?size=20&after=20  → WHERE id > 20 LIMIT 20
// This is O(1) — no matter how deep, it's equally fast
```

### 8.3 Batch Processing (Bulk INSERT / UPDATE)

When you need to insert or update **thousands/millions of records**, doing them one-by-one is catastrophically slow.

```java
// ❌ SLOW: 10,000 separate INSERT statements
for (int i = 0; i < 10000; i++) {
    productRepository.save(new Product("Product-" + i, randomPrice()));
}
// Each save() → individual INSERT → individual commit → 10,000 round trips!
```

**Enable batch inserts (application.properties):**

```properties
# Enable Hibernate batch inserts
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# IMPORTANT: Use SEQUENCE strategy (not IDENTITY) for batching to work!
# IDENTITY forces immediate INSERT to get the ID — prevents batching
```

**Batch insert with periodic flush & clear:**

```java
@Service
public class BulkImportService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    public void importProducts(List<ProductDto> dtos) {
        int batchSize = 50;
        
        for (int i = 0; i < dtos.size(); i++) {
            Product product = mapToEntity(dtos.get(i));
            entityManager.persist(product);
            
            // Every 50 items:
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();   // Send batch INSERT to DB (50 in one go)
                entityManager.clear();   // Clear persistence context (free memory!)
                
                // Without clear(), 100,000 entities would stay in memory
                // causing OutOfMemoryError
            }
        }
        // Final flush for remaining items
        entityManager.flush();
        entityManager.clear();
    }
}
```

> **Why `flush()` + `clear()`?** Without clearing, Hibernate holds ALL entities in the persistence context (first-level cache). For 100K records, that's 100K objects in memory. `flush()` sends SQL to DB. `clear()` frees all those objects from memory.

**Using `saveAll()` for moderate batches:**

```java
// For smaller batches (hundreds, not millions)
// saveAll() is better than individual save() calls
List<Product> products = dtos.stream()
    .map(this::mapToEntity)
    .collect(Collectors.toList());

productRepository.saveAll(products);  // Uses batch insert if configured
```

### 8.4 Streaming Results (Process without loading all)

For processing large query results without loading everything into memory:

```java
// ── Stream approach — processes one row at a time ──
@Query("SELECT p FROM Product p WHERE p.active = true")
@QueryHints(@QueryHint(name = HINT_FETCH_SIZE, value = "50" ))
Stream<Product> streamAllActive();

// Usage: MUST be in @Transactional (stream needs open connection)
@Transactional(readOnly = true)
public void processAllProducts() {
    try (Stream<Product> stream = productRepository.streamAllActive()) {
        stream.forEach(product -> {
            // Process one product at a time — only 50 in memory at once
            processProduct(product);
        });
    }
    // Stream is auto-closed by try-with-resources
}
```

### 8.5 Projections (Load only what you need)

If you have 20 columns but only need 3, **don't load all 20** — use projections.

```java
// ═══ Interface-based Projection (recommended) ═══
public interface ProductSummary {
    Long getId();
    String getName();
    BigDecimal getPrice();
    // Only these 3 columns are queried — not the entire entity!
}

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<ProductSummary> findByCategory(Category category);
    // SQL: SELECT id, name, price FROM products WHERE category = ?
    // Instead of: SELECT * FROM products WHERE category = ?
}


// ═══ DTO-based Projection (using @Query) ═══
public record ProductDto(Long id, String name, BigDecimal price) {}

@Query("SELECT new com.example.dto.ProductDto(p.id, p.name, p.price) " +
       "FROM Product p WHERE p.category = :cat")
List<ProductDto> findDtosByCategory(@Param("cat") Category category);


// ═══ Dynamic Projection ═══
<T> List<T> findByCategory(Category category, Class<T> type);

// Use with different projections:
List<ProductSummary> summaries = repo.findByCategory(cat, ProductSummary.class);
List<ProductDto> dtos = repo.findByCategory(cat, ProductDto.class);
List<Product> full = repo.findByCategory(cat, Product.class);
```

> **Why projections matter for large data:** Loading 1M rows × 20 columns = 20M field values in memory. Loading 1M rows × 3 columns = 3M field values. That's 85% less memory.

### 8.6 Read-Only Optimization

For queries that only READ data (no updates), tell Hibernate to skip dirty checking:

```java
@Transactional(readOnly = true)
public List<Product> getActiveProducts() {
    return productRepository.findByActiveTrue();
    // Hibernate skips:
    // 1. Dirty checking (no snapshot comparison)
    // 2. No flush on commit (nothing to write)
    // 3. Some JDBC drivers optimize read-only connections
    // Result: ~20-30% faster for read-heavy operations
}
```

### 8.7 Database Indexing (Critical for Query Performance)

**Indexes** are data structures that speed up SELECT queries at the cost of slower INSERTs/UPDATEs and extra storage.

```java
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_name", columnList = "name"),
    @Index(name = "idx_product_category", columnList = "category"),
    @Index(name = "idx_product_price_category", columnList = "price, category")
    // Composite index — order matters! Useful for queries filtering by both
})
public class Product { ... }
```

| Rule | Explanation |
|------|-------------|
| Index columns used in `WHERE`, `ORDER BY`, `JOIN` | These are the columns the DB searches through |
| Don't index everything | Each index slows down INSERT/UPDATE and uses disk space |
| Composite index order matters | `(price, category)` helps `WHERE price = ? AND category = ?` but NOT `WHERE category = ?` alone |
| Check slow queries | Use `EXPLAIN ANALYZE` (PostgreSQL) or `EXPLAIN` (MySQL) to see if queries use indexes |

### 8.8 Connection Pooling (HikariCP)

Spring Boot uses **HikariCP** by default — the fastest Java connection pool. Connection pooling **reuses** DB connections instead of creating/closing them for every query.

> **Without pooling:** Each query = open connection (50ms) → execute query (10ms) → close connection (20ms). Total: 80ms per query.
> **With pooling:** Borrow connection from pool (0.1ms) → execute query (10ms) → return to pool (0.1ms). Total: 10.2ms per query.

```properties
# application.properties — tune for your load
spring.datasource.hikari.minimum-idle=5          # Keep 5 connections ready at all times
spring.datasource.hikari.maximum-pool-size=20    # Max 20 concurrent connections
spring.datasource.hikari.idle-timeout=30000      # Close idle connections after 30s
spring.datasource.hikari.connection-timeout=20000 # Wait max 20s for a connection
spring.datasource.hikari.max-lifetime=1800000    # Recycle connections every 30 min
```

| Setting | Too LOW | Too HIGH |
|---------|---------|----------|
| **maximum-pool-size** | Connection wait timeouts under load | Wastes DB resources, DB connection limit hit |
| **minimum-idle** | Cold start delay when load spikes | Wasted idle connections |

> **Rule of thumb:** `pool size = CPU cores * 2 + disk spindles`. For most apps, 10-20 is sufficient. More connections ≠ more performance — too many cause DB contention.

### 8.9 Summary: Handling Large Data Decision Table

| Scenario | Solution |
|----------|----------|
| Displaying lists to users | **Pagination** (`Page<T>` or `Slice<T>`) |
| Infinite scroll / mobile apps | **Slice** (no COUNT query) or **Cursor-based** pagination |
| Very deep pages (page 10,000+) | **Cursor-based pagination** (WHERE id > lastId) |
| Inserting 10K+ records | **Batch inserts** (flush + clear every N rows) |
| Processing all rows (export/report) | **Stream** (`Stream<T>` with `@Transactional(readOnly = true)`) |
| Need only a few columns | **Projections** (interface or DTO) |
| Slow SELECT queries | **Database indexes** + `EXPLAIN ANALYZE` |
| Read-only operations | `@Transactional(readOnly = true)` |
| Connection exhaustion | **HikariCP tuning** (pool size, timeouts) |

---

## 9. Validation

Uses Bean Validation API (`jakarta.validation`) with `spring-boot-starter-validation`.

```java
public class ProductDto {
    
    @NotBlank(message = "Name is required")        // Not null + not empty + not whitespace
    @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
    private String name;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")    // > 0
    @DecimalMax(value = "999999.99", message = "Price too high")
    private BigDecimal price;
    
    @Email(message = "Must be a valid email")
    private String contactEmail;
    
    @Pattern(regexp = "^[A-Z]{2}-\\d{4}$", message = "SKU must be like 'AB-1234'")
    private String sku;
    
    @Past(message = "Manufacture date must be in the past")
    private LocalDate manufacturedDate;
}

// Controller — @Valid triggers validation
@PostMapping
public ResponseEntity<Product> create(@Valid @RequestBody ProductDto dto) {
    // If validation fails, Spring throws MethodArgumentNotValidException
    // which your @ControllerAdvice can catch and format nicely
    return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
}
```

| Annotation | Validates |
|-----------|----------|
| `@NotNull` | Not null (but can be empty string) |
| `@NotEmpty` | Not null + not empty (size > 0) |
| `@NotBlank` | Not null + not empty + not just whitespace |
| `@Size(min, max)` | String length or collection size |
| `@Min(value)` / `@Max(value)` | Numeric min/max (integers) |
| `@Positive` / `@Negative` | > 0 / < 0 |
| `@Email` | Valid email format |
| `@Pattern(regexp)` | Matches regex |
| `@Past` / `@Future` | Date before/after now |

---

## 10. Exception Handling (@ControllerAdvice)

Global exception handling — **one place** to handle errors for ALL controllers.

```java
@RestControllerAdvice  // @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

    // Handle specific custom exceptions
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ProductNotFoundException ex) {
        return new ErrorResponse(404, ex.getMessage(), Instant.now());
    }

    // Handle validation errors (from @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        
        return Map.of(
            "status", 400,
            "message", "Validation failed",
            "errors", fieldErrors,
            "timestamp", Instant.now()
        );
    }

    // Catch-all for unexpected errors
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return new ErrorResponse(500, "Something went wrong", Instant.now());
    }
}

// Error response DTO
public record ErrorResponse(int status, String message, Instant timestamp) {}
```

---

## 11. Complex Technical Scenarios

### Scenario 1: What is the N+1 problem and how to fix it?

**Answer:** Loading N parent entities triggers N additional queries (one per parent) to load children. Total = N+1 queries.

**Fixes (in order of preference):**
1. `JOIN FETCH` in JPQL — loads everything in one query
2. `@EntityGraph` — declarative on repository method
3. `@BatchSize` — reduces N queries to N/batchSize queries
4. `Hibernate.initialize()` — force load within open session

### Scenario 2: `JpaRepository` vs `CrudRepository` vs `PagingAndSortingRepository`?

| | CrudRepository | PagingAndSortingRepository | JpaRepository |
|--|---------------|---------------------------|---------------|
| **CRUD** | ✅ | ✅ | ✅ |
| **Pagination** | ❌ | ✅ | ✅ |
| **Sorting** | ❌ | ✅ | ✅ |
| **Batch deletes** | ❌ | ❌ | ✅ `deleteAllInBatch()` |
| **Flush** | ❌ | ❌ | ✅ `flush()`, `saveAndFlush()` |
| **Query by Example** | ❌ | ❌ | ✅ `findAll(Example)` |

> **Use `JpaRepository`** in most cases — it gives you everything.

### Scenario 3: How does `@Transactional` work internally?

**Answer:** Spring creates a **proxy** around the bean. The proxy:

1. Intercepts the method call
2. Gets a DB connection from the pool
3. Begins a transaction (`BEGIN`)
4. Calls your actual method
5. If method returns normally → `COMMIT`
6. If RuntimeException is thrown → `ROLLBACK`
7. If checked exception is thrown → `COMMIT` (← surprising default!)
8. Returns the DB connection to the pool

```java
// Override default: rollback on ALL exceptions
@Transactional(rollbackFor = Exception.class)

// Read-only optimization (no dirty checking, no flush)
@Transactional(readOnly = true)

// Custom isolation level
@Transactional(isolation = Isolation.SERIALIZABLE)

// Custom propagation (what if we're already in a transaction?)
@Transactional(propagation = Propagation.REQUIRES_NEW)
// REQUIRES_NEW suspends the current transaction and creates a new one
```

### Scenario 4: PUT vs PATCH?

| | PUT | PATCH |
|--|-----|-------|
| **Replaces** | Entire resource | Only specified fields |
| **Missing fields** | Set to null/default | Left unchanged |
| **Idempotent** | ✅ Yes | ❌ Not necessarily |

### Scenario 5: Optimistic vs Pessimistic Locking?

| | Optimistic | Pessimistic |
|--|-----------|-------------|
| **Mechanism** | `@Version` field — check version on UPDATE | `SELECT ... FOR UPDATE` — lock rows in DB |
| **When conflict detected** | At commit time → `OptimisticLockException` | At lock time → other transactions wait |
| **Best for** | High read, low write (most web apps) | High write contention (banking, inventory) |
| **Throughput** | Higher (no locks held) | Lower (locks block other transactions) |

```java
// Optimistic Locking
@Entity
public class Product {
    @Version          // Hibernate auto-increments this on every UPDATE
    private Integer version;
}
// Under the hood:
// UPDATE products SET name=?, price=?, version=3 WHERE id=1 AND version=2
// If version changed (someone else updated it), throws OptimisticLockException

// Pessimistic Locking
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT p FROM Product p WHERE p.id = :id")
Product findByIdForUpdate(@Param("id") Long id);
// Under the hood:
// SELECT * FROM products WHERE id = 1 FOR UPDATE
// Other transactions trying to read/write this row will WAIT
```

### Scenario 6: LazyInitializationException — what is it?

**Answer:** You try to access a LAZY collection AFTER the Hibernate session is closed (outside `@Transactional`).

```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    User user = userRepository.findById(id).orElseThrow();
    user.getOrders().size();  // 💥 LazyInitializationException!
    // Session is closed after findById returns
    // Accessing LAZY collection needs an open session
    return user;
}

// Fix 1: Use JOIN FETCH
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
Optional<User> findByIdWithOrders(@Param("id") Long id);

// Fix 2: @Transactional (keeps session open)
@Transactional(readOnly = true)
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) { ... }

// Fix 3: @EntityGraph
@EntityGraph(attributePaths = {"orders"})
Optional<User> findById(Long id);
```

---

## 12. Key Topics & Explanations

### Topic 1: Difference between Hibernate `save()` and `persist()`?

| | `persist()` (JPA standard) | `save()` (Hibernate/Spring) |
|--|---------------------------|----------------------------|
| **Returns** | `void` | The saved entity (with generated ID) |
| **ID generation** | May delay INSERT until flush | May execute INSERT immediately for IDENTITY strategy |
| **Detached entity** | Throws exception | Performs merge (re-attaches) |

### Topic 2: What is `@MappedSuperclass`?

Share common fields across entities WITHOUT creating a separate table:

```java
@MappedSuperclass
public abstract class BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

@Entity
public class Product extends BaseEntity {
    private String name;  // Inherits id, createdAt, updatedAt
}

@Entity
public class User extends BaseEntity {
    private String email;  // Also inherits id, createdAt, updatedAt
}
// No 'base_entity' table is created — fields go into 'product' and 'user' tables
```

### Topic 3: JPQL vs Native SQL — when to use which?

| | JPQL | Native SQL |
|--|------|-----------|
| **Operates on** | Entities (Java classes) | Tables (database) |
| **Portable** | ✅ Works on any DB | ❌ Database-specific |
| **Use when** | Standard CRUD, joins, filters | DB-specific features (window functions, CTEs, hints) |

### Topic 4: What is `spring.jpa.open-in-view` (OSIV)?

**Answer:** Open Session In View — keeps the Hibernate session open during the entire HTTP request (even in the Controller/View layer). Enabled by default in Spring Boot.

```
spring.jpa.open-in-view=true   (default — session open entire request)
spring.jpa.open-in-view=false  (recommended for production)
```

- **`true`**: Prevents `LazyInitializationException` in controllers — but can cause performance issues (DB connections held too long, unexpected queries in views).
- **`false`**: Forces you to load all needed data in the `@Service` layer — better architecture but requires explicit fetching (JOIN FETCH, @EntityGraph).

> **Best practice:** Set to `false` and properly load all required data in your service layer using JOIN FETCH or DTOs.
