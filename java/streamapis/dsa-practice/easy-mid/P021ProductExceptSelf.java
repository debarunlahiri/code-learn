import java.util.*;

/**
 * P021. Product Except Self. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P021ProductExceptSelf {

    private P021ProductExceptSelf() {
    }

    public int[] productExceptSelf(int[] nums) {
        int[] ans = new int[nums.length];
        int prefix = 1;
        for (int i = 0; i < nums.length; i++) {
    ans[i] = prefix;
    prefix *= nums[i];
        }
        int suffix = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
    ans[i] *= suffix;
    suffix *= nums[i];
        }
        return ans;
    }

}
