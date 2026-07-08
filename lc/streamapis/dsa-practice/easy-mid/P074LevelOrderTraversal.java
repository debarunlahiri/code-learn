import java.util.*;

/**
 * P074. Level Order Traversal. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P074LevelOrderTraversal {

    private P074LevelOrderTraversal() {
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
    this.val = val;
        }
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null)
    return ans;
        Queue<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
    List<Integer> level = new ArrayList<>();
    for (int size = q.size(); size > 0; size--) {
        TreeNode node = q.poll();
        level.add(node.val);
        if (node.left != null)
            q.offer(node.left);
        if (node.right != null)
            q.offer(node.right);
    }
    ans.add(level);
        }
        return ans;
    }

}
