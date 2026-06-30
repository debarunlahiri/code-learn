# 021. Symmetric Tree

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, DFS, BFS

## Problem Statement

Given the root of a binary tree, return `true` if the tree is a mirror of itself.

## Constraints

- The number of nodes is between `1` and `1000`.
- `-100 <= Node.val <= 100`

## Example

Input:

```text
root = [1, 2, 2, 3, 4, 4, 3]
```

Output:

```text
true
```

## Brute Force Approach

Serialize the left subtree in left-first order and the right subtree in right-first order, then compare both strings.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }
}

class Solution {
    public boolean isSymmetric(TreeNode root) {
        return serializeLeft(root.left).equals(serializeRight(root.right));
    }

    private String serializeLeft(TreeNode node) {
        if (node == null) {
            return "#";
        }

        return node.val + "," + serializeLeft(node.left) + "," + serializeLeft(node.right);
    }

    private String serializeRight(TreeNode node) {
        if (node == null) {
            return "#";
        }

        return node.val + "," + serializeRight(node.right) + "," + serializeRight(node.left);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Compare the left and right subtrees as mirrors.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }
}

class Solution {
    public boolean isSymmetric(TreeNode root) {
        return isMirror(root.left, root.right);
    }

    private boolean isMirror(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        }

        if (left == null || right == null) {
            return false;
        }

        return left.val == right.val
                && isMirror(left.left, right.right)
                && isMirror(left.right, right.left);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(h)`

