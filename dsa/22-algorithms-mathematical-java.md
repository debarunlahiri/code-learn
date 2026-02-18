# Algorithms: Mathematical Problems (Easy to Hard)

Goal: Master mathematical algorithms and problem-solving techniques.

---

## 1. Factorial

### What it does
Calculate factorial of a number n (n!).

### Why it matters
- Foundation for permutations and combinations
- Used in probability calculations
- Recursive algorithm practice
- Understanding of large number growth

### Intuition
Factorial is product of all positive integers up to n. Like multiplying all numbers from 1 to n in a chain.

### When to use
- Permutations and combinations
- Probability calculations
- Series expansions
- Mathematical computations

### Time complexity
- Iterative: `O(n)`
- Recursive: `O(n)`
- Space: `O(1)` iterative, `O(n)` recursive

### Edge cases
- 0! = 1
- Negative numbers (undefined)
- Large n (overflow)
- Memory constraints

### Java code
```java
public class Factorial {
    // Iterative approach
    static long factorialIterative(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative number");
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // Recursive approach
    static long factorialRecursive(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative number");
        if (n <= 1) return 1;
        return n * factorialRecursive(n - 1);
    }

    // For large numbers (using BigInteger)
    static java.math.BigInteger factorialBig(int n) {
        java.math.BigInteger result = java.math.BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(java.math.BigInteger.valueOf(i));
        }
        return result;
    }
}
```

---

## 2. Fibonacci Sequence

### What it does
Generate Fibonacci numbers where each number is sum of two preceding ones.

### Why it matters
- Dynamic programming introduction
- Mathematical sequences
- Algorithm optimization examples
- Nature and golden ratio applications

### Intuition
Each number builds on previous two. Like a rabbit population where each pair produces new pairs after maturity.

### When to use
- Dynamic programming practice
- Mathematical sequences
- Algorithm optimization
- Pattern recognition

### Time complexity
- Recursive: `O(2^n)`
- Memoized: `O(n)`
- Iterative: `O(n)`
- Matrix: `O(log n)`

### Edge cases
- n = 0, 1 (base cases)
- Large n (overflow)
- Negative indices (extended Fibonacci)
- Memory limits

### Java code
```java
public class Fibonacci {
    // Recursive (inefficient)
    static int fibRecursive(int n) {
        if (n <= 1) return n;
        return fibRecursive(n - 1) + fibRecursive(n - 2);
    }

    // Memoized (efficient)
    static int fibMemo(int n, int[] memo) {
        if (n <= 1) return n;
        if (memo[n] != 0) return memo[n];
        return memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
    }

    // Iterative (most efficient)
    static int fibIterative(int n) {
        if (n <= 1) return n;
        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            int temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }

    // Matrix exponentiation (O(log n))
    static int fibMatrix(int n) {
        if (n <= 1) return n;
        int[][] F = {{1, 1}, {1, 0}};
        power(F, n - 1);
        return F[0][0];
    }

    static void power(int[][] F, int n) {
        if (n <= 1) return;
        int[][] M = {{1, 1}, {1, 0}};
        power(F, n / 2);
        multiply(F, F);
        if (n % 2 != 0) multiply(F, M);
    }

    static void multiply(int[][] F, int[][] M) {
        int x = F[0][0] * M[0][0] + F[0][1] * M[1][0];
        int y = F[0][0] * M[0][1] + F[0][1] * M[1][1];
        int z = F[1][0] * M[0][0] + F[1][1] * M[1][0];
        int w = F[1][0] * M[0][1] + F[1][1] * M[1][1];
        F[0][0] = x; F[0][1] = y; F[1][0] = z; F[1][1] = w;
    }
}
```

---

## 3. Prime Number Check

### What it does
Determine if a number is prime.

### Why it matters
- Fundamental number theory
- Cryptography applications
- Algorithm optimization
- Mathematical foundations

### Intuition
Prime numbers have no divisors other than 1 and themselves. Check divisibility up to square root.

### When to use
- Number theory problems
- Cryptography
- Factorization
- Mathematical validation

### Time complexity
- Basic: `O(n)`
- Optimized: `O(√n)`
- Sieve: `O(n log log n)`

### Edge cases
- Numbers < 2 (not prime)
- Even numbers > 2 (not prime)
- Large numbers
- Negative numbers

### Java code
```java
public class PrimeCheck {
    // Basic approach
    static boolean isPrimeBasic(int n) {
        if (n <= 1) return false;
        for (int i = 2; i < n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // Optimized approach
    static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    // Miller-Rabin (probabilistic)
    static boolean isPrimeMillerRabin(int n, int k) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0) return false;

        int d = n - 1;
        while (d % 2 == 0) d /= 2;

        for (int i = 0; i < k; i++) {
            if (!millerTest(d, n)) return false;
        }
        return true;
    }

    static boolean millerTest(int d, int n) {
        int a = 2 + (int)(Math.random() % (n - 4));
        int x = power(a, d, n);
        if (x == 1 || x == n - 1) return true;

        while (d != n - 1) {
            x = (x * x) % n;
            d *= 2;
            if (x == 1) return false;
            if (x == n - 1) return true;
        }
        return false;
    }

    static int power(int a, int d, int n) {
        int result = 1;
        a = a % n;
        while (d > 0) {
            if (d % 2 == 1) result = (result * a) % n;
            d /= 2;
            a = (a * a) % n;
        }
        return result;
    }
}
```

---

## 4. Square Root (Newton's Method)

### What it does
Calculate square root of a number using Newton-Raphson method.

### Why it matters
- Numerical methods
- Optimization algorithms
- Mathematical computations
- Convergence understanding

### Intuition
Iteratively improve guess using formula: new_guess = (old_guess + n/old_guess) / 2. Like averaging with the target.

### When to use
- Square root calculations
- Numerical methods
- Optimization problems
- Mathematical computations

### Time complexity
- `O(k)` where k = number of iterations (usually < 20)
- Space: `O(1)`

### Edge cases
- Negative numbers (complex roots)
- Zero (result is 0)
- Very large numbers
- Precision requirements

### Java code
```java
public class SquareRoot {
    static double sqrt(double n) {
        if (n < 0) throw new IllegalArgumentException("Negative number");
        if (n == 0 || n == 1) return n;

        double precision = 0.00001;
        double guess = n / 2;
        double prev = 0;

        while (Math.abs(guess - prev) > precision) {
            prev = guess;
            guess = (guess + n / guess) / 2;
        }
        return guess;
    }

    // Integer square root
    static int integerSqrt(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative number");
        if (n <= 1) return n;

        int left = 1, right = n;
        int result = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (mid <= n / mid) {
                left = mid + 1;
                result = mid;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }
}
```

---

## 5. Pascal's Triangle

### What it does
Generate Pascal's triangle for binomial coefficients.

### Why it matters
- Combinatorics foundation
- Binomial coefficients
- Pattern recognition
- Mathematical relationships

### Intuition
Each number is sum of two numbers above it. Like building combinations pyramid.

### When to use
- Combinatorics problems
- Binomial coefficients
- Pattern generation
- Mathematical visualization

### Time complexity
- Generate n rows: `O(n²)`
- Single element: `O(n)`
- Space: `O(n²)`

### Edge cases
- n = 0 (single 1)
- Large n (memory intensive)
- Integer overflow
- Memory constraints

### Java code
```java
import java.util.*;

public class PascalTriangle {
    // Generate complete triangle
    static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();
        
        for (int row = 0; row < numRows; row++) {
            List<Integer> current = new ArrayList<>();
            for (int col = 0; col <= row; col++) {
                if (col == 0 || col == row) {
                    current.add(1);
                } else {
                    List<Integer> prev = triangle.get(row - 1);
                    current.add(prev.get(col - 1) + prev.get(col));
                }
            }
            triangle.add(current);
        }
        return triangle;
    }

    // Get specific element (nCr)
    static int getElement(int row, int col) {
        if (col < 0 || col > row) return 0;
        return combination(row, col);
    }

    // Calculate nCr
    static int combination(int n, int r) {
        if (r > n - r) r = n - r; // Use smaller r
        long result = 1;
        
        for (int i = 0; i < r; i++) {
            result = result * (n - i) / (i + 1);
        }
        return (int) result;
    }

    // Generate nth row only
    static List<Integer> getRow(int rowIndex) {
        List<Integer> row = new ArrayList<>();
        long value = 1;
        
        for (int i = 0; i <= rowIndex; i++) {
            row.add((int) value);
            value = value * (rowIndex - i) / (i + 1);
        }
        return row;
    }
}
```

---

## 6. Trigonometric Functions

### What it does
Implement basic trigonometric functions using series expansions.

### Why it matters
- Mathematical foundations
- Numerical methods
- Scientific computing
- Algorithm implementation

### Intuition
Use Taylor series expansions to approximate trigonometric functions. Like building complex functions from simple polynomials.

### When to use
- Custom math implementations
- Numerical methods
- Scientific computing
- Educational purposes

### Time complexity
- `O(n)` where n = number of terms
- Space: `O(1)`

### Edge cases
- Large angles (periodicity)
- Precision requirements
- Convergence issues
- Performance concerns

### Java code
```java
public class Trigonometry {
    static final double PI = 3.141592653589793;
    
    // Sine using Taylor series
    static double sin(double x) {
        // Normalize to [-2π, 2π]
        x = x % (2 * PI);
        
        double result = 0;
        double term = x;
        int n = 1;
        
        while (Math.abs(term) > 0.00001) {
            result += term;
            term *= -x * x / ((2 * n) * (2 * n + 1));
            n++;
        }
        return result;
    }

    // Cosine using Taylor series
    static double cos(double x) {
        // Normalize to [-2π, 2π]
        x = x % (2 * PI);
        
        double result = 0;
        double term = 1;
        int n = 0;
        
        while (Math.abs(term) > 0.00001) {
            result += term;
            term *= -x * x / ((2 * n + 1) * (2 * n + 2));
            n++;
        }
        return result;
    }

    // Tangent using sin/cos
    static double tan(double x) {
        double s = sin(x);
        double c = cos(x);
        if (Math.abs(c) < 0.00001) throw new ArithmeticException("Undefined");
        return s / c;
    }

    // Convert degrees to radians
    static double toRadians(double degrees) {
        return degrees * PI / 180;
    }

    // Convert radians to degrees
    static double toDegrees(double radians) {
        return radians * 180 / PI;
    }
}
```

---

## Practice Problems

### Easy
1. **Factorial Calculation** (Basic recursion/iteration)
2. **Fibonacci Numbers** (Multiple approaches)
3. **Prime Number Check** (Optimized)

### Medium
1. **Pascal's Triangle** (Combinatorics)
2. **Square Root Calculation** (Newton's method)
3. **Trigonometric Functions** (Series expansion)

### Hard
1. **Large Factorials** (BigInteger)
2. **Matrix Exponentiation** (Fibonacci optimization)
3. **Miller-Rabin Primality Test** (Probabilistic algorithms)

---

**Remember:** Mathematical algorithms build foundation for advanced problem solving - master these basics!
