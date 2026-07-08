import java.util.*;

/**
 * P160. Path With Minimum Effort. This is a mid-to-hard Java DSA coding problem
 * commonly seen in
 * service based company technical rounds. Read the input represented by the
 * method parameters, apply
 * the standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P160PathWithMinimumEffort {

    private P160PathWithMinimumEffort() {
    }

    public int minimumEffortPath(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        int[][] dist = new int[m][n];
        for (int[] row : dist)
            Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        pq.offer(new int[] { 0, 0, 0 });
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            if (cur[2] != dist[cur[0]][cur[1]])
                continue;
            if (cur[0] == m - 1 && cur[1] == n - 1)
                return cur[2];
            for (int[] d : dirs) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                if (nr >= 0 && nc >= 0 && nr < m && nc < n) {
                    int nd = Math.max(cur[2], Math.abs(heights[cur[0]][cur[1]] - heights[nr][nc]));
                    if (nd < dist[nr][nc]) {
                        dist[nr][nc] = nd;
                        pq.offer(new int[] { nr, nc, nd });
                    }
                }
            }
        }
        return 0;
    }
}
