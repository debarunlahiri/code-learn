import java.util.*;

/**
 * P197. Trapping Rain Water II. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P197TrappingRainWaterIi {

    private P197TrappingRainWaterIi() {
    }

    public int trapRainWater(int[][] heightMap) {
        int m = heightMap.length, n = heightMap[0].length;
        boolean[][] seen = new boolean[m][n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        for (int r = 0; r < m; r++)
            for (int c : new int[] { 0, n - 1 }) {
                pq.offer(new int[] { r, c, heightMap[r][c] });
                seen[r][c] = true;
            }
        for (int c = 1; c < n - 1; c++)
            for (int r : new int[] { 0, m - 1 }) {
                pq.offer(new int[] { r, c, heightMap[r][c] });
                seen[r][c] = true;
            }
        int ans = 0;
        int[][] d = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            for (int[] x : d) {
                int nr = cur[0] + x[0], nc = cur[1] + x[1];
                if (nr >= 0 && nc >= 0 && nr < m && nc < n && !seen[nr][nc]) {
                    seen[nr][nc] = true;
                    ans += Math.max(0, cur[2] - heightMap[nr][nc]);
                    pq.offer(new int[] { nr, nc, Math.max(cur[2], heightMap[nr][nc]) });
                }
            }
        }
        return ans;
    }
}
