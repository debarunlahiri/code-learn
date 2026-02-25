# Data Types and Collections in Python

## Primitive Data Types

### 1. Numbers

#### Integer
```python
# Integers (unlimited precision in Python 3)
age = 25
count = 1000000
negative = -42
binary = 0b1010    # 10
hexadecimal = 0xFF  # 255
octal = 0o77     # 63
```

#### Float
```python
price = 19.99
pi = 3.14159
scientific = 1.5e10  # 15000000000.0
```

#### Complex
```python
complex_num = 3 + 4j
print(complex_num.real)  # 3.0
print(complex_num.imag)  # 4.0
```

### 2. Strings

#### Creating Strings
```python
single = 'Hello'
double = "Hello"
multi = """Multi line
string"""
escape = "Line 1\nLine 2"
raw = r"Raw string\n"
```

#### String Operations
```python
s = "Hello World"

# Indexing
print(s[0])      # H
print(s[-1])     # d

# Slicing
print(s[0:5])    # Hello
print(s[::2])    # HloWrd (every 2nd)
print(s[::-1])   # dlroW olleH (reverse)

# Methods
s.upper()       # HELLO WORLD
s.lower()       # hello world
s.title()       # Hello World
s.strip()       # Remove whitespace
s.split()       # ['Hello', 'World']
s.replace("World", "Python")  # Hello Python
s.find("World") # 6
s.count("l")    # 3
```

#### String Formatting
```python
name = "John"
age = 30

# f-strings (recommended)
print(f"My name is {name}, age is {age}")

# format()
print("My name is {}".format(name))

# % operator
print("My name is %s" % name)

# Template strings
from string import Template
t = Template("My name is $name")
print(t.substitute(name=name))
```

### 3. Booleans

```python
is_active = True
is_deleted = False

# Truthy and Falsy values
bool(1)       # True
bool(0)       # False
bool("")      # False
bool("text")  # True
bool([])      # False
bool([1,2])   # True
bool(None)    # False
```

---

## Collections (Data Structures)

### 1. Lists

#### Creating Lists
```python
empty = []
numbers = [1, 2, 3, 4, 5]
mixed = [1, "hello", 3.14, True]
nested = [[1, 2], [3, 4]]

# List comprehension
squares = [x**2 for x in range(5)]  # [0, 1, 4, 9, 16]
```

#### List Operations
```python
numbers = [1, 2, 3]

# Access
numbers[0]     # 1
numbers[-1]    # 3

# Modify
numbers.append(4)     # [1, 2, 3, 4]
numbers.insert(0, 0) # [0, 1, 2, 3, 4]
numbers.extend([5,6])# [0, 1, 2, 3, 4, 5, 6]
numbers.remove(3)    # [0, 1, 2, 4, 5, 6]
numbers.pop()        # removes last, returns it

# Slicing
numbers[1:4]    # [1, 2, 4]
numbers[::2]    # [0, 2, 4, 6]
```

#### List Methods
```python
numbers = [3, 1, 4, 1, 5, 9]

len(numbers)      # 6
sorted(numbers)   # [1, 1, 3, 4, 5, 9]
numbers.sort()    # modifies in place
numbers.reverse() # [9, 5, 4, 1, 3, 1]
numbers.count(1)  # 2
numbers.index(4)  # 2
```

### 2. Tuples

```python
# Immutable lists
point = (10, 20)
coords = (100, 200, 300)

# Tuple unpacking
x, y = point
a, b, c = coords

# Named tuples
from collections import namedtuple
Point = namedtuple('Point', ['x', 'y'])
p = Point(10, 20)
print(p.x, p.y)
```

### 3. Dictionaries

```python
# Creating
person = {
    "name": "John",
    "age": 30,
    "city": "NYC"
}

# Access
person["name"]       # John
person.get("name")   # John
person.get("country", "USA")  # USA (default)

# Modify
person["age"] = 31
person["country"] = "USA"
del person["city"]
person.pop("age")

# Methods
person.keys()    # dict_keys(['name', 'age', 'country'])
person.values()  # dict_values(['John', 31, 'USA'])
person.items()   # dict_items([...])

# Dictionary comprehension
squares = {x: x**2 for x in range(5)}
```

### 4. Sets

```python
# Unordered, unique elements
fruits = {"apple", "banana", "cherry"}

# Operations
fruits.add("orange")
fruits.remove("banana")
fruits.pop()       # removes random

# Set operations
A = {1, 2, 3}
B = {2, 3, 4}

A | B   # Union: {1, 2, 3, 4}
A & B   # Intersection: {2, 3}
A - B   # Difference: {1}
A ^ B   # Symmetric difference: {1, 4}
```

---

## Type Conversion

```python
# To int
int(5.7)      # 5
int("100")   # 100

# To float
float(5)      # 5.0
float("3.14") # 3.14

# To string
str(123)      # "123"

# To list
list("abc")   # ['a', 'b', 'c']
list({1,2,3}) # [1, 2, 3]

# To tuple
tuple([1,2,3]) # (1, 2, 3)

# To set
set([1,2,2,3]) # {1, 2, 3}
```

---

## Interview Questions & Answers

### Q1: What is the difference between list and tuple?
**Answer**: Lists are mutable (can be modified after creation), tuples are immutable (cannot be changed). Lists use more memory, tuples are faster. Use lists when you need to modify data, tuples for fixed data like coordinates or database records.

### Q2: What is dictionary in Python?
**Answer**: Dictionary is a key-value pair data structure. Keys must be unique and hashable (immutable types). Values can be any type. Dictionaries provide O(1) average time complexity for lookups, insertions, and deletions.

### Q3: What are set and when to use it?
**Answer**: Set is an unordered collection of unique elements. Useful for removing duplicates, membership testing, and mathematical set operations (union, intersection, difference). Not suitable when you need ordered data or duplicates.

### Q4: What is the difference between append() and extend()?
**Answer**: append() adds a single element to the end of a list. extend() adds all elements from another iterable to the end.
```python
list1 = [1, 2]
list1.append([3, 4])   # [1, 2, [3, 4]]
list1 = [1, 2]
list1.extend([3, 4])    # [1, 2, 3, 4]
```

### Q5: How do you remove duplicates from a list?
**Answer**: 
```python
# Method 1: Using set
unique = list(set(original_list))

# Method 2: Preserve order
unique = []
seen = set()
for item in original_list:
    if item not in seen:
        unique.append(item)
        seen.add(item)
```

### Q6: What is list comprehension?
**Answer**: List comprehension provides a concise way to create lists. Syntax: `[expression for item in iterable if condition]`
```python
# Squares of even numbers
squares = [x**2 for x in range(10) if x % 2 == 0]
```

### Q7: What is dictionary comprehension?
**Answer**: Similar to list comprehension but creates dictionaries.
```python
squares = {x: x**2 for x in range(5)}  # {0: 0, 1: 1, 2: 4, 3: 9, 4: 16}
```

### Q8: What is the difference between del, pop, and remove?
**Answer**: 
- `del list[index]`: Delete by index, can delete entire list
- `list.pop(index)`: Remove and return by index (default last)
- `list.remove(value)`: Remove first occurrence by value (raises error if not found)

### Q9: How do you merge two dictionaries?
**Answer**:
```python
# Python 3.9+
merged = dict1 | dict2

# Python 3.5+
merged = {**dict1, **dict2}

# Using update
dict1.update(dict2)
```

### Q10: What are mutable and immutable types?
**Answer**: Mutable types can be changed after creation: list, dict, set. Immutable types cannot be changed: int, float, str, tuple, frozenset. This affects how they're used as dictionary keys or in sets.
