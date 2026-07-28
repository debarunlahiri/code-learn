# 29. Pacific Atlantic Water Flow

Platform: LeetCode  
Collection: Blind 75  
Implementation language: Java

## Complete Problem Statement

Given an elevation matrix, return coordinates from which water can reach both the Pacific edges and Atlantic edges by moving to cells of equal or lower height.

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

### Brute-Force Java — O(m×n×(m×n)) time — BFS from each cell

```java
// Run BFS/DFS from each cell; check if both oceans reachable
// Omitted for brevity — same logic as optimal but inverted direction
```

## Approach 2: Optimized

The optimized solution removes repeated work while preserving the same correctness condition.

### Optimized Java (Reverse BFS from oceans) — O(m×n) time, O(m×n) space

```java
public List<List<Integer>> pacificAtlantic(int[][] heights) {
    int m = heights.length, n = heights[0].length;
    boolean[][] pac = new boolean[m][n], atl = new boolean[m][n];
    Queue<int[]> pq = new LinkedList<>(), aq = new LinkedList<>();
    for (int i = 0; i < m; i++) { pq.offer(new int[]{i,0}); pac[i][0]=true; aq.offer(new int[]{i,n-1}); atl[i][n-1]=true; }
    for (int j = 0; j < n; j++) { pq.offer(new int[]{0,j}); pac[0][j]=true; aq.offer(new int[]{m-1,j}); atl[m-1][j]=true; }
    bfs(heights, pq, pac); bfs(heights, aq, atl);
    List<List<Integer>> result = new ArrayList<>();
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            if (pac[i][j] && atl[i][j]) result.add(Arrays.asList(i, j));
    return result;
}
private void bfs(int[][] h, Queue<int[]> q, boolean[][] visited) {
    int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
    while (!q.isEmpty()) {
        int[] cell = q.poll(); int r = cell[0], c = cell[1];
        for (int[] d : dirs) {
            int nr = r+d[0], nc = c+d[1];
            if (nr<0||nr>=h.length||nc<0||nc>=h[0].length||visited[nr][nc]||h[nr][nc]<h[r][c]) continue;
            visited[nr][nc] = true; q.offer(new int[]{nr,nc});
        }
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
copying it. The source heading for this implementation is “Pacific Atlantic Water Flow”.
