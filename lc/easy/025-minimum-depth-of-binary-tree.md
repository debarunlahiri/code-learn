# 025. Minimum Depth of Binary Tree

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, BFS, DFS

## Problem Statement

Given the root of a binary tree, return its minimum depth.

The minimum depth is the number of nodes on the shortest path from the root to a leaf node.

## Constraints

- The number of nodes is between `0` and `10^5`.
- `-1000 <= Node.val <= 1000`

## Example

Input:

```text
root = [3, 9, 20, null, null, 15, 7]
```

Output:

```text
2
```

## Brute Force Approach

Use recursion and carefully handle nodes that have only one child.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        if (root.left == null && root.right == null) {
            return 1;
        }

        if (root.left == null) {
            return 1 + minDepth(root.right);
        }

        if (root.right == null) {
            return 1 + minDepth(root.left);
        }

        return 1 + Math.min(minDepth(root.left), minDepth(root.right));
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(h)`

## Best Approach

Use BFS. The first leaf reached gives the minimum depth.

```java
import java.util.LinkedList;
import java.util.Queue;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int depth = 1;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();

                if (node.left == null && node.right == null) {
                    return depth;
                }

                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }

            depth++;
        }

        return depth;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

