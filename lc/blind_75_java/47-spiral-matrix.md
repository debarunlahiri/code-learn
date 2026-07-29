# 47. Spiral Matrix

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Return every matrix value in clockwise spiral order, beginning at the top-left corner and moving inward layer by layer.

Your solution must return the requested value or data structure; printing alone
does not solve the problem. Treat the input as read-only unless the statement
explicitly requires an in-place transformation. A correct solution must handle
the smallest valid input, the largest valid input, duplicate or negative values
when the problem permits them, and cases where the answer occurs at an input
boundary.

The central challenge is not only to produce a correct result, but to recognize
and remove repeated work. Begin with the direct exhaustive method because it
makes the correctness condition visible. Then compare it with the optimized
method and identify the invariant, cached state, ordering property, or data
structure that prevents the same work from being performed again.

## Requirements and Edge Cases

- Do not silently assume extra ordering or uniqueness beyond what the statement
  guarantees.
- Preserve the required output order when the problem defines one.
- Consider empty intermediate results, single-element structures, and answers
  found at the first or last legal position.
- Avoid integer overflow where a sum, product, distance, or boundary can exceed
  the input element range.
- The Java methods below focus on the algorithm and use conventional LeetCode
  model classes such as `ListNode`, `TreeNode`, or `Node` where needed.

## Approach 1: Brute Force

Start from the definition of a valid answer and enumerate the possible choices
directly. This approach is intentionally straightforward: it is useful for
understanding the search space, checking small examples by hand, and serving as
a correctness oracle for randomized tests. Its weakness is repeated work, which
becomes too expensive near the upper input limits.

### Brute-Force Java

For this problem, the straightforward correctness-first implementation uses the same core traversal shown below. It is presented first as the baseline; the following section explains the production interpretation and invariant.

```java
import java.util.*;

public class Solution {

    public List<Integer> spiralOrder(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        boolean[][] visited = new boolean[rows][columns];
        int[][] directions = {
            { 0, 1 },
            { 1, 0 },
            { 0, -1 },
            { -1, 0 },
        };
        List<Integer> result = new ArrayList<>();

        int row = 0;
        int column = 0;
        int direction = 0;

        for (int count = 0; count < rows * columns; count++) {
            result.add(matrix[row][column]);
            visited[row][column] = true;

            int nextRow = row + directions[direction][0];
            int nextColumn = column + directions[direction][1];
            if (
                nextRow < 0 ||
                nextRow >= rows ||
                nextColumn < 0 ||
                nextColumn >= columns ||
                visited[nextRow][nextColumn]
            ) {
                direction = (direction + 1) % directions.length;
                nextRow = row + directions[direction][0];
                nextColumn = column + directions[direction][1];
            }

            row = nextRow;
            column = nextColumn;
        }
        return result;
    }
}
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java — O(m×n) time, O(1) space

```java
import java.util.*;

public class Solution {

    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        int top = 0,
            bottom = matrix.length - 1,
            left = 0,
            right = matrix[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int j = left; j <= right; j++) {
                result.add(matrix[top][j]);
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                result.add(matrix[i][right]);
            }
            right--;
            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    result.add(matrix[bottom][j]);
                }
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    result.add(matrix[i][left]);
                }
                left++;
            }
        }
        return result;
    }
}
```

## Why the Optimized Approach Is Correct

The optimized method keeps exactly the information required to make the next
decision. At each iteration or recursive call, its maintained state represents
all relevant choices processed so far. Updating that state preserves the
problem's validity condition, while discarded choices cannot produce a better
or different valid answer. When the algorithm terminates, every candidate that
could affect the result has therefore been considered either explicitly or
through the stored summary.

## Final Review

Before moving to the next problem, trace both approaches on a normal case and an
edge case. Explain why the brute-force version repeats work, state the optimized
invariant in one sentence, and reproduce the optimized Java method without
copying it. The source heading for this implementation is “Spiral Matrix”.
