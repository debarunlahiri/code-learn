# Core Java OOP Concepts - Complete Guide (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** All four pillars of OOP, SOLID principles, design patterns basics, and tricky technical scenarios.

--

## Table of Contents

1. [What is OOP?](#what-is-oop)
2. [Classes and Objects](#classes-and-objects)
3. [Constructors](#constructors)
4. [Encapsulation](#encapsulation)
5. [Inheritance](#inheritance)
6. [Polymorphism](#polymorphism)
7. [Abstraction](#abstraction)
8. [Interfaces](#interfaces)
9. [Abstract Class vs Interface](#abstract-class-vs-interface)
10. [The `this` and `super` Keywords](#the-this-and-super-keywords)
11. [The `static` Keyword](#the-static-keyword)
12. [The `final` Keyword](#the-final-keyword)
13. [Object Class and Its Methods](#object-class-and-its-methods)
14. [Type Casting (Upcasting & Downcasting)](#type-casting-upcasting-downcasting)
15. [Composition vs Inheritance](#composition-vs-inheritance)
16. [SOLID Principles](#solid-principles)
17. [Coupling and Cohesion](#coupling-and-cohesion)
18. [Association, Aggregation, Composition](#association-aggregation-composition)
19. [Immutable Classes](#immutable-classes)
20. [Marker Interfaces](#marker-interfaces)
21. [Sealed Classes (Java 17+)](#sealed-classes-java-17)
22. [Records (Java 16+)](#records-java-16)
23. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. What is OOP?

**Object-Oriented Programming** is a programming paradigm based on the concept of **objects** which contain **data** (fields/attributes) and **behavior** (methods).

### Four Pillars of OOP

| Pillar            | Purpose                                                   |
|----------|------------------------------|
| **Encapsulation** | Bundling data + methods, hiding internal state            |
| **Inheritance**   | Reusing code by deriving new classes from existing ones   |
| **Polymorphism**  | One interface, multiple implementations                   |
| **Abstraction**   | Hiding complexity, showing only essential features        |

### OOP vs Procedural Programming

| Feature           | OOP                              | Procedural                      |
|----------|-----------------|-----------------|
| **Focus**         | Objects                          | Functions/Procedures            |
| **Data**          | Encapsulated in objects          | Shared/global                   |
| **Reusability**   | High (inheritance, composition)  | Low (copy-paste)                |
| **Security**      | Data hiding via access modifiers | No built-in data hiding         |
| **Example**       | Java, C++, Python                | C, Pascal                       |

--

## 2. Classes and Objects

### Class — Blueprint

```java
public class Employee {
    // Fields (state)
    private String name;
    private int age;
    private double salary;

    // Constructor
    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    // Method (behavior)
    public void displayInfo() {
        System.out.println(name + " | Age: " + age + " | Salary: " + salary);
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }
}
```

### Object — Instance of a Class

```java
Employee emp1 = new Employee("Rahul", 28, 50000);
Employee emp2 = new Employee("Sneha", 25, 45000);

emp1.displayInfo(); // Rahul | Age: 28 | Salary: 50000.0
emp2.displayInfo(); // Sneha | Age: 25 | Salary: 45000.0
```

### What happens when `new Employee()` is called?

1. Memory is allocated on the **heap** for the object.
2. Instance variables are initialized to **default values** (0, null, false).
3. The **constructor** is executed.
4. A **reference** to the object is returned and stored in the variable on the **stack**.

--

## 3. Constructors

### 3.1 Types of Constructors

```java
public class Student {

    String name;
    int age;

    // 1. Default Constructor (no-arg)
    public Student() {
        this.name = "Unknown";
        this.age = 0;
    }

    // 2. Parameterized Constructor
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 3. Copy Constructor (Java doesn't have built-in, we create manually)
    public Student(Student other) {
        this.name = other.name;
        this.age = other.age;
    }
}
```

### 3.2 Constructor Chaining

```java
public class Person {
    String name;
    int age;
    String city;

    public Person() {
        this("Unknown", 0, "Unknown"); // calls 3-arg constructor
    }

    public Person(String name) {
        this(name, 0, "Unknown"); // calls 3-arg constructor
    }

    public Person(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }
}
```

### 3.3 Constructor Rules

- Constructor name **must match** the class name.
- No return type (not even `void`).
- If you don't write any constructor, Java provides a **default no-arg constructor**.
- If you write **any** constructor, Java does **NOT** provide the default one.
- `this()` must be the **first statement** in a constructor.
- A constructor **cannot** be `abstract`, `static`, `final`, or `synchronized`.

--

## 4. Encapsulation

**Encapsulation** = Wrapping data (fields) and methods into a single unit (class) + restricting direct access to internal state.

### Implementation

```java
public class BankAccount {
    // Private fields — hidden from outside
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // Controlled access through public methods
    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid amount");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount");
        }
    }
}
```

### Access Modifiers

| Modifier      | Class | Package | Subclass | World |
|--------|----|-----|-----|----|
| `private`     | ✅    | ❌      | ❌       | ❌    |
| `default`     | ✅    | ✅      | ❌       | ❌    |
| `protected`   | ✅    | ✅      | ✅       | ❌    |
| `public`      | ✅    | ✅      | ✅       | ✅    |

### Benefits of Encapsulation

- **Data hiding** — internal state is protected.
- **Validation** — setters can validate input before modifying state.
- **Flexibility** — internal implementation can change without affecting external code.
- **Read-only / Write-only** — provide only getter or only setter.

--

## 5. Inheritance

**Inheritance** = A mechanism where a child class acquires the properties and behaviors of a parent class.

### 5.1 Single Inheritance

```java
class Animal {
    String name;

    void eat() {
        System.out.println(name + " is eating");
    }

    void sleep() {
        System.out.println(name + " is sleeping");
    }
}

class Dog extends Animal {
    void bark() {
        System.out.println(name + " is barking");
    }
}

// Usage
Dog dog = new Dog();
dog.name = "Buddy";
dog.eat();   // Buddy is eating (inherited)
dog.bark();  // Buddy is barking (own method)
```

### 5.2 Types of Inheritance in Java

```
1. Single:       A → B
2. Multilevel:   A → B → C
3. Hierarchical: A → B, A → C

NOT supported in Java (with classes):
4. Multiple:     A, B → C  (use interfaces instead)
5. Hybrid:       combination of above
```

### 5.3 Multilevel Inheritance

```java
class Animal {
    void eat() { System.out.println("eating"); }
}

class Dog extends Animal {
    void bark() { System.out.println("barking"); }
}

class Puppy extends Dog {
    void play() { System.out.println("playing"); }
}

Puppy p = new Puppy();
p.eat();  // from Animal
p.bark(); // from Dog
p.play(); // own
```

### 5.4 Why Java Doesn't Support Multiple Inheritance (with classes)

The **Diamond Problem:**

```
      Animal
      /    \
   Dog    Cat
      \    /
      Hybrid  ← Which eat() to use? Dog's or Cat's?
```

Java avoids this ambiguity. Use **interfaces** instead (a class can implement multiple interfaces).

### 5.5 Method Overriding

```java
class Animal {
    void sound() {
        System.out.println("Some generic sound");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Woof Woof!");
    }
}

class Cat extends Animal {
    @Override
    void sound() {
        System.out.println("Meow!");
    }
}

Animal a = new Dog();
a.sound(); // Woof Woof! (runtime polymorphism)
```

### 5.6 Overriding Rules

| Rule | Description |
|---|-------|
| **Method signature** | Must be exactly the same |
| **Return type** | Same or covariant (subtype) |
| **Access modifier** | Same or wider (not narrower) |
| **Exceptions** | Can throw same, fewer, or narrower checked exceptions |
| **static methods** | Cannot be overridden (they are hidden) |
| **final methods** | Cannot be overridden |
| **private methods** | Cannot be overridden (not visible to subclass) |
| **Constructors** | Cannot be overridden |

### 5.7 `super` Keyword in Inheritance

```java
class Animal {
    String type = "Animal";

    Animal() {
        System.out.println("Animal constructor");
    }

    void display() {
        System.out.println("I am an Animal");
    }
}

class Dog extends Animal {
    String type = "Dog";

    Dog() {
        super(); // calls Animal constructor (implicit if not written)
        System.out.println("Dog constructor");
    }

    void display() {
        super.display(); // calls Animal's display()
        System.out.println("I am a Dog");
        System.out.println("Parent type: " + super.type); // Animal
        System.out.println("My type: " + this.type);      // Dog
    }
}
```

--

## 6. Polymorphism

**Polymorphism** = "Many forms" — the ability of an object to take different forms.

### 6.1 Compile-Time Polymorphism (Method Overloading)

Resolved at **compile time** based on method signature.

```java
class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }

    String add(String a, String b) {
        return a + b;
    }
}

Calculator calc = new Calculator();
calc.add(1, 2);         // calls int version → 3
calc.add(1.5, 2.5);     // calls double version → 4.0
calc.add(1, 2, 3);      // calls 3-arg version → 6
calc.add("Hello", " World"); // calls String version
```

### Overloading Rules

- **Must differ** in: number of parameters, type of parameters, or order of parameter types.
- **Cannot differ** only in return type.
- **Can differ** in access modifiers and exceptions.

### 6.2 Runtime Polymorphism (Method Overriding)

Resolved at **runtime** based on the actual object type.

```java
class Shape {
    void draw() {
        System.out.println("Drawing a shape");
    }

    double area() {
        return 0;
    }
}

class Circle extends Shape {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    void draw() {
        System.out.println("Drawing a circle");
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    double width, height;

    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    void draw() {
        System.out.println("Drawing a rectangle");
    }

    @Override
    double area() {
        return width * height;
    }
}

// Runtime polymorphism in action
Shape s1 = new Circle(5);
Shape s2 = new Rectangle(4, 6);

s1.draw(); // Drawing a circle
s2.draw(); // Drawing a rectangle

// Process different shapes uniformly
List<Shape> shapes = Arrays.asList(new Circle(5), new Rectangle(4, 6));
for (Shape s : shapes) {
    System.out.println("Area: " + s.area()); // calls correct area() at runtime
}
```

### 6.3 Overloading vs Overriding

| Feature           | Overloading                    | Overriding                      |
|----------|----------------|-----------------|
| **When**          | Compile-time                   | Runtime                         |
| **Where**         | Same class                     | Parent-child classes            |
| **Method name**   | Same                           | Same                            |
| **Parameters**    | Must differ                    | Must be same                    |
| **Return type**   | Can differ                     | Same or covariant               |
| **Access**        | Can differ                     | Same or wider                   |
| **static**        | Can be overloaded              | Cannot be overridden            |
| **Binding**       | Static (early) binding         | Dynamic (late) binding          |

### 6.4 Tricky Technical Review Scenario

```java
class Parent {
    void show(Object o) {
        System.out.println("Parent-Object");
    }
}

class Child extends Parent {
    void show(String s) {
        System.out.println("Child-String");
    }
}

Child c = new Child();
c.show("Hello");  // Child-String (overloading, not overriding — different param type)
c.show(new Object()); // Parent-Object

Parent p = new Child();
p.show("Hello");  // Parent-Object (reference type is Parent, which only has show(Object))
```

--

## 7. Abstraction

**Abstraction** = Hiding implementation details and showing only the essential features.

### 7.1 Abstract Class

```java
abstract class Vehicle {
    String brand;

    // Constructor (abstract classes CAN have constructors)
    Vehicle(String brand) {
        this.brand = brand;
    }

    // Abstract method — no body, must be implemented by subclass
    abstract void start();
    abstract double fuelEfficiency();

    // Concrete method — has a body
    void displayBrand() {
        System.out.println("Brand: " + brand);
    }
}

class Car extends Vehicle {
    Car(String brand) {
        super(brand);
    }

    @Override
    void start() {
        System.out.println(brand + " car starts with key ignition");
    }

    @Override
    double fuelEfficiency() {
        return 15.5; // km/l
    }
}

class ElectricCar extends Vehicle {
    ElectricCar(String brand) {
        super(brand);
    }

    @Override
    void start() {
        System.out.println(brand + " electric car starts with push button");
    }

    @Override
    double fuelEfficiency() {
        return 0; // no fuel
    }
}

// Cannot instantiate abstract class
// Vehicle v = new Vehicle("Generic"); // COMPILE ERROR

Vehicle car = new Car("Maruti");
car.start();          // Maruti car starts with key ignition
car.displayBrand();   // Brand: Maruti
```

### 7.2 Abstract Class Rules

| Rule | Description |
|---|-------|
| Cannot be instantiated | `new AbstractClass()` → compile error |
| Can have constructors | Called via `super()` from subclass |
| Can have abstract methods | Must be implemented by concrete subclass |
| Can have concrete methods | Inherited by subclasses |
| Can have fields | Including `private`, `static`, `final` |
| Can have `static` methods | Yes |
| Subclass must implement all abstract methods | Or be declared abstract itself |

--

## 8. Interfaces

An **interface** is a contract that defines what a class must do, but not how.

### 8.1 Basic Interface

```java
interface Flyable {
    void fly(); // implicitly public abstract
}

interface Swimmable {
    void swim();
}

class Duck implements Flyable, Swimmable {
    @Override
    public void fly() {
        System.out.println("Duck is flying");
    }

    @Override
    public void swim() {
        System.out.println("Duck is swimming");
    }
}

// Multiple inheritance through interfaces
Flyable f = new Duck();
f.fly();

Swimmable s = new Duck();
s.swim();
```

### 8.2 Interface Evolution (Java 8+)

```java
interface PaymentGateway {

    // Abstract method (must be implemented)
    void processPayment(double amount);

    // Default method (Java 8) — has a body, can be overridden
    default void validatePayment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        System.out.println("Payment validated: " + amount);
    }

    // Static method (Java 8) — belongs to interface, cannot be overridden
    static String getGatewayVersion() {
        return "v2.0";
    }

    // Private method (Java 9) — helper for default methods
    private void logTransaction(String message) {
        System.out.println("[LOG] " + message);
    }

    // Private static method (Java 9)
    private static void internalHelper() {
        System.out.println("Internal helper");
    }
}

class RazorPay implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        validatePayment(amount); // using default method
        System.out.println("Processing via RazorPay: ₹" + amount);
    }
}
```

### 8.3 Interface Fields

All fields in an interface are implicitly `public static final` (constants).

```java
interface Constants {
    int MAX_SIZE = 100;        // public static final
    String APP_NAME = "MyApp"; // public static final
}
```

### 8.4 Functional Interface

An interface with **exactly one abstract method**. Used with lambda expressions.

```java
@FunctionalInterface
interface Greeting {
    void greet(String name); // single abstract method
}

// Lambda expression
Greeting g = name -> System.out.println("Hello, " + name);
g.greet("Rahul"); // Hello, Rahul
```

### 8.5 Diamond Problem with Default Methods

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

// Must resolve the conflict explicitly
class C implements A, B {
    @Override
    public void show() {
        A.super.show(); // explicitly choose A's version
        // or B.super.show();
        // or provide own implementation
    }
}
```

--

## 9. Abstract Class vs Interface

| Feature                    | Abstract Class                  | Interface                        |
|--------------|-----------------|-----------------|
| **Methods**                | Abstract + concrete             | Abstract + default + static + private |
| **Fields**                 | Any type                        | Only `public static final`       |
| **Constructors**           | Yes                             | No                               |
| **Access modifiers**       | Any                             | Methods are `public` by default  |
| **Multiple inheritance**   | No (single extends)             | Yes (multiple implements)        |
| **When to use**            | IS-A relationship with shared state | Capability/contract (CAN-DO)  |
| **Speed**                  | Slightly faster                 | Slightly slower (indirection)    |

### When to Use What?

- **Abstract class:** When classes share common state/behavior and have IS-A relationship.
  - Example: `Animal` → `Dog`, `Cat` (they share `name`, `age` fields)
- **Interface:** When unrelated classes share a capability.
  - Example: `Flyable` → `Bird`, `Airplane`, `Drone` (no common state)

--

## 10. The `this` and `super` Keywords

### `this`

```java
class Employee {
    String name;
    int age;

    // 1. Refer to current instance variable
    Employee(String name, int age) {
        this.name = name; // distinguishes field from parameter
        this.age = age;
    }

    // 2. Call another constructor
    Employee(String name) {
        this(name, 0); // calls 2-arg constructor
    }

    // 3. Pass current object as argument
    void register() {
        EmployeeService.save(this);
    }

    // 4. Return current object (method chaining)
    Employee setName(String name) {
        this.name = name;
        return this;
    }
}
```

### `super`

```java
class Animal {
    String name = "Animal";

    Animal() {
        System.out.println("Animal constructor");
    }

    void display() {
        System.out.println("Animal display");
    }
}

class Dog extends Animal {
    String name = "Dog";

    Dog() {
        super(); // 1. Call parent constructor (must be first line)
        System.out.println("Dog constructor");
    }

    void display() {
        super.display();                    // 2. Call parent method
        System.out.println("Dog display");
        System.out.println(super.name);     // 3. Access parent field → "Animal"
        System.out.println(this.name);      // Access own field → "Dog"
    }
}
```

### Important Rules

- `this()` and `super()` **cannot** be used together in the same constructor (both must be first statement).
- `super()` is implicitly called if not written explicitly.
- `this` and `super` **cannot** be used in `static` context.

--

## 11. The `static` Keyword

### 11.1 Static Variable (Class Variable)

Shared across all instances of the class.

```java
class Counter {
    static int count = 0; // shared by all objects
    String name;

    Counter(String name) {
        this.name = name;
        count++; // increments for every new object
    }
}

Counter c1 = new Counter("A"); // count = 1
Counter c2 = new Counter("B"); // count = 2
Counter c3 = new Counter("C"); // count = 3

System.out.println(Counter.count); // 3 (access via class name)
```

### 11.2 Static Method

Belongs to the class, not to any instance.

```java
class MathUtils {
    static int add(int a, int b) {
        return a + b;
    }

    static boolean isEven(int n) {
        return n % 2 == 0;
    }
}

// Call without creating object
int sum = MathUtils.add(5, 3);
```

### 11.3 Static Block

Executed once when the class is loaded.

```java
class Config {
    static Map<String, String> properties;

    static {
        // Runs once when class is loaded
        properties = new HashMap<>();
        properties.put("db.url", "jdbc:mysql://localhost:3306/mydb");
        properties.put("db.user", "root");
        System.out.println("Config loaded!");
    }
}
```

### 11.4 Static Inner Class

```java
class Outer {
    static int x = 10;
    int y = 20;

    static class Inner {
        void display() {
            System.out.println("x = " + x);  // can access static members
            // System.out.println("y = " + y); // CANNOT access non-static members
        }
    }
}

Outer.Inner inner = new Outer.Inner(); // no need for Outer instance
inner.display();
```

### 11.5 Static Rules

| Rule | Description |
|---|-------|
| Static method cannot access non-static members | No `this` or instance variables |
| Non-static method CAN access static members | Works fine |
| Static method cannot be overridden | Can be hidden (method hiding) |
| Static method cannot use `this` or `super` | No instance context |

--

## 12. The `final` Keyword

### 12.1 Final Variable

```java
final int MAX = 100;
// MAX = 200; // COMPILE ERROR — cannot reassign

// Blank final — initialized in constructor
class Employee {
    final String employeeId;

    Employee(String id) {
        this.employeeId = id; // must be initialized here
    }
}

// Final reference — reference can't change, but object can be modified
final List<String> names = new ArrayList<>();
names.add("Rahul");    // OK — modifying the object
// names = new ArrayList<>(); // COMPILE ERROR — reassigning reference
```

### 12.2 Final Method

Cannot be overridden by subclasses.

```java
class Payment {
    final void validate() {
        System.out.println("Validating payment...");
    }
}

class UPIPayment extends Payment {
    // void validate() {} // COMPILE ERROR — cannot override final method
}
```

### 12.3 Final Class

Cannot be extended (no subclasses).

```java
final class ImmutablePoint {
    private final int x;
    private final int y;

    ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}

// class Point3D extends ImmutablePoint {} // COMPILE ERROR
```

**Examples of final classes in Java:** `String`, `Integer`, `Double`, all wrapper classes.

--

## 13. Object Class and Its Methods

Every class in Java implicitly extends `java.lang.Object`.

### 13.1 Key Methods

```java
public class Employee {
    String name;
    int age;

    // 1. toString() — string representation
    @Override
    public String toString() {
        return "Employee{name='" + name + "', age=" + age + "}";
    }

    // 2. equals() — logical equality
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return age == employee.age && Objects.equals(name, employee.name);
    }

    // 3. hashCode() — must override when equals is overridden
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

### 13.2 equals() and hashCode() Contract

| Rule | Description |
|---|-------|
| If `a.equals(b)` is true | Then `a.hashCode() == b.hashCode()` must be true |
| If `a.hashCode() == b.hashCode()` | `a.equals(b)` may or may not be true (collision) |
| If `a.equals(b)` is false | `hashCode` may or may not be equal |

**Why this matters:** `HashMap`, `HashSet`, `Hashtable` use `hashCode()` to find the bucket and `equals()` to find the exact match.

### 13.3 == vs equals()

```java
String s1 = new String("Hello");
String s2 = new String("Hello");

System.out.println(s1 == s2);      // false (different references)
System.out.println(s1.equals(s2)); // true (same content)

String s3 = "Hello";
String s4 = "Hello";
System.out.println(s3 == s4);      // true (String pool — same reference)
```

### 13.4 Other Object Methods

```java
// clone() — creates a copy (class must implement Cloneable)
// getClass() — returns runtime class
// finalize() — called by GC before object is destroyed (deprecated since Java 9)
// wait(), notify(), notifyAll() — for thread synchronization
```

--

## 14. Type Casting (Upcasting & Downcasting)

### 14.1 Upcasting (Implicit)

Child reference → Parent reference. Always safe.

```java
class Animal {
    void eat() { System.out.println("eating"); }
}

class Dog extends Animal {
    void bark() { System.out.println("barking"); }
}

Animal a = new Dog(); // Upcasting (implicit)
a.eat();              // OK
// a.bark();          // COMPILE ERROR — Animal reference can't see Dog methods
```

### 14.2 Downcasting (Explicit)

Parent reference → Child reference. Risky — can throw `ClassCastException`.

```java
Animal a = new Dog(); // Upcasting
Dog d = (Dog) a;      // Downcasting (explicit) — OK because actual object is Dog
d.bark();             // OK

Animal a2 = new Animal();
// Dog d2 = (Dog) a2; // ClassCastException at runtime!
```

### 14.3 instanceof Check

Always check before downcasting.

```java
Animal a = new Dog();

if (a instanceof Dog) {
    Dog d = (Dog) a;
    d.bark();
}

// Java 16+ Pattern Matching for instanceof
if (a instanceof Dog d) {
    d.bark(); // d is already cast
}
```

--

## 15. Composition vs Inheritance

### Inheritance (IS-A)

```java
class Engine {
    void start() { System.out.println("Engine started"); }
}

// Car IS-A Engine? Doesn't make sense!
class Car extends Engine { } // BAD design
```

### Composition (HAS-A) — Preferred

```java
class Engine {
    void start() { System.out.println("Engine started"); }
}

class Car {
    private Engine engine; // Car HAS-A Engine

    Car() {
        this.engine = new Engine();
    }

    void start() {
        engine.start();
        System.out.println("Car is ready to drive");
    }
}
```

### When to Use

| Use Inheritance When | Use Composition When |
|-----------|-----------|
| True IS-A relationship | HAS-A relationship |
| `Dog` IS-A `Animal` | `Car` HAS-A `Engine` |
| Need to override behavior | Need to use behavior |
| Tight coupling is acceptable | Loose coupling preferred |

> **Rule of thumb:** "Favor composition over inheritance" — Gang of Four

--

## 16. SOLID Principles

### S — Single Responsibility Principle (SRP)

A class should have **only one reason to change**.

```java
// BAD — Employee class does too many things
class Employee {
    void calculateSalary() { }
    void saveToDatabase() { }
    void generateReport() { }
}

// GOOD — Each class has one responsibility
class Employee {
    String name;
    double salary;
}

class SalaryCalculator {
    double calculate(Employee emp) { return emp.salary * 1.1; }
}

class EmployeeRepository {
    void save(Employee emp) { /* DB logic */ }
}

class ReportGenerator {
    void generate(Employee emp) { /* Report logic */ }
}
```

### O — Open/Closed Principle (OCP)

Open for **extension**, closed for **modification**.

```java
// BAD — must modify this class for every new shape
class AreaCalculator {
    double calculate(Object shape) {
        if (shape instanceof Circle) { /* ... */ }
        else if (shape instanceof Rectangle) { /* ... */ }
        // Adding Triangle requires modifying this class!
    }
}

// GOOD — extend without modifying existing code
interface Shape {
    double area();
}

class Circle implements Shape {
    double radius;
    public double area() { return Math.PI * radius * radius; }
}

class Rectangle implements Shape {
    double width, height;
    public double area() { return width * height; }
}

// Adding Triangle doesn't require changing existing code
class Triangle implements Shape {
    double base, height;
    public double area() { return 0.5 * base * height; }
}

class AreaCalculator {
    double calculate(Shape shape) {
        return shape.area(); // works for any shape
    }
}
```

### L — Liskov Substitution Principle (LSP)

Subtypes must be substitutable for their base types without breaking the program.

```java
// BAD — Square violates LSP when substituted for Rectangle
class Rectangle {
    int width, height;
    void setWidth(int w) { this.width = w; }
    void setHeight(int h) { this.height = h; }
    int area() { return width * height; }
}

class Square extends Rectangle {
    @Override
    void setWidth(int w) { this.width = w; this.height = w; } // breaks expectation
    @Override
    void setHeight(int h) { this.width = h; this.height = h; }
}

// This test fails for Square:
Rectangle r = new Square();
r.setWidth(5);
r.setHeight(3);
assert r.area() == 15; // FAILS! Square makes it 9
```

### I — Interface Segregation Principle (ISP)

Clients should not be forced to depend on interfaces they don't use.

```java
// BAD — fat interface
interface Worker {
    void work();
    void eat();
    void sleep();
}

class Robot implements Worker {
    public void work() { /* OK */ }
    public void eat() { /* Robots don't eat! */ }  // forced to implement
    public void sleep() { /* Robots don't sleep! */ }
}

// GOOD — segregated interfaces
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

class Human implements Workable, Eatable, Sleepable {
    public void work() { }
    public void eat() { }
    public void sleep() { }
}

class Robot implements Workable {
    public void work() { } // only implements what it needs
}
```

### D — Dependency Inversion Principle (DIP)

High-level modules should not depend on low-level modules. Both should depend on **abstractions**.

```java
// BAD — high-level depends on low-level
class MySQLDatabase {
    void save(String data) { /* MySQL specific */ }
}

class UserService {
    private MySQLDatabase db = new MySQLDatabase(); // tightly coupled
    void saveUser(String user) { db.save(user); }
}

// GOOD — both depend on abstraction
interface Database {
    void save(String data);
}

class MySQLDatabase implements Database {
    public void save(String data) { /* MySQL specific */ }
}

class MongoDatabase implements Database {
    public void save(String data) { /* MongoDB specific */ }
}

class UserService {
    private Database db; // depends on abstraction

    UserService(Database db) { // injected via constructor
        this.db = db;
    }

    void saveUser(String user) { db.save(user); }
}

// Easy to switch implementations
UserService service = new UserService(new MySQLDatabase());
UserService service2 = new UserService(new MongoDatabase());
```

--

## 17. Coupling and Cohesion

### Coupling (Low is better)

How much one class depends on another.

| Type | Description | Example |
|---|-------|-----|
| **Tight coupling** | Classes are heavily dependent | Directly creating objects inside |
| **Loose coupling** | Classes interact through interfaces | Dependency injection |

```java
// Tight coupling
class OrderService {
    private EmailService emailService = new EmailService(); // directly creates
}

// Loose coupling
class OrderService {
    private NotificationService notificationService; // interface

    OrderService(NotificationService ns) {
        this.notificationService = ns; // injected
    }
}
```

### Cohesion (High is better)

How closely related the responsibilities of a class are.

```java
// Low cohesion — does too many unrelated things
class Utils {
    void sendEmail() { }
    void calculateTax() { }
    void formatDate() { }
    void connectToDatabase() { }
}

// High cohesion — focused on one area
class EmailService {
    void sendEmail() { }
    void validateEmail() { }
    void formatEmailBody() { }
}
```

--

## 18. Association, Aggregation, Composition

### Association

A general relationship between two classes. No ownership.

```java
class Teacher {
    String name;
}

class Student {
    String name;
    // A student knows about a teacher, but neither owns the other
}
```

### Aggregation (Weak HAS-A)

Child can exist **independently** of parent.

```java
class Department {
    String name;
    List<Employee> employees; // employees can exist without department

    Department(String name, List<Employee> employees) {
        this.name = name;
        this.employees = employees;
    }
}

// Employees exist independently
Employee e1 = new Employee("Rahul");
Employee e2 = new Employee("Sneha");
Department dept = new Department("IT", Arrays.asList(e1, e2));
// If dept is destroyed, e1 and e2 still exist
```

### Composition (Strong HAS-A)

Child **cannot exist** without parent.

```java
class House {
    private List<Room> rooms;

    House(int numberOfRooms) {
        rooms = new ArrayList<>();
        for (int i = 0; i < numberOfRooms; i++) {
            rooms.add(new Room()); // rooms are created inside house
        }
    }
}

class Room {
    // Room has no meaning without a House
}

// If House is destroyed, Rooms are also destroyed
```

### Summary

| Relationship | Strength | Lifecycle | Example |
|-------|-----|------|-----|
| **Association** | Weak | Independent | Teacher ↔ Student |
| **Aggregation** | Medium | Independent | Department → Employee |
| **Composition** | Strong | Dependent | House → Room |

--

## 19. Immutable Classes

An immutable object's state **cannot be changed** after creation.

### Rules to Create an Immutable Class

1. Declare the class as `final`.
2. Make all fields `private` and `final`.
3. No setter methods.
4. Initialize all fields via constructor.
5. Return deep copies of mutable fields (defensive copying).

```java
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public final class ImmutableStudent {
    private final String name;
    private final int age;
    private final List<String> subjects;

    public ImmutableStudent(String name, int age, List<String> subjects) {
        this.name = name;
        this.age = age;
        // Defensive copy — don't store the original reference
        this.subjects = new ArrayList<>(subjects);
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    // Return unmodifiable copy — caller can't modify internal list
    public List<String> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }
}

// Usage
List<String> subjects = new ArrayList<>(Arrays.asList("Math", "Science"));
ImmutableStudent student = new ImmutableStudent("Rahul", 20, subjects);

subjects.add("English"); // original list modified
System.out.println(student.getSubjects()); // [Math, Science] — not affected!

// student.getSubjects().add("History"); // UnsupportedOperationException
```

### Why Immutability Matters

- **Thread-safe** — no synchronization needed.
- **Safe as HashMap keys** — hashCode won't change.
- **No side effects** — predictable behavior.
- **String** is the most common example of an immutable class.

--

## 20. Marker Interfaces

An interface with **no methods** — used to "mark" a class with a capability.

```java
// Built-in marker interfaces
public interface Serializable { }   // marks class as serializable
public interface Cloneable { }      // marks class as cloneable
public interface RandomAccess { }   // marks list as supporting fast random access

// Custom marker interface
interface Auditable { }

class Transaction implements Auditable {
    // This class is "marked" as auditable
}

// Check at runtime
if (obj instanceof Auditable) {
    // perform audit logging
}
```

### Marker Interface vs Annotation

| Feature | Marker Interface | Annotation |
|-----|---------|------|
| **Type checking** | Compile-time (instanceof) | Runtime (reflection) |
| **Polymorphism** | Yes (can be used as type) | No |
| **Modern approach** | Older pattern | Preferred in modern Java |
| **Example** | `Serializable` | `@Entity`, `@Override` |

--

## 21. Sealed Classes (Java 17+)

Sealed classes restrict which classes can extend them.

```java
// Only Circle, Rectangle, and Triangle can extend Shape
public sealed class Shape permits Circle, Rectangle, Triangle {
    abstract double area();
}

// Must be final, sealed, or non-sealed
public final class Circle extends Shape {
    double radius;
    double area() { return Math.PI * radius * radius; }
}

public final class Rectangle extends Shape {
    double width, height;
    double area() { return width * height; }
}

public non-sealed class Triangle extends Shape {
    double base, height;
    double area() { return 0.5 * base * height; }
    // non-sealed means any class can extend Triangle
}
```

### Benefits

- **Exhaustive pattern matching** — compiler knows all subtypes.
- **Controlled hierarchy** — prevents unauthorized extensions.
- **Better with switch expressions** (Java 21+):

```java
double getArea(Shape shape) {
    return switch (shape) {
        case Circle c -> c.area();
        case Rectangle r -> r.area();
        case Triangle t -> t.area();
        // No default needed — compiler knows all cases
    };
}
```

--

## 22. Records (Java 16+)

Records are **immutable data carriers** — concise alternative to boilerplate POJOs.

```java
// Traditional POJO — lots of boilerplate
class EmployeeOld {
    private final String name;
    private final int age;

    EmployeeOld(String name, int age) {
        this.name = name;
        this.age = age;
    }

    String getName() { return name; }
    int getAge() { return age; }

    @Override
    public boolean equals(Object o) { /* ... */ }
    @Override
    public int hashCode() { /* ... */ }
    @Override
    public String toString() { /* ... */ }
}

// Record — same thing in one line!
record Employee(String name, int age) { }
```

### What Records Auto-Generate

- `private final` fields for each component.
- A canonical constructor.
- Getter methods (`name()`, `age()` — no `get` prefix).
- `equals()`, `hashCode()`, `toString()`.

### Custom Constructors and Methods

```java
record Employee(String name, int age) {

    // Compact constructor — for validation
    Employee {
        if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
        name = name.trim(); // can modify before assignment
    }

    // Custom method
    String nameUpperCase() {
        return name.toUpperCase();
    }
}

Employee emp = new Employee("Rahul", 28);
System.out.println(emp.name());     // Rahul (getter — no 'get' prefix)
System.out.println(emp.age());      // 28
System.out.println(emp);            // Employee[name=Rahul, age=28]
```

### Record Restrictions

- Records are implicitly `final` — cannot be extended.
- Cannot extend other classes (but can implement interfaces).
- Fields are implicitly `final` — immutable.
- Cannot declare instance fields outside the header.

--

## 23. Key Topics & Explanations

### Topic 1: What are the four pillars of OOP?

**Answer:** Encapsulation (data hiding), Inheritance (code reuse), Polymorphism (many forms), Abstraction (hiding complexity). Encapsulation bundles data and methods; Inheritance allows child classes to reuse parent code; Polymorphism allows the same method to behave differently; Abstraction hides implementation details.

--

### Topic 2: Can we create an object of an abstract class?

**Answer:** No, we cannot instantiate an abstract class directly. But we can create a reference of an abstract class pointing to a concrete subclass object: `Animal a = new Dog();`. We can also use anonymous inner classes: `Animal a = new Animal() { void sound() { } };`.

--

### Topic 3: What is the difference between `==` and `equals()`?

**Answer:** `==` compares **references** (memory addresses) — checks if two variables point to the same object. `equals()` compares **content/logical equality** — checks if two objects are meaningfully equal. For `String`, `equals()` compares character sequences. Always override `equals()` in custom classes for logical comparison.

--

### Topic 4: Why do we need to override `hashCode()` when we override `equals()`?

**Answer:** The contract states: if two objects are equal (`equals()` returns true), they must have the same `hashCode()`. `HashMap` and `HashSet` use `hashCode()` to determine the bucket. If `hashCode()` is not overridden, two logically equal objects may end up in different buckets, causing lookup failures.

--

### Topic 5: Can a constructor be private?

**Answer:** Yes. Private constructors are used in:
- **Singleton pattern** — only one instance allowed.
- **Factory methods** — object creation controlled via static methods.
- **Utility classes** — prevent instantiation (e.g., `java.util.Collections`).

```java
class Singleton {
    private static Singleton instance;
    private Singleton() { }
    public static Singleton getInstance() {
        if (instance == null) instance = new Singleton();
        return instance;
    }
}
```

--

### Topic 6: What is method hiding?

**Answer:** When a subclass defines a `static` method with the same signature as a `static` method in the parent class, it's called method hiding (not overriding). The method called depends on the **reference type**, not the object type.

```java
class Parent {
    static void show() { System.out.println("Parent"); }
}
class Child extends Parent {
    static void show() { System.out.println("Child"); }
}

Parent p = new Child();
p.show(); // "Parent" — based on reference type (method hiding)
```

--

### Topic 7: What is covariant return type?

**Answer:** A subclass can override a method and return a **subtype** of the original return type.

```java
class Animal {
    Animal create() { return new Animal(); }
}
class Dog extends Animal {
    @Override
    Dog create() { return new Dog(); } // Dog is subtype of Animal — valid
}
```

--

### Topic 8: Can we override a private or static method?

**Answer:**
- **Private methods:** No. They are not visible to subclasses, so there's nothing to override.
- **Static methods:** No. They belong to the class, not instances. A subclass can define the same static method (method hiding), but it's not overriding.

--

### Topic 9: What is the difference between aggregation and composition?

**Answer:**
- **Aggregation:** Weak relationship. Child can exist independently. Example: Department has Employees — employees exist even if department is deleted.
- **Composition:** Strong relationship. Child cannot exist without parent. Example: House has Rooms — rooms are destroyed when house is destroyed.

--

### Topic 10: What is an immutable class? How do you create one?

**Answer:** An immutable class is one whose state cannot be changed after creation. Steps: (1) Make class `final`, (2) Make all fields `private final`, (3) No setters, (4) Initialize via constructor, (5) Return defensive copies of mutable fields. Example: `String`, `Integer`, `LocalDate`.

--

### Topic 11: What is the diamond problem? How does Java solve it?

**Answer:** The diamond problem occurs when a class inherits from two classes that have a method with the same signature. Java solves this by:
- **Not allowing multiple inheritance with classes** (only single `extends`).
- **Allowing multiple inheritance with interfaces**. If two interfaces have the same default method, the implementing class must override it explicitly.

--

### Topic 12: What is the difference between shallow copy and deep copy?

**Answer:**
- **Shallow copy:** Copies the object but shares references to nested objects. Changes to nested objects affect both copies.
- **Deep copy:** Copies the object and all nested objects recursively. Both copies are completely independent.

```java
// Shallow copy
Employee copy = original.clone(); // nested objects still shared

// Deep copy
Employee deepCopy = new Employee(original.getName(),
    new Address(original.getAddress().getCity())); // new nested objects
```

--

### Topic 13: Can an interface extend another interface?

**Answer:** Yes. An interface can extend **multiple** interfaces.

```java
interface A { void methodA(); }
interface B { void methodB(); }
interface C extends A, B { void methodC(); } // extends both A and B
```

--

### Topic 14: What is the order of execution: static block, instance block, constructor?

**Answer:**
1. **Static block** — when class is loaded (once).
2. **Instance initializer block** — before constructor (every object creation).
3. **Constructor** — after instance block.

```java
class Demo {
    static { System.out.println("1. Static block"); }
    { System.out.println("2. Instance block"); }
    Demo() { System.out.println("3. Constructor"); }
}
// Output: 1. Static block → 2. Instance block → 3. Constructor
```

--

### Topic 15: What is the difference between `String`, `StringBuilder`, and `StringBuffer`?

**Answer:**

| Feature | String | StringBuilder | StringBuffer |
|-----|----|--------|-------|
| **Mutability** | Immutable | Mutable | Mutable |
| **Thread-safe** | Yes (immutable) | No | Yes (synchronized) |
| **Performance** | Slow for concatenation | Fast | Slower than StringBuilder |
| **When to use** | Few modifications | Single-threaded string manipulation | Multi-threaded string manipulation |

--

## Quick Reference

```
OOP Pillars:
├── Encapsulation  → private fields + public getters/setters
├── Inheritance    → extends (IS-A)
├── Polymorphism   → Overloading (compile-time) + Overriding (runtime)
└── Abstraction    → abstract class / interface

SOLID:
├── S → Single Responsibility
├── O → Open/Closed
├── L → Liskov Substitution
├── I → Interface Segregation
└── D → Dependency Inversion

Relationships:
├── IS-A      → Inheritance (extends/implements)
├── HAS-A     → Composition/Aggregation
├── Association → General relationship
├── Aggregation → Weak HAS-A (child survives)
└── Composition → Strong HAS-A (child dies with parent)

Access Modifiers (narrow → wide):
private → default → protected → public
```

--

> **Tip:** WITCH companies heavily focus on OOP concepts, especially the four pillars, SOLID principles, `equals()`/`hashCode()`, immutability, and abstract class vs interface. Make sure you can explain each with real-world examples.
