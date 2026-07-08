import java.util.*;

/**
 * P061. Word Search. This is a easy-to-mid Java DSA coding problem commonly
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
public final class P061WordSearch {

    private P061WordSearch() {
    }

    public boolean exist(char[][] board, String word) {
        for (int r = 0; r < board.length; r++)
    for (int c = 0; c < board[0].length; c++)
        if (dfsWord(board, word, r, c, 0))
            return true;
        return false;
    }

    private boolean dfsWord(char[][] b, String w, int r, int c, int i) {
        if (i == w.length())
    return true;
        if (r < 0 || c < 0 || r == b.length || c == b[0].length || b[r][c] != w.charAt(i))
    return false;
        char old = b[r][c];
        b[r][c] = '#';
        boolean ok = dfsWord(b, w, r + 1, c, i + 1) || dfsWord(b, w, r - 1, c, i + 1)
        || dfsWord(b, w, r, c + 1, i + 1) || dfsWord(b, w, r, c - 1, i + 1);
        b[r][c] = old;
        return ok;
    }

}
