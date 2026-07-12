import java.util.*;

/**
 * P054. Next Greater Element. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P054NextGreaterElement {

    private P054NextGreaterElement() {
    }

    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> next = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int n : nums2) {
    while (!stack.isEmpty() && n > stack.peek())
        next.put(stack.pop(), n);
    stack.push(n);
        }
        int[] ans = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++)
    ans[i] = next.getOrDefault(nums1[i], -1);
        return ans;
    }

}
