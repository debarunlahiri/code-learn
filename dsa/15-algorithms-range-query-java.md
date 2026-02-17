# Algorithms: Range Query and Advanced Data Structures

Goal: Master efficient range queries and updates using advanced data structures.

---

## 1. Prefix Sum (static range sum)

### What it does
Preprocess array to answer range sum queries in O(1) time.

### Why it matters
- Fast range sum queries for static arrays
- Foundation for many range query problems
- Used in image processing, data analysis
- Simple yet powerful technique

### Intuition
Store cumulative sums. Range sum [l, r] = prefix[r] - prefix[l-1]. Like keeping a running total to quickly calculate any sub-total.

### When to use
- Static arrays (no updates)
- Multiple range sum queries
- Subarray sum problems
- 2D prefix sums for matrices

### Time complexity
- Preprocessing: `O(n)`
- Query: `O(1)`
- Space: `O(n)`

### Edge cases
- Empty array
- Single element range
- Large sums (use long)

### Java code
```java
public class PrefixSum {
    static int[] build(int[] arr) {
        if (arr == null) return new int[1];
        int[] pre = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            pre[i + 1] = pre[i] + arr[i];
        }
        return pre;
    }

    static int rangeSum(int[] pre, int l, int r) {
        if (pre == null || l < 0 || r >= pre.length - 1 || l > r) return 0;
        return pre[r + 1] - pre[l];
    }

    // 2D prefix sum for matrices
    static int[][] build2D(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] pre = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                pre[i][j] = matrix[i - 1][j - 1] + pre[i - 1][j] + pre[i][j - 1] - pre[i - 1][j - 1];
            }
        }
        return pre;
    }

    static int rangeSum2D(int[][] pre, int r1, int c1, int r2, int c2) {
        return pre[r2 + 1][c2 + 1] - pre[r1][c2 + 1] - pre[r2 + 1][c1] + pre[r1][c1];
    }
}
```

---

## 2. Difference Array (range increment updates)

### What it does
Efficiently apply range increment operations to an array.

### Why it matters
- Range updates in O(1) time
- Used in scheduling, coloring problems
- Transforms range updates to point updates
- Foundation for lazy propagation

### Intuition
Mark only the start and end of each range. Later, sweep through to apply all changes. Like marking when to start/stop painting a fence section.

### When to use
- Multiple range increment operations
- Range addition/subtraction
- Scheduling problems
- Array manipulation with ranges

### Time complexity
- Updates: `O(1)` each
- Final array construction: `O(n)`
- Space: `O(n)`

### Edge cases
- Overlapping ranges
- Updates at boundaries
- Negative values
- Large number of updates

### Java code
```java
public class DifferenceArray {
    static int[] applyUpdates(int n, int[][] updates) {
        if (n <= 0) return new int[0];
        int[] diff = new int[n + 1];

        for (int[] u : updates) {
            int l = u[0], r = u[1], val = u[2];
            if (l < 0 || r >= n || l > r) continue;
            diff[l] += val;
            if (r + 1 < n) diff[r + 1] -= val;
        }

        int[] arr = new int[n];
        int run = 0;
        for (int i = 0; i < n; i++) {
            run += diff[i];
            arr[i] = run;
        }
        return arr;
    }

    // For range set operations (not just increment)
    static int[] applyRangeSets(int n, int[][] operations) {
        int[] diff = new int[n + 1];
        
        for (int[] op : operations) {
            int l = op[0], r = op[1], val = op[2];
            diff[l] += val;
            if (r + 1 < n) diff[r + 1] -= val;
        }

        int[] arr = new int[n];
        int run = 0;
        for (int i = 0; i < n; i++) {
            run += diff[i];
            arr[i] = run;
        }
        return arr;
    }
}
```

---

## 3. Fenwick Tree (BIT)

### What it does
Binary Indexed Tree for point updates and prefix sum queries.

### Why it matters
- Both updates and queries in O(log n)
- Space efficient
- Simpler than segment tree
- Used in many competitive programming problems

### Intuition
Store partial sums in a tree structure. Each node represents a range and can be accessed using bit manipulation.

### When to use
- Point updates + range sum queries
- Frequency counting
- Order statistics
- When segment tree is overkill

### Time complexity
- Update: `O(log n)`
- Query: `O(log n)`
- Space: `O(n)`

### Edge cases
- 1-based vs 0-based indexing
- Large values (use long)
- Range queries (sum of range)

### Java code
```java
public class FenwickTree {
    int[] bit;
    int n;

    FenwickTree(int n) {
        this.n = n;
        this.bit = new int[n + 1];
    }

    FenwickTree(int[] arr) {
        this(arr.length);
        for (int i = 0; i < arr.length; i++) {
            add(i, arr[i]);
        }
    }

    // Point update: add delta to index
    void add(int idx, int delta) {
        idx++; // Convert to 1-based
        while (idx <= n) {
            bit[idx] += delta;
            idx += idx & -idx;
        }
    }

    // Prefix sum: sum of [0, idx]
    int sumPrefix(int idx) {
        idx++; // Convert to 1-based
        int ans = 0;
        while (idx > 0) {
            ans += bit[idx];
            idx -= idx & -idx;
        }
        return ans;
    }

    // Range sum: sum of [l, r]
    int rangeSum(int l, int r) {
        if (l < 0) l = 0;
        if (r >= n) r = n - 1;
        if (l > r) return 0;
        return sumPrefix(r) - (l == 0 ? 0 : sumPrefix(l - 1));
    }

    // Find index with given prefix sum (lower bound)
    int lowerBound(int target) {
        int idx = 0;
        for (int i = 31 - Integer.numberOfLeadingZeros(n); i >= 0; i--) {
            int next = idx + (1 << i);
            if (next <= n && bit[next] < target) {
                target -= bit[next];
                idx = next;
            }
        }
        return idx;
    }
}
```

---

## 4. Segment Tree (range sum query + point update)

### What it does
Tree structure for range queries and point/segment updates.

### Why it matters
- Flexible range queries (min, max, sum, gcd, etc.)
- Range updates with lazy propagation
- Used in complex range query problems
- More powerful than Fenwick tree

### Intuition
Divide array into segments, each node stores information about its range. Queries combine information from relevant nodes.

### When to use
- Range queries with updates
- Complex operations (min, max, gcd)
- Range updates (with lazy propagation)
- When Fenwick tree is insufficient

### Time complexity
- Build: `O(n)`
- Point update: `O(log n)`
- Range query: `O(log n)`
- Space: `O(4n)`

### Edge cases
- Large arrays (memory)
- Complex operations
- Lazy propagation for range updates

### Java code
```java
public class SegmentTree {
    int[] tree;
    int n;

    SegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];
        build(arr, 0, n - 1, 1);
    }

    private void build(int[] arr, int start, int end, int node) {
        if (start == end) {
            tree[node] = arr[start];
            return;
        }
        int mid = (start + end) / 2;
        build(arr, start, mid, 2 * node);
        build(arr, mid + 1, end, 2 * node + 1);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    // Point update: set arr[idx] = value
    void update(int idx, int value) {
        update(0, n - 1, 1, idx, value);
    }

    private void update(int start, int end, int node, int idx, int value) {
        if (start == end) {
            tree[node] = value;
            return;
        }
        int mid = (start + end) / 2;
        if (idx <= mid) {
            update(start, mid, 2 * node, idx, value);
        } else {
            update(mid + 1, end, 2 * node + 1, idx, value);
        }
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    // Range sum query
    int query(int l, int r) {
        return query(0, n - 1, 1, l, r);
    }

    private int query(int start, int end, int node, int l, int r) {
        if (r < start || end < l) return 0; // No overlap
        if (l <= start && end <= r) return tree[node]; // Complete overlap
        int mid = (start + end) / 2;
        return query(start, mid, 2 * node, l, r) + 
               query(mid + 1, end, 2 * node + 1, l, r);
    }

    // For range minimum query
    static class SegmentTreeMin {
        int[] tree;
        int n;

        SegmentTreeMin(int[] arr) {
            n = arr.length;
            tree = new int[4 * n];
            buildMin(arr, 0, n - 1, 1);
        }

        private void buildMin(int[] arr, int start, int end, int node) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = (start + end) / 2;
            buildMin(arr, start, mid, 2 * node);
            buildMin(arr, mid + 1, end, 2 * node + 1);
            tree[node] = Math.min(tree[2 * node], tree[2 * node + 1]);
        }

        int queryMin(int l, int r) {
            return queryMin(0, n - 1, 1, l, r);
        }

        private int queryMin(int start, int end, int node, int l, int r) {
            if (r < start || end < l) return Integer.MAX_VALUE;
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return Math.min(queryMin(start, mid, 2 * node, l, r),
                              queryMin(mid + 1, end, 2 * node + 1, l, r));
        }
    }
}
```
    int n;

    SegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];
        build(1, 0, n - 1, arr);
    }

    void build(int node, int l, int r, int[] arr) {
        if (l == r) {
            tree[node] = arr[l];
            return;
        }
        int mid = l + (r - l) / 2;
        build(2 * node, l, mid, arr);
        build(2 * node + 1, mid + 1, r, arr);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    int query(int ql, int qr) {
        return query(1, 0, n - 1, ql, qr);
    }

    int query(int node, int l, int r, int ql, int qr) {
        if (qr < l || r < ql) return 0;
        if (ql <= l && r <= qr) return tree[node];
        int mid = l + (r - l) / 2;
        return query(2 * node, l, mid, ql, qr) +
               query(2 * node + 1, mid + 1, r, ql, qr);
    }

    void update(int idx, int val) {
        update(1, 0, n - 1, idx, val);
    }

    void update(int node, int l, int r, int idx, int val) {
        if (l == r) {
            tree[node] = val;
            return;
        }
        int mid = l + (r - l) / 2;
        if (idx <= mid) update(2 * node, l, mid, idx, val);
        else update(2 * node + 1, mid + 1, r, idx, val);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }
}
```

