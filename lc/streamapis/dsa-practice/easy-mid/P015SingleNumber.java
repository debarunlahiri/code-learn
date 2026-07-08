import java.util.*;

/**
 * P015. Single Number.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P015SingleNumber {

    private P015SingleNumber() {
    }

    public int singleNumber(int[] nums) {
        int ans = 0;
        for (int n : nums)
    ans ^= n;
        return ans;
    }

}
