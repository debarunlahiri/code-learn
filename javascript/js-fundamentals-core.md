# JavaScript Fundamentals - Complete Guide (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Variables, Data Types, Operators, Type Coercion, Scope, Hoisting, and tricky technical scenarios.

--

## Table of Contents

1. [JavaScript Overview](#javascript-overview)
2. [Variables - var, let, const](#variables---var-let-const)
3. [Data Types](#data-types)
4. [Type Coercion & Conversion](#type-coercion-conversion)
5. [Operators](#operators)
6. [Truthy and Falsy Values](#truthy-and-falsy-values)
7. [Scope](#scope)
8. [Hoisting](#hoisting)
9. [Temporal Dead Zone (TDZ)](#temporal-dead-zone-tdz)
10. [Strict Mode](#strict-mode)
11. [Complex Technical Scenarios](#complex-technical-scenarios)
12. [Coding Problems](#coding-problems)
13. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. JavaScript Overview

JavaScript is a **single-threaded**, **interpreted**, **dynamically-typed** programming language that runs in browsers and Node.js.

### Key Characteristics

| Feature | Description |
|-----|-------|
| **Single-threaded** | Executes one operation at a time (uses Event Loop for async) |
| **Interpreted** | No compilation step (JIT compilation in modern engines) |
| **Dynamically-typed** | Variable types determined at runtime |
| **Prototype-based** | Inheritance through prototypes |
| **First-class functions** | Functions can be assigned to variables, passed as arguments |
| **Event-driven** | Responds to user interactions and I/O events |

### JavaScript Engines

| Browser | Engine |
|-----|----|
| Chrome | V8 |
| Firefox | SpiderMonkey |
| Safari | JavaScriptCore (Nitro) |
| Edge | V8 (Chromium-based) |
| Node.js | V8 |

--

## 2. Variables - var, let, const

### Declaration Methods

```javascript
// var - function scoped, can be redeclared, hoisted
var name = "Rahul";
var name = "Amit"; // OK - redeclaration allowed

// let - block scoped, cannot be redeclared, hoisted (TDZ)
let age = 25;
// let age = 30; // Error: Identifier 'age' has already been declared

// const - block scoped, cannot be reassigned, must be initialized
const PI = 3.14159;
// PI = 3.14; // Error: Assignment to constant variable
// const X; // Error: Missing initializer in const declaration
```

### var vs let vs const - Comparison

| Feature | var | let | const |
|-----|---|---|----|
| **Scope** | Function | Block | Block |
| **Redeclaration** | ✅ Allowed | ❌ Not allowed | ❌ Not allowed |
| **Reassignment** | ✅ Allowed | ✅ Allowed | ❌ Not allowed |
| **Hoisting** | ✅ (initialized as undefined) | ✅ (TDZ) | ✅ (TDZ) |
| **Global object property** | ✅ (window.x) | ❌ | ❌ |
| **Must initialize** | ❌ | ❌ | ✅ |

### Tricky Examples

```javascript
// Example 1: var in loop
for (var i = 0; i < 3; i++) {
    setTimeout(() => console.log(i), 100);
}
// Output: 3, 3, 3 (var is function scoped, same i shared)

// Fix with let
for (let j = 0; j < 3; j++) {
    setTimeout(() => console.log(j), 100);
}
// Output: 0, 1, 2 (let is block scoped, new j each iteration)

// Example 2: const with objects
const person = { name: "Rahul" };
person.name = "Amit";    // ✅ OK - modifying property
person.age = 25;         // ✅ OK - adding property
// person = {};          // ❌ Error - reassigning reference

const arr = [1, 2, 3];
arr.push(4);             // ✅ OK
arr[0] = 100;            // ✅ OK
// arr = [5, 6, 7];      // ❌ Error

// Example 3: var redeclaration and shadowing
var x = 10;
function test() {
    var x = 20;        // New variable, shadows outer x
    console.log(x);    // 20
}
test();
console.log(x);         // 10
```

### When to Use Which?

```javascript
// Use const by default
const API_URL = "https://api.example.com";
const user = { name: "Rahul", age: 25 };

// Use let when you need to reassign
let count = 0;
count++; // Need to update

for (let i = 0; i < 10; i++) { } // Loop counter

// Avoid var - legacy code only
```

--

## 3. Data Types

JavaScript has **8 data types**: 7 primitives + 1 non-primitive.

### Primitive Types (Immutable)

```javascript
// 1. Number - 64-bit floating point
let integer = 42;
let float = 3.14;
let negative = -100;
let infinity = Infinity;
let notANumber = NaN;

// Special values
console.log(1 / 0);          // Infinity
console.log(-1 / 0);         // -Infinity
console.log(0 / 0);          // NaN
console.log("hello" * 2);    // NaN
console.log(NaN === NaN);    // false (NaN is not equal to itself!)
console.log(Number.isNaN(NaN)); // true (proper way to check)

// 2. String - sequence of characters
let single = 'Hello';
let double = "World";
let template = `Hello ${double}!`; // Template literal (ES6)

// String methods
console.log("hello".toUpperCase());     // "HELLO"
console.log("  trim  ".trim());         // "trim"
console.log("hello".charAt(0));         // "h"
console.log("hello".indexOf("l"));      // 2
console.log("hello".slice(1, 3));       // "el"
console.log("hello".split(""));         // ["h", "e", "l", "l", "o"]

// 3. Boolean
let isTrue = true;
let isFalse = false;

// 4. undefined - variable declared but not assigned
let notAssigned;
console.log(notAssigned);    // undefined
console.log(typeof notAssigned); // "undefined"

// 5. null - intentional absence of value
let empty = null;
console.log(typeof null);    // "object" (historic bug!)
console.log(null === undefined); // false
console.log(null == undefined);  // true (loose equality)

// 6. Symbol (ES6) - unique identifier
let sym1 = Symbol("id");
let sym2 = Symbol("id");
console.log(sym1 === sym2);  // false (always unique)

// 7. BigInt (ES2020) - arbitrary precision integers
let bigNum = 12345678901234567890n;
let bigNum2 = BigInt("12345678901234567890");
console.log(bigNum + 10n);   // Must use BigInt arithmetic
// console.log(bigNum + 10); // Error: Cannot mix BigInt and Number
```

### Non-Primitive Type (Reference)

```javascript
// 8. Object - collections of key-value pairs
let person = {
    name: "Rahul",
    age: 25,
    greet: function() {
        return `Hello, I'm ${this.name}`;
    }
};

// Arrays are objects
let arr = [1, 2, 3];
console.log(typeof arr);     // "object"
console.log(Array.isArray(arr)); // true (proper way to check)

// Functions are objects
function greet() { return "Hello"; }
console.log(typeof greet);   // "function"

// Dates, RegExp, etc. are objects
let date = new Date();
let regex = /hello/gi;
```

### typeof Operator - Gotchas

```javascript
console.log(typeof 42);           // "number"
console.log(typeof "hello");      // "string"
console.log(typeof true);         // "boolean"
console.log(typeof undefined);    // "undefined"
console.log(typeof null);         // "object" ⚠️ BUG!
console.log(typeof Symbol());     // "symbol"
console.log(typeof 10n);          // "bigint"
console.log(typeof {});           // "object"
console.log(typeof []);           // "object" ⚠️ Use Array.isArray()
console.log(typeof function(){}); // "function"
console.log(typeof NaN);          // "number" ⚠️
```

### Primitive vs Reference Types

```javascript
// Primitives - copied by value
let a = 10;
let b = a;
b = 20;
console.log(a); // 10 (unchanged)
console.log(b); // 20

// Objects - copied by reference
let obj1 = { name: "Rahul" };
let obj2 = obj1;
obj2.name = "Amit";
console.log(obj1.name); // "Amit" (changed!)
console.log(obj2.name); // "Amit"

// To copy object (shallow)
let obj3 = { ...obj1 };           // Spread operator
let obj4 = Object.assign({}, obj1); // Object.assign

// Deep copy
let obj5 = JSON.parse(JSON.stringify(obj1)); // Has limitations
let obj6 = structuredClone(obj1);             // Modern way (ES2022)
```

--

## 4. Type Coercion & Conversion

JavaScript performs **implicit type conversion** (coercion) in many situations.

### String Coercion

```javascript
// + operator with string converts to string
console.log("5" + 3);        // "53" (string concatenation)
console.log("5" + true);     // "5true"
console.log("5" + null);     // "5null"
console.log("5" + undefined);// "5undefined"
console.log("5" + {});       // "5[object Object]"
console.log("5" + [1,2]);    // "51,2"

// Explicit conversion
console.log(String(123));    // "123"
console.log((123).toString());// "123"
console.log(`${123}`);       // "123"
```

### Number Coercion

```javascript
// Mathematical operators (except +) convert to number
console.log("6" - 2);        // 4
console.log("6" * "2");      // 12
console.log("6" / "2");      // 3
console.log("10" % "3");     // 1

// Unary + converts to number
console.log(+"123");         // 123
console.log(+"");            // 0
console.log(+"hello");       // NaN
console.log(+true);          // 1
console.log(+false);         // 0
console.log(+null);          // 0
console.log(+undefined);     // NaN
console.log(+[]);            // 0
console.log(+[5]);           // 5
console.log(+[1,2]);         // NaN

// Explicit conversion
console.log(Number("123"));  // 123
console.log(Number("12.5")); // 12.5
console.log(Number("12abc"));// NaN
console.log(parseInt("12abc")); // 12
console.log(parseFloat("12.5abc")); // 12.5
```

### Boolean Coercion

```javascript
// To boolean
console.log(Boolean(1));     // true
console.log(Boolean(0));     // false
console.log(Boolean(""));    // false
console.log(Boolean("hi"));  // true
console.log(Boolean(null));  // false
console.log(Boolean(undefined)); // false
console.log(Boolean({}));    // true (even empty object)
console.log(Boolean([]));    // true (even empty array)

// Double NOT (!!) is a common pattern
console.log(!!1);            // true
console.log(!!"hello");      // true
console.log(!!0);            // false
console.log(!!"");           // false
```

### Tricky Coercion Examples ⚠️

```javascript
// These are commonly asked in technical reviews!
console.log([] + []);        // "" (empty string)
console.log([] + {});        // "[object Object]"
console.log({} + []);        // "[object Object]" or 0 (depends on context)
console.log([] == false);    // true ([] → "" → 0, false → 0)
console.log([] == ![]);      // true ([] == false → true)
console.log("" == false);    // true
console.log(null == undefined); // true
console.log(null === undefined);// false

// More tricky ones
console.log(true + true);    // 2
console.log(true + false);   // 1
console.log("5" - - "3");    // 8 (- - is positive)
console.log("5" + - "3");    // "5-3" (string concatenation)
console.log(1 + "2" + "2");  // "122"
console.log(1 + +"2" + "2"); // "32" (unary + first)
console.log(1 + -"1" + "2"); // "02"
console.log(+"1" + "1" + "2");// "112"
console.log("A" - "B" + "2");// "NaN2"
console.log("A" - "B" + 2);  // NaN
```

--

## 5. Operators

### Comparison Operators

```javascript
// == (loose equality) - performs type coercion
console.log(5 == "5");       // true
console.log(null == undefined); // true
console.log(0 == false);     // true
console.log("" == false);    // true
console.log("0" == false);   // true
console.log([] == false);    // true

// === (strict equality) - no type coercion
console.log(5 === "5");      // false
console.log(null === undefined); // false
console.log(0 === false);    // false

// Always use === unless you have a specific reason for ==

// Object comparison (by reference)
console.log({} === {});      // false
console.log([] === []);      // false
let arr = [1, 2];
let arr2 = arr;
console.log(arr === arr2);   // true (same reference)
```

### Logical Operators - Short Circuit Evaluation

```javascript
// && returns first falsy value or last value
console.log(true && "hello");  // "hello"
console.log(false && "hello"); // false
console.log("a" && "b" && "c");// "c"
console.log("a" && 0 && "c");  // 0

// || returns first truthy value or last value
console.log(false || "hello"); // "hello"
console.log("" || "default");  // "default"
console.log(0 || null || "a"); // "a"
console.log("a" || "b");       // "a"

// Practical usage
let username = user.name || "Guest";
let settings = loadSettings() || defaultSettings;

// ?? (Nullish coalescing - ES2020)
// Returns right side only if left is null or undefined
console.log(null ?? "default");     // "default"
console.log(undefined ?? "default");// "default"
console.log(0 ?? "default");        // 0 (not null/undefined)
console.log("" ?? "default");       // "" (not null/undefined)
console.log(false ?? "default");    // false

// Use ?? when 0, "", false are valid values
let count = userInput ?? 0;  // 0 only if null/undefined
```

### Optional Chaining (?.) - ES2020

```javascript
let user = {
    name: "Rahul",
    address: {
        city: "Mumbai"
    }
};

// Without optional chaining
let city = user.address && user.address.city;

// With optional chaining
let city2 = user.address?.city;           // "Mumbai"
let zip = user.address?.zip;              // undefined
let country = user.location?.country;     // undefined (no error)

// With methods
let user2 = {};
console.log(user2.getName?.());           // undefined

// With arrays
let arr = [1, 2, 3];
console.log(arr?.[0]);                    // 1
console.log(arr?.[10]);                   // undefined
```

### Spread and Rest Operators (...)

```javascript
// Spread - expands array/object
let arr1 = [1, 2, 3];
let arr2 = [...arr1, 4, 5];     // [1, 2, 3, 4, 5]

let obj1 = { a: 1, b: 2 };
let obj2 = { ...obj1, c: 3 };   // { a: 1, b: 2, c: 3 }

// Function arguments
Math.max(...arr1);               // 3

// Rest - collects remaining
function sum(...numbers) {
    return numbers.reduce((a, b) => a + b, 0);
}
console.log(sum(1, 2, 3, 4));   // 10

// Destructuring with rest
let [first, ...rest] = [1, 2, 3, 4];
console.log(first);              // 1
console.log(rest);               // [2, 3, 4]

let { name, ...others } = { name: "Rahul", age: 25, city: "Mumbai" };
console.log(others);             // { age: 25, city: "Mumbai" }
```

--

## 6. Truthy and Falsy Values

### Falsy Values (only 8)

```javascript
// These are ALL the falsy values in JavaScript:
false
0
-0
0n              // BigInt zero
""              // Empty string
null
undefined
NaN

// Everything else is truthy!
```

### Truthy Values - Including Surprising Ones

```javascript
// These are truthy:
true
1
-1
"0"             // Non-empty string (even "0" or "false")
"false"
" "             // String with space
[]              // Empty array
{}              // Empty object
function(){}    // Function
new Date()
Infinity
-Infinity
```

### Practical Examples

```javascript
// Checking for empty array - WRONG
if ([]) { console.log("This runs!"); }  // Empty array is truthy

// Correct way
if (arr.length > 0) { console.log("Has items"); }
if (arr.length) { console.log("Has items"); }   // 0 is falsy

// Checking for empty object
if ({}) { console.log("This runs!"); } // Empty object is truthy

// Correct way
if (Object.keys(obj).length > 0) { console.log("Has properties"); }

// Default values
function greet(name) {
    name = name || "Guest";  // Fails for empty string ""
    // Better:
    name = name ?? "Guest";  // Only if null/undefined
}
```

--

## 7. Scope

### Types of Scope

```javascript
// 1. Global Scope
var globalVar = "I'm global";
let globalLet = "I'm also global";

// 2. Function Scope
function outer() {
    var functionVar = "I'm function scoped";
    let functionLet = "I'm also function scoped";
    
    console.log(functionVar);  // ✅
    console.log(functionLet);  // ✅
}
// console.log(functionVar);   // ❌ ReferenceError
// console.log(functionLet);   // ❌ ReferenceError

// 3. Block Scope (let and const only)
{
    var blockVar = "var ignores blocks";
    let blockLet = "let respects blocks";
    const blockConst = "const respects blocks";
}
console.log(blockVar);        // ✅ "var ignores blocks"
// console.log(blockLet);     // ❌ ReferenceError
// console.log(blockConst);   // ❌ ReferenceError

// Block scope in if/for/while
if (true) {
    let x = 10;
    var y = 20;
}
// console.log(x);            // ❌ ReferenceError
console.log(y);               // ✅ 20
```

### Lexical Scope (Static Scope)

```javascript
// Functions have access to variables from their outer scope
let outerVar = "outer";

function outer() {
    let innerVar = "inner";
    
    function inner() {
        let deepVar = "deep";
        console.log(outerVar);  // ✅ "outer" (lexical scope)
        console.log(innerVar);  // ✅ "inner"
        console.log(deepVar);   // ✅ "deep"
    }
    
    inner();
    // console.log(deepVar);   // ❌ ReferenceError
}
outer();
```

### Scope Chain

```javascript
let a = 1;

function first() {
    let b = 2;
    
    function second() {
        let c = 3;
        
        function third() {
            let d = 4;
            // Scope chain: third → second → first → global
            console.log(a, b, c, d);  // 1, 2, 3, 4
        }
        third();
    }
    second();
}
first();
```

--

## 8. Hoisting

JavaScript moves declarations to the top of their scope before execution.

### var Hoisting

```javascript
console.log(x);  // undefined (hoisted, initialized as undefined)
var x = 5;
console.log(x);  // 5

// What actually happens:
// var x;         // Declaration hoisted
// console.log(x); // undefined
// x = 5;         // Assignment stays in place
// console.log(x); // 5
```

### let and const Hoisting (TDZ)

```javascript
// console.log(y); // ❌ ReferenceError: Cannot access 'y' before initialization
let y = 10;

// console.log(z); // ❌ ReferenceError
const z = 20;

// They ARE hoisted, but in Temporal Dead Zone until initialization
```

### Function Hoisting

```javascript
// Function declarations are fully hoisted
sayHello();  // ✅ "Hello!"

function sayHello() {
    console.log("Hello!");
}

// Function expressions are NOT hoisted (or partially)
// sayBye();  // ❌ TypeError: sayBye is not a function
var sayBye = function() {
    console.log("Bye!");
};

// Arrow functions behave like function expressions
// greet();  // ❌ ReferenceError (with let) or TypeError (with var)
const greet = () => console.log("Hi!");
```

### Tricky Hoisting Examples

```javascript
// Example 1
var x = 1;
function foo() {
    console.log(x);  // undefined (local x is hoisted)
    var x = 2;
    console.log(x);  // 2
}
foo();

// Example 2
function bar() {
    console.log(a);  // [Function: a]
    var a = 10;
    function a() {}
    console.log(a);  // 10
}
bar();
// Function declarations are hoisted ABOVE variable declarations

// Example 3
console.log(typeof foo);  // "function"
var foo = "hello";
function foo() {}
console.log(typeof foo);  // "string"
```

--

## 9. Temporal Dead Zone (TDZ)

The period between entering scope and variable initialization where accessing the variable throws an error.

```javascript
{
    // TDZ starts for x
    // console.log(x);  // ❌ ReferenceError
    
    let x = 10;        // TDZ ends
    console.log(x);    // ✅ 10
}

// TDZ also applies to function parameters
function example(x = y, y = 2) {
    console.log(x, y);
}
// example();  // ❌ ReferenceError: Cannot access 'y' before initialization
// y is in TDZ when x's default value is evaluated

function example2(x = 2, y = x) {
    console.log(x, y);  // 2, 2
}
example2();  // ✅ Works because x is already initialized
```

### TDZ with typeof

```javascript
// typeof with undeclared variable is safe
console.log(typeof undeclaredVar);  // "undefined"

// typeof with TDZ variable throws error
// console.log(typeof tdzVar);  // ❌ ReferenceError
let tdzVar = 10;
```

--

## 10. Strict Mode

Strict mode catches common coding mistakes and makes JavaScript more secure.

```javascript
"use strict";  // Enable at top of script or function

// 1. Must declare variables
// x = 10;  // ❌ ReferenceError (no implicit global)

// 2. Cannot delete variables or functions
let y = 10;
// delete y;  // ❌ SyntaxError

// 3. Cannot use reserved keywords
// let arguments = 10;  // ❌ SyntaxError
// let eval = 10;       // ❌ SyntaxError

// 4. Duplicate parameter names
// function test(a, a) {}  // ❌ SyntaxError

// 5. Octal literals
// let octal = 010;  // ❌ SyntaxError (use 0o10)

// 6. Writing to read-only properties
const obj = {};
Object.defineProperty(obj, 'x', { value: 10, writable: false });
// obj.x = 20;  // ❌ TypeError

// 7. 'this' is undefined in standalone functions
function testThis() {
    console.log(this);  // undefined (not window)
}
testThis();
```

--

## 11. Complex Technical Scenarios

### Scenario 1: What will be the output?

```javascript
console.log(1 + "2" + "2");   // ?
console.log(1 + +"2" + "2");  // ?
console.log(1 + -"1" + "2");  // ?
console.log(+"1" + "1" + "2");// ?
console.log("A" - "B" + "2"); // ?
console.log("A" - "B" + 2);   // ?

// Answers:
// "122" - 1 + "2" = "12", "12" + "2" = "122"
// "32" - +"2" = 2, 1 + 2 = 3, 3 + "2" = "32"
// "02" - -"1" = -1, 1 + (-1) = 0, 0 + "2" = "02"
// "112" - +"1" = 1, 1 + "1" = "11", "11" + "2" = "112"
// "NaN2" - "A" - "B" = NaN, NaN + "2" = "NaN2"
// NaN - "A" - "B" = NaN, NaN + 2 = NaN
```

### Scenario 2: What will be the output?

```javascript
console.log([] == ![]);     // ?
console.log([] == []);      // ?
console.log({} == {});      // ?
console.log([] == false);   // ?
console.log(!![] == true);  // ?
console.log(!!{} == true);  // ?

// Answers:
// true - ![] = false, [] == false → "" == false → 0 == 0 → true
// false - Different references
// false - Different references
// true - [] → "" → 0, false → 0, 0 == 0
// true - [] is truthy, !![] = true
// true - {} is truthy, !!{} = true
```

### Scenario 3: What will be the output?

```javascript
var x = 10;
function test() {
    console.log(x);
    var x = 20;
    console.log(x);
}
test();

// Answer:
// undefined (local x is hoisted, shadows global x)
// 20
```

### Scenario 4: What will be the output?

```javascript
for (var i = 0; i < 3; i++) {
    setTimeout(() => console.log(i), 0);
}

// Answer: 3, 3, 3
// Because var is function-scoped, all callbacks share the same i

// Fix 1: Use let
for (let i = 0; i < 3; i++) {
    setTimeout(() => console.log(i), 0);
}
// Output: 0, 1, 2

// Fix 2: Use IIFE
for (var i = 0; i < 3; i++) {
    (function(j) {
        setTimeout(() => console.log(j), 0);
    })(i);
}
// Output: 0, 1, 2
```

### Scenario 5: What will be the output?

```javascript
console.log(typeof typeof 1);

// Answer: "string"
// typeof 1 = "number" (a string)
// typeof "number" = "string"
```

### Scenario 6: What will be the output?

```javascript
let a = { x: 1 };
let b = a;
a.x = 2;
console.log(b.x);

a = { x: 3 };
console.log(b.x);

// Answer:
// 2 - b points to same object, a.x changed the object
// 2 - a now points to new object, b still points to original
```

### Scenario 7: What will be the output?

```javascript
const arr = [1, 2, 3, 4, 5];
arr.length = 2;
console.log(arr);

// Answer: [1, 2]
// Setting length truncates the array
```

### Scenario 8: What will be the output?

```javascript
console.log(0.1 + 0.2 === 0.3);
console.log(0.1 + 0.2);

// Answer:
// false
// 0.30000000000000004 (floating point precision issue)

// Fix:
console.log((0.1 + 0.2).toFixed(1) === (0.3).toFixed(1)); // true
console.log(Math.abs((0.1 + 0.2) - 0.3) < Number.EPSILON); // true
```

--

## 12. Coding Problems

### Problem 1: Check if a string is palindrome

```javascript
function isPalindrome(str) {
    const cleaned = str.toLowerCase().replace(/[^a-z0-9]/g, '');
    return cleaned === cleaned.split('').reverse().join('');
}

console.log(isPalindrome("A man, a plan, a canal: Panama")); // true
console.log(isPalindrome("race a car")); // false

// Without built-in methods
function isPalindromeManual(str) {
    const cleaned = str.toLowerCase().replace(/[^a-z0-9]/g, '');
    let left = 0, right = cleaned.length - 1;
    
    while (left < right) {
        if (cleaned[left] !== cleaned[right]) return false;
        left++;
        right-;
    }
    return true;
}
```

### Problem 2: Find duplicate elements in array

```javascript
function findDuplicates(arr) {
    const seen = new Set();
    const duplicates = new Set();
    
    for (const item of arr) {
        if (seen.has(item)) {
            duplicates.add(item);
        } else {
            seen.add(item);
        }
    }
    
    return [...duplicates];
}

console.log(findDuplicates([1, 2, 3, 2, 4, 3, 5])); // [2, 3]
```

### Problem 3: Flatten nested array

```javascript
// Using recursion
function flatten(arr) {
    const result = [];
    
    for (const item of arr) {
        if (Array.isArray(item)) {
            result.push(...flatten(item));
        } else {
            result.push(item);
        }
    }
    
    return result;
}

console.log(flatten([1, [2, [3, [4]]]])); // [1, 2, 3, 4]

// Using ES2019 flat()
const nested = [1, [2, [3, [4]]]];
console.log(nested.flat(Infinity)); // [1, 2, 3, 4]
```

### Problem 4: Debounce function

```javascript
function debounce(func, delay) {
    let timeoutId;
    
    return function(...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            func.apply(this, args);
        }, delay);
    };
}

// Usage
const search = debounce((term) => {
    console.log("Searching for:", term);
}, 300);

// Only executes once after user stops typing for 300ms
```

### Problem 5: Deep clone an object

```javascript
function deepClone(obj) {
    if (obj === null || typeof obj !== 'object') {
        return obj;
    }
    
    if (Array.isArray(obj)) {
        return obj.map(item => deepClone(item));
    }
    
    const cloned = {};
    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            cloned[key] = deepClone(obj[key]);
        }
    }
    
    return cloned;
}

// Or use modern approach
const clone = structuredClone(originalObj); // ES2022
```

--

## 13. Key Topics & Explanations

### Topic 1: What is the difference between == and ===?

**Answer:** `==` (loose equality) performs type coercion before comparison, so `5 == "5"` is true. `===` (strict equality) checks both value and type without coercion, so `5 === "5"` is false. Always prefer `===` unless you specifically need type coercion. Common gotcha: `null == undefined` is true but `null === undefined` is false.

--

### Topic 2: What is hoisting in JavaScript?

**Answer:** Hoisting is JavaScript's behavior of moving declarations to the top of their scope before execution. `var` declarations are hoisted and initialized as `undefined`. `let` and `const` are hoisted but remain in the Temporal Dead Zone until their declaration line. Function declarations are fully hoisted (both declaration and definition), while function expressions behave like variable hoisting.

--

### Topic 3: What is the difference between null and undefined?

**Answer:** `undefined` means a variable has been declared but not assigned a value, or a property doesn't exist. `null` is an intentional assignment indicating "no value." `typeof undefined` is "undefined", but `typeof null` is "object" (historic bug). `null == undefined` is true, but `null === undefined` is false. Use `null` when you want to explicitly indicate absence of value.

--

### Topic 4: What is the Temporal Dead Zone (TDZ)?

**Answer:** TDZ is the period between entering a scope and the point where a `let` or `const` variable is declared. During TDZ, accessing the variable throws a ReferenceError. This exists because `let` and `const` are hoisted but not initialized. This prevents using variables before declaration, which is a common source of bugs with `var`.

--

### Topic 5: Why does `typeof null` return "object"?

**Answer:** This is a historic bug from the first version of JavaScript. In the original implementation, values had a type tag stored in the first 3 bits. Objects had tag 000, and null was represented as the NULL pointer (0x00), so it was mistakenly identified as an object. This bug cannot be fixed as it would break existing code.

--

### Topic 6: What is the difference between primitive and reference types?

**Answer:** Primitives (number, string, boolean, null, undefined, symbol, bigint) are stored directly in memory and copied by value. Reference types (objects, arrays, functions) are stored as memory addresses, and variables hold references to them, so copies share the same underlying data. Primitives are immutable; reference types are mutable.

--

### Topic 7: Explain var vs let vs const.

**Answer:** 
- `var`: Function-scoped, can be redeclared, hoisted (initialized as undefined), creates window property
- `let`: Block-scoped, cannot be redeclared, hoisted (but TDZ), can be reassigned
- `const`: Block-scoped, cannot be redeclared or reassigned, must be initialized, but object properties can be modified

Best practice: Use `const` by default, `let` when reassignment is needed, avoid `var`.

--

### Topic 8: What will `typeof NaN` return and why?

**Answer:** `typeof NaN` returns "number". NaN stands for "Not a Number" but it's actually a special numeric value in the IEEE 754 floating-point standard. It represents an undefined or unrepresentable mathematical result (like 0/0 or Math.sqrt(-1)). To check for NaN, use `Number.isNaN()` because NaN is not equal to itself (`NaN === NaN` is false).

--

### Topic 9: What is short-circuit evaluation?

**Answer:** Short-circuit evaluation means logical operators stop evaluating as soon as the result is determined. `&&` returns the first falsy value or the last value. `||` returns the first truthy value or the last value. Used for default values: `name || "Guest"` and conditional execution: `isLoggedIn && showDashboard()`. Nullish coalescing `??` is similar but only checks for null/undefined.

--

### Topic 10: Why does `[] == ![]` return true?

**Answer:** Step by step: `![]` evaluates first - since `[]` is truthy, `![]` is `false`. Now we have `[] == false`. Arrays convert to primitives, so `[]` becomes empty string `""`. Empty string converts to number `0`. `false` converts to number `0`. So `0 == 0` is `true`. This demonstrates JavaScript's complex type coercion rules.

--

## Quick Reference Cheat Sheet

```
Data Types:
├── Primitives (7): number, string, boolean, null, undefined, symbol, bigint
└── Reference (1): object (includes arrays, functions)

Falsy Values (8 only):
false, 0, -0, 0n, "", null, undefined, NaN

Scope:
├── var → function scope
├── let/const → block scope
└── Lexical scope (static) → access outer variables

Hoisting:
├── var → hoisted, initialized as undefined
├── let/const → hoisted, but TDZ applies
├── function declarations → fully hoisted
└── function expressions → not hoisted

Type Coercion with +/-:
├── string + anything = string concatenation
├── number - string = number (tries numeric conversion)
└── Unary + converts to number

Equality:
├── == → type coercion (avoid)
├── === → strict (use this)
└── Objects compared by reference

Best Practices:
├── Use const by default
├── Use let when reassignment needed
├── Never use var
├── Always use === unless null/undefined check needed
└── Use ?? for nullish coalescing (not || for falsy defaults)
```

--

> **Tip:** Type coercion and hoisting are the **#1 most asked** JavaScript topics in WITCH technical reviews. Make sure you can predict outputs of tricky expressions and understand the difference between var/let/const. Practice the common gotchas like `[] == ![]`, `typeof null`, and var in loops with setTimeout.
