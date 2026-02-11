# Angular Components - Deep Dive (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** Component anatomy, lifecycle hooks, communication patterns, content projection, ViewEncapsulation, dynamic components, and best practices.

--

## Table of Contents

1. [What is a Component?](#what-is-a-component)
2. [Component Anatomy](#component-anatomy)
3. [Component Metadata (@Component)](#component-metadata)
4. [Templates — Inline vs External](#templates-inline-vs-external)
5. [Styles — Scoping & Encapsulation](#styles-scoping-encapsulation)
6. [Lifecycle Hooks](#lifecycle-hooks)
7. [Component Communication](#component-communication)
8. [@Input — Parent to Child](#input-parent-to-child)
9. [@Output — Child to Parent](#output-child-to-parent)
10. [@ViewChild & @ViewChildren](#viewchild-viewchildren)
11. [@ContentChild & @ContentChildren](#contentchild-contentchildren)
12. [Content Projection (ng-content)](#content-projection)
13. [Dynamic Components](#dynamic-components)
14. [Component Inheritance](#component-inheritance)
15. [Smart vs Dumb Components](#smart-vs-dumb-components)
16. [Best Practices](#best-practices)

--

## 1. What is a Component?

A component is the **fundamental building block** of an Angular application. Every Angular app is a **tree of components** starting from the root component (`AppComponent`).

A component controls a **patch of the screen** called a **view**. It combines:
- **Template (HTML)** — what the user sees
- **Class (TypeScript)** — logic and data
- **Styles (CSS/SCSS)** — how it looks
- **Metadata (@Component)** — configuration

### Component Tree

```
AppComponent
├── HeaderComponent
│   ├── LogoComponent
│   └── NavMenuComponent
├── SidebarComponent
│   └── MenuItemComponent (×5)
├── MainContentComponent
│   ├── DashboardComponent
│   │   ├── ChartComponent
│   │   └── StatsCardComponent (×4)
│   └── UserListComponent
│       └── UserCardComponent (×N)
└── FooterComponent
```

--

## 2. Component Anatomy

### Minimal Component

```typescript
import { Component } from '@angular/core';

@Component({
  selector: 'app-hello',
  standalone: true,
  template: `<h1>Hello, Angular!</h1>`
})
export class HelloComponent {}
```

### Full Component

```typescript
import { Component, Input, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserCardComponent implements OnInit, OnDestroy {
  @Input({ required: true }) user!: User;
  @Output() deleted = new EventEmitter<number>();

  isExpanded = false;

  ngOnInit() {
    console.log('UserCard initialized for:', this.user.name);
  }

  toggleExpand() {
    this.isExpanded = !this.isExpanded;
  }

  onDelete() {
    this.deleted.emit(this.user.id);
  }

  ngOnDestroy() {
    console.log('UserCard destroyed for:', this.user.name);
  }
}
```

--

## 3. Component Metadata (@Component)

The `@Component` decorator accepts a metadata object that tells Angular how to create and present the component.

```typescript
@Component({
  // REQUIRED
  selector: 'app-user-card',        // CSS selector — how to use in HTML
  template: `<p>Inline HTML</p>`,   // OR templateUrl: './user.component.html'

  // OPTIONAL
  standalone: true,                  // No NgModule needed (default in Angular 17+)
  imports: [CommonModule, FormsModule],  // Dependencies (standalone only)
  styleUrls: ['./user.component.scss'],  // External styles
  styles: [`p { color: red; }`],         // Inline styles
  changeDetection: ChangeDetectionStrategy.OnPush,  // Performance optimization
  encapsulation: ViewEncapsulation.Emulated,         // Style scoping
  providers: [UserService],          // Component-scoped DI
  host: {                            // Host element bindings
    'class': 'user-card',
    '[class.active]': 'isActive',
    '(click)': 'onClick()'
  },
  animations: [fadeInAnimation]      // Angular animations
})
```

### Selector Types

```typescript
// Element selector (most common)
selector: 'app-user-card'
// Usage: <app-user-card></app-user-card>

// Attribute selector
selector: '[appUserCard]'
// Usage: <div appUserCard></div>

// Class selector (rare)
selector: '.user-card'
// Usage: <div class="user-card"></div>
```

**Convention:** Always prefix component selectors with your app prefix (e.g., `app-`, `my-`). Configure in `angular.json` → `prefix`.

--

## 4. Templates — Inline vs External

### Inline Template

```typescript
@Component({
  selector: 'app-greeting',
  standalone: true,
  template: `
    <h2>Hello, {{ name }}!</h2>
    <p>Welcome to our app.</p>
  `
})
export class GreetingComponent {
  name = 'Rahul';
}
```

### External Template

```typescript
@Component({
  selector: 'app-user-list',
  standalone: true,
  templateUrl: './user-list.component.html'
})
export class UserListComponent { ... }
```

```html
<!-- user-list.component.html -->
<div class="user-list">
  <h2>Users</h2>
  @for (user of users; track user.id) {
    <app-user-card [user]="user" (deleted)="onDelete($event)" />
  } @empty {
    <p>No users found.</p>
  }
</div>
```

### When to Use Which?

| Approach | Use When |
|----------|----------|
| **Inline** | Template is small (< 10 lines), simple components |
| **External** | Template is complex, has many elements, easier to read |

### Template Syntax Quick Reference

```html
<!-- Interpolation -->
{{ user.name }}
{{ 1 + 1 }}
{{ getFullName() }}

<!-- Property binding -->
<img [src]="user.avatar" [alt]="user.name">
<button [disabled]="isLoading">Submit</button>

<!-- Event binding -->
<button (click)="save()">Save</button>
<input (input)="onSearch($event)">
<input (keyup.enter)="submit()">

<!-- Two-way binding -->
<input [(ngModel)]="searchTerm">

<!-- Template reference variable -->
<input #nameInput>
<button (click)="greet(nameInput.value)">Greet</button>

<!-- Safe navigation operator (optional chaining) -->
{{ user?.address?.city }}

<!-- Non-null assertion -->
{{ user!.name }}

<!-- Nullish coalescing -->
{{ user.nickname ?? 'No nickname' }}

<!-- Ternary in template -->
<p>{{ isAdmin ? 'Admin' : 'User' }}</p>
```

--

## 5. Styles — Scoping & Encapsulation

### ViewEncapsulation

Angular scopes component styles so they **don't leak** to other components. This is controlled by `ViewEncapsulation`.

```typescript
import { ViewEncapsulation } from '@angular/core';

@Component({
  encapsulation: ViewEncapsulation.Emulated  // Default
})
```

| Mode | Behavior | Use Case |
|------|----------|----------|
| **Emulated** (default) | Scopes styles using attribute selectors. Styles only apply to this component. | Most components |
| **None** | No scoping. Styles become **global**. Affects entire app. | Global utility styles (use carefully!) |
| **ShadowDom** | Uses native Shadow DOM. True browser-level isolation. | Web components, strict isolation |

### How Emulated Encapsulation Works

```typescript
@Component({
  selector: 'app-user',
  template: `<p class="name">Rahul</p>`,
  styles: [`
    .name { color: blue; font-weight: bold; }
  `]
})
```

Angular generates:
```html
<!-- Angular adds a unique attribute to scope styles -->
<app-user _nghost-abc>
  <p _ngcontent-abc class="name">Rahul</p>
</app-user>

<!-- Generated CSS -->
<style>
  .name[_ngcontent-abc] { color: blue; font-weight: bold; }
</style>
```

### Styling Child Components

```scss
// Problem: Parent styles DON'T reach child components (by design)

// Solution 1: ::ng-deep (deprecated but widely used)
:host ::ng-deep .child-class {
  color: red;
}

// Solution 2: Global styles (styles.scss)
.child-class { color: red; }

// Solution 3: ViewEncapsulation.None (dangerous — affects everything)

// Solution 4: CSS custom properties (recommended)
// Parent
:host {
  --text-color: red;
}
// Child reads it
.child-class {
  color: var(--text-color, black);  // fallback: black
}
```

### :host Selector

```scss
// Style the HOST element itself (the <app-user> tag)
:host {
  display: block;
  padding: 16px;
  border: 1px solid #ccc;
}

// Conditional host styling
:host(.active) {
  border-color: blue;
}

// Host context — style based on parent
:host-context(.dark-theme) {
  background: #333;
  color: white;
}
```

--

## 6. Lifecycle Hooks

Angular components have a lifecycle managed by Angular. Lifecycle hooks let you tap into key moments.

### Complete Lifecycle Order

```
1. constructor()              — DI only. No @Input values yet.
        ↓
2. ngOnChanges()              — @Input values received/changed.
        ↓                       Called BEFORE ngOnInit and on every @Input change.
3. ngOnInit()                 — Component initialized. @Input values available.
        ↓                       Called ONCE. Best place for initialization logic.
4. ngDoCheck()                — Custom change detection. Called on EVERY change detection run.
        ↓
5. ngAfterContentInit()       — After <ng-content> projected content initialized.
        ↓                       Called ONCE.
6. ngAfterContentChecked()    — After projected content checked.
        ↓                       Called on every change detection run.
7. ngAfterViewInit()          — After component's view (and child views) initialized.
        ↓                       Called ONCE. @ViewChild available here.
8. ngAfterViewChecked()       — After view checked.
        ↓                       Called on every change detection run.
9. ngOnDestroy()              — Before component is destroyed.
                                Cleanup: unsubscribe, clear timers, close connections.
```

### Most Important Hooks (Interview Focus)

| Hook | When | Use For | Called |
|------|------|---------|--------|
| **ngOnChanges** | @Input changes | React to parent data changes | Multiple times |
| **ngOnInit** | After first ngOnChanges | Fetch data, initialize logic | Once |
| **ngAfterViewInit** | After view rendered | Access @ViewChild, DOM manipulation | Once |
| **ngOnDestroy** | Before removal | Unsubscribe, cleanup | Once |

### ngOnInit — Initialization

```typescript
export class UserListComponent implements OnInit {
  users: User[] = [];
  @Input() departmentId!: number;

  constructor(private userService: UserService) {
    // Constructor: ONLY for dependency injection
    // @Input values are NOT available here
    console.log(this.departmentId);  // undefined!
  }

  ngOnInit() {
    // ngOnInit: @Input values ARE available
    // Perfect for: API calls, initialization logic
    console.log(this.departmentId);  // has value!
    this.loadUsers();
  }

  private loadUsers() {
    this.userService.getUsersByDept(this.departmentId)
      .subscribe(users => this.users = users);
  }
}
```

### ngOnChanges — React to Input Changes

```typescript
export class UserDetailComponent implements OnChanges {
  @Input() userId!: number;
  @Input() mode: 'view' | 'edit' = 'view';
  user: User | null = null;

  ngOnChanges(changes: SimpleChanges) {
    // Called BEFORE ngOnInit (first time) and on every @Input change

    if (changes['userId']) {
      const change = changes['userId'];
      console.log('userId changed from', change.previousValue, 'to', change.currentValue);
      console.log('Is first change?', change.firstChange);

      // Reload user data when userId changes
      if (!change.firstChange) {
        this.loadUser(change.currentValue);
      }
    }

    if (changes['mode']) {
      this.handleModeChange(changes['mode'].currentValue);
    }
  }
}
```

### ngAfterViewInit — Access View Elements

```typescript
export class SearchComponent implements AfterViewInit {
  @ViewChild('searchInput') searchInput!: ElementRef<HTMLInputElement>;
  @ViewChild(DataTableComponent) dataTable!: DataTableComponent;

  ngAfterViewInit() {
    // DOM is ready, @ViewChild references are available
    this.searchInput.nativeElement.focus();

    // Access child component methods
    this.dataTable.refresh();

    // CAUTION: Changing component state here causes
    // ExpressionChangedAfterItHasBeenCheckedError
    // Fix: use setTimeout or ChangeDetectorRef
  }
}
```

### ngOnDestroy — Cleanup

```typescript
export class DashboardComponent implements OnDestroy {
  private subscriptions: Subscription[] = [];
  private refreshInterval: any;
  private ws!: WebSocket;

  ngOnInit() {
    // Subscription 1
    this.subscriptions.push(
      this.dataService.getData().subscribe(data => this.data = data)
    );

    // Subscription 2
    this.subscriptions.push(
      this.authService.user$.subscribe(user => this.currentUser = user)
    );

    // Interval
    this.refreshInterval = setInterval(() => this.refresh(), 30000);

    // WebSocket
    this.ws = new WebSocket('ws://api.example.com/live');
  }

  ngOnDestroy() {
    // 1. Unsubscribe ALL observables
    this.subscriptions.forEach(sub => sub.unsubscribe());

    // 2. Clear timers
    clearInterval(this.refreshInterval);

    // 3. Close connections
    this.ws.close();

    // 4. Remove manual event listeners
    // window.removeEventListener('resize', this.onResize);
  }
}
```

### Modern Cleanup — takeUntilDestroyed (Angular 16+)

```typescript
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

export class DashboardComponent {
  constructor(
    private dataService: DataService,
    private authService: AuthService
  ) {
    // Auto-unsubscribes when component is destroyed — no ngOnDestroy needed!
    this.dataService.getData().pipe(
      takeUntilDestroyed()
    ).subscribe(data => this.data = data);

    this.authService.user$.pipe(
      takeUntilDestroyed()
    ).subscribe(user => this.currentUser = user);
  }
}
```

--

## 7. Component Communication

Angular provides multiple ways for components to communicate.

### Communication Patterns Overview

```
Parent ──@Input()──► Child              (data down)
Parent ◄──@Output()── Child             (events up)
Parent ──@ViewChild()──► Child          (direct access)
Any ◄──Service──► Any                   (shared state)
Parent ──<ng-content>──► Child          (content projection)
```

| Pattern | Direction | Use Case |
|---------|-----------|----------|
| **@Input** | Parent → Child | Pass data to child |
| **@Output** | Child → Parent | Notify parent of events |
| **@ViewChild** | Parent → Child | Direct access to child component/DOM |
| **Service** | Any ↔ Any | Shared state between unrelated components |
| **ng-content** | Parent → Child | Pass HTML content to child |
| **Template ref** | Template → Class | Access DOM elements |

--

## 8. @Input — Parent to Child

### Basic @Input

```typescript
// Child component
@Component({
  selector: 'app-user-card',
  standalone: true,
  template: `
    <div class="card">
      <h3>{{ user.name }}</h3>
      <p>{{ user.email }}</p>
      <span class="badge" [class]="theme">{{ user.role }}</span>
    </div>
  `
})
export class UserCardComponent {
  @Input() user!: User;
  @Input() theme: 'light' | 'dark' = 'light';  // With default value
}

// Parent component template
<app-user-card [user]="selectedUser" theme="dark"></app-user-card>
```

### Required Inputs (Angular 16+)

```typescript
export class UserCardComponent {
  @Input({ required: true }) user!: User;  // Must be provided by parent
}

// Signal-based required input (Angular 17.1+)
export class UserCardComponent {
  user = input.required<User>();
  theme = input<string>('light');  // Optional with default
}
```

### Input Transform (Angular 16+)

```typescript
export class ToggleComponent {
  // Automatically converts string "true"/"false" to boolean
  @Input({ transform: booleanAttribute }) disabled = false;

  // Automatically converts string to number
  @Input({ transform: numberAttribute }) size = 16;
}

// Usage — these all work:
<app-toggle disabled></app-toggle>          <!-- disabled = true -->
<app-toggle disabled="true"></app-toggle>   <!-- disabled = true -->
<app-toggle [disabled]="false"></app-toggle> <!-- disabled = false -->
<app-toggle size="24"></app-toggle>          <!-- size = 24 (number) -->
```

### Input Aliasing

```typescript
export class UserCardComponent {
  @Input('userData') user!: User;  // External name: userData, internal name: user
}

// Parent uses the alias
<app-user-card [userData]="selectedUser"></app-user-card>
```

### Input Setter (React to Changes)

```typescript
export class SearchResultsComponent {
  private _searchTerm = '';
  filteredResults: Result[] = [];

  @Input()
  set searchTerm(value: string) {
    this._searchTerm = value;
    this.filterResults();  // React to every change
  }
  get searchTerm(): string {
    return this._searchTerm;
  }

  private filterResults() {
    this.filteredResults = this.allResults.filter(r =>
      r.name.toLowerCase().includes(this._searchTerm.toLowerCase())
    );
  }
}
```

--

## 9. @Output — Child to Parent

### Basic @Output

```typescript
// Child component
@Component({
  selector: 'app-user-card',
  standalone: true,
  template: `
    <div class="card">
      <h3>{{ user.name }}</h3>
      <button (click)="onEdit()">Edit</button>
      <button (click)="onDelete()">Delete</button>
    </div>
  `
})
export class UserCardComponent {
  @Input({ required: true }) user!: User;
  @Output() edited = new EventEmitter<User>();
  @Output() deleted = new EventEmitter<number>();

  onEdit() {
    this.edited.emit(this.user);
  }

  onDelete() {
    this.deleted.emit(this.user.id);
  }
}

// Parent component
@Component({
  template: `
    @for (user of users; track user.id) {
      <app-user-card
        [user]="user"
        (edited)="openEditDialog($event)"
        (deleted)="deleteUser($event)">
      </app-user-card>
    }
  `
})
export class UserListComponent {
  users: User[] = [];

  openEditDialog(user: User) {
    console.log('Edit user:', user);
  }

  deleteUser(userId: number) {
    this.users = this.users.filter(u => u.id !== userId);
  }
}
```

### Two-Way Binding with @Input + @Output

```typescript
// Child — convention: @Output name = @Input name + "Change"
@Component({
  selector: 'app-star-rating',
  standalone: true,
  template: `
    @for (star of [1,2,3,4,5]; track star) {
      <span (click)="setRating(star)" [style.color]="star <= rating ? 'gold' : 'gray'">★</span>
    }
  `
})
export class StarRatingComponent {
  @Input() rating = 0;
  @Output() ratingChange = new EventEmitter<number>();  // Must be: inputName + "Change"

  setRating(value: number) {
    this.rating = value;
    this.ratingChange.emit(value);
  }
}

// Parent — two-way binding with banana-in-a-box syntax
<app-star-rating [(rating)]="product.rating"></app-star-rating>

// Which is shorthand for:
<app-star-rating [rating]="product.rating" (ratingChange)="product.rating = $event"></app-star-rating>
```

### Signal-based Output (Angular 17.3+)

```typescript
import { output } from '@angular/core';

export class UserCardComponent {
  deleted = output<number>();     // Replaces @Output() + EventEmitter

  onDelete(id: number) {
    this.deleted.emit(id);
  }
}
```

--

## 10. @ViewChild & @ViewChildren

### @ViewChild — Access Single Child

```typescript
@Component({
  template: `
    <input #searchInput placeholder="Search...">
    <app-data-table #table [data]="users"></app-data-table>
    <button (click)="focusAndRefresh()">Refresh</button>
  `
})
export class UserListComponent implements AfterViewInit {
  // Access DOM element
  @ViewChild('searchInput') searchInput!: ElementRef<HTMLInputElement>;

  // Access child component instance
  @ViewChild('table') dataTable!: DataTableComponent;

  // Access by component type (no template ref needed)
  @ViewChild(DataTableComponent) dataTableByType!: DataTableComponent;

  ngAfterViewInit() {
    // @ViewChild is available ONLY after ngAfterViewInit
    this.searchInput.nativeElement.focus();
  }

  focusAndRefresh() {
    this.searchInput.nativeElement.focus();
    this.searchInput.nativeElement.value = '';
    this.dataTable.refresh();  // Call child component method
  }
}
```

### @ViewChild with Static Option

```typescript
// static: true — resolved BEFORE ngOnInit (available in ngOnInit)
// Use when the element is NOT inside *ngIf, *ngFor, or @if
@ViewChild('alwaysVisible', { static: true }) element!: ElementRef;

// static: false (default) — resolved AFTER ngAfterViewInit
// Use when the element might be conditionally rendered
@ViewChild('conditionalElement', { static: false }) element!: ElementRef;
```

### @ViewChildren — Access Multiple Children

```typescript
@Component({
  template: `
    @for (item of items; track item.id) {
      <app-item-card #card [item]="item"></app-item-card>
    }
    <button (click)="expandAll()">Expand All</button>
  `
})
export class ItemListComponent implements AfterViewInit {
  @ViewChildren('card') cards!: QueryList<ItemCardComponent>;

  ngAfterViewInit() {
    console.log('Total cards:', this.cards.length);

    // React to changes (items added/removed)
    this.cards.changes.subscribe((cards: QueryList<ItemCardComponent>) => {
      console.log('Cards changed, new count:', cards.length);
    });
  }

  expandAll() {
    this.cards.forEach(card => card.expand());
  }
}
```

--

## 11. @ContentChild & @ContentChildren

These access **projected content** (content passed via `<ng-content>`).

```typescript
// Child component (wrapper)
@Component({
  selector: 'app-card',
  standalone: true,
  template: `
    <div class="card">
      <div class="card-header">
        <ng-content select="[cardHeader]"></ng-content>
      </div>
      <div class="card-body">
        <ng-content></ng-content>
      </div>
    </div>
  `
})
export class CardComponent implements AfterContentInit {
  @ContentChild('cardTitle') titleElement!: ElementRef;
  @ContentChildren(ButtonComponent) buttons!: QueryList<ButtonComponent>;

  ngAfterContentInit() {
    // Projected content is available here
    console.log('Title:', this.titleElement?.nativeElement.textContent);
    console.log('Buttons:', this.buttons.length);
  }
}

// Parent usage
<app-card>
  <h3 cardHeader #cardTitle>User Profile</h3>
  <p>This is the card body content.</p>
  <app-button label="Save"></app-button>
  <app-button label="Cancel"></app-button>
</app-card>
```

### @ViewChild vs @ContentChild

| Feature | @ViewChild | @ContentChild |
|---------|-----------|--------------|
| **Accesses** | Elements in component's own template | Elements projected via `<ng-content>` |
| **Available in** | `ngAfterViewInit` | `ngAfterContentInit` |
| **Defined in** | Component's template | Parent's template (projected) |

--

## 12. Content Projection (ng-content)

Content projection lets you pass HTML from a parent into a child component's template. Similar to React's `children` prop or Vue's `slots`.

### Single-Slot Projection

```typescript
// Card component
@Component({
  selector: 'app-card',
  standalone: true,
  template: `
    <div class="card">
      <ng-content></ng-content>  <!-- Everything from parent goes here -->
    </div>
  `,
  styles: [`.card { border: 1px solid #ccc; padding: 16px; border-radius: 8px; }`]
})
export class CardComponent {}

// Usage
<app-card>
  <h3>User Profile</h3>
  <p>Name: Rahul Sharma</p>
  <p>Email: rahul@test.com</p>
</app-card>
```

### Multi-Slot Projection

```typescript
@Component({
  selector: 'app-dialog',
  standalone: true,
  template: `
    <div class="dialog-overlay">
      <div class="dialog">
        <div class="dialog-header">
          <ng-content select="[dialogTitle]"></ng-content>
          <button (click)="close()">×</button>
        </div>
        <div class="dialog-body">
          <ng-content select="[dialogBody]"></ng-content>
        </div>
        <div class="dialog-footer">
          <ng-content select="[dialogFooter]"></ng-content>
        </div>
      </div>
    </div>
  `
})
export class DialogComponent {
  @Output() closed = new EventEmitter<void>();
  close() { this.closed.emit(); }
}

// Usage — content goes to matching slots
<app-dialog (closed)="onClose()">
  <h2 dialogTitle>Confirm Delete</h2>
  <p dialogBody>Are you sure you want to delete this user?</p>
  <div dialogFooter>
    <button (click)="onClose()">Cancel</button>
    <button (click)="onConfirm()">Delete</button>
  </div>
</app-dialog>
```

### Conditional Projection with ng-template

```typescript
@Component({
  selector: 'app-data-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    @for (item of items; track item.id) {
      <div class="list-item">
        <!-- Use custom template if provided, otherwise default -->
        <ng-container *ngTemplateOutlet="itemTemplate || defaultTemplate; context: { $implicit: item }">
        </ng-container>
      </div>
    }

    <ng-template #defaultTemplate let-item>
      <span>{{ item.name }}</span>
    </ng-template>
  `
})
export class DataListComponent {
  @Input() items: any[] = [];
  @ContentChild('itemTemplate') itemTemplate!: TemplateRef<any>;
}

// Usage with custom template
<app-data-list [items]="users">
  <ng-template #itemTemplate let-user>
    <div class="custom-item">
      <img [src]="user.avatar" />
      <strong>{{ user.name }}</strong>
      <span>{{ user.email }}</span>
    </div>
  </ng-template>
</app-data-list>

// Usage with default template (no ng-template provided)
<app-data-list [items]="users"></app-data-list>
```

--

## 13. Dynamic Components

Creating components programmatically at runtime.

### Using ViewContainerRef (Modern Approach)

```typescript
@Component({
  selector: 'app-dynamic-host',
  standalone: true,
  template: `
    <div #container></div>
    <button (click)="loadAlert()">Show Alert</button>
    <button (click)="loadChart()">Show Chart</button>
  `
})
export class DynamicHostComponent {
  @ViewChild('container', { read: ViewContainerRef }) container!: ViewContainerRef;

  loadAlert() {
    this.container.clear();
    const componentRef = this.container.createComponent(AlertComponent);
    componentRef.instance.message = 'This is a dynamic alert!';
    componentRef.instance.type = 'warning';
  }

  loadChart() {
    this.container.clear();
    const componentRef = this.container.createComponent(ChartComponent);
    componentRef.instance.data = [10, 20, 30, 40];
  }
}
```

### Lazy Loading Dynamic Components

```typescript
async loadHeavyComponent() {
  this.container.clear();
  const { HeavyChartComponent } = await import('./heavy-chart/heavy-chart.component');
  this.container.createComponent(HeavyChartComponent);
}
```

--

## 14. Component Inheritance

Components can extend other components to share logic. Use sparingly — prefer composition.

```typescript
// Base component
@Component({ template: '' })
export abstract class BaseListComponent<T> implements OnInit, OnDestroy {
  items: T[] = [];
  loading = false;
  error = '';
  protected destroy$ = new Subject<void>();

  abstract loadItems(): Observable<T[]>;

  ngOnInit() {
    this.loading = true;
    this.loadItems().pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (items) => { this.items = items; this.loading = false; },
      error: (err) => { this.error = err.message; this.loading = false; }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// Concrete component
@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (loading) { <p>Loading...</p> }
    @if (error) { <p class="error">{{ error }}</p> }
    @for (user of items; track user.id) {
      <div>{{ user.name }}</div>
    }
  `
})
export class UserListComponent extends BaseListComponent<User> {
  constructor(private userService: UserService) { super(); }

  loadItems(): Observable<User[]> {
    return this.userService.getUsers();
  }
}
```

--

## 15. Smart vs Dumb Components

### Smart (Container) Components

- **Know about** services, state, routing.
- **Fetch data** from APIs.
- **Handle business logic**.
- **Pass data** to dumb components via @Input.
- **React to events** from dumb components via @Output.

```typescript
// Smart component — knows about services, fetches data
@Component({
  selector: 'app-user-page',
  standalone: true,
  imports: [UserListComponent, UserFilterComponent],
  template: `
    <app-user-filter (filterChanged)="onFilter($event)"></app-user-filter>
    <app-user-list [users]="filteredUsers" (userDeleted)="onDelete($event)"></app-user-list>
  `
})
export class UserPageComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.getUsers().subscribe(users => {
      this.users = users;
      this.filteredUsers = users;
    });
  }

  onFilter(filter: string) {
    this.filteredUsers = this.users.filter(u =>
      u.name.toLowerCase().includes(filter.toLowerCase())
    );
  }

  onDelete(userId: number) {
    this.userService.deleteUser(userId).subscribe(() => {
      this.users = this.users.filter(u => u.id !== userId);
      this.filteredUsers = this.filteredUsers.filter(u => u.id !== userId);
    });
  }
}
```

### Dumb (Presentational) Components

- **Know nothing** about services or state.
- **Receive data** via @Input.
- **Emit events** via @Output.
- **Pure UI** — easy to test and reuse.
- **Use OnPush** change detection.

```typescript
// Dumb component — pure UI, no service dependencies
@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (user of users; track user.id) {
      <div class="user-card">
        <span>{{ user.name }}</span>
        <button (click)="userDeleted.emit(user.id)">Delete</button>
      </div>
    }
  `
})
export class UserListComponent {
  @Input() users: User[] = [];
  @Output() userDeleted = new EventEmitter<number>();
}
```

--

## 16. Best Practices

### Naming Conventions

| Item | Convention | Example |
|------|-----------|---------|
| **Component** | `feature-name.component.ts` | `user-list.component.ts` |
| **Service** | `feature-name.service.ts` | `user.service.ts` |
| **Directive** | `feature-name.directive.ts` | `highlight.directive.ts` |
| **Pipe** | `feature-name.pipe.ts` | `truncate.pipe.ts` |
| **Interface** | `feature-name.model.ts` | `user.model.ts` |
| **Guard** | `feature-name.guard.ts` | `auth.guard.ts` |
| **Selector prefix** | `app-` or custom | `app-user-list` |

### Component Best Practices

1. **One component per file** — never define multiple components in one file.
2. **Keep components small** — if a template exceeds 50 lines, break it into child components.
3. **Use OnPush** for presentational components — significant performance gain.
4. **Prefer composition over inheritance** — use services and content projection.
5. **Use smart/dumb pattern** — separate data fetching from presentation.
6. **Always unsubscribe** — use `takeUntilDestroyed()`, `async` pipe, or manual cleanup.
7. **Use trackBy** with `*ngFor` / `@for` — prevents unnecessary DOM recreation.
8. **Avoid logic in templates** — move complex expressions to component methods or getters.
9. **Use interfaces** for @Input types — provides type safety and documentation.
10. **Prefix selectors** — avoid naming collisions with HTML elements or third-party components.

### Template Best Practices

```typescript
// BAD — complex logic in template
<p>{{ user.firstName + ' ' + user.lastName + ' (' + user.role.toUpperCase() + ')' }}</p>

// GOOD — move to component
<p>{{ displayName }}</p>

get displayName(): string {
  return `${this.user.firstName} ${this.user.lastName} (${this.user.role.toUpperCase()})`;
}

// BAD — function call in template (called on every change detection)
<p>{{ calculateTotal(items) }}</p>

// GOOD — use a getter or computed signal
get total(): number { return this.items.reduce((sum, i) => sum + i.price, 0); }
// Or with signals:
total = computed(() => this.items().reduce((sum, i) => sum + i.price, 0));
```
