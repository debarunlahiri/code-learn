import java.util.*;

/**
 * P181. Find Critical Connections. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P181FindCriticalConnections {

    private P181FindCriticalConnections() {
    }

    private int time;

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++)
            g.add(new ArrayList<>());
        for (List<Integer> e : connections) {
            g.get(e.get(0)).add(e.get(1));
            g.get(e.get(1)).add(e.get(0));
        }
        int[] disc = new int[n], low = new int[n];
        List<List<Integer>> ans = new ArrayList<>();
        dfs(0, -1, g, disc, low, ans);
        return ans;
    }

    private void dfs(int u, int p, List<List<Integer>> g, int[] disc, int[] low, List<List<Integer>> ans) {
        disc[u] = low[u] = ++time;
        for (int v : g.get(u)) {
            if (v == p)
                continue;
            if (disc[v] == 0) {
                dfs(v, u, g, disc, low, ans);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > disc[u])
                    ans.add(List.of(u, v));
            } else
                low[u] = Math.min(low[u], disc[v]);
        }
    }
}
