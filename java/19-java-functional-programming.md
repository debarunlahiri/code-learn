# Functional Programming in Java - Complete Guide

Functional programming (FP) is a programming paradigm that treats computation as the evaluation of mathematical functions and avoids changing-state and mutable data. Java has increasingly embraced functional programming concepts since Java 8.

---

## 1. What is Functional Programming?

### Core Principles
- **Pure Functions**: Functions without side effects
- **Immutability**: Data cannot be modified after creation
- **First-class Functions**: Functions can be passed as arguments, returned from other functions
- **Higher-order Functions**: Functions that operate on other functions
- **Function Composition**: Combining simple functions to build complex ones

### Benefits
- **Predictable Code**: Same input always produces same output
- **Easier Testing**: Pure functions are easy to unit test
- **Better Concurrency**: No shared state reduces race conditions
- **Modularity**: Small, reusable functions

---

## 2. Functional Programming Features in Java

### 2.1 Lambda Expressions

```java
// Traditional approach
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello World!");
    }
};

// Lambda expression
Runnable r2 = () -> System.out.println("Hello World!");

// Lambda with parameters
Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();

// Multi-line lambda
Function<String, String> processor = s -> {
    String trimmed = s.trim();
    return trimmed.toUpperCase();
};
```

### 2.2 Method References

```java
// Static method reference
Function<String, Integer> parser = Integer::parseInt;

// Instance method reference
String str = "Hello";
Supplier<Integer> lengthSupplier = str::length;

// Constructor reference
Supplier<List<String>> listSupplier = ArrayList::new;

// Array constructor reference
Function<Integer, String[]> arrayCreator = String[]::new;
```

### 2.3 Functional Interfaces

```java
@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
    
    // Default methods are allowed
    default int operateTwice(int a, int b) {
        return operate(a, b) * 2;
    }
}

// Using the functional interface
MathOperation addition = (a, b) -> a + b;
MathOperation multiplication = (a, b) -> a * b;

// Built-in functional interfaces
Predicate<String> isEmpty = String::isEmpty;
Function<String, Integer> stringLength = String::length;
Consumer<String> printer = System.out::println;
Supplier<String> stringSupplier = () -> "Hello";
BiFunction<Integer, Integer, Integer> adder = Integer::sum;
```

---

## 3. Stream API - Functional Data Processing

### 3.1 Creating Streams

```java
// From collections
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream = list.stream();

// From arrays
String[] array = {"a", "b", "c"};
Stream<String> arrayStream = Arrays.stream(array);

// From values
Stream<Integer> valueStream = Stream.of(1, 2, 3, 4, 5);

// Generate infinite stream
Stream<Double> randomStream = Stream.generate(Math::random).limit(10);

// Iterate
Stream<Integer> iterateStream = Stream.iterate(0, n -> n + 2).limit(10);
```

### 3.2 Intermediate Operations

```java
List<Person> people = Arrays.asList(
    new Person("John", 25, "Engineer"),
    new Person("Jane", 30, "Designer"),
    new Person("Bob", 35, "Engineer")
);

// Filter
List<Person> engineers = people.stream()
    .filter(p -> "Engineer".equals(p.getProfession()))
    .collect(Collectors.toList());

// Map
List<String> names = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());

// FlatMap
List<List<String>> nestedList = Arrays.asList(
    Arrays.asList("a", "b"),
    Arrays.asList("c", "d")
);
List<String> flattened = nestedList.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());

// Distinct
List<Integer> distinctNumbers = Arrays.asList(1, 2, 2, 3, 3, 4).stream()
    .distinct()
    .collect(Collectors.toList());

// Sorted
List<Person> sortedByAge = people.stream()
    .sorted(Comparator.comparing(Person::getAge))
    .collect(Collectors.toList());

// Peek (for debugging)
List<String> processed = people.stream()
    .peek(p -> System.out.println("Processing: " + p.getName()))
    .map(Person::getName)
    .collect(Collectors.toList());

// Limit and Skip
List<Integer> paginated = IntStream.range(0, 100)
    .skip(10)
    .limit(20)
    .boxed()
    .collect(Collectors.toList());
```

### 3.3 Terminal Operations

```java
// Collect
List<String> names = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());

Set<String> uniqueProfessions = people.stream()
    .map(Person::getProfession)
    .collect(Collectors.toSet());

Map<String, List<Person>> byProfession = people.stream()
    .collect(Collectors.groupingBy(Person::getProfession));

Map<String, Long> countByProfession = people.stream()
    .collect(Collectors.groupingBy(Person::getProfession, Collectors.counting()));

String joinedNames = people.stream()
    .map(Person::getName)
    .collect(Collectors.joining(", "));

// Reduce
Optional<Integer> sum = Arrays.asList(1, 2, 3, 4, 5).stream()
    .reduce(Integer::sum);

int product = Arrays.asList(1, 2, 3, 4, 5).stream()
    .reduce(1, (a, b) -> a * b);

// Match
boolean allAdults = people.stream()
    .allMatch(p -> p.getAge() >= 18);

boolean anyEngineer = people.stream()
    .anyMatch(p -> "Engineer".equals(p.getProfession()));

// ForEach
people.stream()
    .forEach(p -> System.out.println(p.getName()));

// Count
long count = people.stream().count();

// Min/Max
Optional<Person> oldest = people.stream()
    .max(Comparator.comparing(Person::getAge));

Optional<Person> youngest = people.stream()
    .min(Comparator.comparing(Person::getAge));
```

---

## 4. Advanced Functional Patterns

### 4.1 Function Composition

```java
import java.util.function.Function;

// Function composition utility
public class FunctionUtils {
    public static <A, B, C> Function<A, C> compose(Function<B, C> f, Function<A, B> g) {
        return x -> f.apply(g.apply(x));
    }
    
    public static <T> Function<T, T> compose(Function<T, T>... functions) {
        return Arrays.stream(functions)
            .reduce(Function.identity(), Function::andThen);
    }
}

// Usage
Function<String, String> addHello = s -> "Hello " + s;
Function<String, String> addExclamation = s -> s + "!";

Function<String, String> composed = FunctionUtils.compose(addExclamation, addHello);
String result = composed.apply("World"); // "Hello World!"
```

### 4.2 Currying

```java
// Currying example
public class CurryingExample {
    // Normal function
    public static int add(int a, int b, int c) {
        return a + b + c;
    }
    
    // Curried version
    public static Function<Integer, Function<Integer, Function<Integer, Integer>>> addCurried() {
        return a -> b -> c -> a + b + c;
    }
    
    public static void main(String[] args) {
        // Using curried function
        Function<Integer, Function<Integer, Function<Integer, Integer>>> curried = addCurried();
        Function<Integer, Function<Integer, Integer>> add5 = curried.apply(5);
        Function<Integer, Integer> add5And10 = add5.apply(10);
        int result = add5And10.apply(20); // 35
        
        // Partial application
        Function<Integer, Integer> add5And10Partial = addCurried().apply(5).apply(10);
        int result2 = add5And10Partial.apply(20); // 35
    }
}
```

### 4.3 Memoization

```java
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Memoizer {
    public static <T, R> Function<T, R> memoize(Function<T, R> function) {
        Map<T, R> cache = new HashMap<>();
        return input -> cache.computeIfAbsent(input, function);
    }
    
    public static void main(String[] args) {
        // Expensive function
        Function<Integer, Integer> fibonacci = Memoizer.memoize(n -> {
            if (n <= 1) return n;
            return fibonacci.apply(n - 1) + fibonacci.apply(n - 2);
        });
        
        System.out.println(fibonacci.apply(10)); // 55
        System.out.println(fibonacci.apply(10)); // Cached result
    }
}
```

---

## 5. Functional Data Structures

### 5.1 Immutable List

```java
import java.util.*;
import java.util.stream.Collectors;

public class ImmutableList<T> {
    private final List<T> elements;
    
    public ImmutableList() {
        this.elements = Collections.emptyList();
    }
    
    public ImmutableList(List<T> elements) {
        this.elements = Collections.unmodifiableList(new ArrayList<>(elements));
    }
    
    public ImmutableList<T> add(T element) {
        List<T> newList = new ArrayList<>(elements);
        newList.add(element);
        return new ImmutableList<>(newList);
    }
    
    public ImmutableList<T> remove(T element) {
        List<T> newList = new ArrayList<>(elements);
        newList.remove(element);
        return new ImmutableList<>(newList);
    }
    
    public ImmutableList<T> update(int index, T element) {
        List<T> newList = new ArrayList<>(elements);
        newList.set(index, element);
        return new ImmutableList<>(newList);
    }
    
    public List<T> toList() {
        return elements;
    }
    
    public int size() {
        return elements.size();
    }
    
    public T get(int index) {
        return elements.get(index);
    }
}

// Usage
ImmutableList<String> list = new ImmutableList<>();
ImmutableList<String> list2 = list.add("Hello");
ImmutableList<String> list3 = list2.add("World");
```

### 5.2 Optional Usage Patterns

```java
import java.util.*;
import java.util.function.*;

public class OptionalPatterns {
    
    // Creating Optionals
    public Optional<String> findUserById(String id) {
        // Simulate database lookup
        if ("123".equals(id)) {
            return Optional.of("John Doe");
        }
        return Optional.empty();
    }
    
    // Safe operations
    public String getUserName(String id) {
        return findUserById(id)
            .map(String::toUpperCase)
            .orElse("UNKNOWN");
    }
    
    // Chaining operations
    public Optional<Integer> getUserAge(String id) {
        return findUserById(id)
            .map(this::getUserFromDatabase)
            .map(User::getAge);
    }
    
    // Filtering
    public List<String> getAdultUsers(List<String> ids) {
        return ids.stream()
            .map(this::findUserById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(user -> user.getAge() >= 18)
            .map(User::getName)
            .collect(Collectors.toList());
    }
    
    // Optional as return type
    public Optional<String> findFirstMatch(List<String> list, Predicate<String> predicate) {
        return list.stream()
            .filter(predicate)
            .findFirst();
    }
    
    // Avoiding null checks
    public String processUser(User user) {
        return Optional.ofNullable(user)
            .map(User::getName)
            .map(String::toUpperCase)
            .orElse("DEFAULT");
    }
    
    // Optional in streams
    public List<String> getValidNames(List<Optional<String>> optionalNames) {
        return optionalNames.stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
}
```

---

## 6. Functional Error Handling

### 6.1 Either Pattern

```java
public class Either<L, R> {
    private final L left;
    private final R right;
    private final boolean isRight;
    
    private Either(L left, R right, boolean isRight) {
        this.left = left;
        this.right = right;
        this.isRight = isRight;
    }
    
    public static <L, R> Either<L, R> left(L value) {
        return new Either<>(value, null, false);
    }
    
    public static <L, R> Either<L, R> right(R value) {
        return new Either<>(null, value, true);
    }
    
    public boolean isRight() {
        return isRight;
    }
    
    public boolean isLeft() {
        return !isRight;
    }
    
    public R getRight() {
        if (!isRight) {
            throw new IllegalStateException("Not a right value");
        }
        return right;
    }
    
    public L getLeft() {
        if (isRight) {
            throw new IllegalStateException("Not a left value");
        }
        return left;
    }
    
    public <T> Either<L, T> map(Function<R, T> mapper) {
        if (isRight) {
            return Either.right(mapper.apply(right));
        }
        return Either.left(left);
    }
    
    public <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper) {
        if (isRight) {
            return mapper.apply(right);
        }
        return Either.left(left);
    }
    
    public R orElse(R defaultValue) {
        return isRight ? right : defaultValue;
    }
}

// Usage
public class UserService {
    public Either<String, User> findUser(String id) {
        try {
            User user = database.findById(id);
            return Either.right(user);
        } catch (Exception e) {
            return Either.left("User not found: " + e.getMessage());
        }
    }
    
    public Either<String, String> getUserName(String id) {
        return findUser(id)
            .map(User::getName)
            .map(String::toUpperCase);
    }
}
```

### 6.2 Try Pattern

```java
public class Try<T> {
    private final T value;
    private final Exception exception;
    private final boolean isSuccess;
    
    private Try(T value, Exception exception, boolean isSuccess) {
        this.value = value;
        this.exception = exception;
        this.isSuccess = isSuccess;
    }
    
    public static <T> Try<T> success(T value) {
        return new Try<>(value, null, true);
    }
    
    public static <T> Try<T> failure(Exception exception) {
        return new Try<>(null, exception, false);
    }
    
    public static <T> Try<T> of(Callable<T> callable) {
        try {
            return success(callable.call());
        } catch (Exception e) {
            return failure(e);
        }
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }
    
    public boolean isFailure() {
        return !isSuccess;
    }
    
    public T get() {
        if (!isSuccess) {
            throw new IllegalStateException("Not a success");
        }
        return value;
    }
    
    public Exception getException() {
        if (isSuccess) {
            throw new IllegalStateException("Not a failure");
        }
        return exception;
    }
    
    public <R> Try<R> map(Function<T, R> mapper) {
        if (isSuccess) {
            return Try.of(() -> mapper.apply(value));
        }
        return failure(exception);
    }
    
    public <R> Try<R> flatMap(Function<T, Try<R>> mapper) {
        if (isSuccess) {
            try {
                return mapper.apply(value);
            } catch (Exception e) {
                return failure(e);
            }
        }
        return failure(exception);
    }
    
    public T orElse(T defaultValue) {
        return isSuccess ? value : defaultValue;
    }
    
    public T orElseGet(Supplier<T> supplier) {
        return isSuccess ? value : supplier.get();
    }
}

// Usage
public class FileService {
    public Try<String> readFile(String path) {
        return Try.of(() -> {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path)));
        });
    }
    
    public Try<Integer> countLines(String path) {
        return readFile(path)
            .map(content -> content.split("\n").length);
    }
}
```

---

## 7. Functional Design Patterns

### 7.1 Strategy Pattern

```java
// Traditional approach
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " with credit card");
    }
}

class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " with PayPal");
    }
}

// Functional approach
public class PaymentProcessor {
    private final DoubleConsumer paymentMethod;
    
    public PaymentProcessor(DoubleConsumer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public void processPayment(double amount) {
        paymentMethod.accept(amount);
    }
}

// Usage
PaymentProcessor creditCard = new PaymentProcessor(
    amount -> System.out.println("Paid $" + amount + " with credit card")
);

PaymentProcessor payPal = new PaymentProcessor(
    amount -> System.out.println("Paid $" + amount + " with PayPal")
);

creditCard.processPayment(100.0);
payPal.processPayment(50.0);
```

### 7.2 Command Pattern

```java
// Functional command pattern
@FunctionalInterface
interface Command {
    void execute();
}

public class CommandProcessor {
    private final List<Command> commands = new ArrayList<>();
    
    public void addCommand(Command command) {
        commands.add(command);
    }
    
    public void executeCommands() {
        commands.forEach(Command::execute);
        commands.clear();
    }
}

// Usage
CommandProcessor processor = new CommandProcessor();
processor.addCommand(() -> System.out.println("Command 1 executed"));
processor.addCommand(() -> System.out.println("Command 2 executed"));
processor.addCommand(() -> System.out.println("Command 3 executed"));
processor.executeCommands();
```

---

## 8. Performance Considerations

### 8.1 Stream Performance Tips

```java
// Good practices
public class StreamPerformance {
    
    // Use primitive streams for numeric operations
    public int sumPrimitive(List<Integer> numbers) {
        return numbers.stream()
            .mapToInt(Integer::intValue)
            .sum();
    }
    
    // Avoid boxing with primitive streams
    public IntStream getPrimitiveStream() {
        return IntStream.range(0, 1000);
    }
    
    // Parallel streams for CPU-intensive operations
    public List<String> processInParallel(List<String> data) {
        return data.parallelStream()
            .map(this::expensiveOperation)
            .collect(Collectors.toList());
    }
    
    // Use appropriate collectors
    public Map<String, Long> countByProfession(List<Person> people) {
        return people.stream()
            .collect(Collectors.groupingByConcurrent(
                Person::getProfession, 
                Collectors.counting()
            ));
    }
    
    // Short-circuit operations
    public Optional<String> findFirstMatch(List<String> list, Predicate<String> predicate) {
        return list.stream()
            .filter(predicate)
            .findFirst(); // Short-circuits
    }
    
    // Avoid unnecessary operations
    public List<String> efficientProcessing(List<String> data) {
        return data.stream()
            .filter(s -> s != null && !s.isEmpty()) // Filter first
            .map(String::toLowerCase)
            .distinct()
            .collect(Collectors.toList());
    }
}
```

### 8.2 Memory Management

```java
public class FunctionalMemoryManagement {
    
    // Use method references instead of lambdas when possible
    private final List<String> data = Arrays.asList("a", "b", "c");
    
    // Good - method reference
    public List<Integer> getLengthsGood() {
        return data.stream()
            .map(String::length)
            .collect(Collectors.toList());
    }
    
    // Less efficient - lambda creates new object
    public List<Integer> getLengthsBad() {
        return data.stream()
            .map(s -> s.length())
            .collect(Collectors.toList());
    }
    
    // Avoid capturing large objects
    public Function<String, String> createProcessor() {
        // Bad - captures entire list
        List<String> largeList = createLargeList();
        return s -> s + largeList.size();
        
        // Good - capture only what's needed
        // int size = largeList.size();
        // return s -> s + size;
    }
    
    // Use lazy evaluation
    public Stream<String> getLazyStream() {
        return data.stream()
            .filter(this::expensiveFilter)
            .map(this::expensiveMap);
    }
    
    private boolean expensiveFilter(String s) {
        // Expensive operation
        return true;
    }
    
    private String expensiveMap(String s) {
        // Expensive operation
        return s.toUpperCase();
    }
}
```

---

## 9. Testing Functional Code

### 9.1 Testing Pure Functions

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FunctionalTesting {
    
    @Test
    public void testPureFunction() {
        // Pure functions are easy to test
        Function<Integer, Integer> square = x -> x * x;
        
        assertEquals(4, square.apply(2));
        assertEquals(9, square.apply(3));
        assertEquals(0, square.apply(0));
    }
    
    @Test
    public void testStreamOperations() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        
        List<String> result = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        
        assertEquals(Arrays.asList("ALICE", "CHARLIE"), result);
    }
    
    @Test
    public void testOptionalOperations() {
        Optional<String> optional = Optional.of("Hello");
        
        String result = optional
            .map(String::toUpperCase)
            .orElse("DEFAULT");
        
        assertEquals("HELLO", result);
    }
    
    @Test
    public void testEitherPattern() {
        Either<String, Integer> result = divide(10, 2);
        
        assertTrue(result.isRight());
        assertEquals(5, result.getRight());
        
        Either<String, Integer> error = divide(10, 0);
        
        assertTrue(error.isLeft());
        assertEquals("Cannot divide by zero", error.getLeft());
    }
    
    private Either<String, Integer> divide(int a, int b) {
        if (b == 0) {
            return Either.left("Cannot divide by zero");
        }
        return Either.right(a / b);
    }
}
```

### 9.2 Mocking Functional Interfaces

```java
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class FunctionalMocking {
    
    @Test
    public void testMockingFunction() {
        // Mock a function
        Function<String, Integer> mockFunction = mock(Function.class);
        
        when(mockFunction.apply("test")).thenReturn(42);
        
        assertEquals(42, mockFunction.apply("test"));
        verify(mockFunction).apply("test");
    }
    
    @Test
    public void testMockingPredicate() {
        Predicate<String> mockPredicate = mock(Predicate.class);
        
        when(mockPredicate.test("valid")).thenReturn(true);
        when(mockPredicate.test("invalid")).thenReturn(false);
        
        assertTrue(mockPredicate.test("valid"));
        assertFalse(mockPredicate.test("invalid"));
    }
    
    @Test
    public void testMockingConsumer() {
        Consumer<String> mockConsumer = mock(Consumer.class);
        
        mockConsumer.accept("test");
        
        verify(mockConsumer).accept("test");
    }
}
```

---

## 10. Best Practices and Guidelines

### 10.1 When to Use Functional Programming

```java
public class FunctionalGuidelines {
    
    // Good for data transformation
    public List<String> transformData(List<String> data) {
        return data.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .map(String::toLowerCase)
            .distinct()
            .collect(Collectors.toList());
    }
    
    // Good for validation chains
    public boolean validateUser(User user) {
        return Optional.ofNullable(user)
            .filter(u -> u.getName() != null && !u.getName().trim().isEmpty())
            .filter(u -> u.getEmail() != null && u.getEmail().contains("@"))
            .filter(u -> u.getAge() >= 18)
            .isPresent();
    }
    
    // Good for configuration
    public void configureSystem() {
        List<Consumer<SystemConfig>> configurations = Arrays.asList(
            config -> config.setDatabaseUrl("jdbc:mysql://localhost:3306/mydb"),
            config -> config.setMaxConnections(100),
            config -> config.setTimeout(30000)
        );
        
        configurations.forEach(config -> config.accept(new SystemConfig()));
    }
    
    // Avoid for simple operations
    public String badExample(String input) {
        return Optional.ofNullable(input)
            .map(String::trim)
            .map(String::toLowerCase)
            .orElse("default");
        // Better: return input == null ? "default" : input.trim().toLowerCase();
    }
}
```

### 10.2 Code Style Guidelines

```java
public class FunctionalStyle {
    
    // Use meaningful variable names
    public List<String> getActiveUserNames(List<User> users) {
        return users.stream()
            .filter(User::isActive)
            .map(User::getName)
            .collect(Collectors.toList());
    }
    
    // Keep lambda expressions short
    public List<Integer> getSquares(List<Integer> numbers) {
        return numbers.stream()
            .map(n -> n * n)  // Simple operation
            .collect(Collectors.toList());
    }
    
    // Extract complex logic to methods
    public List<String> getFormattedUserNames(List<User> users) {
        return users.stream()
            .filter(this::isValidUser)
            .map(this::formatUserName)
            .collect(Collectors.toList());
    }
    
    private boolean isValidUser(User user) {
        return user != null && 
               user.getName() != null && 
               !user.getName().trim().isEmpty() &&
               user.isActive();
    }
    
    private String formatUserName(User user) {
        return user.getName().trim().toUpperCase();
    }
    
    // Use method references when appropriate
    public List<String> getNames(List<User> users) {
        return users.stream()
            .map(User::getName)  // Method reference
            .collect(Collectors.toList());
    }
}
```

---

## 11. Common Pitfalls and How to Avoid Them

### 11.1 Side Effects in Streams

```java
public class StreamPitfalls {
    
    private List<String> processedItems = new ArrayList<>();
    
    // BAD - Modifying external state
    public void processItemsBad(List<String> items) {
        items.stream()
            .forEach(item -> processedItems.add(item.toUpperCase()));
    }
    
    // GOOD - Collect results
    public List<String> processItemsGood(List<String> items) {
        return items.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    }
    
    // BAD - Exception handling in streams
    public void processWithExceptionsBad(List<String> items) {
        items.stream()
            .forEach(item -> {
                try {
                    // Risky operation
                    riskyOperation(item);
                } catch (Exception e) {
                    // Swallowing exceptions
                }
            });
    }
    
    // GOOD - Handle exceptions properly
    public List<String> processWithExceptionsGood(List<String> items) {
        return items.stream()
            .map(this::safeOperation)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
    
    private Optional<String> safeOperation(String item) {
        try {
            return Optional.of(riskyOperation(item));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    private String riskyOperation(String item) throws Exception {
        // Simulate risky operation
        return item.toUpperCase();
    }
}
```

### 11.2 Performance Issues

```java
public class PerformancePitfalls {
    
    // BAD - Creating large intermediate collections
    public List<String> processBad(List<String> items) {
        List<String> filtered = items.stream()
            .filter(s -> s.length() > 5)
            .collect(Collectors.toList());
            
        List<String> mapped = filtered.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
            
        return mapped.stream()
            .distinct()
            .collect(Collectors.toList());
    }
    
    // GOOD - Single pipeline
    public List<String> processGood(List<String> items) {
        return items.stream()
            .filter(s -> s.length() > 5)
            .map(String::toUpperCase)
            .distinct()
            .collect(Collectors.toList());
    }
    
    // BAD - Using parallel streams inappropriately
    public List<String> parallelBad(List<String> items) {
        return items.parallelStream()  // Small list, overhead > benefit
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    }
    
    // GOOD - Use parallel streams for large, CPU-intensive operations
    public List<String> parallelGood(List<String> items) {
        if (items.size() > 10000) {
            return items.parallelStream()
                .map(this::expensiveOperation)
                .collect(Collectors.toList());
        } else {
            return items.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        }
    }
    
    private String expensiveOperation(String item) {
        // Simulate expensive operation
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return item.toUpperCase();
    }
}
```

---

## 12. Integration with Modern Java Features

### 12.1 Records and Functional Programming

```java
// Record with functional interfaces
public record Person(String name, int age, String profession) {}

public class FunctionalRecords {
    
    // Using records in streams
    public List<String> getAdultNames(List<Person> people) {
        return people.stream()
            .filter(person -> person.age() >= 18)
            .map(Person::name)
            .collect(Collectors.toList());
    }
    
    // Functional record patterns
    public Optional<String> findFirstEngineer(List<Person> people) {
        return people.stream()
            .filter(person -> "Engineer".equals(person.profession()))
            .findFirst()
            .map(Person::name);
    }
    
    // Record as function parameter
    public Function<Person, String> personFormatter() {
        return person -> String.format("%s (%d) - %s", 
            person.name(), person.age(), person.profession());
    }
}
```

### 12.2 Sealed Classes and Pattern Matching

```java
// Sealed hierarchy for functional programming
public sealed interface Result permits Success, Failure {}

public final record Success<T>(T value) implements Result {}

public final record Failure(String error) implements Result {}

public class FunctionalPatternMatching {
    
    public String processResult(Result result) {
        return switch (result) {
            case Success(String s) -> "Success: " + s.toUpperCase();
            case Success(Integer i) -> "Number: " + i * 2;
            case Success(Object o) -> "Other: " + o.toString();
            case Failure(String error) -> "Error: " + error;
        };
    }
    
    public List<String> processResults(List<Result> results) {
        return results.stream()
            .map(this::processResult)
            .collect(Collectors.toList());
    }
}
```

---

## 13. Real-World Examples

### 13.1 Data Processing Pipeline

```java
public class DataProcessingPipeline {
    
    public static class DataProcessor {
        private final List<Function<String, String>> processors = new ArrayList<>();
        
        public DataProcessor addProcessor(Function<String, String> processor) {
            processors.add(processor);
            return this;
        }
        
        public List<String> process(List<String> data) {
            return data.stream()
                .map(this::applyProcessors)
                .collect(Collectors.toList());
        }
        
        private String applyProcessors(String input) {
            return processors.stream()
                .reduce(Function.identity(), Function::andThen)
                .apply(input);
        }
    }
    
    public static void main(String[] args) {
        List<String> rawData = Arrays.asList(
            "  hello world  ",
            "  java programming  ",
            "  functional style  "
        );
        
        List<String> processed = new DataProcessor()
            .addProcessor(String::trim)
            .addProcessor(String::toLowerCase)
            .addProcessor(s -> s.replace(" ", "_"))
            .addProcessor(s -> s + "_processed")
            .process(rawData);
        
        processed.forEach(System.out::println);
        // Output:
        // hello_world_processed
        // java_programming_processed
        // functional_style_processed
    }
}
```

### 13.2 Event Processing System

```java
public class EventProcessor {
    
    @FunctionalInterface
    public interface EventHandler<T> {
        void handle(T event);
    }
    
    private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unchecked")
    public <T> void register(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }
    
    @SuppressWarnings("unchecked")
    public <T> void emit(T event) {
        Class<?> eventType = event.getClass();
        List<EventHandler<?>> eventHandlers = handlers.get(eventType);
        
        if (eventHandlers != null) {
            eventHandlers.stream()
                .map(handler -> (EventHandler<T>) handler)
                .forEach(handler -> handler.handle(event));
        }
    }
    
    public static void main(String[] args) {
        EventProcessor processor = new EventProcessor();
        
        // Register handlers
        processor.register(String.class, 
            event -> System.out.println("String event: " + event));
            
        processor.register(Integer.class, 
            event -> System.out.println("Integer event: " + event));
        
        // Emit events
        processor.emit("Hello World");
        processor.emit(42);
        processor.emit("Another string");
    }
}
```

---

## 14. Summary and Key Takeaways

### Core Concepts to Remember
1. **Pure Functions**: No side effects, predictable output
2. **Immutability**: Create new objects instead of modifying existing ones
3. **Higher-Order Functions**: Functions that operate on other functions
4. **Lazy Evaluation**: Stream operations are evaluated when needed
5. **Function Composition**: Build complex functions from simple ones

### Best Practices
1. Use streams for data transformation and processing
2. Prefer method references over lambdas when possible
3. Keep lambda expressions short and focused
4. Use Optional to avoid null pointer exceptions
5. Extract complex logic to separate methods

### When to Use Functional Programming
- Data transformation and processing
- Validation chains
- Configuration and setup
- Event handling
- Parallel processing

### When to Avoid Functional Programming
- Simple operations where imperative code is clearer
- Performance-critical sections with tight loops
- When dealing with mutable state is more natural
- Complex control flow that's hard to express functionally

Functional programming in Java provides powerful tools for writing more declarative, testable, and maintainable code. Master these concepts to write modern, efficient Java applications!
