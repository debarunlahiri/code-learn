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

---

## 5. 2D Prefix Sum (matrix range sum)

### What it does
Preprocess 2D matrix to answer rectangular range sum queries in O(1) time.

### Why it matters
- Fast range sum queries for static matrices
- Used in image processing, game development
- Foundation for 2D range query problems
- Essential for submatrix sum problems

### Intuition
Store cumulative sums in 2D. Rectangle sum [r1,c1] to [r2,c2] = prefix[r2][c2] - prefix[r1-1][c2] - prefix[r2][c1-1] + prefix[r1-1][c1-1]. Like a running total in both dimensions.

### When to use
- Static matrices (no updates)
- Multiple rectangular range sum queries
- Submatrix sum problems
- Image processing operations

### Time complexity
- Preprocessing: `O(m*n)`
- Query: `O(1)`
- Space: `O(m*n)`

### Edge cases
- Empty matrix
- Single element matrix
- Large sums (use long)
- Boundary rectangles

### Java code
```java
public class PrefixSum2D {
    static int[][] build(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return new int[1][1];
        int m = matrix.length, n = matrix[0].length;
        int[][] pre = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                pre[i][j] = pre[i - 1][j] + pre[i][j - 1] 
                          - pre[i - 1][j - 1] + matrix[i - 1][j - 1];
            }
        }
        return pre;
    }

    static int rangeSum(int[][] pre, int r1, int c1, int r2, int c2) {
        if (pre == null || pre.length <= 1) return 0;
        // Convert to 1-based indexing
        r1++; c1++; r2++; c2++;
        return pre[r2][c2] - pre[r1 - 1][c2] - pre[r2][c1 - 1] + pre[r1 - 1][c1 - 1];
    }
}
```

---

## 6. Sparse Table (range min/max query)

### What it does
Preprocess array for static range minimum/maximum queries in O(1) time.

### Why it matters
- Fast range min/max queries for static arrays
- Used in RMQ problems, LCA calculations
- Better than Segment Tree for static queries
- O(1) query time after O(n log n) preprocessing

### Intuition
Store precomputed min/max for ranges of size 2^k. Any range can be represented as union of two power-of-two ranges. Like having precomputed answers for all possible power-of-two lengths.

### When to use
- Static arrays (no updates)
- Multiple range min/max queries
- RMQ (Range Minimum Query) problems
- LCA (Lowest Common Ancestor) preprocessing

### Time complexity
- Preprocessing: `O(n log n)`
- Query: `O(1)`
- Space: `O(n log n)`

### Edge cases
- Empty array
- Single element range
- Large values (use appropriate type)

### Java code
```java
public class SparseTable {
    static int[][] buildMin(int[] arr) {
        if (arr == null || arr.length == 0) return new int[1][1];
        int n = arr.length;
        int log = (int) (Math.log(n) / Math.log(2)) + 1;
        int[][] st = new int[n][log];
        
        // Initialize for range size 1
        for (int i = 0; i < n; i++) st[i][0] = arr[i];
        
        // Build for powers of 2
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                st[i][j] = Math.min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }
        return st;
    }

    static int queryMin(int[][] st, int l, int r) {
        if (st == null || st.length <= 1) return Integer.MAX_VALUE;
        int j = (int) (Math.log(r - l + 1) / Math.log(2));
        return Math.min(st[l][j], st[r - (1 << j) + 1][j]);
    }
}
```

---

## 7. Mo's Algorithm (offline range queries)

### What it does
Answer multiple range queries offline in O((n+q)√n) time by processing queries in optimal order.

### Why it matters
- Handles range queries with updates
- Better than naive O(nq) approach
- Used in competitive programming
- Foundation for advanced range query techniques

### Intuition
Sort queries in a special order (block size, then right endpoint) and maintain current answer while moving pointers. Like sliding window but for multiple queries in optimal order.

### When to use
- Multiple range queries on static array
- No updates to array
- Query count is large
- O((n+q)√n) is acceptable

### Time complexity
- Sorting queries: `O(q log q)`
- Processing: `O((n+q)√n)`
- Space: `O(n)`

### Edge cases
- Empty array
- Single element queries
- Large query ranges

### Java code
```java
import java.util.*;

public class MoAlgorithm {
    static class Query {
        int l, r, idx, block;
        Query(int l, int r, int idx, int blockSize) {
            this.l = l; this.r = r; this.idx = idx;
            this.block = l / blockSize;
        }
    }

    static int[] answerQueries(int[] arr, Query[] queries) {
        int blockSize = (int) Math.sqrt(arr.length);
        
        // Sort queries
        Arrays.sort(queries, (a, b) -> {
            if (a.block != b.block) return a.block - b.block;
            return (a.block % 2 == 0) ? a.r - b.r : b.r - a.r;
        });

        int[] answers = new int[queries.length];
        int currL = 0, currR = -1, currSum = 0;

        for (Query q : queries) {
            // Expand to the right
            while (currR < q.r) currSum += arr[++currR];
            // Shrink from the right
            while (currR > q.r) currSum -= arr[currR--];
            // Expand to the left
            while (currL < q.l) currSum -= arr[currL++];
            // Shrink from the left
            while (currL > q.l) currSum += arr[--currL];
            
            answers[q.idx] = currSum;
        }
        return answers;
    }
}
```

---

## 8. Wavelet Tree (range queries on arrays)

### What it does
Advanced data structure for range queries on arrays with small value ranges.

### Why it matters
- Handles various range queries efficiently
- Used in advanced competitive programming
- Better than Segment Tree for certain problems
- Supports k-th order statistics

### Intuition
Recursively split array based on median, building a binary tree structure. Each level stores bitset indicating which elements go to right child. Like a binary search tree but for array positions.

### When to use
- Arrays with small value ranges
- Range k-th smallest queries
- Range counting queries
- When O(log n) per operation is acceptable

### Time complexity
- Build: `O(n log V)` where V is value range
- Query: `O(log V)`
- Space: `O(n log V)`

### Edge cases
- Empty array
- Large value ranges
- Single element arrays

### Java code
```java
public class WaveletTree {
    static class Node {
        int[] left;
        int[] right;
        Node leftChild, rightChild;
        int lo, hi;
        
        Node(int[] arr, int lo, int hi) {
            this.lo = lo; this.hi = hi;
            if (lo == hi || arr.length == 0) return;
            
            int mid = (lo + hi) / 2;
            left = new int[arr.length + 1];
            right = new int[arr.length + 1];
            
            int[] leftArr = new int[arr.length];
            int[] rightArr = new int[arr.length];
            int leftCount = 0, rightCount = 0;
            
            for (int i = 0; i < arr.length; i++) {
                left[i + 1] = left[i];
                right[i + 1] = right[i];
                
                if (arr[i] <= mid) {
                    left[i + 1]++;
                    leftArr[leftCount++] = arr[i];
                } else {
                    right[i + 1]++;
                    rightArr[rightCount++] = arr[i];
                }
            }
            
            leftChild = new Node(leftArr, lo, mid);
            rightChild = new Node(rightArr, mid + 1, hi);
        }
    }
}
```

---

## Practice Problems

### Easy
1. **Range Sum Query - Immutable** (LeetCode 303)
2. **Range Sum Query - Mutable** (LeetCode 307)
3. **Minimum Size Subarray Sum** (LeetCode 209)

### Medium
1. **Range Sum Query 2D - Immutable** (LeetCode 304)
2. **Falling Squares** (LeetCode 699)
3. **Range Maximum Query** (Segment Tree)

### Hard
1. **Range Sum Query 2D - Mutable** (LeetCode 308)
2. **Rectangle Area II** (LeetCode 850)
3. **Maximum Sum of 3 Non-Overlapping Subarrays** (LeetCode 689)

---

**Remember:** Choose the right data structure based on whether you need updates, query types, and time/space constraints.

