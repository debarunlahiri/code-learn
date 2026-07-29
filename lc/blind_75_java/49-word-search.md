# 49. Word Search

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Given a board of letters and a word, decide whether the word can be formed by adjacent horizontal or vertical cells without reusing a cell in one path.

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

    public boolean exist(char[][] board, String word) {
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                boolean[][] visited = new boolean[board.length][board[0].length];
                if (search(board, word, row, column, 0, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean search(
        char[][] board,
        String word,
        int row,
        int column,
        int index,
        boolean[][] visited
    ) {
        if (index == word.length()) {
            return true;
        }
        if (
            row < 0 ||
            row >= board.length ||
            column < 0 ||
            column >= board[0].length ||
            visited[row][column] ||
            board[row][column] != word.charAt(index)
        ) {
            return false;
        }

        visited[row][column] = true;
        boolean found =
            search(board, word, row + 1, column, index + 1, visited) ||
            search(board, word, row - 1, column, index + 1, visited) ||
            search(board, word, row, column + 1, index + 1, visited) ||
            search(board, word, row, column - 1, index + 1, visited);
        visited[row][column] = false;
        return found;
    }
}
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java (Backtracking DFS) — O(m×n×4^L) time

```java
import java.util.*;

public class Solution {

    public boolean exist(char[][] board, String word) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (dfs(board, word, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int i, int j, int k) {
        if (k == word.length()) {
            return true;
        }
        if (
            i < 0 ||
            i >= board.length ||
            j < 0 ||
            j >= board[0].length ||
            board[i][j] != word.charAt(k)
        ) {
            return false;
        }
        char tmp = board[i][j];
        board[i][j] = '#';
        boolean found =
            dfs(board, word, i + 1, j, k + 1) ||
            dfs(board, word, i - 1, j, k + 1) ||
            dfs(board, word, i, j + 1, k + 1) ||
            dfs(board, word, i, j - 1, k + 1);
        board[i][j] = tmp;
        return found;
    }
}
```

---

## String

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
copying it. The source heading for this implementation is “Word Search”.
