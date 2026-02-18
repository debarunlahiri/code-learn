# Algorithms: Programming Puzzles and Brain Teasers

Goal: Master creative problem-solving and algorithmic thinking through puzzles.

---

## 1. Two Eggs Problem

### What it does
Find the minimum number of drops needed to determine the highest floor from which an egg won't break.

### Why it matters
- Optimization thinking
- Mathematical reasoning
- Algorithm design
- Interview preparation

### Intuition
Balance between linear and binary search. Use triangular numbers to minimize worst-case drops. Like finding the optimal strategy with limited resources.

### When to use
- Optimization problems
- Resource allocation
- Search strategies
- Algorithm design

### Time complexity
- `O(√n)` optimal solution
- Space: `O(1)`

### Edge cases
- 1 egg (must use linear search)
- 1 floor
- Very tall buildings
- Multiple eggs

### Java code
```java
public class TwoEggsProblem {
    // Calculate minimum drops with 2 eggs and n floors
    static int minDrops(int floors) {
        if (floors <= 0) return 0;
        if (floors == 1) return 1;
        
        // Find the smallest k such that k(k+1)/2 >= floors
        int k = 1;
        while (k * (k + 1) / 2 < floors) {
            k++;
        }
        return k;
    }

    // General solution with e eggs and f floors
    static int minDrops(int eggs, int floors) {
        if (eggs <= 0 || floors <= 0) return 0;
        if (eggs == 1) return floors; // Linear search
        if (floors == 1) return 1;
        
        int[][] dp = new int[eggs + 1][floors + 1];
        
        // Base cases
        for (int e = 1; e <= eggs; e++) {
            dp[e][0] = 0;
            dp[e][1] = 1;
        }
        for (int f = 1; f <= floors; f++) {
            dp[1][f] = f; // Linear search with 1 egg
        }
        
        // Fill DP table
        for (int e = 2; e <= eggs; e++) {
            for (int f = 2; f <= floors; f++) {
                dp[e][f] = Integer.MAX_VALUE;
                
                // Try dropping from each floor
                for (int x = 1; x <= f; x++) {
                    int drops = 1 + Math.max(dp[e - 1][x - 1], dp[e][f - x]);
                    dp[e][f] = Math.min(dp[e][f], drops);
                }
            }
        }
        
        return dp[eggs][floors];
    }

    // Optimized using binary search for drop point
    static int minDropsOptimized(int eggs, int floors) {
        if (eggs <= 0 || floors <= 0) return 0;
        if (eggs == 1) return floors;
        if (floors == 1) return 1;
        
        int[][] dp = new int[eggs + 1][floors + 1];
        
        for (int e = 1; e <= eggs; e++) {
            dp[e][0] = 0;
            dp[e][1] = 1;
        }
        for (int f = 1; f <= floors; f++) {
            dp[1][f] = f;
        }
        
        for (int e = 2; e <= eggs; e++) {
            for (int f = 2; f <= floors; f++) {
                dp[e][f] = Integer.MAX_VALUE;
                
                int low = 1, high = f;
                while (low <= high) {
                    int mid = (low + high) / 2;
                    int breakCount = dp[e - 1][mid - 1];
                    int surviveCount = dp[e][f - mid];
                    
                    int worstCase = Math.max(breakCount, surviveCount);
                    dp[e][f] = Math.min(dp[e][f], 1 + worstCase);
                    
                    if (breakCount > surviveCount) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }
                }
            }
        }
        
        return dp[eggs][floors];
    }
}
```

---

## 2. Tower of Hanoi

### What it does
Move disks from one peg to another following rules: only one disk at a time, larger disk cannot be on smaller.

### Why it matters
- Recursion understanding
- Mathematical patterns
- Algorithm complexity
- Problem decomposition

### Intuition
Move n-1 disks to auxiliary peg, move largest disk to target, then move n-1 disks from auxiliary to target. Like solving smaller version of same problem repeatedly.

### When to use
- Recursion practice
- Mathematical induction
- Algorithm analysis
- Problem solving

### Time complexity
- `O(2^n)` moves required
- Space: `O(n)` for recursion stack

### Edge cases
- 0 disks (no moves)
- 1 disk (one move)
- Very large n (exponential time)
- Stack overflow

### Java code
```java
import java.util.*;

public class TowerOfHanoi {
    static class Move {
        char from, to;
        Move(char from, char to) { this.from = from; this.to = to; }
        
        @Override
        public String toString() {
            return "Move disk from " + from + " to " + to;
        }
    }

    // Recursive solution
    static List<Move> solveRecursive(int n, char from, char to, char aux) {
        List<Move> moves = new ArrayList<>();
        
        if (n == 1) {
            moves.add(new Move(from, to));
            return moves;
        }
        
        // Move n-1 disks from 'from' to 'aux'
        moves.addAll(solveRecursive(n - 1, from, aux, to));
        
        // Move nth disk from 'from' to 'to'
        moves.add(new Move(from, to));
        
        // Move n-1 disks from 'aux' to 'to'
        moves.addAll(solveRecursive(n - 1, aux, to, from));
        
        return moves;
    }

    // Iterative solution using binary representation
    static List<Move> solveIterative(int n) {
        List<Move> moves = new ArrayList<>();
        
        // Total moves = 2^n - 1
        int totalMoves = (1 << n) - 1;
        char[] pegs = {'A', 'B', 'C'};
        
        for (int i = 1; i <= totalMoves; i++) {
            // Find the disk to move
            int disk = Integer.bitCount(i & (i - 1)) + 1;
            
            // Determine source and destination
            int from = (i >> (disk - 1)) & 1;
            int to = 1 - from;
            
            // Adjust peg indices based on disk parity
            if (disk % 2 == 0) {
                int temp = from;
                from = to;
                to = temp;
            }
            
            moves.add(new Move(pegs[from], pegs[to]));
        }
        
        return moves;
    }

    // Print moves
    static void printMoves(List<Move> moves) {
        for (int i = 0; i < moves.size(); i++) {
            System.out.println((i + 1) + ". " + moves.get(i));
        }
    }

    // Verify solution
    static boolean verifySolution(List<Move> moves, int n) {
        // This is a complex verification - simplified version
        // In practice, you'd simulate the moves
        return moves.size() == (1 << n) - 1;
    }
}
```

---

## 3. N-Queens Problem

### What it does
Place N queens on N×N chessboard so no two queens attack each other.

### Why it matters
- Backtracking algorithms
- Constraint satisfaction
- Combinatorial optimization
- Algorithm design patterns

### Intuition
Place queens row by row, backtrack when conflict occurs. Use column and diagonal tracking for efficiency. Like finding valid arrangements through systematic trial and error.

### When to use
- Backtracking practice
- Constraint satisfaction
- Combinatorial problems
- Algorithm optimization

### Time complexity
- Worst case: `O(N!)`
- Pruned: Much better in practice
- Space: `O(N)` for recursion stack

### Edge cases
- N = 0 (empty board)
- N = 1 (trivial solution)
- N = 2, 3 (no solutions)
- Large N (computationally intensive)

### Java code
```java
import java.util.*;

public class NQueens {
    static class Position {
        int row, col;
        Position(int row, int col) { this.row = row; this.col = col; }
    }

    // Solve N-Queens and return all solutions
    static List<List<String>> solveNQueens(int n) {
        List<List<String>> solutions = new ArrayList<>();
        char[][] board = new char[n][n];
        
        // Initialize board
        for (int i = 0; i < n; i++) {
            Arrays.fill(board[i], '.');
        }
        
        solve(board, 0, solutions);
        return solutions;
    }

    static void solve(char[][] board, int row, List<List<String>> solutions) {
        int n = board.length;
        
        if (row == n) {
            // Found a solution
            List<String> solution = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                solution.add(new String(board[i]));
            }
            solutions.add(solution);
            return;
        }
        
        for (int col = 0; col < n; col++) {
            if (isValid(board, row, col)) {
                board[row][col] = 'Q';
                solve(board, row + 1, solutions);
                board[row][col] = '.'; // Backtrack
            }
        }
    }

    static boolean isValid(char[][] board, int row, int col) {
        int n = board.length;
        
        // Check column
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q') return false;
        }
        
        // Check upper-left diagonal
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q') return false;
        }
        
        // Check upper-right diagonal
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
            if (board[i][j] == 'Q') return false;
        }
        
        return true;
    }

    // Optimized version using bit manipulation
    static List<List<String>> solveNQueensOptimized(int n) {
        List<List<String>> solutions = new ArrayList<>();
        solveOptimized(n, 0, 0, 0, 0, new ArrayList<>(), solutions);
        return solutions;
    }

    static void solveOptimized(int n, int row, int cols, int diag1, int diag2, 
                              List<Integer> positions, List<List<String>> solutions) {
        if (row == n) {
            // Convert positions to board representation
            List<String> solution = new ArrayList<>();
            for (int pos : positions) {
                char[] rowStr = new char[n];
                Arrays.fill(rowStr, '.');
                rowStr[pos] = 'Q';
                solution.add(new String(rowStr));
            }
            solutions.add(solution);
            return;
        }
        
        // Available positions
        int available = (~(cols | diag1 | diag2)) & ((1 << n) - 1);
        
        while (available != 0) {
            int position = available & -available; // Rightmost 1-bit
            available = available - position;
            
            int col = Integer.bitCount(position - 1);
            positions.add(col);
            
            solveOptimized(n, row + 1, cols | position, 
                          (diag1 | position) << 1, 
                          (diag2 | position) >> 1, 
                          positions, solutions);
            
            positions.remove(positions.size() - 1);
        }
    }

    // Count solutions without storing them
    static int totalNQueens(int n) {
        return solveCount(n, 0, 0, 0, 0);
    }

    static int solveCount(int n, int row, int cols, int diag1, int diag2) {
        if (row == n) return 1;
        
        int count = 0;
        int available = (~(cols | diag1 | diag2)) & ((1 << n) - 1);
        
        while (available != 0) {
            int position = available & -available;
            available = available - position;
            
            count += solveCount(n, row + 1, cols | position, 
                              (diag1 | position) << 1, 
                              (diag2 | position) >> 1);
        }
        
        return count;
    }
}
```

---

## 4. Sudoku Solver

### What it does
Solve Sudoku puzzles using backtracking algorithm.

### Why it matters
- Backtracking mastery
- Constraint propagation
- Algorithm optimization
- Real-world problem solving

### Intuition
Try filling empty cells one by one, backtrack when stuck. Use constraints to prune search space. Like solving a puzzle through systematic trial and error.

### When to use
- Backtracking practice
- Constraint satisfaction
- Puzzle solving
- Algorithm optimization

### Time complexity
- Worst case: Exponential
- Average case: Much better with heuristics
- Space: `O(1)` for board + recursion stack

### Edge cases
- Empty board
- Invalid puzzles
- Multiple solutions
- No solution

### Java code
```java
public class SudokuSolver {
    static class Sudoku {
        private static final int SIZE = 9;
        private static final int EMPTY = 0;
        
        int[][] board;
        
        Sudoku(int[][] board) {
            this.board = board;
        }
        
        boolean solve() {
            int[] empty = findEmpty();
            if (empty == null) return true; // Solved
            
            int row = empty[0], col = empty[1];
            
            for (int num = 1; num <= SIZE; num++) {
                if (isValid(row, col, num)) {
                    board[row][col] = num;
                    
                    if (solve()) return true;
                    
                    board[row][col] = EMPTY; // Backtrack
                }
            }
            
            return false;
        }
        
        int[] findEmpty() {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (board[row][col] == EMPTY) {
                        return new int[]{row, col};
                    }
                }
            }
            return null;
        }
        
        boolean isValid(int row, int col, int num) {
            // Check row
            for (int x = 0; x < SIZE; x++) {
                if (board[row][x] == num) return false;
            }
            
            // Check column
            for (int x = 0; x < SIZE; x++) {
                if (board[x][col] == num) return false;
            }
            
            // Check 3x3 box
            int boxRow = row - row % 3;
            int boxCol = col - col % 3;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[boxRow + i][boxCol + j] == num) return false;
                }
            }
            
            return true;
        }
        
        void print() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    System.out.print(board[i][j] + " ");
                    if (j == 2 || j == 5) System.out.print("| ");
                }
                System.out.println();
                if (i == 2 || i == 5) System.out.println("---------------------");
            }
        }
    }

    // Optimized solver with constraint propagation
    static class OptimizedSudoku {
        private static final int SIZE = 9;
        private static final int EMPTY = 0;
        
        int[][] board;
        boolean[][] rows = new boolean[SIZE][SIZE + 1];
        boolean[][] cols = new boolean[SIZE][SIZE + 1];
        boolean[][] boxes = new boolean[SIZE][SIZE + 1];
        
        OptimizedSudoku(int[][] board) {
            this.board = board;
            initializeConstraints();
        }
        
        void initializeConstraints() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] != EMPTY) {
                        int num = board[i][j];
                        rows[i][num] = true;
                        cols[j][num] = true;
                        boxes[(i/3)*3 + (j/3)][num] = true;
                    }
                }
            }
        }
        
        boolean solve() {
            int[] empty = findBestEmpty();
            if (empty == null) return true;
            
            int row = empty[0], col = empty[1];
            int box = (row/3)*3 + (col/3);
            
            for (int num = 1; num <= SIZE; num++) {
                if (!rows[row][num] && !cols[col][num] && !boxes[box][num]) {
                    // Place number
                    board[row][col] = num;
                    rows[row][num] = cols[col][num] = boxes[box][num] = true;
                    
                    if (solve()) return true;
                    
                    // Backtrack
                    board[row][col] = EMPTY;
                    rows[row][num] = cols[col][num] = boxes[box][num] = false;
                }
            }
            
            return false;
        }
        
        int[] findBestEmpty() {
            int[] best = null;
            int minOptions = SIZE + 1;
            
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        int options = countOptions(i, j);
                        if (options < minOptions) {
                            minOptions = options;
                            best = new int[]{i, j};
                            if (options == 1) break; // Can't get better
                        }
                    }
                }
            }
            
            return best;
        }
        
        int countOptions(int row, int col) {
            int box = (row/3)*3 + (col/3);
            int count = 0;
            
            for (int num = 1; num <= SIZE; num++) {
                if (!rows[row][num] && !cols[col][num] && !boxes[box][num]) {
                    count++;
                }
            }
            
            return count;
        }
    }
}
```

---

## 5. Knight's Tour

### What it does
Find a sequence of knight moves on chessboard that visits every square exactly once.

### Why it matters
- Backtracking algorithms
- Heuristic optimization
- Graph traversal
- Hamiltonian path problems

### Intuition
Use backtracking with Warnsdorff's heuristic (move to square with fewest onward moves). Like finding a path through all cities exactly once.

### When to use
- Backtracking practice
- Graph algorithms
- Heuristic methods
- Path finding

### Time complexity
- Backtracking: Exponential
- With heuristics: Much better
- Space: `O(n²)` for board

### Edge cases
- Small boards (1x1, 2x2 have no solution)
- Standard 8x8 board
- Large boards
- Multiple solutions

### Java code
```java
import java.util.*;

public class KnightsTour {
    static class Position {
        int row, col;
        Position(int row, int col) { this.row = row; this.col = col; }
    }

    private static final int[] ROW_MOVES = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] COL_MOVES = {1, 2, 2, 1, -1, -2, -2, -1};

    // Basic backtracking solution
    static boolean knightsTour(int[][] board, int row, int col, int moveCount) {
        int n = board.length;
        
        // Mark current position
        board[row][col] = moveCount;
        
        // Check if tour is complete
        if (moveCount == n * n) {
            return true;
        }
        
        // Try all 8 possible moves
        for (int i = 0; i < 8; i++) {
            int nextRow = row + ROW_MOVES[i];
            int nextCol = col + COL_MOVES[i];
            
            if (isValidMove(board, nextRow, nextCol)) {
                if (knightsTour(board, nextRow, nextCol, moveCount + 1)) {
                    return true;
                }
            }
        }
        
        // Backtrack
        board[row][col] = 0;
        return false;
    }

    static boolean isValidMove(int[][] board, int row, int col) {
        int n = board.length;
        return row >= 0 && row < n && col >= 0 && col < n && board[row][col] == 0;
    }

    // Warnsdorff's heuristic (much faster)
    static boolean knightsTourWarnsdorff(int[][] board, int row, int col, int moveCount) {
        int n = board.length;
        
        board[row][col] = moveCount;
        
        if (moveCount == n * n) {
            return true;
        }
        
        // Get all valid next moves and sort by accessibility
        List<Move> nextMoves = getNextMoves(board, row, col);
        nextMoves.sort((a, b) -> Integer.compare(a.accessibility, b.accessibility));
        
        for (Move move : nextMoves) {
            if (knightsTourWarnsdorff(board, move.row, move.col, moveCount + 1)) {
                return true;
            }
        }
        
        board[row][col] = 0;
        return false;
    }

    static List<Move> getNextMoves(int[][] board, int row, int col) {
        List<Move> moves = new ArrayList<>();
        
        for (int i = 0; i < 8; i++) {
            int nextRow = row + ROW_MOVES[i];
            int nextCol = col + COL_MOVES[i];
            
            if (isValidMove(board, nextRow, nextCol)) {
                int accessibility = countAccessibility(board, nextRow, nextCol);
                moves.add(new Move(nextRow, nextCol, accessibility));
            }
        }
        
        return moves;
    }

    static int countAccessibility(int[][] board, int row, int col) {
        int count = 0;
        
        for (int i = 0; i < 8; i++) {
            int nextRow = row + ROW_MOVES[i];
            int nextCol = col + COL_MOVES[i];
            
            if (isValidMove(board, nextRow, nextCol)) {
                count++;
            }
        }
        
        return count;
    }

    static class Move {
        int row, col, accessibility;
        Move(int row, int col, int accessibility) {
            this.row = row;
            this.col = col;
            this.accessibility = accessibility;
        }
    }

    // Print board
    static void printBoard(int[][] board) {
        int n = board.length;
        int maxDigits = String.valueOf(n * n).length();
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%" + maxDigits + "d ", board[i][j]);
            }
            System.out.println();
        }
    }

    // Find closed tour (returns to starting position)
    static boolean closedTour(int n) {
        int[][] board = new int[n][n];
        
        // Try all starting positions
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (knightsTourWarnsdorff(board, row, col, 1)) {
                    // Check if last position can move to first
                    int lastRow = -1, lastCol = -1;
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (board[i][j] == n * n) {
                                lastRow = i;
                                lastCol = j;
                                break;
                            }
                        }
                    }
                    
                    // Check if can return to start
                    for (int i = 0; i < 8; i++) {
                        if (lastRow + ROW_MOVES[i] == row && 
                            lastCol + COL_MOVES[i] == col) {
                            return true;
                        }
                    }
                }
                // Reset board
                for (int i = 0; i < n; i++) {
                    Arrays.fill(board[i], 0);
                }
            }
        }
        
        return false;
    }
}
```

---

## Practice Problems

### Easy
1. **Two Eggs Problem** (Optimization)
2. **Tower of Hanoi** (Recursion)
3. **Basic N-Queens** (Backtracking)

### Medium
1. **Sudoku Solver** (Constraint satisfaction)
2. **Knight's Tour** (Heuristics)
3. **8-Puzzle Problem** (A* search)

### Hard
1. **Rubik's Cube** (Group theory)
2. **Maze Generation** (Graph algorithms)
3. **Crossword Puzzle** (Advanced backtracking)

---

**Remember:** Puzzles teach creative problem-solving - think outside the box!
