import java.util.*;

/**
 * P150. Wildcard Matching. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the input represented by the method
 * parameters, apply the
 * standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P150WildcardMatching {

    private P150WildcardMatching() {
    }

    public boolean isMatch(String s, String p) {
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        dp[0][0] = true;
        for (int j = 1; j <= p.length(); j++)
            if (p.charAt(j - 1) == '*')
                dp[0][j] = dp[0][j - 1];
        for (int i = 1; i <= s.length(); i++)
            for (int j = 1; j <= p.length(); j++) {
                char pc = p.charAt(j - 1);
                dp[i][j] = pc == '*' ? dp[i][j - 1] || dp[i - 1][j]
                        : (pc == '?' || pc == s.charAt(i - 1)) && dp[i - 1][j - 1];
            }
        return dp[s.length()][p.length()];
    }
}
