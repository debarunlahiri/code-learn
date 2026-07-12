import java.util.*;

/**
 * P076. Kth Smallest In Bst. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P076KthSmallestInBst {

    private P076KthSmallestInBst() {
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
    this.val = val;
        }
    }

    public int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        while (true) {
    while (root != null) {
        stack.push(root);
        root = root.left;
    }
    root = stack.pop();
    if (--k == 0)
        return root.val;
    root = root.right;
        }
    }

}
