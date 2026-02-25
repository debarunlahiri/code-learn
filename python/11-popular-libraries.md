# Popular Python Libraries & Frameworks

## 1. NumPy

### What is NumPy?
Numerical Python - fundamental package for numerical computing.

### Installation
```bash
pip install numpy
```

### Basics
```python
import numpy as np

# Array creation
arr = np.array([1, 2, 3, 4, 5])
zeros = np.zeros(5)
ones = np.ones((3, 3))
range_arr = np.arange(0, 10, 2)
linspace = np.linspace(0, 1, 5)

# Array operations
arr + 5          # Add scalar
          # Multiply
arr * 2arr.sum()        # Sum
arr.mean()       # Mean
arr.max()        # Max
arr.reshape(2, 3) # Reshape
```

### Array Indexing
```python
arr = np.array([[1, 2, 3], [4, 5, 6]])
arr[0, 0]        # First element
arr[1, :]        # Second row
arr[:, 2]        # Third column
arr[0:2, 1:3]    # Slicing
```

---

## 2. Pandas

### What is Pandas?
Data analysis### Installation
```bash
pip install and manipulation library.

 pandas
```

### DataFrames
```python
import pandas as pd

# Create DataFrame
data = {"name": ["John", "Jane", "Bob"],
        "age": [25, 30, 35],
        "city": ["NYC", "LA", "Chicago"]}
df = pd.DataFrame(data)

# Read CSV
df = pd.read_csv("file.csv")

# Operations
df.head()           # First 5 rows
df.tail()           # Last 5 rows
df.shape           , columns
df # Rows.columns          # Column names
df.dtypes           # Data types
df.describe()       # Statistics

# Selection
df["name"]          # Single column
df[["name", "age"]] # Multiple columns
df.iloc[0]          # By index
df.loc[0]           # By label

# Filtering
df[df["age"] > 25]
df.query("age > 25")

# Grouping
df.groupby("city").mean()

# Missing values
df.isnull()
df.dropna()
df.fillna(0)
```

---

## 3. Flask

### What is Flask?
Lightweight web framework for Python.

### Installation
```bash
pip install flask
```

### Basic App
```python
from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route("/")
def home():
    return "Hello, World!"

@app.route("/api/data")
def get_data():
    return jsonify({"message": "Data"})

@app.route("/api/users", methods=["POST"])
def create_user():
    data = request.get_json()
    return jsonify({"user": data}), 201

if __name__ == "__main__":
    app.run(debug=True)
```

### Routes with Parameters
```python
@app.route("/user/<int:user_id>")
def get_user(user_id):
    return f"User {user_id}"
```

---

## 4. Django

### What is Django?
Full-stack web framework.

### Installation
```bash
pip install django
django-admin startproject myproject
cd myproject
python manage.py startapp myapp
```

### Models
```python
# models.py
from django.db import models

class Post(models.Model):
    title = models.CharField(max_length=200)
    content = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    author = models.ForeignKey(User, on_delete=models.CASCADE)
    
    def __str__(self):
        return self.title
```

### Views
```python
# views.py
from django.shortcuts import render
from django.http import JsonResponse

def home(request):
    return render(request, "home.html")

def api_data(request):
    return JsonResponse({"data": "value"})
```

### URLs
```python
# urls.py
from django.urls import path
from . import views

urlpatterns = [
    path("", views.home, name="home"),
    path("api/", views.api_data),
]
```

---

## 5. Requests

### What is Requests?
HTTP library for making web requests.

### Installation
```bash
pip install requests
```

### Examples
```python
import requests

# GET request
response = requests.get("https://api.example.com/data")
print(response.status_code)
print(response.json())

# POST request
data = {"name": "John", "age": 30}
response = requests.post("https://api.example.com/users", json=data)

# Headers
headers = {"Authorization": "Bearer token"}
response = requests.get(url, headers=headers)

# Error handling
if response.status_code == 200:
    data = response.json()
elif response.status_code == 404:
    print("Not found")
```

---

## 6. BeautifulSoup

### What is BeautifulSoup?
Web scraping library.

### Installation
```bash
pip install beautifulsoup4 requests
```

### Examples
```python
import requests
from bs4 import BeautifulSoup

# Fetch page
response = requests.get("https://example.com")
html = response.text

# Parse
soup = BeautifulSoup(html, "html.parser")

# Find elements
soup.find("h1")              # First h1
soup.find_all("a")           # All links
soup.find(class_="title")    # By class

# Extract text
soup.get_text()

# Extract attributes
link = soup.find("a")
href = link.get("href")
```

---

## 7. SQLAlchemy

### What is SQLAlchemy?
SQL toolkit and ORM.

### Installation
```bash
pip install sqlalchemy
```

### Basic Usage
```python
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

engine = create_engine("sqlite:///mydb.sqlite")
Base = declarative_base()

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True)
    name = Column(String)
    email = Column(String)

Base.metadata.create_all(engine)

Session = sessionmaker(bind=engine)
session = Session()

# Create
user = User(name="John", email="john@example.com")
session.add(user)
session.commit()

# Read
users = session.query(User).all()
john = session.query(User).filter_by(name="John").first()

# Update
john.email = "newemail@example.com"
session.commit()

# Delete
session.delete(john)
session.commit()
```

---

## 8. PyTest

### What is PyTest?
Testing framework.

### Installation
```bash
pip install pytest
```

### Writing Tests
```python
# test_math.py
import pytest

def test_add():
    assert 1 + 1 == 2

def test_divide():
    assert 10 / 2 == 5

def test_raises():
    with pytest.raises(ZeroDivisionError):
        1 / 0

# Fixtures
@pytest.fixture
def sample_data():
    return [1, 2, 3]

def test_sum(sample_data):
    assert sum(sample_data) == 6
```

### Running Tests
```bash
pytest
pytest -v           # Verbose
pytest test_file.py # Specific file
pytest -k "test_"   # Pattern match
```

---

## 9. Matplotlib

### What is Matplotlib?
Plotting library.

### Installation
```bash
pip install matplotlib
```

### Basic Plot
```python
import matplotlib.pyplot as plt
import numpy as np

x = np.linspace(0, 10, 100)
y = np.sin(x)

plt.plot(x, y)
plt.xlabel("X")
plt.ylabel("Y")
plt.title("Sine Wave")
plt.show()
```

### Other Plots
```python
# Bar chart
plt.bar(["A", "B", "C"], [10, 20, 30])

# Histogram
plt.hist(data, bins=20)

# Scatter
plt.scatter(x, y)

# Pie chart
plt.pie([30, 40, 30], labels=["A", "B", "C"])
```

---

## 10. Scikit-learn

### What is Scikit-learn?
Machine learning library.

### Installation
```bash
pip install scikit-learn
```

### Basic Example
```python
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error

# Data
X, y = [[1], [2], [3], [4], [5]], [2, 4, 6, 8, 10]

# Split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)

# Train
model = LinearRegression()
model.fit(X_train, y_train)

# Predict
predictions = model.predict(X_test)

# Evaluate
error = mean_squared_error(y_test, predictions)
```

---

## Interview Questions

### Q1: What is NumPy?
**Answer**: NumPy (Numerical Python) is the fundamental package for numerical computing in Python. Provides support for arrays, matrices, and mathematical functions. Much faster than Python lists due to contiguous memory allocation.

### Q2: What is Pandas?
**Answer**: Pandas is a data analysis library providing DataFrame (2D) and Series (1D) data structures. Offers data manipulation, cleaning, analysis tools. Great for handling structured data.

### Q3: What is Flask vs Django?
**Answer**: Flask is lightweight, flexible, great for small apps and APIs. Django is full-stack, batteries-included, great for large applications with built-in admin, auth, ORM.

### Q4: What is an ORM?
**Answer**: Object-Relational Mapping. Converts Python objects to database tables and vice versa. SQLAlchemy is Python's ORM. Allows using SQL through Python code.

### Q5: How do you handle missing data in Pandas?
**Answer**: 
- df.isnull() - Find missing values
- df.dropna() - Remove rows/columns with missing
- df.fillna(value) - Fill missing values
- df.interpolate() - Interpolate values

### Q6: What is Scikit-learn?
**Answer**: Machine learning library. Provides simple and efficient tools for data analysis and modeling. Includes classification, regression, clustering, dimensionality reduction algorithms.

### Q7: How do you make HTTP requests in Python?
**Answer**: Use requests library:
```python
response = requests.get(url)
response = requests.post(url, json=data)
response.json()  # Parse response
```

### Q8: What is BeautifulSoup used for?
**Answer**: Web scraping. Parses HTML/XML documents. Extracts data from web pages. Works with requests library.
