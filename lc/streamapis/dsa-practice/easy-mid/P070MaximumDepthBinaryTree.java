import java.util.*;

/**
 * P070. Maximum Depth Binary Tree. This is a easy-to-mid Java DSA coding
 * problem commonly practiced
 * for service based company coding rounds. Given the input described by the
 * method signature,
 * implement the required operation efficiently and return the expected result.
 * Handle normal edge
 * cases such as empty collections, duplicate values, boundary indexes, and null
 * child pointers when
 * the data structure allows them. Prefer the standard optimal approach used in
 * coding rounds, and keep
 * the implementation readable for revision.
 */
public final class P070MaximumDepthBinaryTree {

    private P070MaximumDepthBinaryTree() {
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
    this.val = val;
        }
    }

    public int maxDepth(TreeNode root) {
        if (root == null)
    return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

}
