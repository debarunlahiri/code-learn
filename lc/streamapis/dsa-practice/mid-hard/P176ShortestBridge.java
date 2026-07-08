import java.util.*;

/**
 * P176. Shortest Bridge. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the full input from the method parameters,
 * choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P176ShortestBridge {

    private P176ShortestBridge() {
    }

    public int shortestBridge(int[][] grid) {
        int n = grid.length;
        Queue<int[]> q = new ArrayDeque<>();
        boolean found = false;
        for (int r = 0; r < n && !found; r++)
            for (int c = 0; c < n && !found; c++)
                if (grid[r][c] == 1) {
                    mark(grid, r, c, q);
                    found = true;
                }
        int[][] d = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int step = 0; !q.isEmpty(); step++)
            for (int size = q.size(); size > 0; size--) {
                int[] cur = q.poll();
                for (int[] x : d) {
                    int nr = cur[0] + x[0], nc = cur[1] + x[1];
                    if (nr >= 0 && nc >= 0 && nr < n && nc < n) {
                        if (grid[nr][nc] == 1)
                            return step;
                        if (grid[nr][nc] == 0) {
                            grid[nr][nc] = 2;
                            q.offer(new int[] { nr, nc });
                        }
                    }
                }
            }
        return -1;
    }

    private void mark(int[][] g, int r, int c, Queue<int[]> q) {
        if (r < 0 || c < 0 || r == g.length || c == g.length || g[r][c] != 1)
            return;
        g[r][c] = 2;
        q.offer(new int[] { r, c });
        mark(g, r + 1, c, q);
        mark(g, r - 1, c, q);
        mark(g, r, c + 1, q);
        mark(g, r, c - 1, q);
    }
}
