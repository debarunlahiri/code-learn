# Algorithms: Recursion and Backtracking (Easy to Hard)

## 1. Recursion Basics: Print 1 to N
```java
public class PrintOneToN {
    static void print(int n) {
        if (n == 0) return;
        print(n - 1);
        System.out.print(n + " ");
    }
}
```

## 2. Recursion: Power (x^n)
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

## 3. Backtracking: Subsets
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
        dfs(index + 1, nums, path, ans);
        path.add(nums[index]);
        dfs(index + 1, nums, path, ans);
        path.remove(path.size() - 1);
    }
}
```

## 4. Backtracking: Permutations
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
            path.remove(path.size() - 1);
            used[i] = false;
        }
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

