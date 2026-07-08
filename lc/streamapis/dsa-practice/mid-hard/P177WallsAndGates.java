import java.util.*;

/**
 * P177. Walls And Gates. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the full input from the method parameters,
 * choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P177WallsAndGates {

    private P177WallsAndGates() {
    }

    public void wallsAndGates(int[][] rooms) {
        Queue<int[]> q = new ArrayDeque<>();
        for (int r = 0; r < rooms.length; r++)
            for (int c = 0; c < rooms[0].length; c++)
                if (rooms[r][c] == 0)
                    q.offer(new int[] { r, c });
        int[][] d = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            for (int[] x : d) {
                int nr = cur[0] + x[0], nc = cur[1] + x[1];
                if (nr >= 0 && nc >= 0 && nr < rooms.length && nc < rooms[0].length
                        && rooms[nr][nc] == Integer.MAX_VALUE) {
                    rooms[nr][nc] = rooms[cur[0]][cur[1]] + 1;
                    q.offer(new int[] { nr, nc });
                }
            }
        }
    }
}
