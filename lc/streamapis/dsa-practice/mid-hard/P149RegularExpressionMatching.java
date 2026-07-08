import java.util.*;

/**
 * P149. Regular Expression Matching. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the input represented by the
 * method parameters, apply
 * the standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P149RegularExpressionMatching {

    private P149RegularExpressionMatching() {
    }

    public boolean isMatch(String s, String p) {
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        dp[0][0] = true;
        for (int j = 2; j <= p.length(); j++)
            if (p.charAt(j - 1) == '*')
                dp[0][j] = dp[0][j - 2];
        for (int i = 1; i <= s.length(); i++)
            for (int j = 1; j <= p.length(); j++) {
                char pc = p.charAt(j - 1);
                if (pc == '.' || pc == s.charAt(i - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else if (pc == '*')
                    dp[i][j] = dp[i][j - 2]
                            || ((p.charAt(j - 2) == '.' || p.charAt(j - 2) == s.charAt(i - 1)) && dp[i - 1][j]);
            }
        return dp[s.length()][p.length()];
    }
}
