# Java Multithreading & Concurrency - Complete Guide (Basics to Advanced)

**Target:** Interview preparation for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Threads, Synchronization, Locks, ExecutorService, CompletableFuture, Concurrent Collections, and Virtual Threads.

---

## Table of Contents

1. [What is Multithreading?](#1-what-is-multithreading)
2. [Creating Threads](#2-creating-threads)
3. [Thread Lifecycle (States)](#3-thread-lifecycle-states)
4. [Thread Methods](#4-thread-methods)
5. [Thread Priority and Daemon Threads](#5-thread-priority-and-daemon-threads)
6. [Synchronization](#6-synchronization)
7. [Inter-Thread Communication (wait, notify, notifyAll)](#7-inter-thread-communication-wait-notify-notifyall)
8. [Deadlock, Livelock, Starvation](#8-deadlock-livelock-starvation)
9. [volatile Keyword](#9-volatile-keyword)
10. [ThreadLocal](#10-threadlocal)
11. [Locks (ReentrantLock, ReadWriteLock)](#11-locks-reentrantlock-readwritelock)
12. [Executor Framework](#12-executor-framework)
13. [Callable and Future](#13-callable-and-future)
14. [CompletableFuture](#14-completablefuture)
15. [Fork/Join Framework](#15-forkjoin-framework)
16. [Concurrent Collections](#16-concurrent-collections)
17. [Atomic Variables](#17-atomic-variables)
18. [CountDownLatch, CyclicBarrier, Semaphore](#18-countdownlatch-cyclicbarrier-semaphore)
19. [BlockingQueue](#19-blockingqueue)
20. [Producer-Consumer Problem](#20-producer-consumer-problem)
21. [Virtual Threads (Java 21)](#21-virtual-threads-java-21)
22. [Common Coding Problems](#22-common-coding-problems)
23. [Interview Questions & Answers](#23-interview-questions--answers)

---

## 1. What is Multithreading?

**Multithreading** is the ability of a CPU to execute multiple threads concurrently, sharing the same process resources.

### Key Concepts

| Term | Definition |
|------|-----------|
| **Process** | An independent program in execution with its own memory space |
| **Thread** | A lightweight sub-process, the smallest unit of execution within a process |
| **Concurrency** | Multiple tasks making progress (may not be simultaneous) |
| **Parallelism** | Multiple tasks executing simultaneously on multiple cores |
| **Multitasking** | OS running multiple processes |
| **Multithreading** | Multiple threads within a single process |

### Why Multithreading?

- **Better CPU utilization** — keep CPU busy while waiting for I/O.
- **Responsive applications** — UI thread stays responsive while background work happens.
- **Faster execution** — divide work across multiple cores.
- **Resource sharing** — threads share the same memory space (heap).

### Thread Memory Model

```
Process Memory:
├── Heap (shared by all threads)
│   ├── Objects
│   └── Instance variables
├── Method Area (shared)
│   ├── Class data
│   └── Static variables
└── Thread-specific:
    ├── Stack (local variables, method calls)
    ├── Program Counter (current instruction)
    └── Native method stack
```

---

## 2. Creating Threads

### 2.1 Extending Thread Class

```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread: " + Thread.currentThread().getName());
    }
}

MyThread t = new MyThread();
t.start(); // creates new thread and calls run()
// t.run(); // WRONG — runs in current thread, no new thread created
```

### 2.2 Implementing Runnable Interface (Preferred)

```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread: " + Thread.currentThread().getName());
    }
}

Thread t = new Thread(new MyRunnable());
t.start();

// Lambda (Java 8+)
Thread t2 = new Thread(() -> System.out.println("Lambda thread"));
t2.start();
```

### 2.3 Implementing Callable Interface (Returns a result)

```java
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

Callable<Integer> task = () -> {
    Thread.sleep(1000);
    return 42;
};

FutureTask<Integer> futureTask = new FutureTask<>(task);
Thread t = new Thread(futureTask);
t.start();

Integer result = futureTask.get(); // blocks until result is available
System.out.println(result); // 42
```

### 2.4 Thread vs Runnable vs Callable

| Feature | Thread | Runnable | Callable |
|---------|--------|----------|----------|
| **Type** | Class (extends) | Interface (implements) | Interface (implements) |
| **Method** | `run()` | `run()` | `call()` |
| **Return value** | No | No | Yes |
| **Exception** | Cannot throw checked | Cannot throw checked | Can throw checked |
| **Multiple inheritance** | No (already extends Thread) | Yes ✅ | Yes ✅ |
| **Preferred** | No | Yes ✅ | Yes (when result needed) ✅ |

---

## 3. Thread Lifecycle (States)

```
        ┌─────────────────────────────────────────────┐
        │                                             │
        ▼                                             │
      NEW ──start()──► RUNNABLE ──scheduler──► RUNNING
                          ▲                      │  │
                          │                      │  │
                     notify/                     │  │
                     notifyAll                   │  │
                          │                      │  │
                       WAITING ◄──wait()─────────┘  │
                       TIMED_WAITING ◄──sleep()─────┘
                                                    │
                                              TERMINATED
```

### Thread States (Thread.State enum)

| State | Description |
|-------|-------------|
| **NEW** | Thread created but `start()` not yet called |
| **RUNNABLE** | Thread is ready to run or currently running |
| **BLOCKED** | Waiting to acquire a monitor lock (synchronized) |
| **WAITING** | Waiting indefinitely (`wait()`, `join()`, `LockSupport.park()`) |
| **TIMED_WAITING** | Waiting for a specified time (`sleep()`, `wait(timeout)`, `join(timeout)`) |
| **TERMINATED** | Thread has completed execution |

```java
Thread t = new Thread(() -> {
    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
});

System.out.println(t.getState()); // NEW
t.start();
System.out.println(t.getState()); // RUNNABLE
Thread.sleep(500);
System.out.println(t.getState()); // TIMED_WAITING
t.join();
System.out.println(t.getState()); // TERMINATED
```

---

## 4. Thread Methods

### 4.1 sleep(long millis)

Pauses the current thread for the specified time. **Does NOT release the lock.**

```java
try {
    Thread.sleep(2000); // sleep for 2 seconds
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

### 4.2 join()

Makes the current thread wait until the specified thread completes.

```java
Thread t1 = new Thread(() -> {
    System.out.println("Task 1 started");
    try { Thread.sleep(2000); } catch (InterruptedException e) {}
    System.out.println("Task 1 completed");
});

Thread t2 = new Thread(() -> {
    System.out.println("Task 2 started");
    try { Thread.sleep(1000); } catch (InterruptedException e) {}
    System.out.println("Task 2 completed");
});

t1.start();
t2.start();

t1.join(); // main thread waits for t1 to finish
t2.join(); // main thread waits for t2 to finish

System.out.println("Both tasks completed");
```

### 4.3 yield()

Hints to the scheduler that the current thread is willing to give up its time slice. **Not guaranteed.**

```java
Thread.yield(); // hint to scheduler — may or may not work
```

### 4.4 interrupt()

Requests a thread to stop. Sets the interrupt flag.

```java
Thread t = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        System.out.println("Working...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
            Thread.currentThread().interrupt(); // restore interrupt flag
            break;
        }
    }
});

t.start();
Thread.sleep(3000);
t.interrupt(); // request thread to stop
```

### 4.5 setName() / getName() / currentThread()

```java
Thread t = new Thread(() -> {
    System.out.println("Running in: " + Thread.currentThread().getName());
});
t.setName("Worker-1");
t.start();
```

---

## 5. Thread Priority and Daemon Threads

### 5.1 Thread Priority

```java
// Priority range: 1 (MIN) to 10 (MAX), default = 5 (NORM)
Thread t = new Thread(() -> {});
t.setPriority(Thread.MAX_PRIORITY);  // 10
t.setPriority(Thread.MIN_PRIORITY);  // 1
t.setPriority(Thread.NORM_PRIORITY); // 5

// Priority is a HINT — not guaranteed by all OS schedulers
```

### 5.2 Daemon Threads

Background threads that don't prevent JVM from exiting. JVM exits when all non-daemon threads finish.

```java
Thread daemon = new Thread(() -> {
    while (true) {
        System.out.println("Daemon running...");
        try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
    }
});

daemon.setDaemon(true); // must be set BEFORE start()
daemon.start();

// When main thread (non-daemon) finishes, JVM exits — daemon thread is killed
```

**Examples of daemon threads:** Garbage Collector, Finalizer thread.

### Daemon Thread Rules

- Must call `setDaemon(true)` **before** `start()`.
- A thread created by a daemon thread is also a daemon thread.
- `main` thread is a **non-daemon** thread.

---

## 6. Synchronization

Synchronization ensures that only **one thread** can access a shared resource at a time.

### 6.1 The Problem — Race Condition

```java
class Counter {
    int count = 0;

    void increment() {
        count++; // NOT atomic: read → modify → write
    }
}

Counter counter = new Counter();

Thread t1 = new Thread(() -> {
    for (int i = 0; i < 10000; i++) counter.increment();
});
Thread t2 = new Thread(() -> {
    for (int i = 0; i < 10000; i++) counter.increment();
});

t1.start(); t2.start();
t1.join(); t2.join();

System.out.println(counter.count); // Expected: 20000, Actual: random (e.g., 17834)
```

### 6.2 Synchronized Method

```java
class Counter {
    int count = 0;

    synchronized void increment() { // locks on 'this' object
        count++;
    }
}
// Now count will always be 20000
```

### 6.3 Synchronized Block

More fine-grained — locks only the critical section.

```java
class Counter {
    int count = 0;
    private final Object lock = new Object();

    void increment() {
        // Non-critical code here (no lock needed)
        synchronized (lock) { // locks on specific object
            count++;
        }
        // More non-critical code
    }
}
```

### 6.4 Static Synchronized

Locks on the **Class object** (not instance).

```java
class Counter {
    static int count = 0;

    static synchronized void increment() { // locks on Counter.class
        count++;
    }

    // Equivalent to:
    static void increment2() {
        synchronized (Counter.class) {
            count++;
        }
    }
}
```

### 6.5 Synchronized Rules

| Rule | Description |
|------|-------------|
| **Instance synchronized** | Locks on `this` — different instances have different locks |
| **Static synchronized** | Locks on `Class` object — shared across all instances |
| **Reentrant** | A thread can acquire the same lock multiple times |
| **No lock on read** | Reads without synchronization may see stale values |
| **Performance** | Synchronization has overhead — use only when needed |

---

## 7. Inter-Thread Communication (wait, notify, notifyAll)

These methods are defined in `Object` class and must be called from within a `synchronized` block.

### 7.1 wait()

Releases the lock and puts the thread in WAITING state until notified.

### 7.2 notify()

Wakes up **one** waiting thread (arbitrary choice).

### 7.3 notifyAll()

Wakes up **all** waiting threads.

### 7.4 Example — Producer-Consumer (Basic)

```java
class SharedBuffer {
    private int data;
    private boolean hasData = false;

    synchronized void produce(int value) throws InterruptedException {
        while (hasData) {
            wait(); // wait until consumer consumes
        }
        data = value;
        hasData = true;
        System.out.println("Produced: " + value);
        notify(); // notify consumer
    }

    synchronized int consume() throws InterruptedException {
        while (!hasData) {
            wait(); // wait until producer produces
        }
        hasData = false;
        System.out.println("Consumed: " + data);
        notify(); // notify producer
        return data;
    }
}

SharedBuffer buffer = new SharedBuffer();

Thread producer = new Thread(() -> {
    for (int i = 1; i <= 5; i++) {
        try { buffer.produce(i); } catch (InterruptedException e) {}
    }
});

Thread consumer = new Thread(() -> {
    for (int i = 1; i <= 5; i++) {
        try { buffer.consume(); } catch (InterruptedException e) {}
    }
});

producer.start();
consumer.start();
```

### 7.5 Why Use while Loop Instead of if?

```java
// WRONG — spurious wakeup can cause issues
synchronized (lock) {
    if (!condition) {
        wait();
    }
    // proceed — but condition might still be false (spurious wakeup!)
}

// CORRECT — always use while
synchronized (lock) {
    while (!condition) {
        wait();
    }
    // proceed — condition is guaranteed to be true
}
```

### 7.6 wait() vs sleep()

| Feature | wait() | sleep() |
|---------|--------|---------|
| **Class** | Object | Thread |
| **Lock** | Releases lock ✅ | Does NOT release lock ❌ |
| **Where** | Inside synchronized block | Anywhere |
| **Wakeup** | notify/notifyAll or timeout | After specified time |
| **Purpose** | Inter-thread communication | Pause execution |

---

## 8. Deadlock, Livelock, Starvation

### 8.1 Deadlock

Two or more threads are **blocked forever**, each waiting for the other to release a lock.

```java
Object lock1 = new Object();
Object lock2 = new Object();

Thread t1 = new Thread(() -> {
    synchronized (lock1) {
        System.out.println("T1: holding lock1");
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        synchronized (lock2) { // waiting for lock2 (held by T2)
            System.out.println("T1: holding lock1 & lock2");
        }
    }
});

Thread t2 = new Thread(() -> {
    synchronized (lock2) {
        System.out.println("T2: holding lock2");
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        synchronized (lock1) { // waiting for lock1 (held by T1)
            System.out.println("T2: holding lock1 & lock2");
        }
    }
});

t1.start();
t2.start();
// DEADLOCK — both threads wait forever
```

### 8.2 How to Prevent Deadlock

1. **Lock ordering** — always acquire locks in the same order.
2. **Lock timeout** — use `tryLock()` with timeout.
3. **Avoid nested locks** — minimize synchronized blocks.
4. **Use higher-level concurrency utilities** — `java.util.concurrent`.

```java
// FIX: Consistent lock ordering
Thread t1 = new Thread(() -> {
    synchronized (lock1) {
        synchronized (lock2) { /* ... */ }
    }
});

Thread t2 = new Thread(() -> {
    synchronized (lock1) { // same order as t1
        synchronized (lock2) { /* ... */ }
    }
});
```

### 8.3 Livelock

Threads are not blocked but keep responding to each other without making progress.

```java
// Example: Two people in a corridor, both keep stepping aside for each other
// Neither is blocked, but neither makes progress
```

### 8.4 Starvation

A thread is perpetually denied access to a resource because other threads keep acquiring it.

- Caused by thread priority issues or unfair locks.
- Solution: Use fair locks (`new ReentrantLock(true)`).

---

## 9. volatile Keyword

`volatile` ensures that a variable's value is always read from **main memory** (not from thread's local cache).

### The Problem — Visibility

```java
class Flag {
    boolean running = true; // may be cached by threads

    void stop() {
        running = false; // T1 writes to its cache
    }

    void run() {
        while (running) { // T2 reads from its cache — may never see false!
            // infinite loop possible
        }
    }
}
```

### The Fix — volatile

```java
class Flag {
    volatile boolean running = true; // always read from main memory

    void stop() {
        running = false; // writes directly to main memory
    }

    void run() {
        while (running) { // reads from main memory — sees the update
            // will stop when running becomes false
        }
    }
}
```

### volatile vs synchronized

| Feature | volatile | synchronized |
|---------|----------|-------------|
| **Visibility** | Yes ✅ | Yes ✅ |
| **Atomicity** | No ❌ | Yes ✅ |
| **Use case** | Simple flags, single read/write | Compound operations (check-then-act) |
| **Performance** | Faster | Slower (lock overhead) |
| **Blocking** | No | Yes |

```java
// volatile is NOT enough for count++
volatile int count = 0;
count++; // NOT atomic: read → increment → write (race condition still possible)

// Use synchronized or AtomicInteger for count++
```

---

## 10. ThreadLocal

`ThreadLocal` provides **thread-specific** variables. Each thread has its own independent copy.

```java
ThreadLocal<String> threadLocal = new ThreadLocal<>();

Thread t1 = new Thread(() -> {
    threadLocal.set("Thread-1 data");
    System.out.println(threadLocal.get()); // Thread-1 data
});

Thread t2 = new Thread(() -> {
    threadLocal.set("Thread-2 data");
    System.out.println(threadLocal.get()); // Thread-2 data
});

t1.start();
t2.start();
```

### Common Use Cases

```java
// 1. SimpleDateFormat (not thread-safe) — one per thread
ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(
    () -> new SimpleDateFormat("dd-MM-yyyy")
);

// 2. Database connection per thread
ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

// 3. User context in web applications
ThreadLocal<UserContext> userContext = new ThreadLocal<>();
```

### Important: Always Clean Up

```java
try {
    threadLocal.set("value");
    // use value
} finally {
    threadLocal.remove(); // IMPORTANT — prevent memory leaks in thread pools
}
```

---

## 11. Locks (ReentrantLock, ReadWriteLock)

`java.util.concurrent.locks` provides more flexible locking than `synchronized`.

### 11.1 ReentrantLock

```java
import java.util.concurrent.locks.ReentrantLock;

class Counter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock(); // ALWAYS unlock in finally
        }
    }

    int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
```

### 11.2 tryLock() — Non-blocking

```java
if (lock.tryLock()) {
    try {
        // critical section
    } finally {
        lock.unlock();
    }
} else {
    System.out.println("Could not acquire lock — doing something else");
}

// With timeout
if (lock.tryLock(2, TimeUnit.SECONDS)) {
    try {
        // critical section
    } finally {
        lock.unlock();
    }
}
```

### 11.3 Fair Lock

```java
// Fair lock — threads acquire lock in FIFO order (prevents starvation)
ReentrantLock fairLock = new ReentrantLock(true);

// Unfair lock (default) — no ordering guarantee, but faster
ReentrantLock unfairLock = new ReentrantLock(false);
```

### 11.4 ReadWriteLock

Allows **multiple readers** OR **one writer** at a time.

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Cache {
    private final Map<String, String> map = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    String get(String key) {
        rwLock.readLock().lock(); // multiple threads can read simultaneously
        try {
            return map.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    void put(String key, String value) {
        rwLock.writeLock().lock(); // exclusive access for writing
        try {
            map.put(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
```

### 11.5 ReentrantLock vs synchronized

| Feature | synchronized | ReentrantLock |
|---------|-------------|---------------|
| **Lock/Unlock** | Automatic | Manual (lock/unlock) |
| **tryLock** | No | Yes ✅ |
| **Timeout** | No | Yes ✅ |
| **Fair lock** | No | Yes ✅ |
| **Interruptible** | No | Yes (`lockInterruptibly()`) |
| **Condition** | wait/notify | `Condition` objects (multiple) |
| **Performance** | Similar | Similar (slightly more flexible) |
| **Simplicity** | Simpler ✅ | More verbose |

---

## 12. Executor Framework

The Executor framework manages thread pools, eliminating the need to manually create and manage threads.

### 12.1 Why Executor Framework?

```java
// BAD — creating threads manually
for (int i = 0; i < 1000; i++) {
    new Thread(() -> doWork()).start(); // 1000 threads! Resource waste
}

// GOOD — using thread pool
ExecutorService executor = Executors.newFixedThreadPool(10);
for (int i = 0; i < 1000; i++) {
    executor.submit(() -> doWork()); // reuses 10 threads
}
executor.shutdown();
```

### 12.2 Types of Thread Pools

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 1. Fixed Thread Pool — fixed number of threads
ExecutorService fixed = Executors.newFixedThreadPool(5);
// Use when: known number of concurrent tasks

// 2. Cached Thread Pool — creates threads as needed, reuses idle ones
ExecutorService cached = Executors.newCachedThreadPool();
// Use when: many short-lived tasks, unpredictable load

// 3. Single Thread Executor — one thread, tasks execute sequentially
ExecutorService single = Executors.newSingleThreadExecutor();
// Use when: tasks must execute in order

// 4. Scheduled Thread Pool — schedule tasks with delay or periodically
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(3);
// Use when: periodic tasks (cron-like)

// 5. Work Stealing Pool (Java 8+) — uses ForkJoinPool
ExecutorService workStealing = Executors.newWorkStealingPool();
// Use when: divide-and-conquer tasks

// 6. Virtual Thread Executor (Java 21+)
ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();
// Use when: high-throughput I/O-bound tasks
```

### 12.3 Submitting Tasks

```java
ExecutorService executor = Executors.newFixedThreadPool(3);

// execute() — fire and forget (Runnable)
executor.execute(() -> System.out.println("Task 1"));

// submit() — returns Future (Runnable or Callable)
Future<?> future1 = executor.submit(() -> System.out.println("Task 2"));
Future<Integer> future2 = executor.submit(() -> 42);

// invokeAll() — submit multiple tasks, wait for all
List<Callable<String>> tasks = Arrays.asList(
    () -> "Result 1",
    () -> "Result 2",
    () -> "Result 3"
);
List<Future<String>> futures = executor.invokeAll(tasks);

// invokeAny() — returns result of first completed task
String first = executor.invokeAny(tasks);
```

### 12.4 Shutting Down

```java
executor.shutdown();       // no new tasks, finish existing
executor.shutdownNow();    // attempt to stop all tasks immediately

// Best practice
executor.shutdown();
if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
    executor.shutdownNow();
}
```

### 12.5 ScheduledExecutorService

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

// Run after 5 seconds delay
scheduler.schedule(() -> System.out.println("Delayed task"), 5, TimeUnit.SECONDS);

// Run every 3 seconds (fixed rate)
scheduler.scheduleAtFixedRate(
    () -> System.out.println("Periodic task"),
    0,    // initial delay
    3,    // period
    TimeUnit.SECONDS
);

// Run with 3 seconds delay between end of one execution and start of next
scheduler.scheduleWithFixedDelay(
    () -> System.out.println("Delayed periodic"),
    0,    // initial delay
    3,    // delay between executions
    TimeUnit.SECONDS
);
```

### 12.6 Custom ThreadPoolExecutor

```java
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

ThreadPoolExecutor executor = new ThreadPoolExecutor(
    2,                      // corePoolSize
    5,                      // maximumPoolSize
    60,                     // keepAliveTime
    TimeUnit.SECONDS,       // time unit
    new LinkedBlockingQueue<>(100), // work queue
    new ThreadPoolExecutor.CallerRunsPolicy() // rejection policy
);

// Rejection Policies:
// AbortPolicy (default) — throws RejectedExecutionException
// CallerRunsPolicy — caller thread executes the task
// DiscardPolicy — silently discards the task
// DiscardOldestPolicy — discards oldest task in queue
```

---

## 13. Callable and Future

### 13.1 Callable — Returns a result

```java
Callable<Integer> task = () -> {
    Thread.sleep(2000);
    return 42;
};

ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Integer> future = executor.submit(task);

// Do other work while task executes...

Integer result = future.get(); // blocks until result is ready
System.out.println(result); // 42

executor.shutdown();
```

### 13.2 Future Methods

```java
Future<Integer> future = executor.submit(task);

// Check if done
boolean done = future.isDone();

// Check if cancelled
boolean cancelled = future.isCancelled();

// Cancel the task
future.cancel(true); // true = interrupt if running

// Get with timeout
Integer result = future.get(5, TimeUnit.SECONDS); // throws TimeoutException if not done
```

### 13.3 Limitations of Future

- `get()` is **blocking** — no way to attach callbacks.
- Cannot **combine** multiple futures.
- Cannot **chain** operations.
- No **exception handling** mechanism.

**Solution:** Use `CompletableFuture` (Java 8+).

---

## 14. CompletableFuture

`CompletableFuture` is a powerful asynchronous programming tool that supports **non-blocking**, **chainable**, and **combinable** operations.

### 14.1 Creating CompletableFuture

```java
import java.util.concurrent.CompletableFuture;

// Run async task (no return value)
CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> {
    System.out.println("Running async task");
});

// Supply async task (returns value)
CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
    return "Hello from async";
});

String result = cf2.get(); // "Hello from async"

// With custom executor
ExecutorService executor = Executors.newFixedThreadPool(3);
CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> "Custom", executor);
```

### 14.2 Chaining — thenApply, thenAccept, thenRun

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")        // transform result (Function)
    .thenApply(String::toUpperCase);     // chain another transformation

System.out.println(future.get()); // HELLO WORLD

// thenAccept — consume result (Consumer)
CompletableFuture.supplyAsync(() -> "Hello")
    .thenAccept(System.out::println); // prints "Hello"

// thenRun — run after completion (Runnable, no access to result)
CompletableFuture.supplyAsync(() -> "Hello")
    .thenRun(() -> System.out.println("Done!"));
```

### 14.3 Async Variants

```java
// thenApplyAsync — runs transformation on a different thread
CompletableFuture.supplyAsync(() -> "Hello")
    .thenApplyAsync(s -> s + " World")  // may run on different thread
    .thenAccept(System.out::println);
```

### 14.4 Combining Two Futures

```java
// thenCombine — combine results of two independent futures
CompletableFuture<String> name = CompletableFuture.supplyAsync(() -> "Rahul");
CompletableFuture<Integer> age = CompletableFuture.supplyAsync(() -> 28);

CompletableFuture<String> combined = name.thenCombine(age,
    (n, a) -> n + " is " + a + " years old");

System.out.println(combined.get()); // Rahul is 28 years old

// thenCompose — chain dependent futures (flatMap equivalent)
CompletableFuture<String> result = CompletableFuture
    .supplyAsync(() -> "user123")
    .thenCompose(userId -> fetchUserDetails(userId)); // returns another CompletableFuture
```

### 14.5 Combining Multiple Futures

```java
// allOf — wait for ALL futures to complete
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Result 1");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "Result 2");
CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "Result 3");

CompletableFuture<Void> allDone = CompletableFuture.allOf(f1, f2, f3);
allDone.get(); // blocks until all complete

// Get all results
List<String> results = Stream.of(f1, f2, f3)
    .map(CompletableFuture::join)
    .collect(Collectors.toList());

// anyOf — returns when ANY future completes
CompletableFuture<Object> anyDone = CompletableFuture.anyOf(f1, f2, f3);
System.out.println(anyDone.get()); // first completed result
```

### 14.6 Exception Handling

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        if (true) throw new RuntimeException("Something went wrong");
        return "Success";
    })
    // exceptionally — handle exception, return fallback
    .exceptionally(ex -> {
        System.out.println("Error: " + ex.getMessage());
        return "Fallback value";
    });

System.out.println(future.get()); // Fallback value

// handle — handle both success and failure
CompletableFuture<String> handled = CompletableFuture
    .supplyAsync(() -> "Success")
    .handle((result, ex) -> {
        if (ex != null) return "Error: " + ex.getMessage();
        return "Result: " + result;
    });

// whenComplete — like handle but doesn't transform the result
CompletableFuture<String> completed = CompletableFuture
    .supplyAsync(() -> "Success")
    .whenComplete((result, ex) -> {
        if (ex != null) System.out.println("Failed: " + ex);
        else System.out.println("Completed: " + result);
    });
```

### 14.7 Real-World Example — Parallel API Calls

```java
CompletableFuture<User> userFuture = CompletableFuture
    .supplyAsync(() -> userService.getUser(userId));

CompletableFuture<List<Order>> ordersFuture = CompletableFuture
    .supplyAsync(() -> orderService.getOrders(userId));

CompletableFuture<Address> addressFuture = CompletableFuture
    .supplyAsync(() -> addressService.getAddress(userId));

// Combine all three (run in parallel)
CompletableFuture<UserProfile> profileFuture = userFuture
    .thenCombine(ordersFuture, (user, orders) -> new UserWithOrders(user, orders))
    .thenCombine(addressFuture, (userWithOrders, address) ->
        new UserProfile(userWithOrders, address));

UserProfile profile = profileFuture.get();
```

### 14.8 thenApply vs thenCompose

| Method | Analogy | Returns | Use When |
|--------|---------|---------|----------|
| `thenApply` | `map()` | `CompletableFuture<R>` | Synchronous transformation |
| `thenCompose` | `flatMap()` | `CompletableFuture<R>` (flattened) | Async transformation (returns CF) |

---

## 15. Fork/Join Framework

Designed for **divide-and-conquer** tasks that can be split into smaller subtasks.

### 15.1 How It Works

```
Main Task
├── Subtask 1
│   ├── Sub-subtask 1a
│   └── Sub-subtask 1b
└── Subtask 2
    ├── Sub-subtask 2a
    └── Sub-subtask 2b

Each subtask runs on a separate thread from the ForkJoinPool.
Work-stealing: idle threads steal tasks from busy threads' queues.
```

### 15.2 RecursiveTask (Returns a result)

```java
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

class SumTask extends RecursiveTask<Long> {
    private final long[] array;
    private final int start, end;
    private static final int THRESHOLD = 1000;

    SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            // Base case — compute directly
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }

        // Divide
        int mid = (start + end) / 2;
        SumTask left = new SumTask(array, start, mid);
        SumTask right = new SumTask(array, mid, end);

        left.fork();  // submit left task to pool
        long rightResult = right.compute(); // compute right in current thread
        long leftResult = left.join();      // wait for left result

        return leftResult + rightResult;
    }
}

// Usage
long[] array = LongStream.rangeClosed(1, 1_000_000).toArray();
ForkJoinPool pool = new ForkJoinPool();
long sum = pool.invoke(new SumTask(array, 0, array.length));
System.out.println(sum); // 500000500000
```

### 15.3 RecursiveAction (No return value)

```java
class SortTask extends RecursiveAction {
    private final int[] array;
    private final int start, end;

    @Override
    protected void compute() {
        if (end - start <= 1000) {
            Arrays.sort(array, start, end); // base case
            return;
        }
        int mid = (start + end) / 2;
        invokeAll(
            new SortTask(array, start, mid),
            new SortTask(array, mid, end)
        );
        merge(array, start, mid, end); // merge sorted halves
    }
}
```

---

## 16. Concurrent Collections

Thread-safe collections in `java.util.concurrent`.

### 16.1 ConcurrentHashMap

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("A", 1);
map.putIfAbsent("B", 2);
map.compute("A", (key, val) -> val + 1);
map.merge("A", 1, Integer::sum);

// Safe iteration — no ConcurrentModificationException
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    map.put("C", 3); // safe during iteration
}
```

### 16.2 CopyOnWriteArrayList

```java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
list.add("A");
list.add("B");

// Safe iteration — iterates over snapshot
for (String s : list) {
    list.add("C"); // safe — doesn't affect current iteration
}
// After loop: [A, B, C, C] (C added twice — once per iteration)
```

**Use when:** Many reads, few writes (e.g., listener lists).

### 16.3 ConcurrentLinkedQueue

```java
ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
queue.offer("A");
queue.offer("B");
String head = queue.poll(); // A
```

**Lock-free** implementation using CAS (Compare-And-Swap).

### 16.4 ConcurrentSkipListMap / ConcurrentSkipListSet

Thread-safe sorted map/set (concurrent alternative to TreeMap/TreeSet).

```java
ConcurrentSkipListMap<String, Integer> sortedMap = new ConcurrentSkipListMap<>();
sortedMap.put("Banana", 2);
sortedMap.put("Apple", 5);
System.out.println(sortedMap); // {Apple=5, Banana=2} — sorted
```

---

## 17. Atomic Variables

`java.util.concurrent.atomic` provides lock-free, thread-safe operations on single variables.

### 17.1 AtomicInteger

```java
import java.util.concurrent.atomic.AtomicInteger;

AtomicInteger counter = new AtomicInteger(0);

counter.incrementAndGet();  // ++counter (returns new value)
counter.getAndIncrement();  // counter++ (returns old value)
counter.decrementAndGet();  // --counter
counter.addAndGet(5);       // counter += 5
counter.get();              // read
counter.set(10);            // write

// Compare-And-Swap (CAS)
counter.compareAndSet(10, 20); // if current == 10, set to 20

// Atomic update
counter.updateAndGet(x -> x * 2); // atomically double the value
counter.accumulateAndGet(5, Integer::sum); // atomically add 5
```

### 17.2 Other Atomic Types

```java
AtomicLong atomicLong = new AtomicLong(0);
AtomicBoolean atomicBoolean = new AtomicBoolean(false);
AtomicReference<String> atomicRef = new AtomicReference<>("Hello");

// AtomicIntegerArray
AtomicIntegerArray atomicArray = new AtomicIntegerArray(10);
atomicArray.incrementAndGet(0); // increment element at index 0
```

### 17.3 LongAdder (Java 8+) — Better for High Contention

```java
import java.util.concurrent.atomic.LongAdder;

LongAdder adder = new LongAdder();
adder.increment();
adder.add(5);
long sum = adder.sum(); // get current sum

// LongAdder is faster than AtomicLong under high contention
// because it uses striped cells to reduce CAS contention
```

### 17.4 When to Use What

| Scenario | Solution |
|----------|----------|
| Simple counter | `AtomicInteger` |
| High-contention counter | `LongAdder` |
| Boolean flag | `AtomicBoolean` or `volatile boolean` |
| Reference swap | `AtomicReference` |
| Complex operations | `synchronized` or `ReentrantLock` |

---

## 18. CountDownLatch, CyclicBarrier, Semaphore

### 18.1 CountDownLatch

Allows one or more threads to **wait** until a set of operations in other threads completes.

```java
import java.util.concurrent.CountDownLatch;

CountDownLatch latch = new CountDownLatch(3); // count = 3

// Three worker threads
for (int i = 1; i <= 3; i++) {
    final int taskId = i;
    new Thread(() -> {
        System.out.println("Task " + taskId + " completed");
        latch.countDown(); // decrement count
    }).start();
}

latch.await(); // main thread waits until count reaches 0
System.out.println("All tasks completed!");

// With timeout
latch.await(10, TimeUnit.SECONDS);
```

**Use case:** Wait for multiple services to initialize before starting the application.

**Note:** CountDownLatch is **one-time use** — cannot be reset.

### 18.2 CyclicBarrier

All threads wait at a barrier point until all have arrived, then proceed together. **Reusable.**

```java
import java.util.concurrent.CyclicBarrier;

CyclicBarrier barrier = new CyclicBarrier(3, () -> {
    System.out.println("All threads reached barrier — proceeding!");
});

for (int i = 1; i <= 3; i++) {
    final int id = i;
    new Thread(() -> {
        System.out.println("Thread " + id + " doing phase 1");
        try {
            barrier.await(); // wait for all threads
        } catch (Exception e) {}

        System.out.println("Thread " + id + " doing phase 2");
        try {
            barrier.await(); // reusable — wait again
        } catch (Exception e) {}

        System.out.println("Thread " + id + " done");
    }).start();
}
```

**Use case:** Multi-phase computation where all threads must complete phase N before starting phase N+1.

### 18.3 CountDownLatch vs CyclicBarrier

| Feature | CountDownLatch | CyclicBarrier |
|---------|---------------|---------------|
| **Reusable** | No (one-time) | Yes ✅ |
| **Who waits** | One thread waits for N | N threads wait for each other |
| **Count** | Decremented by `countDown()` | Automatically managed |
| **Barrier action** | No | Yes (Runnable on barrier trip) |

### 18.4 Semaphore

Controls access to a shared resource by limiting the number of concurrent threads.

```java
import java.util.concurrent.Semaphore;

Semaphore semaphore = new Semaphore(3); // max 3 concurrent threads

for (int i = 1; i <= 10; i++) {
    final int id = i;
    new Thread(() -> {
        try {
            semaphore.acquire(); // acquire permit (blocks if none available)
            System.out.println("Thread " + id + " accessing resource");
            Thread.sleep(2000); // simulate work
        } catch (InterruptedException e) {
        } finally {
            semaphore.release(); // release permit
            System.out.println("Thread " + id + " released");
        }
    }).start();
}
```

**Use case:** Connection pool, rate limiting, resource pooling.

```java
// Binary semaphore (mutex) — same as lock
Semaphore mutex = new Semaphore(1);

// tryAcquire with timeout
if (semaphore.tryAcquire(5, TimeUnit.SECONDS)) {
    try {
        // use resource
    } finally {
        semaphore.release();
    }
}
```

---

## 19. BlockingQueue

A thread-safe queue that **blocks** when trying to dequeue from an empty queue or enqueue to a full queue.

### 19.1 Types

| Implementation | Bounded | Ordering | Notes |
|---------------|---------|----------|-------|
| `ArrayBlockingQueue` | Yes | FIFO | Fixed capacity |
| `LinkedBlockingQueue` | Optional | FIFO | Optionally bounded |
| `PriorityBlockingQueue` | No | Priority | Unbounded |
| `SynchronousQueue` | 0 capacity | N/A | Direct handoff |
| `DelayQueue` | No | Delay | Elements available after delay |

### 19.2 Key Methods

| Operation | Blocks | Throws Exception | Returns Special Value | Times Out |
|-----------|--------|------------------|-----------------------|-----------|
| **Insert** | `put(e)` | `add(e)` | `offer(e)` | `offer(e, time, unit)` |
| **Remove** | `take()` | `remove()` | `poll()` | `poll(time, unit)` |
| **Examine** | N/A | `element()` | `peek()` | N/A |

```java
BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

// Producer
queue.put("item"); // blocks if queue is full

// Consumer
String item = queue.take(); // blocks if queue is empty
```

---

## 20. Producer-Consumer Problem

The classic concurrency problem — solved elegantly with BlockingQueue.

### 20.1 Using BlockingQueue (Recommended)

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

class ProducerConsumer {
    private final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            queue.put(value); // blocks if queue is full
            System.out.println("Produced: " + value);
            value++;
            Thread.sleep(500);
        }
    }

    void consume() throws InterruptedException {
        while (true) {
            int value = queue.take(); // blocks if queue is empty
            System.out.println("Consumed: " + value);
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();

        new Thread(() -> {
            try { pc.produce(); } catch (InterruptedException e) {}
        }).start();

        new Thread(() -> {
            try { pc.consume(); } catch (InterruptedException e) {}
        }).start();
    }
}
```

### 20.2 Using wait/notify (Traditional)

```java
class Buffer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    Buffer(int capacity) {
        this.capacity = capacity;
    }

    synchronized void produce(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // buffer full — wait
        }
        queue.offer(item);
        System.out.println("Produced: " + item);
        notifyAll(); // notify consumers
    }

    synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // buffer empty — wait
        }
        int item = queue.poll();
        System.out.println("Consumed: " + item);
        notifyAll(); // notify producers
        return item;
    }
}
```

---

## 21. Virtual Threads (Java 21)

Virtual threads are **lightweight threads** managed by the JVM, not the OS. They enable creating **millions of threads** without the overhead of platform threads.

### 21.1 Platform Threads vs Virtual Threads

| Feature | Platform Thread | Virtual Thread |
|---------|----------------|----------------|
| **Managed by** | OS | JVM |
| **Memory** | ~1 MB stack | ~few KB |
| **Creation cost** | Expensive | Cheap |
| **Max count** | ~thousands | ~millions |
| **Blocking** | Blocks OS thread | Unmounts from carrier thread |
| **Best for** | CPU-bound tasks | I/O-bound tasks |

### 21.2 Creating Virtual Threads

```java
// Method 1: Thread.ofVirtual()
Thread vThread = Thread.ofVirtual().start(() -> {
    System.out.println("Running in: " + Thread.currentThread());
});

// Method 2: Thread.startVirtualThread()
Thread vThread2 = Thread.startVirtualThread(() -> {
    System.out.println("Virtual thread running");
});

// Method 3: ExecutorService
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return "Done";
        });
    }
} // auto-closes and waits for all tasks

// Method 4: Thread factory
ThreadFactory factory = Thread.ofVirtual().name("worker-", 0).factory();
Thread t = factory.newThread(() -> System.out.println("Worker"));
t.start();
```

### 21.3 When to Use Virtual Threads

**USE for:**
- HTTP request handling (web servers)
- Database queries
- File I/O
- API calls
- Any I/O-bound task

**AVOID for:**
- CPU-intensive computation
- Tasks that hold locks for long periods
- Tasks using `ThreadLocal` heavily (each virtual thread gets its own copy)

### 21.4 Example — Handling 100K Concurrent Requests

```java
// Platform threads — would crash or be very slow
// ExecutorService executor = Executors.newFixedThreadPool(200);

// Virtual threads — handles easily
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<String>> futures = new ArrayList<>();

    for (int i = 0; i < 100_000; i++) {
        futures.add(executor.submit(() -> {
            // Simulate HTTP call
            Thread.sleep(Duration.ofMillis(500));
            return "Response from " + Thread.currentThread();
        }));
    }

    for (Future<String> f : futures) {
        f.get(); // process results
    }
}
```

### 21.5 Structured Concurrency (Preview in Java 21)

```java
// Ensures that subtasks are properly managed as a unit
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<User> user = scope.fork(() -> fetchUser(userId));
    Subtask<List<Order>> orders = scope.fork(() -> fetchOrders(userId));

    scope.join();           // wait for both
    scope.throwIfFailed();  // propagate errors

    return new UserProfile(user.get(), orders.get());
}
```

---

## 22. Common Coding Problems

### Problem 1: Print numbers 1-10 using two threads alternately

```java
class AlternatePrinter {
    private int count = 1;
    private final int max = 10;
    private final Object lock = new Object();

    void printOdd() throws InterruptedException {
        synchronized (lock) {
            while (count <= max) {
                while (count % 2 == 0) lock.wait();
                if (count <= max) {
                    System.out.println("Odd: " + count);
                    count++;
                    lock.notify();
                }
            }
        }
    }

    void printEven() throws InterruptedException {
        synchronized (lock) {
            while (count <= max) {
                while (count % 2 != 0) lock.wait();
                if (count <= max) {
                    System.out.println("Even: " + count);
                    count++;
                    lock.notify();
                }
            }
        }
    }
}

AlternatePrinter printer = new AlternatePrinter();
new Thread(() -> { try { printer.printOdd(); } catch (Exception e) {} }).start();
new Thread(() -> { try { printer.printEven(); } catch (Exception e) {} }).start();
```

### Problem 2: Implement a thread-safe Singleton

```java
// Double-Checked Locking (most asked in interviews)
class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {                // first check (no lock)
            synchronized (Singleton.class) {
                if (instance == null) {         // second check (with lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

// Bill Pugh Singleton (using inner static class — lazy + thread-safe)
class Singleton2 {
    private Singleton2() {}

    private static class Holder {
        private static final Singleton2 INSTANCE = new Singleton2();
    }

    public static Singleton2 getInstance() {
        return Holder.INSTANCE;
    }
}

// Enum Singleton (simplest, recommended by Joshua Bloch)
enum Singleton3 {
    INSTANCE;

    public void doSomething() {
        System.out.println("Singleton method");
    }
}
```

### Problem 3: Implement a simple thread pool

```java
class SimpleThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final List<Thread> threads;
    private volatile boolean isShutdown = false;

    SimpleThreadPool(int poolSize, int queueSize) {
        taskQueue = new ArrayBlockingQueue<>(queueSize);
        threads = new ArrayList<>();

        for (int i = 0; i < poolSize; i++) {
            Thread t = new Thread(() -> {
                while (!isShutdown || !taskQueue.isEmpty()) {
                    try {
                        Runnable task = taskQueue.poll(1, TimeUnit.SECONDS);
                        if (task != null) task.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            t.start();
            threads.add(t);
        }
    }

    void submit(Runnable task) throws InterruptedException {
        if (!isShutdown) {
            taskQueue.put(task);
        }
    }

    void shutdown() {
        isShutdown = true;
    }
}
```

### Problem 4: Print "FizzBuzz" using three threads

```java
class FizzBuzz {
    private int n;
    private int current = 1;

    FizzBuzz(int n) { this.n = n; }

    synchronized void fizz() throws InterruptedException {
        while (current <= n) {
            while (current <= n && !(current % 3 == 0 && current % 5 != 0)) wait();
            if (current > n) return;
            System.out.println("Fizz");
            current++;
            notifyAll();
        }
    }

    synchronized void buzz() throws InterruptedException {
        while (current <= n) {
            while (current <= n && !(current % 5 == 0 && current % 3 != 0)) wait();
            if (current > n) return;
            System.out.println("Buzz");
            current++;
            notifyAll();
        }
    }

    synchronized void fizzBuzz() throws InterruptedException {
        while (current <= n) {
            while (current <= n && !(current % 15 == 0)) wait();
            if (current > n) return;
            System.out.println("FizzBuzz");
            current++;
            notifyAll();
        }
    }

    synchronized void number() throws InterruptedException {
        while (current <= n) {
            while (current <= n && (current % 3 == 0 || current % 5 == 0)) wait();
            if (current > n) return;
            System.out.println(current);
            current++;
            notifyAll();
        }
    }
}
```

---

## 23. Interview Questions & Answers

### Q1: What is the difference between a process and a thread?

**Answer:** A process is an independent program with its own memory space (heap, stack, code). A thread is a lightweight unit of execution within a process. Threads share the process's heap memory but have their own stack. Creating a thread is cheaper than creating a process. Threads within the same process can communicate via shared memory; processes need IPC (Inter-Process Communication).

---

### Q2: What is the difference between `start()` and `run()`?

**Answer:** `start()` creates a **new thread** and then calls `run()` on that new thread. `run()` simply executes the method in the **current thread** — no new thread is created. Always use `start()` to achieve multithreading.

---

### Q3: What is a race condition?

**Answer:** A race condition occurs when two or more threads access shared data concurrently, and the final result depends on the timing/order of execution. For example, `count++` is not atomic (read → modify → write) — two threads may read the same value and both write the same incremented value, losing one update. Fix with `synchronized`, `AtomicInteger`, or `Lock`.

---

### Q4: What is a deadlock? How to prevent it?

**Answer:** Deadlock occurs when two or more threads are blocked forever, each waiting for a lock held by the other. Four conditions for deadlock: (1) Mutual exclusion, (2) Hold and wait, (3) No preemption, (4) Circular wait. Prevention: (1) Lock ordering — always acquire locks in the same order, (2) Use `tryLock()` with timeout, (3) Avoid nested locks, (4) Use higher-level concurrency utilities.

---

### Q5: What is the difference between `synchronized` and `ReentrantLock`?

**Answer:** `synchronized` is a keyword with automatic lock/unlock; `ReentrantLock` is a class requiring manual lock/unlock. ReentrantLock offers: `tryLock()` (non-blocking), timeout, fair locking, `lockInterruptibly()`, and multiple `Condition` objects. Use `synchronized` for simplicity; use `ReentrantLock` when you need advanced features.

---

### Q6: What is the difference between `wait()` and `sleep()`?

**Answer:** `wait()` is called on an Object inside a synchronized block — it **releases the lock** and waits until notified. `sleep()` is called on Thread — it **does NOT release the lock** and pauses for a specified time. `wait()` is for inter-thread communication; `sleep()` is for pausing execution.

---

### Q7: What is `volatile`? When to use it?

**Answer:** `volatile` ensures that a variable is always read from and written to **main memory**, not from thread-local cache. It provides **visibility** but NOT **atomicity**. Use for simple flags (boolean) that are read by one thread and written by another. Don't use for compound operations like `count++` — use `AtomicInteger` or `synchronized` instead.

---

### Q8: What is the difference between `Callable` and `Runnable`?

**Answer:** `Runnable.run()` returns `void` and cannot throw checked exceptions. `Callable.call()` returns a value and can throw checked exceptions. `Callable` is used with `ExecutorService.submit()` which returns a `Future` to retrieve the result.

---

### Q9: What is `CompletableFuture`? How is it better than `Future`?

**Answer:** `CompletableFuture` (Java 8) is an enhanced `Future` that supports: (1) Non-blocking callbacks (`thenApply`, `thenAccept`), (2) Chaining operations, (3) Combining multiple futures (`thenCombine`, `allOf`, `anyOf`), (4) Exception handling (`exceptionally`, `handle`). `Future.get()` is blocking with no callback support.

---

### Q10: What is a thread pool? Why use it?

**Answer:** A thread pool is a collection of pre-created, reusable threads. Benefits: (1) Avoids the overhead of creating/destroying threads for each task, (2) Controls the maximum number of concurrent threads, (3) Provides task queuing. Use `ExecutorService` (`Executors.newFixedThreadPool()`, etc.) instead of manually creating threads.

---

### Q11: What is the difference between `ConcurrentHashMap` and `Hashtable`?

**Answer:** Both are thread-safe, but `ConcurrentHashMap` uses fine-grained locking (bucket-level in Java 8+) allowing multiple threads to read/write concurrently. `Hashtable` synchronizes every method (full lock) — only one thread can access at a time. `ConcurrentHashMap` doesn't allow null keys/values. `Hashtable` is legacy — always use `ConcurrentHashMap`.

---

### Q12: What is `ThreadLocal`?

**Answer:** `ThreadLocal` provides thread-specific variables — each thread has its own independent copy. Common uses: `SimpleDateFormat` (not thread-safe), database connections, user context in web apps. Always call `remove()` after use to prevent memory leaks, especially in thread pools where threads are reused.

---

### Q13: What are virtual threads? (Java 21)

**Answer:** Virtual threads are lightweight threads managed by the JVM (not OS). They use only a few KB of memory (vs ~1 MB for platform threads), allowing millions of concurrent threads. When a virtual thread blocks (I/O), it's unmounted from the carrier (platform) thread, which can then run other virtual threads. Ideal for I/O-bound tasks like web servers and database queries. Not suitable for CPU-bound tasks.

---

### Q14: What is the difference between `CountDownLatch` and `CyclicBarrier`?

**Answer:** `CountDownLatch` is one-time use — one thread waits for N events (countDown). `CyclicBarrier` is reusable — N threads wait for each other at a barrier point, then all proceed together. Use `CountDownLatch` when one thread waits for others to finish; use `CyclicBarrier` when threads need to synchronize at checkpoints.

---

### Q15: How does `AtomicInteger` work internally?

**Answer:** `AtomicInteger` uses **Compare-And-Swap (CAS)** operations — a CPU-level atomic instruction. CAS takes three values: memory location, expected value, and new value. It atomically checks if the current value equals the expected value, and if so, updates it. If not (another thread changed it), it retries. This is lock-free and faster than synchronization under low contention.

---

## Quick Reference Cheat Sheet

```
Thread Creation:
├── extends Thread (avoid)
├── implements Runnable ✅
├── implements Callable<V> ✅ (returns result)
└── Lambda with ExecutorService ✅✅

Synchronization:
├── synchronized (keyword) — simple cases
├── ReentrantLock — advanced (tryLock, fair, timeout)
├── ReadWriteLock — multiple readers, one writer
├── volatile — visibility only (no atomicity)
└── Atomic* — lock-free atomic operations

Executor Framework:
├── FixedThreadPool — known concurrent tasks
├── CachedThreadPool — many short-lived tasks
├── SingleThreadExecutor — sequential execution
├── ScheduledThreadPool — periodic tasks
├── WorkStealingPool — divide-and-conquer
└── VirtualThreadPerTaskExecutor — I/O-bound (Java 21)

Concurrent Collections:
├── ConcurrentHashMap — thread-safe HashMap
├── CopyOnWriteArrayList — many reads, few writes
├── ConcurrentLinkedQueue — lock-free queue
├── BlockingQueue — producer-consumer
└── ConcurrentSkipListMap — thread-safe TreeMap

Synchronizers:
├── CountDownLatch — wait for N events (one-time)
├── CyclicBarrier — N threads sync at barrier (reusable)
├── Semaphore — limit concurrent access
└── Phaser — flexible barrier (Java 7+)

CompletableFuture:
├── supplyAsync / runAsync — start async task
├── thenApply / thenAccept / thenRun — chain operations
├── thenCombine — combine two futures
├── thenCompose — chain dependent futures (flatMap)
├── allOf / anyOf — combine multiple futures
└── exceptionally / handle — error handling
```

---

> **Tip:** For WITCH interviews, focus on: thread creation (Runnable vs Callable), synchronized vs Lock, wait/notify, deadlock (with code example), volatile, ExecutorService, and CompletableFuture basics. HashMap internal working + ConcurrentHashMap is the most crossover topic between Collections and Multithreading. Singleton pattern with double-checked locking is almost always asked.
