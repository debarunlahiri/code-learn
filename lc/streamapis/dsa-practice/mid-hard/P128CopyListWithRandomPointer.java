import java.util.*;

/**
 * P128. Copy List With Random Pointer. This is a mid-to-hard Java DSA coding
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
public final class P128CopyListWithRandomPointer {

    private P128CopyListWithRandomPointer() {
    }

    static class Node {
        int val;
        Node next;
        Node random;

        Node(int val) {
            this.val = val;
        }
    }

    public Node copyRandomList(Node head) {
        Map<Node, Node> map = new HashMap<>();
        for (Node cur = head; cur != null; cur = cur.next)
            map.put(cur, new Node(cur.val));
        for (Node cur = head; cur != null; cur = cur.next) {
            map.get(cur).next = map.get(cur.next);
            map.get(cur).random = map.get(cur.random);
        }
        return map.get(head);
    }
}
