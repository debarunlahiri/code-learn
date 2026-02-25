# Modules and Packages in Python

## What are Modules?
A module is a Python file containing code (functions, classes, variables) that can be imported and used in other Python files.

## Importing Modules

### Import Entire Module
```python
import math
print(math.sqrt(16))  # 4.0
print(math.pi)        # 3.141592653589793
```

### Import Specific Items
```python
from math import sqrt, pi
print(sqrt(16))  # 4.0
print(pi)        # 3.141592653589793
```

### Import with Alias
```python
import numpy as np
import pandas as pd
from datetime import datetime as dt
```

### Import All (Not Recommended)
```python
from math import *  # Imports everything
```

---

## Standard Library Modules

### os - Operating System
```python
import os

os.getcwd()           # Current directory
os.listdir()          # List files
os.mkdir("folder")    # Create directory
os.rmdir("folder")    # Remove directory
os.remove("file.txt") # Delete file
os.rename("old", "new") # Rename
os.path.exists("path") # Check existence
os.path.join("dir", "file") # Path joining
```

### sys - System
```python
import sys

sys.argv          # Command line arguments
sys.exit(0)       # Exit program
sys.path          # Module search paths
sys.version       # Python version
sys.stdout        # Standard output
```

### datetime - Date and Time
```python
from datetime import datetime, date, time

now = datetime.now()
print(now.year, now.month, now.day)

dt = datetime(2024, 1, 15, 10, 30)
print(dt.strftime("%Y-%m-%d %H:%M"))

# Parse string
dt = datetime.strptime("2024-01-15", "%Y-%m-%d")
```

### time
```python
import time

time.sleep(2)     # Sleep for 2 seconds
time.time()        # Current timestamp
```

### random
```python
import random

random.random()          # 0.0 to 1.0
random.randint(1, 10)    # Integer 1 to 10
random.choice([1,2,3])   # Random element
random.shuffle(list)     # Shuffle in place
random.sample(list, 3)   # 3 unique elements
```

### json
```python
import json

data = {"name": "John"}
json.dumps(data)    # Dict to JSON string
json.loads(string) # JSON string to dict
json.dump(data, f) # Write to file
json.load(f)        # Read from file
```

### re - Regular Expressions
```python
import re

pattern = r"\d+"  # Match digits
text = "There are 123 numbers"

re.findall(pattern, text)  # ['123']
re.search(pattern, text)   # Match object
re.sub(pattern, "X", text) # Replace
```

---

## Creating Your Own Modules

### mymodule.py
```python
# mymodule.py
def greet(name):
    return f"Hello, {name}!"

def add(a, b):
    return a + b

class Calculator:
    @staticmethod
    def multiply(a, b):
        return a * b

PI = 3.14159
```

### Using Your Module
```python
import mymodule

print(mymodule.greet("John"))       # Hello, John!
print(mymodule.add(3, 4))           # 7
print(mymodule.Calculator.multiply(3, 4))  # 12

from mymodule import greet, PI
print(greet("Jane"))  # Hello, Jane!
print(PI)             # 3.14159
```

---

## Packages

### Package Structure
```
mypackage/
    __init__.py
    module1.py
    module2.py
    subpackage/
        __init__.py
        module3.py
```

### __init__.py
```python
# __init__.py
from .module1 import func1
from .module2 import func2

__version__ = "1.0.0"
```

### Using Package
```python
from mypackage import func1
from mypackage.subpackage import module3

import mypackage
```

---

## pip - Package Manager

### Installing Packages
```bash
pip install package_name
pip install numpy pandas requests
pip install package==1.0.0  # Specific version
pip install "package>=1.0"  # Minimum version
```

### Managing Requirements
```bash
pip freeze > requirements.txt
pip install -r requirements.txt
```

### Common Packages
```bash
pip install numpy      # Numerical computing
pip install pandas     # Data analysis
pip install requests   # HTTP requests
pip install flask      # Web framework
pip install django     # Full-stack framework
pip install pytest     # Testing
pip install beautifulsoup4  # Web scraping
```

---

## Virtual Environments

### Why Use Virtual Environments?
- Isolated Python environments
- Different package versions for different projects
- Avoid conflicts between projects

### Creating Virtual Environment
```bash
# Using venv
python -m venv myenv

# Activate
source myenv/bin/activate  # Linux/Mac
myenv\Scripts\activate    # Windows

# Install packages
pip install flask

# Deactivate
deactivate
```

### Using virtualenvwrapper
```bash
pip install virtualenvwrapper
mkvirtualenv myenv
workon myenv
deactivate
```

---

## Interview Questions & Answers

### Q1: What is a Python module?
**Answer**: A module is a Python file containing functions, classes, and variables that can be imported and reused in other Python files. It helps organize code and promotes code reuse.

### Q2: What is the difference between import and from...import?
**Answer**: `import module` imports the entire module, access items with `module.item`. `from module import item` imports specific items directly. `from module import *` imports all (not recommended).

### Q3: What is __init__.py?
**Answer**: __init__.py makes a directory a Python package. It can also initialize package-level setup and control what's imported with `from package import *`.

### Q4: What is the Python path?
**Answer**: sys.path is a list of directories where Python looks for modules. Includes current directory, standard library, and installed packages.

### Q5: How do you install a Python package?
**Answer**: Use pip: `pip install package_name`. For specific version: `pip install package==1.0.0`. To save requirements: `pip freeze > requirements.txt`.

### Q6: What is a virtual environment?
**Answer**: An isolated Python environment with its own packages. Allows different versions of packages for different projects without conflicts. Created with `python -m venv env_name`.

### Q7: How do you create a package?
**Answer**: Create a directory with __init__.py file inside. Add Python modules (.py files) to the directory. Import using: `from packagename import module`.

### Q8: What is pip freeze?
**Answer**: pip freeze lists all installed packages and their versions. Commonly used to create requirements.txt: `pip freeze > requirements.txt`.

### Q9: What is the difference between pip and conda?
**Answer**: pip is Python's package manager, works with PyPI. conda is Anaconda's package manager, handles packages and environments, can install non-Python dependencies.

### Q10: How do you handle import errors?
**Answer**: Check if package is installed: `pip list`. Install missing package. Ensure module path is in sys.path. Check for circular imports.

### Q11: What is __all__?
**Answer**: __all__ defines what gets imported with `from module import *`. If not defined, all public names are imported.
```python
__all__ = ['function1', 'class1']  # Only these imported with *
```

### Q12: What are some built-in modules you should know?
**Answer**: 
- os, sys: System operations
- datetime, time: Date/time
- json: JSON handling
- re: Regular expressions
- math, random: Math operations
- collections: Specialized containers
- itertools: Iterator functions
