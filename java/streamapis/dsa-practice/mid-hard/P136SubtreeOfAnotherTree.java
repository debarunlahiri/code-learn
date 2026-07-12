import java.util.*;

/**
 * P136. Subtree Of Another Tree. This is a mid-to-hard Java DSA coding problem
 * commonly seen in
 * service based company technical rounds. Implement the required method using
 * an efficient algorithm,
 * not brute force where a better standard approach exists. The solution should
 * handle boundary cases,
 * duplicate values, disconnected states, and large inputs according to the
 * method signature. Return
 * the final computed value or data structure exactly as the platform-style
 * method expects.
 */
public final class P136SubtreeOfAnotherTree {

    private P136SubtreeOfAnotherTree() {
    }

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (root == null)
            return subRoot == null;
        return same(root, subRoot) || isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    private boolean same(TreeNode a, TreeNode b) {
        if (a == null || b == null)
            return a == b;
        return a.val == b.val && same(a.left, b.left) && same(a.right, b.right);
    }
}
