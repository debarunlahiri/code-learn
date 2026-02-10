# Java Exception Handling - Complete Guide (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Exception Hierarchy, Checked vs Unchecked, Custom Exceptions, Try-with-Resources, Best Practices, and tricky scenarios.

---

## Table of Contents

1. [Exception Hierarchy](#exception-hierarchy)
2. [Checked vs Unchecked Exceptions](#checked-vs-unchecked-exceptions)
3. [try-catch-finally](#try-catch-finally)
4. [throw vs throws](#throw-vs-throws)
5. [Custom Exceptions](#custom-exceptions)
6. [Try-with-Resources (Java 7+)](#try-with-resources-java-7)
7. [Multi-Catch (Java 7+)](#multi-catch-java-7)
8. [Exception Chaining](#exception-chaining)
9. [Best Practices](#best-practices)
10. [Complex Technical Scenarios](#complex-technical-scenarios)
11. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Exception Hierarchy

```
                        Object
                          |
                       Throwable
                      /         \
                 Error          Exception
                /    \         /          \
        VirtualMachineError  RuntimeException   IOException
        StackOverflowError   NullPointerException FileNotFoundException
        OutOfMemoryError     ArrayIndexOOBException SQLException
                             ClassCastException     ClassNotFoundException
                             ArithmeticException
                             IllegalArgumentException
                             NumberFormatException
                             ConcurrentModificationException
```

### Key Points

| Type | Description | Handling |
|------|-------------|----------|
| **Error** | JVM/System level problems. Cannot recover. | Do NOT catch |
| **Checked Exception** | Compile-time checked. Must handle or declare. | Must catch or throws |
| **Unchecked Exception** | Runtime exceptions. Compiler does NOT check. | Optional catch |

---

## 2. Checked vs Unchecked Exceptions

### Checked Exceptions (Compile-time)

Compiler forces you to handle these. They represent **recoverable** conditions.

```java
// Must handle or declare — compiler enforces
try {
    FileReader reader = new FileReader("file.txt"); // FileNotFoundException (checked)
    Connection conn = DriverManager.getConnection(url); // SQLException (checked)
} catch (FileNotFoundException e) {
    System.out.println("File not found: " + e.getMessage());
} catch (SQLException e) {
    System.out.println("DB error: " + e.getMessage());
}
```

**Common Checked Exceptions:**
- `IOException`, `FileNotFoundException`
- `SQLException`
- `ClassNotFoundException`
- `InterruptedException`
- `CloneNotSupportedException`

### Unchecked Exceptions (Runtime)

Compiler does NOT force handling. They represent **programming bugs**.

```java
// Compiler doesn't complain — but will crash at runtime
String str = null;
str.length(); // NullPointerException

int[] arr = new int[5];
arr[10] = 1; // ArrayIndexOutOfBoundsException

int result = 10 / 0; // ArithmeticException

Object obj = "Hello";
Integer num = (Integer) obj; // ClassCastException
```

**Common Unchecked Exceptions:**
- `NullPointerException`
- `ArrayIndexOutOfBoundsException`
- `ArithmeticException`
- `ClassCastException`
- `IllegalArgumentException`
- `NumberFormatException`
- `ConcurrentModificationException`
- `StackOverflowError` (Error, not Exception)

### Comparison

| Feature | Checked | Unchecked |
|---------|---------|-----------|
| **Superclass** | `Exception` (not RuntimeException) | `RuntimeException` |
| **Compile-time check** | ✅ Yes | ❌ No |
| **Must handle** | ✅ Yes (catch or throws) | ❌ Optional |
| **Represents** | Recoverable conditions | Programming bugs |
| **Examples** | IOException, SQLException | NullPointer, ClassCast |

---

## 3. try-catch-finally

### Basic Structure

```java
try {
    // Code that may throw exception
    int result = 10 / 0;
} catch (ArithmeticException e) {
    // Handle specific exception
    System.out.println("Cannot divide by zero: " + e.getMessage());
} catch (Exception e) {
    // Handle general exception (must be after specific)
    System.out.println("Error: " + e.getMessage());
} finally {
    // ALWAYS executes (cleanup code)
    System.out.println("Finally block executed");
}
```

### finally Block Rules

```java
// 1. finally ALWAYS executes — even after return
public static int testFinally() {
    try {
        return 1;
    } catch (Exception e) {
        return 2;
    } finally {
        System.out.println("Finally executed!"); // ✅ This runs
        // return 3; // ⚠️ BAD PRACTICE — overrides try/catch return
    }
}
// Output: "Finally executed!" → returns 1

// 2. finally does NOT execute in these cases:
// a. System.exit(0) is called
// b. JVM crashes
// c. Thread is killed (Thread.stop() — deprecated)
// d. Infinite loop or deadlock in try block
```

### Tricky finally Scenario ⚠️

```java
// What does this return?
public static int getValue() {
    try {
        return 10;
    } finally {
        return 20; // ⚠️ Overrides the try return!
    }
}
// Returns 20 — finally's return overrides try's return
// This is BAD PRACTICE — never return from finally
```

---

## 4. throw vs throws

### throw — Used inside method body

```java
public void withdraw(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
    }
    if (amount > balance) {
        throw new InsufficientFundsException("Not enough balance");
    }
    balance -= amount;
}
```

### throws — Used in method signature

```java
// Declares potential exceptions — caller must handle
public void readFile(String path) throws IOException, FileNotFoundException {
    FileReader reader = new FileReader(path);
    // ...
}

// Caller must handle
try {
    readFile("data.txt");
} catch (FileNotFoundException e) {
    System.out.println("File not found");
} catch (IOException e) {
    System.out.println("IO error");
}
```

### Comparison

| Feature | throw | throws |
|---------|-------|--------|
| **Location** | Inside method body | In method signature |
| **Purpose** | Actually throw exception | Declare potential exceptions |
| **Count** | One exception at a time | Multiple exceptions (comma-separated) |
| **Followed by** | Exception instance | Exception class names |

---

## 5. Custom Exceptions

### Creating Custom Checked Exception

```java
// Extends Exception → Checked
public class InsufficientFundsException extends Exception {

    private double amount;

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, double amount) {
        super(message);
        this.amount = amount;
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public double getAmount() {
        return amount;
    }
}
```

### Creating Custom Unchecked Exception

```java
// Extends RuntimeException → Unchecked
public class UserNotFoundException extends RuntimeException {

    private Long userId;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}

// Usage
public User findUser(Long id) {
    return userRepo.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
}
```

---

## 6. Try-with-Resources (Java 7+)

Automatically closes resources that implement `AutoCloseable` or `Closeable`.

### Before Java 7

```java
BufferedReader reader = null;
try {
    reader = new BufferedReader(new FileReader("file.txt"));
    String line = reader.readLine();
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (reader != null) {
        try {
            reader.close(); // Verbose!
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### After Java 7 — try-with-resources

```java
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line = reader.readLine();
    System.out.println(line);
} catch (IOException e) {
    e.printStackTrace();
}
// reader.close() is called AUTOMATICALLY — even if exception occurs
```

### Multiple Resources

```java
try (
    FileInputStream fis = new FileInputStream("input.txt");
    FileOutputStream fos = new FileOutputStream("output.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(fis))
) {
    // Use resources
} catch (IOException e) {
    e.printStackTrace();
}
// All resources closed in REVERSE order of creation
```

### Java 9 Enhancement

```java
// Java 9 — can use effectively final variables
BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
try (reader) { // No need to re-declare
    String line = reader.readLine();
}
```

### Custom AutoCloseable

```java
public class DatabaseConnection implements AutoCloseable {

    public DatabaseConnection() {
        System.out.println("Connection opened");
    }

    public void query(String sql) {
        System.out.println("Executing: " + sql);
    }

    @Override
    public void close() {
        System.out.println("Connection closed");
    }
}

try (DatabaseConnection conn = new DatabaseConnection()) {
    conn.query("SELECT * FROM users");
}
// Output:
// Connection opened
// Executing: SELECT * FROM users
// Connection closed
```

---

## 7. Multi-Catch (Java 7+)

```java
// Before Java 7 — repetitive
try {
    // code
} catch (IOException e) {
    logger.error("Error", e);
    throw e;
} catch (SQLException e) {
    logger.error("Error", e);
    throw e;
}

// After Java 7 — clean
try {
    // code
} catch (IOException | SQLException e) {
    logger.error("Error", e);
    throw e;
}
// Note: 'e' is implicitly final in multi-catch
// e = new IOException(); // COMPILE ERROR
```

### Rules

- Exceptions in multi-catch must NOT be in parent-child relationship.
- `catch (IOException | FileNotFoundException e)` → COMPILE ERROR (FNF is child of IO)
- The variable `e` is implicitly final — cannot be reassigned.

---

## 8. Exception Chaining

Preserving the original cause of an exception while throwing a higher-level exception.

```java
public User loadUser(long id) {
    try {
        return database.findById(id);
    } catch (SQLException e) {
        // Wrap low-level exception in business exception
        throw new ServiceException("Failed to load user: " + id, e);
        // 'e' is preserved as the CAUSE
    }
}

// Later — accessing the chain
try {
    loadUser(1);
} catch (ServiceException e) {
    System.out.println("Service error: " + e.getMessage());
    System.out.println("Root cause: " + e.getCause().getMessage());
    e.printStackTrace(); // Shows full chain
}
```

---

## 9. Best Practices

### Do's ✅

```java
// 1. Catch specific exceptions first
try {
    // code
} catch (FileNotFoundException e) {   // Specific FIRST
    // handle
} catch (IOException e) {             // General AFTER
    // handle
}

// 2. Use try-with-resources for closeable resources
try (Connection conn = dataSource.getConnection()) {
    // use connection
}

// 3. Log with the exception object
catch (Exception e) {
    logger.error("Operation failed", e); // ✅ Passes stack trace
}

// 4. Use custom exceptions for business logic
throw new InsufficientFundsException("Balance too low");

// 5. Document exceptions with @throws Javadoc
/**
 * @throws UserNotFoundException if user ID doesn't exist
 */
public User getUser(Long id) { ... }
```

### Don'ts ❌

```java
// 1. DON'T catch Exception/Throwable generically
catch (Exception e) { } // ❌ Swallows everything — dangerous

// 2. DON'T use empty catch blocks
catch (IOException e) { } // ❌ Silent failure — bugs will be hidden

// 3. DON'T use exceptions for flow control
try {
    int i = Integer.parseInt(str); // ❌ Use validation instead
} catch (NumberFormatException e) {
    // treat as default
}
// ✅ Better: if (str.matches("\\d+")) { ... }

// 4. DON'T return from finally block
finally {
    return value; // ❌ Overrides try/catch returns — confusing
}

// 5. DON'T throw from finally block
finally {
    throw new RuntimeException(); // ❌ Replaces original exception
}

// 6. DON'T lose the original exception
catch (SQLException e) {
    throw new RuntimeException("Error"); // ❌ Lost original cause
    throw new RuntimeException("Error", e); // ✅ Chain it
}
```

---

## 10. Complex Technical Scenarios

### Topic 1: What happens when exception occurs in finally block?

**Explanation:**
If both `try` and `finally` throw exceptions, the exception from `finally` **replaces** the one from `try`. The original exception is **lost**.

```java
try {
    throw new RuntimeException("From try");
} finally {
    throw new RuntimeException("From finally"); // This replaces the try exception
}
// Only "From finally" is thrown — "From try" is LOST
```

**Solution:** Use try-with-resources which handles **suppressed exceptions**.

### Topic 2: Suppressed Exceptions (Java 7+)

**Explanation:**
When using try-with-resources, if both the `try` block and `close()` throw exceptions, the `close()` exception is **suppressed** (not lost).

```java
try (MyResource res = new MyResource()) { // close() throws "Close Error"
    throw new RuntimeException("Try Error");
}
// Primary exception: "Try Error"
// Suppressed: "Close Error"
// Access: e.getSuppressed() returns array of suppressed exceptions
```

### Topic 3: Can you override a method and throw different exceptions?

**Explanation:**
- **Unchecked exceptions**: Override can throw any unchecked exception regardless of parent.
- **Checked exceptions**: Override can throw:
  - Same exception
  - Subclass of the exception
  - No exception (narrower)
  - **Cannot** throw broader/new checked exception

```java
class Parent {
    void show() throws IOException { }
}

class Child extends Parent {
    void show() throws FileNotFoundException { } // ✅ Subclass of IOException
    // void show() throws Exception { }          // ❌ Broader than IOException
    // void show() throws SQLException { }       // ❌ New checked exception
    // void show() { }                           // ✅ No exception (narrower)
}
```

### Topic 4: Error vs Exception?

**Explanation:**
- **Error**: Unrecoverable JVM problems. Should NOT be caught. E.g., `OutOfMemoryError`, `StackOverflowError`, `VirtualMachineError`.
- **Exception**: Application-level problems. Can/should be caught. E.g., `IOException`, `NullPointerException`.

### Topic 5: ClassNotFoundException vs NoClassDefFoundError?

**Explanation:**
- `ClassNotFoundException`: **Checked exception**. Thrown when `Class.forName()` or `ClassLoader.loadClass()` cannot find the class at runtime. The class was never available.
- `NoClassDefFoundError`: **Error**. Class was available at compile time but missing at runtime (e.g., deleted JAR, dependency issue). JVM cannot load it.

---

## 11. Key Topics & Explanations

### Topic 1: final vs finally vs finalize?

**Explanation:**
- `final`: Keyword. Makes variable constant, method non-overridable, class non-extendable.
- `finally`: Exception handling block. Always executes after try/catch.
- `finalize()`: Object method (deprecated in Java 9). Called by GC before destroying object. Unreliable — do NOT use.

### Topic 2: When should you create a Checked vs Unchecked custom exception?

**Explanation:**
- **Checked**: When the caller can reasonably be expected to **recover** from it. E.g., `InsufficientFundsException` — caller can show a message.
- **Unchecked**: When it represents a **programming error** or unrecoverable state. E.g., `InvalidConfigurationException` — app cannot proceed.

### Topic 3: Can we rethrow an exception?

**Explanation:** Yes. Three patterns:
```java
// 1. Rethrow as-is
catch (IOException e) {
    throw e;
}

// 2. Wrap and rethrow (Exception Chaining)
catch (IOException e) {
    throw new ServiceException("Failed", e);
}

// 3. Rethrow as unchecked (avoid checked exception propagation)
catch (IOException e) {
    throw new RuntimeException(e);
}
```

### Topic 4: What is a StackOverflowError?

**Explanation:** An `Error` (not Exception) thrown when the call stack exceeds its limit — usually from **infinite recursion**.
```java
void recurse() {
    recurse(); // No base case → StackOverflowError
}
```
**Fix**: Always have a proper base case in recursive methods.
