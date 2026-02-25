# Control Flow in Python

## Conditional Statements

### if-elif-else
```python
age = 18

if age < 13:
    print("Child")
elif age < 20:
    print("Teenager")
else:
    print("Adult")

# Ternary operator
status = "Adult" if age >= 18 else "Minor"
```

### Nested Conditionals
```python
x = 10

if x > 0:
    if x % 2 == 0:
        print("Positive even")
    else:
        print("Positive odd")
else:
    print("Non-positive")
```

### Multiple Conditions
```python
age = 25
income = 50000

# Using and
if age >= 18 and income > 30000:
    print("Eligible")

# Using or
day = "Saturday"
if day == "Saturday" or day == "Sunday":
    print("Weekend")
```

---

## Loops

### for Loop
```python
# Iterate over sequence
fruits = ["apple", "banana", "cherry"]
for fruit in fruits:
    print(fruit)

# Range
for i in range(5):        # 0, 1, 2, 3, 4
for i in range(1, 6):     # 1, 2, 3, 4, 5
for i in range(0, 10, 2): # 0, 2, 4, 6, 8

# Enumerate
fruits = ["apple", "banana"]
for index, fruit in enumerate(fruits):
    print(f"{index}: {fruit}")
```

### while Loop
```python
count = 0
while count < 5:
    print(count)
    count += 1

# While with break
while True:
    user_input = input("Enter 'quit' to exit: ")
    if user_input == "quit":
        break
```

### Loop Control
```python
# break - exit loop
for i in range(10):
    if i == 5:
        break
    print(i)  # 0, 1, 2, 3, 4

# continue - skip iteration
for i in range(5):
    if i == 2:
        continue
    print(i)  # 0, 1, 3, 4 (skips 2)

# pass - do nothing
for i in range(5):
    pass  # Placeholder
```

---

## Advanced Loop Patterns

### for-else
```python
# else executes when loop completes without break
numbers = [1, 2, 3, 4, 5]
target = 7

for n in numbers:
    if n == target:
        print("Found")
        break
else:
    print("Not found")
```

### List Comprehension
```python
# Basic
squares = [x**2 for x in range(5)]

# With condition
evens = [x for x in range(10) if x % 2 == 0]

# Nested
matrix = [[i*j for j in range(3)] for i in range(3)]
# [[0, 0, 0], [0, 1, 2], [0, 2, 4]]
```

### Dictionary Comprehension
```python
squares = {x: x**2 for x in range(5)}
words = ["hello", "world"]
lengths = {word: len(word) for word in words}
```

### Zip - Iterate Multiple Sequences
```python
names = ["Alice", "Bob", "Charlie"]
ages = [25, 30, 35]

for name, age in zip(names, ages):
    print(f"{name}: {age}")

# Unzip
pairs = [("Alice", 25), ("Bob", 30)]
names, ages = zip(*pairs)
```

### Generator Expressions
```python
# Like list comprehension but memory efficient
squares = (x**2 for x in range(1000000))
# Use: next(squares) or for loop
```

---

## Pattern Examples

### FizzBuzz
```python
for i in range(1, 16):
    if i % 15 == 0:
        print("FizzBuzz")
    elif i % 3 == 0:
        print("Fizz")
    elif i % 5 == 0:
        print("Buzz")
    else:
        print(i)
```

### Pyramid Pattern
```python
n = 5
for i in range(1, n + 1):
    print(" " * (n - i) + "*" * (2 * i - 1))
```

### Prime Numbers
```python
def is_prime(n):
    if n < 2:
        return False
    for i in range(2, int(n**0.5) + 1):
        if n % i == 0:
            return False
    return True

primes = [n for n in range(2, 50) if is_prime(n)]
```

### Fibonacci
```python
# Using loop
a, b = 0, 1
for _ in range(10):
    print(a, end=" ")
    a, b = b, a + b

# Using list comprehension
fib = [0, 1]
[fib.append(fib[-1] + fib[-2]) for _ in range(8) if len(fib) < 10]
```

---

## Interview Questions & Answers

### Q1: What is the difference between for and while loops?
**Answer**: For loops iterate over a known sequence (list, range, etc.) and are typically used when the number of iterations is known. While loops continue as long as a condition is true and are used when the number of iterations is unknown. For loops are generally preferred when iterating over collections.

### Q2: What is the purpose of break and continue?
**Answer**: Break immediately exits the loop entirely. Continue skips the rest of the current iteration and moves to the next iteration. Both help control loop flow.

### Q3: What is the difference between range(10) and range(0, 10)?
**Answer**: They are functionally identical. Both generate numbers from 0 to 9. Range can take start, stop, and step parameters: range(start, stop, step).

### Q4: How does for-else work in Python?
**Answer**: The else block executes after the loop completes normally (without break). If break is executed, the else block is skipped. Useful for search operations where you want to handle the "not found" case.

### Q5: What is zip() function and when to use it?
**Answer**: Zip combines multiple iterables element-wise into tuples. Useful for iterating over multiple sequences simultaneously. Returns a lazy iterator in Python 3.

### Q6: How do you create a list of even numbers from 1 to 100?
**Answer**: 
```python
evens = [x for x in range(1, 101) if x % 2 == 0]
# Or: evens = list(range(2, 101, 2))
```

### Q7: What is the difference between list comprehension and generator expression?
**Answer**: List comprehension creates the entire list in memory at once. Generator expression creates a lazy iterator that yields values on demand, saving memory for large datasets.

### Q8: How do you skip the header row when reading a file?
**python
with open('file.csv', 'r') as f:
    next(f)  # Skip header
    for line in f:
        process(line)
```

### Q9: How do you loop through a dictionary?
```python
# Keys
for key in dict:
    print(key)

# Values
for value in dict.values():
    print(value)

# Key-value pairs
for key, value in dict.items():
    print(f"{key}: {value}")
```

### Q10: What is the output of this code?
```python
for i in range(3):
    print(i)
else:
    print("Done")
```
**Answer**: Prints 0, 1, 2, then "Done". The else runs because the loop completed without break.
