import java.util.*;

/**
 * P143. Partition Equal Subset Sum. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the input represented by the
 * method parameters, apply
 * the standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P143PartitionEqualSubsetSum {

    private P143PartitionEqualSubsetSum() {
    }

    public boolean canPartition(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        if ((sum & 1) == 1)
            return false;
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int n : nums)
            for (int t = target; t >= n; t--)
                dp[t] |= dp[t - n];
        return dp[target];
    }
}
