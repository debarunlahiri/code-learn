# Java Design Patterns - Complete Guide (GoF)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** All 23 GoF Patterns (Creational, Structural, Behavioral), Real-world usage in Spring Framework, and Anti-Patterns.

---

## Table of Contents

1. [Creational Patterns](#creational-patterns)
2. [Structural Patterns](#structural-patterns)
3. [Behavioral Patterns](#behavioral-patterns)
4. [Design Patterns in Spring](#design-patterns-in-spring)
5. [Anti-Patterns](#anti-patterns)
6. [Complex Technical Scenarios](#complex-technical-scenarios)
7. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Creational Patterns

Creational patterns deal with **how objects are created**. Instead of using `new` directly everywhere (which makes code rigid), these patterns provide flexible and reusable ways to create objects.

> **Think of it this way:** You don't build a car by welding steel yourself every time you need one. You go to a factory (Factory Pattern), or you order one custom-built (Builder Pattern). Creational patterns are those "factories" for your code.

---

### 1.1 Singleton

**What:** Ensures a class has **only one instance** across the entire application and provides a global access point to it.

> **Real-world Analogy:** Think of the President of a country — there is only ONE at any time. No matter who asks "Who is the President?", they all get the **same** person.

**When to use:** Database connections, logging, configuration managers — anything where multiple instances would cause conflicts or waste resources.

```java
// ══════════════════════════════════════════════
// Thread-Safe Singleton (Double-Checked Locking)
// ══════════════════════════════════════════════
public class DatabaseConnection {
    // volatile ensures visibility across threads
    private static volatile DatabaseConnection instance;
    
    private DatabaseConnection() {
        // Private constructor — nobody can call new DatabaseConnection()
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {                       // First check (no locking)
            synchronized (DatabaseConnection.class) { // Lock only when needed
                if (instance == null) {               // Second check (inside lock)
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
}

// ══════════════════════════════════════════════
// Enum Singleton (BEST — Thread-safe, Serialization-safe)
// ══════════════════════════════════════════════
public enum AppConfig {
    INSTANCE;
    
    private String apiKey = "default";
    
    public String getApiKey() { return apiKey; }
    public void setApiKey(String key) { this.apiKey = key; }
}
// Usage: AppConfig.INSTANCE.getApiKey();
```

**Why Enum is best:** Handles serialization, reflection attacks, and thread safety automatically. Joshua Bloch (Effective Java author) recommends this.

**Java/Spring examples:** `Runtime.getRuntime()`, Spring Beans (default scope is Singleton).

---

### 1.2 Factory Method

**What:** Defines a method for creating objects, but lets the **subclass or logic decide** which class to instantiate. The calling code doesn't know (or care) about the exact type.

> **Real-world Analogy:** You walk into a pizza shop and say "I want a large pizza." You don't tell them HOW to make it — the shop (factory) decides the recipe, oven temperature, etc. You just get a pizza back.

**When to use:** When you need to create objects but the exact type depends on some condition — user input, configuration, etc.

```java
// ══════════════════════════════════════════════
// Step 1: Define a common interface
// ══════════════════════════════════════════════
interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SMSNotification implements Notification {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

class PushNotification implements Notification {
    public void send(String message) {
        System.out.println("Push: " + message);
    }
}

// ══════════════════════════════════════════════
// Step 2: Create the Factory
// ══════════════════════════════════════════════
class NotificationFactory {
    public static Notification createNotification(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms"   -> new SMSNotification();
            case "push"  -> new PushNotification();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}

// ══════════════════════════════════════════════
// Step 3: Use it — caller doesn't know concrete class
// ══════════════════════════════════════════════
Notification notif = NotificationFactory.createNotification("email");
notif.send("Hello!");  // Email: Hello!
```

**Java examples:** `Calendar.getInstance()`, `NumberFormat.getInstance()`, `Connection` from `DriverManager`.

---

### 1.3 Builder

**What:** Separates the construction of a complex object from its representation, allowing you to create different representations with the **same construction process**.

> **Real-world Analogy:** Think of ordering a customized burger — you add bun, patty, cheese, lettuce, tomato step by step. You don't need to pass 10 arguments at once. Some toppings are optional, some are mandatory.

**When to use:** When a class has too many constructor parameters (the "Telescoping Constructor" problem), or when many parameters are optional.

```java
// ══════════════════════════════════════════════
// Without Builder — Confusing constructor!
// ══════════════════════════════════════════════
// What does each boolean mean? Nobody knows!
new Pizza(14, true, false, true, false, true);  // ❌ Unreadable

// ══════════════════════════════════════════════
// With Builder — Crystal clear!
// ══════════════════════════════════════════════
public class Pizza {
    private final int size;         // Required
    private final boolean cheese;   // Optional
    private final boolean pepperoni;
    private final boolean mushrooms;
    
    private Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.mushrooms = builder.mushrooms;
    }
    
    public static class Builder {
        private final int size;          // Required — set in constructor
        private boolean cheese = false;  // Optional — defaults
        private boolean pepperoni = false;
        private boolean mushrooms = false;
        
        public Builder(int size) { this.size = size; }
        
        public Builder cheese(boolean val) { cheese = val; return this; }
        public Builder pepperoni(boolean val) { pepperoni = val; return this; }
        public Builder mushrooms(boolean val) { mushrooms = val; return this; }
        
        public Pizza build() { return new Pizza(this); }
    }
}

// Usage — Readable, immutable object
Pizza pizza = new Pizza.Builder(12)
    .cheese(true)
    .pepperoni(true)
    .build();
```

**Java/Spring examples:** `StringBuilder`, `Stream.builder()`, Lombok `@Builder`, `HttpRequest.newBuilder()`.

---

### 1.4 Abstract Factory

**What:** Provides an interface for creating **families of related objects** without specifying their concrete classes.

> **Real-world Analogy:** IKEA has different furniture lines — Modern, Victorian, Art Deco. When you pick "Modern," you get a Modern Chair + Modern Table + Modern Sofa. All pieces are from the **same family** and go together.

**When to use:** When your system needs to support multiple themes/platforms and the objects created must be from the same "family."

```java
// Family 1: Windows UI components
interface Button { void render(); }
interface TextField { void render(); }

class WindowsButton implements Button {
    public void render() { System.out.println("[Windows Button]"); }
}
class WindowsTextField implements TextField {
    public void render() { System.out.println("[Windows TextField]"); }
}

// Family 2: Mac UI components
class MacButton implements Button {
    public void render() { System.out.println("[Mac Button]"); }
}
class MacTextField implements TextField {
    public void render() { System.out.println("[Mac TextField]"); }
}

// Abstract Factory
interface UIFactory {
    Button createButton();
    TextField createTextField();
}

class WindowsFactory implements UIFactory {
    public Button createButton() { return new WindowsButton(); }
    public TextField createTextField() { return new WindowsTextField(); }
}

class MacFactory implements UIFactory {
    public Button createButton() { return new MacButton(); }
    public TextField createTextField() { return new MacTextField(); }
}
```

**Java examples:** `javax.xml.parsers.DocumentBuilderFactory`, `javax.xml.transform.TransformerFactory`.

---

### 1.5 Prototype

**What:** Creates new objects by **copying** (cloning) an existing object instead of creating from scratch.

> **Real-world Analogy:** Instead of designing a new document from scratch, you **duplicate** an existing template and modify what you need.

**When to use:** When creating an object is expensive (database queries, complex computation) and a similar object already exists.

```java
public class Employee implements Cloneable {
    private String name;
    private List<String> skills;  // Mutable field — needs deep copy!
    
    @Override
    public Employee clone() {
        try {
            Employee clone = (Employee) super.clone();
            clone.skills = new ArrayList<>(this.skills);  // Deep copy
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

**Java examples:** `Object.clone()`, `java.util.Date.clone()`.

---

## 2. Structural Patterns

Structural patterns deal with **how classes and objects are composed** to form larger, more useful structures. They help ensure that when one part of a system changes, the entire structure doesn't need to change.

> **Think of it this way:** LEGO bricks are simple on their own, but structural patterns are the clever ways you connect them to build castles, spaceships, or bridges.

---

### 2.1 Adapter (Wrapper)

**What:** Allows two **incompatible interfaces** to work together by wrapping one with a compatible interface.

> **Real-world Analogy:** A power plug adapter — your Indian laptop charger doesn't fit a US outlet, so you use an adapter that converts the interface without changing either device.

```java
// Old system — returns XML
class OldPaymentGateway {
    String processXML(String xml) { return "<response>OK</response>"; }
}

// Your app expects JSON
interface ModernPaymentProcessor {
    String processJSON(String json);
}

// Adapter — makes old system work with new interface
class PaymentAdapter implements ModernPaymentProcessor {
    private OldPaymentGateway oldGateway = new OldPaymentGateway();
    
    public String processJSON(String json) {
        String xml = convertJsonToXml(json);       // Convert input
        String xmlResponse = oldGateway.processXML(xml);  // Use old system
        return convertXmlToJson(xmlResponse);      // Convert output
    }
}
```

**Java examples:** `Arrays.asList()` (adapts array to List), `InputStreamReader` (adapts byte stream to character stream).

---

### 2.2 Decorator

**What:** Adds **new behavior** to an object dynamically by wrapping it, without modifying the original class.

> **Real-world Analogy:** You buy a coffee (base). Then you add milk (decorator 1), sugar (decorator 2), whipped cream (decorator 3). Each addition wraps the previous and adds something new, but the core is still coffee.

```java
interface Coffee {
    double cost();
    String description();
}

class BasicCoffee implements Coffee {
    public double cost() { return 5.0; }
    public String description() { return "Basic Coffee"; }
}

class MilkDecorator implements Coffee {
    private Coffee coffee;
    MilkDecorator(Coffee coffee) { this.coffee = coffee; }
    public double cost() { return coffee.cost() + 1.5; }
    public String description() { return coffee.description() + " + Milk"; }
}

// Usage — wrap and stack!
Coffee order = new MilkDecorator(new BasicCoffee());
// cost = 6.5, description = "Basic Coffee + Milk"
```

**Java examples:** `java.io` (the entire I/O library!) — `new BufferedReader(new InputStreamReader(new FileInputStream("file.txt")))`.

---

### 2.3 Proxy

**What:** Provides a **surrogate or placeholder** for another object to control access to it — for security, lazy loading, logging, etc.

> **Real-world Analogy:** A credit card is a proxy for cash. You don't carry cash around — the card controls access to your bank account, adding security and convenience.

**Types of Proxy:**
- **Virtual Proxy:** Delays expensive object creation until actually needed (lazy loading).
- **Protection Proxy:** Controls access based on permissions.
- **Logging Proxy:** Adds logging around method calls.

```java
interface Image {
    void display();
}

class RealImage implements Image {
    RealImage(String file) {
        System.out.println("Loading image from disk: " + file);  // Expensive!
    }
    public void display() { System.out.println("Displaying image"); }
}

class ProxyImage implements Image {
    private RealImage realImage;
    private String file;
    
    ProxyImage(String file) { this.file = file; }  // Cheap — no loading yet
    
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(file);  // Load only when needed!
        }
        realImage.display();
    }
}
```

**Spring examples:** `@Transactional` (AOP Proxy wraps method with transaction logic), Hibernate Lazy Loading (loads related entities only when accessed).

---

### 2.4 Facade

**What:** Provides a **simplified interface** to a complex subsystem, hiding its internal complexity.

> **Real-world Analogy:** A car's ignition button. You press ONE button, but behind the scenes it starts the engine, fuel pump, alternator, AC compressor, and dashboard lights. The button is the **facade** hiding all that complexity.

```java
// Complex subsystem classes
class CPU { void start() { System.out.println("CPU started"); } }
class Memory { void load() { System.out.println("Memory loaded"); } }
class HardDrive { void read() { System.out.println("Disk read"); } }

// Facade — ONE simple method hides the complexity
class ComputerFacade {
    private CPU cpu = new CPU();
    private Memory memory = new Memory();
    private HardDrive hdd = new HardDrive();
    
    public void startComputer() {
        cpu.start();
        memory.load();
        hdd.read();
        System.out.println("Computer ready!");
    }
}

// Usage — Simple!
new ComputerFacade().startComputer();
```

**Java examples:** `Slf4j` (facade for logging frameworks — you code against Slf4j, not Log4j/Logback directly).

---

### 2.5 Bridge

**What:** Separates an **abstraction** from its **implementation** so both can vary independently.

> **Real-world Analogy:** A TV remote (abstraction) and the TV (implementation). Sony remotes work with Sony TVs, Samsung with Samsung. The Bridge pattern lets ANY remote work with ANY TV by defining a standard interface between them.

**Java examples:** JDBC is a perfect Bridge — your code uses the JDBC API (abstraction), and the driver (MySQL driver, PostgreSQL driver) provides the implementation. Changing databases doesn't change your JDBC code.

---

### 2.6 Composite

**What:** Composes objects into **tree structures** to represent part-whole hierarchies. Allows clients to treat individual objects and compositions uniformly.

> **Real-world Analogy:** A file system — a **folder** can contain files AND other folders. Both are treated the same way (you can open, rename, delete both). A folder is a "composite" of items.

**Java examples:** `java.awt.Container` (can contain other Components and Containers).

---

### 2.7 Flyweight

**What:** Shares common parts of object state among multiple objects to **save memory** when you have thousands of similar objects.

> **Real-world Analogy:** In a book, the letter "A" appears thousands of times. Instead of creating a new "A" object each time, you reuse ONE "A" definition and just change its position. The "A" shape is the **shared (intrinsic)** state; its position is the **unique (extrinsic)** state.

**Java examples:** `String` Pool (identical strings share the same object), `Integer.valueOf()` caches -128 to 127.

```java
Integer a = Integer.valueOf(127);
Integer b = Integer.valueOf(127);
System.out.println(a == b);  // true — same cached object (Flyweight!)

Integer c = Integer.valueOf(128);
Integer d = Integer.valueOf(128);
System.out.println(c == d);  // false — outside cache range, new objects
```

---

## 3. Behavioral Patterns

Behavioral patterns deal with **communication between objects** — how they interact, distribute responsibilities, and coordinate behavior.

> **Think of it this way:** If creational patterns are about "birth" and structural patterns are about "body structure," behavioral patterns are about "personality and behavior" — how objects talk to each other and make decisions.

---

### 3.1 Observer (Pub/Sub)

**What:** Defines a **one-to-many** dependency — when one object (Subject) changes state, all its dependents (Observers) are automatically notified and updated.

> **Real-world Analogy:** A YouTube channel (Subject). When a new video is uploaded, ALL subscribers (Observers) get notified automatically. You don't need to check the channel manually.

**Java examples:** `java.util.EventListener`, `PropertyChangeListener`, RxJava `Observable`, Spring `ApplicationEvent`.

---

### 3.2 Strategy

**What:** Defines a **family of interchangeable algorithms** and lets you switch between them at runtime.

> **Real-world Analogy:** Google Maps gives you multiple route strategies — fastest, shortest, avoid tolls. Same starting point and destination, different algorithms to get there.

```java
interface SortStrategy {
    void sort(int[] array);
}

class BubbleSort implements SortStrategy {
    public void sort(int[] array) { /* bubble sort logic */ }
}

class QuickSort implements SortStrategy {
    public void sort(int[] array) { /* quicksort logic */ }
}

class Sorter {
    private SortStrategy strategy;
    
    Sorter(SortStrategy strategy) { this.strategy = strategy; }
    
    void setStrategy(SortStrategy strategy) { this.strategy = strategy; }
    
    void sort(int[] array) { strategy.sort(array); }
}

// Usage — switch algorithm at runtime
Sorter sorter = new Sorter(new QuickSort());
sorter.sort(data);                // Uses QuickSort
sorter.setStrategy(new BubbleSort());
sorter.sort(smallData);           // Uses BubbleSort
```

**Java examples:** `Collections.sort(list, Comparator)` — Comparator IS the strategy, `java.util.Comparator`.

---

### 3.3 Template Method

**What:** Defines the **skeleton** of an algorithm in a base class, and lets subclasses **override specific steps** without changing the overall structure.

> **Real-world Analogy:** A recipe template. Steps: 1) Prep ingredients, 2) Cook, 3) Plate, 4) Serve. The overall flow is fixed, but HOW you cook (bake, fry, steam) changes per dish.

```java
abstract class DataProcessor {
    // Template method — defines the skeleton (final = can't be overridden)
    public final void process() {
        readData();       // Step 1 — subclass implements
        processData();    // Step 2 — subclass implements
        writeData();      // Step 3 — subclass implements
    }
    
    abstract void readData();
    abstract void processData();
    abstract void writeData();
}

class CSVProcessor extends DataProcessor {
    void readData() { System.out.println("Reading CSV file"); }
    void processData() { System.out.println("Parsing CSV rows"); }
    void writeData() { System.out.println("Writing to database"); }
}
```

**Java/Spring examples:** `AbstractList` (defines list behavior, subclasses implement get/size), `JdbcTemplate`, `RestTemplate`.

---

### 3.4 Chain of Responsibility

**What:** Passes a request along a **chain of handlers**. Each handler decides either to process the request or pass it to the next handler.

> **Real-world Analogy:** Customer support escalation — Level 1 agent tries first. If they can't solve it, it goes to Level 2, then Level 3 manager. The request "travels" the chain until someone handles it.

**Java/Spring examples:** Servlet Filters, Spring Security Filter Chain, `java.util.logging.Logger` (parent loggers).

---

### 3.5 Command

**What:** Encapsulates a **request as an object**, allowing you to parameterize, queue, log, or undo operations.

> **Real-world Analogy:** A restaurant order ticket. Your food request is written on a ticket (command object), placed in a queue, and executed by the chef later. The waiter doesn't cook — they just carry the command.

**Java examples:** `Runnable` interface (encapsulates a task), GUI Action listeners, `Callable`.

---

### 3.6 Iterator

**What:** Provides a way to access elements of a collection **sequentially** without exposing its underlying structure (array, linked list, tree, etc.).

> **Real-world Analogy:** A TV remote's channel up/down button. You press "Next" without knowing how channels are stored internally. You just get the next one.

**Java examples:** `java.util.Iterator`, enhanced for-loop (`for-each`) uses Iterator internally, `Stream`.

---

### 3.7 Mediator

**What:** Defines an object that **encapsulates how a set of objects interact**, promoting loose coupling by preventing objects from referring to each other directly.

> **Real-world Analogy:** An airport control tower. Planes don't talk to each other directly — they all communicate through the tower (mediator). The tower coordinates takeoffs, landings, and movements.

**Java examples:** `java.util.Timer` (schedules tasks), `java.util.concurrent.ExecutorService`.

---

### 3.8 Memento

**What:** Captures an object's **internal state** so it can be **restored** later without violating encapsulation.

> **Real-world Analogy:** Saving a game. You save your progress (memento), continue playing, and if things go wrong, you **load** the saved state to go back. The save file doesn't expose the game's internal engine.

**Java examples:** `java.io.Serializable`, undo/redo functionality in text editors.

---

### 3.9 State

**What:** Allows an object to **change its behavior** when its internal state changes. The object appears to change its class.

> **Real-world Analogy:** A traffic light. It behaves differently in each state (Red = stop, Yellow = slow down, Green = go). The same traffic light object acts differently depending on its current state.

**Java examples:** Connection pool states (Active, Idle, Closed), Order status (Pending → Confirmed → Shipped → Delivered).

---

### 3.10 Visitor

**What:** Lets you add **new operations** to existing objects without modifying their classes.

> **Real-world Analogy:** A tax inspector (visitor) visiting different types of businesses. Each business type (restaurant, store, factory) handles the visit differently, but the inspector's role is defined separately from the businesses.

**Java examples:** `FileVisitor` API in NIO, `java.beans.Statement`.

---

### 3.11 Interpreter

**What:** Given a language, defines a **grammar representation** and an interpreter that uses the representation to interpret sentences in the language.

> **Real-world Analogy:** Google Translate. It interprets text in one language and produces output in another using defined grammar rules.

**Java/Spring examples:** `SpEL` (Spring Expression Language), `java.util.regex.Pattern` (interprets regex grammar).

---

## 4. Design Patterns in Spring

The Spring Framework is **built** on Design Patterns. Understanding these helps you understand how Spring works internally:

| # | Pattern | Spring Usage | How it works |
|---|---------|-------------|-------------|
| 1 | **Singleton** | Default Bean scope | One instance per Spring container — shared everywhere |
| 2 | **Factory** | `BeanFactory`, `ApplicationContext` | Spring creates and manages beans for you |
| 3 | **Prototype** | `@Scope("prototype")` | New instance every time `getBean()` is called |
| 4 | **Proxy** | `@Transactional`, AOP | Spring wraps your beans with proxy objects that add behavior (transaction management, logging) |
| 5 | **Template Method** | `JdbcTemplate`, `RestTemplate` | Execute boilerplate steps (open connection, execute, close, handle errors), you just provide the custom logic |
| 6 | **Front Controller** | `DispatcherServlet` | Single entry point that routes ALL HTTP requests to appropriate controllers |
| 7 | **MVC** | Spring MVC | Separates Model (data), View (UI), Controller (logic) |
| 8 | **Chain of Responsibility** | Security Filter Chain | Request passes through a chain of filters (authentication → authorization → CSRF → etc.) |
| 9 | **Observer** | `ApplicationEvent` | Beans publish events, listener beans react to them asynchronously |

---

## 5. Anti-Patterns

Anti-patterns are **common solutions that seem good but are actually harmful**. Knowing them helps you avoid common traps:

| # | Anti-Pattern | What it is | Why it's bad | Fix |
|---|-------------|-----------|-------------|-----|
| 1 | **God Object** | One class that does EVERYTHING — 5000+ lines, knows too much | Impossible to test, maintain, or reuse | Apply **Single Responsibility Principle** — break into smaller focused classes |
| 2 | **Spaghetti Code** | No structure, everything tangled together, no clear flow | Hard to read, debug, or modify | Use proper architecture (MVC, layers), refactor regularly |
| 3 | **Gold Plating** | Adding features nobody asked for — "just in case" | Wasted time, added complexity, more bugs | Follow YAGNI (You Aren't Gonna Need It) |
| 4 | **Magic Numbers** | Using `if (status == 3)` instead of `if (status == ACTIVE)` | Nobody knows what "3" means 6 months later | Use constants or enums: `Status.ACTIVE` |
| 5 | **Copy-Paste Programming** | Duplicating code across multiple places | Bug fix in one place missed in others | Extract common logic into shared methods/classes (DRY principle) |

---

## 6. Complex Technical Scenarios

### 6.1 Singleton vs Static Class — when to use which?

**Explanation:**

| Feature | Singleton | Static Class |
|---------|-----------|-------------|
| **Is an Object?** | ✅ Yes — can be passed as parameter, stored in variables | ❌ No — just a container for static methods |
| **Can implement interfaces?** | ✅ Yes — Dependency Injection works | ❌ No |
| **Can extend classes?** | ✅ Yes | ❌ No |
| **Lazy loading?** | ✅ Can be initialized when first needed | ❌ Loaded when class is loaded |
| **Easy to mock/test?** | ✅ Yes (implement interface) | ❌ Hard (need PowerMock) |
| **State?** | ✅ Can hold state (connection, config) | ❌ Better for stateless utilities |

> **Rule of thumb:** Use **Singleton** for stateful services (DB connections, configuration). Use **Static class** for stateless utility methods (`Math.abs()`, `StringUtils.isEmpty()`).

### 6.2 Dependency Injection vs Factory Pattern — what's the difference?

**Explanation:**

| Aspect | Factory Pattern | Dependency Injection |
|--------|----------------|---------------------|
| **Who creates?** | Client asks Factory for dependency | Container pushes dependency to client |
| **Direction** | Client → Factory ("Give me X") | Container → Client ("Here's X for you") |
| **Coupling** | Client coupled to Factory class | Client coupled only to interface |
| **Principle** | Client pulls dependencies | "Don't call us, we'll call you" (Hollywood Principle) |

```java
// Factory — Client actively ASKS for dependency
UserService service = ServiceFactory.create("userService");

// DI — Container GIVES dependency (constructor injection)
class OrderService {
    private final PaymentService paymentService;
    
    @Autowired
    OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;  // Injected by Spring
    }
}
```

### 6.3 When NOT to use design patterns?

**Explanation:** Over-engineering is a real risk. Don't use a pattern just because you can. Signs you're over-engineering:
- Adding a Factory for a class that will only ever have ONE implementation.
- Creating an Observer system when only one listener exists.
- Using Strategy pattern when you'll never swap algorithms.

> **Best practice:** Start simple. Refactor toward a pattern **when the need arises** (e.g., when you add a second implementation, that's when you introduce the Factory).

---

## 7. Key Topics & Explanations

### 7.1 Why use the Builder Pattern?

**Explanation:** When a class has too many constructor parameters, the code becomes unreadable — this is called the **Telescoping Constructor Problem**:

```java
// ❌ What does each argument mean? 
new Pizza(12, true, false, true, false, true, 2, "thin");

// ✅ Builder makes it self-documenting
new Pizza.Builder(12)
    .cheese(true)
    .pepperoni(true)
    .mushrooms(true)
    .crustType("thin")
    .build();
```

Additional benefit: The resulting object can be **immutable** (all fields are final), which makes it thread-safe.

### 7.2 Explain SOLID Principles

**Explanation:** SOLID is a set of 5 design principles that make code more **maintainable, flexible, and understandable**:

| Principle | Full Name | What it means | Real-world Analogy |
|-----------|-----------|--------------|-------------------|
| **S** | Single Responsibility | A class should have **one reason to change** | A chef cooks. A waiter serves. They don't do each other's jobs. |
| **O** | Open/Closed | Open for **extension**, closed for **modification** | Plugins — add new features without changing the core code |
| **L** | Liskov Substitution | Subtypes must be **substitutable** for their base types | If it looks like a duck and quacks like a duck, it should swim like a duck too |
| **I** | Interface Segregation | Many specific interfaces > one fat interface | A printer shouldn't need to implement `fax()` and `scan()` if it can only print |
| **D** | Dependency Inversion | Depend on **abstractions**, not concrete implementations | Your lamp has a standard socket — works with ANY bulb brand, not just one specific one |

### 7.3 How to choose the right design pattern?

**Explanation:**

| Problem | Pattern to Consider |
|---------|-------------------|
| Need only ONE instance of a class | Singleton |
| Object creation depends on conditions | Factory |
| Too many constructor parameters | Builder |
| Making incompatible systems work together | Adapter |
| Adding behavior without modifying class | Decorator |
| Hiding complex subsystem behind simple API | Facade |
| Notify multiple objects about state changes | Observer |
| Switch algorithms at runtime | Strategy |
| Define algorithm skeleton, customize steps | Template Method |
