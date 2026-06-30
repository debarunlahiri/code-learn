# 007. Maximum Subarray

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Dynamic Programming

## Problem Statement

Given an integer array `nums`, find the maximum possible sum of a non-empty contiguous subarray.

## Constraints

- `1 <= nums.length <= 10^5`
- `-10^4 <= nums[i] <= 10^4`

## Example

Input:

```text
nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
```

Output:

```text
6
```

Explanation: The subarray `[4, -1, 2, 1]` has sum `6`.

## Brute Force Approach

Try every possible subarray and calculate its sum.

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int bestSum = Integer.MIN_VALUE;

        for (int start = 0; start < nums.length; start++) {
            int currentSum = 0;

            for (int end = start; end < nums.length; end++) {
                currentSum += nums[end];
                bestSum = Math.max(bestSum, currentSum);
            }
        }

        return bestSum;
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(1)`

## Best Approach

Use Kadane's algorithm. At each index, decide whether to extend the previous subarray or start a new subarray.

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int currentSum = nums[0];
        int bestSum = nums[0];

        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            bestSum = Math.max(bestSum, currentSum);
        }

        return bestSum;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

