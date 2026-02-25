# Exception Handling in Python

## What are Exceptions?
Exceptions are events that disrupt normal program flow. They occur when something goes wrong (division by zero, file not found, etc.).

## Basic Try-Except

```python
try:
    result = 10 / 0
except ZeroDivisionError:
    print("Cannot divide by zero!")
```

## Multiple Except Blocks

```python
try:
    num = int(input("Enter a number: "))
    result = 10 / num
except ValueError:
    print("Invalid input - not a number")
except ZeroDivisionError:
    print("Cannot divide by zero")
```

## Catch All Exceptions

```python
try:
    # Risky code
    result = 10 / 0
except Exception as e:
    print(f"Error: {e}")
    print(type(e).__name__)  # ZeroDivisionError
```

## Else Clause

```python
try:
    num = int("10")
except ValueError:
    print("Invalid number")
else:
    print("Conversion successful!")  # Runs only if no exception
```

## Finally Clause

```python
try:
    file = open("data.txt", "r")
    content = file.read()
except FileNotFoundError:
    print("File not found")
finally:
    # Always executes
    if 'file' in locals():
        file.close()
    print("Cleanup complete")
```

## Raising Exceptions

```python
def validate_age(age):
    if age < 0:
        raise ValueError("Age cannot be negative")
    if age > 150:
        raise ValueError("Invalid age")
    return age

try:
    validate_age(-5)
except ValueError as e:
    print(f"Validation error: {e}")
```

## Custom Exceptions

```python
class InvalidAgeError(Exception):
    """Raised when age is invalid"""
    pass

class NegativeAgeError(InvalidAgeError):
    """Raised when age is negative"""
    pass

def set_age(age):
    if age < 0:
        raise NegativeAgeError("Age cannot be negative")
    if age > 150:
        raise InvalidAgeError("Age too high")
    return age
```

## Common Built-in Exceptions

| Exception | Description |
|-----------|-------------|
| ZeroDivisionError | Division by zero |
| ValueError | Invalid value type |
| TypeError | Invalid type operation |
| IndexError | Index out of range |
| KeyError | Dictionary key not found |
| FileNotFoundError | File doesn't exist |
| AttributeError | Attribute not found |
| ImportError | Module import fails |
| NameError | Variable not defined |
| SyntaxError | Invalid Python syntax |
| IndentationError | Indentation issues |
| AssertionError | Assert statement fails |

## Context Managers

### Using 'with' Statement
```python
# File handling - automatically closes
with open("file.txt", "r") as f:
    content = f.read()
# File automatically closed here

# Database connection
with get_connection() as conn:
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM users")
# Connection automatically closed
```

### Custom Context Manager
```python
class Timer:
    def __enter__(self):
        import time
        self.start = time.time()
        return self
    
    def __exit__(self, exc_type, exc_val, exc_tb):
        import time
        self.end = time.time()
        print(f"Took {self.end - self.start:.2f} seconds")
        return False  # Don't suppress exceptions

with Timer():
    sum(range(1000000))
```

### Using contextlib
```python
from contextlib import contextmanager

@contextmanager
def temp_file(filename):
    print(f"Creating {filename}")
    yield filename
    print(f"Deleting {filename}")

with temp_file("temp.txt"):
    print("Using file")
```

---

## Interview Questions & Answers

### Q1: What is the difference between try-except-finally?
**Answer**: try contains code that might raise an exception. except handles the exception if raised. finally always executes regardless of exception - used for cleanup (closing files, connections).

### Q2: What is the difference between except Exception and except Exception as e?
**Answer**: Both catch exceptions. The 'as e' form stores the exception object in variable 'e', allowing you to access the error message, type, and traceback.

### Q3: What happens if an exception is not caught?
**Answer**: The program terminates and displays a traceback showing the exception type, message, and where it occurred. Unhandled exceptions stop execution.

### Q4: Can try-finally work without except?
**Answer**: Yes! finally always executes. You can use try-finally without except, but any exception will still propagate after finally runs.

### Q5: What is the else clause in try-except?
**Answer**: The else block executes only if no exception was raised in the try block. It's for code that should run when the try succeeds.

### Q6: How do you create custom exceptions?
**Answer**: Define a class inheriting from Exception (or subclass):
```python
class MyError(Exception):
    pass

raise MyError("Custom error message")
```

### Q7: What is the purpose of raise?
**Answer**: raise is used to explicitly trigger an exception. Can raise built-in or custom exceptions. Can also re-raise caught exceptions.

### Q8: What is a context manager?
**Answer**: Context managers (with statement) ensure proper resource cleanup. They guarantee that setup and teardown happen, even if exceptions occur. Examples: file handling, database connections.

### Q9: What is the difference between file.closed and with statement?
**Answer**: with statement automatically closes the file when exiting the block. file.closed only checks if file is closed - you must manually call close() otherwise.

### Q10: What does return do in a finally block?
**Answer**: The finally block's return overrides any exception. It's generally bad practice to return from finally as it hides exceptions. If you must return, ensure you understand the implications.

### Q11: How do you handle multiple exceptions?
**Answer**: 
```python
# Multiple except blocks
except (ValueError, TypeError):
    pass

# Or separate blocks for different handling
except ValueError:
    pass
except TypeError:
    pass
```

### Q12: What is exception propagation?
**Answer**: When an exception isn't caught, it propagates up the call stack. Each level can catch or let it continue. Uncaught exceptions reach top level and terminate program.

### Q13: Best practices for exception handling?
**Answer**: 
1. Be specific with exception types
2. Don't suppress exceptions without reason
3. Clean up resources with finally or context managers
4. Log exceptions for debugging
5. Don't use exceptions for flow control
6. Include meaningful error messages
