# 019. Binary Tree Inorder Traversal

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, DFS, Stack

## Problem Statement

Given the root of a binary tree, return the inorder traversal of its node values.

In inorder traversal, visit:

1. Left subtree
2. Current node
3. Right subtree

## Constraints

- The number of nodes is between `0` and `100`.
- `-100 <= Node.val <= 100`

## Example

Input:

```text
root = [1, null, 2, 3]
```

Output:

```text
[1, 3, 2]
```

## Brute Force Approach

Use recursion to visit left, root, and right.

```java
import java.util.ArrayList;
import java.util.List;

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
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }

        inorder(node.left, result);
        result.add(node.val);
        inorder(node.right, result);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)` because of recursion stack and result list.

## Best Approach

Use an explicit stack to avoid recursive calls.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            result.add(current.val);
            current = current.right;
        }

        return result;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

