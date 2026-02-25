# Python Basics - Complete Guide

## What is Python?
Python is a high-level, interpreted, general-purpose programming language. It emphasizes code readability and allows you to express concepts in fewer lines of code.

## Why Python?
- Easy to learn and read
- Versatile (web, data science, AI, automation)
- Large standard library
- Strong community support
- Cross-platform

## Your First Python Program

```python
print("Hello, World!")
```

## Running Python

### Interactive Mode
```bash
python
>>> print("Hello")
```

### Script Mode
```bash
python script.py
```

### Jupyter Notebooks
- Browser-based interactive computing
- Great for data science

## Variables and Data Types

### Variables
```python
name = "John"          # String
age = 25              # Integer
height = 5.9          # Float
is_active = True      # Boolean
```

### Data Types
- **int**: Integer (1, 2, 100)
- **float**: Decimal (1.5, 3.14)
- **str**: String ("hello")
- **bool**: True/False
- **list**: Ordered, mutable
- **tuple**: Ordered, immutable
- **dict**: Key-value pairs
- **set**: Unordered, unique

## Comments

```python
# This is a single line comment

"""
This is a
multi-line string
(also used for documentation)
"""

'''
Another way for
multi-line comments
'''
```

## Input and Output

```python
# Input
name = input("Enter your name: ")

# Output
print("Hello,", name)
print(f"Hello, {name}")  # f-string
```

## Type Conversion

```python
int("10")      # "10" → 10
float("10.5")  # "10.5" → 10.5
str(10)        # 10 → "10"
bool(1)        # 1 → True
```

## Operators

### Arithmetic Operators
```python
+   # Addition
-   # Subtraction
*   # Multiplication
/   # Division
//  # Floor Division
%   # Modulus
**  # Exponent
```

### Comparison Operators
```python
==  # Equal
!=  # Not Equal
>   # Greater Than
<   # Less Than
>=  # Greater or Equal
<=  # Less or Equal
```

### Logical Operators
```python
and  # Logical AND
or   # Logical OR
not  # Logical NOT
```

## PEP 8 - Python Style Guide
- Use 4 spaces for indentation
- Maximum line length: 79 characters
- Two blank lines between top-level definitions
- Variable naming: snake_case (e.g., my_variable)
- Constants: UPPER_CASE

---

## Interview Questions & Answers

### Q1: What is Python and what are its key features?
**Answer**: Python is a high-level, interpreted programming language known for its simplicity and readability. Key features include:
- Easy to learn and write
- Interpreted (no compilation needed)
- Dynamically typed
- Object-oriented
- Large standard library
- Cross-platform
- Strong community support

### Q2: What is the difference between compiled and interpreted languages?
**Answer**: In compiled languages (C, C++), the entire code is converted to machine code before execution, resulting in faster execution. In interpreted languages (Python, JavaScript), the code is executed line by line, making debugging easier but execution slower. Python uses both - it's compiled to bytecode (.pyc files) and then interpreted by the Python Virtual Machine.

### Q3: What is dynamically typed language?
**Answer**: In dynamically typed languages like Python, variable types are determined at runtime. You don't need to declare variable types explicitly. For example: `x = 5` automatically makes x an integer. This provides flexibility but can lead to runtime errors if type mismatches occur.

### Q4: What is the difference between Python 2 and Python 3?
**Answer**: Python 2 is deprecated (2020). Key differences:
- Print: `print "hello"` (Py2) vs `print("hello")` (Py3)
- Division: 5/2 = 2 (Py2) vs 5/2 = 2.5 (Py3)
- Unicode: Separate str/unicode in Py2, all strings are unicode in Py3
- Exception: `except Exception, e:` (Py2) vs `except Exception as e:` (Py3)

### Q5: How do you install Python and packages?
**Answer**: Download from python.org or use Anaconda. For packages, use pip:
```bash
pip install package_name
pip install numpy pandas
pip list  # List installed packages
pip uninstall package_name
```

### Q6: What is PEP 8?
**Answer**: PEP (Python Enhancement Proposal) 8 is the style guide for Python code. It covers naming conventions, code layout, comments, and more. Following PEP 8 ensures consistent and readable Python code across projects.

### Q7: What is the difference between == and is?
**Answer**: `==` checks value equality (content comparison), while `is` checks identity (whether both variables reference the same object in memory). Example:
```python
a = [1, 2, 3]
b = [1, 2, 3]
print(a == b)  # True (same values)
print(a is b)  # False (different objects)
```

### Q8: What are Python keywords?
**Answer**: Reserved words that cannot be used as variable names: `and, as, assert, async, await, break, class, continue, def, del, elif, else, except, False, finally, for, from, global, if, import, in, is, lambda, None, nonlocal, not, or, pass, raise, return, True, try, while, with, yield`

### Q9: How do you check Python version?
**Answer**: 
```bash
python --version
python3 --version
```
Or in code:
```python
import sys
print(sys.version)
```

### Q10: What is the purpose of __name__ == "__main__"?
**Answer**: This checks if the script is being run directly or imported as a module. When run directly, `__name__` is `"__main__"`. When imported, it's the module name. This allows code to run only when the file is executed directly, not when imported.
