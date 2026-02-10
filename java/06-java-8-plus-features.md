# Java 8+ Features - Complete Guide (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Lambda, Functional Interfaces, Method References, Optional, Date/Time API, and features from Java 9 to 21.

--

## Table of Contents

1. [Lambda Expressions](#lambda-expressions)
2. [Functional Interfaces](#functional-interfaces)
3. [Built-in Functional Interfaces](#built-in-functional-interfaces)
4. [Method References](#method-references)
5. [Optional](#optional)
6. [Default and Static Methods in Interfaces](#default-and-static-methods-in-interfaces)
7. [Date and Time API (java.time)](#date-and-time-api-javatime)
8. [Nashorn JavaScript Engine (Java 8, Removed in 15)](#nashorn-javascript-engine-java-8-removed-in-15)
9. [Java 9 Features](#java-9-features)
10. [Java 10 Features](#java-10-features)
11. [Java 11 Features](#java-11-features)
12. [Java 12-13 Features](#java-12-13-features)
13. [Java 14 Features](#java-14-features)
14. [Java 15-16 Features](#java-15-16-features)
15. [Java 17 Features (LTS)](#java-17-features-lts)
16. [Java 18-21 Features](#java-18-21-features)
17. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. Lambda Expressions

A **lambda expression** is a concise way to represent an anonymous function (a function without a name).

### Syntax

```
(parameters) -> expression
(parameters) -> { statements; }
```

### 1.1 Basic Examples

```java
// Before Java 8 — Anonymous inner class
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello from thread");
    }
};

// Java 8 — Lambda expression
Runnable r2 = () -> System.out.println("Hello from thread");

// With parameters
Comparator<String> comp1 = (a, b) -> a.compareTo(b);

// Multi-line body
Comparator<String> comp2 = (a, b) -> {
    System.out.println("Comparing " + a + " and " + b);
    return a.compareTo(b);
};
```

### 1.2 Lambda with Collections

```java
List<String> names = Arrays.asList("Rahul", "Amit", "Sneha", "Priya");

// Sort
names.sort((a, b) -> a.compareTo(b));

// forEach
names.forEach(name -> System.out.println(name));

// removeIf
names.removeIf(name -> name.startsWith("A"));

// replaceAll
names.replaceAll(name -> name.toUpperCase());
```

### 1.3 Lambda Scope and Effectively Final

Lambdas can access:
- Local variables (must be **effectively final**)
- Instance variables
- Static variables

```java
int factor = 2; // effectively final (never reassigned)

List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
nums.stream()
    .map(n -> n * factor) // OK — factor is effectively final
    .forEach(System.out::println);

// factor = 3; // If uncommented, the lambda above would NOT compile
```

### 1.4 Lambda vs Anonymous Inner Class

| Feature | Lambda | Anonymous Inner Class |
|-----|----|------------|
| **Syntax** | Concise | Verbose |
| **`this` keyword** | Refers to enclosing class | Refers to anonymous class itself |
| **Type** | Must be a functional interface | Can be any interface/abstract class |
| **Compilation** | Uses `invokedynamic` | Generates a separate `.class` file |
| **State** | Cannot have instance variables | Can have instance variables |

--

## 2. Functional Interfaces

A **functional interface** has exactly **one abstract method**. It can have multiple default and static methods.

### 2.1 Defining a Functional Interface

```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b); // single abstract method

    // These are allowed:
    default void log(String msg) { System.out.println(msg); }
    static void info() { System.out.println("Calculator v1.0"); }
}

// Usage with lambda
Calculator add = (a, b) -> a + b;
Calculator multiply = (a, b) -> a * b;

System.out.println(add.calculate(5, 3));      // 8
System.out.println(multiply.calculate(5, 3)); // 15
```

### 2.2 @FunctionalInterface Annotation

- **Optional** but recommended.
- Causes a **compile error** if the interface has more than one abstract method.
- Acts as documentation.

```java
@FunctionalInterface
interface Greeting {
    void greet(String name);
    // void farewell(String name); // COMPILE ERROR — two abstract methods
}
```

--

## 3. Built-in Functional Interfaces

All in `java.util.function` package.

### 3.1 Predicate<T> — Test a condition

```java
// boolean test(T t)
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<String> isLong = s -> s.length() > 5;

System.out.println(isEven.test(4));  // true
System.out.println(isLong.test("Hi")); // false

// Chaining predicates
Predicate<Integer> isPositive = n -> n > 0;
Predicate<Integer> isPositiveEven = isPositive.and(isEven);
Predicate<Integer> isNegativeOrEven = isPositive.negate().or(isEven);

System.out.println(isPositiveEven.test(4));  // true
System.out.println(isPositiveEven.test(-4)); // false

// With streams
List<Integer> evens = numbers.stream()
        .filter(isEven)
        .collect(Collectors.toList());
```

### 3.2 Function<T, R> — Transform input to output

```java
// R apply(T t)
Function<String, Integer> length = String::length;
Function<Integer, String> intToString = String::valueOf;

System.out.println(length.apply("Hello")); // 5

// Chaining: andThen and compose
Function<String, String> toUpper = String::toUpperCase;
Function<String, String> addExclaim = s -> s + "!";

Function<String, String> shout = toUpper.andThen(addExclaim);
System.out.println(shout.apply("hello")); // HELLO!

Function<String, String> shout2 = addExclaim.compose(toUpper);
System.out.println(shout2.apply("hello")); // HELLO!
```

### 3.3 Consumer<T> — Consume input, return nothing

```java
// void accept(T t)
Consumer<String> printer = System.out::println;
Consumer<String> logger = s -> System.out.println("[LOG] " + s);

printer.accept("Hello"); // Hello

// Chaining
Consumer<String> printAndLog = printer.andThen(logger);
printAndLog.accept("Test");
// Test
// [LOG] Test

// With forEach
names.forEach(printer);
```

### 3.4 Supplier<T> — Supply a value, take no input

```java
// T get()
Supplier<String> greeting = () -> "Hello, World!";
Supplier<Double> random = Math::random;
Supplier<List<String>> listFactory = ArrayList::new;

System.out.println(greeting.get()); // Hello, World!
System.out.println(random.get());   // 0.7234...

// Useful with Optional
Optional<String> opt = Optional.empty();
String value = opt.orElseGet(() -> "Default"); // Supplier for lazy evaluation
```

### 3.5 BiFunction<T, U, R>, BiPredicate<T, U>, BiConsumer<T, U>

```java
// BiFunction — two inputs, one output
BiFunction<String, String, String> concat = (a, b) -> a + " " + b;
System.out.println(concat.apply("Hello", "World")); // Hello World

// BiPredicate — two inputs, boolean output
BiPredicate<String, Integer> isLongerThan = (s, len) -> s.length() > len;
System.out.println(isLongerThan.test("Hello", 3)); // true

// BiConsumer — two inputs, no output
BiConsumer<String, Integer> printEntry = (key, value) ->
    System.out.println(key + " = " + value);

Map<String, Integer> map = Map.of("a", 1, "b", 2);
map.forEach(printEntry);
```

### 3.6 UnaryOperator<T> and BinaryOperator<T>

```java
// UnaryOperator<T> extends Function<T, T> — same input and output type
UnaryOperator<String> toUpper = String::toUpperCase;
System.out.println(toUpper.apply("hello")); // HELLO

// BinaryOperator<T> extends BiFunction<T, T, T> — same types
BinaryOperator<Integer> sum = Integer::sum;
BinaryOperator<Integer> max = Integer::max;

System.out.println(sum.apply(5, 3)); // 8
System.out.println(max.apply(5, 3)); // 5
```

### 3.7 Complete Reference Table

| Interface | Method | Input → Output | Example |
|------|----|--------|-----|
| `Predicate<T>` | `test(T)` | `T → boolean` | Filter condition |
| `Function<T,R>` | `apply(T)` | `T → R` | Transform/map |
| `Consumer<T>` | `accept(T)` | `T → void` | forEach action |
| `Supplier<T>` | `get()` | `() → T` | Factory/lazy value |
| `UnaryOperator<T>` | `apply(T)` | `T → T` | replaceAll |
| `BinaryOperator<T>` | `apply(T,T)` | `(T,T) → T` | reduce |
| `BiFunction<T,U,R>` | `apply(T,U)` | `(T,U) → R` | toMap value mapper |
| `BiPredicate<T,U>` | `test(T,U)` | `(T,U) → boolean` | Two-arg condition |
| `BiConsumer<T,U>` | `accept(T,U)` | `(T,U) → void` | Map.forEach |

--

## 4. Method References

A **method reference** is a shorthand for a lambda that calls an existing method.

### 4.1 Four Types

```java
// 1. Static method reference — ClassName::staticMethod
Function<String, Integer> parseInt = Integer::parseInt;
// Equivalent: s -> Integer.parseInt(s)

// 2. Instance method of a particular object — object::instanceMethod
String prefix = "Hello";
Predicate<String> startsWith = prefix::startsWith;
// Equivalent: s -> prefix.startsWith(s)

// 3. Instance method of an arbitrary object — ClassName::instanceMethod
Function<String, String> toUpper = String::toUpperCase;
// Equivalent: s -> s.toUpperCase()

BiPredicate<String, String> contains = String::contains;
// Equivalent: (s1, s2) -> s1.contains(s2)

// 4. Constructor reference — ClassName::new
Supplier<ArrayList<String>> listFactory = ArrayList::new;
// Equivalent: () -> new ArrayList<>()

Function<String, Integer> toInt = Integer::new;
// Equivalent: s -> new Integer(s)
```

### 4.2 Practical Examples

```java
List<String> names = Arrays.asList("Rahul", "Amit", "Sneha");

// forEach with method reference
names.forEach(System.out::println);

// map with method reference
List<String> upper = names.stream()
        .map(String::toUpperCase)
        .collect(Collectors.toList());

// sort with method reference
names.sort(String::compareToIgnoreCase);

// Convert strings to integers
List<String> numStrings = Arrays.asList("1", "2", "3");
List<Integer> nums = numStrings.stream()
        .map(Integer::parseInt)
        .collect(Collectors.toList());

// Create objects
List<Employee> employees = names.stream()
        .map(Employee::new) // calls Employee(String name) constructor
        .collect(Collectors.toList());
```

--

## 5. Optional

`Optional<T>` is a container that may or may not hold a non-null value. Introduced to avoid `NullPointerException`.

### 5.1 Creating Optional

```java
// Empty
Optional<String> empty = Optional.empty();

// Of (throws NPE if null)
Optional<String> present = Optional.of("Hello");
// Optional.of(null); // NullPointerException!

// OfNullable (safe for null)
Optional<String> nullable = Optional.ofNullable(null);   // empty Optional
Optional<String> nullable2 = Optional.ofNullable("Hi");  // Optional with value
```

### 5.2 Checking and Extracting Values

```java
Optional<String> opt = Optional.of("Hello");

// isPresent / isEmpty (Java 11)
if (opt.isPresent()) {
    System.out.println(opt.get()); // Hello
}

// ifPresent — preferred over isPresent + get
opt.ifPresent(value -> System.out.println(value));

// ifPresentOrElse (Java 9)
opt.ifPresentOrElse(
    value -> System.out.println("Found: " + value),
    () -> System.out.println("Not found")
);
```

### 5.3 Default Values

```java
// orElse — always evaluates the default
String value1 = opt.orElse("Default");

// orElseGet — lazy, evaluates only if empty
String value2 = opt.orElseGet(() -> expensiveComputation());

// orElseThrow
String value3 = opt.orElseThrow(() -> new RuntimeException("Not found"));

// orElseThrow without argument (Java 10) — throws NoSuchElementException
String value4 = opt.orElseThrow();

// or (Java 9) — returns another Optional
Optional<String> result = opt.or(() -> Optional.of("Fallback"));
```

### 5.4 Transforming Optional

```java
Optional<String> name = Optional.of("  Rahul  ");

// map — transform the value
Optional<String> trimmed = name.map(String::trim);
Optional<Integer> length = name.map(String::trim).map(String::length);

// flatMap — when the function returns Optional
Optional<String> flatMapped = name.flatMap(n -> findNickname(n));

// filter — keep value only if it matches
Optional<String> filtered = name.filter(n -> n.trim().length() > 3);
```

### 5.5 Optional Best Practices

```java
// DO — use as return type for methods that may not return a value
public Optional<Employee> findById(int id) {
    // return Optional.ofNullable(result);
}

// DON'T — use as method parameter
public void process(Optional<String> name) { } // BAD

// DON'T — use as field
class Employee {
    Optional<String> nickname; // BAD — use null or default value
}

// DON'T — use Optional.get() without checking
opt.get(); // BAD — may throw NoSuchElementException

// DO — chain operations
String result = findById(1)
        .map(Employee::getName)
        .map(String::toUpperCase)
        .orElse("UNKNOWN");
```

### 5.6 orElse vs orElseGet

```java
// orElse — ALWAYS evaluates the argument
String val1 = opt.orElse(expensiveCall()); // expensiveCall() runs even if opt has value

// orElseGet — evaluates ONLY if Optional is empty
String val2 = opt.orElseGet(() -> expensiveCall()); // lazy — runs only when needed
```

**Rule:** Use `orElseGet` when the default value is expensive to compute.

--

## 6. Default and Static Methods in Interfaces

### 6.1 Default Methods

Allow adding new methods to interfaces **without breaking existing implementations**.

```java
interface Collection<E> {
    // Existing abstract methods...

    // Added in Java 8 — existing implementations don't break
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
```

### 6.2 Resolving Conflicts

```java
interface A {
    default void hello() { System.out.println("A"); }
}

interface B {
    default void hello() { System.out.println("B"); }
}

class C implements A, B {
    @Override
    public void hello() {
        A.super.hello(); // explicitly choose A's version
    }
}
```

### Resolution Rules

1. **Class wins over interface** — if a class and interface have the same method, the class method is used.
2. **Sub-interface wins** — more specific interface wins over parent interface.
3. **Must resolve explicitly** — if two unrelated interfaces have the same default method, the class must override.

### 6.3 Static Methods in Interfaces

```java
interface Validator {
    boolean validate(String input);

    static Validator emailValidator() {
        return input -> input.contains("@");
    }

    static Validator phoneValidator() {
        return input -> input.matches("\\d{10}");
    }
}

Validator email = Validator.emailValidator();
System.out.println(email.validate("test@mail.com")); // true
```

--

## 7. Date and Time API (java.time)

Introduced in Java 8 to replace the problematic `java.util.Date` and `java.util.Calendar`.

### Why New API?

| Old API Problem | New API Solution |
|--------|---------|
| `Date` is mutable | All classes are **immutable** |
| Not thread-safe | **Thread-safe** |
| Poor API design | Clean, fluent API |
| No timezone support | Built-in timezone support |
| Months start from 0 | Months start from 1 |

### 7.1 LocalDate — Date without time

```java
import java.time.LocalDate;
import java.time.Month;

LocalDate today = LocalDate.now();                    // 2025-02-08
LocalDate specific = LocalDate.of(2025, Month.MARCH, 15); // 2025-03-15
LocalDate parsed = LocalDate.parse("2025-03-15");     // 2025-03-15

// Getters
int year = today.getYear();           // 2025
Month month = today.getMonth();       // FEBRUARY
int day = today.getDayOfMonth();      // 8
DayOfWeek dow = today.getDayOfWeek(); // SATURDAY

// Manipulation (returns new object — immutable)
LocalDate tomorrow = today.plusDays(1);
LocalDate lastMonth = today.minusMonths(1);
LocalDate nextYear = today.plusYears(1);

// Comparison
boolean isBefore = today.isBefore(specific);
boolean isAfter = today.isAfter(specific);
boolean isLeap = today.isLeapYear();
```

### 7.2 LocalTime — Time without date

```java
import java.time.LocalTime;

LocalTime now = LocalTime.now();              // 14:30:45.123
LocalTime specific = LocalTime.of(14, 30);    // 14:30
LocalTime parsed = LocalTime.parse("14:30:00"); // 14:30

int hour = now.getHour();
int minute = now.getMinute();
int second = now.getSecond();

LocalTime later = now.plusHours(2).plusMinutes(30);
```

### 7.3 LocalDateTime — Date + Time

```java
import java.time.LocalDateTime;

LocalDateTime now = LocalDateTime.now();
LocalDateTime specific = LocalDateTime.of(2025, 3, 15, 14, 30, 0);
LocalDateTime combined = LocalDateTime.of(LocalDate.now(), LocalTime.now());

// Convert between types
LocalDate date = now.toLocalDate();
LocalTime time = now.toLocalTime();
```

### 7.4 ZonedDateTime — Date + Time + Timezone

```java
import java.time.ZonedDateTime;
import java.time.ZoneId;

ZonedDateTime nowIST = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));

// Convert timezone
ZonedDateTime istToUTC = nowIST.withZoneSameInstant(ZoneId.of("UTC"));

// List all available zones
Set<String> zones = ZoneId.getAvailableZoneIds();
```

### 7.5 Instant — Machine timestamp

```java
import java.time.Instant;

Instant now = Instant.now();                    // 2025-02-08T09:00:00Z
Instant epoch = Instant.ofEpochSecond(0);       // 1970-01-01T00:00:00Z
long epochMillis = now.toEpochMilli();

// Duration between two instants
Instant start = Instant.now();
// ... some operation
Instant end = Instant.now();
Duration elapsed = Duration.between(start, end);
```

### 7.6 Duration and Period

```java
import java.time.Duration;
import java.time.Period;

// Duration — time-based (hours, minutes, seconds, nanos)
Duration twoHours = Duration.ofHours(2);
Duration between = Duration.between(LocalTime.of(10, 0), LocalTime.of(14, 30));
System.out.println(between.toMinutes()); // 270

// Period — date-based (years, months, days)
Period tenDays = Period.ofDays(10);
Period between2 = Period.between(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 3, 15));
System.out.println(between2); // P1Y2M14D (1 year, 2 months, 14 days)

// Age calculation
LocalDate birthDate = LocalDate.of(1997, 5, 15);
Period age = Period.between(birthDate, LocalDate.now());
System.out.println("Age: " + age.getYears() + " years");
```

### 7.7 DateTimeFormatter

```java
import java.time.format.DateTimeFormatter;

LocalDateTime now = LocalDateTime.now();

// Predefined formatters
String iso = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

// Custom pattern
DateTimeFormatter custom = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
String formatted = now.format(custom); // 08-02-2025 14:30:45

// Parse with custom format
LocalDate parsed = LocalDate.parse("15-03-2025",
    DateTimeFormatter.ofPattern("dd-MM-yyyy"));

// Indian format
DateTimeFormatter indianFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String indian = LocalDate.now().format(indianFormat); // 08/02/2025
```

### 7.8 Converting Between Old and New API

```java
// Date → LocalDate
Date oldDate = new Date();
LocalDate newDate = oldDate.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();

// LocalDate → Date
Date backToOld = Date.from(
    LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
);

// Calendar → LocalDateTime
Calendar cal = Calendar.getInstance();
LocalDateTime ldt = LocalDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault());
```

--

## 8. Nashorn JavaScript Engine

Introduced in Java 8, **removed in Java 15**. Allowed running JavaScript from Java.

```java
// Java 8-14 only
ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
engine.eval("print('Hello from JavaScript')");
engine.eval("var x = 10 + 20; print(x)"); // 30
```

> **Note:** Replaced by GraalVM's JavaScript engine. Rarely asked in technical reviews.

--

## 9. Java 9 Features

### 9.1 Module System (Project Jigsaw)

```java
// module-info.java
module com.myapp {
    requires java.sql;
    exports com.myapp.api;
}
```

### 9.2 Collection Factory Methods

```java
// Immutable collections — concise creation
List<String> list = List.of("a", "b", "c");
Set<Integer> set = Set.of(1, 2, 3);
Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);

// More than 10 entries
Map<String, Integer> bigMap = Map.ofEntries(
    Map.entry("a", 1),
    Map.entry("b", 2),
    Map.entry("c", 3)
);

// These are UNMODIFIABLE — any modification throws UnsupportedOperationException
// list.add("d"); // UnsupportedOperationException
```

### 9.3 Stream Improvements

```java
// takeWhile — take elements while predicate is true
List<Integer> result = Stream.of(1, 2, 3, 4, 5, 6)
        .takeWhile(n -> n < 4)
        .collect(Collectors.toList()); // [1, 2, 3]

// dropWhile — drop elements while predicate is true
List<Integer> result2 = Stream.of(1, 2, 3, 4, 5, 6)
        .dropWhile(n -> n < 4)
        .collect(Collectors.toList()); // [4, 5, 6]

// ofNullable — create stream of 0 or 1 elements
Stream<String> s = Stream.ofNullable(null);  // empty stream
Stream<String> s2 = Stream.ofNullable("Hi"); // stream of one element

// iterate with predicate
Stream.iterate(1, n -> n < 100, n -> n * 2)
        .forEach(System.out::println); // 1, 2, 4, 8, 16, 32, 64
```

### 9.4 Optional Improvements

```java
// ifPresentOrElse
Optional.of("Hello").ifPresentOrElse(
    System.out::println,
    () -> System.out.println("Empty")
);

// or — return another Optional if empty
Optional<String> result = Optional.<String>empty()
        .or(() -> Optional.of("Fallback"));

// stream — convert Optional to Stream
long count = Optional.of("Hello").stream().count(); // 1
```

### 9.5 Private Methods in Interfaces

```java
interface Logger {
    default void logInfo(String msg) { log("INFO", msg); }
    default void logError(String msg) { log("ERROR", msg); }

    // Private helper — shared by default methods
    private void log(String level, String msg) {
        System.out.println("[" + level + "] " + msg);
    }
}
```

### 9.6 Try-With-Resources Enhancement

```java
// Java 7 — must declare in try
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    // use br
}

// Java 9 — can use effectively final variable
BufferedReader br = new BufferedReader(new FileReader("file.txt"));
try (br) { // no need to re-declare
    // use br
}
```

### 9.7 JShell — Interactive REPL

```bash
$ jshell
jshell> int x = 10;
x ==> 10
jshell> System.out.println(x * 2);
20
jshell> /exit
```

--

## 10. Java 10 Features

### 10.1 Local Variable Type Inference (`var`)

```java
// Before Java 10
String name = "Rahul";
List<String> names = new ArrayList<>();
Map<String, List<Integer>> map = new HashMap<>();

// Java 10+ — var infers the type
var name = "Rahul";                    // inferred as String
var names = new ArrayList<String>();   // inferred as ArrayList<String>
var map = new HashMap<String, List<Integer>>();
var stream = names.stream();           // inferred as Stream<String>

// Works in for loops
for (var n : names) {
    System.out.println(n);
}

// Works in try-with-resources
try (var br = new BufferedReader(new FileReader("file.txt"))) {
    // ...
}
```

### 10.2 Where `var` CANNOT Be Used

```java
// var x;                    // NO — must have initializer
// var x = null;             // NO — cannot infer type from null
// var arr = {1, 2, 3};     // NO — array initializer
// public var name = "Hi";  // NO — not for fields
// void method(var x) {}    // NO — not for parameters
// var lambda = (x) -> x;   // NO — not for lambdas
```

### 10.3 Unmodifiable Collections

```java
// copyOf — creates unmodifiable copy
List<String> original = new ArrayList<>(Arrays.asList("a", "b", "c"));
List<String> copy = List.copyOf(original);
// copy.add("d"); // UnsupportedOperationException

// Collectors.toUnmodifiableList/Set/Map
List<String> unmodifiable = names.stream()
        .collect(Collectors.toUnmodifiableList());
```

--

## 11. Java 11 Features (LTS)

### 11.1 New String Methods

```java
" Hello ".strip();       // "Hello" (Unicode-aware trim)
" Hello ".stripLeading(); // "Hello "
" Hello ".stripTrailing(); // " Hello"
"".isBlank();             // true
" ".isBlank();            // true (whitespace only)
"Hello".isBlank();        // false

// lines() — split by line terminators
"Line1\nLine2\nLine3".lines()
    .forEach(System.out::println);

// repeat()
"Ha".repeat(3); // "HaHaHa"
```

### 11.2 Local Variable Syntax for Lambda Parameters

```java
// Java 11 — var in lambda parameters (useful for annotations)
list.stream()
    .map((@NotNull var s) -> s.toUpperCase())
    .collect(Collectors.toList());
```

### 11.3 HTTP Client (Standard)

```java
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

HttpClient client = HttpClient.newHttpClient();

HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/data"))
        .GET()
        .build();

HttpResponse<String> response = client.send(request,
    HttpResponse.BodyHandlers.ofString());

System.out.println(response.statusCode());
System.out.println(response.body());

// Async
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    .thenApply(HttpResponse::body)
    .thenAccept(System.out::println);
```

### 11.4 Files Utility Methods

```java
// Read entire file as string
String content = Files.readString(Path.of("file.txt"));

// Write string to file
Files.writeString(Path.of("output.txt"), "Hello World");
```

### 11.5 Optional.isEmpty()

```java
Optional<String> opt = Optional.empty();
System.out.println(opt.isEmpty()); // true (opposite of isPresent)
```

### 11.6 Running Single-File Programs

```bash
# No need to compile first
java HelloWorld.java
```

--

## 12. Java 12-13 Features

### 12.1 Switch Expressions (Preview in 12, Standard in 14)

```java
// Old switch
String day = "MONDAY";
String type;
switch (day) {
    case "MONDAY":
    case "TUESDAY":
        type = "Weekday";
        break;
    case "SATURDAY":
    case "SUNDAY":
        type = "Weekend";
        break;
    default:
        type = "Unknown";
}

// New switch expression (Java 14+)
String type2 = switch (day) {
    case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> "Weekday";
    case "SATURDAY", "SUNDAY" -> "Weekend";
    default -> "Unknown";
};

// With blocks and yield
String type3 = switch (day) {
    case "MONDAY" -> {
        System.out.println("Start of week");
        yield "Weekday";
    }
    case "SATURDAY", "SUNDAY" -> "Weekend";
    default -> "Unknown";
};
```

### 12.2 Collectors.teeing() (Java 12)

```java
// Apply two collectors simultaneously and merge results
var result = Stream.of(1, 2, 3, 4, 5).collect(
    Collectors.teeing(
        Collectors.summingInt(Integer::intValue),  // sum
        Collectors.counting(),                      // count
        (sum, count) -> "Sum=" + sum + ", Count=" + count
    )
);
// Sum=15, Count=5
```

### 12.3 Text Blocks (Preview in 13, Standard in 15)

```java
// Old way
String json = "{\n" +
    "  \"name\": \"Rahul\",\n" +
    "  \"age\": 28\n" +
    "}";

// Text blocks (Java 15+)
String json2 = """
    {
        "name": "Rahul",
        "age": 28
    }
    """;

String html = """
    <html>
        <body>
            <h1>Hello</h1>
        </body>
    </html>
    """;

String sql = """
    SELECT name, age
    FROM employees
    WHERE department = 'IT'
    ORDER BY name
    """;
```

--

## 13. Java 14 Features

### 13.1 Pattern Matching for instanceof

```java
// Old way
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// Java 14+ (standard in 16)
if (obj instanceof String s) {
    System.out.println(s.length()); // s is already cast
}

// With logical operators
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long string: " + s);
}
```

### 13.2 Helpful NullPointerExceptions

```java
// Before Java 14
// Exception: NullPointerException (no details)

// Java 14+
// Exception: NullPointerException: Cannot invoke "String.length()"
// because "employee.getName()" is null
```

### 13.3 Records (Preview in 14, Standard in 16)

```java
record Point(int x, int y) { }

Point p = new Point(3, 4);
System.out.println(p.x());     // 3 (getter — no 'get' prefix)
System.out.println(p.y());     // 4
System.out.println(p);         // Point[x=3, y=4]

// Auto-generated: constructor, getters, equals, hashCode, toString
```

--

## 14. Java 15-16 Features

### 14.1 Sealed Classes (Preview in 15, Standard in 17)

```java
public sealed class Shape permits Circle, Rectangle, Triangle { }

public final class Circle extends Shape { }
public final class Rectangle extends Shape { }
public non-sealed class Triangle extends Shape { }
```

### 14.2 Records (Standard in 16)

```java
record Employee(String name, int age, double salary) {
    // Compact constructor for validation
    Employee {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
    }

    // Custom method
    String nameUpperCase() {
        return name.toUpperCase();
    }
}
```

### 14.3 Stream.toList() (Java 16)

```java
// Before Java 16
List<String> list = names.stream()
        .filter(n -> n.length() > 3)
        .collect(Collectors.toList());

// Java 16+ — shorter
List<String> list = names.stream()
        .filter(n -> n.length() > 3)
        .toList(); // returns unmodifiable list
```

> **Note:** `toList()` returns an **unmodifiable** list, while `Collectors.toList()` returns a **modifiable** list.

--

## 15. Java 17 Features (LTS)

### 15.1 Sealed Classes (Standard)

See Section 14.1 — now fully standard.

### 15.2 Pattern Matching for switch (Preview)

```java
// Standard in Java 21
static String format(Object obj) {
    return switch (obj) {
        case Integer i -> "Integer: " + i;
        case String s  -> "String: " + s;
        case Long l    -> "Long: " + l;
        case null      -> "null";
        default        -> "Unknown: " + obj;
    };
}
```

### 15.3 Enhanced Pseudo-Random Number Generators

```java
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

RandomGenerator rng = RandomGenerator.of("L128X256MixRandom");
int random = rng.nextInt(100);
```

--

## 16. Java 18-21 Features

### 16.1 Simple Web Server (Java 18)

```bash
# Start a simple HTTP file server
jwebserver -port 8080
```

### 16.2 UTF-8 by Default (Java 18)

All Java APIs now default to UTF-8 charset.

### 16.3 Record Patterns (Java 21)

```java
record Point(int x, int y) { }

// Deconstruct records in pattern matching
if (obj instanceof Point(int x, int y)) {
    System.out.println("x=" + x + ", y=" + y);
}

// In switch
String describe(Object obj) {
    return switch (obj) {
        case Point(var x, var y) -> "Point at (" + x + ", " + y + ")";
        default -> "Unknown";
    };
}
```

### 16.4 Pattern Matching for switch (Standard in Java 21)

```java
// Guarded patterns
String describe(Shape shape) {
    return switch (shape) {
        case Circle c when c.radius() > 10 -> "Large circle";
        case Circle c -> "Small circle";
        case Rectangle r -> "Rectangle";
        case Triangle t -> "Triangle";
    };
}
```

### 16.5 Virtual Threads (Java 21)

Lightweight threads for high-throughput concurrent applications.

```java
// Create a virtual thread
Thread vThread = Thread.ofVirtual().start(() -> {
    System.out.println("Running in virtual thread");
});

// Using ExecutorService
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10_000; i++) {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return "Done";
        });
    }
} // executor.close() is called automatically — waits for all tasks

// Thread.ofVirtual().factory()
ThreadFactory factory = Thread.ofVirtual().factory();
```

### 16.6 Sequenced Collections (Java 21)

```java
// New interfaces: SequencedCollection, SequencedSet, SequencedMap
List<String> list = new ArrayList<>(List.of("a", "b", "c"));

list.getFirst();    // "a"
list.getLast();     // "c"
list.addFirst("z"); // ["z", "a", "b", "c"]
list.addLast("d");  // ["z", "a", "b", "c", "d"]
list.reversed();    // reversed view

// SequencedMap
LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
map.put("a", 1);
map.put("b", 2);
map.firstEntry();   // a=1
map.lastEntry();    // b=2
map.reversed();     // reversed view
```

### 16.7 String Templates (Preview in Java 21)

```java
// Preview feature — may change
String name = "Rahul";
int age = 28;

// String template (preview)
String message = STR."Hello \{name}, you are \{age} years old";
// "Hello Rahul, you are 28 years old"

// Multi-line
String json = STR."""
    {
        "name": "\{name}",
        "age": \{age}
    }
    """;
```

--

## 17. Key Topics & Explanations

### Topic 1: What are the main features introduced in Java 8?

**Answer:**
- Lambda expressions
- Functional interfaces
- Stream API
- Optional class
- Default and static methods in interfaces
- Method references
- New Date/Time API (`java.time`)
- Nashorn JavaScript engine

--

### Topic 2: What is the difference between `Predicate` and `Function`?

**Answer:** `Predicate<T>` takes one argument and returns `boolean` (`test()` method) — used for filtering/conditions. `Function<T, R>` takes one argument and returns a result of a different type (`apply()` method) — used for transformations/mapping.

--

### Topic 3: What is the difference between `map()` and `flatMap()` in Optional?

**Answer:**
- `map()` wraps the result in an Optional: `Optional<String>` → `Optional<Optional<String>>` if the function returns Optional.
- `flatMap()` flattens the result: `Optional<String>` → `Optional<String>` even if the function returns Optional.

```java
Optional<String> name = Optional.of("Rahul");
Optional<Optional<String>> mapped = name.map(n -> Optional.of(n.toUpperCase()));
Optional<String> flatMapped = name.flatMap(n -> Optional.of(n.toUpperCase()));
```

--

### Topic 4: What is effectively final?

**Answer:** A variable that is not declared `final` but is never reassigned after initialization. Lambdas can only capture local variables that are final or effectively final. This restriction exists because lambdas may execute on a different thread, and allowing mutation would cause concurrency issues.

--

### Topic 5: What is the difference between `orElse()` and `orElseGet()`?

**Answer:**
- `orElse(value)` — always evaluates the argument, even if Optional has a value.
- `orElseGet(supplier)` — evaluates the supplier only if Optional is empty (lazy).

Use `orElseGet()` when the default value is expensive to compute.

--

### Topic 6: Why was the new Date/Time API introduced?

**Answer:** The old `java.util.Date` had many problems: mutable (not thread-safe), poor API design (months start from 0, year from 1900), no timezone support, and `SimpleDateFormat` is not thread-safe. The new `java.time` API is immutable, thread-safe, has a clean fluent API, and proper timezone support.

--

### Topic 7: What is the difference between `var` and explicit type declaration?

**Answer:** `var` is just syntactic sugar — the compiler infers the type at compile time. There is **no runtime difference**. The inferred type is fixed and cannot change. `var` improves readability for complex generic types but should be avoided when the type is not obvious from the right-hand side.

--

### Topic 8: What are the four types of method references?

**Answer:**
1. **Static method:** `Integer::parseInt` → `s -> Integer.parseInt(s)`
2. **Instance method of particular object:** `System.out::println` → `s -> System.out.println(s)`
3. **Instance method of arbitrary object:** `String::toUpperCase` → `s -> s.toUpperCase()`
4. **Constructor:** `ArrayList::new` → `() -> new ArrayList<>()`

--

### Topic 9: What is the difference between `Stream.toList()` (Java 16) and `Collectors.toList()`?

**Answer:**
- `Stream.toList()` returns an **unmodifiable** list. Adding/removing elements throws `UnsupportedOperationException`.
- `Collectors.toList()` returns a **modifiable** `ArrayList`.

--

### Topic 10: What are virtual threads? (Java 21)

**Answer:** Virtual threads are lightweight threads managed by the JVM (not the OS). They allow creating millions of threads without the overhead of platform threads. Ideal for I/O-bound tasks (HTTP calls, DB queries). They use the same `Thread` API but are much cheaper to create and block. Created via `Thread.ofVirtual()` or `Executors.newVirtualThreadPerTaskExecutor()`.

--

### Topic 11: What is the difference between `Comparable` and `Comparator`?

**Answer:**
- `Comparable<T>` — defines natural ordering inside the class itself (`compareTo()` method). A class can have only one natural ordering.
- `Comparator<T>` — defines external ordering. Multiple comparators can exist for different sorting criteria.

```java
// Comparable — inside the class
class Employee implements Comparable<Employee> {
    public int compareTo(Employee other) { return this.name.compareTo(other.name); }
}

// Comparator — outside the class
Comparator<Employee> bySalary = Comparator.comparingDouble(Employee::getSalary);
```

--

### Topic 12: What is a default method conflict? How is it resolved?

**Answer:** When a class implements two interfaces that have the same default method, the compiler forces the class to override the method and explicitly choose which version to use (or provide its own implementation) using `InterfaceName.super.methodName()`.

--

## Version Summary Cheat Sheet

```
Java 8  (LTS) → Lambda, Streams, Optional, Functional Interfaces, Date/Time API
Java 9        → Modules, Collection.of(), Stream improvements, JShell
Java 10       → var (local variable type inference)
Java 11 (LTS) → String methods, HTTP Client, var in lambda, Files utility
Java 12       → Switch expressions (preview), Collectors.teeing()
Java 13       → Text blocks (preview)
Java 14       → Pattern matching instanceof (preview), Records (preview), Helpful NPE
Java 15       → Sealed classes (preview), Text blocks (standard)
Java 16       → Records (standard), Stream.toList(), instanceof pattern (standard)
Java 17 (LTS) → Sealed classes (standard), Pattern matching switch (preview)
Java 18       → Simple web server, UTF-8 default
Java 19       → Virtual threads (preview)
Java 20       → Scoped values (preview)
Java 21 (LTS) → Virtual threads (standard), Record patterns, Sequenced collections,
                 Pattern matching switch (standard), String templates (preview)
```

--

> **Tip:** For WITCH technical reviews, focus heavily on Java 8 features (Lambda, Streams, Optional, Functional Interfaces). Java 11 and 17 features are increasingly asked. Virtual threads (Java 21) is a hot topic for senior-level technical reviews.
