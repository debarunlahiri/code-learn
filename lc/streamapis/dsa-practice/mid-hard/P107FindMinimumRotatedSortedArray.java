import java.util.*;

/**
 * P107. Find Minimum In Rotated Sorted Array. This is a mid-to-hard Java DSA
 * coding problem commonly
 * seen in service based company technical rounds. Implement the required method
 * using an efficient
 * algorithm, not brute force where a better standard approach exists. The
 * solution should handle
 * boundary cases, duplicate values, disconnected states, and large inputs
 * according to the method
 * signature. Return the final computed value or data structure exactly as the
 * platform-style method
 * expects.
 */
public final class P107FindMinimumRotatedSortedArray {

    private P107FindMinimumRotatedSortedArray() {
    }

    public int findMin(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] > nums[r])
                l = m + 1;
            else
                r = m;
        }
        return nums[l];
    }
}
