# Angular Directives & Pipes - Complete Guide (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Structural directives, attribute directives, custom directives, built-in pipes, custom pipes, pure vs impure pipes, and Angular 17+ control flow.

--

## Table of Contents

1. [What are Directives?](#what-are-directives)
2. [Types of Directives](#types-of-directives)
3. [Structural Directives](#structural-directives)
4. [Angular 17+ Built-in Control Flow](#angular-17-built-in-control-flow)
5. [Attribute Directives](#attribute-directives)
6. [Custom Attribute Directives](#custom-attribute-directives)
7. [Custom Structural Directives](#custom-structural-directives)
8. [Host Listener & Host Binding](#host-listener-host-binding)
9. [What are Pipes?](#what-are-pipes)
10. [Built-in Pipes](#built-in-pipes)
11. [Custom Pipes](#custom-pipes)
12. [Pure vs Impure Pipes](#pure-vs-impure-pipes)
13. [Async Pipe](#async-pipe)
14. [Chaining Pipes](#chaining-pipes)
15. [Practical Examples](#practical-examples)

--

## 1. What are Directives?

Directives are **classes that add behavior to elements** in Angular templates. They are instructions that tell Angular to **modify the DOM** — either by changing the structure, appearance, or behavior of elements.

Every component is technically a directive (a directive with a template). But when we say "directive," we usually mean directives **without their own template**.

```
Directive = Instructions to modify DOM elements
Component = Directive + Template (has its own view)
```

--

## 2. Types of Directives

| Type | Purpose | Example | Prefix |
|------|---------|---------|--------|
| **Component** | Directive with a template | `<app-user-card>` | `app-` |
| **Structural** | Add/remove DOM elements | `*ngIf`, `*ngFor`, `*ngSwitch` | `*` |
| **Attribute** | Change appearance/behavior | `ngClass`, `ngStyle`, custom | `[attr]` |

### Quick Comparison

```html
<!-- Component directive — has its own template -->
<app-user-card [user]="user"></app-user-card>

<!-- Structural directive — adds/removes elements from DOM -->
<div *ngIf="isVisible">I may or may not exist in DOM</div>

<!-- Attribute directive — modifies existing element -->
<div [ngClass]="{'active': isActive}">I always exist, but my class changes</div>
```

--

## 3. Structural Directives

Structural directives **change the DOM layout** by adding, removing, or manipulating elements. They are prefixed with `*` (asterisk), which is syntactic sugar for `<ng-template>`.

### *ngIf — Conditional Rendering

```html
<!-- Basic -->
<div *ngIf="isLoggedIn">Welcome back!</div>

<!-- With else -->
<div *ngIf="isLoggedIn; else loginTemplate">
  Welcome, {{ user.name }}!
</div>
<ng-template #loginTemplate>
  <p>Please <a routerLink="/login">login</a></p>
</ng-template>

<!-- With then and else -->
<div *ngIf="isLoggedIn; then loggedInTemplate; else loginTemplate"></div>
<ng-template #loggedInTemplate>
  <p>Welcome back!</p>
</ng-template>
<ng-template #loginTemplate>
  <p>Please login</p>
</ng-template>

<!-- Store result in variable (useful with async pipe) -->
<div *ngIf="user$ | async as user">
  <p>{{ user.name }}</p>
  <p>{{ user.email }}</p>
</div>
```

**How *ngIf works internally:**
```html
<!-- This syntactic sugar: -->
<div *ngIf="isVisible">Content</div>

<!-- Is actually: -->
<ng-template [ngIf]="isVisible">
  <div>Content</div>
</ng-template>
```

**Key point:** `*ngIf` **removes** the element from DOM entirely (not just hides it). The component is **destroyed** when false and **recreated** when true. `ngOnInit` fires again each time.

### *ngFor — Loop/Iteration

```html
<!-- Basic -->
<li *ngFor="let user of users">{{ user.name }}</li>

<!-- With index -->
<li *ngFor="let user of users; let i = index">
  {{ i + 1 }}. {{ user.name }}
</li>

<!-- With all exported variables -->
<li *ngFor="let user of users;
           let i = index;
           let first = first;
           let last = last;
           let even = even;
           let odd = odd;
           let count = count;
           trackBy: trackById">
  <span [class.highlight]="first">{{ user.name }}</span>
  <span *ngIf="last"> (Last item)</span>
  <span [class.even-row]="even">Row {{ i }}</span>
</li>
```

| Variable | Type | Description |
|----------|------|-------------|
| `index` | number | Zero-based index of current item |
| `first` | boolean | True if first item |
| `last` | boolean | True if last item |
| `even` | boolean | True if even index |
| `odd` | boolean | True if odd index |
| `count` | number | Total number of items |

### trackBy — Performance Optimization

```typescript
// Without trackBy: Angular destroys and recreates ALL DOM elements on any array change
// With trackBy: Angular only updates changed items

// Component
trackById(index: number, user: User): number {
  return user.id;  // Unique identifier
}
```

```html
<li *ngFor="let user of users; trackBy: trackById">{{ user.name }}</li>
```

### *ngSwitch — Multiple Conditions

```html
<div [ngSwitch]="user.role">
  <admin-panel *ngSwitchCase="'admin'"></admin-panel>
  <user-dashboard *ngSwitchCase="'user'"></user-dashboard>
  <moderator-view *ngSwitchCase="'moderator'"></moderator-view>
  <guest-view *ngSwitchDefault></guest-view>
</div>
```

### Important Rule: One Structural Directive Per Element

```html
<!-- INVALID — two structural directives on same element -->
<div *ngIf="isVisible" *ngFor="let item of items">{{ item }}</div>

<!-- FIX: Wrap with ng-container (doesn't create extra DOM element) -->
<ng-container *ngIf="isVisible">
  <div *ngFor="let item of items">{{ item }}</div>
</ng-container>
```

### ng-container vs ng-template

```html
<!-- ng-container: Grouping element that doesn't render in DOM -->
<ng-container *ngIf="isAdmin">
  <button>Edit</button>
  <button>Delete</button>
</ng-container>
<!-- Renders: <button>Edit</button><button>Delete</button> (no wrapper div) -->

<!-- ng-template: Defines a template that is NOT rendered by default -->
<ng-template #myTemplate>
  <p>This won't render unless explicitly used</p>
</ng-template>

<!-- Render it with ngTemplateOutlet -->
<ng-container *ngTemplateOutlet="myTemplate"></ng-container>
```

--

## 4. Angular 17+ Built-in Control Flow

Angular 17 introduced a **new control flow syntax** that replaces `*ngIf`, `*ngFor`, and `*ngSwitch`. It's built into the template compiler — no imports needed.

### @if — Replaces *ngIf

```html
@if (isLoggedIn) {
  <p>Welcome, {{ user.name }}!</p>
} @else if (isGuest) {
  <p>Welcome, Guest!</p>
} @else {
  <p>Please login.</p>
}

<!-- With async pipe -->
@if (user$ | async; as user) {
  <p>{{ user.name }}</p>
  <p>{{ user.email }}</p>
}
```

### @for — Replaces *ngFor

```html
<!-- track is REQUIRED (replaces trackBy) -->
@for (user of users; track user.id) {
  <div>{{ $index + 1 }}. {{ user.name }}</div>
} @empty {
  <p>No users found.</p>
}

<!-- Available implicit variables -->
@for (user of users; track user.id; let i = $index, let c = $count) {
  <div [class.first]="$first" [class.last]="$last" [class.even]="$even">
    {{ i + 1 }} of {{ c }}: {{ user.name }}
  </div>
}
```

| Variable | Description |
|----------|-------------|
| `$index` | Zero-based index |
| `$first` | True if first item |
| `$last` | True if last item |
| `$even` | True if even index |
| `$odd` | True if odd index |
| `$count` | Total items |

### @switch — Replaces *ngSwitch

```html
@switch (user.role) {
  @case ('admin') {
    <admin-panel />
  }
  @case ('user') {
    <user-dashboard />
  }
  @default {
    <guest-view />
  }
}
```

### @defer — Deferred/Lazy Loading (New!)

```html
<!-- Load when element enters viewport -->
@defer (on viewport) {
  <heavy-chart-component />
} @placeholder {
  <div class="skeleton">Chart loading area...</div>
} @loading (minimum 500ms) {
  <spinner />
} @error {
  <p>Failed to load chart.</p>
}

<!-- Other triggers -->
@defer (on idle) { ... }                <!-- When browser is idle -->
@defer (on interaction) { ... }         <!-- On user click/focus -->
@defer (on hover) { ... }              <!-- On mouse hover -->
@defer (on timer(3s)) { ... }          <!-- After 3 seconds -->
@defer (when isDataReady) { ... }      <!-- When condition is true -->
@defer (prefetch on idle) { ... }      <!-- Prefetch when idle, show on viewport -->
```

### Old vs New Syntax Comparison

| Old | New (Angular 17+) |
|-----|-------------------|
| `*ngIf="cond"` | `@if (cond) { }` |
| `*ngIf="cond; else tmpl"` | `@if (cond) { } @else { }` |
| `*ngFor="let x of items; trackBy: fn"` | `@for (x of items; track x.id) { }` |
| `[ngSwitch]` + `*ngSwitchCase` | `@switch` + `@case` |
| No equivalent | `@defer`, `@empty` |

### Migration

```bash
# Automatic migration from old to new syntax
ng generate @angular/core:control-flow
```

--

## 5. Attribute Directives

Attribute directives change the **appearance or behavior** of an existing element without adding/removing it from the DOM.

### ngClass — Dynamic CSS Classes

```html
<!-- String -->
<div [ngClass]="'active bold'">Multiple classes as string</div>

<!-- Array -->
<div [ngClass]="['active', 'bold']">Multiple classes as array</div>

<!-- Object (most common) — key: class name, value: condition -->
<div [ngClass]="{
  'active': isActive,
  'disabled': isDisabled,
  'highlight': isSelected,
  'error-text': hasError
}">
  Conditional classes
</div>

<!-- With method -->
<div [ngClass]="getClasses()">Dynamic classes from method</div>
```

```typescript
getClasses() {
  return {
    'active': this.isActive,
    'admin': this.user.role === 'admin',
    'dark-theme': this.isDarkMode
  };
}
```

### ngStyle — Dynamic Inline Styles

```html
<!-- Object -->
<div [ngStyle]="{
  'color': isError ? 'red' : 'green',
  'font-size': fontSize + 'px',
  'background-color': bgColor,
  'font-weight': isBold ? 'bold' : 'normal'
}">
  Dynamic styles
</div>

<!-- Single style property (simpler alternative) -->
<div [style.color]="isError ? 'red' : 'green'">Colored text</div>
<div [style.font-size.px]="fontSize">Sized text</div>
<div [style.width.%]="progress">Progress bar</div>
```

### ngClass vs Class Binding

```html
<!-- [class.name] — single class toggle -->
<div [class.active]="isActive">Single class</div>
<div [class.error-border]="hasError">Single class</div>

<!-- [ngClass] — multiple classes with conditions -->
<div [ngClass]="{'active': isActive, 'error': hasError, 'bold': isBold}">Multiple</div>

<!-- Recommendation: Use [class.x] for single class, [ngClass] for multiple -->
```

--

## 6. Custom Attribute Directives

### Basic Custom Directive

```typescript
import { Directive, ElementRef, OnInit } from '@angular/core';

@Directive({
  selector: '[appHighlight]',
  standalone: true
})
export class HighlightDirective implements OnInit {
  constructor(private el: ElementRef) {}

  ngOnInit() {
    this.el.nativeElement.style.backgroundColor = 'yellow';
  }
}

// Usage
<p appHighlight>This text has a yellow background</p>
```

### Directive with @Input

```typescript
@Directive({
  selector: '[appHighlight]',
  standalone: true
})
export class HighlightDirective implements OnInit, OnChanges {
  @Input() appHighlight = 'yellow';       // Same name as selector — pass value directly
  @Input() highlightText = 'black';       // Additional input

  constructor(private el: ElementRef) {}

  ngOnInit() {
    this.applyStyles();
  }

  ngOnChanges() {
    this.applyStyles();
  }

  private applyStyles() {
    this.el.nativeElement.style.backgroundColor = this.appHighlight;
    this.el.nativeElement.style.color = this.highlightText;
  }
}

// Usage
<p [appHighlight]="'lightblue'" highlightText="white">Custom colors</p>
<p appHighlight>Default yellow highlight</p>
```

### Directive with @HostListener (Events)

```typescript
@Directive({
  selector: '[appHoverHighlight]',
  standalone: true
})
export class HoverHighlightDirective {
  @Input() hoverColor = 'yellow';
  @Input() defaultColor = 'transparent';

  constructor(private el: ElementRef) {}

  @HostListener('mouseenter')
  onMouseEnter() {
    this.setColor(this.hoverColor);
  }

  @HostListener('mouseleave')
  onMouseLeave() {
    this.setColor(this.defaultColor);
  }

  @HostListener('click', ['$event'])
  onClick(event: MouseEvent) {
    console.log('Clicked at:', event.clientX, event.clientY);
  }

  private setColor(color: string) {
    this.el.nativeElement.style.backgroundColor = color;
  }
}

// Usage
<p appHoverHighlight hoverColor="lightgreen">Hover over me!</p>
```

### Directive with Renderer2 (Best Practice)

```typescript
import { Directive, ElementRef, Renderer2, HostListener } from '@angular/core';

// Renderer2 is safer than direct DOM manipulation (works with SSR)
@Directive({
  selector: '[appSafeHighlight]',
  standalone: true
})
export class SafeHighlightDirective {
  constructor(private el: ElementRef, private renderer: Renderer2) {}

  @HostListener('mouseenter')
  onMouseEnter() {
    // Safe DOM manipulation — works in browser AND server
    this.renderer.setStyle(this.el.nativeElement, 'background-color', 'yellow');
    this.renderer.addClass(this.el.nativeElement, 'highlighted');
    this.renderer.setAttribute(this.el.nativeElement, 'title', 'Highlighted!');
  }

  @HostListener('mouseleave')
  onMouseLeave() {
    this.renderer.removeStyle(this.el.nativeElement, 'background-color');
    this.renderer.removeClass(this.el.nativeElement, 'highlighted');
    this.renderer.removeAttribute(this.el.nativeElement, 'title');
  }
}
```

### ElementRef vs Renderer2

| Feature | ElementRef | Renderer2 |
|---------|-----------|-----------|
| **Access** | Direct DOM access | Abstracted DOM access |
| **SSR** | Breaks (no DOM on server) | Works everywhere |
| **Security** | XSS risk with innerHTML | Safer |
| **Use** | Reading values | Modifying DOM |

**Rule:** Use `Renderer2` for DOM modifications. Use `ElementRef` only for reading values.

### Practical Directives

```typescript
// Click Outside Directive — close dropdowns when clicking outside
@Directive({
  selector: '[appClickOutside]',
  standalone: true
})
export class ClickOutsideDirective {
  @Output() appClickOutside = new EventEmitter<void>();

  constructor(private el: ElementRef) {}

  @HostListener('document:click', ['$event.target'])
  onClick(target: HTMLElement) {
    if (!this.el.nativeElement.contains(target)) {
      this.appClickOutside.emit();
    }
  }
}

// Usage
<div class="dropdown" (appClickOutside)="closeDropdown()">
  <button (click)="toggleDropdown()">Menu</button>
  <ul *ngIf="isOpen">
    <li>Option 1</li>
    <li>Option 2</li>
  </ul>
</div>
```

```typescript
// Auto Focus Directive
@Directive({
  selector: '[appAutoFocus]',
  standalone: true
})
export class AutoFocusDirective implements AfterViewInit {
  constructor(private el: ElementRef) {}

  ngAfterViewInit() {
    this.el.nativeElement.focus();
  }
}

// Usage
<input appAutoFocus placeholder="I'm focused on load!">
```

```typescript
// Debounce Click Directive — prevent double clicks
@Directive({
  selector: '[appDebounceClick]',
  standalone: true
})
export class DebounceClickDirective implements OnInit, OnDestroy {
  @Input() debounceTime = 500;
  @Output() debounceClick = new EventEmitter<MouseEvent>();
  private clicks$ = new Subject<MouseEvent>();
  private subscription!: Subscription;

  ngOnInit() {
    this.subscription = this.clicks$.pipe(
      debounceTime(this.debounceTime)
    ).subscribe(event => this.debounceClick.emit(event));
  }

  @HostListener('click', ['$event'])
  onClick(event: MouseEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.clicks$.next(event);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}

// Usage — only fires once even if user double-clicks
<button appDebounceClick (debounceClick)="save()" [debounceTime]="300">
  Save
</button>
```

--

## 7. Custom Structural Directives

Structural directives modify the DOM structure by adding or removing elements.

### Basic Custom Structural Directive

```typescript
import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';

// Custom *appUnless — opposite of *ngIf
@Directive({
  selector: '[appUnless]',
  standalone: true
})
export class UnlessDirective {
  private hasView = false;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef
  ) {}

  @Input()
  set appUnless(condition: boolean) {
    if (!condition && !this.hasView) {
      // Condition is false — show the element
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.hasView = true;
    } else if (condition && this.hasView) {
      // Condition is true — hide the element
      this.viewContainer.clear();
      this.hasView = false;
    }
  }
}

// Usage — shows when condition is FALSE
<p *appUnless="isLoggedIn">Please login to continue.</p>
```

### Role-Based Structural Directive

```typescript
@Directive({
  selector: '[appHasRole]',
  standalone: true
})
export class HasRoleDirective implements OnDestroy {
  private subscription!: Subscription;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private authService: AuthService
  ) {}

  @Input()
  set appHasRole(roles: string[]) {
    this.subscription = this.authService.currentUser$.subscribe(user => {
      if (user && roles.includes(user.role)) {
        this.viewContainer.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainer.clear();
      }
    });
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }
}

// Usage — only visible to admins and managers
<button *appHasRole="['admin', 'manager']">Delete User</button>
```

--

## 8. Host Listener & Host Binding

### @HostListener — Listen to Host Element Events

```typescript
@Directive({
  selector: '[appKeyLogger]',
  standalone: true
})
export class KeyLoggerDirective {
  // Listen to events on the host element
  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    console.log('Key pressed:', event.key);
  }

  // Listen to window/document events
  @HostListener('window:resize', ['$event'])
  onResize(event: Event) {
    console.log('Window resized:', window.innerWidth);
  }

  @HostListener('document:keydown.escape')
  onEscape() {
    console.log('Escape pressed anywhere');
  }
}
```

### @HostBinding — Bind to Host Element Properties

```typescript
@Directive({
  selector: '[appDropdown]',
  standalone: true
})
export class DropdownDirective {
  // Bind to host element's class
  @HostBinding('class.open') isOpen = false;

  // Bind to host element's style
  @HostBinding('style.border') border = '1px solid #ccc';

  // Bind to host element's attribute
  @HostBinding('attr.aria-expanded') get ariaExpanded() {
    return this.isOpen;
  }

  @HostListener('click')
  toggle() {
    this.isOpen = !this.isOpen;
    this.border = this.isOpen ? '2px solid blue' : '1px solid #ccc';
  }
}

// Usage
<div appDropdown>
  <button>Toggle Menu</button>
  <ul *ngIf="...">...</ul>
</div>
```

### host Property Alternative (Component Metadata)

```typescript
// Instead of @HostListener and @HostBinding decorators,
// you can use the host property in @Component/@Directive metadata

@Component({
  selector: 'app-button',
  standalone: true,
  host: {
    'class': 'btn',                          // Static class
    '[class.active]': 'isActive',            // Dynamic class
    '[class.disabled]': 'isDisabled',
    '[attr.aria-label]': 'label',            // Dynamic attribute
    '[style.opacity]': 'isDisabled ? 0.5 : 1',
    '(click)': 'onClick($event)',            // Event listener
    '(keydown.enter)': 'onClick($event)'
  },
  template: `<ng-content></ng-content>`
})
export class ButtonComponent {
  @Input() label = 'Button';
  @Input() isActive = false;
  @Input() isDisabled = false;

  onClick(event: Event) {
    if (!this.isDisabled) {
      console.log('Button clicked');
    }
  }
}
```

--

## 9. What are Pipes?

Pipes **transform data for display** in templates. They take a value, process it, and return a formatted result. Pipes do NOT change the original data — they only change how it's **displayed**.

```html
<!-- Syntax: value | pipeName:arg1:arg2 -->
{{ birthday | date:'fullDate' }}
{{ price | currency:'INR':'symbol':'1.2-2' }}
{{ user.name | uppercase }}
```

### Why Use Pipes?

- **Separation of concerns** — formatting logic stays in template, not component.
- **Reusability** — same pipe used across multiple components.
- **Performance** — pure pipes are memoized (cached).
- **Readability** — `{{ price | currency }}` is clearer than `{{ formatCurrency(price) }}`.

--

## 10. Built-in Pipes

### String Pipes

```html
{{ 'hello world' | uppercase }}          <!-- HELLO WORLD -->
{{ 'HELLO WORLD' | lowercase }}          <!-- hello world -->
{{ 'hello world' | titlecase }}          <!-- Hello World -->
```

### Number Pipes

```html
<!-- DecimalPipe: {{ value | number:'minIntDigits.minFracDigits-maxFracDigits' }} -->
{{ 3.14159 | number:'1.2-4' }}           <!-- 3.1416 -->
{{ 3.14159 | number:'3.1-1' }}           <!-- 003.1 -->
{{ 1234567 | number }}                    <!-- 1,234,567 -->

<!-- PercentPipe -->
{{ 0.85 | percent }}                      <!-- 85% -->
{{ 0.8567 | percent:'1.1-2' }}           <!-- 85.67% -->

<!-- CurrencyPipe -->
{{ 1234.5 | currency }}                   <!-- $1,234.50 -->
{{ 1234.5 | currency:'INR' }}            <!-- ₹1,234.50 -->
{{ 1234.5 | currency:'INR':'symbol':'1.0-0' }}  <!-- ₹1,235 -->
{{ 1234.5 | currency:'EUR':'code' }}     <!-- EUR1,234.50 -->
```

### Date Pipe

```html
<!-- DatePipe: {{ value | date:'format' }} -->
{{ today | date }}                        <!-- Jun 15, 2024 -->
{{ today | date:'short' }}               <!-- 6/15/24, 9:30 AM -->
{{ today | date:'medium' }}              <!-- Jun 15, 2024, 9:30:00 AM -->
{{ today | date:'long' }}                <!-- June 15, 2024 at 9:30:00 AM GMT+5 -->
{{ today | date:'fullDate' }}            <!-- Saturday, June 15, 2024 -->

<!-- Custom format -->
{{ today | date:'dd/MM/yyyy' }}          <!-- 15/06/2024 -->
{{ today | date:'dd-MMM-yyyy' }}         <!-- 15-Jun-2024 -->
{{ today | date:'yyyy-MM-dd HH:mm:ss' }} <!-- 2024-06-15 09:30:00 -->
{{ today | date:'hh:mm a' }}             <!-- 09:30 AM -->
{{ today | date:'EEEE' }}                <!-- Saturday -->
```

| Symbol | Meaning | Example |
|--------|---------|---------|
| `yyyy` | 4-digit year | 2024 |
| `yy` | 2-digit year | 24 |
| `MM` | Month (01-12) | 06 |
| `MMM` | Month short | Jun |
| `MMMM` | Month full | June |
| `dd` | Day (01-31) | 15 |
| `EEEE` | Day name | Saturday |
| `HH` | Hour 24h (00-23) | 14 |
| `hh` | Hour 12h (01-12) | 02 |
| `mm` | Minutes | 30 |
| `ss` | Seconds | 45 |
| `a` | AM/PM | PM |

### Other Built-in Pipes

```html
<!-- JsonPipe — debug objects in template -->
<pre>{{ user | json }}</pre>
<!-- Output: { "name": "Rahul", "age": 25 } -->

<!-- SlicePipe — extract portion of array or string -->
{{ 'Hello World' | slice:0:5 }}          <!-- Hello -->
{{ [1,2,3,4,5] | slice:1:3 }}           <!-- [2,3] -->

<!-- KeyValuePipe — iterate over object properties -->
<div *ngFor="let item of user | keyvalue">
  {{ item.key }}: {{ item.value }}
</div>

<!-- AsyncPipe — subscribe to Observable/Promise in template -->
<p>{{ user$ | async | json }}</p>
```

--

## 11. Custom Pipes

### Basic Custom Pipe

```typescript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncate',
  standalone: true
})
export class TruncatePipe implements PipeTransform {
  transform(value: string, limit: number = 50, trail: string = '...'): string {
    if (!value) return '';
    if (value.length <= limit) return value;
    return value.substring(0, limit) + trail;
  }
}

// Usage
{{ longText | truncate }}              <!-- Default: 50 chars + ... -->
{{ longText | truncate:20 }}           <!-- 20 chars + ... -->
{{ longText | truncate:20:'>>>' }}     <!-- 20 chars + >>> -->
```

### Time Ago Pipe

```typescript
@Pipe({
  name: 'timeAgo',
  standalone: true
})
export class TimeAgoPipe implements PipeTransform {
  transform(value: Date | string): string {
    if (!value) return '';
    const date = new Date(value);
    const now = new Date();
    const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);

    if (seconds < 60) return 'just now';
    if (seconds < 3600) return `${Math.floor(seconds / 60)} minutes ago`;
    if (seconds < 86400) return `${Math.floor(seconds / 3600)} hours ago`;
    if (seconds < 2592000) return `${Math.floor(seconds / 86400)} days ago`;
    if (seconds < 31536000) return `${Math.floor(seconds / 2592000)} months ago`;
    return `${Math.floor(seconds / 31536000)} years ago`;
  }
}

// Usage
{{ comment.createdAt | timeAgo }}      <!-- "5 minutes ago" -->
```

### File Size Pipe

```typescript
@Pipe({
  name: 'fileSize',
  standalone: true
})
export class FileSizePipe implements PipeTransform {
  transform(bytes: number, decimals: number = 2): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(decimals)) + ' ' + sizes[i];
  }
}

// Usage
{{ 1024 | fileSize }}                  <!-- 1 KB -->
{{ 1048576 | fileSize }}               <!-- 1 MB -->
{{ 1073741824 | fileSize:1 }}          <!-- 1.0 GB -->
```

### Filter Pipe (Search)

```typescript
@Pipe({
  name: 'filter',
  standalone: true,
  pure: false  // Impure — recalculates on every change detection
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], field: string, searchTerm: string): any[] {
    if (!items || !searchTerm) return items;
    return items.filter(item =>
      item[field].toLowerCase().includes(searchTerm.toLowerCase())
    );
  }
}

// Usage
<input [(ngModel)]="searchTerm" placeholder="Search...">
@for (user of users | filter:'name':searchTerm; track user.id) {
  <div>{{ user.name }}</div>
}
```

### Sort Pipe

```typescript
@Pipe({
  name: 'sort',
  standalone: true
})
export class SortPipe implements PipeTransform {
  transform(items: any[], field: string, direction: 'asc' | 'desc' = 'asc'): any[] {
    if (!items || !field) return items;
    return [...items].sort((a, b) => {
      const valA = a[field];
      const valB = b[field];
      const comparison = valA > valB ? 1 : valA < valB ? -1 : 0;
      return direction === 'asc' ? comparison : -comparison;
    });
  }
}

// Usage
@for (user of users | sort:'name':'asc'; track user.id) {
  <div>{{ user.name }}</div>
}
```

--

## 12. Pure vs Impure Pipes

### Pure Pipes (Default)

- **Recalculate only when input reference changes** (not on mutation).
- Angular caches the result — if input hasn't changed, returns cached value.
- **Better performance**.
- Default behavior.

```typescript
@Pipe({
  name: 'truncate',
  standalone: true,
  pure: true  // Default — can omit
})
export class TruncatePipe implements PipeTransform {
  transform(value: string, limit: number = 50): string {
    return value.length > limit ? value.substring(0, limit) + '...' : value;
  }
}
```

**Gotcha with pure pipes:**
```typescript
// Pure pipe won't detect this (same array reference, just mutated):
this.users.push(newUser);  // Array reference didn't change!

// Fix: Create new array reference
this.users = [...this.users, newUser];  // New reference → pipe recalculates
```

### Impure Pipes

- **Recalculate on EVERY change detection cycle** (even if input hasn't changed).
- Can be expensive — use carefully.
- Needed when pipe depends on external state or needs to detect mutations.

```typescript
@Pipe({
  name: 'filter',
  standalone: true,
  pure: false  // Impure — runs on every change detection
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], searchTerm: string): any[] {
    if (!items || !searchTerm) return items;
    return items.filter(item =>
      item.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }
}
```

### Comparison

| Feature | Pure | Impure |
|---------|------|--------|
| **Recalculates** | Only when input reference changes | Every change detection cycle |
| **Performance** | Fast (cached) | Slow (runs frequently) |
| **Detects mutations** | No | Yes |
| **Default** | Yes | No (`pure: false`) |
| **Use for** | Simple transforms (date, currency) | Filters, sorts on mutable arrays |

### Best Practice

Avoid impure pipes for performance. Instead:
```typescript
// Instead of impure filter pipe, use component logic:
get filteredUsers(): User[] {
  return this.users.filter(u => u.name.includes(this.searchTerm));
}

// Or with signals (Angular 16+):
filteredUsers = computed(() =>
  this.users().filter(u => u.name.includes(this.searchTerm()))
);
```

--

## 13. Async Pipe

The `async` pipe subscribes to an Observable or Promise and returns the latest emitted value. It **automatically unsubscribes** when the component is destroyed.

### Basic Usage

```typescript
@Component({
  standalone: true,
  imports: [CommonModule, AsyncPipe],
  template: `
    <!-- Observable -->
    @if (users$ | async; as users) {
      @for (user of users; track user.id) {
        <div>{{ user.name }}</div>
      }
    }

    <!-- Promise -->
    <p>{{ greeting$ | async }}</p>
  `
})
export class UserListComponent {
  users$: Observable<User[]>;
  greeting$: Promise<string>;

  constructor(private userService: UserService) {
    this.users$ = this.userService.getUsers();
    this.greeting$ = Promise.resolve('Hello!');
  }
}
```

### Avoid Multiple Subscriptions

```html
<!-- BAD: 3 subscriptions = 3 API calls! -->
<p>{{ (user$ | async)?.name }}</p>
<p>{{ (user$ | async)?.email }}</p>
<p>{{ (user$ | async)?.age }}</p>

<!-- GOOD: Single subscription -->
@if (user$ | async; as user) {
  <p>{{ user.name }}</p>
  <p>{{ user.email }}</p>
  <p>{{ user.age }}</p>
}
```

### Async Pipe with Loading State

```typescript
@Component({
  template: `
    @if (users$ | async; as users) {
      @for (user of users; track user.id) {
        <div>{{ user.name }}</div>
      } @empty {
        <p>No users found.</p>
      }
    } @else {
      <p>Loading users...</p>
    }
  `
})
```

### Why Async Pipe is Preferred

| Feature | Manual subscribe() | async pipe |
|---------|-------------------|-----------|
| **Unsubscribe** | Must do manually | Automatic |
| **Memory leaks** | Risk if forgotten | No risk |
| **OnPush compatible** | Need markForCheck() | Works automatically |
| **Code** | More boilerplate | Cleaner template |

--

## 14. Chaining Pipes

Pipes can be chained — output of one pipe becomes input of the next.

```html
<!-- Chain multiple pipes -->
{{ user.name | uppercase | slice:0:10 }}
<!-- "RAHUL SHAR" -->

{{ birthday | date:'fullDate' | uppercase }}
<!-- "SATURDAY, JUNE 15, 2024" -->

{{ price | currency:'INR' | slice:1 }}
<!-- "1,234.50" (removes ₹ symbol) -->

{{ users | filter:'name':searchTerm | sort:'name':'asc' | slice:0:10 }}
<!-- Filter → Sort → Take first 10 -->
```

--

## 15. Practical Examples

### Highlight Search Term Pipe

```typescript
@Pipe({
  name: 'highlight',
  standalone: true
})
export class HighlightPipe implements PipeTransform {
  transform(text: string, search: string): string {
    if (!search || !text) return text;
    const regex = new RegExp(`(${search})`, 'gi');
    return text.replace(regex, '<mark>$1</mark>');
  }
}

// Usage (must use innerHTML for HTML output)
<p [innerHTML]="user.name | highlight:searchTerm"></p>
```

### Initials Pipe

```typescript
@Pipe({
  name: 'initials',
  standalone: true
})
export class InitialsPipe implements PipeTransform {
  transform(fullName: string): string {
    if (!fullName) return '';
    return fullName
      .split(' ')
      .map(word => word.charAt(0).toUpperCase())
      .join('');
  }
}

// Usage
{{ 'Rahul Kumar Sharma' | initials }}    <!-- RKS -->
```

### Ordinal Pipe

```typescript
@Pipe({
  name: 'ordinal',
  standalone: true
})
export class OrdinalPipe implements PipeTransform {
  transform(value: number): string {
    if (!value) return '';
    const suffixes = ['th', 'st', 'nd', 'rd'];
    const remainder = value % 100;
    const suffix = suffixes[(remainder - 20) % 10] || suffixes[remainder] || suffixes[0];
    return value + suffix;
  }
}

// Usage
{{ 1 | ordinal }}    <!-- 1st -->
{{ 2 | ordinal }}    <!-- 2nd -->
{{ 3 | ordinal }}    <!-- 3rd -->
{{ 11 | ordinal }}   <!-- 11th -->
{{ 21 | ordinal }}   <!-- 21st -->
```

### Pluralize Pipe

```typescript
@Pipe({
  name: 'pluralize',
  standalone: true
})
export class PluralizePipe implements PipeTransform {
  transform(count: number, singular: string, plural?: string): string {
    const word = count === 1 ? singular : (plural || singular + 's');
    return `${count} ${word}`;
  }
}

// Usage
{{ userCount | pluralize:'user' }}           <!-- "1 user" or "5 users" -->
{{ childCount | pluralize:'child':'children' }}  <!-- "1 child" or "3 children" -->
```

### Summary Table — Directives vs Pipes

| Feature | Directives | Pipes |
|---------|-----------|-------|
| **Purpose** | Modify DOM structure/behavior | Transform display data |
| **Where** | On elements as attributes | In template expressions with `\|` |
| **Types** | Structural, Attribute | Pure, Impure |
| **Examples** | `*ngIf`, `ngClass`, custom | `date`, `currency`, custom |
| **Decorator** | `@Directive` | `@Pipe` |
| **Interface** | — | `PipeTransform` |
