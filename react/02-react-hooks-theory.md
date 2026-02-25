# React Hooks (Theory + Easy Examples)

Hooks let functional components use state and lifecycle features.

## 1. `useState`

Stores local component state.

```jsx
import { useState } from "react";

function NameInput() {
  const [name, setName] = useState("");
  return (
    <input
      value={name}
      onChange={(e) => setName(e.target.value)}
      placeholder="Enter name"
    />
  );
}
```

## 2. `useEffect`

Runs side effects after render (API calls, timers, subscriptions).

```jsx
import { useEffect, useState } from "react";

function Timer() {
  const [seconds, setSeconds] = useState(0);

  useEffect(() => {
    const id = setInterval(() => setSeconds((s) => s + 1), 1000);
    return () => clearInterval(id);
  }, []);

  return <p>{seconds}s</p>;
}
```

## 3. `useRef`

Stores mutable value without re-render, also used for DOM reference.

```jsx
import { useRef } from "react";

function FocusInput() {
  const inputRef = useRef(null);

  return (
    <>
      <input ref={inputRef} />
      <button onClick={() => inputRef.current.focus()}>Focus</button>
    </>
  );
}
```

## 4. `useMemo`

Memoizes expensive calculation.

```jsx
import { useMemo, useState } from "react";

function ExpensiveValue({ number }) {
  const value = useMemo(() => number * 1000, [number]);
  return <p>{value}</p>;
}
```

## 5. `useCallback`

Memoizes function so child components do not re-render unnecessarily.

```jsx
import { useCallback, useState } from "react";

function Parent() {
  const [count, setCount] = useState(0);
  const onClick = useCallback(() => setCount((c) => c + 1), []);
  return <button onClick={onClick}>Count {count}</button>;
}
```

## 6. Custom Hook

Custom hook is a reusable function using built-in hooks.

```jsx
import { useState } from "react";

function useToggle(initial = false) {
  const [value, setValue] = useState(initial);
  const toggle = () => setValue((v) => !v);
  return [value, toggle];
}
```

## 7. Hook Rules

1. Call hooks only at top level.
2. Call hooks only inside React functions.
3. Do not call hooks inside loops/conditions.
