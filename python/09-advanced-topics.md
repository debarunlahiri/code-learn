# Advanced Python Topics

## 1. Decorators

### Basic Decorator
```python
def my_decorator(func):
    def wrapper():
        print("Before function")
        func()
        print("After function")
    return wrapper

@my_decorator
def say_hello():
    print("Hello!")

say_hello()
```

### Decorator with Arguments
```python
def repeat(times):
    def decorator(func):
        def wrapper(*args, **kwargs):
            for _ in range(times):
                result = func(*args, **kwargs)
            return result
        return wrapper
    return decorator

@repeat(times=3)
def greet(name):
    print(f"Hello, {name}!")

greet("John")
```

### Preserving Function Metadata
```python
import functools

def my_decorator(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        """Wrapper function"""
        return func(*args, **kwargs)
    return wrapper

@my_decorator
def example():
    """Example function"""
    pass

print(example.__name__)  # example (not wrapper)
print(example.__doc__)  # Example function
```

### Built-in Decorators
```python
class MyClass:
    @property
    def value(self):
        return self._value
    
    @classmethod
    def create(cls):
        return cls()
    
    @staticmethod
    def utility():
        return "utility"

# Property, classmethod, staticmethod
```

---

## 2. Generators

### Creating Generators
```python
def count_up_to(n):
    i = 1
    while i <= n:
        yield i
        i += 1

# Using generator
gen = count_up_to(5)
print(next(gen))  # 1
print(next(gen))  # 2

for num in count_up_to(5):
    print(num)
```

### Generator Expression
```python
# Like list comprehension but lazy
squares = (x**2 for x in range(10))
print(next(squares))  # 0

# With sum, max, etc.
total = sum(x**2 for x in range(100))
```

### Generator Methods
```python
def infinite():
    count = 0
    while True:
        value = yield count
        if value is not None:
            count = value
        else:
            count += 1

gen = infinite()
next(gen)   # 0
gen.send(5) # Send value, continues from 5
gen.throw(ValueError("Error"))  # Raise exception in generator
gen.close() # Close generator
```

---

## 3. Context Managers

### with Statement
```python
with open("file.txt", "r") as f:
    content = f.read()
# File automatically closed
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
        print(f"Time: {time.time() - self.start:.2f}s")
        return False

with Timer():
    sum(range(1000000))
```

### Using contextlib
```python
from contextlib import contextmanager

@contextmanager
def temp_folder():
    import tempfile, shutil
    folder = tempfile.mkdtemp()
    try:
        yield folder
    finally:
        shutil.rmtree(folder)

with temp_folder() as f:
    print(f"Using {f}")
```

---

## 4. Closures

### What is a Closure?
```python
def make_multiplier(n):
    def multiplier(x):
        return x * n
    return multiplier

double = make_multiplier(2)
triple = make_multiplier(3)

print(double(5))  # 10
print(triple(5))  # 15
```

### Practical Closure
```python
def make_logger(func):
    def wrapper(*args, **kwargs):
        print(f"Calling {func.__name__}")
        result = func(*args, **kwargs)
        print(f"Finished {func.__name__}")
        return result
    return wrapper
```

---

## 5. Iterators

### Creating Iterator
```python
class Counter:
    def __init__(self, limit):
        self.limit = limit
        self.current = 0
    
    def __iter__(self):
        return self
    
    def __next__(self):
        if self.current >= self.limit:
            raise StopIteration
        self.current += 1
        return self.current

for num in Counter(5):
    print(num)  # 1, 2, 3, 4, 5
```

### Using itertools
```python
import itertools

# Infinite iterator
counter = itertools.count(1)
next(counter)  # 1
next(counter)  # 2

# Cycle
cycler = itertools.cycle([1, 2, 3])

# Chain
combined = itertools.chain([1, 2], [3, 4])

# Groupby
groups = itertools.groupby([1, 2, 2, 3, 3, 3])

# Permutations
perms = itertools.permutations([1, 2, 3])

# Combinations
combs = itertools.combinations([1, 2, 3], 2)
```

---

## 6. Comprehensions

### List Comprehension
```python
# Basic
squares = [x**2 for x in range(10)]

# With condition
evens = [x for x in range(20) if x % 2 == 0]

# Nested
matrix = [[i*j for j in range(3)] for i in range(3)]
```

### Dictionary Comprehension
```python
squares = {x: x**2 for x in range(5)}
word_lengths = {word: len(word) for word in ["hello", "world"]}
```

### Set Comprehension
```python
unique_lengths = {len(word) for word in ["hello", "world", "hi"]}
```

### Generator Expression
```python
# Memory efficient
squares = (x**2 for x in range(1000000))
for s in squares:
    process(s)
```

---

## 7. Collections Module

### namedtuple
```python
from collections import namedtuple

Point = namedtuple('Point', ['x', 'y'])
p = Point(10, 20)
print(p.x, p.y)  # 10 20
```

### Counter
```python
from collections import Counter

text = "hello world"
counts = Counter(text)  # Counter({'l': 3, 'o': 2, ...})
counts.most_common(2)    # Top 2
```

### defaultdict
```python
from collections import defaultdict

d = defaultdict(list)
d["fruits"].append("apple")
d["fruits"].append("banana")
```

### OrderedDict
```python
from collections import OrderedDict

d = OrderedDict()
d["a"] = 1
d["b"] = 2
```

### deque
```python
from collections import deque

d = deque([1, 2, 3])
d.appendleft(0)
d.append(4)
d.popleft()
```

---

## 8. Functional Programming

### map, filter, reduce
```python
from functools import reduce

# Map
numbers = [1, 2, 3, 4]
squared = list(map(lambda x: x**2, numbers))

# Filter
evens = list(filter(lambda x: x % 2 == 0, numbers))

# Reduce
total = reduce(lambda x, y: x + y, numbers)
```

### Lambda Functions
```python
add = lambda x, y: x + y
square = lambda x: x**2
```

---

## Interview Questions & Answers

### Q1: What is a decorator?
**Answer**: A decorator is a function that wraps another function to extend its behavior without modifying it. Uses @syntax. Common uses: logging, timing, authentication, caching.

### Q2: What is the difference between generator and iterator?
**Answer**: Iterators implement __iter__ and __next__. Generators are a simpler way to create iterators using yield keyword. Generators automatically create __iter__ and __next__.

### Q3: What are closures?
**Answer**: A closure is a function that retains access to variables from its enclosing scope even after the outer function has finished executing. Created when a nested function references outer variables.

### Q4: What is the purpose of yield?
**Answer**: yield turns a function into a generator. When called, it returns a generator object without executing the function body. Execution starts when next() is called.

### Q5: What is a context manager?
**Answer**: A context manager defines setup and teardown code that executes before and after a code block. Used with 'with' statement. Ensures resources are properly managed.

### Q6: What are *args and **kwargs?
**Answer**: *args collects extra positional arguments as a tuple. **kwargs collects extra keyword arguments as a dictionary. Allow functions to accept variable arguments.

### Q7: What is the difference between list and generator?
**Answer**: Lists store all elements in memory. Generators yield elements one at a time, using less memory. Generators are lazy - elements are generated on demand.

### Q8: How do decorators work with arguments?
**Answer**: Create a decorator factory that returns a decorator. The factory takes arguments, the decorator takes the function. @decorator(args) becomes decorator_factory(args)(func).

### Q9: What is functools.wraps?
**Answer**: functools.wraps copies the original function's __name__, __doc__, and other attributes to the wrapper function. Preserves function metadata.

### Q10: What is the difference between map and filter?
**Answer**: map transforms each element, returns same number of elements. filter selects elements based on condition, may return fewer elements.

### Q11: What is reduce used for?
**Answer**: reduce applies a function to pairs of elements cumulatively, reducing the sequence to a single value. Example: sum all elements, find maximum.

### Q12: What is the difference between @staticmethod and @classmethod?
**Answer**: @staticmethod doesn't receive any implicit first argument. @classmethod receives the class as first argument (cls). Use classmethod for factory methods or modifying class state.
