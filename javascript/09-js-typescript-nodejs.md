# JavaScript TypeScript Basics & Node.js Fundamentals - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** TypeScript core concepts, Node.js fundamentals, Express.js basics, npm, and tricky scenarios.

---

## Table of Contents

### Part 1: TypeScript Essentials
1. [TypeScript Overview](#typescript-overview)
2. [Basic Types](#basic-types)
3. [Interfaces & Type Aliases](#interfaces-type-aliases)
4. [Functions in TypeScript](#functions-in-typescript)
5. [Generics](#generics)
6. [Enums](#enums)
7. [Type Guards & Narrowing](#type-guards-narrowing)
8. [Utility Types](#utility-types)

### Part 2: Node.js Fundamentals
9. [Node.js Core Concepts](#nodejs-core-concepts)
10. [Modules System](#modules-system)
11. [File System (fs)](#file-system-fs)
12. [HTTP Module & Express.js Basics](#http-module-expressjs-basics)
13. [npm & Package Management](#npm-package-management)
14. [Environment Variables](#environment-variables)

### Part 3: Scenarios
15. [Complex Technical Scenarios](#complex-technical-scenarios)
16. [Key Topics & Explanations](#key-topics-explanations)

---

# Part 1: TypeScript Essentials

## 1. TypeScript Overview

TypeScript = **JavaScript + Static Types**. It compiles to JavaScript. All JS is valid TS.

```
TypeScript (.ts) → tsc compiler → JavaScript (.js) → Browser/Node.js
```

**Why TypeScript?**
- Catch errors at **compile time** (not runtime)
- Better IDE support (autocompletion, refactoring)
- Self-documenting code
- Easier to maintain large codebases

---

## 2. Basic Types

```typescript
// Primitive types
let name: string = "Rahul";
let age: number = 25;
let isActive: boolean = true;
let nothing: null = null;
let notDefined: undefined = undefined;

// Arrays
let numbers: number[] = [1, 2, 3];
let names: Array<string> = ["Rahul", "Amit"]; // Generic syntax

// Tuple — fixed length, fixed types
let user: [string, number] = ["Rahul", 25];
// user = [25, "Rahul"]; // ❌ Error — wrong order

// any — opt out of type checking (avoid when possible)
let data: any = "hello";
data = 42;      // OK — no checking
data = true;    // OK — defeats the purpose of TS

// unknown — safer version of any (must narrow before use)
let value: unknown = "hello";
// value.toUpperCase(); // ❌ Error — can't use without checking
if (typeof value === "string") {
    value.toUpperCase(); // ✅ After type check
}

// void — function that returns nothing
function log(msg: string): void {
    console.log(msg);
}

// never — function that never returns
function throwError(msg: string): never {
    throw new Error(msg);
}

function infiniteLoop(): never {
    while (true) { }
}

// Union Types — one of several types
let id: string | number = "ABC123";
id = 456; // Also valid

// Literal Types — exact values
let status: "active" | "inactive" | "pending" = "active";
// status = "deleted"; // ❌ Error

// Type Assertion — tell TS you know better
const input = document.getElementById("name") as HTMLInputElement;
input.value = "Rahul";
// Alternative syntax:
const input2 = <HTMLInputElement>document.getElementById("name");
```

---

## 3. Interfaces & Type Aliases

### Interface

```typescript
interface User {
    id: number;
    name: string;
    email: string;
    age?: number;           // Optional property
    readonly createdAt: Date; // Cannot be modified after creation
}

const user: User = {
    id: 1,
    name: "Rahul",
    email: "rahul@example.com",
    createdAt: new Date()
};

// user.createdAt = new Date(); // ❌ Error — readonly

// Extending interfaces
interface Employee extends User {
    department: string;
    salary: number;
}

// Multiple inheritance
interface Manager extends Employee {
    teamSize: number;
}

// Declaration Merging (unique to interfaces)
interface User {
    phone?: string; // Merged with original User interface
}
```

### Type Alias

```typescript
type ID = string | number;

type Point = {
    x: number;
    y: number;
};

type Status = "active" | "inactive" | "pending";

// Intersection — combine types
type Employee = User & {
    department: string;
    salary: number;
};
```

### Interface vs Type

| Feature | Interface | Type |
|---------|-----------|------|
| **Object shapes** | ✅ Yes | ✅ Yes |
| **Extend/Inherit** | `extends` | `&` (intersection) |
| **Declaration merging** | ✅ Yes | ❌ No |
| **Union types** | ❌ No | ✅ Yes (`string \| number`) |
| **Primitives** | ❌ No | ✅ Yes (`type ID = string`) |
| **Computed/Mapped** | ❌ No | ✅ Yes |
| **Best for** | Object shapes, APIs | Unions, complex types |

---

## 4. Functions in TypeScript

```typescript
// Function with types
function add(a: number, b: number): number {
    return a + b;
}

// Arrow function
const multiply = (a: number, b: number): number => a * b;

// Optional & Default parameters
function greet(name: string, prefix: string = "Mr.", suffix?: string): string {
    return `${prefix} ${name}${suffix ? ` ${suffix}` : ""}`;
}

// Rest parameters
function sum(...nums: number[]): number {
    return nums.reduce((acc, n) => acc + n, 0);
}

// Function type
type MathOp = (a: number, b: number) => number;
const divide: MathOp = (a, b) => a / b;

// Callback type
function fetchData(url: string, callback: (data: string) => void): void {
    // ...
    callback("result");
}

// Overloads
function format(value: string): string;
function format(value: number): string;
function format(value: string | number): string {
    if (typeof value === "string") return value.trim();
    return value.toFixed(2);
}
```

---

## 5. Generics

```typescript
// Without generics — type info lost
function identity(value: any): any {
    return value;
}

// With generics — type preserved
function identity<T>(value: T): T {
    return value;
}
const result = identity<string>("Hello"); // Type: string
const num = identity(42);                  // Type: number (inferred)

// Generic interface
interface ApiResponse<T> {
    data: T;
    status: number;
    message: string;
}

const userResponse: ApiResponse<User> = {
    data: { id: 1, name: "Rahul", email: "r@e.com", createdAt: new Date() },
    status: 200,
    message: "Success"
};

// Generic constraints
function getLength<T extends { length: number }>(value: T): number {
    return value.length;
}
getLength("hello");    // 5
getLength([1, 2, 3]);  // 3
// getLength(123);     // ❌ Error — number has no 'length'

// Generic with keyof
function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
    return obj[key];
}
getProperty({ name: "Rahul", age: 25 }, "name"); // string
```

---

## 6. Enums

```typescript
// Numeric enum (default — starts from 0)
enum Direction {
    Up,     // 0
    Down,   // 1
    Left,   // 2
    Right   // 3
}
const dir: Direction = Direction.Up; // 0

// String enum (preferred — readable values)
enum Status {
    Active = "ACTIVE",
    Inactive = "INACTIVE",
    Pending = "PENDING"
}
const status: Status = Status.Active; // "ACTIVE"

// const enum — inlined at compile time (no runtime object)
const enum Color {
    Red = "RED",
    Blue = "BLUE"
}
```

---

## 7. Type Guards & Narrowing

```typescript
// typeof guard
function process(value: string | number) {
    if (typeof value === "string") {
        return value.toUpperCase(); // TS knows it's string here
    }
    return value.toFixed(2);        // TS knows it's number here
}

// instanceof guard
function handleError(err: Error | string) {
    if (err instanceof Error) {
        console.log(err.stack);
    } else {
        console.log(err);
    }
}

// in operator guard
interface Dog { bark(): void; }
interface Cat { meow(): void; }

function speak(animal: Dog | Cat) {
    if ("bark" in animal) {
        animal.bark();
    } else {
        animal.meow();
    }
}

// Custom type guard (type predicate)
function isString(value: unknown): value is string {
    return typeof value === "string";
}

if (isString(input)) {
    input.toUpperCase(); // TS knows it's string
}

// Discriminated Union
interface Circle { kind: "circle"; radius: number; }
interface Square { kind: "square"; side: number; }
type Shape = Circle | Square;

function area(shape: Shape): number {
    switch (shape.kind) {
        case "circle": return Math.PI * shape.radius ** 2;
        case "square": return shape.side ** 2;
    }
}
```

---

## 8. Utility Types

```typescript
// Partial<T> — all properties optional
type PartialUser = Partial<User>;
// { id?: number; name?: string; email?: string; ... }

// Required<T> — all properties required
type RequiredUser = Required<User>;

// Readonly<T> — all properties readonly
type ImmutableUser = Readonly<User>;

// Pick<T, K> — select specific properties
type UserName = Pick<User, "id" | "name">;
// { id: number; name: string; }

// Omit<T, K> — exclude specific properties
type UserWithoutEmail = Omit<User, "email">;

// Record<K, V> — object type with key-value mapping
type UserMap = Record<string, User>;
const users: UserMap = { "u1": user };

// Exclude<T, U> — remove types from union
type NotString = Exclude<string | number | boolean, string>;
// number | boolean

// Extract<T, U> — extract types from union
type OnlyString = Extract<string | number | boolean, string>;
// string

// ReturnType<T> — get return type of function
type AddResult = ReturnType<typeof add>; // number

// Parameters<T> — get parameter types
type AddParams = Parameters<typeof add>; // [number, number]
```

---

# Part 2: Node.js Fundamentals

## 9. Node.js Core Concepts

### What is Node.js?

- JavaScript runtime built on **Chrome V8 engine**
- **Non-blocking, event-driven** I/O model
- **Single-threaded** (but uses thread pool for heavy I/O)
- Great for I/O-intensive applications (APIs, real-time apps)
- NOT ideal for CPU-intensive operations (use Worker Threads)

### Event Loop in Node.js

```
┌───────────────────────┐
│      Timers           │  ← setTimeout, setInterval
├───────────────────────┤
│   Pending Callbacks   │  ← I/O callbacks deferred to next loop
├───────────────────────┤
│      Idle/Prepare     │  ← Internal use
├───────────────────────┤
│        Poll           │  ← Retrieve new I/O events (main work)
├───────────────────────┤
│       Check           │  ← setImmediate callbacks
├───────────────────────┤
│   Close Callbacks     │  ← socket.on('close', ...)
└───────────────────────┘

Microtask Queue (priority):
1. process.nextTick()    ← Highest priority
2. Promise callbacks     ← Second priority
```

```javascript
console.log("1");

setTimeout(() => console.log("2"), 0);
setImmediate(() => console.log("3"));

Promise.resolve().then(() => console.log("4"));
process.nextTick(() => console.log("5"));

console.log("6");

// Output: 1, 6, 5, 4, 2, 3
// Sync first → nextTick → Promises → setTimeout → setImmediate
```

---

## 10. Modules System

### CommonJS (Node.js default)

```javascript
// math.js
function add(a, b) { return a + b; }
function subtract(a, b) { return a - b; }

module.exports = { add, subtract };
// OR: exports.add = add;

// app.js
const { add, subtract } = require("./math");
console.log(add(5, 3)); // 8
```

### ES Modules (ESM — modern)

```javascript
// math.mjs (or set "type": "module" in package.json)
export function add(a, b) { return a + b; }
export default function multiply(a, b) { return a * b; }

// app.mjs
import multiply, { add } from "./math.mjs";
```

### CommonJS vs ES Modules

| Feature | CommonJS | ESM |
|---------|----------|-----|
| **Syntax** | `require()`/`module.exports` | `import`/`export` |
| **Loading** | Synchronous | Asynchronous |
| **Tree Shaking** | ❌ No | ✅ Yes |
| **Top-level await** | ❌ No | ✅ Yes |
| **Default in** | Node.js | Browsers |
| **File extension** | `.js` | `.mjs` or `"type": "module"` |

---

## 11. File System (fs)

```javascript
const fs = require("fs");
const fsPromises = require("fs").promises;

// READ — Async (callback)
fs.readFile("data.txt", "utf8", (err, data) => {
    if (err) throw err;
    console.log(data);
});

// READ — Promise
const data = await fsPromises.readFile("data.txt", "utf8");

// READ — Sync (blocks event loop — avoid in production)
const data = fs.readFileSync("data.txt", "utf8");

// WRITE
await fsPromises.writeFile("output.txt", "Hello World");

// APPEND
await fsPromises.appendFile("log.txt", "New entry\n");

// DELETE
await fsPromises.unlink("temp.txt");

// CHECK EXISTS
try {
    await fsPromises.access("file.txt");
    console.log("File exists");
} catch {
    console.log("File does not exist");
}

// Directory operations
await fsPromises.mkdir("new-dir", { recursive: true });
const files = await fsPromises.readdir(".");

// Streams (for large files)
const readStream = fs.createReadStream("large-file.csv");
const writeStream = fs.createWriteStream("output.csv");
readStream.pipe(writeStream);
```

---

## 12. HTTP Module & Express.js Basics

### Native HTTP

```javascript
const http = require("http");

const server = http.createServer((req, res) => {
    if (req.url === "/api/users" && req.method === "GET") {
        res.writeHead(200, { "Content-Type": "application/json" });
        res.end(JSON.stringify([{ name: "Rahul" }]));
    } else {
        res.writeHead(404);
        res.end("Not Found");
    }
});

server.listen(3000, () => console.log("Server on port 3000"));
```

### Express.js (Most Popular Framework)

```javascript
const express = require("express");
const app = express();

// Middleware
app.use(express.json());           // Parse JSON bodies
app.use(express.urlencoded({ extended: true })); // Parse form data

// Custom middleware
app.use((req, res, next) => {
    console.log(`${req.method} ${req.url}`);
    next(); // Pass to next middleware/route
});

// Routes
app.get("/api/users", (req, res) => {
    res.json([{ id: 1, name: "Rahul" }]);
});

app.get("/api/users/:id", (req, res) => {
    const { id } = req.params;
    res.json({ id, name: "Rahul" });
});

app.post("/api/users", (req, res) => {
    const { name, email } = req.body;
    res.status(201).json({ id: Date.now(), name, email });
});

// Error handling middleware (must have 4 params)
app.use((err, req, res, next) => {
    res.status(500).json({ error: err.message });
});

app.listen(3000, () => console.log("Express server running"));
```

---

## 13. npm & Package Management

```bash
# Initialize project
npm init -y

# Install dependencies
npm install express          # Production dependency
npm install -D nodemon       # Dev dependency (-D / --save-dev)
npm install -g typescript    # Global install

# package.json scripts
{
    "scripts": {
        "start": "node app.js",
        "dev": "nodemon app.js",
        "test": "jest"
    }
}

# Run scripts
npm start       # npm run start
npm run dev
npm test        # npm run test
```

### package.json vs package-lock.json

| Feature | package.json | package-lock.json |
|---------|-------------|-------------------|
| **Purpose** | Project config, dependency ranges | Exact dependency tree |
| **Versions** | `^2.0.0` (range) | `2.0.3` (exact) |
| **Commit?** | ✅ Always | ✅ Always |
| **Editable** | ✅ Yes | ❌ Auto-generated |

---

## 14. Environment Variables

```javascript
// .env file
// PORT=3000
// DB_URL=mongodb://localhost/mydb
// JWT_SECRET=supersecretkey

// Using dotenv
require("dotenv").config();

const port = process.env.PORT || 3000;
const dbUrl = process.env.DB_URL;

// NEVER commit .env file — add to .gitignore
```

---

# Part 3: Scenarios

## 15. Complex Technical Scenarios

### Topic 1: What does `unknown` solve that `any` doesn't?

**Explanation:** `any` disables ALL type checking — you can call any method, assign to anything. `unknown` forces you to check the type BEFORE using it, preventing runtime errors. Use `unknown` for values whose type you don't know — it's a safe `any`.

### Topic 2: Why use interfaces for API responses?

**Explanation:** Interfaces define the **shape** of data you expect from APIs. Without them, you access properties blindly and get errors at runtime. With interfaces, TypeScript catches typos (`user.nmae` instead of `user.name`) at compile time.

### Topic 3: require() is synchronous — why is that a problem?

**Explanation:** `require()` blocks the event loop while loading a module. For initial app startup, this is acceptable. But dynamic `require()` inside request handlers blocks ALL other requests while the module loads. For dynamic loading in production, use `import()` (async).

### Topic 4: What is the N+1 problem in Node.js?

**Explanation:** Same as in any backend — fetching a list of items, then making a separate query for each item's related data. Example: fetch 100 users, then 100 separate queries for each user's orders. **Fix**: Use JOINs, batch queries, or DataLoader pattern.

---

## 16. Key Topics & Explanations

### Topic 1: TypeScript vs JavaScript — when to use which?

**Explanation:**
- **TypeScript**: Large projects, team collaboration, API-heavy apps, complex data models. Cost: Setup overhead.
- **JavaScript**: Small scripts, quick prototypes, simple DOM manipulation. Faster to write initially.

### Topic 2: Node.js — single-threaded but handles thousands of connections?

**Explanation:** Node.js uses **non-blocking I/O** and an **event loop**. While I/O operations (file read, DB query, network) wait, Node processes other requests. The thread pool (libuv) handles heavy I/O in the background. This is why Node excels at I/O-intensive (API servers) but struggles with CPU-intensive (video encoding) work.

### Topic 3: What is middleware in Express.js?

**Explanation:** Functions that have access to `req`, `res`, and `next()`. They execute in order, forming a pipeline. Each can modify request/response, end the cycle, or pass to next. Types: Application-level, Router-level, Error-handling (4 params), Built-in (`express.json()`), Third-party (`cors`, `helmet`).

### Topic 4: Difference between dependencies and devDependencies?

**Explanation:**
- `dependencies`: Needed at **runtime** in production (express, mongoose). Installed with `npm install`.
- `devDependencies`: Needed only during **development** (jest, nodemon, typescript). NOT installed in production (`npm install --production`).
