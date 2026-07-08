import java.util.*;

/**
 * P140. Min Cost To Connect Points. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Implement the required method using
 * an efficient algorithm,
 * not brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P140MinCostConnectPoints {

    private P140MinCostConnectPoints() {
    }

    public int minCostConnectPoints(int[][] points) {
        int n = points.length, ans = 0, used = 0;
        boolean[] seen = new boolean[n];
        int[] min = new int[n];
        Arrays.fill(min, Integer.MAX_VALUE);
        min[0] = 0;
        while (used < n) {
            int u = -1;
            for (int i = 0; i < n; i++)
                if (!seen[i] && (u == -1 || min[i] < min[u]))
                    u = i;
            seen[u] = true;
            ans += min[u];
            used++;
            for (int v = 0; v < n; v++)
                if (!seen[v])
                    min[v] = Math.min(min[v],
                            Math.abs(points[u][0] - points[v][0]) + Math.abs(points[u][1] - points[v][1]));
        }
        return ans;
    }
}
