# Algorithms: Bit Manipulation (Easy to Hard)

## 1. Check Odd or Even
```java
public class OddEvenBit {
    static boolean isOdd(int x) {
        return (x & 1) == 1;
    }
}
```

## 2. Count Set Bits (Brian Kernighan)
```java
public class CountSetBits {
    static int count(int x) {
        int c = 0;
        while (x != 0) {
            x = x & (x - 1);
            c++;
        }
        return c;
    }
}
```

## 3. Find Single Number (others appear twice)
```java
public class SingleNumber {
    static int single(int[] nums) {
        int ans = 0;
        for (int x : nums) ans ^= x;
        return ans;
    }
}
```

## 4. Power of Two
```java
public class PowerOfTwo {
    static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
}
```

## 5. Subsets using Bitmask
```java
import java.util.ArrayList;
import java.util.List;

public class SubsetsBitmask {
    static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        int n = nums.length;
        int total = 1 << n;

        for (int mask = 0; mask < total; mask++) {
            List<Integer> cur = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) cur.add(nums[i]);
            }
            ans.add(cur);
        }
        return ans;
    }
}
```

## 6. Maximum XOR Pair (Trie)
```java
public class MaxXorPair {
    static class Node {
        Node[] child = new Node[2];
    }

    static void insert(Node root, int num) {
        Node cur = root;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (cur.child[bit] == null) cur.child[bit] = new Node();
            cur = cur.child[bit];
        }
    }

    static int query(Node root, int num) {
        Node cur = root;
        int best = 0;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int want = 1 - bit;
            if (cur.child[want] != null) {
                best |= (1 << i);
                cur = cur.child[want];
            } else {
                cur = cur.child[bit];
            }
        }
        return best;
    }
}
```

