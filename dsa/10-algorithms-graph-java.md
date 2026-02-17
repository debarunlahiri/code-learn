# Algorithms: Graph (Easy to Hard)

Goal: Understand graph traversal, shortest paths, and fundamental graph algorithms.

---

## 1. Topological Sort (Kahn's BFS)

### What it does
Linear ordering of vertices in a DAG where each directed edge u→v means u comes before v.

### Why it matters
- Course scheduling, task dependencies
- Build systems, makefiles
- Detect cycles in directed graphs

### Intuition
Think of prerequisites for courses. You can only take a course when all its prerequisites are completed. Topological sort gives a valid order to take all courses.

### When to use
- Directed Acyclic Graph (DAG)
- Task scheduling with dependencies
- Build order determination

### Time complexity
- Time: `O(V + E)` where V = vertices, E = edges
- Space: `O(V)`

### Edge cases
- Graph has cycle (no valid topological order)
- Disconnected graph (multiple valid orders)
- Single vertex

### Java code
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
        return order; // If size != n, graph has cycle
    }
}
```

---

## 2. Dijkstra (Shortest path in weighted graph)

### What it does
Find shortest path from a source to all other vertices in a weighted graph with non-negative edges.

### Why it matters
- GPS navigation, network routing
- Flight connections, delivery routes
- Foundation for many pathfinding algorithms

### Intuition
Imagine exploring cities from your starting point. Always visit the closest unvisited city next. Once visited, you know the shortest distance to it.

### When to use
- Weighted graphs with non-negative edge weights
- Single-source shortest path
- Navigation and routing problems

### Time complexity
- Time: `O((V + E) log V)` with binary heap
- Space: `O(V)`

### Edge cases
- Negative edge weights (use Bellman-Ford instead)
- Disconnected graph (infinite distance to unreachable nodes)
- Single source

### Java code
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
            if (d != dist[node]) continue; // Skip outdated entries

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

---

## 3. Union-Find Cycle Detection

### What it does
Detect cycles in undirected graph using Disjoint Set Union (DSU).

### Why it matters
- Kruskal's MST algorithm
- Dynamic connectivity
- Network connectivity checks

### Intuition
Think of separate groups of friends. When two people become friends, you merge their groups. If you try to connect two people already in the same group, you create a cycle.

### When to use
- Cycle detection in undirected graphs
- Dynamic connectivity queries
- Minimum spanning tree (Kruskal)

### Time complexity
- Time: `O(α(V))` per operation (inverse Ackermann, essentially constant)
- Space: `O(V)`

### Edge cases
- Self-loops (immediate cycle)
- Multiple edges between same vertices
- Disconnected components

### Java code
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
            if (p[x] != x) p[x] = find(p[x]); // Path compression
            return p[x];
        }

        boolean union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return false; // Cycle detected

            // Union by rank
            if (rank[px] < rank[py]) p[px] = py;
            else if (rank[px] > rank[py]) p[py] = px;
            else {
                p[py] = px;
                rank[px]++;
            }
            return true;
        }
    }

    static boolean hasCycle(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        for (int[] e : edges) {
            if (!dsu.union(e[0], e[1])) return true;
        }
        return false;
    }
}
```

---

## 4. BFS Traversal

### What it does
Visit all vertices level by level from a starting vertex.

### Why it matters
- Shortest path in unweighted graphs
- Connected components
- Web crawling, social networks

### Intuition
Like ripples in a pond. Start from center, visit all immediate neighbors, then their neighbors, and so on.

### When to use
- Unweighted shortest path
- Level-order traversal
- Connected components

### Time complexity
- Time: `O(V + E)`
- Space: `O(V)`

### Edge cases
- Disconnected graph (run BFS from each unvisited vertex)
- Single vertex
- Empty graph

### Java code
```java
import java.util.*;

public class BFSTraversal {
    static List<Integer> bfs(int start, List<List<Integer>> graph) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.size()];
        Queue<Integer> q = new LinkedList<>();

        visited[start] = true;
        q.offer(start);

        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);

            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    q.offer(v);
                }
            }
        }
        return order;
    }

    // For disconnected graph
    static List<Integer> bfsAll(List<List<Integer>> graph) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.size()];

        for (int i = 0; i < graph.size(); i++) {
            if (!visited[i]) {
                order.addAll(bfs(i, graph));
            }
        }
        return order;
    }
}
```

---

## 5. DFS Traversal

### What it does
Visit all vertices by going as deep as possible before backtracking.

### Why it matters
- Path finding, maze solving
- Topological sort, cycle detection
- Tree traversals (special case of DFS)

### Intuition
Like exploring a maze. Always take the first available path, go as far as possible, then backtrack and try other paths.

### When to use
- Path existence checking
- Connected components
- Topological sort (with stack)

### Time complexity
- Time: `O(V + E)`
- Space: `O(V)` (recursion stack)

### Edge cases
- Very deep graphs (stack overflow - use iterative DFS)
- Disconnected graph
- Cycles (need visited array to avoid infinite loops)

### Java code
```java
import java.util.*;

public class DFSTraversal {
    static void dfs(int u, List<List<Integer>> graph, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        order.add(u);

        for (int v : graph.get(u)) {
            if (!visited[v]) {
                dfs(v, graph, visited, order);
            }
        }
    }

    static List<Integer> dfsAll(List<List<Integer>> graph) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.size()];

        for (int i = 0; i < graph.size(); i++) {
            if (!visited[i]) {
                dfs(i, graph, visited, order);
            }
        }
        return order;
    }

    // Iterative DFS to avoid stack overflow
    static List<Integer> dfsIterative(int start, List<List<Integer>> graph) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.size()];
        Stack<Integer> stack = new Stack<>();

        stack.push(start);
        visited[start] = true;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            order.add(u);

            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    stack.push(v);
                }
            }
        }
        return order;
    }
}
```
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

