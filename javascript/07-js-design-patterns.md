# JavaScript Design Patterns - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Singleton, Factory, Observer, Module, Prototype, Proxy, Iterator, and behavioral patterns.

---

## Table of Contents

1. [Singleton Pattern](#singleton-pattern)
2. [Factory Pattern](#factory-pattern)
3. [Observer Pattern (Pub/Sub)](#observer-pattern-pubsub)
4. [Module Pattern](#module-pattern)
5. [Prototype Pattern](#prototype-pattern)
6. [Proxy Pattern](#proxy-pattern)
7. [Iterator Pattern](#iterator-pattern)
8. [Complex Technical Scenarios](#complex-technical-scenarios)
9. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Singleton Pattern

### 1.1 What is it?

The Singleton pattern ensures a class has **only ONE instance** across your entire application and provides a global access point to it.

> **Real-world Analogy:** Think of a country's President — there is only ONE at any time. No matter who asks "Who is the President?", everyone gets the **same** answer. Creating a "second President" isn't possible.

**When to use:**
- Database connections (one connection pool shared by everyone)
- Configuration settings (one global config object)
- Logging services (one logger shared by all modules)
- State stores (like Redux — one store for the entire app)

### 1.2 Implementation

```javascript
// ══════════════════════════════════════════════
// Method 1: Using ES6 Class with static property
// ══════════════════════════════════════════════
class Database {
    constructor(connectionString) {
        // If an instance already exists, return it
        // (ignores new constructor arguments — this is intentional)
        if (Database.instance) {
            return Database.instance;
        }
        
        this.connectionString = connectionString;
        this.isConnected = false;
        Database.instance = this;  // Store the instance
    }

    connect() {
        this.isConnected = true;
        console.log(`Connected to ${this.connectionString}`);
    }

    query(sql) {
        if (!this.isConnected) throw new Error("Not connected!");
        console.log(`Executing: ${sql}`);
    }
}

const db1 = new Database("mongodb://localhost");
const db2 = new Database("mysql://localhost");  // Tries to create new, but...

console.log(db1 === db2);                       // true — same object!
console.log(db2.connectionString);              // "mongodb://localhost"
// ↑ Second constructor call was ignored — first instance preserved


// ══════════════════════════════════════════════
// Method 2: Using Closure (more robust)
// ══════════════════════════════════════════════
const DatabaseService = (() => {
    let instance = null;  // Private — closure hides this
    
    function createInstance(config) {
        return {
            host: config.host,
            port: config.port,
            query(sql) { console.log(`[${this.host}] ${sql}`); }
        };
    }
    
    return {
        getInstance(config) {
            if (!instance) {
                instance = createInstance(config);
            }
            return instance;
        }
    };
})();

const a = DatabaseService.getInstance({ host: "localhost", port: 5432 });
const b = DatabaseService.getInstance({ host: "remote", port: 3306 });
console.log(a === b);  // true — same instance


// ══════════════════════════════════════════════
// Method 3: Object Literal (Simplest — often sufficient in JS!)
// ══════════════════════════════════════════════
const AppConfig = {
    apiKey: "sk-123456",
    apiUrl: "https://api.example.com",
    debug: false
};
Object.freeze(AppConfig);  // Prevent any modifications

AppConfig.apiKey = "hacked";     // Silently fails (strict mode: TypeError)
console.log(AppConfig.apiKey);   // Still "sk-123456"
```

> **JavaScript shortcut:** In JavaScript, you often don't even need a class-based Singleton. A simple `Object.freeze()`'d object literal IS a singleton — it's one object, globally accessible, and immutable.

---

## 2. Factory Pattern

### 2.1 What is it?

The Factory pattern creates objects **without exposing the creation logic** to the caller. You tell the factory WHAT you want, and it figures out HOW to build it.

> **Real-world Analogy:** A pizza shop. You say "I want a Margherita pizza." You don't tell them how to knead dough, what temperature to bake, or how to arrange toppings. The kitchen (factory) handles all that and gives you the finished product.

**When to use:**
- Object creation involves complex logic (conditionals, configurations)
- You want to decouple "creating" from "using"
- The type of object depends on runtime conditions (user input, API response)

### 2.2 Implementation

```javascript
// ══════════════════════════════════════════════
// Step 1: Define the product classes
// ══════════════════════════════════════════════
class EmailNotification {
    constructor(recipient) {
        this.type = "email";
        this.recipient = recipient;
    }
    send(message) {
        console.log(`📧 Email to ${this.recipient}: ${message}`);
    }
}

class SMSNotification {
    constructor(recipient) {
        this.type = "sms";
        this.recipient = recipient;
    }
    send(message) {
        console.log(`📱 SMS to ${this.recipient}: ${message}`);
    }
}

class PushNotification {
    constructor(recipient) {
        this.type = "push";
        this.recipient = recipient;
    }
    send(message) {
        console.log(`🔔 Push to ${this.recipient}: ${message}`);
    }
}

// ══════════════════════════════════════════════
// Step 2: Create the Factory
// ══════════════════════════════════════════════
class NotificationFactory {
    static create(type, recipient) {
        switch (type) {
            case "email": return new EmailNotification(recipient);
            case "sms":   return new SMSNotification(recipient);
            case "push":  return new PushNotification(recipient);
            default:      throw new Error(`Unknown notification type: ${type}`);
        }
    }
}

// ══════════════════════════════════════════════
// Step 3: Use it — caller doesn't know which class was created
// ══════════════════════════════════════════════
const notif = NotificationFactory.create("email", "rahul@test.com");
notif.send("Welcome aboard!");  // 📧 Email to rahul@test.com: Welcome aboard!

// Later, changing from email to push is just ONE word:
const pushNotif = NotificationFactory.create("push", "user123");
pushNotif.send("New message!");  // 🔔 Push to user123: New message!
```

> **Why use Factory?** If you used `new EmailNotification(...)` directly in 50 places and later need to switch to `PushNotification`, you'd change 50 lines. With a Factory, you change ONE word in the factory method.

---

## 3. Observer Pattern (Pub/Sub)

### 3.1 What is it?

The Observer pattern sets up a **subscription system** where multiple objects (observers/subscribers) automatically get notified when another object (subject/publisher) changes state.

> **Real-world Analogy:** A YouTube channel. When a creator uploads a new video, ALL subscribers get notified automatically. Each subscriber can react differently — one watches immediately, another adds to "Watch Later", another ignores it.

**When to use:**
- Event systems (click handlers, custom events)
- State management (Redux, MobX)
- Real-time updates (chat apps, live dashboards)
- Any time multiple parts of your app need to react to the same event

**This is arguably the MOST important pattern in JavaScript.** DOM events, Node.js EventEmitter, React hooks, Redux, RxJS — they're ALL based on the Observer pattern.

### 3.2 Implementation

```javascript
class EventBus {
    constructor() {
        this.subscribers = {};  // { eventName: [fn1, fn2, ...] }
    }

    // Subscribe to an event
    subscribe(event, callback) {
        if (!this.subscribers[event]) {
            this.subscribers[event] = [];
        }
        this.subscribers[event].push(callback);
        
        // Return an unsubscribe function (for easy cleanup)
        return () => {
            this.subscribers[event] = this.subscribers[event]
                .filter(fn => fn !== callback);
        };
    }

    // Publish an event — notify ALL subscribers
    publish(event, data) {
        if (!this.subscribers[event]) return;
        
        this.subscribers[event].forEach(callback => {
            try {
                callback(data);
            } catch (error) {
                console.error(`Error in subscriber for "${event}":`, error);
            }
        });
    }

    // Remove ALL subscribers for an event
    clear(event) {
        delete this.subscribers[event];
    }
}

// ══════════════════════════════════════════════
// Usage — Decoupled components communicating via events
// ══════════════════════════════════════════════
const bus = new EventBus();

// Component A: Listens for user login
const unsubscribe = bus.subscribe("user:login", (user) => {
    console.log(`Welcome back, ${user.name}!`);
    loadDashboard(user);
});

// Component B: Also listens for user login
bus.subscribe("user:login", (user) => {
    analytics.track("login", { userId: user.id });
});

// Component C: Listens for user logout
bus.subscribe("user:logout", () => {
    clearSession();
    redirectToLogin();
});

// When user logs in — both Component A and B are notified
bus.publish("user:login", { id: 1, name: "Rahul" });
// Output:
// "Welcome back, Rahul!"
// (analytics tracked)

// Later — unsubscribe Component A
unsubscribe();  // Component A no longer receives events
```

> **Key insight:** Neither the publisher nor the subscribers know about each other. The EventBus acts as a middleman. This is the foundation of scalable architectures.

---

## 4. Module Pattern

### 4.1 What is it?

The Module pattern uses **closures** to create private variables and methods, exposing only what you want through a public API. This achieves **encapsulation** in JavaScript.

> **Real-world Analogy:** A bank vault. The public API is the ATM outside — you can `deposit()`, `withdraw()`, and `checkBalance()`. But you CANNOT directly access the vault (private `balance` variable) or the counting machines (private `calculateInterest()` method).

**When to use:**
- Before ES6 modules existed (maintaining legacy code)
- When you need private state that can't be tampered with
- Organizing code into self-contained units

### 4.2 Implementation (IIFE)

```javascript
// ══════════════════════════════════════════════
// Classic Module Pattern using IIFE
// (Immediately Invoked Function Expression)
// ══════════════════════════════════════════════
const BankAccount = (function() {
    // ── PRIVATE ── (only accessible inside the IIFE)
    let balance = 0;
    const transactionLog = [];
    
    function logTransaction(type, amount) {
        transactionLog.push({ 
            type, 
            amount, 
            date: new Date(),
            balanceAfter: balance 
        });
    }

    // ── PUBLIC API ── (returned object — all users see)
    return {
        deposit(amount) {
            if (amount <= 0) throw new Error("Amount must be positive");
            balance += amount;
            logTransaction("deposit", amount);
            console.log(`✅ Deposited ₹${amount}. Balance: ₹${balance}`);
        },
        
        withdraw(amount) {
            if (amount > balance) {
                console.log("❌ Insufficient funds");
                return false;
            }
            balance -= amount;
            logTransaction("withdrawal", amount);
            console.log(`✅ Withdrew ₹${amount}. Balance: ₹${balance}`);
            return true;
        },
        
        getBalance() {
            return balance;
        },
        
        getStatement() {
            return [...transactionLog];  // Return copy, not original
        }
    };
})();

// Usage
BankAccount.deposit(1000);      // ✅ Deposited ₹1000. Balance: ₹1000
BankAccount.withdraw(300);      // ✅ Withdrew ₹300. Balance: ₹700
console.log(BankAccount.getBalance());  // 700

// Private variables are truly inaccessible:
console.log(BankAccount.balance);        // undefined ← Can't access!
console.log(BankAccount.transactionLog); // undefined ← Can't access!
```

### 4.3 Why it Works

The IIFE creates a **closure**. The inner function (the returned object's methods) remembers the variables from the outer function (balance, transactionLog), even after the IIFE has finished executing. This is the "closure" keeping private data alive.

### 4.4 Modern ES Modules (Preferred Today)

ES6 modules (`import`/`export`) provide a built-in module system that's now the standard:

```javascript
// ── bankAccount.js ──
// Not exported = private (can't be imported by other files)
let balance = 0;

function logTransaction(type, amount) {
    console.log(`${type}: ${amount}`);
}

// Exported = public API
export function deposit(amount) {
    balance += amount;
    logTransaction("deposit", amount);
}

export function getBalance() {
    return balance;
}

// ── main.js ──
import { deposit, getBalance } from './bankAccount.js';

deposit(500);
console.log(getBalance());  // 500
// balance variable is NOT accessible — it wasn't exported
```

> **Which to use?** Use **ES Modules** for new projects. Use the **IIFE Module Pattern** when working with legacy code or when you need a self-contained module in a `<script>` tag.

---

## 5. Prototype Pattern

### 5.1 What is it?

The Prototype pattern creates new objects by **cloning** (copying) an existing object, rather than building from scratch.

> **Real-world Analogy:** Instead of designing a resume from scratch every time, you **duplicate** your template resume and just change the details (company name, job title). The template IS the prototype.

**When to use:**
- Object creation is expensive (requires DB queries, API calls, complex setup)
- You need many objects that are similar to an existing one
- Understanding JavaScript's own object model (prototypal inheritance)

### 5.2 Implementation

```javascript
// ══════════════════════════════════════════════
// Method 1: Object.create() — Create from prototype
// ══════════════════════════════════════════════
const vehiclePrototype = {
    type: "vehicle",
    wheels: 4,
    start() {
        console.log(`${this.name} started! Type: ${this.type}`);
    },
    describe() {
        console.log(`${this.name}: ${this.wheels} wheels, ${this.type}`);
    }
};

// Create new objects using vehiclePrototype as their prototype
const car = Object.create(vehiclePrototype);
car.name = "Tesla Model 3";
car.type = "electric car";

const truck = Object.create(vehiclePrototype);
truck.name = "Ford F-150";
truck.type = "pickup truck";
truck.wheels = 6;

car.start();      // "Tesla Model 3 started! Type: electric car"
truck.describe();  // "Ford F-150: 6 wheels, pickup truck"

// The prototype chain:
console.log(car.__proto__ === vehiclePrototype);  // true
console.log(car.hasOwnProperty('name'));          // true  (own property)
console.log(car.hasOwnProperty('start'));         // false (inherited from prototype)


// ══════════════════════════════════════════════
// Method 2: Using structuredClone() for deep copying (Modern JS)
// ══════════════════════════════════════════════
const original = {
    name: "Rahul",
    scores: [90, 85, 92],
    address: { city: "Mumbai", state: "MH" }
};

const clone = structuredClone(original);
clone.scores.push(100);
clone.address.city = "Delhi";

console.log(original.scores);       // [90, 85, 92] ← Not affected!
console.log(original.address.city); // "Mumbai"     ← Not affected!

// Compare with spread (shallow copy):
const shallow = { ...original };
shallow.scores.push(999);
console.log(original.scores);       // [90, 85, 92, 999] ← AFFECTED!! 😱
// Spread only copies the first level — nested objects share references
```

> **Key insight:** JavaScript itself is a **prototypal** language. Every object has a prototype, and `class` syntax is just syntactic sugar over prototypal inheritance. Understanding prototypes means understanding JavaScript deeply.

---

## 6. Proxy Pattern

### 6.1 What is it?

The Proxy pattern creates a **wrapper** around an object that intercepts and controls access to it — for validation, logging, caching, or access control.

> **Real-world Analogy:** A credit card is a proxy for cash. Instead of giving someone direct access to your bank account, the credit card (proxy) controls access — it validates PIN, checks balance, logs transactions, and can be blocked if stolen.

**When to use:**
- Input validation (prevent invalid data from being set)
- Logging / debugging (track property access)
- Lazy loading (load expensive data only when accessed)
- Access control (restrict who can read/write certain properties)
- Reactive systems (Vue.js uses Proxy for reactivity!)

### 6.2 Implementation

```javascript
// ══════════════════════════════════════════════
// Example 1: Validation Proxy
// ══════════════════════════════════════════════
const user = {
    name: "Rahul",
    age: 25,
    email: "rahul@test.com"
};

const validatedUser = new Proxy(user, {
    // Intercept property SET
    set(target, property, value) {
        if (property === 'age') {
            if (typeof value !== 'number') {
                throw new TypeError("Age must be a number");
            }
            if (value < 0 || value > 150) {
                throw new RangeError("Age must be between 0 and 150");
            }
        }
        if (property === 'email') {
            if (!value.includes('@')) {
                throw new Error("Invalid email format");
            }
        }
        target[property] = value;
        return true;  // Indicate success
    },
    
    // Intercept property GET
    get(target, property) {
        if (property === 'age') {
            return `${target[property]} years old`;
        }
        if (!(property in target)) {
            return `Property "${property}" does not exist`;
        }
        return target[property];
    }
});

console.log(validatedUser.age);         // "25 years old" (formatted)
console.log(validatedUser.name);        // "Rahul"
console.log(validatedUser.nonExistent); // 'Property "nonExistent" does not exist'

validatedUser.age = 30;                 // ✅ OK
// validatedUser.age = -5;              // ❌ RangeError: Age must be between 0 and 150
// validatedUser.age = "thirty";        // ❌ TypeError: Age must be a number
// validatedUser.email = "bad";         // ❌ Error: Invalid email format


// ══════════════════════════════════════════════
// Example 2: Logging Proxy (Debug tool)
// ══════════════════════════════════════════════
function createLoggingProxy(target, name) {
    return new Proxy(target, {
        get(target, property) {
            console.log(`📖 Reading ${name}.${property}`);
            return target[property];
        },
        set(target, property, value) {
            console.log(`✏️ Writing ${name}.${property} = ${JSON.stringify(value)}`);
            target[property] = value;
            return true;
        }
    });
}

const settings = createLoggingProxy({ theme: "dark", lang: "en" }, "settings");
settings.theme;           // 📖 Reading settings.theme
settings.lang = "hi";     // ✏️ Writing settings.lang = "hi"


// ══════════════════════════════════════════════
// Example 3: Negative Array Index (Python-like!)
// ══════════════════════════════════════════════
function createSmartArray(...items) {
    return new Proxy(items, {
        get(target, property) {
            const index = Number(property);
            if (Number.isInteger(index) && index < 0) {
                // Negative index → count from end
                return target[target.length + index];
            }
            return target[property];
        }
    });
}

const arr = createSmartArray("a", "b", "c", "d", "e");
console.log(arr[-1]);  // "e" (last element!)
console.log(arr[-2]);  // "d" (second to last!)
```

> **Fun fact:** Vue 3 uses JavaScript `Proxy` internally for its reactivity system. When you modify a reactive variable, the Proxy intercepts the change and triggers UI updates automatically!

---

## 7. Iterator Pattern

### 7.1 What is it?

The Iterator pattern provides a way to access elements of a collection **one at a time**, in sequence, without exposing how the collection is stored internally.

> **Real-world Analogy:** A TV remote's "Next Channel" button. You press it and get the next channel without knowing how channels are stored in memory. You just get the next item in the sequence.

**When to use:**
- Traversing custom data structures (trees, graphs, linked lists)
- Creating lazy sequences (generate values on demand)
- Implementing pagination
- JavaScript's `for...of` loop, spread operator, and destructuring all use the Iterator protocol

### 7.2 Implementation

```javascript
// ══════════════════════════════════════════════
// Custom Iterator (following JS Iterator Protocol)
// ══════════════════════════════════════════════
function createRange(start, end, step = 1) {
    let current = start;
    
    return {
        // The iterator protocol requires a next() method
        next() {
            if (current <= end) {
                const value = current;
                current += step;
                return { value, done: false };
            }
            return { done: true };  // Signal: no more items
        },
        
        // Make it work with for...of
        [Symbol.iterator]() { return this; }
    };
}

const range = createRange(1, 10, 2);

// Manual iteration
console.log(range.next()); // { value: 1, done: false }
console.log(range.next()); // { value: 3, done: false }
console.log(range.next()); // { value: 5, done: false }

// Or use for...of (because we defined Symbol.iterator)
for (const num of createRange(0, 5)) {
    console.log(num);  // 0, 1, 2, 3, 4, 5
}

// Or spread into an array
const numbers = [...createRange(1, 5)];  // [1, 2, 3, 4, 5]


// ══════════════════════════════════════════════
// Make your own class iterable
// ══════════════════════════════════════════════
class TodoList {
    constructor() {
        this.items = [];
    }
    
    add(task) {
        this.items.push({ task, done: false });
    }
    
    // This makes TodoList work with for...of, spread, destructuring
    [Symbol.iterator]() {
        let index = 0;
        const items = this.items;
        
        return {
            next() {
                if (index < items.length) {
                    return { value: items[index++], done: false };
                }
                return { done: true };
            }
        };
    }
}

const todos = new TodoList();
todos.add("Learn JavaScript");
todos.add("Learn Design Patterns");
todos.add("Build a project");

// Now works with for...of!
for (const todo of todos) {
    console.log(`📌 ${todo.task}`);
}
// 📌 Learn JavaScript
// 📌 Learn Design Patterns
// 📌 Build a project

// And with spread!
const allTodos = [...todos];  // Array of todo objects
```

> **Why Iterator matters in JS:** Arrays, Strings, Maps, Sets — they're all iterable. When you write `for (const item of array)`, JavaScript is calling the array's `[Symbol.iterator]()` method under the hood. Understanding this helps you create custom iterable objects.

---

## 8. Complex Technical Scenarios

### 8.1 Is Singleton an anti-pattern?

**Explanation:** Many developers consider Singleton an anti-pattern because:

| Problem | Explanation |
|---------|------------|
| **Global state** | Singleton is essentially a global variable. Any part of your code can modify it, making bugs hard to trace |
| **Tight coupling** | Code that uses a Singleton is directly tied to that specific class, making it hard to swap implementations |
| **Testing difficulty** | Mocking/replacing a Singleton in tests is hard — the static instance persists between tests |
| **Hidden dependencies** | When a function uses a Singleton internally, you can't tell from its signature that it depends on global state |

**When it IS acceptable:** Configuration objects, logging services, connection pools — things that genuinely should have only one instance and rarely need mocking.

> **Modern alternative:** Use **Dependency Injection** instead. Let a DI container manage the lifecycle and ensure single instances where needed, without the downsides of a true Singleton.

### 8.2 Factory vs Abstract Factory — what's the difference?

**Explanation:**

| Pattern | Creates | Example |
|---------|---------|---------|
| **Factory Method** | **ONE type** of object (decides subclass based on input) | `createNotification("email")` → returns `EmailNotification` or `SMSNotification` |
| **Abstract Factory** | **A family** of related objects (ensures compatibility) | `createUIKit("dark")` → returns matching `DarkButton` + `DarkInput` + `DarkCard` all from the same theme |

```javascript
// Factory Method — creates ONE type
NotificationFactory.create("email");  // Returns some Notification subclass

// Abstract Factory — creates a FAMILY of related objects
const darkTheme = ThemeFactory.create("dark");
darkTheme.createButton();    // DarkButton
darkTheme.createInput();     // DarkInput
darkTheme.createCard();      // DarkCard
// All components are guaranteed to belong to the "dark" family
```

### 8.3 When should you use design patterns?

**Explanation:** Design patterns are **not** meant to be used everywhere. They solve **specific, recurring problems**:

| Problem You're Facing | Pattern to Consider |
|----------------------|-------------------|
| Need only one instance of something | Singleton |
| Object creation depends on conditions | Factory |
| Need to notify multiple parts of your app about changes | Observer |
| Need to hide private state | Module |
| Need to validate/control object access | Proxy |
| Need to traverse a custom collection | Iterator |
| Need clones of complex objects | Prototype |

> **Warning:** Over-engineering with patterns is as bad as not using them. Don't add a Factory when `new MyClass()` works fine. Don't add an Observer when you only have one listener. Patterns should **simplify** your code, not add complexity.

---

## 9. Key Topics & Explanations

### 9.1 Why use the Module Pattern?

**Explanation:** JavaScript (before ES6) had no native way to create private variables. Everything in a `<script>` tag was global. The Module Pattern used closures to achieve **encapsulation** — hiding internal state and only exposing a public API.

```javascript
// Without Module Pattern: Everything is global! 💀
var balance = 1000;              // Anyone can access this
var secret = "admin123";         // Anyone can read this

// With Module Pattern: Private by default, expose what you choose ✅
const Account = (function() {
    let balance = 1000;          // PRIVATE — nobody can access directly
    let secret = "admin123";     // PRIVATE
    
    return { getBalance() { return balance; } };  // Only this is public
})();
```

> **Today:** ES Modules (`import`/`export`) handle this natively. The Module Pattern is still relevant for understanding closures and maintaining legacy code.

### 9.2 Where is the Observer Pattern used in real life?

**Explanation:** The Observer Pattern is **everywhere** in JavaScript:

| Usage | How it's the Observer Pattern |
|-------|------------------------------|
| **DOM Events** | `button.addEventListener('click', fn)` — button is Subject, `fn` is Observer |
| **Redux** | `store.subscribe(listener)` — store is Subject, listener is Observer |
| **Node.js EventEmitter** | `emitter.on('data', fn)` — emitter is Subject, `fn` is Observer |
| **RxJS** | `observable.subscribe(observer)` — built entirely on Observer Pattern |
| **React hooks** | `useEffect(() => {...}, [dep])` — re-runs when `dep` changes (observing state) |
| **WebSockets** | `socket.on('message', fn)` — socket is Subject |
| **MutationObserver** | Observes DOM changes — the DOM is the Subject |

### 9.3 Prototype Pattern vs Class Pattern

**Explanation:**

| Aspect | Prototype Pattern | Class Pattern |
|--------|-----------------|---------------|
| **Foundation** | Object-based — objects inherit from other objects | Blueprint-based — objects are instances of classes |
| **Inheritance** | Prototypal — `Object.create(prototype)` | Classical — `class Child extends Parent` |
| **Memory** | Methods shared via prototype chain (efficient) | Methods on class prototype (same mechanism!) |
| **Nature** | JavaScript's native model | Syntactic sugar over prototypes |

```javascript
// Under the hood, classes ARE prototypes:
class Dog { bark() { return "Woof!"; } }

// is equivalent to:
function Dog() {}
Dog.prototype.bark = function() { return "Woof!"; };

// Both create the exact same prototype chain!
```

> **Key insight:** JavaScript `class` is NOT like Java/C++ classes. It's just nicer syntax for prototypal inheritance. Understanding prototypes helps you debug unexpected behavior.

### 9.4 How does Redux use the Singleton and Observer patterns?

**Explanation:** Redux combines two patterns:

1. **Singleton:** There is only **one store** per application that holds the entire state tree. All components read from the same store.

2. **Observer:** Components **subscribe** to the store. When state changes (via dispatch/reducer), all subscribed components are **notified** and re-render with new data.

```javascript
// Simplified Redux — Singleton + Observer
function createStore(reducer) {
    let state = reducer(undefined, { type: '@@INIT' });  // Singleton state
    let listeners = [];                                    // Observer list
    
    return {
        getState: () => state,
        dispatch(action) {
            state = reducer(state, action);
            listeners.forEach(fn => fn());  // Notify all observers
        },
        subscribe(fn) {
            listeners.push(fn);
            return () => { listeners = listeners.filter(l => l !== fn); };
        }
    };
}
```
