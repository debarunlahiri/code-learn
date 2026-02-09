# Java Stream API - Complete Guide (Basics to Advanced)

**Target:** Interview preparation for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Java Version:** 8+ (Stream API was introduced in Java 8)

---

## Table of Contents

1. [What is Stream API?](#1-what-is-stream-api)
2. [Stream vs Collection](#2-stream-vs-collection)
3. [Creating Streams](#3-creating-streams)
4. [Intermediate Operations](#4-intermediate-operations)
5. [Terminal Operations](#5-terminal-operations)
6. [Collectors in Depth](#6-collectors-in-depth)
7. [Primitive Streams](#7-primitive-streams)
8. [FlatMap - Flattening Nested Structures](#8-flatmap---flattening-nested-structures)
9. [Sorting with Streams](#9-sorting-with-streams)
10. [Grouping and Partitioning](#10-grouping-and-partitioning)
11. [Reducing and Summarizing](#11-reducing-and-summarizing)
12. [Optional with Streams](#12-optional-with-streams)
13. [Parallel Streams](#13-parallel-streams)
14. [Stream Pipeline Internals (Lazy Evaluation)](#14-stream-pipeline-internals-lazy-evaluation)
15. [Common Coding Problems Using Streams](#15-common-coding-problems-using-streams)
16. [Interview Questions & Answers](#16-interview-questions--answers)
17. [Common Mistakes & Best Practices](#17-common-mistakes--best-practices)

---

## 1. What is Stream API?

A **Stream** is a sequence of elements from a source that supports **aggregate operations** (like filter, map, reduce, etc.).

- Introduced in **Java 8** under `java.util.stream` package.
- It does **NOT store data** — it operates on the source (Collection, Array, I/O channel).
- It does **NOT modify the original data source** — it produces a new stream/result.
- It supports **lazy evaluation** and **short-circuit operations**.

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

List<String> names = Arrays.asList("Rahul", "Amit", "Sneha", "Priya");

// Filter names starting with 'A' and convert to uppercase
List<String> result = names.stream()
        .filter(name -> name.startsWith("A"))
        .map(String::toUpperCase)
        .collect(Collectors.toList());

System.out.println(result); // [AMIT]
```

### Key Characteristics

| Feature              | Description                                      |
|----------------------|--------------------------------------------------|
| **Not a data structure** | Does not store elements                      |
| **Functional style**     | Uses lambda expressions and method references|
| **Lazy**                 | Intermediate operations are lazy             |
| **Possibly unbounded**   | Can work with infinite streams               |
| **Consumable**           | A stream can be traversed only once          |

---

## 2. Stream vs Collection

| Feature            | Collection                     | Stream                              |
|--------------------|--------------------------------|-------------------------------------|
| **Storage**        | Stores all elements in memory  | Does NOT store elements             |
| **Iteration**      | External (for-each, iterator)  | Internal (handled by stream)        |
| **Traversal**      | Can be traversed multiple times| Can be traversed **only once**      |
| **Modification**   | Elements can be added/removed  | Cannot modify the source            |
| **Lazy**           | No                             | Yes (intermediate ops are lazy)     |
| **Parallelism**    | Manual (threads)               | Built-in (`parallelStream()`)       |

```java
// Collection - External Iteration
for (String name : names) {
    System.out.println(name);
}

// Stream - Internal Iteration
names.stream().forEach(System.out::println);
```

---

## 3. Creating Streams

### 3.1 From Collection

```java
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream = list.stream();
```

### 3.2 From Array

```java
String[] arr = {"a", "b", "c"};
Stream<String> stream = Arrays.stream(arr);
```

### 3.3 Using Stream.of()

```java
Stream<String> stream = Stream.of("a", "b", "c");
```

### 3.4 Using Stream.empty()

```java
Stream<String> emptyStream = Stream.empty();
```

### 3.5 Using Stream.builder()

```java
Stream<String> stream = Stream.<String>builder()
        .add("a")
        .add("b")
        .add("c")
        .build();
```

### 3.6 Using Stream.generate() — Infinite Stream

```java
// Generates infinite stream of random numbers
Stream<Double> randoms = Stream.generate(Math::random);

// Always use limit() with generate
randoms.limit(5).forEach(System.out::println);
```

### 3.7 Using Stream.iterate() — Infinite Stream

```java
// 0, 2, 4, 6, 8, ...
Stream<Integer> evenNumbers = Stream.iterate(0, n -> n + 2);
evenNumbers.limit(10).forEach(System.out::println);

// Java 9+ : iterate with predicate (bounded)
Stream<Integer> bounded = Stream.iterate(0, n -> n < 20, n -> n + 2);
```

### 3.8 From String (chars)

```java
IntStream charStream = "Hello".chars();
charStream.forEach(c -> System.out.print((char) c + " ")); // H e l l o
```

### 3.9 From File (Java NIO)

```java
import java.nio.file.Files;
import java.nio.file.Paths;

Stream<String> lines = Files.lines(Paths.get("data.txt"));
lines.forEach(System.out::println);
lines.close(); // Always close file streams
```

### 3.10 Using IntStream.range() / rangeClosed()

```java
IntStream range = IntStream.range(1, 5);       // 1, 2, 3, 4
IntStream rangeClosed = IntStream.rangeClosed(1, 5); // 1, 2, 3, 4, 5
```

---

## 4. Intermediate Operations

Intermediate operations return a **new stream** and are **lazy** — they don't execute until a terminal operation is called.

### 4.1 filter(Predicate)

Filters elements based on a condition.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Get even numbers
List<Integer> evens = numbers.stream()
        .filter(n -> n % 2 == 0)
        .collect(Collectors.toList());
// [2, 4, 6, 8, 10]
```

### 4.2 map(Function)

Transforms each element to another form.

```java
List<String> names = Arrays.asList("rahul", "amit", "sneha");

List<String> upperNames = names.stream()
        .map(String::toUpperCase)
        .collect(Collectors.toList());
// [RAHUL, AMIT, SNEHA]

// Map to length of each name
List<Integer> lengths = names.stream()
        .map(String::length)
        .collect(Collectors.toList());
// [5, 4, 5]
```

### 4.3 flatMap(Function)

Flattens nested structures (covered in detail in [Section 8](#8-flatmap---flattening-nested-structures)).

```java
List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2, 3),
    Arrays.asList(4, 5, 6),
    Arrays.asList(7, 8, 9)
);

List<Integer> flat = nested.stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
// [1, 2, 3, 4, 5, 6, 7, 8, 9]
```

### 4.4 distinct()

Removes duplicate elements (uses `equals()` and `hashCode()`).

```java
List<Integer> nums = Arrays.asList(1, 2, 2, 3, 3, 3, 4);

List<Integer> unique = nums.stream()
        .distinct()
        .collect(Collectors.toList());
// [1, 2, 3, 4]
```

### 4.5 sorted()

Sorts elements in natural order or using a Comparator.

```java
List<Integer> nums = Arrays.asList(5, 3, 1, 4, 2);

// Natural order
List<Integer> sorted = nums.stream()
        .sorted()
        .collect(Collectors.toList());
// [1, 2, 3, 4, 5]

// Reverse order
List<Integer> reversed = nums.stream()
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());
// [5, 4, 3, 2, 1]
```

### 4.6 peek(Consumer)

Performs an action on each element **without modifying** the stream. Useful for debugging.

```java
List<String> result = names.stream()
        .filter(name -> name.length() > 4)
        .peek(name -> System.out.println("Filtered: " + name))
        .map(String::toUpperCase)
        .peek(name -> System.out.println("Mapped: " + name))
        .collect(Collectors.toList());
```

### 4.7 limit(long n)

Returns a stream with at most `n` elements.

```java
List<Integer> first3 = numbers.stream()
        .limit(3)
        .collect(Collectors.toList());
// [1, 2, 3]
```

### 4.8 skip(long n)

Skips the first `n` elements.

```java
List<Integer> skipped = numbers.stream()
        .skip(5)
        .collect(Collectors.toList());
// [6, 7, 8, 9, 10]
```

### 4.9 Combining limit and skip (Pagination)

```java
int pageSize = 3;
int pageNumber = 2; // 0-indexed

List<Integer> page = numbers.stream()
        .skip((long) pageNumber * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
// [7, 8, 9]
```

### 4.10 mapToInt / mapToLong / mapToDouble

Converts to primitive streams to avoid boxing overhead.

```java
int totalLength = names.stream()
        .mapToInt(String::length)
        .sum();
```

### 4.11 takeWhile(Predicate) — Java 9+

Takes elements **while** the predicate is true, stops at first false.

```java
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);

List<Integer> result = nums.stream()
        .takeWhile(n -> n < 4)
        .collect(Collectors.toList());
// [1, 2, 3]
```

### 4.12 dropWhile(Predicate) — Java 9+

Drops elements **while** the predicate is true, keeps the rest.

```java
List<Integer> result = nums.stream()
        .dropWhile(n -> n < 4)
        .collect(Collectors.toList());
// [4, 5, 6]
```

---

## 5. Terminal Operations

Terminal operations **trigger the stream pipeline** and produce a result or side-effect.

### 5.1 forEach(Consumer)

```java
names.stream().forEach(System.out::println);

// On collections directly (not a stream operation)
names.forEach(System.out::println);
```

### 5.2 forEachOrdered(Consumer)

Guarantees order even in parallel streams.

```java
names.parallelStream().forEachOrdered(System.out::println);
```

### 5.3 collect(Collector)

The most commonly used terminal operation. Covered in detail in [Section 6](#6-collectors-in-depth).

```java
List<String> list = names.stream()
        .filter(n -> n.length() > 3)
        .collect(Collectors.toList());
```

### 5.4 toArray()

```java
String[] arr = names.stream().toArray(String[]::new);
```

### 5.5 count()

```java
long count = names.stream()
        .filter(n -> n.startsWith("A"))
        .count();
```

### 5.6 reduce()

Combines elements into a single result.

```java
// Sum of all numbers
int sum = numbers.stream()
        .reduce(0, Integer::sum);

// With Optional (no identity)
Optional<Integer> sum2 = numbers.stream()
        .reduce(Integer::sum);

// Find max
Optional<Integer> max = numbers.stream()
        .reduce(Integer::max);

// Concatenate strings
String joined = names.stream()
        .reduce("", (a, b) -> a + " " + b)
        .trim();
```

### 5.7 min() and max()

```java
Optional<Integer> min = numbers.stream().min(Comparator.naturalOrder());
Optional<Integer> max = numbers.stream().max(Comparator.naturalOrder());

Optional<String> shortest = names.stream()
        .min(Comparator.comparingInt(String::length));
```

### 5.8 findFirst() and findAny()

```java
Optional<String> first = names.stream()
        .filter(n -> n.startsWith("S"))
        .findFirst();

// findAny() is non-deterministic, useful in parallel streams
Optional<String> any = names.parallelStream()
        .filter(n -> n.startsWith("S"))
        .findAny();
```

### 5.9 anyMatch(), allMatch(), noneMatch()

Short-circuit operations — they don't process all elements if result is determined early.

```java
boolean anyStartsWithA = names.stream().anyMatch(n -> n.startsWith("A"));   // true
boolean allLong = names.stream().allMatch(n -> n.length() > 3);             // depends
boolean noneEmpty = names.stream().noneMatch(String::isEmpty);              // true
```

---

## 6. Collectors in Depth

`Collectors` is a utility class with static factory methods for common collection operations.

### 6.1 toList(), toSet(), toMap()

```java
// toList
List<String> list = names.stream().collect(Collectors.toList());

// toUnmodifiableList (Java 10+)
List<String> unmodifiable = names.stream().collect(Collectors.toUnmodifiableList());

// toSet
Set<String> set = names.stream().collect(Collectors.toSet());

// toMap
Map<String, Integer> nameToLength = names.stream()
        .collect(Collectors.toMap(
            name -> name,           // key mapper
            String::length          // value mapper
        ));

// toMap with merge function (handles duplicate keys)
Map<Integer, String> lengthToName = names.stream()
        .collect(Collectors.toMap(
            String::length,
            name -> name,
            (existing, replacement) -> existing + ", " + replacement
        ));
```

### 6.2 joining()

```java
String joined = names.stream()
        .collect(Collectors.joining());
// "RahulAmitSnehaPriya"

String joinedWithDelimiter = names.stream()
        .collect(Collectors.joining(", "));
// "Rahul, Amit, Sneha, Priya"

String joinedWithPrefixSuffix = names.stream()
        .collect(Collectors.joining(", ", "[", "]"));
// "[Rahul, Amit, Sneha, Priya]"
```

### 6.3 counting()

```java
long count = names.stream()
        .collect(Collectors.counting());
```

### 6.4 summarizingInt / summarizingDouble / summarizingLong

```java
IntSummaryStatistics stats = names.stream()
        .collect(Collectors.summarizingInt(String::length));

System.out.println("Count: " + stats.getCount());
System.out.println("Sum: " + stats.getSum());
System.out.println("Min: " + stats.getMin());
System.out.println("Max: " + stats.getMax());
System.out.println("Average: " + stats.getAverage());
```

### 6.5 averagingInt / averagingDouble

```java
double avgLength = names.stream()
        .collect(Collectors.averagingInt(String::length));
```

### 6.6 toCollection()

Collect into a specific collection type.

```java
LinkedList<String> linkedList = names.stream()
        .collect(Collectors.toCollection(LinkedList::new));

TreeSet<String> treeSet = names.stream()
        .collect(Collectors.toCollection(TreeSet::new));
```

### 6.7 collectingAndThen()

Perform a final transformation after collecting.

```java
List<String> unmodifiableList = names.stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toList(),
            Collections::unmodifiableList
        ));
```

---

## 7. Primitive Streams

Java provides specialized streams for primitives to avoid **autoboxing overhead**.

| Stream Type   | For Primitive | Wrapper Equivalent |
|---------------|---------------|--------------------|
| `IntStream`   | `int`         | `Stream<Integer>`  |
| `LongStream`  | `long`        | `Stream<Long>`     |
| `DoubleStream`| `double`      | `Stream<Double>`   |

### 7.1 Creating Primitive Streams

```java
IntStream intStream = IntStream.of(1, 2, 3, 4, 5);
IntStream range = IntStream.range(1, 10);          // 1 to 9
IntStream rangeClosed = IntStream.rangeClosed(1, 10); // 1 to 10

LongStream longStream = LongStream.of(100L, 200L, 300L);
DoubleStream doubleStream = DoubleStream.of(1.1, 2.2, 3.3);
```

### 7.2 Useful Methods on Primitive Streams

```java
int sum = IntStream.rangeClosed(1, 100).sum();                    // 5050
OptionalInt max = IntStream.of(3, 1, 4, 1, 5).max();             // 5
OptionalDouble avg = IntStream.rangeClosed(1, 10).average();      // 5.5
IntSummaryStatistics stats = IntStream.rangeClosed(1, 10).summaryStatistics();
```

### 7.3 Boxing and Unboxing

```java
// Primitive to Wrapper (boxing)
Stream<Integer> boxed = IntStream.rangeClosed(1, 5).boxed();

// Wrapper to Primitive (unboxing)
IntStream unboxed = Arrays.asList(1, 2, 3).stream().mapToInt(Integer::intValue);
```

---

## 8. FlatMap - Flattening Nested Structures

`flatMap` is one of the most important and frequently asked topics.

### 8.1 Basic FlatMap

```java
// Problem: List of lists -> single flat list
List<List<String>> departments = Arrays.asList(
    Arrays.asList("Rahul", "Amit"),
    Arrays.asList("Sneha", "Priya"),
    Arrays.asList("Karan", "Neha")
);

List<String> allEmployees = departments.stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
// [Rahul, Amit, Sneha, Priya, Karan, Neha]
```

### 8.2 FlatMap with Strings

```java
// Split sentences into words
List<String> sentences = Arrays.asList(
    "Java is great",
    "Streams are powerful"
);

List<String> words = sentences.stream()
        .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
        .collect(Collectors.toList());
// [Java, is, great, Streams, are, powerful]
```

### 8.3 FlatMap with Objects

```java
class Student {
    String name;
    List<String> subjects;
    // constructor, getters
}

List<Student> students = Arrays.asList(
    new Student("Rahul", Arrays.asList("Math", "Science")),
    new Student("Amit", Arrays.asList("English", "Math")),
    new Student("Sneha", Arrays.asList("Science", "History"))
);

// Get all unique subjects
Set<String> allSubjects = students.stream()
        .flatMap(student -> student.getSubjects().stream())
        .collect(Collectors.toSet());
// [Math, Science, English, History]
```

### 8.4 map() vs flatMap()

| `map()`                                    | `flatMap()`                                   |
|--------------------------------------------|-----------------------------------------------|
| One-to-one transformation                  | One-to-many transformation                    |
| Returns `Stream<Stream<T>>` for nested     | Returns `Stream<T>` (flattened)               |
| Does NOT flatten                           | Flattens the result                           |

```java
// map produces Stream<Stream<String>>
Stream<Stream<String>> mapped = departments.stream()
        .map(List::stream);

// flatMap produces Stream<String>
Stream<String> flatMapped = departments.stream()
        .flatMap(List::stream);
```

---

## 9. Sorting with Streams

### 9.1 Natural Order

```java
List<String> sorted = names.stream()
        .sorted()
        .collect(Collectors.toList());
```

### 9.2 Reverse Order

```java
List<String> reversed = names.stream()
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());
```

### 9.3 Sort by Property

```java
class Employee {
    String name;
    int age;
    double salary;
    String department;
    // constructor, getters
}

List<Employee> employees = Arrays.asList(
    new Employee("Rahul", 28, 50000, "IT"),
    new Employee("Amit", 25, 45000, "HR"),
    new Employee("Sneha", 30, 60000, "IT"),
    new Employee("Priya", 27, 55000, "Finance")
);

// Sort by age
List<Employee> byAge = employees.stream()
        .sorted(Comparator.comparingInt(Employee::getAge))
        .collect(Collectors.toList());

// Sort by salary descending
List<Employee> bySalaryDesc = employees.stream()
        .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
        .collect(Collectors.toList());

// Sort by department, then by name
List<Employee> multiSort = employees.stream()
        .sorted(Comparator.comparing(Employee::getDepartment)
                .thenComparing(Employee::getName))
        .collect(Collectors.toList());

// Sort by name length
List<Employee> byNameLength = employees.stream()
        .sorted(Comparator.comparingInt(e -> e.getName().length()))
        .collect(Collectors.toList());
```

### 9.4 Null-safe Sorting

```java
List<String> withNulls = Arrays.asList("Rahul", null, "Amit", null, "Sneha");

List<String> sorted = withNulls.stream()
        .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
        .collect(Collectors.toList());
// [null, null, Amit, Rahul, Sneha]

List<String> sorted2 = withNulls.stream()
        .sorted(Comparator.nullsLast(Comparator.naturalOrder()))
        .collect(Collectors.toList());
// [Amit, Rahul, Sneha, null, null]
```

---

## 10. Grouping and Partitioning

### 10.1 groupingBy — Single Level

```java
// Group employees by department
Map<String, List<Employee>> byDept = employees.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment));
// {IT=[Rahul, Sneha], HR=[Amit], Finance=[Priya]}
```

### 10.2 groupingBy — With Downstream Collector

```java
// Count employees per department
Map<String, Long> countByDept = employees.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.counting()
        ));
// {IT=2, HR=1, Finance=1}

// Average salary per department
Map<String, Double> avgSalaryByDept = employees.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.averagingDouble(Employee::getSalary)
        ));

// Max salary employee per department
Map<String, Optional<Employee>> topEarnerByDept = employees.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))
        ));

// Names per department
Map<String, List<String>> namesByDept = employees.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.mapping(Employee::getName, Collectors.toList())
        ));
```

### 10.3 groupingBy — Multi-Level (Nested Grouping)

```java
// Group by department, then by age group
Map<String, Map<String, List<Employee>>> nested = employees.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.groupingBy(e -> e.getAge() > 27 ? "Senior" : "Junior")
        ));
```

### 10.4 partitioningBy

Splits into exactly **two groups** based on a predicate (true/false).

```java
// Partition into high salary (>50000) and low salary
Map<Boolean, List<Employee>> partitioned = employees.stream()
        .collect(Collectors.partitioningBy(e -> e.getSalary() > 50000));

List<Employee> highSalary = partitioned.get(true);
List<Employee> lowSalary = partitioned.get(false);

// Partition with downstream collector
Map<Boolean, Long> countPartitioned = employees.stream()
        .collect(Collectors.partitioningBy(
            e -> e.getSalary() > 50000,
            Collectors.counting()
        ));
```

### 10.5 groupingBy vs partitioningBy

| `groupingBy`                          | `partitioningBy`                      |
|---------------------------------------|---------------------------------------|
| Groups by any classifier function     | Groups by a predicate (true/false)    |
| Can have N groups                     | Always exactly 2 groups               |
| Key type is the classifier's return   | Key type is always `Boolean`          |

---

## 11. Reducing and Summarizing

### 11.1 reduce() — Three Forms

```java
// Form 1: With identity and accumulator
int sum = numbers.stream().reduce(0, Integer::sum);

// Form 2: Without identity (returns Optional)
Optional<Integer> sum2 = numbers.stream().reduce(Integer::sum);

// Form 3: With identity, accumulator, and combiner (for parallel streams)
int sum3 = numbers.parallelStream().reduce(0, Integer::sum, Integer::sum);
```

### 11.2 How reduce() Works Internally

```
reduce(0, Integer::sum) on [1, 2, 3, 4, 5]

Step 1: 0 + 1 = 1
Step 2: 1 + 2 = 3
Step 3: 3 + 3 = 6
Step 4: 6 + 4 = 10
Step 5: 10 + 5 = 15

Result: 15
```

### 11.3 Custom Reduce Examples

```java
// Find longest string
Optional<String> longest = names.stream()
        .reduce((a, b) -> a.length() >= b.length() ? a : b);

// Multiply all numbers
int product = numbers.stream()
        .reduce(1, (a, b) -> a * b);

// Build a comma-separated string
String csv = names.stream()
        .reduce((a, b) -> a + ", " + b)
        .orElse("");
```

### 11.4 Summarizing Statistics

```java
DoubleSummaryStatistics salaryStats = employees.stream()
        .collect(Collectors.summarizingDouble(Employee::getSalary));

System.out.println("Total Salary: " + salaryStats.getSum());
System.out.println("Avg Salary: " + salaryStats.getAverage());
System.out.println("Max Salary: " + salaryStats.getMax());
System.out.println("Min Salary: " + salaryStats.getMin());
System.out.println("Count: " + salaryStats.getCount());
```

---

## 12. Optional with Streams

`Optional<T>` is a container that may or may not contain a value. Streams return `Optional` from many terminal operations.

### 12.1 Creating Optional

```java
Optional<String> empty = Optional.empty();
Optional<String> present = Optional.of("Hello");
Optional<String> nullable = Optional.ofNullable(null); // won't throw NPE
```

### 12.2 Using Optional

```java
Optional<Employee> topEarner = employees.stream()
        .max(Comparator.comparingDouble(Employee::getSalary));

// isPresent + get (old style, avoid)
if (topEarner.isPresent()) {
    System.out.println(topEarner.get().getName());
}

// ifPresent (preferred)
topEarner.ifPresent(e -> System.out.println(e.getName()));

// orElse
String name = topEarner.map(Employee::getName).orElse("No employee found");

// orElseGet (lazy evaluation)
String name2 = topEarner.map(Employee::getName)
        .orElseGet(() -> "Default Name");

// orElseThrow
String name3 = topEarner.map(Employee::getName)
        .orElseThrow(() -> new RuntimeException("No employee found"));

// Java 11: isEmpty()
boolean empty = topEarner.isEmpty();
```

### 12.3 Optional as a Stream (Java 9+)

```java
// Optional.stream() converts Optional to a Stream of 0 or 1 elements
long count = Optional.of("Hello").stream().count(); // 1
long count2 = Optional.empty().stream().count();    // 0

// Useful with flatMap
List<Optional<String>> optionals = Arrays.asList(
    Optional.of("A"), Optional.empty(), Optional.of("B")
);

List<String> values = optionals.stream()
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
// [A, B]
```

---

## 13. Parallel Streams

Parallel streams split the source into multiple chunks and process them on different threads using the **ForkJoinPool**.

### 13.1 Creating Parallel Streams

```java
// Method 1: From collection
Stream<String> parallel1 = names.parallelStream();

// Method 2: Convert sequential to parallel
Stream<String> parallel2 = names.stream().parallel();
```

### 13.2 When to Use Parallel Streams

**USE when:**
- Large dataset (10,000+ elements)
- CPU-intensive operations (not I/O bound)
- Stateless, independent operations
- No shared mutable state
- Source is easily splittable (ArrayList, arrays)

**AVOID when:**
- Small datasets (overhead > benefit)
- Order matters (use `forEachOrdered` if needed)
- Operations have side effects
- I/O bound operations (DB calls, file I/O)
- Source is hard to split (LinkedList, Stream.iterate)

### 13.3 Example

```java
// Sequential
long start1 = System.currentTimeMillis();
long sum1 = LongStream.rangeClosed(1, 100_000_000).sum();
long time1 = System.currentTimeMillis() - start1;

// Parallel
long start2 = System.currentTimeMillis();
long sum2 = LongStream.rangeClosed(1, 100_000_000).parallel().sum();
long time2 = System.currentTimeMillis() - start2;

System.out.println("Sequential: " + time1 + "ms");
System.out.println("Parallel: " + time2 + "ms");
```

### 13.4 Thread Safety Issue

```java
// WRONG - shared mutable state
List<Integer> result = new ArrayList<>();
IntStream.rangeClosed(1, 1000).parallel().forEach(result::add); // NOT thread-safe!

// CORRECT - use collect
List<Integer> result = IntStream.rangeClosed(1, 1000)
        .parallel()
        .boxed()
        .collect(Collectors.toList());

// CORRECT - use synchronized collection
List<Integer> syncResult = Collections.synchronizedList(new ArrayList<>());
IntStream.rangeClosed(1, 1000).parallel().forEach(syncResult::add);
```

### 13.5 Custom Thread Pool for Parallel Streams

```java
ForkJoinPool customPool = new ForkJoinPool(4); // 4 threads

List<Integer> result = customPool.submit(() ->
    numbers.parallelStream()
        .filter(n -> n % 2 == 0)
        .collect(Collectors.toList())
).get();

customPool.shutdown();
```

---

## 14. Stream Pipeline Internals (Lazy Evaluation)

### 14.1 How Lazy Evaluation Works

```java
List<String> names = Arrays.asList("Rahul", "Amit", "Sneha", "Priya", "Karan");

// Nothing happens here - all intermediate operations are lazy
Stream<String> pipeline = names.stream()
        .filter(name -> {
            System.out.println("filter: " + name);
            return name.length() > 4;
        })
        .map(name -> {
            System.out.println("map: " + name);
            return name.toUpperCase();
        });

System.out.println("Pipeline created, no processing yet!");

// Processing happens only when terminal operation is called
List<String> result = pipeline.collect(Collectors.toList());
```

**Output:**
```
Pipeline created, no processing yet!
filter: Rahul
map: Rahul
filter: Amit
filter: Sneha
map: Sneha
filter: Priya
map: Priya
filter: Karan
map: Karan
```

Notice: Elements are processed **one by one** through the entire pipeline (not filter all, then map all).

### 14.2 Short-Circuit Operations

These operations don't need to process all elements.

```java
// findFirst stops after finding the first match
Optional<String> first = names.stream()
        .filter(name -> {
            System.out.println("Checking: " + name);
            return name.startsWith("S");
        })
        .findFirst();

// Output:
// Checking: Rahul
// Checking: Amit
// Checking: Sneha    <-- stops here, doesn't check Priya and Karan
```

Short-circuit operations: `findFirst()`, `findAny()`, `anyMatch()`, `allMatch()`, `noneMatch()`, `limit()`

### 14.3 Stream Cannot Be Reused

```java
Stream<String> stream = names.stream().filter(n -> n.length() > 4);

stream.forEach(System.out::println); // Works fine

stream.forEach(System.out::println); // IllegalStateException: stream has already been operated upon
```

---

## 15. Common Coding Problems Using Streams

These are the most frequently asked coding problems in WITCH company interviews.

### Problem 1: Find the second highest number

```java
List<Integer> numbers = Arrays.asList(5, 9, 11, 2, 8, 21, 1);

Optional<Integer> secondHighest = numbers.stream()
        .sorted(Comparator.reverseOrder())
        .distinct()
        .skip(1)
        .findFirst();

System.out.println(secondHighest.orElse(-1)); // 11
```

### Problem 2: Find duplicate elements

```java
List<Integer> nums = Arrays.asList(1, 2, 3, 2, 4, 5, 3, 6, 1);

Set<Integer> seen = new HashSet<>();
List<Integer> duplicates = nums.stream()
        .filter(n -> !seen.add(n))
        .collect(Collectors.toList());
// [2, 3, 1]

// Alternative using groupingBy
List<Integer> duplicates2 = nums.stream()
        .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() > 1)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
```

### Problem 3: Find first non-repeated character in a string

```java
String input = "aabbcdeff";

Character result = input.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() == 1)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);

System.out.println(result); // c
```

### Problem 4: Count occurrences of each character

```java
String str = "programming";

Map<Character, Long> charCount = str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

System.out.println(charCount);
// {p=1, r=2, o=1, g=2, a=1, m=2, i=1, n=1}
```

### Problem 5: Find the employee with the highest salary in each department

```java
Map<String, Optional<Employee>> topEarners = employees.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))
        ));
```

### Problem 6: Separate odd and even numbers

```java
Map<Boolean, List<Integer>> oddEven = numbers.stream()
        .collect(Collectors.partitioningBy(n -> n % 2 == 0));

List<Integer> evens = oddEven.get(true);
List<Integer> odds = oddEven.get(false);
```

### Problem 7: Find the sum of all digits in a string

```java
String input = "abc123def456";

int digitSum = input.chars()
        .filter(Character::isDigit)
        .map(c -> c - '0')
        .sum();

System.out.println(digitSum); // 1+2+3+4+5+6 = 21
```

### Problem 8: Merge two sorted lists into a single sorted list

```java
List<Integer> list1 = Arrays.asList(1, 3, 5, 7);
List<Integer> list2 = Arrays.asList(2, 4, 6, 8);

List<Integer> merged = Stream.concat(list1.stream(), list2.stream())
        .sorted()
        .collect(Collectors.toList());
// [1, 2, 3, 4, 5, 6, 7, 8]
```

### Problem 9: Convert list of strings to a map (name -> length)

```java
Map<String, Integer> nameLength = names.stream()
        .collect(Collectors.toMap(
            name -> name,
            String::length
        ));
```

### Problem 10: Find the longest string in a list

```java
Optional<String> longest = names.stream()
        .max(Comparator.comparingInt(String::length));
```

### Problem 11: Reverse each word in a sentence

```java
String sentence = "Java Streams Are Powerful";

String reversed = Arrays.stream(sentence.split(" "))
        .map(word -> new StringBuilder(word).reverse().toString())
        .collect(Collectors.joining(" "));

System.out.println(reversed); // "avaJ smaertS erA lufrewoP"
```

### Problem 12: Find common elements between two lists

```java
List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> list2 = Arrays.asList(3, 4, 5, 6, 7);

List<Integer> common = list1.stream()
        .filter(list2::contains)
        .collect(Collectors.toList());
// [3, 4, 5]
```

### Problem 13: Frequency map — most repeated element

```java
List<String> words = Arrays.asList("apple", "banana", "apple", "cherry", "banana", "apple");

Optional<Map.Entry<String, Long>> mostFrequent = words.stream()
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue());

mostFrequent.ifPresent(e ->
    System.out.println(e.getKey() + " -> " + e.getValue())); // apple -> 3
```

### Problem 14: Fibonacci series using Stream

```java
Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0] + f[1]})
        .limit(10)
        .map(f -> f[0])
        .forEach(System.out::println);
// 0, 1, 1, 2, 3, 5, 8, 13, 21, 34
```

### Problem 15: Check if a number is prime using Stream

```java
boolean isPrime(int n) {
    if (n <= 1) return false;
    return IntStream.rangeClosed(2, (int) Math.sqrt(n))
            .noneMatch(i -> n % i == 0);
}

// Find all primes between 1 and 100
List<Integer> primes = IntStream.rangeClosed(1, 100)
        .filter(this::isPrime)
        .boxed()
        .collect(Collectors.toList());
```

### Problem 16: Flatten a map's values and collect

```java
Map<String, List<String>> deptEmployees = new HashMap<>();
deptEmployees.put("IT", Arrays.asList("Rahul", "Sneha"));
deptEmployees.put("HR", Arrays.asList("Amit", "Priya"));

List<String> allEmployees = deptEmployees.values().stream()
        .flatMap(List::stream)
        .sorted()
        .collect(Collectors.toList());
// [Amit, Priya, Rahul, Sneha]
```

### Problem 17: Group strings by their first character

```java
List<String> words = Arrays.asList("apple", "avocado", "banana", "blueberry", "cherry");

Map<Character, List<String>> grouped = words.stream()
        .collect(Collectors.groupingBy(w -> w.charAt(0)));
// {a=[apple, avocado], b=[banana, blueberry], c=[cherry]}
```

### Problem 18: Convert a list of integers to a comma-separated string

```java
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

String csv = nums.stream()
        .map(String::valueOf)
        .collect(Collectors.joining(", "));
// "1, 2, 3, 4, 5"
```

### Problem 19: Find the Kth largest element

```java
int k = 3;
Optional<Integer> kthLargest = numbers.stream()
        .sorted(Comparator.reverseOrder())
        .distinct()
        .skip(k - 1)
        .findFirst();
```

### Problem 20: Palindrome check using streams

```java
boolean isPalindrome(String str) {
    return IntStream.range(0, str.length() / 2)
            .allMatch(i -> str.charAt(i) == str.charAt(str.length() - 1 - i));
}
```

---

## 16. Interview Questions & Answers

### Q1: What is the difference between `map()` and `flatMap()`?

**Answer:** `map()` applies a function to each element and wraps the result in a stream (one-to-one). `flatMap()` applies a function that returns a stream for each element and then flattens all the streams into a single stream (one-to-many). Use `flatMap()` when each element maps to multiple elements (e.g., list of lists).

---

### Q2: What is lazy evaluation in streams?

**Answer:** Intermediate operations (filter, map, sorted, etc.) are not executed immediately. They are only executed when a terminal operation (collect, forEach, count, etc.) is invoked. This allows the stream to optimize the pipeline — for example, if `findFirst()` is used, the stream stops processing after finding the first match.

---

### Q3: Can we reuse a stream?

**Answer:** No. A stream can be consumed only once. Calling a terminal operation on an already consumed stream throws `IllegalStateException`. You need to create a new stream from the source.

---

### Q4: What is the difference between `findFirst()` and `findAny()`?

**Answer:**
- `findFirst()` returns the **first element** in encounter order. Deterministic.
- `findAny()` returns **any element** and is non-deterministic. It performs better in **parallel streams** because it doesn't need to maintain order.

---

### Q5: What is the difference between `Collection.stream()` and `Collection.parallelStream()`?

**Answer:** `stream()` creates a sequential stream that processes elements one by one on a single thread. `parallelStream()` creates a parallel stream that splits the data and processes chunks on multiple threads using the ForkJoinPool. Parallel streams are faster for large datasets with CPU-intensive operations but have overhead for small datasets.

---

### Q6: What is the difference between `reduce()` and `collect()`?

**Answer:**
- `reduce()` combines elements into a single immutable value (sum, max, concatenation). It creates new intermediate values at each step.
- `collect()` is a mutable reduction — it accumulates elements into a mutable container (List, Set, Map, StringBuilder). It's more efficient for building collections.

---

### Q7: What happens if `toMap()` encounters duplicate keys?

**Answer:** It throws `IllegalStateException`. To handle duplicates, provide a merge function:
```java
Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> v1) // keep first
```

---

### Q8: What is the difference between `peek()` and `map()`?

**Answer:**
- `peek()` takes a `Consumer` (returns void) — used for side effects like logging. It does not transform elements.
- `map()` takes a `Function` — transforms each element to a new value.

---

### Q9: How does `sorted()` work in a stream pipeline?

**Answer:** `sorted()` is a **stateful intermediate operation**. It needs to see all elements before it can sort, so it acts as a barrier in the pipeline. It buffers all elements, sorts them, and then passes them downstream. This breaks the one-element-at-a-time processing pattern.

---

### Q10: What is the difference between `Stream.of()` and `Arrays.stream()`?

**Answer:**
- `Stream.of(array)` treats the entire array as a single element (creates `Stream<int[]>` for primitive arrays).
- `Arrays.stream(array)` properly streams the array elements. For primitive arrays, it returns `IntStream`, `LongStream`, or `DoubleStream`.

```java
int[] arr = {1, 2, 3};
Stream.of(arr);         // Stream<int[]> — single element!
Arrays.stream(arr);     // IntStream — three elements
```

---

### Q11: What are stateful vs stateless intermediate operations?

**Answer:**
- **Stateless:** Each element is processed independently — `filter()`, `map()`, `flatMap()`, `peek()`
- **Stateful:** Need to know about other elements — `sorted()`, `distinct()`, `limit()`, `skip()`

Stateful operations may need to process the entire stream before producing results and can be less efficient in parallel streams.

---

### Q12: Why should we avoid using `forEach()` to collect results?

**Answer:** Using `forEach()` with an external mutable collection is not thread-safe in parallel streams and is considered bad practice. Use `collect()` instead, which is designed for mutable reduction and handles parallel execution correctly.

```java
// BAD
List<String> result = new ArrayList<>();
stream.forEach(result::add);

// GOOD
List<String> result = stream.collect(Collectors.toList());
```

---

### Q13: What is `Collectors.teeing()`? (Java 12+)

**Answer:** `teeing()` allows you to apply two collectors simultaneously and merge their results.

```java
// Find both min and max in a single pass
var result = numbers.stream()
        .collect(Collectors.teeing(
            Collectors.minBy(Comparator.naturalOrder()),
            Collectors.maxBy(Comparator.naturalOrder()),
            (min, max) -> "Min: " + min.orElse(0) + ", Max: " + max.orElse(0)
        ));
```

---

### Q14: What is the default thread pool size for parallel streams?

**Answer:** Parallel streams use the common `ForkJoinPool` with a default parallelism level of `Runtime.getRuntime().availableProcessors() - 1`. You can change it with:
```java
System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
```
Or use a custom ForkJoinPool.

---

### Q15: What is the difference between `Stream.iterate()` and `Stream.generate()`?

**Answer:**
- `iterate(seed, unaryOperator)` produces a sequence where each element depends on the previous one (e.g., 0, 2, 4, 6...).
- `generate(supplier)` produces elements independently — each call to the supplier is independent (e.g., random numbers).

---

## 17. Common Mistakes & Best Practices

### Mistake 1: Modifying source during stream processing

```java
// WRONG - ConcurrentModificationException
List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C"));
names.stream().forEach(name -> {
    if (name.equals("B")) names.remove(name); // DON'T DO THIS
});

// CORRECT
List<String> filtered = names.stream()
        .filter(name -> !name.equals("B"))
        .collect(Collectors.toList());
```

### Mistake 2: Using streams for simple iterations

```java
// Overkill - just use a for loop
names.stream().forEach(System.out::println);

// Better for simple cases
for (String name : names) {
    System.out.println(name);
}
```

### Mistake 3: Ignoring return value of intermediate operations

```java
// WRONG - filter returns a new stream, original is unchanged
names.stream().filter(n -> n.length() > 4); // result is lost!

// CORRECT
List<String> result = names.stream()
        .filter(n -> n.length() > 4)
        .collect(Collectors.toList());
```

### Mistake 4: Using parallel streams everywhere

```java
// DON'T use parallel for small collections or I/O operations
smallList.parallelStream().forEach(item -> saveToDatabase(item)); // BAD

// Use sequential for I/O
smallList.stream().forEach(item -> saveToDatabase(item)); // BETTER
```

### Best Practices Summary

| Practice | Description |
|----------|-------------|
| **Prefer `collect()` over `forEach()`** | For building results, always use collect |
| **Use method references** | `String::toUpperCase` instead of `s -> s.toUpperCase()` |
| **Avoid side effects** | Stream operations should be stateless |
| **Use primitive streams** | `IntStream`, `LongStream` to avoid boxing |
| **Close file-based streams** | Use try-with-resources for `Files.lines()` |
| **Keep lambdas short** | Extract complex logic into named methods |
| **Use `toList()` (Java 16+)** | `stream.toList()` instead of `collect(Collectors.toList())` |
| **Profile before parallelizing** | Measure if parallel actually improves performance |

---

## Quick Reference Cheat Sheet

```
Source → stream() → [Intermediate Ops] → Terminal Op → Result

INTERMEDIATE (Lazy):          TERMINAL (Eager):
├── filter(Predicate)         ├── collect(Collector)
├── map(Function)             ├── forEach(Consumer)
├── flatMap(Function)         ├── forEachOrdered(Consumer)
├── distinct()                ├── toArray()
├── sorted()                  ├── reduce()
├── sorted(Comparator)        ├── count()
├── peek(Consumer)            ├── min(Comparator)
├── limit(long)               ├── max(Comparator)
├── skip(long)                ├── findFirst()
├── takeWhile(Predicate) [9+] ├── findAny()
├── dropWhile(Predicate) [9+] ├── anyMatch(Predicate)
├── mapToInt/Long/Double      ├── allMatch(Predicate)
└── flatMapToInt/Long/Double  └── noneMatch(Predicate)
```

---

> **Tip:** Practice these examples in your IDE. Most WITCH company interviews ask 3-5 stream-based coding questions. Focus on `filter`, `map`, `collect`, `groupingBy`, `reduce`, and `flatMap` — these cover 90% of interview questions.
