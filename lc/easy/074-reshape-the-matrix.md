# 074. Reshape the Matrix

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Matrix

## Problem Statement

Given a matrix `mat` and target dimensions `r` and `c`, reshape the matrix into `r x c` while keeping the original row-traversal order.

If reshape is impossible, return the original matrix.

## Constraints

- `1 <= mat.length, mat[i].length <= 100`
- `-1000 <= mat[i][j] <= 1000`
- `1 <= r, c <= 300`

## Example

Input:

```text
mat = [[1, 2], [3, 4]], r = 1, c = 4
```

Output:

```text
[[1, 2, 3, 4]]
```

## Brute Force Approach

Flatten the matrix into a list, then fill the new matrix from that list.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public int[][] matrixReshape(int[][] mat, int r, int c) {
        int rows = mat.length;
        int cols = mat[0].length;

        if (rows * cols != r * c) {
            return mat;
        }

        List<Integer> values = new ArrayList<>();

        for (int[] row : mat) {
            for (int value : row) {
                values.add(value);
            }
        }

        int[][] result = new int[r][c];

        for (int i = 0; i < values.size(); i++) {
            result[i / c][i % c] = values.get(i);
        }

        return result;
    }
}
```

Complexity:

- Time: `O(m * n)`
- Space: `O(m * n)`

## Best Approach

Map each original position to a one-dimensional index, then map that index to the new matrix.

```java
class Solution {
    public int[][] matrixReshape(int[][] mat, int r, int c) {
        int rows = mat.length;
        int cols = mat[0].length;

        if (rows * cols != r * c) {
            return mat;
        }

        int[][] result = new int[r][c];

        for (int i = 0; i < rows * cols; i++) {
            result[i / c][i % c] = mat[i / cols][i % cols];
        }

        return result;
    }
}
```

Complexity:

- Time: `O(m * n)`
- Space: `O(1)` apart from output.

