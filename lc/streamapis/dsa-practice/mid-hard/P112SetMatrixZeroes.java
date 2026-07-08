import java.util.*;

/**
 * P112. Set Matrix Zeroes. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Implement the required method using an
 * efficient algorithm, not
 * brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P112SetMatrixZeroes {

    private P112SetMatrixZeroes() {
    }

    public void setZeroes(int[][] matrix) {
        boolean firstRow = false, firstCol = false;
        for (int c = 0; c < matrix[0].length; c++)
            if (matrix[0][c] == 0)
                firstRow = true;
        for (int r = 0; r < matrix.length; r++)
            if (matrix[r][0] == 0)
                firstCol = true;
        for (int r = 1; r < matrix.length; r++)
            for (int c = 1; c < matrix[0].length; c++)
                if (matrix[r][c] == 0) {
                    matrix[r][0] = 0;
                    matrix[0][c] = 0;
                }
        for (int r = 1; r < matrix.length; r++)
            for (int c = 1; c < matrix[0].length; c++)
                if (matrix[r][0] == 0 || matrix[0][c] == 0)
                    matrix[r][c] = 0;
        if (firstRow)
            Arrays.fill(matrix[0], 0);
        if (firstCol)
            for (int r = 0; r < matrix.length; r++)
                matrix[r][0] = 0;
    }
}
