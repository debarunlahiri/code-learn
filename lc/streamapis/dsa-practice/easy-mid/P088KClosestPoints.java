import java.util.*;

/**
 * P088. K Closest Points. This is a easy-to-mid Java DSA coding problem
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
public final class P088KClosestPoints {

    private P088KClosestPoints() {
    }

    public int[][] kClosest(int[][] points, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(dist(b), dist(a)));
        for (int[] p : points) {
    pq.offer(p);
    if (pq.size() > k)
        pq.poll();
        }
        int[][] ans = new int[k][];
        for (int i = 0; i < k; i++)
    ans[i] = pq.poll();
        return ans;
    }

    private int dist(int[] p) {
        return p[0] * p[0] + p[1] * p[1];
    }

}
