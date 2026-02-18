# Common Interview Questions with Java Solutions

Goal: Master frequently asked interview questions with detailed solutions.

---

## 📋 Array Questions

### 1. Two Sum
**Problem**: Find two numbers that add up to target.

**Approach**: Use HashMap for O(n) solution.

**Java Solution**:
```java
import java.util.*;

public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        
        throw new IllegalArgumentException("No solution found");
    }
    
    // Alternative: Two pointers for sorted array
    public int[] twoSumSorted(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                return new int[]{left, right};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        
        throw new IllegalArgumentException("No solution found");
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(n)

---

### 2. Best Time to Buy and Sell Stock
**Problem**: Maximum profit from stock prices.

**Approach**: Track minimum price and maximum profit.

**Java Solution**:
```java
public class StockProfit {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        
        int minPrice = prices[0];
        int maxProfit = 0;
        
        for (int i = 1; i < prices.length; i++) {
            maxProfit = Math.max(maxProfit, prices[i] - minPrice);
            minPrice = Math.min(minPrice, prices[i]);
        }
        
        return maxProfit;
    }
    
    // Multiple transactions allowed
    public int maxProfitMultiple(int[] prices) {
        int profit = 0;
        
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                profit += prices[i] - prices[i - 1];
            }
        }
        
        return profit;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(1)

---

### 3. Container With Most Water
**Problem**: Find max area between two lines.

**Approach**: Two pointers from both ends.

**Java Solution**:
```java
public class ContainerWithMostWater {
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;
        
        while (left < right) {
            int width = right - left;
            int currentHeight = Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, width * currentHeight);
            
            // Move the pointer with smaller height
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(1)

---

## 📋 String Questions

### 4. Longest Substring Without Repeating Characters
**Problem**: Find length of longest substring without duplicates.

**Approach**: Sliding window with HashMap.

**Java Solution**:
```java
import java.util.*;

public class LongestSubstring {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastIndex = new HashMap<>();
        int left = 0, maxLength = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            
            if (lastIndex.containsKey(c) && lastIndex.get(c) >= left) {
                left = lastIndex.get(c) + 1;
            }
            
            lastIndex.put(c, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    // Alternative: Using array for ASCII characters
    public int lengthOfLongestSubstringASCII(String s) {
        int[] lastIndex = new int[128];
        Arrays.fill(lastIndex, -1);
        
        int left = 0, maxLength = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            
            if (lastIndex[c] >= left) {
                left = lastIndex[c] + 1;
            }
            
            lastIndex[c] = right;
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(min(n, charset))

---

### 5. Valid Parentheses
**Problem**: Check if parentheses are valid.

**Approach**: Stack to track opening brackets.

**Java Solution**:
```java
import java.util.*;

public class ValidParentheses {
    public boolean isValid(String s) {
        if (s == null || s.length() % 2 != 0) return false;
        
        Deque<Character> stack = new ArrayDeque<>();
        Map<Character, Character> mapping = new HashMap<>();
        mapping.put(')', '(');
        mapping.put('}', '{');
        mapping.put(']', '[');
        
        for (char c : s.toCharArray()) {
            if (mapping.containsValue(c)) {
                stack.push(c);
            } else {
                if (stack.isEmpty() || stack.pop() != mapping.get(c)) {
                    return false;
                }
            }
        }
        
        return stack.isEmpty();
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(n)

---

## 📋 Linked List Questions

### 6. Reverse Linked List
**Problem**: Reverse a singly linked list.

**Approach**: Iterative with three pointers.

**Java Solution**:
```java
public class ReverseLinkedList {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }
    
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        return prev;
    }
    
    // Recursive solution
    public ListNode reverseListRecursive(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode reversed = reverseListRecursive(head.next);
        head.next.next = head;
        head.next = null;
        
        return reversed;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(1) iterative, O(n) recursive

---

### 7. Merge Two Sorted Lists
**Problem**: Merge two sorted linked lists.

**Approach**: Compare and merge nodes.

**Java Solution**:
```java
public class MergeSortedLists {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }
    
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        
        current.next = (l1 != null) ? l1 : l2;
        
        return dummy.next;
    }
}
```

**Time Complexity**: O(m + n)
**Space Complexity**: O(1)

---

## 📋 Tree Questions

### 8. Maximum Depth of Binary Tree
**Problem**: Find maximum depth of binary tree.

**Approach**: DFS recursion.

**Java Solution**:
```java
public class MaxDepth {
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }
    
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    // BFS solution
    public int maxDepthBFS(TreeNode root) {
        if (root == null) return 0;
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            depth++;
            
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        
        return depth;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(h) recursive, O(w) BFS

---

### 9. Validate Binary Search Tree
**Problem**: Check if tree is valid BST.

**Approach**: Inorder traversal with bounds.

**Java Solution**:
```java
public class ValidateBST {
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }
    
    public boolean isValidBST(TreeNode root) {
        return validate(root, null, null);
    }
    
    private boolean validate(TreeNode node, Integer min, Integer max) {
        if (node == null) return true;
        
        if ((min != null && node.val <= min) || 
            (max != null && node.val >= max)) {
            return false;
        }
        
        return validate(node.left, min, node.val) && 
               validate(node.right, node.val, max);
    }
    
    // Inorder traversal solution
    private Integer prev = null;
    
    public boolean isValidBSTInorder(TreeNode root) {
        prev = null;
        return inorder(root);
    }
    
    private boolean inorder(TreeNode node) {
        if (node == null) return true;
        
        if (!inorder(node.left)) return false;
        
        if (prev != null && node.val <= prev) return false;
        prev = node.val;
        
        return inorder(node.right);
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(h)

---

## 📋 Dynamic Programming Questions

### 10. Climbing Stairs
**Problem**: Number of ways to climb n stairs.

**Approach**: DP with O(1) space.

**Java Solution**:
```java
public class ClimbingStairs {
    public int climbStairs(int n) {
        if (n <= 2) return n;
        
        int a = 1, b = 2, c = 0;
        
        for (int i = 3; i <= n; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        
        return b;
    }
    
    // DP with array
    public int climbStairsDP(int n) {
        if (n <= 2) return n;
        
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    // Recursive with memoization
    private Map<Integer, Integer> memo = new HashMap<>();
    
    public int climbStairsMemo(int n) {
        if (n <= 2) return n;
        
        if (memo.containsKey(n)) {
            return memo.get(n);
        }
        
        int result = climbStairsMemo(n - 1) + climbStairsMemo(n - 2);
        memo.put(n, result);
        
        return result;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(1)

---

### 11. House Robber
**Problem**: Maximum money without robbing adjacent houses.

**Approach**: DP with two states.

**Java Solution**:
```java
public class HouseRobber {
    public int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int prev2 = 0, prev1 = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            int current = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    // DP with array
    public int robDP(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        
        return dp[nums.length - 1];
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(1)

---

## 📋 Graph Questions

### 12. Number of Islands
**Problem**: Count islands in 2D grid.

**Approach**: DFS/BFS to mark visited.

**Java Solution**:
```java
public class NumberOfIslands {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int count = 0;
        int rows = grid.length, cols = grid[0].length;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || 
            grid[i][j] != '1') {
            return;
        }
        
        grid[i][j] = '0'; // Mark as visited
        
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
    
    // BFS solution
    public int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int count = 0;
        int rows = grid.length, cols = grid[0].length;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    bfs(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private void bfs(char[][] grid, int i, int j) {
        int rows = grid.length, cols = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{i, j});
        grid[i][j] = '0';
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0], col = current[1];
            
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && 
                    grid[newRow][newCol] == '1') {
                    grid[newRow][newCol] = '0';
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
    }
}
```

**Time Complexity**: O(m × n)
**Space Complexity**: O(m × n) worst case

---

## 📋 Sorting Questions

### 13. Merge Sort
**Problem**: Implement merge sort algorithm.

**Approach**: Divide and conquer.

**Java Solution**:
```java
public class MergeSort {
    public int[] mergeSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }
        
        int[] auxiliary = new int[arr.length];
        mergeSort(arr, auxiliary, 0, arr.length - 1);
        return arr;
    }
    
    private void mergeSort(int[] arr, int[] auxiliary, int left, int right) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        
        mergeSort(arr, auxiliary, left, mid);
        mergeSort(arr, auxiliary, mid + 1, right);
        merge(arr, auxiliary, left, mid, right);
    }
    
    private void merge(int[] arr, int[] auxiliary, int left, int mid, int right) {
        // Copy to auxiliary array
        for (int i = left; i <= right; i++) {
            auxiliary[i] = arr[i];
        }
        
        int i = left, j = mid + 1, k = left;
        
        while (i <= mid && j <= right) {
            if (auxiliary[i] <= auxiliary[j]) {
                arr[k++] = auxiliary[i++];
            } else {
                arr[k++] = auxiliary[j++];
            }
        }
        
        // Copy remaining elements
        while (i <= mid) {
            arr[k++] = auxiliary[i++];
        }
    }
}
```

**Time Complexity**: O(n log n)
**Space Complexity**: O(n)

---

### 14. Quick Sort
**Problem**: Implement quick sort algorithm.

**Approach**: Partition around pivot.

**Java Solution**:
```java
public class QuickSort {
    public void quickSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSort(arr, 0, arr.length - 1);
    }
    
    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }
    
    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        
        swap(arr, i + 1, high);
        return i + 1;
    }
    
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    // Randomized quicksort
    private void quickSortRandom(int[] arr, int low, int high) {
        if (low < high) {
            int randomIndex = low + (int)(Math.random() * (high - low + 1));
            swap(arr, randomIndex, high);
            
            int pivotIndex = partition(arr, low, high);
            quickSortRandom(arr, low, pivotIndex - 1);
            quickSortRandom(arr, pivotIndex + 1, high);
        }
    }
}
```

**Time Complexity**: O(n log n) average, O(n²) worst
**Space Complexity**: O(log n) recursive stack

---

## 📋 Advanced Questions

### 15. LRU Cache
**Problem**: Implement LRU cache with O(1) operations.

**Approach**: HashMap + Doubly Linked List.

**Java Solution**:
```java
import java.util.*;

public class LRUCache {
    private final int capacity;
    private final Map<Integer, DLinkedNode> cache;
    private final DLinkedNode head;
    private final DLinkedNode tail;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.head = new DLinkedNode();
        this.tail = new DLinkedNode();
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        DLinkedNode node = cache.get(key);
        if (node == null) return -1;
        
        moveToHead(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        DLinkedNode node = cache.get(key);
        
        if (node == null) {
            DLinkedNode newNode = new DLinkedNode(key, value);
            cache.put(key, newNode);
            addNode(newNode);
            
            if (cache.size() > capacity) {
                DLinkedNode tail = popTail();
                cache.remove(tail.key);
            }
        } else {
            node.value = value;
            moveToHead(node);
        }
    }
    
    private void addNode(DLinkedNode node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    private void removeNode(DLinkedNode node) {
        DLinkedNode prev = node.prev;
        DLinkedNode next = node.next;
        prev.next = next;
        next.prev = prev;
    }
    
    private void moveToHead(DLinkedNode node) {
        removeNode(node);
        addNode(node);
    }
    
    private DLinkedNode popTail() {
        DLinkedNode res = tail.prev;
        removeNode(res);
        return res;
    }
    
    class DLinkedNode {
        int key;
        int value;
        DLinkedNode prev;
        DLinkedNode next;
        
        DLinkedNode() {}
        
        DLinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
```

**Time Complexity**: O(1) for both operations
**Space Complexity**: O(capacity)

---

## 📯 Tips for Success

### Problem-Solving Strategy
1. **Understand**: Clarify requirements and constraints
2. **Plan**: Choose appropriate data structures and algorithms
3. **Implement**: Write clean, readable code
4. **Test**: Verify with examples and edge cases
5. **Optimize**: Discuss time/space complexity

### Common Mistakes to Avoid
- Not handling edge cases (null, empty, single element)
- Ignoring time/space complexity requirements
- Writing code without explaining approach
- Not testing with examples
- Giving up too early on hard problems

### Practice Recommendations
- Start with easy problems and build confidence
- Focus on understanding patterns, not memorizing solutions
- Practice explaining solutions out loud
- Time yourself to improve speed
- Review and learn from mistakes

---

**Remember**: Practice consistently and focus on understanding patterns rather than memorizing solutions! 🚀
