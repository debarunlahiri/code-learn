import java.util.*;

/**
 * P097. Task Scheduler. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service
 * based company coding rounds. Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P097TaskScheduler {

    private P097TaskScheduler() {
    }

    public int leastInterval(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks)
    count[task - 'A']++;
        Arrays.sort(count);
        int max = count[25] - 1, idle = max * n;
        for (int i = 24; i >= 0; i--)
    idle -= Math.min(max, count[i]);
        return idle > 0 ? tasks.length + idle : tasks.length;
    }

}
