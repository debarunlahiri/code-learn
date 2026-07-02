# Java Confusing Coding Snippets - Questions and Answers

**Target:** Short technical coding rounds, quick output questions, and concept checks.  
**Focus:** Small snippets that test Java rules, not full algorithm problems.

## Table of Contents

1. [String Reference Comparison](#1-string-reference-comparison)
2. [String Interning](#2-string-interning)
3. [String Immutability](#3-string-immutability)
4. [Integer Cache](#4-integer-cache)
5. [Autoboxing and Null](#5-autoboxing-and-null)
6. [Method Overloading with `null`](#6-method-overloading-with-null)
7. [Widening vs Boxing](#7-widening-vs-boxing)
8. [Varargs Priority](#8-varargs-priority)
9. [Overriding Field Access](#9-overriding-field-access)
10. [Static Method Hiding](#10-static-method-hiding)
11. [Constructor Execution Order](#11-constructor-execution-order)
12. [Static Block Order](#12-static-block-order)
13. [Final Reference Mutation](#13-final-reference-mutation)
14. [Pass by Value](#14-pass-by-value)
15. [Post-Increment Confusion](#15-post-increment-confusion)
16. [Compound Assignment](#16-compound-assignment)
17. [Floating Point Precision](#17-floating-point-precision)
18. [`switch` with String](#18-switch-with-string)
19. [`equals()` Without `hashCode()`](#19-equals-without-hashcode)
20. [Mutable Key in `HashMap`](#20-mutable-key-in-hashmap)
21. [`HashSet` Duplicate Logic](#21-hashset-duplicate-logic)
22. [`TreeSet` Comparator Equality](#22-treeset-comparator-equality)
23. [`ArrayList` Remove Overload](#23-arraylist-remove-overload)
24. [Concurrent Modification](#24-concurrent-modification)
25. [Safe Iterator Removal](#25-safe-iterator-removal)
26. [Try-Catch-Finally Return](#26-try-catch-finally-return)
27. [Finally Overriding Return](#27-finally-overriding-return)
28. [Exception Catch Order](#28-exception-catch-order)
29. [Try-With-Resources Close Order](#29-try-with-resources-close-order)
30. [Checked Exception in Lambda](#30-checked-exception-in-lambda)
31. [Stream Reuse](#31-stream-reuse)
32. [Stream Laziness](#32-stream-laziness)
33. [`map()` vs `peek()`](#33-map-vs-peek)
34. [`findFirst()` with Parallel Stream](#34-findfirst-with-parallel-stream)
35. [`reduce()` Identity Mistake](#35-reduce-identity-mistake)
36. [`Collectors.toMap()` Duplicate Key](#36-collectorstomap-duplicate-key)
37. [`Optional.orElse()` vs `orElseGet()`](#37-optionalorelse-vs-orelseget)
38. [Lambda Captured Variable](#38-lambda-captured-variable)
39. [Anonymous Class `this`](#39-anonymous-class-this)
40. [Method Reference Binding](#40-method-reference-binding)
41. [`volatile` Increment](#41-volatile-increment)
42. [Race Condition](#42-race-condition)
43. [`sleep()` Does Not Release Lock](#43-sleep-does-not-release-lock)
44. [`wait()` Requires Monitor](#44-wait-requires-monitor)
45. [Deadlock Mini Snippet](#45-deadlock-mini-snippet)
46. [StringBuilder Thread Safety](#46-stringbuilder-thread-safety)
47. [Record Equality](#47-record-equality)
48. [Enum Constructor](#48-enum-constructor)
49. [LocalDate Immutability](#49-localdate-immutability)
50. [Spring Self-Invocation Pitfall](#50-spring-self-invocation-pitfall)

---

## 1. String Reference Comparison

Question:

```java
String a = "java";
String b = "java";
System.out.println(a == b);
```

Answer:

```text
true
```

Both literals point to the same string pool object.

## 2. String Interning

Question:

```java
String a = "java";
String b = new String("java");
System.out.println(a == b);
System.out.println(a == b.intern());
```

Answer:

```text
false
true
```

`new String()` creates a heap object. `intern()` returns the pooled object.

## 3. String Immutability

Question:

```java
String s = "hello";
s.concat(" world");
System.out.println(s);
```

Answer:

```text
hello
```

`concat()` returns a new string. The original string is unchanged.

## 4. Integer Cache

Question:

```java
Integer a = 127;
Integer b = 127;
Integer c = 128;
Integer d = 128;

System.out.println(a == b);
System.out.println(c == d);
```

Answer:

```text
true
false
```

Java caches boxed integers from `-128` to `127` by default.

## 5. Autoboxing and Null

Question:

```java
Integer x = null;
int y = x;
System.out.println(y);
```

Answer:

```text
NullPointerException
```

Unboxing `null` throws `NullPointerException`.

## 6. Method Overloading with `null`

Question:

```java
void test(String s) {
    System.out.println("String");
}

void test(Object o) {
    System.out.println("Object");
}

test(null);
```

Answer:

```text
String
```

Java chooses the most specific overload.

## 7. Widening vs Boxing

Question:

```java
void test(long x) {
    System.out.println("long");
}

void test(Integer x) {
    System.out.println("Integer");
}

test(10);
```

Answer:

```text
long
```

Widening is preferred over boxing.

## 8. Varargs Priority

Question:

```java
void test(int x) {
    System.out.println("int");
}

void test(int... x) {
    System.out.println("varargs");
}

test(10);
```

Answer:

```text
int
```

Exact match is preferred over varargs.

## 9. Overriding Field Access

Question:

```java
class Parent {
    String name = "parent";
}

class Child extends Parent {
    String name = "child";
}

Parent p = new Child();
System.out.println(p.name);
```

Answer:

```text
parent
```

Fields are not overridden. Field access depends on reference type.

## 10. Static Method Hiding

Question:

```java
class Parent {
    static void show() {
        System.out.println("parent");
    }
}

class Child extends Parent {
    static void show() {
        System.out.println("child");
    }
}

Parent p = new Child();
p.show();
```

Answer:

```text
parent
```

Static methods are hidden, not overridden.

## 11. Constructor Execution Order

Question:

```java
class Parent {
    Parent() {
        System.out.print("P ");
    }
}

class Child extends Parent {
    Child() {
        System.out.print("C ");
    }
}

new Child();
```

Answer:

```text
P C
```

Parent constructor runs before child constructor.

## 12. Static Block Order

Question:

```java
class Demo {
    static {
        System.out.print("A ");
    }

    public static void main(String[] args) {
        System.out.print("B ");
    }
}
```

Answer:

```text
A B
```

Static blocks run when the class is initialized, before `main()`.

## 13. Final Reference Mutation

Question:

```java
final List<String> list = new ArrayList<>();
list.add("A");
System.out.println(list);
```

Answer:

```text
[A]
```

`final` prevents reassignment of the reference, not mutation of the object.

## 14. Pass by Value

Question:

```java
static void change(StringBuilder sb) {
    sb.append("X");
    sb = new StringBuilder("Y");
}

StringBuilder sb = new StringBuilder("A");
change(sb);
System.out.println(sb);
```

Answer:

```text
AX
```

Java passes the reference value by copy. Mutation is visible, reassignment is not.

## 15. Post-Increment Confusion

Question:

```java
int i = 1;
i = i++;
System.out.println(i);
```

Answer:

```text
1
```

`i++` returns old value first, then increments. The assignment writes the old value back.

## 16. Compound Assignment

Question:

```java
byte b = 10;
b += 5;
System.out.println(b);
```

Answer:

```text
15
```

Compound assignment includes an implicit cast.

This would not compile:

```java
b = b + 5;
```

## 17. Floating Point Precision

Question:

```java
System.out.println(0.1 + 0.2 == 0.3);
```

Answer:

```text
false
```

Binary floating point cannot represent many decimal fractions exactly.

## 18. `switch` with String

Question:

```java
String value = null;
switch (value) {
    case "A" -> System.out.println("A");
    default -> System.out.println("default");
}
```

Answer:

```text
NullPointerException
```

Switching on a null string throws `NullPointerException`.

## 19. `equals()` Without `hashCode()`

Question:

```java
class User {
    int id;

    User(int id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        return o instanceof User u && u.id == id;
    }
}

Set<User> set = new HashSet<>();
set.add(new User(1));
set.add(new User(1));
System.out.println(set.size());
```

Answer:

```text
2
```

Without matching `hashCode()`, `HashSet` may store logically equal objects separately.

## 20. Mutable Key in `HashMap`

Question:

```java
class Key {
    int id;

    Key(int id) {
        this.id = id;
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object o) {
        return o instanceof Key k && k.id == id;
    }
}

Key key = new Key(1);
Map<Key, String> map = new HashMap<>();
map.put(key, "value");
key.id = 2;
System.out.println(map.get(key));
```

Answer:

```text
null
```

Changing key state changes its hash bucket lookup.

## 21. `HashSet` Duplicate Logic

Question:

```java
Set<String> set = new HashSet<>();
set.add("A");
set.add(new String("A"));
System.out.println(set.size());
```

Answer:

```text
1
```

`HashSet` uses `equals()` and `hashCode()`, not reference equality.

## 22. `TreeSet` Comparator Equality

Question:

```java
Set<String> set = new TreeSet<>(Comparator.comparingInt(String::length));
set.add("aa");
set.add("bb");
System.out.println(set.size());
```

Answer:

```text
1
```

`TreeSet` treats elements as duplicates when comparator returns `0`.

## 23. `ArrayList` Remove Overload

Question:

```java
List<Integer> list = new ArrayList<>(List.of(1, 2, 3));
list.remove(1);
System.out.println(list);
```

Answer:

```text
[1, 3]
```

`remove(1)` removes index `1`, not value `1`.

To remove value:

```java
list.remove(Integer.valueOf(1));
```

## 24. Concurrent Modification

Question:

```java
List<String> list = new ArrayList<>(List.of("A", "B"));

for (String item : list) {
    list.remove(item);
}
```

Answer:

```text
ConcurrentModificationException may occur
```

Structural modification during enhanced-for iteration is unsafe.

## 25. Safe Iterator Removal

Question:

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("A")) {
        it.remove();
    }
}
```

Answer:

```text
Safe removal
```

Use iterator's `remove()` while iterating.

## 26. Try-Catch-Finally Return

Question:

```java
static int test() {
    try {
        return 1;
    } finally {
        System.out.println("finally");
    }
}
```

Answer:

```text
prints finally, then returns 1
```

`finally` runs before the method returns.

## 27. Finally Overriding Return

Question:

```java
static int test() {
    try {
        return 1;
    } finally {
        return 2;
    }
}
```

Answer:

```text
2
```

Returning from `finally` overrides the original return. Avoid this.

## 28. Exception Catch Order

Question:

```java
try {
    throw new FileNotFoundException();
} catch (IOException e) {
    System.out.println("IO");
} catch (FileNotFoundException e) {
    System.out.println("File");
}
```

Answer:

```text
Compilation error
```

`FileNotFoundException` catch block is unreachable because `IOException` catches it
first.

## 29. Try-With-Resources Close Order

Question:

```java
try (Resource a = new Resource("A");
     Resource b = new Resource("B")) {
    System.out.print("body ");
}
```

Answer:

```text
body B A
```

Resources close in reverse order.

## 30. Checked Exception in Lambda

Question:

```java
list.forEach(item -> {
    Thread.sleep(1000);
});
```

Answer:

```text
Compilation error
```

`Thread.sleep()` throws checked `InterruptedException`, which must be handled.

## 31. Stream Reuse

Question:

```java
Stream<String> stream = Stream.of("A", "B");
stream.count();
stream.findFirst();
```

Answer:

```text
IllegalStateException
```

A stream cannot be reused after a terminal operation.

## 32. Stream Laziness

Question:

```java
Stream.of("A", "B")
        .filter(s -> {
            System.out.println(s);
            return true;
        });
```

Answer:

```text
No output
```

Intermediate operations are lazy. Nothing runs without a terminal operation.

## 33. `map()` vs `peek()`

Question:

```java
List<String> result = Stream.of("a", "b")
        .peek(String::toUpperCase)
        .toList();

System.out.println(result);
```

Answer:

```text
[a, b]
```

`peek()` is mainly for debugging. It does not replace elements. Use `map()`.

## 34. `findFirst()` with Parallel Stream

Question:

```java
Optional<Integer> result = List.of(1, 2, 3, 4)
        .parallelStream()
        .findFirst();
```

Answer:

```text
Optional[1]
```

`findFirst()` preserves encounter order even in parallel streams.

## 35. `reduce()` Identity Mistake

Question:

```java
int result = List.of(1, 2, 3)
        .stream()
        .reduce(10, Integer::sum);
System.out.println(result);
```

Answer:

```text
16
```

Identity value participates in the reduction.

## 36. `Collectors.toMap()` Duplicate Key

Question:

```java
List<String> names = List.of("Ram", "Raj");
Map<Character, String> map = names.stream()
        .collect(Collectors.toMap(s -> s.charAt(0), s -> s));
```

Answer:

```text
IllegalStateException
```

Duplicate keys require a merge function.

```java
Collectors.toMap(s -> s.charAt(0), s -> s, (a, b) -> a)
```

## 37. `Optional.orElse()` vs `orElseGet()`

Question:

```java
String value = Optional.of("A").orElse(expensive());
```

Answer:

```text
expensive() still runs
```

`orElse()` evaluates eagerly. `orElseGet()` evaluates lazily.

```java
String value = Optional.of("A").orElseGet(() -> expensive());
```

## 38. Lambda Captured Variable

Question:

```java
int count = 0;
Runnable r = () -> System.out.println(count);
count++;
```

Answer:

```text
Compilation error
```

Variables captured by lambda must be final or effectively final.

## 39. Anonymous Class `this`

Question:

```java
class Demo {
    void test() {
        Runnable r = new Runnable() {
            public void run() {
                System.out.println(this.getClass().getName());
            }
        };
    }
}
```

Answer:

```text
this refers to anonymous class object
```

In an anonymous class, `this` is the anonymous object.

## 40. Method Reference Binding

Question:

```java
List<String> names = List.of("a", "bb");
names.stream().map(String::length).toList();
```

Answer:

```text
[1, 2]
```

`String::length` is equivalent to `s -> s.length()`.

## 41. `volatile` Increment

Question:

```java
volatile int count = 0;
count++;
```

Answer:

```text
Not atomic
```

`volatile` guarantees visibility, not atomicity.

## 42. Race Condition

Question:

```java
int count = 0;

void increment() {
    count++;
}
```

Answer:

```text
Unsafe with multiple threads
```

`count++` is read, add, write. Multiple threads can overwrite each other.

## 43. `sleep()` Does Not Release Lock

Question:

```java
synchronized (lock) {
    Thread.sleep(1000);
}
```

Answer:

```text
Lock is still held
```

`sleep()` pauses the thread but does not release monitor locks.

## 44. `wait()` Requires Monitor

Question:

```java
Object lock = new Object();
lock.wait();
```

Answer:

```text
IllegalMonitorStateException
```

`wait()` must be called while owning the object's monitor.

```java
synchronized (lock) {
    lock.wait();
}
```

## 45. Deadlock Mini Snippet

Question:

```java
Thread t1 = new Thread(() -> {
    synchronized (a) {
        synchronized (b) {}
    }
});

Thread t2 = new Thread(() -> {
    synchronized (b) {
        synchronized (a) {}
    }
});
```

Answer:

```text
Possible deadlock
```

Threads acquire locks in opposite order.

## 46. StringBuilder Thread Safety

Question:

```java
StringBuilder sb = new StringBuilder();
// multiple threads call sb.append(...)
```

Answer:

```text
Not thread-safe
```

Use synchronization, `StringBuffer`, or avoid sharing mutable builders.

## 47. Record Equality

Question:

```java
record User(int id, String name) {}

System.out.println(new User(1, "A").equals(new User(1, "A")));
```

Answer:

```text
true
```

Records generate value-based `equals()` and `hashCode()`.

## 48. Enum Constructor

Question:

```java
enum Status {
    ACTIVE;

    Status() {
        System.out.println("created");
    }
}
```

Answer:

```text
Enum constructors are private implicitly
```

Enum constants are created when the enum class is initialized.

## 49. LocalDate Immutability

Question:

```java
LocalDate date = LocalDate.of(2024, 1, 1);
date.plusDays(1);
System.out.println(date);
```

Answer:

```text
2024-01-01
```

`LocalDate` is immutable. Assign the returned value.

## 50. Spring Self-Invocation Pitfall

Question:

```java
@Service
class UserService {
    public void outer() {
        inner();
    }

    @Transactional
    public void inner() {}
}
```

Answer:

```text
@Transactional may not apply
```

Self-invocation bypasses Spring proxy behavior.

