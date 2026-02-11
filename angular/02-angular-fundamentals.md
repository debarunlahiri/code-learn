# Angular Fundamentals - Complete Guide (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Angular architecture, project structure, CLI, modules vs standalone, TypeScript essentials, and Angular 17+ features.

--

## Table of Contents

1. [What is Angular?](#what-is-angular)
2. [Angular Architecture](#angular-architecture)
3. [Angular CLI](#angular-cli)
4. [Project Structure](#project-structure)
5. [Modules (NgModule)](#modules-ngmodule)
6. [Standalone Components (Angular 14+)](#standalone-components)
7. [TypeScript Essentials for Angular](#typescript-essentials-for-angular)
8. [Angular Versions & Key Changes](#angular-versions-key-changes)
9. [Angular vs AngularJS](#angular-vs-angularjs)
10. [Angular vs React vs Vue](#angular-vs-react-vs-vue)
11. [Decorators](#decorators)
12. [Angular Compilation (AOT vs JIT)](#angular-compilation-aot-vs-jit)
13. [Zone.js & Change Detection Basics](#zonejs-change-detection-basics)
14. [Signals (Angular 16+)](#signals-angular-16)
15. [Environment Configuration](#environment-configuration)

--

## 1. What is Angular?

Angular is a **TypeScript-based, open-source front-end web application framework** developed and maintained by **Google**. It is a complete platform for building single-page applications (SPAs).

### Key Characteristics

| Feature | Description |
|---------|-------------|
| **Language** | TypeScript (superset of JavaScript) |
| **Architecture** | Component-based |
| **Data Binding** | Two-way binding built-in |
| **Routing** | Built-in router |
| **HTTP** | Built-in HttpClient |
| **Forms** | Template-driven & Reactive |
| **Testing** | Built-in testing utilities |
| **CLI** | Powerful command-line tool |
| **Maintained by** | Google |

### When to Use Angular

- **Large enterprise applications** with complex business logic
- **Team projects** where consistency and structure matter
- **Applications needing** built-in routing, forms, HTTP, and DI
- **Long-term projects** with ongoing maintenance

### When NOT to Use Angular

- Simple static websites (use plain HTML/CSS)
- Small widgets or micro-frontends (React might be lighter)
- Projects where team has no TypeScript experience and tight deadline

--

## 2. Angular Architecture

Angular follows a **component-based architecture** where the application is a tree of components.

### Core Building Blocks

```
┌─────────────────────────────────────────────────┐
│                  Angular App                     │
│                                                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────────┐  │
│  │ Modules   │  │Components│  │  Services     │  │
│  │(NgModule) │  │          │  │ (Injectable)  │  │
│  └──────────┘  └──────────┘  └──────────────┘  │
│                                                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────────┐  │
│  │Directives│  │  Pipes   │  │   Guards      │  │
│  │          │  │          │  │               │  │
│  └──────────┘  └──────────┘  └──────────────┘  │
│                                                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────────┐  │
│  │Templates │  │  Router  │  │ Interceptors  │  │
│  │ (HTML)   │  │          │  │               │  │
│  └──────────┘  └──────────┘  └──────────────┘  │
└─────────────────────────────────────────────────┘
```

### How They Work Together

```
User Action (click, type)
    ↓
Component (handles event)
    ↓
Service (business logic, API call)
    ↓
HttpClient → Interceptor → Backend API
    ↓
Response flows back through Observable
    ↓
Component updates → Template re-renders → User sees result
```

### Building Block Summary

| Block | Purpose | Example |
|-------|---------|---------|
| **Component** | UI building block (HTML + CSS + TypeScript) | `UserListComponent` |
| **Template** | HTML view with Angular syntax | `{{ user.name }}`, `*ngFor` |
| **Service** | Reusable business logic, data access | `UserService`, `AuthService` |
| **Module** | Groups related components/services | `UserModule`, `SharedModule` |
| **Directive** | Modify DOM behavior | `*ngIf`, `*ngFor`, custom highlight |
| **Pipe** | Transform display data | `date`, `currency`, custom pipes |
| **Guard** | Protect routes | `AuthGuard`, `RoleGuard` |
| **Interceptor** | Modify HTTP requests/responses | Add auth token, logging |
| **Resolver** | Pre-fetch data before route loads | `UserResolver` |

--

## 3. Angular CLI

The Angular CLI (`ng`) is a command-line tool that automates development tasks.

### Installation

```bash
# Install globally
npm install -g @angular/cli

# Check version
ng version

# Update Angular CLI
ng update @angular/cli @angular/core
```

### Essential Commands

```bash
# Create new project
ng new my-app                    # With routing and CSS
ng new my-app --standalone       # Standalone (no NgModule) — Angular 17+ default
ng new my-app --style=scss       # Use SCSS instead of CSS
ng new my-app --routing --style=scss --ssr  # With SSR support

# Generate components, services, etc.
ng generate component user-list        # or: ng g c user-list
ng generate service user               # or: ng g s user
ng generate directive highlight        # or: ng g d highlight
ng generate pipe truncate              # or: ng g p truncate
ng generate guard auth                 # or: ng g guard auth
ng generate interceptor auth           # or: ng g interceptor auth
ng generate module shared              # or: ng g m shared
ng generate interface user             # or: ng g i user
ng generate enum status                # or: ng g e status
ng generate class models/user          # or: ng g cl models/user

# Development
ng serve                    # Start dev server (http://localhost:4200)
ng serve --port 3000        # Custom port
ng serve --open             # Auto-open browser
ng serve --hmr              # Hot Module Replacement

# Build
ng build                    # Development build
ng build --configuration=production  # Production build (AOT, minification, tree-shaking)

# Testing
ng test                     # Run unit tests (Karma + Jasmine)
ng test --watch=false       # Run once (CI mode)
ng e2e                      # Run end-to-end tests

# Other
ng lint                     # Lint the project
ng add @angular/material    # Add Angular Material library
ng update                   # Update dependencies
ng analytics                # Configure analytics
```

### Generate Command Flags

```bash
# Component with inline template and styles
ng g c user-card --inline-template --inline-style

# Component without test file
ng g c user-card --skip-tests

# Component in a specific directory
ng g c features/user/user-list

# Dry run — see what would be created without actually creating
ng g c user-list --dry-run

# Standalone component (default in Angular 17+)
ng g c user-list --standalone
```

--

## 4. Project Structure

### Default Angular 17+ Project Structure

```
my-app/
├── src/
│   ├── app/
│   │   ├── app.component.ts          ← Root component
│   │   ├── app.component.html        ← Root template
│   │   ├── app.component.css         ← Root styles
│   │   ├── app.component.spec.ts     ← Root component test
│   │   ├── app.config.ts             ← App configuration (providers)
│   │   └── app.routes.ts             ← Route definitions
│   │
│   ├── assets/                        ← Static files (images, fonts)
│   ├── environments/                  ← Environment configs
│   │   ├── environment.ts
│   │   └── environment.prod.ts
│   ├── index.html                     ← Main HTML page
│   ├── main.ts                        ← Entry point (bootstraps app)
│   └── styles.css                     ← Global styles
│
├── angular.json                       ← Angular workspace config
├── package.json                       ← Dependencies
├── tsconfig.json                      ← TypeScript config
├── tsconfig.app.json                  ← App-specific TS config
├── tsconfig.spec.json                 ← Test-specific TS config
└── README.md
```

### Recommended Enterprise Structure

```
src/app/
├── core/                              ← Singleton services, guards, interceptors
│   ├── services/
│   │   ├── auth.service.ts
│   │   ├── logger.service.ts
│   │   └── storage.service.ts
│   ├── guards/
│   │   ├── auth.guard.ts
│   │   └── role.guard.ts
│   ├── interceptors/
│   │   ├── auth.interceptor.ts
│   │   ├── error.interceptor.ts
│   │   └── loading.interceptor.ts
│   ├── models/
│   │   ├── user.model.ts
│   │   └── api-response.model.ts
│   └── constants/
│       └── app.constants.ts
│
├── shared/                            ← Reusable components, pipes, directives
│   ├── components/
│   │   ├── header/
│   │   ├── footer/
│   │   ├── loading-spinner/
│   │   └── confirm-dialog/
│   ├── pipes/
│   │   ├── truncate.pipe.ts
│   │   └── time-ago.pipe.ts
│   ├── directives/
│   │   ├── highlight.directive.ts
│   │   └── click-outside.directive.ts
│   └── validators/
│       └── custom-validators.ts
│
├── features/                          ← Feature modules (lazy-loaded)
│   ├── dashboard/
│   │   ├── dashboard.component.ts
│   │   ├── dashboard.component.html
│   │   ├── dashboard.routes.ts
│   │   └── widgets/
│   ├── users/
│   │   ├── user-list/
│   │   ├── user-detail/
│   │   ├── user-form/
│   │   ├── services/
│   │   │   └── user.service.ts
│   │   └── users.routes.ts
│   └── admin/
│       ├── admin-dashboard/
│       ├── admin-settings/
│       └── admin.routes.ts
│
├── app.component.ts
├── app.config.ts
└── app.routes.ts
```

### Key Files Explained

| File | Purpose |
|------|---------|
| `main.ts` | Entry point — bootstraps the app |
| `app.config.ts` | Registers providers (HttpClient, Router, etc.) |
| `app.routes.ts` | Defines all application routes |
| `app.component.ts` | Root component — contains `<router-outlet>` |
| `angular.json` | Build config, assets, styles, scripts |
| `tsconfig.json` | TypeScript compiler options |
| `package.json` | npm dependencies and scripts |

### main.ts — Entry Point

```typescript
// Angular 17+ standalone bootstrap
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
```

### app.config.ts — Application Configuration

```typescript
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideAnimations()
  ]
};
```

--

## 5. Modules (NgModule)

NgModules are containers that group related components, directives, pipes, and services. They were the **primary way to organize Angular apps before standalone components**.

### Basic Module Structure

```typescript
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { UserListComponent } from './user-list/user-list.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { UserService } from './user.service';

@NgModule({
  declarations: [          // Components, Directives, Pipes that BELONG to this module
    UserListComponent,
    UserDetailComponent
  ],
  imports: [               // Other modules this module NEEDS
    CommonModule,          // *ngIf, *ngFor, pipes (DatePipe, etc.)
    FormsModule,           // Template-driven forms (ngModel)
    ReactiveFormsModule,   // Reactive forms (FormGroup, FormControl)
    RouterModule.forChild([
      { path: '', component: UserListComponent },
      { path: ':id', component: UserDetailComponent }
    ])
  ],
  exports: [               // What OTHER modules can use from this module
    UserListComponent      // Only export what needs to be used outside
  ],
  providers: [             // Services scoped to this module (optional)
    UserService            // Better: use providedIn: 'root' in the service itself
  ]
})
export class UserModule {}
```

### Module Types

| Type | Purpose | Example |
|------|---------|---------|
| **Root Module** | Bootstraps the app. Only one per app. | `AppModule` |
| **Feature Module** | Groups a feature (users, admin). Lazy-loaded. | `UserModule` |
| **Shared Module** | Reusable components/pipes/directives. Imported by many. | `SharedModule` |
| **Core Module** | Singleton services, guards, interceptors. Imported once. | `CoreModule` |

### Shared Module Pattern

```typescript
@NgModule({
  declarations: [
    LoadingSpinnerComponent,
    ConfirmDialogComponent,
    TruncatePipe,
    HighlightDirective
  ],
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  exports: [
    // Re-export everything other modules might need
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    LoadingSpinnerComponent,
    ConfirmDialogComponent,
    TruncatePipe,
    HighlightDirective
  ]
})
export class SharedModule {}
```

### Core Module Pattern (Import Once)

```typescript
@NgModule({
  providers: [AuthService, LoggerService]
})
export class CoreModule {
  // Prevent re-importing
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error('CoreModule is already loaded. Import it only in AppModule.');
    }
  }
}
```

--

## 6. Standalone Components (Angular 14+)

Standalone components **don't need NgModule**. They declare their own dependencies directly. This is the **default in Angular 17+**.

### Creating a Standalone Component

```typescript
@Component({
  selector: 'app-user-list',
  standalone: true,                    // No module needed!
  imports: [CommonModule, RouterLink], // Import what THIS component needs
  template: `
    @for (user of users; track user.id) {
      <div>
        <a [routerLink]="['/users', user.id]">{{ user.name }}</a>
      </div>
    }
  `
})
export class UserListComponent {
  users: User[] = [];
}
```

### Module vs Standalone Comparison

| Feature | NgModule | Standalone |
|---------|----------|-----------|
| **Introduced** | Angular 2 | Angular 14 |
| **Default** | Before Angular 17 | Angular 17+ |
| **Dependencies** | Declared in module | Declared in component |
| **Boilerplate** | More (module + component) | Less (component only) |
| **Lazy loading** | `loadChildren` | `loadComponent` |
| **Tree-shaking** | Module-level | Component-level (better) |

### Migrating from Modules to Standalone

```bash
# Angular CLI migration schematic
ng generate @angular/core:standalone
```

### Bootstrapping Standalone App

```typescript
// main.ts — No AppModule needed!
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';

bootstrapApplication(AppComponent, appConfig);
```

--

## 7. TypeScript Essentials for Angular

Angular is built with TypeScript. Here are the TypeScript features you **must know** for Angular development.

### Types

```typescript
// Basic types
let name: string = 'Rahul';
let age: number = 25;
let isActive: boolean = true;
let items: string[] = ['a', 'b', 'c'];
let tuple: [string, number] = ['Rahul', 25];

// Any (avoid in Angular — defeats type safety)
let data: any = 'anything';

// Unknown (safer than any — must check type before using)
let value: unknown = 'hello';
if (typeof value === 'string') {
  console.log(value.toUpperCase());  // OK after type check
}

// Void (function returns nothing)
function log(msg: string): void {
  console.log(msg);
}

// Null and Undefined
let x: string | null = null;
let y: string | undefined = undefined;
```

### Interfaces

```typescript
// Define shape of an object
interface User {
  id: number;
  name: string;
  email: string;
  role: 'admin' | 'user' | 'guest';  // Union type
  address?: Address;                   // Optional property
  readonly createdAt: Date;            // Cannot be modified
}

interface Address {
  street: string;
  city: string;
  pincode: string;
}

// Usage
const user: User = {
  id: 1,
  name: 'Rahul',
  email: 'rahul@test.com',
  role: 'admin',
  createdAt: new Date()
};
```

### Generics

```typescript
// Generic function
function getFirst<T>(items: T[]): T {
  return items[0];
}

const firstNum = getFirst<number>([1, 2, 3]);    // number
const firstStr = getFirst<string>(['a', 'b']);     // string

// Generic interface (used heavily in Angular services)
interface ApiResponse<T> {
  data: T;
  status: number;
  message: string;
}

// Usage in service
getUsers(): Observable<ApiResponse<User[]>> {
  return this.http.get<ApiResponse<User[]>>('/api/users');
}
```

### Enums

```typescript
// Numeric enum
enum UserRole {
  Admin = 1,
  User = 2,
  Guest = 3
}

// String enum (preferred in Angular)
enum Status {
  Active = 'ACTIVE',
  Inactive = 'INACTIVE',
  Pending = 'PENDING'
}

// Usage
const role: UserRole = UserRole.Admin;
const status: Status = Status.Active;
```

### Access Modifiers

```typescript
class UserService {
  public name: string;           // Accessible everywhere (default)
  private apiUrl: string;        // Only within this class
  protected baseUrl: string;     // Within class and subclasses
  readonly version: string;      // Cannot be modified after initialization

  // Shorthand constructor (Angular DI pattern)
  constructor(
    private http: HttpClient,    // Creates and assigns private property
    private router: Router
  ) {}
}
```

### Decorators (TypeScript Feature Used by Angular)

```typescript
// Decorators are functions that add metadata to classes/properties/methods
// Angular uses them extensively:

@Component({ ... })      // Class decorator
@Injectable({ ... })     // Class decorator
@Input()                 // Property decorator
@Output()                // Property decorator
@ViewChild('ref')        // Property decorator
@HostListener('click')   // Method decorator
```

--

## 8. Angular Versions & Key Changes

| Version | Year | Key Features |
|---------|------|-------------|
| **Angular 2** | 2016 | Complete rewrite from AngularJS. TypeScript, components, modules. |
| **Angular 4** | 2017 | Smaller bundles, `*ngIf else`, animations package. |
| **Angular 6** | 2018 | `ng add`, `ng update`, Angular Elements, `providedIn: 'root'`. |
| **Angular 8** | 2019 | Lazy loading with `import()`, differential loading. |
| **Angular 9** | 2020 | Ivy renderer (default), smaller bundles, faster compilation. |
| **Angular 12** | 2021 | Strict mode default, Webpack 5, nullish coalescing in templates. |
| **Angular 14** | 2022 | **Standalone components**, typed reactive forms, `inject()` function. |
| **Angular 15** | 2022 | Standalone APIs stable, functional guards/interceptors, `NgOptimizedImage`. |
| **Angular 16** | 2023 | **Signals**, `takeUntilDestroyed`, required inputs, SSR improvements. |
| **Angular 17** | 2023 | **New control flow** (`@if`, `@for`, `@switch`), deferred loading (`@defer`), new branding. |
| **Angular 18** | 2024 | Zoneless change detection (experimental), stable signals. |

### Angular 17+ New Control Flow

```html
<!-- Old syntax -->
<div *ngIf="isLoggedIn; else loginTemplate">
  Welcome, {{ user.name }}
</div>
<ng-template #loginTemplate>
  <p>Please login</p>
</ng-template>

<ul>
  <li *ngFor="let item of items; trackBy: trackById">{{ item.name }}</li>
</ul>

<!-- New syntax (Angular 17+) — PREFERRED -->
@if (isLoggedIn) {
  <div>Welcome, {{ user.name }}</div>
} @else {
  <p>Please login</p>
}

@for (item of items; track item.id) {
  <li>{{ item.name }}</li>
} @empty {
  <p>No items found.</p>
}

@switch (user.role) {
  @case ('admin') { <admin-panel /> }
  @case ('user') { <user-dashboard /> }
  @default { <guest-view /> }
}
```

### Deferred Loading (Angular 17+)

```html
<!-- Load component only when visible in viewport -->
@defer (on viewport) {
  <heavy-chart-component />
} @placeholder {
  <p>Chart will load when you scroll here...</p>
} @loading (minimum 500ms) {
  <p>Loading chart...</p>
} @error {
  <p>Failed to load chart.</p>
}

<!-- Other triggers -->
@defer (on idle) { ... }           <!-- When browser is idle -->
@defer (on interaction) { ... }    <!-- On click/focus -->
@defer (on hover) { ... }          <!-- On mouse hover -->
@defer (on timer(3s)) { ... }      <!-- After 3 seconds -->
@defer (when condition) { ... }    <!-- When expression is true -->
```

--

## 9. Angular vs AngularJS

| Feature | AngularJS (1.x) | Angular (2+) |
|---------|-----------------|-------------|
| **Language** | JavaScript | TypeScript |
| **Architecture** | MVC | Component-based |
| **Mobile support** | No | Yes (Ionic, NativeScript) |
| **CLI** | No | Yes (`ng`) |
| **Performance** | Slower (dirty checking) | Faster (Ivy, OnPush) |
| **Dependency Injection** | Basic | Hierarchical, advanced |
| **Rendering** | DOM manipulation | Virtual DOM-like (Ivy) |
| **Status** | End of life (Dec 2021) | Actively maintained |

**Important:** AngularJS and Angular are **completely different frameworks**. Angular is NOT an upgrade of AngularJS — it's a complete rewrite.

--

## 10. Angular vs React vs Vue

| Feature | Angular | React | Vue |
|---------|---------|-------|-----|
| **Type** | Full framework | Library | Progressive framework |
| **Language** | TypeScript | JavaScript/JSX | JavaScript |
| **Data binding** | Two-way | One-way | Two-way |
| **DOM** | Real DOM (with Ivy optimization) | Virtual DOM | Virtual DOM |
| **Size** | ~130KB | ~40KB | ~30KB |
| **Learning curve** | Steep | Moderate | Easy |
| **Routing** | Built-in | react-router | vue-router |
| **State** | Services/NgRx | Context/Redux | Vuex/Pinia |
| **CLI** | `ng` | `create-react-app`/Vite | `create-vue` |
| **Backed by** | Google | Meta (Facebook) | Community (Evan You) |
| **Best for** | Large enterprise apps | Flexible UI development | Small-medium apps |

--

## 11. Decorators

Decorators are **TypeScript functions** that attach metadata to classes, methods, or properties. Angular uses decorators extensively.

### Class Decorators

```typescript
// @Component — defines a component
@Component({
  selector: 'app-user',           // HTML tag name
  standalone: true,                // No module needed
  imports: [CommonModule],         // Dependencies
  templateUrl: './user.component.html',  // External template
  styleUrls: ['./user.component.css'],   // External styles
  changeDetection: ChangeDetectionStrategy.OnPush,  // Performance
  encapsulation: ViewEncapsulation.Emulated          // Style scoping
})
export class UserComponent {}

// @Injectable — marks a class as injectable service
@Injectable({
  providedIn: 'root'  // Singleton, available app-wide
})
export class UserService {}

// @NgModule — defines a module (legacy, before standalone)
@NgModule({
  declarations: [...],
  imports: [...],
  exports: [...],
  providers: [...]
})
export class UserModule {}

// @Directive — defines a directive
@Directive({
  selector: '[appHighlight]',
  standalone: true
})
export class HighlightDirective {}

// @Pipe — defines a pipe
@Pipe({
  name: 'truncate',
  standalone: true,
  pure: true           // Default: only recalculates when input reference changes
})
export class TruncatePipe implements PipeTransform {}
```

### Property Decorators

```typescript
export class UserDetailComponent {
  @Input() userId!: number;                    // Receive from parent
  @Input({ required: true }) name!: string;    // Required input (Angular 16+)
  @Output() userDeleted = new EventEmitter<number>();  // Emit to parent
  @ViewChild('nameInput') nameInput!: ElementRef;      // Access DOM element
  @ViewChild(ChildComponent) child!: ChildComponent;   // Access child component
  @ContentChild(HeaderComponent) header!: HeaderComponent;  // Access projected content
}
```

### Method Decorators

```typescript
@Directive({ selector: '[appClickTracker]' })
export class ClickTrackerDirective {
  @HostListener('click', ['$event'])
  onClick(event: MouseEvent) {
    console.log('Element clicked at:', event.clientX, event.clientY);
  }

  @HostListener('mouseenter')
  onMouseEnter() {
    console.log('Mouse entered');
  }
}
```

--

## 12. Angular Compilation (AOT vs JIT)

### JIT (Just-in-Time) Compilation

- Compiles TypeScript and templates **in the browser at runtime**.
- Used during **development** (`ng serve`).
- Larger bundle (includes Angular compiler).
- Slower startup.
- Errors caught at runtime.

### AOT (Ahead-of-Time) Compilation

- Compiles TypeScript and templates **at build time** before the browser downloads.
- Used in **production** (`ng build`).
- Smaller bundle (no compiler needed).
- Faster startup.
- Errors caught at build time (template errors, type errors).

### Comparison

| Feature | JIT | AOT |
|---------|-----|-----|
| **When** | Runtime (browser) | Build time |
| **Bundle size** | Larger (+compiler) | Smaller |
| **Startup** | Slower | Faster |
| **Error detection** | Runtime | Build time |
| **Security** | Templates as strings | Pre-compiled (no eval) |
| **Default** | `ng serve` | `ng build --prod` |

### How AOT Works

```
TypeScript + HTML Templates
        ↓
Angular Compiler (ngc)
        ↓
Optimized JavaScript (no templates to parse)
        ↓
Tree-shaking (remove unused code)
        ↓
Minification + Bundling
        ↓
Small, fast production bundle
```

--

## 13. Zone.js & Change Detection Basics

### What is Zone.js?

Zone.js is a library that **patches all async browser APIs** (setTimeout, Promise, addEventListener, XHR, fetch) to notify Angular when an async operation completes. This triggers **change detection** automatically.

### How Change Detection Works

```
1. User clicks a button
        ↓
2. Zone.js detects the async event
        ↓
3. Zone.js notifies Angular: "Something happened!"
        ↓
4. Angular runs change detection from ROOT component DOWN the tree
        ↓
5. For each component:
   - Check all template bindings ({{ value }}, [property], etc.)
   - If value changed → update the DOM
        ↓
6. User sees the updated view
```

### What Triggers Change Detection?

- **DOM events:** click, input, submit, mouseover
- **HTTP responses:** API calls completing
- **Timers:** setTimeout, setInterval
- **Promises:** Promise.resolve/reject
- **Observables:** When they emit (via async pipe)

### Change Detection Strategies

```typescript
// Default — checks EVERY component on EVERY event
@Component({
  changeDetection: ChangeDetectionStrategy.Default
})

// OnPush — only checks when:
// 1. @Input reference changes
// 2. Event fires within this component
// 3. Observable emits via async pipe
// 4. Manual markForCheck() called
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush
})
```

### Zoneless Angular (Angular 18+ Experimental)

```typescript
// No Zone.js needed — uses Signals for reactivity
import { provideExperimentalZonelessChangeDetection } from '@angular/core';

export const appConfig: ApplicationConfig = {
  providers: [
    provideExperimentalZonelessChangeDetection()
  ]
};
```

--

## 14. Signals (Angular 16+)

Signals are a **new reactive primitive** for managing state in Angular. They are simpler than RxJS for synchronous state and integrate with Angular's change detection.

### Why Signals?

| Problem with Current Approach | How Signals Fix It |
|-------------------------------|-------------------|
| Zone.js patches ALL async APIs (overhead) | Signals notify Angular directly |
| Change detection checks entire tree | Signals enable fine-grained updates |
| RxJS is complex for simple state | Signals are simple get/set |
| Memory leaks from forgotten unsubscribe | No subscriptions needed |

### Signal Basics

```typescript
import { signal, computed, effect } from '@angular/core';

@Component({
  template: `
    <p>Count: {{ count() }}</p>
    <p>Double: {{ doubleCount() }}</p>
    <button (click)="increment()">+1</button>
    <button (click)="reset()">Reset</button>
  `
})
export class CounterComponent {
  // Writable signal — holds a value
  count = signal(0);

  // Computed signal — derived from other signals (read-only, auto-updates)
  doubleCount = computed(() => this.count() * 2);

  // Effect — side effect that runs when signals it reads change
  logger = effect(() => {
    console.log('Count changed to:', this.count());
  });

  increment() {
    // update() — modify based on current value
    this.count.update(c => c + 1);
  }

  reset() {
    // set() — replace value entirely
    this.count.set(0);
  }
}
```

### Signals vs Observables

| Feature | Signal | Observable (RxJS) |
|---------|--------|-------------------|
| **Values** | Single current value | Stream of values over time |
| **Read** | `signal()` (call it) | `.subscribe()` or `async` pipe |
| **Write** | `.set()`, `.update()` | `.next()` on Subject |
| **Lazy** | No (always has value) | Yes (only on subscribe) |
| **Async** | Synchronous | Asynchronous |
| **Operators** | `computed()`, `effect()` | `pipe()`, `map()`, `filter()`, etc. |
| **Unsubscribe** | Not needed | Required (memory leaks) |
| **Best for** | Component state, UI state | HTTP, events, WebSocket, complex async |

### Signal-based Inputs (Angular 17.1+)

```typescript
@Component({ ... })
export class UserCardComponent {
  // Signal-based input (replaces @Input decorator)
  name = input<string>();                    // Optional
  userId = input.required<number>();         // Required

  // Computed from input signal
  displayName = computed(() => this.name()?.toUpperCase() ?? 'Unknown');
}
```

### Signal-based Outputs (Angular 17.3+)

```typescript
@Component({ ... })
export class UserCardComponent {
  // Signal-based output (replaces @Output + EventEmitter)
  deleted = output<number>();

  onDelete(id: number) {
    this.deleted.emit(id);
  }
}
```

--

## 15. Environment Configuration

### Environment Files

```typescript
// src/environments/environment.ts (development)
export const environment = {
  production: false,
  apiUrl: 'http://localhost:3000/api',
  enableDebug: true
};

// src/environments/environment.prod.ts (production)
export const environment = {
  production: true,
  apiUrl: 'https://api.myapp.com/api',
  enableDebug: false
};
```

### Using Environment Variables

```typescript
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }
}
```

### angular.json Configuration

```json
{
  "configurations": {
    "production": {
      "fileReplacements": [
        {
          "replace": "src/environments/environment.ts",
          "with": "src/environments/environment.prod.ts"
        }
      ],
      "budgets": [
        {
          "type": "initial",
          "maximumWarning": "500kb",
          "maximumError": "1mb"
        }
      ]
    }
  }
}
```

### Build for Different Environments

```bash
# Development (uses environment.ts)
ng serve

# Production (uses environment.prod.ts)
ng build --configuration=production

# Custom environment
ng build --configuration=staging
```
