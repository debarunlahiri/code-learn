import java.util.*;

/**
 * P065. Course Schedule. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service
 * based company coding rounds. Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P065CourseSchedule {

    private P065CourseSchedule() {
    }

    public boolean canFinish(int n, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
    graph.add(new ArrayList<>());
        int[] indegree = new int[n];
        for (int[] p : prerequisites) {
    graph.get(p[1]).add(p[0]);
    indegree[p[0]]++;
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++)
    if (indegree[i] == 0)
        q.offer(i);
        int done = 0;
        while (!q.isEmpty())
    for (int next : graph.get(q.poll())) {
        done++;
        if (--indegree[next] == 0)
            q.offer(next);
    }
        return done == prerequisites.length;
    }

}
