import java.util.*;

/**
 * P144. Target Sum Ways. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the input represented by the method
 * parameters, apply the standard
 * efficient approach for this topic, and return the exact result requested.
 * Handle empty inputs,
 * duplicate values, boundary indexes, and large constraints in a clean Java
 * implementation.
 */
public final class P144TargetSumWays {

    private P144TargetSumWays() {
    }

    public int findTargetSumWays(int[] nums, int target) {
        Map<Integer, Integer> dp = new HashMap<>();
        dp.put(0, 1);
        for (int n : nums) {
            Map<Integer, Integer> next = new HashMap<>();
            for (var e : dp.entrySet()) {
                next.merge(e.getKey() + n, e.getValue(), Integer::sum);
                next.merge(e.getKey() - n, e.getValue(), Integer::sum);
            }
            dp = next;
        }
        return dp.getOrDefault(target, 0);
    }
}
