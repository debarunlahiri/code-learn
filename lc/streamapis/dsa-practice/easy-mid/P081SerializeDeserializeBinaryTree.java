import java.util.*;

/**
 * P081. Serialize Deserialize Binary Tree. This is a easy-to-mid Java DSA
 * coding problem commonly
 * practiced for service based company coding rounds. Given the input described
 * by the method
 * signature, implement the required operation efficiently and return the
 * expected result. Handle
 * normal edge cases such as empty collections, duplicate values, boundary
 * indexes, and null child
 * pointers when the data structure allows them. Prefer the standard optimal
 * approach used in coding
 * rounds, and keep the implementation readable for revision.
 */
public final class P081SerializeDeserializeBinaryTree {

    private P081SerializeDeserializeBinaryTree() {
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
    this.val = val;
        }
    }

    class Codec {
        public String serialize(TreeNode root) {
    StringBuilder sb = new StringBuilder();
    preorder(root, sb);
    return sb.toString();
        }

        private void preorder(TreeNode node, StringBuilder sb) {
    if (node == null) {
        sb.append("#,");
        return;
    }
    sb.append(node.val).append(',');
    preorder(node.left, sb);
    preorder(node.right, sb);
        }

        public TreeNode deserialize(String data) {
    Queue<String> q = new ArrayDeque<>(Arrays.asList(data.split(",")));
    return build(q);
        }

        private TreeNode build(Queue<String> q) {
    String s = q.poll();
    if (s.equals("#"))
        return null;
    TreeNode node = new TreeNode(Integer.parseInt(s));
    node.left = build(q);
    node.right = build(q);
    return node;
        }
    }

}
