# Java Collections Framework - Complete Guide (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** List, Set, Map, Queue, Deque, internal workings, performance, and tricky technical scenarios.

--

## Table of Contents

1. [Collections Framework Overview](#collections-framework-overview)
2. [Collection Hierarchy](#collection-hierarchy)
3. [List Interface](#list-interface)
4. [ArrayList](#arraylist)
5. [LinkedList](#linkedlist)
6. [ArrayList vs LinkedList](#arraylist-vs-linkedlist)
7. [Vector and Stack](#vector-and-stack)
8. [Set Interface](#set-interface)
9. [HashSet](#hashset)
10. [LinkedHashSet](#linkedhashset)
11. [TreeSet](#treeset)
12. [Map Interface](#map-interface)
13. [HashMap — Internal Working](#hashmap-internal-working)
14. [LinkedHashMap](#linkedhashmap)
15. [TreeMap](#treemap)
16. [Hashtable vs HashMap vs ConcurrentHashMap](#hashtable-vs-hashmap-vs-concurrenthashmap)
17. [Queue Interface](#queue-interface)
18. [PriorityQueue](#priorityqueue)
19. [Deque Interface (ArrayDeque)](#deque-interface-arraydeque)
20. [Comparable vs Comparator](#comparable-vs-comparator)
21. [Collections Utility Class](#collections-utility-class)
22. [Fail-Fast vs Fail-Safe Iterators](#fail-fast-vs-fail-safe-iterators)
23. [Immutable / Unmodifiable Collections](#immutable-unmodifiable-collections)
24. [Common Coding Challenges](#common-coding-challenges)
25. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. Collections Framework Overview

The **Java Collections Framework (JCF)** provides a unified architecture for storing and manipulating groups of objects.

### Key Components

| Component | Description |
|------|-------|
| **Interfaces** | Abstract data types (List, Set, Map, Queue) |
| **Implementations** | Concrete classes (ArrayList, HashMap, etc.) |
| **Algorithms** | Static methods in `Collections` class (sort, search, shuffle) |

### Why Collections over Arrays?

| Feature | Array | Collection |
|-----|----|------|
| **Size** | Fixed | Dynamic (grows/shrinks) |
| **Type** | Homogeneous | Homogeneous (with generics) |
| **Primitives** | Yes | No (uses wrappers) |
| **Methods** | None (only `.length`) | Rich API (add, remove, search, sort) |
| **Performance** | Faster (no overhead) | Slightly slower (boxing, overhead) |
| **Thread-safe** | No | Some implementations (Vector, ConcurrentHashMap) |

--

## 2. Collection Hierarchy

```
                        Iterable<E>
                            |
                        Collection<E>
                       /    |       \
                    List   Set     Queue
                    /       |        \
            ArrayList   HashSet   PriorityQueue
            LinkedList  LinkedHashSet  ArrayDeque
            Vector      TreeSet
              |
            Stack

                        Map<K,V>  (separate hierarchy)
                       /    |      \
                HashMap  TreeMap  Hashtable
                   |
             LinkedHashMap
```

### Key Interfaces

| Interface | Ordered | Duplicates | Null | Key Feature |
|------|-----|------|---|-------|
| `List` | Yes (indexed) | Yes | Yes | Positional access |
| `Set` | Depends | No | Depends | Uniqueness |
| `Queue` | Yes (FIFO) | Yes | Depends | Processing order |
| `Deque` | Yes | Yes | No (ArrayDeque) | Double-ended |
| `Map` | Depends | Keys: No, Values: Yes | Depends | Key-value pairs |

--

## 3. List Interface

An **ordered collection** (sequence) that allows duplicate elements and positional access.

### Common Methods

```java
List<String> list = new ArrayList<>();

// Add
list.add("A");                  // append
list.add(0, "B");              // insert at index
list.addAll(Arrays.asList("C", "D"));

// Access
String first = list.get(0);    // by index
int index = list.indexOf("A"); // first occurrence
int last = list.lastIndexOf("A");

// Modify
list.set(0, "Z");              // replace at index

// Remove
list.remove(0);                // by index
list.remove("A");              // by object (first occurrence)
list.removeIf(s -> s.length() > 1);

// Search
boolean contains = list.contains("A");
boolean empty = list.isEmpty();
int size = list.size();

// Sublist
List<String> sub = list.subList(0, 2); // [0, 2) — view, not copy

// Sort
list.sort(Comparator.naturalOrder());
Collections.sort(list);

// Convert
Object[] arr = list.toArray();
String[] strArr = list.toArray(new String[0]);
```

--

## 4. ArrayList

**Resizable array** implementation of the `List` interface.

### Internal Working

```java
// Default initial capacity = 10
ArrayList<String> list = new ArrayList<>();

// Custom initial capacity
ArrayList<String> list2 = new ArrayList<>(50);
```

### How ArrayList Grows

1. Initial capacity: **10** (default).
2. When full, new capacity = **oldCapacity + (oldCapacity >> 1)** = **1.5x**.
3. A new array is created and elements are copied (using `Arrays.copyOf`).

```
Capacity growth: 10 → 15 → 22 → 33 → 49 → 73 → ...
```

### Performance

| Operation | Time Complexity | Notes |
|------|--------|----|
| `get(index)` | **O(1)** | Direct array access |
| `add(element)` | **O(1)** amortized | O(n) when resizing |
| `add(index, element)` | **O(n)** | Shifts elements right |
| `remove(index)` | **O(n)** | Shifts elements left |
| `remove(object)` | **O(n)** | Search + shift |
| `contains(object)` | **O(n)** | Linear search |
| `set(index, element)` | **O(1)** | Direct array access |
| `size()` | **O(1)** | Stored as field |

### When to Use

- **Frequent reads** (random access by index).
- **Rare insertions/deletions** in the middle.
- Most common `List` implementation — **use by default**.

--

## 5. LinkedList

**Doubly-linked list** implementation of `List` and `Deque` interfaces.

### Internal Structure

```
null ← [prev|"A"|next] ⇄ [prev|"B"|next] ⇄ [prev|"C"|next] → null
        ^head                                    ^tail
```

Each node contains: `prev` pointer, `element`, `next` pointer.

### Performance

| Operation | Time Complexity | Notes |
|------|--------|----|
| `get(index)` | **O(n)** | Must traverse from head/tail |
| `add(element)` | **O(1)** | Append to tail |
| `add(index, element)` | **O(n)** | Traverse + O(1) insert |
| `addFirst/addLast` | **O(1)** | Direct pointer manipulation |
| `remove(index)` | **O(n)** | Traverse + O(1) remove |
| `removeFirst/removeLast` | **O(1)** | Direct pointer manipulation |
| `contains(object)` | **O(n)** | Linear search |

### LinkedList as Queue/Deque

```java
LinkedList<String> deque = new LinkedList<>();

// Queue operations (FIFO)
deque.offer("A");       // add to tail
deque.offer("B");
String head = deque.poll();  // remove from head → "A"
String peek = deque.peek();  // view head without removing → "B"

// Deque operations
deque.offerFirst("X");  // add to head
deque.offerLast("Y");   // add to tail
deque.pollFirst();      // remove from head
deque.pollLast();       // remove from tail
```

--

## 6. ArrayList vs LinkedList

| Feature | ArrayList | LinkedList |
|-----|------|------|
| **Internal** | Dynamic array | Doubly-linked list |
| **Random access** | O(1) ✅ | O(n) ❌ |
| **Add at end** | O(1) amortized | O(1) |
| **Add at beginning** | O(n) ❌ | O(1) ✅ |
| **Add in middle** | O(n) | O(n) |
| **Remove from middle** | O(n) | O(n) |
| **Memory** | Less (contiguous) | More (node overhead) |
| **Cache friendly** | Yes ✅ | No ❌ |
| **Implements** | List | List + Deque |
| **Default choice** | ✅ Yes | Only for queue/deque use |

> **Rule of thumb:** Use `ArrayList` unless you specifically need `Deque` operations or frequent add/remove at the beginning.

--

## 7. Vector and Stack

### Vector

- **Synchronized** version of ArrayList (thread-safe but slow).
- Grows by **2x** (doubles capacity), unlike ArrayList's 1.5x.
- **Legacy class** — use `ArrayList` + `Collections.synchronizedList()` or `CopyOnWriteArrayList` instead.

```java
Vector<String> vector = new Vector<>();
vector.add("A");
vector.add("B");
// Thread-safe but slow due to synchronization on every method
```

### Stack

- Extends `Vector`. LIFO (Last In, First Out).
- **Legacy class** — use `ArrayDeque` instead.

```java
Stack<Integer> stack = new Stack<>();
stack.push(1);
stack.push(2);
stack.push(3);
int top = stack.pop();   // 3
int peek = stack.peek(); // 2

// Preferred alternative
Deque<Integer> stack2 = new ArrayDeque<>();
stack2.push(1);
stack2.push(2);
stack2.pop(); // 2
```

--

## 8. Set Interface

A collection that contains **no duplicate elements**.

### Set Implementations Comparison

| Feature | HashSet | LinkedHashSet | TreeSet |
|-----|-----|--------|-----|
| **Order** | No order | Insertion order | Sorted (natural/comparator) |
| **Null** | 1 null allowed | 1 null allowed | No null (throws NPE) |
| **Performance** | O(1) | O(1) | O(log n) |
| **Internal** | HashMap | LinkedHashMap | TreeMap (Red-Black tree) |
| **Thread-safe** | No | No | No |

--

## 9. HashSet

**Unordered** set backed by a `HashMap` internally.

### How HashSet Works Internally

HashSet uses a `HashMap` where:
- The **element** is stored as the **key**.
- A **dummy constant object** (`PRESENT`) is stored as the **value**.

```java
// Internally in HashSet source code:
private transient HashMap<E, Object> map;
private static final Object PRESENT = new Object();

public boolean add(E e) {
    return map.put(e, PRESENT) == null;
}
```

### Usage

```java
Set<String> set = new HashSet<>();
set.add("Rahul");
set.add("Amit");
set.add("Sneha");
set.add("Rahul"); // duplicate — ignored, returns false

System.out.println(set); // [Amit, Rahul, Sneha] — no guaranteed order
System.out.println(set.size()); // 3
System.out.println(set.contains("Amit")); // true

set.remove("Amit");

// Iterate
for (String name : set) {
    System.out.println(name);
}

// Set operations
Set<Integer> a = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> b = new HashSet<>(Arrays.asList(3, 4, 5, 6));

// Union
Set<Integer> union = new HashSet<>(a);
union.addAll(b); // [1, 2, 3, 4, 5, 6]

// Intersection
Set<Integer> intersection = new HashSet<>(a);
intersection.retainAll(b); // [3, 4]

// Difference
Set<Integer> difference = new HashSet<>(a);
difference.removeAll(b); // [1, 2]
```

### Why equals() and hashCode() Matter

```java
class Employee {
    String name;
    int id;

    // Without overriding equals/hashCode:
    // Two Employee objects with same name and id are treated as DIFFERENT
}

Set<Employee> set = new HashSet<>();
set.add(new Employee("Rahul", 1));
set.add(new Employee("Rahul", 1)); // BOTH added! (different objects)
System.out.println(set.size()); // 2 — NOT what we want

// After overriding equals() and hashCode():
// set.size() would be 1
```

--

## 10. LinkedHashSet

**Insertion-ordered** set backed by a `LinkedHashMap`.

```java
Set<String> set = new LinkedHashSet<>();
set.add("Banana");
set.add("Apple");
set.add("Cherry");

System.out.println(set); // [Banana, Apple, Cherry] — insertion order preserved
```

### When to Use

- Need **unique elements** + **insertion order**.
- Slightly slower than `HashSet` due to maintaining linked list.

--

## 11. TreeSet

**Sorted** set backed by a `TreeMap` (Red-Black tree).

```java
Set<Integer> set = new TreeSet<>();
set.add(5);
set.add(2);
set.add(8);
set.add(1);

System.out.println(set); // [1, 2, 5, 8] — sorted

// Navigation methods
TreeSet<Integer> treeSet = new TreeSet<>(Arrays.asList(10, 20, 30, 40, 50));

System.out.println(treeSet.first());     // 10
System.out.println(treeSet.last());      // 50
System.out.println(treeSet.lower(30));   // 20 (strictly less than)
System.out.println(treeSet.higher(30));  // 40 (strictly greater than)
System.out.println(treeSet.floor(25));   // 20 (less than or equal)
System.out.println(treeSet.ceiling(25)); // 30 (greater than or equal)
System.out.println(treeSet.headSet(30)); // [10, 20] (less than 30)
System.out.println(treeSet.tailSet(30)); // [30, 40, 50] (>= 30)
System.out.println(treeSet.subSet(20, 40)); // [20, 30] (>= 20 and < 40)

// Custom sorting
Set<String> names = new TreeSet<>(Comparator.comparingInt(String::length));
names.add("Rahul");
names.add("Amit");
names.add("Sneha");
System.out.println(names); // [Amit, Rahul, Sneha] — sorted by length
// Note: "Sneha" replaces nothing because length 5 == "Rahul"'s length,
// but TreeSet uses comparator for equality — so "Sneha" is NOT added (compareTo returns 0)
```

### TreeSet Gotcha — Comparator and Equality

```java
// TreeSet uses compareTo/compare for equality, NOT equals()
TreeSet<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
set.add("Hello");
set.add("hello"); // NOT added — comparator says they're equal
System.out.println(set.size()); // 1
```

--

## 12. Map Interface

Stores **key-value pairs**. Keys must be unique; values can be duplicated.

### Common Methods

```java
Map<String, Integer> map = new HashMap<>();

// Put
map.put("Rahul", 28);
map.put("Amit", 25);
map.put("Sneha", 30);
map.put("Rahul", 29); // overwrites previous value for "Rahul"

// Get
int age = map.get("Rahul");                    // 29
int ageOrDefault = map.getOrDefault("Unknown", 0); // 0

// Check
boolean hasKey = map.containsKey("Rahul");     // true
boolean hasValue = map.containsValue(25);      // true
boolean empty = map.isEmpty();
int size = map.size();

// Remove
map.remove("Amit");
map.remove("Sneha", 30); // remove only if key maps to this value

// Iterate
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}

map.forEach((key, value) -> System.out.println(key + " = " + value));

// Views
Set<String> keys = map.keySet();
Collection<Integer> values = map.values();
Set<Map.Entry<String, Integer>> entries = map.entrySet();

// Java 8+ methods
map.putIfAbsent("Priya", 27);  // put only if key doesn't exist
map.computeIfAbsent("Karan", k -> k.length()); // compute if absent
map.computeIfPresent("Rahul", (k, v) -> v + 1); // compute if present
map.compute("Rahul", (k, v) -> v == null ? 1 : v + 1);
map.merge("Rahul", 1, Integer::sum); // merge with existing value
map.replaceAll((k, v) -> v + 1); // replace all values
```

--

## 13. HashMap — Internal Working

**Most important topic for technical reviews.** HashMap uses an **array of buckets** where each bucket is a **linked list** (or **tree** for large buckets).

### 13.1 How put() Works

```
Step 1: Calculate hashCode() of the key
Step 2: Calculate bucket index = hash & (capacity - 1)
Step 3: If bucket is empty → create new Node and place it
Step 4: If bucket has entries:
        a. Check if key already exists (using equals())
           → If yes, replace the value
        b. If key doesn't exist → add to the end of linked list
Step 5: If linked list length > 8 AND capacity >= 64
        → Convert linked list to Red-Black tree (treeification)
Step 6: If size > threshold (capacity * loadFactor)
        → Resize (double the capacity) and rehash all entries
```

### 13.2 How get() Works

```
Step 1: Calculate hashCode() of the key
Step 2: Calculate bucket index = hash & (capacity - 1)
Step 3: If bucket is empty → return null
Step 4: If bucket has entries:
        a. Check first node — if key matches (hashCode + equals) → return value
        b. If it's a tree node → search in Red-Black tree (O(log n))
        c. If it's a linked list → traverse and compare (O(n))
```

### 13.3 Key Constants

```java
static final int DEFAULT_INITIAL_CAPACITY = 16;   // default bucket count
static final float DEFAULT_LOAD_FACTOR = 0.75f;   // when to resize
static final int TREEIFY_THRESHOLD = 8;            // linked list → tree
static final int UNTREEIFY_THRESHOLD = 6;          // tree → linked list
static final int MIN_TREEIFY_CAPACITY = 64;        // min capacity for treeification
```

### 13.4 Resizing (Rehashing)

```
Initial:  16 buckets, threshold = 16 * 0.75 = 12
After 12 entries: resize to 32 buckets, threshold = 24
After 24 entries: resize to 64 buckets, threshold = 48
...

Resizing is expensive — O(n) — all entries are rehashed
```

### 13.5 Visual Example

```
HashMap with capacity 4:

Bucket 0: null
Bucket 1: [key="Amit", value=25] → [key="Priya", value=27] → null
Bucket 2: [key="Rahul", value=28] → null
Bucket 3: null

After treeification (bucket 1 has > 8 entries):
Bucket 1: Red-Black Tree
```

### 13.6 Why hashCode() and equals() Contract Matters

```java
// If two objects are equal (equals() returns true),
// they MUST have the same hashCode()

// If hashCode() is different → they go to different buckets
// → equals() is never called → duplicate keys in HashMap!

class BadKey {
    String name;

    @Override
    public boolean equals(Object o) {
        return this.name.equals(((BadKey) o).name);
    }

    // hashCode() NOT overridden — uses default Object.hashCode()
    // Two BadKey objects with same name will have DIFFERENT hashCodes
    // → They go to different buckets → HashMap treats them as different keys!
}
```

### 13.7 HashMap Allows

- **One null key** (stored in bucket 0).
- **Multiple null values**.
- **Not thread-safe** — use `ConcurrentHashMap` for thread safety.

--

## 14. LinkedHashMap

**Insertion-ordered** (or access-ordered) HashMap.

```java
Map<String, Integer> map = new LinkedHashMap<>();
map.put("Banana", 2);
map.put("Apple", 5);
map.put("Cherry", 3);

System.out.println(map); // {Banana=2, Apple=5, Cherry=3} — insertion order

// Access-order LinkedHashMap (useful for LRU cache)
Map<String, Integer> accessOrder = new LinkedHashMap<>(16, 0.75f, true);
accessOrder.put("A", 1);
accessOrder.put("B", 2);
accessOrder.put("C", 3);
accessOrder.get("A"); // accessing "A" moves it to the end
System.out.println(accessOrder); // {B=2, C=3, A=1}
```

### LRU Cache using LinkedHashMap

```java
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // access-order = true
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity; // remove oldest when capacity exceeded
    }
}

LRUCache<String, Integer> cache = new LRUCache<>(3);
cache.put("A", 1);
cache.put("B", 2);
cache.put("C", 3);
cache.get("A");       // access A → moves to end
cache.put("D", 4);    // capacity exceeded → removes B (least recently used)
System.out.println(cache); // {C=3, A=1, D=4}
```

--

## 15. TreeMap

**Sorted** map backed by a **Red-Black tree**. Keys are sorted in natural order or by a Comparator.

```java
TreeMap<String, Integer> map = new TreeMap<>();
map.put("Banana", 2);
map.put("Apple", 5);
map.put("Cherry", 3);

System.out.println(map); // {Apple=5, Banana=2, Cherry=3} — sorted by key

// Navigation methods
System.out.println(map.firstKey());           // Apple
System.out.println(map.lastKey());            // Cherry
System.out.println(map.lowerKey("Banana"));   // Apple
System.out.println(map.higherKey("Banana"));  // Cherry
System.out.println(map.floorKey("Banana"));   // Banana
System.out.println(map.ceilingKey("Banana")); // Banana

System.out.println(map.headMap("Cherry"));    // {Apple=5, Banana=2}
System.out.println(map.tailMap("Banana"));    // {Banana=2, Cherry=3}
System.out.println(map.subMap("Apple", "Cherry")); // {Apple=5, Banana=2}

// Reverse order
NavigableMap<String, Integer> reversed = map.descendingMap();
```

### Performance

| Operation | Time Complexity |
|------|--------|
| `put()` | O(log n) |
| `get()` | O(log n) |
| `remove()` | O(log n) |
| `containsKey()` | O(log n) |
| `firstKey/lastKey` | O(log n) |

--

## 16. Hashtable vs HashMap vs ConcurrentHashMap

| Feature | HashMap | Hashtable | ConcurrentHashMap |
|-----|-----|------|----------|
| **Thread-safe** | No | Yes (synchronized) | Yes (segment locking) |
| **Null key** | 1 allowed | Not allowed | Not allowed |
| **Null value** | Allowed | Not allowed | Not allowed |
| **Performance** | Fast | Slow (full lock) | Fast (fine-grained lock) |
| **Legacy** | No | Yes (Java 1.0) | No (Java 5) |
| **Iterator** | Fail-fast | Fail-fast (Enumerator is fail-safe) | Fail-safe |
| **Preferred** | Single-threaded | Never (use ConcurrentHashMap) | Multi-threaded |

### ConcurrentHashMap Internal Working

```
Java 7: Segment-based locking (16 segments by default)
         Each segment is independently lockable
         → 16 threads can write simultaneously

Java 8+: Node-level locking using CAS + synchronized
          Uses the same bucket array as HashMap
          Locks only the specific bucket being modified
          → Even more fine-grained concurrency
```

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("A", 1);
map.putIfAbsent("B", 2);

// Atomic operations
map.compute("A", (key, val) -> val + 1);
map.merge("A", 1, Integer::sum);

// Bulk operations (Java 8)
map.forEach(1, (key, value) -> System.out.println(key + "=" + value));
long count = map.mappingCount(); // preferred over size() for concurrent maps
```

--

## 17. Queue Interface

**FIFO** (First In, First Out) data structure.

### Queue Methods — Two Flavors

| Operation | Throws Exception | Returns Special Value |
|------|---------|-----------|
| **Insert** | `add(e)` | `offer(e)` → returns false |
| **Remove** | `remove()` | `poll()` → returns null |
| **Examine** | `element()` | `peek()` → returns null |

```java
Queue<String> queue = new LinkedList<>();

queue.offer("A");
queue.offer("B");
queue.offer("C");

System.out.println(queue.peek()); // A (doesn't remove)
System.out.println(queue.poll()); // A (removes)
System.out.println(queue.poll()); // B
System.out.println(queue.poll()); // C
System.out.println(queue.poll()); // null (empty)
```

--

## 18. PriorityQueue

A queue where elements are ordered by **priority** (natural order or Comparator), not insertion order.

### Internal Working

- Uses a **min-heap** (binary heap) internally.
- The **head** is always the **smallest** element (or highest priority).
- **Not thread-safe** — use `PriorityBlockingQueue` for concurrency.

```java
// Min-heap (default — natural order)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.offer(30);
minHeap.offer(10);
minHeap.offer(20);

System.out.println(minHeap.poll()); // 10 (smallest first)
System.out.println(minHeap.poll()); // 20
System.out.println(minHeap.poll()); // 30

// Max-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
maxHeap.offer(30);
maxHeap.offer(10);
maxHeap.offer(20);

System.out.println(maxHeap.poll()); // 30 (largest first)

// Custom priority
PriorityQueue<String> byLength = new PriorityQueue<>(
    Comparator.comparingInt(String::length)
);
byLength.offer("Banana");
byLength.offer("Apple");
byLength.offer("Fig");

System.out.println(byLength.poll()); // Fig (shortest)
```

### Performance

| Operation | Time Complexity |
|------|--------|
| `offer()` | O(log n) |
| `poll()` | O(log n) |
| `peek()` | O(1) |
| `remove(Object)` | O(n) |
| `contains()` | O(n) |

--

## 19. Deque Interface (ArrayDeque)

**Double-Ended Queue** — elements can be added/removed from both ends.

```java
Deque<String> deque = new ArrayDeque<>();

// Add
deque.offerFirst("A"); // add to front
deque.offerLast("B");  // add to back
deque.offerFirst("C"); // [C, A, B]

// Remove
deque.pollFirst(); // C
deque.pollLast();  // B

// Peek
deque.peekFirst(); // A
deque.peekLast();  // A
```

### ArrayDeque as Stack (Preferred over Stack class)

```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);    // pushes to front
stack.push(2);
stack.push(3);

stack.peek();     // 3 (top)
stack.pop();      // 3
stack.pop();      // 2
```

### ArrayDeque as Queue (Preferred over LinkedList for Queue)

```java
Deque<Integer> queue = new ArrayDeque<>();
queue.offer(1);   // adds to back
queue.offer(2);
queue.offer(3);

queue.poll();     // 1 (front)
queue.poll();     // 2
```

### Why ArrayDeque over LinkedList?

| Feature | ArrayDeque | LinkedList |
|-----|------|------|
| **Internal** | Resizable circular array | Doubly-linked list |
| **Memory** | Less (no node overhead) | More (prev + next pointers) |
| **Cache** | Cache-friendly ✅ | Not cache-friendly |
| **Null** | Not allowed | Allowed |
| **Performance** | Faster in practice | Slower |

--

## 20. Comparable vs Comparator

### Comparable — Natural Ordering (Inside the class)

```java
class Employee implements Comparable<Employee> {
    String name;
    double salary;

    Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    @Override
    public int compareTo(Employee other) {
        return Double.compare(this.salary, other.salary); // sort by salary
    }

    @Override
    public String toString() {
        return name + "(" + salary + ")";
    }
}

List<Employee> employees = Arrays.asList(
    new Employee("Rahul", 50000),
    new Employee("Amit", 45000),
    new Employee("Sneha", 60000)
);

Collections.sort(employees); // uses compareTo
// [Amit(45000.0), Rahul(50000.0), Sneha(60000.0)]
```

### Comparator — Custom Ordering (Outside the class)

```java
// Sort by name
Comparator<Employee> byName = Comparator.comparing(e -> e.name);

// Sort by salary descending
Comparator<Employee> bySalaryDesc = (e1, e2) ->
    Double.compare(e2.salary, e1.salary);

// Using Comparator utility methods (Java 8+)
Comparator<Employee> byNameThenSalary = Comparator
    .comparing((Employee e) -> e.name)
    .thenComparingDouble(e -> e.salary);

Comparator<Employee> bySalaryReversed = Comparator
    .comparingDouble((Employee e) -> e.salary)
    .reversed();

// Null-safe
Comparator<Employee> nullSafe = Comparator.nullsFirst(
    Comparator.comparing(e -> e.name)
);

employees.sort(byName);
employees.sort(bySalaryDesc);
```

### Comparable vs Comparator

| Feature | Comparable | Comparator |
|-----|------|------|
| **Package** | `java.lang` | `java.util` |
| **Method** | `compareTo(T o)` | `compare(T o1, T o2)` |
| **Defined in** | The class itself | Separate class/lambda |
| **Number** | One natural ordering | Multiple orderings |
| **Modifies class** | Yes | No |
| **Usage** | `Collections.sort(list)` | `Collections.sort(list, comparator)` |

--

## 21. Collections Utility Class

`java.util.Collections` provides static utility methods.

```java
List<Integer> list = new ArrayList<>(Arrays.asList(5, 3, 1, 4, 2));

// Sort
Collections.sort(list);                          // [1, 2, 3, 4, 5]
Collections.sort(list, Comparator.reverseOrder()); // [5, 4, 3, 2, 1]

// Search (list must be sorted)
int index = Collections.binarySearch(list, 3);    // returns index

// Min / Max
int min = Collections.min(list);
int max = Collections.max(list);

// Reverse
Collections.reverse(list);

// Shuffle
Collections.shuffle(list);

// Frequency
int freq = Collections.frequency(list, 3);

// Swap
Collections.swap(list, 0, 1);

// Fill
Collections.fill(list, 0); // [0, 0, 0, 0, 0]

// Unmodifiable wrappers
List<Integer> unmodifiable = Collections.unmodifiableList(list);
Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(map);

// Synchronized wrappers
List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

// Singleton collections
List<String> single = Collections.singletonList("only");
Map<String, Integer> singleMap = Collections.singletonMap("key", 1);

// Empty collections
List<String> emptyList = Collections.emptyList();
Map<String, Integer> emptyMap = Collections.emptyMap();

// nCopies
List<String> copies = Collections.nCopies(5, "Hello"); // [Hello, Hello, Hello, Hello, Hello]

// Rotate
Collections.rotate(list, 2); // rotates right by 2 positions

// Disjoint — true if no common elements
boolean disjoint = Collections.disjoint(list1, list2);
```

--

## 22. Fail-Fast vs Fail-Safe Iterators

### Fail-Fast

- Throws `ConcurrentModificationException` if the collection is modified during iteration.
- Uses a `modCount` variable to detect structural modifications.
- **All standard collections** (ArrayList, HashMap, HashSet, etc.) have fail-fast iterators.

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

// WRONG — ConcurrentModificationException
for (String s : list) {
    if (s.equals("B")) {
        list.remove(s); // modifies collection during iteration
    }
}

// CORRECT — use Iterator.remove()
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("B")) {
        it.remove(); // safe removal
    }
}

// CORRECT — use removeIf (Java 8+)
list.removeIf(s -> s.equals("B"));
```

### Fail-Safe

- Does **NOT** throw `ConcurrentModificationException`.
- Works on a **clone/snapshot** of the collection.
- **Concurrent collections** (ConcurrentHashMap, CopyOnWriteArrayList) have fail-safe iterators.

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("A", 1);
map.put("B", 2);
map.put("C", 3);

// Safe — no exception
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    if (entry.getKey().equals("B")) {
        map.remove("B"); // OK — fail-safe iterator
    }
}

CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>(
    Arrays.asList("A", "B", "C")
);
for (String s : cowList) {
    cowList.add("D"); // OK — iterates over snapshot
}
```

### Comparison

| Feature | Fail-Fast | Fail-Safe |
|-----|------|------|
| **Exception** | ConcurrentModificationException | No exception |
| **Works on** | Original collection | Clone/snapshot |
| **Memory** | No extra memory | Extra memory for clone |
| **Examples** | ArrayList, HashMap, HashSet | ConcurrentHashMap, CopyOnWriteArrayList |

--

## 23. Immutable / Unmodifiable Collections

### Java 9+ Factory Methods

```java
// Immutable — cannot add, remove, or modify
List<String> list = List.of("A", "B", "C");
Set<Integer> set = Set.of(1, 2, 3);
Map<String, Integer> map = Map.of("a", 1, "b", 2);

// list.add("D"); // UnsupportedOperationException
```

### Java 10+ copyOf

```java
List<String> mutable = new ArrayList<>(Arrays.asList("A", "B"));
List<String> immutable = List.copyOf(mutable);

mutable.add("C");  // OK
// immutable.add("C"); // UnsupportedOperationException
```

### Collections.unmodifiableXxx

```java
List<String> original = new ArrayList<>(Arrays.asList("A", "B"));
List<String> unmodifiable = Collections.unmodifiableList(original);

// unmodifiable.add("C"); // UnsupportedOperationException
original.add("C"); // OK — and this ALSO affects unmodifiable! (it's a view)
System.out.println(unmodifiable); // [A, B, C] — changed!
```

> **Important:** `Collections.unmodifiableList()` is a **view** — changes to the original affect it. `List.of()` and `List.copyOf()` create truly **independent** immutable copies.

--

## 24. Common Coding Challenges

### Problem 1: Find duplicates in a list

```java
List<Integer> nums = Arrays.asList(1, 2, 3, 2, 4, 5, 3, 6, 1);

Set<Integer> seen = new HashSet<>();
Set<Integer> duplicates = new HashSet<>();

for (int n : nums) {
    if (!seen.add(n)) {
        duplicates.add(n);
    }
}
System.out.println(duplicates); // [1, 2, 3]
```

### Problem 2: Find the first non-repeating character

```java
String str = "aabbcdeff";

Map<Character, Long> freq = new LinkedHashMap<>();
for (char c : str.toCharArray()) {
    freq.merge(c, 1L, Long::sum);
}

Character result = freq.entrySet().stream()
        .filter(e -> e.getValue() == 1)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);

System.out.println(result); // c
```

### Problem 3: Sort a map by values

```java
Map<String, Integer> map = new HashMap<>();
map.put("Rahul", 50000);
map.put("Amit", 45000);
map.put("Sneha", 60000);

// Sort by value ascending
Map<String, Integer> sorted = map.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
// {Amit=45000, Rahul=50000, Sneha=60000}
```

### Problem 4: Implement a stack using Queue

```java
class StackUsingQueue<T> {
    Queue<T> queue = new LinkedList<>();

    void push(T item) {
        queue.offer(item);
        // Rotate all elements except the last one
        for (int i = 0; i < queue.size() - 1; i++) {
            queue.offer(queue.poll());
        }
    }

    T pop() {
        return queue.poll();
    }

    T peek() {
        return queue.peek();
    }
}
```

### Problem 5: Find the intersection of two lists

```java
List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> list2 = Arrays.asList(3, 4, 5, 6, 7);

Set<Integer> set2 = new HashSet<>(list2);
List<Integer> intersection = list1.stream()
        .filter(set2::contains)
        .collect(Collectors.toList());
// [3, 4, 5]
```

### Problem 6: Count word frequency

```java
String text = "java is great java is powerful java";

Map<String, Long> wordCount = Arrays.stream(text.split(" "))
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
// {java=3, is=2, great=1, powerful=1}
```

### Problem 7: Convert List to Map

```java
List<Employee> employees = Arrays.asList(
    new Employee("Rahul", 50000),
    new Employee("Amit", 45000)
);

Map<String, Double> nameToSalary = employees.stream()
        .collect(Collectors.toMap(e -> e.name, e -> e.salary));
```

### Problem 8: Find the Kth largest element using PriorityQueue

```java
int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    for (int num : nums) {
        minHeap.offer(num);
        if (minHeap.size() > k) {
            minHeap.poll(); // remove smallest
        }
    }
    return minHeap.peek(); // kth largest
}
```

--

## 25. Key Topics & Explanations

### Topic 1: What is the difference between ArrayList and LinkedList?

**Answer:** ArrayList uses a dynamic array internally — O(1) random access but O(n) for insertions/deletions in the middle. LinkedList uses a doubly-linked list — O(1) for add/remove at ends but O(n) for random access. ArrayList is more cache-friendly and uses less memory. Use ArrayList by default; use LinkedList only for Deque operations.

--

### Topic 2: How does HashMap work internally?

**Answer:** HashMap uses an array of buckets. When we put a key-value pair: (1) hashCode() of key is calculated, (2) bucket index is computed using `hash & (capacity-1)`, (3) if bucket is empty, a new Node is created, (4) if bucket has entries, it traverses the linked list/tree checking equals() for duplicate keys. If linked list exceeds 8 nodes and capacity >= 64, it converts to a Red-Black tree. When size exceeds `capacity * loadFactor (0.75)`, the map resizes to double capacity.

--

### Topic 3: What happens if two keys have the same hashCode in HashMap?

**Answer:** This is called a **hash collision**. Both entries go to the same bucket. They are stored as a linked list (or Red-Black tree if > 8 entries). During `get()`, the map traverses the list and uses `equals()` to find the exact key. This is why both `hashCode()` and `equals()` must be properly overridden.

--

### Topic 4: Why is the initial capacity of HashMap 16 and load factor 0.75?

**Answer:** 16 is a power of 2, which allows efficient bucket index calculation using bitwise AND (`hash & 15`) instead of modulo. Load factor 0.75 is a trade-off between space and time — lower values waste space, higher values increase collisions. At 0.75, on average each bucket has 0-1 entries, giving near O(1) performance.

--

### Topic 5: What is the difference between HashMap and ConcurrentHashMap?

**Answer:** HashMap is not thread-safe. ConcurrentHashMap is thread-safe using fine-grained locking (Java 8+ uses CAS + synchronized on individual buckets). ConcurrentHashMap does not allow null keys or values. It provides atomic operations like `putIfAbsent()`, `compute()`, `merge()`. It uses fail-safe iterators that don't throw ConcurrentModificationException.

--

### Topic 6: What is the difference between fail-fast and fail-safe iterators?

**Answer:** Fail-fast iterators (ArrayList, HashMap) throw ConcurrentModificationException if the collection is modified during iteration. They use a `modCount` variable to detect changes. Fail-safe iterators (ConcurrentHashMap, CopyOnWriteArrayList) work on a clone/snapshot and don't throw exceptions, but may not reflect recent modifications.

--

### Topic 7: When should we use TreeMap over HashMap?

**Answer:** Use TreeMap when you need sorted keys, range queries (headMap, tailMap, subMap), or navigation methods (firstKey, lastKey, lowerKey, higherKey). TreeMap has O(log n) for all operations vs HashMap's O(1). Use HashMap for general-purpose key-value storage where order doesn't matter.

--

### Topic 8: How to make a collection thread-safe?

**Answer:** Options:
1. `Collections.synchronizedList/Map/Set()` — wraps with synchronized methods.
2. `ConcurrentHashMap`, `CopyOnWriteArrayList`, `ConcurrentLinkedQueue` — purpose-built concurrent collections.
3. `Vector`, `Hashtable` — legacy synchronized collections (avoid).

Prefer concurrent collections over synchronized wrappers for better performance.

--

### Topic 9: What is the difference between HashSet and TreeSet?

**Answer:** HashSet is unordered with O(1) operations, backed by HashMap. TreeSet is sorted with O(log n) operations, backed by TreeMap (Red-Black tree). HashSet allows one null; TreeSet doesn't allow null. TreeSet provides navigation methods (first, last, lower, higher). Use HashSet for general uniqueness; TreeSet when you need sorted order.

--

### Topic 10: Can we use a mutable object as a HashMap key?

**Answer:** Technically yes, but it's **dangerous**. If the key's hashCode changes after insertion (due to mutation), the entry becomes unreachable — `get()` will return null even though the entry exists. Always use **immutable objects** as HashMap keys (String, Integer, etc.) or ensure the fields used in hashCode/equals are never modified.

--

### Topic 11: What is the difference between `Collections.unmodifiableList()` and `List.of()`?

**Answer:**
- `Collections.unmodifiableList()` creates a **view** — changes to the original list are reflected.
- `List.of()` creates a truly **independent immutable** copy — no connection to any source.
- Both throw `UnsupportedOperationException` on modification attempts.

--

### Topic 12: What is CopyOnWriteArrayList?

**Answer:** A thread-safe variant of ArrayList where all mutative operations (add, set, remove) create a **new copy** of the underlying array. Reads are lock-free and fast. Writes are expensive (copy entire array). Ideal for scenarios with many reads and few writes (e.g., listener lists, configuration).

--

## Quick Reference Cheat Sheet

```
Choose the right collection:

Need ordered + duplicates?
├── Random access → ArrayList ✅
├── Frequent add/remove at ends → ArrayDeque
└── Need List + Deque → LinkedList

Need unique elements?
├── No order needed → HashSet ✅
├── Insertion order → LinkedHashSet
└── Sorted order → TreeSet

Need key-value pairs?
├── No order needed → HashMap ✅
├── Insertion order → LinkedHashMap
├── Sorted by key → TreeMap
└── Thread-safe → ConcurrentHashMap ✅

Need FIFO queue?
├── General purpose → ArrayDeque ✅
└── Priority-based → PriorityQueue

Need LIFO stack?
└── ArrayDeque ✅ (not Stack class)

Thread-safe alternatives:
├── ArrayList → CopyOnWriteArrayList / Collections.synchronizedList
├── HashMap → ConcurrentHashMap
├── HashSet → ConcurrentHashMap.newKeySet() / Collections.synchronizedSet
└── Queue → ConcurrentLinkedQueue / BlockingQueue implementations
```

--

> **Tip:** HashMap internal working is the **#1 most asked** collections topic in WITCH technical reviews. Make sure you can explain put(), get(), hashing, collision handling, treeification, and resizing. Also practice equals/hashCode contract and fail-fast vs fail-safe.
