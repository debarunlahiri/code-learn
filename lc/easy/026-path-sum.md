# 026. Path Sum

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, DFS

## Problem Statement

Given the root of a binary tree and an integer `targetSum`, return `true` if there is a root-to-leaf path whose values add up to `targetSum`.

## Constraints

- The number of nodes is between `0` and `5000`.
- `-1000 <= Node.val <= 1000`
- `-1000 <= targetSum <= 1000`

## Example

Input:

```text
root = [5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1]
targetSum = 22
```

Output:

```text
true
```

## Brute Force Approach

Collect every root-to-leaf sum and check whether any sum equals `targetSum`.

```java
import java.util.ArrayList;
import java.util.List;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public boolean hasPathSum(TreeNode root, int targetSum) {
        List<Integer> sums = new ArrayList<>();
        collectSums(root, 0, sums);

        for (int sum : sums) {
            if (sum == targetSum) {
                return true;
            }
        }

        return false;
    }

    private void collectSums(TreeNode node, int currentSum, List<Integer> sums) {
        if (node == null) {
            return;
        }

        currentSum += node.val;

        if (node.left == null && node.right == null) {
            sums.add(currentSum);
            return;
        }

        collectSums(node.left, currentSum, sums);
        collectSums(node.right, currentSum, sums);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Subtract node values from the target while moving down. At a leaf, the remaining target must equal the leaf value.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }

        if (root.left == null && root.right == null) {
            return root.val == targetSum;
        }

        int remaining = targetSum - root.val;
        return hasPathSum(root.left, remaining) || hasPathSum(root.right, remaining);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(h)`

