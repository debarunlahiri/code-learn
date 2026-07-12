import java.util.*;

/**
 * P007. Remove Duplicates Sorted Array. This is a easy-to-mid Java DSA coding
 * problem commonly
 * practiced for service based company coding rounds. Given the input described
 * by the method
 * signature, implement the required operation efficiently and return the
 * expected result. Handle
 * normal edge cases such as empty collections, duplicate values, boundary
 * indexes, and null child
 * pointers when the data structure allows them. Prefer the standard optimal
 * approach used in coding
 * rounds, and keep the implementation readable for revision.
 */
public final class P007RemoveDuplicatesSortedArray {

    private P007RemoveDuplicatesSortedArray() {
    }

    public int removeDuplicates(int[] nums) {
        if (nums.length == 0)
            return 0;
        int write = 1;
        for (int read = 1; read < nums.length; read++) {
            if (nums[read] != nums[read - 1])
                nums[write++] = nums[read];
        }
        return write;
    }

}
