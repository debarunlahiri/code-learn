import java.util.*;

/**
 * P115. Word Break. This is a mid-to-hard Java DSA coding problem commonly seen
 * in service based
 * company technical rounds. Implement the required method using an efficient
 * algorithm, not brute
 * force where a better standard approach exists. The solution should handle
 * boundary cases, duplicate
 * values, disconnected states, and large inputs according to the method
 * signature. Return the final
 * computed value or data structure exactly as the platform-style method
 * expects.
 */
public final class P115WordBreak {

    private P115WordBreak() {
    }

    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i; j++)
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
        return dp[s.length()];
    }
}
