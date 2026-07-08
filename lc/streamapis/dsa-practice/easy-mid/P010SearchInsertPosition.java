import java.util.*;

/**
 * P010. Search Insert Position. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P010SearchInsertPosition {

    private P010SearchInsertPosition() {
    }

    public int searchInsert(int[] nums, int target) {
        int left = 0, right = nums.length;
        while (left < right) {
    int mid = left + (right - left) / 2;
    if (nums[mid] < target)
        left = mid + 1;
    else
        right = mid;
        }
        return left;
    }

}
