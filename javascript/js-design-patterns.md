# JavaScript Design Patterns - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Singleton, Factory, Observer, Module, Prototype, Proxy, and behavioral patterns.

--

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

--

## 1. Singleton Pattern

Allows only **one instance** of a class to exist and provides a global access point.

### Implementation

```javascript
// Using ES6 Class & Static Property
class Database {
    constructor(data) {
        if (Database.instance) {
            return Database.instance;
        }
        this.data = data;
        Database.instance = this;
    }

    getData() {
        return this.data;
    }
}

const db1 = new Database("MongoDB");
const db2 = new Database("MySQL");

console.log(db1 === db2); // true
console.log(db2.getData()); // "MongoDB" (First instance data preserved)

// Using Object Literal (Simplest Singleton)
const Config = {
    apiKey: "12345",
    apiUrl: "https://api.com"
};
Object.freeze(Config); // Prevent modification
```

--

## 2. Factory Pattern

Creates objects without specifying the exact class of object that will be created.

### Implementation

```javascript
class Car {
    constructor(options) {
        this.doors = options.doors || 4;
        this.state = options.state || "brand new";
        this.color = options.color || "silver";
    }
}

class Truck {
    constructor(options) {
        this.state = options.state || "used";
        this.wheelSize = options.wheelSize || "large";
        this.color = options.color || "blue";
    }
}

// Factory
class VehicleFactory {
    createVehicle(options) {
        if (options.vehicleType === "car") {
            return new Car(options);
        } else if (options.vehicleType === "truck") {
            return new Truck(options);
        }
    }
}

const factory = new VehicleFactory();
const car = factory.createVehicle({ 
    vehicleType: "car", 
    color: "yellow", 
    doors: 6 
});

console.log(car instanceof Car); // true
console.log(car); // Car { doors: 6, state: 'brand new', color: 'yellow' }
```

--

## 3. Observer Pattern (Pub/Sub)

Defines a subscription mechanism to notify multiple objects about any events that happen to the object they're observing. **Crucial for Redux/RxJS**.

### Implementation

```javascript
class Subject {
    constructor() {
        this.observers = [];
    }

    subscribe(fn) {
        this.observers.push(fn);
    }

    unsubscribe(fn) {
        this.observers = this.observers.filter(obs => obs !== fn);
    }

    notify(data) {
        this.observers.forEach(observer => observer(data));
    }
}

// Usage
const subject = new Subject();

function observer1(data) {
    console.log(`Observer 1 received: ${data}`);
}

function observer2(data) {
    console.log(`Observer 2 received: ${data}`);
}

subject.subscribe(observer1);
subject.subscribe(observer2);

subject.notify("Hello World");
// Output:
// Observer 1 received: Hello World
// Observer 2 received: Hello World

subject.unsubscribe(observer1);
subject.notify("Again");
// Output:
// Observer 2 received: Again
```

--

## 4. Module Pattern

Used to emulate classes with private and public access (Encapsulation).

### Implementation (IIFE)

```javascript
const BankAccount = (function() {
    // Private variables
    let balance = 0;

    function changeBalance(amount) {
        balance += amount;
    }

    // Public API
    return {
        deposit: function(amount) {
            changeBalance(amount);
            console.log(`Deposited: ${amount}`);
        },
        withdraw: function(amount) {
            if (amount <= balance) {
                changeBalance(-amount);
                console.log(`Withdrew: ${amount}`);
            } else {
                console.log("Insufficient funds");
            }
        },
        getBalance: function() {
            return balance;
        }
    };
})();

BankAccount.deposit(100);
BankAccount.withdraw(50);
console.log(BankAccount.getBalance()); // 50
console.log(BankAccount.balance); // undefined (Private)
```

### Modern ES Modules (Preferred)

```javascript
// math.js
const privateVal = 42;
export const add = (a, b) => a + b;
export default function subtract(a, b) { return a - b; }
```

--

## 5. Prototype Pattern

Creates objects based on a template of an existing object through cloning.

```javascript
const car = {
    wheels: 4,
    start() {
        return "started";
    }
};

// Create new object with 'car' as prototype
const myCar = Object.create(car);
myCar.owner = "Rahul";

console.log(myCar.owner); // "Rahul"
console.log(myCar.wheels); // 4 (Inherited)
console.log(myCar.__proto__ === car); // true
```

--

## 6. Proxy Pattern

Provides a surrogate or placeholder for another object to control access to it.

```javascript
const user = {
    name: "Rahul",
    age: 25
};

const userProxy = new Proxy(user, {
    get(target, prop) {
        if (prop === 'age') {
            return `${target[prop]} years old`;
        }
        return target[prop];
    },
    set(target, prop, value) {
        if (prop === 'age' && typeof value !== 'number') {
            throw new Error("Age must be a number");
        }
        target[prop] = value;
        return true;
    }
});

console.log(userProxy.age); // "25 years old"
userProxy.age = 30; // OK
// userProxy.age = "thirty"; // Error
```

--

## 7. Iterator Pattern

Provides a way to access elements of a collection sequentially without exposing its underlying representation.

```javascript
const items = [1, "Rahul", false];

function createIterator(array) {
    let index = 0;
    return {
        next: function() {
            return index < array.length ?
                { value: array[index++], done: false } :
                { done: true };
        }
    };
}

const iterator = createIterator(items);
console.log(iterator.next()); // { value: 1, done: false }
console.log(iterator.next()); // { value: "Rahul", done: false }
```

--

## 8. Complex Technical Scenarios

### Topic 1: Is Singleton an anti-pattern?
**Answer:** Often considered an anti-pattern because:
1. It introduces **Global State** (hard to debug).
2. Tightly couples code to the specific implementation.
3. Makes **Unit Testing** difficult (mocking static instances is hard).

### Topic 2: What is the difference between Factory and Abstract Factory?
**Answer:**
- **Factory Method**: Creates **one** type of object (e.g., `CarFactory` creates `Car`).
- **Abstract Factory**: Creates **families** of related objects (e.g., `VehicleFactory` creates `Car`, `Truck`, `Bike`).

--

## 9. Key Topics & Explanations

### Topic 1: Why use the Module Pattern?
**Answer:** To achieve **encapsulation** and create **private** variables/methods in JavaScript (before Classes/ES Modules). It prevents polluting the global namespace.

### Topic 2: Where is the Observer Pattern used in real life?
**Answer:**
1. **DOM Events**: `document.addEventListener('click', fn)` calls `fn` when click happens.
2. **Redux**: The store notifies subscribers on state change.
3. **Node.js**: `EventEmitter` class.
4. **RxJS**: Observables are the core.

### Topic 3: Explain Prototype vs Class Pattern.
**Answer:**
- **Class Pattern**: Blueprint-based. Objects are instances of a class. (Java/C++ style).
- **Prototype Pattern**: Object-based. Objects are clones/extensions of other objects. JavaScript is naturally prototypal, even though `class` syntax hides it.

### Topic 4: How does Redux use the Singleton pattern?
**Answer:** The Redux **Store** is a Singleton. There is typically only **one** store per application that holds the entire state tree.
