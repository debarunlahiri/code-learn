import java.util.*;

/**
 * P188. My Calendar I. This is a mid-to-hard Java DSA coding problem commonly
 * seen in service based
 * company technical rounds. Read the full input from the method parameters,
 * choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P188MyCalendarI {

    private P188MyCalendarI() {
    }

    private final TreeMap<Integer, Integer> calendar = new TreeMap<>();

    public boolean book(int start, int end) {
        Integer prev = calendar.floorKey(start), next = calendar.ceilingKey(start);
        if (prev != null && calendar.get(prev) > start)
            return false;
        if (next != null && next < end)
            return false;
        calendar.put(start, end);
        return true;
    }
}
