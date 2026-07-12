import java.util.*;

/**
 * P152. Distinct Subsequences. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the input represented by the method
 * parameters, apply the
 * standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P152DistinctSubsequences {

    private P152DistinctSubsequences() {
    }

    public int numDistinct(String s, String t) {
        long[] dp = new long[t.length() + 1];
        dp[0] = 1;
        for (char sc : s.toCharArray())
            for (int j = t.length() - 1; j >= 0; j--)
                if (sc == t.charAt(j))
                    dp[j + 1] += dp[j];
        return (int) dp[t.length()];
    }
}
