import java.util.*;

/**
 * P134. Lowest Common Ancestor Of Binary Tree. This is a mid-to-hard Java DSA
 * coding problem commonly
 * seen in service based company technical rounds. Implement the required method
 * using an efficient
 * algorithm, not brute force where a better standard approach exists. The
 * solution should handle
 * boundary cases, duplicate values, disconnected states, and large inputs
 * according to the method
 * signature. Return the final computed value or data structure exactly as the
 * platform-style method
 * expects.
 */
public final class P134LowestCommonAncestorBinaryTree {

    private P134LowestCommonAncestorBinaryTree() {
    }

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q)
            return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q), right = lowestCommonAncestor(root.right, p, q);
        return left == null ? right : right == null ? left : root;
    }
}
