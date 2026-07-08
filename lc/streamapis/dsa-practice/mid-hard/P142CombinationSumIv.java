import java.util.*;

/**
 * P142. Combination Sum IV. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the input represented by the method
 * parameters, apply the
 * standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P142CombinationSumIv {

    private P142CombinationSumIv() {
    }

    public int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int t = 1; t <= target; t++)
            for (int n : nums)
                if (t >= n)
                    dp[t] += dp[t - n];
        return dp[target];
    }
}
