# Algorithms: Dynamic Programming (Easy to Hard)

Goal: Understand DP patterns, intuition, and step-by-step Java solutions.

---

## 1. Climbing Stairs

### What it does
Count ways to reach the top of N stairs, taking 1 or 2 steps at a time.

### Why it matters
- Classic DP introduction
- Fibonacci-like recurrence
- Teaches optimal substructure and overlapping subproblems

### Intuition
To reach step N, you either came from step N-1 (took 1 step) or step N-2 (took 2 steps). So ways(N) = ways(N-1) + ways(N-2).

### When to use
- Counting ways with small step choices
- Problems with Fibonacci-style recurrence
- Linear DP with O(1) space possible

### Time complexity
- Time: `O(n)`
- Space: `O(1)` (optimized from `O(n)`)

### Edge cases
- N = 0 or 1 (base cases)
- Very large N (use modulo if required)

### Java code
```java
public class ClimbingStairs {
    static int climbStairs(int n) {
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

---

## 2. House Robber

### What it does
Maximum money you can rob from houses in a line without robbing adjacent houses.

### Why it matters
- Classic interval selection problem
- Demonstrates DP with two choices at each step
- Foundation for circular and tree variants

### Intuition
At each house, you have two choices:
1. Rob this house: add its value + max from houses before previous
2. Skip this house: take max from previous house
Pick the better choice.

### When to use
- Problems with adjacency constraints
- Resource allocation with conflicts
- Weighted interval scheduling basics

### Time complexity
- Time: `O(n)`
- Space: `O(1)`

### Edge cases
- Empty array
- Single house
- All houses have same value

### Java code
```java
public class HouseRobber {
    static int rob(int[] nums) {
        int prev2 = 0, prev1 = 0;
        for (int num : nums) {
            int take = prev2 + num;
            int skip = prev1;
            int curr = Math.max(take, skip);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

---

## 3. 0/1 Knapsack

### What it does
Select items with given weights and values to maximize total value without exceeding capacity.

### Why it matters
- Fundamental DP problem
- Teaches 2D DP and state transitions
- Basis for many optimization problems

### Intuition
For each item, decide to include or exclude. Build a table where dp[i][w] = max value using first i items with weight limit w.

### When to use
- Resource allocation with weight constraints
- Subset selection problems
- Budget/capacity optimization

### Time complexity
- Time: `O(n * W)` where n = items, W = capacity
- Space: `O(n * W)` (can be optimized to `O(W)`)

### Edge cases
- Zero capacity
- All items too heavy
- Multiple optimal solutions

### Java code
```java
public class Knapsack01 {
    static int knapsack(int[] wt, int[] val, int W) {
        int n = wt.length;
        int[][] dp = new int[n + 1][W + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i - 1][w];
                if (wt[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i][w], val[i - 1] + dp[i - 1][w - wt[i - 1]]);
                }
            }
        }
        return dp[n][W];
    }
}
```

---

## 4. Longest Common Subsequence (LCS)

### What it does
Find longest sequence that appears in both strings in order (not necessarily contiguous).

### Why it matters
- Classic string DP problem
- Used in version control, DNA analysis
- Teaches 2D DP with string indices

### Intuition
If characters match, they contribute to LCS. If not, skip one character from either string and take the better result.

### When to use
- Sequence similarity
- Diff algorithms
- Pattern matching with gaps

### Time complexity
- Time: `O(n * m)`
- Space: `O(n * m)` (can be optimized to `O(min(n, m))`)

### Edge cases
- Empty strings
- Identical strings
- No common characters

### Java code
```java
public class LCS {
    static int lcs(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }
}
```

---

## 5. Coin Change (minimum coins)

### What it does
Find minimum number of coins to make a given amount using given coin denominations.

### Why it matters
- Practical optimization problem
- Teaches unbounded knapsack variant
- Used in vending machines, currency exchange

### Intuition
For each amount, try all coins and take the minimum of (1 + minCoins(amount - coin)).

### When to use
- Change-making problems
- Resource optimization with unlimited supply
- Pathfinding in weighted graphs (simplified)

### Time complexity
- Time: `O(n * amount)` where n = number of coin types
- Space: `O(amount)`

### Edge cases
- Amount = 0
- No solution possible
- Large amounts (use greedy if coins are canonical)

### Java code
```java
import java.util.Arrays;

public class CoinChange {
    static int minCoins(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i && dp[i - coin] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], 1 + dp[i - coin]);
                }
            }
        }
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }
}
```

public class CoinChange {
    static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], 1 + dp[i - coin]);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
```

## 6. Longest Increasing Subsequence (O(n log n))
```java
import java.util.ArrayList;
import java.util.List;

public class LISBinarySearch {
    static int lengthOfLIS(int[] nums) {
        List<Integer> tails = new ArrayList<>();
        for (int x : nums) {
            int l = 0, r = tails.size();
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (tails.get(mid) < x) l = mid + 1;
                else r = mid;
            }
            if (l == tails.size()) tails.add(x);
            else tails.set(l, x);
        }
        return tails.size();
    }
}
```

