import java.util.*;

/**
 * P146. Palindrome Partitioning Minimum Cuts. This is a mid-to-hard Java DSA
 * coding problem commonly
 * seen in service based company technical rounds. Read the input represented by
 * the method parameters,
 * apply the standard efficient approach for this topic, and return the exact
 * result requested. Handle
 * empty inputs, duplicate values, boundary indexes, and large constraints in a
 * clean Java
 * implementation.
 */
public final class P146PalindromePartitioningMinimumCuts {

    private P146PalindromePartitioningMinimumCuts() {
    }

    public int minCut(String s) {
        int n = s.length();
        boolean[][] pal = new boolean[n][n];
        int[] dp = new int[n];
        for (int i = 0; i < n; i++)
            dp[i] = i;
        for (int end = 0; end < n; end++)
            for (int start = 0; start <= end; start++)
                if (s.charAt(start) == s.charAt(end) && (end - start < 2 || pal[start + 1][end - 1])) {
                    pal[start][end] = true;
                    dp[end] = start == 0 ? 0 : Math.min(dp[end], dp[start - 1] + 1);
                }
        return n == 0 ? 0 : dp[n - 1];
    }
}
