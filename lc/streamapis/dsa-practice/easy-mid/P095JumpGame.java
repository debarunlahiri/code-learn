import java.util.*;

/**
 * P095. Jump Game. This is a easy-to-mid Java DSA coding problem commonly practiced for service based
 * company coding rounds. Given the input described by the method signature, implement the required
 * operation efficiently and return the expected result. Handle normal edge cases such as empty
 * collections, duplicate values, boundary indexes, and null child pointers when the data structure
 * allows them. Prefer the standard optimal approach used in coding rounds, and keep the implementation
 * readable for revision.
 */
public final class P095JumpGame {

    private P095JumpGame() {
    }

    public boolean canJump(int[] nums) {
        int far = 0;
        for (int i = 0; i < nums.length && i <= far; i++)
            far = Math.max(far, i + nums[i]);
        return far >= nums.length - 1;
    }

}
