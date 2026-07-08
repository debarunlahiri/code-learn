import java.util.*;

/**
 * P025. House Robber. This is a easy-to-mid Java DSA coding problem commonly
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
public final class P025HouseRobber {

    private P025HouseRobber() {
    }

    public int rob(int[] nums) {
        int prev2 = 0, prev1 = 0;
        for (int n : nums) {
    int cur = Math.max(prev1, prev2 + n);
    prev2 = prev1;
    prev1 = cur;
        }
        return prev1;
    }

}
