import java.util.*;

/**
 * P198. Minimum Number Of Refueling Stops. This is a mid-to-hard Java DSA
 * coding problem commonly seen
 * in service based company technical rounds. Read the full input from the
 * method parameters, choose
 * the expected optimal data structure or algorithm, handle edge cases such as
 * empty inputs and
 * duplicates, and return the exact platform-style output.
 */
public final class P198MinimumNumberOfRefuelingStops {

    private P198MinimumNumberOfRefuelingStops() {
    }

    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        int fuel = startFuel, i = 0, stops = 0;
        while (fuel < target) {
            while (i < stations.length && stations[i][0] <= fuel)
                pq.offer(stations[i++][1]);
            if (pq.isEmpty())
                return -1;
            fuel += pq.poll();
            stops++;
        }
        return stops;
    }
}
