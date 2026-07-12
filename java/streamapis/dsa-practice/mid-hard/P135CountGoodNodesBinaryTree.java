import java.util.*;

/**
 * P135. Count Good Nodes In Binary Tree. This is a mid-to-hard Java DSA coding
 * problem commonly seen
 * in service based company technical rounds. Implement the required method
 * using an efficient
 * algorithm, not brute force where a better standard approach exists. The
 * solution should handle
 * boundary cases, duplicate values, disconnected states, and large inputs
 * according to the method
 * signature. Return the final computed value or data structure exactly as the
 * platform-style method
 * expects.
 */
public final class P135CountGoodNodesBinaryTree {

    private P135CountGoodNodesBinaryTree() {
    }

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public int goodNodes(TreeNode root) {
        return dfs(root, Integer.MIN_VALUE);
    }

    private int dfs(TreeNode node, int max) {
        if (node == null)
            return 0;
        int good = node.val >= max ? 1 : 0;
        max = Math.max(max, node.val);
        return good + dfs(node.left, max) + dfs(node.right, max);
    }
}
