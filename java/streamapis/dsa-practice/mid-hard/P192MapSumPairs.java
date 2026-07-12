import java.util.*;

/**
 * P192. Map Sum Pairs. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the full input from the method parameters,
 * choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P192MapSumPairs {

    private P192MapSumPairs() {
    }

    private final Map<String, Integer> values = new HashMap<>();

    public void insert(String key, int val) {
        values.put(key, val);
    }

    public int sum(String prefix) {
        int ans = 0;
        for (var e : values.entrySet())
            if (e.getKey().startsWith(prefix))
                ans += e.getValue();
        return ans;
    }
}
