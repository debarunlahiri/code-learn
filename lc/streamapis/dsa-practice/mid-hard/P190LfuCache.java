import java.util.*;

/**
 * P190. LFU Cache. This is a mid-to-hard Java DSA coding problem commonly seen
 * in service based
 * company technical rounds. Read the full input from the method parameters,
 * choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P190LfuCache {

    private final int capacity;
    private int time;
    private final Map<Integer, int[]> map = new HashMap<>();

    public P190LfuCache(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        if (!map.containsKey(key))
            return -1;
        int[] a = map.get(key);
        a[1]++;
        a[2] = ++time;
        return a[0];
    }

    public void put(int key, int value) {
        if (capacity == 0)
            return;
        if (map.containsKey(key)) {
            map.get(key)[0] = value;
            get(key);
            return;
        }
        if (map.size() == capacity) {
            int victim = -1;
            for (var e : map.entrySet())
                if (victim == -1 || e.getValue()[1] < map.get(victim)[1]
                        || e.getValue()[1] == map.get(victim)[1] && e.getValue()[2] < map.get(victim)[2])
                    victim = e.getKey();
            map.remove(victim);
        }
        map.put(key, new int[] { value, 1, ++time });
    }
}
