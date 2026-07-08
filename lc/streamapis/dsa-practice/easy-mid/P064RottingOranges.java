import java.util.*;

/**
 * P064. Rotting Oranges. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service
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
public final class P064RottingOranges {

    private P064RottingOranges() {
    }

    public int orangesRotting(int[][] grid) {
        Queue<int[]> q = new ArrayDeque<>();
        int fresh = 0, minutes = 0;
        for (int r = 0; r < grid.length; r++)
    for (int c = 0; c < grid[0].length; c++) {
        if (grid[r][c] == 2)
            q.offer(new int[] { r, c });
        if (grid[r][c] == 1)
            fresh++;
    }
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        while (fresh > 0 && !q.isEmpty()) {
    for (int size = q.size(); size > 0; size--) {
        int[] cur = q.poll();
        for (int[] d : dirs) {
            int nr = cur[0] + d[0], nc = cur[1] + d[1];
            if (nr >= 0 && nc >= 0 && nr < grid.length && nc < grid[0].length && grid[nr][nc] == 1) {
                grid[nr][nc] = 2;
                fresh--;
                q.offer(new int[] { nr, nc });
            }
        }
    }
    minutes++;
        }
        return fresh == 0 ? minutes : -1;
    }

}
