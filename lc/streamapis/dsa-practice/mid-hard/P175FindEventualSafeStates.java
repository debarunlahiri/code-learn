import java.util.*;

/**
 * P175. Find Eventual Safe States. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P175FindEventualSafeStates {

    private P175FindEventualSafeStates() {
    }

    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n];
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (safe(graph, color, i))
                ans.add(i);
        return ans;
    }

    private boolean safe(int[][] g, int[] color, int u) {
        if (color[u] != 0)
            return color[u] == 2;
        color[u] = 1;
        for (int v : g[u])
            if (!safe(g, color, v))
                return false;
        color[u] = 2;
        return true;
    }
}
