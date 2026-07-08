import java.util.*;

/**
 * P005. Maximum Subarray. Given the input described by the method signature, implement the
 * required operation efficiently and return the expected result. Handle normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding rounds, and keep the
 * implementation readable for revision.
 */
public final class P005MaximumSubarray {

    private P005MaximumSubarray() {
    }

    public int maxSubArray(int[] nums) {
        int current = nums[0], best = nums[0];
        for (int i = 1; i < nums.length; i++) {
            current = Math.max(nums[i], current + nums[i]);
            best = Math.max(best, current);
        }
        return best;
    }

}
