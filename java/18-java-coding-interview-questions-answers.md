# Java Coding Interview Questions & Answers (Topic-wise)

**Target:** WITCH companies and product-based interview prep.  
**Focus:** Most asked Java coding patterns with clear approach + optimized code.

---

## Table of Contents

1. [Arrays & Two Pointers](#1-arrays--two-pointers)
2. [Strings](#2-strings)
3. [HashMap / HashSet Patterns](#3-hashmap--hashset-patterns)
4. [Stack & Queue](#4-stack--queue)
5. [Linked List](#5-linked-list)
6. [Trees & BST](#6-trees--bst)
7. [Recursion & Backtracking](#7-recursion--backtracking)
8. [Dynamic Programming](#8-dynamic-programming)
9. [Java 8+ Coding Style Questions](#9-java-8-coding-style-questions)
10. [Concurrency Coding Questions](#10-concurrency-coding-questions)
11. [WITCH Companies - Frequently Asked Coding Patterns](#11-witch-companies---frequently-asked-coding-patterns)

---

## 1. Arrays & Two Pointers

### Q1) Find two numbers that add up to target (Two Sum)

**Input:** `nums = [2, 7, 11, 15], target = 9`  
**Output:** `[0, 1]`

```java
import java.util.*;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(); // value -> index

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }
}
```

**Complexity:** `O(n)` time, `O(n)` space.

### Q2) Move all zeros to end

```java
class Solution {
    public void moveZeroes(int[] nums) {
        int insertPos = 0;

        for (int num : nums) {
            if (num != 0) nums[insertPos++] = num;
        }

        while (insertPos < nums.length) {
            nums[insertPos++] = 0;
        }
    }
}
```

### Q3) Best time to buy and sell stock (single transaction)

```java
class Solution {
    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int p : prices) {
            minPrice = Math.min(minPrice, p);
            maxProfit = Math.max(maxProfit, p - minPrice);
        }
        return maxProfit;
    }
}
```

## 2. Strings

### Q4) Reverse words in a sentence

**Input:** `"I love Java"`  
**Output:** `"Java love I"`

```java
class Solution {
    public String reverseWords(String s) {
        String[] parts = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (int i = parts.length - 1; i >= 0; i--) {
            sb.append(parts[i]);
            if (i != 0) sb.append(" ");
        }
        return sb.toString();
    }
}
```

### Q5) Check if two strings are anagrams

```java
import java.util.*;

class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] freq = new int[26];
        for (char c : s.toCharArray()) freq[c - 'a']++;
        for (char c : t.toCharArray()) freq[c - 'a']--;

        for (int val : freq) if (val != 0) return false;
        return true;
    }
}
```

### Q6) First non-repeating character

```java
import java.util.*;

class Solution {
    public int firstUniqChar(String s) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) freq[c - 'a']++;

        for (int i = 0; i < s.length(); i++) {
            if (freq[s.charAt(i) - 'a'] == 1) return i;
        }
        return -1;
    }
}
```

---

## 3. HashMap / HashSet Patterns

### Q7) Longest consecutive sequence

```java
import java.util.*;

class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);

        int longest = 0;

        for (int n : set) {
            if (!set.contains(n - 1)) { // start of sequence
                int curr = n;
                int length = 1;

                while (set.contains(curr + 1)) {
                    curr++;
                    length++;
                }
                longest = Math.max(longest, length);
            }
        }
        return longest;
    }
}
```

### Q8) Group anagrams

```java
import java.util.*;

class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            String key = new String(arr);

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }
}
```

---

## 4. Stack & Queue

### Q9) Valid parentheses

```java
import java.util.*;

class Solution {
    public boolean isValid(String s) {
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

### Q10) Implement Min Stack

```java
import java.util.*;

class MinStack {
    private Stack<Integer> stack = new Stack<>();
    private Stack<Integer> minStack = new Stack<>();

    public void push(int val) {
        stack.push(val);
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }

    public void pop() {
        if (stack.pop().equals(minStack.peek())) {
            minStack.pop();
        }
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
```

---

## 5. Linked List

### Q11) Reverse a linked list

```java
class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode nextNode = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextNode;
        }
        return prev;
    }
}
```

### Q12) Detect cycle in linked list (Floyd’s cycle)

```java
class Solution {
    public boolean hasCycle(ListNode head) {
        if (head == null) return false;

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }
}
```

---

## 6. Trees & BST

### Q13) Maximum depth of binary tree

```java
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

class Solution {
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }
}
```

### Q14) Validate BST

```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;

        return validate(node.left, min, node.val) &&
               validate(node.right, node.val, max);
    }
}
```

### Q15) Level order traversal

```java
import java.util.*;

class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                level.add(node.val);

                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }
}
```

---

## 7. Recursion & Backtracking

### Q16) Generate all subsets (power set)

```java
import java.util.*;

class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(0, nums, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int index, int[] nums, List<Integer> curr, List<List<Integer>> result) {
        result.add(new ArrayList<>(curr));

        for (int i = index; i < nums.length; i++) {
            curr.add(nums[i]);
            backtrack(i + 1, nums, curr, result);
            curr.remove(curr.size() - 1); // undo
        }
    }
}
```

### Q17) Permutations

```java
import java.util.*;

class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, boolean[] used, List<Integer> curr, List<List<Integer>> result) {
        if (curr.size() == nums.length) {
            result.add(new ArrayList<>(curr));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            curr.add(nums[i]);

            backtrack(nums, used, curr, result);

            curr.remove(curr.size() - 1);
            used[i] = false;
        }
    }
}
```

---

## 8. Dynamic Programming

### Q18) Climbing stairs

```java
class Solution {
    public int climbStairs(int n) {
        if (n <= 2) return n;

        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return b;
    }
}
```

### Q19) House robber

```java
class Solution {
    public int rob(int[] nums) {
        int prev2 = 0; // dp[i-2]
        int prev1 = 0; // dp[i-1]

        for (int num : nums) {
            int curr = Math.max(prev1, prev2 + num);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

### Q20) Longest common subsequence

```java
class Solution {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }
}
```

---

## 9. Java 8+ Coding Style Questions

### Q21) Find duplicate elements in list using streams

```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Solution {
    public static Set<Integer> findDuplicates(List<Integer> list) {
        return list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
```

### Q22) Sort employees by salary descending then name ascending

```java
import java.util.*;

class Employee {
    String name;
    double salary;

    Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() { return name; }
    public double getSalary() { return salary; }
}

class Solution {
    public List<Employee> sortEmployees(List<Employee> employees) {
        employees.sort(Comparator
                .comparingDouble(Employee::getSalary).reversed()
                .thenComparing(Employee::getName));
        return employees;
    }
}
```

### Q23) Frequency of each character in string

```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Solution {
    public Map<Character, Long> charFrequency(String s) {
        return s.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
```

---

## 10. Concurrency Coding Questions

### Q24) Print odd and even numbers using two threads

```java
class OddEvenPrinter {
    private int num = 1;
    private final int limit;

    public OddEvenPrinter(int limit) {
        this.limit = limit;
    }

    public synchronized void printOdd() {
        while (num <= limit) {
            while (num % 2 == 0) {
                try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
            if (num <= limit) System.out.println("Odd: " + num++);
            notifyAll();
        }
    }

    public synchronized void printEven() {
        while (num <= limit) {
            while (num % 2 != 0) {
                try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
            if (num <= limit) System.out.println("Even: " + num++);
            notifyAll();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        OddEvenPrinter printer = new OddEvenPrinter(10);

        Thread oddThread = new Thread(printer::printOdd);
        Thread evenThread = new Thread(printer::printEven);

        oddThread.start();
        evenThread.start();
    }
}
```

### Q25) Thread-safe singleton (double-checked locking)

```java
class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

---

## 11. WITCH Companies - Frequently Asked Coding Patterns

This section focuses on patterns commonly seen in **Wipro, Infosys, TCS, Cognizant, HCL** coding rounds.

### Typical WITCH Round Pattern

| Round Type | Usually Asked |
|-----------|----------------|
| **Aptitude + Basic Coding** | Prime, palindrome, Fibonacci, number reversal, sorting basics |
| **Coding Round 1** | Arrays, strings, HashMap, stack/queue basics |
| **Coding Round 2 (if any)** | Linked list, tree traversal, recursion/DP basics |
| **Technical Interview** | Explain code complexity, optimize brute force, edge case handling |

### Q26) Palindrome number

```java
class Solution {
    public boolean isPalindrome(int n) {
        if (n < 0) return false;

        int original = n;
        int reversed = 0;

        while (n > 0) {
            int digit = n % 10;
            reversed = reversed * 10 + digit;
            n /= 10;
        }
        return original == reversed;
    }
}
```

### Q27) Check if number is prime

```java
class Solution {
    public boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
```

### Q28) Fibonacci series up to N terms

```java
import java.util.*;

class Solution {
    public List<Integer> fibonacci(int n) {
        List<Integer> result = new ArrayList<>();
        if (n <= 0) return result;

        int a = 0, b = 1;
        result.add(a);
        if (n == 1) return result;

        result.add(b);
        for (int i = 3; i <= n; i++) {
            int c = a + b;
            result.add(c);
            a = b;
            b = c;
        }
        return result;
    }
}
```

### Q29) Second largest element in array

```java
class Solution {
    public int secondLargest(int[] nums) {
        if (nums == null || nums.length < 2) return -1;

        Integer first = null;
        Integer second = null;

        for (int n : nums) {
            if (first == null || n > first) {
                second = first;
                first = n;
            } else if (n != first && (second == null || n > second)) {
                second = n;
            }
        }
        return second == null ? -1 : second;
    }
}
```

### Q30) Frequency of elements in array

```java
import java.util.*;

class Solution {
    public Map<Integer, Integer> frequency(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) {
            freq.put(n, freq.getOrDefault(n, 0) + 1);
        }
        return freq;
    }
}
```

### WITCH-specific preparation checklist

1. Practice **basic coding speed** (write 2-3 correct solutions in 45-60 min).
2. Be fluent with **arrays + strings + HashMap** (most frequent).
3. Always state **time and space complexity**.
4. Handle edge cases: empty input, duplicates, negative values, large constraints.
5. Prefer clean iterative solutions first; then discuss optimized/Java 8 variant.

---

## Fast Revision: Must-practice Coding Set

1. Two Sum
2. Reverse Linked List
3. Valid Parentheses
4. Merge Intervals
5. Binary Search variants
6. BFS + DFS on tree/graph
7. Sliding Window (longest substring without repeat)
8. Top K Frequent Elements (Heap)
9. LRU Cache
10. DP basics (climb stairs, house robber, coin change)

---

## Interview Tips (Coding Round)

1. Clarify constraints first (`n`, duplicates, sorted?, negative values?).
2. Start with brute force and speak complexity.
3. Propose optimized approach with trade-offs.
4. Write clean method names and edge-case handling.
5. Dry-run with sample input.
6. State final complexity.
7. If time remains, discuss alternative approach.
