# JavaScript Async Programming - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Event Loop, Callbacks, Promises, async/await, Error Handling, and tricky technical scenarios.

--

## Table of Contents

1. [JavaScript Runtime & Event Loop](#javascript-runtime-event-loop)
2. [Callbacks](#callbacks)
3. [Promises](#promises)
4. [Promise Methods](#promise-methods)
5. [async/await](#asyncawait)
6. [Error Handling](#error-handling)
7. [Common Async Patterns](#common-async-patterns)
8. [Microtasks vs Macrotasks](#microtasks-vs-macrotasks)
9. [Complex Technical Scenarios](#complex-technical-scenarios)
10. [Coding Problems](#coding-problems)
11. [Key Topics & Explanations](#key-topics-explanations)

--

## 1. JavaScript Runtime & Event Loop

### JavaScript is Single-Threaded

```javascript
// JavaScript runs one task at a time
// But it handles async operations through the Event Loop

console.log("First");

setTimeout(() => {
    console.log("Second (async)");
}, 0);

console.log("Third");

// Output:
// First
// Third
// Second (async)

// Even with 0ms delay, setTimeout is async!
```

### Components of JS Runtime

```
┌─────────────────────────────────────────────────────────────┐
│                      JavaScript Engine                       │
│  ┌─────────────────────┐    ┌──────────────────────────┐   │
│  │     Call Stack      │    │         Heap             │   │
│  │  (Function Calls)   │    │    (Object Storage)      │   │
│  └─────────────────────┘    └──────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                  Web APIs (Browser) / Node APIs             │
│         setTimeout, fetch, DOM events, etc.                 │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                     Callback Queue                          │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Macrotask Queue: setTimeout, setInterval, I/O       │  │
│  └──────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Microtask Queue: Promises, queueMicrotask           │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      Event Loop                             │
│        (Moves tasks from Queue to Call Stack)               │
└─────────────────────────────────────────────────────────────┘
```

### How Event Loop Works

```javascript
console.log("Start");

setTimeout(() => {
    console.log("Timeout 1");
}, 0);

Promise.resolve().then(() => {
    console.log("Promise 1");
}).then(() => {
    console.log("Promise 2");
});

setTimeout(() => {
    console.log("Timeout 2");
}, 0);

console.log("End");

// Output:
// Start
// End
// Promise 1
// Promise 2
// Timeout 1
// Timeout 2

// Order: Sync code → Microtasks → Macrotasks
```

### Event Loop Algorithm

```
1. Execute synchronous code in Call Stack
2. When Call Stack is empty:
   a. Execute ALL microtasks (Promise callbacks, queueMicrotask)
   b. Execute ONE macrotask (setTimeout, setInterval, I/O)
   c. Repeat from step 2a
3. Render UI (if browser)
```

--

## 2. Callbacks

A callback is a function passed as an argument to be executed later.

### Basic Callbacks

```javascript
// Synchronous callback
function greet(name, callback) {
    const message = `Hello, ${name}!`;
    callback(message);
}

greet("Rahul", function(msg) {
    console.log(msg);  // "Hello, Rahul!"
});

// Asynchronous callback
setTimeout(function() {
    console.log("This runs later");
}, 1000);

// Array methods use callbacks
[1, 2, 3].forEach(function(num) {
    console.log(num);
});
```

### Callback Pattern for Async Operations

```javascript
// Node.js style: error-first callbacks
function readFile(filename, callback) {
    setTimeout(() => {
        const error = filename === "missing.txt" ? new Error("File not found") : null;
        const data = error ? null : "File contents here";
        callback(error, data);
    }, 100);
}

// Using the callback
readFile("test.txt", function(error, data) {
    if (error) {
        console.error("Error:", error.message);
        return;
    }
    console.log("Data:", data);
});
```

### Callback Hell (Pyramid of Doom)

```javascript
// Problems with nested callbacks
getUser(userId, function(error, user) {
    if (error) {
        handleError(error);
        return;
    }
    getOrders(user.id, function(error, orders) {
        if (error) {
            handleError(error);
            return;
        }
        getProducts(orders[0].productId, function(error, products) {
            if (error) {
                handleError(error);
                return;
            }
            getReviews(products[0].id, function(error, reviews) {
                if (error) {
                    handleError(error);
                    return;
                }
                // Finally use the data
                displayData(user, orders, products, reviews);
            });
        });
    });
});

// Problems:
// 1. Deeply nested - hard to read
// 2. Error handling at every level
// 3. Hard to maintain and debug
// 4. Can't easily run in parallel
```

--

## 3. Promises

A Promise represents a value that may be available now, in the future, or never.

### Promise States

```
┌──────────────────────────────────────────────────────────┐
│                      Promise States                       │
├──────────────────────────────────────────────────────────┤
│  pending  ─────────► fulfilled (resolved with value)    │
│              │                                           │
│              └────► rejected (rejected with reason)      │
│                                                          │
│  Once settled (fulfilled/rejected), cannot change        │
└──────────────────────────────────────────────────────────┘
```

### Creating Promises

```javascript
// Promise constructor
const promise = new Promise((resolve, reject) => {
    // Async operation
    setTimeout(() => {
        const success = Math.random() > 0.5;
        
        if (success) {
            resolve("Operation successful!");
        } else {
            reject(new Error("Operation failed!"));
        }
    }, 1000);
});

// Using the promise
promise
    .then(result => console.log(result))
    .catch(error => console.error(error.message));
```

### then(), catch(), finally()

```javascript
const promise = fetch("https://api.example.com/data");

// then() - handles success
promise.then(response => {
    console.log("Response received");
    return response.json();  // Returns new promise
});

// Chaining then()
promise
    .then(response => response.json())
    .then(data => {
        console.log("Data:", data);
        return processData(data);
    })
    .then(result => {
        console.log("Processed:", result);
    });

// catch() - handles errors (any error in chain)
promise
    .then(response => response.json())
    .then(data => {
        throw new Error("Processing error");
    })
    .catch(error => {
        console.error("Caught:", error.message);
    });

// finally() - runs regardless of outcome
promise
    .then(data => console.log(data))
    .catch(error => console.error(error))
    .finally(() => {
        console.log("Cleanup: hide loading spinner");
        // Does not receive any argument
        // Cannot change the result
    });
```

### Promise Chaining

```javascript
// Each then() returns a new promise
function fetchUser(userId) {
    return fetch(`/api/users/${userId}`)
        .then(response => response.json());
}

function fetchOrders(userId) {
    return fetch(`/api/orders?userId=${userId}`)
        .then(response => response.json());
}

function fetchProducts(orderId) {
    return fetch(`/api/products?orderId=${orderId}`)
        .then(response => response.json());
}

// Chained - much cleaner than callbacks
fetchUser(123)
    .then(user => {
        console.log("User:", user.name);
        return fetchOrders(user.id);
    })
    .then(orders => {
        console.log("Orders:", orders.length);
        return fetchProducts(orders[0].id);
    })
    .then(products => {
        console.log("Products:", products);
    })
    .catch(error => {
        console.error("Error anywhere in chain:", error);
    });
```

### Returning Values vs Promises

```javascript
// Returning a value - automatically wrapped in resolved promise
Promise.resolve(5)
    .then(n => n * 2)        // Returns 10, wrapped in Promise
    .then(n => n + 3)        // Returns 13
    .then(n => console.log(n)); // 13

// Returning a promise - chain waits for it
Promise.resolve(5)
    .then(n => {
        return new Promise(resolve => {
            setTimeout(() => resolve(n * 2), 1000);
        });
    })
    .then(n => console.log(n)); // 10 (after 1 second)

// Common mistake - not returning
Promise.resolve(5)
    .then(n => {
        fetch('/api/data');  // Not returned!
        return n;
    })
    .then(n => console.log(n)); // 5 (fetch result lost)
```

--

## 4. Promise Methods

### Promise.all()

Waits for ALL promises to fulfill, or rejects on first rejection.

```javascript
const promise1 = fetch('/api/users');
const promise2 = fetch('/api/products');
const promise3 = fetch('/api/orders');

Promise.all([promise1, promise2, promise3])
    .then(([users, products, orders]) => {
        // All three resolved
        console.log("All data received");
    })
    .catch(error => {
        // Any one rejected
        console.error("One failed:", error);
    });

// Practical example
async function fetchAllData() {
    const urls = [
        '/api/users',
        '/api/products',
        '/api/categories'
    ];
    
    try {
        const responses = await Promise.all(
            urls.map(url => fetch(url))
        );
        const data = await Promise.all(
            responses.map(r => r.json())
        );
        return data;
    } catch (error) {
        console.error("One request failed");
    }
}
```

### Promise.allSettled()

Waits for ALL promises to settle (fulfill OR reject).

```javascript
const promises = [
    Promise.resolve("Success 1"),
    Promise.reject(new Error("Failure")),
    Promise.resolve("Success 2")
];

Promise.allSettled(promises)
    .then(results => {
        results.forEach((result, index) => {
            if (result.status === 'fulfilled') {
                console.log(`${index}: ${result.value}`);
            } else {
                console.log(`${index}: ${result.reason.message}`);
            }
        });
    });

// Output:
// 0: Success 1
// 1: Failure
// 2: Success 2

// Always resolves, never rejects
// Useful when you want partial results
```

### Promise.race()

Returns first promise to settle (fulfill or reject).

```javascript
const slow = new Promise(resolve => 
    setTimeout(() => resolve("Slow"), 2000)
);
const fast = new Promise(resolve => 
    setTimeout(() => resolve("Fast"), 1000)
);

Promise.race([slow, fast])
    .then(result => console.log(result)); // "Fast"

// Practical: Timeout pattern
function fetchWithTimeout(url, timeout) {
    const fetchPromise = fetch(url);
    const timeoutPromise = new Promise((_, reject) => 
        setTimeout(() => reject(new Error("Timeout")), timeout)
    );
    
    return Promise.race([fetchPromise, timeoutPromise]);
}

fetchWithTimeout('/api/data', 5000)
    .then(response => response.json())
    .catch(error => console.error(error.message));
```

### Promise.any()

Returns first promise to fulfill (ignores rejections until all fail).

```javascript
const promises = [
    Promise.reject(new Error("Error 1")),
    Promise.resolve("Success"),
    Promise.reject(new Error("Error 2"))
];

Promise.any(promises)
    .then(result => console.log(result)) // "Success"
    .catch(error => console.error("All failed"));

// If ALL reject, throws AggregateError
const allFail = [
    Promise.reject(new Error("Error 1")),
    Promise.reject(new Error("Error 2"))
];

Promise.any(allFail)
    .catch(error => {
        console.log(error.errors); // [Error, Error]
    });
```

### Promise.resolve() & Promise.reject()

```javascript
// Create immediately resolved promise
const resolved = Promise.resolve("Success");
resolved.then(value => console.log(value)); // "Success"

// Useful for: starting chains, normalizing values
function maybeAsync(value) {
    if (typeof value.then === 'function') {
        return value; // Already a promise
    }
    return Promise.resolve(value); // Wrap in promise
}

// Create immediately rejected promise
const rejected = Promise.reject(new Error("Failed"));
rejected.catch(error => console.error(error.message)); // "Failed"
```

### Comparison Table

| Method | Waits For | Resolves When | Rejects When |
|----|------|--------|-------|
| `Promise.all` | All | All fulfill | Any rejects |
| `Promise.allSettled` | All | All settle | Never |
| `Promise.race` | First | First settles | First rejects |
| `Promise.any` | First success | First fulfills | All reject |

--

## 5. async/await

Syntactic sugar over Promises for cleaner async code.

### Basic Syntax

```javascript
// async function always returns a Promise
async function greet() {
    return "Hello!";
}
greet().then(msg => console.log(msg)); // "Hello!"

// await pauses execution until Promise resolves
async function fetchUser(id) {
    const response = await fetch(`/api/users/${id}`);
    const user = await response.json();
    return user;
}

// Equivalent with Promises
function fetchUserPromise(id) {
    return fetch(`/api/users/${id}`)
        .then(response => response.json());
}
```

### await Behavior

```javascript
// await with different values
async function examples() {
    // 1. With Promise - waits for resolution
    const result1 = await Promise.resolve("Resolved");
    console.log(result1);  // "Resolved"
    
    // 2. With non-Promise - returns immediately
    const result2 = await 42;
    console.log(result2);  // 42
    
    // 3. With rejected Promise - throws error
    try {
        const result3 = await Promise.reject(new Error("Failed"));
    } catch (error) {
        console.error(error.message);  // "Failed"
    }
}
```

### Sequential vs Parallel

```javascript
// SEQUENTIAL - one after another (slower)
async function sequential() {
    const user = await fetchUser(1);      // Wait 1 sec
    const orders = await fetchOrders(1);   // Wait 1 sec
    const products = await fetchProducts(1); // Wait 1 sec
    // Total: ~3 seconds
    return { user, orders, products };
}

// PARALLEL - all at once (faster)
async function parallel() {
    const [user, orders, products] = await Promise.all([
        fetchUser(1),
        fetchOrders(1),
        fetchProducts(1)
    ]);
    // Total: ~1 second (max of the three)
    return { user, orders, products };
}

// PARALLEL start, sequential await (NOT truly parallel)
async function notReallyParallel() {
    const userPromise = fetchUser(1);      // Starts
    const ordersPromise = fetchOrders(1);  // Starts
    const productsPromise = fetchProducts(1); // Starts
    
    const user = await userPromise;        // Wait
    const orders = await ordersPromise;    // Wait
    const products = await productsPromise; // Wait
    // This IS parallel! All started before any await
}
```

### async/await in Different Contexts

```javascript
// Async arrow function
const fetchData = async () => {
    const data = await fetch('/api/data');
    return data.json();
};

// Async method in class
class DataService {
    async fetch(url) {
        const response = await fetch(url);
        return response.json();
    }
}

// Async IIFE
(async () => {
    const data = await fetchData();
    console.log(data);
})();

// Top-level await (ES2022, modules only)
// In a .mjs file or type="module" script
const data = await fetch('/api/data');
console.log(await data.json());

// Async in forEach - DOESN'T wait!
const ids = [1, 2, 3];
ids.forEach(async (id) => {
    const data = await fetchUser(id);
    console.log(data); // Order not guaranteed
});

// Use for...of instead
for (const id of ids) {
    const data = await fetchUser(id);
    console.log(data); // Sequential, ordered
}

// Or Promise.all for parallel
const users = await Promise.all(
    ids.map(id => fetchUser(id))
);
```

--

## 6. Error Handling

### try/catch with async/await

```javascript
async function fetchUserData(userId) {
    try {
        const response = await fetch(`/api/users/${userId}`);
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        
        const data = await response.json();
        return data;
        
    } catch (error) {
        console.error("Fetch failed:", error.message);
        return null; // or throw error to propagate
    } finally {
        console.log("Cleanup");
    }
}
```

### Error Handling Patterns

```javascript
// Pattern 1: Wrapper function that catches errors
async function safeAsync(promise) {
    try {
        const data = await promise;
        return [data, null];
    } catch (error) {
        return [null, error];
    }
}

// Usage
const [user, error] = await safeAsync(fetchUser(1));
if (error) {
    console.error("Failed to fetch user");
}

// Pattern 2: Catch at the call site
async function getUser(id) {
    const response = await fetch(`/api/users/${id}`);
    return response.json();
}

getUser(1)
    .then(user => console.log(user))
    .catch(error => console.error(error));

// Pattern 3: Error with fallback
async function getUserWithFallback(id) {
    try {
        return await fetchUser(id);
    } catch {
        return { id, name: "Unknown" };
    }
}

// Pattern 4: Retry logic
async function fetchWithRetry(url, retries = 3) {
    for (let i = 0; i < retries; i++) {
        try {
            const response = await fetch(url);
            return response.json();
        } catch (error) {
            if (i === retries - 1) throw error;
            console.log(`Retry ${i + 1}...`);
            await delay(1000 * (i + 1)); // Exponential backoff
        }
    }
}
```

### Common Mistakes

```javascript
// Mistake 1: Unhandled rejection
async function broken() {
    const data = await fetch('/api'); // If this fails...
    // No try/catch, error propagates
}
broken(); // Unhandled promise rejection!

// Fix: Always handle or propagate
broken().catch(console.error);

// Mistake 2: catch swallowing errors
async function swallower() {
    try {
        await riskyOperation();
    } catch (error) {
        console.log("Something went wrong");
        // Error swallowed - caller doesn't know!
    }
}

// Fix: re-throw if needed
async function proper() {
    try {
        await riskyOperation();
    } catch (error) {
        console.error("Error:", error.message);
        throw error; // Propagate to caller
    }
}

// Mistake 3: try/catch only catches awaited rejections
async function incomplete() {
    try {
        fetch('/api'); // Not awaited - not caught!
        throw new Error("This IS caught");
    } catch (error) {
        console.log(error.message);
    }
}
```

--

## 7. Common Async Patterns

### Sequential Processing

```javascript
// Process array items one by one
async function processSequential(items) {
    const results = [];
    
    for (const item of items) {
        const result = await processItem(item);
        results.push(result);
    }
    
    return results;
}

// Using reduce (more functional)
async function processSequentialReduce(items) {
    return items.reduce(async (accPromise, item) => {
        const acc = await accPromise;
        const result = await processItem(item);
        return [...acc, result];
    }, Promise.resolve([]));
}
```

### Parallel with Concurrency Limit

```javascript
// Process N items at a time
async function processWithLimit(items, limit, processor) {
    const results = [];
    const executing = [];
    
    for (const item of items) {
        const promise = processor(item).then(result => {
            executing.splice(executing.indexOf(promise), 1);
            return result;
        });
        
        results.push(promise);
        executing.push(promise);
        
        if (executing.length >= limit) {
            await Promise.race(executing);
        }
    }
    
    return Promise.all(results);
}

// Usage
const urls = ['url1', 'url2', 'url3', 'url4', 'url5'];
const data = await processWithLimit(urls, 2, fetchUrl);
// Only 2 requests at a time
```

### Timeout Pattern

```javascript
function timeout(ms) {
    return new Promise((_, reject) => 
        setTimeout(() => reject(new Error('Timeout')), ms)
    );
}

async function fetchWithTimeout(url, ms) {
    return Promise.race([
        fetch(url),
        timeout(ms)
    ]);
}

// Using AbortController (better - cancels fetch)
async function fetchWithAbort(url, ms) {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), ms);
    
    try {
        const response = await fetch(url, { signal: controller.signal });
        return response;
    } finally {
        clearTimeout(timeoutId);
    }
}
```

### Polling Pattern

```javascript
async function poll(fn, interval, maxAttempts) {
    let attempts = 0;
    
    while (attempts < maxAttempts) {
        try {
            const result = await fn();
            if (result.status === 'complete') {
                return result;
            }
        } catch (error) {
            console.error('Poll error:', error);
        }
        
        attempts++;
        await delay(interval);
    }
    
    throw new Error('Max attempts reached');
}

// Usage
const result = await poll(
    () => fetch('/api/job/123').then(r => r.json()),
    2000,  // Check every 2 seconds
    30     // Max 30 attempts
);
```

### Debounced Async Function

```javascript
function debounceAsync(fn, delay) {
    let timeoutId;
    let currentPromise = null;
    
    return function(...args) {
        return new Promise((resolve, reject) => {
            clearTimeout(timeoutId);
            
            timeoutId = setTimeout(async () => {
                try {
                    const result = await fn.apply(this, args);
                    resolve(result);
                } catch (error) {
                    reject(error);
                }
            }, delay);
        });
    };
}

// Usage
const debouncedSearch = debounceAsync(async (term) => {
    const response = await fetch(`/api/search?q=${term}`);
    return response.json();
}, 300);
```

--

## 8. Microtasks vs Macrotasks

### Execution Order

```javascript
console.log("1: Script start");

setTimeout(() => {
    console.log("2: setTimeout");
}, 0);

Promise.resolve()
    .then(() => console.log("3: Promise 1"))
    .then(() => console.log("4: Promise 2"));

queueMicrotask(() => {
    console.log("5: queueMicrotask");
});

console.log("6: Script end");

// Output:
// 1: Script start
// 6: Script end
// 3: Promise 1
// 5: queueMicrotask
// 4: Promise 2
// 2: setTimeout
```

### Classification

| Microtasks | Macrotasks |
|------|------|
| Promise callbacks (.then, .catch, .finally) | setTimeout |
| queueMicrotask() | setInterval |
| MutationObserver | setImmediate (Node.js) |
| process.nextTick() (Node.js - actually runs before microtasks) | I/O operations |
| | requestAnimationFrame |
| | UI rendering events |

### Complex Example

```javascript
async function asyncFunc() {
    console.log("A: Inside async function");
    await Promise.resolve();
    console.log("B: After await");
}

console.log("1: Start");

setTimeout(() => console.log("2: Timeout 1"), 0);

asyncFunc();

new Promise(resolve => {
    console.log("3: Promise executor");
    resolve();
}).then(() => {
    console.log("4: Promise then");
});

setTimeout(() => console.log("5: Timeout 2"), 0);

console.log("6: End");

// Output:
// 1: Start
// A: Inside async function
// 3: Promise executor
// 6: End
// B: After await
// 4: Promise then
// 2: Timeout 1
// 5: Timeout 2
```

--

## 9. Complex Technical Scenarios

### Scenario 1: What is the output?

```javascript
console.log('Start');

setTimeout(() => console.log('Timeout'), 0);

Promise.resolve('Promise').then(console.log);

console.log('End');

// Answer:
// Start
// End
// Promise
// Timeout

// Explanation:
// 1. Sync: Start, End
// 2. Microtask: Promise
// 3. Macrotask: Timeout
```

### Scenario 2: What is the output?

```javascript
const promise = new Promise((resolve, reject) => {
    console.log(1);
    resolve();
    console.log(2);
    reject();
    console.log(3);
});

promise
    .then(() => console.log(4))
    .catch(() => console.log(5));

console.log(6);

// Answer: 1, 2, 3, 6, 4

// Explanation:
// - Promise executor runs synchronously
// - resolve() queues the .then callback
// - Code after resolve/reject still runs
// - Second resolve/reject is ignored
// - 6 is sync, 4 is microtask
```

### Scenario 3: What is the output?

```javascript
async function async1() {
    console.log('async1 start');
    await async2();
    console.log('async1 end');
}

async function async2() {
    console.log('async2');
}

console.log('script start');
async1();
console.log('script end');

// Answer:
// script start
// async1 start
// async2
// script end
// async1 end

// Explanation:
// - async1 runs until await
// - async2 is called synchronously
// - await pauses async1, returns to main
// - "script end" prints
// - Microtask: "async1 end"
```

### Scenario 4: What is the output?

```javascript
setTimeout(() => console.log(1), 0);

new Promise((resolve) => {
    console.log(2);
    resolve();
}).then(() => {
    console.log(3);
    Promise.resolve().then(() => console.log(4));
}).then(() => console.log(5));

console.log(6);

// Answer: 2, 6, 3, 4, 5, 1

// Explanation:
// Sync: 2, 6
// Microtask queue after sync: [3]
// After 3 runs, adds: [4, 5]
// Microtasks: 3, 4, 5
// Macrotask: 1
```

### Scenario 5: What is the output?

```javascript
let a = 0;
let test = async () => {
    a = a + await 10;
    console.log(a);
};
test();
console.log(++a);

// Answer: 1, 10

// Explanation:
// 1. test() starts, a = 0, `a + await 10` captures a=0
// 2. await pauses test()
// 3. ++a makes a = 1
// 4. console.log(++a) prints 1
// 5. test resumes: 0 + 10 = 10, a = 10
// 6. console.log(a) prints 10
```

### Scenario 6: Promise finally timing

```javascript
Promise.resolve('Success')
    .then(result => {
        console.log(result);
        return 'Then';
    })
    .finally(() => {
        console.log('Finally');
        return 'Finally Return';
    })
    .then(result => {
        console.log(result);
    });

// Answer:
// Success
// Finally
// Then

// Explanation:
// - finally doesn't receive or change value
// - finally's return is ignored
// - Chain continues with previous value
```

--

## 10. Coding Problems

### Problem 1: Implement Promise.all

```javascript
function myPromiseAll(promises) {
    return new Promise((resolve, reject) => {
        if (!Array.isArray(promises)) {
            return reject(new TypeError('Argument must be an array'));
        }
        
        const results = [];
        let completed = 0;
        
        if (promises.length === 0) {
            return resolve([]);
        }
        
        promises.forEach((promise, index) => {
            Promise.resolve(promise)
                .then(value => {
                    results[index] = value;
                    completed++;
                    
                    if (completed === promises.length) {
                        resolve(results);
                    }
                })
                .catch(reject);
        });
    });
}

// Test
myPromiseAll([
    Promise.resolve(1),
    Promise.resolve(2),
    Promise.resolve(3)
]).then(console.log); // [1, 2, 3]
```

### Problem 2: Implement Promise.race

```javascript
function myPromiseRace(promises) {
    return new Promise((resolve, reject) => {
        if (!Array.isArray(promises)) {
            return reject(new TypeError('Argument must be an array'));
        }
        
        promises.forEach(promise => {
            Promise.resolve(promise)
                .then(resolve)
                .catch(reject);
        });
    });
}
```

### Problem 3: Implement delay function

```javascript
function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

// With value
function delayWithValue(ms, value) {
    return new Promise(resolve => setTimeout(() => resolve(value), ms));
}

// Usage
async function test() {
    console.log('Start');
    await delay(1000);
    console.log('After 1 second');
    
    const value = await delayWithValue(500, 'Hello');
    console.log(value); // 'Hello' after 500ms
}
```

### Problem 4: Implement retry with exponential backoff

```javascript
async function retry(fn, maxRetries = 3, baseDelay = 1000) {
    let lastError;
    
    for (let attempt = 0; attempt < maxRetries; attempt++) {
        try {
            return await fn();
        } catch (error) {
            lastError = error;
            console.log(`Attempt ${attempt + 1} failed`);
            
            if (attempt < maxRetries - 1) {
                const delay = baseDelay * Math.pow(2, attempt);
                console.log(`Retrying in ${delay}ms`);
                await new Promise(r => setTimeout(r, delay));
            }
        }
    }
    
    throw lastError;
}

// Usage
const result = await retry(
    () => fetch('/api/unstable').then(r => r.json()),
    3,
    1000
);
```

### Problem 5: Promisify callback-based function

```javascript
function promisify(fn) {
    return function(...args) {
        return new Promise((resolve, reject) => {
            fn(...args, (error, result) => {
                if (error) {
                    reject(error);
                } else {
                    resolve(result);
                }
            });
        });
    };
}

// Usage
const fs = require('fs');
const readFileAsync = promisify(fs.readFile);

readFileAsync('file.txt', 'utf8')
    .then(content => console.log(content))
    .catch(error => console.error(error));
```

### Problem 6: Execute promises in sequence

```javascript
// Run array of promise-returning functions sequentially
async function sequence(promiseFns) {
    const results = [];
    
    for (const fn of promiseFns) {
        results.push(await fn());
    }
    
    return results;
}

// Using reduce
function sequenceReduce(promiseFns) {
    return promiseFns.reduce(
        (chain, fn) => chain.then(results => 
            fn().then(result => [...results, result])
        ),
        Promise.resolve([])
    );
}

// Usage
const tasks = [
    () => delay(1000).then(() => 'First'),
    () => delay(500).then(() => 'Second'),
    () => delay(200).then(() => 'Third')
];

sequence(tasks).then(console.log); // ['First', 'Second', 'Third']
```

### Problem 7: Implement async queue with concurrency

```javascript
class AsyncQueue {
    constructor(concurrency = 1) {
        this.concurrency = concurrency;
        this.running = 0;
        this.queue = [];
    }
    
    add(task) {
        return new Promise((resolve, reject) => {
            this.queue.push({ task, resolve, reject });
            this.process();
        });
    }
    
    async process() {
        while (this.running < this.concurrency && this.queue.length > 0) {
            const { task, resolve, reject } = this.queue.shift();
            this.running++;
            
            try {
                const result = await task();
                resolve(result);
            } catch (error) {
                reject(error);
            } finally {
                this.running-;
                this.process();
            }
        }
    }
}

// Usage
const queue = new AsyncQueue(2); // Max 2 concurrent

queue.add(() => fetch('/api/1').then(r => r.json()));
queue.add(() => fetch('/api/2').then(r => r.json()));
queue.add(() => fetch('/api/3').then(r => r.json())); // Waits
```

--

## 11. Key Topics & Explanations

### Topic 1: What is the Event Loop?

**Answer:** The Event Loop is JavaScript's mechanism for handling asynchronous operations despite being single-threaded. It continuously checks if the call stack is empty, then moves callbacks from the task queues to the stack. Order: Synchronous code → All microtasks (Promises) → One macrotask (setTimeout) → Repeat. This allows non-blocking I/O operations.

--

### Topic 2: What is the difference between Promises and Callbacks?

**Answer:** Callbacks are functions passed to async functions, leading to "callback hell" with nesting. Promises represent future values with `.then()/.catch()` chaining, better error handling, and avoid deep nesting. Promises are composable (Promise.all, race) and can be converted between sync/async. async/await makes Promise code look synchronous.

--

### Topic 3: What is the difference between Promise.all and Promise.allSettled?

**Answer:** `Promise.all` rejects immediately if ANY promise rejects - you lose other results. `Promise.allSettled` waits for ALL promises to settle and returns an array of `{status, value/reason}` objects, never rejects. Use `all` when you need all-or-nothing, `allSettled` when you want partial results even if some fail.

--

### Topic 4: What is the difference between microtasks and macrotasks?

**Answer:** Microtasks (Promise callbacks, queueMicrotask) run after the current script and before any macrotasks. Macrotasks (setTimeout, setInterval, I/O) run one at a time with the DOM potentially rendered between them. After each macrotask, ALL pending microtasks run. This is why Promise.then() always runs before setTimeout, even with 0ms delay.

--

### Topic 5: What happens when you don't handle a Promise rejection?

**Answer:** An unhandled rejection triggers `unhandledrejection` event (browser) or `process.on('unhandledRejection')` (Node.js). In Node.js 15+, it terminates the process. Always add `.catch()` to promise chains or use try/catch with async/await. You can set a global handler, but it's better to handle errors explicitly.

--

### Topic 6: Can you explain async/await error handling?

**Answer:** async functions use try/catch blocks instead of `.catch()`. Any rejected promise within try block is caught by catch. Without try/catch, the async function returns a rejected promise. Common mistakes: forgetting to await (rejection not caught), catching too broadly (swallowing errors). Use try/catch for await, or `.catch()` at the call site.

--

### Topic 7: How do you run async operations in parallel vs sequential?

**Answer:** **Sequential:** `await` in a loop - each waits for previous. **Parallel:** `Promise.all([...])` - all run simultaneously. For parallel with concurrency limit, use a queue/pool pattern. Common mistake: `forEach` with async - appears to work but doesn't wait. Use `for...of` for sequential or `Promise.all(array.map())` for parallel.

--

### Topic 8: What is callback hell and how do you solve it?

**Answer:** Callback hell is deeply nested callbacks forming a "pyramid of doom." Solutions: (1) Named functions instead of anonymous (2) Promises with chaining (3) async/await for synchronous-looking code (4) Modularize into smaller functions. async/await is now the preferred solution as it's most readable and maintainable.

--

### Topic 9: Why does setTimeout(..., 0) not run immediately?

**Answer:** setTimeout schedules a macrotask, not immediate execution. Even with 0ms, the callback goes to the macrotask queue and waits for: (1) Current synchronous code to complete (2) All microtasks to complete. Only then does the Event Loop check macrotasks. Minimum actual delay is ~4ms in browsers due to implementation limits.

--

### Topic 10: How does await work under the hood?

**Answer:** `await` pauses the async function, wrapping the right-hand value in Promise.resolve(). The function returns a pending Promise to its caller. The remainder of the function becomes a microtask continuation. When the awaited promise resolves, the continuation runs. This is syntactic sugar over `.then()` - `await x; code()` equals `Promise.resolve(x).then(() => code())`.

--

## Quick Reference Cheat Sheet

```
Event Loop Order:
1. Call Stack (synchronous code)
2. All Microtasks (Promises, queueMicrotask)
3. One Macrotask (setTimeout, setInterval, I/O)
4. Repeat from step 2

Promise States:
pending → fulfilled (with value)
pending → rejected (with reason)
(cannot change once settled)

Promise Methods:
├── .then(onFulfilled, onRejected) - chain handling
├── .catch(onRejected) - error handling
├── .finally(onFinally) - cleanup
├── Promise.all([...]) - all must succeed
├── Promise.allSettled([...]) - wait for all
├── Promise.race([...]) - first to settle
├── Promise.any([...]) - first to succeed
├── Promise.resolve(value) - create resolved
└── Promise.reject(reason) - create rejected

async/await:
├── async function returns Promise
├── await pauses until Promise resolves
├── try/catch for error handling
├── Sequential: for...of with await
└── Parallel: Promise.all([...])

Common Patterns:
├── Timeout: Promise.race([fetch, timeout])
├── Retry: loop with catch and delay
├── Polling: while loop with delay
├── Queue: limit concurrent operations
└── Debounce: clearTimeout before setTimeout

Common Mistakes:
├── Forgetting await (fires and forgets)
├── forEach with async (doesn't wait)
├── Not handling rejections
├── Swallowing errors in catch
└── Sequential when parallel is possible
```

--

> **Tip:** Event Loop and Promise execution order are **#1 most asked** async topics in WITCH technical reviews. Practice predicting output of code mixing setTimeout, Promises, and async/await. Know the difference between microtasks and macrotasks. Understand why forEach doesn't work with async and how to fix it.
