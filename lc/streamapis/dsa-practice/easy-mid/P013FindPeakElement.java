import java.util.*;

/**
 * P013. Find Peak Element. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P013FindPeakElement {

    private P013FindPeakElement() {
    }

    public int findPeakElement(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
    int mid = left + (right - left) / 2;
    if (nums[mid] < nums[mid + 1])
        left = mid + 1;
    else
        right = mid;
        }
        return left;
    }

}
