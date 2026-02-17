# More Algorithms in Java (Easy)

Goal: Learn additional core algorithms with simple code and clear explanations.

---

## 1. Insertion Sort

### What it does
Build sorted part one element at a time by inserting each element into its correct position.

### Why it matters
- Efficient for small arrays or nearly sorted data
- Stable sort (maintains relative order of equal elements)
- In-place sorting (no extra space needed)
- Best case O(n) when array is already sorted

### Intuition
Like sorting playing cards. Pick up one card at a time and insert it into the correct position in your already-sorted hand.

### When to use
- Small arrays (n < 50)
- Nearly sorted data
- When stability matters
- Online algorithms (data arrives continuously)

### Time complexity
- Best: `O(n)` (already sorted)
- Average: `O(n²)`
- Worst: `O(n²)` (reverse sorted)
- Space: `O(1)`

### Edge cases
- Empty array or single element
- All elements equal
- Already sorted (best case)

### Java code
```java
import java.util.Arrays;

public class InsertionSort {
    static void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            // Shift larger elements to right
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static void main(String[] args) {
        int[] arr = {5, 2, 4, 6, 1, 3};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
```

---

## 2. Selection Sort

### What it does
Select smallest element and place it at correct position, repeat for remaining elements.

### Why it matters
- Simple to understand and implement
- Minimizes swaps (at most n swaps)
- Performance independent of data order
- Good when memory writes are expensive

### Intuition
Like finding the shortest person in a line and moving them to the front, then finding the next shortest and moving them to second position, and so on.

### When to use
- Educational purposes
- Small arrays where swap cost matters
- When you need minimum number of swaps

### Time complexity
- Best: `O(n²)`
- Average: `O(n²)`
- Worst: `O(n²)`
- Space: `O(1)`

### Edge cases
- Empty array or single element
- All elements equal
- Already sorted (still O(n²))

### Java code
```java
import java.util.Arrays;

public class SelectionSort {
    static void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            // Find minimum in unsorted part
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            // Swap with current position
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    public static void main(String[] args) {
        int[] arr = {29, 10, 14, 37, 13};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
```

---

## 3. Recursion: Factorial

### What it does
Calculate n! = n × (n-1) × ... × 1 using recursion.

### Why it matters
- Classic example of recursion
- Demonstrates base case and recursive case
- Foundation for understanding recursive problems
- Used in permutations, combinations

### Intuition
Factorial of n is n times factorial of (n-1). Like a chain reaction where each step depends on the previous one.

### When to use
- Mathematics problems
- Permutations and combinations
- Understanding recursion basics

### Time complexity
- Time: `O(n)`
- Space: `O(n)` (call stack)

### Edge cases
- 0! = 1 (base case)
- Negative numbers (undefined)
- Large n (stack overflow, use iterative or BigInteger)

### Java code
```java
public class Factorial {
    // Recursive version
    static long factorialRecursive(int n) {
        if (n <= 1) return 1;
        return n * factorialRecursive(n - 1);
    }

    // Iterative version (better for large n)
    static long factorialIterative(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(factorialRecursive(5)); // 120
        System.out.println(factorialIterative(5)); // 120
    }
}
```

---

## 4. GCD (Greatest Common Divisor)

### What it does
Find largest number that divides both a and b.

### Why it matters
- Fundamental number theory algorithm
- Used in fraction simplification
- Cryptography applications
- Euclidean algorithm is very efficient

### Intuition
If a divides both numbers, it also divides their difference. Keep replacing larger number with difference until they become equal.

### When to use
- Simplifying fractions
- Finding LCM (LCM × GCD = a × b)
- Number theory problems

### Time complexity
- Time: `O(log(min(a, b)))`
- Space: `O(1)` (iterative) or `O(log n)` (recursive)

### Edge cases
- GCD(0, n) = n
- GCD(n, 0) = n
- GCD(0, 0) is undefined
- Negative numbers (use absolute values)

### Java code
```java
public class GCD {
    // Euclidean algorithm (recursive)
    static int gcdRecursive(int a, int b) {
        if (b == 0) return Math.abs(a);
        return gcdRecursive(b, a % b);
    }

    // Euclidean algorithm (iterative)
    static int gcdIterative(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        System.out.println(gcdRecursive(48, 18)); // 6
        System.out.println(gcdIterative(48, 18)); // 6
    }
}
```

---

## 5. LCM (Least Common Multiple)

### What it does
Find smallest number that is multiple of both a and b.

### Why it matters
- Used in time-based problems
- Fraction arithmetic
- Periodicity calculations
- Related to GCD: LCM(a, b) = |a × b| / GCD(a, b)

### Intuition
Think of two gears. LCM is when they both align at the starting position again.

### When to use
- Finding common periods
- Fraction addition/subtraction
- Scheduling problems

### Time complexity
- Time: `O(log(min(a, b)))` (due to GCD)
- Space: `O(1)`

### Edge cases
- LCM(0, n) = 0
- Large numbers (use long to avoid overflow)

### Java code
```java
public class LCM {
    static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    static int lcm(int a, int b) {
        if (a == 0 || b == 0) return 0;
        return Math.abs(a / gcd(a, b) * b);
    }

    public static void main(String[] args) {
        System.out.println(lcm(12, 18)); // 36
        System.out.println(lcm(5, 7));  // 35
    }
}
```

### Idea
Solve by calling same function for smaller input.

### Java code
```java
public class FactorialRecursion {
    static int factorial(int n) {
        if (n == 0 || n == 1) return 1;
        return n * factorial(n - 1);
    }

    public static void main(String[] args) {
        System.out.println(factorial(5)); // 120
    }
}
```

---

## 4. Sliding Window: Maximum Sum of size k

### Idea
Keep a moving window instead of recalculating every time.

### Time complexity
- `O(n)`

### Java code
```java
public class SlidingWindowMaxSum {
    static int maxSum(int[] arr, int k) {
        int windowSum = 0;

        for (int i = 0; i < k; i++) {
            windowSum += arr[i];
        }

        int maxSum = windowSum;

        for (int i = k; i < arr.length; i++) {
            windowSum += arr[i] - arr[i - k];
            if (windowSum > maxSum) {
                maxSum = windowSum;
            }
        }

        return maxSum;
    }

    public static void main(String[] args) {
        int[] arr = {2, 1, 5, 1, 3, 2};
        System.out.println(maxSum(arr, 3)); // 9
    }
}
```

---

## 5. Two Pointers: Pair Sum in Sorted Array

### Idea
Use one pointer at start and one at end.

### Time complexity
- `O(n)`

### Java code
```java
import java.util.Arrays;

public class PairSumSorted {
    static int[] findPair(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int sum = arr[left] + arr[right];
            if (sum == target) {
                return new int[]{left, right};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 4, 6, 10};
        System.out.println(Arrays.toString(findPair(arr, 8))); // [1, 3]
    }
}
```

---

## 6. Dynamic Programming: Fibonacci

### Idea
Store previous results to avoid repeated work.

### Time complexity
- `O(n)`

### Java code
```java
public class FibonacciDP {
    static int fib(int n) {
        if (n <= 1) return n;

        int prev2 = 0;
        int prev1 = 1;

        for (int i = 2; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;
    }

    public static void main(String[] args) {
        System.out.println(fib(10)); // 55
    }
}
```
