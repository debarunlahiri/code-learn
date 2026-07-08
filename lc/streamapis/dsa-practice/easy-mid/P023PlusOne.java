import java.util.*;

/**
 * P023. Plus One. This is a easy-to-mid Java DSA coding problem commonly
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
public final class P023PlusOne {

    private P023PlusOne() {
    }

    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
    if (digits[i] < 9) {
        digits[i]++;
        return digits;
    }
    digits[i] = 0;
        }
        int[] ans = new int[digits.length + 1];
        ans[0] = 1;
        return ans;
    }

}
