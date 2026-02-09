# JavaScript Technical Proficiency Bank - Ultimate Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Purpose:** A comprehensive repository of 50+ technical inquiries and scenarios covering Core JS, DOM, ES6+, and Async Programming.

---

## Table of Contents

1. [Core JavaScript Fundamentals](#1-core-javascript-fundamentals)
2. [Data Types & Coercion](#2-data-types--coercion)
3. [Functions & Scope](#3-functions--scope)
4. [Objects & Prototypes](#4-objects--prototypes)
5. [DOM & Browser APIs](#5-dom--browser-apis)
6. [Asynchronous JavaScript](#6-asynchronous-javascript)
7. [ES6+ Features](#7-es6-features)
8. [Design Patterns & Algorithms](#8-design-patterns--algorithms)

---

## 1. Core JavaScript Fundamentals

### Inquiry 1: Hoisting
**Scenario:** Predict Output.
```javascript
x = 5;
console.log(x);
var x;
```
**Explanation:** `5`. Variables declared with `var` are hoisted to the top of the function/global scope. `x = 5` assigns before `console.log`.

### Inquiry 2: `let` vs `var`
**Scenario:** Loop Scope.
```javascript
for(var i=0; i<3; i++) setTimeout(()=>console.log(i), 100);
```
**Explanation:** Prints `3, 3, 3`. `var` is function scoped, so `i` is shared.
**Fix:** Use `let i=0`. `let` is block scoped, creating a new `i` for each iteration.

### Inquiry 3: `const` Usage
**Scenario:** Can we modify a `const` object?
**Explanation:** Yes. `const` prevents reassignment of the variable identifier, but does not make the object immutable. `obj.prop = 5` is allowed. `obj = {}` is not.

### Inquiry 4: Temporal Dead Zone (TDZ)
**Scenario:** Accessing `let` before declaration.
**Explanation:** Throws `ReferenceError`. The variable exists in scope but is uninitialized until the line of declaration is executed.

### Inquiry 5: Strict Mode
**Scenario:** Benefits of `"use strict"`?
**Explanation:** Prevents accidental globals. Disallows duplicate parameter names. Throws errors on silent failures (assigning to read-only property). `this` is `undefined` in global functions.

---

## 2. Data Types & Coercion

### Inquiry 6: `null` vs `undefined`
**Scenario:** Difference?
**Explanation:**
*   `undefined`: Variable declared but not assigned. Default value for function return.
*   `null`: Intentional absence of value. Must be assigned. Type is `object` (legacy bug).

### Inquiry 7: `NaN`
**Scenario:** Is `NaN === NaN`?
**Explanation:** No. `NaN` is the only value in JS not equal to itself. Use `isNaN()` or `Number.isNaN()`.

### Inquiry 8: Implicit Coercion
**Scenario:** `[] + []`?
**Explanation:** `""`. Arrays convert to empty strings. `"" + ""` is `""`.

### Inquiry 9: Truthy vs Falsy
**Scenario:** List all falsy values.
**Explanation:** `false`, `0`, `""` (empty string), `null`, `undefined`, `NaN`. Everything else is truthy (including `[]`, `{}`).

### Inquiry 10: `typeof` Operator
**Scenario:** `typeof null`? `typeof array`?
**Explanation:** Both return `"object"`. Use `Array.isArray()` to check arrays.

---

## 3. Functions & Scope

### Inquiry 11: Closures
**Scenario:** Explain with example.
**Explanation:** Function remembering lexical scope even when executed outside that scope. Used for data privacy, currying, memoization.

### Inquiry 12: `this` Keyword
**Scenario:** Arrow vs Regular Function.
**Explanation:**
*   **Regular**: `this` depends on invocation (`obj.method()` vs `func()`).
*   **Arrow**: `this` is lexically inherited from enclosing scope. Fixed at definition time.

### Inquiry 13: `bind`, `call`, `apply`
**Scenario:** Difference?
**Explanation:** Allow explicit setting of `this`.
*   `call(this, arg1, arg2)`: Invokes immediately.
*   `apply(this, [args])`: Invokes immediately. Array arguments.
*   `bind(this)`: Returns a **new function**. Does not invoke immediately.

### Inquiry 14: IIFE
**Scenario:** Purpose?
**Explanation:** Used to avoid polluting global namespace variables. `(function(){...})()`.

### Inquiry 15: Currying
**Scenario:** Transform `add(1,2)` to `add(1)(2)`.
**Explanation:** `const add = a => b => a + b;`.

### Inquiry 16: Higher Order Functions
**Scenario:** Definition?
**Explanation:** Function that takes a function as argument OR returns a function. Example: `map`, `filter`, `reduce`.

---

## 4. Objects & Prototypes

### Inquiry 17: Prototypal Inheritance
**Scenario:** How does lookup work?
**Explanation:** Checks own properties -> Checks `__proto__` -> Checks `__proto__.__proto__` -> ... -> `null`.

### Inquiry 18: `Object.create()`
**Scenario:** Why use it?
**Explanation:** Creates a new object with the specified prototype object and properties. Cleaner inheritance than `new Func()`.

### Inquiry 19: Shallow vs Deep Copy
**Scenario:** How to deep copy?
**Explanation:** `JSON.parse(JSON.stringify(obj))` (Basic). Recursion (Best). `structuredClone()` (Modern).

### Inquiry 20: Object Freeze vs Seal
**Scenario:** Difference?
**Explanation:**
*   `Freeze`: Immutable. Cannot add/remove/change properties.
*   `Seal`: Cannot add/remove. Can change existing values. Reference is still mutable.

---

## 5. DOM & Browser APIs

### Inquiry 21: Event Bubbling vs Capturing
**Scenario:** Stages?
**Explanation:**
1.  **Capturing**: Root -> Target.
2.  **Target**: At element.
3.  **Bubbling**: Target -> Root. (Default listeners use this). `event.stopPropagation()` stops it.

### Inquiry 22: Event Delegation
**Scenario:** Why use it?
**Explanation:** Attach 1 listener to parent instead of N to children. Uses bubbling. Memory efficient. Handles dynamic elements.

### Inquiry 23: `debouncing` vs `throttling`
**Scenario:** Use cases?
**Explanation:**
*   **Debounce**: Wait for "quiet period". Stops firing until N ms have passed since last call. (Search Input).
*   **Throttle**: Rate limit. Fire at most once every N ms. (Scroll event, Resize).

### Inquiry 24: `localStorage` vs `sessionStorage` vs Cookies
**Scenario:** Storage limits?
**Explanation:**
*   **Local**: ~5MB. Persistent.
*   **Session**: ~5MB. Tab session only.
*   **Cookie**: 4KB. Sent with every HTTP request.

### Inquiry 25: Critical Rendering Path
**Scenario:** Steps?
**Explanation:** HTML -> DOM, CSS -> CSSOM. Combine -> Render Tree -> Layout -> Paint -> Composite.

---

## 6. Asynchronous JavaScript

### Inquiry 26: Event Loop
**Scenario:** Microtasks vs Macrotasks priority?
**Explanation:**
*   **Microtasks** (`Promise`, `MutationObserver`) run **immediately** after current script, before rendering.
*   **Macrotasks** (`setTimeout`, `setInterval`) run next loop iteration.
*   **Answer**: Microtasks have higher priority.

### Inquiry 27: Callback Hell
**Scenario:** Solution?
**Explanation:** Promises or Async/Await (flattens nesting).

### Inquiry 28: `Promise.all` vs `Promise.race`
**Scenario:** Usage?
**Explanation:**
*   **All**: Waits for all to resolve. Fails if **one** fails.
*   **Race**: Settles as soon as **first** one settles (resolve or reject).
*   **AllSettled**: Waits for all (even if some fail). Returns array of outcomes.

### Inquiry 29: Async/Await Error Handling
**Scenario:** How to catch errors?
**Explanation:** Use `try...catch` block.

---

## 7. ES6+ Features

### Inquiry 30: Destructuring
**Scenario:** Swapping variables?
**Explanation:** `[a, b] = [b, a]`.

### Inquiry 31: Spread vs Rest Operator
**Scenario:** `...` usage?
**Explanation:**
*   **Spread** (Function call / Array literal): Expands array into elements. `Math.max(...arr)`.
*   **Rest** (Function definition): Collects arguments into array. `function f(...args)`.

### Inquiry 32: Modules (ESM)
**Scenario:** Export default vs named?
**Explanation:**
*   **Default**: One per module. `import X from ...`.
*   **Named**: Multiple per module. `import { A, B } from ...`.

### Inquiry 33: Generatros
**Scenario:** Function*?
**Explanation:** Function that can pause execution (`yield`) and resume (`next()`).

### Inquiry 34: `Map` vs Object
**Scenario:** Key differences?
**Explanation:**
*   **Map**: Keys can be **any type** (objects, functions). Preserves insertion order. Has `.size`. Iterable.
*   **Object**: Keys are strings/symbols. Unordered (historically). 

### Inquiry 35: `Set`
**Scenario:** Usage?
**Explanation:** Store unique values. `[...new Set(array)]` removes duplicates.

### Inquiry 36: Symbol
**Scenario:** Purpose?
**Explanation:** Unique identifier. Used for object properties to avoid name collisions (private-ish members).

---

## 8. Design Patterns & Algorithms

### Inquiry 37: Singleton Pattern
**Scenario:** Implementation?
**Explanation:** Use IIFE or Module to return single instance.

### Inquiry 38: Observer Pattern
**Scenario:** Real world example?
**Explanation:** DOM Event Listeners (`addEventListener`), Redux Store (`subscribe`).

### Inquiry 39: Flatten Array
**Scenario:** Deep flatten?
**Explanation:** Recursive function or `arr.flat(Infinity)`.

### Inquiry 40: Palindrome Check
**Scenario:** One liner?
**Explanation:** `str === str.split('').reverse().join('')`.

### Inquiry 41: Factorial (Recursion)
**Scenario:** Implementation?
**Explanation:** `f(n) => n <= 1 ? 1 : n * f(n-1)`.

### Inquiry 42: Fibonacci
**Scenario:** Iterative vs Recursive?
**Explanation:** Iterative is O(n). Recursive is O(2^n) (Bad). Memoization optimizes recursive to O(n).

### Inquiry 43: Binary Search
**Scenario:** Requirement?
**Explanation:** Array must be **Sorted**. O(log n).

### Inquiry 44: Big O Notation
**Scenario:** `forEach` complexity?
**Explanation:** O(n).

### Inquiry 45: Linked List Reversal
**Scenario:** Concept?
**Explanation:** Iterate with 3 pointers: prev, current, next. Flip `current.next` to `prev`. Move pointers forward.

### Inquiry 46: Anagram Check
**Scenario:** Approach?
**Explanation:** Sort both strings and compare OR Count char frequencies in Map.

### Inquiry 47: Valid Parentheses
**Scenario:** Data structure?
**Explanation:** Stack. Push open, Pop match on close.

### Inquiry 48: Deep Clone
**Scenario:** Manual implementation?
**Explanation:** Check if null/primitive -> Return. Create new Obj/Arr. Loop keys -> Recursive Call -> Assign.

### Inquiry 49: Memoization
**Scenario:** Implementation?
**Explanation:** Higher Order Function wrapping target function. Checks cache (Map) for arguments. Returns cache or executes and saves.

### Inquiry 50: Service Worker
**Scenario:** Purpose?
**Explanation:** Proxy script. Offline capability (PWA), Caching, Push Notifications.
