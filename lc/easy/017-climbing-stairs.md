# 017. Climbing Stairs

Platform: LeetCode  
Difficulty: Easy  
Topic: Dynamic Programming

## Problem Statement

You are climbing a staircase with `n` steps. Each time, you can climb either `1` step or `2` steps.

Return the number of distinct ways to reach the top.

## Constraints

- `1 <= n <= 45`

## Example

Input:

```text
n = 3
```

Output:

```text
3
```

Explanation: The ways are `1 + 1 + 1`, `1 + 2`, and `2 + 1`.

## Brute Force Approach

Use recursion. From each step, try moving one step or two steps.

```java
class Solution {
    public int climbStairs(int n) {
        return countWays(0, n);
    }

    private int countWays(int currentStep, int n) {
        if (currentStep == n) {
            return 1;
        }

        if (currentStep > n) {
            return 0;
        }

        return countWays(currentStep + 1, n) + countWays(currentStep + 2, n);
    }
}
```

Complexity:

- Time: `O(2^n)`
- Space: `O(n)`

## Best Approach

The answer follows the Fibonacci pattern. Keep only the previous two results.

```java
class Solution {
    public int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }

        int oneStepBefore = 2;
        int twoStepsBefore = 1;

        for (int step = 3; step <= n; step++) {
            int current = oneStepBefore + twoStepsBefore;
            twoStepsBefore = oneStepBefore;
            oneStepBefore = current;
        }

        return oneStepBefore;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

