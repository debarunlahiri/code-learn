import java.util.*;

/**
 * P080. Path Sum. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service based
 * company coding rounds. Given the input described by the method signature,
 * implement the required
 * operation efficiently and return the expected result. Handle normal edge
 * cases such as empty
 * collections, duplicate values, boundary indexes, and null child pointers when
 * the data structure
 * allows them. Prefer the standard optimal approach used in coding rounds, and
 * keep the implementation
 * readable for revision.
 */
public final class P080PathSum {

    private P080PathSum() {
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
    this.val = val;
        }
    }

    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null)
    return false;
        if (root.left == null && root.right == null)
    return targetSum == root.val;
        return hasPathSum(root.left, targetSum - root.val) || hasPathSum(root.right, targetSum - root.val);
    }

}
