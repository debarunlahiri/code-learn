import java.util.*;

/**
 * P079. Balanced Binary Tree. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P079BalancedBinaryTree {

    private P079BalancedBinaryTree() {
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
    this.val = val;
        }
    }

    public boolean isBalanced(TreeNode root) {
        return checkHeight(root) != -1;
    }

    private int checkHeight(TreeNode node) {
        if (node == null)
    return 0;
        int left = checkHeight(node.left), right = checkHeight(node.right);
        if (left == -1 || right == -1 || Math.abs(left - right) > 1)
    return -1;
        return 1 + Math.max(left, right);
    }

}
