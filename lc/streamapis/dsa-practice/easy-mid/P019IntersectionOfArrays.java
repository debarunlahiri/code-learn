import java.util.*;

/**
 * P019. Intersection Of Arrays. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P019IntersectionOfArrays {

    private P019IntersectionOfArrays() {
    }

    public int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>(), ans = new HashSet<>();
        for (int n : nums1)
    set.add(n);
        for (int n : nums2)
    if (set.contains(n))
        ans.add(n);
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }

}
