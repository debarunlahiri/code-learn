import java.util.*;

/**
 * P009. Binary Search. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service
 * based company coding rounds. Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P009BinarySearch {

    private P009BinarySearch() {
    }

    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
    int mid = left + (right - left) / 2;
    if (nums[mid] == target)
        return mid;
    if (nums[mid] < target)
        left = mid + 1;
    else
        right = mid - 1;
        }
        return -1;
    }

}
