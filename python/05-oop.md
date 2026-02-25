# Object-Oriented Programming (OOP) in Python

## Classes and Objects

### Creating a Class
```python
class Dog:
    # Class attribute
    species = "Canis familiaris"
    
    # Constructor
    def __init__(self, name, age):
        # Instance attributes
        self.name = name
        self.age = age
    
    # Instance method
    def bark(self):
        return f"{self.name} says Woof!"
    
    # String representation
    def __str__(self):
        return f"{self.name} is {self.age} years old"

# Creating objects
my_dog = Dog("Buddy", 3)
print(my_dog.name)     # Buddy
print(my_dog.bark())   # Buddy says Woof!
print(my_dog)          # Buddy is 3 years old
```

---

## The __init__ Method

```python
class Person:
    def __init__(self, name, email):
        self.name = name
        self._email = email  # Protected attribute
    
    def get_email(self):
        return self._email

person = Person("John", "john@example.com")
```

---

## Class vs Instance Attributes

```python
class Car:
    # Class attribute - shared by all instances
    wheels = 4
    
    def __init__(self, brand, model):
        # Instance attributes - unique to each instance
        self.brand = brand
        self.model = model

car1 = Car("Toyota", "Camry")
car2 = Car("Honda", "Accord")

print(car1.wheels)  # 4 (inherited from class)
print(car2.wheels)  # 4

Car.wheels = 5  # Changes for all instances
print(car1.wheels)  # 5
print(car2.wheels)  # 5
```

---

## Methods

### Instance Methods
```python
class Rectangle:
    def __init__(self, width, height):
        self.width = width
        self.height = height
    
    def area(self):
        return self.width * self.height
    
    def perimeter(self):
        return 2 * (self.width + self.height)

rect = Rectangle(5, 3)
print(rect.area())      # 15
print(rect.perimeter()) # 16
```

### Class Methods
```python
class Person:
    count = 0
    
    def __init__(self, name):
        self.name = name
        Person.count += 1
    
    @classmethod
    def get_count(cls):
        return f"Total persons: {cls.count}"

p1 = Person("Alice")
p2 = Person("Bob")
print(Person.get_count())  # Total persons: 2
```

### Static Methods
```python
class MathUtils:
    @staticmethod
    def add(a, b):
        return a + b
    
    @staticmethod
    def is_even(n):
        return n % 2 == 0

print(MathUtils.add(5, 3))      # 8
print(MathUtils.is_even(10))    # True
```

---

## Inheritance

### Basic Inheritance
```python
class Animal:
    def __init__(self, name):
        self.name = name
    
    def speak(self):
        return "Some sound"

class Dog(Animal):
    def speak(self):
        return "Woof!"

class Cat(Animal):
    def speak(self):
        return "Meow!"

dog = Dog("Buddy")
print(dog.name)    # Buddy
print(dog.speak()) # Woof!
```

### Multiple Inheritance
```python
class Flyable:
    def fly(self):
        return "Flying"

class Swimmable:
    def swim(self):
        return "Swimming"

class Duck(Animal, Flyable, Swimmable):
    pass

duck = Duck("Donald")
print(duck.speak()) # Some sound
print(duck.fly())   # Flying
print(duck.swim())  # Swimming
```

### Method Resolution Order (MRO)
```python
class Duck(Animal, Flyable, Swimmable):
    pass

print(Duck.__mro__)
# (<class 'Duck'>, <class 'Animal'>, <class 'Flyable'>, 
#  <class 'Swimmable'>, <class 'object'>)
```

### super() Function
```python
class Animal:
    def __init__(self, name, species):
        self.name = name
        self.species = species

class Dog(Animal):
    def __init__(self, name, breed):
        super().__init__(name, "Canine")
        self.breed = breed

dog = Dog("Buddy", "Labrador")
print(dog.name)    # Buddy
print(dog.species) # Canine
print(dog.breed)  # Labrador
```

---

## Encapsulation

### Public, Protected, Private
```python
class BankAccount:
    def __init__(self, balance):
        self.balance = balance      # Public
        self._pin = "1234"          # Protected (convention)
        self.__secret = "hidden"    # Private (name mangling)
    
    def get_secret(self):
        return self.__secret

account = BankAccount(1000)
print(account.balance)    # 1000
print(account._pin)        # 1234 (accessible but discouraged)
print(account.get_secret()) # hidden
# print(account.__secret)  # Error!
```

### Property Decorator
```python
class Temperature:
    def __init__(self, celsius):
        self._celsius = celsius
    
    @property
    def celsius(self):
        return self._celsius
    
    @celsius.setter
    def celsius(self, value):
        if value < -273.15:
            raise ValueError("Too cold!")
        self._celsius = value
    
    @property
    def fahrenheit(self):
        return self._celsius * 9/5 + 32

temp = Temperature(25)
print(temp.celsius)    # 25
print(temp.fahrenheit) # 77.0

temp.celsius = 30     # Uses setter
print(temp.fahrenheit) # 86.0
```

---

## Polymorphism

### Duck Typing
```python
class Cat:
    def speak(self):
        return "Meow"

class Dog:
    def speak(self):
        return "Woof"

def make_speak(animal):
    print(animal.speak())

make_speak(Cat())  # Meow
make_speak(Dog())  # Woof
```

### Operator Overloading
```python
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)
    
    def __str__(self):
        return f"({self.x}, {self.y})"

v1 = Vector(1, 2)
v2 = Vector(3, 4)
v3 = v1 + v2
print(v3)  # (4, 6)
```

---

## Special Methods (Dunder Methods)

```python
class Book:
    def __init__(self, title, author, pages):
        self.title = title
        self.author = author
        self.pages = pages
    
    def __str__(self):
        return f"{self.title} by {self.author}"
    
    def __repr__(self):
        return f"Book('{self.title}', '{self.author}', {self.pages})"
    
    def __len__(self):
        return self.pages
    
    def __eq__(self, other):
        return self.title == other.title
    
    def __lt__(self, other):
        return self.pages < other.pages
```

---

## Abstract Classes

```python
from abc import ABC, abstractmethod

class Shape(ABC):
    @abstractmethod
    def area(self):
        pass
    
    @abstractmethod
    def perimeter(self):
        pass

class Rectangle(Shape):
    def __init__(self, width, height):
        self.width = width
        self.height = height
    
    def area(self):
        return self.width * self.height
    
    def perimeter(self):
        return 2 * (self.width + self.height)

# shape = Shape()  # Error - can't instantiate abstract class
rect = Rectangle(5, 3)
print(rect.area())  # 15
```

---

## Interview Questions & Answers

### Q1: What is the difference between class and instance attributes?
**Answer**: Class attributes are shared by all instances of a class and defined at class level. Instance attributes are unique to each instance and defined in __init__. Class attributes can be accessed via instance or class name.

### Q2: What is __init__ method?
**Answer**: __init__ is a constructor method called when an object is created. It's used to initialize instance attributes. Not a true constructor (that's __new__), but commonly called constructor.

### Q3: What is the difference between method overloading and overriding?
**Answer**: Overriding: Subclass provides different implementation of inherited method. Python doesn't support true overloading (multiple methods same name), but can simulate with default arguments.

### Q4: What is inheritance?
**Answer**: Inheritance allows a class (child/subclass) to inherit attributes and methods from another class (parent/superclass). Promotes code reuse and represents "is-a" relationship.

### Q5: What is multiple inheritance?
**Answer**: A class inherits from multiple parent classes. Python supports it. Use MRO (Method Resolution Order) to determine which parent's method to call. Can lead to complexity.

### Q6: What is polymorphism?
**Answer**: Polymorphism allows objects of different classes to be treated uniformly. In Python, it's achieved through duck typing - if an object has the required method, it can be used regardless of its class.

### Q7: What are access modifiers?
**Answer**: Public: accessible anywhere. Protected (_): accessible in class and subclasses. Private (__): accessible only in class (name mangling). Python uses convention, not enforcement.

### Q8: What is a property decorator?
**Answer**: @property allows defining methods that act like attributes. Provides getter, setter, deleter functionality. Used for encapsulation and computed properties.

### Q9: What is the difference between __str__ and __repr__?
**Answer**: __str__ is for end users, returns readable string. __repr__ is for developers, returns unambiguous representation. __str__ used by print() if defined.

### Q10: What are abstract classes?
**Answer**: Abstract classes define methods that subclasses must implement. Cannot be instantiated directly. Use ABC module and @abstractmethod decorator. Ensures consistency across implementations.

### Q11: What is the difference between class method and static method?
**Answer**: Class method receives class as first argument (cls), can access class attributes. Static method doesn't receive implicit first argument, works like regular function but belongs to class namespace.

### Q12: What is super() function?
**Answer**: super() returns a proxy object to access parent class methods. Used in __init__ to initialize parent class, or to call parent method with modified behavior.
