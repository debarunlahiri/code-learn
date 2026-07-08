import java.util.*;

/**
 * P199. Russian Doll Envelopes. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P199RussianDollEnvelopes {

    private P199RussianDollEnvelopes() {
    }

    public int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);
        int[] tails = new int[envelopes.length];
        int size = 0;
        for (int[] e : envelopes) {
            int i = Arrays.binarySearch(tails, 0, size, e[1]);
            if (i < 0)
                i = -(i + 1);
            tails[i] = e[1];
            if (i == size)
                size++;
        }
        return size;
    }
}
