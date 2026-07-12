import java.util.*;

/**
 * P139. Cheapest Flights Within K Stops. This is a mid-to-hard Java DSA coding
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
public final class P139CheapestFlightsWithinKStops {

    private P139CheapestFlightsWithinKStops() {
    }

    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        int inf = 1_000_000_000;
        int[] cost = new int[n];
        Arrays.fill(cost, inf);
        cost[src] = 0;
        for (int i = 0; i <= k; i++) {
            int[] next = cost.clone();
            for (int[] f : flights)
                if (cost[f[0]] != inf)
                    next[f[1]] = Math.min(next[f[1]], cost[f[0]] + f[2]);
            cost = next;
        }
        return cost[dst] == inf ? -1 : cost[dst];
    }
}
