import java.util.*;

/**
 * P052. Implement Stack Using Queues. This is a easy-to-mid Java DSA coding
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
public final class P052ImplementStackUsingQueues {

    private P052ImplementStackUsingQueues() {
    }

    class MyStack {
        private final Queue<Integer> q = new ArrayDeque<>();

        public void push(int x) {
    q.offer(x);
    for (int i = 0; i < q.size() - 1; i++)
        q.offer(q.poll());
        }

        public int pop() {
    return q.poll();
        }

        public int top() {
    return q.peek();
        }

        public boolean empty() {
    return q.isEmpty();
        }
    }

}
