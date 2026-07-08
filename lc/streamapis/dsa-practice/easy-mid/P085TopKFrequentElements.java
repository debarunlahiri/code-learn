import java.util.*;

/**
 * P085. Top K Frequent Elements. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P085TopKFrequentElements {

    private P085TopKFrequentElements() {
    }

    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int n : nums)
    count.merge(n, 1, Integer::sum);
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(count::get));
        for (int n : count.keySet()) {
    pq.offer(n);
    if (pq.size() > k)
        pq.poll();
        }
        int[] ans = new int[k];
        for (int i = k - 1; i >= 0; i--)
    ans[i] = pq.poll();
        return ans;
    }

}
