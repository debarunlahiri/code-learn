import java.util.*;

/**
 * P067. Pacific Atlantic Water Flow. This is a easy-to-mid Java DSA coding
 * problem commonly practiced
 * for service based company coding rounds. Given the input described by the
 * method signature,
 * implement the required operation efficiently and return the expected result.
 * Handle normal edge
 * cases such as empty collections, duplicate values, boundary indexes, and null
 * child pointers when
 * the data structure allows them. Prefer the standard optimal approach used in
 * coding rounds, and keep
 * the implementation readable for revision.
 */
public final class P067PacificAtlanticWaterFlow {

    private P067PacificAtlanticWaterFlow() {
    }

    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        boolean[][] pac = new boolean[m][n], atl = new boolean[m][n];
        for (int r = 0; r < m; r++) {
    oceanDfs(heights, pac, r, 0, -1);
    oceanDfs(heights, atl, r, n - 1, -1);
        }
        for (int c = 0; c < n; c++) {
    oceanDfs(heights, pac, 0, c, -1);
    oceanDfs(heights, atl, m - 1, c, -1);
        }
        List<List<Integer>> ans = new ArrayList<>();
        for (int r = 0; r < m; r++)
    for (int c = 0; c < n; c++)
        if (pac[r][c] && atl[r][c])
            ans.add(List.of(r, c));
        return ans;
    }

    private void oceanDfs(int[][] h, boolean[][] seen, int r, int c, int prev) {
        if (r < 0 || c < 0 || r == h.length || c == h[0].length || seen[r][c] || h[r][c] < prev)
    return;
        seen[r][c] = true;
        oceanDfs(h, seen, r + 1, c, h[r][c]);
        oceanDfs(h, seen, r - 1, c, h[r][c]);
        oceanDfs(h, seen, r, c + 1, h[r][c]);
        oceanDfs(h, seen, r, c - 1, h[r][c]);
    }

}
