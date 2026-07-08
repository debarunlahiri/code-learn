import java.util.*;

/**
 * P164. Merge K Sorted Arrays. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P164MergeKSortedArrays {

    private P164MergeKSortedArrays() {
    }

    public List<Integer> mergeKSortedArrays(List<List<Integer>> arrays) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> arrays.get(a[0]).get(a[1])));
        for (int i = 0; i < arrays.size(); i++)
            if (!arrays.get(i).isEmpty())
                pq.offer(new int[] { i, 0 });
        List<Integer> ans = new ArrayList<>();
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            ans.add(arrays.get(cur[0]).get(cur[1]));
            if (++cur[1] < arrays.get(cur[0]).size())
                pq.offer(cur);
        }
        return ans;
    }
}
