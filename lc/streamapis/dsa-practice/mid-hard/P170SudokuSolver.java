import java.util.*;

/**
 * P170. Sudoku Solver. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the full input from the method parameters,
 * choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P170SudokuSolver {

    private P170SudokuSolver() {
    }

    public void solveSudoku(char[][] board) {
        solve(board);
    }

    private boolean solve(char[][] b) {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (b[r][c] == '.') {
                    for (char ch = '1'; ch <= '9'; ch++)
                        if (valid(b, r, c, ch)) {
                            b[r][c] = ch;
                            if (solve(b))
                                return true;
                            b[r][c] = '.';
                        }
                    return false;
                }
        return true;
    }

    private boolean valid(char[][] b, int r, int c, char ch) {
        for (int i = 0; i < 9; i++)
            if (b[r][i] == ch || b[i][c] == ch || b[3 * (r / 3) + i / 3][3 * (c / 3) + i % 3] == ch)
                return false;
        return true;
    }
}
