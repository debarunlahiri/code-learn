import java.util.*;

/**
 * P073. Symmetric Tree.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P073SymmetricTree {

    private P073SymmetricTree() {
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
    this.val = val;
        }
    }

    public boolean isSymmetric(TreeNode root) {
        return root == null || mirror(root.left, root.right);
    }

    private boolean mirror(TreeNode a, TreeNode b) {
        if (a == null || b == null)
    return a == b;
        return a.val == b.val && mirror(a.left, b.right) && mirror(a.right, b.left);
    }

}
