# JavaScript Performance, Security & Optimization - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Memory Management, Performance Optimization, Security, Web APIs, Storage, and tricky scenarios.

---

## Table of Contents

1. [Memory Management & Garbage Collection](#memory-management-garbage-collection)
2. [Performance Optimization](#performance-optimization)
3. [Web Security](#web-security)
4. [Web Storage APIs](#web-storage-apis)
5. [Web Workers](#web-workers)
6. [Service Workers & PWA](#service-workers-pwa)
7. [Complex Technical Scenarios](#complex-technical-scenarios)
8. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. Memory Management & Garbage Collection

### How JS Memory Works

```
┌─────────────────────────────────────────┐
│           JavaScript Memory              │
├─────────────────┬───────────────────────┤
│      Stack      │         Heap           │
├─────────────────┼───────────────────────┤
│ Primitives      │ Objects               │
│ References      │ Arrays                │
│ Function calls  │ Functions             │
│ Fixed size      │ Dynamic size          │
│ LIFO            │ Unstructured          │
└─────────────────┴───────────────────────┘
```

### Garbage Collection — Mark and Sweep

```javascript
// Objects are collected when no references point to them

// 1. Reference counting (basic — has circular reference problem)
let obj1 = { name: "Rahul" };
let obj2 = obj1;  // Two references to same object
obj1 = null;      // One reference removed
obj2 = null;      // Last reference removed → object eligible for GC

// 2. Mark and sweep (modern — handles circular references)
// GC starts from "roots" (global, local vars) and marks reachable objects
// Unreachable objects are swept (collected)
```

### Common Memory Leaks

```javascript
// 1. Accidental Global Variables
function leak() {
    globalVar = "I'm global!"; // Missing 'let/const' → global
}

// 2. Forgotten Timers
const id = setInterval(() => {
    // This closure keeps references alive
    processData(heavyObject);
}, 1000);
// Fix: clearInterval(id) when no longer needed

// 3. Detached DOM References
const elements = [];
function addElement() {
    const div = document.createElement("div");
    document.body.appendChild(div);
    elements.push(div); // Reference kept in array
}
function removeElement() {
    const div = elements.pop();
    document.body.removeChild(div);
    // div is removed from DOM but still in 'elements' array → leak
}

// 4. Closures holding large data
function createHandler() {
    const largeData = new Array(1000000).fill("x"); // 1M items
    
    return function handler() {
        console.log(largeData.length); // Closure keeps largeData alive
    };
}

// 5. Event Listeners not removed
const button = document.getElementById("btn");
function onClick() { /* ... */ }
button.addEventListener("click", onClick);
// Fix: button.removeEventListener("click", onClick);
```

### WeakRef & FinalizationRegistry (ES2021)

```javascript
// WeakRef — does not prevent GC
let obj = { name: "Rahul" };
const weakRef = new WeakRef(obj);

console.log(weakRef.deref()); // { name: "Rahul" }
obj = null; // Object can now be GC'd
// weakRef.deref() may return undefined after GC

// FinalizationRegistry — callback when object is GC'd
const registry = new FinalizationRegistry((value) => {
    console.log(`${value} was garbage collected`);
});

let user = { name: "Amit" };
registry.register(user, "User Amit");
user = null; // Eventually: "User Amit was garbage collected"
```

---

## 2. Performance Optimization

### Debounce & Throttle

```javascript
// DEBOUNCE — Wait until user stops for N ms, then execute ONCE
// Use case: Search input, window resize
function debounce(fn, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => fn.apply(this, args), delay);
    };
}

const searchInput = debounce((query) => {
    fetch(`/api/search?q=${query}`);
}, 300);

// THROTTLE — Execute at most once every N ms
// Use case: Scroll events, button clicks
function throttle(fn, limit) {
    let inThrottle = false;
    return function (...args) {
        if (!inThrottle) {
            fn.apply(this, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

const onScroll = throttle(() => {
    console.log("Scroll position:", window.scrollY);
}, 200);
```

### Lazy Loading

```javascript
// Images — native lazy loading
// <img src="photo.jpg" loading="lazy" alt="Photo">

// Intersection Observer — lazy load anything
const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            const img = entry.target;
            img.src = img.dataset.src; // Load actual image
            observer.unobserve(img);    // Stop observing
        }
    });
}, { rootMargin: "200px" }); // Start loading 200px before viewport

document.querySelectorAll("img[data-src]").forEach(img => {
    observer.observe(img);
});
```

### requestAnimationFrame

```javascript
// NEVER use setInterval for animations
// ❌ Bad
setInterval(() => {
    element.style.left = pos++ + "px";
}, 16); // Tries to match 60fps but jitters

// ✅ Good — syncs with display refresh rate
function animate() {
    element.style.left = pos++ + "px";
    if (pos < 300) {
        requestAnimationFrame(animate); // ~60fps, battery efficient
    }
}
requestAnimationFrame(animate);
```

### Virtual Scrolling (Concept)

```javascript
// Instead of rendering 10,000 DOM elements:
// Only render visible items (~20-30)
// As user scrolls, recycle DOM elements with new data
// Libraries: react-virtualized, tanstack-virtual
```

### Code Splitting & Dynamic Import

```javascript
// Static import — loaded at initial bundle (everything upfront)
import { heavyModule } from "./heavy.js";

// Dynamic import — loaded on demand (code splitting)
button.addEventListener("click", async () => {
    const { heavyModule } = await import("./heavy.js");
    heavyModule.process();
});
```

---

## 3. Web Security

### XSS (Cross-Site Scripting)

```javascript
// ATTACK: Injecting malicious scripts via user input
// User enters: <script>stealCookies()</script> in a form field
// If rendered as HTML, the script executes

// PREVENTION:
// 1. Sanitize/Escape output
function escapeHTML(str) {
    const div = document.createElement("div");
    div.textContent = str; // textContent auto-escapes
    return div.innerHTML;
}

// 2. Use textContent instead of innerHTML
element.textContent = userInput;    // ✅ Safe
// element.innerHTML = userInput;   // ❌ Dangerous

// 3. Content Security Policy (CSP) header
// Content-Security-Policy: script-src 'self'

// 4. HttpOnly cookies — JS cannot access
// Set-Cookie: session=abc; HttpOnly; Secure
```

### CSRF (Cross-Site Request Forgery)

```javascript
// ATTACK: Tricking user into making unwanted requests
// Malicious site has: <img src="https://bank.com/transfer?to=attacker&amount=1000">

// PREVENTION:
// 1. CSRF Token — unique token per session, validated on server
// 2. SameSite Cookie attribute
// Set-Cookie: session=abc; SameSite=Strict

// 3. Check Origin/Referer headers on server
```

### CORS (Cross-Origin Resource Sharing)

```javascript
// Browser blocks cross-origin requests by default
// Server must explicitly allow it

// Server response headers:
// Access-Control-Allow-Origin: https://myapp.com
// Access-Control-Allow-Methods: GET, POST, PUT
// Access-Control-Allow-Headers: Content-Type, Authorization
// Access-Control-Allow-Credentials: true

// Preflight request (OPTIONS) — sent for complex requests
// Browser automatically sends OPTIONS before actual request
```

### Clickjacking Prevention

```javascript
// ATTACK: Invisible iframe overlaying legitimate page
// PREVENTION:
// 1. X-Frame-Options: DENY (HTTP header)
// 2. Content-Security-Policy: frame-ancestors 'none'
// 3. JavaScript frame-busting:
if (window.top !== window.self) {
    window.top.location = window.self.location;
}
```

### Input Validation

```javascript
// Always validate on BOTH client AND server

// Client-side (UX only — not security)
function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// Sanitize before use
function sanitize(input) {
    return input.replace(/[<>&"'/]/g, (char) => ({
        '<': '&lt;',
        '>': '&gt;',
        '&': '&amp;',
        '"': '&quot;',
        "'": '&#39;',
        '/': '&#x2F;'
    }[char]));
}

// SQL Injection prevention — use parameterized queries
// ❌ db.query(`SELECT * FROM users WHERE name = '${userInput}'`);
// ✅ db.query("SELECT * FROM users WHERE name = $1", [userInput]);
```

---

## 4. Web Storage APIs

### localStorage vs sessionStorage vs Cookies

| Feature | localStorage | sessionStorage | Cookies |
|---------|-------------|----------------|---------|
| **Capacity** | ~5-10 MB | ~5-10 MB | ~4 KB |
| **Expiry** | Never (manual clear) | Tab close | Configurable |
| **Scope** | Per origin (all tabs) | Per tab | Per origin + path |
| **Sent with requests** | ❌ No | ❌ No | ✅ Yes (every request) |
| **Access** | JS only | JS only | JS + Server |
| **Use case** | User preferences | Form data, wizard | Auth tokens, tracking |

### Usage

```javascript
// localStorage
localStorage.setItem("theme", "dark");
const theme = localStorage.getItem("theme"); // "dark"
localStorage.removeItem("theme");
localStorage.clear(); // Remove all

// Storing objects (must stringify)
const user = { name: "Rahul", age: 25 };
localStorage.setItem("user", JSON.stringify(user));
const loaded = JSON.parse(localStorage.getItem("user"));

// sessionStorage — same API, cleared on tab close
sessionStorage.setItem("token", "abc123");

// Storage event — detect changes from OTHER tabs
window.addEventListener("storage", (event) => {
    console.log(`Key: ${event.key}, Old: ${event.oldValue}, New: ${event.newValue}`);
});
```

### IndexedDB (Brief)

```javascript
// For large structured data (offline apps, caching)
// Transactional, async, supports indexes
// Capacity: 50MB+ (browser-dependent)
// More complex API than localStorage — consider using Dexie.js wrapper
```

---

## 5. Web Workers

Run JavaScript in background threads — no UI blocking.

```javascript
// main.js
const worker = new Worker("worker.js");

// Send data to worker
worker.postMessage({ numbers: [1, 2, 3, 4, 5] });

// Receive result from worker
worker.onmessage = (event) => {
    console.log("Sum:", event.data.sum); // Computed in background
};

worker.onerror = (error) => {
    console.error("Worker error:", error.message);
};

// Terminate worker
worker.terminate();

// worker.js
self.onmessage = (event) => {
    const sum = event.data.numbers.reduce((a, b) => a + b, 0);
    self.postMessage({ sum });
};
```

### Shared Workers

```javascript
// Shared across multiple tabs/windows of same origin
const shared = new SharedWorker("shared-worker.js");
shared.port.start();
shared.port.postMessage("Hello from tab");
shared.port.onmessage = (e) => console.log(e.data);
```

### Worker Limitations

- **No DOM access** (`document`, `window` unavailable)
- **No direct variable sharing** (communication via `postMessage`)
- **Separate global scope** (`self` instead of `window`)
- **Same-origin policy** applies

---

## 6. Service Workers & PWA

```javascript
// Register service worker
if ("serviceWorker" in navigator) {
    navigator.serviceWorker.register("/sw.js")
        .then(reg => console.log("SW registered"))
        .catch(err => console.error("SW failed:", err));
}

// sw.js — Cache-first strategy
const CACHE_NAME = "v1";
const ASSETS = ["/", "/index.html", "/styles.css", "/app.js"];

// Install — cache assets
self.addEventListener("install", (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME).then(cache => cache.addAll(ASSETS))
    );
});

// Fetch — serve from cache, fallback to network
self.addEventListener("fetch", (event) => {
    event.respondWith(
        caches.match(event.request)
            .then(cached => cached || fetch(event.request))
    );
});
```

---

## 7. Complex Technical Scenarios

### Topic 1: How to detect memory leaks?

**Explanation:**
1. Chrome DevTools → Memory tab → Take Heap Snapshot
2. Compare snapshots before/after action
3. Look for growing "Detached DOM trees" or increasing heap size
4. Performance tab → Record → Check if memory keeps growing over time

### Topic 2: What is tree shaking?

**Explanation:** Dead code elimination. Build tools (Webpack, Rollup) analyze `import/export` statements and remove unused exports from the final bundle. Only works with ES Modules (`import/export`), NOT CommonJS (`require`).

### Topic 3: What is the difference between Server-Side Rendering (SSR) and Client-Side Rendering (CSR)?

**Explanation:**
- **CSR**: Browser downloads empty HTML + JS bundle → JS renders UI. Slow initial load, good for SPAs.
- **SSR**: Server renders HTML with data → sends complete HTML to browser. Fast initial load, better SEO.
- **SSG (Static Site Generation)**: HTML generated at build time. Fastest for static content.
- **ISR (Incremental Static Regeneration)**: SSG + periodic revalidation (Next.js).

### Topic 4: What is CORS preflight?

**Explanation:** For "complex" requests (non-GET with custom headers or non-standard Content-Type), the browser automatically sends an `OPTIONS` request first to check if the server allows the actual request. The server must respond with appropriate `Access-Control-Allow-*` headers.

---

## 8. Key Topics & Explanations

### Topic 1: What causes a Memory Leak in JavaScript?

**Explanation:** Objects that are no longer needed but are still referenced, preventing Garbage Collection.
Most common causes:
1. Global variables
2. Forgotten timers (setInterval)
3. Closures holding large objects
4. Detached DOM nodes
5. Event listeners not removed

### Topic 2: localStorage vs Cookies — when to use which?

**Explanation:**
- **Cookies**: When data needs to be sent to server with every request (auth tokens). Small capacity.
- **localStorage**: Client-only data (preferences, theme). Large capacity. Never sent to server.

### Topic 3: Why are Web Workers useful?

**Explanation:** JavaScript is single-threaded. Heavy computation blocks the UI (stops responding). Web Workers run code in a separate thread, keeping the UI responsive. Example: Image processing, data parsing, complex calculations.

### Topic 4: What is a Content Security Policy (CSP)?

**Explanation:** HTTP header that controls which resources the browser can load. Prevents XSS by allowing only trusted script sources.
```
Content-Security-Policy: 
    script-src 'self' https://cdn.example.com;
    style-src 'self' 'unsafe-inline';
    img-src *;
```
