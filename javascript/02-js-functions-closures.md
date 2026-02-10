# JavaScript Functions & Closures - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Functions, Arrow Functions, Closures, this keyword, call/apply/bind, IIFE, Currying, and tricky technical scenarios.

--

## Table of Contents

1. [Function Basics](#function-basics)
2. [Arrow Functions](#arrow-functions)
3. [The 'this' Keyword](#the-this-keyword)
4. [call, apply, bind](#call-apply-bind)
5. [Closures](#closures)
6. [IIFE (Immediately Invoked Function Expression)](#iife-immediately-invoked-function-expression)
7. [Higher-Order Functions](#higher-order-functions)
8. [Currying](#currying)
9. [Function Composition](#function-composition)
10. [Memoization](#memoization)
11. [Complex Technical Scenarios](#complex-technical-scenarios)
12. [Coding Problems](#coding-problems)
13. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. Function Basics

### Ways to Define Functions

```javascript
// 1. Function Declaration (hoisted)
function greet(name) {
    return `Hello, ${name}!`;
}

// 2. Function Expression (not hoisted)
const greet2 = function(name) {
    return `Hello, ${name}!`;
};

// 3. Named Function Expression
const greet3 = function greeting(name) {
    return `Hello, ${name}!`;
}; // 'greeting' only accessible inside the function

// 4. Arrow Function (ES6)
const greet4 = (name) => `Hello, ${name}!`;

// 5. Function Constructor (avoid)
const greet5 = new Function('name', 'return "Hello, " + name');

// 6. Method (inside object)
const obj = {
    greet(name) {
        return `Hello, ${name}!`;
    }
};
```

### Parameters and Arguments

```javascript
// Default parameters (ES6)
function greet(name = "Guest", greeting = "Hello") {
    return `${greeting}, ${name}!`;
}
console.log(greet());             // "Hello, Guest!"
console.log(greet("Rahul"));      // "Hello, Rahul!"
console.log(greet("Rahul", "Hi")); // "Hi, Rahul!"

// Rest parameters (must be last)
function sum(...numbers) {
    return numbers.reduce((acc, n) => acc + n, 0);
}
console.log(sum(1, 2, 3, 4, 5));  // 15

// Arguments object (not in arrow functions)
function showArgs() {
    console.log(arguments);        // array-like object
    console.log([...arguments]);   // convert to real array
}
showArgs(1, 2, 3);                // Arguments(3) [1, 2, 3]

// Destructured parameters
function displayUser({ name, age, city = "Unknown" }) {
    console.log(`${name}, ${age}, ${city}`);
}
displayUser({ name: "Rahul", age: 25 }); // "Rahul, 25, Unknown"

// Parameter order with destructuring
function createUser({ name, age } = {}) {
    return { name: name || "Guest", age: age || 0 };
}
```

### Return Values

```javascript
// Single value
function add(a, b) {
    return a + b;
}

// Multiple values (using object or array)
function getMinMax(arr) {
    return {
        min: Math.min(...arr),
        max: Math.max(...arr)
    };
}
const { min, max } = getMinMax([3, 1, 4, 1, 5]);

// Without return - returns undefined
function noReturn() {
    console.log("Nothing returned");
}
console.log(noReturn()); // undefined

// Early return pattern
function divide(a, b) {
    if (b === 0) return Infinity;
    return a / b;
}
```

### Pure vs Impure Functions

```javascript
// Pure function - same input always gives same output, no side effects
function pureFn(a, b) {
    return a + b;
}

// Impure function - has side effects or uses external state
let counter = 0;
function impureFn(a) {
    counter++;              // Side effect: modifying external state
    console.log(a);         // Side effect: I/O operation
    return a + counter;     // Depends on external state
}

// Impure - mutates input
function impureMutate(arr) {
    arr.push(4);           // Mutates the original array
    return arr;
}

// Pure version
function pureAppend(arr, item) {
    return [...arr, item]; // Returns new array
}
```

--

## 2. Arrow Functions

### Syntax Variations

```javascript
// Multiple parameters
const add = (a, b) => a + b;

// Single parameter (parentheses optional)
const square = x => x * x;

// No parameters
const greet = () => "Hello!";

// Multi-line body (need braces and return)
const process = (a, b) => {
    const sum = a + b;
    const product = a * b;
    return { sum, product };
};

// Returning object literal (wrap in parentheses)
const makeObj = (name, age) => ({ name, age });

// With destructuring
const getFullName = ({ firstName, lastName }) => `${firstName} ${lastName}`;
```

### Arrow Functions vs Regular Functions

| Feature | Regular Function | Arrow Function |
|-----|---------|--------|
| **'this' binding** | Dynamic (caller determines) | Lexical (inherits from scope) |
| **arguments object** | ✅ Available | ❌ Not available |
| **Can be constructor** | ✅ Yes (with new) | ❌ No |
| **prototype property** | ✅ Has | ❌ Doesn't have |
| **Can use yield** | ✅ Generator functions | ❌ Cannot |
| **Method syntax** | Works as expected | ❌ 'this' issues |
| **Hoisting** | Declarations hoisted | Not hoisted |

### When NOT to Use Arrow Functions

```javascript
// 1. Object methods - 'this' won't work as expected
const obj = {
    name: "Rahul",
    // WRONG
    greet: () => {
        console.log(this.name);  // undefined (this is global/undefined)
    },
    // CORRECT
    greet2() {
        console.log(this.name);  // "Rahul"
    }
};

// 2. Prototype methods
function Person(name) {
    this.name = name;
}
// WRONG
Person.prototype.greet = () => {
    console.log(this.name);      // undefined
};
// CORRECT
Person.prototype.greet = function() {
    console.log(this.name);
};

// 3. Event handlers (if you need 'this' to be the element)
button.addEventListener('click', () => {
    console.log(this);           // Window/undefined, not the button
});
button.addEventListener('click', function() {
    console.log(this);           // The button element
});

// 4. Constructor functions
const Car = (make) => {
    this.make = make;
};
// new Car("Toyota"); // Error: Car is not a constructor
```

--

## 3. The 'this' Keyword

The value of `this` depends on HOW the function is called, not where it's defined.

### 'this' in Different Contexts

```javascript
// 1. Global context
console.log(this);               // Window (browser) or {} (Node.js)

// 2. Regular function (non-strict mode)
function showThis() {
    console.log(this);
}
showThis();                       // Window (or global object)

// 3. Regular function (strict mode)
"use strict";
function showThisStrict() {
    console.log(this);
}
showThisStrict();                 // undefined

// 4. Object method
const obj = {
    name: "Rahul",
    greet() {
        console.log(this.name);   // "Rahul"
    }
};
obj.greet();

// 5. Method assigned to variable (loses 'this')
const greet = obj.greet;
greet();                          // undefined (this is now global)

// 6. Arrow function inside method
const obj2 = {
    name: "Rahul",
    greet() {
        const arrow = () => {
            console.log(this.name); // "Rahul" (inherits from greet's this)
        };
        arrow();
    }
};

// 7. Constructor function
function Person(name) {
    this.name = name;
    console.log(this);            // Person { name: "Rahul" }
}
new Person("Rahul");

// 8. Event handler
button.addEventListener('click', function() {
    console.log(this);            // The button element
});
```

### 'this' with Nested Functions

```javascript
const obj = {
    name: "Rahul",
    hobbies: ["reading", "gaming", "coding"],
    
    showHobbies() {
        // PROBLEM: 'this' in inner function is undefined/window
        this.hobbies.forEach(function(hobby) {
            console.log(`${this.name} likes ${hobby}`);
            // undefined likes reading (WRONG!)
        });
        
        // SOLUTION 1: Arrow function (inherits 'this')
        this.hobbies.forEach(hobby => {
            console.log(`${this.name} likes ${hobby}`);
            // Rahul likes reading (CORRECT!)
        });
        
        // SOLUTION 2: Save 'this' to variable
        const self = this;
        this.hobbies.forEach(function(hobby) {
            console.log(`${self.name} likes ${hobby}`);
        });
        
        // SOLUTION 3: Use bind
        this.hobbies.forEach(function(hobby) {
            console.log(`${this.name} likes ${hobby}`);
        }.bind(this));
    }
};
```

--

## 4. call, apply, bind

Methods to explicitly set the `this` value.

### call()

```javascript
// call(thisArg, arg1, arg2, ...)
function greet(greeting, punctuation) {
    console.log(`${greeting}, ${this.name}${punctuation}`);
}

const person = { name: "Rahul" };

greet.call(person, "Hello", "!");  // "Hello, Rahul!"

// Use case: borrowing methods
const arrayLike = { 0: "a", 1: "b", length: 2 };
const arr = Array.prototype.slice.call(arrayLike);
console.log(arr);                   // ["a", "b"]

// Check type
function type(obj) {
    return Object.prototype.toString.call(obj);
}
console.log(type([]));             // "[object Array]"
console.log(type(null));           // "[object Null]"
```

### apply()

```javascript
// apply(thisArg, [argsArray])
function greet(greeting, punctuation) {
    console.log(`${greeting}, ${this.name}${punctuation}`);
}

const person = { name: "Rahul" };

greet.apply(person, ["Hello", "!"]);  // "Hello, Rahul!"

// Use case: passing array to function expecting arguments
const numbers = [5, 6, 2, 3, 7];
console.log(Math.max.apply(null, numbers));  // 7
// Modern alternative:
console.log(Math.max(...numbers));           // 7

// Merging arrays (before spread operator)
const arr1 = [1, 2, 3];
const arr2 = [4, 5, 6];
Array.prototype.push.apply(arr1, arr2);
console.log(arr1);                           // [1, 2, 3, 4, 5, 6]
```

### bind()

```javascript
// bind(thisArg, arg1, arg2, ...) - returns NEW function
function greet(greeting) {
    console.log(`${greeting}, ${this.name}!`);
}

const person = { name: "Rahul" };

const boundGreet = greet.bind(person);
boundGreet("Hello");                  // "Hello, Rahul!"

// Partial application with bind
function add(a, b, c) {
    return a + b + c;
}
const add5 = add.bind(null, 5);       // 'a' is now fixed to 5
console.log(add5(10, 15));            // 30 (5 + 10 + 15)

const add5and10 = add.bind(null, 5, 10);
console.log(add5and10(15));           // 30

// Common use case: preserving 'this' in callbacks
class Timer {
    constructor() {
        this.seconds = 0;
    }
    
    start() {
        // WRONG: 'this' will be undefined in callback
        // setInterval(function() { this.seconds++; }, 1000);
        
        // CORRECT: bind 'this'
        setInterval(function() {
            this.seconds++;
            console.log(this.seconds);
        }.bind(this), 1000);
        
        // OR use arrow function
        setInterval(() => {
            this.seconds++;
            console.log(this.seconds);
        }, 1000);
    }
}
```

### Comparison

| Method | Syntax | Returns | Executes Immediately |
|----|----|-----|-----------|
| `call` | `fn.call(this, a, b)` | Function result | ✅ Yes |
| `apply` | `fn.apply(this, [a, b])` | Function result | ✅ Yes |
| `bind` | `fn.bind(this, a, b)` | New function | ❌ No |

--

## 5. Closures

A closure is a function that has access to variables from its outer (enclosing) scope, even after the outer function has returned.

### How Closures Work

```javascript
function outer() {
    let count = 0;  // This variable is "closed over"
    
    function inner() {
        count++;
        console.log(count);
    }
    
    return inner;
}

const counter = outer();
counter();  // 1
counter();  // 2
counter();  // 3

// 'count' is not accessible directly
// console.log(count); // ReferenceError
// But 'inner' maintains access to it through closure
```

### Practical Closure Examples

```javascript
// 1. Private variables (data encapsulation)
function createCounter() {
    let count = 0;  // Private variable
    
    return {
        increment() { return ++count; },
        decrement() { return -count; },
        getCount() { return count; }
    };
}

const counter = createCounter();
console.log(counter.increment()); // 1
console.log(counter.increment()); // 2
console.log(counter.decrement()); // 1
console.log(counter.getCount());  // 1
// counter.count is undefined - truly private!

// 2. Function factory
function createMultiplier(multiplier) {
    return function(number) {
        return number * multiplier;
    };
}

const double = createMultiplier(2);
const triple = createMultiplier(3);

console.log(double(5));  // 10
console.log(triple(5));  // 15

// 3. Remembering configuration
function createLogger(prefix) {
    return function(message) {
        console.log(`[${prefix}] ${message}`);
    };
}

const infoLogger = createLogger("INFO");
const errorLogger = createLogger("ERROR");

infoLogger("User logged in");   // [INFO] User logged in
errorLogger("Database error");  // [ERROR] Database error

// 4. Event handlers with data
function setupButton(buttonId, message) {
    const button = document.getElementById(buttonId);
    button.addEventListener('click', function() {
        alert(message);  // Closure over 'message'
    });
}

setupButton("btn1", "First button clicked!");
setupButton("btn2", "Second button clicked!");

// 5. setTimeout with closure
for (var i = 0; i < 3; i++) {
    // Using closure with IIFE to capture i
    (function(j) {
        setTimeout(function() {
            console.log(j);
        }, j * 1000);
    })(i);
}
// Output: 0, 1, 2 (each second)
```

### Common Closure Pitfall

```javascript
// PROBLEM: All callbacks share the same 'i'
function createButtons() {
    const buttons = [];
    
    for (var i = 0; i < 5; i++) {
        buttons.push(function() {
            console.log(i);
        });
    }
    
    return buttons;
}

const buttons = createButtons();
buttons[0]();  // 5 (not 0!)
buttons[1]();  // 5 (not 1!)
buttons[2]();  // 5 (not 2!)

// SOLUTION 1: Use let (block scope)
function createButtonsFixed() {
    const buttons = [];
    
    for (let i = 0; i < 5; i++) {
        buttons.push(function() {
            console.log(i);
        });
    }
    
    return buttons;
}

// SOLUTION 2: Use IIFE
function createButtonsFixed2() {
    const buttons = [];
    
    for (var i = 0; i < 5; i++) {
        buttons.push((function(j) {
            return function() {
                console.log(j);
            };
        })(i));
    }
    
    return buttons;
}
```

--

## 6. IIFE (Immediately Invoked Function Expression)

A function that executes immediately upon definition.

### Syntax Variations

```javascript
// Standard IIFE
(function() {
    console.log("I run immediately!");
})();

// Arrow function IIFE
(() => {
    console.log("Arrow IIFE!");
})();

// With parameters
(function(name) {
    console.log(`Hello, ${name}!`);
})("Rahul");

// Named IIFE (for recursion/debugging)
(function factorial(n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
})(5);  // 120

// Alternative syntax (less common)
!function() { console.log("Also works!"); }();
+function() { console.log("And this!"); }();
void function() { console.log("This too!"); }();
```

### Use Cases

```javascript
// 1. Avoid polluting global scope
(function() {
    var privateVar = "I am private";
    // ... code that uses privateVar
})();
// privateVar is not accessible outside

// 2. Create private scope in loops
for (var i = 0; i < 3; i++) {
    (function(index) {
        setTimeout(function() {
            console.log(index);
        }, 100);
    })(i);
}
// Output: 0, 1, 2

// 3. Module pattern (before ES6 modules)
const calculator = (function() {
    // Private
    let result = 0;
    
    // Public API
    return {
        add(x) { result += x; return this; },
        subtract(x) { result -= x; return this; },
        getResult() { return result; },
        reset() { result = 0; return this; }
    };
})();

calculator.add(10).subtract(5).add(3);
console.log(calculator.getResult());  // 8

// 4. Initialize immediately with configuration
const config = (function() {
    const env = process.env.NODE_ENV || 'development';
    
    return {
        apiUrl: env === 'production' 
            ? 'https://api.example.com' 
            : 'http://localhost:3000',
        debug: env !== 'production'
    };
})();
```

--

## 7. Higher-Order Functions

Functions that take functions as arguments or return functions.

### Functions as Arguments

```javascript
// map, filter, reduce are higher-order functions
const numbers = [1, 2, 3, 4, 5];

// map - transform each element
const doubled = numbers.map(n => n * 2);
console.log(doubled);  // [2, 4, 6, 8, 10]

// filter - select elements
const evens = numbers.filter(n => n % 2 === 0);
console.log(evens);    // [2, 4]

// reduce - accumulate to single value
const sum = numbers.reduce((acc, n) => acc + n, 0);
console.log(sum);      // 15

// Custom higher-order function
function repeat(fn, times) {
    for (let i = 0; i < times; i++) {
        fn(i);
    }
}

repeat(i => console.log(`Iteration ${i}`), 3);
```

### Functions that Return Functions

```javascript
// Function factory
function createValidator(minLength) {
    return function(value) {
        return value.length >= minLength;
    };
}

const isValidPassword = createValidator(8);
const isValidUsername = createValidator(3);

console.log(isValidPassword("12345"));     // false
console.log(isValidPassword("12345678"));  // true
console.log(isValidUsername("ab"));        // false
console.log(isValidUsername("abc"));       // true

// Rate limiter
function createRateLimiter(limit, interval) {
    let count = 0;
    
    setInterval(() => { count = 0; }, interval);
    
    return function(callback) {
        if (count < limit) {
            count++;
            callback();
            return true;
        }
        return false;
    };
}

const limiter = createRateLimiter(5, 1000);
// Can only call 5 times per second
```

### Implementing Common Array Methods

```javascript
// Custom map
function myMap(arr, callback) {
    const result = [];
    for (let i = 0; i < arr.length; i++) {
        result.push(callback(arr[i], i, arr));
    }
    return result;
}

// Custom filter
function myFilter(arr, callback) {
    const result = [];
    for (let i = 0; i < arr.length; i++) {
        if (callback(arr[i], i, arr)) {
            result.push(arr[i]);
        }
    }
    return result;
}

// Custom reduce
function myReduce(arr, callback, initialValue) {
    let accumulator = initialValue;
    let startIndex = 0;
    
    if (initialValue === undefined) {
        accumulator = arr[0];
        startIndex = 1;
    }
    
    for (let i = startIndex; i < arr.length; i++) {
        accumulator = callback(accumulator, arr[i], i, arr);
    }
    
    return accumulator;
}

// Custom forEach
function myForEach(arr, callback) {
    for (let i = 0; i < arr.length; i++) {
        callback(arr[i], i, arr);
    }
}
```

--

## 8. Currying

Transforming a function with multiple arguments into a sequence of functions, each taking a single argument.

```javascript
// Normal function
function add(a, b, c) {
    return a + b + c;
}
console.log(add(1, 2, 3));  // 6

// Curried function
function curriedAdd(a) {
    return function(b) {
        return function(c) {
            return a + b + c;
        };
    };
}
console.log(curriedAdd(1)(2)(3));  // 6

// Arrow function syntax
const curriedAddArrow = a => b => c => a + b + c;
console.log(curriedAddArrow(1)(2)(3));  // 6

// Partial application
const add1 = curriedAdd(1);
const add1and2 = add1(2);
console.log(add1and2(3));  // 6
console.log(add1and2(10)); // 13
```

### Generic Curry Function

```javascript
// Utility to curry any function
function curry(fn) {
    return function curried(...args) {
        if (args.length >= fn.length) {
            return fn.apply(this, args);
        }
        return function(...moreArgs) {
            return curried.apply(this, [...args, ...moreArgs]);
        };
    };
}

// Usage
function multiply(a, b, c) {
    return a * b * c;
}

const curriedMultiply = curry(multiply);

console.log(curriedMultiply(2)(3)(4));     // 24
console.log(curriedMultiply(2, 3)(4));     // 24
console.log(curriedMultiply(2)(3, 4));     // 24
console.log(curriedMultiply(2, 3, 4));     // 24

// Practical example
const formatDate = curry(function(format, separator, date) {
    const d = new Date(date);
    const parts = {
        'YYYY': d.getFullYear(),
        'MM': String(d.getMonth() + 1).padStart(2, '0'),
        'DD': String(d.getDate()).padStart(2, '0')
    };
    return format.split(separator).map(f => parts[f]).join(separator);
});

const formatUSDate = formatDate('MM', '/');
const formatEUDate = formatDate('DD', '/');

console.log(formatUSDate('2024-01-15'));  // 01/15/2024
console.log(formatEUDate('2024-01-15'));  // 15/01/2024
```

--

## 9. Function Composition

Combining multiple functions to create a new function.

```javascript
// Manual composition
const addOne = x => x + 1;
const double = x => x * 2;
const square = x => x * x;

// Compose manually
const result = square(double(addOne(2)));  // square(double(3)) = square(6) = 36

// Compose utility (right to left)
const compose = (...fns) => x => fns.reduceRight((acc, fn) => fn(acc), x);

const composedFn = compose(square, double, addOne);
console.log(composedFn(2));  // 36

// Pipe utility (left to right)
const pipe = (...fns) => x => fns.reduce((acc, fn) => fn(acc), x);

const pipedFn = pipe(addOne, double, square);
console.log(pipedFn(2));  // 36

// Practical example
const sanitize = str => str.trim().toLowerCase();
const capitalize = str => str.charAt(0).toUpperCase() + str.slice(1);
const addGreeting = str => `Hello, ${str}!`;

const processName = pipe(sanitize, capitalize, addGreeting);
console.log(processName("  RAHUL  "));  // "Hello, Rahul!"
```

--

## 10. Memoization

Caching function results to avoid repeated calculations.

```javascript
// Basic memoize
function memoize(fn) {
    const cache = {};
    
    return function(...args) {
        const key = JSON.stringify(args);
        
        if (cache[key]) {
            console.log('From cache');
            return cache[key];
        }
        
        console.log('Computing');
        const result = fn.apply(this, args);
        cache[key] = result;
        return result;
    };
}

// Example: Expensive calculation
function slowFibonacci(n) {
    if (n <= 1) return n;
    return slowFibonacci(n - 1) + slowFibonacci(n - 2);
}

const fastFibonacci = memoize(function fib(n) {
    if (n <= 1) return n;
    return fastFibonacci(n - 1) + fastFibonacci(n - 2);
});

console.time('slow');
console.log(slowFibonacci(35));  // Very slow
console.timeEnd('slow');

console.time('fast');
console.log(fastFibonacci(35));  // Much faster
console.timeEnd('fast');

// Memoize with cache limit (LRU-like)
function memoizeWithLimit(fn, limit = 100) {
    const cache = new Map();
    
    return function(...args) {
        const key = JSON.stringify(args);
        
        if (cache.has(key)) {
            // Move to end (most recently used)
            const value = cache.get(key);
            cache.delete(key);
            cache.set(key, value);
            return value;
        }
        
        const result = fn.apply(this, args);
        
        if (cache.size >= limit) {
            // Remove oldest (first key)
            const firstKey = cache.keys().next().value;
            cache.delete(firstKey);
        }
        
        cache.set(key, result);
        return result;
    };
}
```

--

## 11. Complex Technical Scenarios

### Scenario 1: What will be the output?

```javascript
function outer() {
    var x = 10;
    
    function inner() {
        console.log(x);
    }
    
    x = 20;
    return inner;
}

const fn = outer();
fn();

// Answer: 20
// Closures capture variables by reference, not by value
// When inner() is called, x is 20
```

### Scenario 2: What will be the output?

```javascript
for (var i = 0; i < 3; i++) {
    setTimeout(function() {
        console.log(i);
    }, 1000);
}

// Answer: 3, 3, 3
// All callbacks share the same 'i', which is 3 after loop ends
```

### Scenario 3: What will be the output?

```javascript
const obj = {
    name: "Rahul",
    greet: function() {
        console.log(this.name);
    },
    greet2: () => {
        console.log(this.name);
    }
};

obj.greet();
obj.greet2();

// Answer:
// "Rahul" - regular function, this = obj
// undefined - arrow function, this = window/global
```

### Scenario 4: What will be the output?

```javascript
function foo() {
    console.log(this.a);
}

const obj = { a: 42 };
const bar = foo.bind(obj);
const baz = bar.bind({ a: 100 });

bar();
baz();

// Answer:
// 42
// 42
// bind can only bind 'this' once. Subsequent binds are ignored.
```

### Scenario 5: What will be the output?

```javascript
function createFunctions() {
    var result = [];
    
    for (var i = 0; i < 3; i++) {
        result.push(function() { return i; });
    }
    
    return result;
}

var functions = createFunctions();
console.log(functions[0]());
console.log(functions[1]());
console.log(functions[2]());

// Answer: 3, 3, 3
// All functions share the same 'i' (closure over variable, not value)
```

### Scenario 6: What will be the output?

```javascript
const fn = (function() {
    let count = 0;
    return function() {
        return ++count;
    };
})();

console.log(fn());
console.log(fn());
console.log(fn());

// Answer: 1, 2, 3
// IIFE creates closure over 'count', maintains state between calls
```

### Scenario 7: Implement once()

```javascript
function once(fn) {
    let called = false;
    let result;
    
    return function(...args) {
        if (!called) {
            called = true;
            result = fn.apply(this, args);
        }
        return result;
    };
}

const initialize = once(() => {
    console.log("Initializing...");
    return "Initialized!";
});

console.log(initialize());  // Initializing... → "Initialized!"
console.log(initialize());  // "Initialized!" (no log)
console.log(initialize());  // "Initialized!" (no log)
```

--

## 12. Coding Problems

### Problem 1: Implement debounce

```javascript
function debounce(fn, delay) {
    let timeoutId;
    
    return function(...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            fn.apply(this, args);
        }, delay);
    };
}

// Test
const debouncedLog = debounce(console.log, 300);
debouncedLog("First");
debouncedLog("Second");
debouncedLog("Third");  // Only this logs after 300ms
```

### Problem 2: Implement throttle

```javascript
function throttle(fn, limit) {
    let inThrottle = false;
    
    return function(...args) {
        if (!inThrottle) {
            fn.apply(this, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

// Test
const throttledLog = throttle(console.log, 1000);
throttledLog("A");  // Logs
throttledLog("B");  // Ignored
throttledLog("C");  // Ignored
// After 1 second...
throttledLog("D");  // Logs
```

### Problem 3: Implement Function.prototype.bind

```javascript
Function.prototype.myBind = function(context, ...args) {
    const fn = this;
    
    return function(...moreArgs) {
        return fn.apply(context, [...args, ...moreArgs]);
    };
};

// Test
function greet(greeting, punctuation) {
    return `${greeting}, ${this.name}${punctuation}`;
}

const person = { name: "Rahul" };
const boundGreet = greet.myBind(person, "Hello");
console.log(boundGreet("!"));  // "Hello, Rahul!"
```

### Problem 4: Implement pipe and compose

```javascript
// pipe: left to right
const pipe = (...fns) => x => fns.reduce((acc, fn) => fn(acc), x);

// compose: right to left
const compose = (...fns) => x => fns.reduceRight((acc, fn) => fn(acc), x);

// Test
const add1 = x => x + 1;
const multiply2 = x => x * 2;
const subtract3 = x => x - 3;

const piped = pipe(add1, multiply2, subtract3);
console.log(piped(5));  // ((5 + 1) * 2) - 3 = 9

const composed = compose(subtract3, multiply2, add1);
console.log(composed(5));  // ((5 + 1) * 2) - 3 = 9
```

### Problem 5: Create a function that adds all numbers

```javascript
// add(1)(2)(3)() = 6
// add(1,2)(3,4)() = 10
function add(...args) {
    let sum = args.reduce((a, b) => a + b, 0);
    
    function innerAdd(...moreArgs) {
        if (moreArgs.length === 0) {
            return sum;
        }
        sum += moreArgs.reduce((a, b) => a + b, 0);
        return innerAdd;
    }
    
    return innerAdd;
}

console.log(add(1)(2)(3)());         // 6
console.log(add(1, 2)(3, 4)());      // 10
console.log(add(1)(2)(3)(4)(5)());   // 15
```

--

## 13. Key Topics & Explanations

### Topic 1: What is a closure in JavaScript?

**Answer:** A closure is a function that remembers and has access to variables from its outer (enclosing) scope, even after the outer function has returned. Every function in JavaScript creates a closure. Closures are used for data privacy, function factories, maintaining state, and callbacks. The closed-over variables are captured by reference, not value.

--

### Topic 2: What is the difference between call, apply, and bind?

**Answer:** All three explicitly set the `this` value. `call(thisArg, arg1, arg2)` invokes the function immediately with individual arguments. `apply(thisArg, [args])` invokes immediately with arguments as an array. `bind(thisArg, arg1, arg2)` returns a NEW function with `this` permanently bound, without executing it. bind also supports partial application.

--

### Topic 3: What is the difference between arrow functions and regular functions?

**Answer:** Arrow functions: (1) Don't have their own `this` - inherit from enclosing scope (2) Don't have `arguments` object (3) Cannot be used as constructors (no `new`) (4) Don't have `prototype` property (5) Cannot be generators. Use arrow functions for callbacks and when you want lexical `this`. Use regular functions for methods and constructors.

--

### Topic 4: How does 'this' work in JavaScript?

**Answer:** `this` is determined by how a function is called: (1) Global context: window/global (2) Object method: the object (3) call/apply/bind: explicitly specified (4) Constructor (new): new object being created (5) Arrow function: inherited from enclosing scope (6) Event handler: the element. In strict mode, standalone function `this` is undefined instead of global.

--

### Topic 5: What is currying?

**Answer:** Currying transforms a function with multiple arguments into a sequence of functions, each taking a single argument. Example: `add(a, b, c)` becomes `curriedAdd(a)(b)(c)`. Benefits: (1) Partial application - create specialized functions (2) Function composition (3) Point-free programming. Common in functional programming for creating reusable function variants.

--

### Topic 6: What is an IIFE and why use it?

**Answer:** IIFE (Immediately Invoked Function Expression) is a function that executes immediately upon definition: `(function(){})()`. Benefits: (1) Creates private scope - avoids polluting global namespace (2) Module pattern - data encapsulation (3) Capture loop variables (4) Initialize immediately with configuration. Less common with ES6 modules but still useful.

--

### Topic 7: What is the difference between debounce and throttle?

**Answer:** Both limit function execution rate. **Debounce** waits for a pause in events before executing (e.g., wait 300ms after user stops typing). **Throttle** ensures function executes at most once per time period (e.g., once every 100ms during scroll). Debounce is for events that "burst" (input), throttle is for continuous events (scroll, resize).

--

### Topic 8: How do closures enable private variables?

**Answer:** Closures allow inner functions to access outer scope variables that aren't directly accessible from outside. Example: `function counter() { let count = 0; return { increment() { return ++count; } }; }`. The `count` variable is only accessible through the returned methods, not directly. This is JavaScript's way of implementing data privacy.

--

### Topic 9: Why do all callbacks in a loop log the same value?

**Answer:** When using `var` in a loop with async callbacks, all callbacks share the same variable (function-scoped). By the time callbacks execute, the loop has completed and the variable has its final value. Solutions: (1) Use `let` (block-scoped, creates new binding each iteration) (2) Use IIFE to capture current value (3) Use closure with forEach/map.

--

### Topic 10: What is a higher-order function?

**Answer:** A function that either: (1) Takes one or more functions as arguments, or (2) Returns a function as its result. Examples: `map`, `filter`, `reduce` take callback functions; `bind` returns a new function; function factories return functions. HOFs enable functional programming patterns like composition, currying, and abstraction.

--

## Quick Reference Cheat Sheet

```
Function Types:
├── Declaration: function foo() {} - hoisted
├── Expression: const foo = function() {} - not hoisted
├── Arrow: const foo = () => {} - lexical this
└── Method: obj.foo() - this = obj

'this' Binding (priority order):
1. new keyword → new object
2. call/apply/bind → specified object
3. Object method → the object
4. Standalone → window/undefined
5. Arrow function → enclosing scope's this

call vs apply vs bind:
├── call: fn.call(this, arg1, arg2) - immediate
├── apply: fn.apply(this, [args]) - immediate
└── bind: fn.bind(this, arg1) → new function

Closure:
├── Function + its lexical environment
├── Variables captured by reference
├── Enables private variables
└── Used in: callbacks, factories, modules

IIFE: (function() {})()
├── Creates private scope
├── Module pattern
└── Capture loop variables

Higher-Order Functions:
├── Accept functions as arguments
├── Return functions
└── Examples: map, filter, reduce, bind

Currying: fn(a, b, c) → fn(a)(b)(c)
├── Partial application
├── Reusable specialized functions
└── Function composition

Common Patterns:
├── debounce(fn, delay) - wait for pause
├── throttle(fn, limit) - rate limit
├── memoize(fn) - cache results
├── once(fn) - run only once
└── compose/pipe(fns) - chain functions
```

--

> **Tip:** Closures and 'this' are **#1 most asked** function topics in WITCH technical reviews. Practice explaining closures with examples, understand why `var` in loops causes issues, master call/apply/bind differences, and know when arrow functions are appropriate. The for-loop-setTimeout scenario appears in almost every technical review!
