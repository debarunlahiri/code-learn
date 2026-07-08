import java.util.*;

/**
 * P165. Find K Pairs With Smallest Sums. This is a mid-to-hard Java DSA coding
 * problem commonly seen
 * in service based company technical rounds. Read the full input from the
 * method parameters, choose
 * the expected optimal data structure or algorithm, handle edge cases such as
 * empty inputs and
 * duplicates, and return the exact platform-style output.
 */
public final class P165FindKPairsWithSmallestSums {

    private P165FindKPairsWithSmallestSums() {
    }

    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        if (nums1.length == 0 || nums2.length == 0)
            return ans;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> nums1[a[0]] + nums2[a[1]]));
        for (int i = 0; i < Math.min(nums1.length, k); i++)
            pq.offer(new int[] { i, 0 });
        while (k-- > 0 && !pq.isEmpty()) {
            int[] cur = pq.poll();
            ans.add(List.of(nums1[cur[0]], nums2[cur[1]]));
            if (cur[1] + 1 < nums2.length)
                pq.offer(new int[] { cur[0], cur[1] + 1 });
        }
        return ans;
    }
}
