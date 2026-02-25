# React Routing and State Management (Theory)

## 1. Client-side Routing

React apps usually use `react-router-dom` for page navigation without full reload.

### Basic idea

- `BrowserRouter` wraps app
- `Routes` holds route list
- `Route` maps path to component
- `Link` navigates between pages

```jsx
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";

function Home() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}

function App() {
  return (
    <BrowserRouter>
      <nav>
        <Link to="/">Home</Link> | <Link to="/about">About</Link>
      </nav>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
      </Routes>
    </BrowserRouter>
  );
}
```

## 2. Local vs Global State

- Local state: used only inside one component (`useState`)
- Global state: shared across many components

## 3. Context API (built-in global state)

Use when many components need same data (theme, auth, user settings).

```jsx
import { createContext, useContext, useState } from "react";

const ThemeContext = createContext();

function ThemeProvider({ children }) {
  const [theme, setTheme] = useState("light");
  return (
    <ThemeContext.Provider value={{ theme, setTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

function Header() {
  const { theme } = useContext(ThemeContext);
  return <h3>Theme: {theme}</h3>;
}
```

## 4. Redux (external state library)

Redux is useful when app has complex shared state and many updates.

Core terms:
- Store: app state container
- Action: what happened
- Reducer: how state changes
- Dispatch: sends action to reducer

## 5. When to use what

1. Small app: `useState` + props
2. Medium app: Context + custom hooks
3. Large app: Redux Toolkit

## 6. Best practices

- Keep state as small as possible
- Lift state only when needed
- Avoid deeply nested props (use context if needed)
- Use Redux Toolkit instead of raw Redux setup
