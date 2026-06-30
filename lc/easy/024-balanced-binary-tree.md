# 024. Balanced Binary Tree

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, DFS

## Problem Statement

Given a binary tree, return `true` if it is height-balanced.

A tree is height-balanced when the left and right subtree heights of every node differ by no more than one.

## Constraints

- The number of nodes is between `0` and `5000`.
- `-10^4 <= Node.val <= 10^4`

## Example

Input:

```text
root = [3, 9, 20, null, null, 15, 7]
```

Output:

```text
true
```

## Brute Force Approach

For every node, calculate the height of its left and right subtrees.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }

        int leftHeight = height(root.left);
        int rightHeight = height(root.right);

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return false;
        }

        return isBalanced(root.left) && isBalanced(root.right);
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

Calculate height and balance together. Return `-1` when a subtree is already unbalanced.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public boolean isBalanced(TreeNode root) {
        return checkHeight(root) != -1;
    }

    private int checkHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = checkHeight(node.left);
        if (leftHeight == -1) {
            return -1;
        }

        int rightHeight = checkHeight(node.right);
        if (rightHeight == -1) {
            return -1;
        }

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }

        return 1 + Math.max(leftHeight, rightHeight);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(h)`

