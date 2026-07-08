import java.util.*;

/**
 * P101. Maximum Product Subarray. This is a mid-to-hard Java DSA coding problem
 * commonly seen in
 * service based company technical rounds. Implement the required method using
 * an efficient algorithm,
 * not brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P101MaximumProductSubarray {

    private P101MaximumProductSubarray() {
    }

    public int maxProduct(int[] nums) {
        int max = nums[0], min = nums[0], best = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < 0) {
                int t = max;
                max = min;
                min = t;
            }
            max = Math.max(nums[i], max * nums[i]);
            min = Math.min(nums[i], min * nums[i]);
            best = Math.max(best, max);
        }
        return best;
    }
}
