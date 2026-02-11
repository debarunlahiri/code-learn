# Angular Data Binding & Forms - Complete Guide (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** All data binding types, template-driven forms, reactive forms, validation, dynamic forms, and best practices.

--

## Table of Contents

1. [What is Data Binding?](#what-is-data-binding)
2. [Interpolation](#interpolation)
3. [Property Binding](#property-binding)
4. [Event Binding](#event-binding)
5. [Two-Way Binding](#two-way-binding)
6. [Attribute, Class & Style Binding](#attribute-class-style-binding)
7. [Template Reference Variables](#template-reference-variables)
8. [Forms Overview](#forms-overview)
9. [Template-Driven Forms](#template-driven-forms)
10. [Reactive Forms](#reactive-forms)
11. [Form Validation](#form-validation)
12. [Custom Validators](#custom-validators)
13. [Cross-Field Validation](#cross-field-validation)
14. [Dynamic Forms (FormArray)](#dynamic-forms-formarray)
15. [Form Best Practices](#form-best-practices)

--

## 1. What is Data Binding?

Data binding is the mechanism that **connects the component class (TypeScript)** with the **template (HTML)**. It keeps data and UI in sync.

### Four Types of Data Binding

| Type | Syntax | Direction | Example |
|------|--------|-----------|---------|
| **Interpolation** | `{{ value }}` | Component → Template | `{{ user.name }}` |
| **Property Binding** | `[property]="value"` | Component → Template | `[src]="imageUrl"` |
| **Event Binding** | `(event)="handler()"` | Template → Component | `(click)="save()"` |
| **Two-Way Binding** | `[(ngModel)]="value"` | Component ↔ Template | `[(ngModel)]="name"` |

### Data Flow Diagram

```
┌──────────────┐                    ┌──────────────┐
│  Component   │  ── {{ value }} ──►│   Template   │
│  (TypeScript)│  ── [prop] ──────►│   (HTML)     │
│              │  ◄── (event) ─────│              │
│              │  ◄─ [(ngModel)] ─►│              │
└──────────────┘                    └──────────────┘
```

--

## 2. Interpolation

Interpolation evaluates an expression inside `{{ }}` and converts the result to a **string** for display.

### Basic Usage

```html
<!-- Variable -->
<h1>{{ title }}</h1>
<p>Welcome, {{ user.name }}!</p>

<!-- Expression -->
<p>Total: {{ price * quantity }}</p>
<p>{{ 1 + 1 }}</p>

<!-- Method call (avoid for performance — runs on every change detection) -->
<p>{{ getFullName() }}</p>

<!-- Ternary -->
<p>{{ isAdmin ? 'Admin Panel' : 'User Dashboard' }}</p>

<!-- String concatenation -->
<p>{{ 'Hello, ' + user.firstName + ' ' + user.lastName }}</p>

<!-- Template literal (backticks don't work in interpolation, use concatenation) -->

<!-- Nullish coalescing (Angular 12+) -->
<p>{{ user.nickname ?? 'No nickname' }}</p>

<!-- Optional chaining -->
<p>{{ user?.address?.city }}</p>
```

### What You CANNOT Do in Interpolation

```html
<!-- NO assignments -->
{{ x = 5 }}              <!-- ERROR -->

<!-- NO new keyword -->
{{ new Date() }}          <!-- ERROR -->

<!-- NO chaining with ; -->
{{ a; b }}                <!-- ERROR -->

<!-- NO increment/decrement -->
{{ count++ }}             <!-- ERROR -->

<!-- NO bitwise operators -->
{{ a | b }}               <!-- Treated as pipe, not bitwise OR -->
```

### Interpolation vs Property Binding

```html
<!-- These are equivalent for string values: -->
<img src="{{ imageUrl }}">           <!-- Interpolation -->
<img [src]="imageUrl">               <!-- Property binding -->

<!-- But for non-string values, MUST use property binding: -->
<button [disabled]="isDisabled">     <!-- boolean — use property binding -->
<div [hidden]="!isVisible">          <!-- boolean — use property binding -->
<input [value]="count">              <!-- number — use property binding -->
```

--

## 3. Property Binding

Property binding sets a **DOM property** (not HTML attribute) to a component expression value.

### Basic Usage

```html
<!-- Element properties -->
<img [src]="user.avatar" [alt]="user.name">
<a [href]="profileUrl">Profile</a>
<input [value]="username" [placeholder]="'Enter name'">

<!-- Boolean properties -->
<button [disabled]="isLoading">Submit</button>
<input [readonly]="!isEditable">
<div [hidden]="!showDetails">Details here</div>

<!-- Component @Input properties -->
<app-user-card [user]="selectedUser" [theme]="'dark'"></app-user-card>

<!-- Directive properties -->
<div [ngClass]="{'active': isActive}">Styled div</div>
<p [ngStyle]="{'color': textColor}">Colored text</p>
```

### Property vs Attribute

```html
<!-- HTML Attribute: Sets initial value (doesn't change after render) -->
<input value="initial">

<!-- DOM Property: Reflects current value (changes dynamically) -->
<input [value]="currentValue">

<!-- Key difference example: -->
<input value="hello">
<!-- After user types "world": -->
<!-- attribute: still "hello" (getAttribute('value') → 'hello') -->
<!-- property: "world" (element.value → 'world') -->
```

### Binding to Custom Properties

```html
<!-- Pass string literal -->
<app-card title="User Profile"></app-card>

<!-- Pass component variable (property binding) -->
<app-card [title]="cardTitle"></app-card>

<!-- Pass expression -->
<app-card [title]="'Profile: ' + user.name"></app-card>
```

--

## 4. Event Binding

Event binding listens for **DOM events** and calls a component method when the event fires.

### Basic Usage

```html
<!-- Click -->
<button (click)="save()">Save</button>
<button (click)="deleteUser(user.id)">Delete</button>

<!-- With $event object -->
<input (input)="onInput($event)">
<button (click)="onClick($event)">Click Me</button>

<!-- Keyboard events -->
<input (keyup)="onKeyUp($event)">
<input (keyup.enter)="submit()">
<input (keydown.escape)="cancel()">
<input (keydown.control.s)="save()">
<input (keyup.shift.tab)="focusPrevious()">

<!-- Mouse events -->
<div (mouseenter)="onHover()" (mouseleave)="onLeave()">Hover me</div>
<div (dblclick)="onDoubleClick()">Double click me</div>

<!-- Form events -->
<form (submit)="onSubmit()">...</form>
<input (focus)="onFocus()" (blur)="onBlur()">
<select (change)="onSelectionChange($event)">...</select>

<!-- Scroll -->
<div (scroll)="onScroll($event)">Scrollable content</div>
```

### $event Object

```typescript
// Input event
onInput(event: Event) {
  const input = event.target as HTMLInputElement;
  console.log('Value:', input.value);
}

// Click event
onClick(event: MouseEvent) {
  console.log('X:', event.clientX, 'Y:', event.clientY);
  console.log('Ctrl held:', event.ctrlKey);
}

// Keyboard event
onKeyUp(event: KeyboardEvent) {
  console.log('Key:', event.key);
  console.log('Code:', event.code);
}
```

### Event Filtering (Key Events)

```html
<!-- Only fires on specific keys -->
<input (keyup.enter)="submit()">
<input (keydown.escape)="cancel()">
<input (keydown.space)="togglePlay()">
<input (keydown.arrowDown)="nextItem()">
<input (keydown.arrowUp)="previousItem()">

<!-- Modifier keys -->
<input (keydown.control.s)="save()">
<input (keydown.alt.enter)="submitAndNew()">
<input (keydown.shift.tab)="focusPrevious()">
```

### Template Statements

```html
<!-- Single statement -->
<button (click)="save()">Save</button>

<!-- Multiple statements (separated by ;) -->
<button (click)="save(); close()">Save & Close</button>

<!-- Inline expression -->
<button (click)="count = count + 1">+1</button>
<button (click)="isVisible = !isVisible">Toggle</button>

<!-- Pass data -->
<button (click)="addToCart(product, quantity)">Add to Cart</button>
```

--

## 5. Two-Way Binding

Two-way binding keeps the component property and the template input **in sync** — changes in either direction are reflected in the other.

### Using ngModel

```typescript
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  imports: [FormsModule],  // Required for ngModel!
  template: `
    <input [(ngModel)]="username" placeholder="Enter name">
    <p>Hello, {{ username }}!</p>
  `
})
export class GreetingComponent {
  username = '';
}
```

### How [(ngModel)] Works Internally

```html
<!-- Two-way binding (banana in a box): -->
<input [(ngModel)]="username">

<!-- Is syntactic sugar for: -->
<input [ngModel]="username" (ngModelChange)="username = $event">

<!-- Which means: -->
<!-- 1. [ngModel]="username" → sets input value from component -->
<!-- 2. (ngModelChange)="username = $event" → updates component from input -->
```

### Custom Two-Way Binding

```typescript
// Child component — follow the naming convention: propertyName + Change
@Component({
  selector: 'app-counter',
  standalone: true,
  template: `
    <button (click)="decrement()">-</button>
    <span>{{ count }}</span>
    <button (click)="increment()">+</button>
  `
})
export class CounterComponent {
  @Input() count = 0;
  @Output() countChange = new EventEmitter<number>();  // Must be: inputName + "Change"

  increment() {
    this.count++;
    this.countChange.emit(this.count);
  }

  decrement() {
    this.count--;
    this.countChange.emit(this.count);
  }
}

// Parent — uses banana-in-a-box syntax
<app-counter [(count)]="totalItems"></app-counter>
<p>Total: {{ totalItems }}</p>
```

--

## 6. Attribute, Class & Style Binding

### Attribute Binding

```html
<!-- Use attr. prefix for HTML attributes (not DOM properties) -->
<td [attr.colspan]="columnSpan">Merged Cell</td>
<div [attr.role]="'button'" [attr.aria-label]="'Close dialog'">×</div>
<table [attr.summary]="tableSummary">...</table>
<svg>
  <circle [attr.cx]="x" [attr.cy]="y" [attr.r]="radius"></circle>
</svg>
```

### Class Binding

```html
<!-- Single class toggle -->
<div [class.active]="isActive">Active when true</div>
<div [class.error-border]="hasError">Red border when error</div>
<li [class.selected]="item === selectedItem">List item</li>

<!-- Multiple classes with ngClass -->
<div [ngClass]="{
  'active': isActive,
  'disabled': isDisabled,
  'highlight': isSelected,
  'admin-badge': user.role === 'admin'
}">
  Multiple conditional classes
</div>

<!-- Array of classes -->
<div [ngClass]="['card', 'shadow', isLarge ? 'card-lg' : 'card-sm']">Card</div>

<!-- String of classes -->
<div [ngClass]="'card shadow rounded'">Card</div>
```

### Style Binding

```html
<!-- Single style -->
<div [style.color]="isError ? 'red' : 'green'">Status</div>
<div [style.font-size.px]="fontSize">Sized text</div>
<div [style.width.%]="progress">Progress bar</div>
<div [style.background-color]="bgColor">Colored div</div>

<!-- Multiple styles with ngStyle -->
<div [ngStyle]="{
  'color': textColor,
  'font-size': fontSize + 'px',
  'font-weight': isBold ? 'bold' : 'normal',
  'background-color': isHighlighted ? 'yellow' : 'transparent'
}">
  Styled text
</div>
```

### Unit Suffixes

```html
<!-- Angular supports unit suffixes for style binding -->
<div [style.width.px]="200">200px wide</div>
<div [style.width.%]="50">50% wide</div>
<div [style.width.em]="10">10em wide</div>
<div [style.height.vh]="100">Full viewport height</div>
<div [style.opacity]="0.5">Semi-transparent</div>
```

--

## 7. Template Reference Variables

Template reference variables (`#variable`) give you a reference to a DOM element, component, or directive in the template.

### Basic Usage

```html
<!-- Reference to DOM element -->
<input #nameInput type="text" placeholder="Enter name">
<button (click)="greet(nameInput.value)">Greet</button>

<!-- Reference to component -->
<app-timer #timer></app-timer>
<button (click)="timer.start()">Start Timer</button>
<button (click)="timer.stop()">Stop Timer</button>

<!-- Reference to directive (ngForm) -->
<form #myForm="ngForm" (ngSubmit)="onSubmit(myForm)">
  <input name="email" ngModel required>
  <button [disabled]="myForm.invalid">Submit</button>
  <p>Form valid: {{ myForm.valid }}</p>
</form>
```

### Scope Rules

```html
<!-- Template variables are scoped to the template they're defined in -->

<!-- This works — same template scope -->
<input #myInput>
<button (click)="log(myInput.value)">Log</button>

<!-- This FAILS — different structural directive scope -->
<div *ngIf="show">
  <input #myInput>
</div>
<button (click)="log(myInput.value)">Log</button>  <!-- ERROR: myInput not accessible -->

<!-- Fix: Move button inside the same scope -->
<div *ngIf="show">
  <input #myInput>
  <button (click)="log(myInput.value)">Log</button>  <!-- Works -->
</div>
```

--

## 8. Forms Overview

Angular provides two approaches to building forms:

| Feature | Template-Driven | Reactive |
|---------|----------------|----------|
| **Module** | `FormsModule` | `ReactiveFormsModule` |
| **Logic location** | Template (HTML) | Component class (TypeScript) |
| **Data model** | Implicit (ngModel) | Explicit (FormGroup, FormControl) |
| **Validation** | HTML attributes + directives | Functions in TypeScript |
| **Testing** | Harder (needs DOM) | Easier (pure TypeScript) |
| **Dynamic forms** | Difficult | Easy (FormArray) |
| **Best for** | Simple forms (login, contact) | Complex forms (registration, multi-step) |
| **Used in enterprise** | Less common | More common |

--

## 9. Template-Driven Forms

Template-driven forms use **directives in the template** to create and manage form controls. Angular creates the form model automatically behind the scenes.

### Setup

```typescript
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  user = { email: '', password: '' };

  onSubmit(form: NgForm) {
    if (form.valid) {
      console.log('Form data:', this.user);
    }
  }
}
```

### Basic Template-Driven Form

```html
<form #loginForm="ngForm" (ngSubmit)="onSubmit(loginForm)">
  <!-- Email -->
  <div>
    <label for="email">Email</label>
    <input
      id="email"
      name="email"
      type="email"
      [(ngModel)]="user.email"
      required
      email
      #emailField="ngModel">

    <div *ngIf="emailField.invalid && emailField.touched">
      <p *ngIf="emailField.errors?.['required']">Email is required.</p>
      <p *ngIf="emailField.errors?.['email']">Invalid email format.</p>
    </div>
  </div>

  <!-- Password -->
  <div>
    <label for="password">Password</label>
    <input
      id="password"
      name="password"
      type="password"
      [(ngModel)]="user.password"
      required
      minlength="6"
      #passwordField="ngModel">

    <div *ngIf="passwordField.invalid && passwordField.touched">
      <p *ngIf="passwordField.errors?.['required']">Password is required.</p>
      <p *ngIf="passwordField.errors?.['minlength']">
        Minimum {{ passwordField.errors?.['minlength'].requiredLength }} characters.
      </p>
    </div>
  </div>

  <!-- Submit -->
  <button type="submit" [disabled]="loginForm.invalid">Login</button>

  <!-- Debug -->
  <pre>Form value: {{ loginForm.value | json }}</pre>
  <pre>Form valid: {{ loginForm.valid }}</pre>
</form>
```

### Form Control States

| State | CSS Class (true) | CSS Class (false) | Meaning |
|-------|-----------------|-------------------|---------|
| **touched** | `ng-touched` | `ng-untouched` | User has focused and left the field |
| **dirty** | `ng-dirty` | `ng-pristine` | User has changed the value |
| **valid** | `ng-valid` | `ng-invalid` | Passes all validation rules |

```css
/* Style invalid fields that have been touched */
input.ng-invalid.ng-touched {
  border: 2px solid red;
}

input.ng-valid.ng-touched {
  border: 2px solid green;
}
```

--

## 10. Reactive Forms

Reactive forms define the form model **in the component class** using `FormGroup`, `FormControl`, and `FormArray`. This gives you full control over form behavior.

### Setup

```typescript
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './registration.component.html'
})
export class RegistrationComponent implements OnInit {
  registrationForm!: FormGroup;

  ngOnInit() {
    this.registrationForm = new FormGroup({
      firstName: new FormControl('', [Validators.required, Validators.minLength(2)]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      confirmPassword: new FormControl('', [Validators.required]),
      age: new FormControl(null, [Validators.required, Validators.min(18), Validators.max(100)]),
      gender: new FormControl('', [Validators.required]),
      agreeTerms: new FormControl(false, [Validators.requiredTrue])
    });
  }

  onSubmit() {
    if (this.registrationForm.valid) {
      console.log('Form data:', this.registrationForm.value);
    } else {
      // Mark all fields as touched to show validation errors
      this.registrationForm.markAllAsTouched();
    }
  }

  // Helper getter for easy template access
  get f() { return this.registrationForm.controls; }
}
```

### Template for Reactive Form

```html
<form [formGroup]="registrationForm" (ngSubmit)="onSubmit()">
  <!-- First Name -->
  <div>
    <label>First Name</label>
    <input formControlName="firstName" placeholder="First name">
    @if (f['firstName'].invalid && f['firstName'].touched) {
      @if (f['firstName'].errors?.['required']) {
        <p class="error">First name is required.</p>
      }
      @if (f['firstName'].errors?.['minlength']) {
        <p class="error">Minimum 2 characters.</p>
      }
    }
  </div>

  <!-- Email -->
  <div>
    <label>Email</label>
    <input formControlName="email" type="email" placeholder="Email">
    @if (f['email'].invalid && f['email'].touched) {
      @if (f['email'].errors?.['required']) {
        <p class="error">Email is required.</p>
      }
      @if (f['email'].errors?.['email']) {
        <p class="error">Invalid email format.</p>
      }
    }
  </div>

  <!-- Gender (Radio) -->
  <div>
    <label>Gender</label>
    <label><input type="radio" formControlName="gender" value="male"> Male</label>
    <label><input type="radio" formControlName="gender" value="female"> Female</label>
    <label><input type="radio" formControlName="gender" value="other"> Other</label>
  </div>

  <!-- Terms -->
  <div>
    <label>
      <input type="checkbox" formControlName="agreeTerms">
      I agree to the terms and conditions
    </label>
  </div>

  <!-- Submit -->
  <button type="submit" [disabled]="registrationForm.invalid">Register</button>

  <!-- Debug -->
  <pre>{{ registrationForm.value | json }}</pre>
  <pre>Valid: {{ registrationForm.valid }}</pre>
</form>
```

### FormBuilder (Shorthand)

```typescript
import { FormBuilder, Validators } from '@angular/forms';

@Component({ ... })
export class RegistrationComponent {
  registrationForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.registrationForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      age: [null, [Validators.required, Validators.min(18)]],
      address: this.fb.group({       // Nested FormGroup
        street: [''],
        city: ['', Validators.required],
        state: ['', Validators.required],
        pincode: ['', [Validators.required, Validators.pattern('^[0-9]{6}$')]]
      }),
      hobbies: this.fb.array([])     // FormArray
    });
  }
}
```

### Nested FormGroup

```html
<!-- Nested group for address -->
<div formGroupName="address">
  <input formControlName="street" placeholder="Street">
  <input formControlName="city" placeholder="City">
  <input formControlName="state" placeholder="State">
  <input formControlName="pincode" placeholder="Pincode">
</div>
```

### FormControl API

```typescript
const nameControl = this.registrationForm.get('firstName');

// Read value
nameControl.value;                    // Current value
this.registrationForm.value;          // Entire form value as object
this.registrationForm.getRawValue();  // Includes disabled controls

// Set value
nameControl.setValue('Rahul');                    // Set single control
this.registrationForm.patchValue({ firstName: 'Rahul' });  // Partial update
this.registrationForm.setValue({ ... });          // Must provide ALL fields

// Reset
nameControl.reset();                  // Reset single control
this.registrationForm.reset();        // Reset entire form

// State checks
nameControl.valid;       // Passes validation
nameControl.invalid;     // Fails validation
nameControl.pristine;    // Not changed by user
nameControl.dirty;       // Changed by user
nameControl.touched;     // Focused and blurred
nameControl.untouched;   // Never focused
nameControl.disabled;    // Disabled
nameControl.enabled;     // Enabled
nameControl.errors;      // Validation errors object

// Enable/Disable
nameControl.disable();
nameControl.enable();

// Listen to value changes
nameControl.valueChanges.subscribe(value => {
  console.log('Name changed to:', value);
});

// Listen to status changes
nameControl.statusChanges.subscribe(status => {
  console.log('Status:', status);  // 'VALID', 'INVALID', 'PENDING', 'DISABLED'
});
```

--

## 11. Form Validation

### Built-in Validators

```typescript
import { Validators } from '@angular/forms';

this.fb.group({
  name: ['', [
    Validators.required,              // Must have value
    Validators.minLength(2),          // Minimum 2 characters
    Validators.maxLength(50),         // Maximum 50 characters
  ]],
  email: ['', [
    Validators.required,
    Validators.email,                 // Valid email format
  ]],
  age: [null, [
    Validators.required,
    Validators.min(18),               // Minimum value 18
    Validators.max(100),              // Maximum value 100
  ]],
  website: ['', [
    Validators.pattern('https?://.+'), // Regex pattern
  ]],
  pincode: ['', [
    Validators.required,
    Validators.pattern('^[0-9]{6}$'), // Exactly 6 digits
  ]],
  terms: [false, Validators.requiredTrue]  // Must be true (checkbox)
});
```

### Template-Driven Validation (HTML Attributes)

```html
<input
  name="email"
  ngModel
  required                    <!-- Validators.required -->
  email                       <!-- Validators.email -->
  minlength="5"              <!-- Validators.minLength(5) -->
  maxlength="50"             <!-- Validators.maxLength(50) -->
  pattern="[a-z]*"           <!-- Validators.pattern -->
>
```

### Displaying Validation Errors

```html
<!-- Reactive form error display -->
<input formControlName="email">

@if (f['email'].errors && f['email'].touched) {
  <div class="error-messages">
    @if (f['email'].errors['required']) {
      <p>Email is required.</p>
    }
    @if (f['email'].errors['email']) {
      <p>Please enter a valid email.</p>
    }
    @if (f['email'].errors['minlength']) {
      <p>Minimum {{ f['email'].errors['minlength'].requiredLength }} characters required.
         You entered {{ f['email'].errors['minlength'].actualLength }}.</p>
    }
  </div>
}
```

### Reusable Error Component

```typescript
@Component({
  selector: 'app-field-error',
  standalone: true,
  template: `
    @if (control?.invalid && control?.touched) {
      @if (control?.errors?.['required']) {
        <p class="error">{{ label }} is required.</p>
      }
      @if (control?.errors?.['email']) {
        <p class="error">Invalid email format.</p>
      }
      @if (control?.errors?.['minlength']) {
        <p class="error">Minimum {{ control.errors['minlength'].requiredLength }} characters.</p>
      }
      @if (control?.errors?.['maxlength']) {
        <p class="error">Maximum {{ control.errors['maxlength'].requiredLength }} characters.</p>
      }
      @if (control?.errors?.['min']) {
        <p class="error">Minimum value is {{ control.errors['min'].min }}.</p>
      }
      @if (control?.errors?.['pattern']) {
        <p class="error">Invalid format.</p>
      }
    }
  `,
  styles: [`.error { color: red; font-size: 12px; margin-top: 4px; }`]
})
export class FieldErrorComponent {
  @Input() control: AbstractControl | null = null;
  @Input() label = 'Field';
}

// Usage
<input formControlName="email">
<app-field-error [control]="f['email']" label="Email"></app-field-error>
```

--

## 12. Custom Validators

### Synchronous Custom Validator

```typescript
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

// Factory function — accepts parameters
export function forbiddenNameValidator(forbiddenName: RegExp): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const forbidden = forbiddenName.test(control.value);
    return forbidden ? { forbiddenName: { value: control.value } } : null;
  };
}

// Simple validator — no parameters
export function noWhitespaceValidator(control: AbstractControl): ValidationErrors | null {
  if (!control.value) return null;
  const hasWhitespace = control.value.trim().length === 0;
  return hasWhitespace ? { whitespace: true } : null;
}

// Strong password validator
export function strongPasswordValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  if (!value) return null;

  const hasUpperCase = /[A-Z]/.test(value);
  const hasLowerCase = /[a-z]/.test(value);
  const hasNumber = /[0-9]/.test(value);
  const hasSpecial = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(value);

  const errors: ValidationErrors = {};
  if (!hasUpperCase) errors['missingUpperCase'] = true;
  if (!hasLowerCase) errors['missingLowerCase'] = true;
  if (!hasNumber) errors['missingNumber'] = true;
  if (!hasSpecial) errors['missingSpecial'] = true;

  return Object.keys(errors).length ? errors : null;
}

// Usage in form
this.fb.group({
  username: ['', [Validators.required, forbiddenNameValidator(/admin/i), noWhitespaceValidator]],
  password: ['', [Validators.required, Validators.minLength(8), strongPasswordValidator]]
});
```

### Async Custom Validator (API Call)

```typescript
import { AsyncValidatorFn } from '@angular/forms';

// Check if email already exists (calls API)
export function emailExistsValidator(userService: UserService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    if (!control.value) return of(null);

    return userService.checkEmailExists(control.value).pipe(
      map(exists => exists ? { emailExists: true } : null),
      catchError(() => of(null))
    );
  };
}

// Usage — async validators go in the THIRD argument
this.fb.group({
  email: ['',
    [Validators.required, Validators.email],           // Sync validators
    [emailExistsValidator(this.userService)]             // Async validators
  ]
});
```

```html
<!-- Show loading while async validator runs -->
<input formControlName="email">
@if (f['email'].pending) {
  <p class="info">Checking email availability...</p>
}
@if (f['email'].errors?.['emailExists']) {
  <p class="error">This email is already registered.</p>
}
```

--

## 13. Cross-Field Validation

Validate multiple fields together (e.g., password confirmation, date range).

### Password Match Validator

```typescript
export function passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
  const password = group.get('password')?.value;
  const confirmPassword = group.get('confirmPassword')?.value;

  if (password !== confirmPassword) {
    return { passwordMismatch: true };
  }
  return null;
}

// Apply to the FormGroup (not individual controls)
this.registrationForm = this.fb.group({
  password: ['', [Validators.required, Validators.minLength(8)]],
  confirmPassword: ['', Validators.required]
}, {
  validators: passwordMatchValidator  // Group-level validator
});
```

```html
<form [formGroup]="registrationForm">
  <input formControlName="password" type="password" placeholder="Password">
  <input formControlName="confirmPassword" type="password" placeholder="Confirm Password">

  @if (registrationForm.errors?.['passwordMismatch'] && f['confirmPassword'].touched) {
    <p class="error">Passwords do not match.</p>
  }
</form>
```

### Date Range Validator

```typescript
export function dateRangeValidator(group: AbstractControl): ValidationErrors | null {
  const startDate = group.get('startDate')?.value;
  const endDate = group.get('endDate')?.value;

  if (startDate && endDate && new Date(startDate) >= new Date(endDate)) {
    return { invalidDateRange: true };
  }
  return null;
}

this.bookingForm = this.fb.group({
  startDate: ['', Validators.required],
  endDate: ['', Validators.required]
}, {
  validators: dateRangeValidator
});
```

--

## 14. Dynamic Forms (FormArray)

FormArray holds a dynamic list of controls. Perfect for forms where users can add/remove fields.

### Basic FormArray

```typescript
@Component({
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  template: `
    <form [formGroup]="profileForm" (ngSubmit)="onSubmit()">
      <input formControlName="name" placeholder="Name">

      <!-- Dynamic skills -->
      <div formArrayName="skills">
        <h3>Skills</h3>
        @for (skill of skills.controls; track $index; let i = $index) {
          <div>
            <input [formControlName]="i" [placeholder]="'Skill ' + (i + 1)">
            <button type="button" (click)="removeSkill(i)">Remove</button>
          </div>
        }
      </div>
      <button type="button" (click)="addSkill()">+ Add Skill</button>

      <button type="submit" [disabled]="profileForm.invalid">Save</button>
    </form>
  `
})
export class ProfileComponent {
  profileForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.profileForm = this.fb.group({
      name: ['', Validators.required],
      skills: this.fb.array([
        this.fb.control('', Validators.required)  // Start with one skill
      ])
    });
  }

  get skills(): FormArray {
    return this.profileForm.get('skills') as FormArray;
  }

  addSkill() {
    this.skills.push(this.fb.control('', Validators.required));
  }

  removeSkill(index: number) {
    this.skills.removeAt(index);
  }

  onSubmit() {
    console.log(this.profileForm.value);
    // { name: 'Rahul', skills: ['Angular', 'TypeScript', 'RxJS'] }
  }
}
```

### FormArray with FormGroups (Complex Items)

```typescript
@Component({
  template: `
    <form [formGroup]="orderForm" (ngSubmit)="onSubmit()">
      <div formArrayName="items">
        @for (item of items.controls; track $index; let i = $index) {
          <div [formGroupName]="i" class="item-row">
            <input formControlName="productName" placeholder="Product">
            <input formControlName="quantity" type="number" placeholder="Qty">
            <input formControlName="price" type="number" placeholder="Price">
            <span>Subtotal: {{ getSubtotal(i) | currency:'INR' }}</span>
            <button type="button" (click)="removeItem(i)">×</button>
          </div>
        }
      </div>
      <button type="button" (click)="addItem()">+ Add Item</button>
      <p><strong>Total: {{ getTotal() | currency:'INR' }}</strong></p>
    </form>
  `
})
export class OrderComponent {
  orderForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.orderForm = this.fb.group({
      items: this.fb.array([this.createItem()])
    });
  }

  get items(): FormArray {
    return this.orderForm.get('items') as FormArray;
  }

  createItem(): FormGroup {
    return this.fb.group({
      productName: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(0)]]
    });
  }

  addItem() {
    this.items.push(this.createItem());
  }

  removeItem(index: number) {
    this.items.removeAt(index);
  }

  getSubtotal(index: number): number {
    const item = this.items.at(index);
    return item.get('quantity')?.value * item.get('price')?.value;
  }

  getTotal(): number {
    return this.items.controls.reduce((total, item) => {
      return total + (item.get('quantity')?.value * item.get('price')?.value);
    }, 0);
  }
}
```

--

## 15. Form Best Practices

### When to Use Which Form Type

| Scenario | Use |
|----------|-----|
| Login form (2-3 fields) | Template-driven |
| Contact form (simple) | Template-driven |
| Registration (many fields, validation) | Reactive |
| Multi-step wizard | Reactive |
| Dynamic fields (add/remove) | Reactive (FormArray) |
| Form with API validation | Reactive (async validators) |
| Form with cross-field validation | Reactive |

### General Best Practices

1. **Use Reactive Forms for enterprise apps** — better testability, more control.
2. **Create reusable error components** — avoid repeating error messages.
3. **Use FormBuilder** — less verbose than `new FormGroup()`.
4. **Use getter shortcuts** — `get f() { return this.form.controls; }`.
5. **Mark all as touched on submit** — show all errors at once.
6. **Disable submit button** when form is invalid.
7. **Use `patchValue`** for partial updates, `setValue` for complete updates.
8. **Unsubscribe from `valueChanges`** — use `takeUntilDestroyed()`.
9. **Separate validation logic** — put custom validators in a separate file.
10. **Use typed forms** (Angular 14+) — `FormGroup<{ name: FormControl<string> }>`.

### Typed Reactive Forms (Angular 14+)

```typescript
interface UserForm {
  name: FormControl<string>;
  email: FormControl<string>;
  age: FormControl<number | null>;
}

// Typed form — TypeScript catches errors at compile time
const form = new FormGroup<UserForm>({
  name: new FormControl('', { nonNullable: true }),
  email: new FormControl('', { nonNullable: true }),
  age: new FormControl<number | null>(null)
});

// form.value is now typed:
// { name: string, email: string, age: number | null }

// With FormBuilder
const form = this.fb.nonNullable.group({
  name: ['', Validators.required],    // FormControl<string> (never null)
  email: ['', Validators.required],
  age: [null as number | null]
});
```

### Multi-Step Form Pattern

```typescript
@Component({
  template: `
    @switch (currentStep) {
      @case (1) {
        <div [formGroup]="personalForm">
          <h2>Step 1: Personal Info</h2>
          <input formControlName="firstName" placeholder="First Name">
          <input formControlName="lastName" placeholder="Last Name">
          <button (click)="nextStep()" [disabled]="personalForm.invalid">Next</button>
        </div>
      }
      @case (2) {
        <div [formGroup]="addressForm">
          <h2>Step 2: Address</h2>
          <input formControlName="city" placeholder="City">
          <input formControlName="pincode" placeholder="Pincode">
          <button (click)="prevStep()">Back</button>
          <button (click)="nextStep()" [disabled]="addressForm.invalid">Next</button>
        </div>
      }
      @case (3) {
        <div>
          <h2>Step 3: Review</h2>
          <pre>{{ getAllData() | json }}</pre>
          <button (click)="prevStep()">Back</button>
          <button (click)="submit()">Submit</button>
        </div>
      }
    }

    <!-- Progress indicator -->
    <div class="steps">
      @for (step of [1,2,3]; track step) {
        <span [class.active]="currentStep === step" [class.completed]="currentStep > step">
          Step {{ step }}
        </span>
      }
    </div>
  `
})
export class MultiStepFormComponent {
  currentStep = 1;

  personalForm = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required]
  });

  addressForm = this.fb.group({
    city: ['', Validators.required],
    pincode: ['', [Validators.required, Validators.pattern('^[0-9]{6}$')]]
  });

  constructor(private fb: FormBuilder) {}

  nextStep() { this.currentStep++; }
  prevStep() { this.currentStep--; }

  getAllData() {
    return { ...this.personalForm.value, ...this.addressForm.value };
  }

  submit() {
    console.log('Final data:', this.getAllData());
  }
}
```
