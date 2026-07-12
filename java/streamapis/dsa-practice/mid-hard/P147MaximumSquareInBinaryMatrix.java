import java.util.*;

/**
 * P147. Maximum Square In Binary Matrix. This is a mid-to-hard Java DSA coding
 * problem commonly seen
 * in service based company technical rounds. Read the input represented by the
 * method parameters,
 * apply the standard efficient approach for this topic, and return the exact
 * result requested. Handle
 * empty inputs, duplicate values, boundary indexes, and large constraints in a
 * clean Java
 * implementation.
 */
public final class P147MaximumSquareInBinaryMatrix {

    private P147MaximumSquareInBinaryMatrix() {
    }

    public int maximalSquare(char[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length, best = 0;
        int[] dp = new int[cols + 1];
        for (int r = 1; r <= rows; r++) {
            int prev = 0;
            for (int c = 1; c <= cols; c++) {
                int temp = dp[c];
                if (matrix[r - 1][c - 1] == '1') {
                    dp[c] = 1 + Math.min(prev, Math.min(dp[c], dp[c - 1]));
                    best = Math.max(best, dp[c]);
                } else
                    dp[c] = 0;
                prev = temp;
            }
        }
        return best * best;
    }
}
