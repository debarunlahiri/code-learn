# Algorithms: Range Query and Advanced Data Structures

## 1. Prefix Sum (static range sum)
```java
public class PrefixSum {
    static int[] build(int[] arr) {
        int[] pre = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            pre[i + 1] = pre[i] + arr[i];
        }
        return pre;
    }

    static int rangeSum(int[] pre, int l, int r) {
        return pre[r + 1] - pre[l];
    }
}
```

## 2. Difference Array (range increment updates)
```java
public class DifferenceArray {
    static int[] applyUpdates(int n, int[][] updates) {
        int[] diff = new int[n + 1];
        for (int[] u : updates) {
            int l = u[0], r = u[1], val = u[2];
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

## 3. Fenwick Tree (BIT)
```java
public class FenwickTree {
    int[] bit;
    int n;

    FenwickTree(int n) {
        this.n = n;
        this.bit = new int[n + 1];
    }

    void add(int idx, int delta) {
        idx++;
        while (idx <= n) {
            bit[idx] += delta;
            idx += idx & -idx;
        }
    }

    int sumPrefix(int idx) {
        idx++;
        int ans = 0;
        while (idx > 0) {
            ans += bit[idx];
            idx -= idx & -idx;
        }
        return ans;
    }

    int rangeSum(int l, int r) {
        return sumPrefix(r) - (l == 0 ? 0 : sumPrefix(l - 1));
    }
}
```

## 4. Segment Tree (range sum query + point update)
```java
public class SegmentTree {
    int[] tree;
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

