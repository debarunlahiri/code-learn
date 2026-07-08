import java.util.*;

/**
 * P063. Flood Fill. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service based
 * company coding rounds. Given the input described by the method signature,
 * implement the required
 * operation efficiently and return the expected result. Handle normal edge
 * cases such as empty
 * collections, duplicate values, boundary indexes, and null child pointers when
 * the data structure
 * allows them. Prefer the standard optimal approach used in coding rounds, and
 * keep the implementation
 * readable for revision.
 */
public final class P063FloodFill {

    private P063FloodFill() {
    }

    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int old = image[sr][sc];
        if (old != color)
    fill(image, sr, sc, old, color);
        return image;
    }

    private void fill(int[][] a, int r, int c, int old, int color) {
        if (r < 0 || c < 0 || r == a.length || c == a[0].length || a[r][c] != old)
    return;
        a[r][c] = color;
        fill(a, r + 1, c, old, color);
        fill(a, r - 1, c, old, color);
        fill(a, r, c + 1, old, color);
        fill(a, r, c - 1, old, color);
    }

}
