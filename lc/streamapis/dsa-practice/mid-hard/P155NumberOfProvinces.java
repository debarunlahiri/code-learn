import java.util.*;

/**
 * P155. Number Of Provinces. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the input represented by the method
 * parameters, apply the
 * standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P155NumberOfProvinces {

    private P155NumberOfProvinces() {
    }

    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] seen = new boolean[n];
        int count = 0;
        for (int i = 0; i < n; i++)
            if (!seen[i]) {
                count++;
                dfs(isConnected, seen, i);
            }
        return count;
    }

    private void dfs(int[][] g, boolean[] seen, int u) {
        seen[u] = true;
        for (int v = 0; v < g.length; v++)
            if (g[u][v] == 1 && !seen[v])
                dfs(g, seen, v);
    }
}
