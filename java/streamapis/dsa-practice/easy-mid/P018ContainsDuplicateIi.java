import java.util.*;

/**
 * P018. Contains Duplicate Ii. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P018ContainsDuplicateIi {

    private P018ContainsDuplicateIi() {
    }

    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> last = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
    if (last.containsKey(nums[i]) && i - last.get(nums[i]) <= k)
        return true;
    last.put(nums[i], i);
        }
        return false;
    }

}
