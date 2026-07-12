import java.util.*;

/**
 * P166. Task Scheduler With Cooling. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P166TaskSchedulerWithCooling {

    private P166TaskSchedulerWithCooling() {
    }

    public int leastInterval(char[] tasks, int n) {
        int[] count = new int[26];
        for (char t : tasks)
            count[t - 'A']++;
        Arrays.sort(count);
        int maxIdleSlots = (count[25] - 1) * n;
        for (int i = 24; i >= 0; i--)
            maxIdleSlots -= Math.min(count[25] - 1, count[i]);
        return maxIdleSlots > 0 ? tasks.length + maxIdleSlots : tasks.length;
    }
}
