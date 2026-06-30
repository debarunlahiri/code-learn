# 078. Two Sum IV - Input is a BST

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Search Tree, Hash Set

## Problem Statement

Given the root of a binary search tree and an integer `k`, return `true` if there are two different nodes whose values sum to `k`.

## Constraints

- The number of nodes is between `1` and `10^4`.
- `-10^4 <= Node.val <= 10^4`
- `-10^5 <= k <= 10^5`

## Example

Input:

```text
root = [5, 3, 6, 2, 4, null, 7], k = 9
```

Output:

```text
true
```

## Brute Force Approach

Store all values in a list, then check every pair.

```java
import java.util.ArrayList;
import java.util.List;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public boolean findTarget(TreeNode root, int k) {
        List<Integer> values = new ArrayList<>();
        inorder(root, values);

        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                if (values.get(i) + values.get(j) == k) {
                    return true;
                }
            }
        }

        return false;
    }

    private void inorder(TreeNode node, List<Integer> values) {
        if (node == null) {
            return;
        }

        inorder(node.left, values);
        values.add(node.val);
        inorder(node.right, values);
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(n)`

## Best Approach

Use DFS with a hash set. For each node, check whether `k - node.val` was already seen.

```java
import java.util.HashSet;
import java.util.Set;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public boolean findTarget(TreeNode root, int k) {
        Set<Integer> seen = new HashSet<>();
        return dfs(root, k, seen);
    }

    private boolean dfs(TreeNode node, int k, Set<Integer> seen) {
        if (node == null) {
            return false;
        }

        int needed = k - node.val;

        if (seen.contains(needed)) {
            return true;
        }

        seen.add(node.val);

        return dfs(node.left, k, seen) || dfs(node.right, k, seen);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

