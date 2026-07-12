import java.util.*;

/**
 * P062. Number Of Islands. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P062NumberOfIslands {

    private P062NumberOfIslands() {
    }

    public int numIslands(char[][] grid) {
        int count = 0;
        for (int r = 0; r < grid.length; r++)
    for (int c = 0; c < grid[0].length; c++)
        if (grid[r][c] == '1') {
            count++;
            sink(grid, r, c);
        }
        return count;
    }

    private void sink(char[][] g, int r, int c) {
        if (r < 0 || c < 0 || r == g.length || c == g[0].length || g[r][c] != '1')
    return;
        g[r][c] = '0';
        sink(g, r + 1, c);
        sink(g, r - 1, c);
        sink(g, r, c + 1);
        sink(g, r, c - 1);
    }

}
