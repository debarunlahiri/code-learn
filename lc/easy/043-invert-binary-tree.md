# 043. Invert Binary Tree

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, DFS

## Problem Statement

Given the root of a binary tree, invert the tree by swapping the left and right child of every node.

## Constraints

- The number of nodes is between `0` and `100`.
- `-100 <= Node.val <= 100`

## Example

Input:

```text
root = [4, 2, 7, 1, 3, 6, 9]
```

Output:

```text
[4, 7, 2, 9, 6, 3, 1]
```

## Brute Force Approach

Create a new inverted tree instead of modifying the original tree.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        TreeNode newRoot = new TreeNode(root.val);
        newRoot.left = invertTree(root.right);
        newRoot.right = invertTree(root.left);

        return newRoot;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Swap left and right child in-place for every node.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;

        invertTree(root.left);
        invertTree(root.right);

        return root;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(h)`

