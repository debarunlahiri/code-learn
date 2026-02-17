# Algorithms: Recursion and Backtracking (Easy to Hard)

Goal: Understand recursion patterns, backtracking technique, and how to think recursively.

---

## 1. Recursion Basics: Print 1 to N

### What it does
Print numbers from 1 to N using recursion.

### Why it matters
- Introduces recursion fundamentals
- Teaches base case and recursive case
- Foundation for all recursive problems

### Intuition
Think of it as a chain of commands. Each person tells the next person to print their number, starting from the smallest.

### When to use
- Simple recursion practice
- Understanding call stack
- Problems with natural recursive structure

### Time complexity
- Time: `O(n)`
- Space: `O(n)` (call stack)

### Edge cases
- N = 0 (base case)
- Negative numbers (handle separately)

### Java code
```java
public class PrintOneToN {
    static void print(int n) {
        if (n == 0) return;
        print(n - 1);
        System.out.print(n + " ");
    }
}
```

---

## 2. Recursion: Power (x^n)

### What it does
Calculate x raised to power n efficiently using recursion.

### Why it matters
- Demonstrates divide and conquer in recursion
- Reduces time from O(n) to O(log n)
- Foundation for exponentiation algorithms

### Intuition
Instead of multiplying x n times, notice that x^n = (x^(n/2))^2. Compute half the power once, then square it.

### When to use
- Fast exponentiation
- Modular arithmetic
- Problems with repeated multiplication

### Time complexity
- Time: `O(log n)`
- Space: `O(log n)`

### Edge cases
- n = 0 (any number^0 = 1)
- Negative exponents (return 1/x^|n|)
- Overflow (use long or BigInteger)

### Java code
```java
public class FastPower {
    static long power(long x, int n) {
        if (n == 0) return 1;
        long half = power(x, n / 2);
        if (n % 2 == 0) return half * half;
        return half * half * x;
    }
}
```

---

## 3. Backtracking: Subsets

### What it does
Generate all possible subsets (power set) of a given array.

### Why it matters
- Classic backtracking problem
- Foundation for combination problems
- Teaches exploring all possibilities systematically

### Intuition
For each element, make two choices: include it in the current subset or exclude it. Explore both paths recursively.

### When to use
- Combination generation
- Subset selection problems
- Problems requiring exhaustive search

### Time complexity
- Time: `O(2^n)` (each element has 2 choices)
- Space: `O(n)` (recursion depth)

### Edge cases
- Empty array (only empty subset)
- Duplicate elements (handle with Set or skip)
- Large n (exponential growth)

### Java code
```java
import java.util.ArrayList;
import java.util.List;

public class SubsetsBacktracking {
    static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        dfs(0, nums, new ArrayList<>(), ans);
        return ans;
    }

    static void dfs(int index, int[] nums, List<Integer> path, List<List<Integer>> ans) {
        if (index == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }
        dfs(index + 1, nums, path, ans);  // Exclude current element
        path.add(nums[index]);
        dfs(index + 1, nums, path, ans);  // Include current element
        path.remove(path.size() - 1);      // Backtrack
    }
}
```

---

## 4. Backtracking: Permutations

### What it does
Generate all possible orderings of array elements.

### Why it matters
- Fundamental backtracking pattern
- Used in scheduling, arrangement problems
- Teaches tracking used elements

### Intuition
Build permutations one position at a time. For each position, try any unused element, then recurse for the next position.

### When to use
- Arrangement problems
- Traveling salesman basics
- Problems requiring all orderings

### Time complexity
- Time: `O(n!)` (n choices for first position, n-1 for second, etc.)
- Space: `O(n)`

### Edge cases
- Empty array (empty permutation)
- Duplicate elements (handle with Set)
- Single element

### Java code
```java
import java.util.ArrayList;
import java.util.List;

public class PermutationsBacktracking {
    static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), ans);
        return ans;
    }

    static void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> ans) {
        if (path.size() == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            path.add(nums[i]);
            backtrack(nums, used, path, ans);
            path.remove(path.size() - 1);  // Backtrack
            used[i] = false;
        }
    }
}
```

---

## 5. Backtracking: N-Queens

### What it does
Place N queens on N×N chessboard so none attack each other.

### Why it matters
- Advanced backtracking with constraints
- Teaches pruning invalid paths early
- Classic constraint satisfaction problem

### Intuition
Place queens row by row. For each row, try every column, check if it's safe (no conflicts), then move to next row. Backtrack when stuck.

### When to use
- Constraint satisfaction
- Placement problems
- Problems with multiple constraints

### Time complexity
- Worst case: `O(n!)` (much less with pruning)
- Space: `O(n)`

### Edge cases
- N = 1 (trivial solution)
- N = 2, 3 (no solution)
- Large N (computationally intensive)

### Java code
```java
import java.util.*;

public class NQueens {
    static List<List<String>> solveNQueens(int n) {
        List<List<String>> ans = new ArrayList<>();
        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) Arrays.fill(board[i], '.');
        backtrack(0, board, ans);
        return ans;
    }

    static void backtrack(int row, char[][] board, List<List<String>> ans) {
        if (row == board.length) {
            ans.add(construct(board));
            return;
        }

        for (int col = 0; col < board.length; col++) {
            if (isValid(board, row, col)) {
                board[row][col] = 'Q';
                backtrack(row + 1, board, ans);
                board[row][col] = '.';  // Backtrack
            }
        }
    }

    static boolean isValid(char[][] board, int row, int col) {
        // Check column
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q') return false;
        }
        // Check upper-left diagonal
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q') return false;
        }
        // Check upper-right diagonal
        for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++) {
            if (board[i][j] == 'Q') return false;
        }
        return true;
    }

    static List<String> construct(char[][] board) {
        List<String> res = new ArrayList<>();
        for (char[] row : board) res.add(new String(row));
        return res;
    }
}
```

## 5. Backtracking: N-Queens
```java
import java.util.ArrayList;
import java.util.List;

public class NQueens {
    static List<List<String>> solveNQueens(int n) {
        List<List<String>> ans = new ArrayList<>();
        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) board[i][j] = '.';
        }
        place(0, board, ans);
        return ans;
    }

    static void place(int row, char[][] board, List<List<String>> ans) {
        int n = board.length;
        if (row == n) {
            List<String> config = new ArrayList<>();
            for (char[] r : board) config.add(new String(r));
            ans.add(config);
            return;
        }
        for (int col = 0; col < n; col++) {
            if (isSafe(board, row, col)) {
                board[row][col] = 'Q';
                place(row + 1, board, ans);
                board[row][col] = '.';
            }
        }
    }

    static boolean isSafe(char[][] board, int row, int col) {
        for (int r = 0; r < row; r++) {
            if (board[r][col] == 'Q') return false;
        }
        for (int r = row - 1, c = col - 1; r >= 0 && c >= 0; r--, c--) {
            if (board[r][c] == 'Q') return false;
        }
        for (int r = row - 1, c = col + 1; r >= 0 && c < board.length; r--, c++) {
            if (board[r][c] == 'Q') return false;
        }
        return true;
    }
}
```

