# Algorithms: Graph (Easy to Hard)

## 1. Topological Sort (Kahn's BFS)
```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TopologicalSortKahn {
    static List<Integer> topoSort(int n, List<List<Integer>> graph) {
        int[] indegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : graph.get(u)) indegree[v]++;
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) q.offer(i);
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : graph.get(u)) {
                indegree[v]--;
                if (indegree[v] == 0) q.offer(v);
            }
        }
        return order;
    }
}
```

## 2. Dijkstra (Shortest path in weighted graph)
```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstraShortestPath {
    static class Edge {
        int to, wt;
        Edge(int to, int wt) { this.to = to; this.wt = wt; }
    }

    static int[] dijkstra(int n, List<List<Edge>> graph, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{src, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int node = cur[0];
            int d = cur[1];
            if (d != dist[node]) continue;

            for (Edge e : graph.get(node)) {
                if (dist[node] + e.wt < dist[e.to]) {
                    dist[e.to] = dist[node] + e.wt;
                    pq.offer(new int[]{e.to, dist[e.to]});
                }
            }
        }
        return dist;
    }
}
```

## 3. Union-Find Cycle Detection
```java
public class GraphCycleDSU {
    static class DSU {
        int[] p, rank;
        DSU(int n) {
            p = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) p[i] = i;
        }
        int find(int x) {
            if (p[x] != x) p[x] = find(p[x]);
            return p[x];
        }
        boolean union(int a, int b) {
            int pa = find(a), pb = find(b);
            if (pa == pb) return false;
            if (rank[pa] < rank[pb]) p[pa] = pb;
            else if (rank[pb] < rank[pa]) p[pb] = pa;
            else { p[pb] = pa; rank[pa]++; }
            return true;
        }
    }
}
```

## 4. Minimum Spanning Tree: Kruskal
```java
import java.util.Arrays;

public class KruskalMST {
    static int mstCost(int n, int[][] edges) {
        Arrays.sort(edges, (a, b) -> a[2] - b[2]);
        DSU dsu = new DSU(n);
        int cost = 0;
        int used = 0;

        for (int[] e : edges) {
            if (dsu.union(e[0], e[1])) {
                cost += e[2];
                used++;
                if (used == n - 1) break;
            }
        }
        return cost;
    }

    static class DSU {
        int[] p, r;
        DSU(int n) {
            p = new int[n];
            r = new int[n];
            for (int i = 0; i < n; i++) p[i] = i;
        }
        int find(int x) {
            if (p[x] != x) p[x] = find(p[x]);
            return p[x];
        }
        boolean union(int a, int b) {
            int pa = find(a), pb = find(b);
            if (pa == pb) return false;
            if (r[pa] < r[pb]) p[pa] = pb;
            else if (r[pb] < r[pa]) p[pb] = pa;
            else { p[pb] = pa; r[pa]++; }
            return true;
        }
    }
}
```

## 5. Bellman-Ford (handles negative weights)
```java
import java.util.Arrays;

public class BellmanFord {
    static int[] shortestPath(int n, int[][] edges, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, (int)1e9);
        dist[src] = 0;

        for (int i = 1; i < n; i++) {
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != (int)1e9 && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                }
            }
        }
        return dist;
    }
}
```

