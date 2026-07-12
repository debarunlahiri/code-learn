import java.util.*;

/**
 * P102. Subarray Sum Equals K. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Implement the required method using an
 * efficient algorithm, not
 * brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P102SubarraySumEqualsK {

    private P102SubarraySumEqualsK() {
    }

    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        count.put(0, 1);
        int prefix = 0, ans = 0;
        for (int n : nums) {
            prefix += n;
            ans += count.getOrDefault(prefix - k, 0);
            count.merge(prefix, 1, Integer::sum);
        }
        return ans;
    }
}
