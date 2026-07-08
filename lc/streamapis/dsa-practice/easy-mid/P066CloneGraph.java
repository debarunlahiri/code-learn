import java.util.*;

/**
 * P066. Clone Graph. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service
 * based company coding rounds. Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P066CloneGraph {

    private P066CloneGraph() {
    }

    static class Node {
        int val;
        List<Node> neighbors;

        Node(int val, List<Node> neighbors) {
    this.val = val;
    this.neighbors = neighbors;
        }
    }

    public Node cloneGraph(Node node) {
        return cloneGraph(node, new HashMap<>());
    }

    private Node cloneGraph(Node node, Map<Node, Node> seen) {
        if (node == null)
    return null;
        if (seen.containsKey(node))
    return seen.get(node);
        Node copy = new Node(node.val, new ArrayList<>());
        seen.put(node, copy);
        for (Node next : node.neighbors)
    copy.neighbors.add(cloneGraph(next, seen));
        return copy;
    }

}
