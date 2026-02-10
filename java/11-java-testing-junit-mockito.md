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

Software testing is crucial for ensuring code quality and reliability.

### Types of Testing
1.  **Unit Testing**: Testing individual units of source code (e.g., a method) in isolation. Fast and focussed.
2.  **Integration Testing**: Testing how different modules work together (e.g., Service + Database).
3.  **Functional Testing**: Testing the system against functional requirements (Black box).
4.  **End-to-End (E2E) Testing**: Simulating real user scenarios from UI to DB.

### Testing Pyramid
*   **Top**: E2E Tests (Fewest, Slower)
*   **Middle**: Integration Tests
*   **Base**: Unit Tests (Most numerous, Fastest)

---

## 2. JUnit 5 Overview

The standard framework for unit testing in Java.

### Key Annotations
*   `@Test`: Marks a method as a test case.
*   `@BeforeEach`: Executed before *each* test method (Setup).
*   `@AfterEach`: Executed after *each* test method (Teardown).
*   `@BeforeAll`: Executed *once* before all tests (Static setup).
*   `@AfterAll`: Executed *once* after all tests (Static teardown).
*   `@DisplayName("Custom Name")`: Descriptive name for the test.
*   `@Disabled`: Skips the test.

### Assertions (`org.junit.jupiter.api.Assertions`)
*   `assertEquals(expected, actual)`
*   `assertTrue(condition)`
*   `assertNotNull(object)`
*   `assertThrows(Exception.class, () -> functionalInterface)` - Testing Exceptions.

```java
@Test
void testDivision() {
    Calculator calc = new Calculator();
    assertEquals(2, calc.divide(4, 2), "4/2 should be 2");
    
    assertThrows(ArithmeticException.class, () -> {
        calc.divide(1, 0);
    });
}
```

---

## 3. Mockito Framework

Used to create mock objects for dependencies, allowing you to test a class in isolation without external services (DB, API).

### Key Concepts
*   **Mock**: A dummy object that simulates real behavior.
*   **Stub**: Configuring the mock to return specific values (`when(...).thenReturn(...)`).
*   **Verify**: Checking if a method was called on the mock.

### Annotations (`@ExtendWith(MockitoExtension.class)`)
*   `@Mock`: Creates a mock instance.
*   `@InjectMocks`: Injects created mocks into the object under test.
*   `@Spy`: Partial mock (calls real methods unless stubbed).

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Mock Dependency

    @InjectMocks
    private UserService userService; // Class under test

    @Test
    void testGetUser() {
        // Arrange
        User mockUser = new User(1L, "Rahul");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertEquals("Rahul", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }
}
```

---

## 4. Spring Boot Testing

`spring-boot-starter-test` includes JUnit, Mockito, AssertJ, Hamcrest, and Spring TestContext Framework.

### @SpringBootTest
Loads the **complete** application context. Used for Integration Testing.
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate; // Simulates HTTP client

    @Test
    void testEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/hello", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

### @WebMvcTest
Loads **only** the web layer (Controller). Mocks the Service layer. Faster than `@SpringBootTest`.
```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc; // Simulates HTTP requests
    
    @MockBean
    private UserService userService; // Spring's version of @Mock
    
    @Test
    void testGetUser() throws Exception {
        when(userService.findById(1L)).thenReturn(new User(1L, "Rahul"));
        
        mockMvc.perform(get("/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Rahul"));
    }
}
```

### @DataJpaTest
Loads only JPA components (Entities, Repositories). Configures in-memory DB (H2) by default.

---

## 5. TDD (Test Driven Development)

A development process where you write tests **before** writing the code.
**Cycle**:
1.  **Red**: Write a failing test.
2.  **Green**: Write just enough code to pass the test.
3.  **Refactor**: Improve the code without changing behavior.

---

## 6. Testing Best Practices
1.  **F.I.R.S.T Principle**:
    *   **F**ast
    *   **I**ndependent (One test failure shouldn't affect others)
    *   **R**epeatable (Same result every time)
    *   **S**elf-Validating (Pass/Fail output)
    *   **T**imely (Written with or before code)
2.  **Naming Convention**: `methodName_State_ExpectedBehavior` (e.g., `withdraw_InsufficientFunds_ThrowsException`).
3.  **Arrange-Act-Assert (AAA)** pattern.

---

## 7. Complex Technical Scenarios

### Topic 1: Testing Private Methods?
**Answer:** Generally, you **should not** test private methods directly. They are implementation details. Test them publicly via the public methods that call them. If a private method is complex enough to need its own test, it usually belongs in a separate class (Refactoring opportunity).

### Topic 2: Mock vs Spy?
**Answer:**
*   **Mock**: Completely dummy object. All methods return default values (null/0) unless stubbed.
*   **Spy**: Wraps a real object. Methods call real implementation unless stubbed. Useful when you want to test partial behavior of a legacy class.

### Topic 3: How to test void methods?
**Answer:**
*   **Verify side effects**: Check if it called another dependency (`verify(dependency).save(...)`).
*   **Capture arguments**: Use `ArgumentCaptor` to inspect objects passed to dependencies.
*   **Exceptions**: Assert that it throws expected exceptions.

---

## 8. Key Topics & Explanations

### Topic 1: What is Coverage?
**Answer:**
Code Coverage measures the percentage of code lines executed by your tests. Frameworks like **JaCoCo** utilize this.
*   **Line Coverage**: % of lines executed.
*   **Branch Coverage**: % of decision branches (if/else) executed.
*   Target 80%+ is common, but 100% doesn't guarantee lack of bugs.

### Topic 2: Parameterized Tests?
**Answer:**
Running the same test with different inputs.
```java
@ParameterizedTest
@ValueSource(ints = {1, 3, 5, 15})
void isOdd_ShouldReturnTrueForOddNumbers(int number) {
    assertTrue(Numbers.isOdd(number));
}
```

### Topic 3: `MockMvc` vs `TestRestTemplate`?
**Answer:**
*   `MockMvc`: Tests Controller logic in isolation without starting a full HTTP server. Faster. (Slice Test).
*   `TestRestTemplate`: Makes actual HTTP calls to a running server. Slower. (Full Integration Test).
