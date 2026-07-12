import java.util.*;

/**
 * P114. Rotate Image. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Implement the required method using an efficient
 * algorithm, not brute
 * force where a better standard approach exists. The solution should handle
 * boundary cases, duplicate
 * values, disconnected states, and large inputs according to the method
 * signature. Return the final
 * computed value or data structure exactly as the platform-style method
 * expects.
 */
public final class P114RotateImage {

    private P114RotateImage() {
    }

    public void rotate(int[][] matrix) {
        int n = matrix.length;
        for (int r = 0; r < n; r++)
            for (int c = r + 1; c < n; c++) {
                int t = matrix[r][c];
                matrix[r][c] = matrix[c][r];
                matrix[c][r] = t;
            }
        for (int[] row : matrix) {
            int l = 0, h = n - 1;
            while (l < h) {
                int t = row[l];
                row[l++] = row[h];
                row[h--] = t;
            }
        }
    }
}
