import java.util.*;

/**
 * P006. Move Zeroes. This is a easy-to-mid Java DSA coding problem commonly practiced for service
 * based company coding rounds. Given the input described by the method signature, implement the
 * required operation efficiently and return the expected result. Handle normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding rounds, and keep the
 * implementation readable for revision.
 */
public final class P006MoveZeroes {

    private P006MoveZeroes() {
    }

    public void moveZeroes(int[] nums) {
        int write = 0;
        for (int n : nums)
            if (n != 0)
                nums[write++] = n;
        while (write < nums.length)
            nums[write++] = 0;
    }

}
