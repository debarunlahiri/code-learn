import java.util.*;

/**
 * P159. Swim In Rising Water. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the input represented by the method
 * parameters, apply the
 * standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P159SwimInRisingWater {

    private P159SwimInRisingWater() {
    }

    public int swimInWater(int[][] grid) {
        int n = grid.length;
        boolean[][] seen = new boolean[n][n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        pq.offer(new int[] { 0, 0, grid[0][0] });
        int[][] d = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int r = cur[0], c = cur[1], t = cur[2];
            if (seen[r][c])
                continue;
            seen[r][c] = true;
            if (r == n - 1 && c == n - 1)
                return t;
            for (int[] x : d) {
                int nr = r + x[0], nc = c + x[1];
                if (nr >= 0 && nc >= 0 && nr < n && nc < n && !seen[nr][nc])
                    pq.offer(new int[] { nr, nc, Math.max(t, grid[nr][nc]) });
            }
        }
        return -1;
    }
}
