import java.util.*;

/**
 * P084. Kth Largest Element. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P084KthLargestElement {

    private P084KthLargestElement() {
    }

    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int n : nums) {
    pq.offer(n);
    if (pq.size() > k)
        pq.poll();
        }
        return pq.peek();
    }

}
