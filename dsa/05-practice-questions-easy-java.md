# DSA Practice Questions (Easy) with Java Answers

Use this file after finishing the concept files.

---

## 1. Find largest element in array

```java
public class LargestElement {
    static int largest(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
        }
        return max;
    }
}
```

## 2. Reverse an array

```java
import java.util.Arrays;

public class ReverseArray {
    static void reverse(int[] arr) {
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

## 3. Check palindrome string

```java
public class PalindromeCheck {
    static boolean isPalindrome(String s) {
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

## 4. Count frequency using HashMap

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
}
```

## 5. Valid parentheses

```java
import java.util.Stack;

public class ValidParentheses {
    static boolean isValid(String s) {
        Stack<Character> st = new Stack<>();
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
