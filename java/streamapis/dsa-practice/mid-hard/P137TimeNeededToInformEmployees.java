import java.util.*;

/**
 * P137. Time Needed To Inform Employees. This is a mid-to-hard Java DSA coding
 * problem commonly seen
 * in service based company technical rounds. Implement the required method
 * using an efficient
 * algorithm, not brute force where a better standard approach exists. The
 * solution should handle
 * boundary cases, duplicate values, disconnected states, and large inputs
 * according to the method
 * signature. Return the final computed value or data structure exactly as the
 * platform-style method
 * expects.
 */
public final class P137TimeNeededToInformEmployees {

    private P137TimeNeededToInformEmployees() {
    }

    public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());
        for (int i = 0; i < n; i++)
            if (manager[i] != -1)
                graph.get(manager[i]).add(i);
        return dfs(headID, graph, informTime);
    }

    private int dfs(int u, List<List<Integer>> graph, int[] time) {
        int best = 0;
        for (int v : graph.get(u))
            best = Math.max(best, dfs(v, graph, time));
        return time[u] + best;
    }
}
