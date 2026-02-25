# React Fundamentals (Theory + Easy Examples)

## 1. What is React?

React is a JavaScript library for building user interfaces.
It helps you build UI using reusable components.

## 2. Why React?

- Reusable components
- Fast updates using Virtual DOM
- Easy state-based UI
- Large ecosystem

## 3. Component

A component is a small, reusable UI block.

```jsx
function Welcome() {
  return <h1>Hello React</h1>;
}
```

## 4. JSX

JSX looks like HTML inside JavaScript.
Browser cannot read JSX directly, so tools compile it to JavaScript.

```jsx
const title = <h2>Learning React</h2>;
```

## 5. Props

Props are inputs passed from parent to child component.

```jsx
function UserCard(props) {
  return <p>Name: {props.name}</p>;
}

function App() {
  return <UserCard name="Asha" />;
}
```

## 6. State

State is internal data of a component.
When state changes, UI updates automatically.

```jsx
import { useState } from "react";

function Counter() {
  const [count, setCount] = useState(0);
  return (
    <button onClick={() => setCount(count + 1)}>
      Count: {count}
    </button>
  );
}
```

## 7. Event Handling

React uses camelCase event names.

```jsx
function Clicker() {
  function handleClick() {
    alert("Clicked");
  }
  return <button onClick={handleClick}>Click</button>;
}
```

## 8. Conditional Rendering

Show different UI based on condition.

```jsx
function Status({ isLoggedIn }) {
  return <h3>{isLoggedIn ? "Welcome back" : "Please login"}</h3>;
}
```

## 9. List Rendering

Use `map()` and add unique `key`.

```jsx
function FruitList() {
  const fruits = ["Apple", "Mango", "Banana"];
  return (
    <ul>
      {fruits.map((fruit, index) => (
        <li key={index}>{fruit}</li>
      ))}
    </ul>
  );
}
```

## 10. Quick Summary

- React UI is component-based.
- Props = input from parent.
- State = internal changing data.
- JSX is a JavaScript syntax for UI.
