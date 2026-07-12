import java.util.*;

/**
 * P122. Online Stock Span. This is a mid-to-hard Java DSA coding problem
 * commonly seen in service
 * based company technical rounds. Implement the required method using an
 * efficient algorithm, not
 * brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P122OnlineStockSpan {

    private P122OnlineStockSpan() {
    }

    private final Deque<int[]> stack = new ArrayDeque<>();

    public int next(int price) {
        int span = 1;
        while (!stack.isEmpty() && stack.peek()[0] <= price)
            span += stack.pop()[1];
        stack.push(new int[] { price, span });
        return span;
    }
}
