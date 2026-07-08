import java.util.*;

/**
 * P138. Network Delay Time. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Implement the required method using an
 * efficient algorithm, not
 * brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P138NetworkDelayTime {

    private P138NetworkDelayTime() {
    }

    public int networkDelayTime(int[][] times, int n, int k) {
        List<int[]>[] g = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++)
            g[i] = new ArrayList<>();
        for (int[] t : times)
            g[t[0]].add(new int[] { t[1], t[2] });
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[] { k, 0 });
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            if (cur[1] != dist[cur[0]])
                continue;
            for (int[] e : g[cur[0]])
                if (cur[1] + e[1] < dist[e[0]]) {
                    dist[e[0]] = cur[1] + e[1];
                    pq.offer(new int[] { e[0], dist[e[0]] });
                }
        }
        int ans = 0;
        for (int i = 1; i <= n; i++)
            ans = Math.max(ans, dist[i]);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}
