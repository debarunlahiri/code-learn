# Java JVM Internals & Performance Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond (Senior Roles).  
**Covers:** JVM Architecture, Memory Model, Garbage Collection, Class Loading, and Tuning.

---

## Table of Contents

1. [JVM Architecture](#jvm-architecture)
2. [Java Memory Model (Heap vs Stack)](#java-memory-model-heap-vs-stack)
3. [Garbage Collection (GC)](#garbage-collection-gc)
4. [Class Loading Subsystem](#class-loading-subsystem)
5. [JVM Tuning & Flags](#jvm-tuning-flags)
6. [Profiling Tools](#profiling-tools)
7. [Complex Technical Scenarios](#complex-technical-scenarios)
8. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. JVM Architecture

The JVM (Java Virtual Machine) is an abstract machine that provides the runtime environment for executing Java bytecode (.class files).

### Components
1.  **Class Loader Subsystem**: Loads, links, and initializes classes.
2.  **Runtime Data Areas**: Memory (Heap, Stack, Method Area, PC Register, Native Method Stack).
3.  **Execution Engine**: Executes bytecode (Interpreter, JIT Compiler, GC).
4.  **Native Interface (JNI)**: Interacts with Native Method Libraries (C/C++).

---

## 2. Java Memory Model (Heap vs Stack)

### Stack Memory
*   Stores primitive variables and object **references**.
*   Fast access (LIFO).
*   Created per **Thread**.
*   **StackOverFlowError**: Infinite recursion.

### Heap Memory
*   Stores actual **Objects** and **Arrays**.
*   Slower access (Synchronization overhead).
*   Shared by all threads (global).
*   **OutOfMemoryError** (OOM): Heap full (Memory Leaks).

### Method Area (PermGen -> Metaspace in Java 8+)
*   Stores Class Metadata (Static variables, Constants, Methods).
*   **Metaspace**: Uses native OS memory (not heap), so it can grow dynamically.

---

## 3. Garbage Collection (GC)

Automatic memory management process that identifies and discards objects that are no longer needed (Unreachable objects).

### Generational Hypothesis
Most objects die young.
*   **Young Generation** (Eden, S0, S1): New objects created here. Use **Minor GC** (Stop-the-world, fast).
*   **Old Generation** (Tenured): Survivors from Young Gen promoted here. Use **Major GC** (Slow, pauses application).

### Types of GC Collectors
1.  **Serial Collector**: Single-threaded. For small apps. (`-XX:+UseSerialGC`)
2.  **Parallel Collector** (Throughput): Multiple threads for YGen. Good for batch jobs. (`-XX:+UseParallelGC`)
3.  **CMS (Concurrent Mark Sweep)**: Low-latency. Deprecated in Java 9.
4.  **G1 (Garbage First)**: Default in Java 9+. Partitions heap into regions. Balances throughput and latency. (`-XX:+UseG1GC`)
5.  **ZGC / Shenandoah**: Ultra-low latency (<10ms pause) for massive heaps (TB scale).

---

## 4. Class Loading Subsystem

Responsible for loading classes dynamically.

### Loading Phases
1.  **Loading**: Finds .class file.
2.  **Linking**:
    *   **Verification**: Bytecode verification (Security).
    *   **Preparation**: Allocate memory for static variables (default values).
    *   **Resolution**: Resolve symbolic references.
3.  **Initialization**: Execute static blocks and assign static values.

### ClassLoader Hierarchy
1.  **Bootstrap ClassLoader**: Loads core Java classes (`rt.jar` - java.lang.*). Native code.
2.  **Extension/Platform ClassLoader**: Loads extensions (`lib/ext`).
3.  **Application/System ClassLoader**: Loads user classes from classpath.

**Delegation Model**: A class loader asks its parent first. Only if parent fails does it try to load itself.

---

## 5. JVM Tuning & Flags

Optimization of JVM parameters for performance.

### Key Flags
*   `-Xms`: Initial Heap Size.
*   `-Xmx`: Maximum Heap Size. (Set `Xms=Xmx` in production to avoid resizing overhead).
*   `-Xss`: Thread Stack Size.
*   `-XX:MaxMetaspaceSize`: Limit non-heap memory usage.
*   `-XX:+PrintGCDetails`: Log GC activity.
*   `-XX:+HeapDumpOnOutOfMemoryError`: Auto-dump heap for analysis when crash happens.

---

## 6. Profiling Tools

Tools to monitor and analyze JVM performance.

1.  **JVisualVM**: Included in JDK. Monitors CPU, Heap, Threads.
2.  **JConsole**: Older JMX console.
3.  **JProfiler / YourKit**: Commercial profilers (Deep analysis).
4.  **Eclipse Memory Analyzer (MAT)**: Best for analyzing Heap Dumps (Finding leaks).

---

## 7. Complex Technical Scenarios

### Topic 1: Memory Leak in Java?
**Answer:**
Objects are referenced but no longer used, preventing GC from reclaiming them.
**Causes**: 
1.  Static collections (`Map`, `List`) growing indefinitely.
2.  Unclosed resources (Streams, Connections).
3.  Listener callbacks not unregistered.
**Analysis**: Take Heap Dump -> Open in MAT -> Check "Dominator Tree".

### Topic 2: `System.gc()`?
**Answer:**
It suggests the JVM to run GC, but **does not guarantee** it. Avoid using it in production code as it might trigger Full GC (Stop-the-world).

### Topic 3: Why is String immutable?
**Answer:**
1.  **Security**: Used for DB/Network connection strings.
2.  **Thread Safety**: Can be shared safely.
3.  **Caching**: HashCode is cached (fast for HashMap keys).
4.  **String Pool**: Saves memory by reusing literals.

---

## 8. Key Topics & Explanations

### Topic 1: JIT Compiler?
**Answer:**
**Just-In-Time Compiler** converts frequently executed bytecode ("Hot Spots") into native machine code at runtime for faster execution. It performs optimizations like Method Inlining and Dead Code Elimination.

### Topic 2: Interpretation vs Compilation?
**Answer:**
*   **Interpreter**: Reads bytecode line-by-line (Slow).
*   **Compilation (JIT)**: Compiles blocks of bytecode to native code (Fast).
JVM uses mixed mode (Intepreter first -> JIT for hot methods).

### Topic 3: WeakReference vs SoftReference?
**Answer:**
*   **Strong**: `Object o = new Object()` (Standard). Never collected if reachable.
*   **Soft**: Collected only if JVM is running out of memory. Good for Caches.
*   **Weak**: Collected at the next GC event, regardless of memory. Good for Metadata maps (`WeakHashMap`).
*   **Phantom**: Used to perform post-mortem cleanup actions.
