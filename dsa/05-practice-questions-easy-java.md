# DSA Practice Questions (Easy) with Java Answers

Use this file after finishing the concept files. Each problem includes approach, complexity, and edge cases.

---

## 1. Find largest element in array

### Approach
Traverse array once, keeping track of maximum element seen so far.

### Time complexity
- Time: `O(n)`
- Space: `O(1)`

### Edge cases
- Empty array (should throw exception or return special value)
- All elements equal
- Single element array

### Java code
```java
public class LargestElement {
    static int largest(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
        }
        return max;
    }
}
```

---

## 2. Reverse an array

### Approach
Use two-pointer technique: swap first and last elements, move towards center.

### Time complexity
- Time: `O(n/2) = O(n)`
- Space: `O(1)` (in-place)

### Edge cases
- Empty array or single element (no changes needed)
- Even vs odd length arrays

### Java code
```java
import java.util.Arrays;

public class ReverseArray {
    static void reverse(int[] arr) {
        if (arr == null) return;
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}
```

---

## 3. Check palindrome string

### Approach
Two-pointer technique: compare characters from start and end moving inward.

### Time complexity
- Time: `O(n/2) = O(n)`
- Space: `O(1)`

### Edge cases
- Empty string (considered palindrome)
- Single character (palindrome)
- Case sensitivity (handle with toLowerCase() if needed)
- Non-alphanumeric characters (filter if checking "clean" palindrome)

### Java code
```java
public class PalindromeCheck {
    static boolean isPalindrome(String s) {
        if (s == null) return false;
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }

    // Case-insensitive version
    static boolean isPalindromeIgnoreCase(String s) {
        if (s == null) return false;
        s = s.toLowerCase();
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}
```

---

## 4. Count frequency using HashMap

### Approach
Iterate through array, use HashMap to count occurrences of each element.

### Time complexity
- Time: `O(n)`
- Space: `O(k)` where k = number of distinct elements

### Edge cases
- Empty array (returns empty map)
- All elements same
- Large range of values

### Java code
```java
import java.util.HashMap;
import java.util.Map;

public class FrequencyCount {
    static Map<Integer, Integer> count(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : arr) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        return map;
    }

    // Alternative: Get most frequent element
    static int mostFrequent(int[] arr) {
        Map<Integer, Integer> freq = count(arr);
        int maxCount = 0, result = arr[0];
        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                result = entry.getKey();
            }
        }
        return result;
    }
}
```

---

## 5. Valid parentheses

### Approach
Use stack to track opening brackets. For each closing bracket, check if it matches the top of stack.

### Time complexity
- Time: `O(n)`
- Space: `O(n)` in worst case (all opening brackets)

### Edge cases
- Empty string (valid)
- Odd length (invalid)
- Mismatched brackets
- Extra opening/closing brackets

### Java code
```java
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class ValidParentheses {
    static boolean isValid(String s) {
        if (s == null) return false;
        Stack<Character> st = new Stack<>();
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');

        for (char c : s.toCharArray()) {
            if (map.containsKey(c)) {
                // Closing bracket
                if (st.isEmpty() || st.pop() != map.get(c)) {
                    return false;
                }
            } else {
                // Opening bracket
                st.push(c);
            }
        }
        return st.isEmpty();
    }
}
```

---

## 6. Find second largest element

### Approach
Maintain two variables: largest and second largest. Update as you traverse.

### Time complexity
- Time: `O(n)`
- Space: `O(1)`

### Edge cases
- Array with less than 2 elements
- All elements equal
- Duplicates of largest element

### Java code
```java
public class SecondLargest {
    static int secondLargest(int[] arr) {
        if (arr == null || arr.length < 2) {
            throw new IllegalArgumentException("Array must have at least 2 elements");
        }

        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;

        for (int num : arr) {
            if (num > largest) {
                secondLargest = largest;
                largest = num;
            } else if (num > secondLargest && num != largest) {
                secondLargest = num;
            }
        }

        if (secondLargest == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("No second largest element found");
        }
        return secondLargest;
    }
}
```

---

## 7. Remove duplicates from array

### Approach
Use HashSet to track seen elements, create new array with unique elements.

### Time complexity
- Time: `O(n)`
- Space: `O(n)` for HashSet and result

### Edge cases
- Empty array
- All elements duplicates
- Already unique elements

### Java code
```java
import java.util.*;

public class RemoveDuplicates {
    static int[] removeDuplicates(int[] arr) {
        if (arr == null || arr.length == 0) return arr;
        
        Set<Integer> seen = new LinkedHashSet<>(); // Maintains order
        for (int num : arr) {
            seen.add(num);
        }
        
        int[] result = new int[seen.size()];
        int i = 0;
        for (int num : seen) {
            result[i++] = num;
        }
        return result;
    }
}
```

---

## 8. Check if array is sorted

### Approach
Traverse array once, check if each element is >= previous element.

### Time complexity
- Time: `O(n)`
- Space: `O(1)`

### Edge cases
- Empty array (considered sorted)
- Single element (sorted)
- All equal elements (sorted)

### Java code
```java
public class IsSorted {
    static boolean isSorted(int[] arr) {
        if (arr == null || arr.length <= 1) return true;
        
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }

    // For descending order
    static boolean isSortedDescending(int[] arr) {
        if (arr == null || arr.length <= 1) return true;
        
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
```
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                st.push(c);
            } else {
                if (st.isEmpty()) return false;
                char top = st.pop();
                if ((c == ')' && top != '(') ||
                    (c == '}' && top != '{') ||
                    (c == ']' && top != '[')) {
                    return false;
                }
            }
        }
        return st.isEmpty();
    }
}
```

## 6. Move zeros to end

```java
import java.util.Arrays;

public class MoveZeros {
    static void move(int[] arr) {
        int insert = 0;

        for (int num : arr) {
            if (num != 0) {
                arr[insert] = num;
                insert++;
            }
        }

        while (insert < arr.length) {
            arr[insert] = 0;
            insert++;
        }
    }
}
```

## 7. Two sum

```java
import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (map.containsKey(need)) {
                return new int[]{map.get(need), i};
            }
            map.put(nums[i], i);
        }

        return new int[]{-1, -1};
    }
}
```

## 8. Level order traversal of binary tree

```java
import java.util.LinkedList;
import java.util.Queue;

public class LevelOrderTraversal {
    static class Node {
        int value;
        Node left, right;
        Node(int value) { this.value = value; }
    }

    static void levelOrder(Node root) {
        if (root == null) return;

        Queue<Node> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            Node current = q.poll();
            System.out.print(current.value + " ");

            if (current.left != null) q.offer(current.left);
            if (current.right != null) q.offer(current.right);
        }
    }
}
```

## 9. Find factorial (iterative)

```java
public class FactorialIterative {
    static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
```

## 10. Fibonacci (iterative)

```java
public class FibonacciIterative {
    static int fib(int n) {
        if (n <= 1) return n;

        int a = 0;
        int b = 1;

        for (int i = 2; i <= n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }

        return b;
    }
}
```
