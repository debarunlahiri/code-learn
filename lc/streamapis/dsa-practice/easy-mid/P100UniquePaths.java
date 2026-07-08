import java.util.*;

/**
 * P100. Unique Paths.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P100UniquePaths {

    private P100UniquePaths() {
    }

    public int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int r = 1; r < m; r++)
    for (int c = 1; c < n; c++)
        dp[c] += dp[c - 1];
        return dp[n - 1];
    }

}
