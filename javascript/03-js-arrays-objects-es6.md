# JavaScript Arrays, Objects & ES6+ Features - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Array Methods, Object Manipulation, Prototypes, Classes, ES6+ Features, Map/Set, and tricky technical scenarios.

--

## Table of Contents

1. [Array Methods In-Depth](#array-methods-in-depth)
2. [Objects & Prototypes](#objects-prototypes)
3. [Classes & Inheritance](#classes-inheritance)
4. [Destructuring & Spread Syntax](#destructuring-spread-syntax)
5. [Map, Set, WeakMap, WeakSet](#map-set-weakmap-weakset)
6. [Iterators & Generators](#iterators-generators)
7. [Modules (ESM vs CommonJS)](#modules-esm-vs-commonjs)
8. [Complex Technical Scenarios](#complex-technical-scenarios)
9. [Coding Problems](#coding-problems)
10. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. Array Methods In-Depth

Arrays in JavaScript are dynamic, resizable, and can hold mixed types.

### Essential Methods Cheat Sheet

| Method | Modifies Original? | Returns | Description |
|----|----------|-----|-------|
| `push()` | ✅ Yes | New length | Add to end |
| `pop()` | ✅ Yes | Removed item | Remove from end |
| `shift()` | ✅ Yes | Removed item | Remove from start |
| `unshift()`| ✅ Yes | New length | Add to start |
| `splice()` | ✅ Yes | Deleted items | Add/Remove at index |
| `slice()` | ❌ No | New Array | Extract portion |
| `map()` | ❌ No | New Array | Transform elements |
| `filter()` | ❌ No | New Array | Select elements |
| `reduce()` | ❌ No | Single Value | Accumulate result |
| `sort()` | ✅ Yes | Sorted Array | Sort elements |
| `reverse()`| ✅ Yes | Reversed Array| Reverse order |
| `concat()` | ❌ No | New Array | Merge arrays |
| `fill()` | ✅ Yes | Modified Array| Fill with value |
| `find()` | ❌ No | Element | Find first match |
| `forEach()`| ❌ No | undefined | Iterate (side effects) |

### slice vs splice

This is a classic technical review scenario.

```javascript
const arr = ['a', 'b', 'c', 'd', 'e'];

// slice(start, end) - specific portion, end exclusive
// Does NOT modify original
const sliced = arr.slice(1, 3); 
console.log(sliced); // ['b', 'c']
console.log(arr);    // ['a', 'b', 'c', 'd', 'e']

// splice(start, deleteCount, ...itemsToAdd)
// MODIFIES original
const spliced = arr.splice(1, 2, 'X', 'Y');
console.log(spliced); // ['b', 'c'] (removed items)
console.log(arr);     // ['a', 'X', 'Y', 'd', 'e']
```

### map, filter, reduce

The "Holy Trinity" of functional array programming.

```javascript
const numbers = [1, 2, 3, 4, 5];

// map: transform
const doubled = numbers.map(n => n * 2);
// [2, 4, 6, 8, 10]

// filter: select
const evens = numbers.filter(n => n % 2 === 0);
// [2, 4]

// reduce: accumulate
const sum = numbers.reduce((acc, curr) => acc + curr, 0);
// 15

// Chaining
const result = numbers
    .filter(n => n % 2 === 0)  // [2, 4]
    .map(n => n * 10)          // [20, 40]
    .reduce((a, b) => a + b);  // 60
```

### Advanced reduce() Usage

`reduce` is arguably the most powerful array method. It can implement map, filter, and more.

```javascript
const users = [
    { name: "Rahul", age: 25, city: "Pune" },
    { name: "Priya", age: 30, city: "Mumbai" },
    { name: "Amit", age: 25, city: "Pune" }
];

// Group by City
const byCity = users.reduce((acc, user) => {
    // Check if key exists
    if (!acc[user.city]) {
        acc[user.city] = [];
    }
    // Push matches
    acc[user.city].push(user);
    return acc;
}, {});

/* Output:
{
    "Pune": [{name: "Rahul"...}, {name: "Amit"...}],
    "Mumbai": [{name: "Priya"...}]
}
*/

// Flatten Array with reduce
const nested = [[1, 2], [3, 4], [5]];
const flat = nested.reduce((acc, curr) => acc.concat(curr), []);
// [1, 2, 3, 4, 5]
```

### Searching and Finding

```javascript
const arr = [5, 12, 8, 130, 44];

// find() - returns first element
const found = arr.find(element => element > 10); // 12

// findIndex() - returns index of first element
const idx = arr.findIndex(element => element > 10); // 1

// indexOf() - returns index of value (primitives)
const i = arr.indexOf(12); // 1

// includes() - returns boolean
const hasFive = arr.includes(5); // true

// some() - checks if AT LEAST ONE passes
const hasBigNumber = arr.some(n => n > 100); // true

// every() - checks if ALL pass
const allPositive = arr.every(n => n > 0); // true
```

--

## 2. Objects & Prototypes

### Creating Objects

```javascript
// 1. Object Literal
const obj = { key: "value" };

// 2. Object Constructor
const obj2 = new Object();

// 3. Object.create() - with specific prototype
const proto = { greet: function() { return "Hello"; } };
const obj3 = Object.create(proto);
console.log(obj3.greet()); // "Hello" (inherited)
```

### Object Methods (Static)

```javascript
const user = { name: "Rahul", age: 25 };

// Object.keys()
console.log(Object.keys(user)); // ["name", "age"]

// Object.values()
console.log(Object.values(user)); // ["Rahul", 25]

// Object.entries()
console.log(Object.entries(user)); // [["name", "Rahul"], ["age", 25]]

// Object.assign() - shallow copy / merge
const details = { city: "Delhi" };
const merged = Object.assign({}, user, details); 
// { name: "Rahul", age: 25, city: "Delhi" }

// Object.freeze() - Immutable
Object.freeze(user);
user.age = 30; // Fails silently (or throws in strict mode)
console.log(user.age); // 25

// Object.seal() - Cannot add/delete, but CAN modify existing
const config = { port: 8080 };
Object.seal(config);
config.port = 9000; // OK
config.host = "localhost"; // Fails
delete config.port; // Fails
```

### Prototypes and Inheritance

JavaScript uses **Prototypal Inheritance**. Every object has a hidden link to another object called its **prototype**.

```javascript
// The Prototype Chain
const animal = {
    eats: true,
    walk() { console.log("Animal walking"); }
};

const rabbit = {
    jumps: true,
    __proto__: animal // Sets animal as prototype
};

console.log(rabbit.jumps); // true (own property)
console.log(rabbit.eats);  // true (inherited)
rabbit.walk();             // "Animal walking" (inherited)

// Missing property?
// rabbit.fly -> checks rabbit -> checks animal -> checks Object.prototype -> null
```

#### Function Prototypes (Before Classes)

```javascript
function Person(name) {
    this.name = name;
}

// Methods added to prototype to share memory
Person.prototype.sayHello = function() {
    return `Hello, I'm ${this.name}`;
};

const p1 = new Person("Rahul");
const p2 = new Person("Amit");

console.log(p1.sayHello === p2.sayHello); // true (same function instance)
// If defined inside constructor, it would be false (new function per instance)
```

--

## 3. Classes & Inheritance

ES6 introduced `class` syntax, which is syntactic sugar over prototypal inheritance.

### Class Syntax

```javascript
class User {
    // Constructor
    constructor(name) {
        this.name = name;
    }

    // Method (added to User.prototype)
    sayHi() {
        console.log(`Hi, ${this.name}`);
    }

    // Static Method (called on class, not instance)
    static compare(user1, user2) {
        return user1.name === user2.name;
    }
    
    // Getter/Setter
    get uppercaseName() {
        return this.name.toUpperCase();
    }
    
    set newName(value) {
        if(value.length < 3) throw new Error("Name too short");
        this.name = value;
    }
}

const u1 = new User("Rahul");
u1.sayHi(); // "Hi, Rahul"
```

### Inheritance (`extends`)

```javascript
class Animal {
    constructor(name) {
        this.name = name;
    }
    speak() {
        console.log(`${this.name} makes a noise.`);
    }
}

class Dog extends Animal {
    constructor(name) {
        super(name); // Call parent constructor
    }
    
    // Override method
    speak() {
        super.speak(); // Call parent method
        console.log(`${this.name} barks.`);
    }
}

const d = new Dog("Tommy");
d.speak();
// "Tommy makes a noise."
// "Tommy barks."
```

### Private Fields (ES2022)

```javascript
class BankAccount {
    #balance = 0; // Private field (starts with #)

    deposit(amount) {
        this.#balance += amount;
    }

    getBalance() {
        return this.#balance;
    }
}

const acc = new BankAccount();
acc.deposit(100);
// console.log(acc.#balance); // SyntaxError: Private field '#balance' must be declared
console.log(acc.getBalance()); // 100
```

--

## 4. Destructuring & Spread Syntax

### Array Destructuring

```javascript
const numbers = [1, 2, 3, 4];
const [first, second] = numbers; // first=1, second=2

// Skip items
const [a, , c] = numbers; // a=1, c=3

// Rest operator
const [head, ...tail] = numbers; // head=1, tail=[2,3,4]

// Swapping variables (Tricky Scenario!)
let x = 1, y = 2;
[x, y] = [y, x]; // x=2, y=1
```

### Object Destructuring

```javascript
const user = { 
    id: 42, 
    name: "Rahul", 
    address: { city: "Pune" } 
};

// Basic
const { name, id } = user;

// Renaming
const { name: userName } = user; // userName = "Rahul"

// Default values
const { role = "Guest" } = user;

// Nested
const { address: { city } } = user; // city = "Pune"

// Function parameters
function printUser({ name, id }) {
    console.log(`${name} (${id})`);
}
```

### Spread Syntax (`...`)

```javascript
// Arrays
const arr1 = [1, 2];
const arr2 = [...arr1, 3, 4]; // [1, 2, 3, 4]
const copy = [...arr1]; // Shallow copy

// Objects
const obj1 = { a: 1, b: 2 };
const obj2 = { ...obj1, c: 3 }; // { a: 1, b: 2, c: 3 }
const obj3 = { ...obj1, a: 99 }; // { a: 99, b: 2 } (Override)
```

--

## 5. Map, Set, WeakMap, WeakSet

ES6 introduced new data structures.

### Map

Key-value pairs where keys can be **any type** (including objects).

```javascript
const map = new Map();

const keyObj = {};
const keyFunc = function() {};

map.set('string', 'value1');
map.set(keyObj, 'value2');
map.set(keyFunc, 'value3');

console.log(map.size); // 3
console.log(map.get(keyObj)); // 'value2'
console.log(map.has('string')); // true

// Iteration
for (const [key, value] of map) {
    console.log(key, value);
}

// Map vs Object
// 1. Keys: Object keys are strings/symbols. Map keys can be anything.
// 2. Order: Map preserves insertion order. Object (mostly) does too now, but Map is guaranteed.
// 3. Performance: Map is optimized for frequent add/remove.
```

### Set

Collection of **unique** values.

```javascript
const set = new Set();

set.add(1);
set.add(5);
set.add(5); // Duplicate ignored
set.add(1); // Duplicate ignored

console.log(set.size); // 2
console.log(set.has(5)); // true

// Remove duplicates from Array
const arr = [1, 2, 2, 3, 3, 4];
const unique = [...new Set(arr)]; // [1, 2, 3, 4]
```

### WeakMap & WeakSet

- **WeakMap** keys must be objects.
- **WeakSet** values must be objects.
- They hold **weak references** to keys/values. If the object is not referenced elsewhere, it can be garbage collected.
- They are **not iterable** (no forEach, keys(), size).

```javascript
let obj = { name: "test" };
const weakMap = new WeakMap();

weakMap.set(obj, "metadata");

obj = null; 
// The entry in weakMap is now eligible for garbage collection automatically.
// Great for caching or storing metadata for DOM elements.
```

--

## 6. Iterators & Generators

### Iterators

An object is iterable if it implements the `Symbol.iterator` method.

```javascript
const arr = [1, 2, 3];
const iter = arr[Symbol.iterator]();

console.log(iter.next()); // { value: 1, done: false }
console.log(iter.next()); // { value: 2, done: false }
console.log(iter.next()); // { value: 3, done: false }
console.log(iter.next()); // { value: undefined, done: true }

// Making custom object iterable
const range = {
    start: 1,
    end: 3,
    [Symbol.iterator]() {
        let current = this.start;
        let last = this.end;
        return {
            next() {
                if (current <= last) {
                    return { value: current++, done: false };
                } else {
                    return { done: true };
                }
            }
        };
    }
};

for (const num of range) {
    console.log(num); // 1, 2, 3
}
```

### Generators

Functions that can pause and resume execution. Marked with `function*` and uses `yield`.

```javascript
function* numberGen() {
    yield 1;
    yield 2;
    yield 3;
}

const gen = numberGen();
console.log(gen.next().value); // 1
console.log(gen.next().value); // 2

// Practical Use: ID Generator
function* idMaker() {
    let index = 0;
    while (true) {
        yield index++;
    }
}
const ids = idMaker();
console.log(ids.next().value); // 0
console.log(ids.next().value); // 1
```

--

## 7. Modules (ESM vs CommonJS)

### CommonJS (Node.js default)

```javascript
// export
const add = (a, b) => a + b;
module.exports = { add };

// import
const { add } = require('./math');
```

### ES Modules (Modern Standard)

```javascript
// export
export const add = (a, b) => a + b;
export default function subtract(a, b) { return a - b; }

// import
import subtract, { add } from './math.js';
```

--

## 8. Complex Technical Scenarios

### Topic 1: What is the output?
```javascript
const arr = [10, 20, 30];
arr[10] = 99;
console.log(arr.length);
console.log(arr);

// Answer: 
// 11
// [10, 20, 30, empty x 7, 99] (Sparse array)
```

### Topic 2: How empty slots behave?
```javascript
const arr = [1, , 3]; // length 3
// map skips empty slots
console.log(arr.map(x => x * 2)); // [2, empty, 6]
// join includes them
console.log(arr.join('-')); // "1-3"
```

### Topic 3: Object keys order?
```javascript
const obj = {
    "2": "b",
    "1": "a",
    "b": "c",
    "a": "d"
};
console.log(Object.keys(obj));

// Answer: ["1", "2", "b", "a"]
// Integer-like keys are sorted first (ascending).
// String keys follow insertion order.
```

### Topic 4: `__proto__` vs `prototype`?
**Answer:**
- `prototype` is a property of **Functions** (constructor). It defines what the `__proto__` of created instances will be.
- `__proto__` is a property of **Instances** (Objects). It points to the prototype of the constructor that created it.
`p1.__proto__ === Person.prototype` is true.

--

## 9. Coding Problems

### Problem 1: Deep Flatten Object
Convert a nested object into a flat one with dot notation keys.

```javascript
function flattenObject(obj, prefix = '') {
    return Object.keys(obj).reduce((acc, key) => {
        const pre = prefix.length ? prefix + '.' : '';
        const value = obj[key];
        
        if (typeof value === 'object' && value !== null && !Array.isArray(value)) {
            Object.assign(acc, flattenObject(value, pre + key));
        } else {
            acc[pre + key] = value;
        }
        return acc;
    }, {});
}

// Input: { a: 1, b: { c: 2, d: { e: 3 } } }
// Output: { a: 1, 'b.c': 2, 'b.d.e': 3 }
```

### Problem 2: Polyfill for `.map()`

```javascript
Array.prototype.myMap = function(callback) {
    const result = [];
    for (let i = 0; i < this.length; i++) {
        // Check if index exists (handle sparse arrays)
        if (this.hasOwnProperty(i)) {
            result.push(callback(this[i], i, this));
        } else {
            result.length++; // Maintain sparse gap
        }
    }
    return result;
}
```

### Problem 3: Polyfill for `.reduce()`

```javascript
Array.prototype.myReduce = function(callback, initialValue) {
    let accumulator = initialValue;
    let startIndex = 0;

    if (accumulator === undefined) {
        if (this.length === 0) throw new TypeError('Reduce of empty array with no initial value');
        accumulator = this[0];
        startIndex = 1;
    }

    for (let i = startIndex; i < this.length; i++) {
        if (this.hasOwnProperty(i)) {
            accumulator = callback(accumulator, this[i], i, this);
        }
    }
    return accumulator;
};
```

--

## 10. Key Topics & Explanations

### Topic 1: Difference between `map` and `forEach`?
**Answer:** `map` returns a **new array** with transformed elements and does not mutate the original (unless logic does). `forEach` returns `undefined` and is used for side effects (like logging, or mutating external variables). `map` is chainable; `forEach` is not.

### Topic 2: Difference between `Object.freeze` and `Object.seal`?
**Answer:**
- `Object.freeze()`: Ultimate immutable. Cannot add, delete, or modify existing properties.
- `Object.seal()`: Cannot add or delete properties. **CAN modify** values of existing properties.

### Topic 3: What are the benefits of using `map` (data structure) over Objects?
**Answer:**
- Keys can be any type (functions, objects), not just strings/symbols.
- Maintains insertion order guaranteed.
- `size` property exists (Objects need `Object.keys(obj).length`).
- Iteratable directly (`for..of`).

### Topic 4: Explain the difference between `[...]` (Spread) and `Object.assign`?
**Answer:** Both create shallow copies. Spread syntax is cleaner and newer. One subtle difference: `currentObject` properties are defined on the new object. `Object.assign` triggers setters on the target object, while spread defines new properties. For simple data cloning, they are effectively the same.

### Topic 5: Can arrow functions be used as methods in a Class?
**Answer:** Yes, usually as class fields.
```javascript
class A {
    value = 10;
    print = () => console.log(this.value);
}
```
This is useful for event handlers (React) because arrow functions lexically bind `this`, meaning you don't need `.bind(this)` in the constructor. However, the method is created **per instance**, not on the prototype chain, using slightly more memory.
