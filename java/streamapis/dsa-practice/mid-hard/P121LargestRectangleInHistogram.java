import java.util.*;

/**
 * P121. Largest Rectangle In Histogram. This is a mid-to-hard Java DSA coding
 * problem commonly seen in
 * service based company technical rounds. Implement the required method using
 * an efficient algorithm,
 * not brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P121LargestRectangleInHistogram {

    private P121LargestRectangleInHistogram() {
    }

    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int best = 0;
        for (int i = 0; i <= heights.length; i++) {
            int h = i == heights.length ? 0 : heights[i];
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                best = Math.max(best, height * width);
            }
            stack.push(i);
        }
        return best;
    }
}
