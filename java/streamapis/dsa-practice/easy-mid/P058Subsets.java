import java.util.*;

/**
 * P058. Subsets. This is a easy-to-mid Java DSA coding problem commonly
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
public final class P058Subsets {

    private P058Subsets() {
    }

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        for (int mask = 0; mask < (1 << nums.length); mask++) {
    List<Integer> cur = new ArrayList<>();
    for (int i = 0; i < nums.length; i++)
        if ((mask & (1 << i)) != 0)
            cur.add(nums[i]);
    ans.add(cur);
        }
        return ans;
    }

}
