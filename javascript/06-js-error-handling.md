# JavaScript Error Handling & Debugging - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Error Types, try-catch-finally, Custom Errors, Debugging Techniques, Error Boundaries, and tricky scenarios.

---

## Table of Contents

1. [Error Types in JavaScript](#error-types-in-javascript)
2. [try-catch-finally](#try-catch-finally)
3. [Custom Error Classes](#custom-error-classes)
4. [Error Handling in Async Code](#error-handling-in-async-code)
5. [Global Error Handling](#global-error-handling)
6. [Debugging Techniques](#debugging-techniques)
7. [Error Handling Patterns](#error-handling-patterns)
8. [Complex Technical Scenarios](#complex-technical-scenarios)
9. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Error Types in JavaScript

### Built-in Error Types

```javascript
// 1. SyntaxError — Invalid syntax (parsed at load time)
// eval("var a =");  // SyntaxError: Unexpected end of input

// 2. ReferenceError — Variable not defined
console.log(undeclaredVar); // ReferenceError: undeclaredVar is not defined

// 3. TypeError — Wrong type operation
null.toString();            // TypeError: Cannot read properties of null
const num = 5;
num();                      // TypeError: num is not a function

// 4. RangeError — Value out of range
new Array(-1);              // RangeError: Invalid array length
(1).toFixed(200);           // RangeError: toFixed() digits argument must be between 0 and 100

// 5. URIError — Invalid URI functions
decodeURIComponent('%');    // URIError: URI malformed

// 6. EvalError — Error in eval() (legacy, rarely thrown)

// 7. AggregateError (ES2021) — Multiple errors
Promise.any([
    Promise.reject(new Error("Err1")),
    Promise.reject(new Error("Err2"))
]).catch(e => {
    console.log(e instanceof AggregateError); // true
    console.log(e.errors);                     // [Error: Err1, Error: Err2]
});
```

### Error Object Properties

```javascript
try {
    undeclaredVar;
} catch (error) {
    console.log(error.name);    // "ReferenceError"
    console.log(error.message); // "undeclaredVar is not defined"
    console.log(error.stack);   // Full stack trace string
    
    // Checking error type
    console.log(error instanceof ReferenceError); // true
    console.log(error instanceof TypeError);      // false
}
```

---

## 2. try-catch-finally

### Basic Structure

```javascript
try {
    // Code that might throw
    const data = JSON.parse('invalid json');
} catch (error) {
    // Handle the error
    console.error("Parsing failed:", error.message);
} finally {
    // ALWAYS runs — cleanup code
    console.log("Cleanup done");
}
```

### Catch Binding Optional (ES2019)

```javascript
// Before ES2019 — must declare error variable
try {
    JSON.parse(data);
} catch (error) {  // 'error' required even if unused
    handleDefault();
}

// ES2019+ — optional catch binding
try {
    JSON.parse(data);
} catch {          // No variable needed
    handleDefault();
}
```

### Rethrowing Errors

```javascript
try {
    doSomething();
} catch (error) {
    if (error instanceof TypeError) {
        // Handle known error
        console.log("Type error handled");
    } else {
        // Rethrow unknown errors — don't swallow them
        throw error;
    }
}
```

### Nested try-catch

```javascript
try {
    try {
        throw new Error("Inner error");
    } catch (innerError) {
        console.log("Caught inner:", innerError.message);
        throw innerError; // Rethrow to outer
    } finally {
        console.log("Inner finally");
    }
} catch (outerError) {
    console.log("Caught outer:", outerError.message);
}
// Output:
// Caught inner: Inner error
// Inner finally
// Caught outer: Inner error
```

---

## 3. Custom Error Classes

### Creating Custom Errors

```javascript
class ValidationError extends Error {
    constructor(message, field) {
        super(message);
        this.name = "ValidationError";
        this.field = field;
        
        // Maintains proper stack trace (V8 engines)
        if (Error.captureStackTrace) {
            Error.captureStackTrace(this, ValidationError);
        }
    }
}

class NotFoundError extends Error {
    constructor(resource, id) {
        super(`${resource} with ID ${id} not found`);
        this.name = "NotFoundError";
        this.resource = resource;
        this.id = id;
        this.statusCode = 404;
    }
}

class AuthenticationError extends Error {
    constructor(message = "Authentication failed") {
        super(message);
        this.name = "AuthenticationError";
        this.statusCode = 401;
    }
}

// Usage
function findUser(id) {
    const user = database.get(id);
    if (!user) {
        throw new NotFoundError("User", id);
    }
    return user;
}

try {
    findUser(999);
} catch (error) {
    if (error instanceof NotFoundError) {
        console.log(error.statusCode); // 404
        console.log(error.resource);   // "User"
    }
}
```

### Error Hierarchy

```javascript
class AppError extends Error {
    constructor(message, statusCode) {
        super(message);
        this.name = this.constructor.name;
        this.statusCode = statusCode;
    }
}

class ClientError extends AppError {
    constructor(message, statusCode = 400) {
        super(message, statusCode);
    }
}

class ServerError extends AppError {
    constructor(message, statusCode = 500) {
        super(message, statusCode);
    }
}

// Usage
const err = new ClientError("Invalid email");
console.log(err instanceof ClientError); // true
console.log(err instanceof AppError);    // true
console.log(err instanceof Error);       // true
```

---

## 4. Error Handling in Async Code

### Callbacks — Error-First Pattern

```javascript
function fetchData(callback) {
    setTimeout(() => {
        const error = Math.random() > 0.5 ? new Error("Failed") : null;
        const data = error ? null : { name: "Rahul" };
        callback(error, data); // Error FIRST
    }, 1000);
}

fetchData((error, data) => {
    if (error) {
        console.error("Error:", error.message);
        return;
    }
    console.log("Data:", data);
});
```

### Promises — .catch()

```javascript
fetch("/api/users")
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        return response.json();
    })
    .then(data => console.log(data))
    .catch(error => {
        // Catches errors from ALL previous then() blocks
        console.error("Request failed:", error.message);
    })
    .finally(() => {
        // Always runs — cleanup
        hideLoadingSpinner();
    });
```

### async/await — try-catch

```javascript
async function fetchUser(id) {
    try {
        const response = await fetch(`/api/users/${id}`);
        
        if (!response.ok) {
            throw new NotFoundError("User", id);
        }
        
        const user = await response.json();
        return user;
    } catch (error) {
        if (error instanceof NotFoundError) {
            console.log("User not found");
            return null;
        }
        // Unexpected error — rethrow
        throw error;
    } finally {
        console.log("Fetch attempt complete");
    }
}
```

### Handling Multiple Async Operations

```javascript
// Promise.allSettled — never rejects, always returns all results
const results = await Promise.allSettled([
    fetch("/api/users"),
    fetch("/api/orders"),
    fetch("/api/products")
]);

results.forEach((result, index) => {
    if (result.status === "fulfilled") {
        console.log(`Request ${index}: Success`);
    } else {
        console.log(`Request ${index}: Failed - ${result.reason.message}`);
    }
});
```

### Async Error Wrapper Pattern

```javascript
// Utility to avoid repetitive try-catch
function tryCatch(asyncFn) {
    return async (...args) => {
        try {
            const data = await asyncFn(...args);
            return [data, null];
        } catch (error) {
            return [null, error];
        }
    };
}

// Usage
const safeFetch = tryCatch(fetchUser);
const [user, error] = await safeFetch(123);

if (error) {
    console.error("Failed:", error.message);
} else {
    console.log("User:", user);
}
```

---

## 5. Global Error Handling

### Browser — window.onerror

```javascript
// Catches unhandled errors globally
window.onerror = function(message, source, lineno, colno, error) {
    console.error("Global error:", {
        message,
        source,     // Script URL
        lineno,     // Line number
        colno,      // Column number
        error       // Error object
    });
    
    // Return true to prevent default browser error handling
    return true;
};
```

### Unhandled Promise Rejections

```javascript
// Browser
window.addEventListener("unhandledrejection", (event) => {
    console.error("Unhandled rejection:", event.reason);
    event.preventDefault(); // Prevent default logging
});

// Node.js
process.on("unhandledRejection", (reason, promise) => {
    console.error("Unhandled Rejection:", reason);
});

process.on("uncaughtException", (error) => {
    console.error("Uncaught Exception:", error);
    process.exit(1); // Exit — app is in unknown state
});
```

### Express.js Error Middleware

```javascript
// Error handling middleware (4 parameters)
app.use((err, req, res, next) => {
    const statusCode = err.statusCode || 500;
    
    res.status(statusCode).json({
        status: "error",
        message: err.message,
        ...(process.env.NODE_ENV === "development" && { stack: err.stack })
    });
});

// Async route wrapper
const asyncHandler = (fn) => (req, res, next) => {
    Promise.resolve(fn(req, res, next)).catch(next);
};

app.get("/users/:id", asyncHandler(async (req, res) => {
    const user = await findUser(req.params.id);
    if (!user) throw new NotFoundError("User", req.params.id);
    res.json(user);
}));
```

---

## 6. Debugging Techniques

### Console Methods

```javascript
// Basic
console.log("Info");
console.warn("Warning");
console.error("Error");

// Grouped output
console.group("User Details");
console.log("Name: Rahul");
console.log("Age: 25");
console.groupEnd();

// Table (great for arrays/objects)
console.table([
    { name: "Rahul", age: 25 },
    { name: "Amit", age: 30 }
]);

// Timing
console.time("API Call");
await fetch("/api/data");
console.timeEnd("API Call"); // "API Call: 234.5ms"

// Count
function handleClick() {
    console.count("clicks"); // clicks: 1, clicks: 2, ...
}

// Assert (logs only if condition is false)
console.assert(1 === 2, "1 is not 2"); // Assertion failed: 1 is not 2

// Stack trace
console.trace("Where am I?"); // Prints call stack

// Clear
console.clear();
```

### Debugger Statement

```javascript
function processData(data) {
    debugger; // Execution pauses here when DevTools is open
    
    const filtered = data.filter(item => item.active);
    return filtered;
}
```

### Source Maps

```javascript
// In production, minified code is unreadable
// Source maps map minified code back to original source
// webpack.config.js
module.exports = {
    devtool: "source-map" // Generates .map files
};
```

---

## 7. Error Handling Patterns

### Result Pattern (Either/Result type)

```javascript
class Result {
    constructor(value, error) {
        this.value = value;
        this.error = error;
    }
    
    static ok(value) {
        return new Result(value, null);
    }
    
    static fail(error) {
        return new Result(null, error);
    }
    
    isOk() { return this.error === null; }
    isError() { return this.error !== null; }
}

function divide(a, b) {
    if (b === 0) return Result.fail("Division by zero");
    return Result.ok(a / b);
}

const result = divide(10, 0);
if (result.isError()) {
    console.log("Error:", result.error);
} else {
    console.log("Result:", result.value);
}
```

### Retry Pattern

```javascript
async function withRetry(fn, maxRetries = 3, delay = 1000) {
    for (let attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            return await fn();
        } catch (error) {
            if (attempt === maxRetries) throw error;
            
            console.log(`Attempt ${attempt} failed. Retrying in ${delay}ms...`);
            await new Promise(resolve => setTimeout(resolve, delay));
            delay *= 2; // Exponential backoff
        }
    }
}

// Usage
const data = await withRetry(() => fetch("/api/unstable-endpoint"), 3, 500);
```

---

## 8. Complex Technical Scenarios

### Topic 1: Can you catch SyntaxError with try-catch?

**Explanation:** **No** — SyntaxErrors occur at **parse time** (before execution), so try-catch (which runs at execution time) cannot catch them. Exception: `eval()` and `JSON.parse()` throw SyntaxErrors at runtime, which CAN be caught.

```javascript
try {
    eval("var a =");  // ✅ Can be caught (eval runs at runtime)
} catch (e) {
    console.log(e instanceof SyntaxError); // true
}

// But this cannot be caught:
// try {
//     var a =;  // ❌ Parsing fails before try-catch even executes
// } catch (e) { }
```

### Topic 2: What happens if you throw inside a finally block?

**Explanation:** The original error is **replaced** by the `finally` error — the original error is lost.

```javascript
try {
    throw new Error("Original");
} finally {
    throw new Error("From finally"); // Replaces "Original"
}
// Only "From finally" is thrown
```

### Topic 3: Error handling in setTimeout

**Explanation:** try-catch around `setTimeout` does NOT catch errors inside the callback because the callback runs in a different call stack (via the Event Loop).

```javascript
// ❌ Does NOT catch
try {
    setTimeout(() => {
        throw new Error("Async error"); // Uncaught!
    }, 1000);
} catch (e) {
    console.log("Caught"); // Never runs
}

// ✅ Correct — put try-catch INSIDE the callback
setTimeout(() => {
    try {
        throw new Error("Async error");
    } catch (e) {
        console.log("Caught:", e.message); // "Caught: Async error"
    }
}, 1000);
```

### Topic 4: Why does `fetch` not reject on HTTP 404/500?

**Explanation:** `fetch` only rejects on **network failures** (DNS failure, no internet). HTTP error responses (4xx, 5xx) are considered successful network transactions — the promise resolves with `response.ok = false`. You must manually check.

```javascript
const response = await fetch("/api/missing-resource");
// response.ok === false (status 404)
// But fetch resolved successfully — no error thrown

if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
}
```

---

## 9. Key Topics & Explanations

### Topic 1: Error vs Exception in JavaScript?

**Explanation:** JavaScript uses only **Error** (not "Exception"). There's no distinction. All error types (`TypeError`, `ReferenceError`, etc.) extend the `Error` class. You can throw anything — strings, numbers, objects — but best practice is to throw Error instances.

### Topic 2: Why should you always throw Error objects?

**Explanation:** Error objects include:
- `name`: Error type
- `message`: Description
- `stack`: Stack trace for debugging

Throwing primitives (`throw "error"`) loses the stack trace — making debugging impossible.

### Topic 3: How do Error Boundaries work in React?

**Explanation:** Class components with `componentDidCatch()` and `static getDerivedStateFromError()` catch errors in their child component tree during rendering. They display fallback UI. Note: They do NOT catch errors in event handlers, async code, or server-side rendering.

### Topic 4: What is a "Swallowed" error?

**Explanation:** An error that is caught but not handled or logged — silently disappears. This is the **worst practice** in error handling.
```javascript
// ❌ Swallowed error — extremely dangerous
try { riskyOperation(); } catch (e) { }

// ✅ At minimum, log it
try { riskyOperation(); } catch (e) { console.error(e); }
```
