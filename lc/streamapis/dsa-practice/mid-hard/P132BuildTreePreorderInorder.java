import java.util.*;

/**
 * P132. Build Tree From Preorder And Inorder. This is a mid-to-hard Java DSA
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
public final class P132BuildTreePreorderInorder {

    private P132BuildTreePreorderInorder() {
    }

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    private int preIndex;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < inorder.length; i++)
            pos.put(inorder[i], i);
        preIndex = 0;
        return build(preorder, 0, inorder.length - 1, pos);
    }

    private TreeNode build(int[] pre, int l, int r, Map<Integer, Integer> pos) {
        if (l > r)
            return null;
        TreeNode root = new TreeNode(pre[preIndex++]);
        int m = pos.get(root.val);
        root.left = build(pre, l, m - 1, pos);
        root.right = build(pre, m + 1, r, pos);
        return root;
    }
}
