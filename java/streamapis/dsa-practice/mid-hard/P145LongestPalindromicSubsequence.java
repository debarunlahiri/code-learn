import java.util.*;

/**
 * P145. Longest Palindromic Subsequence. This is a mid-to-hard Java DSA coding
 * problem commonly seen
 * in service based company technical rounds. Read the input represented by the
 * method parameters,
 * apply the standard efficient approach for this topic, and return the exact
 * result requested. Handle
 * empty inputs, duplicate values, boundary indexes, and large constraints in a
 * clean Java
 * implementation.
 */
public final class P145LongestPalindromicSubsequence {

    private P145LongestPalindromicSubsequence() {
    }

    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++)
                dp[i][j] = s.charAt(i) == s.charAt(j) ? 2 + dp[i + 1][j - 1] : Math.max(dp[i + 1][j], dp[i][j - 1]);
        }
        return n == 0 ? 0 : dp[0][n - 1];
    }
}
