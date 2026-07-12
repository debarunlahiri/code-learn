import java.util.*;

/**
 * P169. N Queens. This is a mid-to-hard Java DSA coding problem commonly seen
 * in service based company
 * technical rounds. Read the full input from the method parameters, choose the
 * expected optimal data
 * structure or algorithm, handle edge cases such as empty inputs and
 * duplicates, and return the exact
 * platform-style output.
 */
public final class P169NQueens {

    private P169NQueens() {
    }

    public List<List<String>> solveNQueens(int n) {
        List<List<String>> ans = new ArrayList<>();
        char[][] board = new char[n][n];
        for (char[] row : board)
            Arrays.fill(row, '.');
        place(0, board, new boolean[n], new boolean[2 * n], new boolean[2 * n], ans);
        return ans;
    }

    private void place(int r, char[][] b, boolean[] col, boolean[] d1, boolean[] d2, List<List<String>> ans) {
        if (r == b.length) {
            List<String> cur = new ArrayList<>();
            for (char[] row : b)
                cur.add(new String(row));
            ans.add(cur);
            return;
        }
        for (int c = 0; c < b.length; c++) {
            int a = r - c + b.length, d = r + c;
            if (col[c] || d1[a] || d2[d])
                continue;
            col[c] = d1[a] = d2[d] = true;
            b[r][c] = 'Q';
            place(r + 1, b, col, d1, d2, ans);
            b[r][c] = '.';
            col[c] = d1[a] = d2[d] = false;
        }
    }
}
