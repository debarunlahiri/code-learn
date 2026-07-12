import java.util.*;

/**
 * P024. Climbing Stairs.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P024ClimbingStairs {

    private P024ClimbingStairs() {
    }

    public int climbStairs(int n) {
        int a = 1, b = 1;
        for (int i = 2; i <= n; i++) {
    int c = a + b;
    a = b;
    b = c;
        }
        return b;
    }

}
