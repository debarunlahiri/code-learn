import java.util.*;

/**
 * P178. Course Schedule II. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P178CourseScheduleIi {

    private P178CourseScheduleIi() {
    }

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < numCourses; i++)
            g.add(new ArrayList<>());
        int[] in = new int[numCourses];
        for (int[] p : prerequisites) {
            g.get(p[1]).add(p[0]);
            in[p[0]]++;
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++)
            if (in[i] == 0)
                q.offer(i);
        int[] ans = new int[numCourses];
        int idx = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            ans[idx++] = u;
            for (int v : g.get(u))
                if (--in[v] == 0)
                    q.offer(v);
        }
        return idx == numCourses ? ans : new int[0];
    }
}
