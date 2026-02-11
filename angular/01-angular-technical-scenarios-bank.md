# Angular Technical Proficiency Bank - Ultimate Guide

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Purpose:** A comprehensive repository of 120+ technical inquiries and scenarios covering Angular fundamentals, advanced concepts, coding problems, and tricky interview questions.

---

## Table of Contents

1. [Angular Basics & Architecture](#1-angular-basics--architecture)
2. [Components](#2-components)
3. [Directives](#3-directives)
4. [Data Binding & Communication](#4-data-binding--communication)
5. [Services & Dependency Injection](#5-services--dependency-injection)
6. [Routing & Navigation](#6-routing--navigation)
7. [Forms (Template-Driven & Reactive)](#7-forms-template-driven--reactive)
8. [Pipes](#8-pipes)
9. [HTTP Client & API Integration](#9-http-client--api-integration)
10. [RxJS & Observables](#10-rxjs--observables)
11. [Lifecycle Hooks](#11-lifecycle-hooks)
12. [Modules & Lazy Loading](#12-modules--lazy-loading)
13. [State Management](#13-state-management)
14. [Performance Optimization](#14-performance-optimization)
15. [Testing](#15-testing)
16. [Tricky Output & Gotcha Questions](#16-tricky-output--gotcha-questions)
17. [Coding Problems](#17-coding-problems)
18. [Interview Questions & Answers](#18-interview-questions--answers)

---

## 1. Angular Basics & Architecture

### Inquiry 1: What is Angular?
**Scenario:** Explain Angular to a beginner.
**Explanation:** Angular is a **TypeScript-based** front-end framework by Google for building Single Page Applications (SPAs). It provides a complete solution — components, routing, forms, HTTP, testing — all built-in.

| Feature | Description |
|---------|-------------|
| **Language** | TypeScript (superset of JavaScript) |
| **Architecture** | Component-based |
| **Rendering** | Client-side (also supports SSR with Angular Universal) |
| **CLI** | `ng` command for scaffolding, building, testing |
| **Current Version** | Angular 17+ (standalone components by default) |

### Inquiry 2: Angular Architecture
**Scenario:** Explain the building blocks.
**Explanation:**

```
Angular Application
├── Modules (@NgModule)
│   ├── Components (@Component)
│   │   ├── Template (HTML)
│   │   ├── Styles (CSS/SCSS)
│   │   └── Class (TypeScript)
│   ├── Services (@Injectable)
│   ├── Directives (@Directive)
│   ├── Pipes (@Pipe)
│   └── Guards (CanActivate, etc.)
├── Routing (RouterModule)
└── Dependency Injection (DI system)
```

### Inquiry 3: Angular vs AngularJS
**Scenario:** What changed from AngularJS to Angular?
**Explanation:**

| Feature | AngularJS (1.x) | Angular (2+) |
|---------|-----------------|-------------|
| **Language** | JavaScript | TypeScript |
| **Architecture** | MVC (Controllers, $scope) | Component-based |
| **Data Binding** | Two-way (default) | One-way (default), two-way optional |
| **Mobile** | Not optimized | Mobile-first |
| **Performance** | Digest cycle (slow) | Change detection (fast) |
| **CLI** | None | `ng` CLI |
| **Dependency Injection** | Basic | Hierarchical DI |

### Inquiry 4: Angular CLI Commands
**Scenario:** Essential commands?
**Explanation:**

```bash
# Create new project
ng new my-app

# Generate components, services, etc.
ng generate component user-list    # or ng g c user-list
ng generate service user           # or ng g s user
ng generate module admin           # or ng g m admin
ng generate pipe currency-format   # or ng g p currency-format
ng generate guard auth             # or ng g guard auth
ng generate directive highlight    # or ng g d highlight

# Run dev server
ng serve                           # http://localhost:4200
ng serve --port 3000               # custom port
ng serve --open                    # auto-open browser

# Build for production
ng build --configuration production

# Run tests
ng test                            # Unit tests (Karma)
ng e2e                             # End-to-end tests

# Lint
ng lint
```

### Inquiry 5: Standalone Components (Angular 14+)
**Scenario:** What are standalone components?
**Explanation:** Components that don't need to be declared in an `NgModule`. They manage their own imports directly.

```typescript
// Traditional (with NgModule)
@NgModule({
  declarations: [UserComponent],
  imports: [CommonModule, FormsModule],
})
export class UserModule {}

// Standalone (Angular 14+) — no NgModule needed
@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule, FormsModule],  // imports directly in component
  template: `<h1>{{ name }}</h1>`
})
export class UserComponent {
  name = 'Rahul';
}
```

**Angular 17+:** Standalone is the **default** for new projects.

### Inquiry 6: TypeScript Basics for Angular
**Scenario:** Essential TypeScript concepts?
**Explanation:**

```typescript
// 1. Type annotations
let name: string = 'Rahul';
let age: number = 25;
let isActive: boolean = true;
let skills: string[] = ['Angular', 'Java'];

// 2. Interfaces
interface Employee {
  id: number;
  name: string;
  email?: string;  // optional
  readonly dept: string;  // cannot be changed after creation
}

// 3. Enums
enum Status {
  Active = 'ACTIVE',
  Inactive = 'INACTIVE',
  Pending = 'PENDING'
}

// 4. Generics
function getFirst<T>(arr: T[]): T {
  return arr[0];
}

// 5. Access modifiers
class User {
  public name: string;       // accessible everywhere (default)
  private password: string;  // only within class
  protected role: string;    // within class + subclasses

  constructor(name: string, password: string) {
    this.name = name;
    this.password = password;
  }
}

// 6. Decorators (used heavily in Angular)
@Component({...})   // Class decorator
@Input()            // Property decorator
@Output()           // Property decorator
@ViewChild()        // Property decorator
@HostListener()     // Method decorator
```

---

## 2. Components

### Inquiry 7: Component Anatomy
**Scenario:** Explain the parts of a component.
**Explanation:**

```typescript
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-list',          // HTML tag: <app-user-list></app-user-list>
  templateUrl: './user-list.component.html',  // or template: `<h1>Inline</h1>`
  styleUrls: ['./user-list.component.scss'],  // or styles: [`h1 { color: red; }`]
  standalone: true,
  imports: [CommonModule]
})
export class UserListComponent implements OnInit {
  users: string[] = [];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.users = this.userService.getUsers();
  }
}
```

| Part | Purpose |
|------|---------|
| **selector** | Custom HTML tag name |
| **template/templateUrl** | HTML view |
| **styles/styleUrls** | Scoped CSS (only affects this component) |
| **class** | Logic, data, methods |

### Inquiry 8: Component Communication
**Scenario:** How do components talk to each other?
**Explanation:**

| Method | Direction | Use Case |
|--------|-----------|----------|
| **@Input()** | Parent → Child | Pass data down |
| **@Output()** | Child → Parent | Emit events up |
| **Service** | Any → Any | Shared state, sibling communication |
| **@ViewChild()** | Parent → Child | Access child component/element directly |
| **@ContentChild()** | Parent → Projected content | Access ng-content children |
| **Router params** | Route → Component | Pass data via URL |

### Inquiry 9: @Input() and @Output()
**Scenario:** Pass data between parent and child.
**Explanation:**

```typescript
// Child component
@Component({
  selector: 'app-child',
  standalone: true,
  template: `
    <p>Hello, {{ name }}!</p>
    <button (click)="sendMessage()">Send to Parent</button>
  `
})
export class ChildComponent {
  @Input() name: string = '';                    // Receives from parent
  @Output() messageEvent = new EventEmitter<string>();  // Sends to parent

  sendMessage() {
    this.messageEvent.emit('Hello from Child!');
  }
}

// Parent component
@Component({
  selector: 'app-parent',
  standalone: true,
  imports: [ChildComponent],
  template: `
    <app-child
      [name]="parentName"
      (messageEvent)="receiveMessage($event)">
    </app-child>
    <p>{{ childMessage }}</p>
  `
})
export class ParentComponent {
  parentName = 'Rahul';
  childMessage = '';

  receiveMessage(msg: string) {
    this.childMessage = msg;
  }
}
```

### Inquiry 10: @ViewChild()
**Scenario:** Access child component from parent.
**Explanation:**

```typescript
// Child
@Component({
  selector: 'app-timer',
  standalone: true,
  template: `<p>{{ seconds }}s</p>`
})
export class TimerComponent {
  seconds = 0;

  start() {
    setInterval(() => this.seconds++, 1000);
  }

  reset() {
    this.seconds = 0;
  }
}

// Parent — access child methods directly
@Component({
  selector: 'app-parent',
  standalone: true,
  imports: [TimerComponent],
  template: `
    <app-timer></app-timer>
    <button (click)="startTimer()">Start</button>
    <button (click)="resetTimer()">Reset</button>
  `
})
export class ParentComponent implements AfterViewInit {
  @ViewChild(TimerComponent) timer!: TimerComponent;

  ngAfterViewInit() {
    // timer is available here (not in constructor or ngOnInit)
  }

  startTimer() { this.timer.start(); }
  resetTimer() { this.timer.reset(); }
}
```

**Important:** `@ViewChild` is available only after `ngAfterViewInit`, not in `ngOnInit`.

### Inquiry 11: Content Projection (ng-content)
**Scenario:** How to create reusable wrapper components?
**Explanation:**

```typescript
// Card component (reusable wrapper)
@Component({
  selector: 'app-card',
  standalone: true,
  template: `
    <div class="card">
      <div class="card-header">
        <ng-content select="[header]"></ng-content>
      </div>
      <div class="card-body">
        <ng-content></ng-content>
      </div>
      <div class="card-footer">
        <ng-content select="[footer]"></ng-content>
      </div>
    </div>
  `
})
export class CardComponent {}

// Usage — project content into slots
<app-card>
  <h2 header>User Profile</h2>
  <p>Name: Rahul</p>
  <p>Age: 25</p>
  <button footer>Save</button>
</app-card>
```

---

## 3. Directives

### Inquiry 12: Types of Directives
**Scenario:** Classification?
**Explanation:**

| Type | Purpose | Examples |
|------|---------|---------|
| **Component** | Directive with a template | `@Component` (every component is a directive) |
| **Structural** | Change DOM structure (add/remove elements) | `*ngIf`, `*ngFor`, `*ngSwitch` |
| **Attribute** | Change appearance/behavior of element | `ngClass`, `ngStyle`, custom directives |

### Inquiry 13: *ngIf
**Scenario:** Conditional rendering.
**Explanation:**

```html
<!-- Basic -->
<div *ngIf="isLoggedIn">Welcome, {{ username }}!</div>

<!-- With else -->
<div *ngIf="isLoggedIn; else loginTemplate">
  Welcome, {{ username }}!
</div>
<ng-template #loginTemplate>
  <p>Please log in.</p>
</ng-template>

<!-- With then and else -->
<div *ngIf="isLoggedIn; then loggedIn; else loggedOut"></div>
<ng-template #loggedIn><p>Welcome!</p></ng-template>
<ng-template #loggedOut><p>Please log in.</p></ng-template>

<!-- Angular 17+ @if syntax -->
@if (isLoggedIn) {
  <p>Welcome, {{ username }}!</p>
} @else {
  <p>Please log in.</p>
}
```

### Inquiry 14: *ngFor
**Scenario:** Loop through a list.
**Explanation:**

```html
<!-- Basic -->
<ul>
  <li *ngFor="let user of users">{{ user.name }}</li>
</ul>

<!-- With index, first, last, even, odd -->
<ul>
  <li *ngFor="let user of users; let i = index; let isFirst = first; let isLast = last; let isEven = even">
    {{ i + 1 }}. {{ user.name }}
    <span *ngIf="isFirst"> (First)</span>
    <span *ngIf="isLast"> (Last)</span>
  </li>
</ul>

<!-- trackBy — performance optimization (prevents re-rendering entire list) -->
<li *ngFor="let user of users; trackBy: trackByUserId">{{ user.name }}</li>

// In component:
trackByUserId(index: number, user: User): number {
  return user.id;  // Angular tracks by this value instead of object reference
}

<!-- Angular 17+ @for syntax -->
@for (user of users; track user.id) {
  <li>{{ user.name }}</li>
} @empty {
  <li>No users found.</li>
}
```

### Inquiry 15: *ngSwitch
**Scenario:** Multiple conditions.
**Explanation:**

```html
<div [ngSwitch]="role">
  <p *ngSwitchCase="'admin'">Admin Dashboard</p>
  <p *ngSwitchCase="'user'">User Dashboard</p>
  <p *ngSwitchCase="'guest'">Guest View</p>
  <p *ngSwitchDefault>Unknown Role</p>
</div>

<!-- Angular 17+ @switch syntax -->
@switch (role) {
  @case ('admin') { <p>Admin Dashboard</p> }
  @case ('user') { <p>User Dashboard</p> }
  @default { <p>Unknown Role</p> }
}
```

### Inquiry 16: ngClass and ngStyle
**Scenario:** Dynamic styling.
**Explanation:**

```html
<!-- ngClass — toggle CSS classes -->
<div [ngClass]="{'active': isActive, 'disabled': isDisabled, 'highlight': isHighlighted}">
  Content
</div>

<!-- ngClass with array -->
<div [ngClass]="['btn', 'btn-primary']">Button</div>

<!-- ngClass with method -->
<div [ngClass]="getClasses()">Content</div>

<!-- ngStyle — inline styles -->
<div [ngStyle]="{'color': textColor, 'font-size': fontSize + 'px', 'background-color': isActive ? 'green' : 'red'}">
  Styled Content
</div>
```

### Inquiry 17: Custom Directive
**Scenario:** Create a highlight directive.
**Explanation:**

```typescript
import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appHighlight]',
  standalone: true
})
export class HighlightDirective {
  @Input() appHighlight: string = 'yellow';  // Custom color
  @Input() defaultColor: string = '';

  constructor(private el: ElementRef) {}

  @HostListener('mouseenter') onMouseEnter() {
    this.highlight(this.appHighlight || this.defaultColor || 'yellow');
  }

  @HostListener('mouseleave') onMouseLeave() {
    this.highlight('');
  }

  private highlight(color: string) {
    this.el.nativeElement.style.backgroundColor = color;
  }
}

// Usage
<p appHighlight="lightblue">Hover over me!</p>
<p [appHighlight]="'pink'" defaultColor="lightgray">Custom colors</p>
```

---

## 4. Data Binding & Communication

### Inquiry 18: Types of Data Binding
**Scenario:** Explain all binding types.
**Explanation:**

| Type | Syntax | Direction | Example |
|------|--------|-----------|---------|
| **Interpolation** | `{{ }}` | Component → Template | `{{ user.name }}` |
| **Property Binding** | `[property]` | Component → Template | `[src]="imageUrl"` |
| **Event Binding** | `(event)` | Template → Component | `(click)="onClick()"` |
| **Two-way Binding** | `[(ngModel)]` | Both ways | `[(ngModel)]="username"` |

```html
<!-- 1. Interpolation (one-way: component → view) -->
<h1>Hello, {{ name }}</h1>
<p>{{ 1 + 1 }}</p>
<p>{{ getFullName() }}</p>

<!-- 2. Property Binding (one-way: component → view) -->
<img [src]="imageUrl">
<button [disabled]="isDisabled">Submit</button>
<input [value]="username">

<!-- 3. Event Binding (one-way: view → component) -->
<button (click)="onSubmit()">Submit</button>
<input (keyup.enter)="onSearch()">
<input (input)="onInput($event)">

<!-- 4. Two-way Binding (both ways) -->
<input [(ngModel)]="username">
<!-- Equivalent to: -->
<input [ngModel]="username" (ngModelChange)="username = $event">
```

### Inquiry 19: Property Binding vs Interpolation
**Scenario:** When to use which?
**Explanation:**

```html
<!-- Both work for strings -->
<img src="{{ imageUrl }}">        <!-- Interpolation -->
<img [src]="imageUrl">            <!-- Property binding -->

<!-- Property binding is REQUIRED for non-string values -->
<button [disabled]="isDisabled">  <!-- boolean — must use property binding -->
<div [hidden]="isHidden">         <!-- boolean -->
<input [value]="count">           <!-- number -->

<!-- Interpolation CANNOT set non-string properties -->
<button disabled="{{ isDisabled }}">  <!-- WRONG! Sets string "true"/"false", not boolean -->
```

**Rule:** Use **property binding** for non-string values. Use **interpolation** for displaying text.

### Inquiry 20: Template Reference Variables
**Scenario:** Access DOM elements in template.
**Explanation:**

```html
<!-- #ref creates a reference to the element -->
<input #nameInput type="text" placeholder="Enter name">
<button (click)="greet(nameInput.value)">Greet</button>

<!-- Reference to a component -->
<app-timer #timer></app-timer>
<button (click)="timer.start()">Start Timer</button>

<!-- Reference with ngModel -->
<input #emailInput="ngModel" [(ngModel)]="email" required email>
<div *ngIf="emailInput.invalid && emailInput.touched">
  <span *ngIf="emailInput.errors?.['required']">Email is required</span>
  <span *ngIf="emailInput.errors?.['email']">Invalid email format</span>
</div>
```

### Inquiry 21: Signals (Angular 16+)
**Scenario:** New reactive primitive.
**Explanation:** Signals are a **simpler alternative to RxJS** for managing reactive state within components.

```typescript
import { Component, signal, computed, effect } from '@angular/core';

@Component({
  selector: 'app-counter',
  standalone: true,
  template: `
    <p>Count: {{ count() }}</p>
    <p>Double: {{ doubleCount() }}</p>
    <button (click)="increment()">+1</button>
    <button (click)="decrement()">-1</button>
    <button (click)="reset()">Reset</button>
  `
})
export class CounterComponent {
  // Signal — reactive value
  count = signal(0);

  // Computed — derived value (auto-updates when count changes)
  doubleCount = computed(() => this.count() * 2);

  constructor() {
    // Effect — side effect when signal changes
    effect(() => {
      console.log('Count changed to:', this.count());
    });
  }

  increment() { this.count.update(c => c + 1); }
  decrement() { this.count.update(c => c - 1); }
  reset() { this.count.set(0); }
}
```

| Feature | Signal | Observable (RxJS) |
|---------|--------|-------------------|
| **Complexity** | Simple | Complex |
| **Subscription** | Auto (in template) | Manual (subscribe/async pipe) |
| **Use case** | Component state | Async operations, HTTP, events |
| **Memory leaks** | No risk | Must unsubscribe |

---

## 5. Services & Dependency Injection

### Inquiry 22: What is a Service?
**Scenario:** Why use services?
**Explanation:** A service is a class with a focused purpose — data fetching, business logic, shared state. Components should be thin (only UI logic); heavy logic goes in services.

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'  // Singleton — available app-wide, no need to add to providers
})
export class UserService {
  private apiUrl = 'https://api.example.com/users';

  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }
}
```

### Inquiry 23: Dependency Injection (DI)
**Scenario:** How does Angular DI work?
**Explanation:** Angular creates and manages instances of services and injects them into components via the constructor.

```typescript
// Angular sees the constructor parameter type and injects the service
@Component({ ... })
export class UserListComponent {
  constructor(private userService: UserService) {}
  // Angular automatically creates/finds UserService instance and passes it
}
```

**DI Hierarchy:**

```
Root Injector (providedIn: 'root')
    └── Module Injector (providers in @NgModule)
        └── Component Injector (providers in @Component)
```

| Level | Scope | Example |
|-------|-------|---------|
| **Root** | Singleton for entire app | `providedIn: 'root'` |
| **Module** | Singleton within module | `providers: [MyService]` in NgModule |
| **Component** | New instance per component | `providers: [MyService]` in Component |

### Inquiry 24: providedIn Options
**Scenario:** Different ways to provide a service.
**Explanation:**

```typescript
// 1. Root — singleton, tree-shakable (RECOMMENDED)
@Injectable({ providedIn: 'root' })
export class UserService {}

// 2. Module — singleton within that module
@Injectable({ providedIn: AdminModule })
export class AdminService {}

// 3. Component-level — new instance per component
@Component({
  providers: [LoggerService]  // Each component gets its own LoggerService
})
export class UserComponent {
  constructor(private logger: LoggerService) {}
}

// 4. 'any' — new instance per lazy-loaded module
@Injectable({ providedIn: 'any' })
export class AnalyticsService {}
```

### Inquiry 25: Service Communication Between Components
**Scenario:** Sibling components sharing data.
**Explanation:**

```typescript
// Shared service with BehaviorSubject
@Injectable({ providedIn: 'root' })
export class SharedService {
  private messageSource = new BehaviorSubject<string>('default message');
  currentMessage$ = this.messageSource.asObservable();

  changeMessage(message: string) {
    this.messageSource.next(message);
  }
}

// Component A — sends message
@Component({ ... })
export class SenderComponent {
  constructor(private shared: SharedService) {}

  send() {
    this.shared.changeMessage('Hello from Sender!');
  }
}

// Component B — receives message
@Component({
  template: `<p>{{ message }}</p>`
})
export class ReceiverComponent implements OnInit {
  message = '';

  constructor(private shared: SharedService) {}

  ngOnInit() {
    this.shared.currentMessage$.subscribe(msg => this.message = msg);
  }
}
```

---

## 6. Routing & Navigation

### Inquiry 26: Basic Routing Setup
**Scenario:** Configure routes.
**Explanation:**

```typescript
// app.routes.ts (standalone approach — Angular 17+)
import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'users', component: UserListComponent },
  { path: 'users/:id', component: UserDetailComponent },  // Route parameter
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard] },
  { path: '**', component: NotFoundComponent }  // Wildcard — must be LAST
];

// app.component.html
<nav>
  <a routerLink="/home" routerLinkActive="active">Home</a>
  <a routerLink="/users" routerLinkActive="active">Users</a>
</nav>
<router-outlet></router-outlet>  <!-- Components render here -->
```

### Inquiry 27: Route Parameters
**Scenario:** Pass and read URL parameters.
**Explanation:**

```typescript
// Route config
{ path: 'users/:id', component: UserDetailComponent }

// Navigate programmatically
this.router.navigate(['/users', userId]);

// Read route parameter
@Component({ ... })
export class UserDetailComponent implements OnInit {
  userId!: number;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    // Method 1: Snapshot (one-time read)
    this.userId = +this.route.snapshot.paramMap.get('id')!;

    // Method 2: Observable (reacts to param changes — use when same component reloads)
    this.route.paramMap.subscribe(params => {
      this.userId = +params.get('id')!;
      this.loadUser(this.userId);
    });
  }
}

// Query parameters: /users?page=2&sort=name
this.router.navigate(['/users'], { queryParams: { page: 2, sort: 'name' } });

// Read query params
this.route.queryParamMap.subscribe(params => {
  const page = params.get('page');
  const sort = params.get('sort');
});
```

### Inquiry 28: Lazy Loading
**Scenario:** Load modules on demand.
**Explanation:**

```typescript
// Route config — lazy load AdminModule
export const routes: Routes = [
  {
    path: 'admin',
    loadComponent: () => import('./admin/admin.component').then(m => m.AdminComponent)
  },
  // Lazy load entire module (older approach)
  {
    path: 'reports',
    loadChildren: () => import('./reports/reports.module').then(m => m.ReportsModule)
  }
];
```

**Benefits:**
*   Reduces initial bundle size (faster first load).
*   Module is downloaded only when user navigates to that route.
*   Each lazy-loaded module gets its own chunk file.

### Inquiry 29: Route Guards
**Scenario:** Protect routes.
**Explanation:**

```typescript
// Functional guard (Angular 15+ — recommended)
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }
  return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};

// Usage in routes
{ path: 'admin', component: AdminComponent, canActivate: [authGuard] }

// Types of guards:
// canActivate     — Can the user navigate TO this route?
// canDeactivate   — Can the user navigate AWAY from this route? (unsaved changes)
// canMatch        — Should this route be considered for matching?
// resolve         — Pre-fetch data before route activates
```

```typescript
// canDeactivate — prevent leaving with unsaved changes
export const unsavedChangesGuard: CanDeactivateFn<EditComponent> = (component) => {
  if (component.hasUnsavedChanges()) {
    return confirm('You have unsaved changes. Leave anyway?');
  }
  return true;
};
```

---

## 7. Forms (Template-Driven & Reactive)

### Inquiry 30: Template-Driven vs Reactive Forms
**Scenario:** When to use which?
**Explanation:**

| Feature | Template-Driven | Reactive |
|---------|----------------|----------|
| **Setup** | FormsModule | ReactiveFormsModule |
| **Logic in** | Template (HTML) | Component (TypeScript) |
| **Validation** | Directives (`required`, `minlength`) | Validators in code |
| **Complexity** | Simple forms | Complex forms |
| **Testing** | Harder (depends on DOM) | Easier (pure TypeScript) |
| **Dynamic** | Difficult | Easy (add/remove controls) |
| **Best for** | Login, simple contact forms | Registration, multi-step, dynamic forms |

### Inquiry 31: Template-Driven Form
**Scenario:** Simple login form.
**Explanation:**

```html
<!-- Requires FormsModule -->
<form #loginForm="ngForm" (ngSubmit)="onSubmit(loginForm)">
  <div>
    <label>Email:</label>
    <input type="email" name="email" [(ngModel)]="user.email"
           required email #emailInput="ngModel">
    <div *ngIf="emailInput.invalid && emailInput.touched">
      <span *ngIf="emailInput.errors?.['required']">Email is required</span>
      <span *ngIf="emailInput.errors?.['email']">Invalid email</span>
    </div>
  </div>

  <div>
    <label>Password:</label>
    <input type="password" name="password" [(ngModel)]="user.password"
           required minlength="6" #passInput="ngModel">
    <div *ngIf="passInput.invalid && passInput.touched">
      <span *ngIf="passInput.errors?.['required']">Password is required</span>
      <span *ngIf="passInput.errors?.['minlength']">Min 6 characters</span>
    </div>
  </div>

  <button type="submit" [disabled]="loginForm.invalid">Login</button>
</form>
```

```typescript
export class LoginComponent {
  user = { email: '', password: '' };

  onSubmit(form: NgForm) {
    if (form.valid) {
      console.log('Form data:', this.user);
    }
  }
}
```

### Inquiry 32: Reactive Form
**Scenario:** Registration form with validation.
**Explanation:**

```typescript
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  template: `
    <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
      <input formControlName="name" placeholder="Name">
      <div *ngIf="f['name'].invalid && f['name'].touched">
        Name is required
      </div>

      <input formControlName="email" placeholder="Email">
      <div *ngIf="f['email'].invalid && f['email'].touched">
        <span *ngIf="f['email'].errors?.['required']">Email required</span>
        <span *ngIf="f['email'].errors?.['email']">Invalid email</span>
      </div>

      <input formControlName="password" type="password" placeholder="Password">

      <input formControlName="confirmPassword" type="password" placeholder="Confirm Password">
      <div *ngIf="registerForm.errors?.['passwordMismatch'] && f['confirmPassword'].touched">
        Passwords don't match
      </div>

      <button [disabled]="registerForm.invalid">Register</button>
    </form>
  `
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  // Custom cross-field validator
  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirm = form.get('confirmPassword')?.value;
    return password === confirm ? null : { passwordMismatch: true };
  }

  get f() { return this.registerForm.controls; }

  onSubmit() {
    if (this.registerForm.valid) {
      console.log(this.registerForm.value);
    }
  }
}
```

### Inquiry 33: Dynamic Form (FormArray)
**Scenario:** Add/remove form fields dynamically.
**Explanation:**

```typescript
@Component({
  template: `
    <form [formGroup]="orderForm">
      <div formArrayName="items">
        @for (item of items.controls; track $index; let i = $index) {
          <div [formGroupName]="i">
            <input formControlName="name" placeholder="Item name">
            <input formControlName="quantity" type="number" placeholder="Qty">
            <button (click)="removeItem(i)">Remove</button>
          </div>
        }
      </div>
      <button (click)="addItem()">Add Item</button>
    </form>
  `
})
export class OrderComponent {
  orderForm = this.fb.group({
    items: this.fb.array([])
  });

  constructor(private fb: FormBuilder) {}

  get items() {
    return this.orderForm.get('items') as FormArray;
  }

  addItem() {
    this.items.push(this.fb.group({
      name: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]]
    }));
  }

  removeItem(index: number) {
    this.items.removeAt(index);
  }
}
```

---

## 8. Pipes

### Inquiry 34: Built-in Pipes
**Scenario:** Common pipes?
**Explanation:**

```html
<!-- String pipes -->
{{ 'hello world' | uppercase }}         <!-- HELLO WORLD -->
{{ 'HELLO WORLD' | lowercase }}         <!-- hello world -->
{{ 'hello world' | titlecase }}         <!-- Hello World -->

<!-- Number pipes -->
{{ 3.14159 | number:'1.2-3' }}          <!-- 3.142 (min 1 integer, 2-3 decimal) -->
{{ 0.85 | percent }}                     <!-- 85% -->
{{ 1234.5 | currency:'INR' }}           <!-- ₹1,234.50 -->
{{ 1234.5 | currency:'USD':'symbol' }}  <!-- $1,234.50 -->

<!-- Date pipes -->
{{ today | date:'short' }}              <!-- 2/11/26, 5:55 AM -->
{{ today | date:'fullDate' }}           <!-- Tuesday, February 11, 2026 -->
{{ today | date:'dd/MM/yyyy' }}         <!-- 11/02/2026 -->
{{ today | date:'yyyy-MM-dd HH:mm' }}  <!-- 2026-02-11 05:55 -->

<!-- JSON pipe (debugging) -->
{{ user | json }}                        <!-- {"name":"Rahul","age":25} -->

<!-- Slice pipe -->
{{ [1,2,3,4,5] | slice:1:3 }}          <!-- [2,3] -->
{{ 'Hello World' | slice:0:5 }}         <!-- Hello -->

<!-- Async pipe (auto-subscribes to Observable/Promise) -->
{{ users$ | async }}
<div *ngFor="let user of users$ | async">{{ user.name }}</div>

<!-- KeyValue pipe (iterate over object) -->
<div *ngFor="let item of myObject | keyvalue">
  {{ item.key }}: {{ item.value }}
</div>
```

### Inquiry 35: Custom Pipe
**Scenario:** Create a pipe to truncate text.
**Explanation:**

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
{{ longText | truncate }}           <!-- Default: 50 chars + ... -->
{{ longText | truncate:20 }}        <!-- 20 chars + ... -->
{{ longText | truncate:30:'>>>' }}  <!-- 30 chars + >>> -->
```

### Inquiry 36: Pure vs Impure Pipes
**Scenario:** What's the difference?
**Explanation:**

| Feature | Pure Pipe (default) | Impure Pipe |
|---------|-------------------|-------------|
| **Execution** | Only when input **reference** changes | Every change detection cycle |
| **Performance** | Fast (cached) | Slow (runs frequently) |
| **Use case** | Stateless transformations | Filtering arrays, async operations |
| **Declaration** | `pure: true` (default) | `pure: false` |

```typescript
// Pure pipe — does NOT detect changes inside arrays/objects
@Pipe({ name: 'filter', pure: true })  // Won't re-run when array items change

// Impure pipe — detects ALL changes (expensive!)
@Pipe({ name: 'filter', pure: false, standalone: true })
export class FilterPipe implements PipeTransform {
  transform(items: any[], searchText: string): any[] {
    if (!items || !searchText) return items;
    return items.filter(item =>
      item.name.toLowerCase().includes(searchText.toLowerCase())
    );
  }
}

// Usage
<li *ngFor="let user of users | filter:searchText">{{ user.name }}</li>
```

**Warning:** Impure pipes run on **every** change detection cycle. For filtering, prefer doing it in the component class instead.

### Inquiry 37: Async Pipe
**Scenario:** Why is it important?
**Explanation:** The `async` pipe **auto-subscribes** to Observables/Promises and **auto-unsubscribes** when the component is destroyed. Prevents memory leaks.

```typescript
@Component({
  template: `
    <!-- WITHOUT async pipe — manual subscribe (must unsubscribe!) -->
    <!-- <div *ngFor="let user of users">{{ user.name }}</div> -->

    <!-- WITH async pipe — auto-subscribe + auto-unsubscribe -->
    <div *ngFor="let user of users$ | async">{{ user.name }}</div>

    <!-- Loading state -->
    @if (users$ | async; as users) {
      <div *ngFor="let user of users">{{ user.name }}</div>
    } @else {
      <p>Loading...</p>
    }
  `
})
export class UserListComponent {
  users$: Observable<User[]>;

  constructor(private userService: UserService) {
    this.users$ = this.userService.getUsers();
  }
  // No ngOnDestroy needed! async pipe handles cleanup
}
```

---

## 9. HTTP Client & API Integration

### Inquiry 38: HttpClient Setup
**Scenario:** How to make API calls?
**Explanation:**

```typescript
// app.config.ts (Angular 17+ standalone)
import { provideHttpClient } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [provideHttpClient()]
};

// Service — all HTTP methods
@Injectable({ providedIn: 'root' })
export class UserService {
  private api = 'https://api.example.com/users';

  constructor(private http: HttpClient) {}

  // GET
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.api);
  }

  // GET with params
  searchUsers(name: string, page: number): Observable<User[]> {
    const params = new HttpParams()
      .set('name', name)
      .set('page', page.toString());
    return this.http.get<User[]>(this.api, { params });
  }

  // POST
  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.api, user);
  }

  // PUT
  updateUser(id: number, user: User): Observable<User> {
    return this.http.put<User>(`${this.api}/${id}`, user);
  }

  // DELETE
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
```

### Inquiry 39: Error Handling in HTTP
**Scenario:** How to handle API errors?
**Explanation:**

```typescript
import { catchError, retry, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.api).pipe(
      retry(2),  // Retry failed request 2 times
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (error.status === 0) {
      errorMessage = 'Network error. Check your connection.';
    } else if (error.status === 404) {
      errorMessage = 'Resource not found.';
    } else if (error.status === 401) {
      errorMessage = 'Unauthorized. Please login.';
    } else if (error.status === 500) {
      errorMessage = 'Server error. Try again later.';
    } else {
      errorMessage = `Error ${error.status}: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}

// In component
this.userService.getUsers().subscribe({
  next: (users) => this.users = users,
  error: (err) => this.errorMessage = err.message,
  complete: () => console.log('Done')
});
```

### Inquiry 40: HTTP Interceptors
**Scenario:** Add auth token to every request.
**Explanation:** Interceptors sit between your app and the server. They can modify requests/responses globally.

```typescript
// auth.interceptor.ts (functional — Angular 15+)
import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  if (token) {
    const cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(cloned);
  }
  return next(req);
};

// logging.interceptor.ts
export const loggingInterceptor: HttpInterceptorFn = (req, next) => {
  console.log(`${req.method} ${req.url}`);
  const started = Date.now();

  return next(req).pipe(
    tap({
      next: () => console.log(`${req.url} took ${Date.now() - started}ms`),
      error: (err) => console.error(`${req.url} failed:`, err)
    })
  );
};

// Register in app.config.ts
export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor, loggingInterceptor]))
  ]
};
```

### Inquiry 41: Loading Spinner Pattern
**Scenario:** Show loading indicator during API calls.
**Explanation:**

```typescript
@Component({
  template: `
    @if (loading) {
      <div class="spinner">Loading...</div>
    }
    @if (error) {
      <div class="error">{{ error }}</div>
    }
    @for (user of users; track user.id) {
      <div>{{ user.name }}</div>
    }
  `
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  loading = false;
  error = '';

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loading = true;
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }
}
```

---

## 10. RxJS & Observables

### Inquiry 42: Observable vs Promise
**Scenario:** What's the difference?
**Explanation:**

| Feature | Promise | Observable |
|---------|---------|-----------|
| **Values** | Single value | Multiple values over time |
| **Lazy** | No (executes immediately) | Yes (executes only on subscribe) |
| **Cancellable** | No | Yes (unsubscribe) |
| **Operators** | `.then()`, `.catch()` | `pipe()`, `map()`, `filter()`, etc. |
| **Use case** | One-time async (fetch) | Streams (events, WebSocket, HTTP) |

```typescript
// Promise — executes immediately, single value
const promise = fetch('/api/users').then(res => res.json());

// Observable — lazy, can emit multiple values
const observable = this.http.get('/api/users');
// Nothing happens until:
observable.subscribe(data => console.log(data));
```

### Inquiry 43: Common RxJS Operators
**Scenario:** Most used operators?
**Explanation:**

```typescript
import { map, filter, tap, catchError, switchMap, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs/operators';

// map — transform each value
this.http.get<User[]>('/api/users').pipe(
  map(users => users.filter(u => u.active))
);

// filter — only pass values that meet condition
of(1, 2, 3, 4, 5).pipe(
  filter(n => n % 2 === 0)  // 2, 4
);

// tap — side effect without modifying value (logging, debugging)
this.http.get<User[]>('/api/users').pipe(
  tap(users => console.log('Fetched:', users.length)),
  map(users => users.filter(u => u.active))
);

// switchMap — cancel previous inner observable, use latest
// Perfect for search-as-you-type
this.searchInput.valueChanges.pipe(
  debounceTime(300),           // Wait 300ms after last keystroke
  distinctUntilChanged(),       // Only if value actually changed
  switchMap(term => this.userService.search(term))  // Cancel previous request
);

// mergeMap — run all inner observables in parallel
// Use for: saving multiple items simultaneously
userIds.pipe(
  mergeMap(id => this.userService.getUser(id))
);

// concatMap — run inner observables one after another (sequential)
// Use for: ordered operations
actions.pipe(
  concatMap(action => this.processAction(action))
);

// forkJoin — wait for ALL observables to complete
forkJoin({
  users: this.userService.getUsers(),
  departments: this.deptService.getDepts(),
  roles: this.roleService.getRoles()
}).subscribe(({ users, departments, roles }) => {
  // All three completed
});

// combineLatest — emit when ANY observable emits (after all have emitted at least once)
combineLatest([this.filter$, this.sort$, this.page$]).pipe(
  switchMap(([filter, sort, page]) => this.userService.getUsers(filter, sort, page))
);
```

### Inquiry 44: Subject Types
**Scenario:** Different Subject types?
**Explanation:**

| Type | Description | Use Case |
|------|-------------|----------|
| **Subject** | No initial value. Only emits to subscribers after they subscribe. | Event bus |
| **BehaviorSubject** | Has initial value. New subscribers get latest value immediately. | Current state (logged-in user) |
| **ReplaySubject** | Replays N previous values to new subscribers. | Chat history |
| **AsyncSubject** | Only emits the LAST value, and only when complete. | Rarely used |

```typescript
// BehaviorSubject — most commonly used in Angular services
const user$ = new BehaviorSubject<User | null>(null);

// New subscriber immediately gets current value (null)
user$.subscribe(user => console.log(user));

// Emit new value — all subscribers notified
user$.next({ name: 'Rahul', role: 'admin' });

// Get current value synchronously (no subscribe needed)
const currentUser = user$.getValue();
```

### Inquiry 45: Unsubscribing — Preventing Memory Leaks
**Scenario:** When and how to unsubscribe?
**Explanation:**

```typescript
// Method 1: takeUntilDestroyed (Angular 16+ — BEST)
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({ ... })
export class UserComponent {
  constructor(private userService: UserService) {
    this.userService.getUsers().pipe(
      takeUntilDestroyed()  // Auto-unsubscribes when component destroyed
    ).subscribe(users => this.users = users);
  }
}

// Method 2: takeUntil with destroy$ subject
export class UserComponent implements OnDestroy {
  private destroy$ = new Subject<void>();

  ngOnInit() {
    this.userService.getUsers().pipe(
      takeUntil(this.destroy$)
    ).subscribe(users => this.users = users);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// Method 3: Async pipe (auto-unsubscribes — RECOMMENDED for templates)
// <div *ngFor="let user of users$ | async">{{ user.name }}</div>

// Method 4: Manual unsubscribe
export class UserComponent implements OnDestroy {
  private sub!: Subscription;

  ngOnInit() {
    this.sub = this.userService.getUsers().subscribe(users => this.users = users);
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}
```

**When you DON'T need to unsubscribe:**
*   `HttpClient` calls (auto-complete after response).
*   `async` pipe (auto-unsubscribes).
*   Router events (if using `takeUntilDestroyed`).

---

## 11. Lifecycle Hooks

### Inquiry 46: All Lifecycle Hooks
**Scenario:** Order and purpose?
**Explanation:**

```
Constructor
    ↓
ngOnChanges()        — @Input changed (before ngOnInit, and on every change)
    ↓
ngOnInit()           — Component initialized (called ONCE)
    ↓
ngDoCheck()          — Custom change detection
    ↓
ngAfterContentInit() — After ng-content projected (called ONCE)
    ↓
ngAfterContentChecked() — After projected content checked
    ↓
ngAfterViewInit()    — After view (template + children) initialized (called ONCE)
    ↓
ngAfterViewChecked() — After view checked
    ↓
ngOnDestroy()        — Before component destroyed (cleanup)
```

| Hook | When | Use For |
|------|------|---------|
| **ngOnChanges** | @Input value changes | React to input changes |
| **ngOnInit** | After first ngOnChanges | Fetch data, initialize logic |
| **ngDoCheck** | Every change detection | Custom change detection |
| **ngAfterViewInit** | After view rendered | Access @ViewChild, DOM manipulation |
| **ngOnDestroy** | Before removal | Unsubscribe, cleanup timers |

### Inquiry 47: ngOnInit vs Constructor
**Scenario:** What goes where?
**Explanation:**

```typescript
export class UserComponent implements OnInit {
  @Input() userId!: number;

  constructor(private userService: UserService) {
    // Constructor:
    // - DI only (inject services)
    // - @Input values NOT available yet
    // - DOM NOT ready
    console.log(this.userId); // undefined!
  }

  ngOnInit() {
    // ngOnInit:
    // - @Input values ARE available
    // - Perfect for initialization logic
    // - Fetch data here
    console.log(this.userId); // has value!
    this.loadUser();
  }

  private loadUser() {
    this.userService.getUserById(this.userId).subscribe(user => this.user = user);
  }
}
```

**Rule:** Use **constructor** for DI only. Use **ngOnInit** for everything else.

### Inquiry 48: ngOnChanges
**Scenario:** React to @Input changes.
**Explanation:**

```typescript
export class ChildComponent implements OnChanges {
  @Input() userId!: number;
  @Input() filter!: string;

  ngOnChanges(changes: SimpleChanges) {
    // Called BEFORE ngOnInit and on every @Input change

    if (changes['userId']) {
      console.log('Previous:', changes['userId'].previousValue);
      console.log('Current:', changes['userId'].currentValue);
      console.log('First change:', changes['userId'].firstChange);

      if (!changes['userId'].firstChange) {
        this.reloadUser();  // Only reload if not the initial value
      }
    }

    if (changes['filter']) {
      this.applyFilter();
    }
  }
}
```

### Inquiry 49: ngOnDestroy — Cleanup
**Scenario:** What to clean up?
**Explanation:**

```typescript
export class DashboardComponent implements OnDestroy {
  private subscription!: Subscription;
  private intervalId: any;
  private ws!: WebSocket;

  ngOnInit() {
    // Subscribe to observable
    this.subscription = this.dataService.getData().subscribe(data => this.data = data);

    // Start interval
    this.intervalId = setInterval(() => this.refresh(), 5000);

    // Open WebSocket
    this.ws = new WebSocket('ws://api.example.com');
  }

  ngOnDestroy() {
    // 1. Unsubscribe from observables
    this.subscription.unsubscribe();

    // 2. Clear intervals/timeouts
    clearInterval(this.intervalId);

    // 3. Close connections
    this.ws.close();

    // 4. Remove event listeners (if added manually)
    // window.removeEventListener('resize', this.onResize);
  }
}
```

---

## 12. Modules & Lazy Loading

### Inquiry 50: NgModule Structure
**Scenario:** What goes in a module?
**Explanation:**

```typescript
@NgModule({
  declarations: [       // Components, Directives, Pipes that BELONG to this module
    UserListComponent,
    UserDetailComponent,
    HighlightDirective,
    TruncatePipe
  ],
  imports: [             // Other modules this module NEEDS
    CommonModule,        // *ngIf, *ngFor, pipes
    FormsModule,         // Template-driven forms
    ReactiveFormsModule, // Reactive forms
    HttpClientModule,    // HTTP calls
    RouterModule.forChild(routes)
  ],
  exports: [             // What OTHER modules can use from this module
    UserListComponent
  ],
  providers: [           // Services scoped to this module
    UserService
  ]
})
export class UserModule {}
```

### Inquiry 51: Feature Modules
**Scenario:** How to organize a large app?
**Explanation:**

```
src/app/
├── core/                  ← Singleton services, guards, interceptors
│   ├── services/
│   ├── guards/
│   └── interceptors/
├── shared/                ← Reusable components, pipes, directives
│   ├── components/
│   ├── pipes/
│   └── directives/
├── features/              ← Feature modules (lazy-loaded)
│   ├── user/
│   │   ├── user-list/
│   │   ├── user-detail/
│   │   ├── user.service.ts
│   │   └── user.routes.ts
│   ├── admin/
│   └── dashboard/
├── app.component.ts
├── app.routes.ts
└── app.config.ts
```

| Module | Purpose | Loaded |
|--------|---------|--------|
| **Core** | App-wide singletons (auth, logging) | Once at startup |
| **Shared** | Reusable UI (buttons, cards, pipes) | Imported by feature modules |
| **Feature** | Business features (users, orders) | Lazy-loaded on demand |

### Inquiry 52: Lazy Loading Deep Dive
**Scenario:** How does lazy loading work internally?
**Explanation:**

```typescript
// app.routes.ts
export const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'users',
    loadComponent: () => import('./features/user/user-list.component')
      .then(m => m.UserListComponent)
  },
  {
    path: 'admin',
    loadChildren: () => import('./features/admin/admin.routes')
      .then(m => m.ADMIN_ROUTES),
    canActivate: [authGuard]
  }
];

// admin.routes.ts
export const ADMIN_ROUTES: Routes = [
  { path: '', component: AdminDashboardComponent },
  { path: 'users', component: AdminUsersComponent },
  { path: 'settings', component: AdminSettingsComponent }
];
```

**What happens:**
1. Initial load: Only `HomeComponent` bundle is downloaded.
2. User clicks "Users" → browser downloads `user-list.component.js` chunk.
3. User clicks "Admin" → browser downloads `admin.routes.js` chunk.
4. Chunks are **cached** — subsequent visits don't re-download.

### Inquiry 53: Preloading Strategies
**Scenario:** Load lazy modules in background?
**Explanation:**

```typescript
import { PreloadAllModules } from '@angular/router';

// app.config.ts
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withPreloading(PreloadAllModules))
    // After initial load, preload ALL lazy modules in background
  ]
};
```

| Strategy | Description |
|----------|-------------|
| **NoPreloading** (default) | Load only when navigated |
| **PreloadAllModules** | Preload all lazy modules after initial load |
| **Custom** | Preload based on conditions (e.g., user role, network speed) |

```typescript
// Custom preloading — only preload modules marked with data.preload
export const customPreload: PreloadingStrategy = {
  preload(route: Route, load: () => Observable<any>): Observable<any> {
    return route.data?.['preload'] ? load() : of(null);
  }
};

// Route config
{ path: 'dashboard', loadChildren: ..., data: { preload: true } },  // Will preload
{ path: 'admin', loadChildren: ..., data: { preload: false } }      // Won't preload
```

---

## 13. State Management

### Inquiry 54: Why State Management?
**Scenario:** When do you need it?
**Explanation:** When multiple components need to share and react to the same data, passing data through @Input/@Output chains becomes messy ("prop drilling").

| Approach | Complexity | Best For |
|----------|-----------|----------|
| **@Input/@Output** | Low | Parent-child communication |
| **Service + BehaviorSubject** | Medium | Small-medium apps |
| **NgRx (Redux pattern)** | High | Large enterprise apps |
| **Signals** | Low | Angular 16+ component state |

### Inquiry 55: Service-based State Management
**Scenario:** Simple store without NgRx.
**Explanation:**

```typescript
@Injectable({ providedIn: 'root' })
export class CartStore {
  private itemsSubject = new BehaviorSubject<CartItem[]>([]);
  items$ = this.itemsSubject.asObservable();

  // Derived state
  totalPrice$ = this.items$.pipe(
    map(items => items.reduce((sum, item) => sum + item.price * item.quantity, 0))
  );

  itemCount$ = this.items$.pipe(
    map(items => items.reduce((sum, item) => sum + item.quantity, 0))
  );

  addItem(item: CartItem) {
    const current = this.itemsSubject.getValue();
    const existing = current.find(i => i.id === item.id);
    if (existing) {
      existing.quantity++;
      this.itemsSubject.next([...current]);
    } else {
      this.itemsSubject.next([...current, { ...item, quantity: 1 }]);
    }
  }

  removeItem(id: number) {
    const updated = this.itemsSubject.getValue().filter(i => i.id !== id);
    this.itemsSubject.next(updated);
  }

  clearCart() {
    this.itemsSubject.next([]);
  }
}

// Component usage
@Component({
  template: `
    <p>Items: {{ cartStore.itemCount$ | async }}</p>
    <p>Total: {{ cartStore.totalPrice$ | async | currency:'INR' }}</p>
  `
})
export class CartComponent {
  constructor(public cartStore: CartStore) {}
}
```

### Inquiry 56: NgRx Basics
**Scenario:** Redux pattern in Angular.
**Explanation:**

```
Component ──dispatch()──► Action ──► Reducer ──► Store (State) ──select()──► Component
                              │
                          Effect (side effects: API calls)
```

```typescript
// 1. Define Actions
export const loadUsers = createAction('[User] Load Users');
export const loadUsersSuccess = createAction('[User] Load Users Success', props<{ users: User[] }>());
export const loadUsersFailure = createAction('[User] Load Users Failure', props<{ error: string }>());

// 2. Define Reducer
export interface UserState {
  users: User[];
  loading: boolean;
  error: string | null;
}

const initialState: UserState = { users: [], loading: false, error: null };

export const userReducer = createReducer(
  initialState,
  on(loadUsers, (state) => ({ ...state, loading: true })),
  on(loadUsersSuccess, (state, { users }) => ({ ...state, users, loading: false })),
  on(loadUsersFailure, (state, { error }) => ({ ...state, error, loading: false }))
);

// 3. Define Effects (side effects)
@Injectable()
export class UserEffects {
  loadUsers$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadUsers),
      switchMap(() =>
        this.userService.getUsers().pipe(
          map(users => loadUsersSuccess({ users })),
          catchError(error => of(loadUsersFailure({ error: error.message })))
        )
      )
    )
  );

  constructor(private actions$: Actions, private userService: UserService) {}
}

// 4. Define Selectors
export const selectUsers = (state: AppState) => state.user.users;
export const selectLoading = (state: AppState) => state.user.loading;

// 5. Use in Component
@Component({
  template: `
    @if (loading$ | async) { <p>Loading...</p> }
    @for (user of users$ | async; track user.id) {
      <div>{{ user.name }}</div>
    }
  `
})
export class UserListComponent {
  users$ = this.store.select(selectUsers);
  loading$ = this.store.select(selectLoading);

  constructor(private store: Store) {
    this.store.dispatch(loadUsers());
  }
}
```

---

## 14. Performance Optimization

### Inquiry 57: Change Detection Strategies
**Scenario:** How does Angular detect changes?
**Explanation:**

| Strategy | Description | When to Use |
|----------|-------------|-------------|
| **Default** | Checks entire component tree on every event | Simple components |
| **OnPush** | Only checks when @Input reference changes or event fires | Performance-critical components |

```typescript
@Component({
  selector: 'app-user-card',
  changeDetection: ChangeDetectionStrategy.OnPush,  // Only re-render when inputs change
  template: `<p>{{ user.name }}</p>`
})
export class UserCardComponent {
  @Input() user!: User;
}
```

**OnPush triggers re-render when:**
*   `@Input()` reference changes (not mutation!).
*   Event handler fires within the component.
*   Observable emits via `async` pipe.
*   `ChangeDetectorRef.markForCheck()` is called manually.

```typescript
// WRONG with OnPush — mutating object doesn't trigger change detection
this.user.name = 'New Name';  // Same reference → no re-render!

// CORRECT — create new reference
this.user = { ...this.user, name: 'New Name' };  // New object → re-renders!
```

### Inquiry 58: trackBy in *ngFor
**Scenario:** Why is it important?
**Explanation:** Without `trackBy`, Angular destroys and recreates ALL DOM elements when the array changes. With `trackBy`, it only updates changed items.

```html
<!-- WITHOUT trackBy — entire list re-rendered on any change -->
<li *ngFor="let user of users">{{ user.name }}</li>

<!-- WITH trackBy — only changed items re-rendered -->
<li *ngFor="let user of users; trackBy: trackById">{{ user.name }}</li>
```

```typescript
trackById(index: number, user: User): number {
  return user.id;
}
```

### Inquiry 59: Virtual Scrolling
**Scenario:** Render large lists efficiently.
**Explanation:** Only renders items visible in the viewport. Uses `@angular/cdk/scrolling`.

```typescript
import { ScrollingModule } from '@angular/cdk/scrolling';

@Component({
  standalone: true,
  imports: [ScrollingModule],
  template: `
    <cdk-virtual-scroll-viewport itemSize="50" style="height: 400px;">
      <div *cdkVirtualFor="let user of users; trackBy: trackById">
        {{ user.name }}
      </div>
    </cdk-virtual-scroll-viewport>
  `
})
export class UserListComponent {
  users: User[] = [];  // Can be 10,000+ items — only ~10 rendered at a time
}
```

### Inquiry 60: Bundle Size Optimization
**Scenario:** How to reduce app size?
**Explanation:**

| Technique | How |
|-----------|-----|
| **Lazy loading** | Load feature modules on demand |
| **Tree shaking** | Remove unused code (automatic with `ng build --prod`) |
| **AOT compilation** | Compile templates at build time (default in prod) |
| **Source map explorer** | Analyze bundle: `npx source-map-explorer dist/**/*.js` |
| **Import only what you need** | `import { map } from 'rxjs/operators'` not `import 'rxjs'` |
| **Compression** | Enable gzip/brotli on server |
| **Image optimization** | Use `NgOptimizedImage` directive |

```typescript
// NgOptimizedImage (Angular 15+)
import { NgOptimizedImage } from '@angular/common';

@Component({
  imports: [NgOptimizedImage],
  template: `<img ngSrc="user.jpg" width="200" height="200" priority>`
})
```

---

## 15. Testing

### Inquiry 61: Testing Overview
**Scenario:** What types of tests?
**Explanation:**

| Type | Tool | Tests What | Speed |
|------|------|-----------|-------|
| **Unit** | Jasmine + Karma | Components, services, pipes in isolation | Fast |
| **Integration** | TestBed | Component + template + dependencies | Medium |
| **E2E** | Cypress / Playwright | Full app in browser | Slow |

### Inquiry 62: Testing a Service
**Scenario:** Unit test for UserService.
**Explanation:**

```typescript
describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        UserService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();  // Ensure no outstanding requests
  });

  it('should fetch users', () => {
    const mockUsers: User[] = [
      { id: 1, name: 'Rahul' },
      { id: 2, name: 'Amit' }
    ];

    service.getUsers().subscribe(users => {
      expect(users.length).toBe(2);
      expect(users[0].name).toBe('Rahul');
    });

    const req = httpMock.expectOne('https://api.example.com/users');
    expect(req.request.method).toBe('GET');
    req.flush(mockUsers);  // Provide mock response
  });

  it('should handle error', () => {
    service.getUsers().subscribe({
      error: (err) => expect(err.message).toContain('Server error')
    });

    const req = httpMock.expectOne('https://api.example.com/users');
    req.flush('Error', { status: 500, statusText: 'Server Error' });
  });
});
```

### Inquiry 63: Testing a Component
**Scenario:** Test component with template.
**Explanation:**

```typescript
describe('UserListComponent', () => {
  let component: UserListComponent;
  let fixture: ComponentFixture<UserListComponent>;
  let mockUserService: jasmine.SpyObj<UserService>;

  beforeEach(async () => {
    mockUserService = jasmine.createSpyObj('UserService', ['getUsers']);
    mockUserService.getUsers.and.returnValue(of([
      { id: 1, name: 'Rahul' },
      { id: 2, name: 'Amit' }
    ]));

    await TestBed.configureTestingModule({
      imports: [UserListComponent],
      providers: [{ provide: UserService, useValue: mockUserService }]
    }).compileComponents();

    fixture = TestBed.createComponent(UserListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();  // Trigger ngOnInit
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display users', () => {
    const compiled = fixture.nativeElement;
    const items = compiled.querySelectorAll('li');
    expect(items.length).toBe(2);
    expect(items[0].textContent).toContain('Rahul');
  });

  it('should call getUsers on init', () => {
    expect(mockUserService.getUsers).toHaveBeenCalledTimes(1);
  });
});
```

### Inquiry 64: Testing a Pipe
**Scenario:** Unit test for custom pipe.
**Explanation:**

```typescript
describe('TruncatePipe', () => {
  const pipe = new TruncatePipe();

  it('should return empty string for null', () => {
    expect(pipe.transform(null as any)).toBe('');
  });

  it('should not truncate short text', () => {
    expect(pipe.transform('Hello', 10)).toBe('Hello');
  });

  it('should truncate long text with default trail', () => {
    expect(pipe.transform('Hello World', 5)).toBe('Hello...');
  });

  it('should use custom trail', () => {
    expect(pipe.transform('Hello World', 5, '>>>')).toBe('Hello>>>');
  });
});
```

---

## 16. Tricky Output & Gotcha Questions

### Inquiry 65: ExpressionChangedAfterItHasBeenCheckedError
**Scenario:** Most common Angular error.
**Explanation:** Angular checks the view twice in dev mode. If a value changes between the two checks, this error is thrown.

```typescript
// CAUSES ERROR:
@Component({
  template: `<p>{{ value }}</p>`
})
export class BadComponent implements AfterViewInit {
  value = 'initial';

  ngAfterViewInit() {
    this.value = 'changed';  // ERROR! Value changed after view was checked
  }
}

// FIX 1: Use setTimeout (pushes to next change detection cycle)
ngAfterViewInit() {
  setTimeout(() => this.value = 'changed');
}

// FIX 2: Use ChangeDetectorRef
constructor(private cdr: ChangeDetectorRef) {}
ngAfterViewInit() {
  this.value = 'changed';
  this.cdr.detectChanges();  // Manually trigger check
}

// FIX 3: Move logic to ngOnInit (if possible)
ngOnInit() {
  this.value = 'changed';  // No error — before view check
}
```

### Inquiry 66: Two-way Binding Without FormsModule
**Scenario:** What happens?

```html
<!-- This will NOT work without FormsModule -->
<input [(ngModel)]="name">
<!-- Error: Can't bind to 'ngModel' since it isn't a known property -->

<!-- Fix: Import FormsModule -->
@Component({
  standalone: true,
  imports: [FormsModule],  // Required!
  template: `<input [(ngModel)]="name">`
})
```

### Inquiry 67: *ngIf vs [hidden]
**Scenario:** What's the difference?

```html
<!-- *ngIf — REMOVES element from DOM entirely -->
<div *ngIf="isVisible">Content</div>
<!-- When false: element doesn't exist in DOM, component is destroyed -->
<!-- When true: element is re-created, ngOnInit fires again -->

<!-- [hidden] — HIDES element with CSS (display: none) -->
<div [hidden]="!isVisible">Content</div>
<!-- Element always exists in DOM, component stays alive -->
<!-- Faster toggle, but component still consumes memory -->
```

| Feature | *ngIf | [hidden] |
|---------|-------|----------|
| **DOM** | Removes/adds element | Always in DOM |
| **Component** | Destroyed/recreated | Stays alive |
| **Performance** | Better for rarely shown content | Better for frequently toggled |
| **ngOnInit** | Called every time shown | Called once |

### Inquiry 68: Async Pipe Multiple Subscriptions
**Scenario:** Common performance trap.

```html
<!-- BAD: Creates 3 separate subscriptions (3 API calls!) -->
<p>Name: {{ (user$ | async)?.name }}</p>
<p>Email: {{ (user$ | async)?.email }}</p>
<p>Age: {{ (user$ | async)?.age }}</p>

<!-- GOOD: Single subscription with @if / *ngIf as -->
@if (user$ | async; as user) {
  <p>Name: {{ user.name }}</p>
  <p>Email: {{ user.email }}</p>
  <p>Age: {{ user.age }}</p>
}

<!-- Or use *ngIf with as -->
<div *ngIf="user$ | async as user">
  <p>Name: {{ user.name }}</p>
  <p>Email: {{ user.email }}</p>
</div>
```

### Inquiry 69: Banana in a Box Syntax
**Scenario:** Why `[()]` and not `([])`?

```html
<!-- Two-way binding = Property binding + Event binding -->
[(ngModel)]="name"

<!-- Think of it as: [banana] in a (box) -->
<!-- [property] + (event) = [(two-way)] -->

<!-- It's syntactic sugar for: -->
[ngModel]="name" (ngModelChange)="name = $event"

<!-- Order matters! [()] works, ([]) does NOT -->
```

### Inquiry 70: ViewEncapsulation Gotcha
**Scenario:** Why don't my styles apply to child components?

```typescript
@Component({
  // Default: Emulated — styles are SCOPED to this component only
  encapsulation: ViewEncapsulation.Emulated,  // Default
  styles: [`
    p { color: red; }  /* Only affects <p> in THIS component, not children */
  `]
})

// To affect children:
// Option 1: ViewEncapsulation.None (styles become global — dangerous!)
encapsulation: ViewEncapsulation.None

// Option 2: ::ng-deep (deprecated but still used)
::ng-deep .child-class { color: red; }

// Option 3: Use global styles.scss
```

| Encapsulation | Behavior |
|---------------|----------|
| **Emulated** (default) | Scoped via attribute selectors. Safe. |
| **None** | Global styles. Affects entire app. Dangerous. |
| **ShadowDom** | Uses native Shadow DOM. True isolation. |

### Inquiry 71: Router Link vs href
**Scenario:** Why not use `<a href>`?

```html
<!-- BAD: Full page reload! Destroys Angular app state -->
<a href="/users">Users</a>

<!-- GOOD: Client-side navigation, no reload -->
<a routerLink="/users">Users</a>
<a [routerLink]="['/users', userId]">User Detail</a>
```

### Inquiry 72: OnPush + Mutable Object Trap
**Scenario:** Why doesn't my component update?

```typescript
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<p>{{ user.name }}</p>`
})
export class UserCardComponent {
  @Input() user!: User;
}

// Parent component:
// BAD — mutating same object reference → OnPush won't detect!
this.user.name = 'New Name';

// GOOD — create new object → new reference → OnPush detects!
this.user = { ...this.user, name: 'New Name' };
```

### Inquiry 73: Template Variable Scope
**Scenario:** Where can you use template variables?

```html
<!-- #ref is ONLY available within the same template -->
<div *ngIf="show">
  <input #myInput>
  <button (click)="log(myInput.value)">Log</button>  <!-- Works -->
</div>

<!-- FAILS: myInput is not accessible outside the *ngIf block -->
<button (click)="log(myInput.value)">Log Outside</button>  <!-- Error! -->
```

---

## 17. Coding Problems

### Inquiry 74: Search with Debounce
**Scenario:** Implement search-as-you-type.

```typescript
@Component({
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, AsyncPipe],
  template: `
    <input [formControl]="searchControl" placeholder="Search users...">
    @for (user of filteredUsers$ | async; track user.id) {
      <div>{{ user.name }} — {{ user.email }}</div>
    } @empty {
      <p>No results found.</p>
    }
  `
})
export class SearchComponent {
  searchControl = new FormControl('');
  filteredUsers$: Observable<User[]>;

  constructor(private userService: UserService) {
    this.filteredUsers$ = this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => term ? this.userService.search(term) : of([])),
      catchError(() => of([]))
    );
  }
}
```

### Inquiry 75: Todo App (CRUD)
**Scenario:** Complete todo with add, toggle, delete, filter.

```typescript
interface Todo {
  id: number;
  text: string;
  completed: boolean;
}

@Component({
  standalone: true,
  imports: [FormsModule, CommonModule],
  template: `
    <input [(ngModel)]="newTodo" (keyup.enter)="addTodo()" placeholder="Add todo...">
    <button (click)="addTodo()">Add</button>

    <div>
      <button (click)="filter = 'all'" [class.active]="filter === 'all'">All</button>
      <button (click)="filter = 'active'" [class.active]="filter === 'active'">Active</button>
      <button (click)="filter = 'completed'" [class.active]="filter === 'completed'">Completed</button>
    </div>

    @for (todo of filteredTodos; track todo.id) {
      <div [class.completed]="todo.completed">
        <input type="checkbox" [checked]="todo.completed" (change)="toggleTodo(todo.id)">
        <span>{{ todo.text }}</span>
        <button (click)="deleteTodo(todo.id)">X</button>
      </div>
    }

    <p>{{ activeTodoCount }} items left</p>
  `
})
export class TodoComponent {
  todos: Todo[] = [];
  newTodo = '';
  filter: 'all' | 'active' | 'completed' = 'all';
  private nextId = 1;

  addTodo() {
    if (this.newTodo.trim()) {
      this.todos.push({ id: this.nextId++, text: this.newTodo.trim(), completed: false });
      this.newTodo = '';
    }
  }

  toggleTodo(id: number) {
    const todo = this.todos.find(t => t.id === id);
    if (todo) todo.completed = !todo.completed;
  }

  deleteTodo(id: number) {
    this.todos = this.todos.filter(t => t.id !== id);
  }

  get filteredTodos(): Todo[] {
    switch (this.filter) {
      case 'active': return this.todos.filter(t => !t.completed);
      case 'completed': return this.todos.filter(t => t.completed);
      default: return this.todos;
    }
  }

  get activeTodoCount(): number {
    return this.todos.filter(t => !t.completed).length;
  }
}
```

### Inquiry 76: Pagination Component
**Scenario:** Reusable pagination.

```typescript
@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule],
  template: `
    <nav>
      <button (click)="changePage(currentPage - 1)" [disabled]="currentPage === 1">Prev</button>
      @for (page of pages; track page) {
        <button (click)="changePage(page)" [class.active]="page === currentPage">{{ page }}</button>
      }
      <button (click)="changePage(currentPage + 1)" [disabled]="currentPage === totalPages">Next</button>
    </nav>
  `
})
export class PaginationComponent {
  @Input() totalItems = 0;
  @Input() pageSize = 10;
  @Input() currentPage = 1;
  @Output() pageChange = new EventEmitter<number>();

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.pageSize);
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }

  changePage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.pageChange.emit(page);
    }
  }
}

// Usage
<app-pagination
  [totalItems]="100"
  [pageSize]="10"
  [currentPage]="currentPage"
  (pageChange)="onPageChange($event)">
</app-pagination>
```

### Inquiry 77: Star Rating Component
**Scenario:** Reusable rating component.

```typescript
@Component({
  selector: 'app-star-rating',
  standalone: true,
  template: `
    @for (star of stars; track $index) {
      <span
        (click)="rate($index + 1)"
        (mouseenter)="hoverRating = $index + 1"
        (mouseleave)="hoverRating = 0"
        [style.color]="($index < (hoverRating || rating)) ? 'gold' : 'gray'"
        style="cursor: pointer; font-size: 24px;">
        ★
      </span>
    }
    <span>({{ rating }}/{{ maxStars }})</span>
  `
})
export class StarRatingComponent {
  @Input() rating = 0;
  @Input() maxStars = 5;
  @Output() ratingChange = new EventEmitter<number>();
  hoverRating = 0;

  get stars(): number[] {
    return Array(this.maxStars).fill(0);
  }

  rate(value: number) {
    this.rating = value;
    this.ratingChange.emit(value);
  }
}

// Usage (two-way binding)
<app-star-rating [(rating)]="product.rating"></app-star-rating>
```

### Inquiry 78: Countdown Timer
**Scenario:** Timer with start, pause, reset.

```typescript
@Component({
  standalone: true,
  template: `
    <h2>{{ minutes }}:{{ seconds | number:'2.0-0' }}</h2>
    <button (click)="start()" [disabled]="isRunning">Start</button>
    <button (click)="pause()" [disabled]="!isRunning">Pause</button>
    <button (click)="reset()">Reset</button>
  `
})
export class CountdownComponent implements OnDestroy {
  totalSeconds = 300;  // 5 minutes
  remainingSeconds = 300;
  isRunning = false;
  private intervalId: any;

  get minutes(): number { return Math.floor(this.remainingSeconds / 60); }
  get seconds(): number { return this.remainingSeconds % 60; }

  start() {
    if (!this.isRunning && this.remainingSeconds > 0) {
      this.isRunning = true;
      this.intervalId = setInterval(() => {
        this.remainingSeconds--;
        if (this.remainingSeconds <= 0) {
          this.pause();
          alert('Time is up!');
        }
      }, 1000);
    }
  }

  pause() {
    this.isRunning = false;
    clearInterval(this.intervalId);
  }

  reset() {
    this.pause();
    this.remainingSeconds = this.totalSeconds;
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }
}
```

### Inquiry 79: Dynamic Table with Sorting
**Scenario:** Sortable data table.

```typescript
@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <table>
      <thead>
        <tr>
          @for (col of columns; track col.key) {
            <th (click)="sort(col.key)" style="cursor: pointer;">
              {{ col.label }}
              @if (sortKey === col.key) {
                {{ sortDirection === 'asc' ? '▲' : '▼' }}
              }
            </th>
          }
        </tr>
      </thead>
      <tbody>
        @for (row of sortedData; track row.id) {
          <tr>
            @for (col of columns; track col.key) {
              <td>{{ row[col.key] }}</td>
            }
          </tr>
        }
      </tbody>
    </table>
  `
})
export class DataTableComponent {
  columns = [
    { key: 'name', label: 'Name' },
    { key: 'email', label: 'Email' },
    { key: 'age', label: 'Age' }
  ];

  data = [
    { id: 1, name: 'Rahul', email: 'rahul@test.com', age: 25 },
    { id: 2, name: 'Amit', email: 'amit@test.com', age: 30 },
    { id: 3, name: 'Sneha', email: 'sneha@test.com', age: 22 }
  ];

  sortKey = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  sort(key: string) {
    if (this.sortKey === key) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortKey = key;
      this.sortDirection = 'asc';
    }
  }

  get sortedData() {
    if (!this.sortKey) return this.data;
    return [...this.data].sort((a: any, b: any) => {
      const valA = a[this.sortKey];
      const valB = b[this.sortKey];
      const comparison = valA > valB ? 1 : valA < valB ? -1 : 0;
      return this.sortDirection === 'asc' ? comparison : -comparison;
    });
  }
}
```

---

## 18. Interview Questions & Answers

### Inquiry 80: What is Angular? How is it different from React?
**Answer:**

| Feature | Angular | React |
|---------|---------|-------|
| **Type** | Full framework | Library (UI only) |
| **Language** | TypeScript | JavaScript/JSX |
| **Data binding** | Two-way (built-in) | One-way (default) |
| **Routing** | Built-in (RouterModule) | Third-party (react-router) |
| **State** | Services / NgRx | Context / Redux / Zustand |
| **CLI** | `ng` CLI (official) | `create-react-app` / Vite |
| **Learning curve** | Steeper | Easier to start |
| **Used by** | Google, Microsoft, WITCH | Meta, Netflix, Airbnb |

### Inquiry 81: What is AOT vs JIT Compilation?
**Answer:**

| Feature | JIT (Just-in-Time) | AOT (Ahead-of-Time) |
|---------|-------------------|---------------------|
| **When** | At runtime in browser | At build time |
| **Bundle size** | Larger (includes compiler) | Smaller |
| **Speed** | Slower startup | Faster startup |
| **Errors** | Caught at runtime | Caught at build time |
| **Default** | `ng serve` (dev) | `ng build --prod` |

### Inquiry 82: What are Decorators in Angular?
**Answer:** TypeScript functions that add metadata to classes, properties, or methods. Angular uses them to define components, services, etc.

| Decorator | Purpose |
|-----------|---------|
| `@Component` | Define a component |
| `@NgModule` | Define a module |
| `@Injectable` | Mark class as injectable service |
| `@Input` | Receive data from parent |
| `@Output` | Emit events to parent |
| `@ViewChild` | Access child component/element |
| `@HostListener` | Listen to host element events |
| `@Pipe` | Define a pipe |
| `@Directive` | Define a directive |

### Inquiry 83: What is Zone.js?
**Answer:** A library that patches async APIs (setTimeout, Promise, addEventListener, XHR) to notify Angular when async operations complete. This triggers change detection automatically.

**Without Zone.js:** You'd have to manually tell Angular to check for changes after every async operation.

**Angular 18+ (experimental):** Zoneless change detection using Signals — no Zone.js needed.

### Inquiry 84: What is the Difference Between Template-Driven and Reactive Forms?
**Answer:**
*   **Template-Driven:** Logic in HTML. Uses `ngModel`. Simple forms (login, contact). Harder to test.
*   **Reactive:** Logic in TypeScript. Uses `FormGroup`, `FormControl`. Complex forms (dynamic fields, cross-field validation). Easy to test.

**WITCH interview tip:** Know both, but emphasize Reactive Forms — they're used in enterprise projects.

### Inquiry 85: How Does Dependency Injection Work in Angular?
**Answer:**
1. You mark a class with `@Injectable()`.
2. You register it (via `providedIn: 'root'` or module/component providers).
3. Angular's DI system creates an instance and injects it via constructor.
4. `providedIn: 'root'` = singleton. Component-level = new instance per component.

### Inquiry 86: What is Content Projection?
**Answer:** Passing HTML content from parent into a child component's template using `<ng-content>`. Like React's `children` prop. Used for reusable wrapper components (cards, modals, tabs).

### Inquiry 87: Explain Angular Change Detection
**Answer:**
1. An event occurs (click, HTTP response, timer).
2. Zone.js detects it and notifies Angular.
3. Angular runs change detection from root component down the tree.
4. Each component's template bindings are checked.
5. If a value changed, the DOM is updated.

**OnPush optimization:** Skip checking a component unless its @Input reference changes or an event fires within it.

### Inquiry 88: What are Angular Interceptors Used For?
**Answer:**
*   **Auth token** — Add Bearer token to every request.
*   **Logging** — Log all HTTP requests/responses.
*   **Error handling** — Global error handler for all API calls.
*   **Loading spinner** — Show/hide spinner on request start/end.
*   **Caching** — Cache GET responses.
*   **Retry** — Auto-retry failed requests.

### Inquiry 89: What is the Difference Between `subscribe()` and `async` Pipe?
**Answer:**

| Feature | subscribe() | async pipe |
|---------|------------|-----------|
| **Where** | Component class | Template |
| **Unsubscribe** | Manual (ngOnDestroy) | Automatic |
| **Memory leaks** | Risk if forgotten | No risk |
| **Multiple subscriptions** | One per subscribe call | One per pipe usage |
| **Best for** | Side effects, complex logic | Displaying data in template |

### Inquiry 90: What are Signals in Angular?
**Answer:** A new reactive primitive (Angular 16+) for managing component state. Simpler than RxJS for synchronous state. Auto-updates the template when value changes. No subscription management needed.

```typescript
count = signal(0);                          // Create
doubleCount = computed(() => count() * 2);  // Derived
count.set(5);                               // Set
count.update(c => c + 1);                   // Update
```

### Inquiry 91: How to Secure an Angular Application?
**Answer:**
*   **Route Guards** — Prevent unauthorized access to routes.
*   **JWT Tokens** — Store in httpOnly cookies (not localStorage).
*   **HTTP Interceptors** — Attach auth headers, handle 401.
*   **CORS** — Configure on backend.
*   **Input sanitization** — Angular auto-sanitizes HTML bindings (prevents XSS).
*   **Content Security Policy** — HTTP header to prevent script injection.
*   **HTTPS** — Always use in production.

### Inquiry 92: What Questions Should YOU Ask the Interviewer?
**Answer:**
*   "Which Angular version is the project on? Are you using standalone components?"
*   "Do you use NgRx or service-based state management?"
*   "What's the testing strategy — unit tests, e2e, or both?"
*   "How large is the frontend team?"
*   "Do you follow a specific component library (Angular Material, PrimeNG)?"
*   "What's the CI/CD pipeline like?"
