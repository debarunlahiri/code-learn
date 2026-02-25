# File Handling in Python

## Opening and Closing Files

### Basic File Operations
```python
# Open file
file = open("example.txt", "r")

# Read content
content = file.read()
print(content)

# Close file
file.close()
```

### Using with Statement (Recommended)
```python
with open("example.txt", "r") as file:
    content = file.read()
    print(content)
# File automatically closed
```

## File Modes

| Mode | Description |
|------|-------------|
| 'r' | Read (default) |
| 'w' | Write (overwrites) |
| 'a' | Append |
| 'x' | Create new, fail if exists |
| 'rb' | Read binary |
| 'wb' | Write binary |
| 'r+' | Read and write |
| 'w+' | Read and write (overwrites) |
| 'a+' | Read and append |

## Reading Files

### Read Entire File
```python
with open("file.txt", "r") as f:
    content = f.read()
```

### Read Line by Line
```python
with open("file.txt", "r") as f:
    for line in f:
        print(line.strip())
```

### Read as List
```python
with open("file.txt", "r") as f:
    lines = f.readlines()

# Or
lines = list(f)
```

### Read Specific Characters
```python
with open("file.txt", "r") as f:
    first_10 = f.read(10)  # First 10 characters
```

## Writing Files

### Write Text
```python
with open("output.txt", "w") as f:
    f.write("Hello, World!\n")
    f.write("Second line")
```

### Write Multiple Lines
```python
lines = ["Line 1\n", "Line 2\n", "Line 3\n"]
with open("output.txt", "w") as f:
    f.writelines(lines)
```

### Append to File
```python
with open("log.txt", "a") as f:
    f.write("New log entry\n")
```

## Working with CSV

### Reading CSV
```python
import csv

with open("data.csv", "r") as f:
    reader = csv.reader(f)
    for row in reader:
        print(row)

# As dictionary
with open("data.csv", "r") as f:
    reader = csv.DictReader(f)
    for row in reader:
        print(row["name"], row["age"])
```

### Writing CSV
```python
import csv

data = [
    ["Name", "Age", "City"],
    ["John", "30", "NYC"],
    ["Jane", "25", "LA"]
]

with open("output.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerows(data)

# As dictionary
with open("output.csv", "w", newline="") as f:
    fieldnames = ["Name", "Age", "City"]
    writer = csv.DictWriter(f, fieldnames=fieldnames)
    writer.writeheader()
    writer.writerow({"Name": "John", "Age": "30", "City": "NYC"})
```

## Working with JSON

### Reading JSON
```python
import json

with open("data.json", "r") as f:
    data = json.load(f)

# Or from string
json_str = '{"name": "John", "age": 30}'
data = json.loads(json_str)
```

### Writing JSON
```python
import json

data = {
    "name": "John",
    "age": 30,
    "city": "NYC"
}

with open("output.json", "w") as f:
    json.dump(data, f, indent=2)

# Or to string
json_str = json.dumps(data, indent=2)
```

## Working with Binary Files

### Reading Binary
```python
with open("image.png", "rb") as f:
    data = f.read()
```

### Writing Binary
```python
with open("copy.png", "wb") as f:
    f.write(data)
```

### Copying Files
```python
# Binary copy
with open("source.bin", "rb") as src:
    with open("dest.bin", "wb") as dst:
        dst.write(src.read())

# Large files
with open("source.bin", "rb") as src:
    with open("dest.bin", "wb") as dst:
        while chunk := src.read(8192):
            dst.write(chunk)
```

## File Path Operations

```python
import os
from pathlib import Path

# Using pathlib (recommended)
path = Path("folder/file.txt")

path.exists()      # Check if exists
path.is_file()     # Is a file?
path.is_dir()      # Is a directory?
path.name          # filename
path.stem          # filename without ext
path.suffix        # extension
path.parent        # parent directory

# List directory
for item in Path(".").iterdir():
    print(item.name)

# Create directory
Path("new_folder").mkdir()

# File operations
os.path.exists("file.txt")
os.path.getsize("file.txt")
os.path.getmtime("file.txt")  # modification time
os.rename("old.txt", "new.txt")
os.remove("file.txt")
os.rmdir("empty_folder")
```

---

## Interview Questions & Answers

### Q1: What is the difference between read() and readlines()?
**Answer**: read() reads entire file as a single string. readlines() returns a list of lines, each ending with newline character. readlines() is more memory efficient for large files.

### Q2: What does the 'w' mode do?
**Answer**: 'w' mode opens file for writing, overwriting existing content. If file doesn't exist, it creates a new one. Use 'a' to append without overwriting.

### Q3: Why use 'with' statement for file handling?
**Answer**: with statement ensures the file is properly closed even if exceptions occur. It's the recommended way as it handles cleanup automatically and is more Pythonic.

### Q4: How do you handle FileNotFoundError?
**Answer**: Use try-except:
```python
try:
    with open("file.txt", "r") as f:
        content = f.read()
except FileNotFoundError:
    print("File not found")
```

### Q5: How do you read a CSV file?
**Answer**: Use the csv module:
```python
import csv
with open("data.csv", "r") as f:
    reader = csv.reader(f)
    for row in reader:
        print(row)
```

### Q6: How do you write JSON to a file?
**Answer**:
```python
import json
data = {"key": "value"}
with open("data.json", "w") as f:
    json.dump(data, f, indent=2)
```

### Q7: What is the difference between os.path and pathlib?
**Answer**: pathlib is newer, object-oriented (Path objects), more intuitive. os.path is older, string-based. pathlib is recommended for new code.

### Q8: How do you check if a file exists?
```python
import os
os.path.exists("file.txt")

# Or with pathlib
from pathlib import Path
Path("file.txt").exists()
```

### Q9: How do you copy a file?
```python
# Binary copy
with open("source.bin", "rb") as src:
    with open("dest.bin", "wb") as dst:
        dst.write(src.read())

# Or using shutil
import shutil
shutil.copy("source.txt", "dest.txt")
```

### Q10: What is newline="" in CSV writing?
**Answer**: Prevents extra newlines on Windows. Always use newline="" when working with CSV files in Python.

### Q11: How do you read a file line by line efficiently?
```python
with open("file.txt", "r") as f:
    for line in f:  # Iterates without loading entire file
        print(line.strip())
```

### Q12: What is the difference between text and binary mode?
**Answer**: Text mode ('r', 'w') handles encoding (UTF-8 by default) and newline conversion. Binary mode ('rb', 'wb') reads/writes raw bytes without any processing.
