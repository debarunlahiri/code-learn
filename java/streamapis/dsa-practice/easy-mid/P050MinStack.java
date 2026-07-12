import java.util.*;

/**
 * P050. Min Stack. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service based
 * company coding rounds. Given the input described by the method signature,
 * implement the required
 * operation efficiently and return the expected result. Handle normal edge
 * cases such as empty
 * collections, duplicate values, boundary indexes, and null child pointers when
 * the data structure
 * allows them. Prefer the standard optimal approach used in coding rounds, and
 * keep the implementation
 * readable for revision.
 */
public final class P050MinStack {

    private P050MinStack() {
    }

    class MinStack {
        private final Deque<Integer> values = new ArrayDeque<>();
        private final Deque<Integer> mins = new ArrayDeque<>();

        public void push(int val) {
    values.push(val);
    mins.push(mins.isEmpty() ? val : Math.min(val, mins.peek()));
        }

        public void pop() {
    values.pop();
    mins.pop();
        }

        public int top() {
    return values.peek();
        }

        public int getMin() {
    return mins.peek();
        }
    }

}
