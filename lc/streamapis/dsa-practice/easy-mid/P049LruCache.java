import java.util.*;

/**
 * P049. Lru Cache. This is a easy-to-mid Java DSA coding problem commonly
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
public final class P049LruCache {

    private P049LruCache() {
    }

    static class Node {
        int val;
        List<Node> neighbors;

        Node(int val, List<Node> neighbors) {
    this.val = val;
    this.neighbors = neighbors;
        }
    }

    class LRUCache {
        private final int capacity;
        private final Map<Integer, Node> map = new HashMap<>();
        private final Node head = new Node(0, 0), tail = new Node(0, 0);

        class Node {
    int key, value;
    Node prev, next;

    Node(int k, int v) {
        key = k;
        value = v;
    }
        }

        public LRUCache(int capacity) {
    this.capacity = capacity;
    head.next = tail;
    tail.prev = head;
        }

        public int get(int key) {
    if (!map.containsKey(key))
        return -1;
    Node n = map.get(key);
    remove(n);
    addFirst(n);
    return n.value;
        }

        public void put(int key, int value) {
    if (map.containsKey(key))
        remove(map.get(key));
    Node n = new Node(key, value);
    map.put(key, n);
    addFirst(n);
    if (map.size() > capacity) {
        Node old = tail.prev;
        remove(old);
        map.remove(old.key);
    }
        }

        private void addFirst(Node n) {
    n.next = head.next;
    n.prev = head;
    head.next.prev = n;
    head.next = n;
        }

        private void remove(Node n) {
    n.prev.next = n.next;
    n.next.prev = n.prev;
        }
    }

}
