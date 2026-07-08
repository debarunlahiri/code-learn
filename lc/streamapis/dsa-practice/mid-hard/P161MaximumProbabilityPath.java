import java.util.*;

/**
 * P161. Maximum Probability Path. This is a mid-to-hard Java DSA coding problem
 * commonly seen in
 * service based company technical rounds. Read the input represented by the
 * method parameters, apply
 * the standard efficient approach for this topic, and return the exact result
 * requested. Handle empty
 * inputs, duplicate values, boundary indexes, and large constraints in a clean
 * Java implementation.
 */
public final class P161MaximumProbabilityPath {

    private P161MaximumProbabilityPath() {
    }

    public double maxProbability(int n, int[][] edges, double[] succProb, int start, int end) {
        List<double[]>[] g = new ArrayList[n];
        for (int i = 0; i < n; i++)
            g[i] = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            g[edges[i][0]].add(new double[] { edges[i][1], succProb[i] });
            g[edges[i][1]].add(new double[] { edges[i][0], succProb[i] });
        }
        double[] best = new double[n];
        best[start] = 1;
        PriorityQueue<double[]> pq = new PriorityQueue<>((a, b) -> Double.compare(b[1], a[1]));
        pq.offer(new double[] { start, 1 });
        while (!pq.isEmpty()) {
            double[] cur = pq.poll();
            int u = (int) cur[0];
            if (u == end)
                return cur[1];
            if (cur[1] < best[u])
                continue;
            for (double[] e : g[u])
                if (cur[1] * e[1] > best[(int) e[0]]) {
                    best[(int) e[0]] = cur[1] * e[1];
                    pq.offer(new double[] { e[0], best[(int) e[0]] });
                }
        }
        return 0;
    }
}
