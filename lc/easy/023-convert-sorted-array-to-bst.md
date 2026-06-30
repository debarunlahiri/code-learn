# 023. Convert Sorted Array to Binary Search Tree

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Search Tree, Divide and Conquer

## Problem Statement

Given a sorted integer array `nums`, convert it into a height-balanced binary search tree.

A height-balanced tree is a tree where the depth of the two subtrees of every node does not differ by more than one.

## Constraints

- `1 <= nums.length <= 10^4`
- `-10^4 <= nums[i] <= 10^4`
- `nums` is sorted in strictly increasing order.

## Example

Input:

```text
nums = [-10, -3, 0, 5, 9]
```

Output:

```text
[0, -3, 9, -10, null, 5]
```

## Brute Force Approach

Insert values one by one into a BST. This creates a valid BST, but it can become unbalanced.

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
    public TreeNode sortedArrayToBST(int[] nums) {
        TreeNode root = null;

        for (int num : nums) {
            root = insert(root, num);
        }

        return root;
    }

    private TreeNode insert(TreeNode node, int value) {
        if (node == null) {
            return new TreeNode(value);
        }

        if (value < node.val) {
            node.left = insert(node.left, value);
        } else {
            node.right = insert(node.right, value);
        }

        return node;
    }
}
```

Complexity:

- Time: `O(n^2)` in the worst case
- Space: `O(n)`

## Best Approach

Pick the middle value as the root. Recursively build the left half and right half.

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
    public TreeNode sortedArrayToBST(int[] nums) {
        return build(nums, 0, nums.length - 1);
    }

    private TreeNode build(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;
        TreeNode root = new TreeNode(nums[mid]);

        root.left = build(nums, left, mid - 1);
        root.right = build(nums, mid + 1, right);

        return root;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(log n)` for a balanced recursion stack.

