import java.util.*;

/**
 * P133. Binary Tree Maximum Path Sum. This is a mid-to-hard Java DSA coding
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
public final class P133MaximumPathSumBinaryTree {

    private P133MaximumPathSumBinaryTree() {
    }

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    private int best;

    public int maxPathSum(TreeNode root) {
        best = Integer.MIN_VALUE;
        gain(root);
        return best;
    }

    private int gain(TreeNode node) {
        if (node == null)
            return 0;
        int l = Math.max(0, gain(node.left)), r = Math.max(0, gain(node.right));
        best = Math.max(best, node.val + l + r);
        return node.val + Math.max(l, r);
    }
}
