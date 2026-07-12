import java.util.*;

/**
 * P026. Min Cost Climbing Stairs. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P026MinCostClimbingStairs {

    private P026MinCostClimbingStairs() {
    }

    public int minCostClimbingStairs(int[] cost) {
        int a = 0, b = 0;
        for (int c : cost) {
    int next = Math.min(a, b) + c;
    a = b;
    b = next;
        }
        return Math.min(a, b);
    }

}
