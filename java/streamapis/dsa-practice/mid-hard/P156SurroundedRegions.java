import java.util.*;

/**
 * P156. Surrounded Regions. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the input represented by the method
 * parameters, apply the
 * standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P156SurroundedRegions {

    private P156SurroundedRegions() {
    }

    public void solve(char[][] board) {
        int m = board.length, n = board[0].length;
        for (int r = 0; r < m; r++) {
            mark(board, r, 0);
            mark(board, r, n - 1);
        }
        for (int c = 0; c < n; c++) {
            mark(board, 0, c);
            mark(board, m - 1, c);
        }
        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++)
                board[r][c] = board[r][c] == '#' ? 'O' : 'X';
    }

    private void mark(char[][] b, int r, int c) {
        if (r < 0 || c < 0 || r == b.length || c == b[0].length || b[r][c] != 'O')
            return;
        b[r][c] = '#';
        mark(b, r + 1, c);
        mark(b, r - 1, c);
        mark(b, r, c + 1);
        mark(b, r, c - 1);
    }
}
