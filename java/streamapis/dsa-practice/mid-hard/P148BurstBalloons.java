import java.util.*;

/**
 * P148. Burst Balloons. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the input represented by the method
 * parameters, apply the standard
 * efficient approach for this topic, and return the exact result requested.
 * Handle empty inputs,
 * duplicate values, boundary indexes, and large constraints in a clean Java
 * implementation.
 */
public final class P148BurstBalloons {

    private P148BurstBalloons() {
    }

    public int maxCoins(int[] nums) {
        int n = nums.length;
        int[] a = new int[n + 2];
        a[0] = a[n + 1] = 1;
        System.arraycopy(nums, 0, a, 1, n);
        int[][] dp = new int[n + 2][n + 2];
        for (int len = 1; len <= n; len++)
            for (int l = 1; l + len - 1 <= n; l++) {
                int r = l + len - 1;
                for (int k = l; k <= r; k++)
                    dp[l][r] = Math.max(dp[l][r], dp[l][k - 1] + a[l - 1] * a[k] * a[r + 1] + dp[k + 1][r]);
            }
        return dp[1][n];
    }
}
