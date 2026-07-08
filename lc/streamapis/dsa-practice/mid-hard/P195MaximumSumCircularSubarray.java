import java.util.*;

/**
 * P195. Maximum Sum Circular Subarray. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P195MaximumSumCircularSubarray {

    private P195MaximumSumCircularSubarray() {
    }

    public int maxSubarraySumCircular(int[] nums) {
        int total = 0, max = nums[0], curMax = 0, min = nums[0], curMin = 0;
        for (int n : nums) {
            curMax = Math.max(n, curMax + n);
            max = Math.max(max, curMax);
            curMin = Math.min(n, curMin + n);
            min = Math.min(min, curMin);
            total += n;
        }
        return max < 0 ? max : Math.max(max, total - min);
    }
}
