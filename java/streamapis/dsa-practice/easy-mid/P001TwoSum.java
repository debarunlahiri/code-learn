import java.util.*;

/**
 * P001. Two Sum. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service based
 * company coding rounds. Given the input described by the method signature,
 * implement the required
 * operation efficiently and return the expected result. Handle normal edge
 * cases such as empty
 * collections, duplicate values, boundary indexes, and null child pointers when
 * the data structure
 * allows them. Prefer the standard optimal approach used in coding rounds, and
 * keep the implementation
 * readable for revision.
 */
public final class P001TwoSum {

    private P001TwoSum() {
    }

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (map.containsKey(need))
                return new int[] { map.get(need), i };
            map.put(nums[i], i);
        }
        return new int[] { -1, -1 };
    }

}
