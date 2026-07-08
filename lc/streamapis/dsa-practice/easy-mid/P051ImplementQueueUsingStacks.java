import java.util.*;

/**
 * P051. Implement Queue Using Stacks. This is a easy-to-mid Java DSA coding
 * problem commonly practiced
 * for service based company coding rounds. Given the input described by the
 * method signature,
 * implement the required operation efficiently and return the expected result.
 * Handle normal edge
 * cases such as empty collections, duplicate values, boundary indexes, and null
 * child pointers when
 * the data structure allows them. Prefer the standard optimal approach used in
 * coding rounds, and keep
 * the implementation readable for revision.
 */
public final class P051ImplementQueueUsingStacks {

    private P051ImplementQueueUsingStacks() {
    }

    class MyQueue {
        private final Deque<Integer> in = new ArrayDeque<>(), out = new ArrayDeque<>();

        public void push(int x) {
    in.push(x);
        }

        public int pop() {
    move();
    return out.pop();
        }

        public int peek() {
    move();
    return out.peek();
        }

        public boolean empty() {
    return in.isEmpty() && out.isEmpty();
        }

        private void move() {
    if (out.isEmpty())
        while (!in.isEmpty())
            out.push(in.pop());
        }
    }

}
