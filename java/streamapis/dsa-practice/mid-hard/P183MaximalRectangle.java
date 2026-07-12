import java.util.*;

/**
 * P183. Maximal Rectangle. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Read the full input from the method
 * parameters, choose the expected
 * optimal data structure or algorithm, handle edge cases such as empty inputs
 * and duplicates, and
 * return the exact platform-style output.
 */
public final class P183MaximalRectangle {

    private P183MaximalRectangle() {
    }

    public int maximalRectangle(char[][] matrix) {
        int[] h = new int[matrix[0].length];
        int best = 0;
        for (char[] row : matrix) {
            for (int c = 0; c < row.length; c++)
                h[c] = row[c] == '1' ? h[c] + 1 : 0;
            best = Math.max(best, largest(h));
        }
        return best;
    }

    private int largest(int[] h) {
        Deque<Integer> st = new ArrayDeque<>();
        int best = 0;
        for (int i = 0; i <= h.length; i++) {
            int cur = i == h.length ? 0 : h[i];
            while (!st.isEmpty() && cur < h[st.peek()]) {
                int height = h[st.pop()];
                int w = st.isEmpty() ? i : i - st.peek() - 1;
                best = Math.max(best, height * w);
            }
            st.push(i);
        }
        return best;
    }
}
