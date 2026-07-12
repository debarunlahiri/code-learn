import java.util.*;

/**
 * P030. Edit Distance.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P030EditDistance {

    private P030EditDistance() {
    }

    public int minDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++)
    dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++)
    dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++)
    for (int j = 1; j <= b.length(); j++)
        dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1]
                : 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
        return dp[a.length()][b.length()];
    }

}
