# Java DSA Interview Templates

Copy and adapt quickly during interviews.

---

## 1. BFS Template
```java
import java.util.LinkedList;
import java.util.Queue;

class BFSBasic {
    void bfs(int start, java.util.List<java.util.List<Integer>> g) {
        boolean[] vis = new boolean[g.size()];
        Queue<Integer> q = new LinkedList<>();
        vis[start] = true;
        q.offer(start);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : g.get(u)) {
                if (!vis[v]) {
                    vis[v] = true;
                    q.offer(v);
                }
            }
        }
    }
}
```

## 2. DFS Template
```java
class DFSBasic {
    void dfs(int u, java.util.List<java.util.List<Integer>> g, boolean[] vis) {
        vis[u] = true;
        for (int v : g.get(u)) {
            if (!vis[v]) dfs(v, g, vis);
        }
    }
}
```

## 3. Binary Search Template
```java
class BinarySearchTemplate {
    int binarySearch(int[] arr, int target) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) l = mid + 1;
            else r = mid - 1;
        }
        return -1;
    }
}
```

## 4. Sliding Window Template
```java
class SlidingWindowTemplate {
    int maxSumSizeK(int[] arr, int k) {
        int sum = 0;
        for (int i = 0; i < k; i++) sum += arr[i];
        int best = sum;

        for (int i = k; i < arr.length; i++) {
            sum += arr[i] - arr[i - k];
            best = Math.max(best, sum);
        }
        return best;
    }
}
```

## 5. Backtracking Template
```java
import java.util.ArrayList;
import java.util.List;

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

