# JavaScript DOM & Browser APIs - Complete Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** DOM Tree, Event Propagation, Event Delegation, Browser Storage, Fetch API, and critical browser concepts.

--

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

--

## 1. DOM Manipulation

The **Document Object Model (DOM)** is the interface between JavaScript and HTML.

### Selecting Elements

```javascript
// Old School
document.getElementById('header');
document.getElementsByClassName('item'); // HTMLCollection (live)
document.getElementsByTagName('div');    // HTMLCollection (live)

// Modern (Recommended)
document.querySelector('#header');       // First match
document.querySelectorAll('.item');      // NodeList (static)
```

**Key Difference:** `NodeList` returned by `querySelectorAll` is static (snapshot), while `HTMLCollection` is live (updates automatically if DOM changes).

### Modifying Elements

```javascript
const el = document.querySelector('.box');

// Content
el.textContent = 'Hello'; // Safe, text only
el.innerHTML = '<b>Hello</b>'; // Parsed HTML (XSS risk!)
el.innerText = 'Hello'; // Visual text (aware of CSS styling like display: none)

// Styles
el.style.color = 'red';
el.style.backgroundColor = '#ccc';
el.classList.add('active');
el.classList.toggle('visible');

// Attributes
el.setAttribute('data-id', '123');
console.log(el.getAttribute('src'));
el.dataset.role = 'admin'; // specialized for data-* attributes
```

### Creating & Removing Elements (Performance!)

**Bad Practice (Reflow Thrashing):**
```javascript
const list = document.querySelector('ul');
for (let i = 0; i < 100; i++) {
    // Triggers layout recalculation 100 times!
    list.innerHTML += `<li>Item ${i}</li>`; 
}
```

**Good Practice (Fragments):**
```javascript
const list = document.querySelector('ul');
const fragment = document.createDocumentFragment();

for (let i = 0; i < 100; i++) {
    const li = document.createElement('li');
    li.textContent = `Item ${i}`;
    fragment.appendChild(li); // In-memory append
}

list.appendChild(fragment); // Single reflow!
```

--

## 2. Event Handling & Propagation

### Event Phases
1. **Capturing Phase**: Checks hierarchy down to target (Window -> Body -> Button).
2. **Target Phase**: The element itself.
3. **Bubbling Phase**: Bubbles back up to root (Button -> Body -> Window). *Default for listeners.*

```javascript
const parent = document.querySelector('#parent');
const child = document.querySelector('#child');

// Bubbling (default, false)
parent.addEventListener('click', () => console.log('Parent Bubbling'), false);
child.addEventListener('click', () => console.log('Child Bubbling'), false);

// Capturing (true)
parent.addEventListener('click', () => console.log('Parent Capturing'), true);

/* Click on Child Output:
1. Parent Capturing (Down)
2. Child Bubbling (Target)
3. Parent Bubbling (Up)
*/
```

### Stopping Propagation

```javascript
child.addEventListener('click', (e) => {
    e.stopPropagation(); // Stops bubbling up
    e.stopImmediatePropagation(); // Stops bubbling AND other listeners on THIS element
});
```

### Prevent Default

```javascript
form.addEventListener('submit', (e) => {
    e.preventDefault(); // Stops form submission refresh
});
```

--

## 3. Event Delegation

Attaching **one** listener to a parent instead of **many** to children. Uses **Event Bubbling**.

**Why?**
1. Memory efficient (1 listener vs 1000).
2. Handles dynamically added elements automatically.

```javascript
// Suppose we have a list: <ul id="list"><li>1</li><li>2</li>...</ul>

document.querySelector('#list').addEventListener('click', (e) => {
    // Check if clicked element is an LI
    if (e.target.tagName === 'LI') {
        console.log('Clicked item:', e.target.textContent);
    }
    
    // Robust check (if LI has children like spans)
    const li = e.target.closest('li');
    if (li && list.contains(li)) {
        // Handle click
    }
});
```

--

## 4. Browser Storage (Local/Session/Cookies)

### Comparison Table (Critical Concept Check)

| Feature | LocalStorage | SessionStorage | Cookies |
|-----|-------|--------|-----|
| **Capacity** | ~5-10 MB | ~5 MB | ~4 KB |
| **Expires** | Never (Manual clear) | Tab Close | Manually set |
| **Scope** | Domain (All tabs) | Tab only | Domain |
| **Server** | Client-side only | Client-side only | Sent with EVERY request |
| **Access** | JS (sync) | JS (sync) | JS & Server |

### Usage

```javascript
// LocalStorage
localStorage.setItem('user', JSON.stringify({ id: 1 }));
const user = JSON.parse(localStorage.getItem('user'));
localStorage.removeItem('user');
localStorage.clear();

// Cookies (Harder in vanilla JS)
document.cookie = "username=John; expires=Thu, 18 Dec 2025 12:00:00 UTC; path=/";
// Parsing cookies usually requires a utility function or regex
```

--

## 5. Fetch API & AJAX

Modern replacement for `XMLHttpRequest`.

```javascript
// GET Request
fetch('https://api.example.com/data')
    .then(response => {
        if (!response.ok) throw new Error('Network error');
        return response.json(); // Returns a promise
    })
    .then(data => console.log(data))
    .catch(err => console.error(err));

// POST Request
fetch('https://api.example.com/users', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({ name: 'Rahul' })
});

// Async/Await Syntax (Preferred)
async function getData() {
    try {
        const res = await fetch(url);
        const data = await res.json();
        return data; 
    } catch (e) {
        console.log(e);
    }
}
```

--

## 6. BOM (Browser Object Model)

Interaction with the browser itself.

- `window.location`: URL manipulation.
  - `href`: Full URL.
  - `search`: Query string (`?id=1`).
  - `reload()`: Refreshes page.
- `window.history`: Navigation.
  - `back()`, `forward()`, `pushState()`.
- `window.navigator`: Browser info.
  - `userAgent`, `geolocation`.
- `window.screen`: Screen dimensions.

--

## 7. Critical Rendering Path

How the browser renders a page (Core Concept).

1. **DOM**: HTML -> DOM Tree.
2. **CSSOM**: CSS -> CSS Object Model Tree.
3. **Render Tree**: DOM + CSSOM (Visible content only).
4. **Layout (Reflow)**: Calculate geometry (position/size).
5. **Paint**: Fill pixels (color, text, images).
6. **Composite**: Layer assembly.

**Optimization tips:**
- CSS in `<head>` (blocking).
- JS at end of `<body>` or `async`/`defer`.
- Minimize Reflows (batch DOM updates).

--

## 8. Debounce & Throttle (Implementation)

Implemented in Functions guide, but critical for DOM events like scroll/resize.

- **Debounce**: Search Input (Wait for typing to stop).
- **Throttle**: Scroll Event (Fire loop at max speed, e.g., every 100ms).

--

## 9. Coding Problems

### Problem 1: Build a Tree Walker
Traverse DOM elements recursively.

```javascript
function traverse(element) {
    console.log(element.tagName);
    // Recursion
    for (let child of element.children) {
        traverse(child);
    }
}
traverse(document.body);
```

### Problem 2: Delegation (Click counter)

```javascript
// HTML: <div id="container"><button>A</button><button>B</button>...</div>
container.addEventListener('click', (e) => {
    if (e.target.tagName === 'BUTTON') {
        const count = parseInt(e.target.dataset.count || 0);
        e.target.dataset.count = count + 1;
        e.target.textContent = `Clicked ${count + 1}`;
    }
});
```

--

## 10. Key Topics & Explanations

### Topic 1: What is the difference between `defer` and `async` script tags?
**Answer:**
- `<script>`: Blocks parsing until downloaded and executed.
- `<script async>`: Downloads parallel, executes **immediately** when ready (pauses parsing). Order not guaranteed.
- `<script defer>`: Downloads parallel, executes **after HTML parsing** complete (before DOMContentLoaded). Order guaranteed.
**Use `defer`** for dependent scripts (jQuery + Plugin). **Use `async`** for independent scripts (Analytics).

### Topic 2: What is Event Bubbling? How to stop it?
**Answer:** It's the process where an event received by an element is propagated to its parent, grandparent, etc., up to the window.
Stop it using `event.stopPropagation()`.

### Topic 3: `innerHTML` vs `innerText` vs `textContent`?
**Answer:**
- `innerHTML`: Parses HTML string (Risk: XSS). Slowest.
- `innerText`: Visual text. Respects CSS (e.g., won't show `display: none` text). Triggers Reflow.
- `textContent`: Raw text of node and descendants. Fastest. Ignores styling.

### Topic 4: How does `JSON.stringify` handle Functions or Symbols?
**Answer:** It ignores them (if value) or converts to null (if in array). They are not valid JSON data types.

### Topic 5: What is a "Closure" in the context of an Event Listener?
**Answer:** Event listeners often form closures.
```javascript
function attach() {
    let count = 0;
    button.addEventListener('click', () => {
        count++; // Accesses count from outer scope
        console.log(count);
    });
}
```
The listener keeps `count` alive even after `attach()` finishes.
