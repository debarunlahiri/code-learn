import java.util.*;

/**
 * P016. Majority Element. This is a easy-to-mid Java DSA coding problem
 * commonly practiced for service
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
public final class P016MajorityElement {

    private P016MajorityElement() {
    }

    public int majorityElement(int[] nums) {
        int candidate = 0, count = 0;
        for (int n : nums) {
    if (count == 0)
        candidate = n;
    count += (n == candidate) ? 1 : -1;
        }
        return candidate;
    }

}
