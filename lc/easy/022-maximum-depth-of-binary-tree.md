# 022. Maximum Depth of Binary Tree

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, DFS, BFS

## Problem Statement

Given the root of a binary tree, return its maximum depth.

The maximum depth is the number of nodes on the longest path from the root to a leaf.

## Constraints

- The number of nodes is between `0` and `10^4`.
- `-100 <= Node.val <= 100`

## Example

Input:

```text
root = [3, 9, 20, null, null, 15, 7]
```

Output:

```text
3
```

## Brute Force Approach

Use level order traversal and count how many levels exist.

```java
import java.util.LinkedList;
import java.util.Queue;

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
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            depth++;

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();

                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
        }

        return depth;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Use recursion. The depth of a node is `1 + max(left depth, right depth)`.

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
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);

        return 1 + Math.max(leftDepth, rightDepth);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(h)`

