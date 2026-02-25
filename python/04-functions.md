# Functions in Python

## Defining Functions

### Basic Function
```python
def greet():
    """This function greets the user."""
    print("Hello!")

greet()
```

### Function with Parameters
```python
def greet(name):
    print(f"Hello, {name}!")

greet("John")  # Hello, John!
```

### Default Parameters
```python
def greet(name="World"):
    print(f"Hello, {name}!")

greet()        # Hello, World!
greet("John")  # Hello, John!
```

### Multiple Parameters
```python
def add(a, b=0, c=0):
    return a + b + c

add(1)         # 1
add(1, 2)      # 3
add(1, 2, 3)   # 6
```

---

## Return Values

### Single Return
```python
def square(x):
    return x ** 2

result = square(5)  # 25
```

### Multiple Returns
```python
def get_stats(numbers):
    return min(numbers), max(numbers), sum(numbers)/len(numbers)

min_val, max_val, avg = get_stats([1, 2, 3, 4, 5])
```

### Early Return
```python
def find_first_even(numbers):
    for n in numbers:
        if n % 2 == 0:
            return n
    return None
```

---

## Argument Types

### Positional Arguments
```python
def func(a, b, c):
    print(a, b, c)

func(1, 2, 3)  # 1 2 3
```

### Keyword Arguments
```python
func(a=1, b=2, c=3)
func(c=3, a=1, b=2)  # Order doesn't matter
```

### *args - Variable Positional
```python
def sum_all(*args):
    total = 0
    for num in args:
        total += num
    return total

sum_all(1, 2, 3)     # 6
sum_all(1, 2, 3, 4)  # 10
```

### **kwargs - Variable Keyword
```python
def print_info(**kwargs):
    for key, value in kwargs.items():
        print(f"{key}: {value}")

print_info(name="John", age=30, city="NYC")
```

### Combined
```python
def func(*args, **kwargs):
    print(f"Positional: {args}")
    print(f"Keyword: {kwargs}")

func(1, 2, name="John", age=30)
```

---

## Lambda Functions

### Basic Lambda
```python
square = lambda x: x ** 2
print(square(5))  # 25
```

### With Multiple Arguments
```python
add = lambda a, b: a + b
print(add(3, 4))  # 7
```

### Common Use Cases
```python
# Sorting
pairs = [(1, "one"), (3, "three"), (2, "two")]
pairs.sort(key=lambda x: x[0])

# Map
numbers = [1, 2, 3, 4]
squared = list(map(lambda x: x**2, numbers))

# Filter
evens = list(filter(lambda x: x % 2 == 0, numbers))
```

---

## Scope and Namespace

### Global vs Local
```python
x = "global"

def func():
    x = "local"  # New local variable
    print(x)      # local

func()
print(x)          # global
```

### Using Global
```python
x = "global"

def func():
    global x
    x = "modified"
    print(x)

func()  # modified
print(x) # modified
```

### Nonlocal
```python
def outer():
    x = "outer"
    def inner():
        nonlocal x
        x = "inner"
    inner()
    print(x)

outer()  # inner
```

---

## Advanced Topics

### Decorators
```python
def my_decorator(func):
    def wrapper():
        print("Before")
        func()
        print("After")
    return wrapper

@my_decorator
def say_hello():
    print("Hello!")

say_hello()
```

### *args and **kwargs in Decorators
```python
def timer(func):
    def wrapper(*args, **kwargs):
        import time
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()
        print(f"Took {end-start:.2f}s")
        return result
    return wrapper
```

### Closures
```python
def make_multiplier(n):
    def multiplier(x):
        return x * n
    return multiplier

double = make_multiplier(2)
print(double(5))  # 10
```

### Generator Functions
```python
def count_up_to(n):
    i = 1
    while i <= n:
        yield i
        i += 1

for num in count_up_to(5):
    print(num)  # 1, 2, 3, 4, 5
```

---

## Function Best Practices

### Type Hints (Python 3.5+)
```python
def greet(name: str) -> str:
    return f"Hello, {name}!"

def process(items: list[int]) -> dict[str, int]:
    return {"count": len(items)}
```

### Docstrings
```python
def calculate_area(radius: float) -> float:
    """
    Calculate the area of a circle.
    
    Args:
        radius: The radius of the circle
        
    Returns:
        The area of the circle
        
    Example:
        >>> calculate_area(5)
        78.53981633974483
    """
    return 3.14159 * radius ** 2
```

---

## Interview Questions & Answers

### Q1: What is the difference between parameters and arguments?
**Answer**: Parameters are variables defined in the function definition. Arguments are the actual values passed to the function when calling it.

### Q2: What are *args and **kwargs?
**Answer**: *args allows passing variable number of positional arguments as a tuple. **kwargs allows passing variable number of keyword arguments as a dictionary.

### Q3: What is a lambda function?
**Answer**: Lambda is an anonymous function defined inline using the lambda keyword. Syntax: `lambda parameters: expression`. Limited to single expression, commonly used with map, filter, sorted.

### Q4: What is the difference between return and print?
**Answer**: Return sends a value back to the caller and exits the function. Print outputs to console but doesn't affect function flow. Functions should return values for reusability.

### Q5: What is a closure?
**Answer**: A closure is a function that remembers variables from its enclosing scope even after the outer function has returned. Created when a nested function references variables from the outer function.

### Q6: What is a decorator?
**Answer**: A decorator is a function that wraps another function to add functionality without modifying its code. Uses @syntax to apply. Common uses: logging, timing, authentication.

### Q7: What are default mutable arguments pitfalls?
**Answer**: Default mutable arguments (like lists) are shared across all calls. Don't use mutable defaults:
```python
def bad_func(a=[]):  # BAD
    a.append(1)
    return a

def good_func(a=None):  # GOOD
    if a is None:
        a = []
    a.append(1)
    return a
```

### Q8: How do you create a function that can accept any number of arguments?
**Answer**: Use *args for positional and **kwargs for keyword arguments:
```python
def flexible(*args, **kwargs):
    print(f"Args: {args}")
    print(f"Kwargs: {kwargs}")
```

### Q9: What is the purpose of the pass statement in functions?
**Answer**: Pass is a placeholder when you need a statement syntactically but don't want to do anything. Used for empty function bodies or as a stub.

### Q10: How do you return multiple values from a function?
**Answer**: Python automatically wraps multiple values in a tuple. You can return and unpack:
```python
def get_stats():
    return min, max, avg

min_val, max_val, avg_val = get_stats()
```

### Q11: What are function annotations/type hints?
**Answer**: Type hints indicate expected types of parameters and return values:
```python
def greet(name: str) -> str:
    return f"Hello, {name}!"
```
They don't enforce types but improve code readability and IDE support.

### Q12: What is recursion? Write a factorial example.
**Answer**: Recursion is when a function calls itself.
```python
def factorial(n):
    if n <= 1:
        return 1
    return n * factorial(n-1)
```
Remember to have a base case to prevent infinite recursion.
