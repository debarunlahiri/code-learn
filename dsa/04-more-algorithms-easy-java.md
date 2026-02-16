# More Algorithms in Java (Easy)

Goal: Learn additional core algorithms with simple code.

---

## 1. Insertion Sort

### Idea
Build sorted part one element at a time.

### Time complexity
- Worst: `O(n^2)`

### Java code
```java
import java.util.Arrays;

public class InsertionSort {
    static void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

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

### Idea
Select smallest element and place it at correct position.

### Time complexity
- Worst: `O(n^2)`

### Java code
```java
import java.util.Arrays;

public class SelectionSort {
    static void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
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
