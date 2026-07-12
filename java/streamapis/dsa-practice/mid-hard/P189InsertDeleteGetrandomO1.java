import java.util.*;

/**
 * P189. Insert Delete GetRandom O1. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P189InsertDeleteGetrandomO1 {

    private P189InsertDeleteGetrandomO1() {
    }

    private final List<Integer> values = new ArrayList<>();
    private final Map<Integer, Integer> index = new HashMap<>();
    private final Random random = new Random();

    public boolean insert(int val) {
        if (index.containsKey(val))
            return false;
        index.put(val, values.size());
        values.add(val);
        return true;
    }

    public boolean remove(int val) {
        Integer i = index.get(val);
        if (i == null)
            return false;
        int last = values.get(values.size() - 1);
        values.set(i, last);
        index.put(last, i);
        values.remove(values.size() - 1);
        index.remove(val);
        return true;
    }

    public int getRandom() {
        return values.get(random.nextInt(values.size()));
    }
}
