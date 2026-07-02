# Java Confusing Technical Topics - Complete Guide

**Target:** Java backend technical assessments, Spring Boot technical assessments, and senior-level technical
rounds.  
**Focus:** Topics that confuse candidates because they look similar, have hidden rules,
or are frequently asked as tricky technical discussion questions.

## Table of Contents

1. [`==` vs `equals()` vs `hashCode()`](#1--vs-equals-vs-hashcode)
2. [String Pool vs Heap String](#2-string-pool-vs-heap-string)
3. [`String` vs `StringBuilder` vs `StringBuffer`](#3-string-vs-stringbuilder-vs-stringbuffer)
4. [`final` vs `finally` vs `finalize()`](#4-final-vs-finally-vs-finalize)
5. [Overloading vs Overriding](#5-overloading-vs-overriding)
6. [Compile-Time vs Runtime Polymorphism](#6-compile-time-vs-runtime-polymorphism)
7. [Abstract Class vs Interface](#7-abstract-class-vs-interface)
8. [Default Method Conflict in Interfaces](#8-default-method-conflict-in-interfaces)
9. [`this` vs `super`](#9-this-vs-super)
10. [`static` Method Hiding vs Method Overriding](#10-static-method-hiding-vs-method-overriding)
11. [Constructor Confusions](#11-constructor-confusions)
12. [Pass by Value vs Pass by Reference](#12-pass-by-value-vs-pass-by-reference)
13. [Shallow Copy vs Deep Copy](#13-shallow-copy-vs-deep-copy)
14. [Immutable Class vs Final Class](#14-immutable-class-vs-final-class)
15. [`ArrayList` vs `LinkedList`](#15-arraylist-vs-linkedlist)
16. [`HashMap` vs `Hashtable` vs `ConcurrentHashMap`](#16-hashmap-vs-hashtable-vs-concurrenthashmap)
17. [`HashSet` vs `LinkedHashSet` vs `TreeSet`](#17-hashset-vs-linkedhashset-vs-treeset)
18. [`Comparable` vs `Comparator`](#18-comparable-vs-comparator)
19. [Fail-Fast vs Fail-Safe Iterators](#19-fail-fast-vs-fail-safe-iterators)
20. [`Iterator` vs `ListIterator`](#20-iterator-vs-listiterator)
21. [Checked vs Unchecked Exceptions](#21-checked-vs-unchecked-exceptions)
22. [`throw` vs `throws`](#22-throw-vs-throws)
23. [`try-with-resources` vs `finally`](#23-try-with-resources-vs-finally)
24. [`synchronized` vs `Lock`](#24-synchronized-vs-lock)
25. [`wait()` vs `sleep()` vs `join()`](#25-wait-vs-sleep-vs-join)
26. [`notify()` vs `notifyAll()`](#26-notify-vs-notifyall)
27. [`volatile` vs `synchronized`](#27-volatile-vs-synchronized)
28. [`AtomicInteger` vs `volatile int`](#28-atomicinteger-vs-volatile-int)
29. [Thread Pool Confusions](#29-thread-pool-confusions)
30. [Deadlock vs Livelock vs Starvation](#30-deadlock-vs-livelock-vs-starvation)
31. [Stream `map()` vs `flatMap()`](#31-stream-map-vs-flatmap)
32. [Stream `findFirst()` vs `findAny()`](#32-stream-findfirst-vs-findany)
33. [Intermediate vs Terminal Stream Operations](#33-intermediate-vs-terminal-stream-operations)
34. [`reduce()` vs `collect()`](#34-reduce-vs-collect)
35. [Optional Confusions](#35-optional-confusions)
36. [Lambda vs Anonymous Class](#36-lambda-vs-anonymous-class)
37. [Method Reference vs Lambda](#37-method-reference-vs-lambda)
38. [Heap vs Stack](#38-heap-vs-stack)
39. [JVM, JRE, and JDK](#39-jvm-jre-and-jdk)
40. [Class Loading Confusions](#40-class-loading-confusions)
41. [Garbage Collection Confusions](#41-garbage-collection-confusions)
42. [Strong, Soft, Weak, and Phantom References](#42-strong-soft-weak-and-phantom-references)
43. [Serialization Confusions](#43-serialization-confusions)
44. [Reflection Confusions](#44-reflection-confusions)
45. [Records vs Classes](#45-records-vs-classes)
46. [Sealed Classes vs Final Classes](#46-sealed-classes-vs-final-classes)
47. [Spring `@Component` vs `@Service` vs `@Repository`](#47-spring-component-vs-service-vs-repository)
48. [Spring `@Autowired` vs Constructor Injection](#48-spring-autowired-vs-constructor-injection)
49. [Spring `@Controller` vs `@RestController`](#49-spring-controller-vs-restcontroller)
50. [`@RequestParam` vs `@PathVariable` vs `@RequestBody`](#50-requestparam-vs-pathvariable-vs-requestbody)
51. [JPA `persist()` vs `merge()`](#51-jpa-persist-vs-merge)
52. [Lazy Loading vs Eager Loading](#52-lazy-loading-vs-eager-loading)
53. [Transaction Propagation Confusions](#53-transaction-propagation-confusions)
54. [Authentication vs Authorization](#54-authentication-vs-authorization)
55. [JWT vs Session](#55-jwt-vs-session)

---

## 1. `==` vs `equals()` vs `hashCode()`

`==` compares references for objects. `equals()` compares logical equality when the
class overrides it. `hashCode()` is used by hash-based collections.

```java
String a = new String("java");
String b = new String("java");

System.out.println(a == b);      // false
System.out.println(a.equals(b)); // true
```

Key rule:

- If two objects are equal by `equals()`, they must have the same `hashCode()`.
- If two objects have the same `hashCode()`, they are not necessarily equal.

## 2. String Pool vs Heap String

String literals go into the string pool. `new String()` creates a new heap object.

```java
String a = "java";
String b = "java";
String c = new String("java");

System.out.println(a == b); // true
System.out.println(a == c); // false
```

Common common trick:

```java
String c = new String("java").intern();
System.out.println(a == c); // true
```

`intern()` returns the pooled reference.

## 3. `String` vs `StringBuilder` vs `StringBuffer`

| Type | Mutable | Thread-safe | Use case |
| --- | --- | --- | --- |
| `String` | No | Yes, because immutable | Fixed text |
| `StringBuilder` | Yes | No | Fast local string changes |
| `StringBuffer` | Yes | Yes | Legacy synchronized string changes |

Key answer:

Use `StringBuilder` for most concatenation inside loops.

## 4. `final` vs `finally` vs `finalize()`

| Keyword | Meaning |
| --- | --- |
| `final` | Prevent reassignment, overriding, or inheritance depending on usage |
| `finally` | Block that runs after `try/catch` |
| `finalize()` | Deprecated GC callback; avoid using it |

```java
final int x = 10;

try {
    System.out.println("try");
} finally {
    System.out.println("cleanup");
}
```

## 5. Overloading vs Overriding

Overloading happens in the same class with different parameters. Overriding happens in
child class with the same method signature.

```java
class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }
}
```

```java
class Parent {
    void show() {
        System.out.println("parent");
    }
}

class Child extends Parent {
    @Override
    void show() {
        System.out.println("child");
    }
}
```

Key rule:

- Overloading is resolved at compile time.
- Overriding is resolved at runtime.

## 6. Compile-Time vs Runtime Polymorphism

Compile-time polymorphism means method overloading. Runtime polymorphism means method
overriding.

```java
Parent obj = new Child();
obj.show(); // child
```

The reference type decides what methods are accessible. The object type decides which
overridden implementation runs.

## 7. Abstract Class vs Interface

| Feature | Abstract class | Interface |
| --- | --- | --- |
| Inheritance | One abstract class | Multiple interfaces |
| State | Can have instance fields | Mainly constants; default/static methods allowed |
| Constructor | Yes | No |
| Best use | Shared base behavior | Capability or contract |

Key answer:

Use abstract class when classes share state and common implementation. Use interface
when unrelated classes need a common contract.

## 8. Default Method Conflict in Interfaces

If two interfaces provide the same default method, the class must override it.

```java
interface A {
    default void show() {
        System.out.println("A");
    }
}

interface B {
    default void show() {
        System.out.println("B");
    }
}

class C implements A, B {
    @Override
    public void show() {
        A.super.show();
    }
}
```

## 9. `this` vs `super`

`this` refers to the current object. `super` refers to the parent class part of the
object.

```java
class Child extends Parent {
    Child() {
        super();
    }

    void print() {
        this.show();
        super.show();
    }
}
```

## 10. `static` Method Hiding vs Method Overriding

Static methods are not overridden; they are hidden.

```java
class Parent {
    static void test() {
        System.out.println("parent");
    }
}

class Child extends Parent {
    static void test() {
        System.out.println("child");
    }
}

Parent obj = new Child();
obj.test(); // parent
```

The reference type decides static method calls.

## 11. Constructor Confusions

Important rules:

- Constructor is not inherited.
- `this()` or `super()` must be the first statement.
- If no constructor is written, Java provides a default no-arg constructor.
- If any constructor is written, Java does not add a default constructor.

```java
class Parent {
    Parent(String name) {}
}

class Child extends Parent {
    Child() {
        super("test");
    }
}
```

## 12. Pass by Value vs Pass by Reference

Java is always pass by value. For objects, the reference value is copied.

```java
static void changeName(Employee e) {
    e.name = "Updated";
}

static void reassign(Employee e) {
    e = new Employee("New");
}
```

Changing object state is visible. Reassigning the copied reference is not visible
outside.

## 13. Shallow Copy vs Deep Copy

Shallow copy copies object references. Deep copy copies nested objects too.

```java
class Address {
    String city;
}

class Employee {
    String name;
    Address address;
}
```

If two employees share the same `Address`, changing one address affects the other.
Deep copy creates a separate `Address`.

## 14. Immutable Class vs Final Class

`final class` prevents inheritance. Immutable class prevents state changes.

```java
public final class Money {
    private final int amount;

    public Money(int amount) {
        this.amount = amount;
    }

    public int amount() {
        return amount;
    }
}
```

For true immutability, make defensive copies of mutable fields.

## 15. `ArrayList` vs `LinkedList`

| Operation | `ArrayList` | `LinkedList` |
| --- | --- | --- |
| Random access | Fast | Slow |
| Add at end | Fast amortized | Fast |
| Add/remove middle | Shifting cost | Traversal cost |
| Memory | Less overhead | More overhead |

Key answer:

Use `ArrayList` by default. Use `LinkedList` rarely, mostly when queue/deque behavior is
needed.

## 16. `HashMap` vs `Hashtable` vs `ConcurrentHashMap`

| Type | Null keys/values | Thread-safe | Use now |
| --- | --- | --- | --- |
| `HashMap` | One null key, multiple null values | No | Single-threaded |
| `Hashtable` | No null key/value | Yes, old synchronized | Avoid |
| `ConcurrentHashMap` | No null key/value | Yes | Concurrent access |

`ConcurrentHashMap` is preferred over `Hashtable` in modern Java.

## 17. `HashSet` vs `LinkedHashSet` vs `TreeSet`

| Type | Ordering |
| --- | --- |
| `HashSet` | No guaranteed order |
| `LinkedHashSet` | Insertion order |
| `TreeSet` | Sorted order |

`TreeSet` requires elements to be comparable or a comparator to be provided.

## 18. `Comparable` vs `Comparator`

`Comparable` defines natural ordering inside the class. `Comparator` defines external
custom ordering.

```java
class Employee implements Comparable<Employee> {
    int salary;

    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.salary, other.salary);
    }
}
```

```java
Comparator<Employee> bySalaryDesc =
        Comparator.comparing(Employee::salary).reversed();
```

## 19. Fail-Fast vs Fail-Safe Iterators

Fail-fast iterators throw `ConcurrentModificationException` when a collection is
structurally modified during iteration.

```java
for (String item : list) {
    list.remove(item); // can throw ConcurrentModificationException
}
```

Use iterator removal:

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().isBlank()) {
        it.remove();
    }
}
```

Fail-safe is a common common term for iterators over snapshot/concurrent
collections like `CopyOnWriteArrayList`.

## 20. `Iterator` vs `ListIterator`

| Feature | `Iterator` | `ListIterator` |
| --- | --- | --- |
| Direction | Forward | Forward and backward |
| Collection support | All collections | Lists only |
| Add/set support | No | Yes |

## 21. Checked vs Unchecked Exceptions

Checked exceptions are checked at compile time. Unchecked exceptions extend
`RuntimeException`.

```java
void readFile() throws IOException {
    Files.readString(Path.of("a.txt"));
}
```

Key answer:

Use checked exceptions when caller can reasonably recover. Use unchecked exceptions for
programming errors or invalid state.

## 22. `throw` vs `throws`

`throw` actually throws an exception. `throws` declares possible exceptions.

```java
void validate(int age) {
    if (age < 18) {
        throw new IllegalArgumentException("Age must be 18+");
    }
}
```

```java
void read() throws IOException {
    Files.readString(Path.of("data.txt"));
}
```

## 23. `try-with-resources` vs `finally`

Use try-with-resources for objects implementing `AutoCloseable`.

```java
try (BufferedReader br = Files.newBufferedReader(Path.of("data.txt"))) {
    System.out.println(br.readLine());
}
```

It closes resources automatically, even if an exception occurs.

## 24. `synchronized` vs `Lock`

| Feature | `synchronized` | `Lock` |
| --- | --- | --- |
| Release | Automatic | Manual in `finally` |
| Try lock | No | Yes |
| Interruptible lock | No | Yes |
| Fairness option | No | Yes |

```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

## 25. `wait()` vs `sleep()` vs `join()`

| Method | Belongs to | Releases lock | Purpose |
| --- | --- | --- | --- |
| `wait()` | `Object` | Yes | Wait for notification |
| `sleep()` | `Thread` | No | Pause current thread |
| `join()` | `Thread` | No | Wait for another thread to finish |

`wait()` must be called inside synchronized context.

## 26. `notify()` vs `notifyAll()`

`notify()` wakes one waiting thread. `notifyAll()` wakes all waiting threads.

Key answer:

Prefer `notifyAll()` when multiple conditions may be waiting on the same monitor. It is
safer, though possibly less efficient.

## 27. `volatile` vs `synchronized`

`volatile` guarantees visibility, not atomicity. `synchronized` guarantees visibility
and mutual exclusion.

```java
volatile boolean running = true;
```

This is fine for stop flags. It is not enough for `count++`.

## 28. `AtomicInteger` vs `volatile int`

`volatile int` makes reads/writes visible. `AtomicInteger` provides atomic operations.

```java
AtomicInteger counter = new AtomicInteger();
counter.incrementAndGet();
```

Use `AtomicInteger` for concurrent counters.

## 29. Thread Pool Confusions

Common executor types:

| Executor | Use case |
| --- | --- |
| Fixed thread pool | Known concurrency limit |
| Cached thread pool | Short-lived bursty tasks |
| Single thread executor | Sequential background tasks |
| Scheduled thread pool | Delayed or repeated tasks |

Key warning:

Avoid unbounded queues and unlimited thread growth in production systems.

## 30. Deadlock vs Livelock vs Starvation

| Problem | Meaning |
| --- | --- |
| Deadlock | Threads wait forever for each other's locks |
| Livelock | Threads keep reacting but make no progress |
| Starvation | A thread rarely or never gets CPU/lock access |

Deadlock prevention: consistent lock ordering, timeouts, and smaller critical sections.

## 31. Stream `map()` vs `flatMap()`

`map()` transforms one element into one element. `flatMap()` transforms one element into
many elements and flattens the result.

```java
List<List<Integer>> nums = List.of(List.of(1, 2), List.of(3, 4));

List<Integer> flat = nums.stream()
        .flatMap(List::stream)
        .toList();
```

## 32. Stream `findFirst()` vs `findAny()`

`findFirst()` respects encounter order. `findAny()` may return any element, especially
in parallel streams.

Use `findFirst()` when order matters. Use `findAny()` when any match is acceptable.

## 33. Intermediate vs Terminal Stream Operations

Intermediate operations are lazy. Terminal operations trigger execution.

```java
Stream<String> stream = names.stream()
        .filter(name -> name.startsWith("A")); // nothing runs yet

List<String> result = stream.toList(); // execution starts here
```

## 34. `reduce()` vs `collect()`

`reduce()` combines stream elements into a single value. `collect()` puts elements into
a mutable result container like `List`, `Map`, or grouped structure.

```java
int sum = nums.stream().reduce(0, Integer::sum);

Map<String, Long> freq = words.stream()
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
```

## 35. Optional Confusions

`Optional` is mainly for return values, not fields or method parameters.

Bad:

```java
void save(Optional<User> user) {}
```

Better:

```java
Optional<User> findById(long id) {
    return repository.findById(id);
}
```

Avoid calling `get()` without checking presence.

## 36. Lambda vs Anonymous Class

Lambda works with functional interfaces. Anonymous class can extend classes or implement
interfaces and has its own `this`.

```java
Runnable r = () -> System.out.println("lambda");
```

In a lambda, `this` refers to the enclosing object.

## 37. Method Reference vs Lambda

Method reference is a shorter form when lambda only calls an existing method.

```java
names.forEach(name -> System.out.println(name));
names.forEach(System.out::println);
```

Use method references when they improve readability.

## 38. Heap vs Stack

Stack stores method frames and local variables. Heap stores objects.

```java
Employee e = new Employee();
```

The variable `e` is a reference in the stack frame. The `Employee` object is on the
heap.

## 39. JVM, JRE, and JDK

| Term | Meaning |
| --- | --- |
| JVM | Runs bytecode |
| JRE | JVM plus runtime libraries |
| JDK | JRE plus compiler and development tools |

Developer installs JDK. Production runtime may use JRE or JDK depending on deployment.

## 40. Class Loading Confusions

Class loading phases:

1. Loading
2. Linking
3. Initialization

Static fields and static blocks run during initialization.

```java
class Demo {
    static {
        System.out.println("loaded");
    }
}
```

## 41. Garbage Collection Confusions

GC frees unreachable objects, not objects with null references only.

```java
User u = new User();
u = null;
```

The object becomes eligible for GC if no other live reference points to it. GC timing is
not guaranteed.

## 42. Strong, Soft, Weak, and Phantom References

| Reference | GC behavior |
| --- | --- |
| Strong | Not collected while reachable |
| Soft | Collected under memory pressure |
| Weak | Collected eagerly when weakly reachable |
| Phantom | Used for cleanup tracking after finalization stage |

`WeakHashMap` is a common common example for weak references.

## 43. Serialization Confusions

`transient` fields are not serialized.

```java
class User implements Serializable {
    private String username;
    private transient String password;
}
```

`serialVersionUID` helps version compatibility during deserialization.

## 44. Reflection Confusions

Reflection inspects and invokes classes, methods, fields, and constructors at runtime.

```java
Class<?> clazz = Class.forName("com.example.User");
Method method = clazz.getDeclaredMethod("getName");
```

Frameworks use reflection heavily, but application code should use it carefully because
it can break encapsulation and affect performance.

## 45. Records vs Classes

Records are concise immutable data carriers.

```java
record EmployeeDto(long id, String name) {}
```

Records automatically provide constructor, accessors, `equals()`, `hashCode()`, and
`toString()`.

Use records for DTO-like data. Use classes when behavior, mutability, inheritance, or
custom lifecycle is needed.

## 46. Sealed Classes vs Final Classes

`final` class cannot be extended. `sealed` class allows only selected subclasses.

```java
sealed interface Payment permits CardPayment, UpiPayment {}

final class CardPayment implements Payment {}
final class UpiPayment implements Payment {}
```

Use sealed types when the allowed hierarchy is known and controlled.

## 47. Spring `@Component` vs `@Service` vs `@Repository`

All three register Spring beans.

| Annotation | Meaning |
| --- | --- |
| `@Component` | Generic Spring-managed bean |
| `@Service` | Business logic layer |
| `@Repository` | Persistence layer, exception translation |

Key answer:

They behave similarly for component scanning, but communicate different layer
responsibilities.

## 48. Spring `@Autowired` vs Constructor Injection

Constructor injection is preferred.

```java
@Service
class UserService {
    private final UserRepository repository;

    UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

Benefits: immutable dependencies, easier testing, and fail-fast startup.

## 49. Spring `@Controller` vs `@RestController`

`@RestController` equals `@Controller` plus `@ResponseBody`.

```java
@RestController
class UserController {
    @GetMapping("/users")
    List<UserDto> users() {
        return service.findAll();
    }
}
```

Use `@Controller` for MVC views. Use `@RestController` for REST APIs.

## 50. `@RequestParam` vs `@PathVariable` vs `@RequestBody`

| Annotation | Source |
| --- | --- |
| `@RequestParam` | Query parameter |
| `@PathVariable` | URI path segment |
| `@RequestBody` | Request JSON/XML body |

```java
@GetMapping("/users/{id}")
UserDto find(@PathVariable long id, @RequestParam boolean active) {
    return service.find(id, active);
}
```

## 51. JPA `persist()` vs `merge()`

`persist()` makes a new entity managed. `merge()` copies detached entity state into a
managed entity and returns the managed instance.

Common trap:

The object passed to `merge()` may still be detached. Use the returned object.

## 52. Lazy Loading vs Eager Loading

Lazy loading fetches related data when accessed. Eager loading fetches immediately.

Technical Discussion issue:

Lazy loading can cause `LazyInitializationException` outside a transaction and N+1 query
problems inside loops.

## 53. Transaction Propagation Confusions

Common Spring propagation modes:

| Propagation | Meaning |
| --- | --- |
| `REQUIRED` | Join existing transaction or create one |
| `REQUIRES_NEW` | Suspend existing transaction and create new one |
| `MANDATORY` | Must run inside existing transaction |
| `SUPPORTS` | Use transaction if present |
| `NOT_SUPPORTED` | Run without transaction |

Most service methods use `REQUIRED`.

## 54. Authentication vs Authorization

Authentication answers: who are you?  
Authorization answers: what are you allowed to do?

Example:

- Login with username/password is authentication.
- Checking whether user has `ADMIN` role is authorization.

## 55. JWT vs Session

| Topic | JWT | Session |
| --- | --- | --- |
| Storage | Client holds token | Server stores session |
| Scalability | Easier stateless scaling | Needs shared session/store |
| Revocation | Harder before expiry | Easier server-side invalidation |
| Payload | Can carry claims | Usually only session id |

Key answer:

JWT is useful for stateless APIs. Sessions are simpler when server-side control and
revocation matter more.

