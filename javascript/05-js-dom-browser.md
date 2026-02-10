# JavaScript DOM & Browser APIs - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** DOM Tree, Event Propagation, Event Delegation, Browser Storage, Fetch API, and critical browser concepts.

---

## Table of Contents

1. [DOM Manipulation](#dom-manipulation)
2. [Event Handling & Propagation](#event-handling-propagation)
3. [Event Delegation](#event-delegation)
4. [Browser Storage (Local/Session/Cookies)](#browser-storage-localsessioncookies)
5. [Fetch API & AJAX](#fetch-api-ajax)
6. [BOM (Browser Object Model)](#bom-browser-object-model)
7. [Critical Rendering Path](#critical-rendering-path)
8. [Debounce & Throttle (Implementation)](#debounce-throttle-implementation)
9. [Coding Problems](#coding-problems)
10. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. DOM Manipulation

### 1.1 What is the DOM?

The **Document Object Model (DOM)** is a tree-like representation of your HTML page that JavaScript can read and modify. Think of it like this:

> **Analogy:** Your HTML file is like a blueprint of a house. The DOM is the **actual house** built from that blueprint. JavaScript is the **contractor** who can walk through the house, add rooms, change wall colors, or remove furniture — all without rewriting the blueprint.

When the browser loads an HTML page, it creates a DOM tree:

```
document
 └── html
      ├── head
      │    └── title
      └── body
           ├── h1
           ├── div#container
           │    ├── p.intro
           │    └── ul
           │         ├── li
           │         ├── li
           │         └── li
           └── footer
```

Every HTML tag becomes a **node** in this tree. JavaScript interacts with these nodes to make pages dynamic.

### 1.2 Selecting Elements

Before you can change anything on a page, you need to **find** the element first. There are two families of methods:

```javascript
// ══════════════════════════════════════════════
// OLD METHODS (Still work, but less flexible)
// ══════════════════════════════════════════════

document.getElementById('header');           // Returns ONE element by ID
document.getElementsByClassName('item');      // Returns HTMLCollection (live)
document.getElementsByTagName('div');         // Returns HTMLCollection (live)

// ══════════════════════════════════════════════
// MODERN METHODS (Recommended — use CSS selectors)
// ══════════════════════════════════════════════

document.querySelector('#header');           // Returns FIRST match (any CSS selector)
document.querySelector('.item');             // First element with class "item"
document.querySelector('div > p.intro');     // Complex selectors work too!

document.querySelectorAll('.item');          // Returns ALL matches as NodeList (static)
document.querySelectorAll('ul li');          // All <li> inside <ul>
```

**Key Difference — Live vs Static:**

| Type | Returns | Updates Automatically? | Example |
|------|---------|----------------------|---------|
| `getElementsByClassName` | HTMLCollection | ✅ Yes (Live) — if DOM changes, collection updates | Add a new `.item` div → collection grows |
| `querySelectorAll` | NodeList | ❌ No (Static) — snapshot at time of call | Add a new `.item` div → NodeList stays same |

```javascript
// Practical difference:
const liveList = document.getElementsByClassName('item');  // Live
const staticList = document.querySelectorAll('.item');      // Static

console.log(liveList.length);  // 3
console.log(staticList.length); // 3

// Now add a new element with class "item" to the DOM
document.body.innerHTML += '<div class="item">New</div>';

console.log(liveList.length);  // 4 ← Updated automatically!
console.log(staticList.length); // 3 ← Still 3 (snapshot)
```

> **When to use which?** Use `querySelector`/`querySelectorAll` 99% of the time. They accept any CSS selector, making them far more powerful and flexible.

### 1.3 Modifying Elements

Once you have a reference to an element, you can change its content, styles, and attributes:

```javascript
const el = document.querySelector('.box');

// ══════════════════════════════════════════════
// CHANGING CONTENT
// ══════════════════════════════════════════════

// textContent — Sets/gets raw text (Safest, Fastest)
el.textContent = 'Hello World';
// Result: <div class="box">Hello World</div>

// innerHTML — Sets/gets HTML string (⚠️ XSS Risk if using user input!)
el.innerHTML = '<b>Hello</b> <span>World</span>';
// Result: <div class="box"><b>Hello</b> <span>World</span></div>

// innerText — Sets/gets visible text (CSS-aware)
// If a child has display:none, innerText won't include it
// Also triggers a "reflow" (browser recalculates layout) — slower
el.innerText = 'Hello World';

// ══════════════════════════════════════════════
// CHANGING STYLES
// ══════════════════════════════════════════════

// Inline styles (camelCase for CSS properties)
el.style.color = 'red';
el.style.backgroundColor = '#ccc';     // background-color → backgroundColor
el.style.fontSize = '16px';             // font-size → fontSize
el.style.display = 'none';              // Hide element

// CSS Classes (Better than inline styles — keeps styles in CSS)
el.classList.add('active');              // Add a class
el.classList.remove('active');           // Remove a class
el.classList.toggle('visible');          // Add if missing, remove if present
el.classList.contains('active');         // Check if class exists → true/false
el.classList.replace('old', 'new');      // Replace one class with another

// ══════════════════════════════════════════════
// CHANGING ATTRIBUTES
// ══════════════════════════════════════════════

el.setAttribute('data-id', '123');       // Set any attribute
el.getAttribute('data-id');              // Get attribute value → "123"
el.removeAttribute('data-id');           // Remove attribute
el.hasAttribute('data-id');              // Check if exists → true/false

// data-* attributes have a special shortcut
el.dataset.role = 'admin';              // Sets data-role="admin"
console.log(el.dataset.role);           // "admin"
el.dataset.userId = '42';               // Sets data-user-id="42" (auto-converts camelCase)
```

> **Why prefer classList over inline styles?** Inline styles have the highest CSS specificity and are hard to override. Using classes keeps your styling logic in CSS files where it belongs.

### 1.4 Creating & Removing Elements

**Bad Practice — Reflow Thrashing:**

Every time you modify `innerHTML`, the browser has to **re-parse** the HTML, **rebuild** part of the DOM tree, and **recalculate** the layout. Doing this in a loop is extremely slow:

```javascript
const list = document.querySelector('ul');

// ❌ BAD — This triggers layout recalculation 100 times!
for (let i = 0; i < 100; i++) {
    list.innerHTML += `<li>Item ${i}</li>`;
    // Each iteration: browser parses ALL existing HTML + new HTML again
}
```

**Good Practice — Document Fragment:**

A `DocumentFragment` is an invisible container that lives in memory. You build your elements there first, then insert everything into the actual DOM in **one shot**:

```javascript
const list = document.querySelector('ul');
const fragment = document.createDocumentFragment();

// ✅ GOOD — All work happens in memory
for (let i = 0; i < 100; i++) {
    const li = document.createElement('li');
    li.textContent = `Item ${i}`;
    fragment.appendChild(li);  // Append to invisible in-memory container
}

list.appendChild(fragment);  // ONE single DOM update → ONE reflow!
```

> **Analogy:** Instead of delivering 100 packages to a house one at a time (100 trips), you load all 100 packages into a truck first (fragment), then deliver them all at once (1 trip).

**Other Common DOM Operations:**

```javascript
// Create elements
const div = document.createElement('div');
div.textContent = 'Hello';
div.className = 'card';

// Add to DOM
document.body.appendChild(div);           // Add as last child
document.body.prepend(div);               // Add as first child
someElement.before(div);                   // Add before someElement
someElement.after(div);                    // Add after someElement
someElement.replaceWith(div);              // Replace someElement with div

// Remove from DOM
div.remove();                              // Modern — removes itself
// parent.removeChild(child);              // Old way — needs parent reference

// Clone
const clone = div.cloneNode(true);         // true = deep clone (includes children)
const shallowClone = div.cloneNode(false); // false = only the element, no children
```

---

## 2. Event Handling & Propagation

### 2.1 What are Events?

Events are things that happen in the browser — a user clicks a button, types in an input, scrolls the page, resizes the window, etc. JavaScript lets you **listen** for these events and respond to them.

```javascript
// Adding an event listener
const button = document.querySelector('#myBtn');

button.addEventListener('click', function(event) {
    console.log('Button was clicked!');
    console.log('Event type:', event.type);         // "click"
    console.log('Target element:', event.target);     // the button element
    console.log('Mouse X position:', event.clientX);  // X coordinate of click
});

// Removing an event listener (must use named function)
function handleClick() { console.log('Clicked!'); }
button.addEventListener('click', handleClick);
button.removeEventListener('click', handleClick);  // Must pass SAME function reference

// ❌ This WON'T work (anonymous function creates new reference)
// button.removeEventListener('click', () => console.log('Clicked!'));
```

### 2.2 Event Propagation (Capturing & Bubbling)

When you click a `<button>` inside a `<div>` inside `<body>`, the event doesn't just fire on the button — it travels through the DOM tree in **three phases**:

```
Phase 1: CAPTURING (Top → Down)                Phase 3: BUBBLING (Bottom → Up)
    window                                          window
      ↓                                               ↑
    document                                        document
      ↓                                               ↑
    <html>                                          <html>
      ↓                                               ↑
    <body>                                          <body>
      ↓                                               ↑
    <div id="parent">                             <div id="parent">
      ↓                                               ↑
    <button id="child"> ← Phase 2: TARGET         <button id="child">
```

> **Analogy:** Think of it like a stone dropping into a pond. **Capturing** is the stone going down through the water. **Target** is the stone hitting the bottom. **Bubbling** is the ripples rising back up to the surface.

```javascript
const parent = document.querySelector('#parent');
const child = document.querySelector('#child');

// Bubbling (default — third argument is false or omitted)
parent.addEventListener('click', () => console.log('Parent Bubbling'), false);
child.addEventListener('click', () => console.log('Child Bubbling'), false);

// Capturing (third argument is true)
parent.addEventListener('click', () => console.log('Parent Capturing'), true);

/* Click on the child button — Output in order:
   1. "Parent Capturing"  ← Capturing phase (top-down)
   2. "Child Bubbling"     ← Target phase
   3. "Parent Bubbling"    ← Bubbling phase (bottom-up)
*/
```

> **Why does this matter?** Understanding propagation is essential for Event Delegation (section 3) and for debugging when events fire unexpectedly on parent elements.

### 2.3 Stopping Propagation

```javascript
child.addEventListener('click', (e) => {
    // stopPropagation() — Prevents the event from traveling further up/down the tree
    // Parent's click handler will NOT fire
    e.stopPropagation();
    console.log('Only child handles this click');
});

// stopImmediatePropagation() — Stops propagation AND prevents other
// listeners on the SAME element from firing
child.addEventListener('click', (e) => {
    e.stopImmediatePropagation();
    console.log('First handler fires');
});
child.addEventListener('click', (e) => {
    console.log('Second handler — NEVER fires!');
});
```

### 2.4 Prevent Default Behavior

Some elements have built-in behaviors. `preventDefault()` cancels them:

```javascript
// 1. Stop form from refreshing the page on submit
form.addEventListener('submit', (e) => {
    e.preventDefault();  // Page won't reload
    // Handle form data with JavaScript instead
    const data = new FormData(form);
    fetch('/api/submit', { method: 'POST', body: data });
});

// 2. Stop link from navigating
link.addEventListener('click', (e) => {
    e.preventDefault();  // Won't navigate to href
    console.log('Link clicked but navigation prevented');
});

// 3. Stop checkbox from being checked
checkbox.addEventListener('click', (e) => {
    e.preventDefault();  // Checkbox state won't change
});
```

---

## 3. Event Delegation

### 3.1 The Problem

Imagine you have a list of 1000 items. Attaching a click listener to **each** item wastes memory and is slow:

```javascript
// ❌ BAD — 1000 listeners eating memory
const items = document.querySelectorAll('li');
items.forEach(item => {
    item.addEventListener('click', () => {
        console.log('Clicked:', item.textContent);
    });
});
// Problem 1: 1000 event listeners in memory
// Problem 2: Dynamically added <li> items won't have listeners!
```

### 3.2 The Solution — Event Delegation

Instead, attach **ONE** listener to the **parent** and use `event.target` to identify which child was clicked. This works because of **Event Bubbling** — clicks on children bubble up to the parent.

> **Analogy:** Instead of hiring 1000 security guards (one for each room), you hire ONE guard at the main gate who checks everyone's ID badge as they come through.

```javascript
// ✅ GOOD — ONE listener handles ALL items (even future ones!)
const list = document.querySelector('#list');

list.addEventListener('click', (e) => {
    // e.target = the exact element that was clicked
    // e.currentTarget = the element the listener is attached to (the <ul>)
    
    if (e.target.tagName === 'LI') {
        console.log('Clicked item:', e.target.textContent);
        e.target.classList.toggle('selected');
    }
});

// Now even dynamically added items work automatically!
const newItem = document.createElement('li');
newItem.textContent = 'New Item';
list.appendChild(newItem);
// Clicking "New Item" works — no extra listener needed!
```

### 3.3 Robust Delegation with closest()

What if your `<li>` contains child elements like `<span>` or `<img>`? When you click the `<span>`, `e.target` is the `<span>`, NOT the `<li>`:

```javascript
// HTML: <ul id="list">
//         <li><span class="icon">🎯</span> Item 1</li>
//         <li><span class="icon">🎯</span> Item 2</li>
//       </ul>

list.addEventListener('click', (e) => {
    // closest() walks UP the tree and finds the nearest ancestor matching the selector
    const li = e.target.closest('li');
    
    if (li && list.contains(li)) {
        // We found the <li>, even if user clicked the <span> inside it
        console.log('Clicked item:', li.textContent);
    }
});
```

> **When to use `closest()` vs `tagName`?** Use `closest()` when list items contain child elements (icons, badges, buttons inside cards). Use simple `tagName` check only when items have no children.

---

## 4. Browser Storage (Local/Session/Cookies)

### 4.1 Comparison Table

| Feature | localStorage | sessionStorage | Cookies |
|---------|-------------|----------------|---------|
| **Capacity** | ~5-10 MB | ~5 MB | ~4 KB |
| **Expires** | Never (manual clear) | When tab/window closes | Configurable (date or session) |
| **Scope** | Same origin, ALL tabs | Same origin, SAME tab only | Same origin + path |
| **Sent to server?** | ❌ Never | ❌ Never | ✅ With EVERY HTTP request |
| **Access** | JavaScript only | JavaScript only | JavaScript + Server |
| **Best for** | User preferences, theme | Form wizard data, temp state | Auth tokens, session IDs |

> **Analogy:**
> - **localStorage** = A filing cabinet in your office — always there, you have to manually clean it out.
> - **sessionStorage** = A whiteboard — wiped clean when you leave the room (close tab).
> - **Cookies** = A name badge you wear — everyone (server) can see it wherever you go (every request).

### 4.2 Usage

```javascript
// ══════════════════════════════════════════════
// localStorage — Persists until manually cleared
// ══════════════════════════════════════════════

// Store data (strings only — must stringify objects)
localStorage.setItem('theme', 'dark');
localStorage.setItem('user', JSON.stringify({ id: 1, name: 'Rahul' }));

// Retrieve data
const theme = localStorage.getItem('theme');          // "dark"
const user = JSON.parse(localStorage.getItem('user')); // { id: 1, name: 'Rahul' }

// Remove specific item
localStorage.removeItem('theme');

// Clear everything
localStorage.clear();

// Check how many items stored
console.log(localStorage.length); // number of stored items

// ══════════════════════════════════════════════
// sessionStorage — Same API, cleared when tab closes
// ══════════════════════════════════════════════

sessionStorage.setItem('step', '3');               // Wizard step
const step = sessionStorage.getItem('step');        // "3"
// Close tab → data is gone

// ══════════════════════════════════════════════
// Cookies — More complex, sent with every HTTP request
// ══════════════════════════════════════════════

// Set a cookie (raw string manipulation)
document.cookie = "username=Rahul; expires=Thu, 18 Dec 2025 12:00:00 UTC; path=/; SameSite=Lax";

// Read all cookies (returns one long string — needs parsing)
console.log(document.cookie); // "username=Rahul; theme=dark"

// Helper function to get a specific cookie
function getCookie(name) {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? match[2] : null;
}
getCookie('username'); // "Rahul"

// Delete a cookie (set expiry to past date)
document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
```

### 4.3 Storage Events (Cross-Tab Communication!)

When one tab changes localStorage, OTHER tabs of the same origin get notified:

```javascript
// Tab A: Changes a value
localStorage.setItem('cart', JSON.stringify([{ id: 1, qty: 2 }]));

// Tab B: Listens for changes (only fires from OTHER tabs)
window.addEventListener('storage', (event) => {
    console.log('Key changed:', event.key);        // "cart"
    console.log('Old value:', event.oldValue);      // previous value
    console.log('New value:', event.newValue);      // new value
    console.log('URL:', event.url);                 // which tab made the change
});
```

---

## 5. Fetch API & AJAX

### 5.1 What is Fetch?

`fetch()` is the modern way to make HTTP requests from JavaScript. It replaced the older, cumbersome `XMLHttpRequest` (AJAX).

> **Analogy:** `fetch()` is like ordering food delivery — you place an order (make a request), and you get a **promise** that food will arrive. You wait for it, then open the package (parse the response).

### 5.2 GET Requests

```javascript
// === Using Promises (.then/.catch) ===
fetch('https://api.example.com/users')
    .then(response => {
        // Step 1: Check if request was successful
        // ⚠️ fetch does NOT reject on HTTP 404/500 — only on network failure!
        if (!response.ok) {
            throw new Error(`HTTP Error: ${response.status} ${response.statusText}`);
        }
        // Step 2: Parse response body as JSON (returns another Promise)
        return response.json();
    })
    .then(data => {
        // Step 3: Use the data
        console.log('Users:', data);
    })
    .catch(error => {
        // Catches BOTH network errors and thrown errors
        console.error('Request failed:', error.message);
    })
    .finally(() => {
        // Always runs — good for hiding loading spinners
        hideLoadingSpinner();
    });

// === Using async/await (Preferred — cleaner syntax) ===
async function getUsers() {
    try {
        const response = await fetch('https://api.example.com/users');
        
        if (!response.ok) {
            throw new Error(`HTTP Error: ${response.status}`);
        }
        
        const users = await response.json();
        console.log('Users:', users);
        return users;
    } catch (error) {
        console.error('Failed to fetch users:', error.message);
    } finally {
        hideLoadingSpinner();
    }
}
```

### 5.3 POST, PUT, DELETE Requests

```javascript
// POST — Create a new resource
async function createUser(userData) {
    const response = await fetch('https://api.example.com/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer your-token-here'
        },
        body: JSON.stringify(userData)  // Must stringify objects!
    });
    
    return await response.json();
}
await createUser({ name: 'Rahul', email: 'rahul@example.com' });

// PUT — Update existing resource
await fetch('https://api.example.com/users/1', {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name: 'Rahul Kumar' })
});

// DELETE — Remove a resource
await fetch('https://api.example.com/users/1', {
    method: 'DELETE'
});
```

### 5.4 Fetch with Timeout (Fetch has no built-in timeout!)

```javascript
async function fetchWithTimeout(url, timeout = 5000) {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), timeout);
    
    try {
        const response = await fetch(url, { signal: controller.signal });
        return await response.json();
    } catch (error) {
        if (error.name === 'AbortError') {
            throw new Error('Request timed out');
        }
        throw error;
    } finally {
        clearTimeout(timeoutId);
    }
}
```

---

## 6. BOM (Browser Object Model)

The BOM lets JavaScript interact with the browser window itself (not the page content).

```javascript
// ══════════════════════════════════════════════
// window.location — URL information and navigation
// ══════════════════════════════════════════════
// For URL: https://example.com:443/products?id=5#details

console.log(location.href);       // Full URL
console.log(location.hostname);   // "example.com"
console.log(location.pathname);   // "/products"
console.log(location.search);     // "?id=5"
console.log(location.hash);       // "#details"
console.log(location.port);       // "443"
console.log(location.protocol);   // "https:"

location.reload();                // Refresh the page
location.href = '/new-page';     // Navigate to new URL (adds to history)
location.replace('/new-page');   // Navigate but DON'T add to history (can't go back)

// ══════════════════════════════════════════════
// window.history — Browser navigation history
// ══════════════════════════════════════════════

history.back();                   // Same as browser back button
history.forward();                // Same as browser forward button
history.go(-2);                   // Go back 2 pages

// pushState — Change URL without page reload (used by SPAs like React Router)
history.pushState({ page: 'about' }, 'About', '/about');

// ══════════════════════════════════════════════
// window.navigator — Browser & device information
// ══════════════════════════════════════════════

console.log(navigator.userAgent);     // Browser info string
console.log(navigator.language);      // "en-US"
console.log(navigator.onLine);        // true/false (internet connected?)
console.log(navigator.cookieEnabled); // true/false

// ══════════════════════════════════════════════
// window.screen — Screen dimensions
// ══════════════════════════════════════════════

console.log(screen.width);        // Screen width in pixels
console.log(screen.height);       // Screen height in pixels
console.log(window.innerWidth);   // Viewport width (excluding scrollbar)
console.log(window.innerHeight);  // Viewport height
```

---

## 7. Critical Rendering Path

### 7.1 How the Browser Renders a Page

When you type a URL and hit Enter, here's what happens step by step:

```
Step 1: Download HTML
         ↓
Step 2: Parse HTML → Build DOM Tree
         ↓ (encounters <link> CSS → downloads CSS)
Step 3: Parse CSS → Build CSSOM Tree
         ↓
Step 4: Combine DOM + CSSOM → Render Tree
         (Only visible elements — display:none excluded)
         ↓
Step 5: Layout (Reflow) — Calculate position & size of every element
         ↓
Step 6: Paint — Fill in colors, text, images, borders, shadows
         ↓
Step 7: Composite — Combine painted layers into final screen image
```

> **Key Insight:** CSS is **render-blocking** — the browser won't paint anything until ALL CSS is downloaded and parsed. That's why CSS goes in `<head>`.

### 7.2 `<script>` Tag Behavior

```html
<!-- DEFAULT: Blocks EVERYTHING — parsing stops until script downloads + executes -->
<script src="app.js"></script>

<!-- ASYNC: Downloads in parallel, executes IMMEDIATELY when ready -->
<!-- ⚠️ Execution order NOT guaranteed — whichever finishes downloading first runs first -->
<script async src="analytics.js"></script>

<!-- DEFER: Downloads in parallel, executes AFTER HTML parsing is complete -->
<!-- ✅ Execution order IS guaranteed — runs in document order -->
<script defer src="app.js"></script>
<script defer src="utils.js"></script>
<!-- app.js always runs before utils.js -->
```

| Attribute | Downloads during parsing? | When does it execute? | Order guaranteed? | Best for |
|-----------|--------------------------|----------------------|-------------------|----------|
| None | ❌ Blocks parsing | Immediately | ✅ Yes | Must run before page renders |
| `async` | ✅ Parallel | As soon as downloaded | ❌ No | Independent scripts (Analytics) |
| `defer` | ✅ Parallel | After HTML parsing done | ✅ Yes | Scripts that need full DOM |

### 7.3 Reflow vs Repaint

- **Reflow (Layout)**: When element geometry changes (size, position, adding/removing elements). **Expensive** — forces browser to recalculate layout of affected elements and their children.
- **Repaint**: When visual properties change without affecting layout (color, background, visibility). **Cheaper** than reflow.

```javascript
// ❌ Causes MULTIPLE reflows (bad)
element.style.width = '100px';    // Reflow 1
element.style.height = '200px';   // Reflow 2
element.style.margin = '10px';    // Reflow 3

// ✅ Causes ONE reflow (good) — batch changes via CSS class
element.classList.add('resized');  // Single reflow!

// ✅ Or use cssText for multiple inline styles
element.style.cssText = 'width: 100px; height: 200px; margin: 10px;';
```

---

## 8. Debounce & Throttle (Implementation)

### 8.1 Debounce

**What:** Wait until the user **stops** doing something for N milliseconds, then execute ONCE.

> **Analogy:** An elevator door — it doesn't close immediately. Every time someone walks in, the timer resets. It only closes after nobody has entered for 3 seconds.

**Use cases:** Search input (wait for user to stop typing), window resize handler.

```javascript
function debounce(fn, delay) {
    let timer;
    return function(...args) {
        clearTimeout(timer);              // Reset timer on every call
        timer = setTimeout(() => {
            fn.apply(this, args);         // Execute after delay
        }, delay);
    };
}

// Usage — API call fires only after user stops typing for 300ms
const searchInput = document.querySelector('#search');
const debouncedSearch = debounce((query) => {
    console.log('Searching for:', query);
    fetch(`/api/search?q=${query}`);
}, 300);

searchInput.addEventListener('input', (e) => {
    debouncedSearch(e.target.value);
});
// Typing "hello" fast: Only ONE API call with "hello" (not 5 individual calls)
```

### 8.2 Throttle

**What:** Execute at most once every N milliseconds, no matter how many times the event fires.

> **Analogy:** A water tap with a flow limiter — no matter how hard you turn it, water comes out at a fixed maximum rate.

**Use cases:** Scroll events, button click spam prevention, mouse move tracking.

```javascript
function throttle(fn, limit) {
    let inThrottle = false;
    return function(...args) {
        if (!inThrottle) {
            fn.apply(this, args);
            inThrottle = true;
            setTimeout(() => {
                inThrottle = false;       // Allow next execution after limit
            }, limit);
        }
    };
}

// Usage — fires at most once every 200ms during scroll
const throttledScroll = throttle(() => {
    console.log('Scroll position:', window.scrollY);
    // Maybe show/hide a "back to top" button
}, 200);

window.addEventListener('scroll', throttledScroll);
// During continuous scrolling: fires every 200ms (not every pixel)
```

### 8.3 Debounce vs Throttle Comparison

| Feature | Debounce | Throttle |
|---------|----------|----------|
| **Fires** | Once, after user stops | At regular intervals |
| **Timer resets?** | ✅ Yes, on each trigger | ❌ No |
| **Best for** | Search input, resize | Scroll, mousemove |
| **Behavior** | "Wait until calm" | "Limit the rate" |

---

## 9. Coding Problems

### 9.1 Build a DOM Tree Walker

Recursively traverse all elements in the DOM tree:

```javascript
function traverse(element, depth = 0) {
    const indent = '  '.repeat(depth);
    console.log(`${indent}${element.tagName} ${element.className ? '.' + element.className : ''}`);
    
    for (const child of element.children) {
        traverse(child, depth + 1);  // Recurse into children
    }
}

traverse(document.body);
// Output:
// BODY
//   DIV .container
//     H1
//     UL .list
//       LI
//       LI
//       LI
//   FOOTER
```

### 9.2 Click Counter with Event Delegation

```javascript
// HTML: <div id="container"><button>A</button><button>B</button><button>C</button></div>

const container = document.querySelector('#container');

container.addEventListener('click', (e) => {
    if (e.target.tagName === 'BUTTON') {
        const count = parseInt(e.target.dataset.count || '0');
        e.target.dataset.count = count + 1;
        e.target.textContent = `${e.target.textContent.split(' ')[0]} (${count + 1})`;
    }
});
```

### 9.3 Infinite Scroll Implementation

```javascript
window.addEventListener('scroll', throttle(() => {
    // Check if user has scrolled near the bottom
    const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
    
    if (scrollTop + clientHeight >= scrollHeight - 100) {
        loadMoreItems();  // Fetch and append next page of data
    }
}, 200));
```

---

## 10. Key Topics & Explanations

### 10.1 What is the difference between `defer` and `async` script tags?

**Explanation:**
- `<script>`: **Blocks** HTML parsing until downloaded and executed. Slow!
- `<script async>`: Downloads **in parallel**, executes **immediately** when ready. Order NOT guaranteed. Use for independent scripts like analytics.
- `<script defer>`: Downloads **in parallel**, executes **after HTML parsing** complete. Order IS guaranteed. Use for scripts that need the full DOM.

> **Rule of thumb:** Use `defer` for your app scripts, `async` for third-party scripts like Google Analytics that don't depend on your code.

### 10.2 What is Event Bubbling? How to stop it?

**Explanation:** When an event fires on an element, it doesn't stop there — it **bubbles up** through all its parent elements, all the way to `window`. This means clicking a `<button>` inside a `<div>` also fires the `<div>`'s click handler.

**Stop it using:** `event.stopPropagation()` — prevents the event from reaching parent elements. Use carefully — it can break Event Delegation.

### 10.3 `innerHTML` vs `innerText` vs `textContent`?

**Explanation:**

| Property | What it does | Speed | XSS Safe? |
|----------|-------------|-------|-----------|
| `textContent` | Gets/sets raw text of node + all descendants | ⚡ Fastest | ✅ Yes |
| `innerText` | Gets/sets visible text (respects CSS like `display:none`) | 🐢 Slowest (triggers reflow) | ✅ Yes |
| `innerHTML` | Gets/sets HTML string (parses tags) | 🔄 Medium | ❌ No (XSS risk!) |

**When to use:** Use `textContent` for plain text (safest, fastest). Use `innerHTML` only when you need to insert HTML and trust the source. **Never** put user input into `innerHTML`.

### 10.4 How does `JSON.stringify` handle Functions, Symbols, and undefined?

**Explanation:** `JSON.stringify()` silently **ignores** them when they are object values. In arrays, they become `null`:

```javascript
JSON.stringify({ name: "Rahul", greet: function(){}, sym: Symbol() });
// '{"name":"Rahul"}' — function and symbol IGNORED

JSON.stringify([1, undefined, function(){}, Symbol()]);
// '[1,null,null,null]' — replaced with null in arrays
```

### 10.5 What is a "Closure" in the context of an Event Listener?

**Explanation:** When you define an event listener inside a function, the listener forms a **closure** — it "remembers" variables from the outer function even after that function has finished executing:

```javascript
function attachCounter() {
    let count = 0;  // This variable lives as long as the listener exists
    
    button.addEventListener('click', () => {
        count++;  // Closure — accesses 'count' from outer scope
        console.log(`Clicked ${count} times`);
    });
}
attachCounter();
// Clicking button: "Clicked 1 times", "Clicked 2 times", ...
// 'count' is NOT garbage collected because the listener still references it
```

> **Potential memory leak:** If you add listeners that form closures on elements that get removed from the DOM but the listener isn't removed, the closure (and all variables it holds) stays in memory. Always remove listeners when elements are destroyed.
