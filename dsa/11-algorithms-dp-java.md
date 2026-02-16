# Algorithms: Dynamic Programming (Easy to Hard)

## 1. Climbing Stairs
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

## 2. House Robber
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

## 3. 0/1 Knapsack
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

## 4. Longest Common Subsequence (LCS)
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

## 5. Coin Change (minimum coins)
```java
import java.util.Arrays;

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

