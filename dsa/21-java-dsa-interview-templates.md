# Java DSA Interview Templates

Copy and adapt quickly during interviews. Each template includes common patterns and variations.

---

## 1. BFS Template

### Standard BFS
```java
import java.util.LinkedList;
import java.util.Queue;

class BFSTemplate {
    // Basic BFS traversal
    List<Integer> bfs(int start, List<List<Integer>> graph) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.size()];
        Queue<Integer> q = new LinkedList<>();

        visited[start] = true;
        q.offer(start);

        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    q.offer(v);
                }
            }
        }
        return order;
    }

    // BFS with level tracking
    List<List<Integer>> bfsLevels(int start, List<List<Integer>> graph) {
        List<List<Integer>> levels = new ArrayList<>();
        boolean[] visited = new boolean[graph.size()];
        Queue<Integer> q = new LinkedList<>();

        visited[start] = true;
        q.offer(start);

        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                int u = q.poll();
                level.add(u);
                for (int v : graph.get(u)) {
                    if (!visited[v]) {
                        visited[v] = true;
                        q.offer(v);
                    }
                }
            }
            levels.add(level);
        }
        return levels;
    }

    // BFS for shortest path in unweighted graph
    int[] shortestPath(int start, List<List<Integer>> graph) {
        int[] dist = new int[graph.size()];
        Arrays.fill(dist, -1);
        Queue<Integer> q = new LinkedList<>();

        dist[start] = 0;
        q.offer(start);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : graph.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    q.offer(v);
                }
            }
        }
        return dist;
    }
}
```

---

## 2. DFS Template

### Standard DFS
```java
import java.util.ArrayList;
import java.util.List;

class DFSTemplate {
    // Recursive DFS
    void dfs(int u, List<List<Integer>> graph, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        order.add(u);
        for (int v : graph.get(u)) {
            if (!visited[v]) {
                dfs(v, graph, visited, order);
            }
        }
    }

    // Iterative DFS
    List<Integer> dfsIterative(int start, List<List<Integer>> graph) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.size()];
        Stack<Integer> stack = new Stack<>();

        stack.push(start);
        visited[start] = true;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            order.add(u);
            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    stack.push(v);
                }
            }
        }
        return order;
    }

    // DFS with parent tracking (for cycle detection)
    boolean hasCycle(int u, int parent, List<List<Integer>> graph, boolean[] visited) {
        visited[u] = true;
        for (int v : graph.get(u)) {
            if (!visited[v]) {
                if (hasCycle(v, u, graph, visited)) return true;
            } else if (v != parent) {
                return true; // Back edge found
            }
        }
        return false;
    }
}
```

---

## 3. Binary Search Template

### Standard Binary Search
```java
class BinarySearchTemplate {
    // Find exact match
    int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // Find first occurrence (lower bound)
    int lowerBound(int[] arr, int target) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) left = mid + 1;
            else right = mid;
        }
        return left < arr.length && arr[left] == target ? left : -1;
    }

    // Find last occurrence (upper bound)
    int upperBound(int[] arr, int target) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= target) left = mid + 1;
            else right = mid;
        }
        return left > 0 && arr[left - 1] == target ? left - 1 : -1;
    }

    // Binary search on rotated sorted array
    int searchRotated(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;
            
            if (nums[left] <= nums[mid]) {
                // Left half is sorted
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // Right half is sorted
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }
}
```

---

## 4. Sliding Window Template

### Fixed Size Window
```java
class SlidingWindowTemplate {
    // Maximum sum of subarray of size k
    int maxSumSizeK(int[] nums, int k) {
        if (nums == null || nums.length < k) return 0;
        
        int sum = 0;
        for (int i = 0; i < k; i++) sum += nums[i];
        int maxSum = sum;

        for (int i = k; i < nums.length; i++) {
            sum += nums[i] - nums[i - k];
            maxSum = Math.max(maxSum, sum);
        }
        return maxSum;
    }

    // Longest substring with at most k distinct characters
    int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (s == null || s.length() == 0) return 0;
        
        Map<Character, Integer> freq = new HashMap<>();
        int left = 0, maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            freq.put(c, freq.getOrDefault(c, 0) + 1);

            while (freq.size() > k) {
                char leftChar = s.charAt(left);
                freq.put(leftChar, freq.get(leftChar) - 1);
                if (freq.get(leftChar) == 0) freq.remove(leftChar);
                left++;
            }
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    // Minimum window substring
    String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        Map<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray()) need.put(c, need.getOrDefault(c, 0) + 1);

        int left = 0, minLen = Integer.MAX_VALUE, start = 0;
        int formed = 0, required = need.size();
        Map<Character, Integer> windowCounts = new HashMap<>();

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            windowCounts.put(c, windowCounts.getOrDefault(c, 0) + 1);

            if (need.containsKey(c) && windowCounts.get(c).intValue() == need.get(c).intValue()) {
                formed++;
            }

            while (left <= right && formed == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }

                char leftChar = s.charAt(left);
                windowCounts.put(leftChar, windowCounts.get(leftChar) - 1);
                if (need.containsKey(leftChar) && windowCounts.get(leftChar) < need.get(leftChar)) {
                    formed--;
                }
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }
}
```

---

## 5. Backtracking Template

### General Backtracking
```java
import java.util.ArrayList;
import java.util.List;

class BacktrackingTemplate {
    // Subsets/Power set
    List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, int index, List<Integer> path, List<List<Integer>> result) {
        result.add(new ArrayList<>(path));
        for (int i = index; i < nums.length; i++) {
            path.add(nums[i]);
            backtrack(nums, i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }

    // Permutations
    List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            path.add(nums[i]);
            backtrack(nums, used, path, result);
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }

    // Combination Sum
    List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] candidates, int remain, int start, List<Integer> path, List<List<Integer>> result) {
        if (remain == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        if (remain < 0) return;

        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remain) break;
            path.add(candidates[i]);
            backtrack(candidates, remain - candidates[i], i, path, result);
            path.remove(path.size() - 1);
        }
    }
}
```

---

## 6. Union-Find (DSU) Template

### Disjoint Set Union
```java
class UnionFindTemplate {
    class DSU {
        int[] parent, rank;
        int components;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        boolean union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return false;

            if (rank[px] < rank[py]) parent[px] = py;
            else if (rank[py] < rank[px]) parent[py] = px;
            else {
                parent[py] = px;
                rank[px]++;
            }
            components--;
            return true;
        }

        boolean connected(int x, int y) {
            return find(x) == find(y);
        }

        int getComponents() {
            return components;
        }
    }
}
```

---

## 7. Trie Template

### Prefix Tree
```java
class TrieTemplate {
    class TrieNode {
        TrieNode[] children;
        boolean isEnd;

        TrieNode() {
            children = new TrieNode[26];
            isEnd = false;
        }
    }

    class Trie {
        TrieNode root;

        Trie() {
            root = new TrieNode();
        }

        void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) node.children[idx] = new TrieNode();
                node = node.children[idx];
            }
            node.isEnd = true;
        }

        boolean search(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) return false;
                node = node.children[idx];
            }
            return node.isEnd;
        }

        boolean startsWith(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) return false;
                node = node.children[idx];
            }
            return true;
        }
    }
}
```

---

## 8. Heap/Priority Queue Template

### Common Heap Operations
```java
import java.util.PriorityQueue;
import java.util.Collections;

class HeapTemplate {
    // Min heap (default)
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    // Max heap
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

    // Custom comparator
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);

    // K largest elements
    List<Integer> kLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) minHeap.poll();
        }
        List<Integer> result = new ArrayList<>(minHeap);
        Collections.sort(result, Collections.reverseOrder());
        return result;
    }

    // K smallest elements
    List<Integer> kSmallest(int[] nums, int k) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int num : nums) {
            maxHeap.offer(num);
            if (maxHeap.size() > k) maxHeap.poll();
        }
        List<Integer> result = new ArrayList<>(maxHeap);
        Collections.sort(result);
        return result;
    }
}
```

class BacktrackingTemplate {
    List<List<Integer>> ans = new ArrayList<>();

    void solve(int[] nums) {
        dfs(0, nums, new ArrayList<>());
    }

    void dfs(int idx, int[] nums, List<Integer> path) {
        if (idx == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }
        dfs(idx + 1, nums, path);
        path.add(nums[idx]);
        dfs(idx + 1, nums, path);
        path.remove(path.size() - 1);
    }
}
```

## 6. DP 1D Template
```java
class DP1DTemplate {
    int fib(int n) {
        if (n <= 1) return n;
        int prev2 = 0, prev1 = 1;
        for (int i = 2; i <= n; i++) {
            int cur = prev1 + prev2;
            prev2 = prev1;
            prev1 = cur;
        }
        return prev1;
    }
}
```

## 7. Heap Template
```java
import java.util.PriorityQueue;

class HeapTemplate {
    int kthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int x : nums) {
            minHeap.offer(x);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek();
    }
}
```

## 8. Union Find Template
```java
class UnionFindTemplate {
    int[] p, rank;

    UnionFindTemplate(int n) {
        p = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) p[i] = i;
    }

    int find(int x) {
        if (p[x] != x) p[x] = find(p[x]);
        return p[x];
    }

    boolean union(int a, int b) {
        int pa = find(a), pb = find(b);
        if (pa == pb) return false;
        if (rank[pa] < rank[pb]) p[pa] = pb;
        else if (rank[pb] < rank[pa]) p[pb] = pa;
        else { p[pb] = pa; rank[pa]++; }
        return true;
    }
}
```

