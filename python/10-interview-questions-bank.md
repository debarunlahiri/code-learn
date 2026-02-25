# Python Interview Questions Bank

## Section 1: Basics

### Q1: What is Python? What are its key features?
**Answer**: Python is a high-level, interpreted, general-purpose programming language. Key features:
- Easy to learn and read
- Dynamically typed
- Interpreted (no compilation)
- Object-oriented
- Large standard library
- Cross-platform
- Strong community
- Versatile (web, data science, AI, automation)

### Q2: What is the difference between Python 2 and Python 3?
**Answer**: 
- Print: `print "hello"` (Py2) vs `print("hello")` (Py3)
- Division: 5/2 = 2 (Py2) vs 5/2 = 2.5 (Py3)
- Unicode: Separate str/unicode in Py2, all strings unicode in Py3
- Exception syntax: `except Exception, e:` (Py2) vs `except Exception as e:` (Py3)
- Python 2 is deprecated since 2020

### Q3: What are Python keywords?
**Answer**: Reserved words that cannot be used as variable/function names: and, as, assert, async, await, break, class, continue, def, del, elif, else, except, False, finally, for, from, global, if, import, in, is, lambda, None, nonlocal, not, or, pass, raise, return, True, try, while, with, yield

### Q4: What is PEP 8?
**Answer**: PEP (Python Enhancement Proposal) 8 is the style guide for Python code. Covers: 4-space indentation, 79-char max line, snake_case naming, two blank lines between top-level definitions.

### Q5: What is the difference between == and is?
**Answer**: 
- `==` checks value equality (content)
- `is` checks identity (same object in memory)
```python
a = [1,2,3]
b = [1,2,3]
a == b  # True
a is b  # False
```

---

## Section 2: Data Types

### Q6: What are the basic data types in Python?
**Answer**: 
- int: Integer
- float: Decimal
- str: String
- bool: True/False
- list: Ordered, mutable
- tuple: Ordered, immutable
- dict: Key-value pairs
- set: Unordered, unique

### Q7: What is the difference between list and tuple?
**Answer**: Lists are mutable (can modify), tuples are immutable (cannot modify). Lists use more memory, tuples are faster. Use lists for collections that change, tuples for fixed data like coordinates.

### Q8: What is a dictionary?
**Answer**: A dictionary is a key-value pair data structure. Keys must be unique and hashable (immutable). Provides O(1) average lookup time. Created with {} or dict().

### Q9: What are set and when to use it?
**Answer**: Set is an unordered collection of unique elements. Used for: removing duplicates, membership testing (in), mathematical operations (union, intersection). Not for ordered data.

### Q10: What is the difference between append() and extend()?
**Answer**: 
```python
list1 = [1,2]
list1.append([3,4])   # [1, 2, [3, 4]]
list1 = [1,2]
list1.extend([3,4])   # [1, 2, 3, 4]
```

---

## Section 3: Control Flow

### Q11: What is the difference between for and while loops?
**Answer**: For loops iterate over sequences (known iterations). While loops continue while condition is true (unknown iterations). For is preferred for collections.

### Q12: What is the purpose of break and continue?
**Answer**: Break exits the loop entirely. Continue skips the current iteration and moves to the next one.

### Q13: How does for-else work?
**Answer**: Else block executes after loop completes normally (without break). Useful for search operations.

### Q14: What are list comprehensions?
**Answer**: Concise way to create lists: `[expression for item in iterable if condition]`
```python
squares = [x**2 for x in range(5)]  # [0,1,4,9,16]
```

---

## Section 4: Functions

### Q15: What is the difference between parameters and arguments?
**Answer**: Parameters are variables in function definition. Arguments are values passed when calling function.

### Q16: What are *args and **kwargs?
**Answer**: *args collects variable positional arguments as tuple. **kwargs collects variable keyword arguments as dictionary.

### Q17: What is a lambda function?
**Answer**: Anonymous inline function: `lambda parameters: expression`. Used with map, filter, sorted. Limited to single expression.

### Q18: What is the difference between return and print?
**Answer**: Return sends value back to caller and exits function. Print outputs to console. Functions should return for reusability.

### Q19: What is a closure?
**Answer**: A function that remembers variables from its enclosing scope even after outer function returns. Created when nested function references outer variables.

### Q20: What is a decorator?
**Answer**: A function that wraps another to add functionality. Uses @syntax. Common uses: logging, timing, authentication.

### Q21: What is recursion?
**Answer**: A function calling itself. Must have base case to prevent infinite recursion.
```python
def factorial(n):
    if n <= 1: return 1
    return n * factorial(n-1)
```

---

## Section 5: OOP

### Q22: What is a class?
**Answer**: A blueprint for creating objects. Defines attributes (data) and methods (functions).

### Q23: What is __init__ method?
**Answer**: Constructor method called when object is created. Used to initialize instance attributes.

### Q24: What is inheritance?
**Answer**: Mechanism where child class inherits attributes/methods from parent class. Promotes code reuse. Represents "is-a" relationship.

### Q25: What is polymorphism?
**Answer**: Objects of different classes can be treated uniformly. In Python, achieved through duck typing - if object has required method, it works.

### Q26: What are access modifiers?
**Answer**: Public: accessible anywhere. Protected (_): convention for subclasses. Private (__): name mangling, class-only access.

### Q27: What is the difference between class method and static method?
**Answer**: Class method receives class (cls) as first argument. Static method doesn't receive implicit argument. Class methods can modify class state.

### Q28: What is super()?
**Answer**: Returns proxy to access parent class methods. Used in __init__ to initialize parent, or to override parent methods.

### Q29: What is the difference between __str__ and __repr__?
**Answer**: __str__ is for users (readable). __repr__ is for developers (unambiguous). __str__ used by print().

---

## Section 6: Exception Handling

### Q30: What is exception handling?
**Answer**: Mechanism to handle runtime errors gracefully. Uses try-except-finally blocks to catch and handle exceptions.

### Q31: What is the difference between try-except-finally?
**Answer**: Try contains risky code. Except handles exceptions. Finally always executes for cleanup.

### Q32: How do you create custom exceptions?
**Answer**: Define class inheriting from Exception:
```python
class MyError(Exception):
    pass
```

### Q33: What is a context manager?
**Answer**: Defines setup/teardown code with 'with' statement. Ensures resources are properly managed (files, connections).

---

## Section 7: File Handling

### Q34: How do you read a file in Python?
```python
with open("file.txt", "r") as f:
    content = f.read()
```

### Q35: What is the difference between read(), readline(), readlines()?
**Answer**: read() returns entire file. readline() returns one line. readlines() returns list of all lines.

### Q36: What does 'with' statement do?
**Answer**: Automatically closes file when block exits, even if exceptions occur. Preferred way to handle files.

---

## Section 8: Modules & Packages

### Q37: What is a module?
**Answer**: A Python file containing code that can be imported. Helps organize code and promote reuse.

### Q38: What is __init__.py?
**Answer**: Makes a directory a Python package. Can initialize package setup.

### Q39: What is pip?
**Answer**: Python's package manager. Install packages: `pip install package_name`. Freeze: `pip freeze > requirements.txt`

### Q40: What is a virtual environment?
**Answer**: Isolated Python environment with its own packages. Prevents conflicts between projects.

---

## Section 9: Advanced

### Q41: What is a generator?
**Answer**: Function using yield. Returns generator object that yields values lazily (one at a time). Memory efficient.

### Q42: What is the difference between list and generator?
**Answer**: Lists store everything in memory. Generators yield on demand, using less memory.

### Q43: What is duck typing?
**Answer**: "If it walks like a duck and quacks like a duck, it's a duck." In Python, if object has required method, use it regardless of type.

### Q44: What are decorators?
**Answer**: Functions that modify other functions. Add functionality without changing original function code.

### Q45: What is GIL (Global Interpreter Lock)?
**Answer**: A mutex that protects access to Python objects, preventing multiple threads from executing Python bytecode simultaneously. Makes CPU-bound threads not truly parallel.

### Q46: What is the difference between deep copy and shallow copy?
**Answer**: Shallow copy creates new object but references nested objects. Deep copy creates completely independent copy including nested objects.

### Q47: What are metaclasses?
**Answer**: Classes whose instances are classes. Control class creation. Used for frameworks.

---

## Section 10: Practical Coding

### Q48: How do you reverse a string?
```python
s = "hello"
s[::-1]  # "olleh"
```

### Q49: How do you remove duplicates from a list?
```python
# Method 1: Using set
unique = list(set(original))

# Method 2: Preserve order
unique = []
seen = set()
for item in original:
    if item not in seen:
        unique.append(item)
        seen.add(item)
```

### Q50: How do you find prime numbers?
```python
def is_prime(n):
    if n < 2: return False
    for i in range(2, int(n**0.5) + 1):
        if n % i == 0: return False
    return True
```

### Q51: How do you merge two dictionaries?
```python
# Python 3.9+
merged = d1 | d2

# Python 3.5+
merged = {**d1, **d2}
```

### Q52: What is the output?
```python
def func(lst=[]):
    lst.append(1)
    return lst

print(func())
print(func())
```
**Answer**: Both print [1, 1]. Default mutable arguments are shared across calls!

### Q53: What is the output?
```python
x = [1, 2, 3]
y = x
y.append(4)
print(x)
```
**Answer**: [1, 2, 3, 4]. Lists are mutable and assigned by reference.

### Q54: How do you swap two variables?
```python
a, b = 1, 2
a, b = b, a
```

### Q55: How do you flatten a nested list?
```python
nested = [[1, 2], [3, 4], [5]]
flat = [item for sublist in nested for item in sublist]
```

---

## Section 11: Best Practices

### Q56: What are Python best practices?
**Answer**: 
- Follow PEP 8
- Use meaningful variable names
- Use virtual environments
- Write docstrings
- Handle exceptions properly
- Use list comprehensions
- Avoid global variables
- Write tests

### Q57: What is Pythonic code?
**Answer**: Code that follows Python idioms. Uses Python features effectively. Example: `for i, v in enumerate(lst)` instead of manual counter.

### Q58: How do you optimize Python code?
**Answer**: Use list comprehensions, avoid global variables, use built-ins, caching, proper data structures, pypy for performance, profiling with cProfile.

### Q59: What is Python used for?
**Answer**: Web development, data science, machine learning, AI, automation, scripting, testing, DevOps, game development.

### Q60: What is Django vs Flask?
**Answer**: Django: Full-stack, batteries-included, for large apps. Flask: Microframework, lightweight, for small apps and APIs.
