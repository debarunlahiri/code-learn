# Algorithms: Advanced Trees (Medium to Hard)

## 1. Diameter of Binary Tree
```java
public class TreeDiameter {
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static int diameter = 0;

    static int height(TreeNode root) {
        if (root == null) return 0;
        int lh = height(root.left);
        int rh = height(root.right);
        diameter = Math.max(diameter, lh + rh);
        return 1 + Math.max(lh, rh);
    }

    static int diameterOfTree(TreeNode root) {
        diameter = 0;
        height(root);
        return diameter;
    }
}
```

## 2. Binary Tree Maximum Path Sum
```java
public class TreeMaxPathSum {
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static int best;

    static int gain(TreeNode node) {
        if (node == null) return 0;
        int left = Math.max(0, gain(node.left));
        int right = Math.max(0, gain(node.right));
        best = Math.max(best, node.val + left + right);
        return node.val + Math.max(left, right);
    }

    static int maxPathSum(TreeNode root) {
        best = Integer.MIN_VALUE;
        gain(root);
        return best;
    }
}
```

## 3. Validate BST
```java
public class ValidateBST {
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static boolean isValid(TreeNode root, long low, long high) {
        if (root == null) return true;
        if (root.val <= low || root.val >= high) return false;
        return isValid(root.left, low, root.val) && isValid(root.right, root.val, high);
    }
}
```

## 4. Serialize and Deserialize Binary Tree (Preorder)
```java
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class SerializeDeserializeTree {
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        write(root, sb);
        return sb.toString();
    }

    static void write(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("#,");
            return;
        }
        sb.append(node.val).append(",");
        write(node.left, sb);
        write(node.right, sb);
    }

    static TreeNode deserialize(String data) {
        Queue<String> q = new LinkedList<>(Arrays.asList(data.split(",")));
        return read(q);
    }

    static TreeNode read(Queue<String> q) {
        String s = q.poll();
        if (s == null || s.equals("#") || s.isEmpty()) return null;
        TreeNode node = new TreeNode(Integer.parseInt(s));
        node.left = read(q);
        node.right = read(q);
        return node;
    }
}
```

## 5. Lowest Common Ancestor in Binary Tree
```java
public class LCAInBinaryTree {
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static TreeNode lca(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = lca(root.left, p, q);
        TreeNode right = lca(root.right, p, q);
        if (left != null && right != null) return root;
        return left != null ? left : right;
    }
}
```

