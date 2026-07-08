import java.util.*;

/**
 * P096. Jump Game Ii. This is a easy-to-mid Java DSA coding problem commonly
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
public final class P096JumpGameIi {

    private P096JumpGameIi() {
    }

    public int jump(int[] nums) {
        int jumps = 0, end = 0, far = 0;
        for (int i = 0; i < nums.length - 1; i++) {
    far = Math.max(far, i + nums[i]);
    if (i == end) {
        jumps++;
        end = far;
    }
        }
        return jumps;
    }

}
