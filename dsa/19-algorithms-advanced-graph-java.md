# Algorithms: Advanced Graph (Hard)

## 1. Floyd-Warshall (All pairs shortest path)
```java
public class FloydWarshall {
    static final int INF = (int) 1e9;

    static int[][] shortestPaths(int[][] dist) {
        int n = dist.length;
        for (int via = 0; via < n; via++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][via] < INF && dist[via][j] < INF) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][via] + dist[via][j]);
                    }
                }
            }
        }
        return dist;
    }
}
```

## 2. Kosaraju (Strongly Connected Components)
```java
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class KosarajuSCC {
    static int sccCount(int n, List<List<Integer>> g) {
        boolean[] vis = new boolean[n];
        Stack<Integer> order = new Stack<>();

        for (int i = 0; i < n; i++) {
            if (!vis[i]) dfs1(i, g, vis, order);
        }

        List<List<Integer>> rg = new ArrayList<>();
        for (int i = 0; i < n; i++) rg.add(new ArrayList<>());
        for (int u = 0; u < n; u++) {
            for (int v : g.get(u)) rg.get(v).add(u);
        }

        for (int i = 0; i < n; i++) vis[i] = false;
        int count = 0;
        while (!order.isEmpty()) {
            int u = order.pop();
            if (!vis[u]) {
                dfs2(u, rg, vis);
                count++;
            }
        }
        return count;
    }

    static void dfs1(int u, List<List<Integer>> g, boolean[] vis, Stack<Integer> st) {
        vis[u] = true;
        for (int v : g.get(u)) if (!vis[v]) dfs1(v, g, vis, st);
        st.push(u);
    }

    static void dfs2(int u, List<List<Integer>> rg, boolean[] vis) {
        vis[u] = true;
        for (int v : rg.get(u)) if (!vis[v]) dfs2(v, rg, vis);
    }
}
```

## 3. Tarjan Bridges
```java
import java.util.ArrayList;
import java.util.List;

public class TarjanBridges {
    static int timer;
    static List<int[]> bridges;

    static List<int[]> findBridges(int n, List<List<Integer>> g) {
        int[] tin = new int[n];
        int[] low = new int[n];
        boolean[] vis = new boolean[n];
        timer = 1;
        bridges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!vis[i]) dfs(i, -1, g, vis, tin, low);
        }
        return bridges;
    }

    static void dfs(int u, int p, List<List<Integer>> g, boolean[] vis, int[] tin, int[] low) {
        vis[u] = true;
        tin[u] = low[u] = timer++;

        for (int v : g.get(u)) {
            if (v == p) continue;
            if (vis[v]) {
                low[u] = Math.min(low[u], tin[v]);
            } else {
                dfs(v, u, g, vis, tin, low);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > tin[u]) bridges.add(new int[]{u, v});
            }
        }
    }
}
```

## 4. 0-1 BFS
```java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class ZeroOneBFS {
    static class Edge {
        int to, wt;
        Edge(int to, int wt) { this.to = to; this.wt = wt; }
    }

    static int[] shortestPath(int n, List<List<Edge>> g, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        Deque<Integer> dq = new ArrayDeque<>();
        dq.addFirst(src);

        while (!dq.isEmpty()) {
            int u = dq.pollFirst();
            for (Edge e : g.get(u)) {
                if (dist[u] + e.wt < dist[e.to]) {
                    dist[e.to] = dist[u] + e.wt;
                    if (e.wt == 0) dq.addFirst(e.to);
                    else dq.addLast(e.to);
                }
            }
        }
        return dist;
    }
}
```

