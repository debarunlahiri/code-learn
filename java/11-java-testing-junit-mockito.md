# Java Testing Guide (JUnit, Mockito, Spring Boot)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Unit Testing, Integration Testing, Mocking, TDD, and Best Practices.

---

## Table of Contents

1. [Testing Fundamentals](#testing-fundamentals)
2. [JUnit 5 Overview](#junit-5-overview)
3. [Mockito Framework](#mockito-framework)
4. [Spring Boot Testing](#spring-boot-testing)
5. [TDD (Test Driven Development)](#tdd-test-driven-development)
6. [Testing Best Practices](#testing-best-practices)
7. [Complex Technical Scenarios](#complex-technical-scenarios)
8. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Testing Fundamentals

### 1.1 Why Test?

Testing is how you **prove** your code works correctly. Without tests, every change is a gamble — you might fix one bug and accidentally create five more.

> **Analogy:** Writing code without tests is like building a bridge without stress-testing it. It might look fine, but you don't know if it'll hold weight until it's too late.

**Benefits of testing:**
- **Confidence** — Deploy changes without fear of breaking existing features
- **Documentation** — Tests show HOW your code is supposed to behave
- **Refactoring safety** — Change internal code knowing tests will catch regressions
- **Bug prevention** — Catch issues early, before they reach production

### 1.2 Types of Testing

| Type | What it Tests | Speed | Example |
|------|--------------|-------|---------|
| **Unit Testing** | Individual methods/classes in **isolation** | ⚡ Fastest (ms) | Test that `calculateDiscount(100, 10)` returns `90` |
| **Integration Testing** | How modules work **together** | 🔄 Medium (seconds) | Test that Service → Repository → Database flow works correctly |
| **Functional Testing** | System against **functional requirements** (black box) | 🐢 Slower | Test that "Add to Cart" feature works as per specification |
| **E2E (End-to-End)** | Complete user journey from **UI to database** | 🐌 Slowest (minutes) | Simulate: User logs in → searches product → adds to cart → checks out |

### 1.3 Testing Pyramid

```
        ╱╲
       ╱  ╲          🐌 E2E Tests
      ╱    ╲         (Fewest — expensive, slow, brittle)
     ╱──────╲
    ╱        ╲        🔄 Integration Tests
   ╱          ╲       (Some — test module interactions)
  ╱────────────╲
 ╱              ╲     ⚡ Unit Tests
╱________________╲    (Most — fast, cheap, reliable)
```

> **Rule of thumb:** Write MANY unit tests, SOME integration tests, and FEW end-to-end tests. The base of the pyramid should be the largest.

---

## 2. JUnit 5 Overview

JUnit 5 is the **standard framework** for writing and running tests in Java. It's the most widely used testing framework in the Java ecosystem.

### 2.1 Key Annotations

| Annotation | When it Runs | Purpose |
|-----------|-------------|---------|
| `@Test` | On annotated method | Marks a method as a test case |
| `@BeforeEach` | Before **each** `@Test` method | Setup — create fresh objects for each test |
| `@AfterEach` | After **each** `@Test` method | Teardown — clean up after each test |
| `@BeforeAll` | Once before **all** tests in a class | Static setup — expensive one-time operations (DB connection) |
| `@AfterAll` | Once after **all** tests | Static teardown — close connections |
| `@DisplayName("...")` | — | Human-readable name shown in test reports |
| `@Disabled` | — | Skips the test (with optional reason) |
| `@RepeatedTest(5)` | — | Run the same test N times |
| `@Timeout(5)` | — | Fail if test takes longer than N seconds |

### 2.2 Lifecycle Example

```java
class UserServiceTest {

    @BeforeAll
    static void setupOnce() {
        System.out.println("1️⃣ Runs ONCE before all tests (static method)");
        // Example: Start embedded database
    }

    @BeforeEach
    void setupEach() {
        System.out.println("2️⃣ Runs before EACH test");
        // Example: Create fresh UserService instance
    }

    @Test
    @DisplayName("Should create user successfully")
    void testCreateUser() {
        System.out.println("3️⃣ Test runs here");
    }

    @AfterEach
    void teardownEach() {
        System.out.println("4️⃣ Runs after EACH test");
        // Example: Clear test data
    }

    @AfterAll
    static void teardownOnce() {
        System.out.println("5️⃣ Runs ONCE after all tests");
        // Example: Stop embedded database
    }
}
```

### 2.3 Assertions (The Heart of Testing)

Assertions are how you **verify** that actual results match expected results. If an assertion fails, the test fails.

```java
import static org.junit.jupiter.api.Assertions.*;

@Test
@DisplayName("Calculator should perform basic operations correctly")
void testCalculator() {
    Calculator calc = new Calculator();
    
    // ══════════════════════════════════════════════
    // Equality assertions
    // ══════════════════════════════════════════════
    assertEquals(4, calc.add(2, 2), "2 + 2 should equal 4");
    assertEquals(2, calc.divide(10, 5));
    assertNotEquals(0, calc.add(1, 1));
    
    // ══════════════════════════════════════════════
    // Boolean assertions
    // ══════════════════════════════════════════════
    assertTrue(calc.isPositive(5), "5 should be positive");
    assertFalse(calc.isPositive(-1));
    
    // ══════════════════════════════════════════════
    // Null assertions
    // ══════════════════════════════════════════════
    assertNotNull(calc);
    assertNull(calc.getLastError());
    
    // ══════════════════════════════════════════════
    // Exception assertions — Test that code THROWS an exception
    // ══════════════════════════════════════════════
    ArithmeticException ex = assertThrows(ArithmeticException.class, () -> {
        calc.divide(1, 0);  // Should throw!
    });
    assertEquals("/ by zero", ex.getMessage());  // Check exception message
    
    // ══════════════════════════════════════════════
    // Group assertions — Test multiple things, report ALL failures
    // ══════════════════════════════════════════════
    assertAll("calculator operations",
        () -> assertEquals(4, calc.add(2, 2)),
        () -> assertEquals(0, calc.subtract(5, 5)),
        () -> assertEquals(6, calc.multiply(2, 3))
    );
    
    // ══════════════════════════════════════════════
    // Collection/Array assertions
    // ══════════════════════════════════════════════
    assertIterableEquals(List.of(1, 4, 9), calc.squares(List.of(1, 2, 3)));
    assertArrayEquals(new int[]{1, 2, 3}, new int[]{1, 2, 3});
}
```

> **Pro tip:** Always include a **message** in assertions (the third argument). When a test fails at 3 AM in CI, `"User should be active after registration"` is far more helpful than just `"expected true but was false"`.

---

## 3. Mockito Framework

### 3.1 What is Mocking and Why Do We Need It?

In real applications, classes have **dependencies** — `UserService` depends on `UserRepository` (which talks to the database). When testing `UserService`, you don't want to:
- Set up a real database
- Wait for slow DB queries
- Deal with test data pollution

**Mocking** creates **fake versions** of dependencies that simulate real behavior without the overhead.

> **Analogy:** When crash-testing a car, you use a **dummy** (mock) instead of a real person. The dummy simulates a human body for testing purposes, but it's not a real human.

### 3.2 Key Concepts

| Concept | What it does | Example |
|---------|-------------|---------|
| **Mock** | A completely fake object — all methods return defaults (null, 0, false) unless you configure them | `@Mock UserRepository repo;` |
| **Stub** | Configuring a mock to return specific values for specific inputs | `when(repo.findById(1)).thenReturn(user)` |
| **Verify** | Checking that a specific method was called (and how many times) | `verify(repo, times(1)).save(user)` |
| **Spy** | Wraps a REAL object — calls real methods unless you override specific ones | `@Spy UserService service;` |
| **Argument Captor** | Captures the argument passed to a mocked method for inspection | `ArgumentCaptor<User> captor;` |

### 3.3 Complete Example

```java
@ExtendWith(MockitoExtension.class)  // Enables Mockito annotations
class UserServiceTest {

    @Mock
    private UserRepository userRepository;  // Fake database
    
    @Mock
    private EmailService emailService;  // Fake email sender

    @InjectMocks
    private UserService userService;  // Class we're ACTUALLY testing
    // Mockito automatically injects the mocks above into this class

    // ══════════════════════════════════════════════
    // Test 1: Successful user retrieval
    // ══════════════════════════════════════════════
    @Test
    @DisplayName("Should return user when valid ID is provided")
    void testGetUser_Success() {
        // ARRANGE — Set up the test scenario
        User mockUser = new User(1L, "Rahul", "rahul@test.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        
        // ACT — Execute the method under test
        User result = userService.getUserById(1L);
        
        // ASSERT — Verify the results
        assertNotNull(result);
        assertEquals("Rahul", result.getName());
        assertEquals("rahul@test.com", result.getEmail());
        
        // VERIFY — Ensure repository was called exactly once
        verify(userRepository, times(1)).findById(1L);
    }

    // ══════════════════════════════════════════════
    // Test 2: User not found — should throw exception
    // ══════════════════════════════════════════════
    @Test
    @DisplayName("Should throw exception when user not found")
    void testGetUser_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(99L);
        });
    }

    // ══════════════════════════════════════════════
    // Test 3: Create user — verify email was sent
    // ══════════════════════════════════════════════
    @Test
    @DisplayName("Should save user and send welcome email")
    void testCreateUser() {
        User newUser = new User(null, "Priya", "priya@test.com");
        User savedUser = new User(1L, "Priya", "priya@test.com");
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        User result = userService.createUser(newUser);
        
        assertEquals(1L, result.getId());
        verify(userRepository).save(newUser);           // Was save() called?
        verify(emailService).sendWelcomeEmail("priya@test.com"); // Was email sent?
        verify(emailService, never()).sendAdminAlert(anyString()); // This should NOT be called
    }

    // ══════════════════════════════════════════════
    // Test 4: Using ArgumentCaptor to inspect arguments
    // ══════════════════════════════════════════════
    @Test
    void testCreateUser_CaptureArguments() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        
        when(userRepository.save(any())).thenReturn(new User(1L, "Rahul", "r@test.com"));
        
        userService.createUser(new User(null, "Rahul", "r@test.com"));
        
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals("Rahul", capturedUser.getName());
    }

    // ══════════════════════════════════════════════
    // Test 5: Testing exception from dependency
    // ══════════════════════════════════════════════
    @Test
    @DisplayName("Should handle database errors gracefully")
    void testGetUser_DatabaseError() {
        when(userRepository.findById(anyLong()))
            .thenThrow(new RuntimeException("DB connection lost"));
        
        assertThrows(ServiceException.class, () -> {
            userService.getUserById(1L);
        });
    }
}
```

### 3.4 Common Stubbing Patterns

```java
// Return different values on consecutive calls
when(repo.findById(1L))
    .thenReturn(Optional.of(user))    // First call
    .thenReturn(Optional.empty())      // Second call
    .thenThrow(new RuntimeException("DB down")); // Third call

// Use thenAnswer for dynamic responses
when(repo.save(any(User.class))).thenAnswer(invocation -> {
    User input = invocation.getArgument(0);
    input.setId(100L);  // Simulate auto-generated ID
    return input;
});

// doReturn — use for void methods or spies
doNothing().when(emailService).sendEmail(anyString());
doThrow(new RuntimeException("SMTP error")).when(emailService).sendEmail("bad@email.com");
```

---

## 4. Spring Boot Testing

`spring-boot-starter-test` includes JUnit 5, Mockito, AssertJ, Hamcrest, and Spring's TestContext Framework — everything you need for testing Spring applications.

### 4.1 @SpringBootTest (Full Integration Test)

Loads the **complete** application context — all beans, configurations, database. Slowest, but tests everything together.

> **When to use:** When you need to test the full flow from controller to database. Use sparingly — these are expensive tests.

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;  // Real HTTP client
    
    @Test
    @DisplayName("Should create and retrieve user via REST API")
    void testFullUserFlow() {
        // Create user via POST
        User newUser = new User("Rahul", "rahul@test.com");
        ResponseEntity<User> createResponse = restTemplate
            .postForEntity("/api/users", newUser, User.class);
        
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody().getId());
        
        // Get user via GET
        Long userId = createResponse.getBody().getId();
        ResponseEntity<User> getResponse = restTemplate
            .getForEntity("/api/users/" + userId, User.class);
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Rahul", getResponse.getBody().getName());
    }
}
```

### 4.2 @WebMvcTest (Controller Slice Test)

Loads **only** the web layer (Controller + filters). Services are mocked with `@MockBean`. Much **faster** than `@SpringBootTest`.

> **When to use:** When testing controller logic — request mapping, validation, error responses, JSON serialization. You don't need a running database for this.

```java
@WebMvcTest(UserController.class)  // Only loads UserController
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;  // Simulates HTTP requests without actual server
    
    @MockBean
    private UserService userService;  // Spring's @Mock — registered as a Spring Bean
    
    @Test
    @DisplayName("GET /users/1 should return user JSON")
    void testGetUser() throws Exception {
        // Arrange
        User mockUser = new User(1L, "Rahul", "rahul@test.com");
        when(userService.findById(1L)).thenReturn(mockUser);
        
        // Act + Assert
        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Rahul"))
            .andExpect(jsonPath("$.email").value("rahul@test.com"));
    }
    
    @Test
    @DisplayName("GET /users/999 should return 404")
    void testGetUser_NotFound() throws Exception {
        when(userService.findById(999L)).thenThrow(new UserNotFoundException("User not found"));
        
        mockMvc.perform(get("/api/users/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("User not found"));
    }
    
    @Test
    @DisplayName("POST /users with invalid data should return 400")
    void testCreateUser_Validation() throws Exception {
        String invalidJson = "{\"name\": \"\", \"email\": \"not-an-email\"}";
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }
}
```

### 4.3 @DataJpaTest (Repository Slice Test)

Loads **only** JPA components — Entities, Repositories. Uses an **embedded in-memory database** (H2) by default.

> **When to use:** When testing custom queries, entity relationships, or database constraints.

```java
@DataJpaTest
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    @DisplayName("Should find user by email")
    void testFindByEmail() {
        // Arrange
        User user = new User("Rahul", "rahul@test.com");
        entityManager.persistAndFlush(user);
        
        // Act
        Optional<User> found = userRepository.findByEmail("rahul@test.com");
        
        // Assert
        assertTrue(found.isPresent());
        assertEquals("Rahul", found.get().getName());
    }
}
```

### 4.4 Testing Annotations Summary

| Annotation | What it loads | Speed | Use for |
|-----------|-------------|-------|---------|
| `@SpringBootTest` | Everything (full context) | 🐌 Slowest | Full integration tests |
| `@WebMvcTest` | Controller + web layer only | ⚡ Fast | Controller logic, validations |
| `@DataJpaTest` | JPA + embedded DB | ⚡ Fast | Repository queries, entity tests |
| `@MockBean` | — | — | Replace a Spring Bean with a mock |
| `@SpyBean` | — | — | Wrap a real Spring Bean (partial mock) |

---

## 5. TDD (Test Driven Development)

TDD is a development methodology where you write tests **BEFORE** writing the actual code. It follows a strict cycle:

```
    ┌──────────────┐
    │   1. RED      │  Write a test that FAILS
    │   (Write Test)│  (because the code doesn't exist yet)
    └──────┬───────┘
           ↓
    ┌──────────────┐
    │   2. GREEN    │  Write the MINIMUM code
    │   (Make Pass) │  to make the test pass
    └──────┬───────┘
           ↓
    ┌──────────────┐
    │  3. REFACTOR  │  Improve code quality
    │  (Clean Up)   │  without changing behavior
    └──────┬───────┘
           ↓
    (Repeat cycle)
```

> **Analogy:** TDD is like test-fitting a puzzle piece before gluing it. You define the exact shape you need (test), cut the piece to fit (code), then sand it smooth (refactor).

### TDD Example

```java
// Step 1: RED — Write a failing test
@Test
void testWithdraw_SufficientBalance_ShouldReduceBalance() {
    BankAccount account = new BankAccount(1000);
    account.withdraw(200);
    assertEquals(800, account.getBalance());
}
// ❌ This fails because BankAccount class doesn't exist yet!

// Step 2: GREEN — Write minimum code to pass
public class BankAccount {
    private double balance;
    
    public BankAccount(double balance) { this.balance = balance; }
    public void withdraw(double amount) { balance -= amount; }
    public double getBalance() { return balance; }
}
// ✅ Test passes!

// Step 3: REFACTOR — Add validation (write test first!)
@Test
void testWithdraw_InsufficientBalance_ShouldThrow() {
    BankAccount account = new BankAccount(100);
    assertThrows(InsufficientFundsException.class, () -> {
        account.withdraw(200);
    });
}
// Then add the validation logic to make this pass too
```

---

## 6. Testing Best Practices

### 6.1 F.I.R.S.T Principle

Good tests follow the FIRST principle:

| Letter | Principle | Meaning | Example |
|--------|-----------|---------|---------|
| **F** | Fast | Tests should run in milliseconds | Don't make HTTP calls or read files in unit tests |
| **I** | Independent | One test's result should NOT affect another | Each test creates its own data, no shared state |
| **R** | Repeatable | Same result every time, in any environment | Don't depend on system clock, network, or random values |
| **S** | Self-Validating | Tests should clearly PASS or FAIL | Use assertions, don't rely on manual output checking |
| **T** | Timely | Written at the same time as (or before) the code | Don't wait until "later" — later never comes |

### 6.2 Naming Convention

Test names should describe the **behavior** being tested, not the implementation:

```java
// ❌ BAD — describes HOW not WHAT
@Test void test1() { }
@Test void testDivide() { }

// ✅ GOOD — describes WHAT behavior is expected
// Format: methodName_stateUnderTest_expectedBehavior
@Test void withdraw_insufficientFunds_throwsException() { }
@Test void findByEmail_validEmail_returnsUser() { }
@Test void calculateDiscount_premiumMember_applies20Percent() { }
```

### 6.3 Arrange-Act-Assert (AAA) Pattern

Every test should have three clearly separated sections:

```java
@Test
void testTransferFunds() {
    // ARRANGE — Set up test data and conditions
    BankAccount source = new BankAccount(1000);
    BankAccount target = new BankAccount(500);
    
    // ACT — Execute the method under test
    source.transferTo(target, 200);
    
    // ASSERT — Verify the results
    assertEquals(800, source.getBalance());
    assertEquals(700, target.getBalance());
}
```

### 6.4 What NOT to Test

- Getters and setters (unless they have logic)
- Third-party libraries (they have their own tests)
- Framework internals (Spring, Hibernate)
- Private methods directly (test through public methods)

---

## 7. Complex Technical Scenarios

### 7.1 How to test private methods?

**Explanation:** You **should NOT** test private methods directly. They are **implementation details** — they exist to support public methods.

> **Why?** If you test private methods, your tests are coupled to implementation. When you refactor internal code without changing behavior, your tests break. That's bad.

**What to do instead:**
1. Test private methods **indirectly** through the public methods that call them
2. If a private method is complex enough to need its own tests, it's a sign it should be **extracted** into a separate class (Single Responsibility Principle)

```java
// ❌ DON'T do this (using reflection to access private methods)
Method method = UserService.class.getDeclaredMethod("validateEmail", String.class);
method.setAccessible(true);
method.invoke(userService, "test@example.com");

// ✅ DO this — test through public API
@Test
void createUser_invalidEmail_throwsValidationException() {
    // The private validateEmail() method is tested implicitly
    assertThrows(ValidationException.class, () -> {
        userService.createUser(new User("Rahul", "invalid-email"));
    });
}
```

### 7.2 Mock vs Spy — when to use which?

**Explanation:**

| Feature | Mock | Spy |
|---------|------|-----|
| **Object type** | Completely fake | Wraps a REAL object |
| **Default behavior** | All methods return defaults (null, 0, false) | All methods call REAL implementation |
| **Stubbing** | Must stub every method you want to return values | Only stub specific methods you want to override |
| **Use case** | Dependencies you want to fully control (DB, API) | When you need mostly real behavior with a few overrides |

```java
// Mock — everything is fake
@Mock
UserRepository mockRepo;
mockRepo.findById(1L);  // Returns null (not configured)

// Spy — real behavior unless overridden
@Spy
List<String> spyList = new ArrayList<>();
spyList.add("hello");  // Actually adds! (real method)
assertEquals(1, spyList.size());  // Real size = 1

doReturn(100).when(spyList).size();  // Override just this method
assertEquals(100, spyList.size());   // Now returns 100
```

> **Rule of thumb:** Use `@Mock` for external dependencies (database, API clients). Use `@Spy` when you need the real behavior of a class but want to override just one or two methods (common with legacy code).

### 7.3 How to test void methods?

**Explanation:** Void methods don't return values, so you can't use `assertEquals`. Instead, verify **side effects**:

```java
@Test
void testDeleteUser() {
    // Option 1: VERIFY — check that dependencies were called correctly
    userService.deleteUser(1L);
    verify(userRepository).deleteById(1L);
    verify(emailService).sendGoodbyeEmail("user@test.com");
    
    // Option 2: CAPTURE — inspect objects passed to dependencies
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(userRepository).deleteById(idCaptor.capture());
    assertEquals(1L, idCaptor.getValue());
    
    // Option 3: EXCEPTION — verify it throws when it should
    doThrow(new UserNotFoundException("Not found")).when(userRepository).deleteById(999L);
    assertThrows(UserNotFoundException.class, () -> userService.deleteUser(999L));
}
```

---

## 8. Key Topics & Explanations

### 8.1 What is Code Coverage?

**Explanation:** Code coverage measures **what percentage of your code** is executed during tests. It's a metric, NOT a guarantee of quality.

| Coverage Type | What it measures |
|--------------|-----------------|
| **Line Coverage** | % of code lines executed by tests |
| **Branch Coverage** | % of if/else/switch branches taken |
| **Method Coverage** | % of methods called |
| **Condition Coverage** | % of boolean sub-expressions evaluated |

**Common tools:** JaCoCo (most popular for Java), Cobertura, SonarQube.

> **Target:** 80%+ is a common industry standard, but **100% coverage ≠ zero bugs**. You can have 100% line coverage but miss edge cases. Coverage tells you what's NOT tested, not that what IS tested is tested well.

### 8.2 Parameterized Tests — run same test with multiple inputs

**Explanation:** Instead of writing 5 separate test methods for different inputs, write ONE and pass different data:

```java
// ══════════════════════════════════════════════
// Simple — @ValueSource
// ══════════════════════════════════════════════
@ParameterizedTest
@ValueSource(ints = {1, 3, 5, -1, 15})
@DisplayName("Should return true for odd numbers")
void isOdd_ShouldReturnTrue(int number) {
    assertTrue(Numbers.isOdd(number));
}

// ══════════════════════════════════════════════
// Multiple arguments — @CsvSource
// ══════════════════════════════════════════════
@ParameterizedTest
@CsvSource({
    "1, 1, 2",       // input1, input2, expectedResult
    "5, 3, 8",
    "-1, 1, 0",
    "0, 0, 0"
})
@DisplayName("Should add two numbers correctly")
void testAdd(int a, int b, int expected) {
    assertEquals(expected, calculator.add(a, b));
}

// ══════════════════════════════════════════════
// Complex objects — @MethodSource
// ══════════════════════════════════════════════
@ParameterizedTest
@MethodSource("provideUsersForValidation")
void testUserValidation(User user, boolean expectedValid) {
    assertEquals(expectedValid, userService.isValid(user));
}

static Stream<Arguments> provideUsersForValidation() {
    return Stream.of(
        Arguments.of(new User("Rahul", "r@test.com"), true),
        Arguments.of(new User("", "r@test.com"), false),
        Arguments.of(new User("Rahul", ""), false)
    );
}
```

### 8.3 `MockMvc` vs `TestRestTemplate` — when to use which?

**Explanation:**

| Feature | MockMvc | TestRestTemplate |
|---------|---------|-----------------|
| **What it does** | Simulates HTTP requests **without starting a server** | Makes **actual HTTP calls** to a running server |
| **Speed** | ⚡ Fast (in-process) | 🐢 Slower (network overhead) |
| **Server** | No server started | Full embedded server running |
| **Used with** | `@WebMvcTest` (slice test) | `@SpringBootTest` (full integration) |
| **Tests** | Controller logic, request mapping, validation | Full request/response cycle including serialization |
| **Best for** | Unit testing controllers | Integration testing the entire stack |

> **Rule of thumb:** Use `MockMvc` when testing controller behavior in isolation (faster, more focused). Use `TestRestTemplate` when you need to verify the full HTTP request/response cycle.
