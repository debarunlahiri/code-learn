import java.util.*;

/**
 * P017. Contains Duplicate. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P017ContainsDuplicate {

    private P017ContainsDuplicate() {
    }

    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int n : nums)
    if (!seen.add(n))
        return true;
        return false;
    }

}
