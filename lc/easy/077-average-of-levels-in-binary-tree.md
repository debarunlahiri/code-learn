# 077. Average of Levels in Binary Tree

Platform: LeetCode  
Difficulty: Easy  
Topic: Binary Tree, BFS

## Problem Statement

Given the root of a binary tree, return the average value of nodes on each level.

## Constraints

- The number of nodes is between `1` and `10^4`.
- `-2^31 <= Node.val <= 2^31 - 1`

## Example

Input:

```text
root = [3, 9, 20, null, null, 15, 7]
```

Output:

```text
[3.0, 14.5, 11.0]
```

## Brute Force Approach

Use BFS and store all values at each level, then calculate the average.

```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> values = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                values.add(node.val);

                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }

            long sum = 0;
            for (int value : values) {
                sum += value;
            }

            result.add(sum * 1.0 / values.size());
        }

        return result;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Use BFS but keep only the running sum and level size.

```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

class Solution {
    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> averages = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            long sum = 0;

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                sum += node.val;

                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }

            averages.add(sum * 1.0 / levelSize);
        }

        return averages;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

