# 060. Diameter of Binary Tree

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, DFS

## Problem Statement

Given the root of a binary tree, return the length of its diameter.

The diameter is the length of the longest path between any two nodes in the tree. The path may or may not pass through the root.

## Constraints

- The number of nodes is between `1` and `10^4`.
- `-100 <= Node.val <= 100`

## Example

Input:

```text
root = [1, 2, 3, 4, 5]
```

Output:

```text
3
```

## Brute Force Approach

For every node, calculate the height of the left and right subtrees and update the best diameter.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int throughRoot = height(root.left) + height(root.right);
        int leftDiameter = diameterOfBinaryTree(root.left);
        int rightDiameter = diameterOfBinaryTree(root.right);

        return Math.max(throughRoot, Math.max(leftDiameter, rightDiameter));
    }

    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }

        return 1 + Math.max(height(node.left), height(node.right));
    }
}
```

Complexity:

- Time: `O(n^2)` in the worst case
- Space: `O(h)`

## Best Approach

Calculate height once for every node and update the diameter during the same DFS.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    private int bestDiameter = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        height(root);
        return bestDiameter;
    }

    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = height(node.left);
        int rightHeight = height(node.right);

        bestDiameter = Math.max(bestDiameter, leftHeight + rightHeight);

        return 1 + Math.max(leftHeight, rightHeight);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(h)`

