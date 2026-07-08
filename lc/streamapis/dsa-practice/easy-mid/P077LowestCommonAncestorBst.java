import java.util.*;

/**
 * P077. Lowest Common Ancestor Bst. This is a easy-to-mid Java DSA coding
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
public final class P077LowestCommonAncestorBst {

    private P077LowestCommonAncestorBst() {
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
    this.val = val;
        }
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
    if (p.val < root.val && q.val < root.val)
        root = root.left;
    else if (p.val > root.val && q.val > root.val)
        root = root.right;
    else
        return root;
        }
        return null;
    }

}
