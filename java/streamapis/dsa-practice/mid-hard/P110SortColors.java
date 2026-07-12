import java.util.*;

/**
 * P110. Sort Colors. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Implement the required method using an efficient
 * algorithm, not brute
 * force where a better standard approach exists. The solution should handle
 * boundary cases, duplicate
 * values, disconnected states, and large inputs according to the method
 * signature. Return the final
 * computed value or data structure exactly as the platform-style method
 * expects.
 */
public final class P110SortColors {

    private P110SortColors() {
    }

    public void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        while (mid <= high) {
            if (nums[mid] == 0)
                swap(nums, low++, mid++);
            else if (nums[mid] == 1)
                mid++;
            else
                swap(nums, mid, high--);
        }
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
