import java.util.*;

/**
 * P151. Minimum Insertions To Make Palindrome. This is a mid-to-hard Java DSA
 * coding problem commonly
 * seen in service based company technical rounds. Read the input represented by
 * the method parameters,
 * apply the standard efficient approach for this topic, and return the exact
 * result requested. Handle
 * empty inputs, duplicate values, boundary indexes, and large constraints in a
 * clean Java
 * implementation.
 */
public final class P151MinimumInsertionsToMakePalindrome {

    private P151MinimumInsertionsToMakePalindrome() {
    }

    public int minInsertions(String s) {
        String r = new StringBuilder(s).reverse().toString();
        int n = s.length();
        int[][] dp = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = s.charAt(i - 1) == r.charAt(j - 1) ? 1 + dp[i - 1][j - 1]
                        : Math.max(dp[i - 1][j], dp[i][j - 1]);
        return n - dp[n][n];
    }
}
