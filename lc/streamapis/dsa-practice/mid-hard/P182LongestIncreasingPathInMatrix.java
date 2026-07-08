import java.util.*;

/**
 * P182. Longest Increasing Path In Matrix. This is a mid-to-hard Java DSA
 * coding problem commonly seen
 * in service based company technical rounds. Read the full input from the
 * method parameters, choose
 * the expected optimal data structure or algorithm, handle edge cases such as
 * empty inputs and
 * duplicates, and return the exact platform-style output.
 */
public final class P182LongestIncreasingPathInMatrix {

    private P182LongestIncreasingPathInMatrix() {
    }

    public int longestIncreasingPath(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length, best = 0;
        int[][] memo = new int[m][n];
        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++)
                best = Math.max(best, dfs(matrix, r, c, memo));
        return best;
    }

    private int dfs(int[][] a, int r, int c, int[][] memo) {
        if (memo[r][c] != 0)
            return memo[r][c];
        int best = 1;
        int[][] d = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int[] x : d) {
            int nr = r + x[0], nc = c + x[1];
            if (nr >= 0 && nc >= 0 && nr < a.length && nc < a[0].length && a[nr][nc] > a[r][c])
                best = Math.max(best, 1 + dfs(a, nr, nc, memo));
        }
        return memo[r][c] = best;
    }
}
