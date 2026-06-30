# 020. Same Tree

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, DFS

## Problem Statement

Given the roots of two binary trees `p` and `q`, return `true` if both trees are the same.

Two trees are the same when they have the same structure and the same node values.

## Constraints

- The number of nodes in both trees is between `0` and `100`.
- `-10^4 <= Node.val <= 10^4`

## Example

Input:

```text
p = [1, 2, 3]
q = [1, 2, 3]
```

Output:

```text
true
```

## Brute Force Approach

Convert both trees into traversal strings including null markers, then compare the strings.

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
    public boolean isSameTree(TreeNode p, TreeNode q) {
        return serialize(p).equals(serialize(q));
    }

    private String serialize(TreeNode node) {
        if (node == null) {
            return "#";
        }

        return node.val + "," + serialize(node.left) + "," + serialize(node.right);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Compare nodes directly using recursion.

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
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }

        if (p == null || q == null) {
            return false;
        }

        if (p.val != q.val) {
            return false;
        }

        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(h)`, where `h` is tree height.

