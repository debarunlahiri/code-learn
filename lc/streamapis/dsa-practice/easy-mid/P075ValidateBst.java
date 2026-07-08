import java.util.*;

/**
 * P075. Validate Bst. This is a easy-to-mid Java DSA coding problem commonly
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
public final class P075ValidateBst {

    private P075ValidateBst() {
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
    this.val = val;
        }
    }

    public boolean isValidBST(TreeNode root) {
        return valid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean valid(TreeNode node, long low, long high) {
        if (node == null)
    return true;
        return node.val > low && node.val < high && valid(node.left, low, node.val)
        && valid(node.right, node.val, high);
    }

}
